package com.minlia.modules.rbac.bean.qo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserQO {

    private Long id;

    private String guid;

    private String username;

    private String cellphone;

    private String email;

    private Boolean locked;

    private String referral;

    private Boolean enabled;

    private String roleCode;

}