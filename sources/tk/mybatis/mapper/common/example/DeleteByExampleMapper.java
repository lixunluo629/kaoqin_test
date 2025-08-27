package tk.mybatis.mapper.common.example;

import org.apache.ibatis.annotations.DeleteProvider;
import tk.mybatis.mapper.provider.ExampleProvider;

/* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/common/example/DeleteByExampleMapper.class */
public interface DeleteByExampleMapper<T> {
    @DeleteProvider(type = ExampleProvider.class, method = "dynamicSQL")
    int deleteByExample(Object obj);
}
