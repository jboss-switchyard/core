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
package org.switchyard.config.model.selector.v1;

import javax.xml.namespace.QName;

import org.switchyard.config.Configuration;
import org.switchyard.config.model.BaseModel;
import org.switchyard.config.model.Descriptor;
import org.switchyard.config.model.selector.RegexOperationSelectorModel;
import org.switchyard.config.model.switchyard.SwitchYardModel;

/**
 * V1 Regex OperationSelector Model.
 */
public class V1RegexOperationSelectorModel extends BaseModel implements RegexOperationSelectorModel {

    /**
     * Constructor.
     */
    public V1RegexOperationSelectorModel() {
        super(new QName(SwitchYardModel.DEFAULT_NAMESPACE, OPERATION_SELECTOR + '.' + REGEX));
    }

    /**
     * Constructor.
     * @param config configuration
     * @param desc descriptor
     */
    public V1RegexOperationSelectorModel(Configuration config, Descriptor desc) {
        super(config, desc);
    }

    @Override
    public String getExpression() {
        return getModelAttribute(EXPRESSION);
    }

    @Override
    public RegexOperationSelectorModel setExpression(String expression) {
        setModelAttribute(EXPRESSION, expression);
        return this;
    }

}
