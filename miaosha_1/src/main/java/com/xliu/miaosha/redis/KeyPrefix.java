package com.xliu.miaosha.redis;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/4/7 16:04
 */
public interface KeyPrefix {

    int expireSeconds();

    String getPrefix();
}
