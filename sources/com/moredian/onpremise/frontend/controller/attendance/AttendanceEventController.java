package com.moredian.onpremise.frontend.controller.attendance;

import com.moredian.onpremise.api.attendance.AttendanceEventService;
import com.moredian.onpremise.core.aop.operLog.annotation.LogAnnotation;
import com.moredian.onpremise.core.model.request.AttendanceEventListRequest;
import com.moredian.onpremise.core.model.request.DeleteAttendanceEventRequest;
import com.moredian.onpremise.core.model.request.QueryAttendanceEventRequest;
import com.moredian.onpremise.core.model.request.SaveAttendanceEventRequest;
import com.moredian.onpremise.core.model.request.UpdateAttendanceEventRequest;
import com.moredian.onpremise.core.model.response.AttendanceEventDetailResponse;
import com.moredian.onpremise.core.model.response.AttendanceEventListResponse;
import com.moredian.onpremise.core.model.response.CommonResponse;
import com.moredian.onpremise.core.model.response.DataResponse;
import com.moredian.onpremise.core.utils.PageList;
import com.moredian.onpremise.frontend.controller.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "on-premise-biz-attendance", description = "on-premise考勤相关")
@RestController
/* loaded from: onpremise-frontend-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/frontend/controller/attendance/AttendanceEventController.class */
public class AttendanceEventController extends BaseController {

    @Autowired
    private AttendanceEventService attendanceEventService;

    @RequestMapping(value = {"/attendance/event/saveAttendanceEvent"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "考勤-考勤事件相关", description = "考勤事件新增")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "SaveAttendanceEventRequest", paramType = "body")})
    @ApiOperation(value = "考勤事件新增", notes = "考勤事件新增")
    public CommonResponse saveAttendanceEvent(@RequestBody SaveAttendanceEventRequest request) {
        this.attendanceEventService.insertAttendanceEvent((SaveAttendanceEventRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/attendance/event/deleteAttendanceEvent"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "考勤-考勤事件相关", description = "考勤事件删除")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "DeleteAttendanceEventRequest", paramType = "body")})
    @ApiOperation(value = "考勤事件删除", notes = "考勤事件删除")
    public CommonResponse deleteAttendanceEvent(@RequestBody DeleteAttendanceEventRequest request) {
        this.attendanceEventService.deleteAttendanceEvent((DeleteAttendanceEventRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/attendance/event/updateAttendanceEvent"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "考勤-考勤事件相关", description = "考勤事件修改")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "UpdateAttendanceEventRequest", paramType = "body")})
    @ApiOperation(value = "考勤事件修改", notes = "考勤事件修改")
    public CommonResponse updateAttendanceEvent(@RequestBody UpdateAttendanceEventRequest request) {
        this.attendanceEventService.updateAttendanceEvent((UpdateAttendanceEventRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/attendance/event/findAttendanceEvent"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "QueryAttendanceEventRequest", paramType = "body")})
    @ApiOperation(value = "考勤事件查询", notes = "考勤事件查询")
    public DataResponse<AttendanceEventDetailResponse> findAttendanceEvent(@RequestBody QueryAttendanceEventRequest request) {
        AttendanceEventDetailResponse attendanceEventResponse = this.attendanceEventService.findAttendanceEventById((QueryAttendanceEventRequest) super.getBaseRequest(request));
        DataResponse<AttendanceEventDetailResponse> response = new DataResponse().success();
        response.setData(attendanceEventResponse);
        return response;
    }

    @RequestMapping(value = {"/attendance/event/listAttendanceEvent"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "AttendanceEventListRequest", paramType = "body")})
    @ApiOperation(value = "考勤事件列表查询", notes = "考勤事件列表查询")
    public DataResponse<PageList<AttendanceEventListResponse>> listAttendanceEvent(@RequestBody AttendanceEventListRequest request) {
        AttendanceEventListRequest request2 = (AttendanceEventListRequest) super.getBaseRequest(request);
        DataResponse<PageList<AttendanceEventListResponse>> response = new DataResponse().success();
        PageList<AttendanceEventListResponse> pageList = this.attendanceEventService.pageFindAttendanceEvent(request2);
        response.setData(pageList);
        return response;
    }
}
