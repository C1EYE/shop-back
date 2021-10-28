package com.c1eye.server.repository;

import com.c1eye.server.model.Sku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author c1eye
 * time 2021/10/20 15:24
 */
public interface SkuRepository extends JpaRepository<Sku,Long> {
    List<Sku> findAllByIdIn(List<Long> skuIds);

    @Modifying
    @Query("update Sku s\n" +
            "set s.stock = s.stock - :quantity\n" +
            "where s.id = :sid\n" +
            "and s.stock >= :quantity")
    int reduceStock(Long sid, Long quantity);
}
