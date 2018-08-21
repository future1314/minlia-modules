//package com.minlia.module.language.v1.listener;
//
//import com.google.common.collect.Maps;
//import com.minlia.module.language.v1.LanguageService;
//import com.minlia.module.security.constants.SecurityConstants;
//import com.minlia.module.security.v1.repository.RoleRepository;
//import com.minlia.module.security.v1.service.PermissionCreationService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Map;
//
//@Component
//public class LanguageSeedDataInitializeListener implements ApplicationListener<ContextRefreshedEvent> {
//
//    private boolean alreadySetup = false;
//
//    @Autowired
//    private RoleRepository roleRepository;
//
//    @Autowired
//    PermissionCreationService permissionCreationService;
//
//    // API
//
//    @Override
//    @Transactional
//    public void onApplicationEvent(final ContextRefreshedEvent event) {
////        log.debug("Starting Initialize Language Seed Data ");
//        if (alreadySetup) {
//            return;
//        }
//
//        //定义一个MAP, 根据MAP插入初始化数据
//        Map<String,String> initialAdminPermissions= Maps.newHashMap();
//
//        String entityName="国际化译文";
//        //菜单
//        initialAdminPermissions.put(LanguageService.ENTITY_CREATE,SecurityConstants.CREATE_OPERATION_DESCRIPTION_CN+entityName);
//        initialAdminPermissions.put(LanguageService.ENTITY_READ,SecurityConstants.READ_OPERATION_DESCRIPTION_CN+entityName);
//        initialAdminPermissions.put(LanguageService.ENTITY_UPDATE,SecurityConstants.UPDATE_OPERATION_DESCRIPTION_CN+entityName);
//        initialAdminPermissions.put(LanguageService.ENTITY_DELETE,SecurityConstants.DELETE_OPERATION_DESCRIPTION_CN+entityName);
//        initialAdminPermissions.put(LanguageService.ENTITY_SEARCH,SecurityConstants.SEARCH_OPERATION_DESCRIPTION_CN+entityName);
//        permissionCreationService.initialAdminPermissions(initialAdminPermissions);
//
//        alreadySetup = true;
//    }
//
//
//}