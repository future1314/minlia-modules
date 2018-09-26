package com.minlia.modules.qcloud.faceid.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.minlia.cloud.body.Response;
import com.minlia.cloud.utils.ApiAssert;
import com.minlia.module.common.util.NumberGenerator;
import com.minlia.modules.qcloud.faceid.bean.FaceAuth;
import com.minlia.modules.qcloud.faceid.body.QcloudFaceIdRecordQueryRequestBody;
import com.minlia.modules.qcloud.faceid.body.QcloudFaceIdRequestBody;
import com.minlia.modules.qcloud.faceid.body.QcloudFaceIdResponseBody;
import com.minlia.modules.qcloud.faceid.body.QcloudFaceIdResult;
import com.minlia.modules.qcloud.faceid.body.response.QcloudFaceIdResultResponseBody;
import com.minlia.modules.qcloud.faceid.entity.QcloudFaceIdRecord;
import com.minlia.modules.qcloud.faceid.utils.SignUtils;
import com.minlia.modules.qcloud.start.config.QcloudConfig;
import com.minlia.modules.qcloud.start.constant.QcloudCode;
import com.minlia.modules.qcloud.start.service.QcloudAuthService;
import com.minlia.modules.rbac.context.SecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by garen on 2018/4/19.
 */
@Slf4j
@Service
public class QcloudFaceidServiceImpl implements QcloudFaceidService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private QcloudConfig qcloudConfig;

    @Autowired
    private QcloudAuthService qcloudAuthService;

    @Autowired
    private QcloudFaceIdRecordService faceIdRecordService;

    @Override
    public String sign(String orderNo, String name, String idNo, String userId) {
        List<String> values = Lists.newArrayList();
        values.add(this.qcloudConfig.getAppid());
        values.add(orderNo);
        values.add(name);
        values.add(idNo);
        values.add(userId);
        values.add("1.0.0");
        values.add(this.qcloudAuthService.getApiSignTicket());
        return SignUtils.sign(values);
    }

    @Override
    public Response geth5faceid(QcloudFaceIdRequestBody requestBody) {
        String userId = SecurityContextHolder.getCurrentGuid();
        QcloudFaceIdRecord faceIdRecord = faceIdRecordService.queryOne(QcloudFaceIdRecordQueryRequestBody.builder().userId(userId).isAuth(true).build());
        ApiAssert.isNull(faceIdRecord, QcloudCode.Message.FACEID_ALREADY_AUTHENTICATED);
        String orderNo = NumberGenerator.generatorByTimestamp("ON",3);

        FaceAuth faceAuth = FaceAuth.builder()
                .webankAppId(this.qcloudConfig.getAppid())
                .orderNo(orderNo)
                .name(requestBody.getName())
                .idNo(requestBody.getIdNo())
                .userId(userId)
                .sourcePhotoType("1")
                .version("1.0.0")
                .sign(this.sign(orderNo,requestBody.getName(),requestBody.getIdNo(),userId))
                .build();
        ResponseEntity<QcloudFaceIdResponseBody> responseEntity = restTemplate.postForEntity("https://idasc.webank.com/api/server/h5/geth5faceid",faceAuth,QcloudFaceIdResponseBody.class);
        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            if (responseEntity.getBody().isSuccess()) {
                QcloudFaceIdResult result = responseEntity.getBody().getResult();

                String nonce = UUID.randomUUID().toString();
                nonce = nonce.replace("-","");
                //设置签名
                List<String> values = Lists.newArrayList();
                values.add(this.qcloudConfig.getAppid());
                values.add(orderNo);
                values.add(userId);
                values.add("1.0.0");
                values.add(result.getH5faceId());
                values.add(qcloudAuthService.getApiNonceTicket());
                values.add(nonce);
                result.setSign(SignUtils.sign(values));

//                result.setAppId(FACEID_APP_ID);
                result.setWebankAppId(this.qcloudConfig.getAppid());
                result.setVersion("1.0.0");
                result.setNonce(nonce);
                result.setResultType(null);
                result.setUserId(userId);

                //持久化
//                if (null == faceIdRecord) {
                    faceIdRecord = QcloudFaceIdRecord.builder()
                            .orderNo(orderNo)
                            .userId(userId)
                            .name(requestBody.getName())
                            .idNo(requestBody.getIdNo())
                            .isAuth(false)
                            .build();
                    faceIdRecordService.create(faceIdRecord);
//                }
                return Response.success(result);
            } else {
                return Response.failure(Integer.valueOf(responseEntity.getBody().getCode()), responseEntity.getBody().getMsg());
            }
        } else {
            return Response.failure(responseEntity.getStatusCode().value(), responseEntity.getBody().getMsg());
        }
    }

    @Override
    public QcloudFaceIdResultResponseBody getH5faceidResult(String orderNo) {
        String nonce = UUID.randomUUID().toString().replace("-", "");
        List<String> values = Lists.newArrayList();
        values.add(this.qcloudConfig.getAppid());
        values.add(orderNo);
        values.add(this.qcloudAuthService.getApiSignTicket());
        values.add("1.0.0");
        values.add(nonce);
        String sign = SignUtils.sign(values);

        Map<String,String> map = Maps.newHashMap();
        map.put("app_id",this.qcloudConfig.getAppid());
        map.put("version","1.0.0");
        map.put("nonce",nonce);
        map.put("order_no",orderNo);
        map.put("sign",sign);
        QcloudFaceIdResultResponseBody responseBody = restTemplate.getForObject(H5FACEID_RESULT_URL, QcloudFaceIdResultResponseBody.class,map);
        return responseBody;
    }

}