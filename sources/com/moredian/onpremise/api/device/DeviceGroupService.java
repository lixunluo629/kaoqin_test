package com.moredian.onpremise.api.device;

import com.moredian.onpremise.core.model.request.DeviceGroupDeleteRequest;
import com.moredian.onpremise.core.model.request.DeviceGroupRequest;
import com.moredian.onpremise.core.model.response.DeviceGroupResponse;
import java.util.List;

/* loaded from: onpremise-api-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/api/device/DeviceGroupService.class */
public interface DeviceGroupService {
    Long insertOne(DeviceGroupRequest deviceGroupRequest);

    void softDeleteById(DeviceGroupDeleteRequest deviceGroupDeleteRequest);

    void updateOneById(DeviceGroupRequest deviceGroupRequest);

    List<DeviceGroupResponse> getList(Long l);
}
