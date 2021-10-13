package com.c1eye.server.vo;

import com.c1eye.server.model.Category;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

/**
 * @author c1eye
 * time 2021/10/13 19:03
 */
@Getter
@Setter
public class CategoryPureVO {
    private Long id;

    private String name;

    private Boolean isRoot;

    private String img;

    private Long parentId;

    private Long index;

    public CategoryPureVO(Category category) {
        BeanUtils.copyProperties(category, this);
    }
}
