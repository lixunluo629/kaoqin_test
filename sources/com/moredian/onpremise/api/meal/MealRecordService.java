package com.moredian.onpremise.api.meal;

import com.moredian.onpremise.core.model.request.CountMealCardRequest;
import com.moredian.onpremise.core.model.request.CountMealRecordRequest;
import com.moredian.onpremise.core.model.request.ListMealRecordRequest;
import com.moredian.onpremise.core.model.request.SaveMealRecordRequest;
import com.moredian.onpremise.core.model.response.CountMealCardResponse;
import com.moredian.onpremise.core.model.response.CountMealRecordResponse;
import com.moredian.onpremise.core.model.response.ListMealRecordResponse;
import com.moredian.onpremise.core.model.response.SaveMealRecordResponse;
import com.moredian.onpremise.core.utils.PageList;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;

/* loaded from: onpremise-api-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/api/meal/MealRecordService.class */
public interface MealRecordService {
    PageList<ListMealRecordResponse> listSuccessRecord(ListMealRecordRequest listMealRecordRequest);

    PageList<ListMealRecordResponse> listErrorRecord(ListMealRecordRequest listMealRecordRequest);

    SaveMealRecordResponse saveMealRecord(SaveMealRecordRequest saveMealRecordRequest);

    PageList<CountMealRecordResponse> countMealRecord(CountMealRecordRequest countMealRecordRequest);

    PageList<CountMealCardResponse> countMealCard(CountMealCardRequest countMealCardRequest);

    void countMealRecordExportPDF(CountMealRecordRequest countMealRecordRequest, HttpServletResponse httpServletResponse);

    void countMealCardExportPDF(CountMealCardRequest countMealCardRequest, HttpServletResponse httpServletResponse);

    boolean delete(Date date, Date date2);

    String countMealTotal(CountMealRecordRequest countMealRecordRequest);
}
