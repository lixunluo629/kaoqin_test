package com.moredian.onpremise.frontend.controller.device;

import com.moredian.onpremise.api.device.DeviceGroupService;
import com.moredian.onpremise.core.model.request.DeviceGroupDeleteRequest;
import com.moredian.onpremise.core.model.request.DeviceGroupRequest;
import com.moredian.onpremise.core.model.response.CommonResponse;
import com.moredian.onpremise.core.model.response.DataResponse;
import com.moredian.onpremise.core.model.response.DeviceGroupResponse;
import com.moredian.onpremise.frontend.controller.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "on-premise-device", description = "on-premise设备组相关")
@RestController
/* loaded from: onpremise-frontend-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/frontend/controller/device/DeviceGroupController.class */
public class DeviceGroupController extends BaseController {

    @Autowired
    private DeviceGroupService deviceGroupService;

    @RequestMapping(value = {"/device/group/add"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = false, dataType = "DeviceGroupRequest", paramType = "body")})
    @ApiOperation(value = "设备组新增", notes = "设备列表")
    public DataResponse<Long> addDeviceGroup(@RequestBody DeviceGroupRequest request) {
        DeviceGroupRequest request2 = (DeviceGroupRequest) super.getBaseRequest(request);
        DataResponse<Long> response = new DataResponse().success();
        response.setData(this.deviceGroupService.insertOne(request2));
        return response;
    }

    @RequestMapping(value = {"/device/group/delete"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = false, dataType = "DeviceGroupRequest", paramType = "body")})
    @ApiOperation(value = "设备组删除", notes = "设备列表")
    public CommonResponse deleteDeviceGroup(@RequestBody DeviceGroupDeleteRequest request) {
        this.deviceGroupService.softDeleteById((DeviceGroupDeleteRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/device/group/update"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = false, dataType = "DeviceGroupRequest", paramType = "body")})
    @ApiOperation(value = "设备组更新", notes = "设备列表")
    public CommonResponse updateDeviceGroup(@RequestBody DeviceGroupRequest request) {
        this.deviceGroupService.updateOneById((DeviceGroupRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/device/group/list"}, method = {RequestMethod.POST})
    @ApiOperation(value = "设备组列表", notes = "设备列表")
    public DataResponse<List<DeviceGroupResponse>> listDeviceGroup() {
        DataResponse<List<DeviceGroupResponse>> response = new DataResponse().success();
        response.setData(this.deviceGroupService.getList(super.getOrgId()));
        return response;
    }
}
