package com.minlia.modules.rbac.listener;

import com.google.common.collect.Maps;
import com.minlia.cloud.utils.EnvironmentUtils;
import com.minlia.module.language.v1.service.LanguageCreationService;
import com.minlia.modules.rbac.context.PermissionContextHolder;
import com.minlia.modules.rbac.service.PermissionCreationService;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 仅扫描Endpoint层权限注解
 * 使用系统级别的 Language进行国际化配置
 */
@Slf4j
@Component
public class SecuredAnnotationInitializingListener implements
    ApplicationListener<ApplicationReadyEvent> {

  @Autowired
  PermissionCreationService permissionCreationService;

  @Autowired
  LanguageCreationService languageCreationService;
  /**
   * 获取到所有注解的类,初始化到数据库
   */
  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    log.debug("SecuredAnnotationInitializingListener 获取到所有注解的类,初始化到数据库");
    if (EnvironmentUtils.isProduction()) {
      return;
    }

    Set<String> permissions = PermissionContextHolder.get();
    Map<String,String > adminPermissions=Maps.newConcurrentMap();
    for (String permission : permissions) {
//      log.debug("Permission inserted to db: {}", permission);
      permissionCreationService.addPermission(permission,permission);
      languageCreationService.initialLanguage(permission);
      adminPermissions.put(permission,permission);
    }
    permissionCreationService.initialAdminPermissions(adminPermissions);

  }
}