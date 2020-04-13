package com.xliu.miaosha.controller;

import com.xliu.miaosha.domain.MiaoshaOrder;
import com.xliu.miaosha.domain.MiaoshaUser;
import com.xliu.miaosha.domain.OrderInfo;
import com.xliu.miaosha.exception.GlobleException;
import com.xliu.miaosha.result.CodeMsg;
import com.xliu.miaosha.result.Result;
import com.xliu.miaosha.service.GoodsService;
import com.xliu.miaosha.service.MiaoshaService;
import com.xliu.miaosha.service.OrderService;
import com.xliu.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/4/10 16:42
 */
@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @RequestMapping(value = "/do_miaosha",method = RequestMethod.POST)
    @ResponseBody
    public Result<OrderInfo> miaosha(MiaoshaUser user, @RequestParam("goodsId") long goodsId){
        if(user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        GoodsVo good = goodsService.getGoodsVoByGoodsId(goodsId);
        //检查库存
        int stock = good.getStockCount();
        if(stock<=0){
            return Result.error(CodeMsg.MIAOSHA_OVER);
        }
        //检查重复秒杀
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
        if(order != null){
            return Result.error(CodeMsg.MIAOSHA_REPEAT);
        }
        //减库存，下订单，写入秒杀订单
        OrderInfo orderInfo = miaoshaService.miaosha(user,good);
        return Result.success(orderInfo);
    }

}
