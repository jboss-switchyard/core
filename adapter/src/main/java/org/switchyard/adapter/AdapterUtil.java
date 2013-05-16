/* 
 * JBoss, Home of Professional Open Source 
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @author tags. All rights reserved. 
 * See the copyright.txt in the distribution for a 
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use, 
 * modify, copy, or redistribute it subject to the terms and conditions 
 * of the GNU Lesser General Public License, v. 2.1. 
 * This program is distributed in the hope that it will be useful, but WITHOUT A 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details. 
 * You should have received a copy of the GNU Lesser General Public License, 
 * v.2.1 along with this distribution; if not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 */
package org.switchyard.adapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.switchyard.adapter.config.model.JavaAdapterModel;
import org.switchyard.common.type.Classes;
import org.switchyard.config.model.composite.InterfaceModel;
import org.switchyard.config.model.extensions.adapter.AdapterModel;
import org.switchyard.config.model.switchyard.EsbInterfaceModel;
import org.switchyard.exception.SwitchYardException;
import org.switchyard.extensions.wsdl.WSDLReaderException;
import org.switchyard.extensions.wsdl.WSDLService;
import org.switchyard.metadata.InOnlyOperation;
import org.switchyard.metadata.InOnlyService;
import org.switchyard.metadata.InOutOperation;
import org.switchyard.metadata.InOutService;
import org.switchyard.metadata.ServiceInterface;
import org.switchyard.metadata.ServiceOperation;
import org.switchyard.metadata.java.JavaService;


/**
 * Utility class to create adapters.
 * 
 * @author Christoph Gostner &lt;<a href="mailto:christoph.gostner@objectbay.com">christoph.gostner@objectbay.com</a>&gt; &copy; 2013 Objectbay
 */
public final class AdapterUtil {
    private AdapterUtil() {}

    /**
     * Create a new adapter based on an adapter model.
     * @param adapterModel the adapter's model used to create the adapter
     * @return instance of an adapter based on the data from the adapter's model
     */
    public static Adapter newAdapter(AdapterModel adapterModel) {
        if (adapterModel instanceof JavaAdapterModel) {
            JavaAdapterModel jAdapterModel = (JavaAdapterModel) adapterModel;
            Class<?> clazz = loadClass(jAdapterModel.getClazz());
            if (clazz == null) {
                throw new SwitchYardException("Failed to load Adapter class '" + clazz + "'.");
            }
            return createAdapter(clazz, adapterModel.getInterfaceModel());
        }
        return null;
    }

    private static ServiceInterface createServiceInterface(InterfaceModel intfModel) {
        if (isJavaInterface(intfModel.getType())) {
            String interfaceClass = intfModel.getInterface();
            Class<?> serviceInterfaceType = loadClass(interfaceClass);

            if (serviceInterfaceType == null) {
                throw new SwitchYardException("Failed to load Service interface class '" + interfaceClass + "'.");
            }
            return JavaService.fromClass(serviceInterfaceType);
        } else if (InterfaceModel.WSDL.equals(intfModel.getType())) {
            try {
                return WSDLService.fromWSDL(intfModel.getInterface());
            } catch (WSDLReaderException wsdlre) {
                throw new SwitchYardException(wsdlre);
            }
        } else if (EsbInterfaceModel.ESB.equals(intfModel.getType())) {
            EsbInterfaceModel esbIntf = (EsbInterfaceModel)intfModel;
            validateEsbInterface(esbIntf);
            if (esbIntf.getOutputType() == null) {
                return new InOnlyService(new InOnlyOperation(
                        ServiceInterface.DEFAULT_OPERATION, esbIntf.getInputType()));
            } else {
                return new InOutService(new InOutOperation(
                        ServiceInterface.DEFAULT_OPERATION, 
                        esbIntf.getInputType(), esbIntf.getOutputType(), esbIntf.getFaultType()));
            }
        }
        throw new SwitchYardException("Failed to create Service interface from model '" + intfModel + "'.");
    }
    
    // Checks for invalid input/output/fault combinations on ESB interfaces.
    private static void validateEsbInterface(EsbInterfaceModel esbIntf)  {
        if (esbIntf.getInputType() == null) {
            throw new SwitchYardException("inputType required on ESB interface definition: " + esbIntf);
        }
        
        if (esbIntf.getFaultType() != null && esbIntf.getOutputType() == null) {
            throw new SwitchYardException("faultType must be acommpanied by outputType in ESB interface: " + esbIntf);
        }
    }
    
    private static boolean isJavaInterface(final String type) {
        return InterfaceModel.JAVA.equals(type);
    }

    private static Class<?> loadClass(String clazz) {
        return Classes.forName(clazz);
    }

    private static Method getAdapterMethod(Class<?> clazz) {
        if (!isAdapter(clazz)) {
            throw new SwitchYardException("Invalid Adapter class '" + clazz + "'.");    
        }
        for (Method publicMethod : clazz.getMethods()) {
            if (isAdapterMethod(publicMethod)) {
                return publicMethod;
            }
        }
        throw new SwitchYardException("Failed to locate Adapter method in class '" + clazz + "'.");
    }

    private static boolean isAdapterMethod(Method publicMethod) {
        Class<?> returnType = publicMethod.getReturnType();
        Class<?>[] parameterTypes = publicMethod.getParameterTypes();
        
        if (parameterTypes.length != 2) {
            return false;
        }
        return ServiceOperation.class.equals(returnType) && String.class.equals(parameterTypes[0]) && ServiceInterface.class.equals(parameterTypes[1]);
    }
    
    static boolean isAdapter(Class<?> clazz) {
        if (clazz.isInterface()) {
            return false;
        }
        if (clazz.isAnnotation()) {
            return false;
        }
        if (Modifier.isAbstract(clazz.getModifiers())) {
            return false;
        }
        try {
            // Must have a default constructor...
            clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            return false;
        }
        if (org.switchyard.adapter.Adapter.class.isAssignableFrom(clazz)) {
            return true;
        }

        Method[] publicMethods = clazz.getMethods();
        for (Method publicMethod : publicMethods) {
            if (publicMethod.isAnnotationPresent(org.switchyard.annotations.Adapter.class)) {
                return true;
            }
        }
        return false;
    }

    private static Adapter createAdapter(Class<?> clazz, InterfaceModel interfaceModel) {
        ServiceInterface serviceInterface = createServiceInterface(interfaceModel);
        Object instance = newInstance(clazz);
        Adapter adapter = null;
        if (instance instanceof Adapter) {
            adapter = (Adapter) instance;
        } else {
            adapter = createAnnotatedAdapter(instance, interfaceModel);
        }
        adapter.setServiceInterface(serviceInterface);
        return adapter;
    }

    private static Object newInstance(Class<?> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new SwitchYardException("Error constructing Adapter instance of type '" + clazz + "'.  Class must have a public default constructor.", e);
        }
    }

    private static Adapter createAnnotatedAdapter(final Object instance, InterfaceModel interfaceModel) {
        if (!InterfaceModel.JAVA.equals(interfaceModel.getType())) {
            throw new SwitchYardException("Annotated Adapters only supported for Java interfaces: " + interfaceModel + ".");
        }
        final Method adapterMethod = getAdapterMethod(instance.getClass());
        org.switchyard.annotations.Adapter adapter = adapterMethod.getAnnotation(org.switchyard.annotations.Adapter.class);
        if (adapter == null) {
            throw new SwitchYardException("Failed to locate Adapter annotation method: " + instance.getClass() + ".");
        }
        return new BaseAdapter() {
            @Override
            public ServiceOperation lookup(String consumerOperation, ServiceInterface targetInterface) {
                Object[] parameters = new Object[] {
                        consumerOperation, targetInterface 
                    };
                try {
                    return (ServiceOperation) adapterMethod.invoke(instance, parameters);
                } catch (IllegalAccessException e) {
                    throw new SwitchYardException("Invokation of adpater method " + adapterMethod + " failed.", e);
                } catch (IllegalArgumentException e) {
                    throw new SwitchYardException("Invokation of adpater method " + adapterMethod + " failed.", e);
                } catch (InvocationTargetException e) {
                    throw new SwitchYardException("Invokation of adpater method " + adapterMethod + " failed.", e);
                }
            }
        };
    }
}
