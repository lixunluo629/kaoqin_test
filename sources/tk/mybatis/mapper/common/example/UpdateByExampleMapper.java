package tk.mybatis.mapper.common.example;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;
import tk.mybatis.mapper.provider.ExampleProvider;

/* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/common/example/UpdateByExampleMapper.class */
public interface UpdateByExampleMapper<T> {
    @UpdateProvider(type = ExampleProvider.class, method = "dynamicSQL")
    int updateByExample(@Param("record") T t, @Param("example") Object obj);
}
