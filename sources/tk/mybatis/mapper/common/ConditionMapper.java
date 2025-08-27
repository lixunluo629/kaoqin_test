package tk.mybatis.mapper.common;

import tk.mybatis.mapper.common.condition.DeleteByConditionMapper;
import tk.mybatis.mapper.common.condition.SelectByConditionMapper;
import tk.mybatis.mapper.common.condition.SelectCountByConditionMapper;
import tk.mybatis.mapper.common.condition.UpdateByConditionMapper;
import tk.mybatis.mapper.common.condition.UpdateByConditionSelectiveMapper;

/* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/common/ConditionMapper.class */
public interface ConditionMapper<T> extends SelectByConditionMapper<T>, SelectCountByConditionMapper<T>, DeleteByConditionMapper<T>, UpdateByConditionMapper<T>, UpdateByConditionSelectiveMapper<T> {
}
