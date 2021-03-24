package com.example.cli.repository;

import com.example.cli.constant.GoodsTypeEnum;
import com.example.cli.entity.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author liaoheng
 * @version 1.0
 * @date 2021/3/11 23:59
 */
@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Integer>, JpaSpecificationExecutor<SalesOrder> {

    /**
     * 根据创建人查询订单
     * @param creatUserId
     * @return
     */
    List<SalesOrder> findAllByCreateUserId(Integer creatUserId);

    /**
     * 根据创建人查询某个日期后的所有订单
     * @param creatUserId
     * @param time
     * @return
     */
    List<SalesOrder> findAllByCreateUserIdAndCreateTimeAfter(Integer creatUserId, Date time);

    /**
     * 查询某个日期后的所有订单
     * @param time
     * @return
     */
    List<SalesOrder> findAllByCreateTimeAfter(Date time);

    /**
     * 根据用户和商品类型，获取某种类型的商品销售数量
     * @param goodsType
     * @param userId
     * @return
     */
    @Query(value = "select coalesce(sum(sod.amount),0 ) from sales_order_details sod " +
            "left join sales_order so on so.id = sod.order_id and so.create_user_id = :userId " +
            "where sod.goods_type = :goodsType", nativeQuery = true)
    Integer getSaleNum(Integer goodsType, Integer userId);


}
