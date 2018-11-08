package com.minlia.modules.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.minlia.cloud.body.Body;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;
import java.util.List;

/**
 * 当前用户上下文
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class UserContext implements Body {

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户唯一编号
     */
    private String guid;

    /**
     * 过期时间
     */
    private  Date expireDate;

    /**JwtTokenFactory
     * 当前角色
     */
    private String currrole;

    /**
     * 拥有角色
     */
    private List<String> roles;

    /**
     * 拥有权限
     * [{'authority':'user.change.password'}]
     */
    @JsonIgnore
    private List<GrantedAuthority> authorities;

    /**
     * 拥有权限点
     */
    private List<String> permissions;

    /**
     * 拥有菜单
     */
    private Object navigations;


}
