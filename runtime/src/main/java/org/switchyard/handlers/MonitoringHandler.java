package org.switchyard.handlers;

import org.switchyard.Exchange;
import org.switchyard.ExchangeHandler;
import org.switchyard.HandlerException;
import org.switchyard.event.EventPublisher;
import org.switchyard.runtime.event.monitoring.MonitoringEvent;
import org.switchyard.runtime.event.monitoring.MonitoringEventFactory;

/**
 * Integration of Monitoring Agent into Handler chain
 * 
 * @author Andriy Vyedyeneyev (andriy.vyedyneeyev@github.com)
 * 
 */
public class MonitoringHandler implements ExchangeHandler {

	private EventPublisher _eventPublisher;
	private boolean _requestChain = false;

	public MonitoringHandler(EventPublisher eventPublisher) {
		this(eventPublisher, false);
	}
	
	public MonitoringHandler(EventPublisher eventPublisher, boolean requestChain) {
		_eventPublisher = eventPublisher;
		_requestChain = requestChain;
	}

	@Override
	public void handleMessage(Exchange exchange) throws HandlerException {
		publishEvent(new MonitoringEventFactory().createEvent(exchange));
	}

	@Override
	public void handleFault(Exchange exchange) {
		publishEvent(new MonitoringEventFactory().createEvent(exchange));
	}

	private void publishEvent(MonitoringEvent e) {
		_eventPublisher.publish(e);
	}
	
	public boolean isRequestChain() {
		return _requestChain;
	}
}
