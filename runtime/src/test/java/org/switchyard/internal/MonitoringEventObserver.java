package org.switchyard.internal;

import java.util.EventObject;

import org.switchyard.event.EventObserver;
import org.switchyard.internal.monitoring.ExchangeSnapshot;
import org.switchyard.runtime.event.monitoring.ExchangeFaultEvent;
import org.switchyard.runtime.event.monitoring.ExchangeFinishEvent;
import org.switchyard.runtime.event.monitoring.ExchangeStartEvent;
import org.switchyard.runtime.event.monitoring.MonitoringEvent;

public class MonitoringEventObserver implements EventObserver {

	@Override
	public void notify(EventObject event) {
		if (event instanceof MonitoringEvent) {
			MonitoringEvent monitoringEvent = (MonitoringEvent) event;
			String txID = monitoringEvent.getTransactionID();

			// DAO - > getTransaction(txID)
			if (event instanceof ExchangeStartEvent) {

				// dao create exchange call
				// get relates to
				// if (!null) add related info 

			} else if (event instanceof ExchangeFinishEvent) {
				// dao get exchange call
				// dao complete exchange call
			} else if (event instanceof ExchangeFaultEvent) {
				// dao get exchange call
				// dao add fault information
				// dao complete exchange call
			} else {
				// unknown
			}
		}
	}

}
