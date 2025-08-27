package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.AuthModule;
import com.moredian.onpremise.core.model.response.AuthModuleResponse;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/AuthModuleMapper.class */
public interface AuthModuleMapper {
    List<AuthModuleResponse> listById(@Param("moduleIds") List<String> list, @Param("orgId") Long l, @Param("appTypes") List<Integer> list2);

    int insert(AuthModule authModule);

    int update(AuthModule authModule);

    AuthModule getOneById(@Param("moduleId") Long l, @Param("orgId") Long l2);
}
