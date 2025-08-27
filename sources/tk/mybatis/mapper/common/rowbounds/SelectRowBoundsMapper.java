package tk.mybatis.mapper.common.rowbounds;

import java.util.List;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.provider.base.BaseSelectProvider;

/* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/common/rowbounds/SelectRowBoundsMapper.class */
public interface SelectRowBoundsMapper<T> {
    @SelectProvider(type = BaseSelectProvider.class, method = "dynamicSQL")
    List<T> selectByRowBounds(T t, RowBounds rowBounds);
}
