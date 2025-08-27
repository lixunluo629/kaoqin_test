package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.Organization;
import com.moredian.onpremise.core.model.request.UpdateOrgInfoRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/OrganizationMapper.class */
public interface OrganizationMapper {
    Organization getOne();

    Organization getOneById(@Param("orgId") Long l);

    int updateOrgInfo(UpdateOrgInfoRequest updateOrgInfoRequest);
}
