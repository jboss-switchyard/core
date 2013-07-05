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
package org.switchyard.bus.camel;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.xml.namespace.QName;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.switchyard.Context;
import org.switchyard.Message;
import org.switchyard.SwitchYardException;
import org.switchyard.common.camel.HandlerDataSource;
import org.switchyard.common.camel.SwitchYardCamelContext;
import org.switchyard.common.camel.SwitchYardMessage;
import org.switchyard.label.BehaviorLabel;
import org.switchyard.metadata.JavaTypes;
import org.switchyard.transform.Transformer;
import org.switchyard.transform.TransformerRegistry;

/**
 * Message implementation which adapt SwitchYard {@link Message} interface to 
 * {@link org.apache.camel.Message}.
 */
public class CamelMessage extends SwitchYardMessage implements Message {

    /**
     * Creates new Camel message with specified exchange.
     * 
     * @param exchange The camel exchange.
     */
    public CamelMessage(Exchange exchange) {
        setExchange(exchange);
    }

    @Override
    public Message setContent(Object content) {
        setBody(content);
        return this;
    }

    @Override
    public Object getContent() {
        return getBody();
    }

    @Override
    public <T> T getContent(Class<T> type) {
        return getBody(type);
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected <T> T getBody(Class<T> type, Object body) {
        if (type == null) {
            throw new IllegalArgumentException("null 'type' argument.");
        }
        if (body == null) {
            return null;
        }
        if (type.isInstance(body)) {
            return type.cast(body);
        }

        TransformerRegistry transformerRegistry = getTransformerRegistry();
        if (transformerRegistry == null) {
            throw new SwitchYardException("Cannot convert from '" + body.getClass().getName() + "' to '" + type.getName() + "'.  No TransformRegistry available.");
        }

        QName toType = JavaTypes.toMessageType(type);
        QName fromType = JavaTypes.toMessageType(body.getClass());
        Transformer transformer = transformerRegistry.getTransformer(fromType, toType);
        if (transformer == null) {
            T camelBody = super.getBody(type, body);
            if (camelBody == null) {
                throw new SwitchYardException("Cannot convert from '" + body.getClass().getName() + "' to '" + type.getName() + "'.  No registered Transformer available for transforming from '" + fromType + "' to '" + toType + "'.  A Transformer must be registered.");
            }
            return camelBody;
        }

        Object transformedContent = transformer.transform(body);
        if (transformedContent == null) {
            throw new SwitchYardException("Error converting from '" + body.getClass().getName() + "' to '" + type.getName() + "'.  Transformer '" + transformer.getClass().getName() + "' returned null.");
        }
        if (!type.isInstance(transformedContent)) {
            throw new SwitchYardException("Error converting from '" + body.getClass().getName() + "' to '" + type.getName() + "'.  Transformer '" + transformer.getClass().getName() + "' returned incompatible type '" + transformedContent.getClass().getName() + "'.");
        }

        return type.cast(transformedContent);
    }

    @Override
    public Message addAttachment(String name, DataSource attachment) {
        addAttachment(name, new DataHandler(attachment));
        return this;
    }

    @Override
    public HandlerDataSource getAttachment(String name) {
        DataHandler attachement = super.getAttachment(name);
        return attachement != null ? new HandlerDataSource(attachement) : null;
    }

    @Override
    public void removeAttachment(String name) {
        HandlerDataSource attachment = getAttachment(name);
        if (attachment != null) {
            removeAttachment(name);
        }
    }

    @Override
    public Map<String, DataSource> getAttachmentMap() {
        Map<String, DataSource> attachements = new HashMap<String, DataSource>();
        for (Entry<String, DataHandler> attachement : getAttachments().entrySet()) {
            attachements.put(attachement.getKey(), attachement.getValue().getDataSource());
        }
        return attachements;
    }

    @Override
    public Context getContext() {
        return new CamelCompositeContext(getExchange(), this);
    }

    @Override
    public CamelMessage copy() {
        CamelMessage message = newInstance();
        message.setBody(getBody());
        return message;
    }

    @Override
    public CamelMessage newInstance() {
        return new CamelMessage(getExchange());
    }

    /**
     * Mark message as sent.
     */
    public void sent() {
        getContext().setProperty(CamelExchange.MESSAGE_SENT, true)
            .addLabels(BehaviorLabel.TRANSIENT.label());
    }

    /**
     * Verify if message was sent or not.
     * 
     * @return True if message was sent.
     */
    public boolean isSent() {
        return getHeader(CamelExchange.MESSAGE_SENT, false, Boolean.class);
    }

    private TransformerRegistry getTransformerRegistry() {
        CamelContext context = getExchange().getContext();
        if (context instanceof SwitchYardCamelContext) {
            return ((SwitchYardCamelContext) context).getServiceDomain().getTransformerRegistry();
        }
        return null;
    }
}
