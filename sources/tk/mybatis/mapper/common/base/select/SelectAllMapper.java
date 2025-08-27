package tk.mybatis.mapper.common.base.select;

import java.util.List;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.provider.base.BaseSelectProvider;

/* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/common/base/select/SelectAllMapper.class */
public interface SelectAllMapper<T> {
    @SelectProvider(type = BaseSelectProvider.class, method = "dynamicSQL")
    List<T> selectAll();
}
