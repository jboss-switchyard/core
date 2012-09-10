package org.switchyard.runtime.event;

import org.switchyard.Exchange;
import org.switchyard.internal.io.SerializerType;

/**
 * Factory to create
 * 
 * @author Andriy Vyedyeneyev (andriy.vyedyeneyev@github.com)
 * 
 */
public class ExchangeSnapshotFactory {

	protected ExchangeSnapshotFactory() {
	}

	/**
	 * get instance of ExchangeSnapshotFactory class, configuration over
	 * META-INF/ExchangeSnapshotFactory
	 * 
	 * @return
	 */
	public static ExchangeSnapshotFactory getInstance() {
		// TODO add configuration here
		return new ExchangeSnapshotFactory();
	}

	/**
	 * create snapshot of exchange
	 * 
	 * @param exchange
	 * @return
	 */
	public ExchangeSnapshot createSnapshot(Exchange exchange) {
		// take the fastest serializer
		return new ExchangeSnapshotImpl(
				SerializerType.ZIP_GRAPH_OBJECT_STREAM.instance(), exchange);
	}
}
