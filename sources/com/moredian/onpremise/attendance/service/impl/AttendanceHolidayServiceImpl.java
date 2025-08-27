package com.moredian.onpremise.attendance.service.impl;

import com.github.pagehelper.PageHelper;
import com.moredian.onpremise.api.attendance.AttendanceHolidayService;
import com.moredian.onpremise.core.adapter.CacheAdapter;
import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.mapper.AttendanceGroupMapper;
import com.moredian.onpremise.core.mapper.AttendanceHolidayMapper;
import com.moredian.onpremise.core.model.domain.AttendanceHoliday;
import com.moredian.onpremise.core.model.dto.AttendanceGroupDto;
import com.moredian.onpremise.core.model.info.CacheHeartBeatInfo;
import com.moredian.onpremise.core.model.info.DeviceLastModifyTimeInfo;
import com.moredian.onpremise.core.model.request.AttendanceHolidayListRequest;
import com.moredian.onpremise.core.model.request.DeleteAttendanceHolidayRequest;
import com.moredian.onpremise.core.model.request.QueryAttendanceHolidayRequest;
import com.moredian.onpremise.core.model.request.SaveAttendanceHolidayRequest;
import com.moredian.onpremise.core.model.request.TerminalSyncRequest;
import com.moredian.onpremise.core.model.response.AttendanceHolidayListResponse;
import com.moredian.onpremise.core.model.response.AttendanceHolidayResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncAttendanceHolidayResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncResponse;
import com.moredian.onpremise.core.utils.AssertUtil;
import com.moredian.onpremise.core.utils.CacheKeyGenerateUtils;
import com.moredian.onpremise.core.utils.MyDateUtils;
import com.moredian.onpremise.core.utils.MyListUtils;
import com.moredian.onpremise.core.utils.PageList;
import com.moredian.onpremise.core.utils.Paginator;
import com.moredian.onpremise.iot.handle.NettyMessageApi;
import com.moredian.onpremise.model.SyncAttendanceHolidayRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
/* loaded from: onpremise-attendance-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/attendance/service/impl/AttendanceHolidayServiceImpl.class */
public class AttendanceHolidayServiceImpl implements AttendanceHolidayService {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) AttendanceHolidayServiceImpl.class);

    @Autowired
    private AttendanceHolidayMapper attendanceHolidayMapper;

    @Autowired
    private AttendanceGroupMapper attendanceGroupMapper;

    @Autowired
    private NettyMessageApi nettyMessageApi;

    @Override // com.moredian.onpremise.api.attendance.AttendanceHolidayService
    @Transactional(rollbackFor = {Exception.class})
    public Boolean insertAttendanceHoliday(SaveAttendanceHolidayRequest request) throws BeansException {
        checkSaveAttendanceHoliday(request);
        AttendanceHoliday attendanceHoliday = new AttendanceHoliday();
        BeanUtils.copyProperties(request, attendanceHoliday);
        attendanceHoliday.setRepayWorkDate(MyListUtils.listToString(request.getRepayWorkDates()));
        attendanceHoliday.setAttendanceGroupId(MyListUtils.listToString(request.getAttendanceGroupIds()));
        AssertUtil.isTrue(Boolean.valueOf(this.attendanceHolidayMapper.insertOne(attendanceHoliday) > 0), OnpremiseErrorEnum.SAVE_ATTENDANCE_HOLIDAY_FAIL);
        doSendNettyMessage(request.getOrgId());
        return true;
    }

    @Override // com.moredian.onpremise.api.attendance.AttendanceHolidayService
    @Transactional(rollbackFor = {Exception.class})
    public Boolean deleteAttendanceHoliday(DeleteAttendanceHolidayRequest request) {
        AssertUtil.isNullOrEmpty(request.getAttendanceHolidayId(), OnpremiseErrorEnum.ATTENDANCE_HOLIDAY_ID_LACK);
        AttendanceHoliday attendanceHolidayOld = this.attendanceHolidayMapper.findById(request.getOrgId(), request.getAttendanceHolidayId());
        AssertUtil.isNullOrEmpty(attendanceHolidayOld, OnpremiseErrorEnum.ATTENDANCE_HOLIDAY_NOT_EXITS);
        AssertUtil.isTrue(Boolean.valueOf(this.attendanceHolidayMapper.softDeleteById(request.getOrgId(), request.getAttendanceHolidayId()) > 0), OnpremiseErrorEnum.DELETE_ATTENDANCE_HOLIDAY_FAIL);
        doSendNettyMessage(request.getOrgId());
        return true;
    }

    @Override // com.moredian.onpremise.api.attendance.AttendanceHolidayService
    @Transactional(rollbackFor = {Exception.class})
    public Boolean updateAttendanceHoliday(SaveAttendanceHolidayRequest request) throws BeansException {
        AssertUtil.checkId(request.getAttendanceHolidayId(), OnpremiseErrorEnum.ATTENDANCE_HOLIDAY_ID_LACK);
        checkSaveAttendanceHoliday(request);
        AttendanceHoliday attendanceHolidayOld = this.attendanceHolidayMapper.findById(request.getOrgId(), request.getAttendanceHolidayId());
        AssertUtil.isNullOrEmpty(attendanceHolidayOld, OnpremiseErrorEnum.ATTENDANCE_HOLIDAY_NOT_EXITS);
        AttendanceHoliday attendanceHoliday = new AttendanceHoliday();
        BeanUtils.copyProperties(request, attendanceHoliday);
        attendanceHoliday.setAttendanceHolidayId(request.getAttendanceHolidayId());
        attendanceHoliday.setRepayWorkDate(MyListUtils.listToString(request.getRepayWorkDates()));
        attendanceHoliday.setAttendanceGroupId(MyListUtils.listToString(request.getAttendanceGroupIds()));
        AssertUtil.isTrue(Boolean.valueOf(this.attendanceHolidayMapper.updateOne(attendanceHoliday) > 0), OnpremiseErrorEnum.SAVE_ATTENDANCE_HOLIDAY_FAIL);
        doSendNettyMessage(request.getOrgId());
        return true;
    }

    @Override // com.moredian.onpremise.api.attendance.AttendanceHolidayService
    public AttendanceHolidayResponse findAttendanceHolidayById(QueryAttendanceHolidayRequest request) throws BeansException {
        AssertUtil.isNullOrEmpty(request.getAttendanceHolidayId(), OnpremiseErrorEnum.ATTENDANCE_HOLIDAY_ID_LACK);
        AttendanceHolidayResponse response = new AttendanceHolidayResponse();
        AttendanceHoliday attendanceHoliday = this.attendanceHolidayMapper.findById(request.getOrgId(), request.getAttendanceHolidayId());
        AssertUtil.isNullOrEmpty(attendanceHoliday, OnpremiseErrorEnum.ATTENDANCE_HOLIDAY_NOT_EXITS);
        BeanUtils.copyProperties(attendanceHoliday, response);
        List<String> repayWorkDates = new ArrayList();
        if (attendanceHoliday.getRepayWorkDate() != null && attendanceHoliday.getRepayWorkDate().trim().length() > 0) {
            repayWorkDates = Arrays.asList(attendanceHoliday.getRepayWorkDate().split(","));
        }
        response.setRepayWorkDates(repayWorkDates);
        List<AttendanceGroupDto> attendanceGroupResponses = new ArrayList();
        if (attendanceHoliday.getAttendanceGroupId() != null && attendanceHoliday.getAttendanceGroupId().trim().length() > 0) {
            attendanceGroupResponses = this.attendanceGroupMapper.listAttendanceGroupByIds(request.getOrgId(), MyListUtils.stringListToLong(Arrays.asList(attendanceHoliday.getAttendanceGroupId().split(","))));
        }
        response.setAttendanceGroupResponses(attendanceGroupResponses);
        return response;
    }

    @Override // com.moredian.onpremise.api.attendance.AttendanceHolidayService
    public PageList<AttendanceHolidayListResponse> pageFindAttendanceHoliday(AttendanceHolidayListRequest request) {
        Paginator paginator = request.getPaginator();
        if (Paginator.checkPaginator(paginator)) {
            PageHelper.startPage(paginator.getPageNum(), paginator.getPageSize());
            List<AttendanceHolidayListResponse> responses = this.attendanceHolidayMapper.pageFind(request);
            dealAttendanceHolidayList(responses);
            return new PageList<>(responses);
        }
        List<AttendanceHolidayListResponse> responses2 = this.attendanceHolidayMapper.pageFind(request);
        dealAttendanceHolidayList(responses2);
        return new PageList<>(Paginator.initPaginator(responses2), responses2);
    }

    @Override // com.moredian.onpremise.api.attendance.AttendanceHolidayService
    public List<AttendanceHoliday> getAttendanceHolidayByGroupId(Long orgId, Long attendanceGroupId) {
        return this.attendanceHolidayMapper.listByAttendanceGroupId(orgId, attendanceGroupId);
    }

    @Override // com.moredian.onpremise.api.attendance.AttendanceHolidayService
    public TerminalSyncResponse<TerminalSyncAttendanceHolidayResponse> syncAttendanceHoliday(TerminalSyncRequest request) throws BeansException {
        TerminalSyncResponse<TerminalSyncAttendanceHolidayResponse> terminalSyncResponse = new TerminalSyncResponse<>();
        String lastSyncTime = MyDateUtils.getDateString(request.getLastSyncTime().longValue(), "yyyy-MM-dd HH:mm:ss.SSS");
        List<AttendanceHoliday> attendanceHolidays = this.attendanceHolidayMapper.listSyncAttendanceHoliday(request.getOrgId(), lastSyncTime);
        logger.info("==============sync attendance holiday list size :{}=============", Integer.valueOf(attendanceHolidays.size()));
        if (CollectionUtils.isEmpty(attendanceHolidays)) {
            terminalSyncResponse.setSyncTime(CacheAdapter.getLastModifyTime(CacheKeyGenerateUtils.getAttendanceHolidayLastModifyTimeKey(request.getDeviceSn())).getLastModifyTime());
            return terminalSyncResponse;
        }
        List<TerminalSyncAttendanceHolidayResponse> syncInsertGroupList = new ArrayList<>();
        List<TerminalSyncAttendanceHolidayResponse> syncModifyGroupList = new ArrayList<>();
        List<TerminalSyncAttendanceHolidayResponse> syncDeleteGroupList = new ArrayList<>();
        for (AttendanceHoliday holiday : attendanceHolidays) {
            TerminalSyncAttendanceHolidayResponse response = new TerminalSyncAttendanceHolidayResponse();
            BeanUtils.copyProperties(holiday, response);
            response.setRepayWorkDates(Arrays.asList(holiday.getRepayWorkDate().split(",")));
            response.setAttendanceGroupId(MyListUtils.stringListToLong(Arrays.asList(holiday.getAttendanceGroupId().split(","))));
            if (holiday.getDeleteOrNot().intValue() == 1) {
                if (request.getLastSyncTime().longValue() > 0) {
                    syncDeleteGroupList.add(response);
                }
            } else if (holiday.getGmtCreate().getTime() > request.getLastSyncTime().longValue()) {
                syncInsertGroupList.add(response);
            } else {
                syncModifyGroupList.add(response);
            }
        }
        AttendanceHoliday lastOne = this.attendanceHolidayMapper.getLastOne(request.getOrgId());
        terminalSyncResponse.setSyncTime(Long.valueOf(lastOne == null ? 0L : lastOne.getGmtModify().getTime()));
        doCacheLastModifyTime(CacheKeyGenerateUtils.getAttendanceHolidayLastModifyTimeKey(request.getDeviceSn()), terminalSyncResponse.getSyncTime());
        terminalSyncResponse.setSyncDeleteResult(syncDeleteGroupList);
        terminalSyncResponse.setSyncInsertResult(syncInsertGroupList);
        terminalSyncResponse.setSyncModifyResult(syncModifyGroupList);
        return terminalSyncResponse;
    }

    private void doSendNettyMessage(Long orgId) {
        AttendanceHoliday attendanceHoliday = this.attendanceHolidayMapper.getLastOne(orgId);
        Map<String, CacheHeartBeatInfo> map = CacheAdapter.getHeartBeatInfoAll();
        for (String key : map.keySet()) {
            String deviceSn = CacheKeyGenerateUtils.restoreHeartBeatCacheKey(key);
            this.nettyMessageApi.sendMsg(new SyncAttendanceHolidayRequest(Long.valueOf(attendanceHoliday == null ? 0L : attendanceHoliday.getGmtModify().getTime()), orgId), Integer.valueOf(SyncAttendanceHolidayRequest.MODEL_TYPE.type()), deviceSn);
        }
    }

    private void dealAttendanceHolidayList(List<AttendanceHolidayListResponse> responses) {
        for (AttendanceHolidayListResponse response : responses) {
            List<Long> attendanceGroupIds = MyListUtils.stringListToLong(Arrays.asList(response.getAttendanceGroupId().split(",")));
            StringBuffer sb = new StringBuffer();
            if (MyListUtils.listIsEmpty(attendanceGroupIds)) {
                List<AttendanceGroupDto> attendanceGroupDtos = this.attendanceGroupMapper.listAttendanceGroupByIds(response.getOrgId(), attendanceGroupIds);
                if (MyListUtils.listIsEmpty(attendanceGroupDtos)) {
                    for (AttendanceGroupDto attendanceHolidayGroupResponse : attendanceGroupDtos) {
                        sb.append(attendanceHolidayGroupResponse.getGroupName()).append(",");
                    }
                }
            }
            response.setGroupNames(sb.length() == 0 ? sb.toString() : sb.substring(0, sb.length() - 1));
        }
    }

    private void checkSaveAttendanceHoliday(SaveAttendanceHolidayRequest request) {
        AssertUtil.isNullOrEmpty(request.getHolidayName(), OnpremiseErrorEnum.ATTENDANCE_HOLIDAY_NAME_LACK);
        AssertUtil.isNullOrEmpty(request.getStartDate(), OnpremiseErrorEnum.ATTENDANCE_HOLIDAY_START_DATE_LACK);
        AssertUtil.isNullOrEmpty(MyDateUtils.parseDate(request.getStartDate(), "yyyy-MM-dd"), OnpremiseErrorEnum.TIME_FORMAT_ERROR);
        AssertUtil.isNullOrEmpty(request.getEndDate(), OnpremiseErrorEnum.ATTENDANCE_HOLIDAY_START_DATE_LACK);
        AssertUtil.isNullOrEmpty(MyDateUtils.parseDate(request.getEndDate(), "yyyy-MM-dd"), OnpremiseErrorEnum.TIME_FORMAT_ERROR);
        for (String date : request.getRepayWorkDates()) {
            AssertUtil.isNullOrEmpty(MyDateUtils.parseDate(date, "yyyy-MM-dd"), OnpremiseErrorEnum.TIME_FORMAT_ERROR);
        }
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
}
