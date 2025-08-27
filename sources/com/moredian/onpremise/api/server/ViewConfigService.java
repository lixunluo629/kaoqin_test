package com.moredian.onpremise.api.server;

import com.moredian.onpremise.core.model.request.ViewConfigListRequest;
import com.moredian.onpremise.core.model.request.ViewConfigRequest;
import com.moredian.onpremise.core.model.response.ViewConfigResponse;
import com.moredian.onpremise.core.utils.PageList;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/* loaded from: onpremise-api-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/api/server/ViewConfigService.class */
public interface ViewConfigService {
    PageList<ViewConfigResponse> viewConfigPageList(ViewConfigListRequest viewConfigListRequest);

    Long viewConfigAdd(ViewConfigRequest viewConfigRequest);

    Long viewConfigDelete(ViewConfigRequest viewConfigRequest);

    Long viewConfigEdit(ViewConfigRequest viewConfigRequest);

    ViewConfigResponse viewConfigGetOne(ViewConfigRequest viewConfigRequest);

    ViewConfigResponse viewConfigGetLogin();

    List<ViewConfigResponse> viewConfigGetList(ViewConfigListRequest viewConfigListRequest);

    String viewConfigUploadImg(MultipartFile multipartFile);
}
