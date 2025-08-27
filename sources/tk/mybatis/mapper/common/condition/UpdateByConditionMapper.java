package tk.mybatis.mapper.common.condition;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;
import tk.mybatis.mapper.provider.ConditionProvider;

/* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/common/condition/UpdateByConditionMapper.class */
public interface UpdateByConditionMapper<T> {
    @UpdateProvider(type = ConditionProvider.class, method = "dynamicSQL")
    int updateByCondition(@Param("record") T t, @Param("example") Object obj);
}
