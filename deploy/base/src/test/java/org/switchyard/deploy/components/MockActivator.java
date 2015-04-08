/*
 * Copyright 2013 Red Hat Inc. and/or its affiliates and other contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.switchyard.deploy.components;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.switchyard.config.model.composite.BindingModel;
import org.switchyard.config.model.composite.ComponentModel;
import org.switchyard.deploy.BaseActivator;
import org.switchyard.deploy.BaseServiceHandler;
import org.switchyard.deploy.ServiceHandler;

public class MockActivator extends BaseActivator {

    public static final String ACTIVATION_TYPE = "mock";

    private Map<String, MockServiceHandler> _handlers = new HashMap<String, MockServiceHandler>();
    
    public MockActivator() {
        super(ACTIVATION_TYPE);
    }
    
    @Override
    public ServiceHandler activateBinding(QName serviceName, BindingModel config) {
        String activationName =
                (serviceName != null ? serviceName.toString() : "{null}null")
                        + "_"
                        + (config != null ? config.getName() : "null");
        MockServiceHandler handler = new MockServiceHandler(activationName);
        handler.activate();
        _handlers.put(activationName, handler);
        return handler;
    }
    
    @Override
    public ServiceHandler activateService(QName serviceName, ComponentModel config) {
        String activationName =
                (serviceName != null ? serviceName.toString() : "{null}null")
                        + "_"
                        + (config != null ? config.getName() : "null");
        MockServiceHandler handler = new MockServiceHandler(activationName);
        handler.activate();
        _handlers.put(activationName, handler);
        return handler;
    }
    
    
    @Override
    public void deactivateBinding(QName name, ServiceHandler handler) {
        MockServiceHandler.class.cast(handler).deactivate();
    }

    @Override
    public void deactivateService(QName name, ServiceHandler handler) {
        MockServiceHandler.class.cast(handler).deactivate();
    }

    public boolean startCalled(String activationName) {
        MockServiceHandler handler = _handlers.get(activationName);
        return handler != null && handler.startCalled();
    }

    public boolean startCalled() {
        for(MockServiceHandler handler : _handlers.values()) {
            if (!handler.startCalled()) {
                return false;
            }
        }
        return true;
    }

    public boolean stopCalled(String activationName) {
        MockServiceHandler handler = _handlers.get(activationName);
        return handler != null && handler.stopCalled();
    }

    public boolean stopCalled() {
        for(MockServiceHandler handler : _handlers.values()) {
            if (!handler.stopCalled()) {
                return false;
            }
        }
        return true;
    }

    public boolean activateBindingCalled(String activationName) {
        MockServiceHandler handler = _handlers.get(activationName);
        return handler != null && handler.activateCalled();
    }

    public boolean activateBindingCalled() {
        for(MockServiceHandler handler : _handlers.values()) {
            if (!handler.activateCalled()) {
                return false;
            }
        }
        return true;
    }

    public boolean activateServiceCalled(String activationName) {
        MockServiceHandler handler = _handlers.get(activationName);
        return handler != null && handler.activateCalled();
    }

    public boolean activateServiceCalled() {
        for(MockServiceHandler handler : _handlers.values()) {
            if (!handler.activateCalled()) {
                return false;
            }
        }
        return true;
    }

    public boolean deactivateBindingCalled(String activationName) {
        MockServiceHandler handler = _handlers.get(activationName);
        return handler != null && handler.deactivateCalled();
    }

    public boolean deactivateBindingCalled() {
        for(MockServiceHandler handler : _handlers.values()) {
            if (!handler.deactivateCalled()) {
                return false;
            }
        }
        return true;
    }

    public boolean deactivateServiceCalled(String activationName) {
        MockServiceHandler handler = _handlers.get(activationName);
        return handler != null && handler.deactivateCalled();
    }

    public boolean deactivateServiceCalled() {
        for(MockServiceHandler handler : _handlers.values()) {
            if (!handler.deactivateCalled()) {
                return false;
            }
        }
        return true;
    }
    
    class MockServiceHandler extends BaseServiceHandler {
        private String _name;
        private boolean _activateCalled;
        private boolean _deactivateCalled;
        private boolean _startCalled;
        private boolean _stopCalled;

        MockServiceHandler(String name) {
            _name = name;
        }

        public String getActivationName() {
            return _name;
        }

        public void activate() {
            _activateCalled = true;
        }

        public boolean activateCalled() {
            return _activateCalled;
        }

        public void deactivate() {
            _deactivateCalled = true;
        }

        public boolean deactivateCalled() {
            return _deactivateCalled;
        }

        @Override
        public void doStart() {
            _startCalled = true;
        }

        public boolean startCalled() {
            return _startCalled;
        }

        @Override
        public void doStop() {
            _stopCalled = true;
        }

        public boolean stopCalled() {
            return _stopCalled;
        }
    }
}
