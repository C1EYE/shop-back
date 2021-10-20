package com.c1eye.server.core.enumeration;

import com.c1eye.server.model.Coupon;

import java.util.stream.Stream;

/**
 * @author c1eye
 * time 2021/10/19 10:04
 */
public enum CouponStatus {
    AVAILABLE(1, "可以使用"), USED(2, "未使用，已过期"), EXPIRED(3, "已使用");

    CouponStatus(Integer value, String description) {
        this.value = value;
    }

    public static CouponStatus toType(int status) {
        return Stream.of(CouponStatus.values()).filter(c -> c.value == status).findAny().orElse(null);
    }

    public Integer getValue() {
        return value;
    }

    private Integer value;

}
