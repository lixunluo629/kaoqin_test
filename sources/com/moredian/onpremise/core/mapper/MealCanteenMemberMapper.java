package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.MealCanteenMember;
import com.moredian.onpremise.core.model.dto.GroupMemberDto;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/MealCanteenMemberMapper.class */
public interface MealCanteenMemberMapper {
    int deleteCanteenByDept(@Param("deptId") Long l, @Param("orgId") Long l2, @Param("mealCanteenId") Long l3);

    int deleteByMealCanteenIdList(@Param("deptId") Long l, @Param("orgId") Long l2, @Param("mealCanteenIdList") List<Long> list);

    int deleteCanteenMember(@Param("orgId") Long l, @Param("mealCanteenId") Long l2);

    int deleteCanteenByMember(@Param("memberId") Long l, @Param("orgId") Long l2, @Param("mealCanteenId") Long l3);

    int insertCanteenMember(MealCanteenMember mealCanteenMember);

    int batchInsert(@Param("memberLists") List<MealCanteenMember> list);

    List<GroupMemberDto> listConfirmMemberByCanteenId(@Param("mealCanteenId") Long l, @Param("orgId") Long l2);

    List<GroupMemberDto> listMemberByCanteenId(@Param("mealCanteenId") Long l, @Param("orgId") Long l2);

    List<MealCanteenMember> listByCanteenIdAndType(@Param("mealCanteenId") Long l, @Param("orgId") Long l2, @Param("type") int i);

    MealCanteenMember getByMemberIdAndCanteenId(@Param("memberId") Long l, @Param("mealCanteenId") Long l2, @Param("orgId") Long l3);

    List<MealCanteenMember> findCond(Map map);

    List<MealCanteenMember> getByMealCanteenId(@Param("orgId") Long l, @Param("mealCanteenId") Long l2);

    int softDeleteById(@Param("gmtModify") Date date, @Param("orgId") Long l, @Param("mealCanteenMemberId") Long l2);

    List<MealCanteenMember> listByDeptId(@Param("deptId") Long l, @Param("orgId") Long l2);

    List<MealCanteenMember> listByMemberId(@Param("memberId") Long l, @Param("orgId") Long l2);

    List<MealCanteenMember> listByMemberIdsAndDeptIds(@Param("memberIds") List<Long> list, @Param("deptIds") List<Long> list2, @Param("orgId") Long l);

    Long getCanteenIdByMemberId(@Param("memberId") Long l);
}
