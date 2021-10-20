package com.c1eye.server.api.v1;

import com.c1eye.server.core.interceptors.ScopeLevel;
import com.c1eye.server.dto.OrderDTO;
import com.c1eye.server.vo.OrderIdVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author c1eye
 * time 2021/10/20 14:53
 */
@Validated
@RequestMapping("order")
@RestController
public class OrderController {
    @PostMapping("")
    @ScopeLevel
    public OrderIdVO placeOrder(@RequestBody OrderDTO orderDTO) {

    }
}
