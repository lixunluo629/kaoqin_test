package com.moredian.onpremise.api.account;

import com.moredian.onpremise.core.model.request.ListAuthCodeRequest;
import com.moredian.onpremise.core.model.request.SaveAuthCodeRequest;
import com.moredian.onpremise.core.model.response.ListAuthCodeResponse;
import com.moredian.onpremise.core.model.response.SaveAuthCodeResponse;
import com.moredian.onpremise.core.utils.PageList;

/* loaded from: onpremise-api-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/api/account/AuthCodeService.class */
public interface AuthCodeService {
    SaveAuthCodeResponse save(SaveAuthCodeRequest saveAuthCodeRequest);

    PageList<ListAuthCodeResponse> listAuthCode(ListAuthCodeRequest listAuthCodeRequest);
}
