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

package org.switchyard.admin.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.switchyard.admin.Component;
import org.switchyard.admin.ComponentReference;
import org.switchyard.admin.ComponentService;

/**
 * BaseComponent
 * 
 * Basic implementation for Component.
 */
public class BaseComponent implements Component {

    private final QName _name;
    private final String _type;
    private final ComponentService _service;
    private final Map<QName, ComponentReference> _references;
    private final Map<String, String> _properties;

    /**
     * Create a new BaseComponent.
     * 
     * @param name the name of the component.
     * @param type the type of the component.
     * @param service the service provided by this component (may be null).
     * @param references the references required by this component.
     * @param properties the configuration properties of this component.
     */
    public BaseComponent(QName name, String type, ComponentService service, Map<QName, ComponentReference> references, Map<String, String> properties) {
        _name = name;
        _type = type;
        _service = service;
        _references = references;
        _properties = properties;
    }

    @Override
    public Map<String, String> getProperties() {
        return _properties;
    }

    @Override
    public QName getName() {
        return _name;
    }

    @Override
    public String getType() {
        return _type;
    }

    @Override
    public ComponentService getService() {
        return _service;
    }

    @Override
    public List<ComponentReference> getReferences() {
        if (_references == null) {
            return Collections.emptyList();
        }
        return new ArrayList<ComponentReference>(_references.values());
    }

    @Override
    public ComponentReference getReference(QName componentReferenceName) {
        if (_references == null) {
            return null;
        }
        return _references.get(componentReferenceName);
    }
}
