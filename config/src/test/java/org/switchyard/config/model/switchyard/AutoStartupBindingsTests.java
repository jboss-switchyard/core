package org.switchyard.config.model.switchyard;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.switchyard.config.model.ModelPuller;
import org.switchyard.config.model.composite.BindingModel;
import org.switchyard.config.model.composite.CompositeServiceModel;

/**
 * Test auto startup bindings behavior
 */
public class AutoStartupBindingsTests {

    private static final String TEST_WHEN_TRUE = "/org/switchyard/config/model/switchyard/AutoStartupBindingsTestTrue.xml";
    private static final String TEST_DOMAIN_PROPERTY = "/org/switchyard/config/model/switchyard/AutoStartupBindingsTestDomainProperty.xml";
    private static final String TEST_SERVICE_PROPERTY = "/org/switchyard/config/model/switchyard/AutoStartupBindingsTestServiceProperty.xml";
    private static final String TEST_BINDING_ATTRIBUTE = "/org/switchyard/config/model/switchyard/AutoStartupBindingsTestBindingAttribute.xml";

    private ModelPuller<SwitchYardModel> _puller;

    @Before
    public void before() throws Exception {
        _puller = new ModelPuller<SwitchYardModel>();
    }

    @Test
    public void testWhenAutoStartupIsTrue() throws Exception {
        SwitchYardModel model = _puller.pull(TEST_WHEN_TRUE, getClass());
        checkBindingsAutoStartupValue(model, true);
    }

    @Test
    public void testDomainProperty() throws Exception {
        SwitchYardModel model = _puller.pull(TEST_DOMAIN_PROPERTY, getClass());
        checkBindingsAutoStartupValue(model, false);
    }

    @Test
    public void testServiceProperty() throws Exception {
        SwitchYardModel model = _puller.pull(TEST_SERVICE_PROPERTY, getClass());
        checkBindingsAutoStartupValue(model, false);
    }

    @Test
    public void testBindingAttribute() throws Exception {
        SwitchYardModel model = _puller.pull(TEST_BINDING_ATTRIBUTE, getClass());
        checkBindingsAutoStartupValue(model, false);
    }

    private void checkBindingsAutoStartupValue(SwitchYardModel model, boolean expectedValue) {
        for (CompositeServiceModel service : model.getComposite().getServices())
            for (BindingModel binding : service.getBindings())
                Assert.assertEquals(expectedValue, binding.isAutoStartup());
    }

}
