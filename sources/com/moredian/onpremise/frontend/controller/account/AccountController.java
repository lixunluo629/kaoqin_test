package com.moredian.onpremise.frontend.controller.account;

import com.moredian.onpremise.api.account.AccountService;
import com.moredian.onpremise.api.account.AuthCodeService;
import com.moredian.onpremise.api.account.AuthModuleService;
import com.moredian.onpremise.api.server.ConfigService;
import com.moredian.onpremise.api.server.ViewConfigService;
import com.moredian.onpremise.core.adapter.CacheAdapter;
import com.moredian.onpremise.core.aop.operLog.annotation.LogAnnotation;
import com.moredian.onpremise.core.common.constants.ConfigConstants;
import com.moredian.onpremise.core.common.constants.Constants;
import com.moredian.onpremise.core.model.request.BaseRequest;
import com.moredian.onpremise.core.model.request.CloneAccountRequest;
import com.moredian.onpremise.core.model.request.DeleteAccountRequest;
import com.moredian.onpremise.core.model.request.GetOrgCodeRequest;
import com.moredian.onpremise.core.model.request.ListAccountRequest;
import com.moredian.onpremise.core.model.request.ListAuthCodeRequest;
import com.moredian.onpremise.core.model.request.ModifyPasswordRequest;
import com.moredian.onpremise.core.model.request.OpenApiAppKeyRequest;
import com.moredian.onpremise.core.model.request.OpenApiAppTokenRequest;
import com.moredian.onpremise.core.model.request.OperLogListRequest;
import com.moredian.onpremise.core.model.request.ResetPasswordRequest;
import com.moredian.onpremise.core.model.request.SaveAccountRequest;
import com.moredian.onpremise.core.model.request.SaveAuthCodeRequest;
import com.moredian.onpremise.core.model.request.SaveModuleRequest;
import com.moredian.onpremise.core.model.request.SavePoseThresholdRequest;
import com.moredian.onpremise.core.model.request.SaveRecordPeriodRequest;
import com.moredian.onpremise.core.model.request.SaveSnapModeRequest;
import com.moredian.onpremise.core.model.request.SaveStrangerInfoRequest;
import com.moredian.onpremise.core.model.request.SaveStrangerShowInfoRequest;
import com.moredian.onpremise.core.model.request.SaveStrangerSpeechInfoRequest;
import com.moredian.onpremise.core.model.request.SystemBasicConfigRequest;
import com.moredian.onpremise.core.model.request.UpdateLanguageRequest;
import com.moredian.onpremise.core.model.request.UpdateOrgInfoRequest;
import com.moredian.onpremise.core.model.request.UserLoginRequest;
import com.moredian.onpremise.core.model.request.UserLoginRequestV2;
import com.moredian.onpremise.core.model.request.ViewConfigListRequest;
import com.moredian.onpremise.core.model.request.ViewConfigRequest;
import com.moredian.onpremise.core.model.response.AccountListResponse;
import com.moredian.onpremise.core.model.response.AccountOrgInfoResponse;
import com.moredian.onpremise.core.model.response.AuthModuleResponse;
import com.moredian.onpremise.core.model.response.CommonResponse;
import com.moredian.onpremise.core.model.response.DataResponse;
import com.moredian.onpremise.core.model.response.ListAuthCodeResponse;
import com.moredian.onpremise.core.model.response.OpenApiAppKeyResponse;
import com.moredian.onpremise.core.model.response.OpenApiAppTokenResponse;
import com.moredian.onpremise.core.model.response.OperLogResponse;
import com.moredian.onpremise.core.model.response.QueryConfigResponse;
import com.moredian.onpremise.core.model.response.QueryMemoryInfoResponse;
import com.moredian.onpremise.core.model.response.SaveAuthCodeResponse;
import com.moredian.onpremise.core.model.response.SystemBasicConfigResponse;
import com.moredian.onpremise.core.model.response.UserLoginResponse;
import com.moredian.onpremise.core.model.response.ViewConfigResponse;
import com.moredian.onpremise.core.utils.PageList;
import com.moredian.onpremise.core.utils.RuntimeSystemUtils;
import com.moredian.onpremise.frontend.controller.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.hyperic.sigar.FileSystemUsage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Api(value = "on-premise-account", description = "on-premise账户相关")
@RestController
/* loaded from: onpremise-frontend-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/frontend/controller/account/AccountController.class */
public class AccountController extends BaseController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthCodeService authCodeService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private AuthModuleService authModuleService;

    @Autowired
    private ViewConfigService viewConfigService;

    @RequestMapping(value = {"/account/account/login"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "loginRequest", value = "请求实体", required = true, dataType = "UserLoginRequest", paramType = "body")})
    @ApiOperation(value = "用户登录", notes = "用户登录")
    public DataResponse<UserLoginResponse> login(@RequestBody UserLoginRequest loginRequest, HttpServletRequest request) {
        loginRequest.setSessionId(request.getSession().getId());
        DataResponse<UserLoginResponse> response = new DataResponse().success();
        response.setData(this.accountService.login(loginRequest));
        return response;
    }

    @RequestMapping(value = {"/account/account/login/v2"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "loginRequest", value = "请求实体", required = true, dataType = "UserLoginRequestV2", paramType = "body")})
    @ApiOperation(value = "用户登录", notes = "用户登录")
    public DataResponse<UserLoginResponse> loginV2(@RequestBody UserLoginRequestV2 loginRequest, HttpServletRequest request) {
        loginRequest.setSessionId(request.getSession().getId());
        DataResponse<UserLoginResponse> response = new DataResponse().success();
        response.setData(this.accountService.loginV2(loginRequest));
        return response;
    }

    @RequestMapping(value = {"/account/account/logout"}, method = {RequestMethod.POST})
    @ApiOperation(value = "用户登出", notes = "用户登出")
    public CommonResponse logout() {
        BaseRequest request = new BaseRequest();
        this.accountService.logout(super.getBaseRequest(request));
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/account/account/getAccountOrgInfo"}, method = {RequestMethod.POST})
    @ApiOperation(value = "获取账户机构基本信息", notes = "获取账户机构基本信息")
    public DataResponse<AccountOrgInfoResponse> getAccountOrgInfo() {
        BaseRequest request = new BaseRequest();
        BaseRequest request2 = super.getBaseRequest(request);
        DataResponse<AccountOrgInfoResponse> response = new DataResponse().success();
        response.setData(this.accountService.getAccountOrgInfo(request2));
        return response;
    }

    @RequestMapping(value = {"/account/account/updateOrgInfo"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "机构配置相关", description = "修改账户机构名称")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "UpdateOrgInfoRequest", paramType = "body")})
    @ApiOperation(value = "修改账户机构名称", notes = "修改账户机构名称")
    public CommonResponse updateOrgInfo(@RequestBody UpdateOrgInfoRequest request) {
        return this.accountService.updateOrgInfo((UpdateOrgInfoRequest) super.getBaseRequest(request)) ? new CommonResponse().success() : new CommonResponse().fail();
    }

    @RequestMapping(value = {"/account/member/listAccount"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "ListAccountRequest", paramType = "body")})
    @ApiOperation(value = "获取账户管理员列表", notes = "获取账户管理员列表")
    public DataResponse<PageList<AccountListResponse>> listAccount(@RequestBody ListAccountRequest request) {
        ListAccountRequest request2 = (ListAccountRequest) super.getBaseRequest(request);
        DataResponse<PageList<AccountListResponse>> response = new DataResponse().success();
        response.setData(this.accountService.listAccount(request2));
        return response;
    }

    @RequestMapping(value = {"/account/member/saveAccount"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "账户相关", description = "保存管理员账户")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "SaveAccountRequest", paramType = "body")})
    @ApiOperation(value = "保存管理员账户", notes = "保存管理员账户")
    public CommonResponse saveAccount(@RequestBody SaveAccountRequest request) {
        SaveAccountRequest request2 = (SaveAccountRequest) super.getBaseRequest(request);
        DataResponse<Long> response = new DataResponse().success();
        if (request2.getAccountId() != null && request2.getAccountId().longValue() > 0) {
            response.setData(this.accountService.updateAccount(request2));
        } else {
            response.setData(this.accountService.insertAccount(request2));
        }
        return response;
    }

    @RequestMapping(value = {"/account/member/cloneAccount"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "账户相关", description = "克隆管理员账户")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "CloneAccountRequest", paramType = "body")})
    @ApiOperation(value = "克隆管理员账户", notes = "克隆管理员账户")
    public CommonResponse cloneAccount(@RequestBody CloneAccountRequest request) {
        CloneAccountRequest request2 = (CloneAccountRequest) super.getBaseRequest(request);
        DataResponse<Long> response = new DataResponse().success();
        response.setData(this.accountService.cloneAccount(request2));
        return response;
    }

    @RequestMapping(value = {"/account/member/deleteAccount"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "账户相关", description = "删除管理员账户")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "DeleteAccountRequest", paramType = "body")})
    @ApiOperation(value = "删除管理员账户", notes = "删除管理员账户")
    public CommonResponse deleteAccount(@RequestBody DeleteAccountRequest request) {
        this.accountService.deleteAccount((DeleteAccountRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/account/account/modifyPassword"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "账户相关", description = "修改密码")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "ModifyPasswordRequest", paramType = "body")})
    @ApiOperation(value = "修改密码", notes = "修改密码")
    public CommonResponse modifyPassword(@RequestBody ModifyPasswordRequest request) {
        this.accountService.modifyPassword((ModifyPasswordRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/account/account/resetPassword"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "账户相关", description = "重置密码")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "ResetPasswordRequest", paramType = "body")})
    @ApiOperation(value = "重置密码", notes = "重置密码")
    public DataResponse<Long> resetPassword(@RequestBody ResetPasswordRequest request, HttpServletRequest servletRequest) {
        request.setSessionId(servletRequest.getSession().getId());
        DataResponse<Long> response = new DataResponse().success();
        response.setData(this.accountService.resetPassword(request));
        return response;
    }

    @RequestMapping(value = {"/account/account/updateLanguage"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "账户相关", description = "修改语言类型")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "UpdateLanguageRequest", paramType = "body")})
    @ApiOperation(value = "修改语言类型", notes = "修改语言类型")
    public CommonResponse updateLanguage(@RequestBody UpdateLanguageRequest request) {
        this.accountService.updateLanguage((UpdateLanguageRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/account/config/saveRecordPeriod"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "系统设置相关", description = "保存记录存储周期配置")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "SaveRecordPeriodRequest", paramType = "body")})
    @ApiOperation(value = "保存记录存储周期配置", notes = "保存记录存储周期配置")
    public CommonResponse saveRecordPeriod(@RequestBody SaveRecordPeriodRequest request) {
        this.configService.saveRecordPeriod((SaveRecordPeriodRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/account/config/queryRecordPeriod"}, method = {RequestMethod.POST})
    @ApiOperation(value = "查询记录存储周期配置", notes = "查询记录存储周期配置")
    public DataResponse<QueryConfigResponse> queryRecordPeriod() {
        DataResponse<QueryConfigResponse> response = new DataResponse().success();
        response.setData(this.configService.queryRecordPeriod());
        return response;
    }

    @RequestMapping(value = {"/account/memory/queryMemoryInfo"}, method = {RequestMethod.POST})
    @ApiOperation(value = "查询内存信息", notes = "查询内存信息")
    public DataResponse<QueryMemoryInfoResponse> queryMemoryInfo() {
        DataResponse<QueryMemoryInfoResponse> response = new DataResponse().success();
        QueryMemoryInfoResponse data = new QueryMemoryInfoResponse();
        FileSystemUsage memory = RuntimeSystemUtils.memory();
        data.setFree(Long.valueOf(memory.getFree()));
        data.setTotal(Long.valueOf(memory.getTotal()));
        response.setData(data);
        return response;
    }

    @RequestMapping(value = {"/account/member/generatorOpenApiAppKey"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "生成appKey", description = "生成appKey")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "OpenApiAppKeyRequest", paramType = "body")})
    @ApiOperation(value = "生成appkey", notes = "生成appkey")
    public DataResponse<OpenApiAppKeyResponse> generatorOpenApiAppKey(@RequestBody OpenApiAppKeyRequest request) {
        DataResponse<OpenApiAppKeyResponse> response = new DataResponse().success();
        response.setData(this.accountService.generatorOpenApiAppKey(request));
        return response;
    }

    @RequestMapping(value = {"/account/member/generatorOpenApiAppKey/v2"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "生成appKey", description = "生成appKey,v2")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "OpenApiAppKeyRequest", paramType = "body")})
    @ApiOperation(value = "生成appkey,v2", notes = "生成appkey,v2")
    public DataResponse<OpenApiAppKeyResponse> generatorOpenApiAppKeyV2(@RequestBody OpenApiAppKeyRequest request) {
        DataResponse<OpenApiAppKeyResponse> response = new DataResponse().success();
        response.setData(this.accountService.generatorOpenApiAppKeyV2(request));
        return response;
    }

    @RequestMapping(value = {"/account/member/getOpenApiAppKey"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "OpenApiAppKeyRequest", paramType = "body")})
    @ApiOperation(value = "获取appkey", notes = "获取appkey")
    public DataResponse<OpenApiAppKeyResponse> getOpenApiAppKey(@RequestBody OpenApiAppKeyRequest request) {
        DataResponse<OpenApiAppKeyResponse> response = new DataResponse().success();
        response.setData(this.accountService.getOpenApiAppKey(request));
        return response;
    }

    @RequestMapping(value = {"/account/member/getOpenApiToken"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "OpenApiAppTokenRequest", paramType = "body")})
    @ApiOperation(value = "获取appToken", notes = "获取appToken")
    public DataResponse<OpenApiAppTokenResponse> getOpenApiToken(@RequestBody OpenApiAppTokenRequest request) {
        DataResponse<OpenApiAppTokenResponse> response = new DataResponse().success();
        response.setData(this.accountService.getOpenApiToken(request));
        return response;
    }

    @RequestMapping(value = {"/account/member/getOpenApiToken/v2"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "OpenApiAppTokenRequest", paramType = "body")})
    @ApiOperation(value = "获取appToken", notes = "获取appToken")
    public DataResponse<OpenApiAppTokenResponse> getOpenApiTokenV2(@RequestBody OpenApiAppTokenRequest request) {
        DataResponse<OpenApiAppTokenResponse> response = new DataResponse().success();
        response.setData(this.accountService.getOpenApiTokenV2(request));
        return response;
    }

    @RequestMapping(value = {"/account/authCode/saveAuthCode"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "账户相关", description = "保存授权码")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "SaveAuthCodeRequest", paramType = "body")})
    @ApiOperation(value = "保存授权码", notes = "保存授权码")
    public DataResponse<SaveAuthCodeResponse> saveAuthCode(@RequestBody SaveAuthCodeRequest request) {
        SaveAuthCodeRequest request2 = (SaveAuthCodeRequest) super.getBaseRequest(request);
        DataResponse<SaveAuthCodeResponse> response = new DataResponse().success();
        response.setData(this.authCodeService.save(request2));
        return response;
    }

    @RequestMapping(value = {"/account/authCode/listAuthCode"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "ListAuthCodeRequest", paramType = "body")})
    @ApiOperation(value = "授权码列表", notes = "授权码列表")
    public DataResponse<PageList<ListAuthCodeResponse>> listAuthCode(@RequestBody ListAuthCodeRequest request) {
        ListAuthCodeRequest request2 = (ListAuthCodeRequest) super.getBaseRequest(request);
        DataResponse<PageList<ListAuthCodeResponse>> response = new DataResponse().success();
        response.setData(this.authCodeService.listAuthCode(request2));
        return response;
    }

    @RequestMapping(value = {"/account/auth/getOrgCode"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "GetOrgCodeRequest", paramType = "body")})
    @ApiOperation(value = "获取机构码", notes = "获取机构码")
    public DataResponse<String> getOrgCode(@RequestBody GetOrgCodeRequest request) {
        DataResponse<String> response = new DataResponse().success();
        response.setData(CacheAdapter.getServerOrgCode(Constants.SERVER_ORG_CODE_KEY));
        return response;
    }

    @RequestMapping(value = {"/account/config/saveStrangerSpeechInfo"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "系统设置相关", description = "保存陌生人刷脸语音提示")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "SaveStrangerSpeechInfoRequest", paramType = "body")})
    @ApiOperation(value = "保存陌生人刷脸语音提示", notes = "保存陌生人刷脸语音提示")
    public CommonResponse saveStrangerSpeechInfo(@RequestBody SaveStrangerSpeechInfoRequest request) {
        this.configService.saveStrangerSpeechReminderInfo((SaveStrangerSpeechInfoRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/account/config/saveStrangerShowInfo"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "系统设置相关", description = "保存陌生人刷脸界面文字提示")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "SaveStrangerShowInfoRequest", paramType = "body")})
    @ApiOperation(value = "保存陌生人刷脸界面文字提示", notes = "保存陌生人刷脸界面文字提示")
    public CommonResponse saveStrangerShowInfo(@RequestBody SaveStrangerShowInfoRequest request) {
        this.configService.saveStrangerShowReminderInfo((SaveStrangerShowInfoRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/account/config/saveStrangerInfo"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "系统设置相关", description = "保存陌生人刷脸提示")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "SaveStrangerInfoRequest", paramType = "body")})
    @ApiOperation(value = "保存陌生人刷脸提示", notes = "保存陌生人刷脸提示")
    public CommonResponse saveStrangerInfo(@RequestBody SaveStrangerInfoRequest request) {
        this.configService.saveStrangerReminderInfo((SaveStrangerInfoRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/account/config/queryStrangerShowInfo"}, method = {RequestMethod.POST})
    @ApiOperation(value = "查询陌生人刷脸界面文字提示", notes = "查询陌生人刷脸界面文字提示")
    public DataResponse<String> queryStrangerShowInfo() {
        DataResponse<String> response = new DataResponse().success();
        response.setData(this.configService.getOneByKey(ConfigConstants.STRANGER_SHOW_REMINDER_INFO_KEY).getConfigValue());
        return response;
    }

    @RequestMapping(value = {"/account/config/queryStrangerSpeechInfo"}, method = {RequestMethod.POST})
    @ApiOperation(value = "查询陌生人刷脸语音提示", notes = "查询陌生人刷脸语音提示")
    public DataResponse<String> queryStrangerSpeechInfo() {
        DataResponse<String> response = new DataResponse().success();
        response.setData(this.configService.getOneByKey(ConfigConstants.STRANGER_SPEECH_REMINDER_INFO_KEY).getConfigValue());
        return response;
    }

    @RequestMapping(value = {"/auth/module/listModule"}, method = {RequestMethod.POST})
    @ApiOperation(value = "获取模块列表", notes = "获取模块列表")
    public DataResponse<List<AuthModuleResponse>> listModule() {
        DataResponse<List<AuthModuleResponse>> response = new DataResponse().success();
        response.setData(this.authModuleService.listModule(super.getBaseRequest(new BaseRequest())));
        return response;
    }

    @RequestMapping(value = {"/auth/module/saveModule"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "SaveModuleRequest", paramType = "body")})
    @ApiOperation(value = "保存模块", notes = "保存模块")
    public DataResponse<Long> saveModule(@RequestBody SaveModuleRequest request) {
        SaveModuleRequest request2 = (SaveModuleRequest) super.getBaseRequest(request);
        DataResponse<Long> response = new DataResponse().success();
        response.setData(this.authModuleService.saveModule(request2));
        return response;
    }

    @RequestMapping(value = {"/auth/module/listAccountManageModule"}, method = {RequestMethod.POST})
    @ApiOperation(value = "获取当前账户权限模块列表", notes = "获取当前账户权限模块列表")
    public DataResponse<List<AuthModuleResponse>> listAccountManageModule() {
        DataResponse<List<AuthModuleResponse>> response = new DataResponse().success();
        response.setData(this.accountService.listAccountManageModule(super.getBaseRequest(new BaseRequest())));
        return response;
    }

    @RequestMapping(value = {"/auth/member/initMemberAuthInfo"}, method = {RequestMethod.POST})
    @ApiOperation(value = "初始化成员权限", notes = "初始化成员权限")
    public DataResponse<List<AuthModuleResponse>> initMemberAuthInfo() {
        DataResponse<List<AuthModuleResponse>> response = new DataResponse().success();
        response.setData(this.accountService.listAccountManageModule(super.getBaseRequest(new BaseRequest())));
        return response;
    }

    @RequestMapping(value = {"/account/config/savePoseThreshold"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "SavePoseThresholdRequest", paramType = "body")})
    @ApiOperation(value = "保存检测底库角度配置", notes = "保存检测底库角度配置")
    public CommonResponse savePoseThreshold(@RequestBody SavePoseThresholdRequest request) {
        this.configService.savePoseThreshold(request);
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/account/viewConfig/pageList"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "ViewConfigListRequest", paramType = "body")})
    @ApiOperation(value = "页面配置列表", notes = "页面配置列表")
    public DataResponse<PageList<ViewConfigResponse>> viewConfigPageList(@RequestBody ViewConfigListRequest request) {
        ViewConfigListRequest request2 = (ViewConfigListRequest) super.getBaseRequest(request);
        DataResponse<PageList<ViewConfigResponse>> response = new DataResponse().success();
        PageList<ViewConfigResponse> pageList = this.viewConfigService.viewConfigPageList(request2);
        response.setData(pageList);
        return response;
    }

    @RequestMapping(value = {"/account/viewConfig/add"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "ViewConfigRequest", paramType = "body")})
    @ApiOperation(value = "新增页面配置", notes = "新增页面配置")
    public DataResponse<Long> viewConfigAdd(@RequestBody ViewConfigRequest request) {
        ViewConfigRequest request2 = (ViewConfigRequest) super.getBaseRequest(request);
        DataResponse<Long> response = new DataResponse().success();
        Long result = this.viewConfigService.viewConfigAdd(request2);
        response.setData(result);
        return response;
    }

    @RequestMapping(value = {"/account/viewConfig/delete"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "ViewConfigRequest", paramType = "body")})
    @ApiOperation(value = "删除页面配置", notes = "删除页面配置")
    public DataResponse<Long> viewConfigDelete(@RequestBody ViewConfigRequest request) {
        ViewConfigRequest request2 = (ViewConfigRequest) super.getBaseRequest(request);
        DataResponse<Long> response = new DataResponse().success();
        Long result = this.viewConfigService.viewConfigDelete(request2);
        response.setData(result);
        return response;
    }

    @RequestMapping(value = {"/account/viewConfig/edit"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "ViewConfigRequest", paramType = "body")})
    @ApiOperation(value = "修改页面配置", notes = "修改页面配置")
    public DataResponse<Long> viewConfigEdit(@RequestBody ViewConfigRequest request) {
        ViewConfigRequest request2 = (ViewConfigRequest) super.getBaseRequest(request);
        DataResponse<Long> response = new DataResponse().success();
        Long result = this.viewConfigService.viewConfigEdit(request2);
        response.setData(result);
        return response;
    }

    @RequestMapping(value = {"/account/viewConfig/getOne"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "ViewConfigRequest", paramType = "body")})
    @ApiOperation(value = "查询单个页面配置详情", notes = "查询单个页面配置详情")
    public DataResponse<ViewConfigResponse> viewConfigGetOne(@RequestBody ViewConfigRequest request) {
        ViewConfigRequest request2 = (ViewConfigRequest) super.getBaseRequest(request);
        DataResponse<ViewConfigResponse> response = new DataResponse().success();
        ViewConfigResponse result = this.viewConfigService.viewConfigGetOne(request2);
        response.setData(result);
        return response;
    }

    @RequestMapping(value = {"/account/viewConfig/getList"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "ViewConfigListRequest", paramType = "body")})
    @ApiOperation(value = "查询列表-页面配置", notes = "查询列表-页面配置")
    public DataResponse<List<ViewConfigResponse>> viewConfigGetList(@RequestBody ViewConfigListRequest request) {
        ViewConfigListRequest request2 = (ViewConfigListRequest) super.getBaseRequest(request);
        DataResponse<List<ViewConfigResponse>> response = new DataResponse().success();
        List<ViewConfigResponse> result = this.viewConfigService.viewConfigGetList(request2);
        response.setData(result);
        return response;
    }

    @RequestMapping(value = {"/account/viewConfig/uploadImg"}, method = {RequestMethod.POST})
    @ApiOperation(value = "上传图片", notes = "上传图片")
    public DataResponse<String> viewConfigUploadImg(HttpServletRequest request) {
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
        DataResponse<String> response = new DataResponse().success();
        response.setData(this.viewConfigService.viewConfigUploadImg(file));
        return response;
    }

    @RequestMapping(value = {"/account/login/viewConfig"}, method = {RequestMethod.POST})
    @ApiOperation(value = "查询登录页配置详情", notes = "查询登录页配置详情")
    public DataResponse<ViewConfigResponse> loginViewConfig() {
        DataResponse<ViewConfigResponse> response = new DataResponse().success();
        ViewConfigResponse result = this.viewConfigService.viewConfigGetLogin();
        response.setData(result);
        return response;
    }

    @RequestMapping(value = {"/account/config/saveSnapMode"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "系统设置相关", description = "保存抓拍照模式配置")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "SaveSnapModeRequest", paramType = "body")})
    @ApiOperation(value = "保存抓拍照模式配置", notes = "保存抓拍照模式配置")
    public CommonResponse saveSnapMode(@RequestBody SaveSnapModeRequest request) {
        this.configService.saveSnapMode((SaveSnapModeRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/account/config/getSnapMode"}, method = {RequestMethod.POST})
    @ApiOperation(value = "获取抓拍照模式配置", notes = "获取抓拍照模式配置")
    public DataResponse<Integer> getSnapMode() {
        DataResponse<Integer> response = new DataResponse().success();
        response.setData(this.configService.getSnapMode());
        return response;
    }

    @RequestMapping(value = {"/account/password/resetEncode"}, method = {RequestMethod.POST})
    @ApiOperation(value = "重置密码加密方式", notes = "重置密码加密方式")
    public CommonResponse resetPasswordEncode() {
        this.accountService.resetPasswordEncode();
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/account/config/getSystemBasic"}, method = {RequestMethod.POST})
    @ApiOperation(value = "系统设置-基础配置查询", notes = "系统设置-基础配置查询")
    public DataResponse<SystemBasicConfigResponse> getSystemBasicConfig() {
        DataResponse<SystemBasicConfigResponse> response = new DataResponse().success();
        response.setData(this.configService.getSystemBasicConfig());
        return response;
    }

    @RequestMapping(value = {"/account/config/saveSystemBasic"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "系统设置相关", description = "基础配置保存")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "SystemBasicConfigRequest", paramType = "body")})
    @ApiOperation(value = "系统设置-基础配置保存", notes = "系统设置-基础配置报错")
    public CommonResponse saveSystemBasicConfig(@RequestBody SystemBasicConfigRequest request) {
        this.configService.saveSystemBasicConfig((SystemBasicConfigRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/account/operLog/pageList"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "OperLogListRequest", paramType = "body")})
    @ApiOperation(value = "操作日志", notes = "查询操作日志列表")
    public DataResponse<PageList<OperLogResponse>> operLogPageList(@RequestBody OperLogListRequest request) {
        OperLogListRequest request2 = (OperLogListRequest) super.getBaseRequest(request);
        DataResponse<PageList<OperLogResponse>> response = new DataResponse().success();
        response.setData(this.accountService.operLogPageList(request2));
        return response;
    }
}
