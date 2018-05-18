package com.minlia.module.aliyun.dypls.entity;

import com.minlia.module.data.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by garen on 2018/5/18.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DyplsBind extends AbstractEntity {

    private String poolKey;

    /**
     * 三元绑定关系对应的绑定ID
     */
    private String subsId;

    /**
     * 调用绑定接口时分配的隐私号码
     */
    private String secretNo;

    /**
     * AXB关系中的A号码
     */
    private String phoneNoA;

    /**
     * AXB关系中的B号码
     */
    private String phoneNoB;

    /**
     * 绑定关系对应的失效时间-不能早于当前系统时间
     */
    private Date expireTime;

    /**
     * 外部业务自定义ID属性
     */
    private String outId;

}
