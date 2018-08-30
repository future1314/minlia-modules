package com.minlia.modules.rbac.backend.loginlog.service;

import com.minlia.modules.http.NetworkUtil;
import com.minlia.modules.rbac.backend.loginlog.entity.LoginLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by garen on 2018/5/27.
 */
@Aspect
@Component
public class LoginAspect {

    @Autowired
    private LoginLogService loginLogService;

    @Pointcut("execution (* com.minlia.modules.rbac.authentication.RbacAuthenticationService.authentication(..))")
    public void login(){};

//    @Pointcut("execution(@java.lang.Deprecated * *(..))")
//    public void login1(){};

    @Before("login()")
    public void beforeLogin(JoinPoint joinPoint){
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) joinPoint.getArgs()[0];
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        String username = (String) authenticationToken.getPrincipal();
//        String password = (String) authenticationToken.getCredentials();
//        String ipAddress = NetworkUtil.getIpAddress(request);
//        loginLogService.create(LoginLog.builder().username(username).password(password).ipAddress(ipAddress).time(new Date()).build());
    }

}
