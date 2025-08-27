package tk.mybatis.mapper.common.base.select;

import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.provider.base.BaseSelectProvider;

/* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/common/base/select/SelectCountMapper.class */
public interface SelectCountMapper<T> {
    @SelectProvider(type = BaseSelectProvider.class, method = "dynamicSQL")
    int selectCount(T t);
}
