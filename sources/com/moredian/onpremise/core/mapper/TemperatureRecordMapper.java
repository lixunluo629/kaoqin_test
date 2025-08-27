package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.TemperatureRecord;
import com.moredian.onpremise.core.model.request.TemperatureRecordRequest;
import com.moredian.onpremise.core.model.response.TemperatureRecordCountResponse;
import com.moredian.onpremise.core.model.response.TemperatureRecordResponse;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/TemperatureRecordMapper.class */
public interface TemperatureRecordMapper {
    List<TemperatureRecordResponse> listRecord(TemperatureRecordRequest temperatureRecordRequest);

    int insert(TemperatureRecord temperatureRecord);

    List<TemperatureRecordCountResponse> listRecordCount(TemperatureRecordRequest temperatureRecordRequest);
}
