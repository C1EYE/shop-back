package com.c1eye.server.repository;

import com.c1eye.server.core.enumeration.CouponStatus;
import com.c1eye.server.model.Coupon;
import com.c1eye.server.model.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * @author c1eye
 * time 2021/10/18 19:52
 */
public interface UserCouponRepository extends JpaRepository<UserCoupon,Long> {

    Optional<UserCoupon> findFirstByUserIdAndCouponIdAndStatus(Long uid, Long couponId, CouponStatus status);

    Optional<UserCoupon> findFirstByUserIdAndCouponId(Long uid,Long coupon);
    @Modifying
    @Query("update UserCoupon uc\n" +
            "set uc.status = 2, uc.orderId = :oid\n" +
            "where uc.userId = :uid\n" +
            "and uc.couponId = :couponId\n" +
            "and uc.status = 1\n" +
            "and uc.orderId is null")
    int writeOff(Long couponId, Long oid, Long uid);

    @Query("update UserCoupon c\n" +
            "set c.status  = 1,c.orderId = null \n" +
            "where c.couponId = :cid\n" +
            "and c.userId = :uid\n" +
            "and c.orderId is not null\n" +
            "and c.status = 2")
    @Modifying
    int returnBack(Long cid,Long uid);
}
