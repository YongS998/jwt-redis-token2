package com.yongs.token2.service;

import com.yongs.token2.mapper.UserMapper;
import com.yongs.token2.pojo.bo.CustomUserDetails;
import com.yongs.token2.pojo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 功能：
 * 作者：YongS
 * 日期：2025/10/18 13:15
 */
@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.根据用户名获取用户实体
        User user = userMapper.findByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException("用户名不存在！");
        }

        Long userId = user.getId();
        //2.获取角色&权限信息
        List<String> roles = userMapper.getRolesById(userId);
        if (roles == null){
            roles = List.of();
        }
        List<String> perms = userMapper.getPermsById(userId);
        if (perms == null){
            perms = List.of();
        }

        //3.返回UserDetails
        return new CustomUserDetails(user,roles,perms);
    }
}
