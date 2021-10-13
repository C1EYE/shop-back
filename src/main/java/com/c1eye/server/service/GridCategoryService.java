package com.c1eye.server.service;

import com.c1eye.server.model.GridCategory;
import com.c1eye.server.repository.GridCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author c1eye
 * time 2021/10/13 20:38
 */
@Service
public class GridCategoryService {
    @Autowired
    private GridCategoryRepository gridCategoryRepository;
    public List<GridCategory> getGridCategoryList(){
        return gridCategoryRepository.findAll();
    }
}
