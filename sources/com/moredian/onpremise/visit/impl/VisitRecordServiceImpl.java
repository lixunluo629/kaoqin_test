package com.moredian.onpremise.visit.impl;

import com.github.pagehelper.PageHelper;
import com.moredian.FaceDet;
import com.moredian.onpremise.api.member.DeptService;
import com.moredian.onpremise.api.visit.VisitRecordService;
import com.moredian.onpremise.core.adapter.CacheAdapter;
import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.config.UploadConfig;
import com.moredian.onpremise.core.exception.BizException;
import com.moredian.onpremise.core.mapper.ExternalContactMapper;
import com.moredian.onpremise.core.mapper.MemberMapper;
import com.moredian.onpremise.core.mapper.VisitDeviceMapper;
import com.moredian.onpremise.core.mapper.VisitRecordDetailMapper;
import com.moredian.onpremise.core.mapper.VisitRecordMapper;
import com.moredian.onpremise.core.model.domain.ExternalContact;
import com.moredian.onpremise.core.model.domain.Member;
import com.moredian.onpremise.core.model.domain.VisitRecord;
import com.moredian.onpremise.core.model.domain.VisitRecordDetail;
import com.moredian.onpremise.core.model.info.DeviceLastModifyTimeInfo;
import com.moredian.onpremise.core.model.request.SaveVisitRecordDetailRequest;
import com.moredian.onpremise.core.model.request.TerminalSyncExternalContactDetailRequest;
import com.moredian.onpremise.core.model.request.TerminalSyncRequest;
import com.moredian.onpremise.core.model.request.TerminalVisitRecordRequest;
import com.moredian.onpremise.core.model.request.VisitRecordDetailListRequest;
import com.moredian.onpremise.core.model.request.VisitRecordListRequest;
import com.moredian.onpremise.core.model.response.TerminalSyncExternalContactDetailResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncResponse;
import com.moredian.onpremise.core.model.response.VisitRecordDetailResponse;
import com.moredian.onpremise.core.model.response.VisitRecordResponse;
import com.moredian.onpremise.core.utils.AssertUtil;
import com.moredian.onpremise.core.utils.CacheKeyGenerateUtils;
import com.moredian.onpremise.core.utils.IntByteUtils;
import com.moredian.onpremise.core.utils.MyBase64Utils;
import com.moredian.onpremise.core.utils.MyDateUtils;
import com.moredian.onpremise.core.utils.MyFileUtils;
import com.moredian.onpremise.core.utils.PageList;
import com.moredian.onpremise.core.utils.Paginator;
import com.moredian.onpremise.iot.handle.NettyMessageApi;
import com.moredian.onpremise.model.SyncExternalContactRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
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
/* loaded from: onpremise-visit-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/visit/impl/VisitRecordServiceImpl.class */
public class VisitRecordServiceImpl implements VisitRecordService {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) VisitRecordServiceImpl.class);
    private static volatile List<Long> memberIds = new ArrayList();
    private static volatile int syncExternalContactCount = 0;
    private static volatile int syncExternalContactEigenvalueValueCount = 0;

    @Autowired
    private VisitRecordMapper visitRecordMapper;

    @Autowired
    private ExternalContactMapper externalContactMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private UploadConfig uploadConfig;

    @Autowired
    private NettyMessageApi nettyMessageApi;

    @Autowired
    private VisitDeviceMapper visitDeviceMapper;

    @Autowired
    private VisitRecordDetailMapper visitRecordDetailMapper;

    @Autowired
    private DeptService deptService;

    @Override // com.moredian.onpremise.api.visit.VisitRecordService
    @Transactional(rollbackFor = {Exception.class})
    public Long addVisitRecord(TerminalVisitRecordRequest request) throws BeansException {
        checkVisitRecordParams(request);
        loadAllMemberFeature(null);
        String eigenvalue = doExtractFeature(this.uploadConfig.getFilePath() + request.getVisitorFaceUrl());
        ExternalContact externalContact = new ExternalContact();
        externalContact.setOrgId(request.getOrgId());
        externalContact.setName(request.getVisitorName());
        externalContact.setIdCard(request.getVisitorIdCard());
        externalContact.setMobile(request.getVisitorMobile());
        externalContact.setFaceUrl(request.getVisitorFaceUrl());
        externalContact.setEigenvalueValue(eigenvalue);
        this.externalContactMapper.insertOne(externalContact);
        VisitRecord visitRecord = new VisitRecord();
        BeanUtils.copyProperties(request, visitRecord);
        visitRecord.setExternalContactId(externalContact.getId());
        visitRecord.setSignDate(MyDateUtils.formatIntegerDay(Long.valueOf(System.currentTimeMillis())));
        visitRecord.setSignTime(Long.valueOf(System.currentTimeMillis()));
        if (!StringUtils.isEmpty(request.getIntervieweeName())) {
            Member member = this.memberMapper.getMemberInfoByName(request.getIntervieweeName(), request.getOrgId());
            visitRecord.setIntervieweeDeptId(member.getDeptId());
            visitRecord.setIntervieweeDeptName(this.deptService.packagingDeptName(member.getDeptId(), false));
        }
        this.visitRecordMapper.insertOne(visitRecord);
        doSendNettyMsg(request.getOrgId());
        return visitRecord.getId();
    }

    private void checkVisitRecordParams(TerminalVisitRecordRequest request) {
        AssertUtil.isNullOrEmpty(request.getVisitorName(), OnpremiseErrorEnum.VISIT_NAME_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getVisitorMobile(), OnpremiseErrorEnum.VISIT_MOBILE_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getVisitorIdCard(), OnpremiseErrorEnum.VISIT_ID_CARD_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getVisitorFaceUrl(), OnpremiseErrorEnum.VISIT_FACE_URL_MUST_NOT_NULL);
        ExternalContact externalContact = this.externalContactMapper.getOneByIdCard(request.getOrgId(), request.getVisitorIdCard());
        AssertUtil.isTrue(Boolean.valueOf(externalContact == null), OnpremiseErrorEnum.VISIT_ALREADY_EXIST);
        if (!StringUtils.isEmpty(request.getIntervieweeName())) {
            Member member = this.memberMapper.getMemberInfoByName(request.getIntervieweeName(), request.getOrgId());
            AssertUtil.isTrue(Boolean.valueOf(member != null), OnpremiseErrorEnum.INTERVIEWEE_NOT_EXIST);
            AssertUtil.isTrue(Boolean.valueOf(StringUtils.isEmpty(member.getMemberMobile()) || member.getMemberMobile().equals(request.getIntervieweeMobile())), OnpremiseErrorEnum.INTERVIEWEE_MOBILE_NAME_NOT_MATCH);
        }
    }

    private void doSendNettyMsg(Long orgId) {
        ExternalContact externalContact = this.externalContactMapper.getLastModify(orgId);
        List<String> deviceSnList = this.visitDeviceMapper.getListDeviceSnByConfigId(orgId, 1L);
        if (CollectionUtils.isEmpty(deviceSnList)) {
            return;
        }
        this.nettyMessageApi.sendMsg(new SyncExternalContactRequest(Long.valueOf(externalContact == null ? 0L : externalContact.getGmtModify().getTime()), orgId), Integer.valueOf(SyncExternalContactRequest.MODEL_TYPE.type()), deviceSnList);
    }

    private String doExtractFeature(String path) {
        String strEncodeStringForString;
        try {
            BufferedImage image = ImageIO.read(new File(path));
            byte[] buffer = MyFileUtils.getMatrixBGR(image);
            logger.info("=====> buffer size :{}", Integer.valueOf(buffer.length));
            synchronized (this) {
                byte[] bytes = FaceDet.ExtractFeatureByImage(buffer, image.getWidth(), image.getHeight());
                if (bytes != null && bytes.length == 4) {
                    long code = IntByteUtils.byteArrayToLong(bytes);
                    logger.error("=====> doExtractFeature errorCode:{}", Long.valueOf(code));
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
                strEncodeStringForString = MyBase64Utils.encodeStringForString(bytes);
            }
            return strEncodeStringForString;
        } catch (BizException e) {
            logger.error("=====> extractFeature file:{},fail:{}", path, e.getMessage());
            MyFileUtils.deleteFile(path);
            throw new BizException(e.getErrorCode(), e.getMessage());
        } catch (Exception e2) {
            logger.error("=====> extractFeature file:{},fail:{}", path, e2.getMessage());
            MyFileUtils.deleteFile(path);
            throw new BizException(OnpremiseErrorEnum.SYSTEM_ERROR.getErrorCode(), e2.getMessage());
        }
    }

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
        logger.info("faceDet loadAllFeature:{}", Integer.valueOf(FaceDet.LoadAllFeatures(allFeatures, size, 512)));
        return true;
    }

    @Override // com.moredian.onpremise.api.visit.VisitRecordService
    public PageList<VisitRecordResponse> pageVisitRecord(VisitRecordListRequest request) {
        Paginator paginator = request.getPaginator();
        if (Paginator.checkPaginator(paginator)) {
            PageHelper.startPage(paginator.getPageNum(), paginator.getPageSize());
            List<VisitRecordResponse> responses = this.visitRecordMapper.pageListVisitRecord(request);
            buildList(responses);
            return new PageList<>(responses);
        }
        List<VisitRecordResponse> responses2 = this.visitRecordMapper.pageListVisitRecord(request);
        buildList(responses2);
        return new PageList<>(Paginator.initPaginator(responses2), responses2);
    }

    private void buildList(List<VisitRecordResponse> responses) {
    }

    /* JADX WARN: Finally extract failed */
    @Override // com.moredian.onpremise.api.visit.VisitRecordService
    public TerminalSyncResponse<Long> syncExternalContact(TerminalSyncRequest request) {
        TerminalSyncResponse<Long> terminalSyncResponse = new TerminalSyncResponse<>();
        logger.info("=====> request syncExternalContactCount:{}", Integer.valueOf(syncExternalContactCount));
        if (syncExternalContactCount >= 30) {
            throw new BizException(OnpremiseErrorEnum.TERMINAL_REQUEST_BUSY.getErrorCode(), OnpremiseErrorEnum.TERMINAL_REQUEST_BUSY.getMessage());
        }
        syncExternalContactCount++;
        try {
            try {
                AssertUtil.isTrue(Boolean.valueOf(request.getLastSyncTime() != null), OnpremiseErrorEnum.LAST_SYNC_TIME_MUST_NOT_NULL);
                List<ExternalContact> syncExternalContacts = this.externalContactMapper.syncExternalContact(request.getOrgId(), request.getLastSyncTime());
                if (CollectionUtils.isEmpty(syncExternalContacts)) {
                    if (CacheAdapter.getLastModifyTime(CacheKeyGenerateUtils.getExternalContactLastModifyTimeKey(request.getDeviceSn())) == null) {
                        terminalSyncResponse.setSyncTime(0L);
                        doCacheLastModifyTime(CacheKeyGenerateUtils.getExternalContactLastModifyTimeKey(request.getDeviceSn()), terminalSyncResponse.getSyncTime());
                    } else {
                        terminalSyncResponse.setSyncTime(CacheAdapter.getLastModifyTime(CacheKeyGenerateUtils.getExternalContactLastModifyTimeKey(request.getDeviceSn())).getLastModifyTime());
                    }
                    syncExternalContactCount--;
                    return terminalSyncResponse;
                }
                List<Long> syncInsertExternalContactList = new ArrayList<>();
                List<Long> syncModifyExternalContactList = new ArrayList<>();
                List<Long> syncDeleteExternalContactList = new ArrayList<>();
                for (ExternalContact externalContact : syncExternalContacts) {
                    if (!request.getLastSyncTime().equals(0L) || externalContact.getDeleteOrNot().intValue() != 1) {
                        if (externalContact.getDeleteOrNot().intValue() == 1) {
                            syncDeleteExternalContactList.add(externalContact.getId());
                        } else if (request.getLastSyncTime().longValue() > externalContact.getGmtCreate().getTime()) {
                            syncModifyExternalContactList.add(externalContact.getId());
                        } else {
                            syncInsertExternalContactList.add(externalContact.getId());
                        }
                    }
                }
                terminalSyncResponse.setSyncTime(Long.valueOf(syncExternalContacts.get(syncExternalContacts.size() - 1).getGmtModify().getTime()));
                doCacheLastModifyTime(CacheKeyGenerateUtils.getExternalContactLastModifyTimeKey(request.getDeviceSn()), terminalSyncResponse.getSyncTime());
                terminalSyncResponse.setSyncModifyResult(syncModifyExternalContactList);
                terminalSyncResponse.setSyncInsertResult(syncInsertExternalContactList);
                terminalSyncResponse.setSyncDeleteResult(syncDeleteExternalContactList);
                syncExternalContactCount--;
                return terminalSyncResponse;
            } catch (Exception e) {
                throw e;
            }
        } catch (Throwable th) {
            syncExternalContactCount--;
            throw th;
        }
    }

    /* JADX WARN: Finally extract failed */
    @Override // com.moredian.onpremise.api.visit.VisitRecordService
    public List<TerminalSyncExternalContactDetailResponse> syncExternalContactDetail(TerminalSyncExternalContactDetailRequest request) {
        logger.info("=====> request syncExternalContactEigenvalueValueCount:{}", Integer.valueOf(syncExternalContactEigenvalueValueCount));
        if (syncExternalContactEigenvalueValueCount >= 100) {
            throw new BizException(OnpremiseErrorEnum.TERMINAL_REQUEST_BUSY.getErrorCode(), OnpremiseErrorEnum.TERMINAL_REQUEST_BUSY.getMessage());
        }
        syncExternalContactEigenvalueValueCount++;
        List<TerminalSyncExternalContactDetailResponse> terminalSyncExternalContactDetailResponses = new ArrayList<>();
        try {
            try {
                if (CollectionUtils.isEmpty(request.getIds())) {
                    ArrayList arrayList = new ArrayList();
                    syncExternalContactEigenvalueValueCount--;
                    return arrayList;
                }
                List<ExternalContact> externalContacts = this.externalContactMapper.syncExternalContactEigenvalueValue(request.getOrgId(), request.getIds());
                if (!CollectionUtils.isEmpty(externalContacts)) {
                    for (ExternalContact externalContact : externalContacts) {
                        TerminalSyncExternalContactDetailResponse terminalSyncExternalContactDetailResponse = new TerminalSyncExternalContactDetailResponse();
                        BeanUtils.copyProperties(externalContact, terminalSyncExternalContactDetailResponse);
                        terminalSyncExternalContactDetailResponses.add(terminalSyncExternalContactDetailResponse);
                    }
                }
                syncExternalContactEigenvalueValueCount--;
                return terminalSyncExternalContactDetailResponses;
            } catch (Exception e) {
                throw e;
            }
        } catch (Throwable th) {
            syncExternalContactEigenvalueValueCount--;
            throw th;
        }
    }

    @Override // com.moredian.onpremise.api.visit.VisitRecordService
    public Long addVisitRecordDetail(SaveVisitRecordDetailRequest request) throws BeansException {
        VisitRecordDetail visitRecordDetail = new VisitRecordDetail();
        BeanUtils.copyProperties(request, visitRecordDetail);
        ExternalContact externalContact = this.externalContactMapper.getOneById(request.getOrgId(), request.getMemberId());
        if (externalContact != null) {
            visitRecordDetail.setIdCard(externalContact.getIdCard());
            visitRecordDetail.setMemberName(externalContact.getName());
        }
        visitRecordDetail.setVerifyDate(MyDateUtils.formatIntegerDay(request.getVerifyTime()));
        visitRecordDetail.setVerifyTime(request.getVerifyTime());
        this.visitRecordDetailMapper.insertOne(visitRecordDetail);
        return visitRecordDetail.getId();
    }

    @Override // com.moredian.onpremise.api.visit.VisitRecordService
    public void deleteExpiredVisit(Long orgId) {
        Long expiredTimestamp = Long.valueOf(System.currentTimeMillis());
        List<String> faceUrlList = this.externalContactMapper.getListFaceUrl(orgId, expiredTimestamp);
        for (String faceUrl : faceUrlList) {
            MyFileUtils.deleteFile(this.uploadConfig.getFilePath() + faceUrl);
        }
        this.externalContactMapper.softDeleteExpired(orgId, expiredTimestamp);
        this.visitRecordMapper.deleteExpiredVisitorFaceUrl(orgId, expiredTimestamp);
        doSendNettyMsg(orgId);
    }

    @Override // com.moredian.onpremise.api.visit.VisitRecordService
    public PageList<VisitRecordDetailResponse> pageVisitRecordDetail(VisitRecordDetailListRequest request) {
        Paginator paginator = request.getPaginator();
        if (Paginator.checkPaginator(paginator)) {
            PageHelper.startPage(paginator.getPageNum(), paginator.getPageSize());
            return new PageList<>(this.visitRecordDetailMapper.pageListVisitRecordDetail(request));
        }
        List<VisitRecordDetailResponse> responses = this.visitRecordDetailMapper.pageListVisitRecordDetail(request);
        return new PageList<>(Paginator.initPaginator(responses), responses);
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
