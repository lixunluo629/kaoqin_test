package com.moredian.onpremise.api.temperature;

import com.moredian.onpremise.core.model.domain.TemperatureRecord;
import com.moredian.onpremise.core.model.request.TemperatureRecordRequest;
import com.moredian.onpremise.core.model.response.TemperatureRecordCountResponse;
import com.moredian.onpremise.core.model.response.TemperatureRecordResponse;
import com.moredian.onpremise.core.utils.PageList;

/* loaded from: onpremise-api-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/api/temperature/TemperatureRecordService.class */
public interface TemperatureRecordService {
    int insert(TemperatureRecord temperatureRecord);

    PageList<TemperatureRecordResponse> queryRecord(TemperatureRecordRequest temperatureRecordRequest);

    PageList<TemperatureRecordCountResponse> queryRecordCount(TemperatureRecordRequest temperatureRecordRequest);
}
