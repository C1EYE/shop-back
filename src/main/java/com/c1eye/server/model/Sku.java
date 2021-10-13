package com.c1eye.server.model;

import com.c1eye.server.util.GenericAndJson;
import com.c1eye.server.util.ListAndJson;
import com.c1eye.server.util.MapAndJson;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author c1eye
 * time 2021/10/9 14:31
 */
@Entity
@Getter
@Setter
public class Sku extends BaseEntity {
    @Id
    private Long id;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private Boolean online;
    private String img;
    private String title;
    private Long spuId;
    private Long categoryId;
    private Long rootCategoryId;

    private String specs;
    private String code;
    private Long stock;

    public List<Spec> getSpecs() {
        if (specs == null) {
            return Collections.emptyList();
        }
        return GenericAndJson.jsonToList(this.specs);
    }

    public void setSpecs(List<Spec> specs) {
        if (specs.isEmpty()) {
            return;
        }
        this.specs = GenericAndJson.objectToJson(specs);
    }
}
