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

import static org.switchyard.config.model.domain.HandlerModel.HANDLER;
import static org.switchyard.config.model.switchyard.SwitchYardModel.DEFAULT_NAMESPACE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import org.switchyard.config.Configuration;
import org.switchyard.config.model.BaseModel;
import org.switchyard.config.model.Descriptor;
import org.switchyard.config.model.domain.DomainModel;
import org.switchyard.config.model.domain.HandlerModel;
import org.switchyard.config.model.domain.HandlersModel;

/**
 * A version 1 HandlersModel.
 */
public class V1HandlersModel extends BaseModel implements HandlersModel {

    private List<HandlerModel> _handlers = new ArrayList<HandlerModel>();

    /**
     * Constructs a new V1HandlersModel.
     */
    public V1HandlersModel() {
        super(new QName(DEFAULT_NAMESPACE, HANDLERS));
        setModelChildrenOrder(HANDLER);
    }

    /**
     * Constructs a new V1HandlersModel with the specified Configuration and Descriptor.
     * @param config the Configuration
     * @param desc the Descriptor
     */
    public V1HandlersModel(Configuration config, Descriptor desc) {
        super(config, desc);
        for (Configuration handler_config : config.getChildrenStartsWith(HANDLER)) {
            HandlerModel handler = (HandlerModel)readModel(handler_config);
            if (handler != null) {
                _handlers.add(handler);
            }
        }
        setModelChildrenOrder(HANDLER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomainModel getDomain() {
        return (DomainModel)getModelParent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized List<HandlerModel> getHandlers() {
        return Collections.unmodifiableList(_handlers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized HandlersModel addHandler(HandlerModel handler) {
        addChildModel(handler);
        _handlers.add(handler);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HandlerModel removeHandler(String handlerName) {
        HandlerModel removed = null;
        
        for (HandlerModel handler : _handlers) {
            if (handler.getName().equals(handlerName)) {
                removed = handler;
                _handlers.remove(handler);
            }
        }
        return removed;
    }

}
