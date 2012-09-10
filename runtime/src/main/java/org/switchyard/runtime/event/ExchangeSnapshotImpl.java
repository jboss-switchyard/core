package org.switchyard.runtime.event;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.switchyard.Context;
import org.switchyard.Exchange;
import org.switchyard.ExchangePhase;
import org.switchyard.ExchangeState;
import org.switchyard.Message;
import org.switchyard.internal.DefaultContext;
import org.switchyard.internal.DefaultMessage;
import org.switchyard.internal.io.Serializer;
import org.switchyard.metadata.ExchangeContract;

/**
 * Snapshot of Exchange, used to save state of exchange at some point of time
 * 
 * @author Andriy Vyedyeneyev (andriy.vyedyeneyev@github.com)
 * 
 */
public class ExchangeSnapshotImpl implements Externalizable, ExchangeSnapshot {

	private static Logger _log = Logger.getLogger(ExchangeSnapshotImpl.class);

	private long createdAt;
	private Context _context;
	private ExchangeContract _contract;
	private QName _serviceName;
	private Message _message;
	private ExchangeState _state;
	private ExchangePhase _phase;
	private Serializer _serializer;

	private Class<? extends Message> _messageClass;
	private Class<? extends Context> _contextClass;

	private byte[] _messageBytes;
	private byte[] _contextBytes;

	
	public ExchangeSnapshotImpl(Serializer serializer, Exchange exchange) {
		createdAt = System.currentTimeMillis();

		_serializer = serializer;
		
		
		// set parts which unusual to change 
		setContract(exchange.getContract());
		setServiceName(exchange.getServiceName());
		setPhase(exchange.getPhase());
		setState(exchange.getState());

		// save original of the
		setOriginalContext(exchange.getContext());
		setOriginalMessage(exchange.getMessage());
	}

	@Override
	public void readExternal(ObjectInput input) throws IOException,
			ClassNotFoundException {
	}

	@Override
	public void writeExternal(ObjectOutput output) throws IOException {
	}

	@Override
	public Context getContext() {
		if (_context == null) {
			_context = restoreContext();
		}

		return _context;
	}

	/**
	 * restore context from bytes
	 * @return
	 */
	private Context restoreContext() {
		Context restoredContext = null;
		if (_contextBytes != null) {
			try {
				restoredContext = _serializer
						.deserialize(_contextBytes, _contextClass);
			} catch (IOException e) {
				_log.error("Failed to restore message", e);
			}
		}

		if (restoredContext == null) {
			restoredContext = new DefaultContext();
		}

		return restoredContext;
	}

	/**
	 * set context
	 * 
	 * @param _context
	 */
	public <E extends Context> void setOriginalContext(Context _context) {
		if (_serializer != null) {
			try {
				_contextBytes = _serializer.serialize((E) _context,
						(Class<E>) _context.getClass());
				_contextClass = _context.getClass();
			} catch (IOException e) {
				_log.error("Failed to serialize context");
			}
		}
	}

	@Override
	public ExchangeContract getContract() {
		return _contract;
	}

	/**
	 * set contract
	 * 
	 * @param _contract
	 */
	public void setContract(ExchangeContract _contract) {
		this._contract = _contract;
	}

	@Override
	public QName getServiceName() {
		return _serviceName;
	}

	/**
	 * set service name
	 * 
	 * @param _serviceName
	 */
	public void setServiceName(QName _serviceName) {
		this._serviceName = _serviceName;
	}

	@Override
	public Message getMessage() {
		if (_message == null) {
			_message = restoreMessage();
		}
		return _message;
	}

	/**
	 * restore original of the message
	 * 
	 * @return copy of original message, or empty message
	 */
	private Message restoreMessage() {
		Message restoredMessage = null;
		if (_messageBytes != null) {
			try {
				restoredMessage = _serializer
						.deserialize(_messageBytes, _messageClass);
			} catch (IOException e) {
				_log.error("Failed to restore message", e);
			}
		}

		if (restoredMessage == null) {
			restoredMessage = new DefaultMessage();
		}

		return restoredMessage;
	}

	/**
	 * not supported by snapshot
	 */
	@Override
	public Message createMessage() {
		throw new UnsupportedOperationException();
	}

	/**
	 * not supported by snapshot
	 */
	@Override
	public void send(Message message) {
		throw new UnsupportedOperationException();
	}

	/**
	 * not supported by snapshot
	 */
	@Override
	public void sendFault(Message message) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ExchangeState getState() {
		return _state;
	}

	/**
	 * set state
	 * 
	 * @param _state
	 */
	public void setState(ExchangeState _state) {
		this._state = _state;
	}

	@Override
	public ExchangePhase getPhase() {
		return _phase;
	}

	/**
	 * set phase
	 * 
	 * @param _phase
	 */
	public void setPhase(ExchangePhase _phase) {
		this._phase = _phase;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getCreatedAt() {
		return new Date(createdAt);
	}

	/**
	 * set original message
	 * 
	 * @param message
	 */
	public <E extends Message> void setOriginalMessage(E message) {
		if (_serializer != null) {
			try {
				_messageBytes = _serializer.serialize((E) message,
						(Class<E>) message.getClass());
				_messageClass = (Class<E>) message.getClass();
			} catch (IOException e) {
				_log.error("Failed to serialize message", e);
			}
		}
	}

	/**
	 * get bytes of the message
	 * 
	 */
	public byte[] getMessageBytes() {
		return _messageBytes;
	}
}
