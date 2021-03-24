package com.example.cli.controller;

import com.example.cli.domain.common.ResponseBean;
import com.example.cli.domain.search.FinanceSearch;
import com.example.cli.entity.RechargeRecord;
import com.example.cli.service.FinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author liaoheng
 * @version 1.0
 * @date 2021/3/23 11:33
 */
@RestController
@RequestMapping("/finance")
public class FinanceController {
    @Autowired
    private FinanceService financeService;

    /**
     * 获取财务数据
     * @param search
     * @return
     */
    @GetMapping("/getFinanceList")
    public ResponseBean getGoodsList(FinanceSearch search) {
        return new ResponseBean(financeService.getFinanceData(search));
    }


    /**
     * 用户充值
     * @param record
     * @return
     */
    @PostMapping("/recharge")
    public ResponseBean recharge(@RequestBody RechargeRecord record) {
        financeService.recharge(record);
        return new ResponseBean("success");
    }
}
