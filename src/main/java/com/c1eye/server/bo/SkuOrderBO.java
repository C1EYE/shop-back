package com.c1eye.server.bo;

import com.c1eye.server.dto.SkuInfoDTO;
import com.c1eye.server.model.Sku;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author c1eye
 * time 2021/10/21 20:22
 */
@Getter
@Setter
public class SkuOrderBO {
    private BigDecimal actualPrice;
    private Integer count;
    private Long categoryId;

    public SkuOrderBO(Sku sku, SkuInfoDTO skuInfoDTO){
        this.actualPrice = sku.getActualPrice();
        this.count = skuInfoDTO.getCount();
        this.categoryId = sku.getCategoryId();
    }

    public BigDecimal getTotalPrice(){
        return actualPrice.multiply(new BigDecimal(count));
    }
}
