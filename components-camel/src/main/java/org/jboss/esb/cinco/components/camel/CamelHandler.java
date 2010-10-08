package org.jboss.esb.cinco.components.camel;

import javax.xml.namespace.QName;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.jboss.esb.cinco.BaseHandler;
import org.jboss.esb.cinco.HandlerException;
import org.jboss.esb.cinco.event.ExchangeInEvent;
import org.jboss.esb.cinco.event.ExchangeOutEvent;

public class CamelHandler extends BaseHandler
{
    public static final String SERVICE_NAME = "camel";
    private ProducerTemplate producerTemplate;

    public CamelHandler(CamelContext context)
    {
        producerTemplate = context.createProducerTemplate();
    }

    @Override
    public void exchangeIn(ExchangeInEvent event) throws HandlerException
    {
        QName service = event.getExchange().getService();
        String camelTo = service.getLocalPart();
        producerTemplate.sendBody(camelTo, event.getExchange().getMessage().getContent());
    }

    @Override
    public void exchangeOut(ExchangeOutEvent event) throws HandlerException
    {
        super.exchangeOut(event);
    }
    
    public static QName createCamelServiceName(final String to)
    {
        return new QName(SERVICE_NAME, to);
    }

}
