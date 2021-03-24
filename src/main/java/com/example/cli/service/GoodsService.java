package com.example.cli.service;

import com.example.cli.constant.CommonConstant;
import com.example.cli.constant.DeletedEnum;
import com.example.cli.constant.StatusEnum;
import com.example.cli.constant.StockTypeEnum;
import com.example.cli.domain.common.PageInfo;
import com.example.cli.domain.common.StockInfo;
import com.example.cli.domain.search.GoodsSearch;
import com.example.cli.entity.*;
import com.example.cli.exception.BaseException;
import com.example.cli.repository.GoodsRepository;
import com.example.cli.repository.StockRecordRepository;
import com.example.cli.utils.MD5Utils;
import com.example.cli.utils.MyBeanUtils;
import com.example.cli.utils.PageUtils;
import com.example.cli.utils.RequestUserHolder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author liaoheng
 * @version 1.0
 * @date 2021/3/11 2:22
 */
@Service
@Slf4j
public class GoodsService {
    @Autowired
    GoodsRepository goodsRepository;
    @Autowired
    StockRecordRepository stockRecordRepository;

    /**
     * 按条件查询商品集合
     *
     * @param baseSearch
     * @return
     */
    public Object getAll(GoodsSearch baseSearch) {
        Specification<Goods> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasLength(baseSearch.getGoodsName())) {
                predicates.add(criteriaBuilder.like(root.get("goodsName"), "%" + baseSearch.getGoodsName() + "%"));
            }

            if (StringUtils.hasLength(baseSearch.getGoodsCode())) {
                predicates.add(criteriaBuilder.equal(root.get("code"), baseSearch.getGoodsCode()));
            }

            if (null != baseSearch.getStatus()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), baseSearch.getStatus()));
            }

            if (StringUtils.hasLength(baseSearch.getGoodsColor())) {
                predicates.add(criteriaBuilder.like(root.get("goodsColor"), "%" + baseSearch.getGoodsColor() + "%"));
            }

            if (StringUtils.hasLength(baseSearch.getGoodsType())) {
                predicates.add(criteriaBuilder.like(root.get("userName"), "%" + baseSearch.getGoodsType() + "%"));
            }

            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };

        if (baseSearch.getPageNo() == 0 && baseSearch.getPageSize() == 0) {
            return goodsRepository.findAll(specification);
        } else {
            Pageable pageable = PageRequest.of(baseSearch.getPageNo() - 1, baseSearch.getPageSize(), baseSearch.getSort());
            return PageUtils.getPageInfo(goodsRepository.findAll(specification, pageable), Goods.class);
        }
    }

    /**
     * 保存商品信息
     *
     * @param goods
     */
    public void saveGoods(Goods goods) {
        User useUser = RequestUserHolder.getUser();
        Date nowDate = new Date();
        goods.setEditUserId(useUser.getId());
        goods.setEditUsername(useUser.getName());
        goods.setEditTime(nowDate);

        if (StringUtils.isEmpty(goods.getId())) {
            goods.setCode(getGoodsCode(1));
            goods.setCreateTime(nowDate);
            goods.setCreateUserId(useUser.getId());
            goods.setCreateUsername(useUser.getName());
            goodsRepository.saveAndFlush(goods);
        } else {
            Goods old = goodsRepository.getOne(goods.getId());
            MyBeanUtils.copyProperties(goods, old);
            goodsRepository.saveAndFlush(old);
        }
    }

    /**
     * 获取商品编码
     *
     * @param num
     * @return
     */
    public synchronized String getGoodsCode(int num) {
        if (num > 0) {
            String goodsCode = goodsRepository.getGoodsCode(1);
            // 商品编码加1
            goodsRepository.updateGoodsCode(num, 1);
            return goodsCode;
        } else {
            return null;
        }
    }

    /**
     * 更新商品库存
     *
     * @param stockInfo
     */
    public synchronized void updateStock(StockInfo stockInfo) {
        User useUser = RequestUserHolder.getUser();
        if (null == useUser) {
            throw new BaseException(CommonConstant.EXCEPTION, "请先登录");
        }

        if (stockInfo.getStockNum() == 0) {
            throw new BaseException(CommonConstant.EXCEPTION, "更新库存数不能为0");
        }

        Goods goods = goodsRepository.getOne(stockInfo.getGoodsId());

        if (null == goods) {
            throw new BaseException(CommonConstant.EXCEPTION, "商品不存在");
        }

        Integer nowStock = goods.getStock();
        if (nowStock + stockInfo.getStockNum() < 0) {
            throw new BaseException(CommonConstant.EXCEPTION, "商品库存不足");
        }

        modifyStock(goods, stockInfo.getStockNum(), useUser, stockInfo.getMemo(), stockInfo.getStockType());
        log.info("{}更新{}库存数：{}。备注：{}", useUser.getName(), goods.getCode(), stockInfo.getStockNum(), stockInfo.getMemo());
    }

    /**
     * 更新库存并生成库存记录
     * @param goods
     * @param stock 正数入库；负数出库
     * @param useUser
     * @param memo
     */
    public void modifyStock(Goods goods, Integer stock, User useUser, String memo, StockTypeEnum stockType) {

        if (null == goods || null == goods.getId()) {
            throw new BaseException(CommonConstant.EXCEPTION, "商品信息为空失败");
        }

        int ret = goodsRepository.updateGoodsStock(stock, goods.getId());
        if (ret <= 0) {
            throw new BaseException(CommonConstant.EXCEPTION, "更新商品库存失败");
        }

        Date nowDate = new Date();
        StockRecord record = new StockRecord();
        record.setGoods(goods);
        record.setGoodsCode(goods.getCode());
        record.setGoodsName(goods.getGoodsName());
        record.setStockType(stockType);
        if (stock > 0) {
            record.setStockInNum(stock);
        } else {
            record.setStockOutNum(Math.abs(stock));
        }
        record.setCreateTime(nowDate);
        record.setCreateUsername(useUser.getName());
        record.setCreateUserId(useUser.getId());
        record.setMemo(memo);
        stockRecordRepository.saveAndFlush(record);
    }
}
