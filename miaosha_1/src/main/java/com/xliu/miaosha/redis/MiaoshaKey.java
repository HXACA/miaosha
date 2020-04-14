package com.xliu.miaosha.redis;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/4/13 10:26
 */
public class MiaoshaKey extends BasePrefix {



    public MiaoshaKey(String prefix) {
        super(prefix);
    }

    public MiaoshaKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static MiaoshaKey isGoodOver = new MiaoshaKey("go");
    public static MiaoshaKey getMiaoshPath = new MiaoshaKey(60,"path");
    public static MiaoshaKey getLogin = new MiaoshaKey(60,"login");
    public static MiaoshaKey getMiaoshaVerifyCode =new MiaoshaKey(60,"verifyCode");
}
