package com.example.cli.domain.search;

import lombok.Data;

/**
 * @author liaoheng
 * @version 1.0
 * @date 2021/3/11 2:34
 */
@Data
public class GoodsSearch extends BaseSearch {
    private String goodsName;
    private String goodsColor;
    private String goodsType;
    private String goodsCode;
    private Integer status;
    private Boolean isStock;
}
