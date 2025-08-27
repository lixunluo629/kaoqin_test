package tk.mybatis.mapper.provider;

import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;

/* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/provider/EmptyProvider.class */
public class EmptyProvider extends MapperTemplate {
    public EmptyProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    @Override // tk.mybatis.mapper.mapperhelper.MapperTemplate
    public boolean supportMethod(String msId) {
        return false;
    }
}
