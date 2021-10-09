package com.c1eye.server.repository;

import com.c1eye.server.model.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author c1eye
 * time 2021/10/5 10:52
 */
public interface BannerRepository extends JpaRepository<Banner, Long> {

    Banner findBannerByName(String name);

}
