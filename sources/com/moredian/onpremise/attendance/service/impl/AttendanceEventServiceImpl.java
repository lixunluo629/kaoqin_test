package com.moredian.onpremise.attendance.service.impl;

import com.github.pagehelper.PageHelper;
import com.moredian.onpremise.api.attendance.AttendanceEventService;
import com.moredian.onpremise.api.member.DeptService;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import com.moredian.onpremise.core.common.enums.AttendanceEventTypeEnum;
import com.moredian.onpremise.core.common.enums.AttendanceGroupTypeEnum;
import com.moredian.onpremise.core.common.enums.AttendanceRecordResultEnum;
import com.moredian.onpremise.core.common.enums.AttendanceRecordTypeEnum;
import com.moredian.onpremise.core.common.enums.AttendanceStatusTypeEnum;
import com.moredian.onpremise.core.common.enums.AttendanceSupplementTypeEnum;
import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.exception.BizException;
import com.moredian.onpremise.core.mapper.AttendanceEventLeaveMapper;
import com.moredian.onpremise.core.mapper.AttendanceEventMapper;
import com.moredian.onpremise.core.mapper.AttendanceEventOutMapper;
import com.moredian.onpremise.core.mapper.AttendanceEventOvertimeMapper;
import com.moredian.onpremise.core.mapper.AttendanceEventSupplementMapper;
import com.moredian.onpremise.core.mapper.AttendanceEventTripMapper;
import com.moredian.onpremise.core.mapper.AttendanceGroupMemberMapper;
import com.moredian.onpremise.core.mapper.AttendanceGroupTimeMapper;
import com.moredian.onpremise.core.mapper.AttendanceRecordMapper;
import com.moredian.onpremise.core.mapper.MemberMapper;
import com.moredian.onpremise.core.model.domain.AttendanceEvent;
import com.moredian.onpremise.core.model.domain.AttendanceEventLeave;
import com.moredian.onpremise.core.model.domain.AttendanceEventOut;
import com.moredian.onpremise.core.model.domain.AttendanceEventOvertime;
import com.moredian.onpremise.core.model.domain.AttendanceEventSupplement;
import com.moredian.onpremise.core.model.domain.AttendanceEventTrip;
import com.moredian.onpremise.core.model.domain.AttendanceGroupTime;
import com.moredian.onpremise.core.model.domain.AttendanceRecord;
import com.moredian.onpremise.core.model.domain.Member;
import com.moredian.onpremise.core.model.dto.AttendanceEventBusinessOutDto;
import com.moredian.onpremise.core.model.dto.AttendanceEventBusinessTripDto;
import com.moredian.onpremise.core.model.dto.AttendanceEventLeaveDto;
import com.moredian.onpremise.core.model.dto.AttendanceEventMemberDto;
import com.moredian.onpremise.core.model.dto.AttendanceEventOvertimeDto;
import com.moredian.onpremise.core.model.dto.AttendanceEventSupplementDto;
import com.moredian.onpremise.core.model.info.AttendanceGroupMemberInfo;
import com.moredian.onpremise.core.model.info.AttendanceTimeCheckInfo;
import com.moredian.onpremise.core.model.request.AttendanceEventListRequest;
import com.moredian.onpremise.core.model.request.DeleteAttendanceEventRequest;
import com.moredian.onpremise.core.model.request.QueryAttendanceEventRequest;
import com.moredian.onpremise.core.model.request.SaveAttendanceEventRequest;
import com.moredian.onpremise.core.model.request.UpdateAttendanceEventRequest;
import com.moredian.onpremise.core.model.response.AttendanceEventDetailResponse;
import com.moredian.onpremise.core.model.response.AttendanceEventListResponse;
import com.moredian.onpremise.core.utils.AssertUtil;
import com.moredian.onpremise.core.utils.JsonUtils;
import com.moredian.onpremise.core.utils.MyDateUtils;
import com.moredian.onpremise.core.utils.MyListUtils;
import com.moredian.onpremise.core.utils.PageList;
import com.moredian.onpremise.core.utils.Paginator;
import java.util.Date;
import java.util.List;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
/* loaded from: onpremise-attendance-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/attendance/service/impl/AttendanceEventServiceImpl.class */
public class AttendanceEventServiceImpl implements AttendanceEventService {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) AttendanceEventServiceImpl.class);

    @Autowired
    private AttendanceEventMapper attendanceEventMapper;

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
    private AttendanceRecordMapper attendanceRecordMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private AttendanceGroupMemberMapper attendanceGroupMemberMapper;

    @Autowired
    private AttendanceGroupTimeMapper attendanceGroupTimeMapper;

    @Autowired
    private DeptService deptService;

    @Override // com.moredian.onpremise.api.attendance.AttendanceEventService
    @Transactional(rollbackFor = {Exception.class})
    public boolean insertAttendanceEvent(SaveAttendanceEventRequest request) throws BeansException {
        checkSaveAttendanceEvent(request);
        AttendanceEvent attendanceEvent = new AttendanceEvent();
        attendanceEvent.setOrgId(request.getOrgId());
        attendanceEvent.setEventType(request.getEventType());
        AssertUtil.isTrue(Boolean.valueOf(this.attendanceEventMapper.insertOne(attendanceEvent) > 0), OnpremiseErrorEnum.SAVE_ATTENDANCE_EVENT_FAIL);
        for (AttendanceEventMemberDto memberDto : request.getMembers()) {
            AssertUtil.checkId(memberDto.getMemberId(), OnpremiseErrorEnum.MEMBER_ID_MUST_NOT_NULL);
            insertAttendanceEventType(request, attendanceEvent.getAttendanceEventId(), memberDto.getMemberId());
        }
        return true;
    }

    @Override // com.moredian.onpremise.api.attendance.AttendanceEventService
    @Transactional(rollbackFor = {RuntimeException.class})
    public boolean deleteAttendanceEvent(DeleteAttendanceEventRequest request) {
        AssertUtil.checkOrgId(request.getOrgId());
        AssertUtil.checkId(request.getAttendanceEventId(), OnpremiseErrorEnum.ATTENDANCE_EVENT_ID_LACK);
        AssertUtil.checkId(request.getMemberId(), OnpremiseErrorEnum.MEMBER_ID_MUST_NOT_NULL);
        AttendanceEvent attendanceEventOld = this.attendanceEventMapper.findById(request.getOrgId(), request.getAttendanceEventId());
        AssertUtil.isNullOrEmpty(attendanceEventOld, OnpremiseErrorEnum.ATTENDANCE_EVENT_NOT_EXITS);
        AttendanceEventTypeEnum eventTypeEnum = AttendanceEventTypeEnum.getByValue(attendanceEventOld.getEventType().intValue());
        AssertUtil.isNullOrEmpty(eventTypeEnum, OnpremiseErrorEnum.ATTENDANCE_EVENT_TYPE_LACK);
        softDeleteEventType(request.getOrgId(), request.getAttendanceEventId(), request.getMemberId(), eventTypeEnum);
        return true;
    }

    @Override // com.moredian.onpremise.api.attendance.AttendanceEventService
    @Transactional(rollbackFor = {Exception.class})
    public boolean updateAttendanceEvent(UpdateAttendanceEventRequest request) throws BeansException {
        AssertUtil.checkOrgId(request.getOrgId());
        AssertUtil.checkId(request.getAttendanceEventId(), OnpremiseErrorEnum.ATTENDANCE_EVENT_ID_LACK);
        AssertUtil.checkId(request.getMemberId(), OnpremiseErrorEnum.ATTENDANCE_EVENT_MEMBER_LACK);
        AssertUtil.isNullOrEmpty(request.getEventType(), OnpremiseErrorEnum.ATTENDANCE_EVENT_TYPE_LACK);
        AttendanceEvent attendanceEventOld = this.attendanceEventMapper.findById(request.getOrgId(), request.getAttendanceEventId());
        AssertUtil.isNullOrEmpty(attendanceEventOld, OnpremiseErrorEnum.ATTENDANCE_EVENT_NOT_EXITS);
        AssertUtil.isTrue(Boolean.valueOf(attendanceEventOld.getEventType().intValue() == request.getEventType().intValue()), OnpremiseErrorEnum.ATTENDANCE_EVENT_TYPE_NOT_ALLOW_MODIFY);
        AttendanceEventTypeEnum attendanceEventTypeEnumOld = AttendanceEventTypeEnum.getByValue(attendanceEventOld.getEventType().intValue());
        AssertUtil.isNullOrEmpty(Boolean.valueOf(attendanceEventTypeEnumOld == null), OnpremiseErrorEnum.ATTENDANCE_EVENT_TYPE_LACK);
        updateAttendanceEventType(request, request.getAttendanceEventId(), request.getMemberId());
        return true;
    }

    @Override // com.moredian.onpremise.api.attendance.AttendanceEventService
    public AttendanceEventDetailResponse findAttendanceEventById(QueryAttendanceEventRequest request) throws BeansException {
        AttendanceEventDetailResponse response = this.attendanceEventMapper.findByIdAndMemberId(request.getOrgId(), request.getAttendanceEventId(), request.getMemberId());
        if (response == null) {
            return new AttendanceEventDetailResponse();
        }
        dealAttendanceType(response);
        return response;
    }

    @Override // com.moredian.onpremise.api.attendance.AttendanceEventService
    public PageList<AttendanceEventListResponse> pageFindAttendanceEvent(AttendanceEventListRequest request) {
        if (request.getStartTimeStr() != null && MyDateUtils.parseDate(request.getStartTimeStr(), "yyyy-MM-dd") != null) {
            request.setStartTimeStr(request.getStartTimeStr() + MyDateUtils.TIME_OF_DAY_BEGIN);
        }
        if (request.getStartTimeStr() != null && MyDateUtils.parseDate(request.getEndTimeStr(), "yyyy-MM-dd") != null) {
            request.setEndTimeStr(request.getEndTimeStr() + MyDateUtils.TIME_OF_DAY_END);
        }
        Paginator paginator = request.getPaginator();
        if (Paginator.checkPaginator(paginator)) {
            PageHelper.startPage(paginator.getPageNum(), paginator.getPageSize());
            return new PageList<>(this.attendanceEventMapper.pageFind(request));
        }
        List<AttendanceEventListResponse> responses = this.attendanceEventMapper.pageFind(request);
        return new PageList<>(Paginator.initPaginator(responses), responses);
    }

    @Override // com.moredian.onpremise.api.attendance.AttendanceEventService
    public List<AttendanceEventDetailResponse> getAttendanceEventByMemberId(Long orgId, Long memberId) throws BeansException {
        List<AttendanceEventDetailResponse> attendanceEventList = this.attendanceEventMapper.getByMemberId(orgId, memberId);
        if (MyListUtils.listIsEmpty(attendanceEventList)) {
            for (AttendanceEventDetailResponse response : attendanceEventList) {
                dealAttendanceType(response);
            }
        }
        return attendanceEventList;
    }

    private void checkSaveAttendanceEvent(SaveAttendanceEventRequest request) {
        AssertUtil.checkOrgId(request.getOrgId());
        AssertUtil.isTrue(Boolean.valueOf(MyListUtils.listIsEmpty(request.getMembers())), OnpremiseErrorEnum.ATTENDANCE_EVENT_MEMBER_LACK);
        AssertUtil.isNullOrEmpty(request.getEventType(), OnpremiseErrorEnum.ATTENDANCE_EVENT_TYPE_LACK);
    }

    private void insertAttendanceEventType(SaveAttendanceEventRequest request, Long eventId, Long memberId) throws BeansException {
        AttendanceEventTypeEnum attendanceEventTypeEnum = AttendanceEventTypeEnum.getByValue(request.getEventType().intValue());
        AssertUtil.isNullOrEmpty(attendanceEventTypeEnum, OnpremiseErrorEnum.ATTENDANCE_EVENT_TYPE_LACK);
        switch (attendanceEventTypeEnum) {
            case LEAVE:
                AttendanceEventLeaveDto attendanceEventLeaveDto = request.getLeave();
                AssertUtil.isNullOrEmpty(attendanceEventLeaveDto.getStartTime(), OnpremiseErrorEnum.ATTENDANCE_EVENT_START_TIME_LACK);
                AssertUtil.isNullOrEmpty(attendanceEventLeaveDto.getEndTime(), OnpremiseErrorEnum.ATTENDANCE_EVENT_END_TIME_LACK);
                AttendanceEventLeave attendanceEventLeave = new AttendanceEventLeave();
                BeanUtils.copyProperties(attendanceEventLeaveDto, attendanceEventLeave);
                attendanceEventLeave.setOrgId(request.getOrgId());
                attendanceEventLeave.setEventId(eventId);
                attendanceEventLeave.setMemberId(memberId);
                attendanceEventLeave.setLeaveType(attendanceEventLeaveDto.getType());
                this.attendanceEventLeaveMapper.insertOne(attendanceEventLeave);
                return;
            case BUSINESS_TRIP:
                AttendanceEventBusinessTripDto attendanceEventBusinessTripDtoDto = request.getBusinessTrip();
                AssertUtil.isNullOrEmpty(attendanceEventBusinessTripDtoDto.getStartTime(), OnpremiseErrorEnum.ATTENDANCE_EVENT_START_TIME_LACK);
                AssertUtil.isNullOrEmpty(attendanceEventBusinessTripDtoDto.getEndTime(), OnpremiseErrorEnum.ATTENDANCE_EVENT_END_TIME_LACK);
                AttendanceEventTrip attendanceEventTrip = new AttendanceEventTrip();
                BeanUtils.copyProperties(attendanceEventBusinessTripDtoDto, attendanceEventTrip);
                attendanceEventTrip.setOrgId(request.getOrgId());
                attendanceEventTrip.setEventId(eventId);
                attendanceEventTrip.setMemberId(memberId);
                this.attendanceEventTripMapper.insertOne(attendanceEventTrip);
                return;
            case BUSINESS_OUT:
                AttendanceEventBusinessOutDto attendanceEventBusinessOutDto = request.getBusinessOut();
                AssertUtil.isNullOrEmpty(attendanceEventBusinessOutDto.getStartTime(), OnpremiseErrorEnum.ATTENDANCE_EVENT_START_TIME_LACK);
                AssertUtil.isNullOrEmpty(attendanceEventBusinessOutDto.getEndTime(), OnpremiseErrorEnum.ATTENDANCE_EVENT_END_TIME_LACK);
                AttendanceEventOut attendanceEventOut = new AttendanceEventOut();
                BeanUtils.copyProperties(attendanceEventBusinessOutDto, attendanceEventOut);
                attendanceEventOut.setOrgId(request.getOrgId());
                attendanceEventOut.setEventId(eventId);
                attendanceEventOut.setMemberId(memberId);
                this.attendanceEventOutMapper.insertOne(attendanceEventOut);
                return;
            case OVERTIME:
                AttendanceEventOvertimeDto attendanceEventOvertimeDto = request.getOvertime();
                AssertUtil.isNullOrEmpty(attendanceEventOvertimeDto.getStartTime(), OnpremiseErrorEnum.ATTENDANCE_EVENT_START_TIME_LACK);
                AssertUtil.isNullOrEmpty(attendanceEventOvertimeDto.getEndTime(), OnpremiseErrorEnum.ATTENDANCE_EVENT_END_TIME_LACK);
                AttendanceEventOvertime attendanceEventOvertime = new AttendanceEventOvertime();
                BeanUtils.copyProperties(attendanceEventOvertimeDto, attendanceEventOvertime);
                attendanceEventOvertime.setOrgId(request.getOrgId());
                attendanceEventOvertime.setEventId(eventId);
                attendanceEventOvertime.setMemberId(memberId);
                this.attendanceEventOvertimeMapper.insertOne(attendanceEventOvertime);
                return;
            case SUPPLEMENT:
                AttendanceEventSupplementDto attendanceEventSupplementDto = request.getSupplement();
                AssertUtil.isNullOrEmpty(attendanceEventSupplementDto.getSupplementTime(), OnpremiseErrorEnum.ATTENDANCE_EVENT_SUPPLEMENT_TIME_LACK);
                AttendanceEventSupplement attendanceEventSupplement = new AttendanceEventSupplement();
                BeanUtils.copyProperties(attendanceEventSupplementDto, attendanceEventSupplement);
                attendanceEventSupplement.setOrgId(request.getOrgId());
                attendanceEventSupplement.setEventId(eventId);
                attendanceEventSupplement.setMemberId(memberId);
                attendanceEventSupplement.setSupplementType(attendanceEventSupplementDto.getType());
                this.attendanceEventSupplementMapper.insertOne(attendanceEventSupplement);
                doSupplement(attendanceEventSupplement);
                return;
            default:
                throw new BizException();
        }
    }

    private void updateAttendanceEventType(UpdateAttendanceEventRequest request, Long eventId, Long memberId) throws BeansException {
        AttendanceEventTypeEnum attendanceEventTypeEnum = AttendanceEventTypeEnum.getByValue(request.getEventType().intValue());
        AssertUtil.isNullOrEmpty(attendanceEventTypeEnum, OnpremiseErrorEnum.ATTENDANCE_EVENT_TYPE_LACK);
        switch (attendanceEventTypeEnum) {
            case LEAVE:
                AttendanceEventLeaveDto attendanceEventLeaveDto = request.getLeave();
                AttendanceEventLeave attendanceEventLeave = new AttendanceEventLeave();
                BeanUtils.copyProperties(attendanceEventLeaveDto, attendanceEventLeave);
                attendanceEventLeave.setOrgId(request.getOrgId());
                attendanceEventLeave.setEventId(eventId);
                attendanceEventLeave.setMemberId(memberId);
                attendanceEventLeave.setLeaveType(attendanceEventLeaveDto.getType());
                AssertUtil.isNullOrEmpty(attendanceEventLeaveDto.getStartTime(), OnpremiseErrorEnum.ATTENDANCE_EVENT_START_TIME_LACK);
                AssertUtil.isNullOrEmpty(attendanceEventLeaveDto.getEndTime(), OnpremiseErrorEnum.ATTENDANCE_EVENT_END_TIME_LACK);
                attendanceEventLeave.setStartTime(MyDateUtils.getDate(attendanceEventLeaveDto.getStartTime()));
                attendanceEventLeave.setEndTime(MyDateUtils.getDate(attendanceEventLeaveDto.getEndTime()));
                this.attendanceEventLeaveMapper.update(attendanceEventLeave);
                return;
            case BUSINESS_TRIP:
                AttendanceEventBusinessTripDto attendanceEventBusinessTripDtoDto = request.getBusinessTrip();
                AttendanceEventTrip attendanceEventTrip = new AttendanceEventTrip();
                BeanUtils.copyProperties(attendanceEventBusinessTripDtoDto, attendanceEventTrip);
                attendanceEventTrip.setOrgId(request.getOrgId());
                attendanceEventTrip.setEventId(eventId);
                attendanceEventTrip.setMemberId(memberId);
                AssertUtil.isNullOrEmpty(attendanceEventBusinessTripDtoDto.getStartTime(), OnpremiseErrorEnum.ATTENDANCE_EVENT_START_TIME_LACK);
                AssertUtil.isNullOrEmpty(attendanceEventBusinessTripDtoDto.getEndTime(), OnpremiseErrorEnum.ATTENDANCE_EVENT_END_TIME_LACK);
                attendanceEventTrip.setStartTime(MyDateUtils.getDate(attendanceEventBusinessTripDtoDto.getStartTime()));
                attendanceEventTrip.setEndTime(MyDateUtils.getDate(attendanceEventBusinessTripDtoDto.getEndTime()));
                this.attendanceEventTripMapper.update(attendanceEventTrip);
                return;
            case BUSINESS_OUT:
                AttendanceEventBusinessOutDto attendanceEventBusinessOutDto = request.getBusinessOut();
                AttendanceEventOut attendanceEventOut = new AttendanceEventOut();
                BeanUtils.copyProperties(attendanceEventBusinessOutDto, attendanceEventOut);
                attendanceEventOut.setOrgId(request.getOrgId());
                attendanceEventOut.setEventId(eventId);
                attendanceEventOut.setMemberId(memberId);
                AssertUtil.isNullOrEmpty(attendanceEventBusinessOutDto.getStartTime(), OnpremiseErrorEnum.ATTENDANCE_EVENT_START_TIME_LACK);
                AssertUtil.isNullOrEmpty(attendanceEventBusinessOutDto.getEndTime(), OnpremiseErrorEnum.ATTENDANCE_EVENT_END_TIME_LACK);
                attendanceEventOut.setStartTime(MyDateUtils.getDate(attendanceEventBusinessOutDto.getStartTime()));
                attendanceEventOut.setEndTime(MyDateUtils.getDate(attendanceEventBusinessOutDto.getEndTime()));
                this.attendanceEventOutMapper.update(attendanceEventOut);
                return;
            case OVERTIME:
                AttendanceEventOvertimeDto attendanceEventOvertimeDto = request.getOvertime();
                AttendanceEventOvertime attendanceEventOvertime = new AttendanceEventOvertime();
                BeanUtils.copyProperties(attendanceEventOvertimeDto, attendanceEventOvertime);
                attendanceEventOvertime.setOrgId(request.getOrgId());
                attendanceEventOvertime.setEventId(eventId);
                attendanceEventOvertime.setMemberId(memberId);
                AssertUtil.isNullOrEmpty(attendanceEventOvertimeDto.getStartTime(), OnpremiseErrorEnum.ATTENDANCE_EVENT_START_TIME_LACK);
                AssertUtil.isNullOrEmpty(attendanceEventOvertimeDto.getEndTime(), OnpremiseErrorEnum.ATTENDANCE_EVENT_END_TIME_LACK);
                attendanceEventOvertime.setStartTime(MyDateUtils.getDate(attendanceEventOvertimeDto.getStartTime()));
                attendanceEventOvertime.setEndTime(MyDateUtils.getDate(attendanceEventOvertimeDto.getEndTime()));
                this.attendanceEventOvertimeMapper.update(attendanceEventOvertime);
                return;
            case SUPPLEMENT:
                AttendanceEventSupplementDto attendanceEventSupplementDto = request.getSupplement();
                AttendanceEventSupplement attendanceEventSupplement = new AttendanceEventSupplement();
                BeanUtils.copyProperties(attendanceEventSupplementDto, attendanceEventSupplement);
                attendanceEventSupplement.setOrgId(request.getOrgId());
                attendanceEventSupplement.setEventId(eventId);
                attendanceEventSupplement.setMemberId(memberId);
                AssertUtil.isNullOrEmpty(attendanceEventSupplementDto.getSupplementTime(), OnpremiseErrorEnum.ATTENDANCE_EVENT_SUPPLEMENT_TIME_LACK);
                attendanceEventSupplement.setSupplementTime(MyDateUtils.getDate(attendanceEventSupplementDto.getSupplementTime()));
                attendanceEventSupplement.setSupplementType(attendanceEventSupplementDto.getType());
                this.attendanceEventSupplementMapper.update(attendanceEventSupplement);
                doSupplement(attendanceEventSupplement);
                return;
            default:
                throw new BizException();
        }
    }

    private void dealAttendanceType(AttendanceEventDetailResponse response) throws BeansException {
        AttendanceEventTypeEnum attendanceEventTypeEnum = AttendanceEventTypeEnum.getByValue(response.getEventType().intValue());
        if (attendanceEventTypeEnum == null) {
            throw new BizException();
        }
        switch (attendanceEventTypeEnum) {
            case LEAVE:
                AttendanceEventLeave attendanceEventLeave = this.attendanceEventLeaveMapper.findByEventIdAndMemberId(response.getOrgId(), response.getAttendanceEventId(), response.getMemberId());
                AttendanceEventLeaveDto attendanceEventLeaveDto = new AttendanceEventLeaveDto();
                BeanUtils.copyProperties(attendanceEventLeave, attendanceEventLeaveDto);
                attendanceEventLeaveDto.setType(attendanceEventLeave.getLeaveType());
                response.setLeave(attendanceEventLeaveDto);
                return;
            case BUSINESS_TRIP:
                AttendanceEventTrip attendanceEventTrip = this.attendanceEventTripMapper.findByEventIdAndMemberId(response.getOrgId(), response.getAttendanceEventId(), response.getMemberId());
                AttendanceEventBusinessTripDto attendanceEventBusinessTripDto = new AttendanceEventBusinessTripDto();
                BeanUtils.copyProperties(attendanceEventTrip, attendanceEventBusinessTripDto);
                response.setBusinessTrip(attendanceEventBusinessTripDto);
                return;
            case BUSINESS_OUT:
                AttendanceEventOut attendanceEventOut = this.attendanceEventOutMapper.findByEventIdAndMemberId(response.getOrgId(), response.getAttendanceEventId(), response.getMemberId());
                AttendanceEventBusinessOutDto attendanceEventBusinessOutDto = new AttendanceEventBusinessOutDto();
                BeanUtils.copyProperties(attendanceEventOut, attendanceEventBusinessOutDto);
                response.setBusinessOut(attendanceEventBusinessOutDto);
                return;
            case OVERTIME:
                AttendanceEventOvertime attendanceEventOvertime = this.attendanceEventOvertimeMapper.findByEventIdAndMemberId(response.getOrgId(), response.getAttendanceEventId(), response.getMemberId());
                AttendanceEventOvertimeDto attendanceEventOvertimeDto = new AttendanceEventOvertimeDto();
                BeanUtils.copyProperties(attendanceEventOvertime, attendanceEventOvertimeDto);
                response.setOvertime(attendanceEventOvertimeDto);
                return;
            case SUPPLEMENT:
                AttendanceEventSupplement attendanceEventSupplement = this.attendanceEventSupplementMapper.findByEventIdAndMemberId(response.getOrgId(), response.getAttendanceEventId(), response.getMemberId());
                AttendanceEventSupplementDto attendanceEventSupplementDto = new AttendanceEventSupplementDto();
                BeanUtils.copyProperties(attendanceEventSupplement, attendanceEventSupplementDto);
                attendanceEventSupplementDto.setType(attendanceEventSupplement.getSupplementType());
                response.setSupplement(attendanceEventSupplementDto);
                return;
            default:
                throw new BizException();
        }
    }

    private void softDeleteEventType(Long orgId, Long eventId, Long memberId, AttendanceEventTypeEnum attendanceEventTypeEnumOld) {
        switch (attendanceEventTypeEnumOld) {
            case LEAVE:
                this.attendanceEventLeaveMapper.softDeleteByEventIdAndMemberId(orgId, eventId, memberId);
                return;
            case BUSINESS_TRIP:
                this.attendanceEventTripMapper.softDeleteByEventIdAndMemberId(orgId, eventId, memberId);
                return;
            case BUSINESS_OUT:
                this.attendanceEventOutMapper.softDeleteByEventIdAndMemberId(orgId, eventId, memberId);
                return;
            case OVERTIME:
                this.attendanceEventOvertimeMapper.softDeleteByEventIdAndMemberId(orgId, eventId, memberId);
                return;
            case SUPPLEMENT:
                this.attendanceEventSupplementMapper.softDeleteByEventIdAndMemberId(orgId, eventId, memberId);
                return;
            default:
                throw new BizException();
        }
    }

    private void doSupplement(AttendanceEventSupplement attendanceEventSupplement) {
        AttendanceGroupTime groupTime;
        int attendanceResult;
        Long ruleTime;
        Long beginTime;
        Long endTime;
        AssertUtil.isTrue(Boolean.valueOf(AttendanceSupplementTypeEnum.WORK.getValue() == attendanceEventSupplement.getSupplementType().intValue() || AttendanceSupplementTypeEnum.OFF_WORK.getValue() == attendanceEventSupplement.getSupplementType().intValue()), OnpremiseErrorEnum.ATTENDANCE_SUPPLEMENT_TYPE_UNDEFINED);
        Member member = this.memberMapper.getMemberInfoByMemberId(attendanceEventSupplement.getMemberId(), attendanceEventSupplement.getOrgId());
        AssertUtil.isNullOrEmpty(member, OnpremiseErrorEnum.MEMBER_NOT_FIND);
        AttendanceGroupMemberInfo groupMemberInfo = this.attendanceGroupMemberMapper.getOneByMemberId(attendanceEventSupplement.getMemberId(), attendanceEventSupplement.getOrgId());
        AssertUtil.isNullOrEmpty(groupMemberInfo, OnpremiseErrorEnum.MEMBER_ATTENDANCE_NOT_FIND);
        Date date = MyDateUtils.parseDate(attendanceEventSupplement.getSupplementTime());
        if (AttendanceGroupTypeEnum.MANUAL_TYPE.getValue() == groupMemberInfo.getGroupType().intValue()) {
            groupTime = this.attendanceGroupTimeMapper.getOneByAttendanceGroupIdAndDate(groupMemberInfo.getAttendanceGroupId(), attendanceEventSupplement.getOrgId(), date);
        } else {
            groupTime = this.attendanceGroupTimeMapper.getOneByAttendanceGroupIdAndDate(groupMemberInfo.getAttendanceGroupId(), attendanceEventSupplement.getOrgId(), null);
        }
        AssertUtil.isNullOrEmpty(groupTime, OnpremiseErrorEnum.ATTENDANCE_GROUP_TIME_NOT_FIND);
        if (AttendanceGroupTypeEnum.FREE_TYPE.getValue() == groupMemberInfo.getGroupType().intValue()) {
            beginTime = Long.valueOf(MyDateUtils.parseDate(MyDateUtils.formatDate(date, "yyyy-MM-dd") + SymbolConstants.SPACE_SYMBOL + groupTime.getAttendanceBeginTime() + ":" + TarConstants.VERSION_POSIX, "yyyy-MM-dd HH:mm:ss").getTime());
            endTime = Long.valueOf(MyDateUtils.parseDate(MyDateUtils.formatDate(date, "yyyy-MM-dd") + SymbolConstants.SPACE_SYMBOL + groupTime.getAttendanceEndTime() + ":59", "yyyy-MM-dd HH:mm:ss").getTime());
            if (AttendanceSupplementTypeEnum.WORK.getValue() == attendanceEventSupplement.getSupplementType().intValue()) {
                ruleTime = beginTime;
            } else {
                ruleTime = endTime;
            }
            AssertUtil.isTrue(Boolean.valueOf(date.getTime() >= beginTime.longValue() && date.getTime() <= endTime.longValue()), OnpremiseErrorEnum.ATTENDANCE_SUPPLEMENT_TIME_ERROR, member.getMemberName());
            attendanceResult = AttendanceRecordResultEnum.SUCCESS.getValue();
        } else {
            AttendanceTimeCheckInfo info = new AttendanceTimeCheckInfo(groupTime, Long.valueOf(date.getTime()));
            if (AttendanceSupplementTypeEnum.WORK.getValue() == attendanceEventSupplement.getSupplementType().intValue()) {
                AssertUtil.isTrue(Boolean.valueOf(info.isWork()), OnpremiseErrorEnum.ATTENDANCE_SUPPLEMENT_TIME_ERROR.getErrorCode(), member.getMemberName());
                attendanceResult = info.getAttendanceWorkResult();
                ruleTime = info.getWorkRuleTime();
            } else {
                AssertUtil.isTrue(Boolean.valueOf(info.isRest()), OnpremiseErrorEnum.ATTENDANCE_SUPPLEMENT_TIME_ERROR.getErrorCode(), member.getMemberName());
                attendanceResult = info.getAttendanceRestResult();
                ruleTime = info.getRestRuleTime();
            }
            beginTime = info.getEarliestWorkRuleTime();
            endTime = info.getLatestWorkRuleTime();
        }
        logger.info("groupMemberInfo :{}", JsonUtils.toJson(groupMemberInfo));
        doSaveAttendanceRecord(attendanceResult, beginTime, endTime, ruleTime, member, date, attendanceEventSupplement.getSupplementTime(), attendanceEventSupplement.getSupplementType(), groupMemberInfo.getAttendanceGroupId());
    }

    private void doSaveAttendanceRecord(int attendanceResult, Long beginTime, Long endTime, Long ruleTime, Member member, Date date, String supplementTime, Integer supplementType, Long attendanceGroupId) {
        AttendanceRecord firstWorkRecord;
        AttendanceRecord memberRecord = this.attendanceRecordMapper.getRecordByMemberIdAndTime(member.getMemberId(), member.getOrgId(), supplementTime, supplementType.intValue());
        if (memberRecord != null) {
            this.attendanceRecordMapper.deleteById(memberRecord.getAttendanceRecordId());
        }
        AttendanceRecord memberRecord2 = new AttendanceRecord();
        memberRecord2.setAttendanceGroupId(attendanceGroupId);
        memberRecord2.setAttendanceTime(Long.valueOf(date.getTime()));
        memberRecord2.setDeptId(member.getDeptId());
        memberRecord2.setDeptName(this.deptService.packagingDeptName(member.getDeptId(), false));
        memberRecord2.setMemberId(member.getMemberId());
        memberRecord2.setMemberJobNum(member.getMemberJobNum());
        memberRecord2.setMemberName(member.getMemberName());
        memberRecord2.setOrgId(member.getOrgId());
        memberRecord2.setRecordStatus(Integer.valueOf(AttendanceStatusTypeEnum.ATTENDANCE_BY_SIPPLEMENT.getValue()));
        memberRecord2.setRecordType(supplementType);
        memberRecord2.setAttendanceResult(Integer.valueOf(attendanceResult));
        memberRecord2.setRuleTime(ruleTime);
        memberRecord2.setAttendanceDay(MyDateUtils.formatIntegerDay(memberRecord2.getAttendanceTime()));
        if (AttendanceSupplementTypeEnum.WORK.getValue() == supplementType.intValue() || (firstWorkRecord = this.attendanceRecordMapper.getTodayWorkAttendance(beginTime, endTime, member.getMemberId(), member.getOrgId(), attendanceGroupId, Integer.valueOf(AttendanceRecordTypeEnum.BEGAN_WORK.getValue()))) == null) {
            memberRecord2.setWorkTime(0L);
        } else {
            logger.info("supplement worktime : {} {}", Long.valueOf(date.getTime()), firstWorkRecord.getAttendanceTime());
            memberRecord2.setWorkTime(Long.valueOf(date.getTime() - firstWorkRecord.getAttendanceTime().longValue()));
        }
        AssertUtil.isTrue(Boolean.valueOf(this.attendanceRecordMapper.insert(memberRecord2) > 0), OnpremiseErrorEnum.SAVE_ATTENDANCE_RECORD_ERROR);
    }
}
