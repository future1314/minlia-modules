package com.minlia.module.pooul.service.impl;

import com.minlia.cloud.utils.ApiAssert;
import com.minlia.module.pooul.bean.dto.PooulDTO;
import com.minlia.module.pooul.bean.to.PooulLoginTO;
import com.minlia.module.pooul.contract.PooulCode;
import com.minlia.module.pooul.service.PooulAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by garen on 2018/9/5.
 */
@Service
public class PooulAuthServiceImpl implements PooulAuthService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${pooul.auth.username}")
    public String username;

    @Value("${pooul.auth.password}")
    public String password;

    @Override
    public String login() {
        PooulLoginTO loginTO = new PooulLoginTO(username,password);
        ResponseEntity<PooulDTO> responseEntity = restTemplate.postForEntity("https://api-dev.pooul.com/web/user/session/login_name",loginTO,PooulDTO.class);
        ApiAssert.state(responseEntity.getStatusCode().equals(HttpStatus.OK), PooulCode.Message.LOGIN_FAILURE, responseEntity.getStatusCodeValue());
        ApiAssert.state(responseEntity.getBody().isSuccess(), PooulCode.Message.LOGIN_FAILURE, responseEntity.getBody().getMsg());
        return responseEntity.getHeaders().get("authorization").get(0);
    }

    @Override
    public HttpHeaders getHeaders(){
        //获取token
        String authorization = this.login();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization",authorization);
        return headers;
    }

}
