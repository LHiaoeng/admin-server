package com.example.cli.domain.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author liwei
 * @title: LoginForm
 * @projectName luwu
 * @description: TODO
 * @date 2019-10-21 18:07
 */
@Data
@ApiModel(value="登录对象",description="用户登录对象")
public class LoginForm {

    private String username;

    private String password;

    private String mobile;

    private String captcha;

    private String rememberMe;

    /**
     * login type: 0 email, 1 username, 2 telephone
     */
    @ApiModelProperty(value = "登录方式", name = "loginType", required = true)
    @NotNull(message = "登录方式不能为空")
    private Integer loginType;
}
