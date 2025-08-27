package tk.mybatis.mapper.provider;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;

/* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/provider/ConditionProvider.class */
public class ConditionProvider extends MapperTemplate {
    private ExampleProvider exampleProvider;

    public ConditionProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
        this.exampleProvider = new ExampleProvider(mapperClass, mapperHelper);
    }

    public SqlNode selectCountByCondition(MappedStatement ms) {
        return this.exampleProvider.selectCountByExample(ms);
    }

    public SqlNode deleteByCondition(MappedStatement ms) {
        return this.exampleProvider.deleteByExample(ms);
    }

    public SqlNode selectByCondition(MappedStatement ms) {
        return this.exampleProvider.selectByExample(ms);
    }

    public SqlNode selectByConditionAndRowBounds(MappedStatement ms) {
        return this.exampleProvider.selectByExample(ms);
    }

    public SqlNode updateByConditionSelective(MappedStatement ms) {
        return this.exampleProvider.updateByExampleSelective(ms);
    }

    public SqlNode updateByCondition(MappedStatement ms) {
        return this.exampleProvider.updateByExample(ms);
    }
}
