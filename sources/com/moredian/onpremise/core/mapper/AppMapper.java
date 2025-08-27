package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.common.constants.AuthConstants;
import com.moredian.onpremise.core.model.request.DeleteAppRequest;
import com.moredian.onpremise.core.model.request.SaveAppRequest;
import com.moredian.onpremise.core.model.response.AppResponse;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/AppMapper.class */
public interface AppMapper {
    List<AppResponse> getAppList(@Param("orgId") Long l, @Param("appIds") List<String> list);

    int deleteApp(DeleteAppRequest deleteAppRequest);

    int insertApp(SaveAppRequest saveAppRequest);

    int updateApp(SaveAppRequest saveAppRequest);

    int updateForOpen(@Param("appTypes") List<String> list, @Param(AuthConstants.AUTH_PARAM_APP_TYPE_KEY) String str, @Param("orgId") Long l);

    int updateForClose(@Param("orgId") Long l);

    List<String> getNameByType(@Param("appTypes") List<String> list, @Param("orgId") Long l);

    int updateForOpenValid(@Param("appValid") Date date, @Param(AuthConstants.AUTH_PARAM_APP_TYPE_KEY) Integer num, @Param("orgId") Long l);

    AppResponse getAppOneByType(@Param("orgId") Long l, @Param(AuthConstants.AUTH_PARAM_APP_TYPE_KEY) Integer num);

    List<AppResponse> getUnOpenAppList(@Param("orgId") Long l);
}
