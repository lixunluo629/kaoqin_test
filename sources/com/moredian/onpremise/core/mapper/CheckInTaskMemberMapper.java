package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.CheckInTaskMember;
import com.moredian.onpremise.core.model.dto.CheckInMemberDto;
import com.moredian.onpremise.core.model.dto.CheckInTaskMemberDto;
import com.moredian.onpremise.core.model.request.CheckInLogListRequest;
import com.moredian.onpremise.core.model.response.CheckInLogListResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/CheckInTaskMemberMapper.class */
public interface CheckInTaskMemberMapper extends Mapper<CheckInTaskMember> {
    int insertCheckInTaskMember(CheckInTaskMember checkInTaskMember);

    int insertBatch(@Param("list") List<CheckInTaskMember> list);

    int deleteById(@Param("orgId") Long l, @Param("id") Long l2);

    int deleteByTaskId(@Param("orgId") Long l, @Param("taskId") Long l2);

    int softDeleteById(@Param("gmtModify") Date date, @Param("orgId") Long l, @Param("id") Long l2);

    int softDeleteByTaskId(@Param("gmtModify") Date date, @Param("orgId") Long l, @Param("taskId") Long l2);

    int softDeleteByDeptId(@Param("gmtModify") Date date, @Param("orgId") Long l, @Param("deptId") Long l2);

    int softDeleteByMemberId(@Param("gmtModify") Date date, @Param("orgId") Long l, @Param("memberId") Long l2);

    CheckInTaskMember findById(@Param("orgId") Long l, @Param("id") Long l2);

    List<CheckInTaskMember> getByTaskId(@Param("orgId") Long l, @Param("taskId") Long l2);

    List<CheckInMemberDto> getByTaskIdJoinMemberAndDept(@Param("orgId") Long l, @Param("taskId") Long l2);

    List<CheckInMemberDto> getConfirmByTaskIdJoinMemberAndDept(@Param("orgId") Long l, @Param("taskId") Long l2);

    int countByTaskId(@Param("orgId") Long l, @Param("taskId") Long l2);

    List<CheckInLogListResponse> pageFind(CheckInLogListRequest checkInLogListRequest);

    int countByTaskIdAndMemberId(@Param("orgId") Long l, @Param("taskId") Long l2, @Param("memberId") Long l3);

    int countByTaskIdAndDeptId(@Param("orgId") Long l, @Param("taskId") Long l2, @Param("deptId") Long l3);

    List<CheckInTaskMember> findForSync(Map map);

    List<CheckInTaskMember> findCond(Map map);

    CheckInTaskMember findLast(@Param("orgId") Long l);

    List<CheckInTaskMemberDto> listByTaskId(@Param("taskId") Long l, @Param("orgId") Long l2);

    List<CheckInTaskMember> listByDeptId(@Param("deptId") Long l, @Param("orgId") Long l2);

    List<CheckInTaskMember> listByMemberId(@Param("memberId") Long l, @Param("orgId") Long l2);

    int softDeleteByTaskIdList(@Param("deptId") Long l, @Param("orgId") Long l2, @Param("taskIdList") List<Long> list);

    List<CheckInTaskMember> listByMemberIdsAndDeptIds(@Param("memberIds") List<Long> list, @Param("deptIds") List<Long> list2, @Param("orgId") Long l);
}
