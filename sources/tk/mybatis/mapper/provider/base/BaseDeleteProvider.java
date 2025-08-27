package tk.mybatis.mapper.provider.base;

import java.util.LinkedList;
import java.util.List;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.scripting.xmltags.MixedSqlNode;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.scripting.xmltags.StaticTextSqlNode;
import org.apache.ibatis.scripting.xmltags.WhereSqlNode;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;

/* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/provider/base/BaseDeleteProvider.class */
public class BaseDeleteProvider extends MapperTemplate {
    public BaseDeleteProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public SqlNode delete(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        List<SqlNode> sqlNodes = new LinkedList<>();
        sqlNodes.add(new StaticTextSqlNode("DELETE FROM " + tableName(entityClass)));
        sqlNodes.add(new WhereSqlNode(ms.getConfiguration(), getAllIfColumnNode(entityClass)));
        return new MixedSqlNode(sqlNodes);
    }

    public void deleteByPrimaryKey(MappedStatement ms) {
        final Class<?> entityClass = getSelectReturnType(ms);
        List<ParameterMapping> parameterMappings = getPrimaryKeyParameterMappings(ms);
        String sql = new SQL() { // from class: tk.mybatis.mapper.provider.base.BaseDeleteProvider.1
            {
                DELETE_FROM(BaseDeleteProvider.this.tableName(entityClass));
                WHERE(EntityHelper.getPrimaryKeyWhere(entityClass));
            }
        }.toString();
        StaticSqlSource sqlSource = new StaticSqlSource(ms.getConfiguration(), sql, parameterMappings);
        setSqlSource(ms, sqlSource);
    }
}
