package com.moredian.onpremise.group.service.impl;

import com.moredian.onpremise.api.group.AppService;
import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.mapper.AppMapper;
import com.moredian.onpremise.core.model.request.AppRequest;
import com.moredian.onpremise.core.model.request.DeleteAppRequest;
import com.moredian.onpremise.core.model.request.SaveAppRequest;
import com.moredian.onpremise.core.model.response.AppRemainDayResponse;
import com.moredian.onpremise.core.model.response.AppResponse;
import com.moredian.onpremise.core.model.response.UserLoginResponse;
import com.moredian.onpremise.core.utils.AssertUtil;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
/* loaded from: onpremise-group-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/group/service/impl/AppServiceImpl.class */
public class AppServiceImpl implements AppService {

    @Autowired
    private AppMapper appMapper;

    @Override // com.moredian.onpremise.api.group.AppService
    public List<AppResponse> appList(AppRequest request) {
        return this.appMapper.getAppList(request.getOrgId(), UserLoginResponse.getAccountManageAppId(request.getSessionId()));
    }

    @Override // com.moredian.onpremise.api.group.AppService
    public boolean deleteApp(DeleteAppRequest request) {
        AssertUtil.isTrue(Boolean.valueOf(request.getAppId() != null && request.getAppId().longValue() > 0), OnpremiseErrorEnum.APP_ID_MUST_NOT_NULL);
        AssertUtil.isTrue(Boolean.valueOf(this.appMapper.deleteApp(request) > 0), OnpremiseErrorEnum.DELETE_APP_FAIL);
        return true;
    }

    @Override // com.moredian.onpremise.api.group.AppService
    public boolean insertApp(SaveAppRequest request) {
        saveRequestParams(request);
        AssertUtil.isTrue(Boolean.valueOf(this.appMapper.insertApp(request) > 0), OnpremiseErrorEnum.INSERT_APP_FAIL);
        return true;
    }

    @Override // com.moredian.onpremise.api.group.AppService
    public boolean updateApp(SaveAppRequest request) {
        saveRequestParams(request);
        AssertUtil.isTrue(Boolean.valueOf(this.appMapper.updateApp(request) > 0), OnpremiseErrorEnum.UPDATE_APP_FAIL);
        return true;
    }

    private boolean saveRequestParams(SaveAppRequest request) {
        AssertUtil.isNullOrEmpty(request.getAppName(), OnpremiseErrorEnum.APP_NAME_MUST_NOT_NULL);
        return true;
    }

    @Override // com.moredian.onpremise.api.group.AppService
    public List<AppRemainDayResponse> appRemainDay(Long orgId) {
        int diffDay;
        List<AppRemainDayResponse> appRemainDayResponses = new ArrayList<>();
        List<AppResponse> appResponses = this.appMapper.getAppList(orgId, null);
        if (CollectionUtils.isEmpty(appResponses)) {
            return appRemainDayResponses;
        }
        for (AppResponse item : appResponses) {
            if (item.getAppValid() != null && (diffDay = ((int) ((item.getAppValid().getTime() - System.currentTimeMillis()) / 86400000)) + 1) <= 30) {
                AppRemainDayResponse appRemainDayResponse = new AppRemainDayResponse();
                appRemainDayResponse.setAppType(item.getAppType());
                appRemainDayResponse.setRemainDay(Integer.valueOf(diffDay));
                appRemainDayResponses.add(appRemainDayResponse);
            }
        }
        return appRemainDayResponses;
    }
}
