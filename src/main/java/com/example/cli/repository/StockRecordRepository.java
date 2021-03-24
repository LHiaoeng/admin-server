package com.example.cli.repository;

import com.example.cli.entity.StockRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author liaoheng
 * @version 1.0
 * @date 2021/3/11 9:49
 */
@Repository
public interface StockRecordRepository extends JpaRepository<StockRecord, Integer>, JpaSpecificationExecutor<StockRecord> {

}
