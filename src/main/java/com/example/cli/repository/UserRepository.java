package com.example.cli.repository;


import com.example.cli.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    User findByName(String name);

    User findByEmail(String email);

    User findByPhone(String phone);

    /**
     * 查询手机是否存在
     *
     * @param phone
     * @return
     */
    @Query(value = "select coalesce(sum(u.id),0 ) from User u where u.deleted = 0 and u.phone = ?1")
    Integer findPhoneNum(String phone);

    /**
     * 查询账号是否存在
     *
     * @param name
     * @return
     */
    @Query(value = "select coalesce(sum(u.id),0 ) from User u where u.deleted = 0 and u.name = ?1")
    Integer findNameNum(String name);

    /**
     * 更新用户的总已付款金额
     *
     * @param recharge
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE user SET actual_price = ifnull(actual_price,0.00) + ?1 WHERE id = ?2", nativeQuery = true)
    int updateActualPrice(BigDecimal recharge, Integer userId);
}
