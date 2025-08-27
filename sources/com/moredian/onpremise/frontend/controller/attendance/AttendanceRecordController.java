package com.moredian.onpremise.frontend.controller.attendance;

import com.moredian.onpremise.api.attendance.AttendanceRecordService;
import com.moredian.onpremise.core.model.request.AttendanceStatisticsForMonthExportRequest;
import com.moredian.onpremise.core.model.request.CountInfoForDayRequest;
import com.moredian.onpremise.core.model.request.CountInfoForMonthRequest;
import com.moredian.onpremise.core.model.request.ListAttendanceRecordRequest;
import com.moredian.onpremise.core.model.response.CountInfoForMonthResponse;
import com.moredian.onpremise.core.model.response.DataResponse;
import com.moredian.onpremise.core.model.response.ListAttendanceRecordResponse;
import com.moredian.onpremise.core.model.response.ListInfoForDayDetailResponse;
import com.moredian.onpremise.core.model.response.ListInfoForDayResponse;
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
/* loaded from: onpremise-frontend-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/frontend/controller/attendance/AttendanceRecordController.class */
public class AttendanceRecordController extends BaseController {

    @Autowired
    private AttendanceRecordService attendanceRecordService;

    @RequestMapping(value = {"/attendance/record/listAttendanceRecord"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "ListAttendanceRecordRequest", paramType = "body")})
    @ApiOperation(value = "考勤记录列表", notes = "考勤记录列表")
    public DataResponse<PageList<ListAttendanceRecordResponse>> listAttendanceRecord(@RequestBody ListAttendanceRecordRequest request) {
        ListAttendanceRecordRequest request2 = (ListAttendanceRecordRequest) super.getBaseRequest(request);
        DataResponse<PageList<ListAttendanceRecordResponse>> response = new DataResponse().success();
        response.setData(this.attendanceRecordService.listAttendanceRecord(request2));
        return response;
    }

    @RequestMapping(value = {"/attendance/record/listInfoForDay"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "CountInfoForDayRequest", paramType = "body")})
    @Deprecated
    @ApiOperation(value = "每日统计报表-废弃", notes = "每日统计报表-废弃")
    public DataResponse<PageList<ListInfoForDayResponse>> listInfoForDay(@RequestBody CountInfoForDayRequest request) {
        CountInfoForDayRequest request2 = (CountInfoForDayRequest) super.getBaseRequest(request);
        DataResponse<PageList<ListInfoForDayResponse>> response = new DataResponse().success();
        response.setData(this.attendanceRecordService.listInfoForDay(request2));
        return response;
    }

    @RequestMapping(value = {"/attendance/record/listInfoForMonth"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "CountInfoForMonthRequest", paramType = "body")})
    @ApiOperation(value = "月度汇总列表", notes = "月度汇总列表")
    public DataResponse<PageList<CountInfoForMonthResponse>> listInfoForMonth(@RequestBody CountInfoForMonthRequest request) {
        CountInfoForMonthRequest request2 = (CountInfoForMonthRequest) super.getBaseRequest(request);
        DataResponse<PageList<CountInfoForMonthResponse>> response = new DataResponse().success();
        response.setData(this.attendanceRecordService.listInfoForMonth(request2));
        return response;
    }

    @RequestMapping(value = {"/attendance/record/statisticsForDay"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "CountInfoForDayRequest", paramType = "body")})
    @ApiOperation(value = "每日统计报表", notes = "每日统计报表")
    public DataResponse<ListInfoForDayResponse> statisticsForDay(@RequestBody CountInfoForDayRequest request) {
        CountInfoForDayRequest request2 = (CountInfoForDayRequest) super.getBaseRequest(request);
        DataResponse<ListInfoForDayResponse> response = new DataResponse().success();
        response.setData(this.attendanceRecordService.statisticsForDay(request2));
        return response;
    }

    @RequestMapping(value = {"/attendance/record/statisticsForDayDetail"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "CountInfoForDayRequest", paramType = "body")})
    @ApiOperation(value = "每日统计详情报表", notes = "每日统计详情报表")
    public DataResponse<PageList<ListInfoForDayDetailResponse>> statisticsForDayDetail(@RequestBody CountInfoForDayRequest request) {
        CountInfoForDayRequest request2 = (CountInfoForDayRequest) super.getBaseRequest(request);
        DataResponse<PageList<ListInfoForDayDetailResponse>> response = new DataResponse().success();
        response.setData(this.attendanceRecordService.statisticsForDayDetail(request2));
        return response;
    }

    @RequestMapping(value = {"/attendance/record/statisticsForMonth/export"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "AttendanceStatisticsForMonthExportRequest", paramType = "body")})
    @ApiOperation(value = "考勤月报表导出", notes = "考勤月报表导出")
    public DataResponse<String> statisticsForMonthExport(@RequestBody AttendanceStatisticsForMonthExportRequest request) {
        AttendanceStatisticsForMonthExportRequest request2 = (AttendanceStatisticsForMonthExportRequest) super.getBaseRequest(request);
        DataResponse<String> response = new DataResponse().success();
        response.setData(this.attendanceRecordService.statisticsForMonthExport(request2));
        return response;
    }
}
