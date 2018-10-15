package com.minlia.module.aliyun.sesame.endpoint;

import com.minlia.cloud.constant.ApiPrefix;
import com.minlia.module.aliyun.sesame.body.SesameVerificationRequest;
import com.minlia.module.aliyun.sesame.body.SesameVerificationResponse;
import com.minlia.module.aliyun.sesame.service.SesameVerificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = ApiPrefix.API + "sesame")
@Api(tags = "Aliyun Sesame Verification", description = "阿里云实名认证")
@Slf4j
public class SesameVerificationEndpoint {

	@Autowired
	private SesameVerificationService sesameVerificationService;
	
	@ApiOperation(value = "验证", notes = "验证", httpMethod = "POST", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "verification", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public SesameVerificationResponse verification(@Valid @RequestBody SesameVerificationRequest requestBody) {
		return sesameVerificationService.verification(requestBody);
	}

}