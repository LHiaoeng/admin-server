package com.example.cli.domain.search;

import lombok.Data;

/**
 * @author liaoheng
 * @version 1.0
 * @date 2021/3/23 10:47
 */
@Data
public class FinanceSearch extends BaseSearch {
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户账号
     */
    private String userName;
}
