package com.example.cli.utils;


import com.example.cli.constant.CommonConstant;
import com.example.cli.entity.User;
import com.example.cli.exception.BaseException;

public class RequestUserHolder {

    private final static ThreadLocal<User> requestHolder = new ThreadLocal<>();

    public static void add(User info) {
        requestHolder.set(info);
    }

    public static User getUser() {
        User user = requestHolder.get();
        if (null == user) {
            throw new BaseException(CommonConstant.LOGIN_EXCEPTION, "登录异常，请重新登录");
        }

        return requestHolder.get();
    }

    public static void remove() {
        requestHolder.remove();
    }


}
