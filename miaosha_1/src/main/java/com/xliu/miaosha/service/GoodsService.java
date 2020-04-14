package com.xliu.miaosha.service;

import com.xliu.miaosha.dao.GoodsDao;
import com.xliu.miaosha.domain.Goods;
import com.xliu.miaosha.domain.MiaoshaGoods;
import com.xliu.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/4/10 14:54
 */
@Service
public class GoodsService {

    @Autowired
    GoodsDao goodsDao;

    public List<GoodsVo> findAll() {
        return goodsDao.selectAll();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.selectById(goodsId);
    }

    public int reduceStock(long id) {
        return goodsDao.reduceStock(id);
    }

    public void resetStock(List<GoodsVo> goodsList) {
        for (GoodsVo goodsVo : goodsList) {
            MiaoshaGoods g = new MiaoshaGoods();
            g.setGoodsId(goodsVo.getId());
            g.setStockCount(goodsVo.getStockCount());
            goodsDao.resetStock(g);
        }
    }
}
