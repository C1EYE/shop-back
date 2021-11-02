package com.c1eye.server.api.v1;

import com.c1eye.server.core.interceptors.ScopeLevel;
import com.c1eye.server.lib.C1eyeWxNotify;
import com.c1eye.server.service.WxPaymentNotifyService;
import com.c1eye.server.service.WxPaymentService;
import com.github.wxpay.sdk.C1eyeWxPayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Positive;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author c1eye
 * time 2021/10/29 20:39
 */
@RequestMapping("payment")
@RestController
@Validated
public class PaymentController {

    private static C1eyeWxPayConfig c1eyeWxPayConfig = new C1eyeWxPayConfig();

    @Autowired
    private WxPaymentService wxPaymentService;

    @Autowired
    private WxPaymentNotifyService wxPaymentNotifyService;

    @ScopeLevel
    @PostMapping("/pay/order/{id}")
    public Map<String, String> preWxOrder(@Positive @PathVariable(name = "id") Long oid) {
        Map<String, String> miniPayParams = this.wxPaymentService.preOrder(oid);
        return miniPayParams;
    }

    @RequestMapping("/wx/notify")
    public String payCallback(
            HttpServletRequest request,
            HttpServletResponse response) {
        InputStream s;
        try {
            s = request.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return C1eyeWxNotify.fail();
        }
        String xml;
        xml = C1eyeWxNotify.readNotify(s);
        try {
            this.wxPaymentNotifyService.processPayNotify(xml);
        } catch (Exception e) {
            return C1eyeWxNotify.fail();
        }
        return C1eyeWxNotify.success();
    }
}
