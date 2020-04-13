package com.xliu.miaosha.dao;

import com.xliu.miaosha.domain.MiaoshaUser;
import org.apache.ibatis.annotations.*;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/4/7 14:41
 */
@Mapper
public interface MiaoshaUserDao {
    /**
     *
     * @param id
     * @return
     */
    @Select("select * from miaosha_user where id = #{id}")
    public MiaoshaUser getById(@Param("id") Long id);

    @Update("update miaosha_user set password = #{password} where id = #{id}")
    void update(MiaoshaUser toBeUpdate);
}
