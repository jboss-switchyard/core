package org.switchyard.bus.camel;

import javax.xml.namespace.QName;

import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.DefaultMessage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.switchyard.Context;
import org.switchyard.Exchange;
import org.switchyard.ExchangePattern;
import org.switchyard.MockDomain;
import org.switchyard.Property;
import org.switchyard.Scope;
import org.switchyard.ServiceReference;
import org.switchyard.common.camel.SwitchYardCamelContextImpl;
import org.switchyard.internal.ServiceReferenceImpl;
import org.switchyard.label.BehaviorLabel;
import org.switchyard.metadata.InOnlyService;

public class CamelCompositeContextTest {

    private CamelExchangeBus _provider;
    private SwitchYardCamelContextImpl _camelContext;
    private MockDomain _domain;

    @Before
    public void setUp() throws Exception {
        _domain = new MockDomain();
        _camelContext = new SwitchYardCamelContextImpl();
        _camelContext.setServiceDomain(_domain);
        _provider = new CamelExchangeBus(_camelContext);
        _provider.init(_domain);
        _camelContext.start();
    }

    @After
    public void tearDown() throws Exception {
        _camelContext.stop();
    }

    @Test
    public void testCopyFromExchange() throws Exception {
        ServiceReference inOnly = new ServiceReferenceImpl(
            new QName("exchange-copy"), new InOnlyService(), _domain, null);
        ExchangeDispatcher dispatch = _provider.createDispatcher(inOnly);
        
        Exchange ex = dispatch.createExchange(null, ExchangePattern.IN_ONLY);
        Context ctx = ex.getContext();
        ctx.setProperty("message-prop", "message-val", Scope.MESSAGE);
        ctx.setProperty("exchange-prop", "exchange-val", Scope.EXCHANGE).addLabels(BehaviorLabel.TRANSIENT.label());
        Assert.assertEquals(ctx.getProperty("message-prop", Scope.MESSAGE).getValue(), "message-val");
        Assert.assertEquals(ctx.getProperty("exchange-prop", Scope.EXCHANGE).getValue(), "exchange-val");
        Assert.assertTrue(ctx.getProperty("exchange-prop", Scope.EXCHANGE).getLabels().contains(BehaviorLabel.TRANSIENT.label()));
        
        // Merge the context from ex into the context for ex2
        Exchange ex2 = dispatch.createExchange(null, ExchangePattern.IN_ONLY);
        Context ctx2 = ex2.getContext();
        ctx.mergeInto(ctx2);
        Assert.assertNotNull(ctx2.getProperty("message-prop", Scope.MESSAGE));
        Assert.assertNull(ctx2.getProperty("exchange-prop", Scope.EXCHANGE));
    }

    @Test
    public void testHeaderCaseSensitivityFromExchange() throws Exception {
        String lowerCaseHeaderKey = "lowercase_header";
        String lowerCaseHeaderValue = "lowercase_value";

        String mixedCaseHeaderKey = "mixedCASE_HEADER";
        String mixedCaseHeaderValue = "mixedCASE_VALUE";

        String upperCaseHeaderKey = "UPPERCASE_HEADER";
        String upperCaseHeaderValue = "UPPERCASE_VALUE";

        DefaultMessage message = new DefaultMessage();
        message.setHeader(lowerCaseHeaderKey, lowerCaseHeaderValue);
        message.setHeader(mixedCaseHeaderKey, mixedCaseHeaderValue);
        message.setHeader(upperCaseHeaderKey, upperCaseHeaderValue);

        CamelCompositeContext ctx = new CamelCompositeContext(new DefaultExchange(new SwitchYardCamelContextImpl(false)), message);

        boolean foundLowerCaseValue = false;
        boolean foundMixedCaseValue = false;
        boolean foundUpperCaseValue = false;

        for (Property p : ctx.getProperties(Scope.MESSAGE)) {
            if (lowerCaseHeaderKey.equals(p.getName())) {
                foundLowerCaseValue = true;
            }

            if (mixedCaseHeaderKey.equals(p.getName())) {
                foundMixedCaseValue = true;
            }

            if (upperCaseHeaderKey.equals(p.getName())) {
                foundUpperCaseValue = true;
            }
        }

        if (!foundLowerCaseValue || !foundMixedCaseValue || !foundUpperCaseValue) {
            StringBuilder failMessage = new StringBuilder();
            failMessage.append("Could not find MESSAGE-scoped properties: ");

            if (!foundLowerCaseValue) {
                failMessage.append(lowerCaseHeaderKey);
                failMessage.append(", ");
            }

            if (!foundMixedCaseValue) {
                failMessage.append(mixedCaseHeaderKey);
                failMessage.append(", ");
            }

            if (!foundUpperCaseValue) {
                failMessage.append(upperCaseHeaderKey);
                failMessage.append(", ");
            }

            failMessage.delete(failMessage.length() - 2, failMessage.length() - 1);

            Assert.fail(failMessage.toString());
        }
    }
}
