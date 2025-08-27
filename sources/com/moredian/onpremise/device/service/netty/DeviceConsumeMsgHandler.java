package com.moredian.onpremise.device.service.netty;

import com.moredian.onpremise.core.mapper.DeviceMsgLogMapper;
import com.moredian.onpremise.core.model.domain.DeviceMsgLog;
import com.moredian.onpremise.core.utils.JsonUtils;
import com.moredian.onpremise.iot.IOTContext;
import com.moredian.onpremise.iot.handle.AbstractModelEventHandler;
import com.moredian.onpremise.model.DeviceConsumeMsgResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
/* loaded from: onpremise-device-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/device/service/netty/DeviceConsumeMsgHandler.class */
public class DeviceConsumeMsgHandler extends AbstractModelEventHandler<DeviceConsumeMsgResponse> {

    @Autowired
    private DeviceMsgLogMapper deviceMsgLogMapper;

    @Override // com.moredian.onpremise.iot.handle.ModelEventHandler
    public void handle(DeviceConsumeMsgResponse model, IOTContext context) {
        this.logger.info("handle deviceConsumeMsg response:{}", JsonUtils.toJson(model));
        DeviceMsgLog deviceMsgLog = new DeviceMsgLog();
        deviceMsgLog.setOrgId(1L);
        deviceMsgLog.setUuid(model.getUuid());
        deviceMsgLog.setDeviceSn(model.getDeviceSn());
        deviceMsgLog.setStatus(model.getStatus());
        deviceMsgLog.setError(model.getError());
        this.deviceMsgLogMapper.updateByUuidAndDeviceSn(deviceMsgLog);
    }
}
