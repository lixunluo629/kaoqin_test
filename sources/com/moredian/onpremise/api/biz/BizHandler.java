package com.moredian.onpremise.api.biz;

import com.moredian.onpremise.api.checkIn.CheckInService;
import com.moredian.onpremise.core.common.enums.AppTypeEnum;
import com.moredian.onpremise.core.mapper.AppMapper;
import com.moredian.onpremise.core.model.request.BizCheckRequest;
import com.moredian.onpremise.core.model.request.CheckInLogRequest;
import com.moredian.onpremise.core.model.response.AppResponse;
import com.moredian.onpremise.core.model.response.BizCheckResponse;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
/* loaded from: onpremise-api-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/api/biz/BizHandler.class */
public class BizHandler {

    @Autowired
    private CheckInService checkInService;

    @Autowired
    private AppMapper appMapper;

    public List<BizCheckResponse> bizCheck(BizCheckRequest request) {
        List<BizCheckResponse> responseList = new ArrayList<>();
        List<AppResponse> appList = this.appMapper.getAppList(request.getOrgId(), null);
        for (AppResponse app : appList) {
            BizCheckResponse response = null;
            AppTypeEnum appTypeEnum = AppTypeEnum.getByValue(app.getAppType().intValue());
            switch (appTypeEnum) {
                case QD:
                    CheckInLogRequest checkInLogRequest = new CheckInLogRequest();
                    checkInLogRequest.setDeviceSn(request.getDeviceSn());
                    checkInLogRequest.setMemberId(request.getMemberId());
                    checkInLogRequest.setCheckInTime(request.getVerifyTime());
                    checkInLogRequest.setOrgId(request.getOrgId());
                    response = this.checkInService.hasCheck(checkInLogRequest);
                    break;
            }
            if (response != null) {
                responseList.add(response);
            }
        }
        return responseList;
    }
}
