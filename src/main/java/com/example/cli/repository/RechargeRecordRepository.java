package com.example.cli.repository;

import com.example.cli.entity.RechargeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author liaoheng
 * @version 1.0
 * @date 2021/3/23 14:34
 */
@Repository
public interface RechargeRecordRepository extends JpaRepository<RechargeRecord, Integer>, JpaSpecificationExecutor<RechargeRecord> {
}
