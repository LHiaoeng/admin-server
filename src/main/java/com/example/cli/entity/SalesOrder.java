package com.example.cli.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author liaoheng
 * @version 1.0
 * @date 2021/3/11 22:50
 */
@Data
@Table(name = "sales_order")
@Entity
@ExcelTarget("SalesOrder")
public class SalesOrder {
    private static final long serialVersionUID = 1L;

    /**
     * 唯一识别码
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 订单编号
     */
    @Excel(name = "订单编号", width = 30, isImportField = "true_st")
    @Column(name = "order_no", updatable = false, unique = true)
    private String orderNo;

    /**
     * 商品总数
     */
    @Excel(name = "商品总数", width = 30, isImportField = "true_st")
    @Column(name = "total_number")
    private Integer totalNumber;

    /**
     * 订单类型
     * 0体验；1试用；2正常
     */
    @Column(name = "order_type")
    private Integer orderType;

    /**
     * 订单状态
     * 0正常；1作废
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 客户订货时间
     */
    @Column(name = "customer_order_time")
    private Date customerOrderTime;

    /**
     * 创建人id
     */
    @Column(name = "create_user_id", updatable = false)
    private Integer createUserId;

    /**
     * 创建人
     */
    @Column(name = "create_username", updatable = false)
    private String createUsername;

    /**
     * 创建时间
     */
    @Column(name = "create_time", updatable = false)
    private Date createTime;

    /**
     * 编辑人id
     */
    @Column(name = "edit_user_id")
    private Integer editUserId;

    /**
     * 编辑人
     */
    @Column(name = "edit_username")
    private String editUsername;

    /**
     * 创建时间
     */
    @Column(name = "edit_time")
    private Date editTime;

    /**
     * 客户id
     */
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "customer_id", updatable = false)
    private Customer customer;

    /**
     * 订单明细集合
     */
    @ExcelCollection(name = "订单明细")
    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER, mappedBy = "salesOrder")
    private List<SalesOrderDetails> detailsList;

    /**
     * 运单号
     */
    @Column(name = "waybill_no")
    private String waybillNo;

    /**
     * 回传时间
     */
    @Column(name = "waybill_time")
    private Date waybillTime;

    /**
     * 订单合计金额
     */
    @Column(name = "total_price", precision = 12, scale = 2)
    private BigDecimal totalPrice;

    /**
     * 应付款
     */
    @Excel(name = "应付款", isImportField = "true_st")
    @Column(name = "payable_price", precision = 12, scale = 2)
    private BigDecimal payablePrice;

    /**
     * 实付款
     */
    @Column(name = "actual_price", precision = 12, scale = 2)
    private BigDecimal actualPrice;
}
