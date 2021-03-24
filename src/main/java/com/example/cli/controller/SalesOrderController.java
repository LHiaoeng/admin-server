package com.example.cli.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.example.cli.domain.common.ResponseBean;
import com.example.cli.domain.search.SalesOrderSearch;
import com.example.cli.entity.SalesOrder;
import com.example.cli.service.SalesOrderService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author liaoheng
 * @version 1.0
 * @date 2021/3/12 3:15
 */
@RestController
@RequestMapping("/salesOrder")
public class SalesOrderController {
    @Autowired
    SalesOrderService salesOrderService;

    /**
     * 分页或不分页查询销售订单
     * @param search
     * @return
     */
    @GetMapping("/getList")
    public ResponseBean getSalesOrder(SalesOrderSearch search) {
        return new ResponseBean(salesOrderService.getAll(search));
    }

    /**
     * 保存销售订单
     * @param salesOrder
     * @return
     */
    @PostMapping("/saveOrder")
    public ResponseBean saveOrder(@RequestBody SalesOrder salesOrder) {
        salesOrderService.saveSalesOrder(salesOrder);
        return new ResponseBean("success");
    }

    @PostMapping("/exportOrder")
    public void orderExport(SalesOrderSearch baseSearch, HttpServletResponse response) throws Exception {
        baseSearch.setPageNo(0);
        baseSearch.setPageSize(0);

        List<SalesOrder> list = (List<SalesOrder>) salesOrderService.getAll(baseSearch);

        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=orders.xls");

        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), SalesOrder.class, list);
        workbook.write(response.getOutputStream());
    }
}
