package com.xliu.miaosha.service;

import com.xliu.miaosha.dao.GoodsDao;
import com.xliu.miaosha.domain.Goods;
import com.xliu.miaosha.domain.MiaoshaUser;
import com.xliu.miaosha.domain.OrderInfo;
import com.xliu.miaosha.exception.GlobleException;
import com.xliu.miaosha.result.CodeMsg;
import com.xliu.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/4/10 16:51
 */
@Service
public class MiaoshaService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo good) {
        //减库存
        int reduceStock = goodsService.reduceStock(good.getId());
        if(reduceStock == 0){
            throw new GlobleException(CodeMsg.MIAOSHA_OVER);
        }
        return orderService.createOrder(user,good);
    }
}
