package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.ExternalContact;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/ExternalContactMapper.class */
public interface ExternalContactMapper extends Mapper<ExternalContact> {
    int insertOne(ExternalContact externalContact);

    int softDeleteExpired(@Param("orgId") Long l, @Param("expiredTimestamp") Long l2);

    ExternalContact getLastModify(@Param("orgId") Long l);

    List<ExternalContact> syncExternalContact(@Param("orgId") Long l, @Param("lastModifyTimestamp") Long l2);

    List<ExternalContact> syncExternalContactEigenvalueValue(@Param("orgId") Long l, @Param("ids") List<Long> list);

    List<String> getListFaceUrl(@Param("orgId") Long l, @Param("expiredTimestamp") Long l2);

    ExternalContact getOneById(@Param("orgId") Long l, @Param("id") Long l2);

    ExternalContact getOneByIdCard(@Param("orgId") Long l, @Param("idCard") String str);
}
