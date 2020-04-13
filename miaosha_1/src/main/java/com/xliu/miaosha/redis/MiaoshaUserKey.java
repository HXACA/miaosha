package com.xliu.miaosha.redis;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/4/10 11:30
 */
public class MiaoshaUserKey extends BasePrefix {

    public static final int TOKEN_EXPIRE = 3600*24*2;

    public MiaoshaUserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public MiaoshaUserKey(String prefix) {
        super(prefix);
    }

    public static MiaoshaUserKey token = new MiaoshaUserKey(TOKEN_EXPIRE,"token");
    public static MiaoshaUserKey getById = new MiaoshaUserKey("getById");
}
