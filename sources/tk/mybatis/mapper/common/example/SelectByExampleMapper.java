package tk.mybatis.mapper.common.example;

import java.util.List;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.provider.ExampleProvider;

/* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/common/example/SelectByExampleMapper.class */
public interface SelectByExampleMapper<T> {
    @SelectProvider(type = ExampleProvider.class, method = "dynamicSQL")
    List<T> selectByExample(Object obj);
}
