package tk.mybatis.mapper.mapperhelper;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.IdentityDialect;
import tk.mybatis.mapper.code.Style;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.util.StringUtil;

/* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/mapperhelper/EntityHelper.class */
public class EntityHelper {
    private static final Map<Class<?>, EntityTable> entityTableMap = new HashMap();

    public static EntityTable getEntityTable(Class<?> entityClass) {
        EntityTable entityTable = entityTableMap.get(entityClass);
        if (entityTable == null) {
            throw new RuntimeException("无法获取实体类" + entityClass.getCanonicalName() + "对应的表名!");
        }
        return entityTable;
    }

    public static String getOrderByClause(Class<?> entityClass) {
        EntityTable table = getEntityTable(entityClass);
        if (table.getOrderByClause() != null) {
            return table.getOrderByClause();
        }
        StringBuilder orderBy = new StringBuilder();
        for (EntityColumn column : table.getEntityClassColumns()) {
            if (column.getOrderBy() != null) {
                if (orderBy.length() != 0) {
                    orderBy.append(",");
                }
                orderBy.append(column.getColumn()).append(SymbolConstants.SPACE_SYMBOL).append(column.getOrderBy());
            }
        }
        table.setOrderByClause(orderBy.toString());
        return table.getOrderByClause();
    }

    public static Set<EntityColumn> getColumns(Class<?> entityClass) {
        return getEntityTable(entityClass).getEntityClassColumns();
    }

    public static Set<EntityColumn> getPKColumns(Class<?> entityClass) {
        return getEntityTable(entityClass).getEntityClassPKColumns();
    }

    public static String getSelectColumns(Class<?> entityClass) {
        EntityTable entityTable = getEntityTable(entityClass);
        if (entityTable.getBaseSelect() != null) {
            return entityTable.getBaseSelect();
        }
        Set<EntityColumn> columnList = getColumns(entityClass);
        StringBuilder selectBuilder = new StringBuilder();
        boolean skipAlias = Map.class.isAssignableFrom(entityClass);
        for (EntityColumn entityColumn : columnList) {
            selectBuilder.append(entityColumn.getColumn());
            if (!skipAlias && !entityColumn.getColumn().equalsIgnoreCase(entityColumn.getProperty())) {
                if (entityColumn.getColumn().substring(1, entityColumn.getColumn().length() - 1).equalsIgnoreCase(entityColumn.getProperty())) {
                    selectBuilder.append(",");
                } else {
                    selectBuilder.append(" AS ").append(entityColumn.getProperty()).append(",");
                }
            } else {
                selectBuilder.append(",");
            }
        }
        entityTable.setBaseSelect(selectBuilder.substring(0, selectBuilder.length() - 1));
        return entityTable.getBaseSelect();
    }

    public static String getAllColumns(Class<?> entityClass) {
        Set<EntityColumn> columnList = getColumns(entityClass);
        StringBuilder selectBuilder = new StringBuilder();
        for (EntityColumn entityColumn : columnList) {
            selectBuilder.append(entityColumn.getColumn()).append(",");
        }
        return selectBuilder.substring(0, selectBuilder.length() - 1);
    }

    public static String getPrimaryKeyWhere(Class<?> entityClass) {
        Set<EntityColumn> entityColumns = getPKColumns(entityClass);
        StringBuilder whereBuilder = new StringBuilder();
        for (EntityColumn column : entityColumns) {
            whereBuilder.append(column.getColumn()).append(" = ?").append(" AND ");
        }
        return whereBuilder.substring(0, whereBuilder.length() - 4);
    }

    public static synchronized void initEntityNameMap(Class<?> entityClass, Style style) {
        String generator;
        if (entityTableMap.get(entityClass) != null) {
            return;
        }
        if (entityClass.isAnnotationPresent(NameStyle.class)) {
            NameStyle nameStyle = (NameStyle) entityClass.getAnnotation(NameStyle.class);
            style = nameStyle.value();
        }
        EntityTable entityTable = null;
        if (entityClass.isAnnotationPresent(Table.class)) {
            Table table = (Table) entityClass.getAnnotation(Table.class);
            if (!table.name().equals("")) {
                entityTable = new EntityTable();
                entityTable.setTable(table);
            }
        }
        if (entityTable == null) {
            entityTable = new EntityTable();
            entityTable.setName(StringUtil.convertByStyle(entityClass.getSimpleName(), style));
        }
        List<Field> fieldList = getAllField(entityClass, null);
        Set<EntityColumn> columnSet = new LinkedHashSet<>();
        Set<EntityColumn> pkColumnSet = new LinkedHashSet<>();
        for (Field field : fieldList) {
            if (!field.isAnnotationPresent(Transient.class)) {
                EntityColumn entityColumn = new EntityColumn(entityTable);
                if (field.isAnnotationPresent(Id.class)) {
                    entityColumn.setId(true);
                }
                String columnName = null;
                if (field.isAnnotationPresent(Column.class)) {
                    Column column = (Column) field.getAnnotation(Column.class);
                    columnName = column.name();
                }
                if (columnName == null || columnName.equals("")) {
                    columnName = StringUtil.convertByStyle(field.getName(), style);
                }
                entityColumn.setProperty(field.getName());
                entityColumn.setColumn(columnName);
                entityColumn.setJavaType(field.getType());
                if (field.isAnnotationPresent(OrderBy.class)) {
                    OrderBy orderBy = (OrderBy) field.getAnnotation(OrderBy.class);
                    if (orderBy.value().equals("")) {
                        entityColumn.setOrderBy("ASC");
                    } else {
                        entityColumn.setOrderBy(orderBy.value());
                    }
                }
                if (field.isAnnotationPresent(SequenceGenerator.class)) {
                    SequenceGenerator sequenceGenerator = (SequenceGenerator) field.getAnnotation(SequenceGenerator.class);
                    if (sequenceGenerator.sequenceName().equals("")) {
                        throw new RuntimeException(entityClass + "字段" + field.getName() + "的注解@SequenceGenerator未指定sequenceName!");
                    }
                    entityColumn.setSequenceName(sequenceGenerator.sequenceName());
                } else if (field.isAnnotationPresent(GeneratedValue.class)) {
                    GeneratedValue generatedValue = (GeneratedValue) field.getAnnotation(GeneratedValue.class);
                    if (generatedValue.generator().equals("UUID")) {
                        entityColumn.setUuid(true);
                    } else if (generatedValue.generator().equals("JDBC")) {
                        entityColumn.setIdentity(true);
                        entityColumn.setGenerator("JDBC");
                        entityTable.setKeyProperties(entityColumn.getProperty());
                        entityTable.setKeyColumns(entityColumn.getColumn());
                    } else if (generatedValue.strategy() == GenerationType.IDENTITY) {
                        entityColumn.setIdentity(true);
                        if (!generatedValue.generator().equals("")) {
                            IdentityDialect identityDialect = IdentityDialect.getDatabaseDialect(generatedValue.generator());
                            if (identityDialect != null) {
                                generator = identityDialect.getIdentityRetrievalStatement();
                            } else {
                                generator = generatedValue.generator();
                            }
                            entityColumn.setGenerator(generator);
                        }
                    } else {
                        throw new RuntimeException(field.getName() + " - 该字段@GeneratedValue配置只允许以下几种形式:\n1.全部数据库通用的@GeneratedValue(generator=\"UUID\")\n2.useGeneratedKeys的@GeneratedValue(generator=\\\"JDBC\\\")  \n3.类似mysql数据库的@GeneratedValue(strategy=GenerationType.IDENTITY[,generator=\"Mysql\"])");
                    }
                }
                columnSet.add(entityColumn);
                if (entityColumn.isId()) {
                    pkColumnSet.add(entityColumn);
                }
            }
        }
        entityTable.setEntityClassColumns(columnSet);
        if (pkColumnSet.size() == 0) {
            entityTable.setEntityClassPKColumns(columnSet);
        } else {
            entityTable.setEntityClassPKColumns(pkColumnSet);
        }
        entityTableMap.put(entityClass, entityTable);
    }

    private static List<Field> getAllField(Class<?> entityClass, List<Field> fieldList) {
        if (fieldList == null) {
            fieldList = new LinkedList();
        }
        if (entityClass.equals(Object.class)) {
            return fieldList;
        }
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                fieldList.add(field);
            }
        }
        Class<?> superClass = entityClass.getSuperclass();
        if (superClass != null && !superClass.equals(Object.class) && (superClass.isAnnotationPresent(Entity.class) || (!Map.class.isAssignableFrom(superClass) && !Collection.class.isAssignableFrom(superClass)))) {
            return getAllField(entityClass.getSuperclass(), fieldList);
        }
        return fieldList;
    }
}
