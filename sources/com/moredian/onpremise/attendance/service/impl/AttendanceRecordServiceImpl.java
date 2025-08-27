package com.moredian.onpremise.attendance.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.github.pagehelper.PageHelper;
import com.moredian.onpremise.api.attendance.AttendanceEventService;
import com.moredian.onpremise.api.attendance.AttendanceRecordService;
import com.moredian.onpremise.api.member.DeptService;
import com.moredian.onpremise.api.member.MemberService;
import com.moredian.onpremise.core.adapter.CacheAdapter;
import com.moredian.onpremise.core.common.constants.Constants;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import com.moredian.onpremise.core.common.enums.AttendanceEventFrameEnum;
import com.moredian.onpremise.core.common.enums.AttendanceEventTypeEnum;
import com.moredian.onpremise.core.common.enums.AttendanceGroupTypeEnum;
import com.moredian.onpremise.core.common.enums.AttendanceRecordResultEnum;
import com.moredian.onpremise.core.common.enums.AttendanceRecordTypeEnum;
import com.moredian.onpremise.core.common.enums.AttendanceStatusTypeEnum;
import com.moredian.onpremise.core.common.enums.MemberTypeEnum;
import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.common.enums.StatisticsForMonthExportIconEnum;
import com.moredian.onpremise.core.mapper.AttendanceEventLeaveMapper;
import com.moredian.onpremise.core.mapper.AttendanceEventOutMapper;
import com.moredian.onpremise.core.mapper.AttendanceEventOvertimeMapper;
import com.moredian.onpremise.core.mapper.AttendanceEventSupplementMapper;
import com.moredian.onpremise.core.mapper.AttendanceEventTripMapper;
import com.moredian.onpremise.core.mapper.AttendanceGroupMapper;
import com.moredian.onpremise.core.mapper.AttendanceGroupMemberMapper;
import com.moredian.onpremise.core.mapper.AttendanceGroupTimeMapper;
import com.moredian.onpremise.core.mapper.AttendanceRecordMapper;
import com.moredian.onpremise.core.mapper.DeptMapper;
import com.moredian.onpremise.core.mapper.MemberMapper;
import com.moredian.onpremise.core.model.domain.AttendanceEventLeave;
import com.moredian.onpremise.core.model.domain.AttendanceEventOut;
import com.moredian.onpremise.core.model.domain.AttendanceEventOvertime;
import com.moredian.onpremise.core.model.domain.AttendanceEventTrip;
import com.moredian.onpremise.core.model.domain.AttendanceGroup;
import com.moredian.onpremise.core.model.domain.AttendanceGroupTime;
import com.moredian.onpremise.core.model.domain.AttendanceRecord;
import com.moredian.onpremise.core.model.domain.Dept;
import com.moredian.onpremise.core.model.domain.Member;
import com.moredian.onpremise.core.model.dto.GroupMemberDto;
import com.moredian.onpremise.core.model.info.AttendanceGroupMemberInfo;
import com.moredian.onpremise.core.model.info.AttendanceTimeCheckInfo;
import com.moredian.onpremise.core.model.request.AttendanceStatisticsForMonthExportRequest;
import com.moredian.onpremise.core.model.request.CountInfoForDayRequest;
import com.moredian.onpremise.core.model.request.CountInfoForMonthRequest;
import com.moredian.onpremise.core.model.request.ListAttendanceGroupRequest;
import com.moredian.onpremise.core.model.request.ListAttendanceRecordRequest;
import com.moredian.onpremise.core.model.request.SaveAttendanceRecordRequest;
import com.moredian.onpremise.core.model.response.AttendanceEventDetailResponse;
import com.moredian.onpremise.core.model.response.AttendanceGroupListResponse;
import com.moredian.onpremise.core.model.response.CountInfoForMonthResponse;
import com.moredian.onpremise.core.model.response.DeptMemberListResponse;
import com.moredian.onpremise.core.model.response.ListAttendanceRecordResponse;
import com.moredian.onpremise.core.model.response.ListInfoForDayDetailResponse;
import com.moredian.onpremise.core.model.response.ListInfoForDayDetailResponse.ListInfoDayAttendanceDetail;
import com.moredian.onpremise.core.model.response.ListInfoForDayResponse;
import com.moredian.onpremise.core.model.response.UserLoginResponse;
import com.moredian.onpremise.core.utils.AssertUtil;
import com.moredian.onpremise.core.utils.MyDateUtils;
import com.moredian.onpremise.core.utils.MyListUtils;
import com.moredian.onpremise.core.utils.PageList;
import com.moredian.onpremise.core.utils.Paginator;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
/* loaded from: onpremise-attendance-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/attendance/service/impl/AttendanceRecordServiceImpl.class */
public class AttendanceRecordServiceImpl implements AttendanceRecordService {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) AttendanceRecordServiceImpl.class);

    @Value("${onpremise.save.excel.path}")
    private String saveExcelPath;

    @Value("${onpremise.file.path}")
    private String saveFilePath;

    @Autowired
    private AttendanceRecordMapper attendanceRecordMapper;

    @Autowired
    private AttendanceGroupMapper attendanceGroupMapper;

    @Autowired
    private AttendanceGroupTimeMapper attendanceGroupTimeMapper;

    @Autowired
    private AttendanceGroupMemberMapper attendanceGroupMemberMapper;

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private AttendanceEventService attendanceEventService;

    @Autowired
    private AttendanceEventLeaveMapper attendanceEventLeaveMapper;

    @Autowired
    private AttendanceEventTripMapper attendanceEventTripMapper;

    @Autowired
    private AttendanceEventOutMapper attendanceEventOutMapper;

    @Autowired
    private AttendanceEventOvertimeMapper attendanceEventOvertimeMapper;

    @Autowired
    private AttendanceEventSupplementMapper attendanceEventSupplementMapper;

    @Autowired
    private MemberService memberService;

    @Autowired
    private DeptService deptService;

    @Override // com.moredian.onpremise.api.attendance.AttendanceRecordService
    @Transactional(rollbackFor = {RuntimeException.class})
    public boolean saveRecord(SaveAttendanceRecordRequest request) throws BeansException {
        AssertUtil.checkId(request.getMemberId(), OnpremiseErrorEnum.MEMBER_ID_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getMemberName(), OnpremiseErrorEnum.MEMBER_NAME_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getMemberJobNum(), OnpremiseErrorEnum.MEMBER_JOB_NUM_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getDeptId(), OnpremiseErrorEnum.DEPT_ID_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getAttendancePictureUrl(), OnpremiseErrorEnum.MEMBER_VERIFYFACEURL_MUST_NOT_NULL);
        AssertUtil.isTrue(Boolean.valueOf(request.getAttendanceTime() != null && request.getAttendanceTime().longValue() > 0), OnpremiseErrorEnum.ATTENDANCE_TIME_MUST_NOT_NULL);
        AssertUtil.checkId(request.getAttendanceGroupId(), OnpremiseErrorEnum.ATTENDANCE_GROUP_ID_MUST_NOT_NULL);
        AttendanceGroup attendanceGroup = this.attendanceGroupMapper.getOneByIdIncludeDelete(request.getAttendanceGroupId(), request.getOrgId());
        AssertUtil.isNullOrEmpty(attendanceGroup, OnpremiseErrorEnum.ATTENDANCE_GROUP_NOT_FIND);
        boolean result = false;
        if (!passAttendanceEvent(request.getOrgId(), request.getMemberId(), request.getAttendanceTime())) {
            result = false;
        } else {
            AttendanceRecord record = requestToRrcord(request, attendanceGroup.getGroupType());
            if (record != null) {
                this.attendanceRecordMapper.insert(record);
                result = true;
            }
        }
        return result;
    }

    @Override // com.moredian.onpremise.api.attendance.AttendanceRecordService
    public PageList<ListAttendanceRecordResponse> listAttendanceRecord(ListAttendanceRecordRequest request) {
        AssertUtil.checkOrgId(request.getOrgId());
        Paginator paginator = request.getPaginator();
        request.setManagerDeptIds(UserLoginResponse.getAccountManageDeptId(request.getSessionId()));
        if (MyDateUtils.parseDate(request.getStartDate(), "yyyy-MM-dd") != null) {
            request.setStartDate(request.getStartDate() + MyDateUtils.TIME_OF_DAY_BEGIN);
        }
        if (MyDateUtils.parseDate(request.getEndDate(), "yyyy-MM-dd") != null) {
            request.setEndDate(request.getEndDate() + MyDateUtils.TIME_OF_DAY_END);
        }
        if (Paginator.checkPaginator(paginator)) {
            PageHelper.startPage(paginator.getPageNum(), paginator.getPageSize());
            return new PageList<>(this.attendanceRecordMapper.listAttendanceRecord(request));
        }
        List<ListAttendanceRecordResponse> listResponse = this.attendanceRecordMapper.listAttendanceRecord(request);
        return new PageList<>(Paginator.initPaginator(listResponse), listResponse);
    }

    @Override // com.moredian.onpremise.api.attendance.AttendanceRecordService
    public PageList<ListInfoForDayResponse> listInfoForDay(CountInfoForDayRequest request) {
        AssertUtil.isNullOrEmpty(request.getQueryDate(), OnpremiseErrorEnum.COUNT_FOR_DAY_QUERY_DATE_MUST_NOT_NULL);
        request.setDeptIds(UserLoginResponse.getAccountManageDeptId(request.getSessionId()));
        if (Paginator.checkPaginator(request.getPaginator())) {
            PageHelper.startPage(request.getPaginator().getPageNum(), request.getPaginator().getPageSize());
            List<ListInfoForDayResponse> listResp = this.deptMapper.listCountForDayDept(request);
            packageListForDay(listResp, request);
            return new PageList<>(listResp);
        }
        List<ListInfoForDayResponse> listResp2 = this.deptMapper.listCountForDayDept(request);
        packageListForDay(listResp2, request);
        return new PageList<>(Paginator.initPaginator(listResp2), listResp2);
    }

    @Override // com.moredian.onpremise.api.attendance.AttendanceRecordService
    public ListInfoForDayResponse statisticsForDay(CountInfoForDayRequest request) {
        if (request.getDeptId() == null) {
            request.setDeptId(Long.valueOf(NumberUtils.toLong(CacheAdapter.getLoginInfo(request.getSessionId()).getManageDeptId().split(",")[0])));
        }
        AssertUtil.isNullOrEmpty(request.getQueryDate(), OnpremiseErrorEnum.COUNT_FOR_DAY_QUERY_DATE_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getDeptId(), OnpremiseErrorEnum.COUNT_FOR_DAY_DEPT_MUST_NOT_NULL);
        ListInfoForDayResponse listInfoForDayResponse = new ListInfoForDayResponse();
        Dept dept = this.deptMapper.getDeptById(request.getDeptId());
        if (dept != null) {
            listInfoForDayResponse.setDeptId(dept.getDeptId());
            listInfoForDayResponse.setDeptName(dept.getDeptName());
        }
        if (request.getAttendanceGroupId() != null) {
            AttendanceGroup attendanceGroup = this.attendanceGroupMapper.getOneById(request.getAttendanceGroupId(), request.getOrgId());
            listInfoForDayResponse.setAttendanceGroupId(attendanceGroup.getAttendanceGroupId());
            listInfoForDayResponse.setAttendanceGroupName(attendanceGroup.getGroupName());
        } else {
            listInfoForDayResponse.setAttendanceGroupName(Constants.DEVICE_ALL);
        }
        packageStatisticsForDay(request, listInfoForDayResponse);
        return listInfoForDayResponse;
    }

    private void packageStatisticsForDay(CountInfoForDayRequest request, ListInfoForDayResponse response) {
        int earlyNum = 0;
        int lateNum = 0;
        int normalNum = 0;
        int workLackNum = 0;
        int restLackNum = 0;
        int dayLackNum = 0;
        Long totalWorkTime = 0L;
        response.setCountDay(request.getQueryDate());
        Set<Long> deptMemberSet = new HashSet<>();
        this.memberService.getMemberListByDept(request.getOrgId(), request.getDeptId(), deptMemberSet);
        Collection<? extends Long> arrayList = new ArrayList<>(deptMemberSet);
        Set<Long> attendanceMemberSet = new HashSet<>();
        List<GroupMemberDto> memberDtoList = this.attendanceGroupMemberMapper.listByAttendanceGroupIdAndType(Integer.valueOf(MemberTypeEnum.MEMBER.getValue()), request.getOrgId(), request.getAttendanceGroupId());
        for (GroupMemberDto memberDto : memberDtoList) {
            attendanceMemberSet.add(memberDto.getMemberId());
        }
        List<GroupMemberDto> deptDtoList = this.attendanceGroupMemberMapper.listByAttendanceGroupIdAndType(Integer.valueOf(MemberTypeEnum.DEPT.getValue()), request.getOrgId(), request.getAttendanceGroupId());
        for (GroupMemberDto memberDto2 : deptDtoList) {
            this.memberService.getMemberListByDept(request.getOrgId(), memberDto2.getDeptId(), attendanceMemberSet);
        }
        Collection<?> attendanceMemberList = new ArrayList<>(attendanceMemberSet);
        List<Long> memberIdList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(arrayList)) {
            memberIdList.addAll(arrayList);
        }
        if (!CollectionUtils.isEmpty(attendanceMemberList)) {
            memberIdList.retainAll(attendanceMemberList);
        }
        if (!CollectionUtils.isEmpty(memberIdList)) {
            for (Long memberId : memberIdList) {
                List<AttendanceRecord> memberRecord = this.attendanceRecordMapper.getRecordStatusByMemberIdAndTime(memberId, request.getOrgId(), request.getAttendanceGroupId(), request.getQueryDate());
                if (MyListUtils.listIsEmpty(memberRecord)) {
                    logger.info("member record size :{}", Integer.valueOf(memberRecord.size()));
                    if (memberRecord.size() == 2) {
                        if (memberRecord.get(0).getAttendanceResult().intValue() == AttendanceRecordResultEnum.SUCCESS.getValue() && memberRecord.get(1).getAttendanceResult().intValue() == AttendanceRecordResultEnum.SUCCESS.getValue()) {
                            normalNum++;
                        }
                        if (memberRecord.get(0).getAttendanceResult().intValue() == AttendanceRecordResultEnum.LATE.getValue() || memberRecord.get(1).getAttendanceResult().intValue() == AttendanceRecordResultEnum.LATE.getValue()) {
                            lateNum++;
                        }
                        if (memberRecord.get(0).getAttendanceResult().intValue() == AttendanceRecordResultEnum.EARLY.getValue() || memberRecord.get(1).getAttendanceResult().intValue() == AttendanceRecordResultEnum.EARLY.getValue()) {
                            earlyNum++;
                        }
                        totalWorkTime = Long.valueOf(Long.valueOf(totalWorkTime.longValue() + memberRecord.get(0).getWorkTime().longValue()).longValue() + memberRecord.get(1).getWorkTime().longValue());
                    } else if (memberRecord.size() == 1) {
                        if (memberRecord.get(0).getRecordType().intValue() == AttendanceRecordTypeEnum.BEGAN_WORK.getValue()) {
                            restLackNum++;
                        }
                        if (memberRecord.get(0).getRecordType().intValue() == AttendanceRecordTypeEnum.END_WORK.getValue()) {
                            workLackNum++;
                        }
                        if (memberRecord.get(0).getAttendanceResult().intValue() == AttendanceRecordResultEnum.EARLY.getValue()) {
                            earlyNum++;
                        }
                        if (memberRecord.get(0).getAttendanceResult().intValue() == AttendanceRecordResultEnum.LATE.getValue()) {
                            lateNum++;
                        }
                        totalWorkTime = Long.valueOf(totalWorkTime.longValue() + memberRecord.get(0).getWorkTime().longValue());
                    } else {
                        logger.info("dept count for day is error ,dept id is :{}", response.getDeptId());
                    }
                } else {
                    dayLackNum++;
                }
            }
        }
        response.setEarlyNums(Integer.valueOf(earlyNum));
        response.setLateNums(Integer.valueOf(lateNum));
        response.setNormalNums(Integer.valueOf(normalNum));
        response.setWorkLackNums(Integer.valueOf(dayLackNum));
        response.setRestLackNums(Integer.valueOf(restLackNum + workLackNum));
        response.setMemberNums(Integer.valueOf(memberIdList.size()));
        response.setAvgWorkTime(Long.valueOf(response.getMemberNums().intValue() == 0 ? 0L : totalWorkTime.longValue() / response.getMemberNums().intValue()));
        response.setTotalWorkTime(totalWorkTime);
    }

    @Override // com.moredian.onpremise.api.attendance.AttendanceRecordService
    public PageList<ListInfoForDayDetailResponse> statisticsForDayDetail(CountInfoForDayRequest request) throws BeansException {
        List<Member> memberList;
        PageList<ListInfoForDayDetailResponse> result;
        if (request.getDeptId() == null) {
            request.setDeptId(Long.valueOf(NumberUtils.toLong(CacheAdapter.getLoginInfo(request.getSessionId()).getManageDeptId().split(",")[0])));
        }
        List<Long> deptsMatch = this.deptService.packagingChildDept(request.getDeptId(), request.getOrgId(), new ArrayList<>());
        deptsMatch.add(request.getDeptId());
        AssertUtil.isNullOrEmpty(request.getQueryDate(), OnpremiseErrorEnum.COUNT_FOR_DAY_QUERY_DATE_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getDeptId(), OnpremiseErrorEnum.COUNT_FOR_DAY_DEPT_MUST_NOT_NULL);
        List<AttendanceGroup> attendanceGroupList = new LinkedList<>();
        if (request.getAttendanceGroupId() != null) {
            attendanceGroupList.add(this.attendanceGroupMapper.getOneById(request.getAttendanceGroupId(), request.getOrgId()));
        } else {
            ListAttendanceGroupRequest listAttendanceGroupRequest = new ListAttendanceGroupRequest();
            listAttendanceGroupRequest.setOrgId(request.getOrgId());
            List<AttendanceGroupListResponse> tempAttendanceGroupList = this.attendanceGroupMapper.listAttendanceGroup(listAttendanceGroupRequest);
            for (AttendanceGroupListResponse tempAttendanceGroup : tempAttendanceGroupList) {
                AttendanceGroup attendanceGroup = new AttendanceGroup();
                attendanceGroup.setAttendanceGroupId(tempAttendanceGroup.getAttendanceGroupId());
                attendanceGroup.setGroupName(tempAttendanceGroup.getGroupName());
                attendanceGroup.setGroupType(tempAttendanceGroup.getGroupType());
                attendanceGroupList.add(attendanceGroup);
            }
        }
        Set<Long> checkMemberSet = new TreeSet<>();
        Iterator<AttendanceGroup> it = attendanceGroupList.iterator();
        while (it.hasNext()) {
            List<GroupMemberDto> attendanceGroupMemberList = this.attendanceGroupMemberMapper.listByAttendanceGroupIdAndType(null, request.getOrgId(), it.next().getAttendanceGroupId());
            for (GroupMemberDto groupMemberDto : attendanceGroupMemberList) {
                if (1 == groupMemberDto.getType().intValue()) {
                    List<DeptMemberListResponse> tempList = this.memberMapper.listMemberByDeptId(groupMemberDto.getDeptId(), request.getOrgId());
                    for (DeptMemberListResponse each : tempList) {
                        checkMemberSet.add(each.getMemberId());
                    }
                } else if (2 == groupMemberDto.getType().intValue()) {
                    checkMemberSet.add(groupMemberDto.getMemberId());
                }
            }
        }
        if (Paginator.checkPaginator(request.getPaginator())) {
            PageHelper.startPage(request.getPaginator().getPageNum(), request.getPaginator().getPageSize());
            memberList = this.memberMapper.listMember(request.getOrgId(), new ArrayList(checkMemberSet), null);
            PageList<Member> tempPage = new PageList<>(memberList);
            BeanUtils.copyProperties(tempPage.getPaginator(), request.getPaginator());
        } else {
            memberList = this.memberMapper.listMember(request.getOrgId(), new ArrayList(checkMemberSet), null);
        }
        for (int i = memberList.size() - 1; i >= 0; i--) {
            boolean holdFlag = false;
            Iterator<Long> it2 = deptsMatch.iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                Long eachMatch = it2.next();
                if (memberList.get(i).getDeptId().contains(eachMatch.toString())) {
                    holdFlag = true;
                    break;
                }
            }
            if (!holdFlag) {
                memberList.remove(i);
            }
        }
        List<ListInfoForDayDetailResponse> listResp = new LinkedList<>();
        for (Member member : memberList) {
            AttendanceGroupMemberInfo tempInfo = this.attendanceGroupMemberMapper.getOneByMemberId(member.getMemberId(), request.getOrgId());
            ListInfoForDayDetailResponse tempEach = new ListInfoForDayDetailResponse();
            tempEach.setMemberId(member.getMemberId());
            tempEach.setMemberName(member.getMemberName());
            tempEach.setMemberJobNum(member.getMemberJobNum());
            tempEach.setAttendanceGroupId(tempInfo != null ? tempInfo.getAttendanceGroupId() : null);
            String deptNames = MyListUtils.listToString(this.deptMapper.listDeptNameByDeptIds(request.getOrgId(), CollectionUtils.arrayToList(member.getDeptId().split(","))));
            tempEach.setDeptName(deptNames);
            listResp.add(tempEach);
        }
        packageListForDayDetail(request.getOrgId(), listResp, MyDateUtils.formatIntegerDay(MyDateUtils.parseDate(request.getQueryDate(), "yyyy-MM-dd")));
        if (Paginator.checkPaginator(request.getPaginator())) {
            result = new PageList<>(request.getPaginator(), listResp);
        } else {
            result = new PageList<>(Paginator.initPaginator(listResp), listResp);
        }
        return result;
    }

    private void packageListForDayDetail(Long orgId, List<ListInfoForDayDetailResponse> listResp, Integer queryDate) {
        for (ListInfoForDayDetailResponse listInfoForDayDetailResponse : listResp) {
            ListAttendanceRecordRequest request = new ListAttendanceRecordRequest();
            request.setOrgId(orgId);
            request.setMemberId(listInfoForDayDetailResponse.getMemberId());
            request.setAttendanceGroupId(listInfoForDayDetailResponse.getAttendanceGroupId());
            request.setQueryDate(queryDate);
            List<ListAttendanceRecordResponse> listAttendanceRecordResponseList = this.attendanceRecordMapper.listAttendanceRecord(request);
            List<ListInfoForDayDetailResponse.ListInfoDayAttendanceDetail> listInfoDayAttendanceDetailList = new ArrayList<>();
            Set<Integer> attendanceResult = new LinkedHashSet<>();
            for (ListAttendanceRecordResponse listAttendanceRecordResponse : listAttendanceRecordResponseList) {
                listInfoForDayDetailResponse.getClass();
                ListInfoForDayDetailResponse.ListInfoDayAttendanceDetail listInfoDayAttendanceDetail = listInfoForDayDetailResponse.new ListInfoDayAttendanceDetail();
                listInfoDayAttendanceDetail.setRuleTime(listAttendanceRecordResponse.getRuleTime());
                listInfoDayAttendanceDetail.setAttendanceTime(listAttendanceRecordResponse.getAttendanceTime());
                listInfoDayAttendanceDetail.setAttendanceResult(listAttendanceRecordResponse.getAttendanceResult());
                listInfoDayAttendanceDetail.setRecordType(listAttendanceRecordResponse.getRecordType());
                listInfoDayAttendanceDetailList.add(listInfoDayAttendanceDetail);
                if (listAttendanceRecordResponse.getRecordType().equals(Integer.valueOf(AttendanceRecordTypeEnum.END_WORK.getValue()))) {
                    listInfoForDayDetailResponse.setWorkTime(listAttendanceRecordResponse.getWorkTime());
                }
                attendanceResult.add(listAttendanceRecordResponse.getAttendanceResult());
            }
            listInfoForDayDetailResponse.setListInfoDayAttendanceDetails(listInfoDayAttendanceDetailList);
            if (CollectionUtils.isEmpty(attendanceResult) || listAttendanceRecordResponseList.size() < 2) {
                attendanceResult.add(4);
            } else if (attendanceResult.contains(1) && attendanceResult.size() > 1) {
                attendanceResult.remove(1);
            }
            listInfoForDayDetailResponse.setAttendanceResult((Integer[]) attendanceResult.toArray(new Integer[attendanceResult.size()]));
        }
    }

    @Override // com.moredian.onpremise.api.attendance.AttendanceRecordService
    public PageList<CountInfoForMonthResponse> listInfoForMonth(CountInfoForMonthRequest request) {
        AssertUtil.isNullOrEmpty(request.getQueryStartDate(), OnpremiseErrorEnum.COUNT_FOR_MONTH_QUERY_DATE_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getQueryEndDate(), OnpremiseErrorEnum.COUNT_FOR_MONTH_QUERY_DATE_MUST_NOT_NULL);
        request.setDeptIds(UserLoginResponse.getAccountManageDeptId(request.getSessionId()));
        if (Paginator.checkPaginator(request.getPaginator())) {
            PageHelper.startPage(request.getPaginator().getPageNum(), request.getPaginator().getPageSize());
            List<CountInfoForMonthResponse> listResp = this.attendanceGroupMemberMapper.listCountForMonthMember(request);
            packageListForMonth(listResp, request);
            return new PageList<>(listResp);
        }
        List<CountInfoForMonthResponse> listResp2 = this.attendanceGroupMemberMapper.listCountForMonthMember(request);
        packageListForMonth(listResp2, request);
        return new PageList<>(Paginator.initPaginator(listResp2), listResp2);
    }

    private void packageListForMonth(List<CountInfoForMonthResponse> listResp, CountInfoForMonthRequest request) {
        if (MyListUtils.listIsEmpty(listResp)) {
            if (MyDateUtils.parseDate(request.getQueryStartDate(), "yyyy-MM-dd") != null) {
                request.setQueryStartDate(request.getQueryStartDate() + MyDateUtils.TIME_OF_DAY_BEGIN);
            }
            if (MyDateUtils.parseDate(request.getQueryEndDate(), "yyyy-MM-dd") != null) {
                request.setQueryEndDate(request.getQueryEndDate() + MyDateUtils.TIME_OF_DAY_END);
            }
            for (CountInfoForMonthResponse response : listResp) {
                CountInfoForMonthResponse monthResponse = this.attendanceRecordMapper.countForMonthByMemberId(response.getMemberId(), request.getOrgId(), request.getQueryStartDate(), request.getQueryEndDate());
                response.setLateTime(Long.valueOf(monthResponse.getLateTime().longValue() / 1000));
                response.setLateTimes(monthResponse.getLateTimes());
                response.setEarlyTime(Long.valueOf(monthResponse.getEarlyTime().longValue() / 1000));
                response.setEarlyTimes(monthResponse.getEarlyTimes());
                response.setWorkDays(monthResponse.getWorkDays());
                response.setRestDays(monthResponse.getRestDays());
                response.setWorkTime(Long.valueOf(monthResponse.getWorkTime().longValue() / 1000));
                response.setBeginWorkLackTimes(Integer.valueOf(this.attendanceRecordMapper.countTimesByRecordType(response.getMemberId(), request.getOrgId(), AttendanceRecordTypeEnum.END_WORK.getValue(), request.getQueryStartDate(), request.getQueryEndDate())));
                response.setEndWorkLackTimes(Integer.valueOf(this.attendanceRecordMapper.countTimesByRecordType(response.getMemberId(), request.getOrgId(), AttendanceRecordTypeEnum.BEGAN_WORK.getValue(), request.getQueryStartDate(), request.getQueryEndDate())));
            }
        }
    }

    private void packageListForDay(List<ListInfoForDayResponse> listResp, CountInfoForDayRequest request) {
        if (MyListUtils.listIsEmpty(listResp)) {
            int earlyNums = 0;
            int lateNums = 0;
            int normalNums = 0;
            int workLackNums = 0;
            int restLackNums = 0;
            int dayLackNums = 0;
            Long totalWorkTime = 0L;
            for (ListInfoForDayResponse response : listResp) {
                response.setCountDay(request.getQueryDate());
                List<DeptMemberListResponse> memberList = this.memberMapper.listMemberByDeptId(response.getDeptId(), request.getOrgId());
                if (MyListUtils.listIsEmpty(memberList)) {
                    logger.info("====member size :{}", Integer.valueOf(memberList.size()));
                    response.setMemberNums(Integer.valueOf(memberList.size()));
                    for (DeptMemberListResponse member : memberList) {
                        List<Long> groupIds = this.attendanceGroupMemberMapper.listAttendanceGroupIdByMemberIdAndTime(member.getMemberId(), request.getAttendanceGroupId(), request.getOrgId(), request.getQueryDate());
                        if (MyListUtils.listIsEmpty(groupIds)) {
                            logger.info("member attendance group id  size :{}", Integer.valueOf(groupIds.size()));
                            for (Long attendanceGroupId : groupIds) {
                                List<AttendanceRecord> memberRecord = this.attendanceRecordMapper.getRecordStatusByMemberIdAndTime(member.getMemberId(), request.getOrgId(), attendanceGroupId, request.getQueryDate());
                                if (MyListUtils.listIsEmpty(memberRecord)) {
                                    logger.info("member record size :{}", Integer.valueOf(memberRecord.size()));
                                    if (memberRecord.size() == 2) {
                                        if (memberRecord.get(0).getAttendanceResult().intValue() == AttendanceRecordResultEnum.SUCCESS.getValue() && memberRecord.get(1).getAttendanceResult().intValue() == AttendanceRecordResultEnum.SUCCESS.getValue()) {
                                            normalNums++;
                                        }
                                        if (memberRecord.get(0).getAttendanceResult().intValue() == AttendanceRecordResultEnum.LATE.getValue() || memberRecord.get(1).getAttendanceResult().intValue() == AttendanceRecordResultEnum.LATE.getValue()) {
                                            lateNums++;
                                        }
                                        if (memberRecord.get(0).getAttendanceResult().intValue() == AttendanceRecordResultEnum.EARLY.getValue() || memberRecord.get(1).getAttendanceResult().intValue() == AttendanceRecordResultEnum.EARLY.getValue()) {
                                            earlyNums++;
                                        }
                                        totalWorkTime = Long.valueOf(Long.valueOf(totalWorkTime.longValue() + memberRecord.get(0).getWorkTime().longValue()).longValue() + memberRecord.get(1).getWorkTime().longValue());
                                    } else if (memberRecord.size() == 1) {
                                        if (memberRecord.get(0).getRecordType().intValue() == AttendanceRecordTypeEnum.BEGAN_WORK.getValue()) {
                                            restLackNums++;
                                        }
                                        if (memberRecord.get(0).getRecordType().intValue() == AttendanceRecordTypeEnum.END_WORK.getValue()) {
                                            workLackNums++;
                                        }
                                        if (memberRecord.get(0).getAttendanceResult().intValue() == AttendanceRecordResultEnum.EARLY.getValue()) {
                                            earlyNums++;
                                        }
                                        if (memberRecord.get(0).getAttendanceResult().intValue() == AttendanceRecordResultEnum.LATE.getValue()) {
                                            lateNums++;
                                        }
                                        totalWorkTime = Long.valueOf(totalWorkTime.longValue() + memberRecord.get(0).getWorkTime().longValue());
                                    } else {
                                        logger.info("dept count for day is error ,dept id is :{}", response.getDeptId());
                                    }
                                } else {
                                    dayLackNums++;
                                }
                            }
                        }
                    }
                }
                Long totalWorkTime2 = Long.valueOf(totalWorkTime.longValue() / 1000);
                response.setAvgWorkTime(Long.valueOf(response.getMemberNums().intValue() == 0 ? 0L : totalWorkTime2.longValue() / response.getMemberNums().intValue()));
                response.setEarlyNums(Integer.valueOf(earlyNums));
                response.setLateNums(Integer.valueOf(lateNums));
                response.setNormalNums(Integer.valueOf(normalNums));
                response.setWorkLackNums(Integer.valueOf(dayLackNums));
                response.setRestLackNums(Integer.valueOf(restLackNums + workLackNums));
                response.setTotalWorkTime(totalWorkTime2);
                earlyNums = 0;
                lateNums = 0;
                normalNums = 0;
                workLackNums = 0;
                restLackNums = 0;
                totalWorkTime = 0L;
                dayLackNums = 0;
            }
        }
    }

    private AttendanceRecord requestToRrcord(SaveAttendanceRecordRequest request, Integer groupType) throws BeansException {
        AttendanceGroupTime groupTime;
        AttendanceRecord record;
        AttendanceRecord record2 = new AttendanceRecord();
        BeanUtils.copyProperties(request, record2);
        record2.setRecordStatus(Integer.valueOf(AttendanceStatusTypeEnum.ATTENDANCE_BY_SELF.getValue()));
        Date date = MyDateUtils.getDate(request.getAttendanceTime().longValue());
        if (AttendanceGroupTypeEnum.MANUAL_TYPE.getValue() == groupType.intValue()) {
            groupTime = this.attendanceGroupTimeMapper.getOneByAttendanceGroupIdAndDate(request.getAttendanceGroupId(), request.getOrgId(), date);
        } else {
            groupTime = this.attendanceGroupTimeMapper.getOneByAttendanceGroupIdAndDate(request.getAttendanceGroupId(), request.getOrgId(), null);
        }
        if (groupTime == null) {
            return null;
        }
        if (AttendanceGroupTypeEnum.FREE_TYPE.getValue() == groupType.intValue()) {
            record = doSaveFreeAttendance(request.getAttendanceGroupId(), request.getOrgId(), request.getMemberId(), record2, request.getAttendanceTime(), groupTime);
        } else {
            record = doSaveAttendanceRecord(request.getAttendanceGroupId(), request.getOrgId(), request.getMemberId(), record2, request.getAttendanceTime(), groupTime);
        }
        return record;
    }

    private AttendanceRecord doSaveAttendanceRecord(Long attendanceGroupId, Long orgId, Long memberId, AttendanceRecord record, Long attendanceTime, AttendanceGroupTime groupTime) {
        AttendanceTimeCheckInfo info = new AttendanceTimeCheckInfo(groupTime, attendanceTime);
        if (!info.isWork() && !info.isRest()) {
            return null;
        }
        AssertUtil.isTrue(Boolean.valueOf(info.getRestRuleTime() != null && info.getRestRuleTime().longValue() > 0), OnpremiseErrorEnum.SAVE_ATTENDANCE_RECORD_ERROR);
        AssertUtil.isTrue(Boolean.valueOf(info.getWorkRuleTime() != null && info.getWorkRuleTime().longValue() > 0), OnpremiseErrorEnum.SAVE_ATTENDANCE_RECORD_ERROR);
        AttendanceRecord firstWorkRecord = this.attendanceRecordMapper.getTodayWorkAttendance(info.getEarliestWorkRuleTime(), info.getLatestWorkRuleTime(), memberId, orgId, attendanceGroupId, Integer.valueOf(AttendanceRecordTypeEnum.BEGAN_WORK.getValue()));
        int attendanceResult = AttendanceRecordResultEnum.INVALID.getValue();
        if (info.isWork()) {
            if (firstWorkRecord != null && firstWorkRecord.getAttendanceTime().longValue() <= attendanceTime.longValue()) {
                logger.info("member already attendance work record");
                return null;
            }
            if (firstWorkRecord != null) {
                this.attendanceRecordMapper.deleteById(firstWorkRecord.getAttendanceRecordId());
            }
            record.setAttendanceDay(MyDateUtils.formatIntegerDay(info.getWorkRuleTime()));
            record.setRuleTime(info.getWorkRuleTime());
            record.setRecordType(Integer.valueOf(AttendanceRecordTypeEnum.BEGAN_WORK.getValue()));
            attendanceResult = info.getAttendanceWorkResult();
        } else if (info.isRest()) {
            record.setAttendanceDay(MyDateUtils.formatIntegerDay(info.getWorkRuleTime()));
            record.setRuleTime(info.getRestRuleTime());
            record.setRecordType(Integer.valueOf(AttendanceRecordTypeEnum.END_WORK.getValue()));
            attendanceResult = info.getAttendanceRestResult();
        }
        if (AttendanceRecordResultEnum.INVALID.getValue() == attendanceResult) {
            return null;
        }
        record.setAttendanceResult(Integer.valueOf(attendanceResult));
        if (AttendanceRecordTypeEnum.END_WORK.getValue() == record.getRecordType().intValue()) {
            AttendanceRecord todayRestRecord = this.attendanceRecordMapper.getTodayWorkAttendance(info.getEarliestRestRuleTime(), info.getLatestRestRuleTime(), memberId, orgId, attendanceGroupId, Integer.valueOf(AttendanceRecordTypeEnum.END_WORK.getValue()));
            if (todayRestRecord != null) {
                AssertUtil.isTrue(Boolean.valueOf(this.attendanceRecordMapper.deleteById(todayRestRecord.getAttendanceRecordId()) > 0), OnpremiseErrorEnum.SAVE_ATTENDANCE_RECORD_ERROR);
            }
            if (firstWorkRecord == null) {
                record.setWorkTime(0L);
            } else {
                record.setWorkTime(Long.valueOf(attendanceTime.longValue() - firstWorkRecord.getAttendanceTime().longValue()));
            }
        } else {
            record.setWorkTime(0L);
        }
        return record;
    }

    private AttendanceRecord doSaveFreeAttendance(Long attendanceGroupId, Long orgId, Long memberId, AttendanceRecord record, Long attendanceTime, AttendanceGroupTime groupTime) {
        Long beginTime = Long.valueOf(MyDateUtils.parseDate(MyDateUtils.formatDate(MyDateUtils.getDate(attendanceTime.longValue()), "yyyy-MM-dd") + SymbolConstants.SPACE_SYMBOL + groupTime.getAttendanceBeginTime() + ":" + TarConstants.VERSION_POSIX, "yyyy-MM-dd HH:mm:ss").getTime());
        Long endTime = Long.valueOf(MyDateUtils.parseDate(MyDateUtils.formatDate(MyDateUtils.getDate(attendanceTime.longValue()), "yyyy-MM-dd") + SymbolConstants.SPACE_SYMBOL + groupTime.getAttendanceEndTime() + ":59", "yyyy-MM-dd HH:mm:ss").getTime());
        AttendanceRecord firstWorkRecord = this.attendanceRecordMapper.getTodayWorkAttendance(beginTime, endTime, memberId, orgId, attendanceGroupId, Integer.valueOf(AttendanceRecordTypeEnum.BEGAN_WORK.getValue()));
        int attendanceResult = AttendanceRecordResultEnum.INVALID.getValue();
        if (attendanceTime.longValue() >= beginTime.longValue() && attendanceTime.longValue() <= endTime.longValue()) {
            if (firstWorkRecord == null) {
                record.setAttendanceDay(MyDateUtils.formatIntegerDay(record.getAttendanceTime()));
                record.setRuleTime(beginTime);
                record.setRecordType(Integer.valueOf(AttendanceRecordTypeEnum.BEGAN_WORK.getValue()));
                attendanceResult = AttendanceRecordResultEnum.SUCCESS.getValue();
            } else {
                record.setAttendanceDay(MyDateUtils.formatIntegerDay(record.getAttendanceTime()));
                record.setRuleTime(endTime);
                record.setRecordType(Integer.valueOf(AttendanceRecordTypeEnum.END_WORK.getValue()));
                attendanceResult = AttendanceRecordResultEnum.SUCCESS.getValue();
            }
        }
        if (AttendanceRecordResultEnum.INVALID.getValue() == attendanceResult) {
            return null;
        }
        record.setAttendanceResult(Integer.valueOf(attendanceResult));
        if (AttendanceRecordTypeEnum.END_WORK.getValue() == record.getRecordType().intValue()) {
            AttendanceRecord todayRestRecord = this.attendanceRecordMapper.getTodayWorkAttendance(beginTime, endTime, memberId, orgId, attendanceGroupId, Integer.valueOf(AttendanceRecordTypeEnum.END_WORK.getValue()));
            if (todayRestRecord != null) {
                AssertUtil.isTrue(Boolean.valueOf(this.attendanceRecordMapper.deleteById(todayRestRecord.getAttendanceRecordId()) > 0), OnpremiseErrorEnum.SAVE_ATTENDANCE_RECORD_ERROR);
            }
            if (firstWorkRecord != null) {
                record.setWorkTime(Long.valueOf(attendanceTime.longValue() - firstWorkRecord.getAttendanceTime().longValue()));
            }
        } else {
            record.setWorkTime(0L);
        }
        return record;
    }

    private boolean passAttendanceEvent(Long orgId, Long memberId, Long attendanceTime) {
        boolean result = true;
        List<AttendanceEventDetailResponse> memberEvent = this.attendanceEventService.getAttendanceEventByMemberId(orgId, memberId);
        if (MyListUtils.listIsEmpty(memberEvent)) {
            for (AttendanceEventDetailResponse response : memberEvent) {
                AttendanceEventTypeEnum eventTypeEnum = AttendanceEventTypeEnum.getByValue(response.getEventType().intValue());
                switch (eventTypeEnum) {
                    case LEAVE:
                        AttendanceEventLeave attendanceEventLeave = this.attendanceEventLeaveMapper.findByEventIdAndMemberId(response.getOrgId(), response.getAttendanceEventId(), response.getMemberId());
                        String endTimeStr = getEndTime(attendanceEventLeave.getEndTime(), attendanceEventLeave.getEndFrame().intValue());
                        String startTimeStr = getStartTime(attendanceEventLeave.getStartTime(), attendanceEventLeave.getStartFrame().intValue());
                        result = checkTime(startTimeStr, endTimeStr, attendanceTime);
                        break;
                    case SUPPLEMENT:
                        this.attendanceEventSupplementMapper.findByEventIdAndMemberId(response.getOrgId(), response.getAttendanceEventId(), response.getMemberId());
                        break;
                    case OVERTIME:
                        AttendanceEventOvertime attendanceEventOvertime = this.attendanceEventOvertimeMapper.findByEventIdAndMemberId(response.getOrgId(), response.getAttendanceEventId(), response.getMemberId());
                        String endTimeStr2 = getEndTime(attendanceEventOvertime.getEndTime(), attendanceEventOvertime.getEndFrame().intValue());
                        String startTimeStr2 = getStartTime(attendanceEventOvertime.getStartTime(), attendanceEventOvertime.getStartFrame().intValue());
                        result = checkTime(startTimeStr2, endTimeStr2, attendanceTime);
                        break;
                    case BUSINESS_OUT:
                        AttendanceEventOut attendanceEventOut = this.attendanceEventOutMapper.findByEventIdAndMemberId(response.getOrgId(), response.getAttendanceEventId(), response.getMemberId());
                        String endTimeStr3 = getEndTime(attendanceEventOut.getEndTime(), attendanceEventOut.getEndFrame().intValue());
                        String startTimeStr3 = getStartTime(attendanceEventOut.getStartTime(), attendanceEventOut.getStartFrame().intValue());
                        result = checkTime(startTimeStr3, endTimeStr3, attendanceTime);
                        break;
                    case BUSINESS_TRIP:
                        AttendanceEventTrip attendanceEventTrip = this.attendanceEventTripMapper.findByEventIdAndMemberId(response.getOrgId(), response.getAttendanceEventId(), response.getMemberId());
                        String endTimeStr4 = getEndTime(attendanceEventTrip.getEndTime(), attendanceEventTrip.getEndFrame().intValue());
                        String startTimeStr4 = getStartTime(attendanceEventTrip.getStartTime(), attendanceEventTrip.getStartFrame().intValue());
                        result = checkTime(startTimeStr4, endTimeStr4, attendanceTime);
                        break;
                }
                if (!result) {
                }
            }
        }
        return result;
    }

    private String getStartTime(String startTime, int startFrame) {
        if (AttendanceEventFrameEnum.AM.getValue() == startFrame) {
            startTime = startTime + MyDateUtils.TIME_OF_DAY_BEGIN;
        }
        if (AttendanceEventFrameEnum.PM.getValue() == startFrame) {
            startTime = startTime + " 12:00:00";
        }
        return startTime;
    }

    private String getEndTime(String endTime, int endFrame) {
        if (AttendanceEventFrameEnum.AM.getValue() == endFrame) {
            endTime = endTime + " 11:59:59";
        }
        if (AttendanceEventFrameEnum.PM.getValue() == endFrame) {
            endTime = endTime + MyDateUtils.TIME_OF_DAY_END;
        }
        return endTime;
    }

    private boolean checkTime(String startTimeStr, String endTimeStr, Long attendanceTime) {
        boolean result = true;
        Date startTime = MyDateUtils.parseDate(startTimeStr, "yyyy-MM-dd HH:mm:ss");
        Date endTime = MyDateUtils.parseDate(endTimeStr, "yyyy-MM-dd HH:mm:ss");
        if (startTime == null || endTime == null) {
            result = false;
        } else if (startTime.getTime() < attendanceTime.longValue() && attendanceTime.longValue() < endTime.getTime()) {
            result = false;
        }
        return result;
    }

    @Override // com.moredian.onpremise.api.attendance.AttendanceRecordService
    public String statisticsForMonthExport(AttendanceStatisticsForMonthExportRequest request) {
        if (request.getDeptId() == null) {
            request.setDeptId(Long.valueOf(NumberUtils.toLong(CacheAdapter.getLoginInfo(request.getSessionId()).getManageDeptId().split(",")[0])));
        }
        List<Long> deptsMatch = this.deptService.packagingChildDept(request.getDeptId(), request.getOrgId(), new ArrayList<>());
        deptsMatch.add(request.getDeptId());
        Set<Long> deptMemberSet = new HashSet<>();
        this.memberService.getMemberListByDept(request.getOrgId(), request.getDeptId(), deptMemberSet);
        Collection<? extends Long> arrayList = new ArrayList<>(deptMemberSet);
        Set<Long> attendanceMemberSet = new HashSet<>();
        List<GroupMemberDto> memberDtoList = this.attendanceGroupMemberMapper.listByAttendanceGroupIdAndType(Integer.valueOf(MemberTypeEnum.MEMBER.getValue()), request.getOrgId(), request.getAttendanceGroupId());
        for (GroupMemberDto memberDto : memberDtoList) {
            attendanceMemberSet.add(memberDto.getMemberId());
        }
        List<GroupMemberDto> deptDtoList = this.attendanceGroupMemberMapper.listByAttendanceGroupIdAndType(Integer.valueOf(MemberTypeEnum.DEPT.getValue()), request.getOrgId(), request.getAttendanceGroupId());
        for (GroupMemberDto memberDto2 : deptDtoList) {
            this.memberService.getMemberListByDept(request.getOrgId(), memberDto2.getDeptId(), attendanceMemberSet);
        }
        Collection<?> attendanceMemberList = new ArrayList<>(attendanceMemberSet);
        List<Long> memberIdList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(arrayList)) {
            memberIdList.addAll(arrayList);
        }
        if (!CollectionUtils.isEmpty(attendanceMemberList)) {
            memberIdList.retainAll(attendanceMemberList);
        }
        if (CollectionUtils.isEmpty(memberIdList)) {
            memberIdList.add(-1L);
        }
        request.setMemberIds(memberIdList);
        List<Member> memberList = this.memberMapper.listMember(request.getOrgId(), request.getMemberIds(), request.getKeywords());
        for (int i = memberList.size() - 1; i >= 0; i--) {
            boolean holdFlag = false;
            Iterator<Long> it = deptsMatch.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Long eachMatch = it.next();
                if (memberList.get(i).getDeptId().contains(eachMatch.toString())) {
                    holdFlag = true;
                    break;
                }
            }
            if (!holdFlag) {
                memberList.remove(i);
            }
        }
        Date startDay = MyDateUtils.parseDate(request.getStartDay(), "yyyyMMdd");
        Date endDay = MyDateUtils.parseDate(request.getEndDay(), "yyyyMMdd");
        List<String> headList = new ArrayList<>();
        headList.addAll(Arrays.asList("序号", "姓名", MyDateUtils.getMonth(startDay) + "月"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDay);
        String[] weekArr = {"", "日", "一", "二", "三", "四", "五", "六"};
        List<String> weekList = new ArrayList<>();
        while (calendar.getTime().before(endDay)) {
            weekList.add(MyDateUtils.formatDate(calendar.getTime(), "dd") + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR + weekArr[calendar.get(7)]);
            calendar.add(5, 1);
        }
        headList.addAll(weekList);
        headList.addAll(Arrays.asList("出勤\n✔\n天", "迟到\n▲\n次", "早退\n▼\n次", "缺卡\n◯\n次", "旷工\n⛝\n天", "出差\n✈\n天", "事假\n✱\n天", "病假\n✚\n天", "其他\n㊡\n天"));
        List<List<String>> statDataList = new LinkedList<>();
        packageListForMonthExport(memberList, statDataList, request, true);
        List<List<String>> detailList = new LinkedList<>();
        packageListForMonthExport(memberList, detailList, request, false);
        String fileName = this.saveExcelPath + request.getStartDay() + "-" + request.getEndDay() + "考勤报表.xlsx";
        List<List<String>> header = new LinkedList<>();
        for (String eachHead : headList) {
            header.add(Arrays.asList(eachHead));
        }
        ExcelWriter excelWriter = EasyExcel.write(fileName).build();
        WriteSheet writeStatSheet = EasyExcel.writerSheet("统计").build();
        writeStatSheet.setHead(header);
        Map<Integer, Integer> columnWidthMap = new HashMap<>();
        for (int i2 = 0; i2 < headList.size(); i2++) {
            columnWidthMap.put(Integer.valueOf(i2), 1300);
        }
        columnWidthMap.put(1, 2000);
        writeStatSheet.setColumnWidthMap(columnWidthMap);
        excelWriter.write(statDataList, writeStatSheet);
        WriteSheet writeDetailSheet = EasyExcel.writerSheet("详情").build();
        writeDetailSheet.setHead(header);
        excelWriter.write(detailList, writeDetailSheet);
        excelWriter.finish();
        return fileName.replace(this.saveFilePath, "/");
    }

    private void packageListForMonthExport(List<Member> memberList, List<List<String>> dataList, AttendanceStatisticsForMonthExportRequest request, boolean showIcon) {
        String description;
        String description2;
        for (int i = 0; i < memberList.size(); i++) {
            Date startDay = MyDateUtils.parseDate(request.getStartDay(), "yyyyMMdd");
            Date endDay = MyDateUtils.parseDate(request.getEndDay(), "yyyyMMdd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDay);
            request.setMemberId(memberList.get(i).getMemberId());
            List<ListAttendanceRecordResponse> attendanceRecordList = this.attendanceRecordMapper.listStatisticsForMonth(request);
            List<AttendanceEventDetailResponse> events = null;
            List<String> detailBeginWorkList = new ArrayList<>();
            detailBeginWorkList.add((i + 1) + "");
            detailBeginWorkList.add(memberList.get(i).getMemberName());
            List<String> detailEndWorkList = new ArrayList<>();
            detailEndWorkList.addAll(Arrays.asList("", ""));
            detailBeginWorkList.add(AttendanceRecordTypeEnum.BEGAN_WORK.getDescription());
            detailEndWorkList.add(AttendanceRecordTypeEnum.END_WORK.getDescription());
            int successCount = 0;
            int lateCount = 0;
            int leaveEarlyCount = 0;
            int lackCount = 0;
            int awayCount = 0;
            int tripCount = 0;
            int leaveCount = 0;
            int sickCount = 0;
            int otherCount = 0;
            while (calendar.getTime().before(endDay)) {
                String beginWorkIcon = StatisticsForMonthExportIconEnum.LACK.getDescription();
                String beginWorkTime = null;
                boolean beginWorkFlag = false;
                String endWorkIcon = StatisticsForMonthExportIconEnum.LACK.getDescription();
                String endWorkTime = null;
                boolean endWorkFlag = false;
                for (ListAttendanceRecordResponse listAttendanceRecordResponse : attendanceRecordList) {
                    if (listAttendanceRecordResponse.getRecordType().equals(Integer.valueOf(AttendanceRecordTypeEnum.BEGAN_WORK.getValue())) && listAttendanceRecordResponse.getAttendanceDay().intValue() == MyDateUtils.formatIntegerDay(calendar.getTime()).intValue()) {
                        beginWorkIcon = StatisticsForMonthExportIconEnum.getByCode(listAttendanceRecordResponse.getAttendanceResult().intValue()).getDescription();
                        beginWorkTime = MyDateUtils.getDateString(listAttendanceRecordResponse.getAttendanceTime().longValue(), "HH:mm");
                        beginWorkFlag = true;
                    }
                    if (listAttendanceRecordResponse.getRecordType().equals(Integer.valueOf(AttendanceRecordTypeEnum.END_WORK.getValue())) && listAttendanceRecordResponse.getAttendanceDay().intValue() == MyDateUtils.formatIntegerDay(calendar.getTime()).intValue()) {
                        endWorkIcon = StatisticsForMonthExportIconEnum.getByCode(listAttendanceRecordResponse.getAttendanceResult().intValue()).getDescription();
                        endWorkTime = MyDateUtils.getDateString(listAttendanceRecordResponse.getAttendanceTime().longValue(), "HH:mm");
                        endWorkFlag = true;
                    }
                }
                if (showIcon) {
                    events = events != null ? events : this.attendanceEventService.getAttendanceEventByMemberId(request.getOrgId(), memberList.get(i).getMemberId());
                    boolean eventFlag = false;
                    for (AttendanceEventDetailResponse event : events) {
                        AttendanceEventTypeEnum eventTypeEnum = AttendanceEventTypeEnum.getByValue(event.getEventType().intValue());
                        switch (eventTypeEnum) {
                            case LEAVE:
                                String startTimeStr = getStartTime(event.getLeave().getStartTime(), event.getLeave().getStartFrame().intValue());
                                String endTimeStr = getEndTime(event.getLeave().getEndTime(), event.getLeave().getEndFrame().intValue());
                                if (!checkTime(startTimeStr, endTimeStr, Long.valueOf(calendar.getTimeInMillis() + 32400000))) {
                                    eventFlag = true;
                                    if (event.getLeave().getType().intValue() == 2) {
                                        description2 = StatisticsForMonthExportIconEnum.SICK.getDescription();
                                    } else {
                                        description2 = event.getLeave().getType().intValue() == 3 ? StatisticsForMonthExportIconEnum.PERSONAL.getDescription() : StatisticsForMonthExportIconEnum.OTHER.getDescription();
                                    }
                                    beginWorkIcon = description2;
                                }
                                if (checkTime(startTimeStr, endTimeStr, Long.valueOf(calendar.getTimeInMillis() + 61200000))) {
                                    break;
                                } else {
                                    eventFlag = true;
                                    if (event.getLeave().getType().intValue() == 2) {
                                        description = StatisticsForMonthExportIconEnum.SICK.getDescription();
                                    } else {
                                        description = event.getLeave().getType().intValue() == 3 ? StatisticsForMonthExportIconEnum.PERSONAL.getDescription() : StatisticsForMonthExportIconEnum.OTHER.getDescription();
                                    }
                                    endWorkIcon = description;
                                    break;
                                }
                            case BUSINESS_OUT:
                                String startTimeStr2 = getStartTime(event.getBusinessOut().getStartTime(), event.getBusinessOut().getStartFrame().intValue());
                                String endTimeStr2 = getEndTime(event.getBusinessOut().getEndTime(), event.getBusinessOut().getEndFrame().intValue());
                                if (!checkTime(startTimeStr2, endTimeStr2, Long.valueOf(calendar.getTimeInMillis() + 32400000))) {
                                    eventFlag = true;
                                    beginWorkIcon = StatisticsForMonthExportIconEnum.TRIP.getDescription();
                                }
                                if (checkTime(startTimeStr2, endTimeStr2, Long.valueOf(calendar.getTimeInMillis() + 61200000))) {
                                    break;
                                } else {
                                    eventFlag = true;
                                    endWorkIcon = StatisticsForMonthExportIconEnum.TRIP.getDescription();
                                    break;
                                }
                            case BUSINESS_TRIP:
                                String startTimeStr3 = getStartTime(event.getBusinessTrip().getStartTime(), event.getBusinessTrip().getStartFrame().intValue());
                                String endTimeStr3 = getEndTime(event.getBusinessTrip().getEndTime(), event.getBusinessTrip().getEndFrame().intValue());
                                if (!checkTime(startTimeStr3, endTimeStr3, Long.valueOf(calendar.getTimeInMillis() + 32400000))) {
                                    eventFlag = true;
                                    beginWorkIcon = StatisticsForMonthExportIconEnum.TRIP.getDescription();
                                }
                                if (checkTime(startTimeStr3, endTimeStr3, Long.valueOf(calendar.getTimeInMillis() + 61200000))) {
                                    break;
                                } else {
                                    eventFlag = true;
                                    endWorkIcon = StatisticsForMonthExportIconEnum.TRIP.getDescription();
                                    break;
                                }
                        }
                    }
                    if (!beginWorkFlag && !endWorkFlag && !eventFlag) {
                        awayCount++;
                        beginWorkIcon = StatisticsForMonthExportIconEnum.ABSENCE.getDescription();
                        endWorkIcon = StatisticsForMonthExportIconEnum.ABSENCE.getDescription();
                    }
                    detailBeginWorkList.add(beginWorkIcon);
                    detailEndWorkList.add(endWorkIcon);
                } else {
                    detailBeginWorkList.add(beginWorkTime);
                    detailEndWorkList.add(endWorkTime);
                }
                if (beginWorkIcon.equals(StatisticsForMonthExportIconEnum.LATE.getDescription())) {
                    lateCount++;
                } else if (beginWorkIcon.equals(StatisticsForMonthExportIconEnum.LACK.getDescription())) {
                    lackCount++;
                } else if (beginWorkIcon.equals(StatisticsForMonthExportIconEnum.TRIP.getDescription())) {
                    tripCount++;
                } else if (beginWorkIcon.equals(StatisticsForMonthExportIconEnum.OTHER.getDescription())) {
                    otherCount++;
                } else if (beginWorkIcon.equals(StatisticsForMonthExportIconEnum.SICK.getDescription())) {
                    sickCount++;
                } else if (beginWorkIcon.equals(StatisticsForMonthExportIconEnum.PERSONAL.getDescription())) {
                    leaveCount++;
                }
                if (endWorkIcon.equals(StatisticsForMonthExportIconEnum.LEAVE_EARLY.getDescription())) {
                    leaveEarlyCount++;
                } else if (endWorkIcon.equals(StatisticsForMonthExportIconEnum.LACK.getDescription())) {
                    lackCount++;
                } else if (endWorkIcon.equals(StatisticsForMonthExportIconEnum.TRIP.getDescription())) {
                    tripCount++;
                } else if (endWorkIcon.equals(StatisticsForMonthExportIconEnum.OTHER.getDescription())) {
                    otherCount++;
                } else if (endWorkIcon.equals(StatisticsForMonthExportIconEnum.SICK.getDescription())) {
                    sickCount++;
                } else if (endWorkIcon.equals(StatisticsForMonthExportIconEnum.PERSONAL.getDescription())) {
                    leaveCount++;
                }
                if (StatisticsForMonthExportIconEnum.getByDescription(beginWorkIcon) == StatisticsForMonthExportIconEnum.SUCCESS || StatisticsForMonthExportIconEnum.getByDescription(beginWorkIcon) == StatisticsForMonthExportIconEnum.LATE || StatisticsForMonthExportIconEnum.getByDescription(beginWorkIcon) == StatisticsForMonthExportIconEnum.TRIP || StatisticsForMonthExportIconEnum.getByDescription(endWorkIcon) == StatisticsForMonthExportIconEnum.SUCCESS || StatisticsForMonthExportIconEnum.getByDescription(endWorkIcon) == StatisticsForMonthExportIconEnum.LEAVE_EARLY || StatisticsForMonthExportIconEnum.getByDescription(endWorkIcon) == StatisticsForMonthExportIconEnum.TRIP) {
                    successCount++;
                }
                calendar.add(5, 1);
            }
            if (showIcon) {
                detailBeginWorkList.add(successCount + "");
                detailBeginWorkList.add(lateCount + "");
                detailBeginWorkList.add(leaveEarlyCount + "");
                detailBeginWorkList.add(lackCount + "");
                detailBeginWorkList.add(awayCount + "");
                NumberFormat nf = NumberFormat.getInstance();
                detailBeginWorkList.add(nf.format(tripCount / 2.0d));
                detailBeginWorkList.add(nf.format(leaveCount / 2.0d));
                detailBeginWorkList.add(nf.format(sickCount / 2.0d));
                detailBeginWorkList.add(nf.format(otherCount / 2.0d));
                detailEndWorkList.addAll(new ArrayList<String>(detailBeginWorkList.size() - detailEndWorkList.size()) { // from class: com.moredian.onpremise.attendance.service.impl.AttendanceRecordServiceImpl.1
                    {
                        add("");
                    }
                });
            }
            dataList.add(detailBeginWorkList);
            dataList.add(detailEndWorkList);
        }
    }
}
