package com.c1eye.server.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author c1eye
 * time 2021/10/9 14:31
 */
@Entity
@Getter
@Setter
public class SpuImg extends BaseEntity{
    @Id
    private Long id;
    private String img;
    private Long spuId;

}
