package org.switchyard.runtime.event.monitoring;

import org.switchyard.Context;
import org.switchyard.Exchange;
import org.switchyard.ExchangePattern;
import org.switchyard.ExchangePhase;
import org.switchyard.ExchangeState;
import org.switchyard.Property;
import org.switchyard.Scope;

/**
 * Factory to create monitoring events
 * 
 * @author Andriy Vyedyeneyev
 *
 */
public class MonitoringEventFactory {

	/**
	 * create monitoring event
	 *  
	 * @param exchange
	 * @return
	 */
	public MonitoringEvent createEvent(Exchange exchange) {
		return createEvent(exchange, false);
	}
	
	/**
	 * create monitoring event
	 * 
	 * @param exchange
	 * @param requestPhase true if event generated during request phase
	 * 
	 * @return
	 */
	public MonitoringEvent createEvent(Exchange exchange, boolean requestPhase) {
		checkExchangeTransaction(exchange);

		MonitoringEvent event = null;

		if (ExchangePhase.IN.equals(exchange.getPhase())) {
			
			if(requestPhase){
				event = new ExchangeStartEvent(exchange);
			}
			else {
				ExchangePattern mep = exchange.getContract().getConsumerOperation()
						.getExchangePattern();
				if(ExchangePattern.IN_ONLY.equals(mep)) {
					// done
					event = new ExchangeFinishEvent(exchange);
				}
			}
		} else {
			if (ExchangeState.OK.equals(exchange.getState())) {
				event = new ExchangeFinishEvent(exchange);
			} else {
				event = new ExchangeFaultEvent(exchange);
			}
		}

		return event;
	}

	/**
	 * Check transction attributes of exchange, and if required assign new one
	 * to it. The transaction is a base concept of the monitoring, and is
	 * required for further processing of {@link MonitoringEvent}
	 * 
	 * @param exchange
	 */
	private void checkExchangeTransaction(Exchange exchange) {
		// new transaction detected -> set transaction id = message ID
		Context context = exchange.getContext();
		Property transactionID = context.getProperty(MonitoringEvent.TX_ID,
				Scope.EXCHANGE);

		if (transactionID == null || transactionID.getValue() == null) {
			Property messageID = context.getProperty(Exchange.MESSAGE_ID,
					Scope.activeScope(exchange));
			exchange.getContext().setProperty(MonitoringEvent.TX_ID, messageID);
		}
	}
}
