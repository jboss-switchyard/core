package org.switchyard.runtime.event;

import java.util.Date;

import org.switchyard.Exchange;

/**
 * snapshot used to freeze current state of the exchange, all data in exchange
 * is immutable
 * 
 * @author Andriy Vyedyeneyev (andriy.vyedyeneyev@github.com)
 * 
 */
public interface ExchangeSnapshot extends Exchange {

	/**
	 * get timestamp of snapshot creation
	 * 
	 * @return
	 */
	Date getCreatedAt();

	/**
	 * get bytes of the message
	 * 
	 * @return
	 */
	byte[] getMessageBytes();
}
