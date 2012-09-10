package org.switchyard.internal.controlbus;

import java.util.HashMap;
import java.util.Map;

import org.switchyard.ExchangeHandler;
import org.switchyard.Service;
import org.switchyard.ServiceDomain;
import org.switchyard.spi.Dispatcher;
import org.switchyard.spi.ExchangeBus;

public class ControlBusImpl implements ExchangeBus {

	private ExchangeBus _bus;
	private Map<Service, ControlBusDispatcherWrapper> _dispatcherMap = new HashMap<Service, ControlBusDispatcherWrapper>();

	public ControlBusImpl(ExchangeBus exchangeBus) {
		_bus = exchangeBus;
	}

	@Override
	public Dispatcher createDispatcher(Service service,
			ExchangeHandler serviceHandler) {
		Dispatcher serviceDispatcher = null;
		if ((serviceDispatcher = _dispatcherMap.get(service)) == null) {
			serviceDispatcher = new ControlBusDispatcherWrapper(
					_bus.createDispatcher(service, serviceHandler));
			_dispatcherMap.put(service,
					(ControlBusDispatcherWrapper) serviceDispatcher);
		} else {
			// TODO check if it's correct to ignore double create
		}

		return _dispatcherMap.get(service);
	}

	@Override
	public Dispatcher getDispatcher(Service service) {
		return _dispatcherMap.get(service.getName());
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
