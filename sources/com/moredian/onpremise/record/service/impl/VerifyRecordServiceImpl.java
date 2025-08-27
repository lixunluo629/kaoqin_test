package com.moredian.onpremise.record.service.impl;

import com.github.pagehelper.PageHelper;
import com.moredian.onpremise.api.attendance.AttendanceRecordService;
import com.moredian.onpremise.api.checkIn.CheckInService;
import com.moredian.onpremise.api.meal.MealRecordService;
import com.moredian.onpremise.api.member.DeptService;
import com.moredian.onpremise.api.record.VerifyRecordService;
import com.moredian.onpremise.api.temperature.TemperatureRecordService;
import com.moredian.onpremise.api.visit.VisitRecordService;
import com.moredian.onpremise.core.common.enums.AppTypeEnum;
import com.moredian.onpremise.core.common.enums.CallbackTagEnum;
import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.common.enums.VerifyRecordTypeEnum;
import com.moredian.onpremise.core.common.enums.VerifyTypeEnum;
import com.moredian.onpremise.core.config.UploadConfig;
import com.moredian.onpremise.core.exception.BizException;
import com.moredian.onpremise.core.mapper.AppMapper;
import com.moredian.onpremise.core.mapper.AttendanceEventOvertimeMapper;
import com.moredian.onpremise.core.mapper.CallbackServerMapper;
import com.moredian.onpremise.core.mapper.DeptMapper;
import com.moredian.onpremise.core.mapper.DeviceMapper;
import com.moredian.onpremise.core.mapper.ExternalContactMapper;
import com.moredian.onpremise.core.mapper.MemberMapper;
import com.moredian.onpremise.core.mapper.VerifyRecordMapper;
import com.moredian.onpremise.core.model.domain.CallbackServer;
import com.moredian.onpremise.core.model.domain.Device;
import com.moredian.onpremise.core.model.domain.ExternalContact;
import com.moredian.onpremise.core.model.domain.Member;
import com.moredian.onpremise.core.model.domain.TemperatureRecord;
import com.moredian.onpremise.core.model.domain.VerifyRecord;
import com.moredian.onpremise.core.model.request.CheckInLogRequest;
import com.moredian.onpremise.core.model.request.SaveAttendanceRecordRequest;
import com.moredian.onpremise.core.model.request.SaveMealRecordRequest;
import com.moredian.onpremise.core.model.request.SaveVisitRecordDetailRequest;
import com.moredian.onpremise.core.model.request.StatisticsVerifyScoreRequest;
import com.moredian.onpremise.core.model.request.TemperatureCallbackRequest;
import com.moredian.onpremise.core.model.request.VerifyCallbackRequest;
import com.moredian.onpremise.core.model.request.VerifyRecordBatchSaveRequest;
import com.moredian.onpremise.core.model.request.VerifyRecordExcelRequest;
import com.moredian.onpremise.core.model.request.VerifyRecordListRequest;
import com.moredian.onpremise.core.model.request.VerifyRecordSaveRequest;
import com.moredian.onpremise.core.model.response.StatisticsVerifyScoreDetailResponse;
import com.moredian.onpremise.core.model.response.StatisticsVerifyScoreResponse;
import com.moredian.onpremise.core.model.response.StatisticsVerifyScoreV2Response;
import com.moredian.onpremise.core.model.response.UserLoginResponse;
import com.moredian.onpremise.core.model.response.VerifyRecordExcelResponse;
import com.moredian.onpremise.core.model.response.VerifyRecordResponse;
import com.moredian.onpremise.core.model.response.VerifyRecordSaveResponse;
import com.moredian.onpremise.core.utils.AssertUtil;
import com.moredian.onpremise.core.utils.HttpUtils;
import com.moredian.onpremise.core.utils.JsonUtils;
import com.moredian.onpremise.core.utils.MyDateUtils;
import com.moredian.onpremise.core.utils.MyFileUtils;
import com.moredian.onpremise.core.utils.MyListUtils;
import com.moredian.onpremise.core.utils.PageList;
import com.moredian.onpremise.core.utils.Paginator;
import com.moredian.onpremise.core.utils.ThreadPoolUtils;
import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
/* loaded from: onpremise-record-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/record/service/impl/VerifyRecordServiceImpl.class */
public class VerifyRecordServiceImpl implements VerifyRecordService {

    @Autowired
    private VerifyRecordMapper verifyRecordMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private UploadConfig uplocadConfig;

    @Autowired
    private CallbackServerMapper callbackServerMapper;

    @Autowired
    private AppMapper appMapper;

    @Autowired
    private CheckInService checkInService;

    @Value("${onpremise.file.path}")
    private String filePath;

    @Autowired
    private DeptService deptService;

    @Autowired
    private AttendanceRecordService attendanceRecordService;

    @Autowired
    private AttendanceEventOvertimeMapper overtimeMapper;

    @Autowired
    private TemperatureRecordService temperatureRecordService;

    @Autowired
    private VisitRecordService visitRecordService;

    @Autowired
    private ExternalContactMapper externalContactMapper;

    @Autowired
    private MealRecordService mealRecordService;
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) VerifyRecordServiceImpl.class);
    private static final Runtime runtime = Runtime.getRuntime();

    @Override // com.moredian.onpremise.api.record.VerifyRecordService
    public PageList<VerifyRecordResponse> recordList(VerifyRecordListRequest request) {
        request.setManagerDeptIds(UserLoginResponse.getAccountManageDeptId(request.getSessionId()));
        request.setManagerDeviceSns(UserLoginResponse.getAccountManageDeviceSn(request.getSessionId()));
        if (request.getDeptId() != null) {
            List deptIds = new ArrayList();
            deptIds.add(request.getDeptId());
            request.setDeptIds(this.deptService.packagingChildDept(request.getDeptId(), request.getOrgId(), deptIds));
        }
        if (request.getDeviceSn() != null && request.getDeviceSn().trim().length() > 0) {
            request.setDeviceSns(Arrays.asList(request.getDeviceSn().split(",")));
        }
        if (Paginator.checkPaginator(request.getPaginator())) {
            PageHelper.startPage(request.getPaginator().getPageNum(), request.getPaginator().getPageSize());
            List<VerifyRecordResponse> listResp = this.verifyRecordMapper.getVerifyList(request);
            dealVerifyRecordResponseList(listResp);
            return new PageList<>(listResp);
        }
        List<VerifyRecordResponse> listResp2 = this.verifyRecordMapper.getVerifyList(request);
        dealVerifyRecordResponseList(listResp2);
        return new PageList<>(Paginator.initPaginator(listResp2), listResp2);
    }

    private void dealVerifyRecordResponseList(List<VerifyRecordResponse> list) {
        for (VerifyRecordResponse response : list) {
            response.setVerifyTimeMillis(response.getVerifyTimestamp());
            if (!response.getAppType().equals(Integer.valueOf(AppTypeEnum.FK.getValue()))) {
                Member info = this.memberMapper.getMemberInfoByMemberId(response.getMemberId(), response.getOrgId());
                if (info != null) {
                    response.setVerifyFaceUrl(info.getVerifyFaceUrl());
                }
                Member secondMember = this.memberMapper.getMemberInfoByMemberId(response.getSecondMemberId(), response.getOrgId());
                if (secondMember != null) {
                    response.setSecondMemberJobNum(secondMember.getMemberJobNum());
                    response.setSecondMemberName(secondMember.getMemberName());
                    response.setSecondVerifyFaceUrl(secondMember.getVerifyFaceUrl());
                }
            }
        }
    }

    @Override // com.moredian.onpremise.api.record.VerifyRecordService
    @Transactional(rollbackFor = {RuntimeException.class})
    public List<VerifyRecordSaveResponse> saveRecord(VerifyRecordSaveRequest request) throws BeansException {
        if (request.getVerifyType() == null) {
            request.setVerifyType(Integer.valueOf(VerifyTypeEnum.FACE.getValue()));
        }
        AssertUtil.isNullOrEmpty(request.getDeviceSn(), OnpremiseErrorEnum.DEVICE_SN_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getVerifyTime(), OnpremiseErrorEnum.MEMBER_VERIFY_TIME_MUST_NOT_NULL);
        if (VerifyTypeEnum.FACE.getValue() == request.getVerifyType().intValue()) {
            AssertUtil.isNullOrEmpty(request.getSnapFaceBase64(), OnpremiseErrorEnum.MEMBER_VERIFYFACEURL_MUST_NOT_NULL);
        }
        logger.debug("save record appType :{}, doorFlag :{}", request.getAppType(), request.getDoorFlag());
        Device device = this.deviceMapper.getDeviceInfoByDeviceSn(request.getDeviceSn());
        AssertUtil.isNullOrEmpty(device, OnpremiseErrorEnum.DEVICE_NOT_FIND);
        VerifyRecord record = new VerifyRecord();
        record.setMemberId(request.getMemberId());
        record.setDeviceSn(request.getDeviceSn());
        record.setVerifyType(request.getVerifyType());
        record.setVerifyTimestamp(request.getVerifyTime());
        Integer count = this.verifyRecordMapper.countVerifyRecord(record);
        if (count.intValue() > 0) {
            return new ArrayList();
        }
        String url = "";
        if (VerifyTypeEnum.FACE.getValue() == request.getVerifyType().intValue()) {
            url = this.uplocadConfig.uploadImageForReturnUrl(request.getSnapFaceBase64());
        } else {
            request.setVerifyScore(null);
            request.setSecondVerifyScore(null);
        }
        try {
            record.setDeviceId(device.getDeviceId());
            record.setDeviceName(device.getDeviceName());
            record.setSnapFaceUrl(url);
            record.setVerifyScore(request.getVerifyScore());
            record.setSecondMemberId(request.getSecondMemberId());
            record.setSecondVerifyScore(request.getSecondVerifyScore());
            record.setVerifyResult(request.getVerifyResult());
            if (!request.getAppType().equals(Integer.valueOf(AppTypeEnum.FK.getValue()))) {
                Member member = this.memberMapper.getMemberInfoByMemberId(request.getMemberId(), device.getOrgId());
                if (member != null) {
                    record.setMemberId(request.getMemberId());
                    record.setMemberName(member.getMemberName());
                    record.setJobNum(member.getMemberJobNum());
                    record.setDeptId(member.getDeptId());
                    record.setDeptName(this.deptService.packagingDeptName(member.getDeptId(), false));
                }
            } else {
                ExternalContact externalContact = this.externalContactMapper.getOneById(request.getOrgId(), request.getMemberId());
                if (externalContact != null) {
                    record.setMemberId(request.getMemberId());
                    record.setMemberName(externalContact.getName());
                }
            }
            record.setOrgId(request.getOrgId());
            record.setRecordType(Integer.valueOf(VerifyRecordTypeEnum.DEVICE_VERIFY.getValue()));
            record.setMirrorVerifyScore(request.getMirrorVerifyScore());
            record.setAppType(request.getAppType());
            record.setVerifyDate(MyDateUtils.formatIntegerDay(request.getVerifyTime()));
            record.setVerifyTime(MyDateUtils.parseDate(MyDateUtils.formatDate(MyDateUtils.getDate(request.getVerifyTime().longValue()), "yyyy-MM-dd HH:mm:ss.SSS")));
            VerifyRecord var = new VerifyRecord();
            Long bizId = request.getBizId();
            Integer appType = request.getAppType();
            BeanUtils.copyProperties(record, var);
            if (request.getDoorFlag() != null && request.getDoorFlag().equals(1)) {
                AssertUtil.isTrue(Boolean.valueOf(this.verifyRecordMapper.saveVerifyRecord(record) > 0), OnpremiseErrorEnum.SAVE_VERIFY_RECORD_FAIL);
            }
            if (CollectionUtils.isEmpty(request.getAppTypes())) {
                handleBizRecord(var, bizId, appType, request);
            } else {
                for (Integer eachAppType : request.getAppTypes()) {
                    handleBizRecord(var, bizId, eachAppType, request);
                }
            }
        } catch (BizException e) {
            if (url != null && url.trim().length() > 0) {
                File file = new File(this.uplocadConfig.restoreImageUrl(url));
                if (file.exists()) {
                    file.delete();
                }
            }
            logger.error("error:{}", (Throwable) e);
        }
        if (request.getAppType().intValue() != AppTypeEnum.TC.getValue()) {
            if (request.getVerifyResult() != null && request.getVerifyResult().intValue() == 1) {
                logger.info("=====> sendCallback:{}", JsonUtils.toJson(record));
                sendCallback(record, CallbackTagEnum.REC_SUCCESS.getTag());
            } else {
                logger.info("=====> sendFailCallback:{}", JsonUtils.toJson(record));
                sendCallback(record, CallbackTagEnum.REC_FAIL.getTag());
            }
        }
        return new ArrayList();
    }

    @Override // com.moredian.onpremise.api.record.VerifyRecordService
    public List<VerifyRecordExcelResponse> excelRecord(VerifyRecordExcelRequest request) throws BeansException {
        List<VerifyRecordExcelResponse> listResp = new ArrayList<>();
        if (request.getDeptIds() != null && request.getDeptIds().trim().length() > 0) {
            List<Long> deptIdList = new ArrayList<>();
            String[] deptIdArr = request.getDeptIds().split(",");
            for (String deptId : deptIdArr) {
                if (!StringUtils.isEmpty(deptId)) {
                    deptIdList.add(Long.valueOf(Long.parseLong(deptId)));
                    this.deptService.packagingChildDept(Long.valueOf(Long.parseLong(deptId)), request.getOrgId(), deptIdList);
                }
            }
            request.setDeptIdList(deptIdList);
        }
        if (request.getDeviceSns() != null && request.getDeviceSns().trim().length() > 0) {
            request.setDeviceSnList(Arrays.asList(request.getDeviceSns().split(",")));
        }
        if (request.getMemberIds() != null && request.getMemberIds().trim().length() > 0) {
            List<Long> memberIdList = new ArrayList<>();
            String[] memberIdArr = request.getMemberIds().split(",");
            for (String memberId : memberIdArr) {
                memberIdList.add(Long.valueOf(Long.parseLong(memberId)));
            }
            request.setMemberIdList(memberIdList);
        }
        List<VerifyRecord> listRecord = this.verifyRecordMapper.getVerifyExcelList(request);
        if (listRecord != null && listRecord.size() != 0) {
            for (int i = 0; i < listRecord.size(); i++) {
                VerifyRecord record = listRecord.get(i);
                VerifyRecordExcelResponse response = new VerifyRecordExcelResponse();
                BeanUtils.copyProperties(record, response);
                if (StringUtils.isEmpty(record.getMemberName())) {
                    response.setMemberName("陌生人");
                }
                listResp.add(response);
            }
        }
        listRecord.clear();
        return listResp;
    }

    @Override // com.moredian.onpremise.api.record.VerifyRecordService
    public boolean batchSaveRecord(VerifyRecordBatchSaveRequest request) {
        if (MyListUtils.listIsEmpty(request.getSaveRequests())) {
            for (VerifyRecordSaveRequest saveRequest : request.getSaveRequests()) {
                try {
                    saveRecord(saveRequest);
                } catch (Exception e) {
                    logger.error("error:{}", (Throwable) e);
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    @Override // com.moredian.onpremise.api.record.VerifyRecordService
    public boolean delete(Date recordDate, Date snapDate) {
        logger.info("delete verify record :{}, snap {}", Long.valueOf(recordDate.getTime()), Long.valueOf(snapDate.getTime()));
        List<String> snapUrls = this.verifyRecordMapper.listSnapUrl(snapDate);
        for (String snapUrl : snapUrls) {
            MyFileUtils.deleteFile(this.filePath + snapUrl);
        }
        this.verifyRecordMapper.deleteByDate(recordDate);
        return true;
    }

    private void sendCallback(VerifyRecord record, String tag) throws BeansException {
        CallbackServer callbackServer = this.callbackServerMapper.getOneByTag(record.getOrgId(), tag);
        if (callbackServer != null) {
            final VerifyCallbackRequest request = new VerifyCallbackRequest();
            BeanUtils.copyProperties(record, request);
            request.setVerifyTimeMillis(Long.valueOf(record.getVerifyTime().getTime()));
            String[] urls = callbackServer.getCallbackUrl().split(",");
            if (urls != null && urls.length > 0) {
                for (final String url : urls) {
                    ThreadPoolUtils.poolSend.execute(new Runnable() { // from class: com.moredian.onpremise.record.service.impl.VerifyRecordServiceImpl.1
                        @Override // java.lang.Runnable
                        public void run() {
                            try {
                                HttpUtils.sendURLPost(url, JsonUtils.toJson(request));
                            } catch (Exception e) {
                                VerifyRecordServiceImpl.logger.error("error:{}", (Throwable) e);
                            }
                        }
                    });
                }
            }
        }
    }

    private void sendTemperatureCallback(TemperatureRecord record, String tag) throws BeansException {
        CallbackServer callbackServer = this.callbackServerMapper.getOneByTag(record.getOrgId(), tag);
        if (callbackServer != null) {
            final TemperatureCallbackRequest request = new TemperatureCallbackRequest();
            BeanUtils.copyProperties(record, request);
            request.setVerifyTimeMillis(record.getVerifyTime());
            request.setJobNum(record.getMemberJobNum());
            String[] urls = callbackServer.getCallbackUrl().split(",");
            if (urls != null && urls.length > 0) {
                for (final String url : urls) {
                    ThreadPoolUtils.poolSend.execute(new Runnable() { // from class: com.moredian.onpremise.record.service.impl.VerifyRecordServiceImpl.2
                        @Override // java.lang.Runnable
                        public void run() {
                            try {
                                HttpUtils.sendURLPost(url, JsonUtils.toJson(request));
                            } catch (Exception e) {
                                VerifyRecordServiceImpl.logger.error("error:{}", (Throwable) e);
                            }
                        }
                    });
                }
            }
        }
    }

    private void handleBizRecord(VerifyRecord record, Long bizId, Integer appType, VerifyRecordSaveRequest request) throws BeansException {
        AppTypeEnum appTypeEnum = AppTypeEnum.getByValue(appType.intValue());
        switch (appTypeEnum) {
            case MJ:
                if (request.getDoorFlag() == null || !request.getDoorFlag().equals(1)) {
                    AssertUtil.isTrue(Boolean.valueOf(this.verifyRecordMapper.saveVerifyRecord(record) > 0), OnpremiseErrorEnum.SAVE_VERIFY_RECORD_FAIL);
                    break;
                }
                break;
            case QD:
                CheckInLogRequest checkInLogRequest = new CheckInLogRequest();
                checkInLogRequest.setId(bizId);
                checkInLogRequest.setDeviceSn(record.getDeviceSn());
                checkInLogRequest.setUrl(record.getSnapFaceUrl());
                checkInLogRequest.setMemberId(record.getMemberId());
                checkInLogRequest.setCheckInTime(Long.valueOf(record.getVerifyTime().getTime()));
                checkInLogRequest.setOrgId(record.getOrgId());
                this.checkInService.saveCheckInLog(checkInLogRequest);
                break;
            case KQ:
                SaveAttendanceRecordRequest recordRequest = new SaveAttendanceRecordRequest();
                BeanUtils.copyProperties(record, recordRequest);
                recordRequest.setAttendanceGroupId(bizId);
                recordRequest.setAttendancePictureUrl(record.getSnapFaceUrl());
                recordRequest.setAttendanceTime(Long.valueOf(record.getVerifyTime().getTime()));
                recordRequest.setMemberJobNum(record.getJobNum());
                this.attendanceRecordService.saveRecord(recordRequest);
                break;
            case FK:
                SaveVisitRecordDetailRequest saveVisitRecordDetailRequest = new SaveVisitRecordDetailRequest();
                saveVisitRecordDetailRequest.setOrgId(record.getOrgId());
                saveVisitRecordDetailRequest.setDeviceSn(record.getDeviceSn());
                saveVisitRecordDetailRequest.setDeviceName(record.getDeviceName());
                saveVisitRecordDetailRequest.setMemberId(record.getMemberId());
                saveVisitRecordDetailRequest.setMemberName(record.getMemberName());
                saveVisitRecordDetailRequest.setSnapFaceUrl(record.getSnapFaceUrl());
                saveVisitRecordDetailRequest.setVerifyTime(Long.valueOf(record.getVerifyTime().getTime()));
                this.visitRecordService.addVisitRecordDetail(saveVisitRecordDetailRequest);
                break;
            case TC:
                SaveMealRecordRequest requestMeal = new SaveMealRecordRequest();
                BeanUtils.copyProperties(request, requestMeal);
                requestMeal.setMealCanteenId(request.getBizId());
                requestMeal.setRecordType(null);
                this.mealRecordService.saveMealRecord(requestMeal);
                break;
            case CW:
                TemperatureRecord temperatureRecord = new TemperatureRecord();
                BeanUtils.copyProperties(record, temperatureRecord);
                temperatureRecord.setMemberJobNum(record.getJobNum());
                temperatureRecord.setVerifyTime(Long.valueOf(record.getVerifyTime().getTime()));
                temperatureRecord.setVerifyDay(MyDateUtils.formatIntegerDay(record.getVerifyTime()));
                temperatureRecord.setTemperatureValue(request.getTemperatureValue() == null ? new BigDecimal(-1) : request.getTemperatureValue());
                temperatureRecord.setOperator(request.getOperator());
                temperatureRecord.setHealthCode(request.getHealthCode());
                if (2 == record.getVerifyResult().intValue()) {
                    temperatureRecord.setMemberName("");
                    temperatureRecord.setDeptName("");
                    temperatureRecord.setMemberJobNum("");
                }
                this.temperatureRecordService.insert(temperatureRecord);
                sendTemperatureCallback(temperatureRecord, CallbackTagEnum.TEMPERATURE_RECORD.getTag());
                break;
        }
    }

    private boolean checkWorkIsOvertime(Long memberId, Long orgId, Long verifyTime) {
        this.overtimeMapper.getWorkOvertimeEventByMemberIdAndTime(memberId, orgId, verifyTime);
        return true;
    }

    @Override // com.moredian.onpremise.api.record.VerifyRecordService
    public StatisticsVerifyScoreResponse statisticsVerifyScore(StatisticsVerifyScoreRequest request) {
        StatisticsVerifyScoreResponse statisticsVerifyScoreResponse = new StatisticsVerifyScoreResponse();
        List<VerifyRecordResponse> listResp = this.verifyRecordMapper.statisticsVerifyScore(request);
        List<Integer> verifyScore = new ArrayList<>();
        List<Integer> secondVerifyScore = new ArrayList<>();
        for (VerifyRecordResponse recordResponse : listResp) {
            verifyScore.add(recordResponse.getVerifyScore());
            secondVerifyScore.add(Integer.valueOf(recordResponse.getSecondVerifyScore() == null ? 0 : recordResponse.getSecondVerifyScore().intValue()));
        }
        statisticsVerifyScoreResponse.setVerifyScore(verifyScore);
        statisticsVerifyScoreResponse.setSecondVerifyScore(secondVerifyScore);
        return statisticsVerifyScoreResponse;
    }

    @Override // com.moredian.onpremise.api.record.VerifyRecordService
    public StatisticsVerifyScoreV2Response statisticsVerifyScoreV2(StatisticsVerifyScoreRequest request) {
        StatisticsVerifyScoreV2Response statisticsVerifyScoreV2Response = new StatisticsVerifyScoreV2Response();
        Integer total = this.verifyRecordMapper.countStatisticsVerifyScore(request);
        if (total.intValue() == 0) {
            statisticsVerifyScoreV2Response.setVerifyScoreList(new ArrayList<>());
            statisticsVerifyScoreV2Response.setSecondVerifyScoreList(new ArrayList<>());
            statisticsVerifyScoreV2Response.setMirrorVerifyScoreList(new ArrayList<>());
            return statisticsVerifyScoreV2Response;
        }
        List<StatisticsVerifyScoreDetailResponse> verifyScoreList = this.verifyRecordMapper.statisticsFirstVerifyScore(request);
        List<StatisticsVerifyScoreDetailResponse> secondVerifyScoreList = this.verifyRecordMapper.statisticsSecondVerifyScore(request);
        DecimalFormat df = new DecimalFormat("0.00000");
        List<StatisticsVerifyScoreDetailResponse> initList = new ArrayList<>();
        for (int i = 0; i <= 100; i++) {
            StatisticsVerifyScoreDetailResponse statisticsVerifyScoreDetailResponse = new StatisticsVerifyScoreDetailResponse();
            statisticsVerifyScoreDetailResponse.setVerifyScore(Integer.valueOf(i));
            statisticsVerifyScoreDetailResponse.setPercent(null);
            statisticsVerifyScoreDetailResponse.setRecordCount(0);
            initList.add(statisticsVerifyScoreDetailResponse);
        }
        for (StatisticsVerifyScoreDetailResponse statisticsVerifyScoreDetailResponse2 : verifyScoreList) {
            statisticsVerifyScoreDetailResponse2.setPercent(df.format(statisticsVerifyScoreDetailResponse2.getRecordCount().doubleValue() / total.intValue()));
        }
        for (StatisticsVerifyScoreDetailResponse statisticsVerifyScoreDetailResponse3 : secondVerifyScoreList) {
            statisticsVerifyScoreDetailResponse3.setPercent(df.format(statisticsVerifyScoreDetailResponse3.getRecordCount().doubleValue() / total.intValue()));
        }
        List<StatisticsVerifyScoreDetailResponse> verifyScoreListTotal = new ArrayList<>();
        verifyScoreListTotal.addAll(initList);
        verifyScoreListTotal.removeAll(verifyScoreList);
        verifyScoreListTotal.addAll(verifyScoreList);
        Collections.sort(verifyScoreListTotal, new Comparator<StatisticsVerifyScoreDetailResponse>() { // from class: com.moredian.onpremise.record.service.impl.VerifyRecordServiceImpl.3
            @Override // java.util.Comparator
            public int compare(StatisticsVerifyScoreDetailResponse o1, StatisticsVerifyScoreDetailResponse o2) {
                return o1.getVerifyScore().intValue() - o2.getVerifyScore().intValue();
            }
        });
        List<StatisticsVerifyScoreDetailResponse> secondVerifyScoreListTotal = new ArrayList<>();
        secondVerifyScoreListTotal.addAll(initList);
        secondVerifyScoreListTotal.removeAll(secondVerifyScoreList);
        secondVerifyScoreListTotal.addAll(secondVerifyScoreList);
        Collections.sort(secondVerifyScoreListTotal, new Comparator<StatisticsVerifyScoreDetailResponse>() { // from class: com.moredian.onpremise.record.service.impl.VerifyRecordServiceImpl.4
            @Override // java.util.Comparator
            public int compare(StatisticsVerifyScoreDetailResponse o1, StatisticsVerifyScoreDetailResponse o2) {
                return o1.getVerifyScore().intValue() - o2.getVerifyScore().intValue();
            }
        });
        statisticsVerifyScoreV2Response.setVerifyScoreList(verifyScoreListTotal);
        statisticsVerifyScoreV2Response.setSecondVerifyScoreList(secondVerifyScoreListTotal);
        Integer totalMirror = this.verifyRecordMapper.countStatisticsMirrorVerifyScore(request);
        if (totalMirror.intValue() == 0) {
            statisticsVerifyScoreV2Response.setMirrorVerifyScoreList(new ArrayList<>());
        } else {
            List<StatisticsVerifyScoreDetailResponse> mirrorVerifyScoreList = this.verifyRecordMapper.statisticsMirrorVerifyScore(request);
            for (StatisticsVerifyScoreDetailResponse statisticsVerifyScoreDetailResponse4 : mirrorVerifyScoreList) {
                statisticsVerifyScoreDetailResponse4.setPercent(df.format(statisticsVerifyScoreDetailResponse4.getRecordCount().doubleValue() / total.intValue()));
            }
            List<StatisticsVerifyScoreDetailResponse> mirrorVerifyScoreListTotal = new ArrayList<>();
            mirrorVerifyScoreListTotal.addAll(initList);
            mirrorVerifyScoreListTotal.removeAll(mirrorVerifyScoreList);
            mirrorVerifyScoreListTotal.addAll(mirrorVerifyScoreList);
            Collections.sort(mirrorVerifyScoreListTotal, new Comparator<StatisticsVerifyScoreDetailResponse>() { // from class: com.moredian.onpremise.record.service.impl.VerifyRecordServiceImpl.5
                @Override // java.util.Comparator
                public int compare(StatisticsVerifyScoreDetailResponse o1, StatisticsVerifyScoreDetailResponse o2) {
                    return o1.getVerifyScore().intValue() - o2.getVerifyScore().intValue();
                }
            });
            statisticsVerifyScoreV2Response.setMirrorVerifyScoreList(mirrorVerifyScoreListTotal);
        }
        return statisticsVerifyScoreV2Response;
    }
}
