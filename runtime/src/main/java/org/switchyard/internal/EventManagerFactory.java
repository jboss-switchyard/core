package org.switchyard.internal;

/**
 * EventManagerFactory should be used to decouuple instantiation of EventMana
 * 
 * @author Andriy Vyedyeneyev (andriy.vyedyeneyev@github.com)
 * 
 */
public class EventManagerFactory {

	private static EventManagerFactory instance;

	/**
	 * singleton constructor, only for internal use
	 */
	private EventManagerFactory() {
	}

	public static EventManagerFactory getInstance() {
		return instance;
	}

	/**
	 * get new instance of EventManager
	 * 
	 * @return {@link EventManager}
	 */
	public EventManager getEventManager() {
		return new EventManager();
	}

	static {
		// TODO configuration over META-INF/org.switchyard.internal.EventManagerFactory
		instance = new EventManagerFactory();
	}
}
