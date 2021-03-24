package com.example.cli.entity;


import java.io.Serializable;


import java.util.Set;
import javax.persistence.*;

import com.example.cli.domain.common.BaseTree;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;


/**
 * @author lw
 * @email liwei4@chinatelecom.cn
 * @date 2019-10-21 11:26:39
 */
@Data
@Table(name = "menu")
@Entity
public class Menu extends BaseTree<Menu> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 菜单id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 父级id
     */
    @Column(name = "parent_id")
    private Integer parentId;

    /**
     * 0启用；1停用
     */
    @Column(name = "status", columnDefinition = "tinyint default 0")
    private Integer status = 0;

    /**
     * VueRouter Path
     */
    @Column(name = "path")
    private String path;

    /**
     * 页面标题
     */
    @Column(name = "title")
    private String title;

    /**
     * 菜单图标
     */
    @Column(name = "icon")
    private String icon;

    /**
     * 路由Key
     */
    @Column(name = "permission_id")
    private String permissionId;

    /**
     * 路由名称
     */
    @Column(name = "permission_name")
    private String permissionName;

    /**
     * 组件名称
     */
    @Column(name = "component")
    private String component;

    /**
     * 组件路径
     */
    @Column(name = "component_path")
    private String componentPath;

    /**
     * 路由keepAlive
     */
    @Column(name = "keep_alive")
    private Boolean keepAlive;

    /**
     * 前端： false隐藏菜单
     */
    @Column(name = "show_Menu")
    private Boolean showMenu;

    /**
     * 前端：true隐藏子菜单
     */
    @Column(name = "hide_children")
    private Boolean hideChildren;

    /**
     * 前端：true隐藏头部内容
     */
    @Column(name = "hidden_header_content")
    private Boolean hiddenHeaderContent;

    /**
     * 前端a标签target
     */
    private String target;

    /**
     * 类型 1 菜单 2 功能
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 排序
     */
    @Column(name = "sort")
    private Integer sort;

    @Column(name = "default_check")
    private Boolean defaultCheck;

    /**
     * 前端路由重定向
     */
    @Column(name = "redirect")
    private String redirect;
}
