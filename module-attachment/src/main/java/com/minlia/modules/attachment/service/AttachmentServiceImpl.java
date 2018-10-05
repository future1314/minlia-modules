package com.minlia.modules.attachment.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.minlia.cloud.body.Response;
import com.minlia.cloud.utils.ApiAssert;
import com.minlia.modules.attachment.body.AttachmentCreateRequestBody;
import com.minlia.modules.attachment.body.AttachmentData;
import com.minlia.modules.attachment.body.AttachmentQueryRequestBody;
import com.minlia.modules.attachment.constant.AttachmentCode;
import com.minlia.modules.attachment.entity.Attachment;
import com.minlia.modules.attachment.event.AttachmentEvent;
import com.minlia.modules.attachment.mapper.AttachmentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class AttachmentServiceImpl implements AttachmentService {

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Override
    public Attachment create(Attachment attachment) {
        attachmentMapper.create(attachment);
        return attachment;
    }

    @Override
    @Transactional
    public List<Attachment> create(List<Attachment> attachments) {
        attachmentMapper.createBatch(attachments);
        return attachments;
    }

    @Override
    @Transactional
    public List<Attachment> create(AttachmentCreateRequestBody requestBody) {
        attachmentMapper.deleteByRelationIdAndBelongsTo(requestBody.getRelationId(),requestBody.getBelongsTo());

        List<Attachment> attachments = Lists.newArrayList();
        for (AttachmentData data : requestBody.getData()) {
            Attachment attachment = Attachment.builder()
                    .relationId(requestBody.getRelationId())
                    .belongsTo(requestBody.getBelongsTo())
                    .url(data.getUrl())
                    .accessKey(data.getAccessKey())
                    .build();
            attachmentMapper.create(attachment);
            attachments.add(attachment);
        }

        AttachmentEvent.onUpload(attachments.get(0));
        return attachments;
    }

    @Override
    @Transactional
    public Attachment update(Attachment attachment) {
        attachmentMapper.update(attachment);
        AttachmentEvent.onUpload(attachment);
        return attachment;
    }

    @Override
    @Transactional
    public void bindByAccessKey(String accessKey, String relationId, String belongsTo) {
        Attachment attachment = attachmentMapper.queryByKey(accessKey);
        ApiAssert.notNull(attachment, AttachmentCode.Message.ETAG_NOT_EXISTS);
        ApiAssert.isNull(attachment.getRelationId(), AttachmentCode.Message.ETAG_ALREADY_BIND);
        ApiAssert.isNull(attachment.getBelongsTo(), AttachmentCode.Message.ETAG_ALREADY_BIND);

        attachment.setRelationId(relationId);
        attachment.setBelongsTo(belongsTo);
        attachmentMapper.update(attachment);
    }

    @Override
    @Transactional
    public Response delete(Long id) {
        attachmentMapper.delete(id);
        return Response.success();
    }

    @Override
    @Transactional
    public Response delete(String relationId, String belongsTo) {
        attachmentMapper.deleteByRelationIdAndBelongsTo(relationId,belongsTo);
        return Response.success();
    }

    @Override
    public Attachment queryById(Long id) {
        return attachmentMapper.queryById(id);
    }

    @Override
    public Attachment queryByKey(String key) {
        return attachmentMapper.queryByKey(key);
    }

    @Override
    public String queryUrls(String relationId, String belongsTo) {
        return attachmentMapper.queryUrls(relationId,belongsTo);
    }

    @Override
    public String queryFirstUrl(String relationId, String belongsTo) {
        return attachmentMapper.queryFirstUrl(relationId,belongsTo);
    }

    @Override
    public List<Attachment> queryAllByRelationIdAndBelongsTo(String relationId, String belongsTo) {
        return attachmentMapper.queryByRelationIdAndBelongsTo(relationId,belongsTo);
    }

    @Override
    public List<Attachment> queryList(AttachmentQueryRequestBody requestBody) {
        return attachmentMapper.queryList(requestBody);
    }

    @Override
    public PageInfo<Attachment> queryPage(AttachmentQueryRequestBody requestBody, Pageable pageable) {
        return PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize()).doSelectPageInfo(()-> attachmentMapper.queryList(requestBody));
    }

}
