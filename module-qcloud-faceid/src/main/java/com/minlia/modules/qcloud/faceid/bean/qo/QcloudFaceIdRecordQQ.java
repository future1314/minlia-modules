package com.minlia.modules.qcloud.faceid.bean.qo;

import com.minlia.module.data.bean.QueryRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by garen on 2018/4/19.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QcloudFaceIdRecordQQ extends QueryRequest {

    private String userId;

    private String orderNo;

    private Boolean isAuth;

    private Long id;

    private String idNo;

}
