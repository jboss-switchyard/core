package org.switchyard.internal;

import java.util.EventObject;

import org.switchyard.Context;
import org.switchyard.Exchange;
import org.switchyard.Property;
import org.switchyard.Scope;
import org.switchyard.event.EventObserver;
import org.switchyard.runtime.event.MonitoringEvent;

public class MonitoringEventObserver implements EventObserver {

	@Override
	public void notify(EventObject event) {
		Exchange exchange = ((MonitoringEvent)event).getExchange();
		
		Context context = exchange.getContext();
		
		Property transactionID = context.getProperty("Switchyard.Transaction.ID", Scope.EXCHANGE);

		if(transactionID == null) {
			
		}
	}

}
