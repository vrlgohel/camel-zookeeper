package org.apache.camel.component.zookeeper;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.junit.Test;

public class ConsumeDataTest extends ZooKeeperTestSupport {

    private String testPayload = "This is a test";

    @Override
    protected RouteBuilder[] createRouteBuilders() throws Exception {
        return new RouteBuilder[] {new RouteBuilder() {
            public void configure() throws Exception {
                from("zoo://localhost:39913/camel?repeat=true").to("mock:zookeeper-data");
            }
        }};
    }

    @Test
    public void shouldAwaitCreationAndGetDataNotification() throws Exception {

        MockEndpoint mock = getMockEndpoint("mock:zookeeper-data");
        mock.expectedMinimumMessageCount(10);

        createCamelNode();
        updateNode(10);

        mock.await(5, TimeUnit.SECONDS);
        mock.assertIsSatisfied();

        int lastVersion = -1;
        List<Exchange> received = mock.getReceivedExchanges();
        for (int x = 0; x < received.size(); x++) {
            ZooKeeperMessage zkm = (ZooKeeperMessage)mock.getReceivedExchanges().get(x).getIn();
            System.err.println(new String((byte[])zkm.getBody()));
            int version = zkm.getStatistics().getVersion();
            assertTrue(lastVersion < version);
            lastVersion = version;
        }
    }

    @Test
    public void deletionOfAwaitedNodeCausesNoFailure() throws Exception {

        MockEndpoint mock = getMockEndpoint("mock:zookeeper-data");
        mock.expectedMessageCount(11);
        createCamelNode();

        int wait = 200;
        delay(wait);
        client.delete("/camel");

        // recreate and update a number of times.
        createCamelNode();
        updateNode(10);

        mock.await(5, TimeUnit.SECONDS);
        mock.assertIsSatisfied();


    }

    private void updateNode(int times) throws InterruptedException, Exception {
        for (int x = 1; x < times; x++) {
            delay(200);
            client.setData("/camel", testPayload + "_" + x, -1);
        }
    }



    private void createCamelNode() throws InterruptedException, Exception {
        try {
           delay(1000);
            client.create("/camel", testPayload + "_0");
        } catch (NodeExistsException e) {
        }
    }
}