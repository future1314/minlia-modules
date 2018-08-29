package com.minlia.modules.rbac.backend.user.mapper;


import com.minlia.modules.rbac.backend.user.body.UserQueryRequestBody;
import com.minlia.modules.rbac.backend.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public interface UserMapper  {

    void create(User user);

    void update(User user);

    void delete(Long id);

    void locked(User user);

    void disabled(User user);

    void grant(Long id, Set<Long> roles);

    User queryById(Long id);

    User queryByGuid(String guid);

    User queryByUsername(String username);

    User queryByCellphone(String cellphone);

    long count(UserQueryRequestBody body);

    List<User> queryList(UserQueryRequestBody body);

    User queryByUsernameOrCellphoneOrEmail(String username, String cellphone, String email);

}
