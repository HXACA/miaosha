package com.xliu.miaosha.redis;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/4/7 16:09
 */
public class OrderKey extends BasePrefix{

    public OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public OrderKey(String prefix) {
        super(prefix);
    }

    public static OrderKey getOrderList = new OrderKey("ol");
}
