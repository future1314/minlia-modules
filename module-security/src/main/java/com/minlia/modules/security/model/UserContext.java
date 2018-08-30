package com.minlia.modules.security.model;

import com.minlia.cloud.body.Body;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
     * 过期时间
     */
    private  Date expireDate;

    /**
     * 当前角色
     */
    private String currrole;

    /**
     * 拥有角色
     */
    private List<String> roles;

    /**
     * 拥有权限点
     * [{'authority':'user.change.password'}]
     */
    private List<GrantedAuthority> authorities;

    /**
     * 拥有菜单
     */
    private List<String> navigations;


}
