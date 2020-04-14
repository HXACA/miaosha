package com.xliu.miaosha.rabbitmq;

import com.xliu.miaosha.domain.MiaoshaOrder;
import com.xliu.miaosha.domain.MiaoshaUser;
import com.xliu.miaosha.domain.OrderInfo;
import com.xliu.miaosha.redis.RedisService;
import com.xliu.miaosha.result.CodeMsg;
import com.xliu.miaosha.result.Result;
import com.xliu.miaosha.service.GoodsService;
import com.xliu.miaosha.service.MiaoshaService;
import com.xliu.miaosha.service.OrderService;
import com.xliu.miaosha.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/4/14 10:26
 */
@Service
public class MQReceiver {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void receive(String message){
        MiaoshaMessage mm = RedisService.stringToBean(message, MiaoshaMessage.class);
        MiaoshaUser user = mm.getUser();
        long goodsId = mm.getGoodsId();
        GoodsVo good = goodsService.getGoodsVoByGoodsId(goodsId);
        //检查库存
        int stock = good.getStockCount();
        if(stock<=0){
            return;
        }
        //检查重复秒杀
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
        if(order != null){
            return;
        }
        //减库存，下订单，写入秒杀订单
        OrderInfo orderInfo = miaoshaService.miaosha(user,good);
    }
}
