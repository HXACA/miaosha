package com.xliu.miaosha.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/4/7 15:33
 */
@Service
public class RedisService {

    @Autowired
    JedisPool jedisPool;

    public <T> Boolean set(KeyPrefix prefix,String key,T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            key = prefix.getPrefix() + key;
            String str = JSON.toJSONString(value);
            jedis.set(key, str);
            return true;
        } finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }


    public <T> T get(KeyPrefix prefix,String key,Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            key = prefix.getPrefix() + key;
            String str = jedis.get(key);
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        } finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }



}
