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

package org.switchyard.admin;

import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

/**
 * Component
 * 
 * Represents a SwitchYard component within an application.
 */
public interface Component {
    /**
     * @return the name of this component.
     */
    QName getName();

    /**
     * @return implementation type, e.g. bean, bpm, etc.
     */
    String getType();

    /**
     * @return component properties.
     */
    Map<String,String> getProperties();

    
    /**
     * @return the service provided by this component.
     */
    ComponentService getService();

    /**
     * @return the references contained by this component.
     */
    List<ComponentReference> getReferences();

    /**
     * @param componentReferenceName the name of a reference required by
     *            this component.
     * @return the requested reference, may be null
     */
    ComponentReference getReference(QName componentReferenceName);
}
