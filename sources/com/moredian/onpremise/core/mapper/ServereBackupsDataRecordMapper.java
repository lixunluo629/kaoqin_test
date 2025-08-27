package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.ServereBackupsDataRecord;
import com.moredian.onpremise.core.model.request.ListServerBackupsRecordRequest;
import com.moredian.onpremise.core.model.response.ListServerBackupsRecordResponse;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/ServereBackupsDataRecordMapper.class */
public interface ServereBackupsDataRecordMapper {
    List<ListServerBackupsRecordResponse> listRecord(ListServerBackupsRecordRequest listServerBackupsRecordRequest);

    int insert(ServereBackupsDataRecord servereBackupsDataRecord);

    int delete(@Param("orgId") Long l, @Param("backupsDataId") Long l2);

    ServereBackupsDataRecord getOneById(@Param("orgId") Long l, @Param("backupsDataId") Long l2);

    List<String> listRecordName(@Param("queryTime") String str);

    List<String> listExpireZipName(@Param("orgId") Long l, @Param("count") Integer num);

    Integer deleteExpireBackupDataRecordByNames(@Param("orgId") Long l, @Param("recordNames") List<String> list);
}
