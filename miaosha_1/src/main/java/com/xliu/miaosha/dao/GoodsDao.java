package com.xliu.miaosha.dao;

import com.xliu.miaosha.domain.Goods;
import com.xliu.miaosha.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/4/10 14:55
 */
@Mapper
public interface GoodsDao {
    @Select("select g.*,mg.stock_count,mg.start_date,mg.end_date,mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id = g.id")
    public List<GoodsVo> selectAll();
    @Select("select g.*,mg.stock_count,mg.start_date,mg.end_date,mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id = g.id and g.id = #{goodsId}")
    GoodsVo selectById(@Param("goodsId") long goodsId);

    @Update("update miaosha_goods set stock_count=stock_count-1 where goods_id = #{id} and stock_count>0")
    int reduceStock(@Param("id") long id);
}
