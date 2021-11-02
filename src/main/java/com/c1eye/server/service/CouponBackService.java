package com.c1eye.server.service;

import com.c1eye.server.bo.OrderMessageBO;
import com.c1eye.server.core.enumeration.OrderStatus;
import com.c1eye.server.exception.http.ServerErrorException;
import com.c1eye.server.model.Order;
import com.c1eye.server.repository.OrderRepository;
import com.c1eye.server.repository.UserCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author c1eye
 * time 2021/11/1 10:46
 */
@Service
public class CouponBackService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Transactional
//    @EventListener
    public void returnBack(OrderMessageBO messageBO) {
        Long cid = messageBO.getCouponId();
        Long uid = messageBO.getUserId();
        Long oid = messageBO.getOrderId();

        if (cid == -1) {
            return;
        }
        Optional<Order> orderOptional = orderRepository.findFirstByUserIdAndId(uid, oid);
        Order order = orderOptional.orElseThrow(() -> new ServerErrorException(9999));

        if (order.getStatusEnum().equals(OrderStatus.UNPAID)
                || order.getStatusEnum().equals(OrderStatus.CANCELED)
        ) {
            this.userCouponRepository.returnBack(cid,uid);
        }
    }
}
