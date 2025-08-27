package com.moredian.onpremise.api.group;

import com.moredian.onpremise.core.model.request.AppRequest;
import com.moredian.onpremise.core.model.request.DeleteAppRequest;
import com.moredian.onpremise.core.model.request.SaveAppRequest;
import com.moredian.onpremise.core.model.response.AppRemainDayResponse;
import com.moredian.onpremise.core.model.response.AppResponse;
import java.util.List;

/* loaded from: onpremise-api-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/api/group/AppService.class */
public interface AppService {
    List<AppResponse> appList(AppRequest appRequest);

    boolean deleteApp(DeleteAppRequest deleteAppRequest);

    boolean insertApp(SaveAppRequest saveAppRequest);

    boolean updateApp(SaveAppRequest saveAppRequest);

    List<AppRemainDayResponse> appRemainDay(Long l);
}
