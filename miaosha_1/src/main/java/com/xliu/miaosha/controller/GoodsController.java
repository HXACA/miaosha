package com.xliu.miaosha.controller;

import com.xliu.miaosha.domain.Goods;
import com.xliu.miaosha.domain.MiaoshaUser;
import com.xliu.miaosha.redis.GoodsKey;
import com.xliu.miaosha.redis.KeyPrefix;
import com.xliu.miaosha.redis.MiaoshaUserKey;
import com.xliu.miaosha.redis.RedisService;
import com.xliu.miaosha.result.Result;
import com.xliu.miaosha.service.GoodsService;
import com.xliu.miaosha.service.MiaoshaUserService;
import com.xliu.miaosha.vo.GoodsDetailVo;
import com.xliu.miaosha.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    RedisService redisService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    ApplicationContext applicationContext;

    @RequestMapping(value = "/to_list",produces = "text/html")
    @ResponseBody
    public String toLogin(Model model, MiaoshaUser user, HttpServletRequest request,HttpServletResponse response) {
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        //手动渲染
        List<GoodsVo> goods = goodsService.findAll();
        model.addAttribute("goodsList", goods);
        return getHtml(request,response,model,"goods_list",GoodsKey.getGoodsList);
    }

    @RequestMapping(value = "/to_detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> toDetail(MiaoshaUser user, @PathVariable("goodsId") long goodsId) {
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
        GoodsDetailVo goodsDetailVo = new GoodsDetailVo();
        goodsDetailVo.setGoodsVo(goodsVo);
        goodsDetailVo.setMiaoshaStatus(miaoshaStatus);
        goodsDetailVo.setRemainSeconds(remainSeconds);
        goodsDetailVo.setUser(user);
        return Result.success(goodsDetailVo);
    }

    public String getHtml(HttpServletRequest request, HttpServletResponse response, Model model, String templateName, KeyPrefix prefix){
        SpringWebContext ctx = new SpringWebContext(request,response,
                request.getServletContext(),request.getLocale(),model.asMap(),applicationContext);
        String html = thymeleafViewResolver.getTemplateEngine().process(templateName, ctx);
        if(!StringUtils.isEmpty(html)){
            redisService.set(prefix,"",html);
        }
        return html;
    }


}
