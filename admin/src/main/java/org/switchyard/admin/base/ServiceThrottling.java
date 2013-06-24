/* 
 * JBoss, Home of Professional Open Source 
 * Copyright 2013 Red Hat Inc. and/or its affiliates and other contributors
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
package org.switchyard.admin.base;

import org.switchyard.ServiceReference;
import org.switchyard.admin.Throttling;
import org.switchyard.config.model.composite.ExtensionsModel;
import org.switchyard.config.model.switchyard.ThrottlingModel;
import org.switchyard.metadata.ServiceMetadataBuilder;

/**
 * ServiceThrottling
 * <p/>
 * Throttling details for Service objects.
 */
public class ServiceThrottling implements Throttling {

    private final BaseService _service;
    private boolean _enabled = false;
    private int _maxRequests;
    private long _timePeriod = org.switchyard.metadata.qos.Throttling.DEFAULT_TIME_PERIOD;

    /**
     * Create a new ServiceThrottling.
     * 
     * @param service the service being throttled
     * @param extensions configuration details
     */
    public ServiceThrottling(final BaseService service, final ExtensionsModel extensions) {
        _service = service;

        if (extensions == null) {
            return;
        }
        final ThrottlingModel throttling = extensions.getThrottling();
        if (throttling == null) {
            return;
        }
        _maxRequests = throttling.getMaxRequests();
        _enabled = true;
        final Long timePeriod = throttling.getTimePeriod();
        if (timePeriod != null) {
            _timePeriod = timePeriod;
        }
    }

    @Override
    public boolean isEnabled() {
        return _enabled;
    }

    @Override
    public void enable() {
        update(true, null);
    }

    @Override
    public void disable() {
        update(false, null);
    }

    @Override
    public int getMaxRequests() {
        return _maxRequests;
    }

    @Override
    public void setMaxRequests(int maxRequests) {
        update(null, maxRequests);
    }

    @Override
    public long getTimePeriod() {
        return _timePeriod;
    }

    @Override
    public void update(Boolean enabled, Integer maxRequests) {
        boolean doUpdate = false;
        if (maxRequests != null && maxRequests != _maxRequests) {
            _maxRequests = maxRequests;
            // only update if enabled
            doUpdate = _enabled;
        }
        if (enabled != null && enabled != _enabled) {
            _enabled = enabled;
            // always update
            doUpdate = true;
        }
        if (doUpdate) {
            updateThrottler();
        }
    }

    private void updateThrottler() {
        final org.switchyard.metadata.qos.Throttling throttling;
        if (_enabled) {
            throttling = new org.switchyard.metadata.qos.Throttling();
            throttling.setMaxRequests(_maxRequests);
            throttling.setTimePeriod(_timePeriod);
        } else {
            throttling = null;
        }
        final BaseApplication application = (BaseApplication) _service.getApplication();
        final ServiceReference serviceReference = application.getDeployment().getDomain().getServiceReference(_service.getName());
        ServiceMetadataBuilder.update(serviceReference.getServiceMetadata()).throttling(throttling);
    }
}
