package com.moredian.onpremise.api.server;

import com.moredian.onpremise.core.model.request.DeleteServerScheduleRequest;
import com.moredian.onpremise.core.model.request.ExecuteUpgradeServerScheduleRequest;
import com.moredian.onpremise.core.model.request.ListServerScheduleRequest;
import com.moredian.onpremise.core.model.request.UploadUpgradePackageRequest;
import com.moredian.onpremise.core.model.response.ListServerScheduleResponse;
import com.moredian.onpremise.core.utils.PageList;

/* loaded from: onpremise-api-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/api/server/UpgradeServerService.class */
public interface UpgradeServerService {
    boolean uploadPackage(UploadUpgradePackageRequest uploadUpgradePackageRequest);

    PageList<ListServerScheduleResponse> listServerSchedule(ListServerScheduleRequest listServerScheduleRequest);

    boolean delete(DeleteServerScheduleRequest deleteServerScheduleRequest);

    boolean executeSchedule(ExecuteUpgradeServerScheduleRequest executeUpgradeServerScheduleRequest);

    boolean readCacheFromDb();

    boolean reloadMemberFeature();
}
