/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.switchyard.message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.switchyard.message.Builder;
import org.switchyard.message.DefaultMessage;


/**
 * This implementation is simply for demonstration.  For repeatable reads,
 * it creates a copy of the message as soon as the stream is set, which kind
 * of defeats the purpose of using a stream as content.  We should use a 
 * synchronized copy-on-read stream or serialization to a message repository
 * instead.
 */
@Builder("org.switchyard.internal.message.StreamMessageBuilder")
public class StreamMessage extends DefaultMessage {
        
        private ByteArrayOutputStream _contentBuffer;
        private boolean _readRepeatable;
        
        public StreamMessage() {
                this(false);
        }
        
        public StreamMessage(boolean readRepeatable) {
                _readRepeatable = readRepeatable;
        }
        
        public void setContent(InputStream stream) throws java.io.IOException {
                if (_readRepeatable) {
                        // create a copy in the local buffer - not ideal!
                        _contentBuffer = new ByteArrayOutputStream(stream.available());
                        byte[] buf = new byte[8 * 1024];
                        int count;
                        while ((count = stream.read(buf)) != -1) {
                                _contentBuffer.write(buf, 0, count);
                        }
                }
                else {
                        super.setContent(stream);
                }
        }
        
        public InputStream getContent() {
                if(_readRepeatable) {
                        return new ByteArrayInputStream(_contentBuffer.toByteArray());
                }
                else {
                        return super.getContent(InputStream.class);
                }
        }
        
        public boolean isReadRepeatable() {
                return _readRepeatable;
        }
}
