package com.c1eye.server.model;

import com.c1eye.server.dto.SkuInfoDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author c1eye
 * time 2021/10/22 19:51
 */
@Getter
@Setter
@NoArgsConstructor
public class OrderSku {
    private Long id;
    private Long spuId;
    private BigDecimal finalPrice;
    private BigDecimal singlePrice;
    private List<String> specValues;
    private Integer count;
    private String img;
    private String title;

    /**
     *
     * @param sku 后端查询
     * @param skuInfoDTO 前端传入
     */
    public OrderSku(Sku sku, SkuInfoDTO skuInfoDTO) {

        this.id = sku.getId();
        this.spuId = sku.getSpuId();
        this.singlePrice = sku.getActualPrice();
        this.finalPrice = sku.getActualPrice().multiply(new BigDecimal(skuInfoDTO.getCount()));
        this.count = skuInfoDTO.getCount();
        this.img = sku.getImg();
        this.title = sku.getTitle();
        this.specValues = sku.getSpecValueList();
    }
}
