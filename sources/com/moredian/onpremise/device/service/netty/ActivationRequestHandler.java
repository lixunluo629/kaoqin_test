package com.moredian.onpremise.device.service.netty;

import com.moredian.onpremise.api.device.DeviceService;
import com.moredian.onpremise.core.common.enums.NettyErrorEnum;
import com.moredian.onpremise.core.exception.BizException;
import com.moredian.onpremise.core.model.request.ActiveDeviceRequest;
import com.moredian.onpremise.core.model.response.ActiveDeviceResponse;
import com.moredian.onpremise.core.utils.JsonUtils;
import com.moredian.onpremise.event.ModelTransferEvent;
import com.moredian.onpremise.iot.IOTContext;
import com.moredian.onpremise.iot.handle.AbstractModelEventHandler;
import com.moredian.onpremise.model.ActivationRequest;
import com.moredian.onpremise.model.ActivationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
/* loaded from: onpremise-device-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/device/service/netty/ActivationRequestHandler.class */
public class ActivationRequestHandler extends AbstractModelEventHandler<ActivationRequest> {

    @Autowired
    private DeviceService deviceService;

    @Override // com.moredian.onpremise.iot.handle.ModelEventHandler
    public void handle(ActivationRequest model, IOTContext context) {
        ActivationResponse response = new ActivationResponse();
        try {
            try {
                this.logger.info("Handling activation:{}", model.toString());
                ActiveDeviceResponse deviceResponse = this.deviceService.activeDevice(modelToRequest(model));
                response.setSerialNumber(deviceResponse.getDeviceSn());
                response.setCode(0);
                response.setEquipmentType(deviceResponse.getDeviceType());
                response.setToken(deviceResponse.getToken());
                response.setEquipmentId(deviceResponse.getDeviceId());
                response.setEquipmentName(deviceResponse.getDeviceName());
                response.setOrgId(deviceResponse.getOrgId());
                this.logger.info("activation handle response:{}", JsonUtils.toJson(response));
                ModelTransferEvent responseEvent = new ModelTransferEvent(ActivationResponse.MODEL_TYPE.type(), response);
                context.getSession().getChannel().writeAndFlush(responseEvent);
            } catch (BizException e) {
                response.setCode(Integer.valueOf(NettyErrorEnum.ACTIVE_ERROR.getValue()));
                response.setErrorMessage(e.getMessage());
                this.logger.info("activation handle response:{}", JsonUtils.toJson(response));
                ModelTransferEvent responseEvent2 = new ModelTransferEvent(ActivationResponse.MODEL_TYPE.type(), response);
                context.getSession().getChannel().writeAndFlush(responseEvent2);
            } catch (Exception e2) {
                response.setCode(Integer.valueOf(NettyErrorEnum.ACTIVE_ERROR.getValue()));
                response.setErrorMessage(NettyErrorEnum.ACTIVE_ERROR.getDescription());
                this.logger.info("activation handle response:{}", JsonUtils.toJson(response));
                ModelTransferEvent responseEvent3 = new ModelTransferEvent(ActivationResponse.MODEL_TYPE.type(), response);
                context.getSession().getChannel().writeAndFlush(responseEvent3);
            }
        } catch (Throwable th) {
            this.logger.info("activation handle response:{}", JsonUtils.toJson(response));
            ModelTransferEvent responseEvent4 = new ModelTransferEvent(ActivationResponse.MODEL_TYPE.type(), response);
            context.getSession().getChannel().writeAndFlush(responseEvent4);
            throw th;
        }
    }

    private ActiveDeviceRequest modelToRequest(ActivationRequest model) {
        ActiveDeviceRequest request = new ActiveDeviceRequest();
        request.setDeviceSn(model.getSerialNumber());
        request.setDeviceType(model.getEquipmentType());
        request.setIpAddress(model.getIpAddress());
        request.setMacAddress(model.getMacAddress());
        request.setNetType(model.getNetType());
        request.setVersion(model.getVersion());
        request.setRomVersion(model.getRomVersion());
        request.setSecret(model.getSecret());
        request.setDeviceModel(model.getDeviceModel());
        request.setDeviceName(model.getDeviceName());
        return request;
    }
}
