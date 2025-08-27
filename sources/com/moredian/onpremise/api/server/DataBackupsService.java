package com.moredian.onpremise.api.server;

import com.moredian.onpremise.core.model.request.BackupsDataRecordRequest;
import com.moredian.onpremise.core.model.request.DeleteBackupsRequest;
import com.moredian.onpremise.core.model.request.ListServerBackupsRecordRequest;
import com.moredian.onpremise.core.model.request.RestoreBackupsRequest;
import com.moredian.onpremise.core.model.request.UploadBackupsRequest;
import com.moredian.onpremise.core.model.response.ListServerBackupsRecordResponse;
import com.moredian.onpremise.core.utils.PageList;

/* loaded from: onpremise-api-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/api/server/DataBackupsService.class */
public interface DataBackupsService {
    PageList<ListServerBackupsRecordResponse> listRecord(ListServerBackupsRecordRequest listServerBackupsRecordRequest);

    Boolean executeBackups(BackupsDataRecordRequest backupsDataRecordRequest);

    Boolean uploadBackups(UploadBackupsRequest uploadBackupsRequest);

    Boolean restoreBackups(RestoreBackupsRequest restoreBackupsRequest);

    Boolean deleteBackups(DeleteBackupsRequest deleteBackupsRequest);
}
