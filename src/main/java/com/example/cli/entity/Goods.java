package com.example.cli.entity;

import com.example.cli.constant.GoodsTypeEnum;
import com.example.cli.constant.StatusEnum;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liaoheng
 * @version 1.0
 * @date 2021/3/11 1:13
 */
@Data
@Table(name = "goods")
@Entity
public class Goods {

    /**
     * 唯一识别码
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 商品编码
     */
    @Column(name = "code", unique = true, nullable = false, length = 20, updatable = false)
    private String code;

    /**
     * 商品名称
     */
    @Column(name = "goods_name")
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
    private GoodsTypeEnum goodsType;

    /**
     * 0 使用 1 禁用
     */
    private StatusEnum status;

    /**
     * 库存
     */
    @Column(name = "stock", updatable = false, columnDefinition = "int default 0")
    private Integer stock = 0;

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
     * 成本价
     */
    @Column(name = "price", precision = 12, scale = 2)
    private BigDecimal price;

    /**
     * 销售经理销售价
     */
    @Column(name = "manager_price", precision = 12, scale = 2)
    private BigDecimal managerPrice;

    /**
     * 销售主任销售价
     */
    @Column(name = "director_price", precision = 12, scale = 2)
    private BigDecimal directorPrice;

    /**
     * 订单购买数量
     */
    @Transient
    private Integer buyNum;
}
