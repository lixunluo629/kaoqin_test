package com.moredian.onpremise.device.service.netty;

import com.moredian.onpremise.api.device.DeviceService;
import com.moredian.onpremise.core.common.enums.CheckDeviceCodeEnum;
import com.moredian.onpremise.core.common.enums.NettyErrorEnum;
import com.moredian.onpremise.core.model.request.TerminalCheckDeviceActiveRequest;
import com.moredian.onpremise.core.model.response.TerminalCheckDeviceActiveResponse;
import com.moredian.onpremise.core.utils.JsonUtils;
import com.moredian.onpremise.event.ModelTransferEvent;
import com.moredian.onpremise.iot.IOTContext;
import com.moredian.onpremise.iot.handle.AbstractModelEventHandler;
import com.moredian.onpremise.model.DeviceInfoRequest;
import com.moredian.onpremise.model.DeviceInfoResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
/* loaded from: onpremise-device-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/device/service/netty/DeviceInfoRequestHandler.class */
public class DeviceInfoRequestHandler extends AbstractModelEventHandler<DeviceInfoRequest> {

    @Autowired
    private DeviceService deviceService;

    @Override // com.moredian.onpremise.iot.handle.ModelEventHandler
    public void handle(DeviceInfoRequest model, IOTContext context) {
        DeviceInfoResponse response = new DeviceInfoResponse();
        try {
            try {
                this.logger.info("Handling device info:{}", JsonUtils.toJson(model));
                TerminalCheckDeviceActiveRequest request = new TerminalCheckDeviceActiveRequest();
                request.setDeviceSn(model.getSerialNumber());
                TerminalCheckDeviceActiveResponse deviceResponse = this.deviceService.checkDeviceActive(request);
                BeanUtils.copyProperties(deviceResponse, response);
                if (deviceResponse.getCode().intValue() == CheckDeviceCodeEnum.NORMAL.getValue()) {
                    response.setCode(0);
                    response.setToken(deviceResponse.getToken());
                } else if (deviceResponse.getCode().intValue() == CheckDeviceCodeEnum.NOT_ALLOW.getValue()) {
                    response.setCode(Integer.valueOf(NettyErrorEnum.DEVICE_NOT_ALLOW_ERROR.getValue()));
                    response.setErrorMessage(NettyErrorEnum.DEVICE_NOT_ALLOW_ERROR.getDescription());
                } else {
                    response.setCode(Integer.valueOf(NettyErrorEnum.DEVICE_NOT_ACTIVE_ERROR.getValue()));
                    response.setErrorMessage(NettyErrorEnum.DEVICE_NOT_ACTIVE_ERROR.getDescription());
                }
                this.logger.info("device info handle response:{}", JsonUtils.toJson(response));
                ModelTransferEvent responseEvent = new ModelTransferEvent(DeviceInfoResponse.MODEL_TYPE.type(), response);
                context.getSession().getChannel().writeAndFlush(responseEvent);
            } catch (Exception e) {
                response.setCode(Integer.valueOf(NettyErrorEnum.DEVICE_INFO_ERROR.getValue()));
                response.setErrorMessage(NettyErrorEnum.DEVICE_INFO_ERROR.getDescription());
                this.logger.info("device info handle response:{}", JsonUtils.toJson(response));
                ModelTransferEvent responseEvent2 = new ModelTransferEvent(DeviceInfoResponse.MODEL_TYPE.type(), response);
                context.getSession().getChannel().writeAndFlush(responseEvent2);
            }
        } catch (Throwable th) {
            this.logger.info("device info handle response:{}", JsonUtils.toJson(response));
            ModelTransferEvent responseEvent3 = new ModelTransferEvent(DeviceInfoResponse.MODEL_TYPE.type(), response);
            context.getSession().getChannel().writeAndFlush(responseEvent3);
            throw th;
        }
    }
}
