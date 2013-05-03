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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.xml.namespace.QName;

import org.switchyard.exception.SwitchYardException;
import org.switchyard.metadata.ServiceInterface;
import org.switchyard.metadata.ServiceOperation;

/**
 * Utility class to create adapters.
 * 
 * @author Christoph Gostner &lt;<a href="mailto:christoph.gostner@objectbay.com">christoph.gostner@objectbay.com</a>&gt; &copy; 2013 Objectbay
 */
public final class AdapterUtil {
    private AdapterUtil() {}
    
    /**
     * Create adapters based on a generic class.
     * 
     * @see #isAdapter(Class)
     * @param clazz The class representing the adapters.
     * @param from The consumer side service name.
     * @param to The provider side service name.
     * @return The adapters created based on the generic class.
     */
    public static List<Adapter> newAdapter(Class<?> clazz, QName from, QName to) {
        if (!isAdapter(clazz)) {
            throw new SwitchYardException("Invalid Adapter class '" + clazz.getName() + "'.  Must implement the Adapter interface, or have methods annotated with the @Adapter annotation.");
        }
        List<Adapter> adapters = new ArrayList<Adapter>();
        Object adapterObject = createAdapterObject(clazz);
        Collection<Adapter> annotatedAdapters = tryCreateAnnotatedAdapters(clazz, from, to, adapterObject);
        adapters.addAll(annotatedAdapters);
        
        if (adapterObject instanceof Adapter) {
            Collection<Adapter> adapterImplementations = createAdaptersImplementingAdapter(clazz, adapterObject, from, to);
            adapters.addAll(adapterImplementations);
        }
        return adapters;
    }

    /**
     * Test if the class represents adapters.
     * 
     * @param clazz The class to test.
     * @return True if the class represents adapters, otherwise false.
     */
    public static boolean isAdapter(Class<?> clazz) {
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

    private static Collection<Adapter> tryCreateAnnotatedAdapters(Class<?> clazz, QName from, QName to, Object adapterObject) {
        Collection<Adapter> adapters = new ArrayList<Adapter>();
        for (Method publicMethod : clazz.getMethods()) {
            org.switchyard.annotations.Adapter adapterAnno = publicMethod.getAnnotation(org.switchyard.annotations.Adapter.class);
            if (adapterAnno != null) {
                AdapterMethod adapterMethod = toAdapterMethod(publicMethod, adapterAnno);
                if (adapterMethod.getFrom().equals(from) && adapterMethod.getTo().equals(to)) {
                    adapters.add(newAdapter(adapterObject, adapterMethod.getMethod(), adapterMethod.getFrom(), adapterMethod.getTo()));
                }
            }
        }
        return adapters;
    }
    
    private static Collection<Adapter> createAdaptersImplementingAdapter(Class<?> clazz, Object adapterObject, QName from, QName to) {
        Collection<Adapter> adapters = new ArrayList<Adapter>();
        for (Method publicMethod : clazz.getMethods()) {
            Class<?> returnType = publicMethod.getReturnType();
            Class<?>[] parameterTypes = publicMethod.getParameterTypes();

            if (ServiceOperation.class.equals(returnType) && parameterTypes.length == 2 && String.class.equals(parameterTypes[0]) && ServiceInterface.class.equals(parameterTypes[1])) {
                Adapter adapter = (Adapter) adapterObject;
                AdapterMethod adapterMethod = toAdapterMethod(publicMethod, adapter);
                if (adapterMethod.getFrom().equals(from) && adapterMethod.getTo().equals(to)) {
                    adapters.add(newAdapter(adapterObject, adapterMethod.getMethod(), adapterMethod.getFrom(), adapterMethod.getTo()));
                }
            }
        }
        return adapters;
    }

    private static Adapter newAdapter(final Object adapterObject, final Method publicMethod, QName from, QName to) {
        Adapter adapter = new BaseAdapter(from, to) {
            @Override
            public ServiceOperation lookup(String consumerOperation, ServiceInterface targetInterface) {
                Object[] parameters = Arrays.asList(consumerOperation, targetInterface).toArray();
                
                try {
                    return (ServiceOperation) publicMethod.invoke(adapterObject, parameters);
                } catch (InvocationTargetException e) {
                    throw new SwitchYardException("Error executing @Adapter method '" + publicMethod.getName() + "' on class '" + publicMethod.getDeclaringClass().getName() + "'.", e.getCause());
                } catch (Exception e) {
                    throw new SwitchYardException("Error executing @Adapter method '" + publicMethod.getName() + "' on class '" + publicMethod.getDeclaringClass().getName() + "'.", e);
                }
            }
        };
        return adapter;
    }

    private static AdapterMethod toAdapterMethod(Method publicMethod, org.switchyard.annotations.Adapter adapterAnno) {
        QName from = QName.valueOf(adapterAnno.from().trim());
        QName to = QName.valueOf(adapterAnno.to().trim());
        
        return new AdapterMethod(from, to, publicMethod);
    }
    
    private static AdapterMethod toAdapterMethod(Method publicMethod, Adapter adapter) {
        return new AdapterMethod(adapter.getFrom(), adapter.getTo(), publicMethod);
    }

    private static Object createAdapterObject(Class<?> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new SwitchYardException("Error constructing Adapter instance for class '" + clazz.getName() + "'.  Class must have a public default constructor.", e);
        }
    }
    
    private static class AdapterMethod extends AdapterTypes {
        private Method _publicMethod;

        AdapterMethod(QName from, QName to, Method publicMethod) {
            super(from, to);
            this._publicMethod = publicMethod;
        }
        
        public Method getMethod() {
            return _publicMethod;
        }
    }
}
