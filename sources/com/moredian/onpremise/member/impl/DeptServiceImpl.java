package com.moredian.onpremise.member.impl;

import com.github.pagehelper.PageHelper;
import com.moredian.onpremise.api.checkIn.CheckInService;
import com.moredian.onpremise.api.meal.MealCanteenService;
import com.moredian.onpremise.api.member.DeptService;
import com.moredian.onpremise.core.adapter.CacheAdapter;
import com.moredian.onpremise.core.common.enums.AccountGradeEnum;
import com.moredian.onpremise.core.common.enums.MemberTypeEnum;
import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.mapper.AccountAuthMapper;
import com.moredian.onpremise.core.mapper.AttendanceGroupDeviceMapper;
import com.moredian.onpremise.core.mapper.AttendanceGroupMemberMapper;
import com.moredian.onpremise.core.mapper.CheckInTaskMemberMapper;
import com.moredian.onpremise.core.mapper.DeptMapper;
import com.moredian.onpremise.core.mapper.DeviceMapper;
import com.moredian.onpremise.core.mapper.GroupDeviceMapper;
import com.moredian.onpremise.core.mapper.GroupMemberMapper;
import com.moredian.onpremise.core.mapper.MealCanteenMemberMapper;
import com.moredian.onpremise.core.mapper.MemberAuthInfoMapper;
import com.moredian.onpremise.core.mapper.MemberMapper;
import com.moredian.onpremise.core.model.domain.AccountAuth;
import com.moredian.onpremise.core.model.domain.AttendanceGroupMember;
import com.moredian.onpremise.core.model.domain.CheckInTaskMember;
import com.moredian.onpremise.core.model.domain.Dept;
import com.moredian.onpremise.core.model.domain.GroupMember;
import com.moredian.onpremise.core.model.domain.MealCanteenMember;
import com.moredian.onpremise.core.model.domain.Member;
import com.moredian.onpremise.core.model.request.BaseRequest;
import com.moredian.onpremise.core.model.request.DeleteDeptRequest;
import com.moredian.onpremise.core.model.request.DeptDetailRequest;
import com.moredian.onpremise.core.model.request.ListChildImmediateDeptRequest;
import com.moredian.onpremise.core.model.request.ListDeptRequest;
import com.moredian.onpremise.core.model.request.SaveDeptRequest;
import com.moredian.onpremise.core.model.request.TerminalDeptRequest;
import com.moredian.onpremise.core.model.response.DeptDetailResponse;
import com.moredian.onpremise.core.model.response.DeptMemberListResponse;
import com.moredian.onpremise.core.model.response.ListChildImmediateDeptResponse;
import com.moredian.onpremise.core.model.response.ListDeptNoConstructureResponse;
import com.moredian.onpremise.core.model.response.ListDeptResponse;
import com.moredian.onpremise.core.model.response.ListDeptResponseV2;
import com.moredian.onpremise.core.model.response.SaveDeptResponse;
import com.moredian.onpremise.core.model.response.TerminalDeptResponse;
import com.moredian.onpremise.core.model.response.UserLoginResponse;
import com.moredian.onpremise.core.utils.AssertUtil;
import com.moredian.onpremise.core.utils.JsonUtils;
import com.moredian.onpremise.core.utils.MyListUtils;
import com.moredian.onpremise.core.utils.PageList;
import com.moredian.onpremise.core.utils.Paginator;
import com.moredian.onpremise.iot.handle.NettyMessageApi;
import com.moredian.onpremise.model.SyncMemberRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
/* loaded from: onpremise-member-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/member/impl/DeptServiceImpl.class */
public class DeptServiceImpl implements DeptService {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) MemberServiceImpl.class);

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private GroupMemberMapper groupMemberMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private NettyMessageApi nettyMessageApi;

    @Autowired
    private CheckInService checkInService;

    @Autowired
    private MemberAuthInfoMapper memberAuthInfoMapper;

    @Autowired
    private GroupDeviceMapper groupDeviceMapper;

    @Autowired
    private AttendanceGroupDeviceMapper attendanceGroupDeviceMapper;

    @Autowired
    private AttendanceGroupMemberMapper attendanceGroupMemberMapper;

    @Autowired
    private MealCanteenService mealCanteenService;

    @Autowired
    private CheckInTaskMemberMapper checkInTaskMemberMapper;

    @Autowired
    private MealCanteenMemberMapper mealCanteenMemberMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private AccountAuthMapper accountAuthMapper;

    @Override // com.moredian.onpremise.api.member.DeptService
    @Transactional(rollbackFor = {RuntimeException.class})
    public SaveDeptResponse update(SaveDeptRequest request) throws BeansException {
        checkSaveDeptParams(request);
        AssertUtil.isTrue(Boolean.valueOf(request.getDeptId() != null && request.getDeptId().longValue() > 0), OnpremiseErrorEnum.DEPT_ID_MUST_NOT_NULL);
        DeptDetailResponse oldDept = this.deptMapper.getDeptDetail(request.getDeptId());
        AssertUtil.isNullOrEmpty(oldDept, OnpremiseErrorEnum.DEPT_NOT_FIND);
        Dept newDept = requestTransToDept(request);
        if (!oldDept.getSuperDeptId().equals(newDept.getSuperDeptId())) {
            List<Long> childDeptIdList = new ArrayList<>();
            getChildDeptIds(newDept.getDeptId(), request.getOrgId(), childDeptIdList);
            childDeptIdList.remove(request.getDeptId());
            doUpdateAboutDeptGroup(newDept, oldDept, request.getOrgId(), childDeptIdList);
            doUpdateAboutDeptAttendanceGroup(newDept, oldDept, request.getOrgId(), childDeptIdList);
            doUpdateAboutDeptCheckIn(newDept, oldDept, request.getOrgId(), childDeptIdList);
            doUpdateAboutDeptMealCanteen(newDept, oldDept, request.getOrgId(), childDeptIdList);
            doUpdateAboutDeptMember(request.getOrgId(), childDeptIdList, request.getDeptId());
            doSendNettyMessage(request.getOrgId());
        }
        doUpdateAboutDept(newDept, oldDept, request.getOrgId());
        SaveDeptResponse response = new SaveDeptResponse();
        BeanUtils.copyProperties(request, response);
        return response;
    }

    private void doUpdateAboutDeptMember(Long orgId, List<Long> childDeptIdList, Long deptId) {
        List<Long> deptIdList = new ArrayList<>();
        for (Long childDeptId : childDeptIdList) {
            deptIdList.add(childDeptId);
        }
        deptIdList.add(deptId);
        if (!CollectionUtils.isEmpty(deptIdList)) {
            this.memberMapper.updateModifyTime(orgId, null, deptIdList);
        }
    }

    private void doUpdateAboutDept(Dept requestDept, DeptDetailResponse queryDept, Long orgId) {
        int result = this.deptMapper.updateDept(requestDept);
        AssertUtil.isTrue(Boolean.valueOf(result > 0), OnpremiseErrorEnum.UPDATE_DEPT_FAIL);
        List<ListDeptResponse> childDepts = this.deptMapper.listChildDept(requestDept.getDeptId(), requestDept.getOrgId());
        if (childDepts != null && childDepts.size() > 0) {
            int result2 = this.deptMapper.updateChildDept(requestDept);
            AssertUtil.isTrue(Boolean.valueOf(result2 > 0), OnpremiseErrorEnum.UPDATE_DEPT_FAIL);
        }
    }

    private void doUpdateAboutDeptGroup(Dept newDept, DeptDetailResponse oldDept, Long orgId, List<Long> childDeptIdList) {
        List<Long> deptIdList = new ArrayList<>();
        for (Long childDeptId : childDeptIdList) {
            deptIdList.add(childDeptId);
        }
        List<GroupMember> groupMemberList = this.groupMemberMapper.listByDeptId(oldDept.getSuperDeptId(), orgId);
        List<GroupMember> newGroupMemberList = this.groupMemberMapper.listByDeptId(newDept.getSuperDeptId(), orgId);
        deptIdList.add(oldDept.getDeptId());
        if (!CollectionUtils.isEmpty(groupMemberList)) {
            List<Long> deleteGroupIdList = new ArrayList<>();
            for (GroupMember item : groupMemberList) {
                deleteGroupIdList.add(item.getGroupId());
            }
            if (!CollectionUtils.isEmpty(deptIdList)) {
                for (Long deptId : deptIdList) {
                    this.groupMemberMapper.deleteGroupDeptByGroupIdList(deptId, orgId, deleteGroupIdList);
                }
            }
        }
        if (!CollectionUtils.isEmpty(newGroupMemberList) && !CollectionUtils.isEmpty(deptIdList)) {
            List<GroupMember> memberLists = new ArrayList<>();
            for (GroupMember item2 : newGroupMemberList) {
                for (Long deptId2 : deptIdList) {
                    GroupMember groupMember = new GroupMember();
                    groupMember.setOrgId(orgId);
                    groupMember.setGroupId(item2.getGroupId());
                    groupMember.setType(Integer.valueOf(MemberTypeEnum.DEPT.getValue()));
                    groupMember.setDeptId(deptId2);
                    groupMember.setMemberId(0L);
                    groupMember.setDeleteOrNot(0);
                    groupMember.setConfirmFlag(0);
                    memberLists.add(groupMember);
                }
            }
            this.groupMemberMapper.batchInsert(memberLists);
        }
    }

    private void doUpdateAboutDeptAttendanceGroup(Dept newDept, DeptDetailResponse oldDept, Long orgId, List<Long> childDeptIdList) {
        List<Long> deptIdList = new ArrayList<>();
        for (Long childDeptId : childDeptIdList) {
            deptIdList.add(childDeptId);
        }
        List<AttendanceGroupMember> attendanceGroupMemberList = this.attendanceGroupMemberMapper.listByDeptId(oldDept.getSuperDeptId(), orgId);
        List<AttendanceGroupMember> newAttendanceGroupMemberList = this.attendanceGroupMemberMapper.listByDeptId(newDept.getSuperDeptId(), orgId);
        deptIdList.add(oldDept.getDeptId());
        if (!CollectionUtils.isEmpty(attendanceGroupMemberList)) {
            List<Long> deleteAttendanceGroupIdList = new ArrayList<>();
            for (AttendanceGroupMember item : attendanceGroupMemberList) {
                deleteAttendanceGroupIdList.add(item.getAttendanceGroupId());
            }
            if (!CollectionUtils.isEmpty(deptIdList)) {
                for (Long deptId : deptIdList) {
                    this.attendanceGroupMemberMapper.deleteGroupDeptByGroupIdList(deptId, orgId, deleteAttendanceGroupIdList);
                }
            }
        }
        if (!CollectionUtils.isEmpty(newAttendanceGroupMemberList) && !CollectionUtils.isEmpty(deptIdList)) {
            List<AttendanceGroupMember> memberLists = new ArrayList<>();
            for (AttendanceGroupMember item2 : newAttendanceGroupMemberList) {
                for (Long deptId2 : deptIdList) {
                    AttendanceGroupMember attendanceGroupMember = new AttendanceGroupMember();
                    attendanceGroupMember.setOrgId(orgId);
                    attendanceGroupMember.setAttendanceGroupId(item2.getAttendanceGroupId());
                    attendanceGroupMember.setType(Integer.valueOf(MemberTypeEnum.DEPT.getValue()));
                    attendanceGroupMember.setDeptId(deptId2);
                    attendanceGroupMember.setMemberId(0L);
                    attendanceGroupMember.setDeleteOrNot(0);
                    attendanceGroupMember.setConfirmFlag(0);
                    memberLists.add(attendanceGroupMember);
                }
            }
            this.attendanceGroupMemberMapper.batchInsert(memberLists);
        }
    }

    private void doUpdateAboutDeptCheckIn(Dept newDept, DeptDetailResponse oldDept, Long orgId, List<Long> childDeptIdList) {
        List<Long> deptIdList = new ArrayList<>();
        for (Long childDeptId : childDeptIdList) {
            deptIdList.add(childDeptId);
        }
        List<CheckInTaskMember> checkInTaskMemberList = this.checkInTaskMemberMapper.listByDeptId(oldDept.getSuperDeptId(), orgId);
        List<CheckInTaskMember> newCheckInTaskMemberList = this.checkInTaskMemberMapper.listByDeptId(newDept.getSuperDeptId(), orgId);
        deptIdList.add(oldDept.getDeptId());
        if (!CollectionUtils.isEmpty(checkInTaskMemberList)) {
            List<Long> deleteCheckInIdList = new ArrayList<>();
            for (CheckInTaskMember item : checkInTaskMemberList) {
                deleteCheckInIdList.add(item.getTaskId());
            }
            if (!CollectionUtils.isEmpty(deptIdList)) {
                for (Long deptId : deptIdList) {
                    this.checkInTaskMemberMapper.softDeleteByTaskIdList(deptId, orgId, deleteCheckInIdList);
                }
            }
        }
        if (!CollectionUtils.isEmpty(newCheckInTaskMemberList) && !CollectionUtils.isEmpty(deptIdList)) {
            List<CheckInTaskMember> memberLists = new ArrayList<>();
            for (CheckInTaskMember item2 : newCheckInTaskMemberList) {
                for (Long deptId2 : deptIdList) {
                    CheckInTaskMember checkInTaskMember = new CheckInTaskMember();
                    checkInTaskMember.setOrgId(orgId);
                    checkInTaskMember.setTaskId(item2.getTaskId());
                    checkInTaskMember.setType(Integer.valueOf(MemberTypeEnum.DEPT.getValue()));
                    checkInTaskMember.setDeptId(deptId2);
                    checkInTaskMember.setMemberId(0L);
                    checkInTaskMember.setConfirmFlag(0);
                    checkInTaskMember.setDeleteFlag(2);
                    memberLists.add(checkInTaskMember);
                }
            }
            this.checkInTaskMemberMapper.insertBatch(memberLists);
        }
    }

    private void doUpdateAboutDeptMealCanteen(Dept newDept, DeptDetailResponse oldDept, Long orgId, List<Long> childDeptIdList) {
        List<Long> deptIdList = new ArrayList<>();
        for (Long childDeptId : childDeptIdList) {
            deptIdList.add(childDeptId);
        }
        List<MealCanteenMember> mealCanteenMemberList = this.mealCanteenMemberMapper.listByDeptId(oldDept.getSuperDeptId(), orgId);
        List<MealCanteenMember> newMealCanteenMemberList = this.mealCanteenMemberMapper.listByDeptId(newDept.getSuperDeptId(), orgId);
        deptIdList.add(oldDept.getDeptId());
        if (!CollectionUtils.isEmpty(mealCanteenMemberList)) {
            List<Long> deleteMealCanteenIdList = new ArrayList<>();
            for (MealCanteenMember item : mealCanteenMemberList) {
                deleteMealCanteenIdList.add(item.getMealCanteenId());
            }
            if (!CollectionUtils.isEmpty(deptIdList)) {
                for (Long deptId : deptIdList) {
                    this.mealCanteenMemberMapper.deleteByMealCanteenIdList(deptId, orgId, deleteMealCanteenIdList);
                }
            }
        }
        if (!CollectionUtils.isEmpty(newMealCanteenMemberList) && !CollectionUtils.isEmpty(deptIdList)) {
            List<MealCanteenMember> memberLists = new ArrayList<>();
            for (MealCanteenMember item2 : newMealCanteenMemberList) {
                for (Long deptId2 : deptIdList) {
                    MealCanteenMember mealCanteenMember = new MealCanteenMember();
                    mealCanteenMember.setOrgId(orgId);
                    mealCanteenMember.setMealCanteenId(item2.getMealCanteenId());
                    mealCanteenMember.setType(Integer.valueOf(MemberTypeEnum.DEPT.getValue()));
                    mealCanteenMember.setDeptId(deptId2);
                    mealCanteenMember.setMemberId(0L);
                    mealCanteenMember.setDeleteOrNot(0);
                    mealCanteenMember.setConfirmFlag(0);
                    memberLists.add(mealCanteenMember);
                }
            }
            this.mealCanteenMemberMapper.batchInsert(memberLists);
        }
    }

    @Override // com.moredian.onpremise.api.member.DeptService
    @Transactional(rollbackFor = {RuntimeException.class})
    public SaveDeptResponse insert(SaveDeptRequest request) throws BeansException {
        checkSaveDeptParams(request);
        Dept dept = requestTransToDept(request);
        AssertUtil.isTrue(Boolean.valueOf(this.deptMapper.insertDept(dept) > 0), OnpremiseErrorEnum.INSERT_DEPT_FAIL);
        insertSuperDeptGroup(dept);
        insertSuperDeptAttendanceGroup(dept);
        insertSuperDeptCheckIn(dept);
        insertSuperDeptMealCanteen(dept);
        UserLoginResponse loginInfo = CacheAdapter.getLoginInfo(request.getSessionId());
        if (loginInfo.getAccountGrade().equals(Integer.valueOf(AccountGradeEnum.CHILDREN_ACCOUNT.getValue()))) {
            AccountAuth auth = this.accountAuthMapper.getOneByAccountId(request.getLoginAccountId(), request.getOrgId());
            loginInfo.setManageDeptId(getDeptId(auth.getManageDeptId(), request.getOrgId()));
            CacheAdapter.cacheLoginInfo(request.getSessionId(), loginInfo);
        }
        SaveDeptResponse response = new SaveDeptResponse();
        BeanUtils.copyProperties(request, response);
        response.setDeptId(dept.getDeptId());
        return response;
    }

    private String getDeptId(String deptIds, Long orgId) {
        if (StringUtils.isEmpty(deptIds)) {
            return "";
        }
        List<Long> allDeptIdList = new ArrayList<>();
        List<Long> selectedDeptIdList = new ArrayList<>();
        String[] selectedDeptIdArr = deptIds.split(",");
        List<Long> childDeptIdList = new ArrayList<>();
        for (String deptId : selectedDeptIdArr) {
            if (!org.springframework.util.StringUtils.isEmpty(deptId)) {
                selectedDeptIdList.add(Long.valueOf(Long.parseLong(deptId)));
                packagingChildDept(Long.valueOf(deptId), orgId, childDeptIdList);
            }
        }
        allDeptIdList.addAll(selectedDeptIdList);
        if (!CollectionUtils.isEmpty(childDeptIdList)) {
            allDeptIdList.addAll(childDeptIdList);
        }
        String result = JsonUtils.toJson(allDeptIdList);
        return result.substring(1, result.length() - 1);
    }

    @Override // com.moredian.onpremise.api.member.DeptService
    public boolean insertSuperDeptGroup(Dept dept) throws BeansException {
        List<GroupMember> groupDepts = this.groupMemberMapper.listByDeptId(dept.getSuperDeptId(), dept.getOrgId());
        GroupMember groupMember = new GroupMember();
        for (GroupMember groupDept : groupDepts) {
            BeanUtils.copyProperties(groupDept, groupMember);
            groupMember.setDeptId(dept.getDeptId());
            groupMember.setMemberId(0L);
            groupMember.setConfirmFlag(0);
            AssertUtil.isTrue(Boolean.valueOf(this.groupMemberMapper.insertGroupMember(groupMember) > 0), OnpremiseErrorEnum.INSERT_GROUP_MEMBER_FAIL);
        }
        return true;
    }

    @Override // com.moredian.onpremise.api.member.DeptService
    public boolean insertSuperDeptAttendanceGroup(Dept dept) throws BeansException {
        List<AttendanceGroupMember> attendanceGroupDepts = this.attendanceGroupMemberMapper.listByDeptId(dept.getSuperDeptId(), dept.getOrgId());
        AttendanceGroupMember attendanceGroupMember = new AttendanceGroupMember();
        for (AttendanceGroupMember groupDept : attendanceGroupDepts) {
            BeanUtils.copyProperties(groupDept, attendanceGroupMember);
            attendanceGroupMember.setDeptId(dept.getDeptId());
            attendanceGroupMember.setMemberId(0L);
            attendanceGroupMember.setConfirmFlag(0);
            AssertUtil.isTrue(Boolean.valueOf(this.attendanceGroupMemberMapper.insertAttendanceGroupMember(attendanceGroupMember) > 0), OnpremiseErrorEnum.INSERT_ATTENDANCE_GROUP_FAIL);
        }
        return true;
    }

    @Override // com.moredian.onpremise.api.member.DeptService
    public boolean insertSuperDeptCheckIn(Dept dept) throws BeansException {
        List<CheckInTaskMember> checkInTaskMemberList = this.checkInTaskMemberMapper.listByDeptId(dept.getSuperDeptId(), dept.getOrgId());
        CheckInTaskMember checkInTaskMember = new CheckInTaskMember();
        for (CheckInTaskMember item : checkInTaskMemberList) {
            BeanUtils.copyProperties(item, checkInTaskMember);
            checkInTaskMember.setDeptId(dept.getDeptId());
            checkInTaskMember.setMemberId(0L);
            AssertUtil.isTrue(Boolean.valueOf(this.checkInTaskMemberMapper.insertCheckInTaskMember(checkInTaskMember) > 0), OnpremiseErrorEnum.CHECK_IN_TASK_MEMBER_SAVE_FAIL);
        }
        return true;
    }

    @Override // com.moredian.onpremise.api.member.DeptService
    public boolean insertSuperDeptMealCanteen(Dept dept) throws BeansException {
        List<MealCanteenMember> mealCanteenMemberList = this.mealCanteenMemberMapper.listByDeptId(dept.getSuperDeptId(), dept.getOrgId());
        MealCanteenMember mealCanteenMember = new MealCanteenMember();
        for (MealCanteenMember item : mealCanteenMemberList) {
            BeanUtils.copyProperties(item, mealCanteenMember);
            mealCanteenMember.setDeptId(dept.getDeptId());
            mealCanteenMember.setMemberId(0L);
            mealCanteenMember.setConfirmFlag(0);
            AssertUtil.isTrue(Boolean.valueOf(this.mealCanteenMemberMapper.insertCanteenMember(mealCanteenMember) > 0), OnpremiseErrorEnum.INSERT_CANTEEN_MEMBER_FAIL);
        }
        return true;
    }

    @Override // com.moredian.onpremise.api.member.DeptService
    public DeptDetailResponse getDeptDetail(DeptDetailRequest request) {
        AssertUtil.isTrue(Boolean.valueOf(request.getDeptId() != null && request.getDeptId().longValue() > 0), OnpremiseErrorEnum.DEPT_ID_MUST_NOT_NULL);
        return this.deptMapper.getDeptDetail(request.getDeptId());
    }

    @Override // com.moredian.onpremise.api.member.DeptService
    @Transactional(rollbackFor = {RuntimeException.class})
    public boolean deleteDept(DeleteDeptRequest request) {
        Long deptId = request.getDeptId();
        AssertUtil.isTrue(Boolean.valueOf(deptId != null && deptId.longValue() > 0), OnpremiseErrorEnum.DEPT_ID_MUST_NOT_NULL);
        Dept dept = this.deptMapper.getTopDept(request.getOrgId());
        if (dept != null) {
            AssertUtil.isTrue(Boolean.valueOf(dept.getDeptId().longValue() != deptId.longValue()), OnpremiseErrorEnum.DELETE_TOP_DEPT_FAIL);
        }
        int memberNums = this.memberMapper.countByDeptId(request.getOrgId(), deptId);
        AssertUtil.isTrue(Boolean.valueOf(memberNums == 0), OnpremiseErrorEnum.DEPT_MEMBERS_NOT_EMPTY);
        int deptNums = this.deptMapper.countBySuperDeptId(request.getOrgId(), deptId);
        AssertUtil.isTrue(Boolean.valueOf(deptNums == 0), OnpremiseErrorEnum.CHILD_DEPT_NOT_EMPTY);
        AssertUtil.isTrue(Boolean.valueOf(this.deptMapper.deleteDept(deptId) > 0), OnpremiseErrorEnum.DELETE_DEPT_FAIL);
        deleteDeptGroup(request.getOrgId(), request.getDeptId());
        deleteDeptAttendanceGroup(request.getOrgId(), request.getDeptId());
        deleteDeptCheckIn(request.getOrgId(), request.getDeptId());
        deleteDeptMealCanteen(request.getOrgId(), request.getDeptId());
        return true;
    }

    private void deleteDeptGroup(Long orgId, Long deptId) {
        this.groupMemberMapper.deleteGroupDept(deptId, orgId, null);
    }

    private void deleteDeptAttendanceGroup(Long orgId, Long deptId) {
        this.attendanceGroupMemberMapper.deleteGroupDept(deptId, orgId, null);
    }

    private void deleteDeptCheckIn(Long orgId, Long deptId) {
        this.checkInTaskMemberMapper.softDeleteByDeptId(new Date(), orgId, deptId);
    }

    private void deleteDeptMealCanteen(Long orgId, Long deptId) {
        this.mealCanteenMemberMapper.deleteCanteenByDept(deptId, orgId, null);
    }

    @Override // com.moredian.onpremise.api.member.DeptService
    public List<ListDeptResponse> listDept(ListDeptRequest request) {
        List<ListDeptResponse> responses = getTopDepts(request);
        if (MyListUtils.listIsEmpty(responses)) {
            for (ListDeptResponse response : responses) {
                response.setChildDepts(packagingChildDept(response.getDeptId(), request.getOrgId(), request.getNeedMember()));
                response.setMemberNum(Integer.valueOf(packagingChildDeptMemberNum(response.getDeptId(), request.getOrgId(), 0, new ArrayList())));
                response.setDeptMemberLists(this.memberMapper.listMemberByDeptId(response.getDeptId(), request.getOrgId()));
            }
        }
        return responses;
    }

    @Override // com.moredian.onpremise.api.member.DeptService
    public List<ListDeptNoConstructureResponse> listDeptNoConstructure(BaseRequest request) {
        return this.deptMapper.listDeptNoConstructure(request.getOrgId(), UserLoginResponse.getAccountManageDeptId(request.getSessionId()));
    }

    @Override // com.moredian.onpremise.api.member.DeptService
    public PageList<ListChildImmediateDeptResponse> listChildImmediateDept(ListChildImmediateDeptRequest request) {
        Long deptId;
        if (request.getDeptId() != null && request.getDeptId().longValue() > 0) {
            deptId = request.getDeptId();
        } else {
            deptId = this.deptMapper.getTopDept(request.getOrgId()).getDeptId();
        }
        Paginator paginator = request.getPaginator();
        if (Paginator.checkPaginator(paginator)) {
            PageHelper.startPage(paginator.getPageNum(), paginator.getPageSize());
            List<ListChildImmediateDeptResponse> responses = this.deptMapper.listChildImmediateDept(request.getOrgId(), deptId);
            packChildImmediateDept(responses, request.getOrgId());
            return new PageList<>(responses);
        }
        List<ListChildImmediateDeptResponse> responses2 = this.deptMapper.listChildImmediateDept(request.getOrgId(), deptId);
        packChildImmediateDept(responses2, request.getOrgId());
        return new PageList<>(Paginator.initPaginator(responses2), responses2);
    }

    @Override // com.moredian.onpremise.api.member.DeptService
    public List<Long> packagingChildDept(Long superDeptId, Long orgId, List<Long> deptIds) {
        List<ListDeptResponse> childDepts = this.deptMapper.listChildDept(superDeptId, orgId);
        if (childDepts != null && childDepts.size() > 0) {
            for (ListDeptResponse childDept : childDepts) {
                deptIds.add(childDept.getDeptId());
                packagingChildDept(childDept.getDeptId(), orgId, deptIds);
            }
        }
        return deptIds;
    }

    @Override // com.moredian.onpremise.api.member.DeptService
    public String packagingDeptName(String deptIdStr, boolean needFullName) {
        String str;
        String[] deptIds = deptIdStr.split(",");
        String deptName = "";
        for (String deptId : deptIds) {
            Dept dept = this.deptMapper.getDeptById(Long.valueOf(deptId));
            AssertUtil.isNullOrEmpty(dept, OnpremiseErrorEnum.DEPT_NOT_FIND);
            if (dept.getDeptGrade().intValue() == 0) {
                str = "," + dept.getDeptName() + deptName;
            } else {
                String deptName2 = dept.getDeptName() + deptName;
                if (needFullName) {
                    str = "," + getOneDeptName(deptName2, dept.getSuperDeptId());
                } else {
                    str = "," + deptName2;
                }
            }
            deptName = str;
        }
        return deptName.substring(1);
    }

    private List<ListDeptResponse> packagingChildDept(Long superDeptId, Long orgId, Integer needMember) {
        List<ListDeptResponse> childDepts = this.deptMapper.listChildDept(superDeptId, orgId);
        if (childDepts != null && childDepts.size() > 0) {
            for (ListDeptResponse childDept : childDepts) {
                childDept.setChildDepts(packagingChildDept(childDept.getDeptId(), orgId, needMember));
                if (needMember != null && needMember.intValue() == 1) {
                    childDept.setDeptMemberLists(this.memberMapper.listMemberByDeptId(childDept.getDeptId(), orgId));
                }
                childDept.setMemberNum(Integer.valueOf(packagingChildDeptMemberNum(childDept.getDeptId(), orgId, 0, new ArrayList())));
            }
        }
        return childDepts;
    }

    private int packagingChildDeptMemberNum(Long superDeptId, Long orgId, Integer memberNum, List<Long> ids) {
        List<ListDeptResponse> childDepts = this.deptMapper.listChildDept(superDeptId, orgId);
        if (childDepts != null && childDepts.size() > 0) {
            for (ListDeptResponse childDept : childDepts) {
                ids.add(childDept.getDeptId());
                int packNum = packagingChildDeptMemberNum(childDept.getDeptId(), orgId, 0, ids);
                memberNum = Integer.valueOf(memberNum.intValue() + packNum);
            }
        }
        ids.remove(superDeptId);
        int num = this.memberMapper.countByDeptIdAndDeptIds(orgId, superDeptId, ids);
        Integer memberNum2 = Integer.valueOf(memberNum.intValue() + num);
        ids.add(superDeptId);
        return memberNum2.intValue();
    }

    private Dept requestTransToDept(SaveDeptRequest request) throws BeansException {
        Dept dept = new Dept();
        BeanUtils.copyProperties(request, dept);
        dept.setOrgId(request.getOrgId());
        return dept;
    }

    private boolean checkSaveDeptParams(SaveDeptRequest request) {
        Dept dept;
        AssertUtil.isNullOrEmpty(request.getDeptName(), OnpremiseErrorEnum.DEPT_NAME_MUST_NOT_NULL);
        if (request.getSuperDeptId() == null || request.getSuperDeptId().longValue() == 0) {
            dept = this.deptMapper.getTopDept(request.getOrgId());
        } else {
            dept = this.deptMapper.getDeptById(request.getSuperDeptId());
        }
        AssertUtil.isNullOrEmpty(dept, OnpremiseErrorEnum.SUPER_DEPT_NOT_FIND);
        request.setDeptGrade(Integer.valueOf(dept.getDeptGrade().intValue() + 1));
        request.setSuperDeptName(dept.getDeptName());
        request.setSuperDeptId(dept.getDeptId());
        Dept dept2 = this.deptMapper.getDeptByDeptName(request.getOrgId(), request.getDeptName(), request.getSuperDeptId(), Integer.valueOf(dept.getDeptGrade().intValue() + 1));
        if (request.getDeptId() == null || request.getDeptId().longValue() <= 0) {
            AssertUtil.isTrue(Boolean.valueOf(dept2 == null), OnpremiseErrorEnum.DEPT_NAME_ALREADY_EXISTS);
            return true;
        }
        AssertUtil.isTrue(Boolean.valueOf(dept2 == null || dept2.getDeptId().longValue() == request.getDeptId().longValue()), OnpremiseErrorEnum.DEPT_NAME_ALREADY_EXISTS);
        return true;
    }

    private void deleteChildDeptAndGroup(Long deptId, Long orgId) {
        List<ListDeptResponse> childDepts = this.deptMapper.listChildDept(deptId, orgId);
        if (MyListUtils.listIsEmpty(childDepts)) {
            for (ListDeptResponse response : childDepts) {
                deleteChildDeptAndGroup(response.getDeptId(), orgId);
                int memberNums = this.memberMapper.countByDeptId(orgId, response.getDeptId());
                AssertUtil.isTrue(Boolean.valueOf(memberNums == 0), OnpremiseErrorEnum.DEPT_MEMBERS_NOT_EMPTY);
                AssertUtil.isTrue(Boolean.valueOf(this.deptMapper.deleteDept(response.getDeptId()) > 0), OnpremiseErrorEnum.DELETE_DEPT_FAIL);
                List<Long> groupIds = this.groupMemberMapper.listGroupIdByDeptId(response.getDeptId(), orgId);
                if (MyListUtils.listIsEmpty(groupIds)) {
                    AssertUtil.isTrue(Boolean.valueOf(this.groupMemberMapper.deleteGroupDept(response.getDeptId(), orgId, null) > 0), OnpremiseErrorEnum.DELETE_GROUP_MEMBER_FAIL);
                }
                List<Long> attendanceGroupIds = this.attendanceGroupMemberMapper.listGroupIdByDeptId(response.getDeptId(), orgId);
                if (MyListUtils.listIsEmpty(attendanceGroupIds)) {
                    AssertUtil.isTrue(Boolean.valueOf(this.attendanceGroupMemberMapper.deleteGroupDept(response.getDeptId(), orgId, null) > 0), OnpremiseErrorEnum.DELETE_ATTENDANCE_GROUP_FAIL);
                }
            }
        }
    }

    private void deleteChildDeptGroup(List<GroupMember> groupMemberList, Long deptId, Long orgId) {
        List<ListDeptResponse> childDepts = this.deptMapper.listChildDept(deptId, orgId);
        if (childDepts != null && childDepts.size() > 0) {
            for (ListDeptResponse response : childDepts) {
                List<GroupMember> groups = this.groupMemberMapper.listByDeptId(response.getDeptId(), orgId);
                MyListUtils<GroupMember> utils = new MyListUtils<>();
                List<GroupMember> deleteGroups = utils.union(groups, groupMemberList);
                logger.info("delete dept group :{}", Integer.valueOf(deleteGroups.size()));
                logger.info("superGroups :{}", JsonUtils.toJson(groupMemberList));
                logger.info("groups :{}", JsonUtils.toJson(groups));
                if (deleteGroups != null && deleteGroups.size() > 0) {
                    for (GroupMember groupMember : deleteGroups) {
                        this.groupMemberMapper.deleteGroupDept(groupMember.getDeptId(), groupMember.getOrgId(), groupMember.getGroupId());
                    }
                }
                deleteChildDeptGroup(groupMemberList, response.getDeptId(), orgId);
            }
        }
    }

    private void deleteChildDeptAttendanceGroup(Long deptId, Long orgId) {
        List<ListDeptResponse> childDepts = this.deptMapper.listChildDept(deptId, orgId);
        if (childDepts != null && childDepts.size() > 0) {
            for (ListDeptResponse response : childDepts) {
                List<Long> groupIds = this.attendanceGroupMemberMapper.listGroupIdByDeptId(response.getDeptId(), orgId);
                if (MyListUtils.listIsEmpty(groupIds)) {
                    AssertUtil.isTrue(Boolean.valueOf(this.attendanceGroupMemberMapper.deleteGroupDept(response.getDeptId(), orgId, null) > 0), OnpremiseErrorEnum.DELETE_ATTENDANCE_GROUP_FAIL);
                }
                deleteChildDeptAttendanceGroup(response.getDeptId(), orgId);
            }
        }
    }

    private void insertChildDeptGroup(GroupMember groupDept) {
        List<Dept> childDepts = this.deptMapper.listChildDeptToReturnDept(groupDept.getDeptId(), groupDept.getOrgId());
        if (childDepts != null && childDepts.size() > 0) {
            for (Dept childDept : childDepts) {
                groupDept.setDeptId(childDept.getDeptId());
                groupDept.setConfirmFlag(0);
                AssertUtil.isTrue(Boolean.valueOf(this.groupMemberMapper.insertGroupMember(groupDept) > 0), OnpremiseErrorEnum.INSERT_GROUP_MEMBER_FAIL);
                insertChildDeptGroup(groupDept);
            }
        }
    }

    private void insertChildDeptAttendanceGroup(AttendanceGroupMember groupDept) {
        List<Dept> childDepts = this.deptMapper.listChildDeptToReturnDept(groupDept.getDeptId(), groupDept.getOrgId());
        if (childDepts != null && childDepts.size() > 0) {
            for (Dept childDept : childDepts) {
                groupDept.setDeptId(childDept.getDeptId());
                groupDept.setConfirmFlag(0);
                AssertUtil.isTrue(Boolean.valueOf(this.attendanceGroupMemberMapper.insertAttendanceGroupMember(groupDept) > 0), OnpremiseErrorEnum.INSERT_ATTENDANCE_GROUP_FAIL);
                insertChildDeptAttendanceGroup(groupDept);
            }
        }
    }

    private void insertChildMemberGroup(GroupMember groupDept) throws BeansException {
        List<DeptMemberListResponse> members = this.memberMapper.listMemberByDeptId(groupDept.getDeptId(), groupDept.getOrgId());
        for (DeptMemberListResponse member : members) {
            if (this.groupMemberMapper.getByDeptMemberAndGroupId(member.getMemberId(), groupDept.getDeptId(), groupDept.getGroupId(), groupDept.getOrgId()) == null) {
                GroupMember groupmember = new GroupMember();
                BeanUtils.copyProperties(groupDept, groupmember);
                groupmember.setType(Integer.valueOf(MemberTypeEnum.MEMBER.getValue()));
                groupmember.setMemberId(member.getMemberId());
                groupmember.setConfirmFlag(0);
                this.groupMemberMapper.insertGroupMember(groupmember);
            }
        }
    }

    private void packChildImmediateDept(List<ListChildImmediateDeptResponse> responses, Long orgId) {
        for (ListChildImmediateDeptResponse response : responses) {
            response.setMemberNum(Integer.valueOf(packagingChildDeptMemberNum(response.getDeptId(), orgId, 0, new ArrayList())));
        }
    }

    private String getOneDeptName(String deptName, Long superDeptId) {
        Dept superDept = this.deptMapper.getDeptById(superDeptId);
        if (superDept != null) {
            if (superDept.getDeptGrade().intValue() == 0) {
                return deptName;
            }
            deptName = superDept.getDeptName() + "-" + deptName;
            if (superDept.getSuperDeptId() != null && superDept.getSuperDeptId().longValue() > 0) {
                return getOneDeptName(deptName, superDept.getSuperDeptId());
            }
        }
        return deptName;
    }

    private List<Long> getChildDeptIds(Long deptId, Long orgId, List<Long> result) {
        result.add(deptId);
        List<ListDeptResponse> depts = this.deptMapper.listChildDept(deptId, orgId);
        if (MyListUtils.listIsEmpty(depts)) {
            for (ListDeptResponse response : depts) {
                getChildDeptIds(response.getDeptId(), orgId, result);
            }
        }
        return result;
    }

    private void doSendNettyMessage(Long orgId) {
        Member member = this.memberMapper.getLastModify(orgId);
        List<String> deviceSnList = this.deviceMapper.listDeviceSn(orgId);
        if (CollectionUtils.isEmpty(deviceSnList)) {
            return;
        }
        this.nettyMessageApi.sendMsg(new SyncMemberRequest(Long.valueOf(member == null ? 0L : member.getGmtModify().getTime()), orgId), Integer.valueOf(SyncMemberRequest.MODEL_TYPE.type()), deviceSnList);
    }

    private List<ListDeptResponse> getTopDepts(ListDeptRequest request) {
        String[] deptArray;
        List<ListDeptResponse> responses = new ArrayList<>();
        UserLoginResponse loginInfo = CacheAdapter.getLoginInfo(request.getSessionId());
        if (loginInfo != null) {
            List<String> deptIds = null;
            if (loginInfo.getAccountGrade().intValue() == AccountGradeEnum.CHILDREN_ACCOUNT.getValue()) {
                String deptId = loginInfo.getManageDeptId();
                if (!org.springframework.util.StringUtils.isEmpty(deptId) && (deptArray = deptId.split(",")) != null) {
                    if (deptArray.length > 0) {
                        deptIds = Arrays.asList(deptArray);
                    } else {
                        deptIds = new ArrayList();
                        deptIds.add("0");
                    }
                }
            }
            responses = this.deptMapper.getTopDepts(request.getOrgId(), deptIds);
        }
        return responses;
    }

    private void updateMemberAuthInfo(Long deptId, Long orgId, Long groupId, Integer type) {
        List<String> deviceSns = this.groupDeviceMapper.listDeviceSnByGroupId(groupId, orgId);
        this.memberAuthInfoMapper.updateByDeptId(deptId, orgId, groupId, null, null, deviceSns, type);
    }

    private void updateMemberAuthInfo(Long deptId, Long orgId, List<Long> groupIds, Integer type) {
        List<String> deviceSns = this.groupDeviceMapper.listDeviceSnByGroupIds(groupIds, orgId);
        if (MyListUtils.listIsEmpty(deviceSns) || MyListUtils.listIsEmpty(groupIds)) {
            this.memberAuthInfoMapper.updateBatchByDeptId(deptId, orgId, groupIds, null, null, deviceSns, type);
        }
    }

    private void updateMemberAuthInfoByAttendance(Long deptId, Long orgId, Long attendanceGroupId, Integer type) {
        List<String> deviceSns = this.attendanceGroupDeviceMapper.listDeviceSnByTypeAndGroupId(attendanceGroupId, orgId, null);
        this.memberAuthInfoMapper.updateByDeptId(deptId, orgId, null, null, attendanceGroupId, deviceSns, type);
    }

    private void updateMemberAuthInfoByAttendance(Long deptId, Long orgId, List<Long> attendanceGroupIds, Integer type) {
        List<String> deviceSns = this.attendanceGroupDeviceMapper.listDeviceSnByTypeAndGroupIds(attendanceGroupIds, orgId, null);
        if (MyListUtils.listIsEmpty(deviceSns) || MyListUtils.listIsEmpty(attendanceGroupIds)) {
            this.memberAuthInfoMapper.updateBatchByDeptId(deptId, orgId, null, null, attendanceGroupIds, deviceSns, type);
        }
    }

    @Override // com.moredian.onpremise.api.member.DeptService
    public TerminalDeptResponse listDeptForDevice(TerminalDeptRequest request) throws BeansException {
        if (request.getOrgId() == null) {
            request.setOrgId(1L);
        }
        Dept dept = this.deptMapper.getTopDept(request.getOrgId());
        AssertUtil.isNullOrEmpty(dept, OnpremiseErrorEnum.TOP_DEPT_NOT_FIND);
        TerminalDeptResponse response = new TerminalDeptResponse();
        BeanUtils.copyProperties(dept, response);
        buildChildDept(response);
        return response;
    }

    private void buildChildDept(TerminalDeptResponse terminalDeptResponse) throws BeansException {
        List<Dept> childDeptList = this.deptMapper.listChildDeptToReturnDept(terminalDeptResponse.getDeptId(), terminalDeptResponse.getOrgId());
        List<TerminalDeptResponse> childTerminalDeptResponseList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(childDeptList)) {
            for (Dept childDept : childDeptList) {
                TerminalDeptResponse childTerminalDeptResponse = new TerminalDeptResponse();
                BeanUtils.copyProperties(childDept, childTerminalDeptResponse);
                childTerminalDeptResponseList.add(childTerminalDeptResponse);
                buildChildDept(childTerminalDeptResponse);
            }
        }
        terminalDeptResponse.setChildDeptList(childTerminalDeptResponseList);
    }

    @Override // com.moredian.onpremise.api.member.DeptService
    public List<ListDeptResponseV2> listDeptV2(ListDeptRequest request) throws BeansException {
        List resp = new ArrayList();
        List<ListDeptResponseV2> responses = getManageDepts(request);
        if (!CollectionUtils.isEmpty(responses)) {
            resp = createDeptTree(responses);
        }
        return resp;
    }

    @Override // com.moredian.onpremise.api.member.DeptService
    public String findSuperId(Long deptId, Integer depth) {
        if (depth.intValue() == 0) {
            return deptId + "";
        }
        Dept dept = this.deptMapper.getDeptById(deptId);
        if (dept.getSuperDeptId().longValue() != 0) {
            String superDeptId = findSuperId(dept.getSuperDeptId(), Integer.valueOf(depth.intValue() - 1));
            if (deptId.equals(dept.getDeptId())) {
                return deptId + "," + superDeptId;
            }
        }
        return deptId + "";
    }

    private List<ListDeptResponseV2> createDeptTree(List<ListDeptResponseV2> responses) {
        Map<Long, List<ListDeptResponseV2>> groupByParentIdMap = new HashMap<>();
        for (ListDeptResponseV2 item : responses) {
            List<ListDeptResponseV2> listDeptResponseList = new ArrayList<>();
            if (!groupByParentIdMap.containsKey(item.getSuperDeptId())) {
                listDeptResponseList.add(item);
                groupByParentIdMap.put(item.getSuperDeptId(), listDeptResponseList);
            } else {
                groupByParentIdMap.get(item.getSuperDeptId()).add(item);
            }
        }
        Map<Long, ListDeptResponseV2> dataMap = new HashMap<>();
        for (ListDeptResponseV2 item2 : responses) {
            dataMap.put(item2.getDeptId(), item2);
        }
        ArrayList resp = new ArrayList();
        for (Long parentId : groupByParentIdMap.keySet()) {
            if (dataMap.containsKey(parentId)) {
                List<ListDeptResponseV2> child = dataMap.get(parentId).getChildDepts();
                if (CollectionUtils.isEmpty(child)) {
                    child = new ArrayList();
                }
                child.addAll(groupByParentIdMap.get(parentId));
                dataMap.get(parentId).setChildDepts(child);
            } else {
                resp.addAll(groupByParentIdMap.get(parentId));
            }
        }
        return resp;
    }

    private List<ListDeptResponseV2> getManageDepts(ListDeptRequest request) throws BeansException {
        List<ListDeptResponseV2> responses = new ArrayList<>();
        List<String> manageDeptIdList = new ArrayList<>();
        UserLoginResponse loginInfo = CacheAdapter.getLoginInfo(request.getSessionId());
        if (loginInfo != null && loginInfo.getAccountGrade().intValue() == AccountGradeEnum.CHILDREN_ACCOUNT.getValue()) {
            String manageDeptId = loginInfo.getManageDeptId();
            if (!org.springframework.util.StringUtils.isEmpty(manageDeptId)) {
                String[] deptArray = manageDeptId.split(",");
                for (String deptId : deptArray) {
                    manageDeptIdList.add(deptId);
                }
            } else {
                manageDeptIdList.add("0");
            }
        }
        List<Dept> deptList = this.deptMapper.listByDeptIds(request.getOrgId(), manageDeptIdList);
        for (Dept dept : deptList) {
            ListDeptResponseV2 listDeptResponse = new ListDeptResponseV2();
            BeanUtils.copyProperties(dept, listDeptResponse);
            responses.add(listDeptResponse);
        }
        return responses;
    }
}
