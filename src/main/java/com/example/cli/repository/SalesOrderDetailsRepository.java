package com.example.cli.repository;

import com.example.cli.entity.SalesOrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author liaoheng
 * @version 1.0
 * @date 2021/3/12 16:56
 */
@Repository
public interface SalesOrderDetailsRepository extends JpaRepository<SalesOrderDetails, Integer>, JpaSpecificationExecutor<SalesOrderDetails> {
}
