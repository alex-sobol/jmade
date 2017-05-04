package org.jmade.core.message.provider.kafka;


import kafka.admin.AdminUtils;
import kafka.admin.RackAwareMode;
import kafka.utils.ZKStringSerializer$;
import kafka.utils.ZkUtils;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;

import java.util.Properties;

// TODO: Consider removing topic related methods
public class TopicManager {

    private ZkUtils zkUtils;

    public TopicManager() {
        String zookeeperConnect = "localhost:2181";
        int sessionTimeoutMs = 10 * 1000;
        int connectionTimeoutMs = 8 * 1000;
        ZkClient zkClient = new ZkClient(zookeeperConnect, sessionTimeoutMs, connectionTimeoutMs, ZKStringSerializer$.MODULE$);
        zkUtils = new ZkUtils(zkClient, new ZkConnection(zookeeperConnect), false);
    }

    public void createTopic(String name) {
        Properties topicConfig = new Properties();
        if (!isTopicExists(name)) {
            AdminUtils.createTopic(zkUtils, name, 1, 1, topicConfig, RackAwareMode.Disabled$.MODULE$);
        }
    }

    // TODO: Lets not delete topic
    public void deleteTopic(String name) {
        if (isTopicExists(name)) {
            AdminUtils.deleteTopic(zkUtils, name);
        }
    }

    public boolean isTopicExists(String name) {
        return AdminUtils.topicExists(zkUtils, name);
    }
}
