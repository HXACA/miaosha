package com.xliu.miaosha.controller;

import com.sun.org.apache.bcel.internal.classfile.Code;
import com.xliu.miaosha.result.CodeMsg;
import com.xliu.miaosha.result.Result;
import com.xliu.miaosha.service.MiaoshaUserService;
import com.xliu.miaosha.util.ValidatorUtil;
import com.xliu.miaosha.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/4/7 18:01
 */

@Controller
@RequestMapping("/login")
public class LoginController {

    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    MiaoshaUserService miaoshaUserService;

    @RequestMapping("/to_login")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<String> doLogin(HttpServletResponse httpServletResponse, @Valid LoginVo loginVo){
        String token = miaoshaUserService.login(httpServletResponse, loginVo);
        return Result.success(token);
    }


}
