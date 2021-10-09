package com.c1eye.server.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

/**
 * @author c1eye
 * time 2021/10/7 10:45
 */
@Entity
@Getter
@Setter
public class BannerItem extends BaseEntity{
    @Id
    private Long id;
    private String img;
    private String keyword;
    private Short type;
    private Long bannerId;
    private String name;
}
