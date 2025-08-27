package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.MealRecord;
import com.moredian.onpremise.core.model.domain.VerifyRecord;
import com.moredian.onpremise.core.model.request.CountMealCardRequest;
import com.moredian.onpremise.core.model.request.CountMealRecordRequest;
import com.moredian.onpremise.core.model.request.ListMealRecordRequest;
import com.moredian.onpremise.core.model.response.CountMealCardResponse;
import com.moredian.onpremise.core.model.response.CountMealRecordResponse;
import com.moredian.onpremise.core.model.response.ListMealRecordResponse;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/MealRecordMapper.class */
public interface MealRecordMapper {
    List<ListMealRecordResponse> listMealRecord(ListMealRecordRequest listMealRecordRequest);

    int insert(MealRecord mealRecord);

    int countByMemberIdAndType(@Param("memberId") Long l, @Param("recordType") Integer num, @Param("orgId") Long l2, @Param("verifyResult") Integer num2, @Param("verifyDay") Integer num3);

    List<CountMealRecordResponse> countMealRecord(CountMealRecordRequest countMealRecordRequest);

    List<CountMealRecordResponse> countMealRecordGroupByMemberOrDept(CountMealRecordRequest countMealRecordRequest);

    List<CountMealCardResponse> countMealCard(CountMealCardRequest countMealCardRequest);

    Integer countVerifyRecord(VerifyRecord verifyRecord);

    List<String> listSnapUrl(@Param("date") Date date);

    int deleteByDate(@Param("date") Date date);

    Integer countMealRecordByCondition(@Param("canteenId") Long l, @Param("startDay") String str, @Param("endDay") String str2, @Param("verifySuccess") Integer num);
}
