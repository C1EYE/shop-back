package com.c1eye.server.service;

import com.c1eye.server.core.LocalUser;
import com.c1eye.server.exception.http.ForbiddenException;
import com.c1eye.server.exception.http.NotFoundException;
import com.c1eye.server.exception.http.ParameterException;
import com.c1eye.server.exception.http.ServerErrorException;
import com.c1eye.server.model.Order;
import com.c1eye.server.repository.OrderRepository;
import com.c1eye.server.util.CommonUtils;
import com.c1eye.server.util.HttpRequestProxy;
import com.github.wxpay.sdk.C1eyeWxPayConfig;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author c1eye
 * time 2021/10/29 20:43
 */
@Service
public class WxPaymentService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    private static C1eyeWxPayConfig c1eyeWxPayConfig = new C1eyeWxPayConfig();

    @Value("${c1eye.order.pay-callback-host}")
    private String payCallBackHost;

    @Value("${c1eye.order.pay-callback-path}")
    private String payCallBackPath;

    public Map<String, String> preOrder(Long oid) {
        Long uid = LocalUser.getUser().getId();
        Optional<Order> orderOptional = this.orderRepository.findFirstByUserIdAndId(uid, oid);
        Order order = orderOptional.orElseThrow(() -> new NotFoundException(50009));
        if (order.needCancel()) {
            throw new ForbiddenException(50010);
        }
        //像微信服务器下单
        WXPay wxPay = this.assembleWxPayConfig();
        Map<String, String> params = this.makePreOrderParams(order.getFinalTotalPrice(), order.getOrderNo());
        Map<String, String> wxOrder;
        try {
            wxOrder = wxPay.unifiedOrder(params);
        } catch (Exception e) {
            throw new ServerErrorException(9999);
        }
        //保存prepayId 微信订单号
        if (this.unifiedOrderSuccess(wxOrder)) {
            this.orderService.updateOrderPrepayId(order.getId(), wxOrder.get("prepay_id"));
        }
        return this.makePaySignature(wxOrder);
    }

    private Map<String, String> makePaySignature(Map<String, String> wxOrder) {
        Map<String, String> wxPayMap = new HashMap<>();
        String packages = "prepay_id=" + wxOrder.get("prepay_id");

        wxPayMap.put("appId", WxPaymentService.c1eyeWxPayConfig.getAppID());

        wxPayMap.put("timeStamp", CommonUtils.timestamp10());
        wxPayMap.put("nonceStr", RandomStringUtils.randomAlphanumeric(32));
        wxPayMap.put("package", packages);
        wxPayMap.put("signType", "HMAC-SHA256");

        //sdk提供签名工具
        String sign;
        try {
            sign = WXPayUtil.generateSignature(wxPayMap, WxPaymentService.c1eyeWxPayConfig.getKey(), WXPayConstants.SignType.HMACSHA256);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServerErrorException(9999);
        }

        Map<String, String> miniPayParams = new HashMap<>();

        miniPayParams.put("paySign", sign);
        miniPayParams.putAll(wxPayMap);
        miniPayParams.remove("appId");
        return miniPayParams;
    }

    private boolean unifiedOrderSuccess(Map<String, String> wxOrder) {
        if (!wxOrder.get("return_code").equals("SUCCESS")
                || !wxOrder.get("result_code").equals("SUCCESS")) {
            throw new ParameterException(10007);
        }
        return true;
    }

    private Map<String, String> makePreOrderParams(BigDecimal serverFinalPrice, String orderNo) {
        Map<String, String> data = new HashMap<>();
        String payCallbackUrl = payCallBackHost + payCallBackPath;
        data.put("body", "Sleeve");
        data.put("out_trade_no", orderNo);
        data.put("device_info", "Sleeve");
        data.put("fee_type", "CNY");
        data.put("trade_type", "JSAPI");

        data.put("total_fee", CommonUtils.yuanToFenPlainString(serverFinalPrice));
        data.put("openid", LocalUser.getUser().getOpenid());
        data.put("spbill_create_ip", HttpRequestProxy.getRemoteRealIp());

        data.put("notify_url", payCallbackUrl);
        return data;
    }

    public WXPay assembleWxPayConfig() {
        WXPay wxPay;
        try {
            wxPay = new WXPay(WxPaymentService.c1eyeWxPayConfig);
        } catch (Exception e) {
            throw new ServerErrorException(9999);
        }
        return wxPay;
    }
}
