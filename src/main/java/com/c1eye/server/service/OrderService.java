package com.c1eye.server.service;

import com.c1eye.server.core.LocalUser;
import com.c1eye.server.core.enumeration.CouponStatus;
import com.c1eye.server.core.enumeration.OrderStatus;
import com.c1eye.server.core.money.IMoneyDiscount;
import com.c1eye.server.dto.OrderDTO;
import com.c1eye.server.dto.SkuInfoDTO;
import com.c1eye.server.exception.http.ForbiddenException;
import com.c1eye.server.exception.http.NotFoundException;
import com.c1eye.server.exception.http.ParameterException;
import com.c1eye.server.logic.CouponChecker;
import com.c1eye.server.logic.OrderChecker;
import com.c1eye.server.model.*;
import com.c1eye.server.repository.CouponRepository;
import com.c1eye.server.repository.OrderRepository;
import com.c1eye.server.repository.SkuRepository;
import com.c1eye.server.repository.UserCouponRepository;
import com.c1eye.server.util.CommonUtils;
import com.c1eye.server.util.OrderUtil;
import com.c1eye.server.vo.OrderSimplifyVO;
import com.c1eye.server.vo.PagingDozer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author c1eye
 * time 2021/10/20 15:18
 */
@Service
public class OrderService {

    @Autowired
    private SkuService skuService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Autowired
    private SkuRepository skuRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private IMoneyDiscount moneyDiscount;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${c1eye.order.max-sku-limit}")
    private Long maxSkuLimit;

    @Value("${c1eye.order.pay-time-limit}")
    private Long payTimeLimit;

    @Transactional
    public Long placeOrder(Long uid, OrderDTO orderDTO, OrderChecker orderChecker) {
        //生成订单号
        String orderNo = OrderUtil.makeOrderNo();
        //这里需要明确是选择代码中的时间还是数据库生成的时间
        Calendar now = Calendar.getInstance();
        Calendar nowClone = (Calendar) now.clone();
        Date expiredTime = CommonUtils.addSomeSeconds(now, payTimeLimit.intValue()).getTime();
        Order order = Order.builder()
                           .orderNo(orderNo)
                           .totalPrice(orderDTO.getTotalPrice())
                           .finalTotalPrice(orderDTO.getFinalTotalPrice())
                           .userId(uid)
                           .totalCount(orderChecker.getTotalCount().longValue())
                           .snapImg(orderChecker.getLeaderImg())
                           .snapTitle(orderChecker.getLeaderTitle())
                           .status(OrderStatus.UNPAID.value())
                           .expiredTime(expiredTime)
                           .placedTime(nowClone.getTime())
                           .build();

        order.setSnapAddress(orderDTO.getAddress());
        order.setSnapItems(orderChecker.getOrderSkuList());
        this.orderRepository.save(order);
        this.reduceStock(orderChecker);
        //减库存
        //核销优惠券
        //加入延迟消息队列
        Long couponId = -1L;
        if (orderDTO.getCouponId() != null) {
            this.writeOffCoupon(orderDTO.getCouponId(), order.getId(), uid);
            couponId = orderDTO.getCouponId();
        }
        this.sendToRedis(order.getId(), uid, couponId);
        return order.getId();
    }

    private void sendToRedis(Long oid, Long uid, Long couponId) {
        String key = oid.toString() + "," + uid.toString() + "," + couponId.toString();
        try {
            stringRedisTemplate.opsForValue().set(key, "1", this.payTimeLimit, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeOffCoupon(Long couponId, Long oid, Long uid) {
        int result = this.userCouponRepository.writeOff(couponId, oid, uid);
        if (result != 1) {
            throw new ForbiddenException(40012);
        }
    }

    private void reduceStock(OrderChecker orderChecker) {
        List<OrderSku> orderSkuList = orderChecker.getOrderSkuList();
        for (OrderSku orderSku : orderSkuList) {
            int result = this.skuRepository.reduceStock(orderSku.getId(), orderSku.getCount().longValue());
            if (result != 1) {
                throw new ParameterException(50003);
            }
        }
    }

    public OrderChecker isOk(Long uid, OrderDTO orderDTO) {
        if (orderDTO.getFinalTotalPrice().compareTo(new BigDecimal("0")) <= 0) {
            throw new ParameterException(50011);
        }

        List<Long> skuIdList = orderDTO.getSkuInfoList().stream().map(SkuInfoDTO::getId).collect(Collectors.toList());
        List<Sku> skuList = skuService.getSkuListByIds(skuIdList);
        CouponChecker couponChecker = null;
        Long couponId = orderDTO.getCouponId();
        if (couponId != null) {
            Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new NotFoundException(40004));
            UserCoupon userCoupon = userCouponRepository.findFirstByUserIdAndCouponIdAndStatus(uid,
                    couponId, CouponStatus.AVAILABLE).orElseThrow(() -> new NotFoundException(50006));
            couponChecker = new CouponChecker(coupon, moneyDiscount);
        }
        OrderChecker orderChecker = new OrderChecker(orderDTO, skuList, couponChecker, this.maxSkuLimit.intValue());
        orderChecker.isOK();
        return orderChecker;
    }

    public PagingDozer getUnpaid(Integer page, Integer size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        Long uid = LocalUser.getUser().getId();
        Date now = new Date();
        Page<Order> orderPage =
                orderRepository.findByExpiredTimeGreaterThanAndStatusAndUserId(now, OrderStatus.UNPAID.value(), uid,
                        pageable);
        PagingDozer<Order, OrderSimplifyVO> pagingDozer = new PagingDozer<>(orderPage,
                OrderSimplifyVO.class);
        pagingDozer.getItems().forEach(item -> ((OrderSimplifyVO) item).setPeriod(payTimeLimit));
        return pagingDozer;

    }

    public Page<Order> getByStatus(Integer status, Integer page, Integer size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        Long uid = LocalUser.getUser().getId();
        if (status == OrderStatus.All.value()) {
            return orderRepository.findByUserId(uid, pageable);
        }
        return this.orderRepository.findByUserIdAndStatus(uid, status, pageable);
    }

    public void updateOrderPrepayId(Long orderId, String prePayId) {
        Optional<Order> order = this.orderRepository.findById(orderId);
        order.ifPresent(o -> {
            o.setPrepayId(prePayId);
            this.orderRepository.save(o);
        });
        order.orElseThrow(() -> new ParameterException(10007));
    }

    public Optional<Order> getOrderDetail(Long oid) {
        Long uid = LocalUser.getUser().getId();
        return this.orderRepository.findFirstByUserIdAndId(uid, oid);
    }
}
