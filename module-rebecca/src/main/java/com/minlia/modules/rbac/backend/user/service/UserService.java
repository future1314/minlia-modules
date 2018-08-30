package com.minlia.modules.rbac.backend.user.service;


import com.minlia.modules.rbac.backend.user.body.UserCreateRequestBody;
import com.minlia.modules.rbac.backend.user.body.UserGarenRequestBody;
import com.minlia.modules.rbac.backend.user.body.UserUpdateRequestBody;
import com.minlia.modules.rbac.backend.user.entity.User;

import java.util.Set;

/**
 * 用户接口
 */
public interface UserService {

    User create(UserCreateRequestBody entity);

    User update(UserUpdateRequestBody body);

    User update(User user);

    void delete(String guid);

    Boolean locked(String guid);

    Boolean disabled(String guid);

    void grant(String guid, Set<Long> roles);

    void grant(long id, Set<Long> roles);

}
