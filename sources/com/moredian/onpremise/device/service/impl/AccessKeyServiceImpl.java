package com.moredian.onpremise.device.service.impl;

import com.moredian.onpremise.api.device.AccessKeyService;
import com.moredian.onpremise.core.mapper.AccessKeyMapper;
import com.moredian.onpremise.core.model.domain.AccessKey;
import com.moredian.onpremise.core.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;

/* loaded from: onpremise-device-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/device/service/impl/AccessKeyServiceImpl.class */
public class AccessKeyServiceImpl implements AccessKeyService {

    @Autowired
    private AccessKeyMapper accessKeyMapper;

    public boolean insertAccessKey() {
        return true;
    }

    public boolean insertAccessKeys() {
        return true;
    }

    public void getOneBySecret() {
    }

    public String secretToToken(AccessKey accessKey) {
        String token = JwtUtils.token(accessKey.getAccessKeySecret(), accessKey.getAccessKeySecret(), accessKey.getGmtValidEnd());
        return token;
    }
}
