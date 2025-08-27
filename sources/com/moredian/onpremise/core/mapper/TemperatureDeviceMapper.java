package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.TemperatureDevice;
import com.moredian.onpremise.core.model.request.TemperatureConfigRequest;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/TemperatureDeviceMapper.class */
public interface TemperatureDeviceMapper {
    List<TemperatureDevice> listDevice(TemperatureConfigRequest temperatureConfigRequest);

    TemperatureDevice getOneByDeviceSn(String str);

    int insert(TemperatureDevice temperatureDevice);

    int update(TemperatureDevice temperatureDevice);

    int delete(TemperatureDevice temperatureDevice);
}
