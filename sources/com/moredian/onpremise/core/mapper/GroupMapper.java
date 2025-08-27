package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.Group;
import com.moredian.onpremise.core.model.domain.GroupDevice;
import com.moredian.onpremise.core.model.dto.GroupDeviceGroupDto;
import com.moredian.onpremise.core.model.response.GroupAuthListResponse;
import com.moredian.onpremise.core.model.response.GroupListResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncGroupResponse;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/GroupMapper.class */
public interface GroupMapper {
    int insertGroup(Group group);

    int updateGroup(Group group);

    Group getOneById(@Param("groupId") Long l, @Param("orgId") Long l2);

    int deleteGroup(@Param("groupId") Long l, @Param("orgId") Long l2);

    List<GroupAuthListResponse> listGroupAuth(@Param("groupName") String str, @Param("accountId") Long l, @Param("orgId") Long l2);

    Group getDefaultGroup(@Param("orgId") Long l);

    List<TerminalSyncGroupResponse> listSyncGroup(@Param("orgId") Long l, @Param("date") String str, @Param("groupIds") List<Long> list);

    int unbindDefaultGroup(@Param("groupId") Long l, @Param("orgId") Long l2);

    Group getOneByGroupName(@Param("groupName") String str, @Param("orgId") Long l);

    int updateModifyTime(@Param("groupId") Long l, @Param("orgId") Long l2);

    int batchUpdateModifyTime(@Param("groupIds") List<Long> list, @Param("orgId") Long l);

    List<GroupDeviceGroupDto> listByGroupId(@Param("groupIds") List<Long> list, @Param("orgId") Long l);

    Group getLastModify(@Param("orgId") Long l);

    List<String> listGroupNameByGroupIds(@Param("groupIds") List<Long> list, @Param("orgId") Long l);

    Group checkIsAllMemberGroup(@Param("groupDevices") List<GroupDevice> list);

    List<GroupListResponse> listGroup(@Param("groupName") String str, @Param("groupIds") List<Long> list, @Param("orgId") Long l);
}
