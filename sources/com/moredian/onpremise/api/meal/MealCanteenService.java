package com.moredian.onpremise.api.meal;

import com.moredian.onpremise.core.model.request.DeleteMealCanteenRequest;
import com.moredian.onpremise.core.model.request.ListCanteenRequest;
import com.moredian.onpremise.core.model.request.QueryCanteenDetailsRequest;
import com.moredian.onpremise.core.model.request.SaveMealCanteenRequest;
import com.moredian.onpremise.core.model.request.TerminalSyncCanteenRequest;
import com.moredian.onpremise.core.model.request.TerminalSyncRequest;
import com.moredian.onpremise.core.model.response.ListCanteenResponse;
import com.moredian.onpremise.core.model.response.QueryCanteenDetailsResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncCanteenResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncMemberMealCanteenResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncResponse;
import com.moredian.onpremise.core.utils.PageList;

/* loaded from: onpremise-api-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/api/meal/MealCanteenService.class */
public interface MealCanteenService {
    Long insert(SaveMealCanteenRequest saveMealCanteenRequest);

    Long update(SaveMealCanteenRequest saveMealCanteenRequest);

    boolean deleteById(DeleteMealCanteenRequest deleteMealCanteenRequest);

    boolean deleteByName(DeleteMealCanteenRequest deleteMealCanteenRequest);

    QueryCanteenDetailsResponse getDetailsById(QueryCanteenDetailsRequest queryCanteenDetailsRequest);

    QueryCanteenDetailsResponse getDetailsByName(QueryCanteenDetailsRequest queryCanteenDetailsRequest);

    PageList<ListCanteenResponse> listCanteen(ListCanteenRequest listCanteenRequest);

    TerminalSyncCanteenResponse syncCanteen(TerminalSyncCanteenRequest terminalSyncCanteenRequest);

    void generateMealCanteenBaseLibraryByMember(Long l, Long l2, boolean z);

    void generateMealCanteenBaseLibraryByDept(Long l, Long l2);

    void generateMealCanteenBaseLibraryByMealCanteen(Long l, Long l2, boolean z);

    void generateMealCanteenBaseLibraryByDevice(String str, Long l);

    TerminalSyncResponse<TerminalSyncMemberMealCanteenResponse> syncMealCanteenMember(TerminalSyncRequest terminalSyncRequest);

    void doSendNettyMessageForSyncMember(Long l);

    void notify(Long l, Long l2, Integer num);
}
