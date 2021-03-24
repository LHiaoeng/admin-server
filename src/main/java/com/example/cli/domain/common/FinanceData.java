package com.example.cli.domain.common;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liaoheng
 * @version 1.0
 * @date 2021/3/23 9:55
 */
@Data
public class FinanceData {
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 用户账号
     */
    private String name;

    /**
     * 总订单金额
     */
    private BigDecimal allAmount;

    /**
     * 总应付金额
     */
    private BigDecimal allPayable;

    /**
     * 总已付金额
     */
    private BigDecimal allPaid;

}
