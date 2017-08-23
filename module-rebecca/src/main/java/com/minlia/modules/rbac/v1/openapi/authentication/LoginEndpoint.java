package com.minlia.modules.rbac.v1.openapi.authentication;

import com.minlia.modules.security.authentication.credential.Credential;
import com.minlia.modules.security.model.token.AccessJwtToken;
import com.minlia.modules.security.model.token.JwtToken;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by will on 7/21/17.
 * This is just a fake control for springfox-swagger2 to generate api-docs
 */

@CrossOrigin
@Api(tags = "OpenApi", description = "开放接口")
@RestController
public class LoginEndpoint {

  @ApiOperation(value = "登录", notes = "登录")
  @RequestMapping(value = "/api/auth/login", method = RequestMethod.POST, produces = "application/json")
  public JwtToken login(@RequestBody Credential loginRequest) {
    Claims claims = null;
    return new AccessJwtToken("token", claims);
  }

}
