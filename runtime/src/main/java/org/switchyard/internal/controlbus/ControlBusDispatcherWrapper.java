package org.switchyard.internal.controlbus;

import org.switchyard.Exchange;
import org.switchyard.Service;
import org.switchyard.spi.Dispatcher;

public class ControlBusDispatcherWrapper implements Dispatcher {

	private Dispatcher _dispatcher;
	
	public ControlBusDispatcherWrapper(Dispatcher dispatcher) {
		_dispatcher = dispatcher;
	}

	@Override
	public Service getService() {
		return _dispatcher.getService();
	}

	@Override
	public void dispatch(Exchange exchange) {
		_dispatcher.dispatch(exchange);
	}
}
