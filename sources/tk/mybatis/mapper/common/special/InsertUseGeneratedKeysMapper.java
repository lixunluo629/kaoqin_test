package tk.mybatis.mapper.common.special;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import tk.mybatis.mapper.provider.SpecialProvider;

/* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/common/special/InsertUseGeneratedKeysMapper.class */
public interface InsertUseGeneratedKeysMapper<T> {
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @InsertProvider(type = SpecialProvider.class, method = "dynamicSQL")
    int insertUseGeneratedKeys(T t);
}
