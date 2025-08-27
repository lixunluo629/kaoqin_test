package tk.mybatis.mapper.common.base.delete;

import org.apache.ibatis.annotations.DeleteProvider;
import tk.mybatis.mapper.provider.base.BaseDeleteProvider;

/* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/common/base/delete/DeleteMapper.class */
public interface DeleteMapper<T> {
    @DeleteProvider(type = BaseDeleteProvider.class, method = "dynamicSQL")
    int delete(T t);
}
