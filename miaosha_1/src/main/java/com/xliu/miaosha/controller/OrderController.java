package com.xliu.miaosha.controller;

import com.xliu.miaosha.domain.MiaoshaUser;
import com.xliu.miaosha.domain.OrderInfo;
import com.xliu.miaosha.result.CodeMsg;
import com.xliu.miaosha.result.Result;
import com.xliu.miaosha.service.GoodsService;
import com.xliu.miaosha.service.OrderService;
import com.xliu.miaosha.vo.GoodsVo;
import com.xliu.miaosha.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/4/13 15:06
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;

    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderVo> orderDetail(@RequestParam("orderId") long orderId, MiaoshaUser user){
        if(user == null){
            return  Result.error(CodeMsg.SESSION_ERROR);
        }
        OrderInfo orderInfo = orderService.getOrderByOrderOd(orderId);
        if(orderInfo == null){
            return Result.error(CodeMsg.ORDER_ERROR);
        }
        if(orderInfo.getUserId().longValue() != user.getId().longValue()){
            return Result.error(CodeMsg.ORDER_ERROR);
        }
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(orderInfo.getGoodsId());
        OrderVo orderVo = new OrderVo();
        orderVo.setGoods(goods);
        orderVo.setOrder(orderInfo);
        return Result.success(orderVo);
    }

}
