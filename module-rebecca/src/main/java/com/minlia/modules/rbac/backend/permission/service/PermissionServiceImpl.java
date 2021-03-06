package com.minlia.modules.rbac.backend.permission.service;

import com.google.common.collect.Lists;
import com.minlia.modules.rbac.backend.permission.body.PermissionUpdateRequestBody;
import com.minlia.modules.rbac.backend.permission.entity.Permission;
import com.minlia.modules.rbac.backend.permission.mapper.PermissionMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(String code, String label) {
        if (!this.exists(code)) {
            Permission permission = new Permission();
            permission.setCode(code);
            permission.setLabel(label);
            permissionMapper.create(permission);
        }
    }

    @Override
    @Transactional
    public void create(Set<Permission> permissions) {
        if (CollectionUtils.isNotEmpty(permissions)) {
            for (Permission permission : permissions) {
                this.create(permission.getCode(),permission.getLabel());
            }
        }
    }

    @Override
    @Transactional
    public Permission update(PermissionUpdateRequestBody body) {
        Permission permission = permissionMapper.queryById(body.getId());
        permission.setLabel(body.getLabel());
        permissionMapper.update(permission);
        return permission;
    }

    @Override
    public void grantAll(Long roleId) {
        permissionMapper.grantAll(roleId);
    }

    @Override
    public void clear() {
        permissionMapper.clear();
    }

    private boolean exists(String code) {
        return permissionMapper.countByCode(code) > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public Long countById(Long id) {
        return permissionMapper.countById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByCode(String code) {
        return permissionMapper.countByCode(code);
    }

    @Override
    public List<Map<String,Object>> tree() {
        List<Map<String,Object>> ones = Lists.newArrayList();
        List<String> oneLevel = permissionMapper.oneLevel();
        for (String one : oneLevel) {
            List<Map<String,Object>> twos = Lists.newArrayList();
            List<String> twoLevel = permissionMapper.twoLevel(one);
            for (String two : twoLevel) {
                List<Map<String,Object>> threes = Lists.newArrayList();
                List<Map> threeLevel = permissionMapper.threeLevel(one+"."+two);
                for (Map three : threeLevel) {
                    threes.add(three);
                }
                Map map = new HashMap();
                map.put(two,threes);
                twos.add(map);
            }
            Map map = new HashMap();
            map.put(one,twos);
            ones.add(map);
        }
        return ones;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Permission> queryAll() {
        return permissionMapper.queryAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Permission> queryListByGuid(String guid) {
        return permissionMapper.queryListByGuid(guid);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Permission> queryListByRoleCodes(List<String> roleCodes) {
        return permissionMapper.queryListByRoleCodes(roleCodes);
    }

    @Transactional(readOnly = true)
    public List<String> queryCodesByRoleCodes(List<String> roleCodes) {
        return permissionMapper.queryCodesByRoleCodes(roleCodes);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Permission> queryPage(RowBounds rowBounds) {
        return permissionMapper.queryPage(rowBounds);
    }

    @Override
    @Transactional(readOnly = true)
//    @Cacheable(value = { "permission" }, key = "#p0",unless="#result==null")
    public List<GrantedAuthority> getGrantedAuthority(List<String> roleCodes) {
        final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        final List<String> permissions = queryCodesByRoleCodes(roleCodes);
        if (CollectionUtils.isNotEmpty(permissions)) {
            for (String permission : permissions) {
                authorities.add(new SimpleGrantedAuthority(permission));
            }
        }
        return authorities;
    }

}
