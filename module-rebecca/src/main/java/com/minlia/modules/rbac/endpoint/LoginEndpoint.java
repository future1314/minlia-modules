package com.minlia.modules.rbac.endpoint;

import com.minlia.cloud.body.Response;
import com.minlia.cloud.constant.ApiPrefix;
import com.minlia.modules.rbac.bean.to.LoginCredentialRequestBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by will on 7/21/17.
 * This is just a fake control for springfox-swagger2 to generate api-docs
 */
@Api(tags = "System Login", description = "登录")
@CrossOrigin
@RestController
@RequestMapping(value = ApiPrefix.API + "auth/login")
public class LoginEndpoint {

    @ApiOperation(value = "登录", notes = "登录")
    @RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json")
    public Response login(@Valid @RequestBody LoginCredentialRequestBody credential) {
        return Response.success();
    }

}
