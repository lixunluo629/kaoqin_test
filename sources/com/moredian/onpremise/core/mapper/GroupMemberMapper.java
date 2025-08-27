package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.GroupMember;
import com.moredian.onpremise.core.model.dto.GroupMemberDto;
import com.moredian.onpremise.core.model.response.MemberListResponse;
import com.moredian.onpremise.core.model.response.TerminalGroupMemberSyncResponse;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/GroupMemberMapper.class */
public interface GroupMemberMapper {
    List<GroupMemberDto> listMemberByGroupId(@Param("groupId") Long l, @Param("orgId") Long l2);

    List<GroupMemberDto> listMemberByGroupIdExcludeAll(@Param("groupId") Long l, @Param("orgId") Long l2);

    List<GroupMemberDto> listUserAddMemberByGroupIdExcludeAll(@Param("groupId") Long l, @Param("orgId") Long l2);

    int insertGroupMember(GroupMember groupMember);

    int insertDeleteGroupMember(GroupMember groupMember);

    int deleteGroupMember(@Param("groupId") Long l, @Param("memberId") Long l2, @Param("orgId") Long l3);

    int batchDeleteGroupMember(@Param("groupId") Long l, @Param("memberIds") List<Long> list, @Param("orgId") Long l2);

    int deleteGroupDeptMember(@Param("groupId") Long l, @Param("deptId") Long l2, @Param("memberId") Long l3, @Param("orgId") Long l4);

    List<GroupMemberDto> listByMemberId(@Param("memberId") Long l, @Param("orgId") Long l2);

    List<GroupMember> listDeptByGroupId(@Param("groupId") Long l, @Param("orgId") Long l2, @Param("type") Integer num);

    List<TerminalGroupMemberSyncResponse> listSyncGroupMember(@Param("orgId") Long l, @Param("lastSyncTime") String str);

    int deleteGroupDept(@Param("deptId") Long l, @Param("orgId") Long l2, @Param("groupId") Long l3);

    int deleteGroupDeptByGroupIdList(@Param("deptId") Long l, @Param("orgId") Long l2, @Param("groupIdList") List<Long> list);

    int deleteByGroup(@Param("groupId") Long l, @Param("orgId") Long l2);

    List<Long> listGroupIdByDeptId(@Param("deptId") Long l, @Param("orgId") Long l2);

    List<Long> listGroupIdByMemberId(@Param("memberId") Long l, @Param("orgId") Long l2);

    List<GroupMember> listByDeptId(@Param("deptId") Long l, @Param("orgId") Long l2);

    List<Long> listGroupIdByDeptIds(@Param("deptIds") List<Long> list, @Param("orgId") Long l);

    Integer countMembersByGroup(@Param("groupId") Long l, @Param("orgId") Long l2);

    Integer countByGroup(@Param("groupId") Long l, @Param("orgId") Long l2);

    List<GroupMember> listByGroupIdAndQueryDate(@Param("groupId") Long l, @Param("orgId") Long l2, @Param("queryDate") String str, @Param("deleteOrNot") Integer num);

    GroupMember getByMemberAndGroupId(@Param("memberId") Long l, @Param("groupId") Long l2, @Param("orgId") Long l3);

    GroupMember getByDeptAndGroupId(@Param("deptId") Long l, @Param("groupId") Long l2, @Param("orgId") Long l3);

    GroupMember getByDeptMemberAndGroupId(@Param("memberId") Long l, @Param("deptId") Long l2, @Param("groupId") Long l3, @Param("orgId") Long l4);

    int updateModifyTime(@Param("groupId") Long l, @Param("memberId") Long l2, @Param("orgId") Long l3);

    int batchUpdateModifyTime(@Param("groupId") Long l, @Param("memberIds") List<Long> list, @Param("orgId") Long l2);

    GroupMember getLastModify(@Param("orgId") Long l);

    int insertGroupMemberList(@Param("groupId") Long l, @Param("orgId") Long l2, @Param("memberLists") List<MemberListResponse> list);

    int batchInsert(@Param("memberLists") List<GroupMember> list);

    List<GroupMember> listByMemberAndGroup(@Param("memberListResponses") List<MemberListResponse> list, @Param("groupId") Long l, @Param("orgId") Long l2);

    List<Long> getDeptIdListByGroupId(@Param("groupId") Long l, @Param("orgId") Long l2);

    List<GroupMemberDto> listByMemberIdsAndDeptIds(@Param("memberIds") List<Long> list, @Param("deptIds") List<Long> list2, @Param("orgId") Long l);

    List<Long> getMemberIdListByGroupId(@Param("groupId") Long l, @Param("orgId") Long l2);

    List<Long> getDeptIdListByGroupIds(@Param("groupIds") List<Long> list, @Param("orgId") Long l);

    List<Long> getMemberIdListByGroupIds(@Param("groupIds") List<Long> list, @Param("orgId") Long l);
}
