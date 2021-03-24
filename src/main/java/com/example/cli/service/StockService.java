package com.example.cli.service;

import com.example.cli.constant.CommonConstant;
import com.example.cli.constant.Role2AllOrderEnum;
import com.example.cli.domain.search.RechargeSearch;
import com.example.cli.domain.search.StockSearch;
import com.example.cli.entity.RechargeRecord;
import com.example.cli.entity.StockRecord;
import com.example.cli.entity.User;
import com.example.cli.repository.RechargeRecordRepository;
import com.example.cli.repository.StockRecordRepository;
import com.example.cli.utils.PageUtils;
import com.example.cli.utils.RequestUserHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liaoheng
 * @version 1.0
 * @date 2021/3/11 2:22
 */
@Service
@Slf4j
public class StockService {
    @Autowired
    private StockRecordRepository stockRecordRepository;

    /**
     * 按条件查询库存记录
     *
     * @param baseSearch
     * @return
     */
    public Object getAll(StockSearch baseSearch) {


        Specification<StockRecord> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasLength(baseSearch.getGoodsCode())) {
                predicates.add(criteriaBuilder.equal(root.get("goodsCode"), baseSearch.getGoodsCode()));
            }

            if (StringUtils.hasLength(baseSearch.getGoodsName())) {
                predicates.add(criteriaBuilder.like(root.get("goodsName"), "%" + baseSearch.getGoodsName() + "%"));
            }

            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };

        if (baseSearch.getPageNo() == 0 && baseSearch.getPageSize() == 0) {
            return stockRecordRepository.findAll(specification);
        } else {
            Pageable pageable = PageRequest.of(baseSearch.getPageNo() - 1, baseSearch.getPageSize(), baseSearch.getSort());
            return PageUtils.getPageInfo(stockRecordRepository.findAll(specification, pageable), StockRecord.class);
        }
    }

}
