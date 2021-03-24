package com.example.cli.service;

import com.example.cli.constant.CommonConstant;
import com.example.cli.domain.search.RechargeSearch;
import com.example.cli.entity.*;
import com.example.cli.repository.RechargeRecordRepository;
import com.example.cli.utils.PageUtils;
import com.example.cli.utils.RequestUserHolder;
import lombok.extern.slf4j.Slf4j;
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
public class RechargeService {
    @Autowired
    private RechargeRecordRepository rechargeRecordRepository;

    /**
     * 按条件查询充值记录
     *
     * @param baseSearch
     * @return
     */
    public Object getAll(RechargeSearch baseSearch) {
        Specification<RechargeRecord> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            User useUser = RequestUserHolder.getUser();

            //非超级管理员查询自己的充值记录
            if (!CommonConstant.SUPER_ROLE_ID.equals(useUser.getRole().getRoleId())) {
                predicates.add(criteriaBuilder.equal(root.get("account"), useUser.getName()));
            }

            if (StringUtils.hasLength(baseSearch.getAccount())) {
                predicates.add(criteriaBuilder.like(root.get("account"), "%" + baseSearch.getAccount() + "%"));
            }

            if (null != baseSearch.getUserId()) {
                Join<RechargeRecord, User> join = root.join("customer", JoinType.LEFT);
                predicates.add(criteriaBuilder.equal(join.get("id"), baseSearch.getUserId()));
            }

            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };

        if (baseSearch.getPageNo() == 0 && baseSearch.getPageSize() == 0) {
            return rechargeRecordRepository.findAll(specification);
        } else {
            Pageable pageable = PageRequest.of(baseSearch.getPageNo() - 1, baseSearch.getPageSize(), baseSearch.getSort());
            return PageUtils.getPageInfo(rechargeRecordRepository.findAll(specification, pageable), RechargeRecord.class);
        }
    }

}
