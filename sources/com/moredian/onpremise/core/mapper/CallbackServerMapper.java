package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.CallbackServer;
import com.moredian.onpremise.core.model.request.ListCallbackServersRequest;
import com.moredian.onpremise.core.model.response.CallbackServerResponse;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/CallbackServerMapper.class */
public interface CallbackServerMapper {
    int insert(CallbackServer callbackServer);

    int update(CallbackServer callbackServer);

    int delete(@Param("orgId") Long l, @Param("callbackTag") String str);

    CallbackServer getOneByTag(@Param("orgId") Long l, @Param("callbackTag") String str);

    List<CallbackServerResponse> listCallbackServers(ListCallbackServersRequest listCallbackServersRequest);

    int deleteByDeviceSn(@Param("orgId") Long l, @Param("callbackTag") String str, @Param("deviceSn") String str2);

    CallbackServer getOneByTagAndDeviceSn(@Param("orgId") Long l, @Param("callbackTag") String str, @Param("deviceSn") String str2);

    int updateByDeviceSn(CallbackServer callbackServer);
}
