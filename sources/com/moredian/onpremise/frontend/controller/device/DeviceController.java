package com.moredian.onpremise.frontend.controller.device;

import com.moredian.onpremise.api.device.DeviceService;
import com.moredian.onpremise.core.aop.operLog.annotation.LogAnnotation;
import com.moredian.onpremise.core.model.request.BatchSaveDeviceConfigRequest;
import com.moredian.onpremise.core.model.request.BindDeviceGroupRequest;
import com.moredian.onpremise.core.model.request.CancelUpgradeScheduleRequest;
import com.moredian.onpremise.core.model.request.DeleteDeviceRequest;
import com.moredian.onpremise.core.model.request.DeviceDetailRequest;
import com.moredian.onpremise.core.model.request.DeviceListRequest;
import com.moredian.onpremise.core.model.request.DevicePushMsgQueryRequest;
import com.moredian.onpremise.core.model.request.DevicePushMsgRequest;
import com.moredian.onpremise.core.model.request.DeviceResetDataRequest;
import com.moredian.onpremise.core.model.request.DeviceRestartRequest;
import com.moredian.onpremise.core.model.request.DeviceShowDataRequest;
import com.moredian.onpremise.core.model.request.DownloadDeviceLogRequest;
import com.moredian.onpremise.core.model.request.GetGroupDeviceRequest;
import com.moredian.onpremise.core.model.request.ListUpgradeDeviceRequest;
import com.moredian.onpremise.core.model.request.ListUpgradeRecordRequest;
import com.moredian.onpremise.core.model.request.ListUpgradeScheduleRequest;
import com.moredian.onpremise.core.model.request.OpenDoorRequest;
import com.moredian.onpremise.core.model.request.PreActiveDeviceListRequest;
import com.moredian.onpremise.core.model.request.PreActiveDeviceRequest;
import com.moredian.onpremise.core.model.request.PullDeviceLogRequest;
import com.moredian.onpremise.core.model.request.RetryUpgradeScheduleRequest;
import com.moredian.onpremise.core.model.request.SaveDeviceConfigRequest;
import com.moredian.onpremise.core.model.request.SaveUpgradeScheduleRequest;
import com.moredian.onpremise.core.model.request.UploadUpgradePackageRequest;
import com.moredian.onpremise.core.model.response.CommonResponse;
import com.moredian.onpremise.core.model.response.DataResponse;
import com.moredian.onpremise.core.model.response.DevicePushMsgResponse;
import com.moredian.onpremise.core.model.response.DeviceResponse;
import com.moredian.onpremise.core.model.response.DownloadDeviceLogResponse;
import com.moredian.onpremise.core.model.response.GroupDeviceResponse;
import com.moredian.onpremise.core.model.response.ListSearchDeviceResponse;
import com.moredian.onpremise.core.model.response.PreActiveDeviceResponse;
import com.moredian.onpremise.core.model.response.UpgradeRecordResponse;
import com.moredian.onpremise.core.model.response.UpgradeScheduleResponse;
import com.moredian.onpremise.core.model.response.UploadUpgradePackageResponse;
import com.moredian.onpremise.core.utils.MyDateUtils;
import com.moredian.onpremise.core.utils.PageList;
import com.moredian.onpremise.frontend.controller.base.BaseController;
import com.moredian.onpremise.task.impl.PushUpgradeSchedule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Api(value = "on-premise-device", description = "on-premise设备相关")
@RestController
/* loaded from: onpremise-frontend-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/frontend/controller/device/DeviceController.class */
public class DeviceController extends BaseController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private PushUpgradeSchedule pushUpgradeSchedule;

    @RequestMapping(value = {"/device/device/listDevice"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = false, dataType = "DeviceListRequest", paramType = "body")})
    @ApiOperation(value = "设备列表", notes = "设备列表")
    public DataResponse<PageList<DeviceResponse>> listDevice(@RequestBody DeviceListRequest request) {
        DeviceListRequest request2 = (DeviceListRequest) super.getBaseRequest(request);
        DataResponse<PageList<DeviceResponse>> response = new DataResponse().success();
        response.setData(this.deviceService.listDevice(request2));
        return response;
    }

    @RequestMapping(value = {"/device/device/listSearchDevice"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "DeviceListRequest", paramType = "body")})
    @ApiOperation(value = "设备条件筛选列表", notes = "设备条件筛选列表")
    public DataResponse<List<ListSearchDeviceResponse>> listSearchDevice(@RequestBody DeviceListRequest request) {
        DeviceListRequest request2 = (DeviceListRequest) super.getBaseRequest(request);
        DataResponse<List<ListSearchDeviceResponse>> response = new DataResponse().success();
        response.setData(this.deviceService.listSearchDevice(request2));
        return response;
    }

    @RequestMapping(value = {"/device/device/listOnlineDevice"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "DeviceListRequest", paramType = "body")})
    @ApiOperation(value = "在线设备列表", notes = "在线设备列表")
    public DataResponse<List<ListSearchDeviceResponse>> listOnlineDevice(@RequestBody DeviceListRequest request) {
        DeviceListRequest request2 = (DeviceListRequest) super.getBaseRequest(request);
        DataResponse<List<ListSearchDeviceResponse>> response = new DataResponse().success();
        response.setData(this.deviceService.listOnlineDevice(request2));
        return response;
    }

    @RequestMapping(value = {"/device/device/listUpgradeDevice"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "ListUpgradeDeviceRequest", paramType = "body")})
    @ApiOperation(value = "可升级列表查询", notes = "可升级列表查询")
    public DataResponse<List<ListSearchDeviceResponse>> listUpgradeDevice(@RequestBody ListUpgradeDeviceRequest request) {
        ListUpgradeDeviceRequest request2 = (ListUpgradeDeviceRequest) super.getBaseRequest(request);
        DataResponse<List<ListSearchDeviceResponse>> response = new DataResponse().success();
        response.setData(this.deviceService.listUpgradeDevice(request2));
        return response;
    }

    @RequestMapping(value = {"/device/group/groupDeviceList"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = false, dataType = "DeviceListRequest", paramType = "body")})
    @ApiOperation(value = "设备权限列表", notes = "设备权限列表")
    public DataResponse<PageList<GroupDeviceResponse>> groupDeviceList(@RequestBody DeviceListRequest request) {
        DeviceListRequest request2 = (DeviceListRequest) super.getBaseRequest(request);
        DataResponse<PageList<GroupDeviceResponse>> response = new DataResponse().success();
        response.setData(this.deviceService.groupDeviceList(request2));
        return response;
    }

    @RequestMapping(value = {"/device/group/getGroupDeviceByDeviceId"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "GetGroupDeviceRequest", paramType = "body")})
    @ApiOperation(value = "单个设备权限", notes = "单个设备权限")
    public DataResponse<GroupDeviceResponse> getGroupDeviceByDeviceId(@RequestBody GetGroupDeviceRequest request) {
        GetGroupDeviceRequest request2 = (GetGroupDeviceRequest) super.getBaseRequest(request);
        DataResponse<GroupDeviceResponse> response = new DataResponse().success();
        response.setData(this.deviceService.getGroupDeviceByDeviceId(request2));
        return response;
    }

    @RequestMapping(value = {"/device/device/deviceDetail"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "DeviceDetailRequest", paramType = "body")})
    @ApiOperation(value = "设备详情", notes = "设备详情")
    public DataResponse<DeviceResponse> deviceDetail(@RequestBody DeviceDetailRequest request) {
        DeviceDetailRequest request2 = (DeviceDetailRequest) super.getBaseRequest(request);
        DataResponse<DeviceResponse> response = new DataResponse().success();
        response.setData(this.deviceService.deviceDetail(request2));
        return response;
    }

    @RequestMapping(value = {"/device/device/deviceDetailBySn"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "DeviceDetailRequest", paramType = "body")})
    @ApiOperation(value = "设备详情", notes = "设备详情")
    public DataResponse<DeviceResponse> deviceDetailBySn(@RequestBody DeviceDetailRequest request) {
        DeviceDetailRequest request2 = (DeviceDetailRequest) super.getBaseRequest(request);
        DataResponse<DeviceResponse> response = new DataResponse().success();
        response.setData(this.deviceService.deviceDetailBySn(request2));
        return response;
    }

    @RequestMapping(value = {"/device/device/deleteDevice"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "设备相关", description = "设备解绑")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "DeleteDeviceRequest", paramType = "body")})
    @ApiOperation(value = "设备解绑", notes = "设备解绑")
    public CommonResponse deleteDevice(@RequestBody DeleteDeviceRequest request) {
        this.deviceService.deleteDevice((DeleteDeviceRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/device/device/deleteDeviceByDeviceSn"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "设备相关", description = "设备解绑")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "DeleteDeviceRequest", paramType = "body")})
    @ApiOperation(value = "设备解绑", notes = "设备解绑")
    public CommonResponse deleteDeviceByDeviceSn(@RequestBody DeleteDeviceRequest request) {
        this.deviceService.deleteDeviceByDeviceSn((DeleteDeviceRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/device/device/saveDeviceConfig"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "设备相关", description = "设备设置保存")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "SaveDeviceConfigRequest", paramType = "body")})
    @ApiOperation(value = "设备设置保存", notes = "设备设置保存")
    public CommonResponse saveDeviceConfig(@RequestBody SaveDeviceConfigRequest request) {
        this.deviceService.saveDeviceConfig((SaveDeviceConfigRequest) super.getBaseRequest(request), true);
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/device/device/batchSaveDeviceConfig"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "设备相关", description = "批量设备设置保存")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "BatchSaveDeviceConfigRequest", paramType = "body")})
    @ApiOperation(value = "批量设备设置保存", notes = "批量设备设置保存")
    public CommonResponse batchSaveDeviceConfig(@RequestBody BatchSaveDeviceConfigRequest request) {
        this.deviceService.batchSaveDeviceConfig((BatchSaveDeviceConfigRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/device/upload/uploadUpgradePackage"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "设备相关", description = "上传设备升级包")
    @ApiOperation(value = "上传设备升级包", notes = "上传设备升级包")
    public DataResponse<UploadUpgradePackageResponse> uploadUpgradePackage(HttpServletRequest request) {
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
        DataResponse<UploadUpgradePackageResponse> response = new DataResponse().success();
        UploadUpgradePackageRequest upgradePackageRequest = new UploadUpgradePackageRequest(file);
        response.setData(this.deviceService.uploadUpgradePackage((UploadUpgradePackageRequest) super.getBaseRequest(upgradePackageRequest)));
        return response;
    }

    @RequestMapping(value = {"/device/upload/uploadUpgradePackage/v2"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "设备相关", description = "上传设备升级包2")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "UploadUpgradePackageRequest", paramType = "body")})
    @ApiOperation(value = "上传设备升级包2", notes = "上传设备升级包2")
    public DataResponse<UploadUpgradePackageResponse> uploadUpgradePackageV2(@RequestBody UploadUpgradePackageRequest request) {
        DataResponse<UploadUpgradePackageResponse> response = new DataResponse().success();
        response.setData(this.deviceService.uploadUpgradePackageV2((UploadUpgradePackageRequest) super.getBaseRequest(request)));
        return response;
    }

    @RequestMapping(value = {"/device/schedule/saveUpgradeSchedule"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "设备相关", description = "保存升级任务")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "SaveUpgradeScheduleRequest", paramType = "body")})
    @ApiOperation(value = "保存升级任务", notes = "保存升级任务")
    public CommonResponse saveUpgradeSchedule(@RequestBody SaveUpgradeScheduleRequest request) {
        Long updateTime;
        SaveUpgradeScheduleRequest request2 = (SaveUpgradeScheduleRequest) super.getBaseRequest(request);
        Long updateTime2 = Long.valueOf(MyDateUtils.getTimeStampMills());
        if (request2.getUpgradeTime() == null || request2.getUpgradeTime().longValue() == 0) {
            updateTime = Long.valueOf(updateTime2.longValue() + 60000);
        } else {
            updateTime = Long.valueOf(updateTime2.longValue() + request2.getUpgradeTime().longValue());
        }
        request2.setUpgradeTime(updateTime);
        this.pushUpgradeSchedule.saveScheduled(this.deviceService.saveUpgradeSchedule(request2));
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/device/schedule/listUpgradeSchedule"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "ListUpgradeScheduleRequest", paramType = "body")})
    @ApiOperation(value = "升级任务列表", notes = "升级任务列表")
    public DataResponse<PageList<UpgradeScheduleResponse>> listUpgradeSchedule(@RequestBody ListUpgradeScheduleRequest request) {
        ListUpgradeScheduleRequest request2 = (ListUpgradeScheduleRequest) super.getBaseRequest(request);
        DataResponse<PageList<UpgradeScheduleResponse>> response = new DataResponse().success();
        response.setData(this.deviceService.listUpgradeSchedule(request2));
        return response;
    }

    @RequestMapping(value = {"/device/schedule/listUpgradeRecord"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "ListUpgradeRecordRequest", paramType = "body")})
    @ApiOperation(value = "升级记录列表", notes = "升级记录列表")
    public DataResponse<PageList<UpgradeRecordResponse>> listUpgradeRecord(@RequestBody ListUpgradeRecordRequest request) {
        ListUpgradeRecordRequest request2 = (ListUpgradeRecordRequest) super.getBaseRequest(request);
        DataResponse<PageList<UpgradeRecordResponse>> response = new DataResponse().success();
        response.setData(this.deviceService.listUpgradeRecord(request2));
        return response;
    }

    @RequestMapping(value = {"/device/schedule/cancelUpgradeSchedule"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "设备相关", description = "取消升级任务")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "CancelUpgradeScheduleRequest", paramType = "body")})
    @ApiOperation(value = "取消升级任务", notes = "取消升级任务")
    public CommonResponse cancelUpgradeSchedule(@RequestBody CancelUpgradeScheduleRequest request) {
        return this.deviceService.cancelUpgradeSchedule((CancelUpgradeScheduleRequest) super.getBaseRequest(request)) ? new CommonResponse().success() : new CommonResponse().fail();
    }

    @RequestMapping(value = {"/device/schedule/forceClose"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "强制关闭设备升级", description = "强制关闭设备升级")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "CancelUpgradeScheduleRequest", paramType = "body")})
    @ApiOperation(value = "强制关闭设备升级", notes = "强制关闭设备升级")
    public CommonResponse forceCloseUpgradeSchedule(@RequestBody CancelUpgradeScheduleRequest request) {
        this.deviceService.forceCloseUpgradeSchedule((CancelUpgradeScheduleRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/device/schedule/retryUpgradeSchedule"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "设备相关", description = "重试升级任务")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "RetryUpgradeScheduleRequest", paramType = "body")})
    @ApiOperation(value = "重试升级任务", notes = "重试升级任务")
    public CommonResponse retryUpgradeSchedule(@RequestBody RetryUpgradeScheduleRequest request) {
        return this.deviceService.retryUpgradeSchedule((RetryUpgradeScheduleRequest) super.getBaseRequest(request)) ? new CommonResponse().success() : new CommonResponse().fail();
    }

    @RequestMapping(value = {"/device/log/pullDeviceLog"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "设备相关", description = "拉取设备日志")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "PullDeviceLogRequest", paramType = "body")})
    @ApiOperation(value = "拉取设备日志", notes = "拉取设备日志")
    public CommonResponse pullDeviceLog(@RequestBody PullDeviceLogRequest request) {
        return this.deviceService.pullDeviceLog(request) ? new CommonResponse().success() : new CommonResponse().fail();
    }

    @RequestMapping(value = {"/device/device/openTheDoor"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "设备相关", description = "设备远程开门")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "OpenDoorRequest", paramType = "body")})
    @ApiOperation(value = "设备远程开门", notes = "设备远程开门")
    public CommonResponse openTheDoor(@RequestBody OpenDoorRequest request) {
        return this.deviceService.openTheDoor((OpenDoorRequest) super.getBaseRequest(request)) ? new CommonResponse().success() : new CommonResponse().fail();
    }

    @RequestMapping(value = {"/device/log/download"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "设备相关", description = "获取设备日志下载路径")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "DownloadDeviceLogRequest", paramType = "body")})
    @ApiOperation(value = "获取设备日志下载路径", notes = "获取设备日志下载路径")
    public DataResponse<DownloadDeviceLogResponse> downloadDeviceLog(@RequestBody DownloadDeviceLogRequest request) {
        DataResponse<DownloadDeviceLogResponse> response = new DataResponse().success();
        DownloadDeviceLogResponse downloadDeviceLogResponse = this.deviceService.downloadDeviceLog(request);
        response.setData(downloadDeviceLogResponse);
        return response;
    }

    @RequestMapping(value = {"/device/device/restart"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "设备相关", description = "设备重启")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "DeviceRestartRequest", paramType = "body")})
    @ApiOperation(value = "设备重启", notes = "设备重启")
    public CommonResponse restart(@RequestBody DeviceRestartRequest request) {
        return this.deviceService.restart((DeviceRestartRequest) super.getBaseRequest(request)) ? new CommonResponse().success() : new CommonResponse().fail();
    }

    @RequestMapping(value = {"/device/device/resetData"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "设备相关", description = "设备重置数据，重新拉取数据")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "DeviceResetDataRequest", paramType = "body")})
    @ApiOperation(value = "设备重置数据，重新拉取数据", notes = "设备重置数据，重新拉取数据")
    public CommonResponse resetData(@RequestBody DeviceResetDataRequest request) {
        return this.deviceService.resetData((DeviceResetDataRequest) super.getBaseRequest(request)) ? new CommonResponse().success() : new CommonResponse().fail();
    }

    @RequestMapping(value = {"/device/device/pushShowData"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "设备相关", description = "推送设备，展示文本、时长、语音")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "DeviceShowDataRequest", paramType = "body")})
    @ApiOperation(value = "推送设备，展示文本、时长、语音", notes = "推送设备，展示文本、时长、语音")
    public CommonResponse pushShowData(@RequestBody DeviceShowDataRequest request) {
        return this.deviceService.pushShowData((DeviceShowDataRequest) super.getBaseRequest(request)) ? new CommonResponse().success() : new CommonResponse().fail();
    }

    @RequestMapping(value = {"/device/deviceModel/list"}, method = {RequestMethod.POST})
    @ApiOperation(value = "使用中的设备型号列表", notes = "使用中的设备型号列表")
    public DataResponse<List<String>> listDeviceModel() {
        DataResponse<List<String>> dataResponse = new DataResponse<>();
        dataResponse.setData(this.deviceService.listDeviceModel(super.getOrgId()));
        return dataResponse;
    }

    @RequestMapping(value = {"/device/deviceType/list"}, method = {RequestMethod.POST})
    @ApiOperation(value = "使用中的设备类型列表", notes = "使用中的设备类型列表")
    public DataResponse<List<Integer>> listDeviceType() {
        DataResponse<List<Integer>> dataResponse = new DataResponse<>();
        dataResponse.setData(this.deviceService.listDeviceType(super.getOrgId()));
        return dataResponse;
    }

    @RequestMapping(value = {"/device/pushMsg"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "设备相关", description = "推送消息")
    @ApiOperation(value = "推送设备消息", notes = "推送设备消息")
    public DataResponse<String> pushDeviceMsg(@RequestBody DevicePushMsgRequest request) {
        DevicePushMsgRequest request2 = (DevicePushMsgRequest) super.getBaseRequest(request);
        DataResponse<String> dataResponse = new DataResponse<>();
        dataResponse.setData(this.deviceService.pushDeviceMsg(request2));
        return dataResponse;
    }

    @RequestMapping(value = {"/device/pushMsg/query"}, method = {RequestMethod.POST})
    @ApiOperation(value = "查询推送设备消息结果", notes = "查询推送设备消息结果")
    public DataResponse<List<DevicePushMsgResponse>> pushDeviceMsg(@RequestBody DevicePushMsgQueryRequest request) {
        DevicePushMsgQueryRequest request2 = (DevicePushMsgQueryRequest) super.getBaseRequest(request);
        DataResponse<List<DevicePushMsgResponse>> dataResponse = new DataResponse<>();
        dataResponse.setData(this.deviceService.queryPushDeviceMsgResult(request2));
        return dataResponse;
    }

    @RequestMapping(value = {"/device/preActiveDevice"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "设备相关", description = "设备预激活，设备端调用")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "PreActiveDeviceRequest", paramType = "body")})
    @ApiOperation(value = "设备预激活，设备端调用", notes = "设备预激活")
    public DataResponse<PreActiveDeviceResponse> preActiveDevice(@RequestBody PreActiveDeviceRequest request) {
        PreActiveDeviceRequest request2 = (PreActiveDeviceRequest) super.getBaseRequest(request);
        DataResponse<PreActiveDeviceResponse> response = new DataResponse().success();
        response.setData(this.deviceService.preActiveDevice(request2));
        return response;
    }

    @RequestMapping(value = {"/device/preActiveDeviceBatch"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "设备相关", description = "设备批量预激活，设备端调用")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "PreActiveDeviceListRequest", paramType = "body")})
    @ApiOperation(value = "设备预激活，设备端调用", notes = "设备预激活")
    public DataResponse<List<PreActiveDeviceResponse>> preActiveDeviceBatch(@RequestBody PreActiveDeviceListRequest request) {
        PreActiveDeviceListRequest request2 = (PreActiveDeviceListRequest) super.getBaseRequest(request);
        DataResponse<List<PreActiveDeviceResponse>> response = new DataResponse().success();
        List<PreActiveDeviceResponse> preActiveDeviceResponses = new ArrayList<>();
        if (CollectionUtils.isEmpty(request2.getPreActiveDevices())) {
            response.setData(preActiveDeviceResponses);
            return response;
        }
        for (PreActiveDeviceRequest preActiveDeviceRequest : request2.getPreActiveDevices()) {
            try {
                preActiveDeviceRequest.setLoginAccountId(request2.getLoginAccountId());
                preActiveDeviceRequest.setOrgId(request2.getOrgId());
                preActiveDeviceResponses.add(this.deviceService.preActiveDevice(preActiveDeviceRequest));
            } catch (Exception e) {
                PreActiveDeviceResponse error = new PreActiveDeviceResponse("", preActiveDeviceRequest.getDeviceSn(), 0, e.getMessage());
                preActiveDeviceResponses.add(error);
            }
        }
        response.setData(preActiveDeviceResponses);
        return response;
    }

    @RequestMapping(value = {"/device/group/bind"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "设备相关", description = "设备绑定到设备组")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = false, dataType = "BindDeviceGroupRequest", paramType = "body")})
    @ApiOperation(value = "设备绑定到设备组", notes = "设备相关")
    public CommonResponse bindDeviceGroup(@RequestBody BindDeviceGroupRequest request) {
        this.deviceService.bindDeviceGroup((BindDeviceGroupRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }
}
