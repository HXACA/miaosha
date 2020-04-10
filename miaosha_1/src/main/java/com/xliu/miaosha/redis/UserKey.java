package com.xliu.miaosha.redis;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/4/7 16:09
 */
public class UserKey extends BasePrefix {

    public UserKey(String prefix) {
        super(prefix);
    }

    public static UserKey getById = new UserKey("id");
    public static UserKey getByName = new UserKey("name");
}
