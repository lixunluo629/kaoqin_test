package tk.mybatis.mapper.common.rowbounds;

import java.util.List;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.provider.ExampleProvider;

/* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/common/rowbounds/SelectByExampleRowBoundsMapper.class */
public interface SelectByExampleRowBoundsMapper<T> {
    @SelectProvider(type = ExampleProvider.class, method = "dynamicSQL")
    List<T> selectByExampleAndRowBounds(Object obj, RowBounds rowBounds);
}
