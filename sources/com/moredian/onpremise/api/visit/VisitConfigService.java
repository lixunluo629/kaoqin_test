package com.moredian.onpremise.api.visit;

import com.moredian.onpremise.core.model.request.TerminalSyncRequest;
import com.moredian.onpremise.core.model.request.VisitConfigDeviceRequest;
import com.moredian.onpremise.core.model.request.VisitConfigRequest;
import com.moredian.onpremise.core.model.response.TerminalSyncResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncVisitConfigResponse;
import com.moredian.onpremise.core.model.response.VisitConfigResponse;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/* loaded from: onpremise-api-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/api/visit/VisitConfigService.class */
public interface VisitConfigService {
    Long addVisitConfig(VisitConfigRequest visitConfigRequest);

    Long updateVisitConfig(VisitConfigRequest visitConfigRequest);

    VisitConfigResponse getOneVisitConfigById(Long l, Long l2);

    Long updateVisitConfigDevice(VisitConfigDeviceRequest visitConfigDeviceRequest);

    TerminalSyncResponse<TerminalSyncVisitConfigResponse> syncVisitConfig(TerminalSyncRequest terminalSyncRequest);

    List<String> uploadConfigFile(MultipartFile multipartFile);
}
