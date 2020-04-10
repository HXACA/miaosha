package com.xliu.miaosha.controller;

import com.xliu.miaosha.domain.MiaoshaOrder;
import com.xliu.miaosha.domain.MiaoshaUser;
import com.xliu.miaosha.domain.OrderInfo;
import com.xliu.miaosha.exception.GlobleException;
import com.xliu.miaosha.result.CodeMsg;
import com.xliu.miaosha.service.GoodsService;
import com.xliu.miaosha.service.MiaoshaService;
import com.xliu.miaosha.service.OrderService;
import com.xliu.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @RequestMapping("/do_miaosha")
    public String list(Model model, MiaoshaUser user, @RequestParam("goodsId") long goodsId){
        if(user == null){
            return "login";
        }
        GoodsVo good = goodsService.getGoodsVoByGoodsId(goodsId);
        //检查库存
        int stock = good.getStockCount();
        if(stock<=0){
            model.addAttribute("errmsg",CodeMsg.MIAOSHA_OVER.getMsg());
            return "miaosha_fail";
        }
        //检查重复秒杀
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
        if(order != null){
            model.addAttribute("errmsg",CodeMsg.MIAOSHA_REPEAT.getMsg());
            return "miaosha_fail";
        }
        //减库存，下订单，写入秒杀订单
        OrderInfo orderInfo = miaoshaService.miaosha(user,good);
        model.addAttribute("orderInfo",orderInfo);
        model.addAttribute("goods",good);
        return "order_detail";
    }

}
