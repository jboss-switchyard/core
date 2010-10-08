package org.jboss.esb.cinco.components.camel;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import javax.xml.namespace.QName;

import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.jboss.esb.cinco.EsbException;
import org.jboss.esb.cinco.Exchange;
import org.jboss.esb.cinco.ExchangePattern;
import org.jboss.esb.cinco.Message;
import org.jboss.esb.cinco.MessageBuilder;
import org.jboss.esb.cinco.ServiceDomain;
import org.jboss.esb.cinco.internal.ServiceDomains;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link CamelHandler}.
 * 
 * @author Daniel Bevenius
 *
 */
public class CamelHandlerTest extends CamelTestSupport
{
    private QName serviceName = new QName(CamelHandler.SERVICE_NAME, "direct:to");
    private ServiceDomain domain;
    private CamelHandler camelHandler;
    @EndpointInject(uri = "mock:result")
    private MockEndpoint result;
    
    @Before
    public void registerService()
    {
		domain = ServiceDomains.getDomain();
        camelHandler = new CamelHandler(context);
        domain.registerService(serviceName, camelHandler);
    }

    @Test
    public void routeOneWayToCamel() throws EsbException
    {
		Exchange exchange = domain.createExchange(serviceName, ExchangePattern.IN_ONLY);
		Message message = MessageBuilder.newInstance().buildMessage();
		message.setContent("test string");
		
		exchange.sendIn(message);
		assertThat(result.getReceivedCounter(), is(1));
		String body = (String) result.getReceivedExchanges().get(0).getIn().getBody();
		assertThat(body, is(equalTo("test string")));
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception
    {
        return new RouteBuilder() {
            public void configure() {
                from("direct:to").to("mock:result");
            }
        };
    }

}
