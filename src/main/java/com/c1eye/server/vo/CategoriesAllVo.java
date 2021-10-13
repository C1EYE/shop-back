package com.c1eye.server.vo;

import com.c1eye.server.model.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author c1eye
 * time 2021/10/13 17:11
 */
@Getter
@Setter
public class CategoriesAllVo {

    private List<CategoryPureVO> roots;
    private List<CategoryPureVO> subs;

    public CategoriesAllVo(Map<Integer,List<Category>> map) {
        this.roots = map.get(1).stream().map(CategoryPureVO::new).collect(Collectors.toList());
        this.subs = map.get(2).stream().map(CategoryPureVO::new).collect(Collectors.toList());
    }
}
