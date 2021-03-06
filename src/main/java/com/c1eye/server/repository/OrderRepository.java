package com.c1eye.server.repository;

import com.c1eye.server.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

/**
 * @author c1eye
 * time 2021/10/23 11:12
 */

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByExpiredTimeGreaterThanAndStatusAndUserId(Date now, Integer status, Long uid, Pageable pageable);

    Page<Order> findByUserId(Long uid, Pageable pageable);

    /**
     * 这个接口不能查询不明确的状态（unpaid，canceled）
     *
     * @return
     */
    Page<Order> findByUserIdAndStatus(
            Long userId, Integer status, Pageable pageable);

    Optional<Order> findFirstByUserIdAndId(Long uid, Long oid);

    Optional<Order> findFirstByOrderNo(String orderNo);

    @Modifying
    @Query("update Order o set o.status=:status where o.orderNo=:orderNo")
    int updateStatusByOrderNo(String orderNo, Integer status);

    @Modifying
    @Query("update Order o set o.status = 5 where o.status = 1 and o.id=:oid")
    int cancelOrder(Long oid);
}
