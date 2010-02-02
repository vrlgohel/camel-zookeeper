package org.apache.camel.component.zookeeper;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.util.ExchangeHelper;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.ACL;

/**
 * <code>ZooKeeperUtils</code> contains static utility functions mostly for
 * retrieving optional message properties from Message headers.
 */
public class ZooKeeperUtils {

    /**
     * Pulls a createMode flag from the header keyed by
     * {@link ZooKeeperMessage.ZOOKEEPER_CREATE_MODE} in the given message and
     * attemps to parse a {@link CreateMode} from it.
     *
     * @param message the message that may contain a ZOOKEEPER_CREATE_MODE
     *            header.
     * @return the parsed {@link CreateMode} or null if the header was null or
     *         not a valid mode flag.
     */
    public static CreateMode getCreateMode(Message message) {
        CreateMode mode = null;

        mode = message.getHeader(ZooKeeperMessage.ZOOKEEPER_CREATE_MODE, CreateMode.class);
        if (mode == null) {
            Integer modeHeader = message.getHeader(ZooKeeperMessage.ZOOKEEPER_CREATE_MODE, Integer.class);
            if (modeHeader != null) {
                try {
                    mode = CreateMode.fromFlag(modeHeader);
                } catch (Exception e) {
                }
            }
        }
        return mode;
    }

    /**
     * Pulls the target node from the header keyed by
     * {@link ZooKeeperMessage.ZOOKEEPER_NODE}. This node is then typically used
     * in place of the configured node extracted from the endpoint uri.
     *
     * @param message the message that may contain a ZOOKEEPER_NODE header.
     * @return the node property or null if the header was null
     */
    public static String getNodeFromMessage(Message message, String defaultNode) {
        return getZookeeperProperty(message, ZooKeeperMessage.ZOOKEEPER_NODE, defaultNode, String.class);
    }

    public static Integer getVersionFromMessage(Message message) {
        return getZookeeperProperty(message, ZooKeeperMessage.ZOOKEEPER_NODE_VERSION, -1, Integer.class);
    }

    public static byte[] getPayloadFromExchange(Exchange exchange) {
        return ExchangeHelper.convertToType(exchange, byte[].class, exchange.getIn().getBody());
    }

    @SuppressWarnings("unchecked")
    public static List<ACL> getAclListFromMessage(Message in) {
        return getZookeeperProperty(in, ZooKeeperMessage.ZOOKEEPER_ACL, Ids.OPEN_ACL_UNSAFE, List.class);
    }

    public static <T> T getZookeeperProperty(Message m, String propertyName, T defaultValue, Class<? extends T> type) {
        T value = m.getHeader(propertyName, type);
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }
}
