package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.MealCanteenTime;
import com.moredian.onpremise.core.model.dto.CanteenTimeDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/MealCanteenTimeMapper.class */
public interface MealCanteenTimeMapper {
    List<CanteenTimeDto> listTimeByCanteenId(@Param("mealCanteenId") Long l, @Param("orgId") Long l2);

    int deleteCanteenTime(@Param("mealCanteenId") Long l, @Param("orgId") Long l2, @Param("mealCanteenTimeId") Long l3);

    int insertCanteenTime(MealCanteenTime mealCanteenTime);
}
