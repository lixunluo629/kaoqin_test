package com.moredian.onpremise.api.server;

import com.moredian.onpremise.core.model.request.SaveAdvertisingRequest;
import com.moredian.onpremise.core.model.request.SavePoseThresholdRequest;
import com.moredian.onpremise.core.model.request.SaveRecordPeriodRequest;
import com.moredian.onpremise.core.model.request.SaveSnapModeRequest;
import com.moredian.onpremise.core.model.request.SaveStrangerInfoRequest;
import com.moredian.onpremise.core.model.request.SaveStrangerShowInfoRequest;
import com.moredian.onpremise.core.model.request.SaveStrangerSpeechInfoRequest;
import com.moredian.onpremise.core.model.request.SaveTimeZoneRequest;
import com.moredian.onpremise.core.model.request.SystemBasicConfigRequest;
import com.moredian.onpremise.core.model.response.QueryConfigResponse;
import com.moredian.onpremise.core.model.response.SystemBasicConfigResponse;

/* loaded from: onpremise-api-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/api/server/ConfigService.class */
public interface ConfigService {
    boolean saveTimeZone(SaveTimeZoneRequest saveTimeZoneRequest);

    String getTimeZone();

    boolean saveRecordPeriod(SaveRecordPeriodRequest saveRecordPeriodRequest);

    boolean saveStrangerSpeechReminderInfo(SaveStrangerSpeechInfoRequest saveStrangerSpeechInfoRequest);

    boolean saveStrangerShowReminderInfo(SaveStrangerShowInfoRequest saveStrangerShowInfoRequest);

    boolean saveStrangerReminderInfo(SaveStrangerInfoRequest saveStrangerInfoRequest);

    QueryConfigResponse queryRecordPeriod();

    QueryConfigResponse querySnapPeriod();

    QueryConfigResponse getOneByKey(String str);

    boolean savePoseThreshold(SavePoseThresholdRequest savePoseThresholdRequest);

    boolean saveAdvertising(SaveAdvertisingRequest saveAdvertisingRequest);

    boolean saveSnapMode(SaveSnapModeRequest saveSnapModeRequest);

    Integer getSnapMode();

    Integer getRepeatFace();

    SystemBasicConfigResponse getSystemBasicConfig();

    void saveSystemBasicConfig(SystemBasicConfigRequest systemBasicConfigRequest);
}
