package com.c1eye.server.core.money;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author c1eye
 * time 2021/10/20 16:54
 */
public class HalfEvenRound implements IMoneyDiscount{
    @Override
    public BigDecimal discount(BigDecimal original, BigDecimal discount) {
        BigDecimal actualMoney = original.multiply(discount);
        BigDecimal finalMoney = actualMoney.setScale(2, RoundingMode.HALF_EVEN);
        return finalMoney;
    }
}
