package com.moredian.onpremise.api.meal;

import com.moredian.onpremise.core.model.request.CheckMemberHasCardRequest;
import com.moredian.onpremise.core.model.request.DeleteMealCardRequest;
import com.moredian.onpremise.core.model.request.ListMealCardRequest;
import com.moredian.onpremise.core.model.request.QueryMealCardDetailRequest;
import com.moredian.onpremise.core.model.request.SaveMealCardRequest;
import com.moredian.onpremise.core.model.request.SendCardToMemberRequest;
import com.moredian.onpremise.core.model.response.CheckMemberHasCardResponse;
import com.moredian.onpremise.core.model.response.ListMealCardResponse;
import com.moredian.onpremise.core.model.response.ListSearchMealCardResponse;
import com.moredian.onpremise.core.model.response.MealCardDetailResponse;
import com.moredian.onpremise.core.utils.PageList;
import java.util.List;

/* loaded from: onpremise-api-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/api/meal/MealCardService.class */
public interface MealCardService {
    Long insert(SaveMealCardRequest saveMealCardRequest);

    Long update(SaveMealCardRequest saveMealCardRequest);

    boolean delete(DeleteMealCardRequest deleteMealCardRequest);

    PageList<ListMealCardResponse> listMealCard(ListMealCardRequest listMealCardRequest);

    PageList<ListSearchMealCardResponse> listSearchMealCard(ListMealCardRequest listMealCardRequest);

    MealCardDetailResponse getDetailById(QueryMealCardDetailRequest queryMealCardDetailRequest);

    boolean sendCardToMember(SendCardToMemberRequest sendCardToMemberRequest);

    List<CheckMemberHasCardResponse> checkMemberHasCard(CheckMemberHasCardRequest checkMemberHasCardRequest);
}
