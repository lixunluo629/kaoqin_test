package tk.mybatis.mapper.provider;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.scripting.xmltags.IfSqlNode;
import org.apache.ibatis.scripting.xmltags.MixedSqlNode;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.scripting.xmltags.StaticTextSqlNode;
import org.apache.ibatis.scripting.xmltags.TrimSqlNode;
import org.springframework.context.expression.StandardBeanExpressionResolver;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;

/* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/provider/SqlServerProvider.class */
public class SqlServerProvider extends MapperTemplate {
    public SqlServerProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String insert(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        EntityTable table = EntityHelper.getEntityTable(entityClass);
        StringBuilder sql = new StringBuilder();
        sql.append("insert into ");
        sql.append(table.getName());
        sql.append("(");
        boolean first = true;
        for (EntityColumn column : table.getEntityClassColumns()) {
            if (!column.isId()) {
                if (!first) {
                    sql.append(",");
                }
                sql.append(column.getColumn());
                first = false;
            }
        }
        sql.append(") values(");
        boolean first2 = true;
        for (EntityColumn column2 : table.getEntityClassColumns()) {
            if (!column2.isId()) {
                if (!first2) {
                    sql.append(",");
                }
                sql.append(StandardBeanExpressionResolver.DEFAULT_EXPRESSION_PREFIX).append(column2.getProperty()).append("}");
                first2 = false;
            }
        }
        sql.append(")");
        return sql.toString();
    }

    public SqlNode insertSelective(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        List<SqlNode> sqlNodes = new LinkedList<>();
        sqlNodes.add(new StaticTextSqlNode("INSERT INTO " + tableName(entityClass)));
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        List<SqlNode> ifNodes = new LinkedList<>();
        for (EntityColumn column : columnList) {
            if (!column.isId()) {
                ifNodes.add(getIfNotNull(column, new StaticTextSqlNode(column.getColumn() + ",")));
            }
        }
        sqlNodes.add(new TrimSqlNode(ms.getConfiguration(), new MixedSqlNode(ifNodes), "(", (String) null, ")", ","));
        List<SqlNode> ifNodes2 = new LinkedList<>();
        for (EntityColumn column2 : columnList) {
            if (!column2.isId()) {
                ifNodes2.add(new IfSqlNode(new StaticTextSqlNode(StandardBeanExpressionResolver.DEFAULT_EXPRESSION_PREFIX + column2.getProperty() + "},"), column2.getProperty() + " != null "));
            }
        }
        sqlNodes.add(new TrimSqlNode(ms.getConfiguration(), new MixedSqlNode(ifNodes2), "VALUES (", (String) null, ")", ","));
        return new MixedSqlNode(sqlNodes);
    }
}
