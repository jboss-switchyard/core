package org.switchyard.config.model.composite.v1;

import static org.junit.Assert.*;

import org.junit.Test;


public class V1ComponentImplementationModelTest
{
    @Test
    public void verifyQNameUponCreation()
    {
        final String type = "customtype";
        final V1ComponentImplementationModel model = new V1ComponentImplementationModel(type);
        assertEquals(type, model.getType());
    }

}
