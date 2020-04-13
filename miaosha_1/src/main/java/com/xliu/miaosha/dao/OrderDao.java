package com.xliu.miaosha.dao;

import com.xliu.miaosha.domain.MiaoshaOrder;
import com.xliu.miaosha.domain.OrderInfo;
import org.apache.ibatis.annotations.*;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/4/10 17:17
 */
@Mapper
public interface OrderDao {

    @Select("select * from miaosha_order where user_id = #{userId} and goods_id = #{goodsId}")
    MiaoshaOrder getByUserIdGoodsId(@Param("userId") long userId, @Param("goodsId") long goodsId);

    @Insert("insert into order_info(user_id,goods_id,goods_name,goods_account,goods_price,order_channel,status,create_date) " +
            "values(#{userId},#{goodsId},#{goodsName},#{goodsCount},#{goodsPrice},#{orderChannel},#{status},#{createDate})")
    @SelectKey(keyColumn = "id",keyProperty = "id",resultType = long.class,before = false,statement = "select last_insert_id()")
    long insert(OrderInfo orderInfo);

    @Insert("insert into miaosha_order(user_id,goods_id,order_id) values(#{userId},#{goodsId},#{orderId})")
    long insertMiaoshaOrder(MiaoshaOrder miaoshaOrder);

    @Select("select * from order_info where id = #{orderId}")
    OrderInfo selectOrderByOrderOd(@Param("orderId") long orderId);
}
