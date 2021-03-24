package com.example.cli.domain.search;

import lombok.Data;

/**
 * @author wjw
 */
@Data
public class RoleSearch extends BaseSearch {

    private String name;

    private Integer status;
}
