package com.c1eye.server.api.v1;

import com.c1eye.server.exception.http.NotFoundException;
import com.c1eye.server.model.Category;
import com.c1eye.server.model.GridCategory;
import com.c1eye.server.service.CategoryService;
import com.c1eye.server.service.GridCategoryService;
import com.c1eye.server.vo.CategoriesAllVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author c1eye
 * time 2021/10/13 16:49
 */
@RequestMapping("category")
@RestController
@ResponseBody
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private GridCategoryService gridCategoryService;
    @GetMapping("/all")
    public CategoriesAllVo getAll(){
        Map<Integer, List<Category>> categories= categoryService.getAll();
        return new CategoriesAllVo(categories);
    }

    @GetMapping("/grid/all")
    public List<GridCategory> getGridCategoryList(){
        List<GridCategory> gridCategoryList = gridCategoryService.getGridCategoryList();
        if (gridCategoryList.isEmpty()){
            throw new NotFoundException(30009);
        }
        return gridCategoryList;
    }

}
