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

package org.switchyard.transform.smooks.internal;

import org.milyn.Smooks;
import org.milyn.javabean.binding.model.ModelSet;
import org.switchyard.SwitchYardException;
import org.switchyard.transform.Transformer;
import org.switchyard.transform.config.model.SmooksTransformModel;
import org.switchyard.transform.config.model.SmooksTransformType;
import org.switchyard.transform.internal.TransformerFactory;

import javax.xml.namespace.QName;

/**
 * Smooks Transformer factory.
 *
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class SmooksTransformFactory implements TransformerFactory<SmooksTransformModel> {

    /**
     * Create a {@link Transformer} instance from the supplied {@link SmooksTransformModel}.
     * @param model The model.
     * @return The Transformer instance.
     */
    public Transformer newTransformer(SmooksTransformModel model) {
        String transformType = model.getTransformType();
        String config = model.getConfig();
        QName from = model.getFrom();
        QName to = model.getTo();

        if (transformType == null || transformType.trim().length() == 0) {
            throw new SwitchYardException("Invalid Smooks configuration model.  null or empty 'type' specification.");
        }
        if (config == null || config.trim().length() == 0) {
            throw new SwitchYardException("Invalid Smooks configuration model.  null or empty 'config' specification.");
        }
        if (from == null) {
            throw new SwitchYardException("Invalid Smooks configuration model.  null or 'from' specification.");
        }
        if (to == null) {
            throw new SwitchYardException("Invalid Smooks configuration model.  null or 'to' specification.");
        }

        SmooksTransformType transformationType = SmooksTransformType.valueOf(transformType);

        Smooks smooks;
        try {
            smooks = new Smooks(config);
            smooks.createExecutionContext();
        } catch (Exception e) {
            throw new SwitchYardException("Failed to create Smooks instance for config '" + config + "'.", e);
        }

        Transformer transformer;

        if (transformationType == SmooksTransformType.JAVA2XML) {
            transformer = newXMLBindingTransformer(from, to, smooks, XMLBindingTransformer.BindingDirection.JAVA2XML);
        } else if (transformationType == SmooksTransformType.XML2JAVA) {
            transformer = newXMLBindingTransformer(from, to, smooks, XMLBindingTransformer.BindingDirection.XML2JAVA);
        } else if (transformationType == SmooksTransformType.SMOOKS) {
            transformer = new SmooksTransformer(from, to, smooks, model);
        } else {
            throw new SwitchYardException("Unhandled Smooks transformation type '" + transformationType + "'.");
        }

        transformer.setFrom(model.getFrom());
        transformer.setTo(model.getTo());

        return transformer;
    }

    private static Transformer newXMLBindingTransformer(final QName from, final QName to, Smooks smooks, XMLBindingTransformer.BindingDirection direction) {
        ModelSet beanModel = ModelSet.get(smooks.getApplicationContext());
        if (beanModel != null && !beanModel.getModels().isEmpty()) {
            return new XMLBindingTransformer(from, to, smooks, beanModel, direction);
        } else {
            throw new SwitchYardException("Invalid " + direction + " binding configuration.  No <jb:bean> configurations found.");
        }
    }
}
