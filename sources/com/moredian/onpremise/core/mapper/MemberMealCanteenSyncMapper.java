package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.MemberMealCanteenSync;
import com.moredian.onpremise.core.model.response.TerminalSyncMemberMealCanteenResponse;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/MemberMealCanteenSyncMapper.class */
public interface MemberMealCanteenSyncMapper extends Mapper<MemberMealCanteenSync> {
    int insertMemberMealCanteenSync(MemberMealCanteenSync memberMealCanteenSync);

    int softDeleteByMember(@Param("memberId") Long l, @Param("orgId") Long l2);

    int updateByMember(MemberMealCanteenSync memberMealCanteenSync);

    MemberMealCanteenSync findByMember(@Param("memberId") Long l, @Param("orgId") Long l2);

    List<Long> getMemberListByMealCanteen(@Param("mealCanteenId") Long l, @Param("orgId") Long l2);

    List<Long> getMemberListByDevice(@Param("deviceSn") String str, @Param("orgId") Long l);

    List<MemberMealCanteenSync> getListByDevice(@Param("deviceSn") String str, @Param("orgId") Long l);

    List<TerminalSyncMemberMealCanteenResponse> getListForSync(@Param("orgId") Long l, @Param("date") String str, @Param("deviceSn") String str2);

    Date getLastModify(@Param("orgId") Long l);

    int updateNewestDeviceSnsByDevice(@Param("deviceSn") String str, @Param("orgId") Long l);

    int softDeleteByDevice(@Param("deviceSn") String str, @Param("orgId") Long l);

    int updateLastDeviceSnsByDevice(@Param("deviceSn") String str, @Param("orgId") Long l, @Param("date") String str2);
}
