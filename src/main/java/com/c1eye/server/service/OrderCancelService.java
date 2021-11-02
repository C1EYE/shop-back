package com.c1eye.server.service;

import com.c1eye.server.bo.OrderMessageBO;
import com.c1eye.server.exception.http.ServerErrorException;
import com.c1eye.server.model.Order;
import com.c1eye.server.repository.OrderRepository;
import com.c1eye.server.repository.SkuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author c1eye
 * time 2021/11/1 11:19
 */
@Service
public class OrderCancelService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SkuRepository skuRepository;

    /**
     * 取消订单
     * @param orderMessageBO
     */
    public void cancel(OrderMessageBO orderMessageBO){
        if (orderMessageBO.getOrderId() <=0){
            throw new ServerErrorException(9999);
        }
        this.cancel(orderMessageBO.getOrderId());
    }

    private void cancel(Long oid) {
        Optional<Order> orderOptional = orderRepository.findById(oid);
        Order order = orderOptional.orElseThrow(() -> {
             return new ServerErrorException(9999);
        });
        int res = orderRepository.cancelOrder(oid);
        if (res!=1){
            return;
        }
        /**
         * 归还库存
         */
        order.getSnapItems().stream().forEach(i->{
            skuRepository.recoverStock(i.getId(), i.getCount().longValue());
        });
    }
}
