package org.switchyard.runtime.event;

import org.switchyard.Exchange;
import org.switchyard.Property;
import org.switchyard.Scope;

/**
 * InPhase event signals start of message processing, this should be at time
 * just before service handler will be called
 * 
 * @author Andriy Vyedyeneyev (andriy.vyedyeneyev@github.com)
 * 
 */
public class ExchangeInPhaseEvent extends MonitoringEvent {

	public ExchangeInPhaseEvent(Exchange exchange) {
		super(exchange);
	}

	/**
	 * serial
	 */
	private static final long serialVersionUID = -5227451146828928894L;
}
