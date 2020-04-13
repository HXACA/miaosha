package com.xliu.miaosha.service;

import com.xliu.miaosha.dao.OrderDao;
import com.xliu.miaosha.domain.MiaoshaOrder;
import com.xliu.miaosha.domain.MiaoshaUser;
import com.xliu.miaosha.domain.OrderInfo;
import com.xliu.miaosha.redis.OrderKey;
import com.xliu.miaosha.redis.RedisService;
import com.xliu.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/4/10 16:48
 */
@Service
public class OrderService {

    @Autowired
    OrderDao orderDao;

    @Autowired
    RedisService redisService;

    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(long userId, long goodsId) {
        MiaoshaOrder miaoshaOrder = redisService.get(OrderKey.getOrderList, goodsId + "_" + userId, MiaoshaOrder.class);
        if(miaoshaOrder != null){
            return miaoshaOrder;
        }
        return orderDao.getByUserIdGoodsId(userId, goodsId);
    }

    @Transactional
    public OrderInfo createOrder(MiaoshaUser user, GoodsVo good) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(good.getId());
        orderInfo.setGoodsName(good.getGoodsName());
        orderInfo.setGoodsPrice(good.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        long orderId = orderDao.insert(orderInfo);

        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(good.getId());
        miaoshaOrder.setOrderId(orderId);
        miaoshaOrder.setUserId(user.getId());
        orderDao.insertMiaoshaOrder(miaoshaOrder);
        redisService.set(OrderKey.getOrderList,good.getId()+"_"+user.getId(),miaoshaOrder);
        return orderInfo;
    }

    public OrderInfo getOrderByOrderOd(long orderId) {
        return orderDao.selectOrderByOrderOd(orderId);
    }
}
