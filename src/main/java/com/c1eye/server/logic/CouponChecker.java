package com.c1eye.server.logic;

import com.c1eye.server.bo.SkuOrderBO;
import com.c1eye.server.core.enumeration.CouponType;
import com.c1eye.server.core.money.IMoneyDiscount;
import com.c1eye.server.exception.http.ForbiddenException;
import com.c1eye.server.exception.http.ParameterException;
import com.c1eye.server.model.Category;
import com.c1eye.server.model.Coupon;
import com.c1eye.server.util.CommonUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author c1eye
 * time 2021/10/20 15:34
 */
public class CouponChecker {
    private Coupon coupon;
    private IMoneyDiscount moneyDiscount;


    public CouponChecker(Coupon coupon, IMoneyDiscount moneyDiscount) {
        this.coupon = coupon;
        this.moneyDiscount = moneyDiscount;
    }

    public void isOk() {
        Date now = new Date();
        Boolean isInTimeline = CommonUtils.isInTimeLine(now, this.coupon.getStartTime(), this.coupon.getEndTime());
        if (!isInTimeline) {
            throw new ForbiddenException(40007);
        }

    }

    public void finalTotalPriceIsOk(BigDecimal orderFinalTotalPrice, BigDecimal serverTotalPrice) {
        BigDecimal serverFinalTotalPrice;
        switch (CouponType.toType(coupon.getType())) {
            case FULL_MINUS:
            case NO_THRESHOLD_MINUS:
                serverFinalTotalPrice = serverTotalPrice.subtract(coupon.getMinus());
                if (serverFinalTotalPrice.compareTo(new BigDecimal("0")) <= 0) {
                    throw new ForbiddenException(50008);
                }
                break;
            case FULL_OFF:
                serverFinalTotalPrice = this.moneyDiscount.discount(serverTotalPrice, coupon.getRate());
                break;
            default:
                throw new ParameterException(40009);
        }
        int compare = serverFinalTotalPrice.compareTo(orderFinalTotalPrice);
        if (compare != 0) {
            throw new ForbiddenException(50000);
        }
    }

    public void canBeUsed(List<SkuOrderBO> skuOrderBOList, BigDecimal serverTotalPrice) {
        BigDecimal orderCategoryPrice;
        if (this.coupon.getWholeStore()) {
            orderCategoryPrice = serverTotalPrice;
        } else {
            List<Long> cidList = coupon.getCategoryList()
                                       .stream()
                                       .map(Category::getId)
                                       .collect(Collectors.toList());
            orderCategoryPrice = this.getSumByCategoryList(skuOrderBOList, cidList);
        }
        this.couponCanBeUsed(orderCategoryPrice);
    }

    private void couponCanBeUsed(BigDecimal orderCategoryPrice) {
        switch (CouponType.toType(this.coupon.getType())) {
            case FULL_OFF:
            case FULL_MINUS:
                int compare = this.coupon.getFullMoney().compareTo(orderCategoryPrice);
                if (compare > 0) {
                    throw new ParameterException(40008);
                }
                break;
            case NO_THRESHOLD_MINUS:
                break;
            default:
                throw new ParameterException(40009);
        }
    }

    private BigDecimal getSumByCategoryList(List<SkuOrderBO> skuOrderBOList, List<Long> cidList) {
        return cidList.stream()
                      .map(cid -> this.getSumByCategory(skuOrderBOList, cid))
                      .reduce(BigDecimal::add)
                      .orElse(new BigDecimal("0"));
    }

    /**
     * 累加优惠券
     *
     * @param skuOrderBOList
     * @param cid            优惠券类别
     * @return
     */
    private BigDecimal getSumByCategory(List<SkuOrderBO> skuOrderBOList, Long cid) {
        return skuOrderBOList.stream()
                             .filter(sku -> sku.getCategoryId().equals(cid))
                             .map(sku -> sku.getTotalPrice())
                             .reduce(BigDecimal::add).orElse(new BigDecimal("0"));
    }

}
