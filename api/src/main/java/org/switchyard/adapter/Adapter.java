package org.switchyard.adapter;

import org.switchyard.metadata.ServiceInterface;
import org.switchyard.metadata.ServiceOperation;

public interface Adapter {
	
	public Adapter setServiceInterface(ServiceInterface serviceInterface);
	
	public ServiceInterface getServiceInterface();
	
	public ServiceOperation lookup(String consumerOperation, ServiceInterface targetInterface);
}
