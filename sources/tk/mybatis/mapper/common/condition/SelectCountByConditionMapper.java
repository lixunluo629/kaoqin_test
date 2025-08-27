package tk.mybatis.mapper.common.condition;

import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.provider.ConditionProvider;

/* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/common/condition/SelectCountByConditionMapper.class */
public interface SelectCountByConditionMapper<T> {
    @SelectProvider(type = ConditionProvider.class, method = "dynamicSQL")
    int selectCountByCondition(Object obj);
}
