package tk.mybatis.mapper.provider;

import org.apache.ibatis.mapping.MappedStatement;
import org.springframework.context.expression.StandardBeanExpressionResolver;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;

/* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/provider/SpecialProvider.class */
public class SpecialProvider extends MapperTemplate {
    public SpecialProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String insertList(MappedStatement ms) {
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
        sql.append(") values ");
        sql.append("<foreach collection=\"list\" item=\"record\" separator=\",\" >");
        sql.append("(");
        boolean first2 = true;
        for (EntityColumn column2 : table.getEntityClassColumns()) {
            if (!column2.isId()) {
                if (!first2) {
                    sql.append(",");
                }
                sql.append("#{record.").append(column2.getProperty()).append("}");
                first2 = false;
            }
        }
        sql.append(")");
        sql.append("</foreach>");
        return sql.toString();
    }

    public String insertUseGeneratedKeys(MappedStatement ms) {
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
}
