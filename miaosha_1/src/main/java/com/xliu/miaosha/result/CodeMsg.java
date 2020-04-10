package com.xliu.miaosha.result;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/4/7 15:16
 */
public class CodeMsg {
    private int code;
    private String msg;


    public static CodeMsg SUCCESS = new CodeMsg(0,"success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100,"服务端异常");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101,"参数校验异常:%s");


    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211,"登录密码不能为空");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500212,"手机号不能为空");
    public static CodeMsg MOBILE_ERROR= new CodeMsg(500213,"手机号格式错误");
    public static CodeMsg MOBILE_NOT_EXIST= new CodeMsg(500214,"手机号未注册");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215,"登录密码错误");

    public static CodeMsg MIAOSHA_OVER = new CodeMsg(500500,"商品已经被秒杀完毕");
    public static CodeMsg MIAOSHA_REPEAT = new CodeMsg(500501,"一个用户只能秒杀一件！");

    public CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CodeMsg fillArgs(Object... args){
        int code = this.code;
        String message = String.format(this.msg,args);
        return new CodeMsg(code,message);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
