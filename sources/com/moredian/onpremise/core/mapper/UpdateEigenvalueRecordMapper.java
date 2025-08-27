package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.UpdateEigenvalueRecord;
import com.moredian.onpremise.core.model.request.MemberInputFaceRecordListRequest;
import com.moredian.onpremise.core.model.response.MemberInputFaceRecordListResponse;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/UpdateEigenvalueRecordMapper.class */
public interface UpdateEigenvalueRecordMapper {
    int insertRecord(UpdateEigenvalueRecord updateEigenvalueRecord);

    List<String> listOldUrl(@Param("queryTime") String str);

    int batchInsertRecord(@Param("updateEigenvalueRecordList") List<UpdateEigenvalueRecord> list);

    List<MemberInputFaceRecordListResponse> eigenvalueRecordList(MemberInputFaceRecordListRequest memberInputFaceRecordListRequest);
}
