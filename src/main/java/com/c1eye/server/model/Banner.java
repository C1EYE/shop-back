package com.c1eye.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author c1eye
 * time 2021/10/7 10:45
 */
@Entity
@Getter
@Setter
public class Banner extends BaseEntity {
    @Id
    private Long id;
    private String name;
    private String description;
    private String title;
    private String img;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "bannerId")
    private List<BannerItem> items;

}
