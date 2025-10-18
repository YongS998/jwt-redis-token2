package com.yongs.token2.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 功能：
 * 作者：YongS
 * 日期：2025/10/17 23:06
 */
@Data
public class LoginDTO {
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
}
