package com.minlia.module.wechat.mp.service;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.google.common.collect.Lists;
import com.minlia.cloud.body.StatefulBody;
import com.minlia.cloud.body.impl.FailureResponseBody;
import com.minlia.cloud.body.impl.SuccessResponseBody;
import com.minlia.cloud.code.ApiCode;
import com.minlia.cloud.utils.ApiPreconditions;
import com.minlia.module.wechat.ma.body.WechatOpenAccountQueryBody;
import com.minlia.module.wechat.ma.entity.WechatOpenAccount;
import com.minlia.module.wechat.ma.enumeration.WechatOpenidType;
import com.minlia.module.wechat.ma.service.WechatMaService;
import com.minlia.module.wechat.ma.service.WechatOpenAccountService;
import com.minlia.module.wechat.mp.body.BindWxRequestBody;
import com.minlia.module.wechat.mp.body.LoginWechatRequestBody;
import com.minlia.modules.rbac.backend.common.constant.SecurityApiCode;
import com.minlia.modules.rbac.backend.permission.service.PermissionService;
import com.minlia.modules.rbac.backend.role.service.RoleService;
import com.minlia.modules.rbac.backend.user.entity.User;
import com.minlia.modules.rbac.backend.user.service.UserQueryService;
import com.minlia.modules.rbac.bean.to.UserRegistrationRequestBody;
import com.minlia.modules.rbac.service.UserRegistrationService;
import com.minlia.modules.security.constant.SecurityConstant;
import com.minlia.modules.security.model.UserContext;
import com.minlia.modules.security.model.token.AccessJwtToken;
import com.minlia.modules.security.model.token.JwtTokenFactory;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@Transactional
public class LoginThirdPartyServiceImpl implements LoginThirdPartyService {

    @Autowired
    private RoleService roleService;
    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private JwtTokenFactory tokenFactory;
    @Autowired
    private WechatMaService wechatMaService;
    @Autowired
    private UserQueryService userQueryService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserRegistrationService userRegistrationService;
    @Autowired
    private WechatOpenAccountService wechatOpenAccountService;

    @Override
    public StatefulBody loginByWxMpCode(LoginWechatRequestBody body) throws WxErrorException {
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(body.getCode());
        return this.login(WechatOpenidType.MINIAPP,wxMpOAuth2AccessToken.getUnionId(),wxMpOAuth2AccessToken.getOpenId(),body.getType(),body.getCode());
    }

    @Override
    public StatefulBody loginByWxMaCode(LoginWechatRequestBody body) {
        //远程从微信获取小程序信息
        WxMaJscode2SessionResult sessionResult = wechatMaService.getSessionInfo(body.getType(),body.getCode());
        ApiPreconditions.is( null == sessionResult.getOpenid(),ApiCode.NOT_NULL,"获取微信Session失败！");
        return this.login(WechatOpenidType.MINIAPP,sessionResult.getUnionid(),sessionResult.getOpenid(),body.getType(),body.getCode());
    }

    private StatefulBody login(WechatOpenidType wechatOpenidType,String unionId,String openId,String openidSubitem,String wxCode){
        WechatOpenAccount wechatOpenAccount = wechatOpenAccountService.queryOne(WechatOpenAccountQueryBody.builder().type(wechatOpenidType).openId(openId).build());

        if (null == wechatOpenAccount) {
            //创建openId信息
            wechatOpenAccount = new WechatOpenAccount();
            wechatOpenAccount.setUnionId(unionId);
            wechatOpenAccount.setOpenId(openId);
            wechatOpenAccount.setWxCode(wxCode);
            wechatOpenAccount.setType(WechatOpenidType.MINIAPP);
            wechatOpenAccount.setSubitem(openidSubitem);

            //判断是否用其他openId登录过 TODO
            List<WechatOpenAccount> wechatOpenAccounts = wechatOpenAccountService.queryByUnionIdAndGuidNotNull(unionId);
            if (CollectionUtils.isEmpty(wechatOpenAccounts)) {
                wechatOpenAccountService.create(wechatOpenAccount);
                return FailureResponseBody.builder().code(SecurityApiCode.LOGIN_NOT_REGISTRATION).message("未注册").build();
            } else {
                wechatOpenAccount.setGuid(wechatOpenAccounts.get(0).getGuid());
                wechatOpenAccountService.create(wechatOpenAccount);
                User user = userQueryService.queryByGuid(wechatOpenAccount.getGuid());
                return SuccessResponseBody.builder().code(SecurityApiCode.LOGIN_SUCCESS).payload(getLoginInfoByUser(user)).build();
            }
        } else {
            //更新wxCode
            wechatOpenAccount.setWxCode(wxCode);
            wechatOpenAccountService.update(wechatOpenAccount);

            if (null != wechatOpenAccount.getGuid()) {
                User user = userQueryService.queryByGuid(wechatOpenAccount.getGuid());
                return SuccessResponseBody.builder().code(SecurityApiCode.LOGIN_SUCCESS).payload(getLoginInfoByUser(user)).build();
            } else {
                return FailureResponseBody.builder().code(SecurityApiCode.LOGIN_NOT_REGISTRATION).message("未注册").build();
            }
        }
    }

    @Override
    public StatefulBody bindByWxma(BindWxRequestBody body) {
        //根据手机号码查询用户是否存在
        User user = userQueryService.queryByUsername(body.getUsername());
        //明文密码
        String rawPassword = RandomStringUtils.randomAlphabetic(6);
        //不存在就注册
        if (null == user) {
            UserRegistrationRequestBody userRegistrationRequestBody = new UserRegistrationRequestBody();
            userRegistrationRequestBody.setUsername(body.getUsername());
            userRegistrationRequestBody.setCode(body.getSecurityCode());
            userRegistrationRequestBody.setPassword(rawPassword);
            user = userRegistrationService.registration(userRegistrationRequestBody);
        }

        //查询CODE是否存在,CODE必须唯一
        WechatOpenAccount codeAccount = wechatOpenAccountService.queryOne(WechatOpenAccountQueryBody.builder().wxCode(body.getWxCode()).build());
        ApiPreconditions.is(null == codeAccount, ApiCode.NOT_FOUND,"wxCode未判断是否绑定");

        WechatOpenAccount wechatOpenAccountByUser = wechatOpenAccountService.queryOne(WechatOpenAccountQueryBody.builder().guid(user.getGuid()).type(WechatOpenidType.MINIAPP).wxCode(body.getWxCode()).build());

        if (null == wechatOpenAccountByUser) {
            if (null == codeAccount.getGuid()) {
                //userId未绑定openId且 userId为空
                codeAccount.setGuid(user.getGuid());
                wechatOpenAccountService.update(codeAccount);
                return SuccessResponseBody.builder().payload(getLoginInfoByUser(user)).build();
            } else {
                ApiPreconditions.not(codeAccount.getGuid().equals(user.getId()), ApiCode.DATA_ALREADY_EXISTS, "该微信号已经绑定其他手机号码");
                return FailureResponseBody.builder().message("该微信号已经绑定其他手机号码").build();
            }
        } else {
            return FailureResponseBody.builder().message("该手机号码已经绑定其他微信号").build();
        }
    }

    @Override
    public HashMap getLoginInfoByUser(User user) {
        //如果当前角色为空获取默认角色
        String currrole = user.getDefaultRole();
        if (StringUtils.isBlank(currrole)) {
            currrole = SecurityConstant.ROLE_USER_CODE;
        }
        List<GrantedAuthority> authorities= permissionService.getGrantedAuthority(Lists.newArrayList(currrole));
        UserContext userContext = UserContext.builder().username(user.getUsername()).guid(user.getGuid()).currrole(currrole).authorities(authorities).build();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userContext, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(token);

        AccessJwtToken accessToken = this.tokenFactory.createAccessJwtToken(userContext);
        AccessJwtToken refreshToken = this.tokenFactory.createRefreshToken(userContext);
        HashMap tokenMap = new HashMap();
        tokenMap.put("token", accessToken.getToken());
        tokenMap.put("expireDate", (accessToken).getClaims().getExpiration().getTime());
        tokenMap.put("refreshToken", refreshToken.getToken());
        return tokenMap;
    }

}
