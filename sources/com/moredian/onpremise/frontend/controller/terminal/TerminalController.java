package com.moredian.onpremise.frontend.controller.terminal;

import com.moredian.onpremise.api.account.AccountService;
import com.moredian.onpremise.api.attendance.AttendanceGroupService;
import com.moredian.onpremise.api.attendance.AttendanceHolidayService;
import com.moredian.onpremise.api.checkIn.CheckInService;
import com.moredian.onpremise.api.device.DeviceService;
import com.moredian.onpremise.api.group.GroupService;
import com.moredian.onpremise.api.meal.MealCanteenService;
import com.moredian.onpremise.api.meal.MealRecordService;
import com.moredian.onpremise.api.member.DeptService;
import com.moredian.onpremise.api.member.MemberService;
import com.moredian.onpremise.api.record.VerifyRecordService;
import com.moredian.onpremise.api.record.WarnRecordService;
import com.moredian.onpremise.api.server.ConfigService;
import com.moredian.onpremise.api.visit.VisitConfigService;
import com.moredian.onpremise.api.visit.VisitRecordService;
import com.moredian.onpremise.core.aop.token.annotation.AppTokenAnnotation;
import com.moredian.onpremise.core.common.constants.ConfigConstants;
import com.moredian.onpremise.core.common.enums.NettyErrorEnum;
import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.exception.BizException;
import com.moredian.onpremise.core.mapper.MemberMapper;
import com.moredian.onpremise.core.model.domain.Member;
import com.moredian.onpremise.core.model.request.ActiveDeviceRequest;
import com.moredian.onpremise.core.model.request.DevicePushLogRequest;
import com.moredian.onpremise.core.model.request.SaveMealRecordRequest;
import com.moredian.onpremise.core.model.request.SaveMemberFaceRequest;
import com.moredian.onpremise.core.model.request.TerminalCheckDeviceActiveRequest;
import com.moredian.onpremise.core.model.request.TerminalCheckPrivilegeRequest;
import com.moredian.onpremise.core.model.request.TerminalDeptRequest;
import com.moredian.onpremise.core.model.request.TerminalDeviceCallbackRequest;
import com.moredian.onpremise.core.model.request.TerminalFindMemberRequest;
import com.moredian.onpremise.core.model.request.TerminalLoginRequest;
import com.moredian.onpremise.core.model.request.TerminalQrCheckRequest;
import com.moredian.onpremise.core.model.request.TerminalSaveDeviceConfigRequest;
import com.moredian.onpremise.core.model.request.TerminalSaveMemberRequest;
import com.moredian.onpremise.core.model.request.TerminalSearchMemberRequest;
import com.moredian.onpremise.core.model.request.TerminalSyncCanteenRequest;
import com.moredian.onpremise.core.model.request.TerminalSyncCheckInTaskRequest;
import com.moredian.onpremise.core.model.request.TerminalSyncEigenvalueValueRequest;
import com.moredian.onpremise.core.model.request.TerminalSyncExternalContactDetailRequest;
import com.moredian.onpremise.core.model.request.TerminalSyncRequest;
import com.moredian.onpremise.core.model.request.TerminalTemperatureCallbackRequest;
import com.moredian.onpremise.core.model.request.TerminalVisitRecordRequest;
import com.moredian.onpremise.core.model.request.UpdateDeviceRequest;
import com.moredian.onpremise.core.model.request.VerifyRecordBatchSaveRequest;
import com.moredian.onpremise.core.model.request.VerifyRecordSaveRequest;
import com.moredian.onpremise.core.model.request.WarnRecordBatchSaveRequest;
import com.moredian.onpremise.core.model.request.WarnRecordSaveRequest;
import com.moredian.onpremise.core.model.response.ActiveDeviceResponse;
import com.moredian.onpremise.core.model.response.CommonResponse;
import com.moredian.onpremise.core.model.response.DataResponse;
import com.moredian.onpremise.core.model.response.DeviceConfigResponse;
import com.moredian.onpremise.core.model.response.MemberDetailsResponse;
import com.moredian.onpremise.core.model.response.QueryConfigResponse;
import com.moredian.onpremise.core.model.response.SaveImageResponse;
import com.moredian.onpremise.core.model.response.SaveMealRecordResponse;
import com.moredian.onpremise.core.model.response.SaveMemberFaceByDeviceResponse;
import com.moredian.onpremise.core.model.response.TerminalCheckDeviceActiveResponse;
import com.moredian.onpremise.core.model.response.TerminalCheckPrivilegeResponse;
import com.moredian.onpremise.core.model.response.TerminalDeptResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncAccountResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncAttendanceGroupResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncAttendanceHolidayResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncCanteenResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncCheckInTaskResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncDeviceConfigResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncEigenvalueValueResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncExternalContactDetailResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncGroupResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncMemberCheckInTaskResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncMemberMealCanteenResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncMemberResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncVisitConfigResponse;
import com.moredian.onpremise.core.model.response.VerifyRecordSaveResponse;
import com.moredian.onpremise.core.utils.DesUtils;
import com.moredian.onpremise.core.utils.JsonUtils;
import com.moredian.onpremise.frontend.controller.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Api(value = "on-premise-terminal", description = "on-premise终端调用接口相关")
@RestController
/* loaded from: onpremise-frontend-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/frontend/controller/terminal/TerminalController.class */
public class TerminalController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) TerminalController.class);

    @Autowired
    private MemberService memberService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private VerifyRecordService verifyRecordService;

    @Autowired
    private WarnRecordService warnRecordService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CheckInService checkInService;

    @Autowired
    private AttendanceGroupService attendanceGroupService;

    @Autowired
    private AttendanceHolidayService attendanceHolidayService;

    @Autowired
    private MealRecordService mealRecordService;

    @Autowired
    private MealCanteenService mealCanteenService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private VisitRecordService visitRecordService;

    @Autowired
    private VisitConfigService visitConfigService;

    @Autowired
    private ConfigService configService;

    @RequestMapping(value = {"/terminal/member/file/saveMemberFaceImage"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiOperation(value = "上传人脸底库照", notes = "上传人脸底库照")
    public DataResponse<SaveImageResponse> saveMemberFaceImage(HttpServletRequest request) {
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
        DataResponse<SaveImageResponse> response = new DataResponse().success();
        response.setData(this.memberService.saveMemberFaceImage(file));
        return response;
    }

    @RequestMapping(value = {"/terminal/member/file/saveMemberFaceByDevice"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = false, dataType = "SaveMemberFaceRequest", paramType = "body")})
    @ApiOperation(value = "录入人脸，设备端调用", notes = "录入人脸，设备端调用")
    public DataResponse<SaveMemberFaceByDeviceResponse> saveMemberFaceByDevice(@RequestBody SaveMemberFaceRequest request) {
        logger.info("==============saveMemberFaceByDevice request :{}", JsonUtils.toJson(request));
        DataResponse<SaveMemberFaceByDeviceResponse> response = new DataResponse().success();
        request.setNeedSendMsg(true);
        request.setMemberJobNum(DesUtils.decrypt(request.getMemberJobNum()));
        try {
            SaveMemberFaceByDeviceResponse saveMemberFaceByDeviceResponse = this.memberService.saveMemberFaceByDevice(request);
            saveMemberFaceByDeviceResponse.setMemberName(DesUtils.encrypt(saveMemberFaceByDeviceResponse.getMemberName()));
            saveMemberFaceByDeviceResponse.setMemberMobile(DesUtils.encrypt(saveMemberFaceByDeviceResponse.getMemberMobile()));
            response.setData(saveMemberFaceByDeviceResponse);
        } catch (BizException e) {
            response.setCode(e.getErrorCode());
            response.setMessage(e.getMessage());
            if (OnpremiseErrorEnum.REPEAT_FACE_ERROR.getErrorCode().equals(e.getErrorCode())) {
                Member member = this.memberMapper.getMemberInfoByMemberId(Long.valueOf(Long.parseLong(e.getMessage())), request.getOrgId());
                response.setMessage(member.getMemberJobNum());
            }
        }
        return response;
    }

    @RequestMapping(value = {"/terminal/device/device/activeDevice"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "ActiveDeviceRequest", paramType = "body")})
    @ApiOperation(value = "设备激活，设备端调用", notes = "设备激活")
    public DataResponse<ActiveDeviceResponse> activeDevice(@RequestBody ActiveDeviceRequest request) {
        DataResponse<ActiveDeviceResponse> response = new DataResponse().success();
        response.setData(this.deviceService.activeDevice(request));
        return response;
    }

    @RequestMapping(value = {"/terminal/record/verify/saveRecord"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "VerifyRecordSaveRequest", paramType = "body")})
    @ApiOperation(value = "保存识别记录，设备端调用", notes = "保存识别记录")
    public DataResponse<List<VerifyRecordSaveResponse>> saveRecord(@RequestBody VerifyRecordSaveRequest request) {
        DataResponse<List<VerifyRecordSaveResponse>> response = new DataResponse().success();
        List<VerifyRecordSaveResponse> verifyRecordSaveResponse = this.verifyRecordService.saveRecord(request);
        response.setData(verifyRecordSaveResponse);
        return response;
    }

    @RequestMapping(value = {"/terminal/record/verify/batchSaveRecord"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "VerifyRecordBatchSaveRequest", paramType = "body")})
    @ApiOperation(value = "批量保存识别记录，设备端调用", notes = "批量保存识别记录")
    public CommonResponse batchSaveRecord(@RequestBody VerifyRecordBatchSaveRequest request) {
        logger.info("==============batch save record verify request :{}", Integer.valueOf(request.getSaveRequests().size()));
        return this.verifyRecordService.batchSaveRecord(request) ? new CommonResponse().success() : new CommonResponse().fail();
    }

    @RequestMapping(value = {"/terminal/record/warn/saveRecord"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "WarnRecordSaveRequest", paramType = "body")})
    @ApiOperation(value = "保存报警记录，设备端调用", notes = "保存报警记录")
    public CommonResponse saveRecord(@RequestBody WarnRecordSaveRequest request) {
        return this.warnRecordService.saveRecord(request) ? new CommonResponse().success() : new CommonResponse().fail();
    }

    @RequestMapping(value = {"/terminal/record/warn/batchSaveRecord"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "WarnRecordBatchSaveRequest", paramType = "body")})
    @ApiOperation(value = "批量保存报警记录，设备端调用", notes = "批量保存报警记录")
    public CommonResponse batchSaveRecord(@RequestBody WarnRecordBatchSaveRequest request) {
        logger.info("==============batch save warn record request :{}", Integer.valueOf(request.getSaveRequests().size()));
        return this.warnRecordService.batchSaveRecord(request) ? new CommonResponse().success() : new CommonResponse().fail();
    }

    @RequestMapping(value = {"/terminal/member/member/getMemberByMemberNumbers"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "TerminalFindMemberRequest", paramType = "body")})
    @ApiOperation(value = "成员编号搜素员工，设备端调用", notes = "成员编号搜素员工")
    public DataResponse<MemberDetailsResponse> getMemberByMemberNumbers(@RequestBody TerminalFindMemberRequest request) {
        logger.info("find member request :{}", JsonUtils.toJson(request));
        DataResponse<MemberDetailsResponse> response = new DataResponse().success();
        request.setMemberJobNum(DesUtils.decrypt(request.getMemberJobNum()));
        MemberDetailsResponse memberDetailsResponse = this.memberService.getMemberByMemberNumbers(request);
        memberDetailsResponse.setMemberName(DesUtils.encrypt(memberDetailsResponse.getMemberName()));
        memberDetailsResponse.setMemberMobile(DesUtils.encrypt(memberDetailsResponse.getMemberMobile()));
        memberDetailsResponse.setMemberEmail(DesUtils.encrypt(memberDetailsResponse.getMemberEmail()));
        memberDetailsResponse.setMemberTelphone(DesUtils.encrypt(memberDetailsResponse.getMemberTelphone()));
        memberDetailsResponse.setMemberPosition(DesUtils.encrypt(memberDetailsResponse.getMemberPosition()));
        memberDetailsResponse.setMemberJobNum(DesUtils.encrypt(memberDetailsResponse.getMemberJobNum()));
        memberDetailsResponse.setMemberJoinTime(DesUtils.encrypt(memberDetailsResponse.getMemberJoinTime()));
        response.setData(memberDetailsResponse);
        return response;
    }

    @RequestMapping(value = {"/terminal/device/device/checkDeviceActive"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "TerminalCheckDeviceActiveRequest", paramType = "body")})
    @ApiOperation(value = "设备是否激活查询，设备端调用", notes = "设备是否激活查询")
    public DataResponse<TerminalCheckDeviceActiveResponse> checkDeviceActive(@RequestBody TerminalCheckDeviceActiveRequest request) {
        DataResponse<TerminalCheckDeviceActiveResponse> dataResponseFail;
        TerminalCheckDeviceActiveResponse response1 = this.deviceService.checkDeviceActive(request);
        if (response1.getActiveFlag().booleanValue()) {
            dataResponseFail = new DataResponse().success();
        } else {
            dataResponseFail = new DataResponse().fail("device_not_active_error", NettyErrorEnum.DEVICE_NOT_ACTIVE_ERROR.getDescription());
        }
        DataResponse<TerminalCheckDeviceActiveResponse> response = dataResponseFail;
        response.setData(response1);
        return response;
    }

    @RequestMapping(value = {"/terminal/user/user/terminalLogin"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "TerminalLoginRequest", paramType = "body")})
    @ApiOperation(value = "登录管理员界面，设备端调用", notes = "登录管理员界面")
    public DataResponse<Boolean> terminalLogin(@RequestBody TerminalLoginRequest request) {
        DataResponse<Boolean> response = new DataResponse().success();
        response.setData(Boolean.valueOf(this.accountService.terminalLogin(request)));
        return response;
    }

    @RequestMapping(value = {"/terminal/sync/group/syncGroup"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "TerminalSyncRequest", paramType = "body")})
    @ApiOperation(value = "权限组同步，设备端调用", notes = "权限组同步")
    public DataResponse<TerminalSyncResponse<TerminalSyncGroupResponse>> syncGroup(@RequestBody TerminalSyncRequest request) {
        logger.info("==============syncGroup request :{}", JsonUtils.toJson(request));
        DataResponse<TerminalSyncResponse<TerminalSyncGroupResponse>> response = new DataResponse().success();
        response.setData(this.groupService.syncGroup(request));
        logger.info("==============syncGroup response  ,delete : {}; insert : {}, modify : {}", Integer.valueOf(response.getData().getSyncDeleteResult().size()), Integer.valueOf(response.getData().getSyncInsertResult().size()), Integer.valueOf(response.getData().getSyncModifyResult().size()));
        return response;
    }

    @RequestMapping(value = {"/terminal/sync/attendance/syncAttendanceGroup"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "TerminalSyncRequest", paramType = "body")})
    @ApiOperation(value = "考勤组同步，设备端调用", notes = "考勤组同步")
    public DataResponse<TerminalSyncResponse<TerminalSyncAttendanceGroupResponse>> syncAttendanceGroup(@RequestBody TerminalSyncRequest request) {
        logger.info("==============syncAttendanceGroup request :{}", JsonUtils.toJson(request));
        DataResponse<TerminalSyncResponse<TerminalSyncAttendanceGroupResponse>> response = new DataResponse().success();
        response.setData(this.attendanceGroupService.syncAttendanceGroup(request));
        logger.info("==============syncAttendanceGroup response  ,delete : {}; insert : {}, modify : {}", Integer.valueOf(response.getData().getSyncDeleteResult().size()), Integer.valueOf(response.getData().getSyncInsertResult().size()), Integer.valueOf(response.getData().getSyncModifyResult().size()));
        return response;
    }

    @RequestMapping(value = {"/terminal/sync/attendance/syncAttendanceHoliday"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "TerminalSyncRequest", paramType = "body")})
    @ApiOperation(value = "节假日同步，设备端调用", notes = "节假日同步")
    public DataResponse<TerminalSyncResponse<TerminalSyncAttendanceHolidayResponse>> syncAttendanceHoliday(@RequestBody TerminalSyncRequest request) {
        logger.info("==============syncAttendanceHoliday request :{}", JsonUtils.toJson(request));
        DataResponse<TerminalSyncResponse<TerminalSyncAttendanceHolidayResponse>> response = new DataResponse().success();
        response.setData(this.attendanceHolidayService.syncAttendanceHoliday(request));
        logger.info("==============syncAttendanceHoliday response  ,delete : {}; insert : {}, modify : {}", Integer.valueOf(response.getData().getSyncDeleteResult().size()), Integer.valueOf(response.getData().getSyncInsertResult().size()), Integer.valueOf(response.getData().getSyncModifyResult().size()));
        return response;
    }

    @RequestMapping(value = {"/terminal/sync/member/syncMember"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "TerminalSyncRequest", paramType = "body")})
    @ApiOperation(value = "通讯录同步，设备端调用", notes = "通讯录同步")
    public DataResponse<TerminalSyncResponse<TerminalSyncMemberResponse>> syncMember(@RequestBody TerminalSyncRequest request) {
        logger.info("==============syncMember request :{}", JsonUtils.toJson(request));
        DataResponse<TerminalSyncResponse<TerminalSyncMemberResponse>> response = new DataResponse().success();
        Long start = Long.valueOf(System.currentTimeMillis());
        TerminalSyncResponse<TerminalSyncMemberResponse> terminalSyncResponse = this.memberService.syncMember(request);
        List<TerminalSyncMemberResponse> insertList = terminalSyncResponse.getSyncInsertResult();
        List<TerminalSyncMemberResponse> modifyList = terminalSyncResponse.getSyncModifyResult();
        List<TerminalSyncMemberResponse> deleteList = terminalSyncResponse.getSyncDeleteResult();
        for (TerminalSyncMemberResponse item : insertList) {
            item.setMemberName(DesUtils.encrypt(item.getMemberName()));
            item.setMemberJobNum(DesUtils.encrypt(item.getMemberJobNum()));
        }
        for (TerminalSyncMemberResponse item2 : modifyList) {
            item2.setMemberName(DesUtils.encrypt(item2.getMemberName()));
            item2.setMemberJobNum(DesUtils.encrypt(item2.getMemberJobNum()));
        }
        for (TerminalSyncMemberResponse item3 : deleteList) {
            item3.setMemberName(DesUtils.encrypt(item3.getMemberName()));
            item3.setMemberJobNum(DesUtils.encrypt(item3.getMemberJobNum()));
        }
        response.setData(terminalSyncResponse);
        logger.info("==============syncMember response ,delete : {}; insert : {}, modify : {}", Integer.valueOf(response.getData().getSyncDeleteResult().size()), Integer.valueOf(response.getData().getSyncInsertResult().size()), Integer.valueOf(response.getData().getSyncModifyResult().size()));
        logger.info("==============syncMember time :{}", Long.valueOf(System.currentTimeMillis() - start.longValue()));
        return response;
    }

    @RequestMapping(value = {"/terminal/sync/member/syncMemberEigenvalueValue"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "TerminalSyncEigenvalueValueRequest", paramType = "body")})
    @ApiOperation(value = "人脸特征值同步，设备端调用", notes = "人脸特征值同步")
    public DataResponse<List<TerminalSyncEigenvalueValueResponse>> syncMemberEigenvalueValue(@RequestBody TerminalSyncEigenvalueValueRequest request) {
        DataResponse<List<TerminalSyncEigenvalueValueResponse>> response = new DataResponse().success();
        logger.info("==============syncMemberEigenvalueValue request ,size : {}", Integer.valueOf(request.getMemberIds().size()));
        List<TerminalSyncEigenvalueValueResponse> terminalSyncEigenvalueValueResponseList = this.memberService.syncMemberEigenvalueValue(request);
        logger.info("==============syncMemberEigenvalueValue response ,size : {}", Integer.valueOf(terminalSyncEigenvalueValueResponseList.size()));
        response.setData(terminalSyncEigenvalueValueResponseList);
        return response;
    }

    @RequestMapping(value = {"/terminal/sync/account/syncAccount"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "TerminalSyncRequest", paramType = "body")})
    @ApiOperation(value = "管理员同步，设备端调用", notes = "管理员同步")
    public DataResponse<List<TerminalSyncAccountResponse>> syncAccount(@RequestBody TerminalSyncRequest request) {
        logger.info("==============syncAccount request :{}", JsonUtils.toJson(request));
        DataResponse<List<TerminalSyncAccountResponse>> response = new DataResponse().success();
        response.setData(this.accountService.syncAccount(request));
        logger.info("==============syncAccount response ,size : {};", Integer.valueOf(response.getData().size()));
        return response;
    }

    @RequestMapping(value = {"/terminal/device/log/devicePushLog"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiImplicitParams({@ApiImplicitParam(name = "file", value = "上传文件", required = true, dataType = "MultipartFile", paramType = "body"), @ApiImplicitParam(name = "deviceSn", value = "设备sn", required = true, dataType = "String", paramType = "body"), @ApiImplicitParam(name = "fileName", value = "文件名", required = true, dataType = "String", paramType = "body")})
    @ApiOperation(value = "推送日志文件，设备端调用", notes = "推送日志文件")
    public CommonResponse devicePushLog(@RequestParam("file") MultipartFile file, @RequestParam("deviceSn") String deviceSn, @RequestParam("fileName") String fileName) {
        CommonResponse response = new CommonResponse().success();
        this.deviceService.devicePushLog(new DevicePushLogRequest(deviceSn, fileName, file));
        return response;
    }

    @RequestMapping(value = {"/terminal/device/config/updateDeviceConfig"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "TerminalSaveDeviceConfigRequest", paramType = "body")})
    @ApiOperation(value = "修改设备名称", notes = "修改设备名称")
    public CommonResponse updateDeviceConfig(@RequestBody TerminalSaveDeviceConfigRequest request) {
        CommonResponse response = new CommonResponse().success();
        this.deviceService.terminalSaveDeviceConfig(request);
        return response;
    }

    @RequestMapping(value = {"/terminal/sync/checkIn/checkInTask"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "TerminalSyncCheckInTaskRequest", paramType = "body")})
    @ApiOperation(value = "签到任务同步，设备端调用", notes = "签到任务同步")
    public DataResponse<TerminalSyncResponse<TerminalSyncCheckInTaskResponse>> syncCheckInTask(@RequestBody TerminalSyncCheckInTaskRequest request) {
        logger.info("==============syncCheckInTask request :{}", JsonUtils.toJson(request));
        DataResponse<TerminalSyncResponse<TerminalSyncCheckInTaskResponse>> response = new DataResponse().success();
        Long start = Long.valueOf(System.currentTimeMillis());
        response.setData(this.checkInService.syncCheckInTask(request));
        logger.info("==============syncCheckInTask time :{}", Long.valueOf(System.currentTimeMillis() - start.longValue()));
        return response;
    }

    @RequestMapping(value = {"/terminal/group/checkPrivilege"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "TerminalCheckPrivilegeRequest", paramType = "body")})
    @ApiOperation(value = "在线鉴权，设备端调用", notes = "在线鉴权")
    public DataResponse<TerminalCheckPrivilegeResponse> bizCheck(@RequestBody TerminalCheckPrivilegeRequest request) {
        logger.info("=====> group/checkPrivilege 请求：{}:", JsonUtils.toJson(request));
        DataResponse<TerminalCheckPrivilegeResponse> response = new DataResponse().success();
        TerminalCheckPrivilegeResponse res = this.groupService.checkPrivilege(request);
        logger.info("=====> group/checkPrivilege结果:" + JsonUtils.toJson(res));
        response.setData(res);
        return response;
    }

    @RequestMapping(value = {"/terminal/sync/checkIn/syncCheckInMember"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "TerminalSyncRequest", paramType = "body")})
    @ApiOperation(value = "签到人员底库同步，设备端调用", notes = "签到人员底库同步")
    public DataResponse<TerminalSyncResponse<TerminalSyncMemberCheckInTaskResponse>> syncCheckInMember(@RequestBody TerminalSyncRequest request) {
        logger.info("==============syncCheckInMember request :{}", JsonUtils.toJson(request));
        DataResponse<TerminalSyncResponse<TerminalSyncMemberCheckInTaskResponse>> response = new DataResponse().success();
        Long start = Long.valueOf(System.currentTimeMillis());
        TerminalSyncResponse<TerminalSyncMemberCheckInTaskResponse> result = this.checkInService.syncCheckInMember(request);
        response.setData(result);
        logger.info("==============syncCheckInMember response ,delete : {}; insert : {}, modify : {}", Integer.valueOf(response.getData().getSyncDeleteResult().size()), Integer.valueOf(response.getData().getSyncInsertResult().size()), Integer.valueOf(response.getData().getSyncModifyResult().size()));
        logger.info("==============syncCheckInMember time :{}", Long.valueOf(System.currentTimeMillis() - start.longValue()));
        return response;
    }

    @RequestMapping(value = {"/terminal/group/qrCheck"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "TerminalQrCheckRequest", paramType = "body")})
    @ApiOperation(value = "在线鉴权，设备端调用", notes = "在线鉴权")
    public DataResponse<Map<String, Object>> qrCheck(@RequestBody TerminalQrCheckRequest request) {
        logger.info("request params :{},{},{}", request.getQrContent(), request.getDeviceSn(), request.getTimeStamp());
        DataResponse<Map<String, Object>> response = new DataResponse().success();
        Map<String, Object> params = new HashMap<>();
        params.put("tipText", "界面提示语");
        params.put("tipSpeech", "语音播报内容");
        params.put("openDoor", 1);
        response.setData(params);
        return response;
    }

    @RequestMapping(value = {"/terminal/meal/saveMealRecord"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "SaveMealRecordRequest", paramType = "body")})
    @ApiOperation(value = "保存就餐记录", notes = "保存就餐记录")
    public DataResponse<SaveMealRecordResponse> saveMealRecord(@RequestBody SaveMealRecordRequest request) throws BeansException {
        SaveMealRecordRequest recordRequest = new SaveMealRecordRequest();
        BeanUtils.copyProperties(request, recordRequest);
        recordRequest.setSnapFaceBase64("");
        logger.info("============>meal saveMealRecord request :{}", JsonUtils.toJson(recordRequest));
        DataResponse<SaveMealRecordResponse> response = new DataResponse().success();
        SaveMealRecordResponse res = this.mealRecordService.saveMealRecord(request);
        logger.info("============>meal saveMealRecord result {}", JsonUtils.toJson(res));
        response.setData(res);
        return response;
    }

    @RequestMapping(value = {"/terminal/sync/meal/canteen"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "TerminalSyncCanteenRequest", paramType = "body")})
    @ApiOperation(value = "团餐餐厅同步，设备端调用", notes = "团餐餐厅同步")
    public DataResponse<TerminalSyncCanteenResponse> syncCanteen(@RequestBody TerminalSyncCanteenRequest request) {
        logger.info("============>meal syncCanteen request :{}", JsonUtils.toJson(request));
        DataResponse<TerminalSyncCanteenResponse> response = new DataResponse().success();
        Long start = Long.valueOf(System.currentTimeMillis());
        response.setData(this.mealCanteenService.syncCanteen(request));
        logger.info("============>meal syncCanteen time :{}", Long.valueOf(System.currentTimeMillis() - start.longValue()));
        return response;
    }

    @RequestMapping(value = {"/terminal/sync/meal/canteenMember"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "TerminalSyncRequest", paramType = "body")})
    @ApiOperation(value = "团餐餐厅个性化底库同步，设备端调用", notes = "团餐餐厅个性化底库同步")
    public DataResponse<TerminalSyncResponse<TerminalSyncMemberMealCanteenResponse>> syncCanteenMember(@RequestBody TerminalSyncRequest request) {
        logger.info("============>meal syncCanteenMember request :{}", JsonUtils.toJson(request));
        DataResponse<TerminalSyncResponse<TerminalSyncMemberMealCanteenResponse>> response = new DataResponse().success();
        Long start = Long.valueOf(System.currentTimeMillis());
        response.setData(this.mealCanteenService.syncMealCanteenMember(request));
        logger.info("============>meal syncCanteenMember time :{}", Long.valueOf(System.currentTimeMillis() - start.longValue()));
        return response;
    }

    @RequestMapping(value = {"/terminal/member/save"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "TerminalSaveMemberRequest", paramType = "body")})
    @ApiOperation(value = "新增成员，设备端调用", notes = "新增成员")
    public DataResponse<Long> saveMember(@RequestBody TerminalSaveMemberRequest request) {
        logger.info("=====> 新增成员request:{}", JsonUtils.toJson(request));
        DataResponse<Long> response = new DataResponse().success();
        Long result = this.memberService.saveMemberByDevice(request);
        response.setData(result);
        logger.info("=====> 新增成员response:{}" + JsonUtils.toJson(response));
        return response;
    }

    @RequestMapping(value = {"/terminal/device/callbackTest"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "TerminalSaveMemberRequest", paramType = "body")})
    @ApiOperation(value = "设备状态事件回调，设备端调用", notes = "设备状态事件回调")
    public CommonResponse deviceCallbackTest(@RequestBody TerminalDeviceCallbackRequest request) {
        logger.info("=====> 设备状态事件回调 request:{}", JsonUtils.toJson(request));
        CommonResponse response = new CommonResponse().success();
        logger.info("=====> 设备状态事件回调response:{}" + JsonUtils.toJson(response));
        return response;
    }

    @RequestMapping(value = {"/terminal/dept/listDept"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "TerminalDeptRequest", paramType = "body")})
    @ApiOperation(value = "设备查询部门列表", notes = "设备查询部门列表")
    public DataResponse<TerminalDeptResponse> listDept(@RequestBody TerminalDeptRequest request) {
        DataResponse<TerminalDeptResponse> response = new DataResponse().success();
        response.setData(this.deptService.listDeptForDevice(request));
        logger.info("=====> 设备查询部门列表回调response:{}" + JsonUtils.toJson(response));
        return response;
    }

    @RequestMapping(value = {"/terminal/visit/add"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = false, dataType = "TerminalVisitRecordRequest", paramType = "body")})
    @ApiOperation(value = "访客登记，设备端调用", notes = "访客登记，设备端调用")
    public DataResponse<Long> addVisitByDevice(@RequestBody TerminalVisitRecordRequest request) {
        logger.info("==============TerminalVisitRecordRequest request :{}", JsonUtils.toJson(request));
        DataResponse<Long> response = new DataResponse().success();
        try {
            Long id = this.visitRecordService.addVisitRecord(request);
            response.setData(id);
        } catch (BizException e) {
            response.setCode(e.getErrorCode());
            response.setMessage(e.getMessage());
            if (OnpremiseErrorEnum.REPEAT_FACE_ERROR.getErrorCode().equals(e.getErrorCode())) {
                Member member = this.memberMapper.getMemberInfoByMemberId(Long.valueOf(Long.parseLong(e.getMessage())), request.getOrgId());
                response.setMessage(member.getMemberJobNum());
            }
        }
        return response;
    }

    @RequestMapping(value = {"/terminal/sync/externalContact"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "TerminalSyncRequest", paramType = "body")})
    @ApiOperation(value = "外部联系人同步，设备端调用", notes = "外部联系人同步")
    public DataResponse<TerminalSyncResponse<Long>> syncExternalContact(@RequestBody TerminalSyncRequest request) {
        logger.info("============>externalContact sync request :{}", JsonUtils.toJson(request));
        DataResponse<TerminalSyncResponse<Long>> response = new DataResponse().success();
        Long start = Long.valueOf(System.currentTimeMillis());
        response.setData(this.visitRecordService.syncExternalContact(request));
        logger.info("============>externalContact sync time :{}", Long.valueOf(System.currentTimeMillis() - start.longValue()));
        return response;
    }

    @RequestMapping(value = {"/terminal/sync/externalContact/detail"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "TerminalSyncExternalContactDetailRequest", paramType = "body")})
    @ApiOperation(value = "外部联系人特征值同步，设备端调用", notes = "外部联系人同步")
    public DataResponse<List<TerminalSyncExternalContactDetailResponse>> syncExternalContactDetail(@RequestBody TerminalSyncExternalContactDetailRequest request) {
        logger.info("============>externalContactDetail sync request :{}", JsonUtils.toJson(request));
        DataResponse<List<TerminalSyncExternalContactDetailResponse>> response = new DataResponse().success();
        Long start = Long.valueOf(System.currentTimeMillis());
        response.setData(this.visitRecordService.syncExternalContactDetail(request));
        logger.info("============>externalContactDetail sync time :{}", Long.valueOf(System.currentTimeMillis() - start.longValue()));
        return response;
    }

    @RequestMapping(value = {"/terminal/sync/visitConfig"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "TerminalSyncRequest", paramType = "body")})
    @ApiOperation(value = "访客配置同步，设备端调用", notes = "访客配置同步")
    public DataResponse<TerminalSyncResponse<TerminalSyncVisitConfigResponse>> syncVisitConfig(@RequestBody TerminalSyncRequest request) {
        logger.info("============>visitConfig sync request :{}", JsonUtils.toJson(request));
        DataResponse<TerminalSyncResponse<TerminalSyncVisitConfigResponse>> response = new DataResponse().success();
        Long start = Long.valueOf(System.currentTimeMillis());
        response.setData(this.visitConfigService.syncVisitConfig(request));
        logger.info("============>visitConfig sync time :{}", Long.valueOf(System.currentTimeMillis() - start.longValue()));
        return response;
    }

    @RequestMapping(value = {"/terminal/temperature/callbackTest"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "TerminalSaveMemberRequest", paramType = "body")})
    @ApiOperation(value = "测温记录回调，设备端调用", notes = "测温记录回调")
    public CommonResponse temperatureCallbackTest(@RequestBody TerminalTemperatureCallbackRequest request) {
        logger.info("=====> 测温记录回调 request:{}", JsonUtils.toJson(request));
        CommonResponse response = new CommonResponse().success();
        logger.info("=====> 测温记录回调:{}" + JsonUtils.toJson(response));
        return response;
    }

    @RequestMapping(value = {"/terminal/sync/device/config"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "TerminalSyncRequest", paramType = "body")})
    @ApiOperation(value = "设备相关配置同步，设备端调用", notes = "设备相关配置同步")
    public DataResponse<TerminalSyncDeviceConfigResponse> syncDeviceConfig(@RequestBody TerminalSyncRequest request) throws BeansException {
        logger.info("==============syncDeviceConfig request :{}", JsonUtils.toJson(request));
        DataResponse<TerminalSyncDeviceConfigResponse> response = new DataResponse().success();
        TerminalSyncDeviceConfigResponse deviceConfigResponse = new TerminalSyncDeviceConfigResponse();
        DeviceConfigResponse deviceConfig = this.deviceService.getDeviceConfig(request.getDeviceSn(), request.getOrgId());
        Long lastModifyTime = Long.valueOf(deviceConfig.getGmtModify().getTime());
        BeanUtils.copyProperties(deviceConfig, deviceConfigResponse);
        QueryConfigResponse snapMode = this.configService.getOneByKey(ConfigConstants.SNAP_MODE_KEY);
        if (snapMode != null) {
            deviceConfigResponse.setSnapMode(Integer.valueOf(snapMode.getConfigValue()));
            lastModifyTime = Long.valueOf(snapMode.getGmtModify().getTime() > lastModifyTime.longValue() ? snapMode.getGmtModify().getTime() : lastModifyTime.longValue());
        }
        QueryConfigResponse strangerFrame = this.configService.getOneByKey(ConfigConstants.STRANGER_FRAME_KEY);
        if (strangerFrame != null) {
            deviceConfigResponse.setStrangerFrame(Integer.valueOf(strangerFrame.getConfigValue()));
            lastModifyTime = Long.valueOf(strangerFrame.getGmtModify().getTime() > lastModifyTime.longValue() ? strangerFrame.getGmtModify().getTime() : lastModifyTime.longValue());
        }
        QueryConfigResponse strangerShowReminderInfo = this.configService.getOneByKey(ConfigConstants.STRANGER_SHOW_REMINDER_INFO_KEY);
        if (strangerShowReminderInfo != null) {
            deviceConfigResponse.setShowReminderInfo(strangerShowReminderInfo.getConfigValue());
            lastModifyTime = Long.valueOf(strangerShowReminderInfo.getGmtModify().getTime() > lastModifyTime.longValue() ? strangerShowReminderInfo.getGmtModify().getTime() : lastModifyTime.longValue());
        }
        QueryConfigResponse strangerSpeechRemindInfo = this.configService.getOneByKey(ConfigConstants.STRANGER_SPEECH_REMINDER_INFO_KEY);
        if (strangerSpeechRemindInfo != null) {
            deviceConfigResponse.setSpeechReminderInfo(strangerSpeechRemindInfo.getConfigValue());
            lastModifyTime = Long.valueOf(strangerSpeechRemindInfo.getGmtModify().getTime() > lastModifyTime.longValue() ? strangerSpeechRemindInfo.getGmtModify().getTime() : lastModifyTime.longValue());
        }
        if (lastModifyTime.longValue() != deviceConfig.getGmtModify().getTime()) {
            UpdateDeviceRequest updateRequest = new UpdateDeviceRequest();
            updateRequest.setDeviceSn(request.getDeviceSn());
            this.deviceService.updateDevice(updateRequest);
        }
        deviceConfigResponse.setSyncTime(lastModifyTime);
        response.setData(deviceConfigResponse);
        return response;
    }

    @RequestMapping(value = {"/terminal/member/search"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "TerminalSearchMemberRequest", paramType = "body")})
    @ApiOperation(value = "搜素员工，设备端调用", notes = "搜素员工")
    public DataResponse<MemberDetailsResponse> searchMember(@RequestBody TerminalSearchMemberRequest request) {
        logger.info("find member request :{}", JsonUtils.toJson(request));
        DataResponse<MemberDetailsResponse> response = new DataResponse().success();
        request.setIdentityCard(DesUtils.decrypt(request.getIdentityCard()));
        MemberDetailsResponse memberDetailsResponse = this.memberService.getMemberByIdentityCard(request);
        memberDetailsResponse.setMemberName(DesUtils.encrypt(memberDetailsResponse.getMemberName()));
        memberDetailsResponse.setMemberMobile(DesUtils.encrypt(memberDetailsResponse.getMemberMobile()));
        memberDetailsResponse.setMemberEmail(DesUtils.encrypt(memberDetailsResponse.getMemberEmail()));
        memberDetailsResponse.setMemberTelphone(DesUtils.encrypt(memberDetailsResponse.getMemberTelphone()));
        memberDetailsResponse.setMemberPosition(DesUtils.encrypt(memberDetailsResponse.getMemberPosition()));
        memberDetailsResponse.setMemberJobNum(DesUtils.encrypt(memberDetailsResponse.getMemberJobNum()));
        memberDetailsResponse.setMemberJoinTime(DesUtils.encrypt(memberDetailsResponse.getMemberJoinTime()));
        response.setData(memberDetailsResponse);
        return response;
    }
}
