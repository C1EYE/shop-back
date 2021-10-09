package com.c1eye.server.repository;

import com.c1eye.server.model.Spu;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author c1eye
 * time 2021/10/9 11:15
 */
public interface SpuRepository extends JpaRepository<Spu,Long> {
    Spu findOneById(Long id);
}
