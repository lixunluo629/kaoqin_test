package com.moredian.onpremise.frontend.controller.server;

import com.moredian.onpremise.api.server.CallbackServerService;
import com.moredian.onpremise.api.server.ConfigService;
import com.moredian.onpremise.api.server.DataBackupsService;
import com.moredian.onpremise.api.server.UpgradeServerService;
import com.moredian.onpremise.core.adapter.CacheAdapter;
import com.moredian.onpremise.core.aop.operLog.annotation.LogAnnotation;
import com.moredian.onpremise.core.common.constants.Constants;
import com.moredian.onpremise.core.model.info.CurrentVersionInfo;
import com.moredian.onpremise.core.model.request.BackupsDataRecordRequest;
import com.moredian.onpremise.core.model.request.DeleteBackupsRequest;
import com.moredian.onpremise.core.model.request.DeleteCallbackServerRequest;
import com.moredian.onpremise.core.model.request.DeleteServerScheduleRequest;
import com.moredian.onpremise.core.model.request.ExecuteUpgradeServerScheduleRequest;
import com.moredian.onpremise.core.model.request.ListCallbackServersRequest;
import com.moredian.onpremise.core.model.request.ListServerBackupsRecordRequest;
import com.moredian.onpremise.core.model.request.ListServerScheduleRequest;
import com.moredian.onpremise.core.model.request.QueryCllbackServerRequest;
import com.moredian.onpremise.core.model.request.RestoreBackupsRequest;
import com.moredian.onpremise.core.model.request.SaveAdvertisingRequest;
import com.moredian.onpremise.core.model.request.SaveCallbackServerRequest;
import com.moredian.onpremise.core.model.request.SaveTimeZoneRequest;
import com.moredian.onpremise.core.model.request.UploadBackupsRequest;
import com.moredian.onpremise.core.model.request.UploadUpgradePackageRequest;
import com.moredian.onpremise.core.model.request.VerifyCallbackRequest;
import com.moredian.onpremise.core.model.response.CallbackServerResponse;
import com.moredian.onpremise.core.model.response.CommonResponse;
import com.moredian.onpremise.core.model.response.DataResponse;
import com.moredian.onpremise.core.model.response.ListCallbackTagResponse;
import com.moredian.onpremise.core.model.response.ListServerBackupsRecordResponse;
import com.moredian.onpremise.core.model.response.ListServerScheduleResponse;
import com.moredian.onpremise.core.utils.JsonUtils;
import com.moredian.onpremise.core.utils.PageList;
import com.moredian.onpremise.core.utils.RedisUtils;
import com.moredian.onpremise.frontend.controller.base.BaseController;
import com.moredian.onpremise.iot.handle.NettyMessageApi;
import com.moredian.onpremise.model.IOTModelType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Api(value = "on-premise-server", description = "on-premise服务端相关")
@RestController
/* loaded from: onpremise-frontend-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/frontend/controller/server/ServerController.class */
public class ServerController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) ServerController.class);

    @Autowired
    private CallbackServerService callbackServerService;

    @Autowired
    private UpgradeServerService upgradeServerService;

    @Autowired
    private DataBackupsService dataBackupsService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private NettyMessageApi nettyMessageApi;

    @RequestMapping(value = {"/server/backups/backups"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "服务相关", description = "数据备份接口")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = false, dataType = "BackupsDataRecordRequest", paramType = "body")})
    @ApiOperation(value = "数据备份接口", notes = "数据备份接口")
    public CommonResponse backups(@RequestBody BackupsDataRecordRequest request) {
        return this.dataBackupsService.executeBackups((BackupsDataRecordRequest) super.getBaseRequest(request)).booleanValue() ? new CommonResponse().success() : new CommonResponse().fail();
    }

    @RequestMapping(value = {"/server/backups/deleteBackups"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "服务相关", description = "删除数据备份接口")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = false, dataType = "DeleteBackupsRequest", paramType = "body")})
    @ApiOperation(value = "删除数据备份接口", notes = "删除数据备份接口")
    public CommonResponse deleteBackups(@RequestBody DeleteBackupsRequest request) {
        return this.dataBackupsService.deleteBackups((DeleteBackupsRequest) super.getBaseRequest(request)).booleanValue() ? new CommonResponse().success() : new CommonResponse().fail();
    }

    @RequestMapping(value = {"/server/backups/restoreBackups"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "服务相关", description = "恢复备份数据接口")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = false, dataType = "RestoreBackupsRequest", paramType = "body")})
    @ApiOperation(value = "恢复备份数据接口", notes = "恢复备份数据接口")
    public CommonResponse restoreBackups(@RequestBody RestoreBackupsRequest request) {
        return this.dataBackupsService.restoreBackups((RestoreBackupsRequest) super.getBaseRequest(request)).booleanValue() ? new CommonResponse().success() : new CommonResponse().fail();
    }

    @RequestMapping(value = {"/server/backups/uploadBackups"}, method = {RequestMethod.POST})
    @ApiOperation(value = "数据导入接口", notes = "数据导入接口")
    public CommonResponse uploadBackups(HttpServletRequest request) {
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
        UploadBackupsRequest backupsRequest = (UploadBackupsRequest) super.getBaseRequest(new UploadBackupsRequest(file));
        return this.dataBackupsService.uploadBackups(backupsRequest).booleanValue() ? new CommonResponse().success() : new CommonResponse().fail();
    }

    @RequestMapping(value = {"/server/backups/listRecord"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = false, dataType = "ListServerBackupsRecordRequest", paramType = "body")})
    @ApiOperation(value = "备份数据列表接口", notes = "备份数据列表接口")
    public DataResponse<PageList<ListServerBackupsRecordResponse>> listRecord(@RequestBody ListServerBackupsRecordRequest request) {
        ListServerBackupsRecordRequest request2 = (ListServerBackupsRecordRequest) super.getBaseRequest(request);
        DataResponse<PageList<ListServerBackupsRecordResponse>> response = new DataResponse().success();
        response.setData(this.dataBackupsService.listRecord(request2));
        return response;
    }

    @RequestMapping(value = {"/server/upgrade/delete"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "服务相关", description = "删除升级任务")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = false, dataType = "DeleteServerScheduleRequest", paramType = "body")})
    @ApiOperation(value = "删除升级任务", notes = "删除升级任务")
    public CommonResponse delete(@RequestBody DeleteServerScheduleRequest request) {
        return this.upgradeServerService.delete((DeleteServerScheduleRequest) super.getBaseRequest(request)) ? new CommonResponse().success() : new CommonResponse().fail();
    }

    @RequestMapping(value = {"/server/upgrade/executeSchedule"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "服务相关", description = "执行升级任务")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = false, dataType = "ExecuteUpgradeServerScheduleRequest", paramType = "body")})
    @ApiOperation(value = "执行升级任务", notes = "执行升级任务")
    public CommonResponse executeSchedule(@RequestBody ExecuteUpgradeServerScheduleRequest request) {
        return this.upgradeServerService.executeSchedule((ExecuteUpgradeServerScheduleRequest) super.getBaseRequest(request)) ? new CommonResponse().success() : new CommonResponse().fail();
    }

    @RequestMapping(value = {"/server/upgrade/uploadPackage"}, method = {RequestMethod.POST})
    @ApiOperation(value = "上传升级包", notes = "上传升级包")
    public CommonResponse uploadPackage(HttpServletRequest request) {
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
        UploadUpgradePackageRequest upgradePackageRequest = (UploadUpgradePackageRequest) super.getBaseRequest(new UploadUpgradePackageRequest(file));
        return this.upgradeServerService.uploadPackage(upgradePackageRequest) ? new CommonResponse().success() : new CommonResponse().fail();
    }

    @RequestMapping(value = {"/server/upgrade/listServerSchedule"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = false, dataType = "ListServerScheduleRequest", paramType = "body")})
    @ApiOperation(value = "升级任务列表", notes = "升级任务列表")
    public DataResponse<PageList<ListServerScheduleResponse>> listServerSchedule(@RequestBody ListServerScheduleRequest request) {
        ListServerScheduleRequest request2 = (ListServerScheduleRequest) super.getBaseRequest(request);
        DataResponse<PageList<ListServerScheduleResponse>> response = new DataResponse().success();
        response.setData(this.upgradeServerService.listServerSchedule(request2));
        return response;
    }

    @RequestMapping(value = {"/server/server/queryServerStatus"}, method = {RequestMethod.POST})
    @ApiOperation(value = "查询服务是否运行", notes = "查询服务是否运行")
    public CommonResponse queryServerStatus() {
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/server/server/getCurrentVersion"}, method = {RequestMethod.POST})
    @ApiOperation(value = "查询服务当前版本号", notes = "查询服务当前版本号")
    public DataResponse<CurrentVersionInfo> getCurrentVersion() {
        DataResponse<CurrentVersionInfo> response = new DataResponse().success();
        response.setData(CacheAdapter.getCurrentVersionInfo(Constants.SERVER_CURRENT_VERSION_KEY));
        return response;
    }

    @RequestMapping(value = {"/server/callback/listCallbackServers"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = false, dataType = "ListCallbackServersRequest", paramType = "body")})
    @ApiOperation(value = "回调服务列表", notes = "回调服务列表")
    public DataResponse<PageList<CallbackServerResponse>> listCallbackServers(@RequestBody ListCallbackServersRequest request) {
        ListCallbackServersRequest request2 = (ListCallbackServersRequest) super.getBaseRequest(request);
        DataResponse<PageList<CallbackServerResponse>> response = new DataResponse().success();
        response.setData(this.callbackServerService.listCallbackServers(request2));
        return response;
    }

    @RequestMapping(value = {"/server/callback/listTag"}, method = {RequestMethod.POST})
    @ApiOperation(value = "回调服务tag类型列表", notes = "回调服务tag类型列表")
    public DataResponse<List<ListCallbackTagResponse>> listTag() {
        DataResponse<List<ListCallbackTagResponse>> response = new DataResponse().success();
        response.setData(this.callbackServerService.listTag());
        return response;
    }

    @RequestMapping(value = {"/server/callback/getOneByTag"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = false, dataType = "QueryCllbackServerRequest", paramType = "body")})
    @ApiOperation(value = "根据tag获取单个回调服务", notes = "根据tag获取单个回调服务")
    public DataResponse<CallbackServerResponse> getOneByTag(@RequestBody QueryCllbackServerRequest request) {
        QueryCllbackServerRequest request2 = (QueryCllbackServerRequest) super.getBaseRequest(request);
        DataResponse<CallbackServerResponse> response = new DataResponse().success();
        response.setData(this.callbackServerService.getOneByTag(request2));
        return response;
    }

    @RequestMapping(value = {"/server/callback/save"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "服务相关", description = "新增回调服务")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = false, dataType = "SaveCallbackServerRequest", paramType = "body")})
    @ApiOperation(value = "新增回调服务", notes = "新增回调服务")
    public CommonResponse save(@RequestBody SaveCallbackServerRequest request) {
        return this.callbackServerService.save((SaveCallbackServerRequest) super.getBaseRequest(request)) ? new CommonResponse().success() : new CommonResponse().fail();
    }

    @RequestMapping(value = {"/server/callback/delete"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "服务相关", description = "删除回调服务")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = false, dataType = "DeleteCallbackServerRequest", paramType = "body")})
    @ApiOperation(value = "删除回调服务", notes = "删除回调服务")
    public CommonResponse delete(@RequestBody DeleteCallbackServerRequest request) {
        return this.callbackServerService.delete((DeleteCallbackServerRequest) super.getBaseRequest(request)) ? new CommonResponse().success() : new CommonResponse().fail();
    }

    @RequestMapping(value = {"/server/callback/testCallback"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = false, dataType = "VerifyCallbackRequest", paramType = "body")})
    @ApiOperation(value = "测试回调服务", notes = "测试回调服务")
    public DataResponse<Boolean> testCallback(@RequestBody VerifyCallbackRequest request) {
        logger.info("==============receive callback ：{}", JsonUtils.toJson(request));
        DataResponse<Boolean> response = new DataResponse().success();
        response.setData(true);
        return response;
    }

    @RequestMapping(value = {"/server/server/saveTimeZone"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "SaveTimeZoneRequest", paramType = "body")})
    @ApiOperation(value = "同步当前网络时区", notes = "同步当前网络时区")
    public CommonResponse saveTimeZone(@RequestBody SaveTimeZoneRequest request) {
        return this.configService.saveTimeZone((SaveTimeZoneRequest) super.getBaseRequest(request)) ? new CommonResponse().success() : new CommonResponse().fail();
    }

    @RequestMapping(value = {"/netty/netty/sendMsg"}, method = {RequestMethod.GET})
    public CommonResponse sendMsg(@RequestParam("id") String id, @RequestParam("type") Integer type, @RequestParam("deviceSn") String deviceSn) {
        Object query = RedisUtils.query(id, IOTModelType.from(type.intValue()).clazz());
        this.nettyMessageApi.consumeMsg(query, type, deviceSn);
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/account/config/saveAdvertising"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "服务相关", description = "保存推送广告信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "SaveAdvertisingRequest", paramType = "body")})
    @ApiOperation(value = "保存推送广告信息", notes = "保存推送广告信息")
    public CommonResponse saveAdvertising(@RequestBody SaveAdvertisingRequest request) {
        this.configService.saveAdvertising((SaveAdvertisingRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }
}
