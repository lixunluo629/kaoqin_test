package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.MealCanteen;
import com.moredian.onpremise.core.model.request.ListCanteenRequest;
import com.moredian.onpremise.core.model.response.ListCanteenResponse;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/MealCanteenMapper.class */
public interface MealCanteenMapper {
    MealCanteen getOneByCanteenName(@Param("canteenName") String str, @Param("orgId") Long l);

    int insert(MealCanteen mealCanteen);

    MealCanteen getLastModify(@Param("orgId") Long l);

    MealCanteen getOneById(@Param("mealCanteenId") Long l, @Param("orgId") Long l2);

    int update(MealCanteen mealCanteen);

    int deleteById(@Param("mealCanteenId") Long l, @Param("orgId") Long l2);

    int deleteByName(@Param("canteenName") String str, @Param("orgId") Long l);

    List<ListCanteenResponse> listCanteen(ListCanteenRequest listCanteenRequest);

    MealCanteen findForSync(@Param("orgId") Long l, @Param("deviceId") Long l2);

    int updateModify(@Param("gmtModify") Date date, @Param("orgId") Long l, @Param("mealCanteenId") Long l2);
}
