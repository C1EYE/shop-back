package com.c1eye.server.bo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author c1eye
 * time 2021/10/10 10:42
 */
@Getter
@Setter
@Builder
public class PageCounter {
    private Integer page;
    private Integer count;
}
