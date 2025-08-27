package tk.mybatis.mapper.mapperhelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
import org.apache.ibatis.scripting.xmltags.ChooseSqlNode;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.scripting.xmltags.IfSqlNode;
import org.apache.ibatis.scripting.xmltags.MixedSqlNode;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.scripting.xmltags.StaticTextSqlNode;
import org.apache.ibatis.scripting.xmltags.TextSqlNode;
import org.apache.ibatis.scripting.xmltags.TrimSqlNode;
import org.apache.ibatis.scripting.xmltags.WhereSqlNode;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.util.StringUtil;

/* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/mapperhelper/MapperTemplate.class */
public abstract class MapperTemplate {
    private XMLLanguageDriver languageDriver = new XMLLanguageDriver();
    private Map<String, Method> methodMap = new HashMap();
    private Map<String, Class<?>> entityClassMap = new HashMap();
    private Class<?> mapperClass;
    private MapperHelper mapperHelper;

    public MapperTemplate(Class<?> mapperClass, MapperHelper mapperHelper) {
        this.mapperClass = mapperClass;
        this.mapperHelper = mapperHelper;
    }

    public static Class<?> getMapperClass(String msId) {
        if (msId.indexOf(".") == -1) {
            throw new RuntimeException("当前MappedStatement的id=" + msId + ",不符合MappedStatement的规则!");
        }
        String mapperClassStr = msId.substring(0, msId.lastIndexOf("."));
        try {
            return Class.forName(mapperClassStr);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static String getMethodName(MappedStatement ms) {
        return getMethodName(ms.getId());
    }

    public static String getMethodName(String msId) {
        return msId.substring(msId.lastIndexOf(".") + 1);
    }

    public String dynamicSQL(Object record) {
        return "dynamicSQL";
    }

    public void addMethodMap(String methodName, Method method) {
        this.methodMap.put(methodName, method);
    }

    public String getUUID() {
        return this.mapperHelper.getConfig().getUUID();
    }

    public String getIDENTITY() {
        return this.mapperHelper.getConfig().getIDENTITY();
    }

    public boolean getBEFORE() {
        return this.mapperHelper.getConfig().getBEFORE();
    }

    public boolean isNotEmpty() {
        return this.mapperHelper.getConfig().isNotEmpty();
    }

    public boolean supportMethod(String msId) {
        Class<?> mapperClass = getMapperClass(msId);
        if (mapperClass != null && this.mapperClass.isAssignableFrom(mapperClass)) {
            String methodName = getMethodName(msId);
            return this.methodMap.get(methodName) != null;
        }
        return false;
    }

    protected void setResultType(MappedStatement ms, Class<?> entityClass) {
        ResultMap resultMap = ms.getResultMaps().get(0);
        MetaObject metaObject = SystemMetaObject.forObject(resultMap);
        metaObject.setValue("type", entityClass);
    }

    protected void setSqlSource(MappedStatement ms, SqlSource sqlSource) {
        MetaObject msObject = SystemMetaObject.forObject(ms);
        msObject.setValue("sqlSource", sqlSource);
        KeyGenerator keyGenerator = ms.getKeyGenerator();
        if (keyGenerator instanceof Jdbc3KeyGenerator) {
            msObject.setValue("keyGenerator", new MultipleJdbc3KeyGenerator());
        }
    }

    private void checkCache(MappedStatement ms) throws Exception {
        if (ms.getCache() == null) {
            String nameSpace = ms.getId().substring(0, ms.getId().lastIndexOf("."));
            try {
                Cache cache = ms.getConfiguration().getCache(nameSpace);
                if (cache != null) {
                    MetaObject metaObject = SystemMetaObject.forObject(ms);
                    metaObject.setValue("cache", cache);
                }
            } catch (IllegalArgumentException e) {
            }
        }
    }

    public void setSqlSource(MappedStatement ms) throws Exception {
        if (this.mapperClass == getMapperClass(ms.getId())) {
            throw new RuntimeException("请不要配置或扫描通用Mapper接口类：" + this.mapperClass);
        }
        Method method = this.methodMap.get(getMethodName(ms));
        try {
            if (method.getReturnType() == Void.TYPE) {
                method.invoke(this, ms);
            } else if (SqlNode.class.isAssignableFrom(method.getReturnType())) {
                SqlNode sqlNode = (SqlNode) method.invoke(this, ms);
                DynamicSqlSource dynamicSqlSource = new DynamicSqlSource(ms.getConfiguration(), sqlNode);
                setSqlSource(ms, dynamicSqlSource);
            } else if (String.class.equals(method.getReturnType())) {
                String xmlSql = (String) method.invoke(this, ms);
                SqlSource sqlSource = createSqlSource(ms, xmlSql);
                setSqlSource(ms, sqlSource);
            } else {
                throw new RuntimeException("自定义Mapper方法返回类型错误,可选的返回类型为void,SqlNode,String三种!");
            }
            checkCache(ms);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e2) {
            throw new RuntimeException(e2.getTargetException() != null ? e2.getTargetException() : e2);
        }
    }

    public SqlSource createSqlSource(MappedStatement ms, String xmlSql) {
        return this.languageDriver.createSqlSource(ms.getConfiguration(), "<script>\n\t" + xmlSql + "</script>", (Class<?>) null);
    }

    public Class<?> getSelectReturnType(MappedStatement ms) {
        String msId = ms.getId();
        if (this.entityClassMap.containsKey(msId)) {
            return this.entityClassMap.get(msId);
        }
        Class<?> mapperClass = getMapperClass(msId);
        Type[] types = mapperClass.getGenericInterfaces();
        for (Type type : types) {
            if (type instanceof ParameterizedType) {
                ParameterizedType t = (ParameterizedType) type;
                if (t.getRawType() == this.mapperClass || this.mapperClass.isAssignableFrom((Class) t.getRawType())) {
                    Class<?> returnType = (Class) t.getActualTypeArguments()[0];
                    EntityHelper.initEntityNameMap(returnType, this.mapperHelper.getConfig().getStyle());
                    this.entityClassMap.put(msId, returnType);
                    return returnType;
                }
            }
        }
        throw new RuntimeException("无法获取Mapper<T>泛型类型:" + msId);
    }

    protected List<ParameterMapping> getPrimaryKeyParameterMappings(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        Set<EntityColumn> entityColumns = EntityHelper.getPKColumns(entityClass);
        List<ParameterMapping> parameterMappings = new LinkedList<>();
        for (EntityColumn column : entityColumns) {
            ParameterMapping.Builder builder = new ParameterMapping.Builder(ms.getConfiguration(), column.getProperty(), column.getJavaType());
            builder.mode(ParameterMode.IN);
            parameterMappings.add(builder.build());
        }
        return parameterMappings;
    }

    protected String getSeqNextVal(EntityColumn column) {
        return MessageFormat.format(this.mapperHelper.getConfig().getSeqFormat(), column.getSequenceName(), column.getColumn(), column.getProperty());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String tableName(Class<?> entityClass) {
        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        String prefix = entityTable.getPrefix();
        if (StringUtil.isEmpty(prefix)) {
            prefix = this.mapperHelper.getConfig().getPrefix();
        }
        if (StringUtil.isNotEmpty(prefix)) {
            return prefix + "." + entityTable.getName();
        }
        return entityTable.getName();
    }

    protected SqlNode getIfNotNull(EntityColumn column, SqlNode columnNode) {
        return getIfNotNull(column, columnNode, false);
    }

    protected SqlNode getIfNotNull(EntityColumn column, SqlNode columnNode, boolean empty) {
        if (empty && column.getJavaType().equals(String.class)) {
            return new IfSqlNode(columnNode, column.getProperty() + " != null and " + column.getProperty() + " != ''");
        }
        return new IfSqlNode(columnNode, column.getProperty() + " != null ");
    }

    protected SqlNode getIfIsNull(EntityColumn column, SqlNode columnNode) {
        return new IfSqlNode(columnNode, column.getProperty() + " == null ");
    }

    protected SqlNode getIfCacheNotNull(EntityColumn column, SqlNode columnNode) {
        return new IfSqlNode(columnNode, column.getProperty() + "_cache != null ");
    }

    protected SqlNode getIfCacheIsNull(EntityColumn column, SqlNode columnNode) {
        return new IfSqlNode(columnNode, column.getProperty() + "_cache == null ");
    }

    protected SqlNode getColumnEqualsProperty(EntityColumn column, boolean first) {
        return new StaticTextSqlNode((first ? "" : " AND ") + column.getColumn() + " = #{" + column.getProperty() + "} ");
    }

    protected SqlNode getAllIfColumnNode(Class<?> entityClass) {
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        List<SqlNode> ifNodes = new LinkedList<>();
        boolean first = true;
        for (EntityColumn column : columnList) {
            ifNodes.add(getIfNotNull(column, getColumnEqualsProperty(column, first), isNotEmpty()));
            first = false;
        }
        return new MixedSqlNode(ifNodes);
    }

    protected List<ParameterMapping> getColumnParameterMappings(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        Set<EntityColumn> entityColumns = EntityHelper.getColumns(entityClass);
        List<ParameterMapping> parameterMappings = new LinkedList<>();
        for (EntityColumn column : entityColumns) {
            ParameterMapping.Builder builder = new ParameterMapping.Builder(ms.getConfiguration(), column.getProperty(), column.getJavaType());
            builder.mode(ParameterMode.IN);
            parameterMappings.add(builder.build());
        }
        return parameterMappings;
    }

    protected void newSelectKeyMappedStatement(MappedStatement ms, EntityColumn column) {
        KeyGenerator keyGenerator;
        String keyId = ms.getId() + SelectKeyGenerator.SELECT_KEY_SUFFIX;
        if (ms.getConfiguration().hasKeyGenerator(keyId)) {
            return;
        }
        Class<?> entityClass = getSelectReturnType(ms);
        Configuration configuration = ms.getConfiguration();
        Boolean executeBefore = Boolean.valueOf(getBEFORE());
        String IDENTITY = (column.getGenerator() == null || column.getGenerator().equals("")) ? getIDENTITY() : column.getGenerator();
        if (IDENTITY.equalsIgnoreCase("JDBC")) {
            keyGenerator = new Jdbc3KeyGenerator();
        } else {
            SqlSource sqlSource = new RawSqlSource(configuration, IDENTITY, entityClass);
            MappedStatement.Builder statementBuilder = new MappedStatement.Builder(configuration, keyId, sqlSource, SqlCommandType.SELECT);
            statementBuilder.resource(ms.getResource());
            statementBuilder.fetchSize(null);
            statementBuilder.statementType(StatementType.STATEMENT);
            statementBuilder.keyGenerator(new NoKeyGenerator());
            statementBuilder.keyProperty(column.getProperty());
            statementBuilder.keyColumn(null);
            statementBuilder.databaseId(null);
            statementBuilder.lang(configuration.getDefaultScriptingLanuageInstance());
            statementBuilder.resultOrdered(false);
            statementBuilder.resulSets(null);
            statementBuilder.timeout(configuration.getDefaultStatementTimeout());
            List<ParameterMapping> parameterMappings = new LinkedList<>();
            ParameterMap.Builder inlineParameterMapBuilder = new ParameterMap.Builder(configuration, statementBuilder.id() + "-Inline", entityClass, parameterMappings);
            statementBuilder.parameterMap(inlineParameterMapBuilder.build());
            List<ResultMap> resultMaps = new LinkedList<>();
            ResultMap.Builder inlineResultMapBuilder = new ResultMap.Builder(configuration, statementBuilder.id() + "-Inline", column.getJavaType(), new LinkedList(), null);
            resultMaps.add(inlineResultMapBuilder.build());
            statementBuilder.resultMaps(resultMaps);
            statementBuilder.resultSetType(null);
            statementBuilder.flushCacheRequired(false);
            statementBuilder.useCache(false);
            statementBuilder.cache(null);
            MappedStatement statement = statementBuilder.build();
            try {
                configuration.addMappedStatement(statement);
            } catch (Exception e) {
            }
            MappedStatement keyStatement = configuration.getMappedStatement(keyId, false);
            keyGenerator = new SelectKeyGenerator(keyStatement, executeBefore.booleanValue());
            try {
                configuration.addKeyGenerator(keyId, keyGenerator);
            } catch (Exception e2) {
            }
        }
        try {
            MetaObject msObject = SystemMetaObject.forObject(ms);
            msObject.setValue("keyGenerator", keyGenerator);
            msObject.setValue("keyProperties", column.getTable().getKeyProperties());
            msObject.setValue("keyColumns", column.getTable().getKeyColumns());
        } catch (Exception e3) {
        }
    }

    public IfSqlNode ExampleValidSqlNode(Configuration configuration) {
        List<SqlNode> whenSqlNodes = new LinkedList<>();
        IfSqlNode noValueSqlNode = new IfSqlNode(new TextSqlNode(" and ${criterion.condition}"), "criterion.noValue");
        whenSqlNodes.add(noValueSqlNode);
        IfSqlNode singleValueSqlNode = new IfSqlNode(new TextSqlNode(" and ${criterion.condition} #{criterion.value}"), "criterion.singleValue");
        whenSqlNodes.add(singleValueSqlNode);
        IfSqlNode betweenValueSqlNode = new IfSqlNode(new TextSqlNode(" and ${criterion.condition} #{criterion.value} and #{criterion.secondValue}"), "criterion.betweenValue");
        whenSqlNodes.add(betweenValueSqlNode);
        List<SqlNode> listValueContentSqlNodes = new LinkedList<>();
        listValueContentSqlNodes.add(new TextSqlNode(" and ${criterion.condition}"));
        ForEachSqlNode listValueForEachSqlNode = new ForEachSqlNode(configuration, new StaticTextSqlNode("#{listItem}"), "criterion.value", null, "listItem", "(", ")", ",");
        listValueContentSqlNodes.add(listValueForEachSqlNode);
        IfSqlNode listValueSqlNode = new IfSqlNode(new MixedSqlNode(listValueContentSqlNodes), "criterion.listValue");
        whenSqlNodes.add(listValueSqlNode);
        ChooseSqlNode chooseSqlNode = new ChooseSqlNode(whenSqlNodes, null);
        ForEachSqlNode criteriaSqlNode = new ForEachSqlNode(configuration, chooseSqlNode, "criteria.criteria", null, "criterion", null, null, null);
        TrimSqlNode trimSqlNode = new TrimSqlNode(configuration, criteriaSqlNode, "(", "and", ")", (String) null);
        IfSqlNode validSqlNode = new IfSqlNode(trimSqlNode, "criteria.valid");
        return validSqlNode;
    }

    public WhereSqlNode exampleWhereClause(Configuration configuration) {
        ForEachSqlNode forEachSqlNode = new ForEachSqlNode(configuration, ExampleValidSqlNode(configuration), "oredCriteria", null, "criteria", null, null, " or ");
        WhereSqlNode whereSqlNode = new WhereSqlNode(configuration, forEachSqlNode);
        return whereSqlNode;
    }

    public WhereSqlNode updateByExampleWhereClause(Configuration configuration) {
        ForEachSqlNode forEachSqlNode = new ForEachSqlNode(configuration, ExampleValidSqlNode(configuration), "example.oredCriteria", null, "criteria", null, null, " or ");
        WhereSqlNode whereSqlNode = new WhereSqlNode(configuration, forEachSqlNode);
        return whereSqlNode;
    }
}
