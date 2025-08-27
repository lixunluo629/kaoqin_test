package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.MemberCheckInTaskSync;
import com.moredian.onpremise.core.model.response.TerminalSyncMemberCheckInTaskResponse;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/MemberCheckInTaskSyncMapper.class */
public interface MemberCheckInTaskSyncMapper extends Mapper<MemberCheckInTaskSync> {
    int insertMemberCheckInTaskSync(MemberCheckInTaskSync memberCheckInTaskSync);

    int softDeleteByMember(@Param("memberId") Long l, @Param("orgId") Long l2);

    int updateByMember(MemberCheckInTaskSync memberCheckInTaskSync);

    MemberCheckInTaskSync findByMember(@Param("memberId") Long l, @Param("orgId") Long l2);

    List<Long> getMemberListByCheckInTask(@Param("checkInTaskId") Long l, @Param("orgId") Long l2);

    List<Long> getMemberListByDevice(@Param("deviceSn") String str, @Param("orgId") Long l);

    List<MemberCheckInTaskSync> getListByDevice(@Param("deviceSn") String str, @Param("orgId") Long l);

    List<TerminalSyncMemberCheckInTaskResponse> getListForSync(@Param("orgId") Long l, @Param("date") String str, @Param("deviceSn") String str2);

    Date getLastModify(@Param("orgId") Long l);

    int updateNewestDeviceSnsByDevice(@Param("deviceSn") String str, @Param("orgId") Long l);

    int softDeleteByDevice(@Param("deviceSn") String str, @Param("orgId") Long l);

    int updateLastDeviceSnsByDevice(@Param("deviceSn") String str, @Param("orgId") Long l, @Param("date") String str2);
}
