package com.example.cli.controller;

import com.example.cli.domain.common.ResponseBean;
import com.example.cli.domain.search.FinanceSearch;
import com.example.cli.domain.search.RechargeSearch;
import com.example.cli.entity.RechargeRecord;
import com.example.cli.service.FinanceService;
import com.example.cli.service.RechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author liaoheng
 * @version 1.0
 * @date 2021/3/23 11:33
 */
@RestController
@RequestMapping("/recharge")
public class RechargeController {
    @Autowired
    private RechargeService rechargeService;

    /**
     * 分页或不分页获取充值记录
     * @param search
     * @return
     */
    @GetMapping("/getRechargeList")
    public ResponseBean getRechargeList(RechargeSearch search) {
        return new ResponseBean(rechargeService.getAll(search));
    }

}
