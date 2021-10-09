package com.c1eye.server.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author c1eye
 * time 2021/10/9 11:00
 */
@Entity
@Getter
@Setter
public class Spu extends BaseEntity {
    @Id
    private Long id;
    private String title;
    private String subtitle;
    private Long categoryId;
    private Integer rootCategoryId;
    private Boolean online;
    private String price;
    private Long sketchSpecId;
    private Integer defaultSkuId;
    private String img;
    private String discountPrice;
    private String description;
    private String tags;
    private Boolean isTest;
    //    private Object spuThemeImg;
    private String forThemeImg;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "spuId")
    private List<Sku> skuList;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "spuId")
    private List<SpuImg> spuImgList;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "spuId")
    private List<SpuDetailImg> spuDetailImgList;
}
