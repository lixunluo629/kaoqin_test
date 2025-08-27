package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.UpgradeDeviceSchedule;
import com.moredian.onpremise.core.model.request.ListUpgradeScheduleRequest;
import com.moredian.onpremise.core.model.response.UpgradeScheduleResponse;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/UpgradeScheduleMapper.class */
public interface UpgradeScheduleMapper {
    int insert(UpgradeDeviceSchedule upgradeDeviceSchedule);

    int countNotCompleteSchedule(@Param("orgId") Long l);

    UpgradeDeviceSchedule getWaitingSchedule(@Param("orgId") Long l);

    int updateScheduleStatus(@Param("upgradeScheduleId") Long l, @Param("oldStatus") Integer num, @Param("newStatus") int i);

    int forceCloseScheduleStatus(@Param("upgradeScheduleId") Long l, @Param("newStatus") int i);

    List<UpgradeScheduleResponse> listUpgradeSchedule(ListUpgradeScheduleRequest listUpgradeScheduleRequest);

    UpgradeDeviceSchedule getOneByScheduleId(@Param("upgradeScheduleId") Long l);

    List<String> listPackageUrl(@Param("queryTime") String str);
}
