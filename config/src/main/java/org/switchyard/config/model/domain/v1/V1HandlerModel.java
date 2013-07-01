/*
 * 2012 Red Hat Inc. and/or its affiliates and other contributors.
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
package org.switchyard.config.model.domain.v1;

import javax.xml.namespace.QName;

import org.switchyard.config.Configuration;
import org.switchyard.config.model.BaseModel;
import org.switchyard.config.model.Descriptor;
import org.switchyard.config.model.domain.HandlerModel;
import org.switchyard.config.model.domain.HandlersModel;
import org.switchyard.config.model.switchyard.SwitchYardModel;

/**
 * Implementation of HandlerModel : v1.
 */
public class V1HandlerModel extends BaseModel implements HandlerModel {

    /**
     * Constructs a new V1PropertyModel.
     */
    public V1HandlerModel() {
        super(new QName(SwitchYardModel.DEFAULT_NAMESPACE, HandlerModel.HANDLER));
    }

    /**
     * Constructs a new V1HandlerModel with the specified Configuration and Descriptor.
     * @param config the Configuration
     * @param desc the Descriptor
     */
    public V1HandlerModel(Configuration config, Descriptor desc) {
        super(config, desc);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HandlersModel getHandlers() {
        return (HandlersModel)getModelParent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return getModelAttribute(HandlerModel.NAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HandlerModel setName(String name) {
        setModelAttribute(HandlerModel.NAME, name);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getClassName() {
        return getModelAttribute(HandlerModel.CLASS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HandlerModel setClassName(String className) {
        setModelAttribute(HandlerModel.CLASS, className);
        return this;
    }
}
