package com.c1eye.server.service;

import com.c1eye.server.core.enumeration.CouponStatus;
import com.c1eye.server.exception.http.NotFoundException;
import com.c1eye.server.exception.http.ParameterException;
import com.c1eye.server.model.Activity;
import com.c1eye.server.model.Coupon;
import com.c1eye.server.model.UserCoupon;
import com.c1eye.server.repository.ActivityRepository;
import com.c1eye.server.repository.CouponRepository;
import com.c1eye.server.repository.UserCouponRepository;
import com.c1eye.server.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    public List<Coupon> getByCategory(Long cid) {
        Date now = new Date();
        return couponRepository.findByCategory(cid, now);
    }

    public List<Coupon> getWholeStoreCoupons() {
        Date now = new Date();
        return couponRepository.findByWholeStore(true, now);
    }

    //
//    public List<Coupon> getMyAvailableCoupons(Long uid) {
//        Date now = new Date();
//        return this.couponRepository.findMyAvailable(uid, now);
//    }
//
//    public List<Coupon> getMyUsedCoupons(Long uid) {
//        Date now = new Date();
//        return this.couponRepository.findMyUsed(uid, now);
//    }
//
//    public List<Coupon> getMyExpiredCoupons(Long uid) {
//        Date now = new Date();
//        return this.couponRepository.findMyExpired(uid, now);
//    }
//
    public void collectOneCoupon(Long uid, Long couponId) {
        // 确认优惠券是否合法
        this.couponRepository
                .findById(couponId)
                .orElseThrow(() -> new NotFoundException(40003));
        // 确认活动是否过期
        Activity activity = this.activityRepository
                .findByCouponListId(couponId)
                .orElseThrow(() -> new NotFoundException(40010));

        Date now = new Date();
        Boolean isIn = CommonUtils.isInTimeLine(now, activity.getStartTime(), activity.getEndTime());
        if (!isIn) {
            throw new ParameterException(40005);
        }

        this.userCouponRepository
                .findFirstByUserIdAndCouponId(uid, couponId)
                .ifPresent((uc) -> {throw new ParameterException(40006);});

        UserCoupon userCouponNew = UserCoupon.builder()
                                             .userId(uid)
                                             .couponId(couponId)
                                             .status(CouponStatus.AVAILABLE.getValue())
                                             .createTime(now)
                                             .build();
        userCouponRepository.save(userCouponNew);
    }

    public List<Coupon> getMyAvailableCoupons(Long uid) {
        Date now = new Date();
        return couponRepository.findMyAvailable(uid, now);
    }

    public List<Coupon> getMyUsedCoupons(Long uid) {
        Date now = new Date();
        return couponRepository.findMyUsed(uid, now);
    }

    public List<Coupon> getMyExpiredCoupons(Long uid) {
        Date now = new Date();
        return couponRepository.findMyExpired(uid, now);
    }
}