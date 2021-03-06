package com.minlia.modules.attachment.body;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * Created by garen on 2017/12/30.
 */
public class AttachmentData {

    @ApiModelProperty(value = "附件类型",example = "jpg")
    @NotNull
    private String url;

    @ApiModelProperty(value = "访问令牌", example = "234ehdskjds-sdfas-dfds-fds-fa-sdfs-sdfas-d")
    @NotNull
    private String key;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}