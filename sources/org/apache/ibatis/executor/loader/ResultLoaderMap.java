package org.apache.ibatis.executor.loader;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.BaseExecutor;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/executor/loader/ResultLoaderMap.class */
public class ResultLoaderMap {
    private final Map<String, LoadPair> loaderMap = new HashMap();

    public void addLoader(String property, MetaObject metaResultObject, ResultLoader resultLoader) {
        String upperFirst = getUppercaseFirstProperty(property);
        if (!upperFirst.equalsIgnoreCase(property) && this.loaderMap.containsKey(upperFirst)) {
            throw new ExecutorException("Nested lazy loaded result property '" + property + "' for query id '" + resultLoader.mappedStatement.getId() + " already exists in the result map. The leftmost property of all lazy loaded properties must be unique within a result map.");
        }
        this.loaderMap.put(upperFirst, new LoadPair(property, metaResultObject, resultLoader));
    }

    public final Map<String, LoadPair> getProperties() {
        return new HashMap(this.loaderMap);
    }

    public Set<String> getPropertyNames() {
        return this.loaderMap.keySet();
    }

    public int size() {
        return this.loaderMap.size();
    }

    public boolean hasLoader(String property) {
        return this.loaderMap.containsKey(property.toUpperCase(Locale.ENGLISH));
    }

    public boolean load(String property) throws PrivilegedActionException, IllegalAccessException, SQLException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        LoadPair pair = this.loaderMap.remove(property.toUpperCase(Locale.ENGLISH));
        if (pair != null) {
            pair.load();
            return true;
        }
        return false;
    }

    public void remove(String property) {
        this.loaderMap.remove(property.toUpperCase(Locale.ENGLISH));
    }

    public void loadAll() throws PrivilegedActionException, IllegalAccessException, SQLException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        Set<String> methodNameSet = this.loaderMap.keySet();
        String[] methodNames = (String[]) methodNameSet.toArray(new String[methodNameSet.size()]);
        for (String methodName : methodNames) {
            load(methodName);
        }
    }

    private static String getUppercaseFirstProperty(String property) {
        String[] parts = property.split("\\.");
        return parts[0].toUpperCase(Locale.ENGLISH);
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/executor/loader/ResultLoaderMap$LoadPair.class */
    public static class LoadPair implements Serializable {
        private static final long serialVersionUID = 20130412;
        private static final String FACTORY_METHOD = "getConfiguration";
        private final transient Object serializationCheck;
        private transient MetaObject metaResultObject;
        private transient ResultLoader resultLoader;
        private transient Log log;
        private Class<?> configurationFactory;
        private String property;
        private String mappedStatement;
        private Serializable mappedParameter;

        private LoadPair(String property, MetaObject metaResultObject, ResultLoader resultLoader) {
            this.serializationCheck = new Object();
            this.property = property;
            this.metaResultObject = metaResultObject;
            this.resultLoader = resultLoader;
            if (metaResultObject != null && (metaResultObject.getOriginalObject() instanceof Serializable)) {
                Object mappedStatementParameter = resultLoader.parameterObject;
                if (mappedStatementParameter instanceof Serializable) {
                    this.mappedStatement = resultLoader.mappedStatement.getId();
                    this.mappedParameter = (Serializable) mappedStatementParameter;
                    this.configurationFactory = resultLoader.configuration.getConfigurationFactory();
                } else {
                    Log log = getLogger();
                    if (log.isDebugEnabled()) {
                        log.debug("Property [" + this.property + "] of [" + metaResultObject.getOriginalObject().getClass() + "] cannot be loaded after deserialization. Make sure it's loaded before serializing forenamed object.");
                    }
                }
            }
        }

        public void load() throws PrivilegedActionException, IllegalAccessException, SQLException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
            if (this.metaResultObject == null) {
                throw new IllegalArgumentException("metaResultObject is null");
            }
            if (this.resultLoader == null) {
                throw new IllegalArgumentException("resultLoader is null");
            }
            load(null);
        }

        public void load(Object userObject) throws PrivilegedActionException, IllegalAccessException, SQLException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
            if (this.metaResultObject == null || this.resultLoader == null) {
                if (this.mappedParameter == null) {
                    throw new ExecutorException("Property [" + this.property + "] cannot be loaded because required parameter of mapped statement [" + this.mappedStatement + "] is not serializable.");
                }
                Configuration config = getConfiguration();
                MappedStatement ms = config.getMappedStatement(this.mappedStatement);
                if (ms == null) {
                    throw new ExecutorException("Cannot lazy load property [" + this.property + "] of deserialized object [" + userObject.getClass() + "] because configuration does not contain statement [" + this.mappedStatement + "]");
                }
                this.metaResultObject = config.newMetaObject(userObject);
                this.resultLoader = new ResultLoader(config, new ClosedExecutor(), ms, this.mappedParameter, this.metaResultObject.getSetterType(this.property), null, null);
            }
            if (this.serializationCheck == null) {
                ResultLoader old = this.resultLoader;
                this.resultLoader = new ResultLoader(old.configuration, new ClosedExecutor(), old.mappedStatement, old.parameterObject, old.targetType, old.cacheKey, old.boundSql);
            }
            this.metaResultObject.setValue(this.property, this.resultLoader.loadResult());
        }

        private Configuration getConfiguration() throws PrivilegedActionException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
            Object configurationObject;
            if (this.configurationFactory == null) {
                throw new ExecutorException("Cannot get Configuration as configuration factory was not set.");
            }
            try {
                final Method factoryMethod = this.configurationFactory.getDeclaredMethod(FACTORY_METHOD, new Class[0]);
                if (!Modifier.isStatic(factoryMethod.getModifiers())) {
                    throw new ExecutorException("Cannot get Configuration as factory method [" + this.configurationFactory + "]#[" + FACTORY_METHOD + "] is not static.");
                }
                if (!factoryMethod.isAccessible()) {
                    configurationObject = AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() { // from class: org.apache.ibatis.executor.loader.ResultLoaderMap.LoadPair.1
                        @Override // java.security.PrivilegedExceptionAction
                        public Object run() throws Exception {
                            try {
                                factoryMethod.setAccessible(true);
                                return factoryMethod.invoke(null, new Object[0]);
                            } finally {
                                factoryMethod.setAccessible(false);
                            }
                        }
                    });
                } else {
                    configurationObject = factoryMethod.invoke(null, new Object[0]);
                }
                if (!(configurationObject instanceof Configuration)) {
                    throw new ExecutorException("Cannot get Configuration as factory method [" + this.configurationFactory + "]#[" + FACTORY_METHOD + "] didn't return [" + Configuration.class + "] but [" + (configurationObject == null ? "null" : configurationObject.getClass()) + "].");
                }
                return (Configuration) Configuration.class.cast(configurationObject);
            } catch (NoSuchMethodException ex) {
                throw new ExecutorException("Cannot get Configuration as factory class [" + this.configurationFactory + "] is missing factory method of name [" + FACTORY_METHOD + "].", ex);
            } catch (PrivilegedActionException ex2) {
                throw new ExecutorException("Cannot get Configuration as factory method [" + this.configurationFactory + "]#[" + FACTORY_METHOD + "] threw an exception.", ex2.getCause());
            } catch (ExecutorException ex3) {
                throw ex3;
            } catch (Exception ex4) {
                throw new ExecutorException("Cannot get Configuration as factory method [" + this.configurationFactory + "]#[" + FACTORY_METHOD + "] threw an exception.", ex4);
            }
        }

        private Log getLogger() {
            if (this.log == null) {
                this.log = LogFactory.getLog(getClass());
            }
            return this.log;
        }
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/executor/loader/ResultLoaderMap$ClosedExecutor.class */
    private static final class ClosedExecutor extends BaseExecutor {
        public ClosedExecutor() {
            super(null, null);
        }

        @Override // org.apache.ibatis.executor.BaseExecutor, org.apache.ibatis.executor.Executor
        public boolean isClosed() {
            return true;
        }

        @Override // org.apache.ibatis.executor.BaseExecutor
        protected int doUpdate(MappedStatement ms, Object parameter) throws SQLException {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override // org.apache.ibatis.executor.BaseExecutor
        protected List<BatchResult> doFlushStatements(boolean isRollback) throws SQLException {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override // org.apache.ibatis.executor.BaseExecutor
        protected <E> List<E> doQuery(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override // org.apache.ibatis.executor.BaseExecutor
        protected <E> Cursor<E> doQueryCursor(MappedStatement ms, Object parameter, RowBounds rowBounds, BoundSql boundSql) throws SQLException {
            throw new UnsupportedOperationException("Not supported.");
        }
    }
}
