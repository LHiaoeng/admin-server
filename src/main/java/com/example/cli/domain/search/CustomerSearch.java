package com.example.cli.domain.search;

import lombok.Data;

/**
 * @author liaoheng
 * @version 1.0
 * @date 2021/3/11 15:31
 */
@Data
public class CustomerSearch extends BaseSearch {
    private Integer id;
    private String name;
    private String phone;
    private Integer status;
    private String address;
}
