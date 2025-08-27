package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.CheckInSupplement;
import com.moredian.onpremise.core.model.request.ListCheckInSupplementRequest;
import com.moredian.onpremise.core.model.response.CheckInSupplementResponse;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/CheckInSupplementMapper.class */
public interface CheckInSupplementMapper {
    void insert(CheckInSupplement checkInSupplement);

    List<CheckInSupplementResponse> pageFind(ListCheckInSupplementRequest listCheckInSupplementRequest);

    CheckInSupplementResponse findById(@Param("supplementId") Long l, @Param("orgId") Long l2);

    void update(CheckInSupplement checkInSupplement);

    void delete(@Param("supplementId") Long l);
}
