package com.moredian.onpremise.api.temperature;

import com.moredian.onpremise.core.model.request.TemperatureConfigRequest;
import com.moredian.onpremise.core.model.response.TemperatureConfigResponse;

/* loaded from: onpremise-api-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/api/temperature/TemperatureConfigService.class */
public interface TemperatureConfigService {
    TemperatureConfigResponse queryConfigWithoutDevice();

    TemperatureConfigResponse queryConfig(TemperatureConfigRequest temperatureConfigRequest);

    Long updateConfig(TemperatureConfigRequest temperatureConfigRequest);
}
