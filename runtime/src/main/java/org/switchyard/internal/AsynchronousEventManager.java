package org.switchyard.internal;

import java.util.EventObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

/**
 * Asynchronous EventManager allow to proces events asynchronously from
 * notifications. This class
 * 
 * @author Andriy Vyedyeneyev (andriy.vyedyeneyev@github.com)
 * 
 */
public class AsynchronousEventManager extends EventManager {
	private static Logger _logger = Logger
			.getLogger(AsynchronousEventManager.class);
	private ThreadLocal<Boolean> publishInternal = new ThreadLocal<Boolean>();

	/**
	 * pool executes publish asynchronously. One pool for eventManager
	 */
	private ExecutorService threadPool = Executors.newSingleThreadExecutor();

	/**
	 * default constructor
	 */
	public AsynchronousEventManager() {
		publishInternal.set(false);
	}

	/**
	 * add event to pool for processing, or process publishing if called
	 * internally
	 */
	@Override
	public void publish(EventObject event) {
		if (publishInternal.get()) {
			super.publish(event);
		} else {
			threadPool.execute(new PublishEventTask(event));
		}
	}

	/**
	 * Publish Event Task is a trigger of internal publishEvent method. It will
	 * be called asynchronously from {@link AsynchronousEventManager#threadPool}
	 * .
	 * 
	 * <b>Notice</b> since event processed asynchronously, observer
	 * should not rely on state of any object passed by event object. 
	 * If state is important, then event object provide a snapshot.
	 * 
	 * @author Andriy Vyedyeneyev (andriy.vyedyeneyev@github.com)
	 * 
	 */
	public class PublishEventTask implements Runnable {
		private EventObject _eventObject;

		/**
		 * construct task with event
		 * 
		 * @param event
		 */
		public PublishEventTask(EventObject event) {
			_eventObject = event;
		}

		/**
		 * do the publishEvent task
		 */
		@Override
		public void run() {
			try {
				// trigger internal publish
				publishInternal.set(true);
				AsynchronousEventManager.this.publish(_eventObject);
			} catch (Exception e) {
				_logger.error("failed while processing event publish", e);
			} finally {
				publishInternal.set(false);
			}
		}
	}
}
