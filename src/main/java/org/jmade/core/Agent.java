package org.jmade.core;

import kafka.admin.AdminUtils;
import kafka.admin.RackAwareMode;
import kafka.utils.ZKStringSerializer$;
import kafka.utils.ZkUtils;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.jmade.core.message.Consumer;
import org.jmade.core.message.Producer;

import java.util.Properties;
import java.util.UUID;

public class Agent {
    private static final String BROADCAST_TOPIC = "broadcast";

    public String ID;
    private ZkUtils zkUtils;
    private Producer producer;
    private Producer broadCastProducer;
    private Consumer consumer;
    private Consumer broadCastConsumer;

    public Agent() {
        ID = UUID.randomUUID().toString();
    }

    public void onStart() {
        initZkUtills();
        initTopic();
        initMessageSystem();
    }

    public void onStop() {
        AdminUtils.deleteTopic(zkUtils, ID);
        AdminUtils.deleteTopic(zkUtils, BROADCAST_TOPIC);
        broadCastProducer.close();
        producer.close();
        broadCastConsumer.close();
        consumer.close();
    }

    public void broadCastMessage(String message){
        broadCastProducer.sendMessage(message);
    }

    public void sendMessage(String topic, String message){
        producer.sendMessage(topic, message);
    }

    private void initZkUtills(){
        String zookeeperConnect = "localhost:2181";
        int sessionTimeoutMs = 10 * 1000;
        int connectionTimeoutMs = 8 * 1000;
        ZkClient zkClient = new ZkClient(zookeeperConnect, sessionTimeoutMs, connectionTimeoutMs, ZKStringSerializer$.MODULE$);
        zkUtils = new ZkUtils(zkClient, new ZkConnection(zookeeperConnect), false);
    }

    private void initTopic(){
        String topicName = ID;
        Properties topicConfig = new Properties();
        if(!AdminUtils.topicExists(zkUtils, BROADCAST_TOPIC)) {
            AdminUtils.createTopic(zkUtils, BROADCAST_TOPIC, 1, 1, topicConfig, RackAwareMode.Disabled$.MODULE$);
        }
        if(!AdminUtils.topicExists(zkUtils, topicName)) {
            AdminUtils.createTopic(zkUtils, topicName, 1, 1, topicConfig, RackAwareMode.Disabled$.MODULE$);
        }
        System.err.println("Topic " + topicName + " created.");
    }

    private void initMessageSystem(){
        final Properties props = new Properties();
        // props.put("zookeeper.connect", "localhost:2181");
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");


        broadCastProducer = new Producer(BROADCAST_TOPIC, props);
        producer = new Producer(ID, props);

        broadCastConsumer = new Consumer(BROADCAST_TOPIC, props);
        consumer = new Consumer(ID, props);

        /*ExecutorService consumerPool = Executors.newFixedThreadPool(10);
        consumerPool.execute(() -> {
            consumer.consume();
            broadCastConsumer.consume();
        });*/
    }

    public void consume(){
        broadCastConsumer.consume();
        consumer.consume();
    }

}
