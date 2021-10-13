package com.c1eye.server.service;

import com.c1eye.server.model.Category;
import com.c1eye.server.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author c1eye
 * time 2021/10/13 18:36
 */
@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Map<Integer, List<Category>> getAll(){
        List<Category> roots = categoryRepository.findAllByIsRootOrderByIndexAsc(true);
        List<Category> subs = categoryRepository.findAllByIsRootOrderByIndexAsc(false);
        Map<Integer, List<Category>> categories = new HashMap<>();
        categories.put(1, roots);
        categories.put(2, subs);
        return categories;
    }
}
