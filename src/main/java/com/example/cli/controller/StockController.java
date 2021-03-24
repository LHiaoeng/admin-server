package com.example.cli.controller;

import com.example.cli.domain.common.ResponseBean;
import com.example.cli.domain.search.StockSearch;
import com.example.cli.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liaoheng
 * @version 1.0
 * @date 2021/3/23 11:33
 */
@RestController
@RequestMapping("/stock")
public class StockController {
    @Autowired
    private StockService stockService;

    /**
     * 分页或不分页获取库存记录
     * @param search
     * @return
     */
    @GetMapping("/getStockRecordList")
    public ResponseBean getRechargeList(StockSearch search) {
        return new ResponseBean(stockService.getAll(search));
    }

}
