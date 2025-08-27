package com.moredian.onpremise.frontend.controller.attendance;

import com.moredian.onpremise.api.attendance.AttendanceGroupService;
import com.moredian.onpremise.core.aop.operLog.annotation.LogAnnotation;
import com.moredian.onpremise.core.model.request.CheckMemberHasAttendanceGroupRequest;
import com.moredian.onpremise.core.model.request.DeleteAttendanceGroupRequest;
import com.moredian.onpremise.core.model.request.ListAttendanceGroupRequest;
import com.moredian.onpremise.core.model.request.QueryAttendanceGroupDetailRequest;
import com.moredian.onpremise.core.model.request.SaveAttendanceGroupRequest;
import com.moredian.onpremise.core.model.response.AttendanceGroupDetailResponse;
import com.moredian.onpremise.core.model.response.AttendanceGroupListResponse;
import com.moredian.onpremise.core.model.response.CheckMemberHasAttendanceGroupResponse;
import com.moredian.onpremise.core.model.response.CommonResponse;
import com.moredian.onpremise.core.model.response.DataResponse;
import com.moredian.onpremise.core.model.response.SaveAttendanceGroupResponse;
import com.moredian.onpremise.core.utils.PageList;
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

@Api(value = "on-premise-biz-attendance", description = "on-premise考勤相关")
@RestController
/* loaded from: onpremise-frontend-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/frontend/controller/attendance/AttendanceGroupController.class */
public class AttendanceGroupController extends BaseController {

    @Autowired
    private AttendanceGroupService attendanceGroupService;

    @RequestMapping(value = {"/attendance/group/saveAttendanceGroup"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "考勤-考勤组相关", description = "保存考勤组")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "SaveAttendanceGroupRequest", paramType = "body")})
    @ApiOperation(value = "保存考勤组", notes = "保存考勤组")
    public DataResponse<SaveAttendanceGroupResponse> saveAttendanceGroup(@RequestBody SaveAttendanceGroupRequest request) {
        SaveAttendanceGroupRequest request2 = (SaveAttendanceGroupRequest) super.getBaseRequest(request);
        DataResponse<SaveAttendanceGroupResponse> response = new DataResponse().success();
        response.setData(this.attendanceGroupService.saveAttendanceGroup(request2));
        return response;
    }

    @RequestMapping(value = {"/attendance/group/queryAttendanceGroupDetail"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "QueryAttendanceGroupDetailRequest", paramType = "body")})
    @ApiOperation(value = "考勤组详情", notes = "考勤组详情")
    public DataResponse<AttendanceGroupDetailResponse> queryAttendanceGroupDetail(@RequestBody QueryAttendanceGroupDetailRequest request) {
        QueryAttendanceGroupDetailRequest request2 = (QueryAttendanceGroupDetailRequest) super.getBaseRequest(request);
        DataResponse<AttendanceGroupDetailResponse> response = new DataResponse().success();
        response.setData(this.attendanceGroupService.queryAttendanceGroupDetail(request2));
        return response;
    }

    @RequestMapping(value = {"/attendance/group/deleteAttendanceGroup"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "考勤-考勤组相关", description = "考勤组删除")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "DeleteAttendanceGroupRequest", paramType = "body")})
    @ApiOperation(value = "考勤组删除", notes = "考勤组删除")
    public CommonResponse deleteAttendanceGroup(@RequestBody DeleteAttendanceGroupRequest request) {
        this.attendanceGroupService.deleteAttendanceGroup((DeleteAttendanceGroupRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/attendance/group/listAttendanceGroup"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "ListAttendanceGroupRequest", paramType = "body")})
    @ApiOperation(value = "考勤组列表", notes = "考勤组列表")
    public DataResponse<PageList<AttendanceGroupListResponse>> listAttendanceGroup(@RequestBody ListAttendanceGroupRequest request) {
        ListAttendanceGroupRequest request2 = (ListAttendanceGroupRequest) super.getBaseRequest(request);
        DataResponse<PageList<AttendanceGroupListResponse>> response = new DataResponse().success();
        response.setData(this.attendanceGroupService.listAttendanceGroup(request2));
        return response;
    }

    @RequestMapping(value = {"/attendance/group/checkMemberHasAttendanceGroup"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "CheckMemberHasAttendanceGroupRequest", paramType = "body")})
    @ApiOperation(value = "考勤组列表", notes = "考勤组列表")
    public DataResponse<List<CheckMemberHasAttendanceGroupResponse>> checkMemberHasAttendanceGroup(@RequestBody CheckMemberHasAttendanceGroupRequest request) {
        CheckMemberHasAttendanceGroupRequest request2 = (CheckMemberHasAttendanceGroupRequest) super.getBaseRequest(request);
        DataResponse<List<CheckMemberHasAttendanceGroupResponse>> response = new DataResponse().success();
        response.setData(this.attendanceGroupService.checkMemberHasAttendanceGroup(request2));
        return response;
    }
}
