package com.c1eye.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * @author c1eye
 * time 2021/10/7 11:27
 */
@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {
    @JsonIgnore
    @Column(insertable=false, updatable=false)
    private Date createTime;
    @JsonIgnore
    @Column(insertable=false, updatable=false)
    private Date updateTime;
    @JsonIgnore
    private Date deleteTime;
}
