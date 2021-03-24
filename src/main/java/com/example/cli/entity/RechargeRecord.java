package com.example.cli.entity;

import com.example.cli.constant.RechargeEunm;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liaoheng
 * @version 1.0
 * @date 2021/3/23 11:56
 */
@Data
@Table(name = "recharge_record")
@Entity
public class RechargeRecord {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户id
     */
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * 用户账号
     */
    @Column(name = "account", updatable = false)
    private String account;

    /**
     * 充值类型
     * 0余额充值
     */
    private RechargeEunm rechargeType;

    /**
     * 充值金额
     */
    @Column(name = "recharge_amount", updatable = false)
    private BigDecimal rechargeAmount;

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
}
