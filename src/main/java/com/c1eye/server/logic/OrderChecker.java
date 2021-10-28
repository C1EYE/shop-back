package com.c1eye.server.logic;

import com.c1eye.server.bo.SkuOrderBO;
import com.c1eye.server.dto.OrderDTO;
import com.c1eye.server.dto.SkuInfoDTO;
import com.c1eye.server.exception.http.ParameterException;
import com.c1eye.server.model.OrderSku;
import com.c1eye.server.model.Sku;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author c1eye
 * time 2021/10/20 15:35
 */
@Getter
public class OrderChecker {
    private OrderDTO orderDTO;
    private List<Sku> serverSkuList;
    private CouponChecker couponChecker;
    private Integer maxSkuLimit;
    private List<OrderSku> orderSkuList = new ArrayList<>();

    public OrderChecker(OrderDTO orderDTO, List<Sku> serverSkuList, CouponChecker couponChecker,Integer maxSkuLimit) {
        this.orderDTO = orderDTO;
        this.serverSkuList = serverSkuList;
        this.couponChecker = couponChecker;
        this.maxSkuLimit = maxSkuLimit;
    }
    public void isOK(){
        BigDecimal serverTotalPrice = new BigDecimal("0");
        List<SkuOrderBO> skuOrderBOList = new ArrayList<>();

        this.skuNotOnSale(orderDTO.getSkuInfoList().size(),this.serverSkuList.size());

        for (int i = 0; i < serverSkuList.size(); i++) {
            Sku sku = this.serverSkuList.get(i);
            SkuInfoDTO skuInfoDTO = this.orderDTO.getSkuInfoList().get(i);
            this.containsSoldOutSku(sku);
            this.beyondSkuStock(sku,skuInfoDTO);

            serverTotalPrice = serverTotalPrice.add(calculateSkuOrderPrice(sku, skuInfoDTO));
            skuOrderBOList.add(new SkuOrderBO(sku, skuInfoDTO));
            this.orderSkuList.add(new OrderSku(sku, skuInfoDTO));
        }
        this.totalPriceIsOk(orderDTO.getTotalPrice(), serverTotalPrice);
        if(this.couponChecker != null){
            this.couponChecker.isOk();
            this.couponChecker.canBeUsed(skuOrderBOList,serverTotalPrice);
            this.couponChecker.finalTotalPriceIsOk(orderDTO.getFinalTotalPrice(),serverTotalPrice);
        }
    }

    public String getLeaderImg(){
        return this.serverSkuList.get(0).getImg();
    }

    public String getLeaderTitle(){
        return this.serverSkuList.get(0).getTitle();
    }

    public Integer getTotalCount(){
        return this.orderDTO.getSkuInfoList().stream()
                            .map(SkuInfoDTO::getCount)
                            .reduce(Integer::sum)
                            .orElse(0);
    }

    private void totalPriceIsOk(BigDecimal orderTotalPrice,BigDecimal serverTotalPrice){
        if (orderTotalPrice.compareTo(serverTotalPrice)!=0){
            throw new ParameterException(50005);
        }
    }

    /**
     * 一种sku总价
     * @param sku
     * @param skuInfoDTO
     * @return
     */
    private BigDecimal calculateSkuOrderPrice(Sku sku,SkuInfoDTO skuInfoDTO){
        if (skuInfoDTO.getCount() <= 0){
            throw new ParameterException(50007);
        }
        return sku.getActualPrice().multiply(new BigDecimal(skuInfoDTO.getCount()));
    }

    private void skuNotOnSale(int count1,int count2){
        if(count1 != count2){
            throw new ParameterException(50002);
        }
    }

    /**
     * 包含卖光的sku
     * @param sku
     */
    private void containsSoldOutSku(Sku sku){
        //卖光了
        if(sku.getStock() == 0){
            throw new ParameterException(50001);
        }
    }

    /**
     * 库存不足
     * @param sku
     * @param skuInfoDTO
     */
    private void beyondSkuStock(Sku sku,SkuInfoDTO skuInfoDTO){
        if(sku.getStock() < skuInfoDTO.getCount()){
            throw new ParameterException(50003);
        }
    }

    /**
     * 超出最大购买限制
     */
    private void beyondMaxSkuLimit(SkuInfoDTO skuInfoDTO){
        if(skuInfoDTO.getCount() > this.maxSkuLimit){
            throw new ParameterException(50004);
        }
    }

}
