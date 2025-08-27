package tk.mybatis.mapper.common.sqlserver;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import tk.mybatis.mapper.provider.SqlServerProvider;

/* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/common/sqlserver/InsertMapper.class */
public interface InsertMapper<T> {
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @InsertProvider(type = SqlServerProvider.class, method = "dynamicSQL")
    int insert(T t);
}
