package com.c1eye.server.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author c1eye
 * time 2021/10/13 20:31
 */
@Entity
@Getter
@Setter
@Where(clause = "delete_time is null")
public class GridCategory extends BaseEntity{
    @Id
    private Long id;
    private String title;
    private String img;
    private String name;
    private Long categoryId;
    private Long rootCategoryId;
}
