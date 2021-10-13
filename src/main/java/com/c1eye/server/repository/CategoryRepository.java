package com.c1eye.server.repository;

import com.c1eye.server.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author c1eye
 * time 2021/10/13 17:12
 */
public interface CategoryRepository extends JpaRepository<Category,Long> {

    List<Category> findAllByIsRootOrderByIndexAsc(Boolean isRoot);
}
