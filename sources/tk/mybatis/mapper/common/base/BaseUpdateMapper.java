package tk.mybatis.mapper.common.base;

import tk.mybatis.mapper.common.base.update.UpdateByPrimaryKeyMapper;
import tk.mybatis.mapper.common.base.update.UpdateByPrimaryKeySelectiveMapper;

/* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/common/base/BaseUpdateMapper.class */
public interface BaseUpdateMapper<T> extends UpdateByPrimaryKeyMapper<T>, UpdateByPrimaryKeySelectiveMapper<T> {
}
