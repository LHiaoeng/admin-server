package com.example.cli.service;

import com.example.cli.constant.*;
import com.example.cli.domain.search.SalesOrderSearch;
import com.example.cli.entity.*;
import com.example.cli.exception.BaseException;
import com.example.cli.repository.GoodsRepository;
import com.example.cli.repository.SalesOrderRepository;
import com.example.cli.utils.DateUtils;
import com.example.cli.utils.MyBeanUtils;
import com.example.cli.utils.PageUtils;
import com.example.cli.utils.RequestUserHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author liaoheng
 * @version 1.0
 * @date 2021/3/12 0:01
 */
@Service
@Slf4j
public class SalesOrderService {
    @Autowired
    private SalesOrderRepository salesOrderRepository;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private CustomerService customerService;

    /**
     * 分页或不分页查询销售订单
     *
     * @param baseSearch
     * @return
     */
    public Object getAll(SalesOrderSearch baseSearch) {
        Specification<SalesOrder> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            User useUser = RequestUserHolder.getUser();
            Join<SalesOrder, Customer> join = root.join("customer", JoinType.LEFT);
            Join<SalesOrder, SalesOrderDetails> join2 = root.join("detailsList", JoinType.LEFT);

            //非角色枚举里的用户查询自己新增的订单
            if (!EnumUtils.isValidEnum(Role2AllOrderEnum.class, useUser.getRole().getRoleId())) {
                predicates.add(criteriaBuilder.equal(root.get("createUserId"), useUser.getId()));
            }

            if (StringUtils.hasLength(baseSearch.getOrderNo())) {
                predicates.add(criteriaBuilder.equal(root.get("orderId"), baseSearch.getOrderNo()));
            }

            if (null != baseSearch.getStatus()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), baseSearch.getStatus()));
            }

            if (null != baseSearch.getCustomerId()) {
                predicates.add(criteriaBuilder.equal(join.get("id"), baseSearch.getCustomerId()));
            }

            if (StringUtils.hasLength(baseSearch.getCustomerName())) {
                predicates.add(criteriaBuilder.like(join.get("name"), "%" + baseSearch.getCustomerName() + "%"));
            }
            if (StringUtils.hasLength(baseSearch.getGoodsCode())) {
                predicates.add(criteriaBuilder.equal(join2.get("goodsCode"), baseSearch.getGoodsCode()));
            }
            if (StringUtils.hasLength(baseSearch.getGoodsColor())) {
                predicates.add(criteriaBuilder.like(join2.get("goodsColor"), "%" + baseSearch.getGoodsColor() + "%"));
            }
            if (StringUtils.hasLength(baseSearch.getGoodsName())) {
                predicates.add(criteriaBuilder.like(join2.get("goodsName"), "%" + baseSearch.getGoodsName() + "%"));
            }

            //去重
            query.groupBy(root.get("id"));
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };

        if (baseSearch.getPageNo() == 0 && baseSearch.getPageSize() == 0) {
            return salesOrderRepository.findAll(specification);
        } else {
            Pageable pageable = PageRequest.of(baseSearch.getPageNo() - 1, baseSearch.getPageSize(), baseSearch.getSort());
            return PageUtils.getPageInfo(salesOrderRepository.findAll(specification, pageable), SalesOrder.class);
        }
    }

    /**
     * 保存订单
     *
     * @param salesOrder
     */
    public void saveSalesOrder(SalesOrder salesOrder) {
        User useUser = RequestUserHolder.getUser();

        if (CommonConstant.SUPER_ROLE_ID.equals(useUser.getRole().getRoleId())) {
            throw new BaseException(CommonConstant.EXCEPTION, "管理员不可下单");
        }

        Date nowDate = new Date();

        salesOrder.setEditUserId(useUser.getId());
        salesOrder.setEditUsername(useUser.getName());
        salesOrder.setEditTime(nowDate);

        if (StringUtils.isEmpty(salesOrder.getId())) {
            List<SalesOrderDetails> detailsList = salesOrder.getDetailsList();
            if (null == detailsList || detailsList.size() == 0) {
                throw new BaseException(CommonConstant.EXCEPTION, "商品信息不存在");
            }

            StringBuffer sb = new StringBuffer();
            Integer allTotal = 0;
            List<Goods> goodsList = new ArrayList<>();

            //订单总价
            BigDecimal totalPrice = BigDecimal.ZERO;
            //订单应付款
            BigDecimal payablePrice = BigDecimal.ZERO;

            for (SalesOrderDetails details : detailsList) {
                Goods goods = goodsRepository.getOne(details.getGoods().getId());
                if (null == goods) {
                    sb.append("商品" + details.getGoodsCode() + "不存在;");
                    continue;
                }
                if (null == details.getAmount()) {
                    sb.append("商品" + details.getGoodsCode() + "订购数量为空");
                    continue;
                }
                if (goods.getStock() < details.getAmount()) {
                    sb.append("商品" + details.getGoodsCode() + "库存不足");
                    continue;
                }
                allTotal = allTotal + details.getAmount();

                //累计成本价
                totalPrice = totalPrice.add(goods.getPrice().multiply(new BigDecimal(details.getAmount())));

                //如果业务员等级是业务经理，商品价格用业务经理价，其余用业务主任价
                if (useUser.getSalesmanGrade().equals(SalesmanEnum.MANAGER_SALESMAN)) {
                    details.setSalePrice(goods.getManagerPrice());
                    payablePrice = payablePrice.add(goods.getManagerPrice().multiply(new BigDecimal(details.getAmount())));
                } else {
                    details.setSalePrice(goods.getDirectorPrice());
                    payablePrice = payablePrice.add(goods.getDirectorPrice().multiply(new BigDecimal(details.getAmount())));
                }

                //购买数量，下面有用
                goods.setBuyNum(details.getAmount());

                goodsList.add(goods);

                details.setCostPrice(goods.getPrice());
                details.setGoodsType(goods.getGoodsType());
                //保存订单id到订单明细中
                details.setSalesOrder(salesOrder);
            }

            if (allTotal <= 0) {
                throw new BaseException(CommonConstant.EXCEPTION, sb.toString());
            }

            //统计订单商品数
            salesOrder.setTotalNumber(allTotal);

            salesOrder.setTotalPrice(totalPrice);
            salesOrder.setPayablePrice(payablePrice);
            salesOrder.setActualPrice(BigDecimal.ZERO);

            //设置订单状态正常
            salesOrder.setStatus(OrderStatusEnum.NORMAL.ordinal());

            //获取订单编号
            salesOrder.setOrderNo(DateUtils.getOrderNo());

            salesOrder.setCreateTime(nowDate);
            salesOrder.setCreateUserId(useUser.getId());
            salesOrder.setCreateUsername(useUser.getName());
            salesOrderRepository.saveAndFlush(salesOrder);

            //更新库存，客户增加库存数
            for (Goods goods : goodsList) {
                String memo = useUser.getName() + "下单商品" + goods.getCode() + ",数量：" + goods.getBuyNum();
                goodsService.modifyStock(goods, goods.getBuyNum() * -1, useUser, memo, StockTypeEnum.ORDER_OUT);
                log.info("{}下单商品{},数量：{}", useUser.getName(), goods.getCode(), goods.getBuyNum());

                //商品类型是烟弹，增加客户剩余数
                if (goods.getGoodsType().equals(GoodsTypeEnum.SMOKEBOMB)) {
                    customerService.addSurplus(salesOrder.getCustomer().getId(), goods.getBuyNum());
                }
            }

        } else {
            SalesOrder old = salesOrderRepository.getOne(salesOrder.getId());
            MyBeanUtils.copyProperties(salesOrder, old);
            if (null == salesOrder.getCustomerOrderTime()) {
                old.setCustomerOrderTime(null);
            }
            if (null == salesOrder.getWaybillTime()) {
                old.setWaybillTime(null);
            }
            salesOrderRepository.saveAndFlush(old);
        }
    }
}
