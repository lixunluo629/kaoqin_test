package com.moredian.onpremise.temperature.impl;

import com.github.pagehelper.PageHelper;
import com.moredian.onpremise.api.member.DeptService;
import com.moredian.onpremise.api.temperature.TemperatureConfigService;
import com.moredian.onpremise.api.temperature.TemperatureRecordService;
import com.moredian.onpremise.core.mapper.TemperatureRecordMapper;
import com.moredian.onpremise.core.model.domain.TemperatureRecord;
import com.moredian.onpremise.core.model.request.TemperatureRecordRequest;
import com.moredian.onpremise.core.model.response.TemperatureConfigResponse;
import com.moredian.onpremise.core.model.response.TemperatureRecordCountResponse;
import com.moredian.onpremise.core.model.response.TemperatureRecordResponse;
import com.moredian.onpremise.core.model.response.UserLoginResponse;
import com.moredian.onpremise.core.utils.MyDateUtils;
import com.moredian.onpremise.core.utils.PageList;
import com.moredian.onpremise.core.utils.Paginator;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
/* loaded from: onpremise-temperature-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/temperature/impl/TemperatureRecordServiceImpl.class */
public class TemperatureRecordServiceImpl implements TemperatureRecordService {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) TemperatureRecordServiceImpl.class);

    @Autowired
    private TemperatureRecordMapper temperatureRecordMapper;

    @Autowired
    private TemperatureConfigService temperatureConfigService;

    @Autowired
    private DeptService deptService;

    @Override // com.moredian.onpremise.api.temperature.TemperatureRecordService
    public int insert(TemperatureRecord record) {
        TemperatureConfigResponse configResponse = this.temperatureConfigService.queryConfigWithoutDevice();
        Integer temperatureStatus = Integer.valueOf(record.getTemperatureValue().compareTo(configResponse.getTemperatureAlert()) < 0 ? 1 : 2);
        record.setTemperatureStatus(temperatureStatus);
        record.setVerifyDay(MyDateUtils.formatIntegerDay(record.getVerifyTime()));
        return this.temperatureRecordMapper.insert(record);
    }

    @Override // com.moredian.onpremise.api.temperature.TemperatureRecordService
    public PageList<TemperatureRecordResponse> queryRecord(TemperatureRecordRequest request) {
        request.setManageDeptIdList(UserLoginResponse.getAccountManageDeptId(request.getSessionId()));
        request.setManageDeviceSnList(UserLoginResponse.getAccountManageDeviceSn(request.getSessionId()));
        if (request.getDeptId() != null) {
            List<Long> deptIds = new ArrayList<>();
            deptIds.add(request.getDeptId());
            request.setDeptIdList(this.deptService.packagingChildDept(request.getDeptId(), request.getOrgId(), deptIds));
        }
        request.setStartDatetime(MyDateUtils.isValidDate(request.getStartDatetime()) ? request.getStartDatetime() + MyDateUtils.TIME_OF_DAY_BEGIN : request.getStartDatetime());
        request.setEndDatetime(MyDateUtils.isValidDate(request.getEndDatetime()) ? request.getEndDatetime() + MyDateUtils.TIME_OF_DAY_END : request.getEndDatetime());
        if (Paginator.checkPaginator(request.getPaginator())) {
            PageHelper.startPage(request.getPaginator().getPageNum(), request.getPaginator().getPageSize());
            return new PageList<>(this.temperatureRecordMapper.listRecord(request));
        }
        List<TemperatureRecordResponse> listResp = this.temperatureRecordMapper.listRecord(request);
        return new PageList<>(Paginator.initPaginator(listResp), listResp);
    }

    @Override // com.moredian.onpremise.api.temperature.TemperatureRecordService
    public PageList<TemperatureRecordCountResponse> queryRecordCount(TemperatureRecordRequest request) {
        request.setManageDeptIdList(UserLoginResponse.getAccountManageDeptId(request.getSessionId()));
        request.setManageDeviceSnList(UserLoginResponse.getAccountManageDeviceSn(request.getSessionId()));
        if (request.getDeptId() != null) {
            List<Long> deptIds = new ArrayList<>();
            deptIds.add(request.getDeptId());
            request.setDeptIdList(this.deptService.packagingChildDept(request.getDeptId(), request.getOrgId(), deptIds));
        }
        request.setStartDatetime(MyDateUtils.isValidDate(request.getStartDatetime()) ? request.getStartDatetime() + MyDateUtils.TIME_OF_DAY_BEGIN : request.getStartDatetime());
        request.setEndDatetime(MyDateUtils.isValidDate(request.getEndDatetime()) ? request.getEndDatetime() + MyDateUtils.TIME_OF_DAY_END : request.getEndDatetime());
        if (Paginator.checkPaginator(request.getPaginator())) {
            PageHelper.startPage(request.getPaginator().getPageNum(), request.getPaginator().getPageSize());
            return new PageList<>(this.temperatureRecordMapper.listRecordCount(request));
        }
        List<TemperatureRecordCountResponse> listResp = this.temperatureRecordMapper.listRecordCount(request);
        return new PageList<>(Paginator.initPaginator(listResp), listResp);
    }
}
