package org.switchyard.internal.controlbus;

import org.switchyard.Exchange;
import org.switchyard.ServiceReference;
import org.switchyard.spi.Dispatcher;

public class ControlBusDispatcherWrapper implements Dispatcher {

	private Dispatcher _dispatcher;
	
	public ControlBusDispatcherWrapper(Dispatcher dispatcher) {
		_dispatcher = dispatcher;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispatch(Exchange exchange) {
		_dispatcher.dispatch(exchange);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ServiceReference getServiceReference() {
		return _dispatcher.getServiceReference();
	}
}
