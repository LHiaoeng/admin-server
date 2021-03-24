package com.example.cli.service;

import com.example.cli.constant.CommonConstant;
import com.example.cli.constant.RechargeEunm;
import com.example.cli.domain.common.FinanceData;
import com.example.cli.domain.common.PageInfo;
import com.example.cli.domain.search.FinanceSearch;
import com.example.cli.entity.RechargeRecord;
import com.example.cli.entity.SalesOrder;
import com.example.cli.entity.User;
import com.example.cli.exception.BaseException;
import com.example.cli.repository.RechargeRecordRepository;
import com.example.cli.repository.SalesOrderRepository;
import com.example.cli.repository.UserRepository;
import com.example.cli.utils.DateUtils;
import com.example.cli.utils.PageUtils;
import com.example.cli.utils.RequestUserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liaoheng
 * @version 1.0
 * @date 2021/3/22 17:08
 */
@Service
@Slf4j
public class FinanceService {
    @Autowired
    private SalesOrderRepository salesOrderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RechargeRecordRepository rechargeRecordRepository;

    /**
     * 查询财务数据
     *
     * @param baseSearch
     * @return
     */
    public Object getFinanceData(FinanceSearch baseSearch) {
        try {
            User useUser = RequestUserHolder.getUser();

            List<SalesOrder> orderList;

            if (!CommonConstant.SUPER_ROLE_ID.equals(useUser.getRole().getRoleId())) {
                orderList = salesOrderRepository.findAllByCreateUserId(useUser.getId());
            } else {
                orderList = salesOrderRepository.findAll();
            }

            if (null == orderList || orderList.size() == 0) {
                return PageUtils.empty();
            }

            Map<Integer, List<SalesOrder>> orderGroupMap = orderList.stream().collect(Collectors.groupingBy(SalesOrder::getCreateUserId));

            List<FinanceData> financeDataList = new ArrayList<>();

            for (Map.Entry<Integer, List<SalesOrder>> entry : orderGroupMap.entrySet()) {
                User user = userRepository.getOne(entry.getKey());

                FinanceData financeData = new FinanceData();
                financeData.setUserId(entry.getKey());
                financeData.setName(user.getName());
                financeData.setAllPaid(user.getActualPrice());

                BigDecimal allAmount = entry.getValue().stream()
                        .filter(d -> Objects.nonNull(d.getTotalPrice()))
                        .map(SalesOrder::getTotalPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                financeData.setAllAmount(allAmount);

                BigDecimal allPayable = entry.getValue().stream()
                        .filter(d -> Objects.nonNull(d.getPayablePrice()))
                        .map(SalesOrder::getPayablePrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                financeData.setAllPayable(allPayable);


                if (null == user.getActualPrice()) {
                    financeData.setAllPaid(BigDecimal.ZERO);
                } else {
                    financeData.setAllPaid(user.getActualPrice());
                }

                financeDataList.add(financeData);
            }

            if (null != baseSearch.getUserId()) {
                Integer userId = Integer.parseInt(baseSearch.getUserId());
                financeDataList = financeDataList.stream().filter(item -> item.getUserId().equals(userId)).collect(Collectors.toList());
            }

            if (null != baseSearch.getUserName()) {
                financeDataList = financeDataList.stream().filter(item -> item.getName().equals(baseSearch.getUserName())).collect(Collectors.toList());
            }

            if (null == financeDataList || financeDataList.size() == 0) {
                return PageUtils.empty();
            }

            //分不分页
            if (baseSearch.getPageNo() == 0 && baseSearch.getPageSize() == 0) {
                return financeDataList;
            }

            financeDataList = financeDataList.stream().skip(baseSearch.getPageNo() - 1).limit(baseSearch.getPageSize()).collect(Collectors.toList());

            financeDataList.sort((o1, o2)
                    -> o1.getAllPayable() == null ? 1 : (o2.getAllPayable() == null ? -1 : o2.getAllPayable().compareTo(o1.getAllPayable())));

            PageInfo pageInfo = new PageInfo();
            pageInfo.setPageNo(baseSearch.getPageNo());
            pageInfo.setTotalCount(financeDataList.size());
            pageInfo.setData(financeDataList);
            return pageInfo;
        } catch (Exception e) {
            log.error("查询账单", e);
            return PageUtils.empty();
        }
    }

    /**
     * 为指定用户充值a
     *
     * @param record
     */
    @Transactional(rollbackFor = Exception.class)
    public void recharge(RechargeRecord record) {
        User user = RequestUserHolder.getUser();

        User salesman = userRepository.findById(record.getUser().getId()).get();
        if (null == salesman) {
            throw new BaseException(CommonConstant.EXCEPTION, "用户不存在");
        }

        int ret = userRepository.updateActualPrice(record.getRechargeAmount(), salesman.getId());
        if (ret <= 0) {
            throw new BaseException(CommonConstant.EXCEPTION, "充值失败");
        }

        Date nowDate = new Date();
        record.setAccount(salesman.getName());
        record.setCreateUserId(user.getId());
        record.setCreateUsername(user.getName());
        record.setCreateTime(nowDate);
        record.setRechargeType(RechargeEunm.BALANCE);

        rechargeRecordRepository.saveAndFlush(record);

        log.info("{},{}（用户编码：{}）给用户{}（用户编码：{}）充值：{}；备注：{}", DateUtils.date2String(nowDate), user.getName(), user.getId(),
                salesman.getName(), salesman.getId(), record.getRechargeAmount(), record.getMemo());
    }
}
