package com.moredian.onpremise.group.service.impl;

import com.github.pagehelper.PageHelper;
import com.moredian.onpremise.api.group.GroupService;
import com.moredian.onpremise.api.member.DeptService;
import com.moredian.onpremise.api.member.MemberService;
import com.moredian.onpremise.core.adapter.CacheAdapter;
import com.moredian.onpremise.core.common.constants.Constants;
import com.moredian.onpremise.core.common.enums.AccountGradeEnum;
import com.moredian.onpremise.core.common.enums.AllDayFlagEnum;
import com.moredian.onpremise.core.common.enums.DeleteOrNotEnum;
import com.moredian.onpremise.core.common.enums.MemberTypeEnum;
import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.mapper.AccountGroupMapper;
import com.moredian.onpremise.core.mapper.AccountMapper;
import com.moredian.onpremise.core.mapper.DeptMapper;
import com.moredian.onpremise.core.mapper.DeviceMapper;
import com.moredian.onpremise.core.mapper.GroupAuthInfoMapper;
import com.moredian.onpremise.core.mapper.GroupDeviceMapper;
import com.moredian.onpremise.core.mapper.GroupMapper;
import com.moredian.onpremise.core.mapper.GroupMemberMapper;
import com.moredian.onpremise.core.mapper.MemberAuthInfoMapper;
import com.moredian.onpremise.core.mapper.MemberMapper;
import com.moredian.onpremise.core.model.domain.Account;
import com.moredian.onpremise.core.model.domain.AccountGroup;
import com.moredian.onpremise.core.model.domain.Dept;
import com.moredian.onpremise.core.model.domain.Device;
import com.moredian.onpremise.core.model.domain.Group;
import com.moredian.onpremise.core.model.domain.GroupAuthInfo;
import com.moredian.onpremise.core.model.domain.GroupDevice;
import com.moredian.onpremise.core.model.domain.GroupMember;
import com.moredian.onpremise.core.model.domain.Member;
import com.moredian.onpremise.core.model.dto.DeviceDto;
import com.moredian.onpremise.core.model.dto.GroupAuthDto;
import com.moredian.onpremise.core.model.dto.GroupMemberDto;
import com.moredian.onpremise.core.model.dto.PassTimeDto;
import com.moredian.onpremise.core.model.info.DeviceLastModifyTimeInfo;
import com.moredian.onpremise.core.model.info.UpdateGroupDeviceInfo;
import com.moredian.onpremise.core.model.request.DeleteGroupAuthRequest;
import com.moredian.onpremise.core.model.request.GetGroupAuthOneRequest;
import com.moredian.onpremise.core.model.request.GroupAuthListRequest;
import com.moredian.onpremise.core.model.request.MemberListRequest;
import com.moredian.onpremise.core.model.request.SaveGroupAuthRequest;
import com.moredian.onpremise.core.model.request.TerminalCheckPrivilegeRequest;
import com.moredian.onpremise.core.model.request.TerminalSyncRequest;
import com.moredian.onpremise.core.model.response.DeptMemberListResponse;
import com.moredian.onpremise.core.model.response.GroupAuthListResponse;
import com.moredian.onpremise.core.model.response.GroupAuthResponse;
import com.moredian.onpremise.core.model.response.GroupListResponse;
import com.moredian.onpremise.core.model.response.ListDeptResponse;
import com.moredian.onpremise.core.model.response.MemberListResponse;
import com.moredian.onpremise.core.model.response.SaveGroupAuthResponse;
import com.moredian.onpremise.core.model.response.TerminalCheckPrivilegeResponse;
import com.moredian.onpremise.core.model.response.TerminalGroupAuthSyncResponse;
import com.moredian.onpremise.core.model.response.TerminalGroupMemberSyncResponse;
import com.moredian.onpremise.core.model.response.TerminalPassTimeSyncResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncGroupResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncResponse;
import com.moredian.onpremise.core.utils.AssertUtil;
import com.moredian.onpremise.core.utils.CacheKeyGenerateUtils;
import com.moredian.onpremise.core.utils.JsonUtils;
import com.moredian.onpremise.core.utils.MyDateUtils;
import com.moredian.onpremise.core.utils.MyListUtils;
import com.moredian.onpremise.core.utils.PageList;
import com.moredian.onpremise.core.utils.Paginator;
import com.moredian.onpremise.iot.handle.NettyMessageApi;
import com.moredian.onpremise.model.SyncGroupRequest;
import com.moredian.onpremise.model.SyncMemberRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
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

@Service
/* loaded from: onpremise-group-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/group/service/impl/GroupServiceImpl.class */
public class GroupServiceImpl implements GroupService {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) GroupServiceImpl.class);

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private GroupMemberMapper groupMemberMapper;

    @Autowired
    private GroupAuthInfoMapper groupAuthInfoMapper;

    @Autowired
    private GroupDeviceMapper groupDeviceMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private NettyMessageApi nettyMessageApi;

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private DeptService deptService;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private MemberAuthInfoMapper memberAuthInfoMapper;

    @Autowired
    private MemberService memberService;

    @Autowired
    private AccountGroupMapper accountGroupMapper;

    @Override // com.moredian.onpremise.api.group.GroupService
    @Transactional(rollbackFor = {RuntimeException.class})
    public SaveGroupAuthResponse updateGroup(SaveGroupAuthRequest request) throws BeansException {
        checkSaveGroupParams(request);
        AssertUtil.isNullOrEmpty(this.groupMapper.getOneById(request.getGroupId(), request.getOrgId()), OnpremiseErrorEnum.GROUP_NOT_FIND);
        Group group = requestTransToGroup(request);
        unbindDefaultGroup(request);
        AssertUtil.isTrue(Boolean.valueOf(this.groupMapper.updateGroup(group) > 0), OnpremiseErrorEnum.UPDATE_GROUP_FAIL);
        UpdateGroupDeviceInfo updateGroupDevice = updateGroupDevice(request);
        boolean syncMemberFlag = updateGroupMember(request, updateGroupDevice);
        updateGroupAuth(request);
        List<String> deviceSns = new ArrayList<>();
        for (DeviceDto deviceDto : updateGroupDevice.getNewDevices()) {
            if (!deviceSns.contains(deviceDto.getDeviceSn())) {
                deviceSns.add(deviceDto.getDeviceSn());
            }
        }
        for (DeviceDto deviceDto2 : updateGroupDevice.getOldDevices()) {
            if (!deviceSns.contains(deviceDto2.getDeviceSn())) {
                deviceSns.add(deviceDto2.getDeviceSn());
            }
        }
        if (syncMemberFlag) {
            doSendMemberNetty(request.getOrgId());
        }
        doSendNettyMessage(request.getOrgId(), deviceSns);
        return new SaveGroupAuthResponse(group.getGroupId());
    }

    @Override // com.moredian.onpremise.api.group.GroupService
    @Transactional(rollbackFor = {RuntimeException.class})
    public SaveGroupAuthResponse insertGroup(SaveGroupAuthRequest request) throws BeansException {
        checkSaveGroupParams(request);
        Group group = requestTransToGroup(request);
        unbindDefaultGroup(request);
        AssertUtil.isTrue(Boolean.valueOf(this.groupMapper.insertGroup(group) > 0), OnpremiseErrorEnum.INSERT_GROUP_FAIL);
        AccountGroup accountGroup = new AccountGroup();
        accountGroup.setOrgId(request.getOrgId());
        accountGroup.setAccountId(request.getLoginAccountId());
        accountGroup.setGroupId(group.getGroupId());
        this.accountGroupMapper.insertAccountGroup(accountGroup);
        List<GroupMemberDto> members = request.getGroupMembers();
        ArrayList arrayList = new ArrayList();
        for (GroupMemberDto item : members) {
            item.setGroupId(group.getGroupId());
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
            GroupMemberDto groupMemberDto = new GroupMemberDto();
            groupMemberDto.setGroupId(group.getGroupId());
            groupMemberDto.setType(Integer.valueOf(MemberTypeEnum.DEPT.getValue()));
            groupMemberDto.setDeptId(deptId);
            groupMemberDto.setMemberId(0L);
            groupMemberDto.setConfirmFlag(0);
            if (!members.contains(groupMemberDto)) {
                members.add(groupMemberDto);
            }
        }
        doInsertGroupMemberByDto(members, request.getOrgId());
        Set<Long> memberIdSet = new HashSet<>();
        Set<Long> deptIdSet = new HashSet<>();
        for (GroupMemberDto groupMemberDto2 : members) {
            if (groupMemberDto2.getType().equals(Integer.valueOf(MemberTypeEnum.DEPT.getValue()))) {
                deptIdSet.add(groupMemberDto2.getDeptId());
            } else {
                memberIdSet.add(groupMemberDto2.getMemberId());
            }
        }
        if (!CollectionUtils.isEmpty(memberIdSet) || !CollectionUtils.isEmpty(deptIdSet)) {
            this.memberMapper.updateModifyTime(request.getOrgId(), new ArrayList(memberIdSet), new ArrayList(deptIdSet));
        }
        List<GroupAuthDto> authDtos = request.getGroupAuths();
        if (!CollectionUtils.isEmpty(authDtos)) {
            GroupAuthDto authDto = authDtos.get(0);
            doInsertGroupAuthByDto(authDto, request.getOrgId(), group.getGroupId(), request.getCycleFlag(), request.getAllDayFlag());
        }
        List<DeviceDto> devices = request.getGroupDevices();
        List<String> deviceSns = new ArrayList<>();
        doInsertGroupDeviceByDto(devices, request.getOrgId(), group.getGroupId(), deviceSns);
        doSendNettyMessage(request.getOrgId(), deviceSns);
        if (!CollectionUtils.isEmpty(members)) {
            doSendMemberNetty(request.getOrgId());
        }
        return new SaveGroupAuthResponse(group.getGroupId());
    }

    @Override // com.moredian.onpremise.api.group.GroupService
    @Transactional(rollbackFor = {RuntimeException.class})
    public boolean deleteGroup(DeleteGroupAuthRequest request) {
        AssertUtil.isTrue(Boolean.valueOf(request.getGroupId() != null || request.getGroupId().longValue() > 0), OnpremiseErrorEnum.GROUP_ID_MUST_NOT_NULL);
        Group group = this.groupMapper.getOneById(request.getGroupId(), request.getOrgId());
        AssertUtil.isNullOrEmpty(group, OnpremiseErrorEnum.GROUP_NOT_FIND);
        Account account = this.accountMapper.getAccountInfo(request.getLoginAccountId(), request.getOrgId());
        AssertUtil.isNullOrEmpty(account, OnpremiseErrorEnum.ACCOUNT_NOT_FIND);
        AccountGroup accountGroup = this.accountGroupMapper.findByAccountIdAndGroupId(request.getOrgId(), request.getLoginAccountId(), request.getGroupId());
        AssertUtil.isTrue(Boolean.valueOf(account.getAccountGrade().intValue() == AccountGradeEnum.SUPER_ACCOUNT.getValue() || account.getAccountGrade().intValue() == AccountGradeEnum.MAIN_ACCOUNT.getValue() || accountGroup != null), OnpremiseErrorEnum.NO_PRIVILEGE_OPERATE_GROUP);
        List<String> deviceSns = this.groupDeviceMapper.listDeviceSnByGroupId(request.getGroupId(), request.getOrgId());
        if (MyListUtils.listIsEmpty(deviceSns)) {
            AssertUtil.isTrue(Boolean.valueOf(this.groupDeviceMapper.deleteByGroup(request.getGroupId(), request.getOrgId()) > 0), OnpremiseErrorEnum.DELETE_DEVICE_FAIL);
        }
        AssertUtil.isTrue(Boolean.valueOf(this.groupAuthInfoMapper.deleteGroupAuthInfo(request.getGroupId(), request.getOrgId()) > 0), OnpremiseErrorEnum.DELETE_GROUP_AUTH_FAIL);
        AssertUtil.isTrue(Boolean.valueOf(this.groupMapper.deleteGroup(request.getGroupId(), request.getOrgId()) > 0), OnpremiseErrorEnum.DELETE_GROUP_FAIL);
        this.accountGroupMapper.deleteByGroupId(request.getOrgId(), request.getGroupId());
        List<GroupMemberDto> groupMemberList = this.groupMemberMapper.listMemberByGroupId(request.getGroupId(), request.getOrgId());
        List<Long> memberIdList = new ArrayList<>();
        List<Long> deptIdList = new ArrayList<>();
        for (GroupMemberDto groupMemberDto : groupMemberList) {
            if (groupMemberDto.getType().equals(Integer.valueOf(MemberTypeEnum.MEMBER.getValue()))) {
                memberIdList.add(groupMemberDto.getMemberId());
            } else {
                deptIdList.add(groupMemberDto.getDeptId());
            }
        }
        this.groupMemberMapper.deleteByGroup(request.getGroupId(), request.getOrgId());
        if (!CollectionUtils.isEmpty(memberIdList) || !CollectionUtils.isEmpty(deptIdList)) {
            this.memberMapper.updateModifyTime(request.getOrgId(), memberIdList, deptIdList);
            doSendMemberNetty(request.getOrgId());
        }
        doSendNettyMessage(request.getOrgId(), deviceSns);
        return true;
    }

    @Override // com.moredian.onpremise.api.group.GroupService
    public PageList<GroupAuthListResponse> groupAuthList(GroupAuthListRequest request) throws BeansException {
        Paginator paginator = request.getPaginator();
        Account account = this.accountMapper.getAccountInfo(request.getLoginAccountId(), request.getOrgId());
        AssertUtil.isNullOrEmpty(account, OnpremiseErrorEnum.ACCOUNT_NOT_FIND);
        if (account.getAccountGrade().intValue() == AccountGradeEnum.SUPER_ACCOUNT.getValue() || account.getAccountGrade().intValue() == AccountGradeEnum.MAIN_ACCOUNT.getValue()) {
            request.setLoginAccountId(null);
        }
        if (Paginator.checkPaginator(paginator)) {
            PageHelper.startPage(paginator.getPageNum(), paginator.getPageSize());
            List<GroupAuthListResponse> responses = this.groupMapper.listGroupAuth(request.getGroupName(), request.getLoginAccountId(), request.getOrgId());
            packagingGroupList(responses, request.getOrgId());
            return new PageList<>(responses);
        }
        List<GroupAuthListResponse> responses2 = this.groupMapper.listGroupAuth(request.getGroupName(), request.getLoginAccountId(), request.getOrgId());
        packagingGroupList(responses2, request.getOrgId());
        return new PageList<>(Paginator.initPaginator(responses2), responses2);
    }

    @Override // com.moredian.onpremise.api.group.GroupService
    public PageList<GroupListResponse> groupList(GroupAuthListRequest request) {
        List<Long> groupIds = new ArrayList<>();
        if (!StringUtils.isEmpty(request.getDeviceSn())) {
            groupIds = this.groupDeviceMapper.listGroupIdByDeviceIdNotDelete(request.getDeviceSn(), request.getOrgId());
        }
        Paginator paginator = request.getPaginator();
        if (Paginator.checkPaginator(paginator)) {
            PageHelper.startPage(paginator.getPageNum(), paginator.getPageSize());
            return new PageList<>(this.groupMapper.listGroup(request.getGroupName(), groupIds, request.getOrgId()));
        }
        List<GroupListResponse> responses = this.groupMapper.listGroup(request.getGroupName(), groupIds, request.getOrgId());
        return new PageList<>(Paginator.initPaginator(responses), responses);
    }

    @Override // com.moredian.onpremise.api.group.GroupService
    public GroupAuthResponse getGroupAuthById(GetGroupAuthOneRequest request) throws BeansException {
        AssertUtil.isNullOrEmpty(request.getGroupId(), OnpremiseErrorEnum.GROUP_NAME_MUST_NOT_NULL);
        GroupAuthResponse response = new GroupAuthResponse();
        Group group = this.groupMapper.getOneById(request.getGroupId(), request.getOrgId());
        AssertUtil.isNullOrEmpty(group, OnpremiseErrorEnum.GROUP_NOT_FIND);
        BeanUtils.copyProperties(group, response);
        packagingGroupAuthResponse(response, request.getGroupId(), request.getOrgId());
        return response;
    }

    @Override // com.moredian.onpremise.api.group.GroupService
    public TerminalSyncResponse<TerminalSyncGroupResponse> syncGroup(TerminalSyncRequest request) {
        AssertUtil.isNullOrEmpty(request.getDeviceSn(), OnpremiseErrorEnum.DEVICE_SN_MUST_NOT_NULL);
        String lastSyncTime = MyDateUtils.getDateString(request.getLastSyncTime().longValue(), "yyyy-MM-dd HH:mm:ss.SSS");
        Device device = this.deviceMapper.getDeviceInfoByDeviceSn(request.getDeviceSn());
        if (device == null) {
            logger.info("==========================device not find ");
            TerminalSyncResponse terminalSyncResponse = new TerminalSyncResponse();
            terminalSyncResponse.setSyncTime(CacheAdapter.getLastModifyTime(CacheKeyGenerateUtils.getGroupLastModifyTimeKey(request.getDeviceSn())).getLastModifyTime());
            return terminalSyncResponse;
        }
        return packagingSyncGroupResponse(request, lastSyncTime, device);
    }

    private GroupAuthListResponse packagingGroupAuthListResponse(GroupAuthListResponse response, Long orgId) throws BeansException {
        if (response.getAllMemberFlag().intValue() == 1) {
            response.setMemberNum(this.memberMapper.countAllMembers(orgId));
        } else {
            Integer memberNum = 0;
            List<Long> deptIds = new ArrayList<>();
            List<GroupMember> groupDepts = this.groupMemberMapper.listDeptByGroupId(response.getGroupId(), orgId, Integer.valueOf(MemberTypeEnum.DEPT.getValue()));
            for (GroupMember dept : groupDepts) {
                deptIds.add(dept.getDeptId());
            }
            List<Long> memberIds = new ArrayList<>();
            List<GroupMember> groupMembers = this.groupMemberMapper.listDeptByGroupId(response.getGroupId(), orgId, Integer.valueOf(MemberTypeEnum.MEMBER.getValue()));
            for (GroupMember member : groupMembers) {
                memberIds.add(member.getMemberId());
            }
            if (deptIds.size() > 0) {
                memberNum = Integer.valueOf(memberNum.intValue() + this.memberMapper.countMemberByDepts(deptIds, memberIds, orgId).intValue());
            }
            response.setMemberNum(Integer.valueOf(memberNum.intValue() + memberIds.size()));
            String deptName = JsonUtils.toJson(deptIds);
            response.setDeptName(deptName.substring(1, deptName.length() - 1));
        }
        GroupAuthInfo authInfo = this.groupAuthInfoMapper.getAuthByGroupId(response.getGroupId(), orgId);
        GroupAuthDto authDto = new GroupAuthDto();
        BeanUtils.copyProperties(authInfo, authDto);
        authDto.setScope(authInfo.getAuthScope());
        authDto.setPassTimeList(JsonUtils.jsoncastListType(PassTimeDto.class, authInfo.getPassTime()));
        response.setAllDayFlag(Integer.valueOf(exchangeAllDayFlag(authDto)));
        List<GroupAuthDto> authDtoList = new ArrayList<>();
        authDtoList.add(authDto);
        response.setGroupAuths(authDtoList);
        response.setDeviceNum(Integer.valueOf(this.groupDeviceMapper.countDeviceByGroupId(response.getGroupId(), orgId)));
        return response;
    }

    private GroupAuthResponse packagingGroupAuthResponse(GroupAuthResponse response, Long groupId, Long orgId) throws BeansException {
        List<GroupMemberDto> members = this.groupMemberMapper.listUserAddMemberByGroupIdExcludeAll(groupId, orgId);
        response.setGroupMembers(members);
        GroupAuthInfo authInfo = this.groupAuthInfoMapper.getAuthByGroupId(response.getGroupId(), orgId);
        GroupAuthDto authDto = new GroupAuthDto();
        BeanUtils.copyProperties(authInfo, authDto);
        authDto.setScope(authInfo.getAuthScope());
        authDto.setPassTimeList(JsonUtils.jsoncastListType(PassTimeDto.class, authInfo.getPassTime()));
        List<GroupAuthDto> authDtoList = new ArrayList<>();
        authDtoList.add(authDto);
        response.setGroupAuths(authDtoList);
        response.setAllDayFlag(Integer.valueOf(exchangeAllDayFlag(authDto)));
        List<DeviceDto> devices = this.groupDeviceMapper.listDeviceByGroupIdNotDelete(groupId, orgId);
        if (!CollectionUtils.isEmpty(devices)) {
            List<String> deviceSns = new ArrayList<>();
            for (DeviceDto deviceDto : devices) {
                deviceSns.add(deviceDto.getDeviceSn());
            }
            List<DeviceDto> deviceDtoList = this.deviceMapper.listDeviceDtoByDeviceSn(deviceSns, orgId);
            response.setGroupDevices(deviceDtoList);
        }
        return response;
    }

    private boolean updateGroupAuth(SaveGroupAuthRequest request) throws BeansException {
        List<GroupAuthDto> newAuths = request.getGroupAuths();
        if (!CollectionUtils.isEmpty(newAuths)) {
            GroupAuthDto authDto = newAuths.get(0);
            GroupAuthInfo authInfo = new GroupAuthInfo();
            checkAuthDtoTime(authDto, request.getAllDayFlag());
            BeanUtils.copyProperties(authDto, authInfo);
            authInfo.setAuthScope(request.getCycleFlag().intValue() == 1 ? Constants.DEFAULT_SCOPE : authDto.getScope());
            authInfo.setOrgId(request.getOrgId());
            authInfo.setGroupId(request.getGroupId());
            authInfo.setPassTime(JsonUtils.toJson(authDto.getPassTimeList()));
            AssertUtil.isTrue(Boolean.valueOf(this.groupAuthInfoMapper.updateGroupAuthInfoByGroupId(authInfo) > 0), OnpremiseErrorEnum.UPDATE_GROUP_AUTH_FAIL);
            return true;
        }
        return true;
    }

    private UpdateGroupDeviceInfo updateGroupDevice(SaveGroupAuthRequest request) throws BeansException {
        List<DeviceDto> newDevices = request.getGroupDevices() != null ? request.getGroupDevices() : Collections.EMPTY_LIST;
        List<DeviceDto> oldDevices = this.groupDeviceMapper.listDeviceByGroupIdNotDelete(request.getGroupId(), request.getOrgId());
        MyListUtils<DeviceDto> memberUtils = new MyListUtils<>();
        List<DeviceDto> insertDevices = memberUtils.difference(newDevices, oldDevices);
        List<DeviceDto> deleteDevices = memberUtils.difference(oldDevices, newDevices);
        List<String> deviceSns = new ArrayList<>();
        if (MyListUtils.listIsEmpty(deleteDevices)) {
            for (DeviceDto dto : deleteDevices) {
                deviceSns.add(dto.getDeviceSn());
                AssertUtil.isTrue(Boolean.valueOf(this.groupDeviceMapper.deleteGroupDevice(request.getGroupId(), dto.getDeviceSn(), request.getOrgId()) > 0), OnpremiseErrorEnum.DELETE_GROUP_DEVICE_FAIL);
            }
        }
        if (MyListUtils.listIsEmpty(newDevices)) {
            Iterator<DeviceDto> it = newDevices.iterator();
            while (it.hasNext()) {
                deviceSns.add(it.next().getDeviceSn());
            }
        }
        doInsertGroupDeviceByDto(insertDevices, request.getOrgId(), request.getGroupId(), deviceSns);
        return new UpdateGroupDeviceInfo(newDevices, oldDevices);
    }

    private boolean updateGroupMember(SaveGroupAuthRequest request, UpdateGroupDeviceInfo updateGroupDevice) throws BeansException {
        long startTime = System.currentTimeMillis();
        Set<Long> memberIdSet = new HashSet<>();
        Set<Long> deptIdSet = new HashSet<>();
        List<GroupMemberDto> newMembers = request.getGroupMembers();
        ArrayList arrayList = new ArrayList();
        for (GroupMemberDto item : newMembers) {
            item.setGroupId(request.getGroupId());
            item.setConfirmFlag(1);
            if (item.getType().equals(Integer.valueOf(MemberTypeEnum.DEPT.getValue()))) {
                this.deptService.packagingChildDept(item.getDeptId(), request.getOrgId(), arrayList);
            }
        }
        HashSet hashSet = new HashSet();
        List<Long> noRepeatChildDeptIds = new ArrayList<>();
        hashSet.addAll(arrayList);
        noRepeatChildDeptIds.addAll(hashSet);
        new ArrayList();
        for (Long deptId : noRepeatChildDeptIds) {
            GroupMemberDto groupMemberDto = new GroupMemberDto();
            groupMemberDto.setGroupId(request.getGroupId());
            groupMemberDto.setType(Integer.valueOf(MemberTypeEnum.DEPT.getValue()));
            groupMemberDto.setDeptId(deptId);
            groupMemberDto.setMemberId(0L);
            groupMemberDto.setConfirmFlag(0);
            if (!newMembers.contains(groupMemberDto)) {
                newMembers.add(groupMemberDto);
            }
        }
        List<GroupMemberDto> oldMembers = this.groupMemberMapper.listMemberByGroupId(request.getGroupId(), request.getOrgId());
        logger.info("===========newMembers :{}", Integer.valueOf(newMembers.size()));
        logger.info("============oldMembers :{}", Integer.valueOf(oldMembers.size()));
        MyListUtils<GroupMemberDto> memberUtils = new MyListUtils<>();
        List<GroupMemberDto> insertMembers = memberUtils.difference(newMembers, oldMembers);
        List<GroupMemberDto> deleteMembers = memberUtils.difference(oldMembers, newMembers);
        List<GroupMemberDto> repeatMembers = memberUtils.union(oldMembers, newMembers);
        logger.info("===========insertMembers size :{} , deleteMembers size :{}, repeatMembers size :{}", Integer.valueOf(insertMembers.size()), Integer.valueOf(deleteMembers.size()), Integer.valueOf(repeatMembers.size()));
        if (MyListUtils.listIsEmpty(deleteMembers)) {
            logger.info("================update delete member auth info");
            for (GroupMemberDto dto : deleteMembers) {
                if (MemberTypeEnum.MEMBER.getValue() == dto.getType().intValue()) {
                    this.groupMemberMapper.deleteGroupMember(request.getGroupId(), dto.getMemberId(), request.getOrgId());
                    memberIdSet.add(dto.getMemberId());
                } else {
                    this.groupMemberMapper.deleteGroupDept(dto.getDeptId(), request.getOrgId(), request.getGroupId());
                    deptIdSet.add(dto.getDeptId());
                }
            }
        }
        doInsertGroupMemberByDto(insertMembers, request.getOrgId());
        for (GroupMemberDto groupMemberDto2 : insertMembers) {
            if (groupMemberDto2.getType().equals(Integer.valueOf(MemberTypeEnum.DEPT.getValue()))) {
                deptIdSet.add(groupMemberDto2.getDeptId());
            } else {
                memberIdSet.add(groupMemberDto2.getMemberId());
            }
        }
        if (!CollectionUtils.isEmpty(memberIdSet) || !CollectionUtils.isEmpty(deptIdSet)) {
            this.memberMapper.updateModifyTime(request.getOrgId(), new ArrayList(memberIdSet), new ArrayList(deptIdSet));
            return true;
        }
        logger.info("member update : {}", Long.valueOf(System.currentTimeMillis() - startTime));
        return false;
    }

    private boolean doInsertGroupMemberByDto(List<GroupMemberDto> members, Long orgId) throws BeansException {
        if (members != null && members.size() > 0) {
            List<GroupMember> groupMemberList = new ArrayList<>();
            for (GroupMemberDto memberDto : members) {
                GroupMember groupMember = new GroupMember();
                BeanUtils.copyProperties(memberDto, groupMember);
                groupMember.setOrgId(orgId);
                groupMember.setConfirmFlag(memberDto.getConfirmFlag());
                groupMember.setDeleteOrNot(0);
                groupMemberList.add(groupMember);
            }
            AssertUtil.isTrue(Boolean.valueOf(this.groupMemberMapper.batchInsert(groupMemberList) > 0), OnpremiseErrorEnum.INSERT_GROUP_MEMBER_FAIL);
            return true;
        }
        return true;
    }

    private void deleteChildDeptGroup(Long deptId, Long orgId, Long groupId) {
        List<ListDeptResponse> childDepts = this.deptMapper.listChildDept(deptId, orgId);
        if (MyListUtils.listIsEmpty(childDepts)) {
            for (ListDeptResponse response : childDepts) {
                this.groupMapper.updateModifyTime(groupId, orgId);
                this.groupMemberMapper.deleteGroupDept(response.getDeptId(), orgId, groupId);
                deleteChildDeptGroup(response.getDeptId(), orgId, groupId);
            }
        }
    }

    private void insertChildDeptGroup(Long orgId, GroupMember groupDept, List<DeviceDto> insertDevices, List<Long> deptIds) {
        List<Dept> childDepts = this.deptMapper.listChildDeptToReturnDept(groupDept.getDeptId(), orgId);
        logger.info("==============child dept is :{}", JsonUtils.toJson(childDepts));
        if (childDepts != null && childDepts.size() > 0) {
            for (Dept childDept : childDepts) {
                deptIds.add(childDept.getDeptId());
                groupDept.setDeptId(childDept.getDeptId());
                groupDept.setConfirmFlag(0);
                logger.info("===============insert child dept ");
                this.groupMemberMapper.insertGroupMember(groupDept);
                insertChildDeptGroup(orgId, groupDept, insertDevices, deptIds);
            }
        }
    }

    private void passDept(List<GroupMemberDto> memberDtos, Long orgId) {
        List<GroupMemberDto> temp = new ArrayList<>();
        temp.addAll(memberDtos);
        for (GroupMemberDto dto : temp) {
            doPassDept(memberDtos, orgId, dto);
        }
    }

    private void doPassDept(List<GroupMemberDto> memberDtos, Long orgId, GroupMemberDto dto) {
        List<Dept> childDepts = this.deptMapper.listChildDeptToReturnDept(dto.getDeptId(), orgId);
        if (childDepts != null && childDepts.size() > 0) {
            GroupMemberDto var = new GroupMemberDto();
            for (Dept childDept : childDepts) {
                var.setDeptId(childDept.getDeptId());
                var.setDeptName(childDept.getDeptName());
                var.setType(Integer.valueOf(MemberTypeEnum.DEPT.getValue()));
                if (memberDtos.contains(var)) {
                    memberDtos.remove(var);
                }
                doPassDept(memberDtos, orgId, var);
            }
        }
    }

    private void insertChildMemberGroup(Long orgId, GroupMember groupDept) throws BeansException {
        List<DeptMemberListResponse> members = this.memberMapper.listMemberByDeptId(groupDept.getDeptId(), orgId);
        for (DeptMemberListResponse member : members) {
            if (this.groupMemberMapper.getByDeptMemberAndGroupId(member.getMemberId(), groupDept.getDeptId(), groupDept.getGroupId(), orgId) == null) {
                GroupMember groupmember = new GroupMember();
                BeanUtils.copyProperties(groupDept, groupmember);
                groupmember.setType(Integer.valueOf(MemberTypeEnum.MEMBER.getValue()));
                groupmember.setMemberId(member.getMemberId());
                groupmember.setConfirmFlag(0);
                this.groupMemberMapper.insertGroupMember(groupmember);
            }
        }
    }

    private void deleteGroupDept(Long groupId, Long orgId) {
        List<GroupMember> groupDepts = this.groupMemberMapper.listDeptByGroupId(groupId, orgId, Integer.valueOf(MemberTypeEnum.DEPT.getValue()));
        if (MyListUtils.listIsEmpty(groupDepts)) {
            for (GroupMember groupMember : groupDepts) {
                List<DeptMemberListResponse> members = this.memberMapper.listMemberByDeptId(groupMember.getDeptId(), orgId);
                if (MyListUtils.listIsEmpty(members)) {
                    for (DeptMemberListResponse response : members) {
                        GroupMember member = this.groupMemberMapper.getByDeptMemberAndGroupId(response.getMemberId(), groupMember.getDeptId(), groupMember.getGroupId(), orgId);
                        if (member == null) {
                            groupMember.setDeptId(groupMember.getDeptId());
                            groupMember.setMemberId(response.getMemberId());
                            groupMember.setDeleteOrNot(Integer.valueOf(DeleteOrNotEnum.DELETE_YES.getValue()));
                            groupMember.setType(Integer.valueOf(MemberTypeEnum.MEMBER.getValue()));
                            this.groupMemberMapper.insertDeleteGroupMember(groupMember);
                        } else {
                            this.groupMemberMapper.deleteGroupDeptMember(groupMember.getGroupId(), groupMember.getDeptId(), response.getMemberId(), orgId);
                        }
                    }
                }
            }
        }
    }

    private void doInsertDeleteGroupMember(Long deptId, Long orgId, Long groupId) {
        List<GroupMember> groupMembers = this.groupMemberMapper.listByDeptId(deptId, orgId);
        if (MyListUtils.listIsEmpty(groupMembers)) {
            List<DeptMemberListResponse> listResponses = this.memberMapper.listMemberByDeptId(deptId, orgId);
            if (MyListUtils.listIsEmpty(listResponses)) {
                for (DeptMemberListResponse response : listResponses) {
                    for (GroupMember groupMember : groupMembers) {
                        GroupMember member = this.groupMemberMapper.getByDeptMemberAndGroupId(response.getMemberId(), deptId, groupId, orgId);
                        if (member == null) {
                            groupMember.setDeptId(deptId);
                            groupMember.setMemberId(response.getMemberId());
                            groupMember.setDeleteOrNot(Integer.valueOf(DeleteOrNotEnum.DELETE_YES.getValue()));
                            groupMember.setType(Integer.valueOf(MemberTypeEnum.MEMBER.getValue()));
                            this.groupMemberMapper.insertDeleteGroupMember(groupMember);
                        } else {
                            this.groupMemberMapper.deleteGroupDeptMember(groupId, deptId, response.getMemberId(), orgId);
                        }
                    }
                }
            }
        }
    }

    private boolean doInsertGroupAuthByDto(GroupAuthDto authDto, Long orgId, Long groupId, Integer cycleFlag, Integer allDayFlag) throws BeansException {
        GroupAuthInfo authInfo = new GroupAuthInfo();
        checkAuthDtoTime(authDto, allDayFlag);
        BeanUtils.copyProperties(authDto, authInfo);
        authInfo.setAuthScope(cycleFlag.intValue() == 1 ? Constants.DEFAULT_SCOPE : authDto.getScope());
        authInfo.setOrgId(orgId);
        authInfo.setGroupId(groupId);
        authInfo.setPassTime(JsonUtils.toJson(authDto.getPassTimeList()));
        AssertUtil.isTrue(Boolean.valueOf(this.groupAuthInfoMapper.insertGroupAuthInfo(authInfo) > 0), OnpremiseErrorEnum.INSERT_GROUP_AUTH_FAIL);
        return true;
    }

    private boolean doInsertGroupDeviceByDto(List<DeviceDto> devices, Long orgId, Long groupId, List<String> deviceSns) throws BeansException {
        if (devices != null && devices.size() > 0) {
            GroupDevice device = new GroupDevice();
            for (DeviceDto deviceDto : devices) {
                BeanUtils.copyProperties(deviceDto, device);
                device.setOrgId(orgId);
                device.setGroupId(groupId);
                AssertUtil.isTrue(Boolean.valueOf(this.groupDeviceMapper.insertGroupDevice(device) > 0), OnpremiseErrorEnum.INSERT_GROUP_DEVICE_FAIL);
                if (!deviceSns.contains(deviceDto.getDeviceSn())) {
                    deviceSns.add(deviceDto.getDeviceSn());
                }
            }
            return true;
        }
        return true;
    }

    private boolean checkSaveGroupParams(SaveGroupAuthRequest request) {
        AssertUtil.isNullOrEmpty(request.getGroupName(), OnpremiseErrorEnum.GROUP_NAME_MUST_NOT_NULL);
        Group group = this.groupMapper.getOneByGroupName(request.getGroupName(), request.getOrgId());
        if (request.getGroupId() == null || request.getGroupId().longValue() <= 0) {
            AssertUtil.isTrue(Boolean.valueOf(group == null), OnpremiseErrorEnum.GROUP_NAME_ALREADY_EXIST);
        } else {
            AssertUtil.isTrue(Boolean.valueOf(group == null || request.getGroupId().longValue() == group.getGroupId().longValue()), OnpremiseErrorEnum.GROUP_NAME_ALREADY_EXIST);
        }
        AssertUtil.isNullOrEmpty(request.getDefaultFlag(), OnpremiseErrorEnum.GROUP_DEFAULT_FLAG_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getAllMemberFlag(), OnpremiseErrorEnum.GROUP_ALL_MEMBER_FLAG_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getCycleFlag(), OnpremiseErrorEnum.GROUP_CYCLE_FLAG_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getPermanentFlag(), OnpremiseErrorEnum.GROUP_PERMANENT_FLAG_MUST_NOT_NULL);
        Account account = this.accountMapper.getAccountInfo(request.getLoginAccountId(), request.getOrgId());
        AssertUtil.isNullOrEmpty(account, OnpremiseErrorEnum.ACCOUNT_NOT_FIND);
        AssertUtil.isTrue(Boolean.valueOf(account.getAccountGrade().intValue() != AccountGradeEnum.CHILDREN_ACCOUNT.getValue() || request.getAllMemberFlag().intValue() == 0), OnpremiseErrorEnum.CHILD_ACCOUNT_NOT_ALLOW_ALL_MEMBER_GROUP);
        return true;
    }

    private Group requestTransToGroup(SaveGroupAuthRequest request) throws BeansException {
        Group group = new Group();
        BeanUtils.copyProperties(request, group);
        group.setAccountId(request.getLoginAccountId());
        group.setOrgId(request.getOrgId());
        return group;
    }

    private boolean checkAuthDtoTime(GroupAuthDto authDto, Integer allDayFlag) {
        if (allDayFlag.intValue() == AllDayFlagEnum.ALL_DAY.getValue()) {
            List<PassTimeDto> passTimeDtoList = new ArrayList<>();
            PassTimeDto passTimeDto = new PassTimeDto();
            passTimeDto.setStartTime(Constants.DEFAULT_START_TIME);
            passTimeDto.setEndTime(Constants.DEFAULT_END_TIME);
            passTimeDtoList.add(passTimeDto);
            authDto.setPassTimeList(passTimeDtoList);
            authDto.setStartTime(Constants.DEFAULT_START_TIME);
            authDto.setEndTime(Constants.DEFAULT_END_TIME);
        } else if (CollectionUtils.isEmpty(authDto.getPassTimeList()) && !StringUtils.isEmpty(authDto.getStartTime()) && !StringUtils.isEmpty(authDto.getEndTime())) {
            List<PassTimeDto> passTimeDtoList2 = new ArrayList<>();
            PassTimeDto passTimeDto2 = new PassTimeDto();
            passTimeDto2.setStartTime(authDto.getStartTime());
            passTimeDto2.setEndTime(authDto.getEndTime());
            passTimeDtoList2.add(passTimeDto2);
            authDto.setPassTimeList(passTimeDtoList2);
        }
        if (authDto.getStartDate() == null || authDto.getStartDate().trim().length() == 0) {
            authDto.setStartDate(null);
        } else if (authDto.getStartDate().trim().length() == 10) {
            authDto.setStartDate(authDto.getStartDate() + MyDateUtils.TIME_OF_DAY_BEGIN);
        }
        if (authDto.getEndDate() == null || authDto.getEndDate().trim().length() == 0) {
            authDto.setEndDate(null);
        } else if (authDto.getEndDate().trim().length() == 10) {
            authDto.setEndDate(authDto.getEndDate() + MyDateUtils.TIME_OF_DAY_BEGIN);
        }
        if (authDto.getScope() == null || authDto.getScope().trim().length() == 0) {
            authDto.setScope(null);
            return true;
        }
        return true;
    }

    private Map<Long, TerminalGroupAuthSyncResponse> groupAuthsToMap(List<GroupAuthInfo> authInfoList) throws BeansException {
        Map<Long, TerminalGroupAuthSyncResponse> response = new HashMap<>();
        if (!CollectionUtils.isEmpty(authInfoList)) {
            for (GroupAuthInfo authInfo : authInfoList) {
                Long groupId = authInfo.getGroupId();
                TerminalGroupAuthSyncResponse value = new TerminalGroupAuthSyncResponse();
                BeanUtils.copyProperties(authInfo, value);
                value.setScope(authInfo.getAuthScope());
                value.setPassTimeList(JsonUtils.jsoncastListType(TerminalPassTimeSyncResponse.class, authInfo.getPassTime()));
                response.put(groupId, value);
            }
        }
        return response;
    }

    private Map<Long, List<TerminalGroupMemberSyncResponse>> groupMembersToMap(List<TerminalGroupMemberSyncResponse> members) {
        Map<Long, List<TerminalGroupMemberSyncResponse>> response = new HashMap<>();
        if (members != null && members.size() > 0) {
            int size = members.size();
            for (int i = 0; i < size; i++) {
                Long groupId = members.get(i).getGroupId();
                List<TerminalGroupMemberSyncResponse> value = response.get(groupId);
                if (value == null || value.size() == 0) {
                    value = new ArrayList<>();
                }
                value.add(members.get(i));
                response.put(groupId, value);
            }
        }
        return response;
    }

    private void packagingGroupList(List<GroupAuthListResponse> responses, Long orgId) throws BeansException {
        if (responses != null && responses.size() > 0) {
            for (GroupAuthListResponse response : responses) {
                packagingGroupAuthListResponse(response, orgId);
            }
        }
    }

    private void unbindDefaultGroup(SaveGroupAuthRequest request) {
        if (request.getDefaultFlag() != null) {
            Group defaultGroup = this.groupMapper.getDefaultGroup(request.getOrgId());
            if (request.getDefaultFlag().intValue() != 1 || defaultGroup == null) {
                return;
            }
            AssertUtil.isTrue(Boolean.valueOf(this.groupMapper.unbindDefaultGroup(defaultGroup.getGroupId(), request.getOrgId()) > 0), OnpremiseErrorEnum.UNBIND_DEFAULT_GROUP_FAIL);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doSendNettyMessage(Long orgId, List<String> deviceSns) {
        Group group = this.groupMapper.getLastModify(orgId);
        if (MyListUtils.listIsEmpty(deviceSns)) {
            for (String deviceSn : deviceSns) {
                logger.info("====================send deviceSn:{}", deviceSn);
                this.nettyMessageApi.sendMsg(new SyncGroupRequest(Long.valueOf(group.getGmtModify().getTime()), group.getOrgId()), Integer.valueOf(SyncGroupRequest.MODEL_TYPE.type()), deviceSn);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doSendMemberNetty(Long orgId) {
        Member member = this.memberMapper.getLastModify(orgId);
        List<String> deviceSnList = this.deviceMapper.listDeviceSn(orgId);
        if (CollectionUtils.isEmpty(deviceSnList)) {
            return;
        }
        this.nettyMessageApi.sendMsg(new SyncMemberRequest(Long.valueOf(member == null ? 0L : member.getGmtModify().getTime()), orgId), Integer.valueOf(SyncMemberRequest.MODEL_TYPE.type()), deviceSnList);
    }

    private TerminalSyncResponse<TerminalSyncGroupResponse> packagingSyncGroupResponse(TerminalSyncRequest request, String lastSyncTime, Device device) throws BeansException {
        TerminalSyncResponse<TerminalSyncGroupResponse> terminalSyncResponse = new TerminalSyncResponse<>();
        List<Long> groupIds = this.groupDeviceMapper.listGroupIdByDeviceId(device.getDeviceSn(), device.getOrgId());
        logger.info("groupIds :{},lastSyncTime :{},orgId :{}", groupIds, lastSyncTime, request.getOrgId());
        if (CollectionUtils.isEmpty(groupIds)) {
            terminalSyncResponse.setSyncTime(CacheAdapter.getLastModifyTime(CacheKeyGenerateUtils.getGroupLastModifyTimeKey(request.getDeviceSn())).getLastModifyTime());
            return terminalSyncResponse;
        }
        List<TerminalSyncGroupResponse> syncGroupList = this.groupMapper.listSyncGroup(request.getOrgId(), lastSyncTime, groupIds);
        logger.info("==============syncGroupList size :{}=============", Integer.valueOf(syncGroupList.size()));
        if (CollectionUtils.isEmpty(syncGroupList)) {
            terminalSyncResponse.setSyncTime(CacheAdapter.getLastModifyTime(CacheKeyGenerateUtils.getGroupLastModifyTimeKey(request.getDeviceSn())).getLastModifyTime());
            return terminalSyncResponse;
        }
        List<TerminalSyncGroupResponse> syncInsertGroupList = new ArrayList<>();
        List<TerminalSyncGroupResponse> syncModifyGroupList = new ArrayList<>();
        List<TerminalSyncGroupResponse> syncDeleteGroupList = new ArrayList<>();
        List<GroupAuthInfo> groupAuthInfoList = this.groupAuthInfoMapper.listSyncGroupAuth(request.getOrgId(), lastSyncTime);
        Map<Long, TerminalGroupAuthSyncResponse> authMaps = groupAuthsToMap(groupAuthInfoList);
        for (TerminalSyncGroupResponse response : syncGroupList) {
            List<TerminalGroupAuthSyncResponse> terminalGroupAuthSyncResponseList = new ArrayList<>();
            terminalGroupAuthSyncResponseList.add(authMaps.get(response.getGroupId()));
            response.setGroupAuths(terminalGroupAuthSyncResponseList);
            GroupDevice groupDevice = this.groupDeviceMapper.getOneByDeviceAndGroup(device.getDeviceSn(), device.getOrgId(), response.getGroupId());
            if (groupDevice == null || groupDevice.getDeleteOrNot().intValue() == 1 || response.getDeleteOrNot().intValue() == 1) {
                if (request.getLastSyncTime().longValue() > 0) {
                    syncDeleteGroupList.add(response);
                }
            } else if (response.getGmtCreate().getTime() > request.getLastSyncTime().longValue() || groupDevice.getGmtCreate().getTime() > request.getLastSyncTime().longValue()) {
                syncInsertGroupList.add(response);
            } else {
                syncModifyGroupList.add(response);
            }
        }
        Group group = this.groupMapper.getLastModify(device.getOrgId());
        terminalSyncResponse.setSyncTime(Long.valueOf(group == null ? 0L : group.getGmtModify().getTime()));
        terminalSyncResponse.setSyncDeleteResult(syncDeleteGroupList);
        terminalSyncResponse.setSyncInsertResult(syncInsertGroupList);
        terminalSyncResponse.setSyncModifyResult(syncModifyGroupList);
        doCacheLastModifyTime(CacheKeyGenerateUtils.getGroupLastModifyTimeKey(request.getDeviceSn()), terminalSyncResponse.getSyncTime());
        return terminalSyncResponse;
    }

    private int exchangeAllDayFlag(GroupAuthDto authDto) {
        if (CollectionUtils.isEmpty(authDto.getPassTimeList()) || authDto.getPassTimeList().size() != 1) {
            return AllDayFlagEnum.ASSIGN_TIME.getValue();
        }
        return (Constants.DEFAULT_START_TIME.equals(authDto.getPassTimeList().get(0).getStartTime()) && Constants.DEFAULT_END_TIME.equals(authDto.getPassTimeList().get(0).getEndTime())) ? AllDayFlagEnum.ALL_DAY.getValue() : AllDayFlagEnum.ASSIGN_TIME.getValue();
    }

    private void insertAllGroupMember(Long orgId, Long groupId) {
        MemberListRequest memberListRequest = new MemberListRequest();
        memberListRequest.setOrgId(orgId);
        List<MemberListResponse> memberListResponses = this.memberMapper.memberList(memberListRequest);
        this.groupMemberMapper.insertGroupMemberList(groupId, orgId, memberListResponses);
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

    @Override // com.moredian.onpremise.api.group.GroupService
    public TerminalCheckPrivilegeResponse checkPrivilege(TerminalCheckPrivilegeRequest request) throws BeansException {
        AssertUtil.isNullOrEmpty(request.getOrgId(), OnpremiseErrorEnum.ORG_ID_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getMemberId(), OnpremiseErrorEnum.MEMBER_ID_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getDeviceSn(), OnpremiseErrorEnum.DEVICE_SN_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getTimeStamp(), OnpremiseErrorEnum.MEMBER_VERIFY_TIME_MUST_NOT_NULL);
        Long orgId = request.getOrgId();
        TerminalCheckPrivilegeResponse response = new TerminalCheckPrivilegeResponse();
        List<GroupDevice> groupDeviceList = this.groupDeviceMapper.listGroupByDeviceSn(request.getDeviceSn(), orgId);
        if (CollectionUtils.isEmpty(groupDeviceList)) {
            return response;
        }
        for (GroupDevice groupDevice : groupDeviceList) {
            Group group = this.groupMapper.getOneById(groupDevice.getGroupId(), orgId);
            if (group != null && (!group.getAllMemberFlag().equals(0) || memberInGroup(request.getMemberId(), groupDevice.getGroupId(), orgId))) {
                GroupAuthInfo authInfo = this.groupAuthInfoMapper.getAuthByGroupId(groupDevice.getGroupId(), orgId);
                if (authInfo == null) {
                    continue;
                } else {
                    GroupAuthDto authDto = new GroupAuthDto();
                    BeanUtils.copyProperties(authInfo, authDto);
                    authDto.setPassTimeList(JsonUtils.jsoncastListType(PassTimeDto.class, authInfo.getPassTime()));
                    Date verifyTime = MyDateUtils.getDate(request.getTimeStamp().longValue());
                    Date startDate = MyDateUtils.parseDate(authDto.getStartDate());
                    Date endDate = MyDateUtils.parseDate(authDto.getEndDate());
                    if (!group.getPermanentFlag().equals(0) || (!verifyTime.before(startDate) && !verifyTime.after(endDate))) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(verifyTime);
                        int day = calendar.get(7);
                        String[] scopeArr = authDto.getScope().split(",");
                        List<String> scopeList = Arrays.asList(scopeArr);
                        if (!group.getCycleFlag().equals(0) || scopeList.contains(String.valueOf(day))) {
                            boolean passTimeFlag = false;
                            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                            String verifyTimeStr = format.format(verifyTime);
                            Iterator<PassTimeDto> it = authDto.getPassTimeList().iterator();
                            while (true) {
                                if (!it.hasNext()) {
                                    break;
                                }
                                PassTimeDto passTimeDto = it.next();
                                if (verifyTimeStr.compareTo(passTimeDto.getStartTime()) >= 0 && verifyTimeStr.compareTo(passTimeDto.getEndTime()) <= 0) {
                                    passTimeFlag = true;
                                    break;
                                }
                            }
                            if (passTimeFlag) {
                                response.setTipText(group.getShowContent());
                                response.setTipSpeech(group.getSpeechContent());
                                response.setOpenDoor(1);
                                return response;
                            }
                        }
                    }
                }
            }
        }
        return response;
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x011a A[Catch: Exception -> 0x0262, TryCatch #0 {Exception -> 0x0262, blocks: (B:7:0x003e, B:8:0x006d, B:10:0x0077, B:12:0x009d, B:14:0x00a5, B:16:0x00be, B:17:0x00c8, B:24:0x010f, B:26:0x011a, B:28:0x0132, B:29:0x0142, B:31:0x0153, B:32:0x0160, B:18:0x00df, B:20:0x00f0, B:22:0x00f8, B:23:0x0108, B:33:0x016c, B:34:0x0181, B:35:0x019c, B:36:0x01ac, B:37:0x01b7, B:39:0x01c1, B:41:0x01de, B:42:0x01f8, B:44:0x0209, B:47:0x0226), top: B:52:0x003e }] */
    @Override // com.moredian.onpremise.api.group.GroupService
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.moredian.onpremise.core.model.response.UpdateGroupMemberResponse updateGroupMember(final com.moredian.onpremise.core.model.request.UpdateGroupMemberRequest r8) {
        /*
            Method dump skipped, instructions count: 635
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.moredian.onpremise.group.service.impl.GroupServiceImpl.updateGroupMember(com.moredian.onpremise.core.model.request.UpdateGroupMemberRequest):com.moredian.onpremise.core.model.response.UpdateGroupMemberResponse");
    }

    public boolean memberInGroup(Long memberId, Long groupId, Long orgId) {
        boolean flag = false;
        GroupMember groupMember = this.groupMemberMapper.getByMemberAndGroupId(memberId, groupId, orgId);
        if (groupMember != null) {
            flag = true;
        } else {
            List<Long> groupDeptIdList = this.groupMemberMapper.getDeptIdListByGroupId(groupId, orgId);
            Member member = this.memberMapper.getMemberInfoByMemberId(memberId, orgId);
            if (member != null) {
                String[] deptIdArr = member.getDeptId().split(",");
                List<String> deptIdLStrList = Arrays.asList(deptIdArr);
                List<Long> deptIdList = new ArrayList<>();
                for (String deptIdStr : deptIdLStrList) {
                    deptIdList.add(Long.getLong(deptIdStr));
                }
                if (!CollectionUtils.isEmpty(groupDeptIdList) && groupDeptIdList.removeAll(deptIdList)) {
                    flag = true;
                }
            }
        }
        return flag;
    }
}
