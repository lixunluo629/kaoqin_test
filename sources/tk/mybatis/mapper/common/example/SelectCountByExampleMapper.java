package tk.mybatis.mapper.common.example;

import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.provider.ExampleProvider;

/* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/common/example/SelectCountByExampleMapper.class */
public interface SelectCountByExampleMapper<T> {
    @SelectProvider(type = ExampleProvider.class, method = "dynamicSQL")
    int selectCountByExample(Object obj);
}
