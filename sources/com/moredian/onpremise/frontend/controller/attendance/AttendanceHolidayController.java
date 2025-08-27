package com.moredian.onpremise.frontend.controller.attendance;

import com.moredian.onpremise.api.attendance.AttendanceHolidayService;
import com.moredian.onpremise.core.aop.operLog.annotation.LogAnnotation;
import com.moredian.onpremise.core.model.request.AttendanceHolidayListRequest;
import com.moredian.onpremise.core.model.request.DeleteAttendanceHolidayRequest;
import com.moredian.onpremise.core.model.request.QueryAttendanceHolidayRequest;
import com.moredian.onpremise.core.model.request.SaveAttendanceHolidayRequest;
import com.moredian.onpremise.core.model.response.AttendanceHolidayListResponse;
import com.moredian.onpremise.core.model.response.AttendanceHolidayResponse;
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
/* loaded from: onpremise-frontend-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/frontend/controller/attendance/AttendanceHolidayController.class */
public class AttendanceHolidayController extends BaseController {

    @Autowired
    private AttendanceHolidayService attendanceHolidayService;

    @RequestMapping(value = {"/attendance/holiday/saveAttendanceHoliday"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "考勤节假日相关", description = "考勤节假日新增")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "SaveAttendanceHolidayRequest", paramType = "body")})
    @ApiOperation(value = "考勤节假日新增", notes = "考勤节假日新增")
    public CommonResponse saveAttendanceHoliday(@RequestBody SaveAttendanceHolidayRequest request) {
        this.attendanceHolidayService.insertAttendanceHoliday((SaveAttendanceHolidayRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/attendance/holiday/deleteAttendanceHoliday"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "考勤节假日相关", description = "考勤节假日删除")
    @ApiOperation(value = "考勤节假日删除", notes = "考勤节假日删除")
    public CommonResponse deleteAttendanceHoliday(@RequestBody DeleteAttendanceHolidayRequest request) {
        this.attendanceHolidayService.deleteAttendanceHoliday((DeleteAttendanceHolidayRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/attendance/holiday/updateAttendanceHoliday"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "考勤节假日相关", description = "考勤节假日修改")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "SaveAttendanceHolidayRequest", paramType = "body")})
    @ApiOperation(value = "考勤节假日修改", notes = "考勤节假日修改")
    public CommonResponse updateAttendanceHoliday(@RequestBody SaveAttendanceHolidayRequest request) {
        this.attendanceHolidayService.updateAttendanceHoliday((SaveAttendanceHolidayRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/attendance/holiday/findAttendanceHoliday"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "QueryAttendanceHolidayRequest", paramType = "body")})
    @ApiOperation(value = "考勤节假日查询", notes = "考勤节假日查询")
    public DataResponse<AttendanceHolidayResponse> findAttendanceHoliday(@RequestBody QueryAttendanceHolidayRequest request) {
        AttendanceHolidayResponse attendanceHolidayResponse = this.attendanceHolidayService.findAttendanceHolidayById((QueryAttendanceHolidayRequest) super.getBaseRequest(request));
        DataResponse<AttendanceHolidayResponse> response = new DataResponse().success();
        response.setData(attendanceHolidayResponse);
        return response;
    }

    @RequestMapping(value = {"/attendance/holiday/list"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "AttendanceHolidayListRequest", paramType = "body")})
    @ApiOperation(value = "考勤节假日列表查询", notes = "考勤节假日列表查询")
    public DataResponse<PageList<AttendanceHolidayListResponse>> listAttendanceHoliday(@RequestBody AttendanceHolidayListRequest request) {
        AttendanceHolidayListRequest request2 = (AttendanceHolidayListRequest) super.getBaseRequest(request);
        DataResponse<PageList<AttendanceHolidayListResponse>> response = new DataResponse().success();
        PageList<AttendanceHolidayListResponse> pageList = this.attendanceHolidayService.pageFindAttendanceHoliday(request2);
        response.setData(pageList);
        return response;
    }
}
