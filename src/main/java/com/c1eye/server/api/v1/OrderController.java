package com.c1eye.server.api.v1;

import com.c1eye.server.bo.PageCounter;
import com.c1eye.server.core.LocalUser;
import com.c1eye.server.core.interceptors.ScopeLevel;
import com.c1eye.server.dto.OrderDTO;
import com.c1eye.server.exception.http.NotFoundException;
import com.c1eye.server.logic.OrderChecker;
import com.c1eye.server.model.Order;
import com.c1eye.server.service.OrderService;
import com.c1eye.server.util.CommonUtils;
import com.c1eye.server.vo.OrderIdVO;
import com.c1eye.server.vo.OrderPureVO;
import com.c1eye.server.vo.OrderSimplifyVO;
import com.c1eye.server.vo.PagingDozer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author c1eye
 * time 2021/10/20 14:53
 */
@Validated
@RequestMapping("order")
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Value("${c1eye.order.pay-time-limit}")
    private Long payTimeLimit;

    /**
     * 下单
     *
     * @param orderDTO
     * @return
     */
    @PostMapping("")
    @ScopeLevel
    public OrderIdVO placeOrder(@RequestBody OrderDTO orderDTO) {
        Long uid = LocalUser.getUser().getId();
        OrderChecker orderChecker = this.orderService.isOk(uid, orderDTO);
        Long oid = this.orderService.placeOrder(uid, orderDTO, orderChecker);
        return new OrderIdVO(oid);
    }

    /**
     * 获取未支付订单
     *
     * @param start
     * @param count
     * @return
     */
    @ScopeLevel
    @GetMapping("/status/unpaid")
    public PagingDozer getUnpaidSimplifyList(
            @RequestParam(defaultValue = "0") Integer start, @RequestParam(defaultValue = "10") Integer count) {
        PageCounter page = CommonUtils.convertToPageParameter(start, count);
        PagingDozer unpaid = orderService.getUnpaid(page.getPage(), page.getCount());
        return unpaid;
    }

    @ScopeLevel
    @GetMapping("/by/status/{status}")
    public PagingDozer getByStatus(
            @PathVariable int status, @RequestParam(name = "start", defaultValue = "0") Integer start,
            @RequestParam(name = "count", defaultValue = "10") Integer count) {
        PageCounter page = CommonUtils.convertToPageParameter(start, count);
        Page<Order> orders = orderService.getByStatus(status, page.getPage(), page.getCount());
        PagingDozer<Order, OrderSimplifyVO> pagingDozer = new PagingDozer<>(orders,
                OrderSimplifyVO.class);
        pagingDozer.getItems().forEach(item -> ((OrderSimplifyVO) item).setPeriod(payTimeLimit));
        return pagingDozer;
    }

    /**
     * 获取订单信息
     * @param oid
     * @return
     */
    public OrderPureVO getOrderDetail(@PathVariable(name = "id")Long oid){
        Optional<Order> orderOptional = this.orderService.getOrderDetail(oid);
        return orderOptional.map((o) -> new OrderPureVO(o, payTimeLimit))
                            .orElseThrow(() -> new NotFoundException(50009));
    }
}
