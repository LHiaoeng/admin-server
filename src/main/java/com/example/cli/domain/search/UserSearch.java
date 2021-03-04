package com.example.cli.domain.search;

import lombok.Data;

/**
 * @author wjw
 */
@Data
public class UserSearch extends BaseSearch {

    private String name;

    private String email;

    private String roleId;

    private Integer status;

    private String userName;

    private String phone;
}
