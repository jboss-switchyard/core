package org.switchyard.test.mixins;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.NamingException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.switchyard.exception.SwitchYardException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author: <a href="mailto:eduardo.devera@gmail.com">Eduardo de Vera</a>
 * Date: 11/12/12
 * Time: 11:07 PM
 */
public class AMQPMixInTest {

    private NamingMixIn _namingMixIn;
    private AMQPMixIn _mixIn;

    private Context _context;
    private ConnectionFactory _connectionFactory;
    private Connection _connection;

    @Before
    public void setUp() {
        try {
            _namingMixIn = new NamingMixIn();
            _mixIn = new AMQPMixIn();
            _namingMixIn.initialize();
            _mixIn.initialize();
            _context = _mixIn.getContext();
            _connectionFactory = _mixIn.getConnectionFactory(_context);
            _connection = _connectionFactory.createConnection();
            _connection.start();
        } catch (Exception e) {
            throw new SwitchYardException(e);
        }
    }

    @After
    public void tearDown() {
        try {
            _connection.close();
            _context.close();
            _mixIn.uninitialize();
            _namingMixIn.uninitialize();
        } catch (Exception e) {
            throw new SwitchYardException(e);
        }
    }

    @Test
    public void testConnection() {
        assertThat(_connectionFactory, is(notNullValue()));
        assertThat(_connection, is(notNullValue()));
    }

    @Test
    public void testSendMessageToQueue() throws NamingException, IOException, JMSException {
        sendMessage(AMQPMixIn.JNDI_QUEUE_EXCHANGE, "Hello queued world!");
    }

    @Test
    public void testSendMessageToTopic() throws NamingException, IOException, JMSException {
        sendMessage(AMQPMixIn.JNDI_TOPIC_EXCHANGE, "Hello topical world!");
    }

    private void sendMessage(String destinationName, String messageText) throws JMSException, NamingException {
        Session session = _connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = (Destination) _context.lookup(destinationName);

        MessageProducer messageProducer = session.createProducer(destination);
        MessageConsumer messageConsumer = session.createConsumer(destination);

        TextMessage message = session.createTextMessage(messageText);
        messageProducer.send(message);

        message = (TextMessage)messageConsumer.receive();
        assertThat(message.getText(), is(equalTo(messageText)));
        System.out.println(message.getText());
    }
}
