package com.example.cli.repository;

import com.example.cli.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author liaoheng
 * @version 1.0
 * @date 2021/3/11 2:23
 */
@Repository
public interface GoodsRepository extends JpaRepository<Goods, Integer>, JpaSpecificationExecutor<Goods> {

    /**
     * 获取商品编码
     *
     * @param id
     * @return
     */
    @Query(value = "SELECT system_code from system_code where id = ?1", nativeQuery = true)
    String getGoodsCode(Integer id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE system_code SET system_code = (system_code + ?1) WHERE id = ?2", nativeQuery = true)
    void updateGoodsCode(Integer num, Integer id);

    /**
     * 更新库存
     * @param num
     * @param goodsId
     * @return
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE goods SET stock = (goods.stock + ?1) WHERE id = ?2", nativeQuery = true)
    int updateGoodsStock(Integer num, Integer goodsId);
}
