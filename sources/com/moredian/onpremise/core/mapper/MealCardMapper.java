package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.MealCard;
import com.moredian.onpremise.core.model.request.ListMealCardRequest;
import com.moredian.onpremise.core.model.response.ListMealCardResponse;
import com.moredian.onpremise.core.model.response.ListSearchMealCardResponse;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/MealCardMapper.class */
public interface MealCardMapper {
    int insert(MealCard mealCard);

    int update(MealCard mealCard);

    int delete(@Param("mealCardId") Long l, @Param("orgId") Long l2);

    MealCard getOneByCardName(@Param("cardName") String str, @Param("orgId") Long l);

    MealCard getOneById(@Param("mealCardId") Long l, @Param("orgId") Long l2);

    List<ListMealCardResponse> listMealCard(ListMealCardRequest listMealCardRequest);

    MealCard getOneByMemberId(@Param("memberId") Long l, @Param("orgId") Long l2);

    List<ListSearchMealCardResponse> listSearchMealCard(ListMealCardRequest listMealCardRequest);
}
