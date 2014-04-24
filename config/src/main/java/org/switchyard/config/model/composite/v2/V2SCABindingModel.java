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

package org.switchyard.config.model.composite.v2;

import javax.xml.namespace.QName;

import org.switchyard.config.model.composite.SCABindingModel;
import org.switchyard.config.model.composite.v1.V1SCABindingModel;

/**
 * V1 implementation in SCABindingModel.
 */
public class V2SCABindingModel extends V1SCABindingModel{

    /**
     * Create a new V1SCABindingModel.
     * @param switchyardNamespace switchyardNamespace
     */
    public V2SCABindingModel(String switchyardNamespace) {
        super(switchyardNamespace);
    }

	@Override
	public boolean isPreferLocal() {
		return Boolean.valueOf(getModelAttribute(new QName(_switchyardNamespace, PREFER_LOCAL)));
	}

	@Override
	public SCABindingModel setPreferLocal(boolean preferLocal) {
		setModelAttribute(new QName(_switchyardNamespace, PREFER_LOCAL), String.valueOf(preferLocal));
        return this;
	}
}
