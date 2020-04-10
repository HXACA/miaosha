package com.xliu.miaosha.util;

import java.util.UUID;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/4/10 11:28
 */
public class UUIDUtil {
    public static String uuid(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
