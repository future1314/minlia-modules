package com.minlia.modules.rbac.authentication;

//import com.minlia.module.redis.service.RedisService;
import com.minlia.modules.rbac.backend.permission.service.PermissionService;
import com.minlia.modules.rbac.backend.role.service.RoleService;
import com.minlia.modules.rbac.backend.user.entity.User;
import com.minlia.modules.rbac.backend.user.mapper.UserMapper;
import com.minlia.modules.rbac.backend.user.service.UserService;
import com.minlia.modules.rbac.event.LoginSuccessEvent;
import com.minlia.modules.security.authentication.service.AbstractAuthenticationService;
import com.minlia.modules.security.constant.SecurityConstant;
import com.minlia.modules.security.model.UserContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

/**
 * Created by will on 8/14/17.
 * 框架提供的抽象认证实现
 */
@Component
public class RbacAuthenticationService extends AbstractAuthenticationService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

//    @Autowired
//    private RedisService redisService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private PermissionService permissionService;

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Authentication authentication(Authentication authentication) {
        Assert.notNull(authentication, "No authentication data provided");
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        User user = userMapper.queryByUsernameOrCellphoneOrEmail(username,username,username);
        if (null == user) {
            throw new UsernameNotFoundException("User not exists:" + username);
        }//        else if (user.getExpired()) throw new AccountExpiredException("账号过期");
        else if (user.getCredentialsExpired()) {
            throw new CredentialsExpiredException("凭证已过期");
        } else if (!user.getEnabled()) {
            throw new DisabledException("账号已禁用");
        } else if (user.getLocked() && new Date().before(user.getLockTime())) {
            throw new LockedException("账号已锁定");
        } else if (!encoder.matches(password,user.getPassword())) {
            //密码错误 锁定次数+1
            user.setLockLimit(user.getLockLimit()+ NumberUtils.INTEGER_ONE);
            //如果超过5次 直接锁定 TODO 动态配置
            if (user.getLockLimit() > 2) {
                user.setLocked(true);
                //错的越多，锁的越久
//                user.setLockTime(LocalDateTime.now().plusMinutes((long) Math.pow(user.getLockLimit()-3,3)));
                user.setLockTime(DateUtils.addMinutes(new Date(), (int) Math.pow(user.getLockLimit()-3,3)));
                //直接 锁定一天
//                user.setLockTime(user.getLockTime().plusDays(1));
            }
            userService.update(user);
            throw new BadCredentialsException("Password error");
        } else {
            //登录成功事件 TODO
            LoginSuccessEvent.onSuccess(user);
            user.setLocked(Boolean.FALSE);
            user.setLockLimit(NumberUtils.INTEGER_ZERO);
            user.setLastLoginTime(new Date());
            userMapper.update(user);

            //可以取到角色, 需要从角色里取到所有已授权的权限点
//            List<GrantedAuthority> authorities = SecurityUtil.getAuthorities(user.getRoles());
            List<String> roles = roleService.queryCodeByUserId(user.getId());
            if (CollectionUtils.isEmpty(roles)) {
                //如果角色为空设置为游客 TODO
                roles.add(SecurityConstant.ROLE_GUEST_CODE);
            }
            List<GrantedAuthority> authorities= permissionService.getGrantedAuthority(roles);
            UserContext userContext = UserContext.create(user.getUsername(), authorities);

            return new UsernamePasswordAuthenticationToken(userContext, null, userContext.getAuthorities());
        }
    }
}
