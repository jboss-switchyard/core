package org.switchyard.test.mixins;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.qpid.client.AMQConnectionFactory;
import org.apache.qpid.client.AMQDestination;
import org.apache.qpid.server.Broker;
import org.apache.qpid.server.BrokerOptions;
import org.switchyard.deploy.internal.AbstractDeployment;
import org.switchyard.exception.SwitchYardException;
import org.switchyard.test.MixInDependencies;

/**
 * AMQMixIn helps developers test our AMQP Bindings on SwitchYard applications.
 *
 * The following files must be present in the classpath:
 *
 * - amqp.properties file is used to define the following parameters:
 *      - qpidConnectionfactory: connection factory url (e.g. qpidConnectionfactory = amqp://guest:guest@/test?brokerlist='tcp://localhost:5672')
 *      - queueExchange: queue exchange to be used as a destination (e.g. BURL:direct://amq.direct//ping?routingkey='#')
 *      - topicExchange: topic exchange to be used as a destination (e.g. BURL:topic://amq.topic//MyTopic?routingkey='#')
 *
 * - config.xml file used for Apache QPID configuration.
 * - virtualhosts.xml file used for Apache QPID virtual host configurations.
 * - log4j.xml file used for Apache Log4J configuration.
 * - passwd file used for Apache QPID user credentials.
 *
 * @author: <a href="mailto:eduardo.devera@gmail.com">Eduardo de Vera</a>
 * Date: 11/11/12
 * Time: 7:22 PM
 */
@MixInDependencies(required = {CDIMixIn.class, NamingMixIn.class})
public class AMQPMixIn extends AbstractTestMixIn {

    public static final String JNDI_CONNECTION_FACTORY = "java:jboss/qpidConnectionFactory";
    public static final String JNDI_QUEUE_EXCHANGE = "java:jboss/queueExchange";
    public static final String JNDI_TOPIC_EXCHANGE = "java:jboss/topicExchange";

    //amp.properties located in the classpath of the running Thread
    private static final String AMQP_PROPERTIES_FILE = "/amqp.properties";
    private static final String LOG_XML = "/log4j.xml";
    private static final String CONFIG_XML = "/config.xml";
    private static final String QPID_WORK_DIRECTORY = "/qpid";
    private static final String SYS_PROP_QPID_HOME = "QPID_HOME";
    private static final String SYS_PROP_QPID_WORK = "QPID_WORK";
    protected static final String AMQP_PROPS_QPID_CONNECTIONFACTORY = "qpidConnectionfactory";
    protected static final String AMQP_PROPS_QUEUE_EXCHANGE = "queueExchange";
    protected static final String AMPQ_PROPS_TOPIC_EXCHANGE = "topicExchange";

    private Broker broker;

    private Properties properties;
    private String qpidConnectionFactory;
    private String qpidQueueExchange;
    private String qpidTopicExchange;

    public AMQPMixIn() throws NamingException, IOException {
        properties = new Properties();
        properties.load(this.getClass().getResourceAsStream(AMQP_PROPERTIES_FILE));
        qpidConnectionFactory = properties.getProperty(AMQP_PROPS_QPID_CONNECTIONFACTORY);
        if (qpidConnectionFactory == null) {
            throw new SwitchYardException("No connection factory configured. Please set "+AMQP_PROPS_QPID_CONNECTIONFACTORY+
                    " in the "+AMQP_PROPERTIES_FILE+" file found in the class path of your application");
        }

        qpidQueueExchange = properties.getProperty(AMQP_PROPS_QUEUE_EXCHANGE);
        qpidTopicExchange = properties.getProperty(AMPQ_PROPS_TOPIC_EXCHANGE);

        if ((qpidQueueExchange == null) && (qpidTopicExchange == null)) {
            throw new SwitchYardException("No topic or queue configured. Please set either one by using "+
                    AMQP_PROPS_QUEUE_EXCHANGE +" or "+AMPQ_PROPS_TOPIC_EXCHANGE+" in your "+AMQP_PROPERTIES_FILE+" properties file.");
        }
    }

    public Context getContext() throws NamingException, IOException {
        return new InitialContext();
    }

    public ConnectionFactory getConnectionFactory(Context context) throws NamingException {
        ConnectionFactory _connectionFactory = (ConnectionFactory) context.lookup(JNDI_CONNECTION_FACTORY);
        return _connectionFactory;
    }

    @Override
    public void before(AbstractDeployment deployment) {
        super.before(deployment);
    }

    @Override
    public void after(AbstractDeployment deployment) {
        super.after(deployment);
    }

    @Override
    public void initialize() {
        super.initialize();
        File file = new File(this.getClass().getResource("/").getFile());
        BrokerOptions options = new BrokerOptions();
        System.setProperty(SYS_PROP_QPID_HOME, file.getPath());
        System.setProperty(SYS_PROP_QPID_WORK, file.getParent() + QPID_WORK_DIRECTORY);
        final String configFile = this.getClass().getResource(CONFIG_XML).getFile();
        options.setConfigFile(configFile);
        final String logFile = this.getClass().getResource(LOG_XML).getFile();
        options.setLogConfigFile(logFile);
        broker = new Broker();
        try {
            broker.startup(options);
            Context context = new InitialContext();
            context.bind(JNDI_CONNECTION_FACTORY, new AMQConnectionFactory(qpidConnectionFactory));
            if (qpidQueueExchange != null) {
                context.bind(JNDI_QUEUE_EXCHANGE, AMQDestination.createDestination(qpidQueueExchange));
            }
            if (qpidTopicExchange != null) {
                context.bind(JNDI_TOPIC_EXCHANGE, AMQDestination.createDestination(qpidTopicExchange));
            }
        } catch (Exception e) {
            throw new SwitchYardException(e);
        }
    }

    @Override
    public void uninitialize() {
        super.uninitialize();
        broker.shutdown();
    }
}
