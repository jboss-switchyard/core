package ${groupId};

import org.junit.Assert;
import org.junit.Test;

import ${groupId}.HelloWorldApp;
import ${groupId}.model.HelloResponse;

public class HelloWorldAppTest {

    @Test
    public void test() {
        HelloWorldApp hwa = new HelloWorldApp();
        hwa.init();
        HelloResponse response = hwa.send();
        Assert.assertEquals (HelloWorldApp.MESSAGE, response.getMessage());
    }
}
