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
package org.switchyard.config.model.switchyard.v1;

import org.switchyard.config.Configuration;
import org.switchyard.config.model.BaseModel;
import org.switchyard.config.model.Descriptor;
import org.switchyard.config.model.composite.CompositeServiceModel;
import org.switchyard.config.model.composite.ExtensionsModel;
import org.switchyard.config.model.switchyard.ManagementModel;

/**
 * A version 1 ManagementModel
 */
public class V1ManagementModel extends BaseModel implements ManagementModel {

    /**
     * Constructs a new V1ManagementModel.
     * @param namespace namespace
     */
    protected V1ManagementModel(String namespace, String name) {
        super(namespace, name);
    }

    /**
     * Constructs a new V1ManagementModel with the specified Configuration and Descriptor.
     * @param config the Configuration
     * @param desc the Descriptor
     */
    protected V1ManagementModel(Configuration config, Descriptor desc) {
        super(config, desc);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAutoStartupBindings() {
        String autoStartupBindings = getModelAttribute(AUTO_STARTUP_BINDINGS);
        return autoStartupBindings == null || "true".equals(autoStartupBindings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ManagementModel setAutoStartupBindings(boolean autoStartupBindings) {
        setModelAttribute(AUTO_STARTUP_BINDINGS, String.valueOf(autoStartupBindings));
        return this;
    }

}
