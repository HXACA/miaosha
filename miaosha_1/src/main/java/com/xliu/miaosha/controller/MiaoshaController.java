package com.xliu.miaosha.controller;

import com.xliu.miaosha.domain.MiaoshaOrder;
import com.xliu.miaosha.domain.MiaoshaUser;
import com.xliu.miaosha.domain.OrderInfo;
import com.xliu.miaosha.exception.GlobleException;
import com.xliu.miaosha.rabbitmq.MQSender;
import com.xliu.miaosha.rabbitmq.MiaoshaMessage;
import com.xliu.miaosha.redis.GoodsKey;
import com.xliu.miaosha.redis.MiaoshaKey;
import com.xliu.miaosha.redis.OrderKey;
import com.xliu.miaosha.redis.RedisService;
import com.xliu.miaosha.result.CodeMsg;
import com.xliu.miaosha.result.Result;
import com.xliu.miaosha.service.GoodsService;
import com.xliu.miaosha.service.MiaoshaService;
import com.xliu.miaosha.service.OrderService;
import com.xliu.miaosha.util.MD5Util;
import com.xliu.miaosha.util.UUIDUtil;
import com.xliu.miaosha.vo.GoodsVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/4/10 16:42
 */
@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @Autowired
    RedisService redisService;

    @Autowired
    MQSender mqSender;

    private Map<Long,Boolean> localOverMap = new HashMap<>();

    /**
     * 未优化前 1843 QPS
     * 优化后 4916 QPS
     * @param user
     * @param goodsId
     * @return
     */

    @RequestMapping(value = "/{path}/do_miaosha",method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> miaosha(MiaoshaUser user, @RequestParam("goodsId") long goodsId, @PathVariable("path") String path){
        //检查用户是否登录
        if(user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        boolean check = miaoshaService.checkPath(path,user.getId(),goodsId);
        if(!check){
            return Result.error(CodeMsg.REQUEST_ERROR);
        }
        boolean over = localOverMap.get(goodsId);
        if(over){
            return Result.error(CodeMsg.MIAOSHA_OVER);
        }
        //预减库存
        long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock,""+goodsId);
        if(stock<0){
            localOverMap.put(goodsId,true);
            return Result.error(CodeMsg.MIAOSHA_OVER);
        }
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
        if(order != null){
            return Result.error(CodeMsg.MIAOSHA_REPEAT);
        }
        //入队
        MiaoshaMessage mm = new MiaoshaMessage();
        mm.setUser(user);
        mm.setGoodsId(goodsId);
        mqSender.sendMiaoshaMessage(mm);
        return Result.success(0);
    }

    @RequestMapping(value = "/result",method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> getResult(MiaoshaUser user,@RequestParam("goodsId") long goodsId){
        if(user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long  orderId = miaoshaService.getMiaoshaResult(user.getId(),goodsId);
        return Result.success(orderId);
    }

    @RequestMapping(value = "/verifyCode",method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getVerifyCode(MiaoshaUser user, @RequestParam("goodsId") long goodsId, HttpServletResponse response){
        if(user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        BufferedImage image = miaoshaService.createVerifyCode(user,goodsId);
        try {
            OutputStream out = response.getOutputStream();
            ImageIO.write(image,"JPEG",out);
            out.flush();
            out.close();
            return null;
        } catch (IOException e) {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //预加载秒杀库存
        List<GoodsVo> goodsList = goodsService.findAll();
        if(goodsList == null){
            return;
        }
        for(GoodsVo goods:goodsList){
            redisService.set(GoodsKey.getMiaoshaGoodsStock,""+goods.getId(),goods.getStockCount());
            localOverMap.put(goods.getId(),false);
        }
    }

    @RequestMapping("/path")
    @ResponseBody
    public Result<String> getPath(@RequestParam("goodsId") long goodsId,@RequestParam(value = "verifyCode",defaultValue = "0") int verifyCode, MiaoshaUser user){
        int maxClick = 5;
        if(user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        Integer num = redisService.get(MiaoshaKey.getLogin, "" + user.getId() + "_" + goodsId, Integer.class);
        if(num == null){
            redisService.set(MiaoshaKey.getLogin, "" + user.getId() + "_" + goodsId, 0);
        }else if(num > maxClick){
            return Result.error(CodeMsg.REPEAT_CLICK);
        }
        redisService.incr(MiaoshaKey.getLogin, "" + user.getId() + "_" + goodsId);

        if(!checkVerifyCode(verifyCode,goodsId,user.getId())){
            return Result.error(CodeMsg.VERIFY_ERROR);
        }
        String str = MD5Util.md5(UUIDUtil.uuid() + user.getId());
        redisService.set(MiaoshaKey.getMiaoshPath,""+user.getId()+"_"+goodsId,str);


        return Result.success(str);
    }

    private boolean checkVerifyCode(int verifyCode, long goodsId, long userId) {
        Integer result= redisService.get(MiaoshaKey.getMiaoshaVerifyCode, userId + "," + goodsId, Integer.class);
        if(result == null || result-verifyCode !=0){
            return false;
        }
        redisService.delete(MiaoshaKey.getMiaoshaVerifyCode, userId + "," + goodsId);
        return true;
    }

    @RequestMapping("/reset")
    @ResponseBody
    public Result<Boolean> reset(){
        List<GoodsVo> goodsList = goodsService.findAll();
        for(GoodsVo goods:goodsList){
            goods.setStockCount(100);
            redisService.set(GoodsKey.getMiaoshaGoodsStock,""+goods.getId(),goods.getStockCount());
            localOverMap.put(goods.getId(),false);
        }
        redisService.delete(OrderKey.getOrderList);
        redisService.delete(MiaoshaKey.isGoodOver);
        miaoshaService.reset(goodsList);
        return Result.success(true);
    }
}
