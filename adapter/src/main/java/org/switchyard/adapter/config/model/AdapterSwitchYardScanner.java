///* 
// * JBoss, Home of Professional Open Source 
// * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
// * as indicated by the @author tags. All rights reserved. 
// * See the copyright.txt in the distribution for a 
// * full listing of individual contributors.
// *
// * This copyrighted material is made available to anyone wishing to use, 
// * modify, copy, or redistribute it subject to the terms and conditions 
// * of the GNU Lesser General Public License, v. 2.1. 
// * This program is distributed in the hope that it will be useful, but WITHOUT A 
// * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
// * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details. 
// * You should have received a copy of the GNU Lesser General Public License, 
// * v.2.1 along with this distribution; if not, write to the Free Software 
// * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
// * MA  02110-1301, USA.
// */
//package org.switchyard.adapter.config.model;
//
//import java.io.IOException;
//import java.lang.reflect.Method;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Comparator;
//import java.util.List;
//import java.util.Map;
//import java.util.TreeMap;
//
//import javax.xml.namespace.QName;
//
//import org.switchyard.adapter.Adapter;
//import org.switchyard.adapter.AdapterUtil;
//import org.switchyard.adapter.config.model.v1.V1JavaAdaptModel;
//import org.switchyard.common.type.classpath.AbstractTypeFilter;
//import org.switchyard.common.type.classpath.ClasspathScanner;
//import org.switchyard.config.model.Scannable;
//import org.switchyard.config.model.Scanner;
//import org.switchyard.config.model.ScannerInput;
//import org.switchyard.config.model.ScannerOutput;
//import org.switchyard.config.model.adapter.AdaptersModel;
//import org.switchyard.config.model.adapter.v1.V1AdaptersModel;
//import org.switchyard.config.model.switchyard.SwitchYardModel;
//import org.switchyard.config.model.switchyard.v1.V1SwitchYardModel;
//import org.switchyard.exception.DuplicateAdapterException;
//import org.switchyard.exception.SwitchYardException;
//
///**
// * Scanner for {@link org.switchyard.adapter.Adapt} implementations.
// *
// * @author Christoph Gostner &lt;<a href="mailto:christoph.gostner@objectbay.com">christoph.gostner@objectbay.com</a>&gt; &copy; 2013 Objectbay
// */
//public class AdapterSwitchYardScanner implements Scanner<SwitchYardModel> {
//
//    @Override
//    public ScannerOutput<SwitchYardModel> scan(ScannerInput<SwitchYardModel> input) throws IOException {
//        SwitchYardModel switchyardModel = new V1SwitchYardModel();
//        AdaptersModel adapters = new V1AdaptersModel();
//        
//        List<Class<?>> adaptClasses = scanForAdapts(input.getURLs());
//        for (Class<?> adaptClass : adaptClasses) {
//            Collection<AdapterTypes> adapterTypes = listAdaptTypes(adaptClass);
//            
//            for (AdapterTypes adapterType : adapterTypes) {
//                JavaAdaptModel adaptModel = new V1JavaAdaptModel();
//                adaptModel.setClazz(adaptClass.getName());
//                adaptModel.setFrom(adapterType.getFrom());
//                adaptModel.setTo(adapterType.getTo());
//                
//                adapters.addAdapt(adaptModel);
//            }
//        }
//        switchyardModel.setAdapters(adapters);
//        
//        return new ScannerOutput<SwitchYardModel>().setModel(switchyardModel);
//    }
//
//    private Collection<AdapterTypes> listAdaptTypes(Class<?> adaptClass) {
//        Map<QName, AdapterTypes> map = new TreeMap<QName, AdapterSwitchYardScanner.AdapterTypes>(new QNameComparable());
//        
//         AdapterTypes adapterType = adapterTypesFromAdapterInstance(adaptClass);
//         addToMap(map, adapterType);
//         
//        List<AdapterTypes> adapterTypes = adapterTypesFromAdapterAnnotation(adaptClass);
//        addToMap(map, adapterTypes);
//        
//        return map.values();
//    }
//
//    private void addToMap(Map<QName, AdapterTypes> map, List<AdapterTypes> adapterTypes) {
//        for (AdapterTypes adapter : adapterTypes) {
//            addToMap(map, adapter);
//        }
//    }
//
//    private void addToMap(Map<QName, AdapterTypes> map, AdapterTypes adapterType) {
//        if (adapterType != null) {
//            if (!map.containsKey(adapterType.getFrom())) {
//                map.put(adapterType.getFrom(), adapterType);
//            } else {
//                String msg = "An Adapter for the service is already registered: '" + adapterType.getFrom() + "'.";
//                throw new DuplicateAdapterException(msg);
//            }
//        }
//    }
//
//    List<AdapterTypes> adapterTypesFromAdapterAnnotation(Class<?> adaptClass) {
//        List<AdapterTypes> adapterTypes = new ArrayList<AdapterSwitchYardScanner.AdapterTypes>();
//        
//        Method[] publicMethods = adaptClass.getMethods();
//        for (Method method : publicMethods) {
//            org.switchyard.annotations.Adapter adapter = method.getAnnotation(org.switchyard.annotations.Adapter.class);
//            if (adapter != null && adapter.from() != null && adapter.to() != null) {
//                adapterTypes.add(new AdapterTypes(QName.valueOf(adapter.from()), QName.valueOf(adapter.to())));
//            }
//        }
//        return adapterTypes;
//    }
//
//    AdapterTypes adapterTypesFromAdapterInstance(Class<?> adaptClass) {
//        if (Adapter.class.isAssignableFrom(adaptClass)) {
//            try {
//                Adapter adapter = (Adapter) adaptClass.newInstance();
//                if (adapter.getFrom() != null && adapter.getTo() != null) {
//                    return new AdapterTypes(adapter.getFrom(), adapter.getTo());
//                }
//            } catch (Exception e) {
//                throw new SwitchYardException("Error constructing Adapter instance for class '" + adaptClass.getName() + "'.  Class must have a public default constructor.", e);
//            }
//        }
//        return null;
//    }
//
//    private List<Class<?>> scanForAdapts(List<URL> urls) throws IOException {
//        AbstractTypeFilter filter = new AdaptInstanceOfFilter();
//        ClasspathScanner scanner = new ClasspathScanner(filter);
//
//        for (URL url : urls) {
//            scanner.scan(url);
//        }
//
//        return filter.getMatchedTypes();
//    }
//    
//    private class AdapterTypes {
//        private QName _from;
//        private QName _to;
//
//        public AdapterTypes(QName from, QName to) {
//            this._from = from;
//            this._to = to;
//        }
//        
//        public QName getFrom() {
//            return _from;
//        }
//        
//        public QName getTo() {
//            return _to;
//        }
//    }
//
//    private final class AdaptInstanceOfFilter extends AbstractTypeFilter {
//        @Override
//        public boolean matches(Class<?> clazz) {
//            Scannable scannable = clazz.getAnnotation(Scannable.class);
//            if (scannable != null && !scannable.value()) {
//                // Marked as being non-scannable...
//                return false;
//            }
//            return AdapterUtil.isAdapter(clazz);
//        }
//    }
//
//    private final class QNameComparable implements Comparator<QName> {
//        @Override
//        public int compare(QName arg0, QName arg1) {
//            return arg0.toString().compareTo(arg1.toString());
//        }
//    }
//}
