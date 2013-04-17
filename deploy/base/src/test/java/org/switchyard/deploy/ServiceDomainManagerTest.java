/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
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

package org.switchyard.deploy;

import java.lang.management.ManagementFactory;
import java.util.EventObject;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.xml.namespace.QName;

import org.junit.Assert;
import org.junit.Test;
import org.switchyard.ServiceDomain;
import org.switchyard.config.model.ModelPuller;
import org.switchyard.config.model.switchyard.SwitchYardModel;
import org.switchyard.event.EventObserver;

/**
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class ServiceDomainManagerTest {

    @Test
    public void testHandlerRegistration() throws Exception {
        SwitchYardModel switchyard = new ModelPuller<SwitchYardModel>().pull(
                "/switchyard-config-handler-01.xml", getClass());
        
        ServiceDomain domain = new ServiceDomainManager().createDomain(
                new QName("test"), switchyard);
        
        Assert.assertEquals(2, domain.getHandlers().size());
    }
    
    @Test
    public void testRegisteredEventManagerMBean() {
        ServiceDomainManager sdm=new ServiceDomainManager();
        
        TestEventObserver observer=new TestEventObserver();
        
        // Register listener via EventManagerMBean
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer(); 
        ObjectName objname=null;
        
        try {
            objname = new ObjectName(ServiceDomainManager.SWITCHYARD_OBJECTNAME_EVENT_MANAGER);
            
            java.util.List<Class<?>> eventTypes=new java.util.ArrayList<Class<?>>();
            eventTypes.add(TestEvent.class);               
            
            Object[] params={observer, eventTypes};
            
            String[] types={EventObserver.class.getName(), java.util.List.class.getName()};
            
            mbs.invoke(objname, "addObserver", params, types);
        } catch (Exception e) {
            Assert.fail("Failed to register event observer via MBean: "+e);
        }

        TestEvent te=new TestEvent();
        
        sdm.getEventManager().publish(te);
        
        // Check received by the event observer
        if (observer.getEvents().size() != 1) {
            Assert.fail("Expecting 1 event, but got: "+observer.getEvents().size());
        }
        
        if (observer.getEvents().get(0) != te) {
            Assert.fail("Unexpected event: "+observer.getEvents().get(0));
        }
        
        // Make sure can unregister through MBean
        try {            
            Object[] params={observer};
            
            String[] types={EventObserver.class.getName()};
            
            mbs.invoke(objname, "removeObserver", params, types);
        } catch (Exception e) {
            Assert.fail("Failed to register event observer via MBean: "+e);
        }

        TestEvent te2=new TestEvent();
        
        sdm.getEventManager().publish(te2);
        
        // Should still only be a single event received
        if (observer.getEvents().size() != 1) {
            Assert.fail("Still only expecting 1 event, but got: "+observer.getEvents().size());
        }
    }
    
    public class TestEvent extends java.util.EventObject {
        
        private static final long serialVersionUID = 1L;

        public TestEvent() {
            super("TestEvent");
        }
    }
    public class TestEventObserver implements EventObserver {

        private java.util.List<EventObject> _events=new java.util.ArrayList<EventObject>();
        
        @Override
        public void notify(EventObject event) {
            _events.add(event);
        }
        
        public java.util.List<EventObject> getEvents() {
            return (_events);
        }
    }
}
