package com.c1eye.server.repository;

import com.c1eye.server.model.Sku;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author c1eye
 * time 2021/10/20 15:24
 */
public interface SkuRepository extends JpaRepository<Sku,Long> {
    List<Sku> findAllByIdIn(List<Long> skuIds);
}
