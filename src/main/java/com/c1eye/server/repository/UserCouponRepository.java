package com.c1eye.server.repository;

import com.c1eye.server.model.Coupon;
import com.c1eye.server.model.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author c1eye
 * time 2021/10/18 19:52
 */
public interface UserCouponRepository extends JpaRepository<UserCoupon,Long> {

    Optional<Coupon> findFirstByUserIdAndCouponId(Long uid, Long couponId);
}
