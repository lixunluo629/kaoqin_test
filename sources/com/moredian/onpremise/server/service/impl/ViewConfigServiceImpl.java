package com.moredian.onpremise.server.service.impl;

import com.github.pagehelper.PageHelper;
import com.moredian.onpremise.api.server.ViewConfigService;
import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.common.enums.ViewConfigTypeEnum;
import com.moredian.onpremise.core.mapper.ViewConfigMapper;
import com.moredian.onpremise.core.model.domain.ViewConfig;
import com.moredian.onpremise.core.model.request.ViewConfigListRequest;
import com.moredian.onpremise.core.model.request.ViewConfigRequest;
import com.moredian.onpremise.core.model.response.ViewConfigResponse;
import com.moredian.onpremise.core.utils.AssertUtil;
import com.moredian.onpremise.core.utils.MyFileUtils;
import com.moredian.onpremise.core.utils.PageList;
import com.moredian.onpremise.core.utils.Paginator;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
/* loaded from: onpremise-server-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/server/service/impl/ViewConfigServiceImpl.class */
public class ViewConfigServiceImpl implements ViewConfigService {

    @Autowired
    private ViewConfigMapper viewConfigMapper;

    @Value("${onpremise.file.path}")
    private String saveFilePath;

    @Value("${onpremise.save.view.config.path}")
    private String saveViewConfigPath;

    @Override // com.moredian.onpremise.api.server.ViewConfigService
    public PageList<ViewConfigResponse> viewConfigPageList(ViewConfigListRequest request) {
        Paginator paginator = request.getPaginator();
        if (Paginator.checkPaginator(paginator)) {
            PageHelper.startPage(paginator.getPageNum(), paginator.getPageSize());
            return new PageList<>(this.viewConfigMapper.viewConfigGetList(request));
        }
        List<ViewConfigResponse> responses = this.viewConfigMapper.viewConfigGetList(request);
        return new PageList<>(Paginator.initPaginator(responses), responses);
    }

    @Override // com.moredian.onpremise.api.server.ViewConfigService
    public Long viewConfigAdd(ViewConfigRequest request) throws BeansException {
        checkValid(request);
        ViewConfig viewConfig = new ViewConfig();
        BeanUtils.copyProperties(request, viewConfig);
        this.viewConfigMapper.viewConfigAdd(viewConfig);
        return viewConfig.getId();
    }

    private void checkValid(ViewConfigRequest request) {
        Integer type = request.getType();
        AssertUtil.isNullOrEmpty(type, OnpremiseErrorEnum.VIEW_CONFIG_TYPE_NULL);
        ViewConfigTypeEnum viewConfigTypeEnum = ViewConfigTypeEnum.getByValue(type.intValue());
        ViewConfigListRequest viewConfigListRequest = new ViewConfigListRequest();
        viewConfigListRequest.setType(type);
        new ArrayList();
        switch (viewConfigTypeEnum) {
            case LOGIN:
                List<ViewConfigResponse> viewConfigResponseList = this.viewConfigMapper.viewConfigGetList(viewConfigListRequest);
                if (request.getId() != null) {
                    ViewConfigResponse viewConfigResponse = viewConfigResponseList.get(0);
                    AssertUtil.isTrue(Boolean.valueOf(viewConfigResponse.getId().equals(request.getId())), OnpremiseErrorEnum.VIEW_CONFIG_LOGIN_ALREADY_EXIST);
                    break;
                } else {
                    AssertUtil.isTrue(Boolean.valueOf(CollectionUtils.isEmpty(viewConfigResponseList)), OnpremiseErrorEnum.VIEW_CONFIG_LOGIN_ALREADY_EXIST);
                    break;
                }
            case APP:
                AssertUtil.isNullOrEmpty(type, OnpremiseErrorEnum.VIEW_CONFIG_BIZ_KEY_NULL);
                viewConfigListRequest.setBizKey(request.getBizKey());
                List<ViewConfigResponse> viewConfigResponseList2 = this.viewConfigMapper.viewConfigGetList(viewConfigListRequest);
                if (request.getId() != null) {
                    ViewConfigResponse viewConfigResponse2 = viewConfigResponseList2.get(0);
                    AssertUtil.isTrue(Boolean.valueOf(viewConfigResponse2.getId().equals(request.getId())), OnpremiseErrorEnum.VIEW_CONFIG_APP_ALREADY_EXIST);
                    break;
                } else {
                    AssertUtil.isTrue(Boolean.valueOf(CollectionUtils.isEmpty(viewConfigResponseList2)), OnpremiseErrorEnum.VIEW_CONFIG_APP_ALREADY_EXIST);
                    break;
                }
        }
    }

    @Override // com.moredian.onpremise.api.server.ViewConfigService
    public Long viewConfigDelete(ViewConfigRequest request) {
        this.viewConfigMapper.viewConfigDeleteById(request.getOrgId(), request.getId());
        return null;
    }

    @Override // com.moredian.onpremise.api.server.ViewConfigService
    public Long viewConfigEdit(ViewConfigRequest request) throws BeansException {
        checkValid(request);
        ViewConfig viewConfig = new ViewConfig();
        BeanUtils.copyProperties(request, viewConfig);
        this.viewConfigMapper.viewConfigEdit(viewConfig);
        return viewConfig.getId();
    }

    @Override // com.moredian.onpremise.api.server.ViewConfigService
    public ViewConfigResponse viewConfigGetOne(ViewConfigRequest request) {
        ViewConfigResponse response = this.viewConfigMapper.viewConfigGetOne(request.getOrgId(), request.getId());
        return response;
    }

    @Override // com.moredian.onpremise.api.server.ViewConfigService
    public ViewConfigResponse viewConfigGetLogin() {
        ViewConfigResponse response = this.viewConfigMapper.viewConfigGetLogin();
        return response;
    }

    @Override // com.moredian.onpremise.api.server.ViewConfigService
    public List<ViewConfigResponse> viewConfigGetList(ViewConfigListRequest request) {
        List<ViewConfigResponse> viewConfigResponseList = this.viewConfigMapper.viewConfigGetList(request);
        return viewConfigResponseList;
    }

    @Override // com.moredian.onpremise.api.server.ViewConfigService
    public String viewConfigUploadImg(MultipartFile file) {
        String filePath = MyFileUtils.upload(file, this.saveViewConfigPath);
        return (this.saveViewConfigPath + filePath).replace(this.saveFilePath, "/");
    }
}
