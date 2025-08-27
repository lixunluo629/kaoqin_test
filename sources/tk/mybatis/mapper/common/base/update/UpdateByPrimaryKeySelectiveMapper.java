package tk.mybatis.mapper.common.base.update;

import org.apache.ibatis.annotations.UpdateProvider;
import tk.mybatis.mapper.provider.base.BaseUpdateProvider;

/* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/common/base/update/UpdateByPrimaryKeySelectiveMapper.class */
public interface UpdateByPrimaryKeySelectiveMapper<T> {
    @UpdateProvider(type = BaseUpdateProvider.class, method = "dynamicSQL")
    int updateByPrimaryKeySelective(T t);
}
