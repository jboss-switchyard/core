package org.switchyard.runtime.event;

import java.util.EventObject;

import org.switchyard.Exchange;

/**
 * Base class for all monitoring agent events
 * 
 * @author Andriy Vyedyeneyev (andriy.vyedyeneyev@github.com)
 * 
 */
public abstract class MonitoringEvent extends EventObject {

	/**
	 * serial version
	 */
	private static final long serialVersionUID = -8728867699242618330L;

	public MonitoringEvent(Exchange exchange) {
		super(ExchangeSnapshotFactory.getInstance().createSnapshot(exchange));
	}

	/**
	 * restore exchange
	 * 
	 * @return
	 */
	public Exchange getExchange() {
		return (Exchange) getSource();
	}
}
