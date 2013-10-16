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
package org.switchyard.transform.internal.dozer;

import org.junit.Assert;
import org.junit.Test;
import org.switchyard.transform.AbstractTransformerTestCase;
import org.switchyard.transform.Transformer;
import org.switchyard.transform.dozer.internal.DozerTransformer;

import java.io.IOException;

/**
 * @author <a href="mailto:uros.krivec@parsek.net">uros.krivec@parsek.net</a>
 */
public class DozerTransformerTest extends AbstractTransformerTestCase {

    @Test
    public void no_config_attr() throws IOException {
        try {
            getTransformer("sw-config-00_1.xml");
        } catch(RuntimeException e) {
            Assert.assertEquals("Invalid Dozer configuration model.  Null or empty 'config' specification.", e.getMessage());
        }
    }

    @Test
    public void no_from_attr() throws IOException {
        try {
            getTransformer("sw-config-00_2.xml");
        } catch(RuntimeException e) {
            Assert.assertEquals("Invalid Dozer configuration model.  Null or empty 'from' specification.", e.getMessage());
        }
    }

    @Test
    public void no_to_attr() throws IOException {
        try {
            getTransformer("sw-config-00_3.xml");
        } catch(RuntimeException e) {
            Assert.assertEquals("Invalid Dozer configuration model.  Null or empty 'to' specification.", e.getMessage());
        }
    }

    @Test
    public void not_both_java_type() throws IOException {
        try {
            getTransformer("sw-config-00_4.xml");
        } catch(RuntimeException e) {
            Assert.assertEquals("Invalid Dozer Transformer configuration. Both of the specified 'to' and 'from' transform types must be a Java type.", e.getMessage());
        }
    }

    @Test
    public void undefined_mapping() throws IOException {
        try {
            getTransformer("sw-config-00_5.xml");
        } catch(RuntimeException e) {
            Assert.assertEquals("Invalid Dozer configuration file. No mappings defined for class org.switchyard.transform.internal.dozer.UserAccount or " +
                                "class org.switchyard.transform.internal.dozer.UserMetadata. See Dozer User Guide.", e.getMessage());
        }
    }

    @Test
    public void test_from_a_to_b() throws IOException {
        Transformer transformer = getTransformer("sw-config-01.xml");

        User user = new User();
        user.setName("Nick");
        user.setAge(18);
        user.setAddress("Address 1A");
        UserAccount account = (UserAccount) transformer.transform(user);

        Assert.assertEquals("Nick", account.getUsername());
        Assert.assertEquals(new Integer(18), account.getUserAge());
        Assert.assertEquals("Address 1A", account.getAddress());

    }

    @Test
    public void test_from_b_to_a() throws IOException {
        Transformer transformer = getTransformer("sw-config-02.xml");

        UserAccount account = new UserAccount();
        account.setUsername("nick");
        account.setAddress("Address 1A");
        User user = (User) transformer.transform(account);

        Assert.assertEquals("nick", user.getName());
        Assert.assertEquals("Address 1A", account.getAddress());
    }

    protected Transformer getTransformer(String config) throws IOException {
        Transformer transformer = super.getTransformer(config);

        if(!(transformer instanceof DozerTransformer)) {
            Assert.fail("Not an instance of DozerTransformer.");
        }

        return transformer;
    }

}
