package com.minlia.module.version.bean.qo;

import com.minlia.module.data.bean.QueryRequest;
import com.minlia.module.version.enumeration.PlatformTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VersionQO extends QueryRequest {

    /**
     * 版本号
     */
    private String number;

    /**
     * 平台类型
     */
    private PlatformTypeEnum type;

    /**
     * 是否测试
     */
    private boolean test;

    /**
     * 是否最新
     */
    private boolean newest;

    /**
     * 是否启用
     */
    private Boolean enabled;

}