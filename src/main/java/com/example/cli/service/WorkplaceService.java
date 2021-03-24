package com.example.cli.service;

import com.example.cli.constant.CommonConstant;
import com.example.cli.constant.CustomerEnum;
import com.example.cli.constant.GoodsTypeEnum;
import com.example.cli.constant.StatusEnum;
import com.example.cli.domain.common.Statistic;
import com.example.cli.entity.SalesOrder;
import com.example.cli.entity.SalesOrderDetails;
import com.example.cli.entity.User;
import com.example.cli.repository.CustomerRepository;
import com.example.cli.repository.SalesOrderRepository;
import com.example.cli.utils.DateUtils;
import com.example.cli.utils.RequestUserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author liaoheng
 * @version 1.0
 * @date 2021/3/18 9:30
 */
@Service
public class WorkplaceService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private SalesOrderRepository salesOrderRepository;

    /**
     * 工作台统计数据
     * @return
     */
    public Statistic getWorkplaceData() {
        User useUser = RequestUserHolder.getUser();

        Statistic statistic = new Statistic();
        List<SalesOrder> salesOrders;

        if (CommonConstant.SUPER_ROLE_ID.equals(useUser.getRole().getRoleId())) {
            statistic.setExCustomerCount(customerRepository.countByTypeAndStatus(CustomerEnum.EXPERIENCE, StatusEnum.USED));
            statistic.setPrCustomerCount(customerRepository.countByTypeAndStatus(CustomerEnum.PROSPECTIVE, StatusEnum.USED));
            statistic.setCustomerCount(customerRepository.countByTypeAndStatus(CustomerEnum.NORMAL, StatusEnum.USED));

            salesOrders = salesOrderRepository.findAllByCreateTimeAfter(DateUtils.getFirstDayOfMonth());
        } else {
            //非超级管理员查询自己新增的客户
            statistic.setExCustomerCount(customerRepository.countByTypeAndCreateUserIdAndStatus(CustomerEnum.EXPERIENCE, useUser.getId(), StatusEnum.USED));
            statistic.setPrCustomerCount(customerRepository.countByTypeAndCreateUserIdAndStatus(CustomerEnum.PROSPECTIVE, useUser.getId(), StatusEnum.USED));
            statistic.setCustomerCount(customerRepository.countByTypeAndCreateUserIdAndStatus(CustomerEnum.NORMAL, useUser.getId(), StatusEnum.USED));


            salesOrders = salesOrderRepository.findAllByCreateUserIdAndCreateTimeAfter(useUser.getId(), DateUtils.getFirstDayOfMonth());

            //收入
            Integer saleNum = salesOrderRepository.getSaleNum(GoodsTypeEnum.SMOKEBOMB.ordinal(), useUser.getId());
            if (null != useUser.getProfit()) {
                BigDecimal income = new BigDecimal(saleNum).multiply(useUser.getProfit());
                statistic.setIncome(income);
            }
        }

        getOrderData(statistic, salesOrders, useUser);

        return statistic;
    }

    public void getOrderData(Statistic statistic, List<SalesOrder> salesOrders, User useUser) {
        if (null == salesOrders || salesOrders.size() == 0) {
            return;
        }

        List<SalesOrderDetails> details = salesOrders.stream()
                .filter(item -> item.getDetailsList() != null && item.getDetailsList().size() > 0)
                .map(SalesOrder::getDetailsList)
                .flatMap(Collection::stream).collect(Collectors.toList());

        if (null == details || details.size() == 0) {
            return;
        }

        Map<GoodsTypeEnum, List<SalesOrderDetails>> goodsTypeMap = details.stream()
                .filter(item -> item.getGoodsType() != null)
                .collect(Collectors.groupingBy(SalesOrderDetails::getGoodsType));

        List<SalesOrderDetails> smokeBombList = goodsTypeMap.get(GoodsTypeEnum.SMOKEBOMB);

        if (null != smokeBombList && smokeBombList.size() > 0) {
            Integer saleNum = smokeBombList.stream().mapToInt(SalesOrderDetails::getAmount).sum();
            statistic.setSmokeBombNum(saleNum);
        }

        List<SalesOrderDetails> sampleList = goodsTypeMap.get(GoodsTypeEnum.SAMPLE);
        if (null != sampleList && sampleList.size() > 0) {
            Integer saleNum = sampleList.stream().mapToInt(SalesOrderDetails::getAmount).sum();
            statistic.setSaleSampleNum(saleNum);
        }

        List<SalesOrderDetails> suitList = goodsTypeMap.get(GoodsTypeEnum.SUIT);
        if (null != suitList && suitList.size() > 0) {
            Integer saleNum = sampleList.stream().mapToInt(SalesOrderDetails::getAmount).sum();
            statistic.setSaleSuitNum(saleNum);
        }

        statistic.setSampleGoodsList(goodsTypeMap.get(GoodsTypeEnum.SAMPLE));
        statistic.setSuitGoodsList(goodsTypeMap.get(GoodsTypeEnum.SUIT));
        statistic.setSmokeBombGoodsList(smokeBombList);
    }
}
