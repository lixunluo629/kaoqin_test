package com.moredian.onpremise.server.service.impl;

import com.github.pagehelper.PageHelper;
import com.moredian.onpremise.api.device.DeviceService;
import com.moredian.onpremise.api.server.CallbackServerService;
import com.moredian.onpremise.core.common.enums.CallbackTagEnum;
import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.mapper.CallbackServerMapper;
import com.moredian.onpremise.core.model.domain.CallbackServer;
import com.moredian.onpremise.core.model.request.DeleteCallbackServerRequest;
import com.moredian.onpremise.core.model.request.ListCallbackServersRequest;
import com.moredian.onpremise.core.model.request.QueryCllbackServerRequest;
import com.moredian.onpremise.core.model.request.SaveCallbackServerRequest;
import com.moredian.onpremise.core.model.response.CallbackServerResponse;
import com.moredian.onpremise.core.model.response.ListCallbackTagResponse;
import com.moredian.onpremise.core.utils.AssertUtil;
import com.moredian.onpremise.core.utils.HttpUtils;
import com.moredian.onpremise.core.utils.JsonUtils;
import com.moredian.onpremise.core.utils.PageList;
import com.moredian.onpremise.core.utils.Paginator;
import com.moredian.onpremise.iot.handle.NettyMessageApi;
import com.moredian.onpremise.model.DeleteOnlineCheckCallbackRequest;
import com.moredian.onpremise.model.DeleteQrCheckCallbackRequest;
import com.moredian.onpremise.model.RegisterOnlineCheckCallbackRequest;
import com.moredian.onpremise.model.RegisterQrCheckCallbackRequest;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
/* loaded from: onpremise-server-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/server/service/impl/CallbackServerServiceImpl.class */
public class CallbackServerServiceImpl implements CallbackServerService {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) CallbackServerService.class);

    @Autowired
    private CallbackServerMapper callbackServerMapper;

    @Autowired
    private NettyMessageApi nettyMessageApi;

    @Autowired
    private DeviceService deviceService;

    @Override // com.moredian.onpremise.api.server.CallbackServerService
    public boolean save(SaveCallbackServerRequest request) {
        checkSaveCallbackServerParams(request);
        CallbackServer dao = saveRequestToCallbackServer(request);
        CallbackTagEnum callbackTagEnum = CallbackTagEnum.getByTag(dao.getCallbackTag());
        switch (callbackTagEnum) {
            case ONLINE_CHECK:
                if (!StringUtils.isEmpty(request.getDeviceSn())) {
                    String[] deviceSnArr = request.getDeviceSn().split(",");
                    for (String deviceSn : deviceSnArr) {
                        CallbackServer callbackServer = this.callbackServerMapper.getOneByTagAndDeviceSn(request.getOrgId(), request.getCallbackTag(), deviceSn);
                        dao.setDeviceSn(deviceSn);
                        if (callbackServer == null) {
                            AssertUtil.isTrue(Boolean.valueOf(this.callbackServerMapper.insert(dao) > 0), OnpremiseErrorEnum.SAVE_CALLBACK_SERVER_FAIL);
                        } else {
                            AssertUtil.isTrue(Boolean.valueOf(this.callbackServerMapper.updateByDeviceSn(dao) > 0), OnpremiseErrorEnum.UPDATE_CALLBACK_SERVER_FAIL);
                        }
                        doSendNettyMessage(callbackTagEnum, request.getOrgId(), request.getCallbackUrl(), deviceSn, true);
                    }
                    break;
                }
                break;
            case QR_CHECK:
                if (!StringUtils.isEmpty(request.getDeviceSn())) {
                    String[] deviceSnArr2 = request.getDeviceSn().split(",");
                    for (String deviceSn2 : deviceSnArr2) {
                        CallbackServer callbackServer2 = this.callbackServerMapper.getOneByTagAndDeviceSn(request.getOrgId(), request.getCallbackTag(), deviceSn2);
                        dao.setDeviceSn(deviceSn2);
                        if (callbackServer2 == null) {
                            AssertUtil.isTrue(Boolean.valueOf(this.callbackServerMapper.insert(dao) > 0), OnpremiseErrorEnum.SAVE_CALLBACK_SERVER_FAIL);
                        } else {
                            AssertUtil.isTrue(Boolean.valueOf(this.callbackServerMapper.updateByDeviceSn(dao) > 0), OnpremiseErrorEnum.UPDATE_CALLBACK_SERVER_FAIL);
                        }
                        doSendNettyMessage(callbackTagEnum, request.getOrgId(), request.getCallbackUrl(), deviceSn2, true);
                    }
                    break;
                }
                break;
            case REC_SUCCESS:
                CallbackServer callbackServer3 = this.callbackServerMapper.getOneByTag(request.getOrgId(), request.getCallbackTag());
                if (callbackServer3 == null) {
                    AssertUtil.isTrue(Boolean.valueOf(this.callbackServerMapper.insert(dao) > 0), OnpremiseErrorEnum.SAVE_CALLBACK_SERVER_FAIL);
                    break;
                } else {
                    AssertUtil.isTrue(Boolean.valueOf(this.callbackServerMapper.update(dao) > 0), OnpremiseErrorEnum.UPDATE_CALLBACK_SERVER_FAIL);
                    break;
                }
            case REC_FAIL:
                CallbackServer callbackServer4 = this.callbackServerMapper.getOneByTag(request.getOrgId(), request.getCallbackTag());
                if (callbackServer4 == null) {
                    AssertUtil.isTrue(Boolean.valueOf(this.callbackServerMapper.insert(dao) > 0), OnpremiseErrorEnum.SAVE_CALLBACK_SERVER_FAIL);
                    break;
                } else {
                    AssertUtil.isTrue(Boolean.valueOf(this.callbackServerMapper.update(dao) > 0), OnpremiseErrorEnum.UPDATE_CALLBACK_SERVER_FAIL);
                    break;
                }
            case WARN_EVENT:
                CallbackServer callbackServer5 = this.callbackServerMapper.getOneByTag(request.getOrgId(), request.getCallbackTag());
                if (callbackServer5 == null) {
                    AssertUtil.isTrue(Boolean.valueOf(this.callbackServerMapper.insert(dao) > 0), OnpremiseErrorEnum.SAVE_CALLBACK_SERVER_FAIL);
                    break;
                } else {
                    AssertUtil.isTrue(Boolean.valueOf(this.callbackServerMapper.update(dao) > 0), OnpremiseErrorEnum.UPDATE_CALLBACK_SERVER_FAIL);
                    break;
                }
            case TEMPERATURE_RECORD:
                CallbackServer callbackServer6 = this.callbackServerMapper.getOneByTag(request.getOrgId(), request.getCallbackTag());
                if (callbackServer6 == null) {
                    AssertUtil.isTrue(Boolean.valueOf(this.callbackServerMapper.insert(dao) > 0), OnpremiseErrorEnum.SAVE_CALLBACK_SERVER_FAIL);
                    break;
                } else {
                    AssertUtil.isTrue(Boolean.valueOf(this.callbackServerMapper.update(dao) > 0), OnpremiseErrorEnum.UPDATE_CALLBACK_SERVER_FAIL);
                    break;
                }
        }
        return true;
    }

    @Override // com.moredian.onpremise.api.server.CallbackServerService
    public boolean delete(DeleteCallbackServerRequest request) {
        checkCallbackTag(request.getCallbackTag());
        AssertUtil.checkOrgId(request.getOrgId());
        CallbackTagEnum callbackTagEnum = CallbackTagEnum.getByTag(request.getCallbackTag());
        switch (callbackTagEnum) {
            case ONLINE_CHECK:
                if (!StringUtils.isEmpty(request.getDeviceSn())) {
                    String[] deviceSnArr = request.getDeviceSn().split(",");
                    for (String deviceSn : deviceSnArr) {
                        AssertUtil.isTrue(Boolean.valueOf(this.callbackServerMapper.deleteByDeviceSn(request.getOrgId(), request.getCallbackTag(), deviceSn) > 0), OnpremiseErrorEnum.DELETE_CALLBACK_SERVER_FAIL);
                        doSendNettyMessage(callbackTagEnum, request.getOrgId(), "", deviceSn, false);
                    }
                    break;
                }
                break;
            case QR_CHECK:
                if (!StringUtils.isEmpty(request.getDeviceSn())) {
                    String[] deviceSnArr2 = request.getDeviceSn().split(",");
                    for (String deviceSn2 : deviceSnArr2) {
                        AssertUtil.isTrue(Boolean.valueOf(this.callbackServerMapper.deleteByDeviceSn(request.getOrgId(), request.getCallbackTag(), deviceSn2) > 0), OnpremiseErrorEnum.DELETE_CALLBACK_SERVER_FAIL);
                        doSendNettyMessage(callbackTagEnum, request.getOrgId(), "", deviceSn2, false);
                    }
                    break;
                }
                break;
            case REC_SUCCESS:
                AssertUtil.isTrue(Boolean.valueOf(this.callbackServerMapper.delete(request.getOrgId(), request.getCallbackTag()) > 0), OnpremiseErrorEnum.DELETE_CALLBACK_SERVER_FAIL);
                break;
            case REC_FAIL:
                AssertUtil.isTrue(Boolean.valueOf(this.callbackServerMapper.delete(request.getOrgId(), request.getCallbackTag()) > 0), OnpremiseErrorEnum.DELETE_CALLBACK_SERVER_FAIL);
                break;
            case WARN_EVENT:
                AssertUtil.isTrue(Boolean.valueOf(this.callbackServerMapper.delete(request.getOrgId(), request.getCallbackTag()) > 0), OnpremiseErrorEnum.DELETE_CALLBACK_SERVER_FAIL);
                break;
            case TEMPERATURE_RECORD:
                AssertUtil.isTrue(Boolean.valueOf(this.callbackServerMapper.delete(request.getOrgId(), request.getCallbackTag()) > 0), OnpremiseErrorEnum.DELETE_CALLBACK_SERVER_FAIL);
                break;
        }
        return true;
    }

    @Override // com.moredian.onpremise.api.server.CallbackServerService
    public CallbackServerResponse getOneByTag(QueryCllbackServerRequest request) {
        checkCallbackTag(request.getCallbackTag());
        AssertUtil.checkOrgId(request.getOrgId());
        CallbackServer callbackServer = this.callbackServerMapper.getOneByTag(request.getOrgId(), request.getCallbackTag());
        CallbackServerResponse response = null;
        if (callbackServer != null) {
            response = new CallbackServerResponse();
            response.setCallbackTag(callbackServer.getCallbackTag());
            response.setCallbackUrl(callbackServer.getCallbackUrl());
        }
        return response;
    }

    @Override // com.moredian.onpremise.api.server.CallbackServerService
    public PageList<CallbackServerResponse> listCallbackServers(ListCallbackServersRequest request) {
        PageList<CallbackServerResponse> result;
        AssertUtil.checkOrgId(request.getOrgId());
        if (Paginator.checkPaginator(request.getPaginator())) {
            PageHelper.startPage(request.getPaginator().getPageNum(), request.getPaginator().getPageSize());
            result = new PageList<>(this.callbackServerMapper.listCallbackServers(request));
        } else {
            List<CallbackServerResponse> responses = this.callbackServerMapper.listCallbackServers(request);
            result = new PageList<>(Paginator.initPaginator(responses), responses);
        }
        return result;
    }

    @Override // com.moredian.onpremise.api.server.CallbackServerService
    public List<ListCallbackTagResponse> listTag() {
        CallbackTagEnum[] tagEnums = CallbackTagEnum.values();
        List<ListCallbackTagResponse> responses = new ArrayList<>();
        for (CallbackTagEnum tagEnum : tagEnums) {
            ListCallbackTagResponse response = new ListCallbackTagResponse();
            response.setTag(tagEnum.getTag());
            response.setDescription(tagEnum.getDescription());
            responses.add(response);
        }
        return responses;
    }

    private boolean checkSaveCallbackServerParams(SaveCallbackServerRequest request) {
        checkCallbackTag(request.getCallbackTag());
        AssertUtil.checkOrgId(request.getOrgId());
        AssertUtil.isNullOrEmpty(request.getCallbackUrl(), OnpremiseErrorEnum.CALLBACK_SERVER_URL_MUST_NOT_NULL);
        return true;
    }

    private void checkCallbackTag(String callbackTag) {
        AssertUtil.isNullOrEmpty(callbackTag, OnpremiseErrorEnum.CALLBACK_SERVER_TAG_MUST_NOT_NULL);
        CallbackTagEnum[] tagEnums = CallbackTagEnum.values();
        boolean allowTag = false;
        for (CallbackTagEnum tagEnum : tagEnums) {
            if (callbackTag.equals(tagEnum.getTag())) {
                allowTag = true;
            }
        }
        AssertUtil.isTrue(Boolean.valueOf(allowTag), OnpremiseErrorEnum.CALLBACK_SERVER_TAG_NOT_ALLOW);
    }

    private CallbackServer saveRequestToCallbackServer(SaveCallbackServerRequest request) {
        CallbackServer server = new CallbackServer();
        server.setOrgId(request.getOrgId());
        server.setCallbackTag(request.getCallbackTag());
        server.setCallbackUrl(request.getCallbackUrl());
        return server;
    }

    private void sendCallback(Long orgId, String callbackTag, Object o) {
        String[] urls;
        try {
            CallbackServer callbackServer = this.callbackServerMapper.getOneByTag(orgId, callbackTag);
            if (callbackServer != null && (urls = callbackServer.getCallbackUrl().split(",")) != null && urls.length > 0) {
                for (String url : urls) {
                    HttpUtils.sendURLPost(url, JsonUtils.toJson(o));
                }
            }
        } catch (Exception e) {
        }
    }

    private void doSendNettyMessage(CallbackTagEnum callbackTagEnum, Long orgId, String url, String deviceSn, Boolean flag) {
        logger.info("====================send deviceSn:{}", deviceSn);
        AssertUtil.isTrue(Boolean.valueOf(this.deviceService.checkDeviceFailure(deviceSn)), OnpremiseErrorEnum.NOT_FIND_ONLINE_DEVICE);
        switch (callbackTagEnum) {
            case ONLINE_CHECK:
                if (flag.booleanValue()) {
                    this.nettyMessageApi.sendMsg(new RegisterOnlineCheckCallbackRequest(orgId, url), Integer.valueOf(RegisterOnlineCheckCallbackRequest.MODEL_TYPE.type()), deviceSn);
                    break;
                } else {
                    this.nettyMessageApi.sendMsg(new DeleteOnlineCheckCallbackRequest(orgId), Integer.valueOf(DeleteOnlineCheckCallbackRequest.MODEL_TYPE.type()), deviceSn);
                    break;
                }
            case QR_CHECK:
                if (flag.booleanValue()) {
                    this.nettyMessageApi.sendMsg(new RegisterQrCheckCallbackRequest(orgId, url), Integer.valueOf(RegisterQrCheckCallbackRequest.MODEL_TYPE.type()), deviceSn);
                    break;
                } else {
                    this.nettyMessageApi.sendMsg(new DeleteQrCheckCallbackRequest(orgId), Integer.valueOf(DeleteQrCheckCallbackRequest.MODEL_TYPE.type()), deviceSn);
                    break;
                }
        }
    }
}
