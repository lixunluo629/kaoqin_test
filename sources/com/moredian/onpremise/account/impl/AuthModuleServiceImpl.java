package com.moredian.onpremise.account.impl;

import com.moredian.onpremise.api.account.AuthModuleService;
import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.mapper.AuthModuleMapper;
import com.moredian.onpremise.core.model.domain.AuthModule;
import com.moredian.onpremise.core.model.request.BaseRequest;
import com.moredian.onpremise.core.model.request.SaveModuleRequest;
import com.moredian.onpremise.core.model.response.AuthModuleResponse;
import com.moredian.onpremise.core.utils.AssertUtil;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
/* loaded from: onpremise-account-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/account/impl/AuthModuleServiceImpl.class */
public class AuthModuleServiceImpl implements AuthModuleService {

    @Autowired
    private AuthModuleMapper authModuleMapper;

    @Override // com.moredian.onpremise.api.account.AuthModuleService
    public List<AuthModuleResponse> listModule(BaseRequest request) {
        return this.authModuleMapper.listById(null, request.getOrgId(), null);
    }

    @Override // com.moredian.onpremise.api.account.AuthModuleService
    public Long saveModule(SaveModuleRequest request) throws BeansException {
        AuthModule module;
        AssertUtil.isNullOrEmpty(request.getModuleName(), OnpremiseErrorEnum.MODULE_NAME_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getModulePath(), OnpremiseErrorEnum.MODULE_PATH_MUST_NOT_NULL);
        if (request.getModuleId() != null && request.getModuleId().longValue() > 0) {
            module = this.authModuleMapper.getOneById(request.getModuleId(), request.getOrgId());
            AssertUtil.isNullOrEmpty(module, OnpremiseErrorEnum.MODULE_NOT_FIND);
            BeanUtils.copyProperties(request, module);
            AssertUtil.isTrue(Boolean.valueOf(this.authModuleMapper.update(module) > 0), OnpremiseErrorEnum.SAVE_MODULE_FAIL);
        } else {
            module = new AuthModule();
            BeanUtils.copyProperties(request, module);
            AssertUtil.isTrue(Boolean.valueOf(this.authModuleMapper.insert(module) > 0), OnpremiseErrorEnum.SAVE_MODULE_FAIL);
        }
        return module.getModuleId();
    }
}
