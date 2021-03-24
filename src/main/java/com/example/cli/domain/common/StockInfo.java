package com.example.cli.domain.common;

import com.example.cli.constant.StockTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author liaoheng
 * @version 1.0
 * @date 2021/3/11 10:41
 */
@Data
public class StockInfo {
    @NotNull(message = "库存数不能为空")
    private Integer stockNum;
    @NotNull(message = "商品id不能为空")
    private Integer goodsId;
    private String memo;

    /**
     * 库存类型
     */
    @NotNull(message = "库存类型不能为空")
    private StockTypeEnum stockType;
}
