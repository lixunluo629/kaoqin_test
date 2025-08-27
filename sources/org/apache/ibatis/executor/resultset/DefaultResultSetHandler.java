package org.apache.ibatis.executor.resultset;

import java.lang.reflect.Constructor;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.ibatis.annotations.AutomapConstructor;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.cursor.defaults.DefaultCursor;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.loader.ResultLoader;
import org.apache.ibatis.executor.loader.ResultLoaderMap;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.result.DefaultResultContext;
import org.apache.ibatis.executor.result.DefaultResultHandler;
import org.apache.ibatis.executor.result.ResultMapException;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.Discriminator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.reflection.MetaClass;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/executor/resultset/DefaultResultSetHandler.class */
public class DefaultResultSetHandler implements ResultSetHandler {
    private static final Object DEFERED = new Object();
    private final Executor executor;
    private final Configuration configuration;
    private final MappedStatement mappedStatement;
    private final RowBounds rowBounds;
    private final ParameterHandler parameterHandler;
    private final ResultHandler<?> resultHandler;
    private final BoundSql boundSql;
    private final TypeHandlerRegistry typeHandlerRegistry;
    private final ObjectFactory objectFactory;
    private final ReflectorFactory reflectorFactory;
    private Object previousRowValue;
    private boolean useConstructorMappings;
    private final Map<CacheKey, Object> nestedResultObjects = new HashMap();
    private final Map<String, Object> ancestorObjects = new HashMap();
    private final Map<String, ResultMapping> nextResultMaps = new HashMap();
    private final Map<CacheKey, List<PendingRelation>> pendingRelations = new HashMap();
    private final Map<String, List<UnMappedColumnAutoMapping>> autoMappingsCache = new HashMap();
    private final PrimitiveTypes primitiveTypes = new PrimitiveTypes();

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/executor/resultset/DefaultResultSetHandler$PendingRelation.class */
    private static class PendingRelation {
        public MetaObject metaObject;
        public ResultMapping propertyMapping;

        private PendingRelation() {
        }
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/executor/resultset/DefaultResultSetHandler$UnMappedColumnAutoMapping.class */
    private static class UnMappedColumnAutoMapping {
        private final String column;
        private final String property;
        private final TypeHandler<?> typeHandler;
        private final boolean primitive;

        public UnMappedColumnAutoMapping(String column, String property, TypeHandler<?> typeHandler, boolean primitive) {
            this.column = column;
            this.property = property;
            this.typeHandler = typeHandler;
            this.primitive = primitive;
        }
    }

    public DefaultResultSetHandler(Executor executor, MappedStatement mappedStatement, ParameterHandler parameterHandler, ResultHandler<?> resultHandler, BoundSql boundSql, RowBounds rowBounds) {
        this.executor = executor;
        this.configuration = mappedStatement.getConfiguration();
        this.mappedStatement = mappedStatement;
        this.rowBounds = rowBounds;
        this.parameterHandler = parameterHandler;
        this.boundSql = boundSql;
        this.typeHandlerRegistry = this.configuration.getTypeHandlerRegistry();
        this.objectFactory = this.configuration.getObjectFactory();
        this.reflectorFactory = this.configuration.getReflectorFactory();
        this.resultHandler = resultHandler;
    }

    @Override // org.apache.ibatis.executor.resultset.ResultSetHandler
    public void handleOutputParameters(CallableStatement cs) throws SQLException {
        Object parameterObject = this.parameterHandler.getParameterObject();
        MetaObject metaParam = this.configuration.newMetaObject(parameterObject);
        List<ParameterMapping> parameterMappings = this.boundSql.getParameterMappings();
        for (int i = 0; i < parameterMappings.size(); i++) {
            ParameterMapping parameterMapping = parameterMappings.get(i);
            if (parameterMapping.getMode() == ParameterMode.OUT || parameterMapping.getMode() == ParameterMode.INOUT) {
                if (ResultSet.class.equals(parameterMapping.getJavaType())) {
                    handleRefCursorOutputParameter((ResultSet) cs.getObject(i + 1), parameterMapping, metaParam);
                } else {
                    TypeHandler<?> typeHandler = parameterMapping.getTypeHandler();
                    metaParam.setValue(parameterMapping.getProperty(), typeHandler.getResult(cs, i + 1));
                }
            }
        }
    }

    private void handleRefCursorOutputParameter(ResultSet rs, ParameterMapping parameterMapping, MetaObject metaParam) throws SQLException {
        if (rs == null) {
            return;
        }
        try {
            String resultMapId = parameterMapping.getResultMapId();
            ResultMap resultMap = this.configuration.getResultMap(resultMapId);
            ResultSetWrapper rsw = new ResultSetWrapper(rs, this.configuration);
            if (this.resultHandler == null) {
                DefaultResultHandler resultHandler = new DefaultResultHandler(this.objectFactory);
                handleRowValues(rsw, resultMap, resultHandler, new RowBounds(), null);
                metaParam.setValue(parameterMapping.getProperty(), resultHandler.getResultList());
            } else {
                handleRowValues(rsw, resultMap, this.resultHandler, new RowBounds(), null);
            }
        } finally {
            closeResultSet(rs);
        }
    }

    @Override // org.apache.ibatis.executor.resultset.ResultSetHandler
    public List<Object> handleResultSets(Statement stmt) throws SQLException {
        ErrorContext.instance().activity("handling results").object(this.mappedStatement.getId());
        List<Object> multipleResults = new ArrayList<>();
        int resultSetCount = 0;
        ResultSetWrapper rsw = getFirstResultSet(stmt);
        List<ResultMap> resultMaps = this.mappedStatement.getResultMaps();
        int resultMapCount = resultMaps.size();
        validateResultMapsCount(rsw, resultMapCount);
        while (rsw != null && resultMapCount > resultSetCount) {
            ResultMap resultMap = resultMaps.get(resultSetCount);
            handleResultSet(rsw, resultMap, multipleResults, null);
            rsw = getNextResultSet(stmt);
            cleanUpAfterHandlingResultSet();
            resultSetCount++;
        }
        String[] resultSets = this.mappedStatement.getResultSets();
        if (resultSets != null) {
            while (rsw != null && resultSetCount < resultSets.length) {
                ResultMapping parentMapping = this.nextResultMaps.get(resultSets[resultSetCount]);
                if (parentMapping != null) {
                    String nestedResultMapId = parentMapping.getNestedResultMapId();
                    ResultMap resultMap2 = this.configuration.getResultMap(nestedResultMapId);
                    handleResultSet(rsw, resultMap2, null, parentMapping);
                }
                rsw = getNextResultSet(stmt);
                cleanUpAfterHandlingResultSet();
                resultSetCount++;
            }
        }
        return collapseSingleResultList(multipleResults);
    }

    @Override // org.apache.ibatis.executor.resultset.ResultSetHandler
    public <E> Cursor<E> handleCursorResultSets(Statement stmt) throws SQLException {
        ErrorContext.instance().activity("handling cursor results").object(this.mappedStatement.getId());
        ResultSetWrapper rsw = getFirstResultSet(stmt);
        List<ResultMap> resultMaps = this.mappedStatement.getResultMaps();
        int resultMapCount = resultMaps.size();
        validateResultMapsCount(rsw, resultMapCount);
        if (resultMapCount != 1) {
            throw new ExecutorException("Cursor results cannot be mapped to multiple resultMaps");
        }
        ResultMap resultMap = resultMaps.get(0);
        return new DefaultCursor(this, resultMap, rsw, this.rowBounds);
    }

    private ResultSetWrapper getFirstResultSet(Statement stmt) throws SQLException {
        ResultSet rs = stmt.getResultSet();
        while (rs == null) {
            if (stmt.getMoreResults()) {
                rs = stmt.getResultSet();
            } else if (stmt.getUpdateCount() == -1) {
                break;
            }
        }
        if (rs != null) {
            return new ResultSetWrapper(rs, this.configuration);
        }
        return null;
    }

    private ResultSetWrapper getNextResultSet(Statement stmt) throws SQLException {
        try {
            if (stmt.getConnection().getMetaData().supportsMultipleResultSets()) {
                if (stmt.getMoreResults() || stmt.getUpdateCount() != -1) {
                    ResultSet rs = stmt.getResultSet();
                    if (rs == null) {
                        return getNextResultSet(stmt);
                    }
                    return new ResultSetWrapper(rs, this.configuration);
                }
                return null;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private void closeResultSet(ResultSet rs) throws SQLException {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
            }
        }
    }

    private void cleanUpAfterHandlingResultSet() {
        this.nestedResultObjects.clear();
    }

    private void validateResultMapsCount(ResultSetWrapper rsw, int resultMapCount) {
        if (rsw != null && resultMapCount < 1) {
            throw new ExecutorException("A query was run and no Result Maps were found for the Mapped Statement '" + this.mappedStatement.getId() + "'.  It's likely that neither a Result Type nor a Result Map was specified.");
        }
    }

    private void handleResultSet(ResultSetWrapper rsw, ResultMap resultMap, List<Object> multipleResults, ResultMapping parentMapping) throws SQLException {
        try {
            if (parentMapping != null) {
                handleRowValues(rsw, resultMap, null, RowBounds.DEFAULT, parentMapping);
            } else if (this.resultHandler == null) {
                DefaultResultHandler defaultResultHandler = new DefaultResultHandler(this.objectFactory);
                handleRowValues(rsw, resultMap, defaultResultHandler, this.rowBounds, null);
                multipleResults.add(defaultResultHandler.getResultList());
            } else {
                handleRowValues(rsw, resultMap, this.resultHandler, this.rowBounds, null);
            }
            closeResultSet(rsw.getResultSet());
        } catch (Throwable th) {
            closeResultSet(rsw.getResultSet());
            throw th;
        }
    }

    private List<Object> collapseSingleResultList(List<Object> multipleResults) {
        return multipleResults.size() == 1 ? (List) multipleResults.get(0) : multipleResults;
    }

    public void handleRowValues(ResultSetWrapper rsw, ResultMap resultMap, ResultHandler<?> resultHandler, RowBounds rowBounds, ResultMapping parentMapping) throws SQLException {
        if (resultMap.hasNestedResultMaps()) {
            ensureNoRowBounds();
            checkResultHandler();
            handleRowValuesForNestedResultMap(rsw, resultMap, resultHandler, rowBounds, parentMapping);
            return;
        }
        handleRowValuesForSimpleResultMap(rsw, resultMap, resultHandler, rowBounds, parentMapping);
    }

    private void ensureNoRowBounds() {
        if (!this.configuration.isSafeRowBoundsEnabled() || this.rowBounds == null) {
            return;
        }
        if (this.rowBounds.getLimit() < Integer.MAX_VALUE || this.rowBounds.getOffset() > 0) {
            throw new ExecutorException("Mapped Statements with nested result mappings cannot be safely constrained by RowBounds. Use safeRowBoundsEnabled=false setting to bypass this check.");
        }
    }

    protected void checkResultHandler() {
        if (this.resultHandler != null && this.configuration.isSafeResultHandlerEnabled() && !this.mappedStatement.isResultOrdered()) {
            throw new ExecutorException("Mapped Statements with nested result mappings cannot be safely used with a custom ResultHandler. Use safeResultHandlerEnabled=false setting to bypass this check or ensure your statement returns ordered data and set resultOrdered=true on it.");
        }
    }

    private void handleRowValuesForSimpleResultMap(ResultSetWrapper rsw, ResultMap resultMap, ResultHandler<?> resultHandler, RowBounds rowBounds, ResultMapping parentMapping) throws SQLException {
        DefaultResultContext<Object> resultContext = new DefaultResultContext<>();
        skipRows(rsw.getResultSet(), rowBounds);
        while (shouldProcessMoreRows(resultContext, rowBounds) && rsw.getResultSet().next()) {
            ResultMap discriminatedResultMap = resolveDiscriminatedResultMap(rsw.getResultSet(), resultMap, null);
            Object rowValue = getRowValue(rsw, discriminatedResultMap);
            storeObject(resultHandler, resultContext, rowValue, parentMapping, rsw.getResultSet());
        }
    }

    private void storeObject(ResultHandler<?> resultHandler, DefaultResultContext<Object> resultContext, Object rowValue, ResultMapping parentMapping, ResultSet rs) throws SQLException {
        if (parentMapping != null) {
            linkToParents(rs, parentMapping, rowValue);
        } else {
            callResultHandler(resultHandler, resultContext, rowValue);
        }
    }

    private void callResultHandler(ResultHandler<?> resultHandler, DefaultResultContext<Object> resultContext, Object rowValue) {
        resultContext.nextResultObject(rowValue);
        resultHandler.handleResult(resultContext);
    }

    private boolean shouldProcessMoreRows(ResultContext<?> context, RowBounds rowBounds) throws SQLException {
        return !context.isStopped() && context.getResultCount() < rowBounds.getLimit();
    }

    private void skipRows(ResultSet rs, RowBounds rowBounds) throws SQLException {
        if (rs.getType() != 1003) {
            if (rowBounds.getOffset() != 0) {
                rs.absolute(rowBounds.getOffset());
            }
        } else {
            for (int i = 0; i < rowBounds.getOffset(); i++) {
                rs.next();
            }
        }
    }

    private Object getRowValue(ResultSetWrapper rsw, ResultMap resultMap) throws SQLException {
        ResultLoaderMap lazyLoader = new ResultLoaderMap();
        Object rowValue = createResultObject(rsw, resultMap, lazyLoader, null);
        if (rowValue != null && !hasTypeHandlerForResultObject(rsw, resultMap.getType())) {
            MetaObject metaObject = this.configuration.newMetaObject(rowValue);
            boolean foundValues = this.useConstructorMappings;
            if (shouldApplyAutomaticMappings(resultMap, false)) {
                foundValues = applyAutomaticMappings(rsw, resultMap, metaObject, null) || foundValues;
            }
            rowValue = ((lazyLoader.size() > 0 || (applyPropertyMappings(rsw, resultMap, metaObject, lazyLoader, null) || foundValues)) || this.configuration.isReturnInstanceForEmptyRow()) ? rowValue : null;
        }
        return rowValue;
    }

    private boolean shouldApplyAutomaticMappings(ResultMap resultMap, boolean isNested) {
        if (resultMap.getAutoMapping() != null) {
            return resultMap.getAutoMapping().booleanValue();
        }
        return isNested ? AutoMappingBehavior.FULL == this.configuration.getAutoMappingBehavior() : AutoMappingBehavior.NONE != this.configuration.getAutoMappingBehavior();
    }

    private boolean applyPropertyMappings(ResultSetWrapper rsw, ResultMap resultMap, MetaObject metaObject, ResultLoaderMap lazyLoader, String columnPrefix) throws SQLException {
        List<String> mappedColumnNames = rsw.getMappedColumnNames(resultMap, columnPrefix);
        boolean foundValues = false;
        List<ResultMapping> propertyMappings = resultMap.getPropertyResultMappings();
        for (ResultMapping propertyMapping : propertyMappings) {
            String column = prependPrefix(propertyMapping.getColumn(), columnPrefix);
            if (propertyMapping.getNestedResultMapId() != null) {
                column = null;
            }
            if (propertyMapping.isCompositeResult() || ((column != null && mappedColumnNames.contains(column.toUpperCase(Locale.ENGLISH))) || propertyMapping.getResultSet() != null)) {
                Object value = getPropertyMappingValue(rsw.getResultSet(), metaObject, propertyMapping, lazyLoader, columnPrefix);
                String property = propertyMapping.getProperty();
                if (property != null) {
                    if (value == DEFERED) {
                        foundValues = true;
                    } else {
                        if (value != null) {
                            foundValues = true;
                        }
                        if (value != null || (this.configuration.isCallSettersOnNulls() && !metaObject.getSetterType(property).isPrimitive())) {
                            metaObject.setValue(property, value);
                        }
                    }
                }
            }
        }
        return foundValues;
    }

    private Object getPropertyMappingValue(ResultSet rs, MetaObject metaResultObject, ResultMapping propertyMapping, ResultLoaderMap lazyLoader, String columnPrefix) throws SQLException {
        if (propertyMapping.getNestedQueryId() != null) {
            return getNestedQueryMappingValue(rs, metaResultObject, propertyMapping, lazyLoader, columnPrefix);
        }
        if (propertyMapping.getResultSet() != null) {
            addPendingChildRelation(rs, metaResultObject, propertyMapping);
            return DEFERED;
        }
        TypeHandler<?> typeHandler = propertyMapping.getTypeHandler();
        String column = prependPrefix(propertyMapping.getColumn(), columnPrefix);
        return typeHandler.getResult(rs, column);
    }

    private List<UnMappedColumnAutoMapping> createAutomaticMappings(ResultSetWrapper rsw, ResultMap resultMap, MetaObject metaObject, String columnPrefix) throws SQLException {
        String mapKey = resultMap.getId() + ":" + columnPrefix;
        List<UnMappedColumnAutoMapping> autoMapping = this.autoMappingsCache.get(mapKey);
        if (autoMapping == null) {
            autoMapping = new ArrayList();
            List<String> unmappedColumnNames = rsw.getUnmappedColumnNames(resultMap, columnPrefix);
            for (String columnName : unmappedColumnNames) {
                String propertyName = columnName;
                if (columnPrefix != null && !columnPrefix.isEmpty()) {
                    if (columnName.toUpperCase(Locale.ENGLISH).startsWith(columnPrefix)) {
                        propertyName = columnName.substring(columnPrefix.length());
                    }
                }
                String property = metaObject.findProperty(propertyName, this.configuration.isMapUnderscoreToCamelCase());
                if (property != null && metaObject.hasSetter(property)) {
                    if (!resultMap.getMappedProperties().contains(property)) {
                        Class<?> propertyType = metaObject.getSetterType(property);
                        if (this.typeHandlerRegistry.hasTypeHandler(propertyType, rsw.getJdbcType(columnName))) {
                            TypeHandler<?> typeHandler = rsw.getTypeHandler(propertyType, columnName);
                            autoMapping.add(new UnMappedColumnAutoMapping(columnName, property, typeHandler, propertyType.isPrimitive()));
                        } else {
                            this.configuration.getAutoMappingUnknownColumnBehavior().doAction(this.mappedStatement, columnName, property, propertyType);
                        }
                    }
                } else {
                    this.configuration.getAutoMappingUnknownColumnBehavior().doAction(this.mappedStatement, columnName, property != null ? property : propertyName, null);
                }
            }
            this.autoMappingsCache.put(mapKey, autoMapping);
        }
        return autoMapping;
    }

    private boolean applyAutomaticMappings(ResultSetWrapper rsw, ResultMap resultMap, MetaObject metaObject, String columnPrefix) throws SQLException {
        List<UnMappedColumnAutoMapping> autoMapping = createAutomaticMappings(rsw, resultMap, metaObject, columnPrefix);
        boolean foundValues = false;
        if (!autoMapping.isEmpty()) {
            for (UnMappedColumnAutoMapping mapping : autoMapping) {
                Object value = mapping.typeHandler.getResult(rsw.getResultSet(), mapping.column);
                if (value != null) {
                    foundValues = true;
                }
                if (value != null || (this.configuration.isCallSettersOnNulls() && !mapping.primitive)) {
                    metaObject.setValue(mapping.property, value);
                }
            }
        }
        return foundValues;
    }

    private void linkToParents(ResultSet rs, ResultMapping parentMapping, Object rowValue) throws SQLException {
        CacheKey parentKey = createKeyForMultipleResults(rs, parentMapping, parentMapping.getColumn(), parentMapping.getForeignColumn());
        List<PendingRelation> parents = this.pendingRelations.get(parentKey);
        if (parents != null) {
            for (PendingRelation parent : parents) {
                if (parent != null && rowValue != null) {
                    linkObjects(parent.metaObject, parent.propertyMapping, rowValue);
                }
            }
        }
    }

    private void addPendingChildRelation(ResultSet rs, MetaObject metaResultObject, ResultMapping parentMapping) throws SQLException {
        CacheKey cacheKey = createKeyForMultipleResults(rs, parentMapping, parentMapping.getColumn(), parentMapping.getColumn());
        PendingRelation deferLoad = new PendingRelation();
        deferLoad.metaObject = metaResultObject;
        deferLoad.propertyMapping = parentMapping;
        List<PendingRelation> relations = this.pendingRelations.get(cacheKey);
        if (relations == null) {
            relations = new ArrayList();
            this.pendingRelations.put(cacheKey, relations);
        }
        relations.add(deferLoad);
        ResultMapping previous = this.nextResultMaps.get(parentMapping.getResultSet());
        if (previous == null) {
            this.nextResultMaps.put(parentMapping.getResultSet(), parentMapping);
        } else if (!previous.equals(parentMapping)) {
            throw new ExecutorException("Two different properties are mapped to the same resultSet");
        }
    }

    private CacheKey createKeyForMultipleResults(ResultSet rs, ResultMapping resultMapping, String names, String columns) throws SQLException {
        CacheKey cacheKey = new CacheKey();
        cacheKey.update(resultMapping);
        if (columns != null && names != null) {
            String[] columnsArray = columns.split(",");
            String[] namesArray = names.split(",");
            for (int i = 0; i < columnsArray.length; i++) {
                Object value = rs.getString(columnsArray[i]);
                if (value != null) {
                    cacheKey.update(namesArray[i]);
                    cacheKey.update(value);
                }
            }
        }
        return cacheKey;
    }

    private Object createResultObject(ResultSetWrapper rsw, ResultMap resultMap, ResultLoaderMap lazyLoader, String columnPrefix) throws SQLException {
        this.useConstructorMappings = false;
        List<Class<?>> constructorArgTypes = new ArrayList<>();
        List<Object> constructorArgs = new ArrayList<>();
        Object resultObject = createResultObject(rsw, resultMap, constructorArgTypes, constructorArgs, columnPrefix);
        if (resultObject != null && !hasTypeHandlerForResultObject(rsw, resultMap.getType())) {
            List<ResultMapping> propertyMappings = resultMap.getPropertyResultMappings();
            Iterator<ResultMapping> it = propertyMappings.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                ResultMapping propertyMapping = it.next();
                if (propertyMapping.getNestedQueryId() != null && propertyMapping.isLazy()) {
                    resultObject = this.configuration.getProxyFactory().createProxy(resultObject, lazyLoader, this.configuration, this.objectFactory, constructorArgTypes, constructorArgs);
                    break;
                }
            }
        }
        this.useConstructorMappings = (resultObject == null || constructorArgTypes.isEmpty()) ? false : true;
        return resultObject;
    }

    private Object createResultObject(ResultSetWrapper rsw, ResultMap resultMap, List<Class<?>> constructorArgTypes, List<Object> constructorArgs, String columnPrefix) throws SQLException {
        Class<?> resultType = resultMap.getType();
        MetaClass metaType = MetaClass.forClass(resultType, this.reflectorFactory);
        List<ResultMapping> constructorMappings = resultMap.getConstructorResultMappings();
        if (hasTypeHandlerForResultObject(rsw, resultType)) {
            return createPrimitiveResultObject(rsw, resultMap, columnPrefix);
        }
        if (!constructorMappings.isEmpty()) {
            return createParameterizedResultObject(rsw, resultType, constructorMappings, constructorArgTypes, constructorArgs, columnPrefix);
        }
        if (resultType.isInterface() || metaType.hasDefaultConstructor()) {
            return this.objectFactory.create(resultType);
        }
        if (shouldApplyAutomaticMappings(resultMap, false)) {
            return createByConstructorSignature(rsw, resultType, constructorArgTypes, constructorArgs, columnPrefix);
        }
        throw new ExecutorException("Do not know how to create an instance of " + resultType);
    }

    Object createParameterizedResultObject(ResultSetWrapper rsw, Class<?> resultType, List<ResultMapping> constructorMappings, List<Class<?>> constructorArgTypes, List<Object> constructorArgs, String columnPrefix) {
        Object value;
        boolean foundValues = false;
        for (ResultMapping constructorMapping : constructorMappings) {
            Class<?> parameterType = constructorMapping.getJavaType();
            String column = constructorMapping.getColumn();
            try {
                if (constructorMapping.getNestedQueryId() != null) {
                    value = getNestedQueryConstructorValue(rsw.getResultSet(), constructorMapping, columnPrefix);
                } else if (constructorMapping.getNestedResultMapId() != null) {
                    ResultMap resultMap = this.configuration.getResultMap(constructorMapping.getNestedResultMapId());
                    value = getRowValue(rsw, resultMap);
                } else {
                    TypeHandler<?> typeHandler = constructorMapping.getTypeHandler();
                    value = typeHandler.getResult(rsw.getResultSet(), prependPrefix(column, columnPrefix));
                }
                constructorArgTypes.add(parameterType);
                constructorArgs.add(value);
                foundValues = value != null || foundValues;
            } catch (SQLException e) {
                throw new ExecutorException("Could not process result for mapping: " + constructorMapping, e);
            } catch (ResultMapException e2) {
                throw new ExecutorException("Could not process result for mapping: " + constructorMapping, e2);
            }
        }
        if (foundValues) {
            return this.objectFactory.create(resultType, constructorArgTypes, constructorArgs);
        }
        return null;
    }

    private Object createByConstructorSignature(ResultSetWrapper rsw, Class<?> resultType, List<Class<?>> constructorArgTypes, List<Object> constructorArgs, String columnPrefix) throws SQLException, SecurityException {
        Constructor<?>[] constructors = resultType.getDeclaredConstructors();
        Constructor<?> annotatedConstructor = findAnnotatedConstructor(constructors);
        if (annotatedConstructor != null) {
            return createUsingConstructor(rsw, resultType, constructorArgTypes, constructorArgs, columnPrefix, annotatedConstructor);
        }
        for (Constructor<?> constructor : constructors) {
            if (allowedConstructor(constructor, rsw.getClassNames())) {
                return createUsingConstructor(rsw, resultType, constructorArgTypes, constructorArgs, columnPrefix, constructor);
            }
        }
        throw new ExecutorException("No constructor found in " + resultType.getName() + " matching " + rsw.getClassNames());
    }

    private Object createUsingConstructor(ResultSetWrapper rsw, Class<?> resultType, List<Class<?>> constructorArgTypes, List<Object> constructorArgs, String columnPrefix, Constructor<?> constructor) throws SQLException {
        boolean foundValues = false;
        for (int i = 0; i < constructor.getParameterTypes().length; i++) {
            Class<?> parameterType = constructor.getParameterTypes()[i];
            String columnName = rsw.getColumnNames().get(i);
            TypeHandler<?> typeHandler = rsw.getTypeHandler(parameterType, columnName);
            Object value = typeHandler.getResult(rsw.getResultSet(), prependPrefix(columnName, columnPrefix));
            constructorArgTypes.add(parameterType);
            constructorArgs.add(value);
            foundValues = value != null || foundValues;
        }
        if (foundValues) {
            return this.objectFactory.create(resultType, constructorArgTypes, constructorArgs);
        }
        return null;
    }

    private Constructor<?> findAnnotatedConstructor(Constructor<?>[] constructors) {
        for (Constructor<?> constructor : constructors) {
            if (constructor.isAnnotationPresent(AutomapConstructor.class)) {
                return constructor;
            }
        }
        return null;
    }

    private boolean allowedConstructor(Constructor<?> constructor, List<String> classNames) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        if (typeNames(parameterTypes).equals(classNames)) {
            return true;
        }
        if (parameterTypes.length != classNames.size()) {
            return false;
        }
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            if (parameterType.isPrimitive() && !this.primitiveTypes.getWrapper(parameterType).getName().equals(classNames.get(i))) {
                return false;
            }
            if (!parameterType.isPrimitive() && !parameterType.getName().equals(classNames.get(i))) {
                return false;
            }
        }
        return true;
    }

    private List<String> typeNames(Class<?>[] parameterTypes) {
        List<String> names = new ArrayList<>();
        for (Class<?> type : parameterTypes) {
            names.add(type.getName());
        }
        return names;
    }

    private Object createPrimitiveResultObject(ResultSetWrapper rsw, ResultMap resultMap, String columnPrefix) throws SQLException {
        String columnName;
        Class<?> resultType = resultMap.getType();
        if (!resultMap.getResultMappings().isEmpty()) {
            List<ResultMapping> resultMappingList = resultMap.getResultMappings();
            ResultMapping mapping = resultMappingList.get(0);
            columnName = prependPrefix(mapping.getColumn(), columnPrefix);
        } else {
            columnName = rsw.getColumnNames().get(0);
        }
        TypeHandler<?> typeHandler = rsw.getTypeHandler(resultType, columnName);
        return typeHandler.getResult(rsw.getResultSet(), columnName);
    }

    private Object getNestedQueryConstructorValue(ResultSet rs, ResultMapping constructorMapping, String columnPrefix) throws SQLException {
        String nestedQueryId = constructorMapping.getNestedQueryId();
        MappedStatement nestedQuery = this.configuration.getMappedStatement(nestedQueryId);
        Class<?> nestedQueryParameterType = nestedQuery.getParameterMap().getType();
        Object nestedQueryParameterObject = prepareParameterForNestedQuery(rs, constructorMapping, nestedQueryParameterType, columnPrefix);
        Object value = null;
        if (nestedQueryParameterObject != null) {
            BoundSql nestedBoundSql = nestedQuery.getBoundSql(nestedQueryParameterObject);
            CacheKey key = this.executor.createCacheKey(nestedQuery, nestedQueryParameterObject, RowBounds.DEFAULT, nestedBoundSql);
            Class<?> targetType = constructorMapping.getJavaType();
            ResultLoader resultLoader = new ResultLoader(this.configuration, this.executor, nestedQuery, nestedQueryParameterObject, targetType, key, nestedBoundSql);
            value = resultLoader.loadResult();
        }
        return value;
    }

    private Object getNestedQueryMappingValue(ResultSet rs, MetaObject metaResultObject, ResultMapping propertyMapping, ResultLoaderMap lazyLoader, String columnPrefix) throws SQLException {
        String nestedQueryId = propertyMapping.getNestedQueryId();
        String property = propertyMapping.getProperty();
        MappedStatement nestedQuery = this.configuration.getMappedStatement(nestedQueryId);
        Class<?> nestedQueryParameterType = nestedQuery.getParameterMap().getType();
        Object nestedQueryParameterObject = prepareParameterForNestedQuery(rs, propertyMapping, nestedQueryParameterType, columnPrefix);
        Object value = null;
        if (nestedQueryParameterObject != null) {
            BoundSql nestedBoundSql = nestedQuery.getBoundSql(nestedQueryParameterObject);
            CacheKey key = this.executor.createCacheKey(nestedQuery, nestedQueryParameterObject, RowBounds.DEFAULT, nestedBoundSql);
            Class<?> targetType = propertyMapping.getJavaType();
            if (this.executor.isCached(nestedQuery, key)) {
                this.executor.deferLoad(nestedQuery, metaResultObject, property, key, targetType);
                value = DEFERED;
            } else {
                ResultLoader resultLoader = new ResultLoader(this.configuration, this.executor, nestedQuery, nestedQueryParameterObject, targetType, key, nestedBoundSql);
                if (propertyMapping.isLazy()) {
                    lazyLoader.addLoader(property, metaResultObject, resultLoader);
                    value = DEFERED;
                } else {
                    value = resultLoader.loadResult();
                }
            }
        }
        return value;
    }

    private Object prepareParameterForNestedQuery(ResultSet rs, ResultMapping resultMapping, Class<?> parameterType, String columnPrefix) throws SQLException {
        if (resultMapping.isCompositeResult()) {
            return prepareCompositeKeyParameter(rs, resultMapping, parameterType, columnPrefix);
        }
        return prepareSimpleKeyParameter(rs, resultMapping, parameterType, columnPrefix);
    }

    private Object prepareSimpleKeyParameter(ResultSet rs, ResultMapping resultMapping, Class<?> parameterType, String columnPrefix) throws SQLException {
        TypeHandler<?> typeHandler;
        if (this.typeHandlerRegistry.hasTypeHandler(parameterType)) {
            typeHandler = this.typeHandlerRegistry.getTypeHandler(parameterType);
        } else {
            typeHandler = this.typeHandlerRegistry.getUnknownTypeHandler();
        }
        return typeHandler.getResult(rs, prependPrefix(resultMapping.getColumn(), columnPrefix));
    }

    private Object prepareCompositeKeyParameter(ResultSet rs, ResultMapping resultMapping, Class<?> parameterType, String columnPrefix) throws SQLException {
        Object parameterObject = instantiateParameterObject(parameterType);
        MetaObject metaObject = this.configuration.newMetaObject(parameterObject);
        boolean foundValues = false;
        for (ResultMapping innerResultMapping : resultMapping.getComposites()) {
            Class<?> propType = metaObject.getSetterType(innerResultMapping.getProperty());
            TypeHandler<?> typeHandler = this.typeHandlerRegistry.getTypeHandler(propType);
            Object propValue = typeHandler.getResult(rs, prependPrefix(innerResultMapping.getColumn(), columnPrefix));
            if (propValue != null) {
                metaObject.setValue(innerResultMapping.getProperty(), propValue);
                foundValues = true;
            }
        }
        if (foundValues) {
            return parameterObject;
        }
        return null;
    }

    private Object instantiateParameterObject(Class<?> parameterType) {
        if (parameterType == null) {
            return new HashMap();
        }
        if (MapperMethod.ParamMap.class.equals(parameterType)) {
            return new HashMap();
        }
        return this.objectFactory.create(parameterType);
    }

    public ResultMap resolveDiscriminatedResultMap(ResultSet rs, ResultMap resultMap, String columnPrefix) throws SQLException {
        Set<String> pastDiscriminators = new HashSet<>();
        Discriminator discriminator = resultMap.getDiscriminator();
        while (discriminator != null) {
            Object value = getDiscriminatorValue(rs, discriminator, columnPrefix);
            String discriminatedMapId = discriminator.getMapIdFor(String.valueOf(value));
            if (!this.configuration.hasResultMap(discriminatedMapId)) {
                break;
            }
            resultMap = this.configuration.getResultMap(discriminatedMapId);
            Discriminator lastDiscriminator = discriminator;
            discriminator = resultMap.getDiscriminator();
            if (discriminator == lastDiscriminator || !pastDiscriminators.add(discriminatedMapId)) {
                break;
            }
        }
        return resultMap;
    }

    private Object getDiscriminatorValue(ResultSet rs, Discriminator discriminator, String columnPrefix) throws SQLException {
        ResultMapping resultMapping = discriminator.getResultMapping();
        TypeHandler<?> typeHandler = resultMapping.getTypeHandler();
        return typeHandler.getResult(rs, prependPrefix(resultMapping.getColumn(), columnPrefix));
    }

    private String prependPrefix(String columnName, String prefix) {
        if (columnName == null || columnName.length() == 0 || prefix == null || prefix.length() == 0) {
            return columnName;
        }
        return prefix + columnName;
    }

    private void handleRowValuesForNestedResultMap(ResultSetWrapper rsw, ResultMap resultMap, ResultHandler<?> resultHandler, RowBounds rowBounds, ResultMapping parentMapping) throws SQLException {
        DefaultResultContext<Object> resultContext = new DefaultResultContext<>();
        skipRows(rsw.getResultSet(), rowBounds);
        Object rowValue = this.previousRowValue;
        while (shouldProcessMoreRows(resultContext, rowBounds) && rsw.getResultSet().next()) {
            ResultMap discriminatedResultMap = resolveDiscriminatedResultMap(rsw.getResultSet(), resultMap, null);
            CacheKey rowKey = createRowKey(discriminatedResultMap, rsw, null);
            Object partialObject = this.nestedResultObjects.get(rowKey);
            if (this.mappedStatement.isResultOrdered()) {
                if (partialObject == null && rowValue != null) {
                    this.nestedResultObjects.clear();
                    storeObject(resultHandler, resultContext, rowValue, parentMapping, rsw.getResultSet());
                }
                rowValue = getRowValue(rsw, discriminatedResultMap, rowKey, null, partialObject);
            } else {
                rowValue = getRowValue(rsw, discriminatedResultMap, rowKey, null, partialObject);
                if (partialObject == null) {
                    storeObject(resultHandler, resultContext, rowValue, parentMapping, rsw.getResultSet());
                }
            }
        }
        if (rowValue != null && this.mappedStatement.isResultOrdered() && shouldProcessMoreRows(resultContext, rowBounds)) {
            storeObject(resultHandler, resultContext, rowValue, parentMapping, rsw.getResultSet());
            this.previousRowValue = null;
        } else if (rowValue != null) {
            this.previousRowValue = rowValue;
        }
    }

    private Object getRowValue(ResultSetWrapper rsw, ResultMap resultMap, CacheKey combinedKey, String columnPrefix, Object partialObject) throws SQLException {
        String resultMapId = resultMap.getId();
        Object rowValue = partialObject;
        if (rowValue != null) {
            MetaObject metaObject = this.configuration.newMetaObject(rowValue);
            putAncestor(rowValue, resultMapId);
            applyNestedResultMappings(rsw, resultMap, metaObject, columnPrefix, combinedKey, false);
            this.ancestorObjects.remove(resultMapId);
        } else {
            ResultLoaderMap lazyLoader = new ResultLoaderMap();
            rowValue = createResultObject(rsw, resultMap, lazyLoader, columnPrefix);
            if (rowValue != null && !hasTypeHandlerForResultObject(rsw, resultMap.getType())) {
                MetaObject metaObject2 = this.configuration.newMetaObject(rowValue);
                boolean foundValues = this.useConstructorMappings;
                if (shouldApplyAutomaticMappings(resultMap, true)) {
                    foundValues = applyAutomaticMappings(rsw, resultMap, metaObject2, columnPrefix) || foundValues;
                }
                boolean foundValues2 = applyPropertyMappings(rsw, resultMap, metaObject2, lazyLoader, columnPrefix) || foundValues;
                putAncestor(rowValue, resultMapId);
                boolean foundValues3 = applyNestedResultMappings(rsw, resultMap, metaObject2, columnPrefix, combinedKey, true) || foundValues2;
                this.ancestorObjects.remove(resultMapId);
                rowValue = ((lazyLoader.size() > 0 || foundValues3) || this.configuration.isReturnInstanceForEmptyRow()) ? rowValue : null;
            }
            if (combinedKey != CacheKey.NULL_CACHE_KEY) {
                this.nestedResultObjects.put(combinedKey, rowValue);
            }
        }
        return rowValue;
    }

    private void putAncestor(Object resultObject, String resultMapId) {
        this.ancestorObjects.put(resultMapId, resultObject);
    }

    private boolean applyNestedResultMappings(ResultSetWrapper rsw, ResultMap resultMap, MetaObject metaObject, String parentPrefix, CacheKey parentRowKey, boolean newObject) {
        Object rowValue;
        Object ancestorObject;
        boolean foundValues = false;
        for (ResultMapping resultMapping : resultMap.getPropertyResultMappings()) {
            String nestedResultMapId = resultMapping.getNestedResultMapId();
            if (nestedResultMapId != null && resultMapping.getResultSet() == null) {
                try {
                    String columnPrefix = getColumnPrefix(parentPrefix, resultMapping);
                    ResultMap nestedResultMap = getNestedResultMap(rsw.getResultSet(), nestedResultMapId, columnPrefix);
                    if (resultMapping.getColumnPrefix() == null && (ancestorObject = this.ancestorObjects.get(nestedResultMapId)) != null) {
                        if (newObject) {
                            linkObjects(metaObject, resultMapping, ancestorObject);
                        }
                    } else {
                        CacheKey rowKey = createRowKey(nestedResultMap, rsw, columnPrefix);
                        CacheKey combinedKey = combineKeys(rowKey, parentRowKey);
                        Object rowValue2 = this.nestedResultObjects.get(combinedKey);
                        boolean knownValue = rowValue2 != null;
                        instantiateCollectionPropertyIfAppropriate(resultMapping, metaObject);
                        if (anyNotNullColumnHasValue(resultMapping, columnPrefix, rsw) && (rowValue = getRowValue(rsw, nestedResultMap, combinedKey, columnPrefix, rowValue2)) != null && !knownValue) {
                            linkObjects(metaObject, resultMapping, rowValue);
                            foundValues = true;
                        }
                    }
                } catch (SQLException e) {
                    throw new ExecutorException("Error getting nested result map values for '" + resultMapping.getProperty() + "'.  Cause: " + e, e);
                }
            }
        }
        return foundValues;
    }

    private String getColumnPrefix(String parentPrefix, ResultMapping resultMapping) {
        StringBuilder columnPrefixBuilder = new StringBuilder();
        if (parentPrefix != null) {
            columnPrefixBuilder.append(parentPrefix);
        }
        if (resultMapping.getColumnPrefix() != null) {
            columnPrefixBuilder.append(resultMapping.getColumnPrefix());
        }
        if (columnPrefixBuilder.length() == 0) {
            return null;
        }
        return columnPrefixBuilder.toString().toUpperCase(Locale.ENGLISH);
    }

    private boolean anyNotNullColumnHasValue(ResultMapping resultMapping, String columnPrefix, ResultSetWrapper rsw) throws SQLException {
        Set<String> notNullColumns = resultMapping.getNotNullColumns();
        if (notNullColumns != null && !notNullColumns.isEmpty()) {
            ResultSet rs = rsw.getResultSet();
            for (String column : notNullColumns) {
                rs.getObject(prependPrefix(column, columnPrefix));
                if (!rs.wasNull()) {
                    return true;
                }
            }
            return false;
        }
        if (columnPrefix != null) {
            for (String columnName : rsw.getColumnNames()) {
                if (columnName.toUpperCase().startsWith(columnPrefix.toUpperCase())) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    private ResultMap getNestedResultMap(ResultSet rs, String nestedResultMapId, String columnPrefix) throws SQLException {
        ResultMap nestedResultMap = this.configuration.getResultMap(nestedResultMapId);
        return resolveDiscriminatedResultMap(rs, nestedResultMap, columnPrefix);
    }

    private CacheKey createRowKey(ResultMap resultMap, ResultSetWrapper rsw, String columnPrefix) throws SQLException {
        CacheKey cacheKey = new CacheKey();
        cacheKey.update(resultMap.getId());
        List<ResultMapping> resultMappings = getResultMappingsForRowKey(resultMap);
        if (resultMappings.isEmpty()) {
            if (Map.class.isAssignableFrom(resultMap.getType())) {
                createRowKeyForMap(rsw, cacheKey);
            } else {
                createRowKeyForUnmappedProperties(resultMap, rsw, cacheKey, columnPrefix);
            }
        } else {
            createRowKeyForMappedProperties(resultMap, rsw, cacheKey, resultMappings, columnPrefix);
        }
        if (cacheKey.getUpdateCount() < 2) {
            return CacheKey.NULL_CACHE_KEY;
        }
        return cacheKey;
    }

    private CacheKey combineKeys(CacheKey rowKey, CacheKey parentRowKey) {
        if (rowKey.getUpdateCount() > 1 && parentRowKey.getUpdateCount() > 1) {
            try {
                CacheKey combinedKey = rowKey.m3147clone();
                combinedKey.update(parentRowKey);
                return combinedKey;
            } catch (CloneNotSupportedException e) {
                throw new ExecutorException("Error cloning cache key.  Cause: " + e, e);
            }
        }
        return CacheKey.NULL_CACHE_KEY;
    }

    private List<ResultMapping> getResultMappingsForRowKey(ResultMap resultMap) {
        List<ResultMapping> resultMappings = resultMap.getIdResultMappings();
        if (resultMappings.isEmpty()) {
            resultMappings = resultMap.getPropertyResultMappings();
        }
        return resultMappings;
    }

    private void createRowKeyForMappedProperties(ResultMap resultMap, ResultSetWrapper rsw, CacheKey cacheKey, List<ResultMapping> resultMappings, String columnPrefix) throws SQLException {
        Object value;
        for (ResultMapping resultMapping : resultMappings) {
            if (resultMapping.getNestedResultMapId() != null && resultMapping.getResultSet() == null) {
                ResultMap nestedResultMap = this.configuration.getResultMap(resultMapping.getNestedResultMapId());
                createRowKeyForMappedProperties(nestedResultMap, rsw, cacheKey, nestedResultMap.getConstructorResultMappings(), prependPrefix(resultMapping.getColumnPrefix(), columnPrefix));
            } else if (resultMapping.getNestedQueryId() == null) {
                String column = prependPrefix(resultMapping.getColumn(), columnPrefix);
                TypeHandler<?> th = resultMapping.getTypeHandler();
                List<String> mappedColumnNames = rsw.getMappedColumnNames(resultMap, columnPrefix);
                if (column != null && mappedColumnNames.contains(column.toUpperCase(Locale.ENGLISH)) && ((value = th.getResult(rsw.getResultSet(), column)) != null || this.configuration.isReturnInstanceForEmptyRow())) {
                    cacheKey.update(column);
                    cacheKey.update(value);
                }
            }
        }
    }

    private void createRowKeyForUnmappedProperties(ResultMap resultMap, ResultSetWrapper rsw, CacheKey cacheKey, String columnPrefix) throws SQLException {
        String value;
        MetaClass metaType = MetaClass.forClass(resultMap.getType(), this.reflectorFactory);
        List<String> unmappedColumnNames = rsw.getUnmappedColumnNames(resultMap, columnPrefix);
        for (String column : unmappedColumnNames) {
            String property = column;
            if (columnPrefix != null && !columnPrefix.isEmpty()) {
                if (column.toUpperCase(Locale.ENGLISH).startsWith(columnPrefix)) {
                    property = column.substring(columnPrefix.length());
                }
            }
            if (metaType.findProperty(property, this.configuration.isMapUnderscoreToCamelCase()) != null && (value = rsw.getResultSet().getString(column)) != null) {
                cacheKey.update(column);
                cacheKey.update(value);
            }
        }
    }

    private void createRowKeyForMap(ResultSetWrapper rsw, CacheKey cacheKey) throws SQLException {
        List<String> columnNames = rsw.getColumnNames();
        for (String columnName : columnNames) {
            String value = rsw.getResultSet().getString(columnName);
            if (value != null) {
                cacheKey.update(columnName);
                cacheKey.update(value);
            }
        }
    }

    private void linkObjects(MetaObject metaObject, ResultMapping resultMapping, Object rowValue) {
        Object collectionProperty = instantiateCollectionPropertyIfAppropriate(resultMapping, metaObject);
        if (collectionProperty != null) {
            MetaObject targetMetaObject = this.configuration.newMetaObject(collectionProperty);
            targetMetaObject.add(rowValue);
        } else {
            metaObject.setValue(resultMapping.getProperty(), rowValue);
        }
    }

    private Object instantiateCollectionPropertyIfAppropriate(ResultMapping resultMapping, MetaObject metaObject) {
        String propertyName = resultMapping.getProperty();
        Object propertyValue = metaObject.getValue(propertyName);
        if (propertyValue != null) {
            if (this.objectFactory.isCollection(propertyValue.getClass())) {
                return propertyValue;
            }
            return null;
        }
        Class<?> type = resultMapping.getJavaType();
        if (type == null) {
            type = metaObject.getSetterType(propertyName);
        }
        try {
            if (this.objectFactory.isCollection(type)) {
                Object propertyValue2 = this.objectFactory.create(type);
                metaObject.setValue(propertyName, propertyValue2);
                return propertyValue2;
            }
            return null;
        } catch (Exception e) {
            throw new ExecutorException("Error instantiating collection property for result '" + resultMapping.getProperty() + "'.  Cause: " + e, e);
        }
    }

    private boolean hasTypeHandlerForResultObject(ResultSetWrapper rsw, Class<?> resultType) {
        if (rsw.getColumnNames().size() == 1) {
            return this.typeHandlerRegistry.hasTypeHandler(resultType, rsw.getJdbcType(rsw.getColumnNames().get(0)));
        }
        return this.typeHandlerRegistry.hasTypeHandler(resultType);
    }
}
