package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.UpgradeDeviceRecord;
import com.moredian.onpremise.core.model.request.ListUpgradeRecordRequest;
import com.moredian.onpremise.core.model.response.UpgradeRecordResponse;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/UpgradeRecordMapper.class */
public interface UpgradeRecordMapper {
    int insert(UpgradeDeviceRecord upgradeDeviceRecord);

    List<String> getDeviceSnByScheduleId(@Param("upgradeScheduleId") Long l, @Param("upgradeStatus") int i);

    int countDeviceByStatus(@Param("upgradeScheduleId") Long l, @Param("upgradeStatus") Integer num);

    int updateStatusByScheduleId(@Param("upgradeScheduleId") Long l, @Param("oldStatus") int i, @Param("newStatus") int i2, @Param("remark") String str);

    int forceCloseByScheduleId(@Param("upgradeScheduleId") Long l, @Param("newStatus") int i, @Param("remark") String str);

    int updateStatusByDeviceSn(@Param("deviceSn") String str, @Param("oldStatus") int i, @Param("upgradeScheduleId") Long l, @Param("newStatus") int i2, @Param("remark") String str2);

    List<UpgradeRecordResponse> listUpgradeRecord(ListUpgradeRecordRequest listUpgradeRecordRequest);

    UpgradeDeviceRecord getByDeviceSn(@Param("deviceSn") String str);

    int countUnfinishedByScheduleId(@Param("upgradeScheduleId") Long l);
}
