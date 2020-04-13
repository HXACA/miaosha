package com.xliu.miaosha.vo;

import com.xliu.miaosha.domain.OrderInfo;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/4/13 15:11
 */
public class OrderVo {
    private OrderInfo order;
    private GoodsVo goods;

    public OrderInfo getOrder() {
        return order;
    }

    public void setOrder(OrderInfo order) {
        this.order = order;
    }

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }
}
