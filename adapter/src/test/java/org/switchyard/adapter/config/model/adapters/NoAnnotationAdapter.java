package org.switchyard.adapter.config.model.adapters;

import org.switchyard.ExchangePattern;
import org.switchyard.metadata.BaseServiceOperation;
import org.switchyard.metadata.ServiceInterface;
import org.switchyard.metadata.ServiceOperation;
import org.switchyard.metadata.java.JavaService;

public class NoAnnotationAdapter {
	public ServiceOperation lookup(String consumerOperation, ServiceInterface targetInterface) {
		return new BaseServiceOperation(ExchangePattern.IN_ONLY, "test", JavaService.toMessageType(Void.class), JavaService.toMessageType(String.class), null);
	}
}
