package com.c1eye.server.manager.rocketMQ;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author c1eye
 * time 2021/11/2 18:45
 */
@Component
public class ConsumerSchedule implements CommandLineRunner {
    @Value("${rocketmq.consumer.consumer-group}")
    private String consumerGroup;

    @Value("${rocketmq.namesrv-addr}")
    private String namesrvAddr;

    public void messageListener() throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.subscribe("TopicTest", "*");
        consumer.setConsumeMessageBatchMaxSize(1);
        consumer.registerMessageListener((MessageListenerConcurrently) (messages, context) -> {
            for (MessageExt message : messages) {
                System.out.println(message);
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
    }

    @Override
    public void run(String... args) throws Exception {
        this.messageListener();
    }
}
