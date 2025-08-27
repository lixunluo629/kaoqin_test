package tk.mybatis.mapper.provider.base;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.scripting.xmltags.IfSqlNode;
import org.apache.ibatis.scripting.xmltags.MixedSqlNode;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.scripting.xmltags.StaticTextSqlNode;
import org.apache.ibatis.scripting.xmltags.TrimSqlNode;
import org.apache.ibatis.scripting.xmltags.VarDeclSqlNode;
import org.springframework.context.expression.StandardBeanExpressionResolver;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.util.StringUtil;

/* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/provider/base/BaseInsertProvider.class */
public class BaseInsertProvider extends MapperTemplate {
    public BaseInsertProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public SqlNode insert(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        List<SqlNode> sqlNodes = new LinkedList<>();
        sqlNodes.add(new StaticTextSqlNode("INSERT INTO " + tableName(entityClass)));
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        Boolean hasIdentityKey = false;
        for (EntityColumn column : columnList) {
            if (!StringUtil.isNotEmpty(column.getSequenceName())) {
                if (column.isIdentity()) {
                    sqlNodes.add(new VarDeclSqlNode(column.getProperty() + "_cache", column.getProperty()));
                    if (hasIdentityKey.booleanValue()) {
                        if (column.getGenerator() == null || !column.getGenerator().equals("JDBC")) {
                            throw new RuntimeException(ms.getId() + "对应的实体类" + entityClass.getCanonicalName() + "中包含多个MySql的自动增长列,最多只能有一个!");
                        }
                    } else {
                        newSelectKeyMappedStatement(ms, column);
                        hasIdentityKey = true;
                    }
                } else if (column.isUuid()) {
                    sqlNodes.add(new VarDeclSqlNode(column.getProperty() + "_bind", getUUID()));
                }
            }
        }
        sqlNodes.add(new StaticTextSqlNode("(" + EntityHelper.getAllColumns(entityClass) + ")"));
        List<SqlNode> ifNodes = new LinkedList<>();
        for (EntityColumn column2 : columnList) {
            if (column2.isIdentity()) {
                ifNodes.add(getIfCacheNotNull(column2, new StaticTextSqlNode(StandardBeanExpressionResolver.DEFAULT_EXPRESSION_PREFIX + column2.getProperty() + "_cache },")));
            } else {
                ifNodes.add(getIfNotNull(column2, new StaticTextSqlNode(StandardBeanExpressionResolver.DEFAULT_EXPRESSION_PREFIX + column2.getProperty() + "},")));
            }
            if (StringUtil.isNotEmpty(column2.getSequenceName())) {
                ifNodes.add(getIfIsNull(column2, new StaticTextSqlNode(getSeqNextVal(column2) + " ,")));
            } else if (column2.isIdentity()) {
                ifNodes.add(getIfCacheIsNull(column2, new StaticTextSqlNode(StandardBeanExpressionResolver.DEFAULT_EXPRESSION_PREFIX + column2.getProperty() + " },")));
            } else if (column2.isUuid()) {
                ifNodes.add(getIfIsNull(column2, new StaticTextSqlNode(StandardBeanExpressionResolver.DEFAULT_EXPRESSION_PREFIX + column2.getProperty() + "_bind },")));
            } else {
                ifNodes.add(getIfIsNull(column2, new StaticTextSqlNode(StandardBeanExpressionResolver.DEFAULT_EXPRESSION_PREFIX + column2.getProperty() + ",jdbcType=VARCHAR},")));
            }
        }
        sqlNodes.add(new TrimSqlNode(ms.getConfiguration(), new MixedSqlNode(ifNodes), "VALUES (", (String) null, ")", ","));
        return new MixedSqlNode(sqlNodes);
    }

    public SqlNode insertSelective(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        List<SqlNode> sqlNodes = new LinkedList<>();
        sqlNodes.add(new StaticTextSqlNode("INSERT INTO " + tableName(entityClass)));
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        List<SqlNode> ifNodes = new LinkedList<>();
        Boolean hasIdentityKey = false;
        for (EntityColumn column : columnList) {
            if (StringUtil.isNotEmpty(column.getSequenceName())) {
                ifNodes.add(new StaticTextSqlNode(column.getColumn() + ","));
            } else if (column.isIdentity()) {
                sqlNodes.add(new VarDeclSqlNode(column.getProperty() + "_cache", column.getProperty()));
                if (hasIdentityKey.booleanValue()) {
                    if (column.getGenerator() == null || !column.getGenerator().equals("JDBC")) {
                        throw new RuntimeException(ms.getId() + "对应的实体类" + entityClass.getCanonicalName() + "中包含多个MySql的自动增长列,最多只能有一个!");
                    }
                } else {
                    newSelectKeyMappedStatement(ms, column);
                    hasIdentityKey = true;
                    ifNodes.add(new StaticTextSqlNode(column.getColumn() + ","));
                }
            } else if (column.isUuid()) {
                sqlNodes.add(new VarDeclSqlNode(column.getProperty() + "_bind", getUUID()));
                ifNodes.add(new StaticTextSqlNode(column.getColumn() + ","));
            } else {
                ifNodes.add(getIfNotNull(column, new StaticTextSqlNode(column.getColumn() + ",")));
            }
        }
        sqlNodes.add(new TrimSqlNode(ms.getConfiguration(), new MixedSqlNode(ifNodes), "(", (String) null, ")", ","));
        List<SqlNode> ifNodes2 = new LinkedList<>();
        for (EntityColumn column2 : columnList) {
            if (column2.isIdentity()) {
                ifNodes2.add(new IfSqlNode(new StaticTextSqlNode(StandardBeanExpressionResolver.DEFAULT_EXPRESSION_PREFIX + column2.getProperty() + "_cache },"), column2.getProperty() + "_cache != null "));
            } else {
                ifNodes2.add(new IfSqlNode(new StaticTextSqlNode(StandardBeanExpressionResolver.DEFAULT_EXPRESSION_PREFIX + column2.getProperty() + "},"), column2.getProperty() + " != null "));
            }
            if (StringUtil.isNotEmpty(column2.getSequenceName())) {
                ifNodes2.add(getIfIsNull(column2, new StaticTextSqlNode(getSeqNextVal(column2) + " ,")));
            } else if (column2.isIdentity()) {
                ifNodes2.add(getIfCacheIsNull(column2, new StaticTextSqlNode(StandardBeanExpressionResolver.DEFAULT_EXPRESSION_PREFIX + column2.getProperty() + " },")));
            } else if (column2.isUuid()) {
                ifNodes2.add(getIfIsNull(column2, new StaticTextSqlNode(StandardBeanExpressionResolver.DEFAULT_EXPRESSION_PREFIX + column2.getProperty() + "_bind },")));
            }
        }
        sqlNodes.add(new TrimSqlNode(ms.getConfiguration(), new MixedSqlNode(ifNodes2), "VALUES (", (String) null, ")", ","));
        return new MixedSqlNode(sqlNodes);
    }
}
