package tk.mybatis.mapper.common.rowbounds;

import java.util.List;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.provider.ConditionProvider;

/* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/common/rowbounds/SelectByConditionRowBoundsMapper.class */
public interface SelectByConditionRowBoundsMapper<T> {
    @SelectProvider(type = ConditionProvider.class, method = "dynamicSQL")
    List<T> selectByConditionAndRowBounds(Object obj, RowBounds rowBounds);
}
