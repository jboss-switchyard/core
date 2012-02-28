/*
 * JBoss, Home of Professional Open Source Copyright 2009, Red Hat Middleware
 * LLC, and individual contributors by the @authors tag. See the copyright.txt
 * in the distribution for a full listing of individual contributors.
 * 
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 */
package org.switchyard.test.mixins;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hornetq.api.core.HornetQBuffer;
import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;
import org.hornetq.api.core.client.ClientSession;
import org.hornetq.api.core.client.ClientSessionFactory;
import org.hornetq.api.core.client.HornetQClient;
import org.hornetq.api.core.client.ServerLocator;
import org.hornetq.core.config.Configuration;
import org.hornetq.core.registry.JndiBindingRegistry;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.core.remoting.impl.netty.TransportConstants;
import org.hornetq.jms.server.embedded.EmbeddedJMS;

/**
 * HornetQ Test Mix In.
 * 
 * @author Daniel Bevenius
 */
public class HornetQMixIn extends AbstractTestMixIn {
    
    private static final String HORNETQ_CONF_FILE = "hornetq-configuration.xml";
    private static final String HORNETQ_JMS_CONF_FILE = "hornetq-jms.xml";

    private Logger _logger = Logger.getLogger(HornetQMixIn.class);
    private boolean _startEmbedded;
    private String _host = TransportConstants.DEFAULT_HOST;
    private int _port = TransportConstants.DEFAULT_PORT;
    private String _user = null;
    private String _passwd = null; 
    private EmbeddedJMS _embeddedJMS;
    private ServerLocator _serverLocator;
    private ClientSessionFactory _clientSessionFactory;
    private ClientSession _clientSession;

    /**
     * Default constructor.
     */
    public HornetQMixIn() {
        this(true);
    }
    
    /**
     * Constructor.
     * @param embedded true if you want to start embedded HornetQ server,
     * or false if you want to connect to remote HornetQ server.
     */
    public HornetQMixIn(boolean embedded) {
        _startEmbedded = embedded;
    }
    
    @Override
    public void initialize() {
        if (_startEmbedded) {
            _embeddedJMS = new EmbeddedJMS();
            _embeddedJMS.setConfigResourcePath(HORNETQ_CONF_FILE);
            _embeddedJMS.setJmsConfigResourcePath(HORNETQ_JMS_CONF_FILE);
            try {
                _embeddedJMS.setRegistry(new JndiBindingRegistry());
                _embeddedJMS.start();
                _serverLocator = HornetQClient.createServerLocatorWithoutHA(getTransports(getConfiguration()));
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            Map<String, Object> params = new HashMap<String,Object>();
            params.put(TransportConstants.HOST_PROP_NAME, _host);
            params.put(TransportConstants.PORT_PROP_NAME, _port);
            _serverLocator = HornetQClient.createServerLocatorWithoutHA(new TransportConfiguration(NettyConnectorFactory.class.getName(), params));
        }
        
        try {
            _clientSessionFactory = _serverLocator.createSessionFactory();
            _clientSession = _clientSessionFactory.createSession(
                    _user,
                    _passwd,
                    false,
                    true,
                    true,
                    _serverLocator.isPreAcknowledge(),
                    _serverLocator.getAckBatchSize());
            _clientSession.start();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Set host address to connect.
     * @param host address
     * @return this instance
     */
    public HornetQMixIn setHost(String host) {
        _host = host;
        return this;
    }
    
    /**
     * Set port number to connect.
     * @param port port number
     * @return this instance
     */
    public HornetQMixIn setPort(int port) {
        _port = port;
        return this;
    }
    
    /**
     * Set user to connect.
     * @param user user
     * @return this instance
     */
    public HornetQMixIn setUser(String user) {
        _user = user;
        return this;
    }
    
    /**
     * Set password to connect.
     * @param passwd password
     * @return this instance
     */
    public HornetQMixIn setPassword(String passwd) {
        _passwd = passwd;
        return this;
    }
    
    /**
     * Gets the HornetQ {@link Configuration}.
     * 
     * @return Configuration the HornetQ configuration used for this Embedded Server.
     * Return null if this HornetQMixIn instance is initialized for remote HornetQ Server.
     */
    public Configuration getConfiguration() {
        if (_embeddedJMS == null) {
            return null;
        }

        return _embeddedJMS.getHornetQServer().getConfiguration();
    }
    
    private static TransportConfiguration[] getTransports(final Configuration from) {
        final Collection<TransportConfiguration> transports = from.getConnectorConfigurations().values();
        return transports.toArray(new TransportConfiguration[]{});
    }

    @Override
    public void uninitialize() {
        try {
            HornetQMixIn.closeSessionFactory(_clientSessionFactory);
            HornetQMixIn.closeSession(_clientSession);
            HornetQMixIn.closeServerLocator(_serverLocator);
            
            if (_embeddedJMS != null) {
                _embeddedJMS.stop();
            }
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * By giving tests access to the {@link ClientSession} enables test to be able to
     * create queues, consumers, and producers.
     * 
     * @return {@link ClientSession} which can be used to create queues/topics, consumers/producers.
     */
    public ClientSession getClientSession() {
        if (_clientSession != null) {
            return _clientSession;
        }
        throw new IllegalStateException("The ClientSession has not been created. Please check the logs for errors.");
    }
    
    /**
     * Close the existing ClientSession if exists and create new one.
     * @return ClientSession instance
     */
    public ClientSession createClientSession() {
        closeSession(_clientSession);
        try {
            _clientSession = _clientSessionFactory.createSession(
                    _user,
                    _passwd,
                    false,
                    true,
                    true,
                    _serverLocator.isPreAcknowledge(),
                    _serverLocator.getAckBatchSize());
            _clientSession.start();
            return _clientSession;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Returns the {@link ServerLocator} used by this MixIn.
     * 
     * @return {@link ServerLocator} used by this MixIn.
     */
    public ServerLocator getServerLocator() {
        return _serverLocator;
    }
    
    /**
     * Creates a HornetQ {@link ClientMessage} with the passed-in String as the body.
     * 
     * @param body the String to be used as the messages payload(body)
     * @return ClientMessage with the passed-in String as its payload.
     */
    public ClientMessage createMessage(final String body) {
        final ClientMessage message = _clientSession.createMessage(true);
        message.getBodyBuffer().writeBytes(body.getBytes());
        return message;
    }
    
    /**
     * Reads the body of a HornetQ {@link ClientMessage} as an Object.
     * 
     * @param msg the HornetQ {@link ClientMessage}.
     * @return Object the object read.
     * @throws Exception if an error occurs while trying to read the body content.
     */
    public Object readObjectFromMessage(final ClientMessage msg) throws Exception {
        byte[] bytes = new byte[msg.getBodySize()];
        final HornetQBuffer bodyBuffer = msg.getBodyBuffer();
        bodyBuffer.readBytes(bytes);

        Object result = null;
        try {
            final ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
            result = in.readObject();
        } catch (Exception e) {
            _logger.warn("Caught an Exception during deserializing object. Then trying to read as String");
            _logger.debug("",e);
            result = new String(bytes);
        }
        
        return result;
    }

    /**
     * Closes the passed-in {@link ServerLocator}.
     * 
     * @param serverLocator the {@link ServerLocator} instance to close.
     */
    public static void closeServerLocator(final ServerLocator serverLocator) {
        if (serverLocator != null) {
            serverLocator.close();
        }
    }
    
    /**
     * Closes the passed-in {@link ClientSessionFactory}.
     * 
     * @param factory the {@link ClientSessionFactory} instance to close.
     */
    public static void closeSessionFactory(final ClientSessionFactory factory) {
        if (factory != null) {
            factory.close();
        }
    }
    
    /**
     * Closes the passed-in {@link ClientSession}.
     * 
     * @param session the {@link ClientSession} instance to close.
     */
    public static void closeSession(final ClientSession session) {
        if (session != null) {
            try {
                session.close();
            } catch (final HornetQException ignore) {
                ignore.printStackTrace();
            }
        }
    }
    
    /**
     * Closes the passed-in {@link ClientConsumer}.
     * 
     * @param consumer the {@link ClientConsumer} instance to close.
     */
    public static void closeClientConsumer(final ClientConsumer consumer) {
        if (consumer != null) {
            try {
                consumer.close();
            } catch (final HornetQException ignore) {
                ignore.printStackTrace();
            }
        }
    }
    
    /**
     * Closes the passed-in {@link ClientProducer}.
     * 
     * @param producer the {@link ClientProducer} to close.
     */
    public static void closeClientProducer(final ClientProducer producer) {
        if (producer != null) {
            try {
                producer.close();
            } catch (final HornetQException ignore) {
                ignore.printStackTrace();
            }
        }
    }

}
