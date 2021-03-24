package com.example.cli.constant;

/**
 * 库存类型枚举
 * @author liaoheng
 * @version 1.0
 * @date 2021/3/20 14:47
 */
public enum StockTypeEnum {
    /**
     * 进货入库
     */
    STOCK_IN,
    /**
     * 退货入库
     */
    RETURN_IN,
    /**
     * 订单出库
     */
    ORDER_OUT,
    /**
     * 其他出库
     */
    OTHER_OUT
}
