/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package org.switchyard.transform.config.model;

import junit.framework.Assert;
import org.junit.Test;
import org.switchyard.annotations.Transformer;
import org.switchyard.metadata.java.JavaService;
import org.switchyard.transform.BaseTransformer;

import javax.xml.namespace.QName;
import java.util.List;

/**
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class TransformerFactoryTest {

    @Test
    public void test_listTransformations() {
        List<TransformerTypes> transformTypes = TransformerFactory.listTransformations(TestTransformer.class);

        Assert.assertEquals(5, transformTypes.size());
        Assert.assertEquals(JavaService.toMessageType(A.class), transformTypes.get(0).getFrom());
        Assert.assertEquals(JavaService.toMessageType(B.class), transformTypes.get(0).getTo());
        Assert.assertEquals(JavaService.toMessageType(B.class), transformTypes.get(1).getFrom());
        Assert.assertEquals(JavaService.toMessageType(A.class), transformTypes.get(1).getTo());
        Assert.assertEquals(QName.valueOf("X"), transformTypes.get(2).getFrom());
        Assert.assertEquals(QName.valueOf("Y"), transformTypes.get(2).getTo());
        Assert.assertEquals(QName.valueOf("Z"), transformTypes.get(3).getFrom());
        Assert.assertEquals(JavaService.toMessageType(A.class), transformTypes.get(3).getTo());
        Assert.assertEquals(JavaService.toMessageType(B.class), transformTypes.get(4).getFrom());
        Assert.assertEquals(QName.valueOf("Z"), transformTypes.get(4).getTo());
    }

    @Test
    public void test_transform_interface_impl() {
        org.switchyard.transform.Transformer transformer = TransformerFactory.newTransformer(TestTransformer.class, JavaService.toMessageType(A.class), JavaService.toMessageType(B.class));

        Assert.assertTrue(transformer instanceof TestTransformer);
        Assert.assertTrue(transformer.transform(new A()) instanceof B);
    }

    @Test
    public void test_transform_anno_no_types_defined() {
        org.switchyard.transform.Transformer transformer = TransformerFactory.newTransformer(TestTransformer.class, JavaService.toMessageType(B.class), JavaService.toMessageType(A.class));

        Assert.assertTrue(!(transformer instanceof TestTransformer));
        Assert.assertTrue(transformer.transform(new B()) instanceof A);
    }

    @Test
    public void test_transform_unknown() {
        try {
            TransformerFactory.newTransformer(TestTransformer.class, QName.valueOf("AAA"), QName.valueOf("BBB"));
            Assert.fail("Expected Exception");
        } catch(RuntimeException e) {
            Assert.assertEquals("Error constructing Transformer instance for class 'org.switchyard.transform.config.model.TransformerFactoryTest$TestTransformer'.  " +
                    "Class does not support a transformation from type 'AAA' to type 'BBB'.",
                    e.getMessage());
        }
    }

    @Test
    public void test_transform_anno_types_defined() {
        org.switchyard.transform.Transformer transformer = TransformerFactory.newTransformer(TestTransformer.class, QName.valueOf("X"), QName.valueOf("Y"));

        Assert.assertTrue(!(transformer instanceof TestTransformer));
        Assert.assertEquals("Y", transformer.transform("X"));
    }

    public static class TestTransformer extends BaseTransformer {

        public TestTransformer() {
            super(JavaService.toMessageType(A.class), JavaService.toMessageType(B.class));
        }

        // #1: A to B
        @Override
        public Object transform(Object from) {
            return new B();
        }

        // #2: B to A
        @Transformer
        public A bToA(B b) {
            return new A();
        }

        // #3: X to Y
        @Transformer(from = "X", to = "Y")
        public String xToY(String x) {
            return "Y";
        }

        // #4: Z to A
        @Transformer(from = "Z")
        public A zToA(B z) {
            return new A();
        }

        // #5: B to Z
        @Transformer(to = "Z")
        public A bToZ(B b) {
            return new A();
        }
    }

    public static class A {

    }

    public static class B {

    }
}
