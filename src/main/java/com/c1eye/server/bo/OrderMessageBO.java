package com.c1eye.server.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author c1eye
 * time 2021/11/1 10:48
 */

@Getter
@Setter
public class OrderMessageBO {
    private Long orderId;
    private Long couponId;
    private Long userId;
    private String message;

    public OrderMessageBO(String message) {
        this.message = message;
        this.parseId(message);
    }

    private void parseId(String message){
        String[] temp = message.split(",");
        this.userId = Long.valueOf(temp[0]);
        this.orderId = Long.valueOf(temp[1]);
        this.couponId = Long.valueOf(temp[2]);
    }

}
