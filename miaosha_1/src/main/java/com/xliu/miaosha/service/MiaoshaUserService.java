package com.xliu.miaosha.service;

import com.xliu.miaosha.dao.MiaoshaUserDao;
import com.xliu.miaosha.domain.MiaoshaUser;
import com.xliu.miaosha.exception.GlobleException;
import com.xliu.miaosha.redis.MiaoshaUserKey;
import com.xliu.miaosha.redis.RedisService;
import com.xliu.miaosha.result.CodeMsg;
import com.xliu.miaosha.util.MD5Util;
import com.xliu.miaosha.util.UUIDUtil;
import com.xliu.miaosha.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/4/7 14:42
 */
@Service
public class MiaoshaUserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    MiaoshaUserDao userDao;

    @Autowired
    RedisService redisService;

    public MiaoshaUser getById(Long id){
        return userDao.getById(id);
    }

    public boolean login(HttpServletResponse httpServletResponse,LoginVo loginVo){
        if(loginVo == null){
            throw new GlobleException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        MiaoshaUser user = getById(Long.parseLong(mobile));
        if(user == null){
            throw new GlobleException(CodeMsg.MOBILE_NOT_EXIST);
        }
        String dbPass = user.getPassword();
        String slatDB = user.getSalt();
        String calcPass = MD5Util.FormPassToDBPass(password, slatDB);
        if(calcPass.equals(dbPass)){
            String token = UUIDUtil.uuid();
            addCookie(token,user,httpServletResponse);
            return true;
        }else{
            throw new GlobleException(CodeMsg.PASSWORD_ERROR);
        }
    }

    private void addCookie(String token,MiaoshaUser user,HttpServletResponse httpServletResponse){
        redisService.set(MiaoshaUserKey.token,token,user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN,token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
    }

    public MiaoshaUser getByToken(HttpServletResponse response,String token) {
        if(StringUtils.isEmpty(token)){
            return null;
        }
        MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        if(user!=null){
            addCookie(token,user,response);
        }
        return user;
    }
}
