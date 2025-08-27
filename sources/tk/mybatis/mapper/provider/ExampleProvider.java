package tk.mybatis.mapper.provider;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.scripting.xmltags.IfSqlNode;
import org.apache.ibatis.scripting.xmltags.MixedSqlNode;
import org.apache.ibatis.scripting.xmltags.SetSqlNode;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.scripting.xmltags.StaticTextSqlNode;
import org.apache.ibatis.scripting.xmltags.TextSqlNode;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;

/* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/provider/ExampleProvider.class */
public class ExampleProvider extends MapperTemplate {
    public ExampleProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public SqlNode selectCountByExample(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        List<SqlNode> sqlNodes = new LinkedList<>();
        sqlNodes.add(new StaticTextSqlNode("SELECT COUNT(*) FROM " + tableName(entityClass)));
        IfSqlNode ifNullSqlNode = new IfSqlNode(exampleWhereClause(ms.getConfiguration()), "_parameter != null");
        sqlNodes.add(ifNullSqlNode);
        return new MixedSqlNode(sqlNodes);
    }

    public SqlNode deleteByExample(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        List<SqlNode> sqlNodes = new LinkedList<>();
        sqlNodes.add(new StaticTextSqlNode("DELETE FROM " + tableName(entityClass)));
        IfSqlNode ifNullSqlNode = new IfSqlNode(exampleWhereClause(ms.getConfiguration()), "_parameter != null");
        sqlNodes.add(ifNullSqlNode);
        return new MixedSqlNode(sqlNodes);
    }

    public SqlNode selectByExample(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        setResultType(ms, entityClass);
        List<SqlNode> sqlNodes = new LinkedList<>();
        sqlNodes.add(new StaticTextSqlNode("SELECT"));
        IfSqlNode distinctSqlNode = new IfSqlNode(new StaticTextSqlNode("DISTINCT"), "distinct");
        sqlNodes.add(distinctSqlNode);
        ForEachSqlNode forEachSelectColumns = new ForEachSqlNode(ms.getConfiguration(), new TextSqlNode("${selectColumn}"), "_parameter.selectColumns", null, "selectColumn", null, null, ",");
        IfSqlNode ifSelectColumns = new IfSqlNode(forEachSelectColumns, "@tk.mybatis.mapper.util.OGNL@hasSelectColumns(_parameter)");
        sqlNodes.add(ifSelectColumns);
        IfSqlNode ifNoSelectColumns = new IfSqlNode(new StaticTextSqlNode(EntityHelper.getSelectColumns(entityClass)), "@tk.mybatis.mapper.util.OGNL@hasNoSelectColumns(_parameter)");
        sqlNodes.add(ifNoSelectColumns);
        sqlNodes.add(new StaticTextSqlNode(" FROM " + tableName(entityClass)));
        IfSqlNode ifNullSqlNode = new IfSqlNode(exampleWhereClause(ms.getConfiguration()), "_parameter != null");
        sqlNodes.add(ifNullSqlNode);
        IfSqlNode orderByClauseSqlNode = new IfSqlNode(new TextSqlNode("order by ${orderByClause}"), "orderByClause != null");
        sqlNodes.add(orderByClauseSqlNode);
        String orderByClause = EntityHelper.getOrderByClause(entityClass);
        if (orderByClause.length() > 0) {
            IfSqlNode defaultOrderByClauseSqlNode = new IfSqlNode(new StaticTextSqlNode("ORDER BY " + orderByClause), "orderByClause == null");
            sqlNodes.add(defaultOrderByClauseSqlNode);
        }
        return new MixedSqlNode(sqlNodes);
    }

    public SqlNode selectByExampleAndRowBounds(MappedStatement ms) {
        return selectByExample(ms);
    }

    public SqlNode updateByExampleSelective(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        List<SqlNode> sqlNodes = new LinkedList<>();
        sqlNodes.add(new StaticTextSqlNode("UPDATE " + tableName(entityClass)));
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        List<SqlNode> ifNodes = new LinkedList<>();
        for (EntityColumn column : columnList) {
            if (!column.isId()) {
                StaticTextSqlNode columnNode = new StaticTextSqlNode(column.getColumn() + " = #{record." + column.getProperty() + "}, ");
                ifNodes.add(new IfSqlNode(columnNode, "record." + column.getProperty() + " != null"));
            }
        }
        sqlNodes.add(new SetSqlNode(ms.getConfiguration(), new MixedSqlNode(ifNodes)));
        IfSqlNode ifNullSqlNode = new IfSqlNode(updateByExampleWhereClause(ms.getConfiguration()), "_parameter != null");
        sqlNodes.add(ifNullSqlNode);
        return new MixedSqlNode(sqlNodes);
    }

    public SqlNode updateByExample(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        List<SqlNode> sqlNodes = new LinkedList<>();
        sqlNodes.add(new StaticTextSqlNode("UPDATE " + tableName(entityClass)));
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        List<SqlNode> setSqlNodes = new LinkedList<>();
        for (EntityColumn column : columnList) {
            if (!column.isId()) {
                setSqlNodes.add(new StaticTextSqlNode(column.getColumn() + " = #{record." + column.getProperty() + "}, "));
            }
        }
        sqlNodes.add(new SetSqlNode(ms.getConfiguration(), new MixedSqlNode(setSqlNodes)));
        IfSqlNode ifNullSqlNode = new IfSqlNode(updateByExampleWhereClause(ms.getConfiguration()), "_parameter != null");
        sqlNodes.add(ifNullSqlNode);
        return new MixedSqlNode(sqlNodes);
    }
}
