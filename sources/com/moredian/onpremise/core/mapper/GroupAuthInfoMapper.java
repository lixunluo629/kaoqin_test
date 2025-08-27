package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.GroupAuthInfo;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/GroupAuthInfoMapper.class */
public interface GroupAuthInfoMapper {
    List<GroupAuthInfo> listAuthByGroupId(@Param("groupId") Long l, @Param("orgId") Long l2);

    int insertGroupAuthInfo(GroupAuthInfo groupAuthInfo);

    int deleteGroupAuthInfo(@Param("groupId") Long l, @Param("orgId") Long l2);

    List<GroupAuthInfo> listSyncGroupAuth(@Param("orgId") Long l, @Param("lastSyncTime") String str);

    int updateGroupAuthInfoByGroupId(GroupAuthInfo groupAuthInfo);

    GroupAuthInfo getAuthByGroupId(@Param("groupId") Long l, @Param("orgId") Long l2);
}
