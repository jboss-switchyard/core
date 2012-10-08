package org.switchyard.internal.controlbus;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.switchyard.ServiceDomain;
import org.switchyard.ServiceReference;
import org.switchyard.spi.Dispatcher;
import org.switchyard.spi.ExchangeBus;

public class ControlBusImpl implements ExchangeBus {

	private ExchangeBus _bus;
	private Map<QName, ControlBusDispatcherWrapper> _dispatcherMap = new HashMap<QName, ControlBusDispatcherWrapper>();

	public ControlBusImpl(ExchangeBus exchangeBus) {
		_bus = exchangeBus;
	}

	@Override
	public Dispatcher createDispatcher(ServiceReference serviceRef) {
		Dispatcher serviceDispatcher = null;
		if ((serviceDispatcher = _dispatcherMap.get(serviceRef)) == null) {
			serviceDispatcher = new ControlBusDispatcherWrapper(
					_bus.createDispatcher(serviceRef));
			_dispatcherMap.put(serviceRef.getName(),
					(ControlBusDispatcherWrapper) serviceDispatcher);
		} else {
			// TODO check if it's correct to ignore double create
		}

		return _dispatcherMap.get(serviceRef);
	}

	@Override
	public Dispatcher getDispatcher(ServiceReference serviceRef) {
		return _dispatcherMap.get(serviceRef.getName());
	}

	@Override
	public void init(ServiceDomain domain) {
		// TODO Auto-generated method stub
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
	}
}
