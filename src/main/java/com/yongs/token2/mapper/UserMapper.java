package com.yongs.token2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yongs.token2.pojo.entity.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author Yongs
 * @since 2025-10-18
 */
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from sys_user where username = #{username}")
    User findByUsername(String username);

    List<String> getRolesById(Long userId);

    List<String> getPermsById(Long userId);
}
