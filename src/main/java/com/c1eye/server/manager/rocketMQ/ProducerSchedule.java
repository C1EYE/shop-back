package com.c1eye.server.manager.rocketMQ;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.aspectj.lang.annotation.After;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author c1eye
 * time 2021/11/2 16:00
 */
@Component
public class ProducerSchedule {
    private DefaultMQProducer producer;

    @Value("${rocketmq.producer.producer-group}")
    private String producerGroup;

    @Value("${rocketmq.namesrv-addr}")
    private String namesrvAddr;

    public ProducerSchedule() {

    }

    @PostConstruct
    public void defaultMQProducer() {
        if (producer == null) {
            this.producer = new DefaultMQProducer(this.producerGroup);
            this.producer.setNamesrvAddr(this.namesrvAddr);
        }
        try {
            this.producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    public String send(String topic, String tag, String messageText) {
        Message message = new Message(topic, tag, messageText.getBytes());
        message.setDelayTimeLevel(4);
        SendResult result = null;
        try {
            result = this.producer.send(message);
            System.out.println(result.getMsgId());
            System.out.println(result.getSendStatus());
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        }
        return result.getMsgId();
    }

}
