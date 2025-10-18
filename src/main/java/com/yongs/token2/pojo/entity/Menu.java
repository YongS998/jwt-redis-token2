package com.yongs.token2.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 菜单/权限表
 * </p>
 *
 * @author Yongs
 * @since 2025-10-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_menu")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父菜单ID，一级为0
     */
    private Long parentId;

    /**
     * 类型：0-目录，1-菜单，2-按钮/权限
     */
    private Integer type;

    /**
     * 权限标识，如 user:list, ROLE_ADMIN
     */
    private String name;

    /**
     * 菜单/按钮名称
     */
    private String title;

    /**
     * 路由路径（菜单）
     */
    private String path;

    /**
     * 前端组件路径（菜单）
     */
    private String component;

    /**
     * 图标
     */
    private String icon;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否显示：0-否，1-是（菜单）
     */
    private Integer visible;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 权限字符串，如 user:add, order:delete
     */
    private String perms;

    /**
     * 备注
     */
    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
