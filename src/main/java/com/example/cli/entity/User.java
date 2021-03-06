package com.example.cli.entity;

import com.example.cli.constant.DeletedEnum;
import com.example.cli.constant.SalesmanEnum;
import com.example.cli.constant.StatusEnum;
import com.example.cli.constant.TableColumn;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wjw
 */
@Data
@Table(name = "user")
@Entity
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    /***/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableColumn(title = "唯一识别码")
    private Integer id;

    /***/
    @Column(name = "name")
    @TableColumn(title = "用户名")
    private String name;

    /**
     * 密码
     */
    @Column(name = "password")
    @JsonIgnore
    private String password;

    /**
     * 邮箱
     */
    @Column(name = "email")
    @TableColumn(title = "邮箱")
    private String email;

    /**
     * 手机号
     */
    @Column(name = "phone")
    @TableColumn(title = "手机号")
    private String phone;

    /**
     * 名称
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 头像
     */
    @Column(name = "avatar")
    private String avatar;


    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "role_user", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonIgnoreProperties({"users", "menus"})
    private Role role;


    @Column(name = "last_login_ip")
    private String lastLoginIp;

    @Column(name = "last_login_time")
    private String lastLoginTime;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "create_id")
    @JsonIgnore
    private User createUser;

    @Column(name = "create_time")
    @TableColumn(title = "创建时间", scopeSlots = "createTime")
    private Date createTime;

    /**
     * 0 使用 1 禁用
     */
    @TableColumn(title = "状态")
    private StatusEnum status;
    /**
     * 0 未删除 1 已删除
     */
    private DeletedEnum deleted;

    /**
     * 利润
     */
    @Column(name = "profit", precision = 12, scale = 2)
    private BigDecimal profit;

    @Transient
    private Integer roleId;

    @Transient
    private String createUserName;

    /**
     * 业务员等级
     */
    @Column(name = "salesman_grade")
    private SalesmanEnum salesmanGrade;

    /**
     * 总已付款
     * 累计已支付的金额
     */
    @Column(name = "actual_price", precision = 12, scale = 2, updatable = false)
    private BigDecimal actualPrice;
}
