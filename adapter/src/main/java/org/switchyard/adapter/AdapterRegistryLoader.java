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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.switchyard.adapter.config.model.JavaAdaptModel;
import org.switchyard.common.type.Classes;
import org.switchyard.config.model.adapter.AdaptModel;
import org.switchyard.config.model.adapter.AdaptersModel;
import org.switchyard.exception.DuplicateAdapterException;
import org.switchyard.exception.SwitchYardException;

/**
 * Adapter registry loader implementation.
 * 
 * @author Christoph Gostner &lt;<a href="mailto:christoph.gostner@objectbay.com">christoph.gostner@objectbay.com</a>&gt; &copy; 2013 Objectbay
 */
public class AdapterRegistryLoader {
    /**
     * Logger.
     */
    private static Logger _log = Logger.getLogger(AdapterRegistryLoader.class);
    /**
     * Adapters.
     */
    private List<Adapter> _adapters = new LinkedList<Adapter>();
    /**
     * The registry instance into which the adapters were loaded.
     */
    private AdapterRegistry _adapterRegistry;
    
    /**
     * Public constructor.
     * 
     * @param adapterRegistry The registry instance.
     */
    public AdapterRegistryLoader(AdapterRegistry adapterRegistry) {
        if (adapterRegistry == null) {
            throw new IllegalArgumentException("null 'adapterRegistry' argument.");
        }
        this._adapterRegistry = adapterRegistry;
    }
    
    /**
     * Register a set of adapters in the adapter registry associated with this deployment.
     * 
     * @param adaptersModel The adapters model.
     * @throws DuplicateAdapterException an existing adapter has already been registered for the from and to types
     */
    public void registerAdapters(AdaptersModel adaptersModel) throws DuplicateAdapterException {
        if (adaptersModel == null) {
            return;
        }

        try {
            for (AdaptModel adaptModel : adaptersModel.getAdapts()) {
                Collection<Adapter> adapters = newAdapters(adaptModel);
                for (Adapter adapter : adapters) {
                    if (_adapterRegistry.hasAdapter(adapter.getFrom())) {
                        Adapter registeredAdapter = _adapterRegistry.getAdapter(adapter.getFrom());
                        String msg = "Failed to register Adapter '" + toDescription(adapter)
                                + "'.  An Adapter for these services is already registered: '"
                                + toDescription(registeredAdapter) + "'.";
                        throw new DuplicateAdapterException(msg);
                    } else {   
                        _log.debug("Adding adapter =>"
                                + " From: " + adapter.getFrom()
                                + ", To:" + adapter.getTo());
                        _adapterRegistry.addAdapter(adapter);
                        _adapters.add(adapter);
                    }
                }
            }
        } catch (DuplicateAdapterException e) {
            throw e;
        } catch (RuntimeException e) {
            // If there was an exception for any reason... remove all Adapter instance that have
            // already been registered with the domain...
            unregisterAdapters();
            throw e;
        }
    }

    /**
     * Unregister all adapters.
     */
    public void unregisterAdapters() {
        for (Adapter adapter : _adapters) {
            _adapterRegistry.removeAdapter(adapter);
        }
    }
    
    /**
     * Create adapters based on the adapt Model.
     * 
     * @param adaptModel The adapt Model.
     * @return Adapters represented through the adapt Model.
     */
    public Collection<Adapter> newAdapters(AdaptModel adaptModel) {
        Collection<Adapter> adapters = null;

        if (adaptModel instanceof JavaAdaptModel) {
            String className = ((JavaAdaptModel) adaptModel).getClazz();
                Class<?> adapterClass = Classes.forName(className, AdapterUtil.class);
                if (adapterClass == null) {
                    throw new SwitchYardException("Unable to load adapter class " + className);
                }
                adapters = AdapterUtil.newAdapter(adapterClass, adaptModel.getFrom(), adaptModel.getTo());
            
        } else {
            throw new SwitchYardException("Only JavaAdapterModel supported");
        }

        if (adapters == null || adapters.isEmpty()) {
            throw new SwitchYardException("Unknown AModel type '" + adaptModel.getClass().getName() + "'.");
        }

        return adapters;
    }
    
    List<Adapter> getAdapters() {
        return _adapters;
    }
    
    private String toDescription(Adapter adapter) {
        return adapter.getClass().getName() + "(" + adapter.getFrom() + ", " + adapter.getTo() + ")";
    }
}
