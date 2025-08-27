package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.MealMember;
import com.moredian.onpremise.core.model.request.ListMealMemberRequest;
import com.moredian.onpremise.core.model.response.ListMealMemberResponse;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/MealMemberMapper.class */
public interface MealMemberMapper {
    int insert(MealMember mealMember);

    int update(MealMember mealMember);

    int delete(@Param("memberId") Long l);

    MealMember getMealMemberByMemberId(@Param("memberId") Long l);

    MealMember getMealMemberByMemberWorkNum(@Param("memberWorkNum") String str);

    List<ListMealMemberResponse> listMealMember(ListMealMemberRequest listMealMemberRequest);

    Integer countJoinNum(@Param("memberJoinTimeStart") String str, @Param("memberJoinTimeEnd") String str2, @Param("mealCanteenId") Long l);

    Integer countRetireNum(@Param("memberRetireTimeStart") String str, @Param("memberRetireTimeEnd") String str2, @Param("mealCanteenId") Long l);

    Integer countShiftAndSexNum(@Param("memberJoinTimeStart") String str, @Param("memberJoinTimeEnd") String str2, @Param("shiftStatus") Integer num, @Param("memberGender") Integer num2, @Param("mealCanteenId") Long l);

    List<Map<String, Long>> countCard(@Param("timeStart") String str, @Param("timeEnd") String str2, @Param("recordType") Integer num, @Param("mealCanteenId") Long l);
}
