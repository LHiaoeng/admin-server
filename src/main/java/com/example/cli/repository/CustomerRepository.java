package com.example.cli.repository;

import com.example.cli.constant.CustomerEnum;
import com.example.cli.constant.StatusEnum;
import com.example.cli.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liaoheng
 * @version 1.0
 * @date 2021/3/11 15:26
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>, JpaSpecificationExecutor<Customer> {
    /**
     * 按客户类型和创建人统计客户数量
     * @param type
     * @param userId
     * @return
     */
    int countByTypeAndCreateUserIdAndStatus(CustomerEnum type, Integer userId, StatusEnum statusEnum);

    /**
     * 按客户类型和创建人统计客户数量
     * @param type
     * @return
     */
    int countByTypeAndStatus(CustomerEnum type, StatusEnum statusEnum);

    /**
     * 根据用户状态查询客户列表
     * @param statusEnum
     * @return
     */
    List<Customer> findAllByStatus(StatusEnum statusEnum);
}
