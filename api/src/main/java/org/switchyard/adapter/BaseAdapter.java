package org.switchyard.adapter;

import org.switchyard.metadata.ServiceInterface;

public abstract class BaseAdapter implements Adapter {
	private ServiceInterface _serviceInterface;

	public Adapter setServiceInterface(ServiceInterface serviceInterface) {
		_serviceInterface = serviceInterface;
		return this;
	}
	
	public ServiceInterface getServiceInterface() {
		return _serviceInterface;
	}
}
