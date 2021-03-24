package com.example.cli.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.example.cli.constant.GoodsTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author liaoheng
 * @version 1.0
 * @date 2021/3/11 23:20
 */
@Data
@Table(name = "sales_order_details")
@Entity
@ExcelTarget("SalesOrderDetails")
public class SalesOrderDetails {
    private static final long serialVersionUID = 1L;

    /**
     * 唯一识别码
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    /**
     * 订单id
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private SalesOrder salesOrder;

    /**
     * 商品id
     */
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "goods_id", updatable = false)
    private Goods goods;

    /**
     * 商品数量
     */
    @Column(name = "amount")
    @Excel(name = "商品数量", isImportField = "true_st")
    private Integer amount;

    /**
     * 商品编号
     */
    @Column(name = "goods_code")
    @Excel(name = "商品编号", width = 20, isImportField = "true_st")
    private String goodsCode;

    /**
     * 商品名称
     */
    @Column(name = "goods_name")
    @Excel(name = "商品名称", width = 20, isImportField = "true_st")
    private String goodsName;

    /**
     * 商品颜色
     */
    @Column(name = "goods_color")
    private String goodsColor;

    /**
     * goodsSize
     */
    @Column(name = "goods_size")
    private String goodsSize;

    /**
     * 商品类型
     */
    @Excel(name = "商品类型", replace = {"试用装_SAMPLE", "套装_SUIT", "烟弹_SMOKEBOMB"}, isImportField = "true_st")
    private GoodsTypeEnum goodsType;

    /**
     * 成本价
     */
    @Excel(name = "成本价", isImportField = "true_st")
    @Column(name = "cost_price", precision = 12, scale = 2)
    private BigDecimal costPrice;

    /**
     * 销售价
     */
    @Excel(name = "销售价", isImportField = "true_st")
    @Column(name = "sale_price", precision = 12, scale = 2)
    private BigDecimal salePrice;
}
