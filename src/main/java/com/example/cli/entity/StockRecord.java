package com.example.cli.entity;

import com.example.cli.constant.StockTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author liaoheng
 * @version 1.0
 * @date 2021/3/11 9:34
 */
@Data
@Table(name = "stock_record")
@Entity
public class StockRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 唯一识别码
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 商品id
     */
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "goods_id")
    @JsonIgnore
    private Goods goods;

    /**
     * 商品编码
     */
    @Column(name = "goods_code", updatable = false)
    private String goodsCode;

    /**
     * 商品编码
     */
    @Column(name = "goods_name", updatable = false)
    private String goodsName;

    /**
     * 入库数量
     */
    @Column(name = "stock_in_num", updatable = false, columnDefinition = "int default 0")
    private Integer stockInNum;

    /**
     * 出库数量
     */
    @Column(name = "stock_out_num", updatable = false, columnDefinition = "int default 0")
    private Integer stockOutNum;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;

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
     * 库存类型
     */
    private StockTypeEnum stockType;
}
