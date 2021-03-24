package com.example.cli.domain.search;

import lombok.Data;

/**
 * @author liaoheng
 * @version 1.0
 * @date 2021/3/23 10:47
 */
@Data
public class StockSearch extends BaseSearch {
    /**
     * 商品编码
     */
    private String goodsCode;

    /**
     * 商品名称
     */
    private String goodsName;
}
