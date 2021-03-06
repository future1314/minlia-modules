package com.minlia.modules.attachment.service;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.minlia.cloud.body.StatefulBody;
import com.minlia.modules.attachment.body.AttachmentCreateRequestBody;
import com.minlia.modules.attachment.body.AttachmentQueryRequestBody;
import com.minlia.modules.attachment.entity.Attachment;

import java.util.List;

/**
 * Created by will on 6/17/17.
 */
public interface AttachmentService {

    /**
     * 创建
     * @param attachments
     * @return
     */
    List<Attachment> create(List<Attachment> attachments);

    /**
     * 创建
     * @param body
     * @return
     */
    List<Attachment> create(AttachmentCreateRequestBody body);

    /**
     * 修改
     * @param entity
     * @return
     */
    StatefulBody update(Attachment entity);

    /**
     * 删除
     * @param id
     */
    StatefulBody delete(Long id);

    /**
     * 删除
     * @param businessId
     * @param businessType
     */
    StatefulBody delete(String businessId, String businessType);

    /**
     * 通过令牌绑定业务ID
     * @param accessToken
     * @param businessId
     */
    void bindByAccessKey(String accessToken, String businessId, String businessType);

    /**
     * 读取
     * @param id
     * @return
     */
    Attachment queryById(Long id);

    Attachment queryByAccessKey(String accessKey);

    List<Attachment> queryAllByBusinessIdAndBusinessType(String businessId, String businessType);

    /**
     * 返回所有
     * @return
     */
    List<Attachment> queryList(AttachmentQueryRequestBody body);

    /**
     * 返回所有
     * @return
     */
    PageInfo<Attachment> queryPage(AttachmentQueryRequestBody body, Page page);

}
