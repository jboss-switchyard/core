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

package org.switchyard.tests;

import junit.framework.Assert;

import org.junit.Test;
import org.switchyard.MessageBuilder;
import org.switchyard.internal.message.DefaultMessageBuilder;
import org.switchyard.internal.message.StreamMessageBuilder;
import org.switchyard.message.DefaultMessage;
import org.switchyard.message.StreamMessage;

public class MessageBuilderTest {
        
        @Test
        public void testNewInstanceDefault() throws Exception {
                MessageBuilder builder = MessageBuilder.newInstance();
                Assert.assertTrue(builder instanceof DefaultMessageBuilder);
        }
        
        @Test
        public void testNewInstanceTyped() throws Exception {
                MessageBuilder defaultBuilder = 
                        MessageBuilder.newInstance(DefaultMessage.class);
                Assert.assertTrue(defaultBuilder instanceof DefaultMessageBuilder);
                MessageBuilder streamBuilder = 
                        MessageBuilder.newInstance(StreamMessage.class);
                Assert.assertTrue(streamBuilder instanceof StreamMessageBuilder);
        }
}
