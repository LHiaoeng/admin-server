package com.example.cli.domain.common;

import com.example.cli.entity.Goods;
import com.example.cli.entity.SalesOrderDetails;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 首页统计数据
 *
 * @author liaoheng
 * @version 1.0
 * @date 2021/3/16 11:36
 */
@Data
public class Statistic {
    /**
     * 体验用户数量
     */
    private Integer exCustomerCount;
    /**
     * 准客户
     */
    private Integer prCustomerCount;
    /**
     * 正常客户
     */
    private Integer CustomerCount;

    /**
     * 收入
     */
    private BigDecimal income = BigDecimal.ZERO;

    /**
     * 卖出的体验装
     */
    private List<SalesOrderDetails> sampleGoodsList;

    /**
     * 卖出的套装
     */
    private List<SalesOrderDetails> suitGoodsList;

    /**
     * 卖出的烟弹
     */
    private List<SalesOrderDetails> smokeBombGoodsList;

    private Integer saleSampleNum = 0;

    private Integer saleSuitNum = 0;

    private Integer smokeBombNum = 0;
}
