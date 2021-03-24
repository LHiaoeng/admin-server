package com.example.cli.controller;

import com.example.cli.domain.common.ResponseBean;
import com.example.cli.domain.search.GoodsSearch;
import com.example.cli.service.WorkplaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liaoheng
 * @version 1.0
 * @date 2021/3/18 10:37
 */
@RestController
@RequestMapping("/workplace")
public class WorkplaceController {
    @Autowired
    private WorkplaceService workplaceService;

    /**
     * 获取工作台数据
     * @return
     */
    @GetMapping("/getWorkplaceData")
    public ResponseBean getWorkplaceData() {
        return new ResponseBean(workplaceService.getWorkplaceData());
    }
}
