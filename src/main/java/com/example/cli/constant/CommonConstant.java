package com.example.cli.constant;


/**
 * 常量
 *
 * @author Exrickx
 */
public interface CommonConstant {
    /**
     * 鉴权异常
     */
    Integer AUTH_EXCEPTION = 400001;

    /**
     * 参数校验错误
     */
    Integer PARAM_EXCEPTION = 400000;

    /**
     * 其他异常
     */
    Integer EXCEPTION = 500000;

    /**
     * 登录异常
     */
    Integer LOGIN_EXCEPTION = 500001;

    /**
     * 超级管理员权限标志
     */
    String SUPER_ROLE_ID = "admin";

    /**
     * 每月默认天数
     */
    Integer DEFAULT_DAYS = 30;
}
