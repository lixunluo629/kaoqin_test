package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.MealCardMember;
import com.moredian.onpremise.core.model.dto.MemberDto;
import com.moredian.onpremise.core.model.info.MealCardMemberInfo;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/MealCardMemberMapper.class */
public interface MealCardMemberMapper {
    List<MemberDto> listMemberByCardId(@Param("mealCardId") Long l, @Param("orgId") Long l2);

    int deleteCardByMember(@Param("memberId") Long l, @Param("orgId") Long l2, @Param("notMealCardId") Long l3);

    int deleteCardByCardId(@Param("orgId") Long l, @Param("mealCardId") Long l2);

    int deleteCardByCardIdAndMemberId(@Param("memberId") Long l, @Param("orgId") Long l2, @Param("mealCardId") Long l3);

    Integer countMemberByCardId(@Param("mealCardId") Long l, @Param("orgId") Long l2);

    MealCardMemberInfo getOneByMemberId(@Param("memberId") Long l, @Param("orgId") Long l2);

    MealCardMember getOneByMemberIdAndAccountId(@Param("memberId") Long l, @Param("orgId") Long l2, @Param("accountId") Long l3);

    int insert(MealCardMember mealCardMember);

    int updateCardStatus(@Param("memberId") Long l, @Param("orgId") Long l2, @Param("cardStatus") Integer num);
}
