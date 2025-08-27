package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.ViewConfig;
import com.moredian.onpremise.core.model.request.ViewConfigListRequest;
import com.moredian.onpremise.core.model.response.ViewConfigResponse;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/ViewConfigMapper.class */
public interface ViewConfigMapper {
    List<ViewConfigResponse> viewConfigGetList(ViewConfigListRequest viewConfigListRequest);

    int viewConfigAdd(ViewConfig viewConfig);

    int viewConfigDeleteById(@Param("orgId") Long l, @Param("id") Long l2);

    int viewConfigEdit(ViewConfig viewConfig);

    ViewConfigResponse viewConfigGetOne(@Param("orgId") Long l, @Param("id") Long l2);

    ViewConfigResponse viewConfigGetLogin();
}
