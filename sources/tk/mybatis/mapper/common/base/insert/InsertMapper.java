package tk.mybatis.mapper.common.base.insert;

import org.apache.ibatis.annotations.InsertProvider;
import tk.mybatis.mapper.provider.base.BaseInsertProvider;

/* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/common/base/insert/InsertMapper.class */
public interface InsertMapper<T> {
    @InsertProvider(type = BaseInsertProvider.class, method = "dynamicSQL")
    int insert(T t);
}
