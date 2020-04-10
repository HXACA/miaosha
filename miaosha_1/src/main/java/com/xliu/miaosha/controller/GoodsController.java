package com.xliu.miaosha.controller;

import com.xliu.miaosha.domain.Goods;
import com.xliu.miaosha.domain.MiaoshaUser;
import com.xliu.miaosha.redis.MiaoshaUserKey;
import com.xliu.miaosha.service.GoodsService;
import com.xliu.miaosha.service.MiaoshaUserService;
import com.xliu.miaosha.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/4/10 12:01
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    GoodsService goodsService;

    @RequestMapping("/to_list")
    public String toLogin(Model model,MiaoshaUser user) {
        List<GoodsVo> goods = goodsService.findAll();
        model.addAttribute("goodsList", goods);
        return "goods_list";
    }

    @RequestMapping("/to_detail/{goodsId}")
    public String toDetail(Model model, MiaoshaUser user, @PathVariable("goodsId") long goodsId) {
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        long startAt = goodsVo.getStartDate().getTime();
        long endAt = goodsVo.getEndDate().getTime();
        long now = System.currentTimeMillis();
        long remainSeconds = 0;
        int miaoshaStatus = 0;
        if (now < startAt) {
            miaoshaStatus = 0;
            remainSeconds = (startAt - now) / 1000;
        } else if (now > endAt) {
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else {
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("user", user);
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        model.addAttribute("goods", goodsVo);
        return "goods_detail";
    }


}
