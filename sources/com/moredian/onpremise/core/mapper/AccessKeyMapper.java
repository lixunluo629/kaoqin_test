package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.AccessKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/AccessKeyMapper.class */
public interface AccessKeyMapper {
    AccessKey getOneBySecret(@Param("secret") String str, @Param("deviceSn") String str2);

    AccessKey getOneByDeviceSn(@Param("deviceSn") String str);

    int updateKeyStatus(@Param("accessKeyId") Long l, @Param("oldStatus") Integer num, @Param("newStatus") Integer num2);

    int insert(AccessKey accessKey);
}
