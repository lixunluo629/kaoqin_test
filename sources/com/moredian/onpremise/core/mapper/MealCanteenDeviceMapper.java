package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.MealCanteenDevice;
import com.moredian.onpremise.core.model.dto.DeviceDto;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/MealCanteenDeviceMapper.class */
public interface MealCanteenDeviceMapper {
    List<DeviceDto> listDeviceByCanteenIdNotDelete(@Param("mealCanteenId") Long l, @Param("orgId") Long l2);

    int deleteCanteenDevice(@Param("mealCanteenId") Long l, @Param("deviceSn") String str, @Param("orgId") Long l2);

    int insertCanteenDevice(MealCanteenDevice mealCanteenDevice);

    Integer countDeviceByCanteenId(@Param("mealCanteenId") Long l, @Param("orgId") Long l2);

    MealCanteenDevice getByDeviceSnAndCanteenId(@Param("deviceSn") String str, @Param("mealCanteenId") Long l, @Param("orgId") Long l2);

    MealCanteenDevice getOneByDeviceSn(@Param("orgId") Long l, @Param("deviceSn") String str);

    int countByCanteenIdAndDeviceSn(@Param("orgId") Long l, @Param("mealCanteenId") Long l2, @Param("deviceSn") String str);

    List<MealCanteenDevice> getByMealCanteenId(@Param("orgId") Long l, @Param("mealCanteenId") Long l2);

    int softDeleteById(@Param("gmtModify") Date date, @Param("orgId") Long l, @Param("mealCanteenDeviceId") Long l2);

    int softDeleteByDeviceSns(@Param("gmtModify") Date date, @Param("orgId") Long l, @Param("deviceSns") List<String> list);
}
