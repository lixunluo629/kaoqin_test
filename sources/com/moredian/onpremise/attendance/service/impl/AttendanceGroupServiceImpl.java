package com.moredian.onpremise.attendance.service.impl;

import com.github.pagehelper.PageHelper;
import com.moredian.onpremise.api.attendance.AttendanceGroupService;
import com.moredian.onpremise.api.member.DeptService;
import com.moredian.onpremise.api.member.MemberService;
import com.moredian.onpremise.core.adapter.CacheAdapter;
import com.moredian.onpremise.core.common.enums.AttendanceGroupDeviceTypeEnum;
import com.moredian.onpremise.core.common.enums.AttendanceGroupTypeEnum;
import com.moredian.onpremise.core.common.enums.MemberTypeEnum;
import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.mapper.AttendanceGroupDeviceMapper;
import com.moredian.onpremise.core.mapper.AttendanceGroupMapper;
import com.moredian.onpremise.core.mapper.AttendanceGroupMemberMapper;
import com.moredian.onpremise.core.mapper.AttendanceGroupTimeMapper;
import com.moredian.onpremise.core.mapper.DeptMapper;
import com.moredian.onpremise.core.mapper.DeviceMapper;
import com.moredian.onpremise.core.mapper.MemberAuthInfoMapper;
import com.moredian.onpremise.core.mapper.MemberMapper;
import com.moredian.onpremise.core.model.domain.AttendanceGroup;
import com.moredian.onpremise.core.model.domain.AttendanceGroupDevice;
import com.moredian.onpremise.core.model.domain.AttendanceGroupMember;
import com.moredian.onpremise.core.model.domain.AttendanceGroupTime;
import com.moredian.onpremise.core.model.domain.Dept;
import com.moredian.onpremise.core.model.domain.Device;
import com.moredian.onpremise.core.model.domain.Member;
import com.moredian.onpremise.core.model.dto.AttendanceGroupTimeDto;
import com.moredian.onpremise.core.model.dto.DeviceDto;
import com.moredian.onpremise.core.model.dto.GroupMemberDto;
import com.moredian.onpremise.core.model.dto.MemberDto;
import com.moredian.onpremise.core.model.info.AttendanceGroupMemberInfo;
import com.moredian.onpremise.core.model.info.DeviceLastModifyTimeInfo;
import com.moredian.onpremise.core.model.info.SyncAttendanceGroupTimeInfo;
import com.moredian.onpremise.core.model.info.UpdateAttendanceGroupDeviceInfo;
import com.moredian.onpremise.core.model.request.CheckMemberHasAttendanceGroupRequest;
import com.moredian.onpremise.core.model.request.DeleteAttendanceGroupRequest;
import com.moredian.onpremise.core.model.request.ListAttendanceGroupRequest;
import com.moredian.onpremise.core.model.request.QueryAttendanceGroupDetailRequest;
import com.moredian.onpremise.core.model.request.SaveAttendanceGroupRequest;
import com.moredian.onpremise.core.model.request.TerminalSyncRequest;
import com.moredian.onpremise.core.model.response.AttendanceGroupDetailResponse;
import com.moredian.onpremise.core.model.response.AttendanceGroupListResponse;
import com.moredian.onpremise.core.model.response.CheckMemberHasAttendanceGroupResponse;
import com.moredian.onpremise.core.model.response.ListDeptResponse;
import com.moredian.onpremise.core.model.response.QueryAttendanceGroupDateFrameResponse;
import com.moredian.onpremise.core.model.response.SaveAttendanceGroupResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncAttendanceGroupResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncResponse;
import com.moredian.onpremise.core.utils.AssertUtil;
import com.moredian.onpremise.core.utils.CacheKeyGenerateUtils;
import com.moredian.onpremise.core.utils.JsonUtils;
import com.moredian.onpremise.core.utils.MyDateUtils;
import com.moredian.onpremise.core.utils.MyListUtils;
import com.moredian.onpremise.core.utils.PageList;
import com.moredian.onpremise.core.utils.Paginator;
import com.moredian.onpremise.iot.handle.NettyMessageApi;
import com.moredian.onpremise.model.SyncAttendanceGroupRequest;
import com.moredian.onpremise.model.SyncMemberRequest;
import java.util.ArrayList;
import java.util.Date;
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

@Service
/* loaded from: onpremise-attendance-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/attendance/service/impl/AttendanceGroupServiceImpl.class */
public class AttendanceGroupServiceImpl implements AttendanceGroupService {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) AttendanceGroupServiceImpl.class);

    @Autowired
    private AttendanceGroupMapper groupMapper;

    @Autowired
    private AttendanceGroupTimeMapper groupTimeMapper;

    @Autowired
    private AttendanceGroupMemberMapper groupMemberMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private AttendanceGroupDeviceMapper groupDeviceMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private MemberService memberService;

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private MemberAuthInfoMapper memberAuthInfoMapper;

    @Autowired
    private NettyMessageApi nettyMessageApi;

    @Autowired
    private DeptService deptService;

    @Override // com.moredian.onpremise.api.attendance.AttendanceGroupService
    @Transactional(rollbackFor = {RuntimeException.class})
    public SaveAttendanceGroupResponse saveAttendanceGroup(SaveAttendanceGroupRequest request) throws BeansException {
        SaveAttendanceGroupResponse result = new SaveAttendanceGroupResponse();
        checkSaveAttendanceGroupParams(request);
        deleteHasAttendanceGroupMember(request.getAttendanceGroupId(), request.getMemberInfo(), request.getOrgId());
        List<DeviceDto> deviceDtos = new ArrayList<>();
        if (request.getAttendanceGroupId() != null) {
            AssertUtil.checkId(request.getAttendanceGroupId(), OnpremiseErrorEnum.ATTENDANCE_GROUP_ID_MUST_NOT_NULL);
            AttendanceGroup group = this.groupMapper.getOneById(request.getAttendanceGroupId(), request.getOrgId());
            AssertUtil.isNullOrEmpty(group, OnpremiseErrorEnum.ATTENDANCE_GROUP_NOT_FIND);
            AssertUtil.isTrue(Boolean.valueOf(this.groupMapper.updateAttendanceGroup(saveAttendanceGroupRequestToGroup(request)) > 0), OnpremiseErrorEnum.UPDATE_ATTENDANCE_GROUP_FAIL);
            updateGroupTime(request);
            UpdateAttendanceGroupDeviceInfo updateDeviceInfo = updateGroupDevice(request);
            updateGroupMember(request.getMemberInfo(), updateDeviceInfo, request.getOrgId(), request.getAttendanceGroupId());
            deviceDtos.addAll(updateDeviceInfo.getNewDevices());
            deviceDtos.addAll(updateDeviceInfo.getOldDevices());
        } else {
            AttendanceGroup attendanceGroup = saveAttendanceGroupRequestToGroup(request);
            AssertUtil.isTrue(Boolean.valueOf(this.groupMapper.insertAttendanceGroup(attendanceGroup) > 0), OnpremiseErrorEnum.SAVE_ATTENDANCE_GROUP_FAIL);
            doInsertAttendanceGroupTime(request.getGroupTimeInfo(), attendanceGroup.getAttendanceGroupId(), request.getOrgId());
            doInsertAttendanceGroupDevice(request.getAttendanceBeginDeviceInfo(), attendanceGroup.getAttendanceGroupId(), request.getOrgId(), Integer.valueOf(AttendanceGroupDeviceTypeEnum.START_WORK_DEVICE.getValue()));
            doInsertAttendanceGroupDevice(request.getAttendanceEndDeviceInfo(), attendanceGroup.getAttendanceGroupId(), request.getOrgId(), Integer.valueOf(AttendanceGroupDeviceTypeEnum.END_WORK_DEVICE.getValue()));
            List<GroupMemberDto> newMembers = request.getMemberInfo();
            ArrayList arrayList = new ArrayList();
            for (GroupMemberDto item : newMembers) {
                item.setConfirmFlag(1);
                if (item.getType().equals(Integer.valueOf(MemberTypeEnum.DEPT.getValue()))) {
                    this.deptService.packagingChildDept(item.getDeptId(), request.getOrgId(), arrayList);
                }
            }
            HashSet hashSet = new HashSet();
            List<Long> noRepeatDeptIds = new ArrayList<>();
            hashSet.addAll(arrayList);
            noRepeatDeptIds.addAll(hashSet);
            for (Long deptId : noRepeatDeptIds) {
                GroupMemberDto memberDto = new GroupMemberDto();
                memberDto.setType(Integer.valueOf(MemberTypeEnum.DEPT.getValue()));
                memberDto.setDeptId(deptId);
                memberDto.setMemberId(0L);
                memberDto.setConfirmFlag(0);
                if (!newMembers.contains(memberDto)) {
                    newMembers.add(memberDto);
                }
            }
            doInsertAttendanceGroupMember(newMembers, attendanceGroup.getAttendanceGroupId(), request.getOrgId(), request.getAttendanceBeginDeviceInfo());
            Set<Long> memberIdSet = new HashSet<>();
            Set<Long> deptIdSet = new HashSet<>();
            for (GroupMemberDto memberDto2 : newMembers) {
                if (memberDto2.getType().equals(Integer.valueOf(MemberTypeEnum.DEPT.getValue()))) {
                    deptIdSet.add(memberDto2.getDeptId());
                } else {
                    memberIdSet.add(memberDto2.getMemberId());
                }
            }
            if (!CollectionUtils.isEmpty(memberIdSet) || !CollectionUtils.isEmpty(deptIdSet)) {
                this.memberMapper.updateModifyTime(request.getOrgId(), new ArrayList(memberIdSet), new ArrayList(deptIdSet));
            }
            deviceDtos.addAll(request.getAttendanceBeginDeviceInfo());
            deviceDtos.addAll(request.getAttendanceEndDeviceInfo());
            request.setAttendanceGroupId(attendanceGroup.getAttendanceGroupId());
        }
        List<String> deviceSns = DeviceDto.distinctDeviceDto(deviceDtos);
        doSendGroupNetty(deviceSns, request.getOrgId());
        doSendMemberNetty(request.getOrgId());
        result.setAttendanceGroupId(request.getAttendanceGroupId());
        return result;
    }

    @Override // com.moredian.onpremise.api.attendance.AttendanceGroupService
    public AttendanceGroupDetailResponse queryAttendanceGroupDetail(QueryAttendanceGroupDetailRequest request) {
        return queryAttendanceGroupDetail(request.getAttendanceGroupId(), request.getOrgId());
    }

    @Override // com.moredian.onpremise.api.attendance.AttendanceGroupService
    public PageList<AttendanceGroupListResponse> listAttendanceGroup(ListAttendanceGroupRequest request) {
        AssertUtil.checkOrgId(request.getOrgId());
        Paginator paginator = request.getPaginator();
        if (Paginator.checkPaginator(paginator)) {
            PageHelper.startPage(paginator.getPageNum(), paginator.getPageSize());
            List<AttendanceGroupListResponse> listResponse = this.groupMapper.listAttendanceGroup(request);
            packageListAttendanceGroup(listResponse, request.getOrgId());
            return new PageList<>(listResponse);
        }
        List<AttendanceGroupListResponse> listResponse2 = this.groupMapper.listAttendanceGroup(request);
        packageListAttendanceGroup(listResponse2, request.getOrgId());
        return new PageList<>(Paginator.initPaginator(listResponse2), listResponse2);
    }

    @Override // com.moredian.onpremise.api.attendance.AttendanceGroupService
    @Transactional(rollbackFor = {RuntimeException.class})
    public boolean deleteAttendanceGroup(DeleteAttendanceGroupRequest request) {
        AssertUtil.checkOrgId(request.getOrgId());
        AssertUtil.checkId(request.getAttendanceGroupId(), OnpremiseErrorEnum.ATTENDANCE_GROUP_ID_MUST_NOT_NULL);
        List<DeviceDto> deviceDtos = listDeviceDtoByGroupId(request.getAttendanceGroupId(), request.getOrgId(), Integer.valueOf(AttendanceGroupDeviceTypeEnum.START_WORK_DEVICE.getValue()));
        deviceDtos.addAll(listDeviceDtoByGroupId(request.getAttendanceGroupId(), request.getOrgId(), Integer.valueOf(AttendanceGroupDeviceTypeEnum.END_WORK_DEVICE.getValue())));
        List<GroupMemberDto> memberDtoList = this.groupMemberMapper.listByAttendanceGroupIdAndType(null, request.getAttendanceGroupId(), request.getOrgId());
        List<Long> memberIdList = new ArrayList<>();
        List<Long> deptIdList = new ArrayList<>();
        for (GroupMemberDto memberDto : memberDtoList) {
            if (memberDto.getType().equals(Integer.valueOf(MemberTypeEnum.MEMBER.getValue()))) {
                memberIdList.add(memberDto.getMemberId());
            } else {
                deptIdList.add(memberDto.getDeptId());
            }
        }
        if (!CollectionUtils.isEmpty(memberIdList) || !CollectionUtils.isEmpty(deptIdList)) {
            this.memberMapper.updateModifyTime(request.getOrgId(), memberIdList, deptIdList);
        }
        this.groupMapper.deleteById(request.getAttendanceGroupId(), request.getOrgId());
        this.groupTimeMapper.deleteByAttendanceGroupId(request.getAttendanceGroupId(), request.getOrgId());
        this.groupMemberMapper.deleteByAttendanceGroupId(request.getAttendanceGroupId(), request.getOrgId());
        this.groupDeviceMapper.deleteByAttendanceGroupId(request.getAttendanceGroupId(), request.getOrgId());
        List<String> deviceSns = DeviceDto.distinctDeviceDto(deviceDtos);
        doSendGroupNetty(deviceSns, request.getOrgId());
        doSendMemberNetty(request.getOrgId());
        return true;
    }

    @Override // com.moredian.onpremise.api.attendance.AttendanceGroupService
    public TerminalSyncResponse<TerminalSyncAttendanceGroupResponse> syncAttendanceGroup(TerminalSyncRequest request) {
        AssertUtil.isNullOrEmpty(request.getDeviceSn(), OnpremiseErrorEnum.DEVICE_SN_MUST_NOT_NULL);
        String lastSyncTime = MyDateUtils.getDateString(request.getLastSyncTime().longValue(), "yyyy-MM-dd HH:mm:ss.SSS");
        Device device = this.deviceMapper.getDeviceInfoByDeviceSn(request.getDeviceSn());
        if (device == null) {
            logger.info("==========================device not find ");
            TerminalSyncResponse<TerminalSyncAttendanceGroupResponse> response = new TerminalSyncResponse<>();
            response.setSyncTime(CacheAdapter.getLastModifyTime(CacheKeyGenerateUtils.getAttendanceGroupLastModifyTimeKey(request.getDeviceSn())).getLastModifyTime());
            return response;
        }
        return packagingSyncAttendanceGroupResponse(request, lastSyncTime, device);
    }

    @Override // com.moredian.onpremise.api.attendance.AttendanceGroupService
    public List<CheckMemberHasAttendanceGroupResponse> checkMemberHasAttendanceGroup(CheckMemberHasAttendanceGroupRequest request) throws BeansException {
        List<CheckMemberHasAttendanceGroupResponse> responses = new ArrayList<>();
        logger.info("============request :{}", JsonUtils.toJson(request));
        if (request.getAttendanceGroupId() != null && request.getAttendanceGroupId().longValue() != 0) {
            doCheckModifyAttendanceMemberAndDept(request, responses);
        } else {
            doCheckInsertAttendanceMemberAndDept(request, responses);
        }
        return responses;
    }

    public boolean queryMemberAttendanceGroup(SaveAttendanceGroupRequest request) {
        Set<Long> memberIds = new HashSet<>();
        if (MyListUtils.listIsEmpty(request.getMemberInfo())) {
            for (GroupMemberDto dto : request.getMemberInfo()) {
                AssertUtil.isNullOrEmpty(dto.getType(), OnpremiseErrorEnum.ATTENDANCE_GROUP_MEMBER_TYPE_MUST_NOT_NULL);
                if (MemberTypeEnum.DEPT.getValue() == dto.getType().intValue()) {
                    AssertUtil.checkId(dto.getDeptId(), OnpremiseErrorEnum.DEPT_ID_MUST_NOT_NULL);
                    this.memberService.getMemberListByDept(request.getOrgId(), dto.getDeptId(), memberIds);
                } else {
                    AssertUtil.checkId(dto.getMemberId(), OnpremiseErrorEnum.MEMBER_ID_MUST_NOT_NULL);
                    memberIds.add(dto.getMemberId());
                }
            }
        }
        List<AttendanceGroupTimeDto> newTimeInfos = request.getGroupTimeInfo();
        Map<Long, Member> members = Member.listToMap(this.memberMapper.listMemberByMemberIds(new ArrayList(memberIds), request.getOrgId()));
        for (AttendanceGroupTimeDto newTimeInfo : newTimeInfos) {
            logger.info("=========time info for check ");
            if (compareTime(newTimeInfo.getAttendanceBeginTime(), newTimeInfo.getAttendanceEndTime())) {
                logger.info("=========begin time greater than end time ");
                AssertUtil.isTrue(Boolean.valueOf((MyDateUtils.getHours(newTimeInfo.getAttendanceEndTime()) - newTimeInfo.getAttendanceEndAfter().intValue()) + 24 > MyDateUtils.getHours(newTimeInfo.getAttendanceBeginTime()) + newTimeInfo.getAttendanceBeginAfter().intValue()), OnpremiseErrorEnum.ATTENDANCE_TIME_BEGIN_NOT_GREATER_END);
            } else {
                logger.info("=========end time greater than begin time ");
                AssertUtil.isTrue(Boolean.valueOf(MyDateUtils.getHours(newTimeInfo.getAttendanceEndTime()) - newTimeInfo.getAttendanceEndAfter().intValue() > MyDateUtils.getHours(newTimeInfo.getAttendanceBeginTime()) + newTimeInfo.getAttendanceBeginAfter().intValue()), OnpremiseErrorEnum.ATTENDANCE_TIME_BEGIN_NOT_GREATER_END);
            }
            int startHours = MyDateUtils.getHours(newTimeInfo.getAttendanceBeginTime()) - newTimeInfo.getAttendanceBeginBefore().intValue();
            int endHours = MyDateUtils.getHours(newTimeInfo.getAttendanceEndTime()) + newTimeInfo.getAttendanceEndAfter().intValue();
            if (compareTime(newTimeInfo.getAttendanceBeginTime(), newTimeInfo.getAttendanceEndTime())) {
                endHours += 24;
            }
            for (Long memberId : memberIds) {
                List<AttendanceGroupTimeDto> timeInfos = this.groupTimeMapper.listByAttendanceMemberId(memberId, request.getOrgId());
                Member member = members.get(memberId);
                AssertUtil.isNullOrEmpty(member, OnpremiseErrorEnum.MEMBER_NOT_FIND);
                for (AttendanceGroupTimeDto timeInfo : timeInfos) {
                    if (request.getAttendanceGroupId() == null || request.getAttendanceGroupId().longValue() != timeInfo.getAttendanceGroupId().longValue()) {
                        int queryStartHours = MyDateUtils.getHours(timeInfo.getAttendanceBeginTime()) - timeInfo.getAttendanceBeginBefore().intValue();
                        int queryEndHours = MyDateUtils.getHours(timeInfo.getAttendanceEndTime()) + timeInfo.getAttendanceEndAfter().intValue();
                        if (compareTime(timeInfo.getAttendanceBeginTime(), timeInfo.getAttendanceEndTime())) {
                            queryEndHours += 24;
                        }
                        AssertUtil.isTrue(Boolean.valueOf(startHours < queryStartHours && endHours <= queryEndHours), OnpremiseErrorEnum.ATTENDANCE_GROUP_MEMBER_TIME_CONFLICT, member.getMemberName() + "_" + member.getMemberJobNum());
                        AssertUtil.isTrue(Boolean.valueOf(startHours >= queryEndHours && endHours > queryEndHours), OnpremiseErrorEnum.ATTENDANCE_GROUP_MEMBER_TIME_CONFLICT, member.getMemberName() + "_" + member.getMemberJobNum());
                        AssertUtil.isTrue(Boolean.valueOf(startHours == queryStartHours && endHours == queryEndHours), OnpremiseErrorEnum.ATTENDANCE_GROUP_MEMBER_TIME_CONFLICT, member.getMemberName() + "_" + member.getMemberJobNum());
                    }
                }
            }
        }
        return true;
    }

    private void checkSaveAttendanceGroupParams(SaveAttendanceGroupRequest request) {
        AssertUtil.checkOrgId(request.getOrgId());
        AssertUtil.isNullOrEmpty(request.getGroupName(), OnpremiseErrorEnum.ATTENDANCE_GROUP_NAME_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getGroupType(), OnpremiseErrorEnum.ATTENDANCE_GROUP_TYPE_MUST_NOT_NULL);
        AssertUtil.isTrue(Boolean.valueOf(MyListUtils.listIsEmpty(request.getMemberInfo())), OnpremiseErrorEnum.ATTENDANCE_GROUP_MEMBER_MUST_NOT_NULL);
        AssertUtil.isTrue(Boolean.valueOf(MyListUtils.listIsEmpty(request.getAttendanceEndDeviceInfo())), OnpremiseErrorEnum.ATTENDANCE_GROUP_END_DEVICE_MUST_NOT_NULL);
        AssertUtil.isTrue(Boolean.valueOf(MyListUtils.listIsEmpty(request.getAttendanceBeginDeviceInfo())), OnpremiseErrorEnum.ATTENDANCE_GROUP_START_DEVICE_MUST_NOT_NULL);
        if (AttendanceGroupTypeEnum.MANUAL_TYPE.getValue() == request.getGroupType().intValue()) {
            AssertUtil.isNullOrEmpty(request.getGroupStartDate(), OnpremiseErrorEnum.ATTENDANCE_START_DATE_MUST_NOT_NULL);
            AssertUtil.isNullOrEmpty(request.getGroupEndDate(), OnpremiseErrorEnum.ATTENDANCE_END_DATE_MUST_NOT_NULL);
        }
        AssertUtil.isNullOrEmpty(request.getGroupName(), OnpremiseErrorEnum.GROUP_NAME_MUST_NOT_NULL);
        AttendanceGroup group = this.groupMapper.getOneByGroupName(request.getGroupName(), request.getOrgId());
        if (request.getAttendanceGroupId() == null || request.getAttendanceGroupId().longValue() <= 0) {
            AssertUtil.isTrue(Boolean.valueOf(group == null), OnpremiseErrorEnum.ATTENDANCE_GROUP_NAME_ALREADY_EXIST);
        } else {
            AssertUtil.isTrue(Boolean.valueOf(group == null || request.getAttendanceGroupId().longValue() == group.getAttendanceGroupId().longValue()), OnpremiseErrorEnum.ATTENDANCE_GROUP_NAME_ALREADY_EXIST);
        }
    }

    private void checkTime(String time) {
        AssertUtil.isNullOrEmpty(MyDateUtils.parseDate(time, "hh:mm"), OnpremiseErrorEnum.ATTENDANCE_GROUP_TIME_MUST_NOT_NULL);
    }

    private boolean compareTime(String beginTimeStr, String endTimeStr) {
        Date beginTime = MyDateUtils.parseDate(beginTimeStr, "hh:mm");
        Date endTime = MyDateUtils.parseDate(endTimeStr, "hh:mm");
        return beginTime.getTime() > endTime.getTime();
    }

    private void packageListAttendanceGroup(List<AttendanceGroupListResponse> responses, Long orgId) {
        if (MyListUtils.listIsEmpty(responses)) {
            for (AttendanceGroupListResponse response : responses) {
                Integer memberNum = 0;
                List<Long> memberIds = new ArrayList<>();
                List<GroupMemberDto> members = this.groupMemberMapper.listByAttendanceGroupIdAndType(Integer.valueOf(MemberTypeEnum.DEPT.getValue()), orgId, response.getAttendanceGroupId());
                if (MyListUtils.listIsEmpty(members)) {
                    for (GroupMemberDto dto : members) {
                        if (MemberTypeEnum.MEMBER.getValue() == dto.getType().intValue()) {
                            memberIds.add(dto.getDeptId());
                        }
                    }
                }
                Integer memberNum2 = Integer.valueOf(memberNum.intValue() + memberIds.size());
                List<Long> deptIds = new ArrayList<>();
                List<GroupMemberDto> depts = this.groupMemberMapper.listByAttendanceGroupIdAndType(Integer.valueOf(MemberTypeEnum.DEPT.getValue()), orgId, response.getAttendanceGroupId());
                if (MyListUtils.listIsEmpty(depts)) {
                    for (GroupMemberDto dto2 : depts) {
                        if (MemberTypeEnum.DEPT.getValue() == dto2.getType().intValue()) {
                            deptIds.add(dto2.getDeptId());
                        }
                    }
                }
                if (deptIds.size() > 0) {
                    memberNum2 = Integer.valueOf(memberNum2.intValue() + this.memberMapper.countMemberByDepts(deptIds, memberIds, orgId).intValue());
                }
                List<GroupMemberDto> memberDtos = this.groupMemberMapper.listByAttendanceGroupIdAndType(Integer.valueOf(MemberTypeEnum.MEMBER.getValue()), orgId, response.getAttendanceGroupId());
                response.setMemberNums(Integer.valueOf(memberNum2.intValue() + memberDtos.size()));
            }
        }
    }

    private void doSendGroupNetty(List<String> deviceSns, Long orgId) {
        if (MyListUtils.listIsEmpty(deviceSns)) {
            AttendanceGroup group = this.groupMapper.getLastOne(orgId);
            for (String deviceSn : deviceSns) {
                this.nettyMessageApi.sendMsg(new SyncAttendanceGroupRequest(Long.valueOf(group == null ? 0L : group.getGmtModify().getTime()), orgId), Integer.valueOf(SyncAttendanceGroupRequest.MODEL_TYPE.type()), deviceSn);
            }
        }
    }

    private void doSendMemberNetty(Long orgId) {
        Member member = this.memberMapper.getLastModify(orgId);
        List<String> deviceSnList = this.deviceMapper.listDeviceSn(orgId);
        if (CollectionUtils.isEmpty(deviceSnList)) {
            return;
        }
        this.nettyMessageApi.sendMsg(new SyncMemberRequest(Long.valueOf(member == null ? 0L : member.getGmtModify().getTime()), orgId), Integer.valueOf(SyncMemberRequest.MODEL_TYPE.type()), deviceSnList);
    }

    private AttendanceGroup saveAttendanceGroupRequestToGroup(SaveAttendanceGroupRequest request) throws BeansException {
        AttendanceGroup group = new AttendanceGroup();
        BeanUtils.copyProperties(request, group);
        return group;
    }

    private void doInsertAttendanceGroupDevice(List<DeviceDto> deviceInfo, Long attendanceGroupId, Long orgId, Integer type) {
        if (MyListUtils.listIsEmpty(deviceInfo)) {
            for (DeviceDto dto : deviceInfo) {
                AttendanceGroupDevice device = new AttendanceGroupDevice();
                device.setAttendanceGroupId(attendanceGroupId);
                device.setDeviceType(type);
                device.setOrgId(orgId);
                device.setDeviceSn(dto.getDeviceSn());
                this.groupDeviceMapper.insertAttendanceGroupDevice(device);
            }
        }
    }

    private void doInsertAttendanceGroupMember(List<GroupMemberDto> memberInfo, Long attendanceGroupId, Long orgId, List<DeviceDto> insertDevices) throws BeansException {
        if (MyListUtils.listIsEmpty(memberInfo)) {
            for (GroupMemberDto dto : memberInfo) {
                AttendanceGroupMember member = new AttendanceGroupMember();
                BeanUtils.copyProperties(dto, member);
                member.setAttendanceGroupId(attendanceGroupId);
                member.setOrgId(orgId);
                this.groupMemberMapper.insertAttendanceGroupMember(member);
            }
        }
    }

    private void insertChildDeptGroup(Long orgId, AttendanceGroupMember groupDept, List<DeviceDto> insertDevices) {
        List<Dept> childDepts = this.deptMapper.listChildDeptToReturnDept(groupDept.getDeptId(), orgId);
        logger.info("==============child dept is :{}", JsonUtils.toJson(childDepts));
        if (childDepts != null && childDepts.size() > 0) {
            for (Dept childDept : childDepts) {
                groupDept.setDeptId(childDept.getDeptId());
                groupDept.setConfirmFlag(0);
                logger.info("===============insert child dept ");
                insertChildDeptGroup(orgId, groupDept, insertDevices);
            }
        }
    }

    private void doInsertAttendanceGroupTime(List<AttendanceGroupTimeDto> groupTimes, Long attendanceGroupId, Long orgId) throws BeansException {
        if (MyListUtils.listIsEmpty(groupTimes)) {
            List<AttendanceGroupTime> times = new ArrayList<>();
            for (AttendanceGroupTimeDto dto : groupTimes) {
                checkTime(dto.getAttendanceBeginTime());
                checkTime(dto.getAttendanceEndTime());
                AttendanceGroupTime time = new AttendanceGroupTime();
                BeanUtils.copyProperties(dto, time);
                time.setAttendanceGroupId(attendanceGroupId);
                time.setOrgId(orgId);
                times.add(time);
            }
            if (MyListUtils.listIsEmpty(times)) {
                this.groupTimeMapper.batchInsertAttendanceGroupTime(times);
            }
        }
    }

    private boolean updateGroupTime(SaveAttendanceGroupRequest request) throws BeansException {
        List<AttendanceGroupTimeDto> newAuths = request.getGroupTimeInfo();
        List<AttendanceGroupTimeDto> oldAuths = this.groupTimeMapper.listByAttendanceGroupId(request.getAttendanceGroupId(), request.getOrgId());
        MyListUtils<AttendanceGroupTimeDto> memberUtils = new MyListUtils<>();
        List<AttendanceGroupTimeDto> insertAuths = memberUtils.difference(newAuths, oldAuths);
        List<AttendanceGroupTimeDto> deleteAuths = memberUtils.difference(oldAuths, newAuths);
        if (MyListUtils.listIsEmpty(deleteAuths) || MyListUtils.listIsEmpty(insertAuths)) {
            AssertUtil.isTrue(Boolean.valueOf(this.groupTimeMapper.deleteByAttendanceGroupId(request.getAttendanceGroupId(), request.getOrgId()) > 0), OnpremiseErrorEnum.DELETE_GROUP_AUTH_FAIL);
            doInsertAttendanceGroupTime(newAuths, request.getAttendanceGroupId(), request.getOrgId());
            return true;
        }
        return true;
    }

    private UpdateAttendanceGroupDeviceInfo updateGroupDevice(SaveAttendanceGroupRequest request) {
        MyListUtils<DeviceDto> deviceUtils = new MyListUtils<>();
        List<DeviceDto> newBeginDevices = request.getAttendanceBeginDeviceInfo();
        List<DeviceDto> oldBeginDevices = listDeviceDtoByGroupId(request.getAttendanceGroupId(), request.getOrgId(), Integer.valueOf(AttendanceGroupDeviceTypeEnum.START_WORK_DEVICE.getValue()));
        List<DeviceDto> insertBeginDevices = deviceUtils.difference(newBeginDevices, oldBeginDevices);
        List<DeviceDto> deleteBeginDevices = deviceUtils.difference(oldBeginDevices, newBeginDevices);
        logger.info("===========newBeginDevices :{}", JsonUtils.toJson(newBeginDevices));
        logger.info("============oldBeginDevices :{}", JsonUtils.toJson(oldBeginDevices));
        logger.info("===========insertBeginDevices :{} , deleteBeginDevices :{}", JsonUtils.toJson(insertBeginDevices), JsonUtils.toJson(deleteBeginDevices));
        if (MyListUtils.listIsEmpty(deleteBeginDevices)) {
            for (DeviceDto dto : deleteBeginDevices) {
                this.groupDeviceMapper.deleteByAttendanceGroupIdAndDeviceSn(request.getAttendanceGroupId(), request.getOrgId(), Integer.valueOf(AttendanceGroupDeviceTypeEnum.START_WORK_DEVICE.getValue()), dto.getDeviceSn());
            }
        }
        doInsertAttendanceGroupDevice(insertBeginDevices, request.getAttendanceGroupId(), request.getOrgId(), Integer.valueOf(AttendanceGroupDeviceTypeEnum.START_WORK_DEVICE.getValue()));
        List<DeviceDto> newEndDevices = request.getAttendanceEndDeviceInfo();
        List<DeviceDto> oldEndDevices = listDeviceDtoByGroupId(request.getAttendanceGroupId(), request.getOrgId(), Integer.valueOf(AttendanceGroupDeviceTypeEnum.END_WORK_DEVICE.getValue()));
        List<DeviceDto> insertEndDevices = deviceUtils.difference(newEndDevices, oldEndDevices);
        List<DeviceDto> deleteEndDevices = deviceUtils.difference(oldEndDevices, newEndDevices);
        logger.info("===========newEndDevices :{}", JsonUtils.toJson(newBeginDevices));
        logger.info("============oldEndDevices :{}", JsonUtils.toJson(oldBeginDevices));
        logger.info("===========insertEndDevices :{} , deleteEndDevices :{}", JsonUtils.toJson(insertBeginDevices), JsonUtils.toJson(deleteBeginDevices));
        if (MyListUtils.listIsEmpty(deleteEndDevices)) {
            for (DeviceDto dto2 : deleteEndDevices) {
                this.groupDeviceMapper.deleteByAttendanceGroupIdAndDeviceSn(request.getAttendanceGroupId(), request.getOrgId(), Integer.valueOf(AttendanceGroupDeviceTypeEnum.END_WORK_DEVICE.getValue()), dto2.getDeviceSn());
            }
        }
        doInsertAttendanceGroupDevice(insertEndDevices, request.getAttendanceGroupId(), request.getOrgId(), Integer.valueOf(AttendanceGroupDeviceTypeEnum.END_WORK_DEVICE.getValue()));
        return new UpdateAttendanceGroupDeviceInfo(deviceUtils.intersection(newBeginDevices, newEndDevices), deviceUtils.intersection(oldBeginDevices, oldEndDevices));
    }

    private boolean updateGroupMember(List<GroupMemberDto> memberInfo, UpdateAttendanceGroupDeviceInfo deviceInfo, Long orgId, Long attendanceGroupId) throws BeansException {
        Set<Long> memberIdSet = new HashSet<>();
        Set<Long> deptIdSet = new HashSet<>();
        ArrayList arrayList = new ArrayList();
        Iterator<GroupMemberDto> iterator = memberInfo.iterator();
        while (iterator.hasNext()) {
            GroupMemberDto item = iterator.next();
            item.setConfirmFlag(1);
            if (item.getType().equals(Integer.valueOf(MemberTypeEnum.DEPT.getValue()))) {
                Dept deptFlag = this.deptMapper.getDeptById(item.getDeptId());
                if (deptFlag == null) {
                    iterator.remove();
                } else {
                    this.deptService.packagingChildDept(item.getDeptId(), orgId, arrayList);
                }
            } else {
                Member memberFlag = this.memberMapper.getMemberInfoByMemberId(item.getMemberId(), orgId);
                if (memberFlag == null) {
                    iterator.remove();
                }
            }
        }
        HashSet hashSet = new HashSet();
        List<Long> noRepeatDeptIds = new ArrayList<>();
        hashSet.addAll(arrayList);
        noRepeatDeptIds.addAll(hashSet);
        for (Long deptId : noRepeatDeptIds) {
            GroupMemberDto memberDto = new GroupMemberDto();
            memberDto.setType(Integer.valueOf(MemberTypeEnum.DEPT.getValue()));
            memberDto.setDeptId(deptId);
            memberDto.setMemberId(0L);
            memberDto.setConfirmFlag(0);
            if (!memberInfo.contains(memberDto)) {
                memberInfo.add(memberDto);
            }
        }
        List<GroupMemberDto> oldMembers = this.groupMemberMapper.listByAttendanceGroupIdAndType(null, orgId, attendanceGroupId);
        MyListUtils<GroupMemberDto> memberUtils = new MyListUtils<>();
        List<GroupMemberDto> newMembers = memberUtils.removeDuplicate(memberInfo);
        List<GroupMemberDto> oldMembers2 = memberUtils.removeDuplicate(oldMembers);
        logger.info("===========newMembers :{}", Integer.valueOf(newMembers.size()));
        logger.info("============oldMembers :{}", Integer.valueOf(oldMembers2.size()));
        List<GroupMemberDto> insertMembers = memberUtils.difference(newMembers, oldMembers2);
        List<GroupMemberDto> deleteMembers = memberUtils.difference(oldMembers2, newMembers);
        List<GroupMemberDto> repeatMembers = memberUtils.union(oldMembers2, newMembers);
        logger.info("===========insertMembers size :{} , deleteMembers size :{}, repeatMembers size :{}", Integer.valueOf(insertMembers.size()), Integer.valueOf(deleteMembers.size()), Integer.valueOf(repeatMembers.size()));
        if (MyListUtils.listIsEmpty(deleteMembers)) {
            logger.info("================update delete member auth info");
            for (GroupMemberDto dto : deleteMembers) {
                if (MemberTypeEnum.MEMBER.getValue() == dto.getType().intValue()) {
                    this.groupMemberMapper.deleteGroupMember(dto.getMemberId(), orgId, attendanceGroupId);
                    memberIdSet.add(dto.getMemberId());
                } else {
                    this.groupMemberMapper.deleteGroupDept(dto.getDeptId(), orgId, attendanceGroupId);
                    deptIdSet.add(dto.getDeptId());
                }
            }
            logger.info("=========delete member ids :{}", Integer.valueOf(deleteMembers.size()));
        }
        doInsertAttendanceGroupMember(insertMembers, attendanceGroupId, orgId, deviceInfo.getNewDevices());
        for (GroupMemberDto memberDto2 : insertMembers) {
            if (memberDto2.getType().equals(Integer.valueOf(MemberTypeEnum.DEPT.getValue()))) {
                deptIdSet.add(memberDto2.getDeptId());
            } else {
                memberIdSet.add(memberDto2.getMemberId());
            }
        }
        if (!CollectionUtils.isEmpty(memberIdSet) || !CollectionUtils.isEmpty(deptIdSet)) {
            this.memberMapper.updateModifyTime(orgId, new ArrayList(memberIdSet), new ArrayList(deptIdSet));
            return true;
        }
        return true;
    }

    private void passDept(List<MemberDto> memberDtos, Long orgId) {
        List<MemberDto> temp = new ArrayList<>();
        temp.addAll(memberDtos);
        for (MemberDto dto : temp) {
            doPassDept(memberDtos, orgId, dto);
        }
    }

    private void doPassDept(List<MemberDto> memberDtos, Long orgId, MemberDto dto) {
        List<Dept> childDepts = this.deptMapper.listChildDeptToReturnDept(Long.valueOf(Long.parseLong(dto.getDeptId())), orgId);
        if (childDepts != null && childDepts.size() > 0) {
            MemberDto var = new MemberDto();
            for (Dept childDept : childDepts) {
                var.setDeptId(childDept.getDeptId().toString());
                var.setType(Integer.valueOf(MemberTypeEnum.DEPT.getValue()));
                if (memberDtos.contains(var)) {
                    memberDtos.remove(var);
                }
                doPassDept(memberDtos, orgId, var);
            }
        }
    }

    private void deleteHasAttendanceGroupMember(Long attendanceGroupId, List<GroupMemberDto> memberInfo, Long orgId) throws BeansException {
        List<CheckMemberHasAttendanceGroupResponse> responses = new ArrayList<>();
        doCheckAttendanceMember(getMemberIdFromGroupMemberDto(memberInfo), responses, orgId, attendanceGroupId);
        doCheckAttendanceDept(getDeptIdFromGroupMemberDto(memberInfo), responses, orgId, attendanceGroupId);
        Set<Long> attendanceGroupIds = new HashSet<>();
        if (MyListUtils.listIsEmpty(responses)) {
            Set<Long> insertDepts = new HashSet<>();
            Set<Long> deleteDepts = new HashSet<>();
            Set<Long> insertMembers = new HashSet<>();
            Set<Long> deleteMembers = new HashSet<>();
            MyListUtils<DeviceDto> deviceUtils = new MyListUtils<>();
            for (CheckMemberHasAttendanceGroupResponse response : responses) {
                logger.info("group member type :{}", response.getType());
                if (MemberTypeEnum.MEMBER.getValue() == response.getType().intValue()) {
                    deleteGroupMember(response.getMemberId(), orgId, attendanceGroupId, insertMembers, deleteMembers, insertDepts, deleteDepts);
                } else if (MemberTypeEnum.DEPT.getValue() == response.getType().intValue()) {
                    deleteDeptGroupAndAuth(response.getDeptId(), orgId, deleteDepts, insertDepts, insertMembers);
                }
                logger.info("==============insert dept :{} , insert member :{}", JsonUtils.toJson(insertDepts), JsonUtils.toJson(insertMembers));
                List<DeviceDto> beginDevice = listDeviceDtoByGroupId(response.getAttendanceGroupId(), orgId, Integer.valueOf(AttendanceGroupDeviceTypeEnum.START_WORK_DEVICE.getValue()));
                List<DeviceDto> endDevice = listDeviceDtoByGroupId(response.getAttendanceGroupId(), orgId, Integer.valueOf(AttendanceGroupDeviceTypeEnum.END_WORK_DEVICE.getValue()));
                updateGroupMember(packageMemberInfoFromList(insertDepts, insertMembers, deleteDepts, deleteMembers, orgId, response.getAttendanceGroupId()), new UpdateAttendanceGroupDeviceInfo(deviceUtils.intersection(beginDevice, endDevice), deviceUtils.intersection(beginDevice, endDevice)), orgId, response.getAttendanceGroupId());
                attendanceGroupIds.add(response.getAttendanceGroupId());
                insertDepts.clear();
                deleteDepts.clear();
                insertMembers.clear();
                deleteMembers.clear();
            }
        }
    }

    private List<GroupMemberDto> packageMemberInfoFromList(Set<Long> insertDepts, Set<Long> insertMembers, Set<Long> deleteDepts, Set<Long> deleteMembers, Long orgId, Long attendanceGroupId) {
        List<GroupMemberDto> memberInfo = this.groupMemberMapper.listByAttendanceGroupIdAndType(null, orgId, attendanceGroupId);
        if (insertDepts != null && insertDepts.size() > 0) {
            for (Long deptId : insertDepts) {
                GroupMemberDto dto = new GroupMemberDto();
                dto.setType(Integer.valueOf(MemberTypeEnum.DEPT.getValue()));
                dto.setDeptId(deptId);
                memberInfo.add(dto);
            }
        }
        if (insertMembers != null && insertMembers.size() > 0) {
            for (Long memberId : insertMembers) {
                GroupMemberDto dto2 = new GroupMemberDto();
                dto2.setType(Integer.valueOf(MemberTypeEnum.MEMBER.getValue()));
                dto2.setMemberId(memberId);
                memberInfo.add(dto2);
            }
        }
        if (deleteDepts != null && deleteDepts.size() > 0) {
            for (Long deptId2 : deleteDepts) {
                GroupMemberDto dto3 = new GroupMemberDto();
                dto3.setType(Integer.valueOf(MemberTypeEnum.DEPT.getValue()));
                dto3.setDeptId(deptId2);
                if (memberInfo.contains(dto3)) {
                    memberInfo.remove(dto3);
                }
            }
        }
        if (deleteMembers != null && deleteMembers.size() > 0) {
            for (Long memberId2 : deleteMembers) {
                GroupMemberDto dto4 = new GroupMemberDto();
                dto4.setType(Integer.valueOf(MemberTypeEnum.MEMBER.getValue()));
                dto4.setMemberId(memberId2);
                if (memberInfo.contains(dto4)) {
                    memberInfo.remove(dto4);
                }
            }
        }
        logger.debug("=============new  memberInfo :{}", JsonUtils.toJson(memberInfo));
        return memberInfo;
    }

    private List<Long> getMemberIdFromGroupMemberDto(List<GroupMemberDto> memberInfo) {
        List<Long> memberIds = new ArrayList<>();
        if (MyListUtils.listIsEmpty(memberInfo)) {
            for (GroupMemberDto dto : memberInfo) {
                if (MemberTypeEnum.MEMBER.getValue() == dto.getType().intValue()) {
                    memberIds.add(dto.getMemberId());
                }
            }
        }
        return memberIds;
    }

    private List<Long> getDeptIdFromGroupMemberDto(List<GroupMemberDto> memberInfo) {
        List<Long> deptIds = new ArrayList<>();
        if (MyListUtils.listIsEmpty(memberInfo)) {
            for (GroupMemberDto dto : memberInfo) {
                if (MemberTypeEnum.DEPT.getValue() == dto.getType().intValue()) {
                    deptIds.add(dto.getDeptId());
                }
            }
        }
        return deptIds;
    }

    private void deleteChildDeptGroup(Long deptId, Long orgId, Long groupId) {
        List<ListDeptResponse> childDepts = this.deptMapper.listChildDept(deptId, orgId);
        if (MyListUtils.listIsEmpty(childDepts)) {
            for (ListDeptResponse response : childDepts) {
                this.groupMemberMapper.deleteGroupDept(response.getDeptId(), orgId, groupId);
                deleteChildDeptGroup(response.getDeptId(), orgId, groupId);
            }
        }
    }

    private void deleteGroupMember(Long memberId, Long orgId, Long attendanceGroupId, Set<Long> insertMembers, Set<Long> deleteMembers, Set<Long> insertDepts, Set<Long> deleteDepts) {
        AttendanceGroupMemberInfo groupMember = this.groupMemberMapper.getOneByMemberId(memberId, orgId);
        if (groupMember != null) {
            if (attendanceGroupId == null || attendanceGroupId.longValue() == 0 || groupMember.getAttendanceGroupId().longValue() != attendanceGroupId.longValue()) {
                if (MemberTypeEnum.DEPT.getValue() == groupMember.getType().intValue()) {
                    List<Long> otherMemberIds = this.memberMapper.listOtherMemberIdByDeptId(groupMember.getDeptId(), orgId, memberId);
                    insertMembers.addAll(otherMemberIds);
                    deleteDepts.add(groupMember.getDeptId());
                    List<Long> otherDeptIds = this.deptMapper.listChildDeptIds(groupMember.getDeptId(), orgId);
                    insertDepts.addAll(otherDeptIds);
                    deleteDeptGroupAndAuth(groupMember.getDeptId(), orgId, deleteDepts, insertDepts, insertMembers);
                }
                deleteMembers.add(memberId);
            }
        }
    }

    private void deleteSuperDeptGroupAndAuth(Long deptId, Long superDeptId, Long orgId, Set<Long> deleteDepts, Set<Long> insertDepts, Set<Long> insertMembers) {
        AttendanceGroupMemberInfo groupDept = this.groupMemberMapper.getOneByDeptId(superDeptId, orgId);
        if (groupDept != null) {
            List<Long> otherDeptIds = this.deptMapper.listOtherChildDeptIds(superDeptId, orgId, deptId);
            insertDepts.addAll(otherDeptIds);
            List<Long> otherMemberIds = this.memberMapper.listMemberIdByDeptId(superDeptId, orgId);
            insertMembers.addAll(otherMemberIds);
            Dept dept = this.deptMapper.getDeptById(superDeptId);
            if (dept != null) {
                deleteDepts.add(dept.getDeptId());
                deleteSuperDeptGroupAndAuth(dept.getDeptId(), dept.getSuperDeptId(), orgId, deleteDepts, insertDepts, insertMembers);
            }
        }
    }

    private void deleteDeptGroupAndAuth(Long deptId, Long orgId, Set<Long> deleteDepts, Set<Long> insertDepts, Set<Long> insertMembers) {
        AttendanceGroupMemberInfo groupDept = this.groupMemberMapper.getOneByDeptId(deptId, orgId);
        if (groupDept != null) {
            deleteDepts.add(deptId);
        }
        Dept dept = this.deptMapper.getDeptById(deptId);
        deleteSuperDeptGroupAndAuth(dept.getDeptId(), dept.getSuperDeptId(), orgId, deleteDepts, insertDepts, insertMembers);
    }

    private TerminalSyncResponse<TerminalSyncAttendanceGroupResponse> packagingSyncAttendanceGroupResponse(TerminalSyncRequest request, String lastSyncTime, Device device) {
        TerminalSyncResponse<TerminalSyncAttendanceGroupResponse> terminalSyncResponse = new TerminalSyncResponse<>();
        List<Long> groupIds = this.groupDeviceMapper.listGroupIdByDeviceSn(device.getDeviceSn(), device.getOrgId());
        logger.info("attendanceGroupIds :{},lastSyncTime :{},orgId :{}", groupIds, lastSyncTime, request.getOrgId());
        if (CollectionUtils.isEmpty(groupIds)) {
            terminalSyncResponse.setSyncTime(CacheAdapter.getLastModifyTime(CacheKeyGenerateUtils.getAttendanceGroupLastModifyTimeKey(request.getDeviceSn())).getLastModifyTime());
            return terminalSyncResponse;
        }
        List<TerminalSyncAttendanceGroupResponse> syncGroupList = this.groupMapper.listSyncAttendanceGroup(request.getOrgId(), lastSyncTime, groupIds);
        logger.info("==============sync attendance group list size :{}=============", Integer.valueOf(syncGroupList.size()));
        if (CollectionUtils.isEmpty(syncGroupList)) {
            terminalSyncResponse.setSyncTime(CacheAdapter.getLastModifyTime(CacheKeyGenerateUtils.getAttendanceGroupLastModifyTimeKey(request.getDeviceSn())).getLastModifyTime());
            return terminalSyncResponse;
        }
        List<TerminalSyncAttendanceGroupResponse> syncInsertGroupList = new ArrayList<>();
        List<TerminalSyncAttendanceGroupResponse> syncModifyGroupList = new ArrayList<>();
        List<TerminalSyncAttendanceGroupResponse> syncDeleteGroupList = new ArrayList<>();
        for (TerminalSyncAttendanceGroupResponse response : syncGroupList) {
            List<AttendanceGroupDevice> groupDevice = this.groupDeviceMapper.getByDeviceAndGroup(request.getDeviceSn(), request.getOrgId(), response.getAttendanceGroupId());
            response.setGroupTimeInfo(packageSyncAttendanceGroupTime(response.getAttendanceGroupId(), request.getOrgId(), groupDevice, response.getGroupType()));
            for (AttendanceGroupDevice eachDevice : groupDevice) {
                if (response.getGmtModify().getTime() > request.getLastSyncTime().longValue() || eachDevice.getGmtModify().getTime() > request.getLastSyncTime().longValue()) {
                    if (response.getDeleteOrNot().intValue() == 1 || eachDevice.getDeleteOrNot().intValue() == 1) {
                        syncDeleteGroupList.add(response);
                    } else if (response.getGmtModify().getTime() == response.getGmtCreate().getTime()) {
                        syncInsertGroupList.add(response);
                    } else {
                        syncModifyGroupList.add(response);
                    }
                }
            }
        }
        AttendanceGroup group = this.groupMapper.getLastOne(device.getOrgId());
        terminalSyncResponse.setSyncTime(Long.valueOf(group == null ? 0L : group.getGmtModify().getTime()));
        doCacheLastModifyTime(CacheKeyGenerateUtils.getAttendanceGroupLastModifyTimeKey(request.getDeviceSn()), terminalSyncResponse.getSyncTime());
        terminalSyncResponse.setSyncDeleteResult(syncDeleteGroupList);
        terminalSyncResponse.setSyncInsertResult(syncInsertGroupList);
        terminalSyncResponse.setSyncModifyResult(syncModifyGroupList);
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

    private List<SyncAttendanceGroupTimeInfo> packageSyncAttendanceGroupTime(Long attendanceGroupId, Long orgId, List<AttendanceGroupDevice> groupDevice, Integer groupType) throws BeansException {
        List<AttendanceGroupTimeDto> times = this.groupTimeMapper.listByAttendanceGroupId(attendanceGroupId, orgId);
        List<SyncAttendanceGroupTimeInfo> timeInfos = new ArrayList<>();
        if (MyListUtils.listIsEmpty(times)) {
            for (AttendanceGroupTimeDto time : times) {
                for (AttendanceGroupDevice device : groupDevice) {
                    SyncAttendanceGroupTimeInfo timeInfo = new SyncAttendanceGroupTimeInfo();
                    BeanUtils.copyProperties(time, timeInfo);
                    timeInfo.setTimeType(device.getDeviceType());
                    if (AttendanceGroupTypeEnum.FREE_TYPE.getValue() == groupType.intValue()) {
                        if (groupDevice.size() <= 1 || timeInfo.getTimeType().intValue() != 1) {
                            timeInfo.setAttendanceBeginTime(MyDateUtils.formatDate(MyDateUtils.addMinutes(MyDateUtils.parseDate(time.getAttendanceBeginTime(), "HH:mm"), -time.getAttendanceBeginBefore().intValue()), "HH:mm"));
                            timeInfo.setAttendanceEndTime(MyDateUtils.formatDate(MyDateUtils.addMinutes(MyDateUtils.parseDate(time.getAttendanceEndTime(), "HH:mm"), time.getAttendanceEndAfter().intValue()), "HH:mm"));
                        }
                    } else if (device.getDeviceType().intValue() == AttendanceGroupDeviceTypeEnum.START_WORK_DEVICE.getValue()) {
                        timeInfo.setAttendanceBeginTime(MyDateUtils.formatDate(MyDateUtils.addMinutes(MyDateUtils.parseDate(time.getAttendanceBeginTime(), "HH:mm"), -time.getAttendanceBeginBefore().intValue()), "HH:mm"));
                        timeInfo.setAttendanceEndTime(MyDateUtils.formatDate(MyDateUtils.addMinutes(MyDateUtils.parseDate(time.getAttendanceBeginTime(), "HH:mm"), time.getAttendanceBeginAfter().intValue()), "HH:mm"));
                    } else {
                        timeInfo.setAttendanceBeginTime(MyDateUtils.formatDate(MyDateUtils.addMinutes(MyDateUtils.parseDate(time.getAttendanceEndTime(), "HH:mm"), -time.getAttendanceEndBefore().intValue()), "HH:mm"));
                        timeInfo.setAttendanceEndTime(MyDateUtils.formatDate(MyDateUtils.addMinutes(MyDateUtils.parseDate(time.getAttendanceEndTime(), "HH:mm"), time.getAttendanceEndAfter().intValue()), "HH:mm"));
                    }
                    timeInfos.add(timeInfo);
                }
            }
        }
        return timeInfos;
    }

    private int getDeviceStatus(List<AttendanceGroupDevice> groupDevice, Long lastSyncTime) {
        int status = -1;
        if (MyListUtils.listIsEmpty(groupDevice)) {
            if (groupDevice.size() == 2) {
                if (groupDevice.get(0).getDeleteOrNot().intValue() == 1 && groupDevice.get(1).getDeleteOrNot().intValue() == 1) {
                    status = 1;
                } else if (groupDevice.get(0).getGmtCreate().getTime() > lastSyncTime.longValue() && groupDevice.get(1).getGmtCreate().getTime() > lastSyncTime.longValue()) {
                    status = 0;
                }
            } else if (groupDevice.size() != 1 || groupDevice.get(0).getDeleteOrNot().intValue() == 1) {
                status = 1;
            } else if (groupDevice.get(0).getGmtCreate().getTime() > lastSyncTime.longValue()) {
                status = 0;
            }
        } else {
            status = 1;
        }
        return status;
    }

    private void doCheckModifyAttendanceMemberAndDept(CheckMemberHasAttendanceGroupRequest request, List<CheckMemberHasAttendanceGroupResponse> responses) throws BeansException {
        if (MyListUtils.listIsEmpty(request.getMemberIds())) {
            doCheckAttendanceMember(request.getMemberIds(), responses, request.getOrgId(), request.getAttendanceGroupId());
        }
        if (MyListUtils.listIsEmpty(request.getDeptIds())) {
            doCheckAttendanceDept(request.getDeptIds(), responses, request.getOrgId(), request.getAttendanceGroupId());
        }
    }

    private void doCheckInsertAttendanceMemberAndDept(CheckMemberHasAttendanceGroupRequest request, List<CheckMemberHasAttendanceGroupResponse> responses) throws BeansException {
        if (MyListUtils.listIsEmpty(request.getMemberIds())) {
            doCheckAttendanceMember(request.getMemberIds(), responses, request.getOrgId(), request.getAttendanceGroupId());
        }
        if (MyListUtils.listIsEmpty(request.getDeptIds())) {
            doCheckAttendanceDept(request.getDeptIds(), responses, request.getOrgId(), request.getAttendanceGroupId());
        }
    }

    private void doCheckAttendanceMember(List<Long> memberIds, List<CheckMemberHasAttendanceGroupResponse> responses, Long orgId, Long attendanceGroupId) throws BeansException {
        for (Long memberId : memberIds) {
            CheckMemberHasAttendanceGroupResponse response = new CheckMemberHasAttendanceGroupResponse();
            AttendanceGroupMemberInfo groupMember = this.groupMemberMapper.getOneByMemberId(memberId, orgId);
            if (attendanceGroupId == null || attendanceGroupId.longValue() == 0) {
                if (groupMember != null) {
                    BeanUtils.copyProperties(groupMember, response);
                    response.setType(Integer.valueOf(MemberTypeEnum.MEMBER.getValue()));
                    responses.add(response);
                }
            } else if (groupMember != null && groupMember.getAttendanceGroupId().longValue() != attendanceGroupId.longValue()) {
                BeanUtils.copyProperties(groupMember, response);
                response.setType(Integer.valueOf(MemberTypeEnum.MEMBER.getValue()));
                responses.add(response);
            }
        }
    }

    private void doCheckAttendanceDept(List<Long> deptIds, List<CheckMemberHasAttendanceGroupResponse> responses, Long orgId, Long attendanceGroupId) throws BeansException {
        for (Long deptId : deptIds) {
            CheckMemberHasAttendanceGroupResponse response = new CheckMemberHasAttendanceGroupResponse();
            AttendanceGroupMemberInfo groupDept = this.groupMemberMapper.getOneByDeptId(deptId, orgId);
            if (attendanceGroupId == null || attendanceGroupId.longValue() == 0) {
                if (groupDept != null) {
                    BeanUtils.copyProperties(groupDept, response);
                    responses.add(response);
                } else {
                    doCheckAttendanceMember(this.memberMapper.listMemberIdByDeptId(deptId, orgId), responses, orgId, attendanceGroupId);
                }
            } else if (groupDept != null && groupDept.getAttendanceGroupId().longValue() != attendanceGroupId.longValue()) {
                BeanUtils.copyProperties(groupDept, response);
                responses.add(response);
            } else {
                doCheckAttendanceMember(this.memberMapper.listMemberIdByDeptId(deptId, orgId), responses, orgId, attendanceGroupId);
            }
            doCheckAttendanceSubDept(deptId, responses, orgId, attendanceGroupId);
        }
    }

    private void doCheckAttendanceSubDept(Long deptId, List<CheckMemberHasAttendanceGroupResponse> responses, Long orgId, Long attendanceGroupId) throws BeansException {
        List<ListDeptResponse> childDepts = this.deptMapper.listChildDept(deptId, orgId);
        if (childDepts != null && childDepts.size() > 0) {
            for (ListDeptResponse deptResponse : childDepts) {
                CheckMemberHasAttendanceGroupResponse response = new CheckMemberHasAttendanceGroupResponse();
                AttendanceGroupMemberInfo groupDept = this.groupMemberMapper.getOneByDeptId(deptResponse.getDeptId(), orgId);
                if (attendanceGroupId == null || attendanceGroupId.longValue() == 0) {
                    if (groupDept != null) {
                        BeanUtils.copyProperties(groupDept, response);
                        responses.add(response);
                    } else {
                        doCheckAttendanceMember(this.memberMapper.listMemberIdByDeptId(deptResponse.getDeptId(), orgId), responses, orgId, attendanceGroupId);
                    }
                } else if (groupDept != null && groupDept.getAttendanceGroupId().longValue() != attendanceGroupId.longValue()) {
                    BeanUtils.copyProperties(groupDept, response);
                    responses.add(response);
                } else {
                    doCheckAttendanceMember(this.memberMapper.listMemberIdByDeptId(deptResponse.getDeptId(), orgId), responses, orgId, attendanceGroupId);
                }
                doCheckAttendanceSubDept(deptResponse.getDeptId(), responses, orgId, attendanceGroupId);
            }
        }
    }

    private List<DeviceDto> listDeviceDtoByGroupId(Long attendanceGroupId, Long orgId, Integer type) {
        List<String> deviceSns = this.groupDeviceMapper.listDeviceSnByTypeAndGroupId(attendanceGroupId, orgId, type);
        List<DeviceDto> resp = new ArrayList<>();
        if (MyListUtils.listIsEmpty(deviceSns)) {
            resp = this.deviceMapper.listDeviceDtoByDeviceSn(deviceSns, orgId);
        }
        return resp;
    }

    private AttendanceGroupDetailResponse queryAttendanceGroupDetail(Long attendanceGroupId, Long orgId) throws BeansException {
        AssertUtil.checkOrgId(orgId);
        AssertUtil.checkId(attendanceGroupId, OnpremiseErrorEnum.ATTENDANCE_GROUP_ID_MUST_NOT_NULL);
        AttendanceGroup attendanceGroup = this.groupMapper.getOneById(attendanceGroupId, orgId);
        AssertUtil.isNullOrEmpty(attendanceGroup, OnpremiseErrorEnum.ATTENDANCE_GROUP_NOT_FIND);
        List<AttendanceGroupTimeDto> groupTimes = this.groupTimeMapper.listByAttendanceGroupId(attendanceGroupId, orgId);
        AssertUtil.isTrue(Boolean.valueOf(MyListUtils.listIsEmpty(groupTimes)), OnpremiseErrorEnum.ATTENDANCE_GROUP_TIME_NOT_FIND);
        AttendanceGroupDetailResponse response = new AttendanceGroupDetailResponse();
        BeanUtils.copyProperties(attendanceGroup, response);
        if (AttendanceGroupTypeEnum.MANUAL_TYPE.getValue() == attendanceGroup.getGroupType().intValue()) {
            QueryAttendanceGroupDateFrameResponse dateFrame = this.groupTimeMapper.getDateFrame(attendanceGroupId, orgId);
            response.setGroupStartDate(dateFrame.getStartDate());
            response.setGroupEndDate(dateFrame.getEndDate());
        } else {
            response.setGroupTimeInfo(groupTimes);
        }
        response.setMemberInfo(this.groupMemberMapper.listConfirmByAttendanceGroupIdAndType(null, orgId, attendanceGroupId));
        response.setAttendanceBeginDeviceInfo(listDeviceDtoByGroupId(attendanceGroupId, orgId, Integer.valueOf(AttendanceGroupDeviceTypeEnum.START_WORK_DEVICE.getValue())));
        response.setAttendanceEndDeviceInfo(listDeviceDtoByGroupId(attendanceGroupId, orgId, Integer.valueOf(AttendanceGroupDeviceTypeEnum.END_WORK_DEVICE.getValue())));
        return response;
    }
}
