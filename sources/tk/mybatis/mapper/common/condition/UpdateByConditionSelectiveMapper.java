package tk.mybatis.mapper.common.condition;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;
import tk.mybatis.mapper.provider.ConditionProvider;

/* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/common/condition/UpdateByConditionSelectiveMapper.class */
public interface UpdateByConditionSelectiveMapper<T> {
    @UpdateProvider(type = ConditionProvider.class, method = "dynamicSQL")
    int updateByConditionSelective(@Param("record") T t, @Param("example") Object obj);
}
