package com.c1eye.server.manager.redis;

import com.c1eye.server.bo.OrderMessageBO;
import com.c1eye.server.service.CouponBackService;
import com.c1eye.server.service.OrderCancelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

/**
 * @author c1eye
 * time 2021/11/1 10:15
 */
public class TopicMessageListener implements MessageListener {
    @Autowired
    private OrderCancelService orderCancelService;

    @Autowired
    private CouponBackService couponBackService;

    private static ApplicationEventPublisher publisher;

    @Autowired
    public static void setPublisher(ApplicationEventPublisher publisher) {
        TopicMessageListener.publisher = publisher;
    }

    @Override
    public void onMessage(Message message, byte[] bytes) {
        byte[] body = message.getBody();
        byte[] channel = message.getChannel();

        String expiredKey = new String(body);
        String topic = new String(channel);
        System.out.println(expiredKey);
        System.out.println(topic);

        OrderMessageBO messageBO = new OrderMessageBO(expiredKey);
        orderCancelService.cancel(messageBO);
        couponBackService.returnBack(messageBO);

//        TopicMessageListener.publisher.publishEvent(messageBO);
    }
}
