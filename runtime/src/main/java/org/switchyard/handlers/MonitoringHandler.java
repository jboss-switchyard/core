package org.switchyard.handlers;

import org.switchyard.Exchange;
import org.switchyard.ExchangeHandler;
import org.switchyard.ExchangePhase;
import org.switchyard.HandlerException;
import org.switchyard.event.EventPublisher;
import org.switchyard.runtime.event.ExchangeInPhaseEvent;
import org.switchyard.runtime.event.MonitoringEvent;

/**
 * Integration of Monitoring Agent into Handler chain
 * 
 * @author Andriy Vyedyeneyev (andriy.vyedyneeyev@github.com)
 *
 */
public class MonitoringHandler implements ExchangeHandler {

	EventPublisher _eventPublisher;
	
	public MonitoringHandler(EventPublisher eventPublisher) {
		_eventPublisher = eventPublisher;
	}

	@Override
	public void handleMessage(Exchange exchange) throws HandlerException {
		
		if(ExchangePhase.IN.equals(exchange.getPhase())) {
			// generate IN_EVENT
			publishEvent(new ExchangeInPhaseEvent(exchange));
		}
		else if(ExchangePhase.OUT.equals(exchange.getPhase())) {
			// generate OUT_EVENT
		}
		
	}

	@Override
	public void handleFault(Exchange exchange) {
		// generate FAULT_EVENT
	}
	
	private void publishEvent(MonitoringEvent e) {
		_eventPublisher.publish(e);
	}
}
