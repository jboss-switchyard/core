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

import java.text.MessageFormat;
import java.util.EventObject;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.switchyard.event.EventObserver;

/**
 * Unit tests for the EventManager class.
 */
public class EventManagerTest {

	private EventManager _manager;

	@Before
	public void setUp() throws Exception {
		_manager = new EventManager();
	}

	@Test
	public void testEventManagerFactoryNotNull() {
		Assert.assertNotNull(EventManagerFactory.getInstance());
	}

	@Test
	public void testEventManagerNotNull() {
		EventManagerFactory factory = EventManagerFactory.getInstance();

		Assert.assertNotNull(factory.getEventManager());
	}

	@Test
	public void testAsyncEventManagerPerformance() {
		AsynchronousEventManager asynchronousEventManager = new AsynchronousEventManager();

		EventObserverCounter eventObserCounter = new EventObserverCounter();

		asynchronousEventManager
				.addObserver(eventObserCounter, MockEvent.class);

		long start = System.currentTimeMillis();
		for (int i = 0; i < 450; i++) {
			asynchronousEventManager.publish(new MockEvent("test"));
		}

		long end = System.currentTimeMillis();

		while (eventObserCounter.getObserverCall() != 450);
		
		long endFinally = System.currentTimeMillis();
		
		System.out.println(MessageFormat.format("publish events done in {0}ms, processing done in {1}ms", new Object[]{
				end-start, endFinally-start
		}));
	}
	
	@Test
	public void testEventManagerPerformance() {
		EventManager asynchronousEventManager = new EventManager();

		EventObserverCounter eventObserCounter = new EventObserverCounter();

		asynchronousEventManager
				.addObserver(eventObserCounter, MockEvent.class);

		long start = System.currentTimeMillis();
		for (int i = 0; i < 450; i++) {
			asynchronousEventManager.publish(new MockEvent("test"));
		}

		long end = System.currentTimeMillis();

		while (eventObserCounter.getObserverCall() != 450);
		
		long endFinally = System.currentTimeMillis();
		
		System.out.println(MessageFormat.format("publish events done in {0}ms, processing done in {1}ms", new Object[]{
				end-start, endFinally-start
		}));
	}

	public class EventObserverCounter implements EventObserver {

		AtomicInteger observerCall = new AtomicInteger();

		@Override
		public void notify(EventObject event) {
			observerCall.incrementAndGet();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}

		public int getObserverCall() {
			return observerCall.get();
		}
	}

	public class MockEvent extends EventObject {

		public MockEvent(Object source) {
			super(source);
			// TODO Auto-generated constructor stub
		}

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	}
}
