package com.moredian.onpremise.api.meal;

import com.moredian.onpremise.core.model.request.BatchSaveMealMemberRequest;
import com.moredian.onpremise.core.model.request.BatchUpdateCardStatusRequest;
import com.moredian.onpremise.core.model.request.BindMealCardRequest;
import com.moredian.onpremise.core.model.request.ListMealMemberRequest;
import com.moredian.onpremise.core.model.request.MemberDetailsRequest;
import com.moredian.onpremise.core.model.request.SaveMealMemberRequest;
import com.moredian.onpremise.core.model.request.UnbindMealCardRequest;
import com.moredian.onpremise.core.model.request.UpdateCardStatusRequest;
import com.moredian.onpremise.core.model.response.BatchInsertMemberResponse;
import com.moredian.onpremise.core.model.response.ListMealMemberResponse;
import com.moredian.onpremise.core.model.response.MealMemberDetailResponse;
import com.moredian.onpremise.core.model.response.SaveMemberResponse;
import com.moredian.onpremise.core.utils.PageList;
import java.util.List;

/* loaded from: onpremise-api-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/api/meal/MealMemberService.class */
public interface MealMemberService {
    PageList<ListMealMemberResponse> listMealMember(ListMealMemberRequest listMealMemberRequest);

    boolean unbindCard(UnbindMealCardRequest unbindMealCardRequest);

    boolean bindCard(BindMealCardRequest bindMealCardRequest);

    boolean updateCardStatus(UpdateCardStatusRequest updateCardStatusRequest);

    boolean batchUpdateCardStatus(BatchUpdateCardStatusRequest batchUpdateCardStatusRequest);

    SaveMemberResponse saveMealTempMember(SaveMealMemberRequest saveMealMemberRequest);

    PageList<ListMealMemberResponse> listMealTempMember(ListMealMemberRequest listMealMemberRequest);

    MealMemberDetailResponse getMealMemberDetail(MemberDetailsRequest memberDetailsRequest);

    List<BatchInsertMemberResponse> batchSaveMealTempMember(BatchSaveMealMemberRequest batchSaveMealMemberRequest);
}
