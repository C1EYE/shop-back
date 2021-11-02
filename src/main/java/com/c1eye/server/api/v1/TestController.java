package com.c1eye.server.api.v1;

import com.c1eye.server.manager.rocketMQ.ProducerSchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author c1eye
 * time 2021/11/2 16:40
 */
@Controller
public class TestController {

    @Autowired
    private ProducerSchedule producerSchedule;

    @GetMapping("/push")
    public void pushMessageTOMQ(){
        producerSchedule.send("TopicTest","Message","ALG");
    }
}
