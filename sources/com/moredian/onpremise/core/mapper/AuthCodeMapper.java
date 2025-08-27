package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.common.constants.AuthConstants;
import com.moredian.onpremise.core.model.domain.AuthCode;
import com.moredian.onpremise.core.model.response.ListAuthCodeResponse;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/AuthCodeMapper.class */
public interface AuthCodeMapper {
    int insert(AuthCode authCode);

    int delete(@Param("authCodeId") Long l);

    int countAuthCode(@Param("orgId") Long l);

    List<ListAuthCodeResponse> listAuthCode(@Param("orgId") Long l);

    int countAllowDeviceNum(@Param("orgId") Long l);

    AuthCode getByCode(@Param("authCode") String str, @Param("orgId") Long l, @Param(AuthConstants.AUTH_PARAM_GENERATE_TIME_KEY) Long l2);
}
