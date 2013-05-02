/* 
 * JBoss, Home of Professional Open Source 
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @author tags. All rights reserved. 
 * See the copyright.txt in the distribution for a 
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use, 
 * modify, copy, or redistribute it subject to the terms and conditions 
 * of the GNU Lesser General Public License, v. 2.1. 
 * This program is distributed in the hope that it will be useful, but WITHOUT A 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details. 
 * You should have received a copy of the GNU Lesser General Public License, 
 * v.2.1 along with this distribution; if not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 */

package org.switchyard.internal;

import java.util.HashMap;
import java.util.Map;

import javax.activation.DataSource;
import javax.xml.namespace.QName;

import org.switchyard.Context;
import org.switchyard.Message;
import org.switchyard.Scope;
import org.switchyard.metadata.java.JavaService;
import org.switchyard.runtime.RuntimeMessages;
import org.switchyard.transform.Transformer;
import org.switchyard.transform.TransformerRegistry;

/**
 * Implementation of <code>Message</code>.
 */
public class DefaultMessage implements Message {

    private TransformerRegistry _transformerRegistry;
    private Object _content;
    private Map<String, DataSource> _attachments =
        new HashMap<String, DataSource>();
    private Context _context = new DefaultContext(Scope.MESSAGE);
    private boolean _sent;

    /**
     * Create a new instance of DefaultMessage.
     */
    public DefaultMessage() {
    }

    /**
     * Set the transformation registry to be used by the Message instance when
     * performing payload conversions.
     *
     * @param transformerRegistry The transformation registry instance.
     * @return <code>this</code> Message instance.
     */
    public DefaultMessage setTransformerRegistry(TransformerRegistry transformerRegistry) {
        _transformerRegistry = transformerRegistry;
        return this;
    }

    @Override
    public DefaultMessage addAttachment(final String name, final DataSource attachment) {
        _attachments.put(name, attachment);
        return this;
    }

    @Override
    public DataSource getAttachment(final String name) {
        return _attachments.get(name);
    }

    @Override
    public void removeAttachment(final String name) {
        _attachments.remove(name);
    }

    @Override
    public Map<String, DataSource> getAttachmentMap() {
        return new HashMap<String, DataSource>(_attachments);
    }

    @Override
    public Object getContent() {
        return _content;
    }

    @Override
    public Context getContext() {
        return _context;
    }

    @Override
    public <T> T getContent(final Class<T> type) {
        if (type == null) {
            throw RuntimeMessages.MESSAGES.nullTypeArgument();
        }
        if (_content == null) {
            return null;
        }
        if (type.isInstance(_content)) {
            return type.cast(_content);
        }
        if (_transformerRegistry == null) {
            throw RuntimeMessages.MESSAGES.noTransformRegistryAvailable(_content.getClass().getName(), type.getName());
        }

        QName fromType = JavaService.toMessageType(_content.getClass());
        QName toType = JavaService.toMessageType(type);
        Transformer transformer = _transformerRegistry.getTransformer(fromType, toType);
        if (transformer == null) {
            throw RuntimeMessages.MESSAGES.noRegisteredTransformer(_content.getClass().getName(), type.getName(), 
                    fromType.toString(), toType.toString());
        }

        Object transformedContent = transformer.transform(_content);
        if (transformedContent == null) {
            throw RuntimeMessages.MESSAGES.transformerReturnedNull(_content.getClass().getName(), type.getName(),
                    transformer.getClass().getName());
        }
        if (!type.isInstance(transformedContent)) {
            throw RuntimeMessages.MESSAGES.transformerReturnedIncompatibleType(_content.getClass().getName(), type.getName(), transformer.getClass().getName(), 
                    transformedContent.getClass().getName());
        }

        return type.cast(transformedContent);
    }

    @Override
    public DefaultMessage setContent(final Object content) {
        _content = content;
        return this;
    }

    @Override
    public Message copy() {
        DefaultMessage message = new DefaultMessage();
        if (_transformerRegistry != null) {
            message.setTransformerRegistry(_transformerRegistry);
        }
        return message.setContent(_content);
    }

    /**
     * Checks if message was already sent by someone.
     * 
     * @return True if message was already sent.
     */
    public boolean isSent() {
        return _sent;
    }

    /**
     * Sets sent flag to true.
     */
    public void send() {
        _sent = true;
    }

}
