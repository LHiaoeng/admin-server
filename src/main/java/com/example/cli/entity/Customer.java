package com.example.cli.entity;

import com.example.cli.constant.CustomerEnum;
import com.example.cli.constant.StatusEnum;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liaoheng
 * @version 1.0
 * @date 2021/3/11 15:10
 */
@Data
@Table(name = "customer")
@Entity
public class Customer {
    private static final long serialVersionUID = 1L;

    /**
     * 唯一识别码
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 客户名称
     */
    @Column(name="name")
    private String name;

    /**
     * 客户地址
     */
    @Column(name="address")
    private String address;

    /**
     * 客户手机号码
     */
    @Column(name="phone")
    private String phone;

    /**
     * 月用量
     */
    @Column(name="month_usage", scale = 1)
    private BigDecimal monthUsage;

    /**
     * 剩余量
     */
    @Column(name="surplus", scale = 1)
    private BigDecimal surplus;

    /**
     * 剩余时间
     * 剩余量/(月用量/30天) = 剩余时间(保留一位小数）
     */
    @Column(name="time_remaining", scale = 1)
    private BigDecimal timeRemaining;

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
     * 0 使用 1 禁用
     */
    private StatusEnum status;

    /**
     * 0 体验客户；1准客户；2正常客户
     */
    private CustomerEnum type;
}
