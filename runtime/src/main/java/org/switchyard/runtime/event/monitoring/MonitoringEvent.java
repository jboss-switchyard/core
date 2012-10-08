package org.switchyard.runtime.event.monitoring;

import java.util.EventObject;

import org.switchyard.Exchange;
import org.switchyard.Property;
import org.switchyard.Scope;
import org.switchyard.internal.monitoring.ExchangeSnapshot;
import org.switchyard.internal.monitoring.ExchangeSnapshotFactory;

/**
 * Base class for all monitoring agent events
 * 
 * @author Andriy Vyedyeneyev (andriy.vyedyeneyev@github.com)
 * 
 */
public abstract class MonitoringEvent extends EventObject {

	public static final String TX_ID = "Switchyard.Transaction.ID";

	private Property _txID = null;

	/**
	 * serial version
	 */
	private static final long serialVersionUID = -8728867699242618330L;

	public MonitoringEvent(Exchange exchange) {
		super(ExchangeSnapshotFactory.getInstance().createSnapshot(exchange));

		_txID = exchange.getContext().getProperty(TX_ID, Scope.EXCHANGE);
	}

	/**
	 * Get transaction ID
	 * 
	 * @return
	 */
	public String getTransactionID() {
		return _txID != null ? (String) _txID.getValue() : null;
	}

	/**
	 * restore exchange
	 * 
	 * @return
	 */
	public ExchangeSnapshot getExchange() {
		return (ExchangeSnapshot) getSource();
	}
}
