package com.moredian.onpremise.frontend.controller.checkin;

import com.moredian.onpremise.api.checkIn.CheckInService;
import com.moredian.onpremise.core.aop.operLog.annotation.LogAnnotation;
import com.moredian.onpremise.core.common.enums.CommonStatusEnum;
import com.moredian.onpremise.core.model.request.CheckInLogListRequest;
import com.moredian.onpremise.core.model.request.CheckInTaskListRequest;
import com.moredian.onpremise.core.model.request.DeleteCheckInSupplementRequest;
import com.moredian.onpremise.core.model.request.ListCheckInSupplementRequest;
import com.moredian.onpremise.core.model.request.QueryCheckInSupplementDetailRequest;
import com.moredian.onpremise.core.model.request.SaveCheckInSupplementRequest;
import com.moredian.onpremise.core.model.request.SaveCheckInTaskRequest;
import com.moredian.onpremise.core.model.response.CheckInLogDayListResponse;
import com.moredian.onpremise.core.model.response.CheckInLogListResponse;
import com.moredian.onpremise.core.model.response.CheckInLogMemberListResponse;
import com.moredian.onpremise.core.model.response.CheckInLogResponse;
import com.moredian.onpremise.core.model.response.CheckInSupplementResponse;
import com.moredian.onpremise.core.model.response.CheckInTaskListResponse;
import com.moredian.onpremise.core.model.response.CheckInTaskResponse;
import com.moredian.onpremise.core.model.response.CommonResponse;
import com.moredian.onpremise.core.model.response.DataResponse;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "on-premise-biz-checkIn", description = "on-premise签到相关")
@RestController
/* loaded from: onpremise-frontend-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/frontend/controller/checkin/CheckInTaskController.class */
public class CheckInTaskController extends BaseController {

    @Autowired
    private CheckInService checkInService;

    @RequestMapping(value = {"/checkIn/task"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "签到相关", description = "签到任务新增")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "SaveCheckInTaskRequest", paramType = "body")})
    @ApiOperation(value = "签到任务新增", notes = "签到任务新增")
    public DataResponse<Long> saveCheckInTask(@RequestBody SaveCheckInTaskRequest request) {
        SaveCheckInTaskRequest request2 = (SaveCheckInTaskRequest) super.getBaseRequest(request);
        request2.setAllDay(Integer.valueOf(CommonStatusEnum.NO.getValue()));
        DataResponse<Long> response = new DataResponse<>();
        Long id = this.checkInService.insertCheckInTask(request2);
        response.setData(id);
        return response;
    }

    @RequestMapping(value = {"/checkIn/task"}, method = {RequestMethod.DELETE})
    @LogAnnotation(module = "签到相关", description = "签到任务删除")
    @ApiOperation(value = "签到任务删除", notes = "签到任务删除")
    public CommonResponse deleteCheckInTask(@RequestParam("id") Long id) {
        this.checkInService.deleteCheckInTask(getOrgId(), id);
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/checkIn/task"}, method = {RequestMethod.PUT})
    @LogAnnotation(module = "签到相关", description = "签到任务修改")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "SaveCheckInTaskRequest", paramType = "body")})
    @ApiOperation(value = "签到任务修改", notes = "签到任务修改")
    public CommonResponse updateCheckInTask(@RequestBody SaveCheckInTaskRequest request) {
        SaveCheckInTaskRequest request2 = (SaveCheckInTaskRequest) super.getBaseRequest(request);
        request2.setAllDay(Integer.valueOf(CommonStatusEnum.NO.getValue()));
        this.checkInService.updateCheckInTask(request2);
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/checkIn/task"}, method = {RequestMethod.GET})
    @ApiOperation(value = "签到任务查询", notes = "签到任务查询")
    public DataResponse<CheckInTaskResponse> findCheckInTask(@RequestParam("id") Long id) {
        DataResponse response = new DataResponse();
        CheckInTaskResponse checkInTaskResponse = this.checkInService.findCheckInTaskById(getOrgId(), id);
        response.setData(checkInTaskResponse);
        return response;
    }

    @RequestMapping(value = {"/checkIn/task/list"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "CheckInTaskListRequest", paramType = "body")})
    @ApiOperation(value = "签到任务列表查询", notes = "签到任务列表查询")
    public DataResponse<PageList<CheckInTaskListResponse>> listCheckInTask(@RequestBody CheckInTaskListRequest request) {
        CheckInTaskListRequest request2 = (CheckInTaskListRequest) super.getBaseRequest(request);
        DataResponse<PageList<CheckInTaskListResponse>> response = new DataResponse().success();
        PageList<CheckInTaskListResponse> pageList = this.checkInService.pageCheckInTask(request2);
        response.setData(pageList);
        return response;
    }

    @RequestMapping(value = {"/checkIn/log/day/list"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "CheckInLogListRequest", paramType = "body")})
    @ApiOperation(value = "任务报表", notes = "任务报表")
    public DataResponse<PageList<CheckInLogDayListResponse>> listCheckInLogDay(@RequestBody CheckInLogListRequest request) {
        CheckInLogListRequest request2 = (CheckInLogListRequest) super.getBaseRequest(request);
        DataResponse<PageList<CheckInLogDayListResponse>> response = new DataResponse().success();
        PageList<CheckInLogDayListResponse> pageList = this.checkInService.pageCheckInLogDay(request2);
        response.setData(pageList);
        return response;
    }

    @RequestMapping(value = {"/checkIn/log/member/list"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "CheckInLogListRequest", paramType = "body")})
    @ApiOperation(value = "个人记录", notes = "个人记录")
    public DataResponse<PageList<CheckInLogMemberListResponse>> listCheckInLogMember(@RequestBody CheckInLogListRequest request) {
        CheckInLogListRequest request2 = (CheckInLogListRequest) super.getBaseRequest(request);
        DataResponse<PageList<CheckInLogMemberListResponse>> response = new DataResponse().success();
        PageList<CheckInLogMemberListResponse> pageList = this.checkInService.pageCheckInLogMember(request2);
        response.setData(pageList);
        return response;
    }

    @RequestMapping(value = {"/checkIn/log/member/list/export"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "CheckInLogListRequest", paramType = "body")})
    @ApiOperation(value = "个人记录导出", notes = "个人记录导出")
    public DataResponse<List<CheckInLogMemberListResponse>> exportCheckInLogMember(@RequestBody CheckInLogListRequest request) {
        CheckInLogListRequest request2 = (CheckInLogListRequest) super.getBaseRequest(request);
        DataResponse<List<CheckInLogMemberListResponse>> response = new DataResponse().success();
        List<CheckInLogMemberListResponse> list = this.checkInService.exportCheckInLogMember(request2);
        response.setData(list);
        return response;
    }

    @RequestMapping(value = {"/checkIn/log/detail/list"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "CheckInLogListRequest", paramType = "body")})
    @ApiOperation(value = "签到记录", notes = "签到记录")
    public DataResponse<PageList<CheckInLogListResponse>> listCheckInLogDetail(@RequestBody CheckInLogListRequest request) {
        CheckInLogListRequest request2 = (CheckInLogListRequest) super.getBaseRequest(request);
        DataResponse<PageList<CheckInLogListResponse>> response = new DataResponse().success();
        PageList<CheckInLogListResponse> pageList = this.checkInService.pageCheckInLogDetail(request2);
        response.setData(pageList);
        return response;
    }

    @RequestMapping(value = {"/checkIn/log/detail/list/export"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "CheckInLogListRequest", paramType = "body")})
    @ApiOperation(value = "签到记录导出", notes = "签到记录导出")
    public DataResponse<List<CheckInLogListResponse>> exportCheckInLogDetail(@RequestBody CheckInLogListRequest request) {
        CheckInLogListRequest request2 = (CheckInLogListRequest) super.getBaseRequest(request);
        DataResponse<List<CheckInLogListResponse>> response = new DataResponse().success();
        List<CheckInLogListResponse> list = this.checkInService.exportCheckInLogDetail(request2);
        response.setData(list);
        return response;
    }

    @RequestMapping(value = {"/checkIn/log/detail"}, method = {RequestMethod.GET})
    @ApiOperation(value = "签到明细详情查询", notes = "签到明细详情查询")
    public DataResponse<CheckInLogResponse> findCheckInLogDetail(@RequestParam("id") Long id) {
        DataResponse<CheckInLogResponse> response = new DataResponse<>();
        CheckInLogResponse checkInLogResponse = this.checkInService.findCheckInLogById(getOrgId(), id);
        response.setData(checkInLogResponse);
        return response;
    }

    @RequestMapping(value = {"/checkIn/log/listCheckInSupplement"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "ListCheckInSupplementRequest", paramType = "body")})
    @ApiOperation(value = "补签列表查询", notes = "补签列表查询")
    public DataResponse<PageList<CheckInSupplementResponse>> listCheckInSupplement(@RequestBody ListCheckInSupplementRequest request) {
        ListCheckInSupplementRequest request2 = (ListCheckInSupplementRequest) super.getBaseRequest(request);
        DataResponse<PageList<CheckInSupplementResponse>> response = new DataResponse().success();
        PageList<CheckInSupplementResponse> pageList = this.checkInService.listCheckInSupplementResponse(request2);
        response.setData(pageList);
        return response;
    }

    @RequestMapping(value = {"/checkIn/log/queryCheckInSupplementDetail"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "QueryCheckInSupplementDetailRequest", paramType = "body")})
    @ApiOperation(value = "补签详情查询", notes = "补签详情查询")
    public DataResponse<CheckInSupplementResponse> queryCheckInSupplementDetail(@RequestBody QueryCheckInSupplementDetailRequest request) {
        QueryCheckInSupplementDetailRequest request2 = (QueryCheckInSupplementDetailRequest) super.getBaseRequest(request);
        DataResponse<CheckInSupplementResponse> response = new DataResponse().success();
        CheckInSupplementResponse checkInSupplementResponse = this.checkInService.queryCheckInSupplementResponse(request2);
        response.setData(checkInSupplementResponse);
        return response;
    }

    @RequestMapping(value = {"/checkIn/log/saveCheckInSupplement"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "签到相关", description = "签到补签新增修改")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "SaveCheckInSupplementRequest", paramType = "body")})
    @ApiOperation(value = "签到补签新增修改", notes = "签到补签修改")
    public CommonResponse saveCheckInSupplement(@RequestBody SaveCheckInSupplementRequest request) {
        this.checkInService.saveCheckInSupplement((SaveCheckInSupplementRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/checkIn/log/deleteCheckInSupplement"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "签到相关", description = "签到补签删除")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "DeleteCheckInSupplementRequest", paramType = "body")})
    @ApiOperation(value = "签到补签删除", notes = "签到补签删除")
    public CommonResponse deleteCheckInSupplement(@RequestBody DeleteCheckInSupplementRequest request) {
        this.checkInService.deleteCheckInSupplement((DeleteCheckInSupplementRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }
}
