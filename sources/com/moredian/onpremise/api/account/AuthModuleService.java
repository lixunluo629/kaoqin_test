package com.moredian.onpremise.api.account;

import com.moredian.onpremise.core.model.request.BaseRequest;
import com.moredian.onpremise.core.model.request.SaveModuleRequest;
import com.moredian.onpremise.core.model.response.AuthModuleResponse;
import java.util.List;

/* loaded from: onpremise-api-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/api/account/AuthModuleService.class */
public interface AuthModuleService {
    List<AuthModuleResponse> listModule(BaseRequest baseRequest);

    Long saveModule(SaveModuleRequest saveModuleRequest);
}
