package com.moredian.onpremise.record.service.impl;

import com.github.pagehelper.PageHelper;
import com.moredian.onpremise.api.record.WarnRecordService;
import com.moredian.onpremise.core.common.enums.CallbackTagEnum;
import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.config.UploadConfig;
import com.moredian.onpremise.core.mapper.CallbackServerMapper;
import com.moredian.onpremise.core.mapper.DeviceMapper;
import com.moredian.onpremise.core.mapper.WarnRecordMapper;
import com.moredian.onpremise.core.model.domain.CallbackServer;
import com.moredian.onpremise.core.model.domain.Device;
import com.moredian.onpremise.core.model.domain.WarnRecord;
import com.moredian.onpremise.core.model.request.BaseRequest;
import com.moredian.onpremise.core.model.request.WarnCallbackRequest;
import com.moredian.onpremise.core.model.request.WarnRecordBatchSaveRequest;
import com.moredian.onpremise.core.model.request.WarnRecordListRequest;
import com.moredian.onpremise.core.model.request.WarnRecordSaveRequest;
import com.moredian.onpremise.core.model.response.WarnRecordResponse;
import com.moredian.onpremise.core.utils.AssertUtil;
import com.moredian.onpremise.core.utils.HttpUtils;
import com.moredian.onpremise.core.utils.JsonUtils;
import com.moredian.onpremise.core.utils.MyDateUtils;
import com.moredian.onpremise.core.utils.MyListUtils;
import com.moredian.onpremise.core.utils.PageList;
import com.moredian.onpremise.core.utils.Paginator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
/* loaded from: onpremise-record-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/record/service/impl/WarnRecordServiceImpl.class */
public class WarnRecordServiceImpl implements WarnRecordService {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) VerifyRecordServiceImpl.class);

    @Autowired
    private WarnRecordMapper warnRecordMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private UploadConfig uplocadConfig;

    @Autowired
    private CallbackServerMapper callbackServerMapper;

    @Override // com.moredian.onpremise.api.record.WarnRecordService
    public PageList<WarnRecordResponse> recordList(WarnRecordListRequest request) {
        if (Paginator.checkPaginator(request.getPaginator())) {
            PageHelper.startPage(request.getPaginator().getPageNum(), request.getPaginator().getPageSize());
            return new PageList<>(this.warnRecordMapper.getWarnRecordList(request));
        }
        List<WarnRecordResponse> listResp = this.warnRecordMapper.getWarnRecordList(request);
        return new PageList<>(Paginator.initPaginator(listResp), listResp);
    }

    @Override // com.moredian.onpremise.api.record.WarnRecordService
    public boolean saveRecord(WarnRecordSaveRequest request) {
        AssertUtil.isNullOrEmpty(request.getDeviceSn(), OnpremiseErrorEnum.DEVICE_SN_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getWarnType(), OnpremiseErrorEnum.MEMBER_VERIFY_TIME_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getWarnTime(), OnpremiseErrorEnum.MEMBER_ID_MUST_NOT_NULL);
        Device device = this.deviceMapper.getDeviceInfoByDeviceSn(request.getDeviceSn());
        AssertUtil.isNullOrEmpty(device, OnpremiseErrorEnum.DEVICE_NOT_FIND);
        WarnRecord record = new WarnRecord();
        record.setDeviceSn(request.getDeviceSn());
        record.setDeviceId(device.getDeviceId());
        record.setDeviceName(device.getDeviceName());
        record.setOrgId(device.getOrgId());
        record.setWarnTypeCode(request.getWarnTypeCode());
        record.setWarnType(request.getWarnType());
        record.setWarnTime(MyDateUtils.getDate(request.getWarnTime().longValue()));
        if (request.getSnapFaceBase64() != null && request.getSnapFaceBase64().trim().length() > 0) {
            record.setSnapFaceUrl(this.uplocadConfig.uploadImageForReturnUrl(request.getSnapFaceBase64()));
        }
        AssertUtil.isTrue(Boolean.valueOf(this.warnRecordMapper.saveWarnRecord(record) > 0), OnpremiseErrorEnum.SAVE_WARN_RECORD_FAIL);
        sendCallback(record, CallbackTagEnum.WARN_EVENT.getTag());
        return true;
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.moredian.onpremise.record.service.impl.WarnRecordServiceImpl$1] */
    private void sendCallback(final WarnRecord record, final String tag) {
        new Thread() { // from class: com.moredian.onpremise.record.service.impl.WarnRecordServiceImpl.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    CallbackServer callbackServer = WarnRecordServiceImpl.this.callbackServerMapper.getOneByTag(record.getOrgId(), tag);
                    if (callbackServer != null) {
                        WarnCallbackRequest request = new WarnCallbackRequest();
                        BeanUtils.copyProperties(record, request);
                        request.setWarnTimeMillis(Long.valueOf(record.getWarnTime().getTime()));
                        String[] urls = callbackServer.getCallbackUrl().split(",");
                        if (urls != null && urls.length > 0) {
                            for (String url : urls) {
                                HttpUtils.sendURLPost(url, JsonUtils.toJson(request));
                            }
                        }
                    }
                } catch (Exception e) {
                    WarnRecordServiceImpl.logger.error("error:{}", (Throwable) e);
                }
            }
        }.start();
    }

    @Override // com.moredian.onpremise.api.record.WarnRecordService
    public String excelRecord(WarnRecordListRequest request) {
        List<WarnRecordResponse> listResp = this.warnRecordMapper.getWarnRecordList(request);
        return JsonUtils.toJson(listResp);
    }

    @Override // com.moredian.onpremise.api.record.WarnRecordService
    public List<String> getWarnTypes(BaseRequest request) {
        return this.warnRecordMapper.getWarnTypes(request.getOrgId());
    }

    @Override // com.moredian.onpremise.api.record.WarnRecordService
    public boolean batchSaveRecord(WarnRecordBatchSaveRequest request) {
        if (MyListUtils.listIsEmpty(request.getSaveRequests())) {
            for (WarnRecordSaveRequest saveRequest : request.getSaveRequests()) {
                try {
                    saveRecord(saveRequest);
                } catch (Exception e) {
                    logger.info("========backups record fail ");
                    try {
                        saveRecord(saveRequest);
                    } catch (Exception e2) {
                        logger.info("========retry backups record fail ");
                    }
                }
            }
            return true;
        }
        return true;
    }

    private void packagingList(List<WarnRecordResponse> listResp, List<WarnRecord> listRecord) {
        if (listRecord != null && listRecord.size() != 0) {
            for (int i = 0; i < listRecord.size(); i++) {
                WarnRecord record = listRecord.get(i);
                WarnRecordResponse response = new WarnRecordResponse();
                response.setWarnRecordId(record.getWarnRecordId());
                response.setSnapFaceUrl(record.getSnapFaceUrl());
                response.setDeviceName(record.getDeviceName());
                response.setWarnTime(record.getWarnTime());
                response.setWarnType(record.getWarnType());
                listResp.add(response);
            }
        }
    }
}
