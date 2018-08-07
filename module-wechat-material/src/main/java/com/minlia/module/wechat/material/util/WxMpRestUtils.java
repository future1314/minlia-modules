package com.minlia.module.wechat.material.util;

import com.google.gson.Gson;
import com.minlia.cloud.holder.ContextHolder;
import com.minlia.cloud.utils.ApiPreconditions;
import com.minlia.module.wechat.material.dto.WxMpMaterialBatchGetResult;
import me.chanjar.weixin.common.error.WxError;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.util.json.WxGsonBuilder;
import me.chanjar.weixin.mp.api.WxMpService;

import java.util.Map;

/**
 * Created by garen on 2018/6/29.
 */
public class WxMpRestUtils {

    private static WxMpService wxMpService;

    private static WxMpService getWxMpService(){
        if (null == wxMpService) {
            wxMpService = ContextHolder.getContext().getBean(WxMpService.class);
        }
        return wxMpService;
    }

    public static WxMpMaterialBatchGetResult post(String url, Map params) {
        String responseText = null;
        try {
            responseText = getWxMpService().post(url, WxGsonBuilder.create().toJson(params));
        } catch (WxErrorException e) {
            e.printStackTrace();
            ApiPreconditions.is(true, e.getError().getErrorCode(),e.getError().getErrorMsg());
        }
        WxError wxError = WxError.fromJson(responseText);
        if (wxError.getErrorCode() != 0) {
            ApiPreconditions.is(true,wxError.getErrorCode(),wxError.getErrorMsg());
        }
        return new Gson().fromJson(responseText, WxMpMaterialBatchGetResult.class);
    }

}
