package com.c1eye.server.repository;

import com.c1eye.server.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author c1eye
 * time 2021/10/17 11:09
 */
public interface ActivityRepository extends JpaRepository<Activity,Long> {
    Activity findByName(String name);

    Optional<Activity> findByCouponListId(Long couponId);
}
