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

package org.switchyard.internal.transform;

import javax.xml.namespace.QName;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.switchyard.transform.BaseTransformer;
import org.switchyard.transform.TransformerRegistry;

public class BaseTransformerRegistryTest {
    
    private TransformerRegistry _registry;
    
    @Before
    public void setUp() throws Exception {
        _registry = new BaseTransformerRegistry();
    }
    
    @Test
    public void testAddGetTransformer() {
        final QName fromName = new QName("a");
        final QName toName = new QName("b");
        
        BaseTransformer<String, Integer> t = 
            new BaseTransformer<String, Integer>(fromName, toName) {
                public Integer transform(String from) {
                    return null;
                }
        };
        
        _registry.addTransformer(t);
        Assert.assertEquals(t, _registry.getTransformer(fromName, toName));      
    }   
}
