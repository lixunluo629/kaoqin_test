package tk.mybatis.mapper.common.condition;

import org.apache.ibatis.annotations.DeleteProvider;
import tk.mybatis.mapper.provider.ConditionProvider;

/* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/common/condition/DeleteByConditionMapper.class */
public interface DeleteByConditionMapper<T> {
    @DeleteProvider(type = ConditionProvider.class, method = "dynamicSQL")
    int deleteByCondition(Object obj);
}
