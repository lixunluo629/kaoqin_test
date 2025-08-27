package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.AttendanceEvent;
import com.moredian.onpremise.core.model.request.AttendanceEventListRequest;
import com.moredian.onpremise.core.model.response.AttendanceEventDetailResponse;
import com.moredian.onpremise.core.model.response.AttendanceEventListResponse;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/AttendanceEventMapper.class */
public interface AttendanceEventMapper extends Mapper<AttendanceEvent> {
    int insertOne(AttendanceEvent attendanceEvent);

    int softDeleteById(@Param("orgId") Long l, @Param("attendanceEventId") Long l2);

    int updateOne(AttendanceEvent attendanceEvent);

    AttendanceEvent findById(@Param("orgId") Long l, @Param("attendanceEventId") Long l2);

    List<AttendanceEventListResponse> pageFind(AttendanceEventListRequest attendanceEventListRequest);

    List<AttendanceEventDetailResponse> getByMemberId(@Param("orgId") Long l, @Param("memberId") Long l2);

    AttendanceEventDetailResponse findByIdAndMemberId(@Param("orgId") Long l, @Param("attendanceEventId") Long l2, @Param("memberId") Long l3);
}
