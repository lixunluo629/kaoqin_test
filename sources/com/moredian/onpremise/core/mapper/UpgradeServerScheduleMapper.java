package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.UpgradeServerSchedule;
import com.moredian.onpremise.core.model.request.ListServerScheduleRequest;
import com.moredian.onpremise.core.model.response.ListServerScheduleResponse;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/UpgradeServerScheduleMapper.class */
public interface UpgradeServerScheduleMapper {
    List<ListServerScheduleResponse> listServerSchedule(ListServerScheduleRequest listServerScheduleRequest);

    int insert(UpgradeServerSchedule upgradeServerSchedule);

    int delete(@Param("serverScheduleId") Long l, @Param("orgId") Long l2);

    int updateCurrentVersion(@Param("serverScheduleId") Long l, @Param("oldFlag") Integer num, @Param("newFlag") Integer num2);

    UpgradeServerSchedule getOneById(@Param("serverScheduleId") Long l, @Param("orgId") Long l2);

    List<String> listPackageName(@Param("queryTime") String str);
}
