package tk.mybatis.mapper.common.example;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;
import tk.mybatis.mapper.provider.ExampleProvider;

/* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/common/example/UpdateByExampleSelectiveMapper.class */
public interface UpdateByExampleSelectiveMapper<T> {
    @UpdateProvider(type = ExampleProvider.class, method = "dynamicSQL")
    int updateByExampleSelective(@Param("record") T t, @Param("example") Object obj);
}
