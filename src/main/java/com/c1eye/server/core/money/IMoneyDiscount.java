package com.c1eye.server.core.money;

import java.math.BigDecimal;

/**
 * @author c1eye
 * time 2021/10/20 16:50
 */
public interface IMoneyDiscount {
    BigDecimal discount(BigDecimal original, BigDecimal discount);
}
