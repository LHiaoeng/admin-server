package com.example.cli.domain.search;

import lombok.Data;

/**
 * @author liaoheng
 * @version 1.0
 * @date 2021/3/12 0:03
 */
@Data
public class SalesOrderSearch extends BaseSearch {
    private String orderNo;
    private Integer customerId;
    private String customerName;
    private String goodsCode;
    private String goodsName;
    private String goodsColor;
    private Integer status;
}
