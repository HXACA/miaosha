package com.xliu.miaosha.controller;

import com.xliu.miaosha.domain.MiaoshaUser;
import com.xliu.miaosha.redis.MiaoshaUserKey;
import com.xliu.miaosha.redis.RedisService;
import com.xliu.miaosha.result.Result;
import com.xliu.miaosha.service.MiaoshaService;
import com.xliu.miaosha.service.MiaoshaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/4/13 9:42
 */
@Controller
@RequestMapping("/user")
public class MiaoshaUserController {

    @Autowired
    RedisService redisService;

    @Autowired
    MiaoshaUserService miaoshaUserService;

    @RequestMapping("/info")
    @ResponseBody
    public Result<MiaoshaUser> getInfo(MiaoshaUser user) {
        return Result.success(user);
    }

}
