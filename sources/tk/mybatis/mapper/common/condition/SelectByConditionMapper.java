package tk.mybatis.mapper.common.condition;

import java.util.List;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.provider.ConditionProvider;

/* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/common/condition/SelectByConditionMapper.class */
public interface SelectByConditionMapper<T> {
    @SelectProvider(type = ConditionProvider.class, method = "dynamicSQL")
    List<T> selectByCondition(Object obj);
}
