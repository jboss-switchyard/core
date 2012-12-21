package org.switchyard.runtime.event.monitoring;

import org.switchyard.Exchange;

/**
 * InPhase event signals start of message processing, this should be at time
 * just before service handler will be called
 * 
 * @author Andriy Vyedyeneyev (andriy.vyedyeneyev@github.com)
 * 
 */
public class ExchangeStartEvent extends MonitoringEvent {

	public ExchangeStartEvent(Exchange exchange) {
		super(exchange);
	}

	/**
	 * serial
	 */
	private static final long serialVersionUID = -5227451146828928894L;
}
