package com.xliu.miaosha.rabbitmq;

import com.xliu.miaosha.domain.Goods;
import com.xliu.miaosha.domain.MiaoshaUser;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/4/14 13:04
 */
public class MiaoshaMessage {
    private MiaoshaUser user;
    private long goodsId;

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public MiaoshaUser getUser() {
        return user;
    }

    public void setUser(MiaoshaUser user) {
        this.user = user;
    }
}
