package com.example.cli.controller;

import com.example.cli.domain.common.ResponseBean;
import com.example.cli.domain.search.CustomerSearch;
import com.example.cli.entity.Customer;
import com.example.cli.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author liaoheng
 * @version 1.0
 * @date 2021/3/11 16:35
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    /**
     * 按条件分页或不分页查询客户集合
     * @param search
     * @return
     */
    @GetMapping("/getList")
    public ResponseBean getGoodsList(CustomerSearch search) {
        return new ResponseBean(customerService.getAll(search));
    }

    /**
     * 保存客户信息
     * @param customer
     * @return
     */
    @PostMapping("/saveCustomer")
    public ResponseBean saveCustomer(@RequestBody Customer customer) {
        customerService.saveCustomer(customer);
        return new ResponseBean("success");
    }

    /**
     * 根据id删除客户
     * @param id
     * @return
     */
    @DeleteMapping("deleteById")
    public ResponseBean deleteById(Integer id) {
        customerService.deleteById(id);
        return new ResponseBean("success");
    }
}
