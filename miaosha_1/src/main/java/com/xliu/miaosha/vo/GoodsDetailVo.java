package com.xliu.miaosha.vo;

import com.xliu.miaosha.domain.MiaoshaUser;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/4/13 14:10
 */
public class GoodsDetailVo {

    MiaoshaUser user;
    long remainSeconds;
    int miaoshaStatus;
    GoodsVo goodsVo;

    public MiaoshaUser getUser() {
        return user;
    }

    public void setUser(MiaoshaUser user) {
        this.user = user;
    }

    public long getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(long remainSeconds) {
        this.remainSeconds = remainSeconds;
    }

    public int getMiaoshaStatus() {
        return miaoshaStatus;
    }

    public void setMiaoshaStatus(int miaoshaStatus) {
        this.miaoshaStatus = miaoshaStatus;
    }

    public GoodsVo getGoodsVo() {
        return goodsVo;
    }

    public void setGoodsVo(GoodsVo goodsVo) {
        this.goodsVo = goodsVo;
    }
}
