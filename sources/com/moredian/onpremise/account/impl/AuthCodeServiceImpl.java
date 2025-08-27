package com.moredian.onpremise.account.impl;

import com.github.pagehelper.PageHelper;
import com.moredian.onpremise.api.account.AuthCodeService;
import com.moredian.onpremise.core.common.constants.AuthConstants;
import com.moredian.onpremise.core.common.enums.AppTypeEnum;
import com.moredian.onpremise.core.common.enums.LicenseUnitEnum;
import com.moredian.onpremise.core.common.enums.LicenseVersionEnum;
import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.exception.BizException;
import com.moredian.onpremise.core.mapper.AppMapper;
import com.moredian.onpremise.core.mapper.AuthCodeMapper;
import com.moredian.onpremise.core.model.domain.AuthCode;
import com.moredian.onpremise.core.model.info.LicenseDetailAppInfo;
import com.moredian.onpremise.core.model.info.LicenseDetailInfo;
import com.moredian.onpremise.core.model.request.ListAuthCodeRequest;
import com.moredian.onpremise.core.model.request.SaveAuthCodeRequest;
import com.moredian.onpremise.core.model.response.AppResponse;
import com.moredian.onpremise.core.model.response.AppValidInfoResponse;
import com.moredian.onpremise.core.model.response.ListAuthCodeResponse;
import com.moredian.onpremise.core.model.response.SaveAuthCodeResponse;
import com.moredian.onpremise.core.utils.AssertUtil;
import com.moredian.onpremise.core.utils.AuthCodeUtils;
import com.moredian.onpremise.core.utils.MyListUtils;
import com.moredian.onpremise.core.utils.PageList;
import com.moredian.onpremise.core.utils.Paginator;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
/* loaded from: onpremise-account-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/account/impl/AuthCodeServiceImpl.class */
public class AuthCodeServiceImpl implements AuthCodeService {

    @Autowired
    private AuthCodeMapper authCodeMapper;

    @Value("${onpremise.license.public.key}")
    private String publicKey;

    @Autowired
    private AppMapper appMapper;

    @Override // com.moredian.onpremise.api.account.AuthCodeService
    @Transactional(rollbackFor = {RuntimeException.class})
    public SaveAuthCodeResponse save(SaveAuthCodeRequest request) throws BadPaddingException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        AssertUtil.isNullOrEmpty(request.getAuthCode(), OnpremiseErrorEnum.SYSTEM_AUTH_CODE_MUST_NOT_NULL);
        LicenseDetailInfo licenseDetailInfo = licenseToEntity(request.getAuthCode());
        AssertUtil.isTrue(Boolean.valueOf(this.authCodeMapper.getByCode(request.getAuthCode(), request.getOrgId(), licenseDetailInfo.getGenerateTime()) == null), OnpremiseErrorEnum.SYSTEM_AUTH_CODE_ALREADY_EXIST);
        AuthCode authCode = new AuthCode();
        authCode.setOrgId(request.getOrgId());
        authCode.setAllowMaxNum(licenseDetailInfo.getMaxNum());
        authCode.setValidStartTime(licenseDetailInfo.getStartTime());
        authCode.setValidEndTime(licenseDetailInfo.getEndTime());
        authCode.setAuthCode(request.getAuthCode());
        if (LicenseVersionEnum.V1.getDescription().equals(licenseDetailInfo.getVersion())) {
            authCode.setAllowModule(licenseDetailInfo.getModuleTypes());
        } else if (LicenseVersionEnum.V2.getDescription().equals(licenseDetailInfo.getVersion())) {
            List<LicenseDetailAppInfo> licenseDetailAppInfos = licenseDetailInfo.getLicenseDetailApps();
            StringBuffer appsSb = new StringBuffer("");
            if (!CollectionUtils.isEmpty(licenseDetailAppInfos)) {
                for (LicenseDetailAppInfo item : licenseDetailAppInfos) {
                    appsSb.append(item.getAppType()).append(",");
                }
                authCode.setAllowModule(StringUtils.isEmpty(appsSb.toString()) ? "" : appsSb.substring(0, appsSb.length() - 1));
            }
        }
        authCode.setGenerateTime(licenseDetailInfo.getGenerateTime());
        this.authCodeMapper.insert(authCode);
        updateForOpen(request.getOrgId(), licenseDetailInfo);
        SaveAuthCodeResponse response = new SaveAuthCodeResponse();
        response.setAllowMaxNum(authCode.getAllowMaxNum());
        response.setValidEndTime(authCode.getValidEndTime());
        response.setAllowModule(licenseDetailInfo.getDescription());
        return response;
    }

    private void updateForOpen(Long orgId, LicenseDetailInfo licenseDetailInfo) {
        if (LicenseVersionEnum.V1.getDescription().equals(licenseDetailInfo.getVersion())) {
            if (!StringUtils.isEmpty(licenseDetailInfo.getModuleTypes())) {
                String[] modules = licenseDetailInfo.getModuleTypes().split(",");
                if (MyListUtils.arrayIsEmpty(modules)) {
                    this.appMapper.updateForOpen(Arrays.asList(modules), null, orgId);
                    return;
                }
                return;
            }
            return;
        }
        if (LicenseVersionEnum.V2.getDescription().equals(licenseDetailInfo.getVersion()) && !CollectionUtils.isEmpty(licenseDetailInfo.getLicenseDetailApps())) {
            for (LicenseDetailAppInfo item : licenseDetailInfo.getLicenseDetailApps()) {
                if (item.getAppType().equals(Integer.valueOf(AppTypeEnum.MJ.getValue()))) {
                    this.appMapper.updateForOpen(null, String.valueOf(item.getAppType()), orgId);
                } else {
                    AppResponse appResponse = this.appMapper.getAppOneByType(orgId, item.getAppType());
                    if (appResponse != null) {
                        Calendar calendar = Calendar.getInstance();
                        LicenseUnitEnum licenseUnitEnum = LicenseUnitEnum.getByKey(item.getAppUnit());
                        if (appResponse.getAppValid() == null || appResponse.getAppValid().getTime() < System.currentTimeMillis()) {
                            calendar.setTime(new Date());
                            updateForOpenValid(orgId, item, calendar, licenseUnitEnum);
                        } else {
                            calendar.setTime(appResponse.getAppValid());
                            updateForOpenValid(orgId, item, calendar, licenseUnitEnum);
                        }
                    }
                }
            }
        }
    }

    private void updateForOpenValid(Long orgId, LicenseDetailAppInfo item, Calendar calendar, LicenseUnitEnum licenseUnitEnum) {
        calendar.set(11, 23);
        calendar.set(12, 59);
        calendar.set(13, 59);
        switch (licenseUnitEnum) {
            case YEAR:
                calendar.add(1, item.getAppNum().intValue());
                this.appMapper.updateForOpenValid(calendar.getTime(), item.getAppType(), orgId);
                return;
            case MONTH:
                calendar.add(2, item.getAppNum().intValue());
                this.appMapper.updateForOpenValid(calendar.getTime(), item.getAppType(), orgId);
                return;
            case DAY:
                calendar.add(5, item.getAppNum().intValue());
                this.appMapper.updateForOpenValid(calendar.getTime(), item.getAppType(), orgId);
                return;
            default:
                throw new BizException(OnpremiseErrorEnum.LICENSE_ERROR.getErrorCode(), OnpremiseErrorEnum.LICENSE_ERROR.getMessage());
        }
    }

    @Override // com.moredian.onpremise.api.account.AuthCodeService
    public PageList<ListAuthCodeResponse> listAuthCode(ListAuthCodeRequest request) throws BadPaddingException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeyException, IOException, BeansException {
        Paginator paginator = request.getPaginator();
        if (Paginator.checkPaginator(paginator)) {
            PageHelper.startPage(paginator.getPageNum(), paginator.getPageSize());
            List<ListAuthCodeResponse> responses = this.authCodeMapper.listAuthCode(request.getOrgId());
            packageingAuthCodeResponse(responses);
            return new PageList<>(responses);
        }
        List<ListAuthCodeResponse> list = this.authCodeMapper.listAuthCode(request.getOrgId());
        packageingAuthCodeResponse(list);
        return new PageList<>(Paginator.initPaginator(list), list);
    }

    private void packageingAuthCodeResponse(List<ListAuthCodeResponse> codes) throws BadPaddingException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeyException, IOException, BeansException {
        if (MyListUtils.listIsEmpty(codes)) {
            for (ListAuthCodeResponse code : codes) {
                LicenseDetailInfo licenseDetailInfo = licenseToEntity(code.getAuthCode());
                code.setAllowModuleName(licenseDetailInfo.getDescription());
                List<AppValidInfoResponse> appValidInfoResponses = new ArrayList<>();
                List<LicenseDetailAppInfo> licenseDetailApps = licenseDetailInfo.getLicenseDetailApps();
                for (LicenseDetailAppInfo licenseDetailAppInfo : licenseDetailApps) {
                    AppValidInfoResponse appValidInfoResponse = new AppValidInfoResponse();
                    BeanUtils.copyProperties(licenseDetailAppInfo, appValidInfoResponse);
                    appValidInfoResponses.add(appValidInfoResponse);
                }
                code.setAppValidInfos(appValidInfoResponses);
                code.setImportTime(Long.valueOf(code.getGmtCreate().getTime()));
            }
        }
    }

    public LicenseDetailInfo licenseToEntity(String license) throws BadPaddingException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        LicenseDetailInfo licenseDetailInfo = new LicenseDetailInfo();
        AssertUtil.isNullOrEmpty(license, OnpremiseErrorEnum.SYSTEM_AUTH_CODE_MUST_NOT_NULL);
        Map<String, Object> params = AuthCodeUtils.decryptByPublicKey(license, this.publicKey);
        String orgCode = (String) params.get(AuthConstants.AUTH_PARAM_ORG_CODE_KEY);
        Integer maxNum = (Integer) params.get(AuthConstants.AUTH_PARAM_ALLOW_NUM_KEY);
        Long startTime = Long.valueOf(((Integer) params.get(AuthConstants.AUTH_PARAM_START_TIME_KEY)).longValue());
        Long endTime = Long.valueOf(((Integer) params.get(AuthConstants.AUTH_PARAM_END_TIME_KEY)).longValue());
        String moduleTypes = (String) params.get(AuthConstants.AUTH_PARAM_MODULE_TYPE_KEY);
        Long generateTime = (Long) params.get(AuthConstants.AUTH_PARAM_GENERATE_TIME_KEY);
        List<LicenseDetailAppInfo> licenseDetailAppInfos = new ArrayList<>();
        String version = LicenseVersionEnum.V1.getDescription();
        if (params.containsKey("version")) {
            version = String.valueOf(params.get("version"));
        }
        StringBuffer appTextSb = new StringBuffer("");
        if (LicenseVersionEnum.V1.getDescription().equals(version)) {
            String moduleStr = String.valueOf(params.get(AuthConstants.AUTH_PARAM_MODULE_TYPE_KEY));
            if ("null".equals(moduleStr)) {
                appTextSb.append(AppTypeEnum.MJ.getDescription()).append("、");
                LicenseDetailAppInfo licenseDetailAppInfo = new LicenseDetailAppInfo();
                licenseDetailAppInfo.setAppType(Integer.valueOf(AppTypeEnum.MJ.getValue()));
                licenseDetailAppInfos.add(licenseDetailAppInfo);
            }
            if (moduleStr != null && moduleStr != "null" && moduleStr.trim().length() > 0) {
                String[] modules = moduleStr.split(",");
                for (String module : modules) {
                    AppTypeEnum appTypeEnum = AppTypeEnum.getByValue(Integer.parseInt(module));
                    appTextSb.append(appTypeEnum.getDescription()).append("、");
                    LicenseDetailAppInfo licenseDetailAppInfo2 = new LicenseDetailAppInfo();
                    licenseDetailAppInfo2.setAppType(Integer.valueOf(appTypeEnum.getValue()));
                    licenseDetailAppInfos.add(licenseDetailAppInfo2);
                }
            }
        } else if (LicenseVersionEnum.V2.getDescription().equals(version)) {
            List<Map> licenseApps = (List) params.get(AuthConstants.AUTH_PARAM_APPS_KEY);
            if (!CollectionUtils.isEmpty(licenseApps)) {
                for (Map item : licenseApps) {
                    LicenseDetailAppInfo licenseDetailAppInfo3 = new LicenseDetailAppInfo();
                    Integer appType = (Integer) item.get(AuthConstants.AUTH_PARAM_APP_TYPE_KEY);
                    licenseDetailAppInfo3.setAppType(appType);
                    appTextSb.append(AppTypeEnum.getByValue(appType.intValue()).getDescription());
                    if (!appType.equals(Integer.valueOf(AppTypeEnum.MJ.getValue()))) {
                        Integer appNum = (Integer) item.get(AuthConstants.AUTH_PARAM_APP_NUM_KEY);
                        licenseDetailAppInfo3.setAppNum(appNum);
                        String appUnit = (String) item.get(AuthConstants.AUTH_PARAM_APP_UNIT_KEY);
                        licenseDetailAppInfo3.setAppUnit(appUnit);
                        appTextSb.append("-有效期").append(appNum).append(LicenseUnitEnum.getByKey(appUnit).getDescription());
                    }
                    licenseDetailAppInfos.add(licenseDetailAppInfo3);
                    appTextSb.append("、");
                }
            }
        }
        licenseDetailInfo.setOrgCode(orgCode);
        licenseDetailInfo.setMaxNum(maxNum);
        licenseDetailInfo.setStartTime(startTime);
        licenseDetailInfo.setEndTime(endTime);
        licenseDetailInfo.setModuleTypes(moduleTypes);
        licenseDetailInfo.setGenerateTime(generateTime);
        licenseDetailInfo.setLicenseDetailApps(licenseDetailAppInfos);
        licenseDetailInfo.setVersion(version);
        licenseDetailInfo.setDescription(StringUtils.isEmpty(appTextSb.toString()) ? "" : appTextSb.substring(0, appTextSb.length() - 1));
        return licenseDetailInfo;
    }
}
