//package com.minlia.module.wallet.v1.endpoint;
//
//import com.google.common.collect.Lists;
//import com.minlia.boot.v1.bean.StatefulBody;
//import com.minlia.boot.v1.bean.impl.SuccessResponseBody;
//import com.minlia.boot.v1.web.ApiPrefix;
//import com.minlia.module.data.query.v2.DynamicSpecifications;
//import com.minlia.module.data.query.v2.Operator;
//import com.minlia.module.data.query.v2.QueryCondition;
//import com.minlia.module.data.query.v2.bean.ApiSearchRequestBody;
//import com.minlia.module.security.v1.domain.User;
//import com.minlia.module.security.v1.service.UserQueryService;
//import com.minlia.module.security.v1.utils.SecurityUtils;
//import com.minlia.module.wallet.v1.bean.WithdrawQueryRequestBody;
//import com.minlia.module.wallet.v1.constants.WalletSecurityConstants;
//import com.minlia.module.wallet.v1.domain.WithdrawApply;
//import com.minlia.module.wallet.v1.repository.WithdrawApplyRepository;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections.CollectionUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.http.MediaType;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping(value = ApiPrefix.V1 + "withdraw/search")
//@Api(tags = "钱包-提现", description = "钱包")
//@Slf4j
//public class WithdrawSearchEndpoint {
//
//    @Autowired
//    private UserQueryService userQueryService;
//    @Autowired
//    private DynamicSpecifications spec;
//    @Autowired
//    private WithdrawApplyRepository withdrawApplyRepository;
//
//    @PreAuthorize(value = "hasAnyAuthority('" + WalletSecurityConstants.operate_withdraw_read_code + "')")
//    @ApiOperation(value = "我的(分页)", notes = "我的", httpMethod = "POST", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    @PostMapping(value = "me", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public StatefulBody me(@PageableDefault Pageable pageable,@RequestBody ApiSearchRequestBody<WithdrawQueryRequestBody> bean) {
//		bean.getPayload().setUserId(SecurityUtils.getCurrentUser().getId());
//        Page<WithdrawApply> page = withdrawApplyRepository.findAll(spec.buildSpecification(this.builder(bean)),pageable);
//        setUsername(page.getContent());
//        return SuccessResponseBody.builder().payload(page).build();
//    }
//
//    @PreAuthorize(value = "hasAnyAuthority('" + WalletSecurityConstants.operate_withdraw_search_code + "')")
//    @ApiOperation(value = "ID查询", notes = "单个查询", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
//    @GetMapping(value = "{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
//    public StatefulBody findOne(@PathVariable Long id) {
//        WithdrawApply withdrawApply = withdrawApplyRepository.findOne(id);
//        if (null != withdrawApply)
//            setUsername(Lists.newArrayList(withdrawApply));
//        return SuccessResponseBody.builder().payload(withdrawApply).build();
//    }
//
//    @PreAuthorize(value = "hasAnyAuthority('" + WalletSecurityConstants.operate_withdraw_search_code + "')")
//    @ApiOperation(value = "集合查询", notes = "集合查询", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
//    @PostMapping(value = "list", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
//    public StatefulBody list(@RequestBody ApiSearchRequestBody<WithdrawQueryRequestBody> bean) {
//        List<WithdrawApply> withdrawApplies = withdrawApplyRepository.findAll(spec.buildSpecification(this.builder(bean)));
//        setUsername(withdrawApplies);
//        return SuccessResponseBody.builder().payload(withdrawApplies).build();
//    }
//
//    @PreAuthorize(value = "hasAnyAuthority('" + WalletSecurityConstants.operate_withdraw_search_code + "')")
//    @ApiOperation(value = "分页查询", notes = "分页查询", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
//    @PostMapping(value = "paginated", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
//    public StatefulBody paginated(@PageableDefault Pageable pageable,@RequestBody ApiSearchRequestBody<WithdrawQueryRequestBody> bean) {
//        Page page = withdrawApplyRepository.findAll(spec.buildSpecification(this.builder(bean)),pageable);
//        setUsername(page.getContent());
//        return SuccessResponseBody.builder().payload(page).build();
//    }
//
//    private ApiSearchRequestBody<WithdrawQueryRequestBody> builder(ApiSearchRequestBody<WithdrawQueryRequestBody> bean) {
//        if (null != bean.getPayload().getUserId())
//            bean.getConditions().add(new QueryCondition("userId",Operator.eq,bean.getPayload().getUserId()));
//        if (null != bean.getPayload().getCardholder())
//            bean.getConditions().add(new QueryCondition("cardholder",Operator.eq,bean.getPayload().getCardholder()));
//        if (null != bean.getPayload().getCardNumber())
//            bean.getConditions().add(new QueryCondition("cardNumber",Operator.eq,bean.getPayload().getCardNumber()));
//        if (null != bean.getPayload().getWithdrawStatus())
//            bean.getConditions().add(new QueryCondition("withdrawStatus",Operator.eq,bean.getPayload().getWithdrawStatus()));
//        return bean;
//    }
//
//    private void setUsername(List<WithdrawApply> withdrawApplies) {
//        if (CollectionUtils.isNotEmpty(withdrawApplies)) {
//            for (WithdrawApply withdrawApply:withdrawApplies) {
//                User user = userQueryService.findOne(withdrawApply.getUserId());
//                withdrawApply.setUsername(user.getUsername());
//            }
//        }
//    }
//
//}