package com.moredian.onpremise.checkIn.service.impl;

import com.github.pagehelper.PageHelper;
import com.moredian.onpremise.api.checkIn.CheckInService;
import com.moredian.onpremise.api.member.DeptService;
import com.moredian.onpremise.core.adapter.CacheAdapter;
import com.moredian.onpremise.core.common.constants.AuthConstants;
import com.moredian.onpremise.core.common.constants.Constants;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import com.moredian.onpremise.core.common.enums.AppTypeEnum;
import com.moredian.onpremise.core.common.enums.CheckInLogStatusEnum;
import com.moredian.onpremise.core.common.enums.CheckInTaskCycleEnum;
import com.moredian.onpremise.core.common.enums.CheckInTaskMemberTypeEnum;
import com.moredian.onpremise.core.common.enums.CheckInTaskNotifyTypeEnum;
import com.moredian.onpremise.core.common.enums.CheckInTaskStatusEnum;
import com.moredian.onpremise.core.common.enums.CommonStatusEnum;
import com.moredian.onpremise.core.common.enums.DeleteOrNotEnum;
import com.moredian.onpremise.core.common.enums.MemberTypeEnum;
import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.config.UploadConfig;
import com.moredian.onpremise.core.exception.BizException;
import com.moredian.onpremise.core.mapper.CheckInLogMapper;
import com.moredian.onpremise.core.mapper.CheckInSupplementMapper;
import com.moredian.onpremise.core.mapper.CheckInTaskDeviceMapper;
import com.moredian.onpremise.core.mapper.CheckInTaskLogMapper;
import com.moredian.onpremise.core.mapper.CheckInTaskMapper;
import com.moredian.onpremise.core.mapper.CheckInTaskMemberMapper;
import com.moredian.onpremise.core.mapper.DeptMapper;
import com.moredian.onpremise.core.mapper.DeviceMapper;
import com.moredian.onpremise.core.mapper.MemberCheckInTaskSyncMapper;
import com.moredian.onpremise.core.mapper.MemberMapper;
import com.moredian.onpremise.core.model.domain.CheckInLog;
import com.moredian.onpremise.core.model.domain.CheckInSupplement;
import com.moredian.onpremise.core.model.domain.CheckInTask;
import com.moredian.onpremise.core.model.domain.CheckInTaskDevice;
import com.moredian.onpremise.core.model.domain.CheckInTaskLog;
import com.moredian.onpremise.core.model.domain.CheckInTaskMember;
import com.moredian.onpremise.core.model.domain.Dept;
import com.moredian.onpremise.core.model.domain.Device;
import com.moredian.onpremise.core.model.domain.Member;
import com.moredian.onpremise.core.model.domain.MemberCheckInTaskSync;
import com.moredian.onpremise.core.model.dto.CheckInDeviceDto;
import com.moredian.onpremise.core.model.dto.CheckInMemberDto;
import com.moredian.onpremise.core.model.dto.CheckInTaskMemberDto;
import com.moredian.onpremise.core.model.dto.DeviceDto;
import com.moredian.onpremise.core.model.info.DeviceLastModifyTimeInfo;
import com.moredian.onpremise.core.model.request.CheckInLogListRequest;
import com.moredian.onpremise.core.model.request.CheckInLogRequest;
import com.moredian.onpremise.core.model.request.CheckInTaskListRequest;
import com.moredian.onpremise.core.model.request.CheckInTaskOpenOrCloseRequest;
import com.moredian.onpremise.core.model.request.DeleteCheckInSupplementRequest;
import com.moredian.onpremise.core.model.request.ListCheckInSupplementRequest;
import com.moredian.onpremise.core.model.request.QueryCheckInSupplementDetailRequest;
import com.moredian.onpremise.core.model.request.SaveCheckInSupplementRequest;
import com.moredian.onpremise.core.model.request.SaveCheckInTaskRequest;
import com.moredian.onpremise.core.model.request.TerminalSyncCheckInTaskRequest;
import com.moredian.onpremise.core.model.request.TerminalSyncRequest;
import com.moredian.onpremise.core.model.response.BizCheckResponse;
import com.moredian.onpremise.core.model.response.CheckInLogDayListResponse;
import com.moredian.onpremise.core.model.response.CheckInLogListResponse;
import com.moredian.onpremise.core.model.response.CheckInLogMemberListResponse;
import com.moredian.onpremise.core.model.response.CheckInLogResponse;
import com.moredian.onpremise.core.model.response.CheckInResponse;
import com.moredian.onpremise.core.model.response.CheckInSupplementResponse;
import com.moredian.onpremise.core.model.response.CheckInTaskListResponse;
import com.moredian.onpremise.core.model.response.CheckInTaskResponse;
import com.moredian.onpremise.core.model.response.DeptMemberListResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncCheckInTaskMemberResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncCheckInTaskResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncMemberCheckInTaskResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncResponse;
import com.moredian.onpremise.core.utils.AssertUtil;
import com.moredian.onpremise.core.utils.CacheKeyGenerateUtils;
import com.moredian.onpremise.core.utils.JsonUtils;
import com.moredian.onpremise.core.utils.MyDateUtils;
import com.moredian.onpremise.core.utils.MyFileUtils;
import com.moredian.onpremise.core.utils.MyListUtils;
import com.moredian.onpremise.core.utils.PageList;
import com.moredian.onpremise.core.utils.Paginator;
import com.moredian.onpremise.iot.handle.NettyMessageApi;
import com.moredian.onpremise.model.SyncCheckInTaskMemberRequest;
import com.moredian.onpremise.model.SyncCheckInTaskRequest;
import com.moredian.onpremise.model.SyncMemberRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.tags.BindTag;

@Service
/* loaded from: onpremise-checkIn-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/checkIn/service/impl/CheckInServiceImpl.class */
public class CheckInServiceImpl implements CheckInService {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) CheckInServiceImpl.class);

    @Autowired
    private CheckInTaskMapper checkInTaskMapper;

    @Autowired
    private CheckInTaskMemberMapper checkInTaskMemberMapper;

    @Autowired
    private CheckInTaskDeviceMapper checkInTaskDeviceMapper;

    @Autowired
    private CheckInTaskLogMapper checkInTaskLogMapper;

    @Autowired
    private CheckInLogMapper checkInLogMapper;

    @Autowired
    private CheckInSupplementMapper checkInSupplementMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private NettyMessageApi nettyMessageApi;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private MemberCheckInTaskSyncMapper memberCheckInTaskSyncMapper;

    @Autowired
    private DeptService deptService;

    @Autowired
    private UploadConfig uploadConfig;

    @Override // com.moredian.onpremise.api.checkIn.CheckInService
    @Transactional(rollbackFor = {Exception.class})
    public Long insertCheckInTask(SaveCheckInTaskRequest request) throws BeansException {
        Date now = new Date();
        checkSaveTaskParams(request);
        checkDeviceTimeInvalid(request);
        CheckInTask checkInTask = new CheckInTask();
        BeanUtils.copyProperties(request, checkInTask);
        checkInTask.setDeleteFlag(Integer.valueOf(CommonStatusEnum.NO.getValue()));
        checkInTask.setGmtCreate(now);
        checkInTask.setGmtModify(now);
        this.checkInTaskMapper.insertCheckInTask(checkInTask);
        if (request.getAllUser().equals(Integer.valueOf(CommonStatusEnum.NO.getValue()))) {
            Set deptIdSet = new HashSet();
            Set memberIdSet = new HashSet();
            List<CheckInTaskMember> checkInTaskMemberList = new ArrayList<>();
            List<CheckInTaskMemberDto> saveCheckInTaskMemberRequestList = request.getTaskMembers();
            ArrayList arrayList = new ArrayList();
            for (CheckInTaskMemberDto item : saveCheckInTaskMemberRequestList) {
                item.setConfirmFlag(1);
                if (item.getType().equals(Integer.valueOf(MemberTypeEnum.DEPT.getValue()))) {
                    this.deptService.packagingChildDept(item.getDeptId(), request.getOrgId(), arrayList);
                }
            }
            HashSet hashSet = new HashSet();
            List<Long> noRepeatChildDeptIds = new ArrayList<>();
            hashSet.addAll(arrayList);
            noRepeatChildDeptIds.addAll(hashSet);
            for (Long deptId : noRepeatChildDeptIds) {
                CheckInTaskMemberDto checkInTaskMemberDto = new CheckInTaskMemberDto();
                checkInTaskMemberDto.setType(Integer.valueOf(MemberTypeEnum.DEPT.getValue()));
                checkInTaskMemberDto.setDeptId(deptId);
                checkInTaskMemberDto.setMemberId(0L);
                checkInTaskMemberDto.setConfirmFlag(0);
                if (!saveCheckInTaskMemberRequestList.contains(checkInTaskMemberDto)) {
                    saveCheckInTaskMemberRequestList.add(checkInTaskMemberDto);
                }
            }
            if (!CollectionUtils.isEmpty(saveCheckInTaskMemberRequestList)) {
                for (CheckInTaskMemberDto item2 : saveCheckInTaskMemberRequestList) {
                    CheckInTaskMember checkInTaskMember = new CheckInTaskMember();
                    checkInTaskMember.setOrgId(request.getOrgId());
                    checkInTaskMember.setTaskId(checkInTask.getId());
                    checkInTaskMember.setType(item2.getType());
                    checkInTaskMember.setDeptId(item2.getDeptId());
                    checkInTaskMember.setMemberId(item2.getMemberId());
                    checkInTaskMember.setStatus(Integer.valueOf(CommonStatusEnum.NO.getValue()));
                    checkInTaskMember.setDeleteFlag(Integer.valueOf(CommonStatusEnum.NO.getValue()));
                    checkInTaskMember.setConfirmFlag(item2.getConfirmFlag());
                    checkInTaskMember.setGmtCreate(now);
                    checkInTaskMember.setGmtModify(now);
                    checkInTaskMemberList.add(checkInTaskMember);
                    if (checkInTaskMember.getType().equals(Integer.valueOf(MemberTypeEnum.DEPT.getValue()))) {
                        deptIdSet.add(checkInTaskMember.getDeptId());
                    } else {
                        memberIdSet.add(checkInTaskMember.getMemberId());
                    }
                }
                this.checkInTaskMemberMapper.insertBatch(checkInTaskMemberList);
                if (!CollectionUtils.isEmpty(memberIdSet) || !CollectionUtils.isEmpty(deptIdSet)) {
                    this.memberMapper.updateModifyTime(request.getOrgId(), new ArrayList(memberIdSet), new ArrayList(deptIdSet));
                }
            }
        }
        List<CheckInTaskDevice> checkInTaskDeviceList = new ArrayList<>();
        List<String> deviceSns = new ArrayList<>();
        List<CheckInDeviceDto> taskDevices = request.getTaskDevices();
        if (!CollectionUtils.isEmpty(taskDevices)) {
            for (CheckInDeviceDto item3 : taskDevices) {
                CheckInTaskDevice checkInTaskDevice = new CheckInTaskDevice();
                checkInTaskDevice.setOrgId(request.getOrgId());
                checkInTaskDevice.setTaskId(checkInTask.getId());
                checkInTaskDevice.setDeviceId(this.deviceMapper.getDeviceInfoByDeviceSn(item3.getDeviceSn()).getDeviceId());
                checkInTaskDevice.setDeviceSn(item3.getDeviceSn());
                checkInTaskDevice.setDeleteFlag(Integer.valueOf(CommonStatusEnum.NO.getValue()));
                checkInTaskDevice.setGmtCreate(now);
                checkInTaskDevice.setGmtModify(now);
                checkInTaskDeviceList.add(checkInTaskDevice);
                deviceSns.add(item3.getDeviceSn());
            }
            this.checkInTaskDeviceMapper.insertBatch(checkInTaskDeviceList);
        }
        if (request.getStatus().equals(Integer.valueOf(CommonStatusEnum.YES.getValue()))) {
            if (!CollectionUtils.isEmpty(deviceSns)) {
                doSendNettyMessage(Long.valueOf(now.getTime()), request.getOrgId(), deviceSns);
                doSendMemberNetty(request.getOrgId());
            }
            generateCheckInLog(checkInTask);
        }
        return checkInTask.getId();
    }

    private void checkDeviceTimeInvalid(SaveCheckInTaskRequest request) {
        Long orgId = request.getOrgId();
        List<CheckInDeviceDto> checkInTaskDeviceListNew = request.getTaskDevices();
        Integer cycleNew = request.getCycle();
        String cycleExtraNew = request.getCycleExtra();
        String startTimeNew = request.getStartTime();
        String endTimeNew = request.getEndTime();
        new ArrayList();
        List<String> deviceSns = new ArrayList<>();
        for (CheckInDeviceDto checkInDeviceDto : checkInTaskDeviceListNew) {
            deviceSns.add(checkInDeviceDto.getDeviceSn());
        }
        List<CheckInTask> checkInTaskList = this.checkInTaskMapper.getCheckInTaskByDevices(orgId, deviceSns);
        if (CollectionUtils.isEmpty(checkInTaskList)) {
            return;
        }
        for (CheckInTask checkInTask : checkInTaskList) {
            if (!checkInTask.getId().equals(request.getId())) {
                Integer cycle = checkInTask.getCycle();
                if (!cycle.equals(Integer.valueOf(CheckInTaskCycleEnum.EVERYDAY.getValue())) && !cycleNew.equals(Integer.valueOf(CheckInTaskCycleEnum.EVERYDAY.getValue()))) {
                    if (cycle.equals(Integer.valueOf(CheckInTaskCycleEnum.SPECIAL.getValue()))) {
                        if (cycleNew.equals(Integer.valueOf(CheckInTaskCycleEnum.SPECIAL.getValue()))) {
                            String cycleExtra = checkInTask.getCycleExtra();
                            List<Integer> cycleExtraList = JsonUtils.jsoncastListType(Integer.class, cycleExtra);
                            List<Integer> cycleExtraNewList = JsonUtils.jsoncastListType(Integer.class, cycleExtraNew);
                            if (!cycleExtraList.removeAll(cycleExtraNewList)) {
                                continue;
                            }
                        } else if (cycleNew.equals(Integer.valueOf(CheckInTaskCycleEnum.SINGLE.getValue()))) {
                            String cycleExtra2 = checkInTask.getCycleExtra();
                            List<Integer> cycleExtraList2 = JsonUtils.jsoncastListType(Integer.class, cycleExtra2);
                            Calendar calendar = Calendar.getInstance();
                            String[] cycleExtraArr = cycleExtraNew.split("-");
                            calendar.set(1, Integer.parseInt(cycleExtraArr[0]));
                            calendar.set(2, Integer.parseInt(cycleExtraArr[1]));
                            calendar.set(5, Integer.parseInt(cycleExtraArr[2]));
                            if (!cycleExtraList2.contains(Integer.valueOf(calendar.get(7)))) {
                                continue;
                            }
                        }
                    } else if (cycle.equals(Integer.valueOf(CheckInTaskCycleEnum.SINGLE.getValue()))) {
                        if (cycleNew.equals(Integer.valueOf(CheckInTaskCycleEnum.SPECIAL.getValue()))) {
                            String cycleExtra3 = checkInTask.getCycleExtra();
                            List<Integer> cycleExtraNewList2 = JsonUtils.jsoncastListType(Integer.class, cycleExtraNew);
                            Calendar calendar2 = Calendar.getInstance();
                            String[] cycleExtraArr2 = cycleExtra3.split("-");
                            calendar2.set(1, Integer.parseInt(cycleExtraArr2[0]));
                            calendar2.set(2, Integer.parseInt(cycleExtraArr2[1]));
                            calendar2.set(5, Integer.parseInt(cycleExtraArr2[2]));
                            if (!cycleExtraNewList2.contains(cycleExtra3)) {
                                continue;
                            }
                        } else if (!cycleNew.equals(Integer.valueOf(CheckInTaskCycleEnum.SINGLE.getValue())) || cycleExtraNew.equals(checkInTask.getCycleExtra())) {
                        }
                    }
                }
                if (endTimeNew.compareTo(checkInTask.getStartTime()) >= 0 && checkInTask.getEndTime().compareTo(startTimeNew) >= 0) {
                    throw new BizException(OnpremiseErrorEnum.CHECK_IN_TASK_DEVICE_TIME_REPEAT.getErrorCode(), String.format(OnpremiseErrorEnum.CHECK_IN_TASK_DEVICE_TIME_REPEAT.getMessage(), checkInTask.getName()));
                }
            }
        }
    }

    private void generateCheckInLog(CheckInTask checkInTask) {
        if (checkInTask.getStatus().equals(Integer.valueOf(CommonStatusEnum.NO.getValue()))) {
        }
        int cycle = checkInTask.getCycle().intValue();
        Calendar cal = Calendar.getInstance();
        Calendar calEnd = Calendar.getInstance();
        String[] endTimeArr = checkInTask.getEndTime().split(":");
        calEnd.set(11, Integer.parseInt(endTimeArr[0]));
        calEnd.set(12, Integer.parseInt(endTimeArr[1]));
        calEnd.set(13, 59);
        if (cal.before(calEnd)) {
            switch (cycle) {
                case 1:
                    insertCheckInTaskLog(checkInTask);
                    break;
                case 2:
                    int temp = cal.get(7);
                    List<Integer> cycleList = JsonUtils.jsoncastListType(Integer.class, checkInTask.getCycleExtra());
                    if (cycleList.contains(Integer.valueOf(temp))) {
                        insertCheckInTaskLog(checkInTask);
                        break;
                    }
                    break;
                case 3:
                    String nowDate = MyDateUtils.getDate();
                    if (nowDate.equals(checkInTask.getCycleExtra())) {
                        insertCheckInTaskLog(checkInTask);
                        break;
                    }
                    break;
                default:
                    logger.error("任务周期类型有误！任务id = " + checkInTask.getId() + "， 任务名称 = " + checkInTask.getName());
                    break;
            }
        }
    }

    private void doSendNettyMessage(Long now, Long orgId, List<String> deviceSns) {
        if (MyListUtils.listIsEmpty(deviceSns)) {
            for (String deviceSn : deviceSns) {
                logger.info("====================send deviceSn:{}", deviceSn);
                this.nettyMessageApi.sendMsg(new SyncCheckInTaskRequest(now, orgId), Integer.valueOf(SyncCheckInTaskRequest.MODEL_TYPE.type()), deviceSn);
            }
        }
    }

    @Override // com.moredian.onpremise.api.checkIn.CheckInService
    public void doSendNettyMessageForSyncMember(Long orgId) {
        Date date = this.memberCheckInTaskSyncMapper.getLastModify(orgId);
        List<String> deviceSnList = this.deviceMapper.listDeviceSn(orgId);
        if (CollectionUtils.isEmpty(deviceSnList)) {
            return;
        }
        this.nettyMessageApi.sendMsg(new SyncCheckInTaskMemberRequest(Long.valueOf(date == null ? 0L : date.getTime()), orgId), Integer.valueOf(SyncCheckInTaskMemberRequest.MODEL_TYPE.type()), deviceSnList);
    }

    @Override // com.moredian.onpremise.api.checkIn.CheckInService
    public PageList<CheckInSupplementResponse> listCheckInSupplementResponse(ListCheckInSupplementRequest request) {
        Paginator paginator = request.getPaginator();
        if (Paginator.checkPaginator(paginator)) {
            PageHelper.startPage(paginator.getPageNum(), paginator.getPageSize());
            return new PageList<>(this.checkInSupplementMapper.pageFind(request));
        }
        List<CheckInSupplementResponse> responses = this.checkInSupplementMapper.pageFind(request);
        return new PageList<>(Paginator.initPaginator(responses), responses);
    }

    @Override // com.moredian.onpremise.api.checkIn.CheckInService
    public CheckInSupplementResponse queryCheckInSupplementResponse(QueryCheckInSupplementDetailRequest request) {
        AssertUtil.isNullOrEmpty(request.getSupplementId(), OnpremiseErrorEnum.CHECK_IN_TASK_ID_MUST_NOT_NULL);
        CheckInSupplementResponse response = this.checkInSupplementMapper.findById(request.getSupplementId(), request.getOrgId());
        if (response != null) {
            response.setMemberIds(Arrays.asList(response.getMemberId()));
        }
        return response;
    }

    @Override // com.moredian.onpremise.api.checkIn.CheckInService
    @Transactional(rollbackFor = {Exception.class})
    public void saveCheckInSupplement(SaveCheckInSupplementRequest request) throws BeansException {
        AssertUtil.isNullOrEmpty(request.getTaskId(), OnpremiseErrorEnum.CHECK_IN_TASK_ID_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getMemberIds(), OnpremiseErrorEnum.MEMBER_ID_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(this.checkInTaskMapper.findById(request.getOrgId(), request.getTaskId()), OnpremiseErrorEnum.CHECK_IN_TASK_NOT_EXITS);
        if (request.getSupplementId() != null) {
            for (Long memberId : request.getMemberIds()) {
                CheckInSupplement supplement = new CheckInSupplement();
                BeanUtils.copyProperties(request, supplement);
                supplement.setMemberId(memberId);
                supplement.setSupplementTime(new Date(request.getSupplementTime().longValue()));
                supplement.setId(request.getSupplementId());
                this.checkInSupplementMapper.update(supplement);
            }
            return;
        }
        for (Long memberId2 : request.getMemberIds()) {
            CheckInSupplement supplement2 = new CheckInSupplement();
            BeanUtils.copyProperties(request, supplement2);
            supplement2.setMemberId(memberId2);
            supplement2.setSupplementTime(new Date(request.getSupplementTime().longValue()));
            this.checkInSupplementMapper.insert(supplement2);
            CheckInLog findCond = new CheckInLog();
            findCond.setOrgId(request.getOrgId());
            findCond.setTaskId(request.getTaskId());
            findCond.setTaskTime(MyDateUtils.formatDate(supplement2.getSupplementTime(), new Object[0]));
            findCond.setMemberId(memberId2);
            CheckInLog checkInLog = this.checkInLogMapper.findByCond(findCond);
            checkInLog.setStatus(Integer.valueOf(checkInLog.getStatus().intValue() == CheckInLogStatusEnum.NO.getValue() ? CheckInLogStatusEnum.YES.getValue() : checkInLog.getStatus().intValue()));
            this.checkInLogMapper.checkIn(checkInLog);
        }
    }

    @Override // com.moredian.onpremise.api.checkIn.CheckInService
    @Transactional(rollbackFor = {Exception.class})
    public void deleteCheckInSupplement(DeleteCheckInSupplementRequest request) {
        AssertUtil.isNullOrEmpty(request.getSupplementId(), OnpremiseErrorEnum.CHECK_IN_TASK_ID_MUST_NOT_NULL);
        CheckInSupplementResponse supplement = this.checkInSupplementMapper.findById(request.getSupplementId(), request.getOrgId());
        this.checkInSupplementMapper.delete(request.getSupplementId());
        CheckInLog findCond = new CheckInLog();
        findCond.setOrgId(request.getOrgId());
        findCond.setTaskId(supplement.getTaskId());
        findCond.setTaskTime(MyDateUtils.formatDate(new Date(supplement.getSupplementTime().longValue()), new Object[0]));
        findCond.setMemberId(supplement.getMemberId());
        CheckInLog checkInLog = this.checkInLogMapper.findByCond(findCond);
        checkInLog.setStatus(Integer.valueOf(checkInLog.getStatus().intValue() == CheckInLogStatusEnum.YES.getValue() ? CheckInLogStatusEnum.NO.getValue() : checkInLog.getStatus().intValue()));
        this.checkInLogMapper.checkIn(checkInLog);
    }

    private void doSendMemberNetty(Long orgId) {
        Member member = this.memberMapper.getLastModify(orgId);
        List<String> deviceSnList = this.deviceMapper.listDeviceSn(orgId);
        if (CollectionUtils.isEmpty(deviceSnList)) {
            return;
        }
        this.nettyMessageApi.sendMsg(new SyncMemberRequest(Long.valueOf(member == null ? 0L : member.getGmtModify().getTime()), orgId), Integer.valueOf(SyncMemberRequest.MODEL_TYPE.type()), deviceSnList);
    }

    private void checkSaveTaskParams(SaveCheckInTaskRequest request) {
        AssertUtil.isNullOrEmpty(request.getOrgId(), OnpremiseErrorEnum.CHECK_IN_TASK_ORGID_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getName(), OnpremiseErrorEnum.CHECK_IN_TASK_NAME_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getAllUser(), OnpremiseErrorEnum.CHECK_IN_TASK_ALLUSER_MUST_NOT_NULL);
        if (request.getAllUser().equals(Integer.valueOf(CommonStatusEnum.NO.getValue()))) {
            AssertUtil.isTrue(Boolean.valueOf(!CollectionUtils.isEmpty(request.getTaskMembers())), OnpremiseErrorEnum.CHECK_IN_TASK_MEMBERS_MUST_NOT_NULL);
        }
        AssertUtil.isNullOrEmpty(request.getAllDay(), OnpremiseErrorEnum.CHECK_IN_TASK_ALLDAY_MUST_NOT_NULL);
        if (request.getAllDay().equals(Integer.valueOf(CommonStatusEnum.NO.getValue()))) {
            AssertUtil.isTrue(Boolean.valueOf((StringUtils.isEmpty(request.getStartTime()) || StringUtils.isEmpty(request.getEndTime())) ? false : true), OnpremiseErrorEnum.CHECK_IN_TASK_TIME_MUST_NOT_NULL);
            AssertUtil.isTrue(Boolean.valueOf(request.getStartTime().compareTo(request.getEndTime()) < 0), OnpremiseErrorEnum.CHECK_IN_TASK_TIME_INVALID);
        }
        AssertUtil.isTrue(Boolean.valueOf(!CollectionUtils.isEmpty(request.getTaskDevices())), OnpremiseErrorEnum.CHECK_IN_TASK_DEVICE_NO_MUST_NOT_NULL);
        for (CheckInDeviceDto checkInDeviceDto : request.getTaskDevices()) {
            Device device = this.deviceMapper.getDeviceDetail(checkInDeviceDto.getDeviceSn(), request.getOrgId());
            AssertUtil.isNullOrEmpty(device, OnpremiseErrorEnum.DEVICE_NOT_FIND);
        }
        AssertUtil.isNullOrEmpty(request.getCycle(), OnpremiseErrorEnum.CHECK_IN_TASK_CYCLE_MUST_NOT_NULL);
        if (request.getCycle().equals(Integer.valueOf(CheckInTaskCycleEnum.SINGLE.getValue())) && request.getStatus().equals(Integer.valueOf(CommonStatusEnum.YES.getValue()))) {
            AssertUtil.isNullOrEmpty(request.getCycleExtra(), OnpremiseErrorEnum.CHECK_IN_TASK_CYCLE_EXTRA_MUST_NOT_NULL);
            Calendar cal = Calendar.getInstance();
            Calendar calEnd = Calendar.getInstance();
            String[] dateArr = request.getCycleExtra().split("-");
            calEnd.set(1, Integer.parseInt(dateArr[0]));
            calEnd.set(2, Integer.parseInt(dateArr[1]));
            calEnd.set(5, Integer.parseInt(dateArr[2]));
            String[] endTimeArr = request.getEndTime().split(":");
            calEnd.set(11, Integer.parseInt(endTimeArr[0]));
            calEnd.set(12, Integer.parseInt(endTimeArr[1]));
            calEnd.set(13, 59);
            if (calEnd.before(cal)) {
                throw new BizException(OnpremiseErrorEnum.CHECK_IN_TASK_END_TIME_INVALID.getErrorCode(), OnpremiseErrorEnum.CHECK_IN_TASK_END_TIME_INVALID.getMessage());
            }
        }
        AssertUtil.isNullOrEmpty(request.getTipsText(), OnpremiseErrorEnum.CHECK_IN_TASK_TIPS_TEXT_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getStatus(), OnpremiseErrorEnum.CHECK_IN_TASK_STATUS_MUST_NOT_NULL);
    }

    @Override // com.moredian.onpremise.api.checkIn.CheckInService
    @Transactional(rollbackFor = {Exception.class})
    public boolean deleteCheckInTask(Long orgId, Long id) {
        Date now = new Date();
        AssertUtil.isNullOrEmpty(id, OnpremiseErrorEnum.CHECK_IN_TASK_ID_MUST_NOT_NULL);
        CheckInTask checkInTask = this.checkInTaskMapper.findById(orgId, id);
        AssertUtil.isNullOrEmpty(checkInTask, OnpremiseErrorEnum.CHECK_IN_TASK_NOT_EXITS);
        checkInTask.setDeleteFlag(Integer.valueOf(CommonStatusEnum.YES.getValue()));
        checkInTask.setGmtModify(now);
        AssertUtil.isTrue(Boolean.valueOf(this.checkInTaskMapper.updateCheckInTask(checkInTask) > 0), OnpremiseErrorEnum.CHECK_IN_TASK_DELETE_FAIL);
        List<CheckInTaskMemberDto> checkInTaskMemberDtoList = this.checkInTaskMemberMapper.listByTaskId(id, orgId);
        Set<Long> memberIdList = new HashSet<>();
        Set<Long> deptIdList = new HashSet<>();
        for (CheckInTaskMemberDto checkInTaskMemberDto : checkInTaskMemberDtoList) {
            if (checkInTaskMemberDto.getType().equals(Integer.valueOf(MemberTypeEnum.MEMBER.getValue()))) {
                memberIdList.add(checkInTaskMemberDto.getMemberId());
            } else {
                deptIdList.add(checkInTaskMemberDto.getDeptId());
            }
        }
        if (!CollectionUtils.isEmpty(memberIdList) || !CollectionUtils.isEmpty(deptIdList)) {
            this.memberMapper.updateModifyTime(orgId, new ArrayList(memberIdList), new ArrayList(deptIdList));
        }
        this.checkInTaskMemberMapper.softDeleteByTaskId(now, orgId, id);
        List<CheckInTaskDevice> checkInTaskDeviceList = this.checkInTaskDeviceMapper.getByTaskId(orgId, id);
        this.checkInTaskDeviceMapper.softDeleteByTaskId(now, orgId, id);
        Map deleteMap = new HashMap();
        deleteMap.put("orgId", orgId);
        deleteMap.put("taskId", id);
        this.checkInLogMapper.deleteByTask(deleteMap);
        this.checkInTaskLogMapper.deleteByTask(deleteMap);
        List<String> deviceSns = new ArrayList<>();
        if (!CollectionUtils.isEmpty(checkInTaskDeviceList)) {
            for (CheckInTaskDevice item : checkInTaskDeviceList) {
                deviceSns.add(item.getDeviceSn());
            }
            doSendNettyMessage(Long.valueOf(now.getTime()), checkInTask.getOrgId(), deviceSns);
            doSendMemberNetty(checkInTask.getOrgId());
            return true;
        }
        return true;
    }

    @Override // com.moredian.onpremise.api.checkIn.CheckInService
    @Transactional(rollbackFor = {Exception.class})
    public boolean updateCheckInTask(SaveCheckInTaskRequest request) throws BeansException {
        Date now = new Date();
        checkSaveTaskParams(request);
        CheckInTask checkInTaskOld = this.checkInTaskMapper.findById(request.getOrgId(), request.getId());
        if (checkInTaskOld == null) {
            throw new BizException(OnpremiseErrorEnum.CHECK_IN_TASK_NOT_EXITS.getErrorCode(), OnpremiseErrorEnum.CHECK_IN_TASK_NOT_EXITS.getMessage());
        }
        checkDeviceTimeInvalid(request);
        String startTimeOld = checkInTaskOld.getStartTime();
        String[] startTimeOldArr = startTimeOld.split(":");
        Calendar calendarStartOld = Calendar.getInstance();
        calendarStartOld.set(11, Integer.parseInt(startTimeOldArr[0]));
        calendarStartOld.set(12, Integer.parseInt(startTimeOldArr[1]));
        String endTimeOld = checkInTaskOld.getEndTime();
        String[] endTimeOldArr = endTimeOld.split(":");
        Calendar calendarEndOld = Calendar.getInstance();
        calendarEndOld.set(11, Integer.parseInt(endTimeOldArr[0]));
        calendarEndOld.set(12, Integer.parseInt(endTimeOldArr[1]));
        String startTime = request.getStartTime();
        String[] startTimeArr = startTime.split(":");
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.set(11, Integer.parseInt(startTimeArr[0]));
        calendarStart.set(12, Integer.parseInt(startTimeArr[1]));
        if (checkInTaskOld.getStatus().equals(Integer.valueOf(CommonStatusEnum.YES.getValue())) && now.after(calendarStartOld.getTime())) {
            if (checkInTaskOld.getCycle().equals(Integer.valueOf(CheckInTaskCycleEnum.SINGLE.getValue()))) {
                if (MyDateUtils.getDate().equals(checkInTaskOld.getCycleExtra())) {
                    throw new BizException(OnpremiseErrorEnum.CHECK_IN_TASK_UPDATE_FAIL_PROCESSING.getErrorCode(), OnpremiseErrorEnum.CHECK_IN_TASK_UPDATE_FAIL_PROCESSING.getMessage());
                }
            } else if (checkInTaskOld.getCycle().equals(Integer.valueOf(CheckInTaskCycleEnum.SPECIAL.getValue())) && now.before(calendarEndOld.getTime())) {
                int temp = calendarStartOld.get(7);
                List<Integer> cycleExtraArr = JsonUtils.jsoncastListType(Integer.class, checkInTaskOld.getCycleExtra());
                if (cycleExtraArr.contains(Integer.valueOf(temp))) {
                    throw new BizException(OnpremiseErrorEnum.CHECK_IN_TASK_UPDATE_FAIL_PROCESSING.getErrorCode(), OnpremiseErrorEnum.CHECK_IN_TASK_UPDATE_FAIL_PROCESSING.getMessage());
                }
            } else if (checkInTaskOld.getCycle().equals(Integer.valueOf(CheckInTaskCycleEnum.EVERYDAY.getValue())) && now.before(calendarEndOld.getTime())) {
                throw new BizException(OnpremiseErrorEnum.CHECK_IN_TASK_UPDATE_FAIL_PROCESSING.getErrorCode(), OnpremiseErrorEnum.CHECK_IN_TASK_UPDATE_FAIL_PROCESSING.getMessage());
            }
        }
        CheckInTask checkInTask = new CheckInTask();
        BeanUtils.copyProperties(request, checkInTask);
        checkInTask.setGmtModify(new Date());
        this.checkInTaskMapper.updateCheckInTask(checkInTask);
        Long time1 = Long.valueOf(System.currentTimeMillis());
        List<CheckInTaskMemberDto> newTaskMember = request.getTaskMembers();
        ArrayList arrayList = new ArrayList();
        for (CheckInTaskMemberDto item : newTaskMember) {
            item.setConfirmFlag(1);
            if (item.getType().equals(Integer.valueOf(MemberTypeEnum.DEPT.getValue()))) {
                this.deptService.packagingChildDept(item.getDeptId(), request.getOrgId(), arrayList);
            }
        }
        HashSet hashSet = new HashSet();
        List<Long> noRepeatChildDeptIds = new ArrayList<>();
        hashSet.addAll(arrayList);
        noRepeatChildDeptIds.addAll(hashSet);
        for (Long deptId : noRepeatChildDeptIds) {
            CheckInTaskMemberDto checkInTaskMemberDto = new CheckInTaskMemberDto();
            checkInTaskMemberDto.setType(Integer.valueOf(MemberTypeEnum.DEPT.getValue()));
            checkInTaskMemberDto.setDeptId(deptId);
            checkInTaskMemberDto.setMemberId(0L);
            checkInTaskMemberDto.setConfirmFlag(0);
            if (!newTaskMember.contains(checkInTaskMemberDto)) {
                newTaskMember.add(checkInTaskMemberDto);
            }
        }
        List<CheckInTaskMemberDto> oldTaskMember = this.checkInTaskMemberMapper.listByTaskId(request.getId(), request.getOrgId());
        MyListUtils<CheckInTaskMemberDto> utils = new MyListUtils<>();
        List<CheckInTaskMemberDto> insertTaskMember = utils.difference(newTaskMember, oldTaskMember);
        List<CheckInTaskMemberDto> deleteTaskMember = utils.difference(oldTaskMember, newTaskMember);
        if (!CollectionUtils.isEmpty(newTaskMember)) {
            this.checkInTaskMemberMapper.softDeleteByTaskId(now, request.getOrgId(), request.getId());
            List<CheckInTaskMember> checkInTaskMemberList = new ArrayList<>();
            for (CheckInTaskMemberDto item2 : newTaskMember) {
                CheckInTaskMember checkInTaskMember = new CheckInTaskMember();
                checkInTaskMember.setOrgId(request.getOrgId());
                checkInTaskMember.setTaskId(checkInTask.getId());
                checkInTaskMember.setType(item2.getType());
                checkInTaskMember.setDeptId(item2.getDeptId());
                checkInTaskMember.setMemberId(item2.getMemberId());
                checkInTaskMember.setStatus(Integer.valueOf(CommonStatusEnum.NO.getValue()));
                checkInTaskMember.setDeleteFlag(Integer.valueOf(CommonStatusEnum.NO.getValue()));
                checkInTaskMember.setConfirmFlag(item2.getConfirmFlag());
                checkInTaskMember.setGmtCreate(now);
                checkInTaskMember.setGmtModify(now);
                checkInTaskMemberList.add(checkInTaskMember);
            }
            this.checkInTaskMemberMapper.insertBatch(checkInTaskMemberList);
        }
        Set deptIdSet = new HashSet();
        Set memberIdSet = new HashSet();
        for (CheckInTaskMemberDto checkInTaskMemberDto2 : insertTaskMember) {
            if (checkInTaskMemberDto2.getType().equals(Integer.valueOf(MemberTypeEnum.DEPT.getValue()))) {
                deptIdSet.add(checkInTaskMemberDto2.getDeptId());
            } else {
                memberIdSet.add(checkInTaskMemberDto2.getMemberId());
            }
        }
        for (CheckInTaskMemberDto checkInTaskMemberDto3 : deleteTaskMember) {
            if (checkInTaskMemberDto3.getType().equals(Integer.valueOf(MemberTypeEnum.DEPT.getValue()))) {
                deptIdSet.add(checkInTaskMemberDto3.getDeptId());
            } else {
                memberIdSet.add(checkInTaskMemberDto3.getMemberId());
            }
        }
        if (!CollectionUtils.isEmpty(memberIdSet) || !CollectionUtils.isEmpty(deptIdSet)) {
            this.memberMapper.updateModifyTime(request.getOrgId(), new ArrayList(memberIdSet), new ArrayList(deptIdSet));
        }
        Long time2 = Long.valueOf(System.currentTimeMillis());
        logger.info("insert task member time :{}", Long.valueOf(time2.longValue() - time1.longValue()));
        List<CheckInTaskDevice> checkInTaskDeviceListOld = this.checkInTaskDeviceMapper.getByTaskId(checkInTask.getOrgId(), request.getId());
        List<CheckInDeviceDto> taskDevices = request.getTaskDevices();
        if (!CollectionUtils.isEmpty(taskDevices)) {
            this.checkInTaskDeviceMapper.softDeleteByTaskId(now, request.getOrgId(), request.getId());
            List<CheckInTaskDevice> checkInTaskDeviceList = new ArrayList<>();
            for (CheckInDeviceDto item3 : taskDevices) {
                CheckInTaskDevice checkInTaskDevice = new CheckInTaskDevice();
                checkInTaskDevice.setOrgId(request.getOrgId());
                checkInTaskDevice.setTaskId(checkInTask.getId());
                checkInTaskDevice.setDeviceId(this.deviceMapper.getDeviceInfoByDeviceSn(item3.getDeviceSn()).getDeviceId());
                checkInTaskDevice.setDeviceSn(item3.getDeviceSn());
                checkInTaskDevice.setDeleteFlag(Integer.valueOf(CommonStatusEnum.NO.getValue()));
                checkInTaskDevice.setGmtCreate(now);
                checkInTaskDevice.setGmtModify(now);
                checkInTaskDeviceList.add(checkInTaskDevice);
            }
            this.checkInTaskDeviceMapper.insertBatch(checkInTaskDeviceList);
        }
        Long time3 = Long.valueOf(System.currentTimeMillis());
        logger.info("insert task device time :{}", Long.valueOf(time3.longValue() - time2.longValue()));
        logger.info("updateCheckInTask,  status:" + String.valueOf(request.getStatus()) + "  now:" + String.valueOf(now.getTime()) + "  cal:" + String.valueOf(calendarStart.getTime()));
        if (request.getStatus().equals(Integer.valueOf(CommonStatusEnum.YES.getValue())) && now.before(calendarStart.getTime())) {
            Map deleteMap = new HashMap();
            deleteMap.put("orgId", request.getOrgId());
            deleteMap.put("taskId", checkInTaskOld.getId());
            deleteMap.put("taskTime", MyDateUtils.getDate());
            this.checkInTaskLogMapper.deleteByTask(deleteMap);
            this.checkInLogMapper.deleteByTask(deleteMap);
            logger.info("start insert task log time :{}", Long.valueOf(System.currentTimeMillis() - time3.longValue()));
            generateCheckInLog(checkInTask);
        }
        Long time4 = Long.valueOf(System.currentTimeMillis());
        logger.info("generate check log time :{}", Long.valueOf(time4.longValue() - time3.longValue()));
        List<String> deviceSns = new ArrayList<>();
        if (!CollectionUtils.isEmpty(checkInTaskDeviceListOld)) {
            Iterator<CheckInTaskDevice> it = checkInTaskDeviceListOld.iterator();
            while (it.hasNext()) {
                deviceSns.add(it.next().getDeviceSn());
            }
            if (!CollectionUtils.isEmpty(taskDevices)) {
                for (CheckInDeviceDto item4 : taskDevices) {
                    if (!deviceSns.contains(item4.getDeviceSn())) {
                        deviceSns.add(item4.getDeviceSn());
                    }
                }
            }
        }
        doSendMemberNetty(request.getOrgId());
        doSendNettyMessage(Long.valueOf(now.getTime()), request.getOrgId(), deviceSns);
        return true;
    }

    @Override // com.moredian.onpremise.api.checkIn.CheckInService
    @Transactional(rollbackFor = {Exception.class})
    public boolean openOrCloseCheckInTask(CheckInTaskOpenOrCloseRequest request) {
        AssertUtil.isNullOrEmpty(request.getStatus(), OnpremiseErrorEnum.CHECK_IN_TASK_STATUS_MUST_NOT_NULL);
        CheckInTask checkInTask = this.checkInTaskMapper.findById(request.getOrgId(), request.getCheckTaskId());
        AssertUtil.isNullOrEmpty(checkInTask, OnpremiseErrorEnum.CHECK_IN_TASK_NOT_EXITS);
        if (request.getStatus().equals(Integer.valueOf(CommonStatusEnum.YES.getValue()))) {
            Map deleteMap = new HashMap();
            deleteMap.put("orgId", request.getOrgId());
            deleteMap.put("taskId", request.getCheckTaskId());
            deleteMap.put("taskTime", MyDateUtils.getDate());
            this.checkInTaskLogMapper.deleteByTask(deleteMap);
            this.checkInLogMapper.deleteByTask(deleteMap);
            generateCheckInLog(checkInTask);
        }
        checkInTask.setStatus(request.getStatus());
        this.checkInTaskMapper.updateCheckInTask(checkInTask);
        return true;
    }

    @Override // com.moredian.onpremise.api.checkIn.CheckInService
    public PageList<CheckInTaskListResponse> pageCheckInTask(CheckInTaskListRequest request) {
        Paginator paginator = request.getPaginator();
        if (Paginator.checkPaginator(paginator)) {
            PageHelper.startPage(paginator.getPageNum(), paginator.getPageSize());
            List<CheckInTaskListResponse> responses = this.checkInTaskMapper.pageFind(request);
            dealCheckInTaskList(responses);
            return new PageList<>(responses);
        }
        List<CheckInTaskListResponse> responses2 = this.checkInTaskMapper.pageFind(request);
        dealCheckInTaskList(responses2);
        return new PageList<>(Paginator.initPaginator(responses2), responses2);
    }

    private void dealCheckInTaskList(List<CheckInTaskListResponse> responses) {
        if (!CollectionUtils.isEmpty(responses)) {
            for (CheckInTaskListResponse item : responses) {
                int taskMemberCount = 0;
                if (item.getAllUser().equals(Integer.valueOf(CommonStatusEnum.YES.getValue()))) {
                    taskMemberCount = this.memberMapper.countAllMembers(item.getOrgId()).intValue();
                } else {
                    List<Long> memberIdList = new ArrayList<>();
                    List<Long> removeMemberIdList = new ArrayList<>();
                    List<CheckInTaskMember> checkInTaskMemberList = this.checkInTaskMemberMapper.getByTaskId(item.getOrgId(), item.getId());
                    if (!CollectionUtils.isEmpty(checkInTaskMemberList)) {
                        for (CheckInTaskMember checkInTaskMember : checkInTaskMemberList) {
                            if (checkInTaskMember.getType().equals(Integer.valueOf(CheckInTaskMemberTypeEnum.DEPT.getValue()))) {
                                dealDeptMember(memberIdList, checkInTaskMember.getDeptId(), checkInTaskMember.getOrgId());
                            } else if (checkInTaskMember.getStatus().equals(Integer.valueOf(CommonStatusEnum.YES.getValue()))) {
                                removeMemberIdList.add(checkInTaskMember.getMemberId());
                            } else if (checkInTaskMember.getStatus().equals(Integer.valueOf(CommonStatusEnum.NO.getValue()))) {
                                memberIdList.add(checkInTaskMember.getMemberId());
                            }
                            dealMemberIdList(memberIdList, removeMemberIdList);
                            taskMemberCount = memberIdList.size();
                        }
                    }
                }
                item.setTaskMemberCount(Integer.valueOf(taskMemberCount));
                if (item.getAllDay().equals(Integer.valueOf(CommonStatusEnum.YES.getValue()))) {
                    item.setTimeStr("全天");
                    item.setStartTime("");
                    item.setEndTime("");
                } else {
                    item.setTimeStr(item.getStartTime() + "-" + item.getEndTime());
                }
                item.setAllUserName(CommonStatusEnum.getByValue(item.getAllUser().intValue()).getDescription());
                item.setAllDayName(CommonStatusEnum.getByValue(item.getAllDay().intValue()).getDescription());
                item.setCycleName(CheckInTaskCycleEnum.getByValue(item.getCycle().intValue()).getDescription());
                int taskDeviceCount = this.checkInTaskDeviceMapper.countByTaskId(item.getOrgId(), item.getId());
                item.setTaskDeviceCount(Integer.valueOf(taskDeviceCount));
                item.setStatusName(CheckInTaskStatusEnum.getByValue(item.getStatus().intValue()).getDescription());
                if (item.getCycle().equals(Integer.valueOf(CheckInTaskCycleEnum.SPECIAL.getValue()))) {
                    List<Integer> listJsoncastListType = JsonUtils.jsoncastListType(Integer.class, item.getCycleExtra());
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(2);
                    arrayList.add(3);
                    arrayList.add(4);
                    arrayList.add(5);
                    arrayList.add(6);
                    if (listJsoncastListType.containsAll(arrayList) && arrayList.containsAll(listJsoncastListType)) {
                        item.setCycleName("工作日");
                    } else {
                        ArrayList arrayList2 = new ArrayList();
                        arrayList2.add(1);
                        arrayList2.add(7);
                        if (listJsoncastListType.containsAll(arrayList2) && arrayList2.containsAll(listJsoncastListType)) {
                            item.setCycleName("双休日");
                        } else {
                            StringBuffer sb = new StringBuffer("");
                            String[] weekArr = {"", "星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
                            for (Integer index : listJsoncastListType) {
                                sb.append(weekArr[index.intValue()]).append(",");
                            }
                            String res = sb.toString();
                            item.setCycleName(res.substring(0, res.length() - 1));
                        }
                    }
                }
            }
        }
    }

    @Override // com.moredian.onpremise.api.checkIn.CheckInService
    public CheckInTaskResponse findCheckInTaskById(Long orgId, Long id) throws BeansException {
        AssertUtil.isNullOrEmpty(id, OnpremiseErrorEnum.CHECK_IN_TASK_ID_MUST_NOT_NULL);
        CheckInTask checkInTask = this.checkInTaskMapper.findById(orgId, id);
        CheckInTaskResponse response = new CheckInTaskResponse();
        if (checkInTask == null) {
            return response;
        }
        BeanUtils.copyProperties(checkInTask, response);
        response.setTaskMembers(new ArrayList());
        response.setUnTaskMembers(new ArrayList());
        if (checkInTask.getAllUser().equals(Integer.valueOf(CommonStatusEnum.NO.getValue()))) {
            List<CheckInMemberDto> checkInMemberDtoList = this.checkInTaskMemberMapper.getConfirmByTaskIdJoinMemberAndDept(orgId, id);
            if (!CollectionUtils.isEmpty(checkInMemberDtoList)) {
                response.setTaskMembers(checkInMemberDtoList);
            }
        }
        List<CheckInDeviceDto> taskDevices = new ArrayList<>();
        List<CheckInTaskDevice> checkInTaskDeviceList = this.checkInTaskDeviceMapper.getByTaskId(orgId, id);
        if (!CollectionUtils.isEmpty(checkInTaskDeviceList)) {
            List<String> deviceSns = new ArrayList<>();
            for (CheckInTaskDevice checkInTaskDevice : checkInTaskDeviceList) {
                deviceSns.add(checkInTaskDevice.getDeviceSn());
            }
            List<DeviceDto> deviceDtoList = this.deviceMapper.listDeviceDtoByDeviceSn(deviceSns, orgId);
            for (DeviceDto deviceDto : deviceDtoList) {
                CheckInDeviceDto checkInDeviceDto = new CheckInDeviceDto();
                BeanUtils.copyProperties(deviceDto, checkInDeviceDto);
                taskDevices.add(checkInDeviceDto);
            }
        }
        response.setTaskDevices(taskDevices);
        return response;
    }

    @Override // com.moredian.onpremise.api.checkIn.CheckInService
    @Transactional(rollbackFor = {Exception.class})
    public boolean generateCheckInLog(Date date) {
        List<CheckInTask> checkInTaskList = this.checkInTaskMapper.getAllCheckInTask();
        if (CollectionUtils.isEmpty(checkInTaskList)) {
            logger.info("当前没有签到任务！！！");
            return true;
        }
        for (CheckInTask item : checkInTaskList) {
            int cycle = item.getCycle().intValue();
            switch (cycle) {
                case 1:
                    insertCheckInTaskLog(item);
                    break;
                case 2:
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    int temp = cal.get(7);
                    List<Integer> cycleList = JsonUtils.jsoncastListType(Integer.class, item.getCycleExtra());
                    if (cycleList.contains(Integer.valueOf(temp))) {
                        insertCheckInTaskLog(item);
                        break;
                    } else {
                        break;
                    }
                case 3:
                    String nowDate = MyDateUtils.getDate();
                    if (nowDate.equals(item.getCycleExtra())) {
                        insertCheckInTaskLog(item);
                        break;
                    } else {
                        break;
                    }
                default:
                    logger.error("任务周期类型有误！任务id = " + item.getId() + "， 任务名称 = " + item.getName());
                    break;
            }
        }
        return true;
    }

    @Override // com.moredian.onpremise.api.checkIn.CheckInService
    public boolean insertCheckInTaskLog(CheckInTask checkInTask) {
        AssertUtil.isNullOrEmpty(checkInTask, OnpremiseErrorEnum.CHECK_IN_TASK_NOT_EXITS);
        Date now = new Date();
        Map countMap = new HashMap();
        countMap.put("orgId", checkInTask.getOrgId());
        countMap.put("taskId", checkInTask.getId());
        countMap.put("taskTime", MyDateUtils.getDate());
        int count = this.checkInTaskLogMapper.countByTaskIdAndTaskTime(countMap);
        if (count > 0) {
            return false;
        }
        List<Long> memberIdList = new ArrayList<>();
        List<Long> removeMemberIdList = new ArrayList<>();
        CheckInTaskLog checkInTaskLog = new CheckInTaskLog();
        checkInTaskLog.setOrgId(checkInTask.getOrgId());
        checkInTaskLog.setTaskId(checkInTask.getId());
        if (checkInTask.getAllUser().equals(Integer.valueOf(CommonStatusEnum.YES.getValue()))) {
            List<Member> memberList = this.memberMapper.getAllMembers(checkInTask.getOrgId());
            if (!CollectionUtils.isEmpty(memberList)) {
                Iterator<Member> it = memberList.iterator();
                while (it.hasNext()) {
                    memberIdList.add(it.next().getMemberId());
                }
            }
        } else {
            List<CheckInTaskMember> checkInTaskMemberList = this.checkInTaskMemberMapper.getByTaskId(checkInTask.getOrgId(), checkInTask.getId());
            if (!CollectionUtils.isEmpty(checkInTaskMemberList)) {
                for (CheckInTaskMember item : checkInTaskMemberList) {
                    if (item.getType().equals(Integer.valueOf(CheckInTaskMemberTypeEnum.DEPT.getValue()))) {
                        dealDeptMember(memberIdList, item.getDeptId(), checkInTask.getOrgId());
                    } else if (item.getStatus().equals(Integer.valueOf(CommonStatusEnum.YES.getValue()))) {
                        removeMemberIdList.add(item.getMemberId());
                    } else if (item.getStatus().equals(Integer.valueOf(CommonStatusEnum.NO.getValue()))) {
                        memberIdList.add(item.getMemberId());
                    }
                }
            }
        }
        dealMemberIdList(memberIdList, removeMemberIdList);
        if (CollectionUtils.isEmpty(memberIdList)) {
            return true;
        }
        checkInTaskLog.setTotalCount(Integer.valueOf(memberIdList.size()));
        checkInTaskLog.setTaskTime(MyDateUtils.getDate());
        checkInTaskLog.setGmtCreate(now);
        checkInTaskLog.setGmtModify(now);
        this.checkInTaskLogMapper.insertCheckInTaskLog(checkInTaskLog);
        List<CheckInLog> checkInLogList = new ArrayList<>();
        for (Long id : memberIdList) {
            Member member = this.memberMapper.getMemberInfoByMemberId(id, checkInTask.getOrgId());
            if (member != null) {
                String[] deptIds = member.getDeptId().split(",");
                String deptName = getDeptName(deptIds);
                CheckInLog checkInLog = new CheckInLog();
                checkInLog.setOrgId(checkInTaskLog.getOrgId());
                checkInLog.setTaskLogId(checkInTaskLog.getId());
                checkInLog.setTaskId(checkInTask.getId());
                checkInLog.setTaskName(checkInTask.getName());
                checkInLog.setTaskTime(checkInTaskLog.getTaskTime());
                checkInLog.setTaskStartTime(checkInTask.getStartTime());
                checkInLog.setTaskEndTime(checkInTask.getEndTime());
                checkInLog.setDeptId(member.getDeptId());
                checkInLog.setDeptName(deptName);
                checkInLog.setMemberId(id);
                checkInLog.setMemberName(member.getMemberName());
                checkInLog.setStatus(Integer.valueOf(CheckInLogStatusEnum.NO.getValue()));
                checkInLog.setGmtCreate(now);
                checkInLog.setGmtModify(now);
                checkInLogList.add(checkInLog);
            }
        }
        insertCheckInLogBatch(checkInLogList);
        return true;
    }

    private void dealDeptMember(List<Long> memberIdList, Long deptId, Long orgId) {
        List<DeptMemberListResponse> deptMemberListResponseList = this.memberMapper.listMemberByDeptId(deptId, orgId);
        if (!CollectionUtils.isEmpty(deptMemberListResponseList)) {
            for (DeptMemberListResponse itemDeptMember : deptMemberListResponseList) {
                memberIdList.add(itemDeptMember.getMemberId());
            }
        }
        List<Dept> childDeptList = this.deptMapper.listChildDeptToReturnDept(deptId, orgId);
        if (!CollectionUtils.isEmpty(childDeptList)) {
            for (Dept dept : childDeptList) {
                dealDeptMember(memberIdList, dept.getDeptId(), orgId);
            }
        }
    }

    private String getDeptName(String[] deptIds) {
        StringBuffer deptName = new StringBuffer("");
        for (String deptId : deptIds) {
            Dept dept = this.deptMapper.getDeptById(Long.valueOf(deptId));
            if (dept != null) {
                deptName.append(",").append(dept.getDeptName());
            }
        }
        String result = deptName.toString();
        if (StringUtils.isEmpty(result)) {
            return result;
        }
        return result.substring(1);
    }

    private static void dealMemberIdList(List<Long> memberIdList, List<Long> removeMemberIdList) {
        memberIdList.removeAll(removeMemberIdList);
        HashSet h = new HashSet(memberIdList);
        memberIdList.clear();
        memberIdList.addAll(h);
    }

    @Override // com.moredian.onpremise.api.checkIn.CheckInService
    public boolean insertCheckInLogBatch(List<CheckInLog> checkInLogList) {
        this.checkInLogMapper.insertBatch(checkInLogList);
        return true;
    }

    @Override // com.moredian.onpremise.api.checkIn.CheckInService
    public List<CheckInResponse> checkInList(CheckInLogRequest request) {
        List<CheckInResponse> checkInResponseList = new ArrayList<>();
        checkCheckInParams(request);
        Device device = this.deviceMapper.getDeviceInfoByDeviceSn(request.getDeviceSn());
        if (device == null) {
            throw new BizException(OnpremiseErrorEnum.CHECK_IN_DEVICE_NOT_EXITS.getErrorCode(), OnpremiseErrorEnum.CHECK_IN_DEVICE_NOT_EXITS.getMessage());
        }
        String taskTime = MyDateUtils.getDate();
        List<CheckInTask> checkInTaskList = this.checkInTaskMapper.getCheckInTaskByDeviceSnAndTaskTime(request.getOrgId(), request.getDeviceSn(), taskTime);
        if (CollectionUtils.isEmpty(checkInTaskList)) {
            throw new BizException(OnpremiseErrorEnum.CHECK_IN_TASK_NOT_EXITS.getErrorCode(), OnpremiseErrorEnum.CHECK_IN_TASK_NOT_EXITS.getMessage());
        }
        for (CheckInTask item : checkInTaskList) {
            boolean memberCheckIn = false;
            if (item.getAllUser().equals(Integer.valueOf(CommonStatusEnum.YES.getValue()))) {
                memberCheckIn = true;
            } else if (item.getAllUser().equals(Integer.valueOf(CommonStatusEnum.NO.getValue()))) {
                int countCheckInTaskMember = this.checkInTaskMemberMapper.countByTaskIdAndMemberId(request.getOrgId(), item.getId(), request.getMemberId());
                if (countCheckInTaskMember > 0) {
                    memberCheckIn = true;
                }
            }
            if (memberCheckIn) {
                CheckInResponse checkInResponse = new CheckInResponse();
                checkInResponse.setId(item.getId());
                checkInResponse.setName(item.getName());
                Date checkInTime = MyDateUtils.getDate(request.getCheckInTime().longValue());
                if (item.getAllDay().equals(Integer.valueOf(CommonStatusEnum.NO.getValue()))) {
                    String[] startTimeArr = item.getStartTime().split(":");
                    String[] endTimeArr = item.getEndTime().split(":");
                    Calendar calStart = Calendar.getInstance();
                    calStart.set(11, Integer.parseInt(startTimeArr[0]));
                    calStart.set(12, Integer.parseInt(startTimeArr[1]));
                    calStart.set(13, 0);
                    Calendar calEnd = Calendar.getInstance();
                    calEnd.set(11, Integer.parseInt(endTimeArr[0]));
                    calEnd.set(12, Integer.parseInt(endTimeArr[1]));
                    calEnd.set(13, 0);
                    if (checkInTime.before(calStart.getTime())) {
                        checkInResponse.setIsSuccess(Boolean.FALSE);
                        checkInResponse.setError(OnpremiseErrorEnum.CHECK_IN_FAIL_TIME_BEFORE.getErrorCode());
                        checkInResponse.setErrorInfo(OnpremiseErrorEnum.CHECK_IN_FAIL_TIME_BEFORE.getMessage());
                        checkInResponseList.add(checkInResponse);
                    } else if (checkInTime.after(calEnd.getTime())) {
                        checkInResponse.setIsSuccess(Boolean.FALSE);
                        checkInResponse.setError(OnpremiseErrorEnum.CHECK_IN_FAIL_TIME_AFTER.getErrorCode());
                        checkInResponse.setErrorInfo(OnpremiseErrorEnum.CHECK_IN_FAIL_TIME_AFTER.getMessage());
                        checkInResponseList.add(checkInResponse);
                    }
                }
                CheckInLog checkInLog = new CheckInLog();
                checkInLog.setOrgId(request.getOrgId());
                checkInLog.setTaskId(item.getId());
                checkInLog.setTaskTime(taskTime);
                checkInLog.setMemberId(request.getMemberId());
                checkInLog.setDeviceId(device.getDeviceId());
                checkInLog.setDeviceSn(request.getDeviceSn());
                checkInLog.setDeviceName(device.getDeviceName());
                checkInLog.setFaceUrl(request.getUrl());
                checkInLog.setStatus(Integer.valueOf(CheckInLogStatusEnum.YES.getValue()));
                checkInLog.setCheckInTime(MyDateUtils.getDate(request.getCheckInTime().longValue()));
                checkInLog.setGmtModify(new Date());
                CheckInLog res = this.checkInLogMapper.findByCond(checkInLog);
                if (res == null) {
                    Map condMap = new HashMap();
                    condMap.put("orgId", request.getOrgId());
                    condMap.put("taskId", item.getId());
                    condMap.put("taskTime", taskTime);
                    CheckInTaskLog checkInTaskLog = this.checkInTaskLogMapper.findCond(condMap);
                    if (checkInTaskLog == null) {
                        checkInResponse.setIsSuccess(Boolean.FALSE);
                        checkInResponse.setError(OnpremiseErrorEnum.CHECK_IN_TASK_NOT_EXITS.getErrorCode());
                        checkInResponse.setErrorInfo(OnpremiseErrorEnum.CHECK_IN_TASK_NOT_EXITS.getMessage());
                        checkInResponseList.add(checkInResponse);
                    } else {
                        checkInLog.setTaskLogId(checkInTaskLog.getId());
                        checkInLog.setGmtCreate(new Date());
                        this.checkInLogMapper.insertCheckInLog(checkInLog);
                        checkInResponse.setIsSuccess(Boolean.TRUE);
                        checkInResponseList.add(checkInResponse);
                    }
                } else if (res.getStatus().equals(Integer.valueOf(CheckInLogStatusEnum.NO.getValue()))) {
                    this.checkInLogMapper.checkIn(checkInLog);
                    checkInResponse.setIsSuccess(Boolean.TRUE);
                    checkInResponseList.add(checkInResponse);
                } else {
                    checkInResponse.setIsSuccess(Boolean.FALSE);
                    checkInResponse.setError(OnpremiseErrorEnum.CHECK_IN_CANNOT_REPEAT.getErrorCode());
                    checkInResponse.setErrorInfo(OnpremiseErrorEnum.CHECK_IN_CANNOT_REPEAT.getMessage());
                    checkInResponseList.add(checkInResponse);
                }
            }
        }
        return checkInResponseList;
    }

    private void checkCheckInParams(CheckInLogRequest request) {
        AssertUtil.isNullOrEmpty(request.getDeviceSn(), OnpremiseErrorEnum.CHECK_IN_DEVICE_SN_LACK);
        AssertUtil.isNullOrEmpty(request.getMemberId(), OnpremiseErrorEnum.CHECK_IN_MEMBER_ID_LACK);
        AssertUtil.isNullOrEmpty(request.getCheckInTime(), OnpremiseErrorEnum.CHECK_IN_TIME_LACK);
        Calendar cal = Calendar.getInstance();
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        Calendar cal2 = Calendar.getInstance();
        cal2.set(11, 23);
        cal2.set(12, 59);
        cal2.set(13, 59);
        Date checkInTime = MyDateUtils.getDate(request.getCheckInTime().longValue());
        if (!checkInTime.after(cal.getTime()) || !checkInTime.before(cal2.getTime())) {
            throw new BizException(OnpremiseErrorEnum.CHECK_IN_TIME_INVALID.getErrorCode(), OnpremiseErrorEnum.CHECK_IN_TIME_INVALID.getMessage());
        }
    }

    @Override // com.moredian.onpremise.api.checkIn.CheckInService
    public PageList<CheckInLogDayListResponse> pageCheckInLogDay(CheckInLogListRequest request) {
        Paginator paginator = request.getPaginator();
        if (Paginator.checkPaginator(paginator)) {
            PageHelper.startPage(paginator.getPageNum(), paginator.getPageSize());
            List<CheckInLogDayListResponse> responses = this.checkInTaskLogMapper.pageFind(request);
            dealCheckInLogDayList(responses);
            return new PageList<>(responses);
        }
        List<CheckInLogDayListResponse> responses2 = this.checkInTaskLogMapper.pageFind(request);
        dealCheckInLogDayList(responses2);
        return new PageList<>(Paginator.initPaginator(responses2), responses2);
    }

    private void dealCheckInLogDayList(List<CheckInLogDayListResponse> responses) {
        if (!CollectionUtils.isEmpty(responses)) {
            for (CheckInLogDayListResponse item : responses) {
                if (item.getCheckedInMemberCount() == null || item.getUnCheckedInMemberCount() == null) {
                    Map countMap = new HashMap();
                    countMap.put("orgId", item.getOrgId());
                    countMap.put("taskId", item.getCheckInTaskId());
                    countMap.put("taskTime", item.getCheckInTaskTimeStr());
                    countMap.put(BindTag.STATUS_VARIABLE_NAME, Integer.valueOf(CommonStatusEnum.YES.getValue()));
                    int checkedInMemberCount = this.checkInLogMapper.countCheckInLog(countMap);
                    item.setCheckedInMemberCount(Integer.valueOf(checkedInMemberCount));
                    countMap.put(BindTag.STATUS_VARIABLE_NAME, Integer.valueOf(CommonStatusEnum.NO.getValue()));
                    int unCheckedInMemberCount = this.checkInLogMapper.countCheckInLog(countMap);
                    item.setUnCheckedInMemberCount(Integer.valueOf(unCheckedInMemberCount));
                    item.setTotalCheckInMemberCount(Integer.valueOf(checkedInMemberCount + unCheckedInMemberCount));
                    Calendar cal = Calendar.getInstance();
                    cal.add(5, -1);
                    Calendar checkInTaskCal = Calendar.getInstance();
                    checkInTaskCal.setTime(item.getCheckInTaskTime());
                    if (cal.after(checkInTaskCal)) {
                        Map updateMap = new HashMap();
                        updateMap.put("orgId", item.getOrgId());
                        updateMap.put("id", item.getCheckInTaskLogId());
                        updateMap.put("checkedInCount", Integer.valueOf(checkedInMemberCount));
                        updateMap.put("uncheckedInCount", Integer.valueOf(unCheckedInMemberCount));
                        updateMap.put("totalCount", Integer.valueOf(checkedInMemberCount + unCheckedInMemberCount));
                        updateMap.put("gmtModify", new Date());
                        this.checkInTaskLogMapper.updateCheckInCount(updateMap);
                    }
                }
            }
        }
    }

    @Override // com.moredian.onpremise.api.checkIn.CheckInService
    public PageList<CheckInLogMemberListResponse> pageCheckInLogMember(CheckInLogListRequest request) {
        Paginator paginator = request.getPaginator();
        if (Paginator.checkPaginator(paginator)) {
            PageHelper.startPage(paginator.getPageNum(), paginator.getPageSize());
            List<CheckInLogMemberListResponse> responses = this.checkInLogMapper.pageCheckInLogMember(request);
            dealCheckInLogMemberList(responses, request);
            return new PageList<>(responses);
        }
        List<CheckInLogMemberListResponse> responses2 = this.checkInLogMapper.pageCheckInLogMember(request);
        dealCheckInLogMemberList(responses2, request);
        return new PageList<>(Paginator.initPaginator(responses2), responses2);
    }

    @Override // com.moredian.onpremise.api.checkIn.CheckInService
    public List<CheckInLogMemberListResponse> exportCheckInLogMember(CheckInLogListRequest request) {
        List<CheckInLogMemberListResponse> responses = this.checkInLogMapper.pageCheckInLogMember(request);
        dealCheckInLogMemberList(responses, request);
        return responses;
    }

    private void dealCheckInLogMemberList(List<CheckInLogMemberListResponse> responses, CheckInLogListRequest request) {
        if (!CollectionUtils.isEmpty(responses)) {
            for (CheckInLogMemberListResponse item : responses) {
                Map countMap = new HashMap();
                countMap.put("orgId", item.getOrgId());
                countMap.put("memberId", item.getCheckInMemberId());
                countMap.put("taskId", item.getCheckInTaskId());
                countMap.put(BindTag.STATUS_VARIABLE_NAME, Integer.valueOf(CommonStatusEnum.YES.getValue()));
                countMap.put(AuthConstants.AUTH_PARAM_START_TIME_KEY, request.getStartTime());
                countMap.put(AuthConstants.AUTH_PARAM_END_TIME_KEY, request.getEndTime());
                int checkedTaskCount = this.checkInLogMapper.countCheckInLog(countMap);
                item.setCheckedInTaskCount(Integer.valueOf(checkedTaskCount));
                countMap.put(BindTag.STATUS_VARIABLE_NAME, Integer.valueOf(CommonStatusEnum.NO.getValue()));
                int unCheckedTaskCount = this.checkInLogMapper.countCheckInLog(countMap);
                item.setUnCheckedInTaskCount(Integer.valueOf(unCheckedTaskCount));
                item.setTotalCheckInTaskCount(Integer.valueOf(checkedTaskCount + unCheckedTaskCount));
            }
        }
    }

    @Override // com.moredian.onpremise.api.checkIn.CheckInService
    public PageList<CheckInLogListResponse> pageCheckInLogDetail(CheckInLogListRequest request) {
        Paginator paginator = request.getPaginator();
        if (request.getStatus() != null && request.getStatus().equals(Integer.valueOf(CheckInLogStatusEnum.ALL.getValue()))) {
            request.setStatus(null);
        }
        if (Paginator.checkPaginator(paginator)) {
            PageHelper.startPage(paginator.getPageNum(), paginator.getPageSize());
            List<CheckInLogListResponse> responses = this.checkInLogMapper.pageFind(request);
            dealCheckInLogDetailList(responses);
            return new PageList<>(responses);
        }
        List<CheckInLogListResponse> responses2 = this.checkInLogMapper.pageFind(request);
        dealCheckInLogDetailList(responses2);
        return new PageList<>(Paginator.initPaginator(responses2), responses2);
    }

    @Override // com.moredian.onpremise.api.checkIn.CheckInService
    public List<CheckInLogListResponse> exportCheckInLogDetail(CheckInLogListRequest request) {
        List<CheckInLogListResponse> responses = this.checkInLogMapper.pageFind(request);
        dealCheckInLogDetailList(responses);
        return responses;
    }

    private void dealCheckInLogDetailList(List<CheckInLogListResponse> responses) {
        if (!CollectionUtils.isEmpty(responses)) {
            for (CheckInLogListResponse item : responses) {
                item.setCheckInStatusName(CheckInLogStatusEnum.getByValue(item.getCheckInStatus().intValue()).getDescription());
            }
        }
    }

    @Override // com.moredian.onpremise.api.checkIn.CheckInService
    public CheckInLogResponse findCheckInLogById(Long orgId, Long id) throws BeansException {
        CheckInLogResponse checkInLogResponse = new CheckInLogResponse();
        CheckInLog checkInLog = this.checkInLogMapper.findById(orgId, id);
        if (checkInLog == null) {
            return checkInLogResponse;
        }
        BeanUtils.copyProperties(checkInLog, checkInLogResponse);
        Member member = this.memberMapper.getMemberInfoByMemberId(checkInLog.getMemberId(), checkInLog.getOrgId());
        if (member != null) {
            checkInLogResponse.setMemberName(member.getMemberName());
        }
        CheckInTask checkInTask = this.checkInTaskMapper.findById(orgId, checkInLog.getTaskId());
        if (checkInTask != null) {
            checkInLogResponse.setTaskName(checkInTask.getName());
        }
        checkInLogResponse.setStatusName(CheckInLogStatusEnum.getByValue(checkInLog.getStatus().intValue()).getDescription());
        if (checkInLog.getCheckInTime() != null) {
            checkInLogResponse.setCheckInTimeStr(MyDateUtils.formatDate(checkInLog.getCheckInTime(), "yyyy-MM-dd HH:mm:ss"));
        }
        return checkInLogResponse;
    }

    @Override // com.moredian.onpremise.api.checkIn.CheckInService
    @Transactional(rollbackFor = {Exception.class})
    public Map<String, List> closeSingleCheckInTask() {
        Map res = new HashMap();
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        List<CheckInTask> checkInTaskList = this.checkInTaskMapper.getAllSingleCheckInTask(Integer.valueOf(CheckInTaskCycleEnum.SINGLE.getValue()));
        if (!CollectionUtils.isEmpty(checkInTaskList)) {
            for (CheckInTask item : checkInTaskList) {
                if (!item.getAllDay().equals(Integer.valueOf(CommonStatusEnum.YES.getValue()))) {
                    String[] dateArr = item.getCycleExtra().split("-");
                    String[] endTimeArr = item.getEndTime().split(":");
                    Calendar calEnd = Calendar.getInstance();
                    calEnd.set(1, Integer.parseInt(dateArr[0]));
                    calEnd.set(2, Integer.parseInt(dateArr[1]));
                    calEnd.set(5, Integer.parseInt(dateArr[2]));
                    calEnd.set(11, Integer.parseInt(endTimeArr[0]));
                    calEnd.set(12, Integer.parseInt(endTimeArr[1]));
                    Date now = new Date();
                    if (now.after(calEnd.getTime())) {
                        CheckInTask checkInTask = new CheckInTask();
                        checkInTask.setStatus(Integer.valueOf(CommonStatusEnum.NO.getValue()));
                        checkInTask.setId(item.getId());
                        try {
                            int count = this.checkInTaskMapper.updateCheckInTask(checkInTask);
                            if (count > 0) {
                                arrayList.add(item.getId());
                            } else {
                                arrayList2.add(item.getId());
                            }
                        } catch (Exception e) {
                            arrayList2.add(item.getId());
                        }
                    }
                }
            }
        }
        res.put(Constants.SUCCESS, arrayList);
        res.put(Constants.FAIL, arrayList2);
        return res;
    }

    @Override // com.moredian.onpremise.api.checkIn.CheckInService
    public TerminalSyncResponse<TerminalSyncCheckInTaskResponse> syncCheckInTask(TerminalSyncCheckInTaskRequest request) {
        AssertUtil.isNullOrEmpty(request.getLastSyncTime(), OnpremiseErrorEnum.CHECK_IN_TASK_LAST_SYNC_TIME_LACK);
        AssertUtil.isNullOrEmpty(request.getOrgId(), OnpremiseErrorEnum.CHECK_IN_TASK_ORGID_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getDeviceSn(), OnpremiseErrorEnum.CHECK_IN_TASK_DEVICE_NO_MUST_NOT_NULL);
        Device device = this.deviceMapper.getDeviceInfoByDeviceSn(request.getDeviceSn());
        if (device == null) {
            TerminalSyncResponse<TerminalSyncCheckInTaskResponse> terminalSyncResponse = new TerminalSyncResponse<>();
            terminalSyncResponse.setSyncTime(CacheAdapter.getLastModifyTime(CacheKeyGenerateUtils.getCheckInLastModifyTimeKey(request.getDeviceSn())).getLastModifyTime());
            return terminalSyncResponse;
        }
        return dealSyncResponse(request, device);
    }

    private TerminalSyncResponse<TerminalSyncCheckInTaskResponse> dealSyncResponse(TerminalSyncCheckInTaskRequest request, Device device) {
        TerminalSyncResponse<TerminalSyncCheckInTaskResponse> terminalSyncResponse = new TerminalSyncResponse<>();
        List<TerminalSyncCheckInTaskResponse> syncInsertResult = new ArrayList<>();
        List<TerminalSyncCheckInTaskResponse> syncModifyResult = new ArrayList<>();
        List<TerminalSyncCheckInTaskResponse> syncDeleteResult = new ArrayList<>();
        List<CheckInTaskDevice> checkInTaskDeviceList = this.checkInTaskDeviceMapper.getAllByDeviceId(request.getOrgId(), device.getDeviceId());
        if (CollectionUtils.isEmpty(checkInTaskDeviceList)) {
            terminalSyncResponse.setSyncTime(CacheAdapter.getLastModifyTime(CacheKeyGenerateUtils.getCheckInLastModifyTimeKey(request.getDeviceSn())).getLastModifyTime());
            return terminalSyncResponse;
        }
        ArrayList arrayList = new ArrayList();
        for (CheckInTaskDevice checkInTaskDevice : checkInTaskDeviceList) {
            if (!arrayList.contains(checkInTaskDevice.getTaskId())) {
                arrayList.add(checkInTaskDevice.getTaskId());
            }
        }
        Object lastSyncTime = MyDateUtils.getDateString(request.getLastSyncTime().longValue(), "yyyy-MM-dd HH:mm:ss.SSS");
        Map<String, Object> map = new HashMap<>();
        map.put("lastSyncTime", lastSyncTime);
        map.put("taskIds", arrayList);
        map.put("orgId", request.getOrgId());
        List<CheckInTask> checkInTaskList = this.checkInTaskMapper.findForSync(map);
        for (CheckInTask checkInTask : checkInTaskList) {
            TerminalSyncCheckInTaskResponse response = new TerminalSyncCheckInTaskResponse();
            response.setCheckInTaskId(checkInTask.getId());
            response.setCheckInTaskName(checkInTask.getName());
            response.setAllUser(checkInTask.getAllUser());
            response.setStartTime(checkInTask.getStartTime());
            response.setEndTime(checkInTask.getEndTime());
            response.setCycle(checkInTask.getCycle());
            response.setCycleExtra(checkInTask.getCycleExtra());
            response.setDeleteFlag(checkInTask.getDeleteFlag());
            response.setTipsText(checkInTask.getTipsText());
            response.setTipsSpeech(checkInTask.getTipsSpeech());
            response.setType(checkInTask.getType());
            response.setDoorFlag(checkInTask.getDoorFlag());
            response.setVoiceRemind(checkInTask.getVoiceRemind());
            response.setVoiceRemindAdvanceTime(checkInTask.getVoiceRemindAdvanceTime());
            response.setStatus(checkInTask.getStatus());
            int count = this.checkInTaskDeviceMapper.countByTaskIdAndDeviceSn(checkInTask.getOrgId(), checkInTask.getId(), device.getDeviceSn());
            if (checkInTask.getDeleteFlag().equals(Integer.valueOf(CommonStatusEnum.YES.getValue())) || count == 0) {
                syncDeleteResult.add(response);
            } else if (checkInTask.getGmtCreate().getTime() > request.getLastSyncTime().longValue()) {
                syncInsertResult.add(response);
            } else if (checkInTask.getGmtModify().getTime() > request.getLastSyncTime().longValue()) {
                syncModifyResult.add(response);
            }
        }
        CheckInTask lastCheckInTask = this.checkInTaskMapper.findLast(request.getOrgId());
        terminalSyncResponse.setSyncTime(Long.valueOf(lastCheckInTask == null ? 0L : lastCheckInTask.getGmtModify().getTime()));
        doCacheLastModifyTime(CacheKeyGenerateUtils.getCheckInLastModifyTimeKey(request.getDeviceSn()), terminalSyncResponse.getSyncTime());
        terminalSyncResponse.setSyncInsertResult(syncInsertResult);
        terminalSyncResponse.setSyncModifyResult(syncModifyResult);
        terminalSyncResponse.setSyncDeleteResult(syncDeleteResult);
        return terminalSyncResponse;
    }

    private void dealCheckInTaskMember(Map<Long, List<TerminalSyncCheckInTaskMemberResponse>> checkInTaskMemberMap, List<CheckInTaskMember> checkInTaskMemberList) {
        for (CheckInTaskMember item : checkInTaskMemberList) {
            TerminalSyncCheckInTaskMemberResponse terminalSyncCheckInTaskMemberResponse = new TerminalSyncCheckInTaskMemberResponse();
            terminalSyncCheckInTaskMemberResponse.setTaskId(item.getTaskId());
            terminalSyncCheckInTaskMemberResponse.setType(item.getType());
            terminalSyncCheckInTaskMemberResponse.setDeptId(item.getDeptId());
            terminalSyncCheckInTaskMemberResponse.setMemberId(item.getMemberId());
            terminalSyncCheckInTaskMemberResponse.setStatus(item.getStatus());
            List<TerminalSyncCheckInTaskMemberResponse> terminalSyncCheckInTaskMemberResponseList = checkInTaskMemberMap.get(item.getTaskId());
            if (CollectionUtils.isEmpty(terminalSyncCheckInTaskMemberResponseList)) {
                terminalSyncCheckInTaskMemberResponseList = new ArrayList();
                checkInTaskMemberMap.put(item.getTaskId(), terminalSyncCheckInTaskMemberResponseList);
            }
            if (!terminalSyncCheckInTaskMemberResponseList.contains(terminalSyncCheckInTaskMemberResponse)) {
                terminalSyncCheckInTaskMemberResponseList.add(terminalSyncCheckInTaskMemberResponse);
            }
        }
    }

    @Override // com.moredian.onpremise.api.checkIn.CheckInService
    public void notify(Long orgId, Long id, Integer type) {
        Date now = new Date();
        Map findMap = new HashMap();
        findMap.put("orgId", orgId);
        if (type.equals(Integer.valueOf(CheckInTaskNotifyTypeEnum.DEPT.getValue()))) {
            findMap.put("deptId", id);
            findMap.put("deleteFlag", Integer.valueOf(CommonStatusEnum.NO.getValue()));
            List<CheckInTaskMember> checkInTaskMemberList = this.checkInTaskMemberMapper.findCond(findMap);
            for (CheckInTaskMember item : checkInTaskMemberList) {
                this.checkInTaskMapper.updateModify(now, orgId, item.getTaskId());
                this.checkInTaskMemberMapper.softDeleteById(now, orgId, item.getId());
            }
            return;
        }
        if (type.equals(Integer.valueOf(CheckInTaskNotifyTypeEnum.MEMBER.getValue()))) {
            findMap.put("memberId", id);
            findMap.put("deleteFlag", Integer.valueOf(CommonStatusEnum.NO.getValue()));
            List<CheckInTaskMember> checkInTaskMemberList2 = this.checkInTaskMemberMapper.findCond(findMap);
            for (CheckInTaskMember item2 : checkInTaskMemberList2) {
                this.checkInTaskMapper.updateModify(now, orgId, item2.getTaskId());
                this.checkInTaskMemberMapper.softDeleteById(now, orgId, item2.getId());
            }
            return;
        }
        if (type.equals(Integer.valueOf(CheckInTaskNotifyTypeEnum.DEVICE.getValue()))) {
            List<CheckInTaskDevice> checkInTaskDeviceList = this.checkInTaskDeviceMapper.getAllByDeviceId(orgId, id);
            for (CheckInTaskDevice item3 : checkInTaskDeviceList) {
                this.checkInTaskMapper.updateModify(now, orgId, item3.getTaskId());
                this.checkInTaskDeviceMapper.softDeleteById(now, orgId, item3.getId());
            }
        }
    }

    @Override // com.moredian.onpremise.api.checkIn.CheckInService
    public void saveCheckInLog(CheckInLogRequest request) {
        Map countMap = new HashMap();
        countMap.put("orgId", request.getOrgId());
        countMap.put("memberId", request.getMemberId());
        countMap.put("taskId", request.getId());
        countMap.put("taskTime", MyDateUtils.getDateString(request.getCheckInTime().longValue(), new Object[0]));
        int count = this.checkInLogMapper.countCheckInLog(countMap);
        CheckInLog checkInLog = new CheckInLog();
        if (count > 0) {
            checkInLog.setOrgId(request.getOrgId());
            checkInLog.setTaskId(request.getId());
            checkInLog.setTaskTime(MyDateUtils.getDateString(request.getCheckInTime().longValue(), new Object[0]));
            checkInLog.setMemberId(request.getMemberId());
            checkInLog.setDeviceSn(request.getDeviceSn());
            Device device = this.deviceMapper.getDeviceInfoByDeviceSn(request.getDeviceSn());
            if (device != null) {
                checkInLog.setDeviceId(device.getDeviceId());
                checkInLog.setDeviceName(device.getDeviceName());
            }
            checkInLog.setFaceUrl(request.getUrl());
            checkInLog.setStatus(Integer.valueOf(CheckInLogStatusEnum.YES.getValue()));
            checkInLog.setCheckInTime(MyDateUtils.getDate(request.getCheckInTime().longValue()));
            checkInLog.setGmtModify(new Date());
            this.checkInLogMapper.checkIn(checkInLog);
            return;
        }
        checkInLog.setOrgId(request.getOrgId());
        checkInLog.setTaskId(request.getId());
        CheckInTask checkInTask = this.checkInTaskMapper.findById(request.getOrgId(), request.getId());
        if (checkInTask == null) {
            return;
        }
        checkInLog.setTaskName(checkInTask.getName());
        checkInLog.setTaskStartTime(checkInTask.getStartTime());
        checkInLog.setTaskEndTime(checkInTask.getEndTime());
        Map condMap = new HashMap();
        condMap.put("orgId", request.getOrgId());
        condMap.put("taskId", request.getId());
        condMap.put("taskTime", MyDateUtils.getDateString(request.getCheckInTime().longValue(), new Object[0]));
        CheckInTaskLog checkInTaskLog = this.checkInTaskLogMapper.findCond(condMap);
        if (checkInTaskLog == null) {
            return;
        }
        checkInLog.setTaskLogId(checkInTaskLog.getId());
        checkInLog.setTaskTime(MyDateUtils.getDateString(request.getCheckInTime().longValue(), new Object[0]));
        checkInLog.setMemberId(request.getMemberId());
        Member member = this.memberMapper.getMemberInfoByMemberId(request.getMemberId(), request.getOrgId());
        if (member == null) {
            return;
        }
        checkInLog.setMemberName(member.getMemberName());
        checkInLog.setDeptId(member.getDeptId());
        String[] deptIds = member.getDeptId().split(",");
        String deptName = getDeptName(deptIds);
        checkInLog.setDeptName(deptName);
        checkInLog.setDeviceSn(request.getDeviceSn());
        Device device2 = this.deviceMapper.getDeviceInfoByDeviceSn(request.getDeviceSn());
        if (device2 != null) {
            checkInLog.setDeviceId(device2.getDeviceId());
            checkInLog.setDeviceName(device2.getDeviceName());
        }
        checkInLog.setFaceUrl(request.getUrl());
        checkInLog.setStatus(Integer.valueOf(CheckInLogStatusEnum.YES.getValue()));
        checkInLog.setCheckInTime(MyDateUtils.getDate(request.getCheckInTime().longValue()));
        checkInLog.setGmtCreate(new Date());
        checkInLog.setGmtModify(new Date());
        this.checkInLogMapper.insertCheckInLog(checkInLog);
    }

    @Override // com.moredian.onpremise.api.checkIn.CheckInService
    public CheckInResponse checkIn(CheckInLogRequest request) {
        CheckInResponse checkInResponse = new CheckInResponse();
        checkCheckInParams(request);
        Device device = this.deviceMapper.getDeviceInfoByDeviceSn(request.getDeviceSn());
        if (device == null) {
            throw new BizException(OnpremiseErrorEnum.CHECK_IN_DEVICE_NOT_EXITS.getErrorCode(), OnpremiseErrorEnum.CHECK_IN_DEVICE_NOT_EXITS.getMessage());
        }
        String taskTime = MyDateUtils.getDate();
        List<CheckInTask> checkInTaskList = this.checkInTaskMapper.getCheckInTaskByDeviceSnAndTaskTime(request.getOrgId(), request.getDeviceSn(), taskTime);
        if (CollectionUtils.isEmpty(checkInTaskList)) {
            throw new BizException(OnpremiseErrorEnum.CHECK_IN_TASK_NOT_EXITS.getErrorCode(), OnpremiseErrorEnum.CHECK_IN_TASK_NOT_EXITS.getMessage());
        }
        CheckInTask checkInTask = checkInTaskList.get(0);
        boolean memberCheckIn = isMemberCheckIn(request, checkInTask, false);
        if (memberCheckIn) {
            checkInResponse.setId(checkInTask.getId());
            checkInResponse.setName(checkInTask.getName());
            checkInResponse.setTipsText(checkInTask.getTipsText());
            checkInResponse.setTipsSpeech(checkInTask.getTipsSpeech());
            Date checkInTime = MyDateUtils.getDate(request.getCheckInTime().longValue());
            if (checkInTask.getAllDay().equals(Integer.valueOf(CommonStatusEnum.NO.getValue()))) {
                String[] startTimeArr = checkInTask.getStartTime().split(":");
                String[] endTimeArr = checkInTask.getEndTime().split(":");
                Calendar calStart = Calendar.getInstance();
                calStart.set(11, Integer.parseInt(startTimeArr[0]));
                calStart.set(12, Integer.parseInt(startTimeArr[1]));
                calStart.set(13, 0);
                Calendar calEnd = Calendar.getInstance();
                calEnd.set(11, Integer.parseInt(endTimeArr[0]));
                calEnd.set(12, Integer.parseInt(endTimeArr[1]));
                calEnd.set(13, 0);
                if (checkInTime.before(calStart.getTime())) {
                    throw new BizException(OnpremiseErrorEnum.CHECK_IN_TASK_NOT_EXITS.getErrorCode(), OnpremiseErrorEnum.CHECK_IN_TASK_NOT_EXITS.getMessage());
                }
                if (checkInTime.after(calEnd.getTime())) {
                    throw new BizException(OnpremiseErrorEnum.CHECK_IN_TASK_NOT_EXITS.getErrorCode(), OnpremiseErrorEnum.CHECK_IN_TASK_NOT_EXITS.getMessage());
                }
            }
            CheckInLog checkInLog = new CheckInLog();
            checkInLog.setOrgId(request.getOrgId());
            checkInLog.setTaskId(checkInTask.getId());
            checkInLog.setTaskTime(taskTime);
            checkInLog.setMemberId(request.getMemberId());
            checkInLog.setDeviceId(device.getDeviceId());
            checkInLog.setDeviceSn(request.getDeviceSn());
            checkInLog.setDeviceName(device.getDeviceName());
            checkInLog.setFaceUrl(request.getUrl());
            checkInLog.setStatus(Integer.valueOf(CheckInLogStatusEnum.YES.getValue()));
            checkInLog.setCheckInTime(MyDateUtils.getDate(request.getCheckInTime().longValue()));
            checkInLog.setGmtModify(new Date());
            CheckInLog res = this.checkInLogMapper.findByCond(checkInLog);
            if (res == null) {
                Map condMap = new HashMap();
                condMap.put("orgId", request.getOrgId());
                condMap.put("taskId", checkInTask.getId());
                condMap.put("taskTime", taskTime);
                CheckInTaskLog checkInTaskLog = this.checkInTaskLogMapper.findCond(condMap);
                if (checkInTaskLog == null) {
                    throw new BizException(OnpremiseErrorEnum.CHECK_IN_TASK_NOT_EXITS.getErrorCode(), OnpremiseErrorEnum.CHECK_IN_TASK_NOT_EXITS.getMessage());
                }
                checkInLog.setTaskLogId(checkInTaskLog.getId());
                Member member = this.memberMapper.getMemberInfoByMemberId(request.getMemberId(), request.getOrgId());
                if (member == null) {
                    throw new BizException(OnpremiseErrorEnum.MEMBER_NOT_FIND.getErrorCode(), OnpremiseErrorEnum.MEMBER_NOT_FIND.getMessage());
                }
                checkInLog.setTaskName(checkInTask.getName());
                checkInLog.setTaskStartTime(checkInTask.getStartTime());
                checkInLog.setTaskEndTime(checkInTask.getEndTime());
                checkInLog.setDeptId(member.getDeptId());
                String[] deptIds = member.getDeptId().split(",");
                String deptName = getDeptName(deptIds);
                checkInLog.setDeptName(deptName);
                checkInLog.setMemberName(member.getMemberName());
                checkInLog.setGmtCreate(new Date());
                this.checkInLogMapper.insertCheckInLog(checkInLog);
                checkInResponse.setIsSuccess(Boolean.TRUE);
            } else if (res.getStatus().equals(Integer.valueOf(CheckInLogStatusEnum.NO.getValue()))) {
                this.checkInLogMapper.checkIn(checkInLog);
                checkInResponse.setIsSuccess(Boolean.TRUE);
            } else {
                throw new BizException(OnpremiseErrorEnum.CHECK_IN_CANNOT_REPEAT.getErrorCode(), OnpremiseErrorEnum.CHECK_IN_CANNOT_REPEAT.getMessage());
            }
            return checkInResponse;
        }
        throw new BizException(OnpremiseErrorEnum.CHECK_IN_TASK_NOT_EXITS.getErrorCode(), OnpremiseErrorEnum.CHECK_IN_TASK_NOT_EXITS.getMessage());
    }

    private boolean isMemberCheckIn(CheckInLogRequest request, CheckInTask checkInTask, boolean memberCheckIn) {
        if (checkInTask.getAllUser().equals(Integer.valueOf(CommonStatusEnum.YES.getValue()))) {
            memberCheckIn = true;
        } else if (checkInTask.getAllUser().equals(Integer.valueOf(CommonStatusEnum.NO.getValue()))) {
            List<Long> memberIdList = new ArrayList<>();
            List<Long> removeMemberIdList = new ArrayList<>();
            List<CheckInTaskMember> checkInTaskMemberList = this.checkInTaskMemberMapper.getByTaskId(checkInTask.getOrgId(), checkInTask.getId());
            if (!CollectionUtils.isEmpty(checkInTaskMemberList)) {
                for (CheckInTaskMember checkInTaskMember : checkInTaskMemberList) {
                    if (checkInTaskMember.getType().equals(Integer.valueOf(CheckInTaskMemberTypeEnum.DEPT.getValue()))) {
                        dealDeptMember(memberIdList, checkInTaskMember.getDeptId(), checkInTaskMember.getOrgId());
                    } else if (checkInTaskMember.getStatus().equals(Integer.valueOf(CommonStatusEnum.YES.getValue()))) {
                        removeMemberIdList.add(checkInTaskMember.getMemberId());
                    } else if (checkInTaskMember.getStatus().equals(Integer.valueOf(CommonStatusEnum.NO.getValue()))) {
                        memberIdList.add(checkInTaskMember.getMemberId());
                    }
                }
            }
            dealMemberIdList(memberIdList, removeMemberIdList);
            if (memberIdList.contains(request.getMemberId())) {
                memberCheckIn = true;
            }
        }
        return memberCheckIn;
    }

    @Override // com.moredian.onpremise.api.checkIn.CheckInService
    public BizCheckResponse hasCheck(CheckInLogRequest request) {
        BizCheckResponse result = null;
        checkCheckInParams(request);
        Device device = this.deviceMapper.getDeviceInfoByDeviceSn(request.getDeviceSn());
        if (device == null) {
            throw new BizException(OnpremiseErrorEnum.CHECK_IN_DEVICE_NOT_EXITS.getErrorCode(), OnpremiseErrorEnum.CHECK_IN_DEVICE_NOT_EXITS.getMessage());
        }
        String taskTime = MyDateUtils.getDate();
        List<CheckInTask> checkInTaskList = this.checkInTaskMapper.getCheckInTaskByDeviceSnAndTaskTime(request.getOrgId(), request.getDeviceSn(), taskTime);
        if (CollectionUtils.isEmpty(checkInTaskList)) {
            return null;
        }
        CheckInTask checkInTask = checkInTaskList.get(0);
        boolean memberCheckIn = isMemberCheckIn(request, checkInTask, false);
        if (memberCheckIn) {
            Date checkInTime = MyDateUtils.getDate(request.getCheckInTime().longValue());
            if (checkInTask.getAllDay().equals(Integer.valueOf(CommonStatusEnum.NO.getValue()))) {
                String[] startTimeArr = checkInTask.getStartTime().split(":");
                String[] endTimeArr = checkInTask.getEndTime().split(":");
                Calendar calStart = Calendar.getInstance();
                calStart.set(11, Integer.parseInt(startTimeArr[0]));
                calStart.set(12, Integer.parseInt(startTimeArr[1]));
                calStart.set(13, 0);
                Calendar calEnd = Calendar.getInstance();
                calEnd.set(11, Integer.parseInt(endTimeArr[0]));
                calEnd.set(12, Integer.parseInt(endTimeArr[1]));
                calEnd.set(13, 0);
                if (checkInTime.before(calStart.getTime()) || checkInTime.after(calEnd.getTime())) {
                    return null;
                }
            }
            CheckInLog checkInLog = new CheckInLog();
            checkInLog.setOrgId(request.getOrgId());
            checkInLog.setTaskId(checkInTask.getId());
            checkInLog.setTaskTime(taskTime);
            checkInLog.setMemberId(request.getMemberId());
            CheckInLog res = this.checkInLogMapper.findByCond(checkInLog);
            if (res != null && res.getStatus().equals(Integer.valueOf(CheckInLogStatusEnum.YES.getValue()))) {
                BizCheckResponse result2 = new BizCheckResponse();
                result2.setAppType(Integer.valueOf(AppTypeEnum.QD.getValue()));
                result2.setTipsText(OnpremiseErrorEnum.CHECK_IN_CANNOT_REPEAT.getMessage());
                return result2;
            }
            result = new BizCheckResponse();
            result.setAppType(Integer.valueOf(AppTypeEnum.QD.getValue()));
            result.setTipsText(checkInTask.getTipsText());
            result.setTipsSpeech(checkInTask.getTipsSpeech());
        }
        return result;
    }

    @Override // com.moredian.onpremise.api.checkIn.CheckInService
    public void generateCheckInTaskBaseLibraryByMember(Long memberId, Long orgId, boolean flag) {
        if (flag) {
            this.memberCheckInTaskSyncMapper.softDeleteByMember(memberId, orgId);
            return;
        }
        Member member = this.memberMapper.getMemberInfoByMemberId(memberId, orgId);
        if (member == null) {
            return;
        }
        MemberCheckInTaskSync memberCheckInTaskSync = this.memberCheckInTaskSyncMapper.findByMember(memberId, orgId);
        if (memberCheckInTaskSync == null) {
            MemberCheckInTaskSync memberCheckInTaskSync2 = new MemberCheckInTaskSync();
            memberCheckInTaskSync2.setOrgId(orgId);
            memberCheckInTaskSync2.setMemberId(memberId);
            if (dealMemberCheckInTaskSync(member, memberCheckInTaskSync2)) {
                this.memberCheckInTaskSyncMapper.insertMemberCheckInTaskSync(memberCheckInTaskSync2);
                return;
            }
            return;
        }
        if (dealMemberCheckInTaskSync(member, memberCheckInTaskSync)) {
            this.memberCheckInTaskSyncMapper.updateByMember(memberCheckInTaskSync);
        } else {
            this.memberCheckInTaskSyncMapper.softDeleteByMember(memberId, orgId);
        }
    }

    private boolean dealMemberCheckInTaskSync(Member member, MemberCheckInTaskSync memberCheckInTaskSync) {
        memberCheckInTaskSync.setDeptId(member.getDeptId());
        List<Long> checkInTaskIdList = new ArrayList<>();
        Map memberMap = new HashMap();
        memberMap.put("orgId", member.getOrgId());
        memberMap.put("memberId", member.getMemberId());
        List<CheckInTaskMember> checkInTaskMemberList = this.checkInTaskMemberMapper.findCond(memberMap);
        if (!CollectionUtils.isEmpty(checkInTaskMemberList)) {
            for (CheckInTaskMember checkInTaskMember : checkInTaskMemberList) {
                if (!checkInTaskIdList.contains(checkInTaskMember.getTaskId())) {
                    checkInTaskIdList.add(checkInTaskMember.getTaskId());
                }
            }
        }
        List<Long> deptIdList = new ArrayList<>();
        String[] deptIds = member.getDeptId().split(",");
        for (String deptId : deptIds) {
            if (!StringUtils.isEmpty(deptId)) {
                dealSuperDept(Long.valueOf(Long.parseLong(deptId)), deptIdList);
            }
        }
        for (Long deptId2 : deptIdList) {
            Map deptMap = new HashMap();
            deptMap.put("orgId", member.getOrgId());
            deptMap.put("deptId", deptId2);
            List<CheckInTaskMember> checkInTaskDeptList = this.checkInTaskMemberMapper.findCond(deptMap);
            if (!CollectionUtils.isEmpty(checkInTaskDeptList)) {
                for (CheckInTaskMember checkInTaskMember2 : checkInTaskDeptList) {
                    if (!checkInTaskIdList.contains(checkInTaskMember2.getTaskId())) {
                        checkInTaskIdList.add(checkInTaskMember2.getTaskId());
                    }
                }
            }
        }
        if (CollectionUtils.isEmpty(checkInTaskIdList)) {
            return false;
        }
        String checkInTaskIdStrTemp = Arrays.toString(checkInTaskIdList.toArray());
        String checkInTaskIdStrTemp2 = checkInTaskIdStrTemp.substring(1, checkInTaskIdStrTemp.length());
        String checkInTaskIdStr = checkInTaskIdStrTemp2.substring(0, checkInTaskIdStrTemp2.length() - 1);
        memberCheckInTaskSync.setCheckInTaskIds(checkInTaskIdStr.replaceAll(SymbolConstants.SPACE_SYMBOL, ""));
        List<String> deviceSnList = new ArrayList<>();
        for (Long checkInTaskId : checkInTaskIdList) {
            List<CheckInTaskDevice> checkInTaskDeviceList = this.checkInTaskDeviceMapper.getByTaskId(member.getOrgId(), checkInTaskId);
            if (!CollectionUtils.isEmpty(checkInTaskDeviceList)) {
                for (CheckInTaskDevice checkInTaskDevice : checkInTaskDeviceList) {
                    if (!deviceSnList.contains(checkInTaskDevice.getDeviceSn())) {
                        deviceSnList.add(checkInTaskDevice.getDeviceSn());
                    }
                }
            }
        }
        if (CollectionUtils.isEmpty(deviceSnList)) {
            return false;
        }
        String deviceSnStrTemp = Arrays.toString(deviceSnList.toArray());
        String deviceSnStrTemp2 = deviceSnStrTemp.substring(1, deviceSnStrTemp.length());
        String deviceSnStr = deviceSnStrTemp2.substring(0, deviceSnStrTemp2.length() - 1);
        memberCheckInTaskSync.setNewestDeviceSns(deviceSnStr.replaceAll(SymbolConstants.SPACE_SYMBOL, ""));
        return true;
    }

    private void dealSuperDept(Long deptId, List<Long> deptIdList) {
        deptIdList.add(deptId);
        Dept dept = this.deptMapper.getDeptById(deptId);
        Dept superDept = this.deptMapper.getDeptById(dept.getSuperDeptId());
        if (superDept != null) {
            dealSuperDept(superDept.getDeptId(), deptIdList);
        }
    }

    @Override // com.moredian.onpremise.api.checkIn.CheckInService
    public void generateCheckInTaskBaseLibraryByDept(Long deptId, Long orgId) {
        List<Long> memberIdList = new ArrayList<>();
        dealDeptMember(memberIdList, deptId, orgId);
        if (CollectionUtils.isEmpty(memberIdList)) {
            return;
        }
        for (Long memberId : memberIdList) {
            generateCheckInTaskBaseLibraryByMember(memberId, orgId, false);
        }
    }

    @Override // com.moredian.onpremise.api.checkIn.CheckInService
    public void generateCheckInTaskBaseLibraryByTask(Long checkTaskId, Long orgId, boolean flag) {
        if (flag) {
            for (Long memberId : this.memberCheckInTaskSyncMapper.getMemberListByCheckInTask(checkTaskId, orgId)) {
                generateCheckInTaskBaseLibraryByMember(memberId, orgId, false);
            }
            return;
        }
        List<Long> memberIdListOld = this.memberCheckInTaskSyncMapper.getMemberListByCheckInTask(checkTaskId, orgId);
        ArrayList arrayList = new ArrayList();
        List<CheckInTaskMember> checkInTaskMemberList = this.checkInTaskMemberMapper.getByTaskId(orgId, checkTaskId);
        if (!CollectionUtils.isEmpty(checkInTaskMemberList)) {
            for (CheckInTaskMember checkInTaskMember : checkInTaskMemberList) {
                if (checkInTaskMember.getType().equals(Integer.valueOf(CheckInTaskMemberTypeEnum.DEPT.getValue()))) {
                    dealDeptMember(arrayList, checkInTaskMember.getDeptId(), checkInTaskMember.getOrgId());
                } else {
                    arrayList.add(checkInTaskMember.getMemberId());
                }
            }
        }
        List<Long> memberIdList = new ArrayList<>();
        if (CollectionUtils.isEmpty(arrayList)) {
            memberIdList.addAll(memberIdListOld);
        } else if (CollectionUtils.isEmpty(memberIdListOld)) {
            memberIdList.addAll(arrayList);
        } else {
            arrayList.removeAll(memberIdListOld);
            arrayList.addAll(memberIdListOld);
            memberIdList.addAll(arrayList);
        }
        for (Long memberId2 : memberIdList) {
            generateCheckInTaskBaseLibraryByMember(memberId2, orgId, false);
        }
    }

    @Override // com.moredian.onpremise.api.checkIn.CheckInService
    public void generateCheckInTaskBaseLibraryByDevice(String deviceSn, Long orgId) {
        this.memberCheckInTaskSyncMapper.softDeleteByDevice(deviceSn, orgId);
        this.memberCheckInTaskSyncMapper.updateNewestDeviceSnsByDevice(deviceSn, orgId);
    }

    @Override // com.moredian.onpremise.api.checkIn.CheckInService
    public TerminalSyncResponse<TerminalSyncMemberCheckInTaskResponse> syncCheckInMember(TerminalSyncRequest request) {
        Dept dept;
        AssertUtil.isTrue(Boolean.valueOf(request.getLastSyncTime() != null), OnpremiseErrorEnum.LAST_SYNC_TIME_MUST_NOT_NULL);
        Device device = this.deviceMapper.getDeviceInfoByDeviceSn(request.getDeviceSn());
        if (device == null) {
            logger.info("==========================device not find ");
            return new TerminalSyncResponse<>();
        }
        String lastSyncTime = MyDateUtils.getDateString(request.getLastSyncTime().longValue(), "yyyy-MM-dd HH:mm:ss.SSS");
        List<TerminalSyncMemberCheckInTaskResponse> terminalSyncMemberCheckInTaskResponseList = this.memberCheckInTaskSyncMapper.getListForSync(request.getOrgId(), lastSyncTime, request.getDeviceSn());
        List<TerminalSyncMemberCheckInTaskResponse> syncInsertResult = new ArrayList<>();
        List<TerminalSyncMemberCheckInTaskResponse> syncModifyResult = new ArrayList<>();
        List<TerminalSyncMemberCheckInTaskResponse> syncDeleteResult = new ArrayList<>();
        for (TerminalSyncMemberCheckInTaskResponse response : terminalSyncMemberCheckInTaskResponseList) {
            StringBuffer deptNameSb = new StringBuffer("");
            String[] deptIds = response.getDeptId().split(",");
            for (String deptId : deptIds) {
                if (!StringUtils.isEmpty(deptId) && (dept = this.deptMapper.getDeptById(Long.valueOf(Long.parseLong(deptId)))) != null) {
                    deptNameSb.append(dept.getDeptName()).append(",");
                }
            }
            if (!StringUtils.isEmpty(deptNameSb.toString())) {
                response.setDeptName(deptNameSb.toString().substring(0, deptNameSb.toString().length() - 1));
            }
            if (response.getDeleteOrNot().intValue() == DeleteOrNotEnum.DELETE_YES.getValue()) {
                if (request.getLastSyncTime().longValue() > 0) {
                    syncDeleteResult.add(response);
                }
            } else if (request.getLastSyncTime().longValue() == 0) {
                syncInsertResult.add(response);
            } else if (response.getLastSyncDeviceSns().contains(request.getDeviceSn())) {
                if (response.getNewestDeviceSns().contains(request.getDeviceSn())) {
                    syncModifyResult.add(response);
                } else {
                    syncDeleteResult.add(response);
                }
            } else {
                syncInsertResult.add(response);
            }
        }
        TerminalSyncResponse terminalSyncResponse = new TerminalSyncResponse();
        terminalSyncResponse.setSyncInsertResult(syncInsertResult);
        terminalSyncResponse.setSyncDeleteResult(syncDeleteResult);
        terminalSyncResponse.setSyncModifyResult(syncModifyResult);
        Date date = this.memberCheckInTaskSyncMapper.getLastModify(request.getOrgId());
        terminalSyncResponse.setSyncTime(Long.valueOf(date == null ? 0L : date.getTime()));
        doCacheLastModifyTime(CacheKeyGenerateUtils.getCheckInTaskMemberLastModifyTimeKey(request.getDeviceSn()), terminalSyncResponse.getSyncTime());
        this.memberCheckInTaskSyncMapper.updateLastDeviceSnsByDevice(request.getDeviceSn(), request.getOrgId(), lastSyncTime);
        return terminalSyncResponse;
    }

    private void doCacheLastModifyTime(String key, Long lastModifyTime) {
        DeviceLastModifyTimeInfo info = CacheAdapter.getLastModifyTime(key);
        if (info == null) {
            info = new DeviceLastModifyTimeInfo();
        }
        info.setLastModifyTime(lastModifyTime);
        info.setExpireTime(Long.valueOf(MyDateUtils.addYears(new Date(), 30).getTime()));
        CacheAdapter.cacheLastModifyTime(key, info);
    }

    @Override // com.moredian.onpremise.api.checkIn.CheckInService
    public boolean delete(Date date) {
        logger.info("delete verify record :{}", Long.valueOf(date.getTime()));
        List<String> snapUrls = this.checkInLogMapper.listSnapUrl(date);
        for (String snapUrl : snapUrls) {
            MyFileUtils.deleteFile(this.uploadConfig.getFilePath() + snapUrl);
        }
        this.checkInLogMapper.deleteByDate(date);
        return true;
    }
}
