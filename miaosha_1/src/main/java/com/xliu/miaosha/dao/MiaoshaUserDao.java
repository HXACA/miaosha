package com.xliu.miaosha.dao;

import com.xliu.miaosha.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
}
