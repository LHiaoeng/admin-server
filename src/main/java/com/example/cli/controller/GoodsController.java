package com.example.cli.controller;

import com.example.cli.domain.common.ResponseBean;
import com.example.cli.domain.common.StockInfo;
import com.example.cli.domain.search.GoodsSearch;
import com.example.cli.entity.Goods;
import com.example.cli.exception.BaseException;
import com.example.cli.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author liaoheng
 * @version 1.0
 * @date 2021/3/11 3:34
 */
@RestController
@RequestMapping("/goods")
@Validated
public class GoodsController {
    @Autowired
    GoodsService goodsService;

    /**
     * 分页查询商品集合
     * @param search
     * @return
     */
    @GetMapping("/getGoodsList")
    public ResponseBean getGoodsList(GoodsSearch search) {
        return new ResponseBean(goodsService.getAll(search));
    }

    /**
     * 新增或编辑商品
     * @param goods
     * @return
     */
    @PostMapping("/saveGoods")
    public ResponseBean saveUser(@RequestBody Goods goods) {
        goodsService.saveGoods(goods);
        return new ResponseBean("success");
    }

    /**
     * 更新商品库存
     * @param info
     * @return
     */
    @PutMapping("/updateStock")
    public Object updateStock(@RequestBody StockInfo info) {
        try {
            goodsService.updateStock(info);
            return new ResponseBean("success");
        } catch (BaseException e) {
            return e;
        }
    }
}
