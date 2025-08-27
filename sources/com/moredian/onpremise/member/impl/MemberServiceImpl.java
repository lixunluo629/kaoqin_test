package com.moredian.onpremise.member.impl;

import com.github.pagehelper.PageHelper;
import com.moredian.FaceDet;
import com.moredian.onpremise.api.member.DeptService;
import com.moredian.onpremise.api.member.MemberService;
import com.moredian.onpremise.api.server.ConfigService;
import com.moredian.onpremise.core.adapter.CacheAdapter;
import com.moredian.onpremise.core.common.constants.Constants;
import com.moredian.onpremise.core.common.enums.AccountGradeEnum;
import com.moredian.onpremise.core.common.enums.ExtractFeatureStatusEnum;
import com.moredian.onpremise.core.common.enums.InputFaceChannelEnum;
import com.moredian.onpremise.core.common.enums.MemberGenderEnum;
import com.moredian.onpremise.core.common.enums.NeedExtractFeatureEnum;
import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.config.UploadConfig;
import com.moredian.onpremise.core.exception.BizException;
import com.moredian.onpremise.core.mapper.AccountAuthMapper;
import com.moredian.onpremise.core.mapper.AccountMapper;
import com.moredian.onpremise.core.mapper.AttendanceGroupDeviceMapper;
import com.moredian.onpremise.core.mapper.AttendanceGroupMemberMapper;
import com.moredian.onpremise.core.mapper.CheckInTaskMemberMapper;
import com.moredian.onpremise.core.mapper.DeptMapper;
import com.moredian.onpremise.core.mapper.DeviceMapper;
import com.moredian.onpremise.core.mapper.GroupDeviceMapper;
import com.moredian.onpremise.core.mapper.GroupMapper;
import com.moredian.onpremise.core.mapper.GroupMemberMapper;
import com.moredian.onpremise.core.mapper.MealCanteenMemberMapper;
import com.moredian.onpremise.core.mapper.MealCardMemberMapper;
import com.moredian.onpremise.core.mapper.MealMemberMapper;
import com.moredian.onpremise.core.mapper.MemberMapper;
import com.moredian.onpremise.core.mapper.OrganizationMapper;
import com.moredian.onpremise.core.mapper.UpdateEigenvalueRecordMapper;
import com.moredian.onpremise.core.mapper.VerifyRecordMapper;
import com.moredian.onpremise.core.model.domain.Account;
import com.moredian.onpremise.core.model.domain.AccountAuth;
import com.moredian.onpremise.core.model.domain.AttendanceGroupMember;
import com.moredian.onpremise.core.model.domain.CheckInTaskMember;
import com.moredian.onpremise.core.model.domain.Dept;
import com.moredian.onpremise.core.model.domain.GroupMember;
import com.moredian.onpremise.core.model.domain.MealCanteenMember;
import com.moredian.onpremise.core.model.domain.Member;
import com.moredian.onpremise.core.model.domain.Organization;
import com.moredian.onpremise.core.model.domain.UpdateEigenvalueRecord;
import com.moredian.onpremise.core.model.domain.VerifyRecord;
import com.moredian.onpremise.core.model.dto.DeviceDto;
import com.moredian.onpremise.core.model.dto.GroupDeviceGroupDto;
import com.moredian.onpremise.core.model.dto.GroupDto;
import com.moredian.onpremise.core.model.dto.GroupMemberDto;
import com.moredian.onpremise.core.model.info.CacheExtractFeatureStatusInfo;
import com.moredian.onpremise.core.model.info.CacheHeartBeatInfo;
import com.moredian.onpremise.core.model.info.DeviceLastModifyTimeInfo;
import com.moredian.onpremise.core.model.request.BatchInsertMemberDetailRequest;
import com.moredian.onpremise.core.model.request.BatchInsertMemberRequest;
import com.moredian.onpremise.core.model.request.BatchUpdateMemberDeptRequest;
import com.moredian.onpremise.core.model.request.DeleteFaceRequest;
import com.moredian.onpremise.core.model.request.DeleteMemberRequest;
import com.moredian.onpremise.core.model.request.ExcelMemberRequest;
import com.moredian.onpremise.core.model.request.ExtractNoticeRequest;
import com.moredian.onpremise.core.model.request.H5CertifyMemberRequest;
import com.moredian.onpremise.core.model.request.ImageUploadBase64Request;
import com.moredian.onpremise.core.model.request.MatchFeatureRequest;
import com.moredian.onpremise.core.model.request.MemberDetailsRequest;
import com.moredian.onpremise.core.model.request.MemberFaceExportRequest;
import com.moredian.onpremise.core.model.request.MemberGroupListRequest;
import com.moredian.onpremise.core.model.request.MemberInputFaceRecordListRequest;
import com.moredian.onpremise.core.model.request.MemberListRequest;
import com.moredian.onpremise.core.model.request.QueryDeptAndMemberRequest;
import com.moredian.onpremise.core.model.request.QueryExtractFeatureResultRequest;
import com.moredian.onpremise.core.model.request.SaveMemberFaceRequest;
import com.moredian.onpremise.core.model.request.SaveMemberRequest;
import com.moredian.onpremise.core.model.request.TerminalFindMemberRequest;
import com.moredian.onpremise.core.model.request.TerminalSaveMemberRequest;
import com.moredian.onpremise.core.model.request.TerminalSearchMemberRequest;
import com.moredian.onpremise.core.model.request.TerminalSyncEigenvalueValueRequest;
import com.moredian.onpremise.core.model.request.TerminalSyncRequest;
import com.moredian.onpremise.core.model.request.VerifyRecordRequest;
import com.moredian.onpremise.core.model.response.BatchInsertMemberResponse;
import com.moredian.onpremise.core.model.response.BatchUpdateMemberDeptResponse;
import com.moredian.onpremise.core.model.response.ExcelMemberResponse;
import com.moredian.onpremise.core.model.response.ExtractNoticeResponse;
import com.moredian.onpremise.core.model.response.FaceReloadFailResponse;
import com.moredian.onpremise.core.model.response.FaceReloadProgressResponse;
import com.moredian.onpremise.core.model.response.MemberDetailsDeptResponse;
import com.moredian.onpremise.core.model.response.MemberDetailsGroupResponse;
import com.moredian.onpremise.core.model.response.MemberDetailsResponse;
import com.moredian.onpremise.core.model.response.MemberGroupListResponse;
import com.moredian.onpremise.core.model.response.MemberInputFaceRecordListResponse;
import com.moredian.onpremise.core.model.response.MemberListResponse;
import com.moredian.onpremise.core.model.response.QueryDeptAndMemberResponse;
import com.moredian.onpremise.core.model.response.QueryExtractFeatureResultResponse;
import com.moredian.onpremise.core.model.response.SaveImageResponse;
import com.moredian.onpremise.core.model.response.SaveMemberFaceByDeviceResponse;
import com.moredian.onpremise.core.model.response.SaveMemberFaceResponse;
import com.moredian.onpremise.core.model.response.SaveMemberResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncEigenvalueValueResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncMemberResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncResponse;
import com.moredian.onpremise.core.model.response.UserLoginResponse;
import com.moredian.onpremise.core.utils.AssertUtil;
import com.moredian.onpremise.core.utils.CacheKeyGenerateUtils;
import com.moredian.onpremise.core.utils.IntByteUtils;
import com.moredian.onpremise.core.utils.JsonUtils;
import com.moredian.onpremise.core.utils.MyBase64Utils;
import com.moredian.onpremise.core.utils.MyDateUtils;
import com.moredian.onpremise.core.utils.MyFileUtils;
import com.moredian.onpremise.core.utils.MyListUtils;
import com.moredian.onpremise.core.utils.PageList;
import com.moredian.onpremise.core.utils.Paginator;
import com.moredian.onpremise.iot.IOTSession;
import com.moredian.onpremise.iot.handle.NettyMessageApi;
import com.moredian.onpremise.model.ExtractFeatureRequest;
import com.moredian.onpremise.model.NoticeDeviceExtractRequest;
import com.moredian.onpremise.model.SyncAccountRequest;
import com.moredian.onpremise.model.SyncMemberRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.imageio.ImageIO;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
/* loaded from: onpremise-member-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/member/impl/MemberServiceImpl.class */
public class MemberServiceImpl implements MemberService {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) MemberServiceImpl.class);
    private static volatile Map<String, Integer> counter = new ConcurrentHashMap();
    private static volatile List<Long> memberIds = new ArrayList();
    private static volatile int syncMemberCount = 0;
    private static volatile int syncMemberEigenvalueValueCount = 0;
    private static volatile int saveMemberFaceCount = 0;

    @Value("${onpremise.file.path}")
    private String saveFilePath;

    @Value("${onpremise.save.image.path}")
    private String saveImagePath;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private UpdateEigenvalueRecordMapper eigenvalueRecordMapper;

    @Autowired
    private GroupMemberMapper groupMemberMapper;

    @Autowired
    private OrganizationMapper orgMapper;

    @Autowired
    private NettyMessageApi nettyMessageApi;

    @Autowired
    private UploadConfig uplocadConfig;

    @Autowired
    private GroupDeviceMapper groupDeviceMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private AccountAuthMapper accountAuthMapper;

    @Autowired
    private DeptService deptService;

    @Autowired
    private AttendanceGroupMemberMapper attendanceGroupMemberMapper;

    @Autowired
    private AttendanceGroupDeviceMapper attendanceGroupDeviceMapper;

    @Autowired
    private CheckInTaskMemberMapper checkInTaskMemberMapper;

    @Autowired
    private MealCanteenMemberMapper mealCanteenMemberMapper;

    @Autowired
    private MealCardMemberMapper mealCardMemberMapper;

    @Autowired
    private VerifyRecordMapper verifyRecordMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private ConfigService configService;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private MealMemberMapper mealMemberMapper;

    @Override // com.moredian.onpremise.api.member.MemberService
    public PageList<MemberListResponse> memberList(MemberListRequest request) {
        Paginator paginator = request.getPaginator();
        if (request.getNeedSubMember() != null && request.getNeedSubMember().intValue() == 1) {
            List deptIds = new ArrayList();
            deptIds.add(request.getDeptId());
            request.setDeptIds(this.deptService.packagingChildDept(request.getDeptId(), request.getOrgId(), deptIds));
        }
        request.setManageDeptIds(UserLoginResponse.getAccountManageDeptId(request.getSessionId()));
        if (Paginator.checkPaginator(paginator)) {
            PageHelper.startPage(paginator.getPageNum(), paginator.getPageSize());
            List<MemberListResponse> list = this.memberMapper.memberList(request);
            if (request.getNeedDeptName() == null || request.getNeedDeptName().intValue() == 1) {
                for (MemberListResponse response : list) {
                    response.setDeptName(this.deptService.packagingDeptName(response.getDeptId(), true));
                }
            }
            return new PageList<>(list);
        }
        List<MemberListResponse> list2 = this.memberMapper.memberList(request);
        if (request.getNeedDeptName() == null || request.getNeedDeptName().intValue() == 1) {
            for (MemberListResponse response2 : list2) {
                response2.setDeptName(this.deptService.packagingDeptName(response2.getDeptId(), true));
            }
        }
        return new PageList<>(Paginator.initPaginator(list2), list2);
    }

    @Override // com.moredian.onpremise.api.member.MemberService
    @Transactional(rollbackFor = {RuntimeException.class})
    public SaveMemberResponse updateMember(SaveMemberRequest request) throws BeansException {
        AssertUtil.isTrue(Boolean.valueOf(request.getMemberId() != null && request.getMemberId().longValue() > 0), OnpremiseErrorEnum.MEMBER_ID_MUST_NOT_NULL);
        checkSaveMemberRequest(request);
        AssertUtil.isNullOrEmpty(this.memberMapper.getMemberInfoByMemberId(request.getMemberId(), request.getOrgId()), OnpremiseErrorEnum.MEMBER_NOT_FIND);
        Member member = saveMemberRequestToMember(request);
        AssertUtil.isTrue(Boolean.valueOf(this.memberMapper.saveMember(member) > 0), OnpremiseErrorEnum.UPDATE_MEMBER_FAIL);
        saveFaceInfo(request);
        doSendNettyMsg(member.getOrgId());
        SaveMemberResponse response = new SaveMemberResponse();
        BeanUtils.copyProperties(request, response);
        return response;
    }

    @Override // com.moredian.onpremise.api.member.MemberService
    @Transactional(rollbackFor = {RuntimeException.class}, isolation = Isolation.SERIALIZABLE)
    public SaveMemberResponse insertMember(SaveMemberRequest request) throws BeansException {
        checkSaveMemberRequest(request);
        if (request.getMemberJoinTime() != null && request.getMemberJoinTime().trim().length() > 0) {
            AssertUtil.isTrue(Boolean.valueOf(MyDateUtils.isValidDate(request.getMemberJoinTime())), OnpremiseErrorEnum.TIME_FORMAT_ERROR);
        }
        Member member = saveMemberRequestToMember(request);
        AssertUtil.isTrue(Boolean.valueOf(this.memberMapper.addMember(member) > 0), OnpremiseErrorEnum.SAVE_MEMBER_FAIL);
        request.setMemberId(member.getMemberId());
        saveFaceInfo(request);
        if (request.getVerifyFaceUrl() != null && request.getVerifyFaceUrl().trim().length() > 0) {
            doSendNettyMsg(request.getOrgId());
        }
        SaveMemberResponse response = new SaveMemberResponse();
        BeanUtils.copyProperties(request, response);
        response.setMemberId(member.getMemberId());
        return response;
    }

    @Override // com.moredian.onpremise.api.member.MemberService
    @Transactional(rollbackFor = {RuntimeException.class})
    public boolean deleteMember(DeleteMemberRequest request) {
        AssertUtil.isNullOrEmpty(request.getMemberIds(), OnpremiseErrorEnum.MEMBER_ID_MUST_NOT_NULL);
        String[] memberIds2 = request.getMemberIds().split(",");
        for (String memberId : memberIds2) {
            Member member = this.memberMapper.getMemberInfoByMemberId(Long.valueOf(memberId), request.getOrgId());
            AssertUtil.isNullOrEmpty(member, OnpremiseErrorEnum.MEMBER_NOT_FIND);
            doDeleteMember(member.getMemberId(), request.getOrgId(), member.getDeptId());
        }
        doSendNettyMsg(request.getOrgId());
        return true;
    }

    @Override // com.moredian.onpremise.api.member.MemberService
    @Transactional(rollbackFor = {RuntimeException.class})
    public boolean deleteMemberByJobNum(DeleteMemberRequest request) {
        AssertUtil.isNullOrEmpty(request.getMemberJobNums(), OnpremiseErrorEnum.MEMBER_JOB_NUM_MUST_NOT_NULL);
        String[] memberJobNums = request.getMemberJobNums().split(",");
        for (String memberJobNum : memberJobNums) {
            Member member = this.memberMapper.getMemberInfoByJobNum(memberJobNum, request.getOrgId());
            AssertUtil.isNullOrEmpty(member, OnpremiseErrorEnum.MEMBER_NOT_FIND);
            doDeleteMember(member.getMemberId(), request.getOrgId(), member.getDeptId());
        }
        doSendNettyMsg(request.getOrgId());
        return true;
    }

    @Override // com.moredian.onpremise.api.member.MemberService
    public MemberDetailsResponse getMemberDetails(MemberDetailsRequest request) {
        AssertUtil.isTrue(Boolean.valueOf(request.getMemberId() != null && request.getMemberId().longValue() > 0), OnpremiseErrorEnum.MEMBER_ID_MUST_NOT_NULL);
        Member member = this.memberMapper.getMemberInfoByMemberId(request.getMemberId(), request.getOrgId());
        AssertUtil.isNullOrEmpty(member, OnpremiseErrorEnum.MEMBER_NOT_FIND);
        return packageMemberDetailsResponse(request, member);
    }

    private MemberDetailsResponse packageMemberDetailsResponse(MemberDetailsRequest request, Member member) throws BeansException {
        MemberDetailsResponse response = new MemberDetailsResponse();
        BeanUtils.copyProperties(member, response);
        response.setDeptName(this.deptService.packagingDeptName(member.getDeptId(), false));
        List<MemberDetailsDeptResponse> deptList = new ArrayList<>();
        List<MemberDetailsGroupResponse> groupResponses = new ArrayList<>();
        List<Long> groupIds = new ArrayList<>();
        if (StringUtils.isNotEmpty(member.getDeptId())) {
            String[] deptIds = member.getDeptId().split(",");
            for (String deptId : deptIds) {
                Dept dept = this.deptMapper.getDeptById(Long.valueOf(deptId));
                AssertUtil.isNullOrEmpty(dept, OnpremiseErrorEnum.DEPT_NOT_FIND);
                MemberDetailsDeptResponse tempDept = new MemberDetailsDeptResponse();
                tempDept.setDeptId(dept.getDeptId());
                tempDept.setDeptName(dept.getDeptName());
                deptList.add(tempDept);
                List<GroupMember> groupMemberList = this.groupMemberMapper.listByDeptId(dept.getDeptId(), dept.getOrgId());
                for (GroupMember groupMember : groupMemberList) {
                    if (!groupIds.contains(groupMember.getGroupId())) {
                        groupIds.add(groupMember.getGroupId());
                    }
                }
            }
        }
        response.setDeptList(deptList);
        List<GroupMemberDto> groupMemberList2 = this.groupMemberMapper.listByMemberId(member.getMemberId(), member.getOrgId());
        for (GroupMemberDto groupMemberDto : groupMemberList2) {
            if (!groupIds.contains(groupMemberDto.getGroupId())) {
                groupIds.add(groupMemberDto.getGroupId());
            }
        }
        if (!CollectionUtils.isEmpty(groupIds)) {
            List<GroupDeviceGroupDto> groups = this.groupMapper.listByGroupId(groupIds, request.getOrgId());
            for (GroupDeviceGroupDto groupDeviceGroupDto : groups) {
                MemberDetailsGroupResponse memberDetailsGroupResponse = new MemberDetailsGroupResponse();
                BeanUtils.copyProperties(groupDeviceGroupDto, memberDetailsGroupResponse);
                groupResponses.add(memberDetailsGroupResponse);
            }
        }
        response.setGroupList(groupResponses);
        return response;
    }

    @Override // com.moredian.onpremise.api.member.MemberService
    public MemberDetailsResponse getMemberDetailsByJobNum(MemberDetailsRequest request) {
        AssertUtil.isNullOrEmpty(request.getMemberJobNum(), OnpremiseErrorEnum.MEMBER_JOB_NUM_MUST_NOT_NULL);
        Member member = this.memberMapper.getMemberInfoByJobNum(request.getMemberJobNum(), request.getOrgId());
        AssertUtil.isNullOrEmpty(member, OnpremiseErrorEnum.MEMBER_NOT_FIND);
        return packageMemberDetailsResponse(request, member);
    }

    @Override // com.moredian.onpremise.api.member.MemberService
    public MemberDetailsResponse getMemberByMemberNumbers(TerminalFindMemberRequest request) throws BeansException {
        AssertUtil.isNullOrEmpty(request.getMemberJobNum(), OnpremiseErrorEnum.MEMBER_NUMBERS_MUST_NOT_NULL);
        AssertUtil.isTrue(Boolean.valueOf(request.getOrgId() != null && request.getOrgId().longValue() > 0), OnpremiseErrorEnum.ORG_ID_MUST_NOT_NULL);
        Member member = this.memberMapper.getMemberInfoByJobNum(request.getMemberJobNum(), request.getOrgId());
        AssertUtil.isNullOrEmpty(member, OnpremiseErrorEnum.MEMBER_NOT_FIND);
        MemberDetailsResponse response = new MemberDetailsResponse();
        BeanUtils.copyProperties(member, response);
        response.setDeptName(this.deptService.packagingDeptName(member.getDeptId(), false));
        List<MemberDetailsDeptResponse> deptList = null;
        if (StringUtils.isNotEmpty(member.getDeptId())) {
            deptList = new ArrayList<>();
            String[] deptIds = member.getDeptId().split(",");
            for (String deptId : deptIds) {
                Dept dept = this.deptMapper.getDeptById(Long.valueOf(deptId));
                AssertUtil.isNullOrEmpty(dept, OnpremiseErrorEnum.DEPT_NOT_FIND);
                MemberDetailsDeptResponse tempDept = new MemberDetailsDeptResponse();
                tempDept.setDeptId(dept.getDeptId());
                tempDept.setDeptName(dept.getDeptName());
                deptList.add(tempDept);
            }
        }
        response.setDeptList(deptList);
        return response;
    }

    /* JADX WARN: Finally extract failed */
    @Override // com.moredian.onpremise.api.member.MemberService
    public TerminalSyncResponse<TerminalSyncMemberResponse> syncMember(TerminalSyncRequest request) {
        TerminalSyncResponse<TerminalSyncMemberResponse> terminalSyncResponse = new TerminalSyncResponse<>();
        logger.info("=====> request syncMemberCount:{}", Integer.valueOf(syncMemberCount));
        if (syncMemberCount >= 8) {
            throw new BizException(OnpremiseErrorEnum.TERMINAL_REQUEST_BUSY.getErrorCode(), OnpremiseErrorEnum.TERMINAL_REQUEST_BUSY.getMessage());
        }
        syncMemberCount++;
        try {
            try {
                AssertUtil.isTrue(Boolean.valueOf(request.getLastSyncTime() != null), OnpremiseErrorEnum.LAST_SYNC_TIME_MUST_NOT_NULL);
                List<TerminalSyncMemberResponse> syncMemberList = this.memberMapper.listSyncMember(request.getOrgId(), MyDateUtils.getDateString(request.getLastSyncTime().longValue(), "yyyy-MM-dd HH:mm:ss.SSS"), request.getDeviceSn());
                if (CollectionUtils.isEmpty(syncMemberList)) {
                    terminalSyncResponse.setSyncTime(CacheAdapter.getLastModifyTime(CacheKeyGenerateUtils.getMemberLastModifyTimeKey(request.getDeviceSn())).getLastModifyTime());
                    syncMemberCount--;
                    return terminalSyncResponse;
                }
                List<TerminalSyncMemberResponse> syncInsertMemberList = new ArrayList<>();
                List<TerminalSyncMemberResponse> syncModifyMemberList = new ArrayList<>();
                List<TerminalSyncMemberResponse> syncDeleteMemberList = new ArrayList<>();
                Set<Long> memberIdSet = new HashSet<>();
                Set<Long> deptIdSet = new HashSet<>();
                for (TerminalSyncMemberResponse member : syncMemberList) {
                    memberIdSet.add(member.getMemberId());
                    String deptIdStr = member.getDeptId();
                    String[] deptIdStrArr = deptIdStr.split(",");
                    for (String item : deptIdStrArr) {
                        if (!StringUtils.isEmpty(item)) {
                            Long deptId = Long.valueOf(Long.parseLong(item));
                            deptIdSet.add(deptId);
                        }
                    }
                }
                List<GroupMemberDto> groupMemberDtoList = this.groupMemberMapper.listByMemberIdsAndDeptIds(new ArrayList(memberIdSet), new ArrayList(deptIdSet), request.getOrgId());
                List<CheckInTaskMember> checkInTaskMemberList = this.checkInTaskMemberMapper.listByMemberIdsAndDeptIds(new ArrayList(memberIdSet), new ArrayList(deptIdSet), request.getOrgId());
                List<AttendanceGroupMember> attendanceGroupMemberList = this.attendanceGroupMemberMapper.listByMemberIdsAndDeptIds(new ArrayList(memberIdSet), new ArrayList(deptIdSet), request.getOrgId());
                List<MealCanteenMember> mealCanteenMemberList = this.mealCanteenMemberMapper.listByMemberIdsAndDeptIds(new ArrayList(memberIdSet), new ArrayList(deptIdSet), request.getOrgId());
                for (TerminalSyncMemberResponse member2 : syncMemberList) {
                    if (!request.getLastSyncTime().equals(0L) || member2.getDeleteOrNot().intValue() != 1) {
                        if (member2.getDeleteOrNot().intValue() == 1) {
                            syncDeleteMemberList.add(member2);
                        } else {
                            String deptIdStr2 = member2.getDeptId();
                            List<String> deptList = Arrays.asList(deptIdStr2.split(","));
                            Set groupIdSet = new HashSet();
                            for (GroupMemberDto groupMemberDto : groupMemberDtoList) {
                                if (member2.getMemberId().equals(groupMemberDto.getMemberId()) || deptList.contains(String.valueOf(groupMemberDto.getDeptId()))) {
                                    groupIdSet.add(groupMemberDto.getGroupId());
                                }
                            }
                            List<Long> groupIdList = new ArrayList<>(groupIdSet);
                            member2.setPermissionsGroupId(StringUtils.join(groupIdList.toArray(), ","));
                            Set checkInIdSet = new HashSet();
                            for (CheckInTaskMember checkInTaskMember : checkInTaskMemberList) {
                                if (member2.getMemberId().equals(checkInTaskMember.getMemberId()) || deptList.contains(String.valueOf(checkInTaskMember.getDeptId()))) {
                                    checkInIdSet.add(checkInTaskMember.getTaskId());
                                }
                            }
                            List<Long> checkInIdList = new ArrayList<>(checkInIdSet);
                            member2.setCheckInGroupId(StringUtils.join(checkInIdList.toArray(), ","));
                            Set attendanceIdSet = new HashSet();
                            for (AttendanceGroupMember attendanceGroupMember : attendanceGroupMemberList) {
                                if (member2.getMemberId().equals(attendanceGroupMember.getMemberId()) || deptList.contains(String.valueOf(attendanceGroupMember.getDeptId()))) {
                                    attendanceIdSet.add(attendanceGroupMember.getAttendanceGroupId());
                                }
                            }
                            List<Long> attendanceIdList = new ArrayList<>(attendanceIdSet);
                            member2.setAttendanceGroupId(StringUtils.join(attendanceIdList.toArray(), ","));
                            Set mealCanteenIdSet = new HashSet();
                            for (MealCanteenMember mealCanteenMember : mealCanteenMemberList) {
                                if (member2.getMemberId().equals(mealCanteenMember.getMemberId()) || deptList.contains(String.valueOf(mealCanteenMember.getDeptId()))) {
                                    mealCanteenIdSet.add(mealCanteenMember.getMealCanteenId());
                                }
                            }
                            List<Long> mealCanteenIdList = new ArrayList<>(mealCanteenIdSet);
                            member2.setMealCanteenId(StringUtils.join(mealCanteenIdList.toArray(), ","));
                            if (request.getLastSyncTime().longValue() > member2.getGmtCreate().getTime()) {
                                syncModifyMemberList.add(member2);
                            } else {
                                syncInsertMemberList.add(member2);
                            }
                        }
                    }
                }
                Member lastMember = this.memberMapper.getLastModify(request.getOrgId());
                terminalSyncResponse.setSyncTime(Long.valueOf(lastMember.getGmtModify().getTime()));
                doCacheLastModifyTime(CacheKeyGenerateUtils.getMemberLastModifyTimeKey(request.getDeviceSn()), terminalSyncResponse.getSyncTime());
                terminalSyncResponse.setSyncModifyResult(syncModifyMemberList);
                terminalSyncResponse.setSyncInsertResult(syncInsertMemberList);
                terminalSyncResponse.setSyncDeleteResult(syncDeleteMemberList);
                syncMemberCount--;
                return terminalSyncResponse;
            } catch (Exception e) {
                throw e;
            }
        } catch (Throwable th) {
            syncMemberCount--;
            throw th;
        }
    }

    /* JADX WARN: Finally extract failed */
    @Override // com.moredian.onpremise.api.member.MemberService
    public List<TerminalSyncEigenvalueValueResponse> syncMemberEigenvalueValue(TerminalSyncEigenvalueValueRequest request) {
        logger.info("=====> request syncMemberEigenvalueValueCount:{}", Integer.valueOf(syncMemberEigenvalueValueCount));
        if (syncMemberEigenvalueValueCount >= 100) {
            throw new BizException(OnpremiseErrorEnum.TERMINAL_REQUEST_BUSY.getErrorCode(), OnpremiseErrorEnum.TERMINAL_REQUEST_BUSY.getMessage());
        }
        syncMemberEigenvalueValueCount++;
        try {
            try {
                if (CollectionUtils.isEmpty(request.getMemberIds())) {
                    ArrayList arrayList = new ArrayList();
                    syncMemberEigenvalueValueCount--;
                    return arrayList;
                }
                List<TerminalSyncEigenvalueValueResponse> syncMemberEigenvalueValueList = this.memberMapper.listSyncMemberEigenvalueValue(request.getOrgId(), request.getMemberIds());
                syncMemberEigenvalueValueCount--;
                return syncMemberEigenvalueValueList;
            } catch (Exception e) {
                throw e;
            }
        } catch (Throwable th) {
            syncMemberEigenvalueValueCount--;
            throw th;
        }
    }

    @Override // com.moredian.onpremise.api.member.MemberService
    public List<QueryDeptAndMemberResponse> getDeptAndMember(QueryDeptAndMemberRequest request) {
        List<QueryDeptAndMemberResponse> responses = new ArrayList<>();
        if (request.getType() == null || request.getType().intValue() == 2) {
            List<QueryDeptAndMemberResponse> memberList = this.memberMapper.getMemberByNameOrJobNum(request.getOrgId(), request.getSearchKey(), UserLoginResponse.getAccountManageDeptId(request.getSessionId()));
            if (!CollectionUtils.isEmpty(memberList)) {
                responses.addAll(memberList);
            }
        }
        if (request.getType() == null || request.getType().intValue() == 1) {
            List<QueryDeptAndMemberResponse> deptList = this.deptMapper.getDeptByName(request.getOrgId(), request.getSearchKey(), UserLoginResponse.getAccountManageDeptId(request.getSessionId()));
            if (!CollectionUtils.isEmpty(deptList)) {
                responses.addAll(deptList);
            }
        }
        return responses;
    }

    @Override // com.moredian.onpremise.api.member.MemberService
    public List<BatchInsertMemberResponse> batchInsertMember(BatchInsertMemberRequest request) {
        AssertUtil.isNullOrEmpty(request.getBatchList(), OnpremiseErrorEnum.BATCH_INSERT_MEMBER_MUST_NOT_NULL);
        Organization organization = this.orgMapper.getOne();
        AssertUtil.isNullOrEmpty(organization, OnpremiseErrorEnum.ORG_MUST_NOT_NULL);
        List<BatchInsertMemberDetailRequest> list = request.getBatchList();
        int size = list.size();
        AssertUtil.isTrue(Boolean.valueOf(size > 0), OnpremiseErrorEnum.BATCH_INSERT_EXCEL_FORMAT_FAIL);
        List<BatchInsertMemberResponse> responses = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            doBatchInsertMember(list.get(i), responses, request.getOrgId(), i);
        }
        doSendNettyMsg(organization.getOrgId());
        logger.info("===========fail insert size :{} ", Integer.valueOf(responses.size()));
        return responses;
    }

    @Override // com.moredian.onpremise.api.member.MemberService
    public PageList<ExcelMemberResponse> excelMember(ExcelMemberRequest request) {
        List<String> managerDeptIds = UserLoginResponse.getAccountManageDeptId(request.getSessionId());
        Paginator paginator = request.getPaginator();
        if (Paginator.checkPaginator(paginator)) {
            PageHelper.startPage(paginator.getPageNum(), paginator.getPageSize());
            List<ExcelMemberResponse> responses = this.memberMapper.excelMember(request.getOrgId(), managerDeptIds);
            for (ExcelMemberResponse response : responses) {
                response.setDeptName(this.deptService.packagingDeptName(response.getDeptId(), true));
            }
            return new PageList<>(responses);
        }
        List<ExcelMemberResponse> responses2 = this.memberMapper.excelMember(request.getOrgId(), managerDeptIds);
        for (ExcelMemberResponse response2 : responses2) {
            response2.setDeptName(this.deptService.packagingDeptName(response2.getDeptId(), true));
        }
        return new PageList<>(Paginator.initPaginator(responses2), responses2);
    }

    @Override // com.moredian.onpremise.api.member.MemberService
    @Transactional(rollbackFor = {RuntimeException.class})
    public BatchUpdateMemberDeptResponse batchUpdateMemberDept(BatchUpdateMemberDeptRequest request) {
        AssertUtil.isTrue(Boolean.valueOf(request.getOldDeptId() != null && request.getOldDeptId().longValue() > 0), OnpremiseErrorEnum.DEPT_ID_MUST_NOT_NULL);
        AssertUtil.isTrue(Boolean.valueOf(request.getNewDeptIds() != null && request.getNewDeptIds().trim().length() > 0), OnpremiseErrorEnum.DEPT_ID_MUST_NOT_NULL);
        AssertUtil.isTrue(Boolean.valueOf(request.getMemberIds() != null && request.getMemberIds().trim().length() > 0), OnpremiseErrorEnum.MEMBER_ID_MUST_NOT_NULL);
        Dept oldDept = this.deptMapper.getDeptById(request.getOldDeptId());
        AssertUtil.isNullOrEmpty(oldDept, OnpremiseErrorEnum.DEPT_NOT_FIND);
        List<String> newDeptIdList = new ArrayList<>();
        for (String deptId : request.getNewDeptIds().split(",")) {
            newDeptIdList.add(deptId);
        }
        List<Dept> depts = this.deptMapper.listByDeptIds(request.getOrgId(), newDeptIdList);
        AssertUtil.isTrue(Boolean.valueOf(MyListUtils.listIsEmpty(depts)), OnpremiseErrorEnum.DEPT_NOT_FIND);
        BatchUpdateMemberDeptResponse response = new BatchUpdateMemberDeptResponse();
        response.setMemberIds(doBatchUpdateDept(request));
        doSendNettyMsg(request.getOrgId());
        return response;
    }

    @Override // com.moredian.onpremise.api.member.MemberService
    public QueryExtractFeatureResultResponse queryExtractFeatureResult(QueryExtractFeatureResultRequest request) {
        AssertUtil.isNullOrEmpty(request.getJobNum(), OnpremiseErrorEnum.JOB_NUM_MUST_NOT_NULL);
        Map<String, CacheExtractFeatureStatusInfo> infoMap = CacheAdapter.getBatchExtractFeatureStatus(request.getJobNum());
        QueryExtractFeatureResultResponse response = new QueryExtractFeatureResultResponse();
        if (infoMap != null) {
            response.setExtractResut(infoMap);
        }
        return response;
    }

    @Override // com.moredian.onpremise.api.member.MemberService
    public SaveImageResponse saveMemberFaceImage(MultipartFile file) {
        String path = this.uplocadConfig.uploadImage(file);
        AssertUtil.isNullOrEmpty(path, OnpremiseErrorEnum.MEMBER_FACE_UPLOAD_FAIL);
        SaveImageResponse response = new SaveImageResponse();
        response.setFlag(true);
        response.setResult(this.uplocadConfig.getImageUrl(2, path));
        return response;
    }

    @Override // com.moredian.onpremise.api.member.MemberService
    public SaveImageResponse saveMemberFaceImage(ImageUploadBase64Request request) {
        SaveImageResponse response = new SaveImageResponse();
        logger.info("=====> request saveMemberFaceCount:{}", Integer.valueOf(saveMemberFaceCount));
        if (saveMemberFaceCount >= 8) {
            throw new BizException(OnpremiseErrorEnum.SAVE_MEMBER_FACE_REQUEST_BUSY.getErrorCode(), OnpremiseErrorEnum.SAVE_MEMBER_FACE_REQUEST_BUSY.getMessage());
        }
        saveMemberFaceCount++;
        String queryKey = UUID.randomUUID().toString();
        try {
            try {
                try {
                    AssertUtil.isTrue(Boolean.valueOf((StringUtils.isEmpty(request.getImage()) && StringUtils.isEmpty(request.getUrl())) ? false : true), OnpremiseErrorEnum.MEMBER_IMAGE_MUST_NOT_NULL);
                    AssertUtil.isNullOrEmpty(request.getEndFlag(), OnpremiseErrorEnum.MEMBER_UPLOAD_END_FLAG_MUST_NOT_NULL);
                    String url = request.getUrl();
                    if (StringUtils.isEmpty(url)) {
                        String path = this.uplocadConfig.uploadImage(request.getImage());
                        AssertUtil.isNullOrEmpty(path, OnpremiseErrorEnum.MEMBER_FACE_UPLOAD_FAIL);
                        url = this.uplocadConfig.getImageUrl(2, path);
                    }
                    Member member = this.memberMapper.getMemberInfoByJobNum(request.getMemberJobNum(), request.getOrgId());
                    if (request.getNeedExtractFeature() == null || request.getNeedExtractFeature().intValue() != NeedExtractFeatureEnum.EXTRACT_NO.getValue()) {
                        CacheAdapter.cacheBatchExtractFeatureStatus(request.getJobNum(), extractFeature(url, queryKey, request, Long.valueOf(member == null ? 0L : member.getMemberId().longValue())));
                    }
                    response.setFlag(true);
                    response.setResult(url);
                    response.setQueryKey(queryKey);
                    Thread.sleep(1000L);
                    saveMemberFaceCount--;
                } catch (Exception e) {
                    logger.error("error:{}", (Throwable) e);
                    response.setFlag(false);
                    Map<String, String> resultMap = new HashMap<>();
                    resultMap.put("code", OnpremiseErrorEnum.SYSTEM_ERROR.getErrorCode());
                    resultMap.put("msg", e.getMessage());
                    response.setResult(JsonUtils.toJson(resultMap));
                    response.setQueryKey(queryKey);
                    CacheAdapter.cacheBatchExtractFeatureStatus(request.getJobNum(), cacheFailStatus(queryKey, request));
                    saveMemberFaceCount--;
                }
            } catch (BizException e2) {
                logger.error("error:{}", (Throwable) e2);
                response.setFlag(false);
                response.setResult(e2.getErrorCode());
                if (e2.getErrorCode().equals(OnpremiseErrorEnum.REPEAT_FACE_ERROR.getErrorCode())) {
                    Member member2 = this.memberMapper.getMemberInfoByMemberId(Long.valueOf(Long.parseLong(e2.getMessage())), request.getOrgId());
                    response.setMemberJobNum(member2.getMemberJobNum());
                    Map<String, String> resultMap2 = new HashMap<>();
                    resultMap2.put("code", e2.getErrorCode());
                    resultMap2.put("msg", String.format(OnpremiseErrorEnum.REPEAT_FACE_ERROR_DETAIL.getMessage(), member2.getMemberName(), member2.getMemberJobNum()));
                    resultMap2.put("memberJobNum", member2.getMemberJobNum());
                    resultMap2.put("memberName", member2.getMemberName());
                    response.setResult(JsonUtils.toJson(resultMap2));
                }
                response.setQueryKey(queryKey);
                CacheAdapter.cacheBatchExtractFeatureStatus(request.getJobNum(), cacheFailStatus(queryKey, request));
                saveMemberFaceCount--;
            }
            return response;
        } catch (Throwable th) {
            saveMemberFaceCount--;
            throw th;
        }
    }

    @Override // com.moredian.onpremise.api.member.MemberService
    public ExtractNoticeResponse extractNoticeDevice(ExtractNoticeRequest request) {
        AssertUtil.isNullOrEmpty(request.getDeviceSn(), OnpremiseErrorEnum.DEVICE_SN_MUST_NOT_NULL);
        String queryKey = UUID.randomUUID().toString();
        CacheHeartBeatInfo heartBeatInfo = CacheAdapter.getHeartBeatInfo(CacheKeyGenerateUtils.getHeartBeatCacheKey(request.getDeviceSn()));
        AssertUtil.isNullOrEmpty(heartBeatInfo, OnpremiseErrorEnum.NOT_FIND_ONLINE_DEVICE);
        AssertUtil.isNullOrEmpty(IOTSession.getChannels(), OnpremiseErrorEnum.NOT_FIND_ONLINE_DEVICE);
        CacheExtractFeatureStatusInfo info = new CacheExtractFeatureStatusInfo();
        Integer expires = 30;
        if (request.getExpires() != null && request.getExpires().intValue() > 0) {
            expires = request.getExpires();
        }
        info.setExpireTime(Long.valueOf(MyDateUtils.addSeconds(new Date(), expires.intValue()).getTime()));
        info.setStatus(Integer.valueOf(ExtractFeatureStatusEnum.EXTRACTING_STATUS.getValue()));
        info.setOrgId(request.getOrgId());
        Map<String, CacheExtractFeatureStatusInfo> result = new HashMap<>();
        result.put(queryKey, info);
        NoticeDeviceExtractRequest extractRequest = new NoticeDeviceExtractRequest();
        extractRequest.setEndFlag(1);
        extractRequest.setJobNum(request.getJobNum());
        extractRequest.setQueryKey(queryKey);
        extractRequest.setExpires(expires);
        this.nettyMessageApi.sendMsg(extractRequest, Integer.valueOf(NoticeDeviceExtractRequest.MODEL_TYPE.type()), request.getDeviceSn());
        CacheAdapter.cacheBatchExtractFeatureStatus(request.getJobNum(), result);
        ExtractNoticeResponse response = new ExtractNoticeResponse();
        response.setQueryKey(queryKey);
        return response;
    }

    @Override // com.moredian.onpremise.api.member.MemberService
    @Transactional(rollbackFor = {RuntimeException.class})
    public SaveMemberFaceByDeviceResponse saveMemberFaceByDevice(SaveMemberFaceRequest request) throws BeansException {
        Long memberId = null;
        Member member = this.memberMapper.getMemberInfoByJobNum(request.getMemberJobNum(), request.getOrgId());
        if (member != null) {
            memberId = member.getMemberId();
        }
        if (!this.configService.getRepeatFace().equals(1)) {
            memberIds.clear();
            FaceDet.LoadAllFeatures(new byte[0], 0, 512);
        } else {
            loadAllMemberFeature(memberId);
        }
        String feature = doExtractFeature(this.uplocadConfig.getFilePath() + request.getVerifyFaceUrl());
        AssertUtil.isTrue(Boolean.valueOf(feature != null && feature.trim().length() > 0), OnpremiseErrorEnum.IMAGE_EXTRACT_FAIL);
        SaveMemberFaceResponse saveMemberFaceResponse = saveMemberFace(request);
        SaveMemberFaceByDeviceResponse saveMemberFaceByDeviceResponse = new SaveMemberFaceByDeviceResponse();
        BeanUtils.copyProperties(saveMemberFaceResponse, saveMemberFaceByDeviceResponse);
        return saveMemberFaceByDeviceResponse;
    }

    @Override // com.moredian.onpremise.api.member.MemberService
    public boolean reloadMemberFeature() {
        FaceReloadProgressResponse faceReloadProgressResponse = CacheAdapter.getFaceReloadProgress(Constants.FACE_RELOAD_PROGRESS);
        if (faceReloadProgressResponse == null) {
            faceReloadProgressResponse = new FaceReloadProgressResponse(0, 0, 0, 0, null);
        }
        if (faceReloadProgressResponse.getStatus() != null && faceReloadProgressResponse.getStatus().intValue() == 1) {
            throw new BizException(OnpremiseErrorEnum.RELOAD_FACE_PROGRESSING.getErrorCode(), OnpremiseErrorEnum.RELOAD_FACE_PROGRESSING.getMessage());
        }
        List<Member> members = this.memberMapper.listHasFeatureMember(null);
        if (!CollectionUtils.isEmpty(members)) {
            FaceDet.LoadAllFeatures(new byte[0], 0, 512);
            int count = members.size();
            int nowCount = 0;
            int successCount = 0;
            int failCount = 0;
            List<FaceReloadFailResponse> faceReloadFailResponseList = new ArrayList<>();
            faceReloadProgressResponse.setFailList(faceReloadFailResponseList);
            faceReloadProgressResponse.setTotal(Integer.valueOf(count));
            faceReloadProgressResponse.setSuccess(0);
            faceReloadProgressResponse.setFail(0);
            faceReloadProgressResponse.setStatus(1);
            CacheAdapter.cacheFaceReloadProgress(Constants.FACE_RELOAD_PROGRESS, faceReloadProgressResponse);
            for (Member member : members) {
                FaceReloadProgressResponse faceReloadProgressResponseTemp = CacheAdapter.getFaceReloadProgress(Constants.FACE_RELOAD_PROGRESS);
                nowCount++;
                try {
                    SaveMemberFaceRequest saveMemberFaceRequest = new SaveMemberFaceRequest();
                    saveMemberFaceRequest.setOrgId(member.getOrgId());
                    saveMemberFaceRequest.setMemberJobNum(member.getMemberJobNum());
                    saveMemberFaceRequest.setNeedSendMsg(count == nowCount);
                    String feature = doExtractFeature(this.uplocadConfig.getFilePath() + member.getVerifyFaceUrl());
                    if (feature != null && feature.trim().length() > 0) {
                        saveMemberFaceRequest.setEigenvalueValue(feature);
                    }
                    saveMemberFaceRequest.setVerifyFaceUrl(member.getVerifyFaceUrl());
                    saveMemberFace(saveMemberFaceRequest);
                    successCount++;
                    faceReloadProgressResponseTemp.setSuccess(Integer.valueOf(successCount));
                    CacheAdapter.cacheFaceReloadProgress(Constants.FACE_RELOAD_PROGRESS, faceReloadProgressResponseTemp);
                } catch (Exception e) {
                    failCount++;
                    faceReloadProgressResponseTemp.setFail(Integer.valueOf(failCount));
                    FaceReloadFailResponse faceReloadFailResponse = new FaceReloadFailResponse();
                    faceReloadFailResponse.setMemberName(member.getMemberName());
                    faceReloadFailResponse.setMemberJobNum(member.getMemberJobNum());
                    faceReloadFailResponse.setError(e.getMessage());
                    faceReloadFailResponseList.add(faceReloadFailResponse);
                    faceReloadProgressResponseTemp.setFailList(faceReloadFailResponseList);
                    CacheAdapter.cacheFaceReloadProgress(Constants.FACE_RELOAD_PROGRESS, faceReloadProgressResponseTemp);
                }
            }
            FaceReloadProgressResponse faceReloadProgressResponseEnd = CacheAdapter.getFaceReloadProgress(Constants.FACE_RELOAD_PROGRESS);
            faceReloadProgressResponseEnd.setStatus(0);
            CacheAdapter.cacheFaceReloadProgress(Constants.FACE_RELOAD_PROGRESS, faceReloadProgressResponseEnd);
            return true;
        }
        return true;
    }

    @Override // com.moredian.onpremise.api.member.MemberService
    public boolean loadAllMemberFeature(Long notMemberId) {
        memberIds.clear();
        List<Member> members = this.memberMapper.listHasFeatureMember(notMemberId);
        if (CollectionUtils.isEmpty(members)) {
            FaceDet.LoadAllFeatures(new byte[0], 0, 512);
            return true;
        }
        int size = members.size();
        byte[] allFeatures = new byte[2048 * size];
        for (int i = 0; i < size; i++) {
            byte[] var = MyBase64Utils.decodeStringForByte(members.get(i).getEigenvalueValue());
            for (int j = 0; j < var.length; j++) {
                allFeatures[(i * 2048) + j] = var[j];
            }
            memberIds.add(members.get(i).getMemberId());
        }
        logger.info("faceDet allFeatures is null:{}, allFeatures size:{}, loadAllFeature:{}", Integer.valueOf(allFeatures.length), Integer.valueOf(size), Integer.valueOf(FaceDet.LoadAllFeatures(allFeatures, size, 512)));
        return true;
    }

    @Override // com.moredian.onpremise.api.member.MemberService
    public boolean matchFeature(MatchFeatureRequest request) {
        return true;
    }

    @Override // com.moredian.onpremise.api.member.MemberService
    public Set<Long> getMemberListByDept(Long orgId, Long deptId, Set<Long> memberIds2) {
        memberIds2.addAll(this.memberMapper.listMemberIdByDeptId(deptId, orgId));
        List<Dept> childDepts = this.deptMapper.listChildDeptToReturnDept(deptId, orgId);
        if (MyListUtils.listIsEmpty(childDepts)) {
            for (Dept childDept : childDepts) {
                memberIds2.addAll(this.memberMapper.listMemberIdByDeptId(childDept.getDeptId(), orgId));
                getMemberListByDept(orgId, childDept.getDeptId(), memberIds2);
            }
        }
        return memberIds2;
    }

    private boolean checkSaveMemberRequest(SaveMemberRequest request) {
        AssertUtil.isNullOrEmpty(request.getDeptId(), OnpremiseErrorEnum.DEPT_ID_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getMemberName(), OnpremiseErrorEnum.MEMBER_NAME_MUST_NOT_NULL);
        AssertUtil.isTrue(Boolean.valueOf(StringUtils.isNotEmpty(request.getMemberJobNum()) || StringUtils.isNotEmpty(request.getMemberMobile())), OnpremiseErrorEnum.MEMBER_MOBILE_AND_NUMBERS_MUST_NOT_ALL_NULL);
        String[] deptIds = request.getDeptId().split(",");
        logger.debug("deptIds.length:{} deptIds[0]:{}", Integer.valueOf(deptIds.length), deptIds[0]);
        if (deptIds.length == 1 && Long.valueOf(deptIds[0]).equals(0L)) {
            Dept dept = this.deptMapper.getTopDept(request.getOrgId());
            AssertUtil.isNullOrEmpty(dept, OnpremiseErrorEnum.TOP_DEPT_NOT_FIND);
            request.setDeptId(String.valueOf(dept.getDeptId()));
        } else {
            for (String deptId : deptIds) {
                AssertUtil.isNullOrEmpty(this.deptMapper.getDeptById(Long.valueOf(deptId)), OnpremiseErrorEnum.DEPT_NOT_FIND);
            }
        }
        if (StringUtils.isNotEmpty(request.getMemberJobNum())) {
            Member member = this.memberMapper.getMemberInfoByJobNum(request.getMemberJobNum(), request.getOrgId());
            if (request.getMemberId() == null || request.getMemberId().longValue() <= 0) {
                AssertUtil.isTrue(Boolean.valueOf(member == null), OnpremiseErrorEnum.MEMBER_NUMBERS_ALEADY_EXIST);
            } else {
                AssertUtil.isTrue(Boolean.valueOf(member == null || request.getMemberId().equals(member.getMemberId())), OnpremiseErrorEnum.MEMBER_NUMBERS_ALEADY_EXIST);
            }
        }
        if (StringUtils.isNotEmpty(request.getIdentityCard())) {
            Member member2 = this.memberMapper.getMemberInfoByIdentityCard(request.getIdentityCard(), request.getOrgId());
            if (request.getMemberId() == null || request.getMemberId().longValue() <= 0) {
                AssertUtil.isTrue(Boolean.valueOf(member2 == null), OnpremiseErrorEnum.MEMBER_IDENTITY_CARD_ALREADY_EXIST);
                return true;
            }
            AssertUtil.isTrue(Boolean.valueOf(member2 == null || request.getMemberId().equals(member2.getMemberId())), OnpremiseErrorEnum.MEMBER_IDENTITY_CARD_ALREADY_EXIST);
            return true;
        }
        return true;
    }

    private boolean insertUpdateEigenvalueRecord(Member member, SaveMemberFaceRequest request) {
        UpdateEigenvalueRecord record = new UpdateEigenvalueRecord();
        record.setMemberId(member.getMemberId());
        record.setNewEigenvalueValue(request.getEigenvalueValue());
        record.setNewVerifyFaceUrl(request.getVerifyFaceUrl());
        record.setOrgId(member.getOrgId());
        record.setOldEigenvalueValue(member.getEigenvalueValue());
        record.setOldVerifyFaceUrl(member.getVerifyFaceUrl());
        if (request.getChannel() == null) {
            record.setChannel(Integer.valueOf(InputFaceChannelEnum.WEB.getValue()));
        }
        AssertUtil.isTrue(Boolean.valueOf(this.eigenvalueRecordMapper.insertRecord(record) > 0), OnpremiseErrorEnum.SAVE_UPDATE_EIGENVALUE_RECORD_FAIL);
        return true;
    }

    private String parseDeptName(Long orgId, String[] deptNameLists) {
        String result;
        Dept topDept = this.deptMapper.getTopDept(orgId);
        AssertUtil.isNullOrEmpty(topDept, OnpremiseErrorEnum.TOP_DEPT_NOT_FIND);
        if (!MyListUtils.arrayIsEmpty(deptNameLists)) {
            result = String.valueOf(topDept.getDeptId());
        } else {
            StringBuffer deptIdSb = new StringBuffer();
            for (String deptNameStr : deptNameLists) {
                String[] deptNames = deptNameStr.split("-");
                int deptLength = deptNames.length;
                Long superDeptId = topDept.getDeptId();
                String superDeptName = topDept.getDeptName();
                String deptName = superDeptName;
                for (int j = 0; j < deptLength; j++) {
                    deptName = deptName + "-" + deptNames[j];
                    Dept dept = this.deptMapper.getDeptByDeptName(orgId, deptNames[j], superDeptId, Integer.valueOf(j + 1));
                    if (dept == null) {
                        dept = new Dept();
                        dept.setOrgId(orgId);
                        dept.setDeptName(deptNames[j]);
                        dept.setDeptGrade(Integer.valueOf(j + 1));
                        dept.setSuperDeptId(superDeptId);
                        dept.setSuperDeptName(superDeptName);
                        AssertUtil.isTrue(Boolean.valueOf(this.deptMapper.insertDept(dept) > 0), OnpremiseErrorEnum.INSERT_DEPT_FAIL);
                        this.deptService.insertSuperDeptGroup(dept);
                        this.deptService.insertSuperDeptAttendanceGroup(dept);
                        this.deptService.insertSuperDeptCheckIn(dept);
                        this.deptService.insertSuperDeptMealCanteen(dept);
                    }
                    if (j + 1 == deptLength) {
                        deptIdSb.append(dept.getDeptId());
                        deptIdSb.append(",");
                    }
                    superDeptId = dept.getDeptId();
                    superDeptName = deptNames[j];
                }
            }
            result = deptIdSb.substring(0, deptIdSb.length() - 1);
        }
        return result;
    }

    private Member detailRequestToMember(BatchInsertMemberDetailRequest request) throws BeansException {
        Member member = new Member();
        BeanUtils.copyProperties(request, member);
        if (request.getMemberGender() == null || request.getMemberGender().intValue() == 0) {
            request.setMemberGender(Integer.valueOf(MemberGenderEnum.UN_KNOW.getValue()));
        }
        return member;
    }

    private Member saveMemberRequestToMember(SaveMemberRequest request) throws BeansException {
        Member member = new Member();
        BeanUtils.copyProperties(request, member);
        if (member.getMemberMobile() == null) {
            member.setMemberMobile("");
        }
        if (!StringUtils.isEmpty(member.getMemberMobile()) && StringUtils.isEmpty(request.getMemberMobilePre())) {
            member.setMemberMobilePre(Constants.DEFAULT_MOBILE_PRE);
        }
        if (request.getVerifyFaceUrl() != null && request.getVerifyFaceUrl().trim().length() > 0) {
            member.setVerifyFaceMd5(getMd5(request.getVerifyFaceUrl()));
        }
        if (request.getMemberGender() == null || request.getMemberGender().intValue() == 0) {
            member.setMemberGender(Integer.valueOf(MemberGenderEnum.UN_KNOW.getValue()));
        }
        return member;
    }

    private Map<String, CacheExtractFeatureStatusInfo> extractFeature(ImageUploadBase64Request request, String url, String queryKey) {
        String deviceSn = packageExtractDeviceSn(request);
        AssertUtil.isNullOrEmpty(deviceSn, OnpremiseErrorEnum.DEVICE_HAS_NOT_FREE);
        CacheExtractFeatureStatusInfo info = new CacheExtractFeatureStatusInfo();
        if (MyListUtils.listIsEmpty(request.getDeviceSns())) {
            info.setExpireTime(Long.valueOf(MyDateUtils.addSeconds(new Date(), 5 + ((2 * request.getCount().intValue()) / request.getDeviceSns().size())).getTime()));
        } else {
            info.setExpireTime(Long.valueOf(MyDateUtils.addSeconds(new Date(), 5 + (2 * request.getCount().intValue())).getTime()));
        }
        info.setStatus(Integer.valueOf(ExtractFeatureStatusEnum.EXTRACTING_STATUS.getValue()));
        info.setMemberJobNum(request.getMemberJobNum());
        info.setOrgId(request.getOrgId());
        info.setVerifyFaceUrl(url);
        info.setCount(request.getCount());
        Map<String, CacheExtractFeatureStatusInfo> result = new HashMap<>();
        result.put(queryKey, info);
        ExtractFeatureRequest featureRequest = new ExtractFeatureRequest();
        featureRequest.setEndFlag(request.getEndFlag());
        featureRequest.setMemberJobNum(request.getMemberJobNum());
        featureRequest.setPictureUrl(url);
        featureRequest.setJobNum(request.getJobNum());
        featureRequest.setQueryKey(queryKey);
        this.nettyMessageApi.sendMsg(featureRequest, Integer.valueOf(ExtractFeatureRequest.MODEL_TYPE.type()), deviceSn);
        return result;
    }

    private String packageExtractDeviceSn(ImageUploadBase64Request request) {
        String deviceSn = "";
        if (MyListUtils.listIsEmpty(request.getDeviceSns())) {
            if (request.getCount().intValue() < 30) {
                for (String device : request.getDeviceSns()) {
                    if (CacheAdapter.getHeartBeatInfo(CacheKeyGenerateUtils.getHeartBeatCacheKey(device)) != null) {
                        deviceSn = device;
                    }
                }
            } else {
                int num = request.getCurrentNum().intValue() / request.getDeviceSns().size();
                int remainder = request.getCurrentNum().intValue() - (request.getDeviceSns().size() * num);
                CacheHeartBeatInfo heartBeatInfo = CacheAdapter.getHeartBeatInfo(CacheKeyGenerateUtils.getHeartBeatCacheKey(request.getDeviceSns().get(remainder)));
                if (heartBeatInfo != null) {
                    deviceSn = heartBeatInfo.getDeviceSn();
                }
                if (num == request.getCount().intValue() / request.getDeviceSns().size()) {
                    request.setEndFlag(1);
                }
            }
        } else {
            Map<String, CacheHeartBeatInfo> maps = CacheAdapter.getHeartBeatInfoAll();
            AssertUtil.isTrue(Boolean.valueOf(maps.size() > 0), OnpremiseErrorEnum.NOT_FIND_ONLINE_DEVICE);
            AssertUtil.isNullOrEmpty(IOTSession.getChannels(), OnpremiseErrorEnum.NOT_FIND_ONLINE_DEVICE);
            Iterator<String> it = maps.keySet().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                String key = it.next();
                if (maps.get(key) != null) {
                    deviceSn = CacheKeyGenerateUtils.restoreHeartBeatCacheKey(key);
                    break;
                }
            }
        }
        return deviceSn;
    }

    private void saveFaceInfo(SaveMemberRequest request) throws BeansException {
        if (request.getVerifyFaceUrl() != null && request.getVerifyFaceUrl().trim().length() > 0) {
            AssertUtil.isNullOrEmpty(request.getJobNum(), OnpremiseErrorEnum.MEMBER_FACE_JOBNUM_MUST_NOT_NULL);
            Map<String, CacheExtractFeatureStatusInfo> infoMap = CacheAdapter.getBatchExtractFeatureStatus(request.getJobNum());
            if (infoMap != null && infoMap.keySet().size() == 1) {
                SaveMemberFaceRequest saveMemberFaceRequest = new SaveMemberFaceRequest();
                saveMemberFaceRequest.setOrgId(request.getOrgId());
                saveMemberFaceRequest.setMemberJobNum(request.getMemberJobNum());
                saveMemberFaceRequest.setNeedSendMsg(false);
                for (String queryKey : infoMap.keySet()) {
                    CacheExtractFeatureStatusInfo info = infoMap.get(queryKey);
                    if (info != null) {
                        saveMemberFaceRequest.setVerifyFaceUrl(request.getVerifyFaceUrl());
                        saveMemberFaceRequest.setEigenvalueValue(info.getEigenvalue());
                    }
                }
                saveMemberFace(saveMemberFaceRequest);
            }
            Account account = this.accountMapper.getAccountByMemberId(request.getMemberId(), request.getOrgId());
            if (account != null) {
                if (AccountGradeEnum.SUPER_ACCOUNT.getValue() == account.getAccountGrade().intValue() || AccountGradeEnum.MAIN_ACCOUNT.getValue() == account.getAccountGrade().intValue()) {
                    doSendSyncAccountNettyMsg(request.getOrgId());
                } else {
                    AccountAuth accountAuth = this.accountAuthMapper.getOneByAccountId(account.getAccountId(), request.getOrgId());
                    doSendSyncAccountNettyMsgByDeviceSn(request.getOrgId(), accountAuth.getManageDeviceSn());
                }
            }
        }
    }

    private void doSendSyncAccountNettyMsg(Long orgId) {
        Account lastAccount = this.accountMapper.getLastModify(orgId);
        List<String> deviceSnList = this.deviceMapper.listDeviceSn(orgId);
        if (CollectionUtils.isEmpty(deviceSnList)) {
            return;
        }
        this.nettyMessageApi.sendMsg(new SyncAccountRequest(Long.valueOf(lastAccount == null ? 0L : lastAccount.getGmtModify().getTime()), orgId), Integer.valueOf(SyncAccountRequest.MODEL_TYPE.type()), deviceSnList);
    }

    private void doSendSyncAccountNettyMsgByDeviceSn(Long orgId, String deviceSns) {
        if (deviceSns != null) {
            String[] deviceSnArray = deviceSns.split(",");
            if (MyListUtils.arrayIsEmpty(deviceSnArray)) {
                Account account = this.accountMapper.getLastModify(orgId);
                for (String deviceSn : deviceSnArray) {
                    this.nettyMessageApi.sendMsg(new SyncAccountRequest(Long.valueOf(account == null ? 0L : account.getGmtModify().getTime()), orgId), Integer.valueOf(SyncAccountRequest.MODEL_TYPE.type()), deviceSn);
                }
            }
        }
    }

    private List<Long> doBatchUpdateDept(BatchUpdateMemberDeptRequest request) {
        List<Long> fail = new ArrayList<>();
        for (String memberId : request.getMemberIds().split(",")) {
            Member member = this.memberMapper.getMemberInfoByMemberId(Long.valueOf(memberId), request.getOrgId());
            if (member != null && doUpdateMemberDept(request.getOldDeptId(), request.getNewDeptIds(), member) == 0) {
                fail.add(Long.valueOf(memberId));
            }
        }
        return fail;
    }

    private void doSendNettyMsg(Long orgId) {
        Member member = this.memberMapper.getLastModify(orgId);
        List<String> deviceSnList = this.deviceMapper.listDeviceSn(orgId);
        if (CollectionUtils.isEmpty(deviceSnList)) {
            return;
        }
        this.nettyMessageApi.sendMsg(new SyncMemberRequest(Long.valueOf(member == null ? 0L : member.getGmtModify().getTime()), orgId), Integer.valueOf(SyncMemberRequest.MODEL_TYPE.type()), deviceSnList);
    }

    private void doSendGroupNettyMsg(Long orgId) {
    }

    private void doDeleteMember(Long memberId, Long orgId, String deptId) {
        AssertUtil.isTrue(Boolean.valueOf(this.memberMapper.deleteMember(Long.valueOf(memberId.longValue()), orgId) > 0), OnpremiseErrorEnum.DELETE_MEMBER_FAIL);
        Account account = this.accountMapper.getAccountByMemberId(memberId, orgId);
        if (account != null) {
            this.accountMapper.unbindMember(memberId, orgId);
            if (AccountGradeEnum.SUPER_ACCOUNT.getValue() == account.getAccountGrade().intValue() || AccountGradeEnum.MAIN_ACCOUNT.getValue() == account.getAccountGrade().intValue()) {
                doSendSyncAccountNettyMsg(orgId);
            } else {
                AccountAuth accountAuth = this.accountAuthMapper.getOneByAccountId(account.getAccountId(), orgId);
                doSendSyncAccountNettyMsgByDeviceSn(orgId, accountAuth.getManageDeviceSn());
            }
        }
        this.groupMemberMapper.deleteGroupMember(null, memberId, orgId);
        this.attendanceGroupMemberMapper.deleteGroupMember(memberId, orgId, null);
        this.checkInTaskMemberMapper.softDeleteByMemberId(new Date(), orgId, memberId);
        this.mealCanteenMemberMapper.deleteCanteenByMember(memberId, orgId, null);
        this.mealCardMemberMapper.deleteCardByMember(memberId, orgId, null);
        this.mealMemberMapper.delete(memberId);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v11, types: [java.util.List] */
    /* JADX WARN: Type inference failed for: r0v17, types: [java.util.List] */
    private List<Long> compareDeptDifference(String oldDeptId, String nowDeptId) {
        ArrayList arrayList = new ArrayList();
        if (oldDeptId != null && oldDeptId.trim().length() > 0) {
            arrayList = Arrays.asList(oldDeptId.split(","));
        }
        ArrayList arrayList2 = new ArrayList();
        if (oldDeptId != null && oldDeptId.trim().length() > 0) {
            arrayList2 = Arrays.asList(nowDeptId.split(","));
        }
        return stringToLongList(new MyListUtils().difference(arrayList, arrayList2));
    }

    private List<Long> stringToLongList(List<String> list) {
        List<Long> result = new ArrayList<>();
        for (String var : list) {
            if (var != null && var.trim().length() > 0) {
                result.add(Long.valueOf(var));
            }
        }
        return result;
    }

    private String getMd5(String url) {
        String result = "";
        try {
            result = DigestUtils.md5Hex(new FileInputStream(this.uplocadConfig.getFilePath() + url));
        } catch (IOException e) {
            logger.error("error:{}", (Throwable) e);
        }
        return result;
    }

    private Map<Long, Account> accountListToMap(List<Account> accounts) {
        Map<Long, Account> temp = new ConcurrentHashMap<>(16);
        if (MyListUtils.listIsEmpty(accounts)) {
            for (Account account : accounts) {
                if (account != null && account.getMemberId() != null) {
                    temp.put(account.getMemberId(), account);
                }
            }
        }
        return temp;
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

    private int doUpdateMemberDept(Long oldDeptId, String newDeptIdStr, Member member) {
        String newDeptId;
        List<Long> newDeptIds = new ArrayList<>();
        for (String deptId : newDeptIdStr.split(",")) {
            if (!member.getDeptId().contains(deptId) || deptId.equals(oldDeptId.toString())) {
                newDeptIds.add(Long.valueOf(deptId));
            }
        }
        if (newDeptIds.size() > 0) {
            String newDeptId2 = JsonUtils.toJson(newDeptIds);
            newDeptId = newDeptId2.substring(1, newDeptId2.length() - 1);
            logger.info("=============new dept id :{}", newDeptId);
        } else {
            newDeptId = "";
        }
        int result = this.memberMapper.updateDept(member.getMemberId(), oldDeptId, newDeptId, member.getOrgId());
        Member member2 = this.memberMapper.getMemberInfoByMemberId(member.getMemberId(), member.getOrgId());
        if (member2.getDeptId().startsWith(",")) {
            int idx = member2.getDeptId().indexOf(",");
            member2.setDeptId(member2.getDeptId().substring(idx + 1));
            result = this.memberMapper.saveMember(member2);
        }
        if (member2.getDeptId().endsWith(",")) {
            int idx2 = member2.getDeptId().lastIndexOf(",");
            member2.setDeptId(member2.getDeptId().substring(0, idx2));
            result = this.memberMapper.saveMember(member2);
        }
        return result;
    }

    @Transactional(rollbackFor = {RuntimeException.class})
    private Set<Long> doBatchInsertMember(BatchInsertMemberDetailRequest detailRequest, List<BatchInsertMemberResponse> responses, Long orgId, int row) {
        BatchInsertMemberResponse response = new BatchInsertMemberResponse();
        Set<Long> groupIds = new HashSet<>();
        try {
        } catch (Exception e) {
            logger.error("error:{}", (Throwable) e);
            responses.add(response.defaultFail(Integer.valueOf(row + 1)));
        }
        if (response.checkMemberJobNum(detailRequest.getMemberJobNum(), Integer.valueOf(row + 1), responses)) {
            return groupIds;
        }
        if (response.checkMemberExists(this.memberMapper.getMemberInfoByJobNum(detailRequest.getMemberJobNum(), orgId), Integer.valueOf(row + 1), responses, 1)) {
            return groupIds;
        }
        if (!StringUtils.isEmpty(detailRequest.getIdentityCard())) {
            Member idMember = this.memberMapper.getMemberInfoByIdentityCard(detailRequest.getMemberJobNum(), orgId);
            if (response.checkMemberExists(idMember, Integer.valueOf(row + 1), responses, 2)) {
                return groupIds;
            }
        }
        Member member = detailRequestToMember(detailRequest);
        String[] deptNames = detailRequest.getDeptName() == null ? new String[0] : detailRequest.getDeptName().split(",");
        member.setDeptId(parseDeptName(orgId, deptNames));
        member.setOrgId(orgId);
        AssertUtil.isTrue(Boolean.valueOf(this.memberMapper.addMember(member) > 0), OnpremiseErrorEnum.SAVE_MEMBER_FAIL);
        return groupIds;
    }

    private synchronized Map<String, CacheExtractFeatureStatusInfo> extractFeature(String path, String queryKey, ImageUploadBase64Request request, Long memberId) throws BeansException {
        CacheExtractFeatureStatusInfo info = new CacheExtractFeatureStatusInfo();
        logger.info("to do extract feature ");
        if (!this.configService.getRepeatFace().equals(1)) {
            memberIds.clear();
            FaceDet.LoadAllFeatures(new byte[0], 0, 512);
        } else {
            loadAllMemberFeature(memberId);
        }
        String feature = doExtractFeature(this.uplocadConfig.getFilePath() + path);
        AssertUtil.isTrue(Boolean.valueOf(feature != null && feature.trim().length() > 0), OnpremiseErrorEnum.IMAGE_EXTRACT_FAIL);
        info.setStatus(Integer.valueOf(ExtractFeatureStatusEnum.SUCCESS_STATUS.getValue()));
        if (request.getNeedAutoSave() != null && request.getNeedAutoSave().intValue() == 1) {
            logger.info("=====> autoSaveFace");
            SaveMemberFaceRequest saveMemberFaceRequest = new SaveMemberFaceRequest();
            saveMemberFaceRequest.setOrgId(request.getOrgId());
            saveMemberFaceRequest.setMemberJobNum(request.getMemberJobNum());
            saveMemberFaceRequest.setNeedSendMsg(request.getEndFlag().equals(1));
            saveMemberFaceRequest.setEigenvalueValue(feature);
            saveMemberFaceRequest.setVerifyFaceUrl(path);
            saveMemberFace(saveMemberFaceRequest);
        }
        info.setExpireTime(Long.valueOf(MyDateUtils.addSeconds(new Date(), 5 + (2 * request.getCount().intValue())).getTime()));
        info.setVerifyFaceUrl(path);
        info.setEigenvalue(feature);
        Map<String, CacheExtractFeatureStatusInfo> result = new HashMap<>();
        result.put(queryKey, info);
        return result;
    }

    private Map<String, CacheExtractFeatureStatusInfo> cacheFailStatus(String queryKey, ImageUploadBase64Request request) {
        CacheExtractFeatureStatusInfo info = new CacheExtractFeatureStatusInfo();
        info.setStatus(Integer.valueOf(ExtractFeatureStatusEnum.FAIL_STATUS.getValue()));
        info.setExpireTime(Long.valueOf(MyDateUtils.addSeconds(new Date(), 5 + (2 * request.getCount().intValue())).getTime()));
        Map<String, CacheExtractFeatureStatusInfo> result = new HashMap<>();
        result.put(queryKey, info);
        return result;
    }

    private String doExtractFeature(String path) {
        String featrue;
        try {
            BufferedImage image = ImageIO.read(new File(path));
            byte[] buffer = MyFileUtils.getMatrixBGR(image);
            logger.info("=====> buffer size :{}", Integer.valueOf(buffer.length));
            synchronized (this) {
                byte[] bytes = FaceDet.ExtractFeatureByImage(buffer, image.getWidth(), image.getHeight());
                logger.error("=====> 1272 doExtractFeature bytes:{}", Integer.valueOf(bytes.length));
                if (bytes != null && bytes.length == 4) {
                    long code = IntByteUtils.byteArrayToLong(bytes);
                    logger.error("=====> 1275 doExtractFeature errorCode:{}", Long.valueOf(code));
                    if (code == -1) {
                        throw new BizException(OnpremiseErrorEnum.SYSTEM_MEMORY_ERROR.getErrorCode(), OnpremiseErrorEnum.SYSTEM_MEMORY_ERROR.getMessage());
                    }
                    if (code == -2) {
                        throw new BizException(OnpremiseErrorEnum.NO_FACE_ERROR.getErrorCode(), OnpremiseErrorEnum.NO_FACE_ERROR.getMessage());
                    }
                    if (code == -3) {
                        throw new BizException(OnpremiseErrorEnum.INVALID_FACE_ERROR.getErrorCode(), OnpremiseErrorEnum.INVALID_FACE_ERROR.getMessage());
                    }
                    if (code == -4) {
                        throw new BizException(OnpremiseErrorEnum.ANGLE_FACE_ERROR.getErrorCode(), OnpremiseErrorEnum.ANGLE_FACE_ERROR.getMessage());
                    }
                    Long memberId = memberIds.get(new Long(code).intValue());
                    throw new BizException(OnpremiseErrorEnum.REPEAT_FACE_ERROR.getErrorCode(), String.format(OnpremiseErrorEnum.REPEAT_FACE_ERROR.getMessage(), memberId));
                }
                featrue = MyBase64Utils.encodeStringForString(bytes);
            }
            return featrue;
        } catch (BizException e) {
            logger.error("=====> 1297 extractFeature file:{},fail:{}", path, e.getMessage());
            MyFileUtils.deleteFile(path);
            throw new BizException(e.getErrorCode(), e.getMessage());
        } catch (Exception e2) {
            logger.error("=====> 1302 extractFeature file:{},fail:{}", path, e2.getMessage());
            MyFileUtils.deleteFile(path);
            throw new BizException(OnpremiseErrorEnum.SYSTEM_ERROR.getErrorCode(), e2.getMessage());
        }
    }

    public Long doMatchFeature(byte[] feature, Long orgId, Long oldMemberId) {
        Long memberId = 0L;
        try {
            logger.info("memberIds :{}", Integer.valueOf(memberIds.size()));
            float[] floats = null;
            logger.info("floats length :{}", Integer.valueOf(floats.length));
            if (0 != 0 && floats.length > 0) {
                float max = 0.0f;
                int index = 0;
                for (int i = 0; i < floats.length; i++) {
                    if (floats[i] > max) {
                        max = floats[i];
                        index = i;
                    }
                }
                logger.info("max :{}", Float.valueOf(max));
                if (max >= 0.62f) {
                    memberId = memberIds.get(index);
                }
            }
        } catch (BizException e) {
            throw new BizException(e.getErrorCode(), e.getMessage());
        } catch (Exception e2) {
            logger.error("error:{}", (Throwable) e2);
        }
        Member memberInfo = this.memberMapper.getMemberInfoByMemberId(memberId, orgId);
        if (memberInfo != null) {
            Logger logger2 = logger;
            Object[] objArr = new Object[3];
            objArr[0] = oldMemberId;
            objArr[1] = memberId;
            objArr[2] = Boolean.valueOf(memberId.longValue() == oldMemberId.longValue());
            logger2.info("old member id :{} , match member id :{} , match result :{}", objArr);
            AssertUtil.isTrue(Boolean.valueOf(oldMemberId != null && memberId.longValue() == oldMemberId.longValue()), OnpremiseErrorEnum.FACE_HAS_MATCH_MEMBER, memberInfo.getMemberName());
        }
        return memberId;
    }

    @Override // com.moredian.onpremise.api.member.MemberService
    public List<String> listFaceUrl(Long orgId) {
        List<String> faceUrlList = this.memberMapper.listFaceUrl(orgId);
        return faceUrlList;
    }

    @Override // com.moredian.onpremise.api.member.MemberService
    public String faceExport(MemberFaceExportRequest request) {
        List<Member> memberList = this.memberMapper.faceExport(request);
        if (CollectionUtils.isEmpty(memberList)) {
            throw new BizException(OnpremiseErrorEnum.MEMBER_FACE_EXPORT_NULL_FAIL.getErrorCode(), OnpremiseErrorEnum.MEMBER_FACE_EXPORT_NULL_FAIL.getMessage());
        }
        List<String> exportFaceFilePathList = new ArrayList<>();
        for (Member member : memberList) {
            String faceFilePath = this.uplocadConfig.getFilePath() + member.getVerifyFaceUrl();
            if (MyFileUtils.exists(faceFilePath)) {
                String exportFaceFilePath = this.uplocadConfig.getSaveImagePath() + member.getMemberName() + "-" + member.getMemberJobNum() + ".jpg";
                MyFileUtils.copyMirrorImg(faceFilePath, exportFaceFilePath);
                exportFaceFilePathList.add(exportFaceFilePath);
            }
        }
        return MyFileUtils.fileListToZip(exportFaceFilePathList, this.uplocadConfig.getSaveImagePath(), 0).replace(this.uplocadConfig.getFilePath(), "/");
    }

    @Override // com.moredian.onpremise.api.member.MemberService
    @Transactional(rollbackFor = {Exception.class})
    public Long saveMemberByDevice(TerminalSaveMemberRequest request) throws BeansException {
        SaveMemberRequest saveMemberRequest = new SaveMemberRequest();
        BeanUtils.copyProperties(request, saveMemberRequest);
        saveMemberRequest.setDeptId(request.getDeptId());
        checkSaveMemberRequest(saveMemberRequest);
        Member member = saveMemberRequestToMember(saveMemberRequest);
        member.setEigenvalueValue(request.getEigenvalueValue());
        AssertUtil.isTrue(Boolean.valueOf(this.memberMapper.addMember(member) > 0), OnpremiseErrorEnum.SAVE_MEMBER_FAIL);
        if (!StringUtils.isEmpty(request.getVerifyFaceUrl())) {
            SaveMemberFaceRequest saveMemberFaceRequest = new SaveMemberFaceRequest();
            saveMemberFaceRequest.setEigenvalueValue(request.getEigenvalueValue());
            saveMemberFaceRequest.setVerifyFaceUrl(request.getVerifyFaceUrl());
            insertUpdateEigenvalueRecord(member, saveMemberFaceRequest);
            doSendNettyMsg(request.getOrgId());
        }
        return member.getMemberId();
    }

    @Override // com.moredian.onpremise.api.member.MemberService
    @Transactional(rollbackFor = {Exception.class})
    public boolean deleteFace(DeleteFaceRequest request) {
        AssertUtil.isTrue(Boolean.valueOf(request.getMemberId() != null && request.getMemberId().longValue() > 0), OnpremiseErrorEnum.MEMBER_ID_MUST_NOT_NULL);
        Member member = this.memberMapper.getMemberInfoByMemberId(request.getMemberId(), request.getOrgId());
        AssertUtil.isNullOrEmpty(member, OnpremiseErrorEnum.MEMBER_NOT_FIND);
        AssertUtil.isNullOrEmpty(member.getVerifyFaceUrl(), OnpremiseErrorEnum.MEMBER_NO_FACE);
        AssertUtil.isNullOrEmpty(member.getEigenvalueValue(), OnpremiseErrorEnum.MEMBER_NO_FACE);
        AssertUtil.isTrue(Boolean.valueOf(this.memberMapper.deleteFace(member.getOrgId(), member.getMemberId(), new ArrayList()) > 0), OnpremiseErrorEnum.DELETE_MEMBER_FACE_FAIL);
        UpdateEigenvalueRecord record = new UpdateEigenvalueRecord();
        record.setMemberId(member.getMemberId());
        record.setNewEigenvalueValue("");
        record.setNewVerifyFaceUrl("");
        record.setOrgId(member.getOrgId());
        record.setOldEigenvalueValue(member.getEigenvalueValue());
        record.setOldVerifyFaceUrl(member.getVerifyFaceUrl());
        AssertUtil.isTrue(Boolean.valueOf(this.eigenvalueRecordMapper.insertRecord(record) > 0), OnpremiseErrorEnum.SAVE_UPDATE_EIGENVALUE_RECORD_FAIL);
        Account account = this.accountMapper.getAccountByMemberId(member.getMemberId(), member.getOrgId());
        if (account != null) {
            if (AccountGradeEnum.SUPER_ACCOUNT.getValue() == account.getAccountGrade().intValue() || AccountGradeEnum.MAIN_ACCOUNT.getValue() == account.getAccountGrade().intValue()) {
                doSendSyncAccountNettyMsg(member.getOrgId());
            } else {
                AccountAuth accountAuth = this.accountAuthMapper.getOneByAccountId(account.getAccountId(), member.getOrgId());
                doSendSyncAccountNettyMsgByDeviceSn(member.getOrgId(), accountAuth.getManageDeviceSn());
            }
        }
        MyFileUtils.deleteFile(this.uplocadConfig.getFilePath() + member.getVerifyFaceUrl());
        doSendNettyMsg(request.getOrgId());
        return true;
    }

    @Override // com.moredian.onpremise.api.member.MemberService
    @Transactional(rollbackFor = {Exception.class})
    public boolean batchDeleteFace(DeleteFaceRequest request) {
        AssertUtil.isTrue(Boolean.valueOf(!CollectionUtils.isEmpty(request.getMemberIds())), OnpremiseErrorEnum.MEMBER_ID_MUST_NOT_NULL);
        List<Member> memberList = this.memberMapper.listMember(request.getOrgId(), request.getMemberIds(), null);
        AssertUtil.isTrue(Boolean.valueOf(!CollectionUtils.isEmpty(memberList)), OnpremiseErrorEnum.MEMBER_NOT_FIND);
        AssertUtil.isTrue(Boolean.valueOf(this.memberMapper.deleteFace(request.getOrgId(), null, request.getMemberIds()) > 0), OnpremiseErrorEnum.DELETE_MEMBER_FACE_FAIL);
        List<UpdateEigenvalueRecord> updateEigenvalueRecordList = new ArrayList<>();
        for (Member member : memberList) {
            if (!StringUtils.isEmpty(member.getVerifyFaceUrl())) {
                UpdateEigenvalueRecord record = new UpdateEigenvalueRecord();
                record.setMemberId(member.getMemberId());
                record.setNewEigenvalueValue("");
                record.setNewVerifyFaceUrl("");
                record.setOrgId(member.getOrgId());
                record.setOldEigenvalueValue(member.getEigenvalueValue());
                record.setOldVerifyFaceUrl(member.getVerifyFaceUrl());
                updateEigenvalueRecordList.add(record);
                MyFileUtils.deleteFile(this.uplocadConfig.getFilePath() + member.getVerifyFaceUrl());
            }
        }
        AssertUtil.isTrue(Boolean.valueOf(this.eigenvalueRecordMapper.batchInsertRecord(updateEigenvalueRecordList) > 0), OnpremiseErrorEnum.SAVE_UPDATE_EIGENVALUE_RECORD_FAIL);
        doSendSyncAccountNettyMsg(request.getOrgId());
        doSendNettyMsg(request.getOrgId());
        return true;
    }

    @Override // com.moredian.onpremise.api.member.MemberService
    @Transactional(rollbackFor = {Exception.class})
    public boolean deleteFaceByJobNum(DeleteFaceRequest request) {
        AssertUtil.isTrue(Boolean.valueOf(!StringUtils.isEmpty(request.getMemberJobNum())), OnpremiseErrorEnum.MEMBER_JOB_NUM_MUST_NOT_NULL);
        Member member = this.memberMapper.getMemberInfoByJobNum(request.getMemberJobNum(), request.getOrgId());
        AssertUtil.isNullOrEmpty(member, OnpremiseErrorEnum.MEMBER_NOT_FIND);
        AssertUtil.isNullOrEmpty(member.getVerifyFaceUrl(), OnpremiseErrorEnum.MEMBER_NO_FACE);
        AssertUtil.isNullOrEmpty(member.getEigenvalueValue(), OnpremiseErrorEnum.MEMBER_NO_FACE);
        AssertUtil.isTrue(Boolean.valueOf(this.memberMapper.deleteFaceByJobNum(member.getOrgId(), member.getMemberJobNum(), new ArrayList()) > 0), OnpremiseErrorEnum.DELETE_MEMBER_FACE_FAIL);
        UpdateEigenvalueRecord record = new UpdateEigenvalueRecord();
        record.setMemberId(member.getMemberId());
        record.setNewEigenvalueValue("");
        record.setNewVerifyFaceUrl("");
        record.setOrgId(member.getOrgId());
        record.setOldEigenvalueValue(member.getEigenvalueValue());
        record.setOldVerifyFaceUrl(member.getVerifyFaceUrl());
        AssertUtil.isTrue(Boolean.valueOf(this.eigenvalueRecordMapper.insertRecord(record) > 0), OnpremiseErrorEnum.SAVE_UPDATE_EIGENVALUE_RECORD_FAIL);
        Account account = this.accountMapper.getAccountByMemberId(member.getMemberId(), member.getOrgId());
        if (account != null) {
            if (AccountGradeEnum.SUPER_ACCOUNT.getValue() == account.getAccountGrade().intValue() || AccountGradeEnum.MAIN_ACCOUNT.getValue() == account.getAccountGrade().intValue()) {
                doSendSyncAccountNettyMsg(member.getOrgId());
            } else {
                AccountAuth accountAuth = this.accountAuthMapper.getOneByAccountId(account.getAccountId(), member.getOrgId());
                doSendSyncAccountNettyMsgByDeviceSn(member.getOrgId(), accountAuth.getManageDeviceSn());
            }
        }
        MyFileUtils.deleteFile(this.uplocadConfig.getFilePath() + member.getVerifyFaceUrl());
        doSendNettyMsg(request.getOrgId());
        return true;
    }

    @Override // com.moredian.onpremise.api.member.MemberService
    @Transactional(rollbackFor = {Exception.class})
    public boolean batchDeleteFaceByJobNum(DeleteFaceRequest request) {
        AssertUtil.isTrue(Boolean.valueOf(!CollectionUtils.isEmpty(request.getMemberJobNums())), OnpremiseErrorEnum.MEMBER_JOB_NUM_MUST_NOT_NULL);
        List<Member> memberList = this.memberMapper.listMemberByJobNum(request.getOrgId(), request.getMemberJobNums());
        AssertUtil.isTrue(Boolean.valueOf(!CollectionUtils.isEmpty(memberList)), OnpremiseErrorEnum.MEMBER_NOT_FIND);
        AssertUtil.isTrue(Boolean.valueOf(this.memberMapper.deleteFaceByJobNum(request.getOrgId(), null, request.getMemberJobNums()) > 0), OnpremiseErrorEnum.DELETE_MEMBER_FACE_FAIL);
        List<UpdateEigenvalueRecord> updateEigenvalueRecordList = new ArrayList<>();
        for (Member member : memberList) {
            if (!StringUtils.isEmpty(member.getVerifyFaceUrl())) {
                UpdateEigenvalueRecord record = new UpdateEigenvalueRecord();
                record.setMemberId(member.getMemberId());
                record.setNewEigenvalueValue("");
                record.setNewVerifyFaceUrl("");
                record.setOrgId(member.getOrgId());
                record.setOldEigenvalueValue(member.getEigenvalueValue());
                record.setOldVerifyFaceUrl(member.getVerifyFaceUrl());
                updateEigenvalueRecordList.add(record);
                MyFileUtils.deleteFile(this.uplocadConfig.getFilePath() + member.getVerifyFaceUrl());
            }
        }
        AssertUtil.isTrue(Boolean.valueOf(this.eigenvalueRecordMapper.batchInsertRecord(updateEigenvalueRecordList) > 0), OnpremiseErrorEnum.SAVE_UPDATE_EIGENVALUE_RECORD_FAIL);
        doSendSyncAccountNettyMsg(request.getOrgId());
        doSendNettyMsg(request.getOrgId());
        return true;
    }

    @Override // com.moredian.onpremise.api.member.MemberService
    public FaceReloadProgressResponse faceReloadProgress() {
        FaceReloadProgressResponse faceReloadProgressResponse = CacheAdapter.getFaceReloadProgress(Constants.FACE_RELOAD_PROGRESS);
        if (faceReloadProgressResponse == null) {
            return new FaceReloadProgressResponse();
        }
        return faceReloadProgressResponse;
    }

    @Override // com.moredian.onpremise.api.member.MemberService
    @Transactional(rollbackFor = {RuntimeException.class})
    public SaveMemberFaceResponse saveMemberFace(SaveMemberFaceRequest request) throws BeansException {
        AssertUtil.isNullOrEmpty(request.getEigenvalueValue(), OnpremiseErrorEnum.MEMBER_EIGENVALUEVALUE_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getMemberJobNum(), OnpremiseErrorEnum.MEMBER_NUMBERS_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getVerifyFaceUrl(), OnpremiseErrorEnum.MEMBER_VERIFYFACEURL_MUST_NOT_NULL);
        AssertUtil.isTrue(Boolean.valueOf(request.getOrgId() != null && request.getOrgId().longValue() > 0), OnpremiseErrorEnum.ORG_ID_MUST_NOT_NULL);
        Member member = this.memberMapper.getMemberInfoByJobNum(request.getMemberJobNum(), request.getOrgId());
        AssertUtil.isNullOrEmpty(member, OnpremiseErrorEnum.MEMBER_NOT_FIND);
        request.setVerifyFaceMd5(getMd5(request.getVerifyFaceUrl()));
        int result = this.memberMapper.saveMemberFace(request);
        logger.info("===============update result :{}", Integer.valueOf(result));
        AssertUtil.isTrue(Boolean.valueOf(result > 0), OnpremiseErrorEnum.SAVE_MEMBER_FACE_FAIL);
        insertUpdateEigenvalueRecord(member, request);
        SaveMemberFaceResponse response = new SaveMemberFaceResponse();
        BeanUtils.copyProperties(member, response);
        response.setVerifyFaceUrl(request.getVerifyFaceUrl());
        if (request.isNeedSendMsg()) {
            doSendNettyMsg(request.getOrgId());
        }
        return response;
    }

    @Override // com.moredian.onpremise.api.member.MemberService
    public boolean replaceFaceByVerifyRecord(VerifyRecordRequest request) throws BeansException {
        VerifyRecord verifyRecord = this.verifyRecordMapper.getOneByVerifyRecordId(request.getOrgId(), request.getVerifyRecordId());
        AssertUtil.isNullOrEmpty(verifyRecord, OnpremiseErrorEnum.VERIFY_RECORD_NOT_FOUND);
        Member member = this.memberMapper.getMemberInfoByMemberId(verifyRecord.getMemberId(), request.getOrgId());
        AssertUtil.isNullOrEmpty(member, OnpremiseErrorEnum.MEMBER_NOT_FIND);
        String faceImageUrl = MyFileUtils.copyFileUsingFileChannels(this.uplocadConfig.getFilePath() + verifyRecord.getSnapFaceUrl(), this.uplocadConfig.getSaveImagePath());
        if (!this.configService.getRepeatFace().equals(1)) {
            memberIds.clear();
            FaceDet.LoadAllFeatures(new byte[0], 0, 512);
        } else {
            loadAllMemberFeature(verifyRecord.getMemberId());
        }
        String feature = doExtractFeature(this.uplocadConfig.getSaveImagePath() + faceImageUrl);
        SaveMemberFaceRequest saveMemberFaceRequest = new SaveMemberFaceRequest();
        saveMemberFaceRequest.setOrgId(request.getOrgId());
        saveMemberFaceRequest.setMemberJobNum(member.getMemberJobNum());
        saveMemberFaceRequest.setNeedSendMsg(true);
        saveMemberFaceRequest.setEigenvalueValue(feature);
        saveMemberFaceRequest.setVerifyFaceUrl(this.uplocadConfig.getImageUrl(2, faceImageUrl));
        saveMemberFace(saveMemberFaceRequest);
        return true;
    }

    @Override // com.moredian.onpremise.api.member.MemberService
    public PageList<MemberGroupListResponse> memberGroupList(MemberGroupListRequest request) throws BeansException {
        List<Long> groupMemberIds;
        int type;
        List<Long> groupDeptIds = null;
        if (request.getGroupId() != null) {
            groupMemberIds = this.groupMemberMapper.getMemberIdListByGroupId(request.getGroupId(), request.getOrgId());
            type = 2;
            if (request.getDeptId() == null) {
                type = 3;
                groupDeptIds = this.groupMemberMapper.getDeptIdListByGroupId(request.getGroupId(), request.getOrgId());
                if (CollectionUtils.isEmpty(groupDeptIds)) {
                    type = 4;
                }
            }
        } else {
            List<Long> groupIds = new ArrayList<>();
            if (!StringUtils.isEmpty(request.getDeviceSn())) {
                List<Long> deviceGroupIds = this.groupDeviceMapper.listGroupIdByDeviceIdNotDelete(request.getDeviceSn(), request.getOrgId());
                groupIds.addAll(deviceGroupIds);
                if (CollectionUtils.isEmpty(groupIds)) {
                    return new PageList<>(Paginator.initPaginator(new ArrayList()), new ArrayList());
                }
            }
            groupMemberIds = this.groupMemberMapper.getMemberIdListByGroupIds(groupIds, request.getOrgId());
            type = 2;
            if (request.getDeptId() == null) {
                type = 3;
                groupDeptIds = this.groupMemberMapper.getDeptIdListByGroupIds(groupIds, request.getOrgId());
                if (CollectionUtils.isEmpty(groupDeptIds)) {
                    type = 4;
                }
            }
        }
        request.setMemberIds(groupMemberIds);
        request.setDeptIds(groupDeptIds);
        request.setType(Integer.valueOf(type));
        Paginator paginator = request.getPaginator();
        if (Paginator.checkPaginator(paginator)) {
            PageHelper.startPage(paginator.getPageNum(), paginator.getPageSize());
            List<MemberGroupListResponse> list = this.memberMapper.memberGroupList(request);
            packageMemberGroupListResponses(list, request.getOrgId());
            return new PageList<>(list);
        }
        List<MemberGroupListResponse> list2 = this.memberMapper.memberGroupList(request);
        packageMemberGroupListResponses(list2, request.getOrgId());
        return new PageList<>(Paginator.initPaginator(list2), list2);
    }

    @Override // com.moredian.onpremise.api.member.MemberService
    public MemberDetailsResponse getMemberByIdentityCard(TerminalSearchMemberRequest request) throws BeansException {
        AssertUtil.isNullOrEmpty(request.getIdentityCard(), OnpremiseErrorEnum.MEMBER_IDENTITY_CARD_MUST_NOT_NULL);
        AssertUtil.isTrue(Boolean.valueOf(request.getOrgId() != null && request.getOrgId().longValue() > 0), OnpremiseErrorEnum.ORG_ID_MUST_NOT_NULL);
        Member member = this.memberMapper.getMemberInfoByIdentityCard(request.getIdentityCard(), request.getOrgId());
        AssertUtil.isNullOrEmpty(member, OnpremiseErrorEnum.MEMBER_NOT_FIND);
        MemberDetailsResponse response = new MemberDetailsResponse();
        BeanUtils.copyProperties(member, response);
        response.setDeptName(this.deptService.packagingDeptName(member.getDeptId(), false));
        List<MemberDetailsDeptResponse> deptList = null;
        if (StringUtils.isNotEmpty(member.getDeptId())) {
            deptList = new ArrayList<>();
            String[] deptIds = member.getDeptId().split(",");
            for (String deptId : deptIds) {
                Dept dept = this.deptMapper.getDeptById(Long.valueOf(deptId));
                AssertUtil.isNullOrEmpty(dept, OnpremiseErrorEnum.DEPT_NOT_FIND);
                MemberDetailsDeptResponse tempDept = new MemberDetailsDeptResponse();
                tempDept.setDeptId(dept.getDeptId());
                tempDept.setDeptName(dept.getDeptName());
                deptList.add(tempDept);
            }
        }
        response.setDeptList(deptList);
        return response;
    }

    private void packageMemberGroupListResponses(List<MemberGroupListResponse> list, Long orgId) throws BeansException {
        for (MemberGroupListResponse response : list) {
            response.setDeptName(this.deptService.packagingDeptName(response.getDeptId(), true));
            List<Long> groupIds = new ArrayList<>();
            List<GroupDto> groupDtos = new ArrayList<>();
            List<DeviceDto> deviceDtos = new ArrayList<>();
            if (StringUtils.isNotEmpty(response.getDeptId())) {
                String[] deptIds = response.getDeptId().split(",");
                for (String deptId : deptIds) {
                    List<GroupMember> groupMemberList = this.groupMemberMapper.listByDeptId(Long.valueOf(Long.parseLong(deptId)), orgId);
                    for (GroupMember groupMember : groupMemberList) {
                        if (!groupIds.contains(groupMember.getGroupId())) {
                            groupIds.add(groupMember.getGroupId());
                        }
                    }
                }
            }
            List<GroupMemberDto> groupMemberList2 = this.groupMemberMapper.listByMemberId(response.getMemberId(), orgId);
            for (GroupMemberDto groupMemberDto : groupMemberList2) {
                if (!groupIds.contains(groupMemberDto.getGroupId())) {
                    groupIds.add(groupMemberDto.getGroupId());
                }
            }
            if (CollectionUtils.isEmpty(groupIds)) {
                response.setGroups(groupDtos);
                response.setDevices(deviceDtos);
            } else {
                List<GroupDeviceGroupDto> groups = this.groupMapper.listByGroupId(groupIds, orgId);
                for (GroupDeviceGroupDto groupDeviceGroupDto : groups) {
                    GroupDto groupDto = new GroupDto();
                    BeanUtils.copyProperties(groupDeviceGroupDto, groupDto);
                    groupDtos.add(groupDto);
                }
                response.setGroups(groupDtos);
                List<String> deviceSns = this.groupDeviceMapper.listDeviceSnByGroupIds(groupIds, orgId);
                List<DeviceDto> deviceDtos2 = this.deviceMapper.listDeviceDtoByDeviceSn(deviceSns, orgId);
                response.setDevices(deviceDtos2);
            }
        }
    }

    @Override // com.moredian.onpremise.api.member.MemberService
    public PageList<MemberInputFaceRecordListResponse> inputFaceRecordList(MemberInputFaceRecordListRequest request) {
        Paginator paginator = request.getPaginator();
        if (Paginator.checkPaginator(paginator)) {
            PageHelper.startPage(paginator.getPageNum(), paginator.getPageSize());
            return new PageList<>(this.eigenvalueRecordMapper.eigenvalueRecordList(request));
        }
        List<MemberInputFaceRecordListResponse> list = this.eigenvalueRecordMapper.eigenvalueRecordList(request);
        return new PageList<>(Paginator.initPaginator(list), list);
    }

    @Override // com.moredian.onpremise.api.member.MemberService
    public MemberDetailsResponse certifyMember(H5CertifyMemberRequest request) throws BeansException {
        AssertUtil.isNullOrEmpty(request.getIdentityCard(), OnpremiseErrorEnum.MEMBER_IDENTITY_CARD_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getName(), OnpremiseErrorEnum.MEMBER_NAME_MUST_NOT_NULL);
        Member member = this.memberMapper.getMemberInfoByIdentityCard(request.getIdentityCard(), request.getOrgId());
        if (member == null) {
            member = this.memberMapper.getMemberInfoByJobNum(request.getIdentityCard(), request.getOrgId());
        }
        AssertUtil.isNullOrEmpty(member, OnpremiseErrorEnum.MEMBER_NOT_FIND);
        AssertUtil.isTrue(Boolean.valueOf(request.getName().equals(member.getMemberName())), OnpremiseErrorEnum.IDENTITY_CARD_NAME_NOT_MATCH);
        MemberDetailsResponse memberDetailsResponse = new MemberDetailsResponse();
        BeanUtils.copyProperties(member, memberDetailsResponse);
        return memberDetailsResponse;
    }
}
