package com.minlia.modules.rbac.service;


import com.minlia.cloud.service.WriteOnlyService;
import com.minlia.modules.rbac.dao.UserDao;
import com.minlia.modules.rbac.domain.User;
import com.minlia.modules.rbac.endpoint.body.UserGarentRoleRequestBody;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by will on 8/14/17.
 */
@Transactional(readOnly = true)
public interface UserService extends WriteOnlyService<UserDao, User, Long> {


  public static final String ENTITY = "user";

  public static final String ENTITY_CREATE = ENTITY + ".create";
  public static final String ENTITY_READ = ENTITY + ".read";
  public static final String ENTITY_UPDATE = ENTITY + ".update";
  public static final String ENTITY_DELETE = ENTITY + ".delete";


  /**
   * 为用户授权
   */
  User grantRole(UserGarentRoleRequestBody body);

  /**
   * 回收用户权限
   */
  User revokeRole(UserGarentRoleRequestBody body);


}
