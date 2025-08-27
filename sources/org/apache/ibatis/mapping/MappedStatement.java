package org.apache.ibatis.mapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/mapping/MappedStatement.class */
public final class MappedStatement {
    private String resource;
    private Configuration configuration;
    private String id;
    private Integer fetchSize;
    private Integer timeout;
    private StatementType statementType;
    private ResultSetType resultSetType;
    private SqlSource sqlSource;
    private Cache cache;
    private ParameterMap parameterMap;
    private List<ResultMap> resultMaps;
    private boolean flushCacheRequired;
    private boolean useCache;
    private boolean resultOrdered;
    private SqlCommandType sqlCommandType;
    private KeyGenerator keyGenerator;
    private String[] keyProperties;
    private String[] keyColumns;
    private boolean hasNestedResultMaps;
    private String databaseId;
    private Log statementLog;
    private LanguageDriver lang;
    private String[] resultSets;

    MappedStatement() {
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/mapping/MappedStatement$Builder.class */
    public static class Builder {
        private MappedStatement mappedStatement = new MappedStatement();
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !MappedStatement.class.desiredAssertionStatus();
        }

        public Builder(Configuration configuration, String id, SqlSource sqlSource, SqlCommandType sqlCommandType) {
            this.mappedStatement.configuration = configuration;
            this.mappedStatement.id = id;
            this.mappedStatement.sqlSource = sqlSource;
            this.mappedStatement.statementType = StatementType.PREPARED;
            this.mappedStatement.parameterMap = new ParameterMap.Builder(configuration, "defaultParameterMap", null, new ArrayList()).build();
            this.mappedStatement.resultMaps = new ArrayList();
            this.mappedStatement.sqlCommandType = sqlCommandType;
            this.mappedStatement.keyGenerator = (configuration.isUseGeneratedKeys() && SqlCommandType.INSERT.equals(sqlCommandType)) ? Jdbc3KeyGenerator.INSTANCE : NoKeyGenerator.INSTANCE;
            String logId = id;
            this.mappedStatement.statementLog = LogFactory.getLog(configuration.getLogPrefix() != null ? configuration.getLogPrefix() + id : logId);
            this.mappedStatement.lang = configuration.getDefaultScriptingLanguageInstance();
        }

        public Builder resource(String resource) {
            this.mappedStatement.resource = resource;
            return this;
        }

        public String id() {
            return this.mappedStatement.id;
        }

        public Builder parameterMap(ParameterMap parameterMap) {
            this.mappedStatement.parameterMap = parameterMap;
            return this;
        }

        public Builder resultMaps(List<ResultMap> resultMaps) {
            this.mappedStatement.resultMaps = resultMaps;
            for (ResultMap resultMap : resultMaps) {
                this.mappedStatement.hasNestedResultMaps = this.mappedStatement.hasNestedResultMaps || resultMap.hasNestedResultMaps();
            }
            return this;
        }

        public Builder fetchSize(Integer fetchSize) {
            this.mappedStatement.fetchSize = fetchSize;
            return this;
        }

        public Builder timeout(Integer timeout) {
            this.mappedStatement.timeout = timeout;
            return this;
        }

        public Builder statementType(StatementType statementType) {
            this.mappedStatement.statementType = statementType;
            return this;
        }

        public Builder resultSetType(ResultSetType resultSetType) {
            this.mappedStatement.resultSetType = resultSetType;
            return this;
        }

        public Builder cache(Cache cache) {
            this.mappedStatement.cache = cache;
            return this;
        }

        public Builder flushCacheRequired(boolean flushCacheRequired) {
            this.mappedStatement.flushCacheRequired = flushCacheRequired;
            return this;
        }

        public Builder useCache(boolean useCache) {
            this.mappedStatement.useCache = useCache;
            return this;
        }

        public Builder resultOrdered(boolean resultOrdered) {
            this.mappedStatement.resultOrdered = resultOrdered;
            return this;
        }

        public Builder keyGenerator(KeyGenerator keyGenerator) {
            this.mappedStatement.keyGenerator = keyGenerator;
            return this;
        }

        public Builder keyProperty(String keyProperty) {
            this.mappedStatement.keyProperties = MappedStatement.delimitedStringToArray(keyProperty);
            return this;
        }

        public Builder keyColumn(String keyColumn) {
            this.mappedStatement.keyColumns = MappedStatement.delimitedStringToArray(keyColumn);
            return this;
        }

        public Builder databaseId(String databaseId) {
            this.mappedStatement.databaseId = databaseId;
            return this;
        }

        public Builder lang(LanguageDriver driver) {
            this.mappedStatement.lang = driver;
            return this;
        }

        public Builder resultSets(String resultSet) {
            this.mappedStatement.resultSets = MappedStatement.delimitedStringToArray(resultSet);
            return this;
        }

        @Deprecated
        public Builder resulSets(String resultSet) {
            this.mappedStatement.resultSets = MappedStatement.delimitedStringToArray(resultSet);
            return this;
        }

        public MappedStatement build() {
            if (!$assertionsDisabled && this.mappedStatement.configuration == null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && this.mappedStatement.id == null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && this.mappedStatement.sqlSource == null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && this.mappedStatement.lang == null) {
                throw new AssertionError();
            }
            this.mappedStatement.resultMaps = Collections.unmodifiableList(this.mappedStatement.resultMaps);
            return this.mappedStatement;
        }
    }

    public KeyGenerator getKeyGenerator() {
        return this.keyGenerator;
    }

    public SqlCommandType getSqlCommandType() {
        return this.sqlCommandType;
    }

    public String getResource() {
        return this.resource;
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

    public String getId() {
        return this.id;
    }

    public boolean hasNestedResultMaps() {
        return this.hasNestedResultMaps;
    }

    public Integer getFetchSize() {
        return this.fetchSize;
    }

    public Integer getTimeout() {
        return this.timeout;
    }

    public StatementType getStatementType() {
        return this.statementType;
    }

    public ResultSetType getResultSetType() {
        return this.resultSetType;
    }

    public SqlSource getSqlSource() {
        return this.sqlSource;
    }

    public ParameterMap getParameterMap() {
        return this.parameterMap;
    }

    public List<ResultMap> getResultMaps() {
        return this.resultMaps;
    }

    public Cache getCache() {
        return this.cache;
    }

    public boolean isFlushCacheRequired() {
        return this.flushCacheRequired;
    }

    public boolean isUseCache() {
        return this.useCache;
    }

    public boolean isResultOrdered() {
        return this.resultOrdered;
    }

    public String getDatabaseId() {
        return this.databaseId;
    }

    public String[] getKeyProperties() {
        return this.keyProperties;
    }

    public String[] getKeyColumns() {
        return this.keyColumns;
    }

    public Log getStatementLog() {
        return this.statementLog;
    }

    public LanguageDriver getLang() {
        return this.lang;
    }

    public String[] getResultSets() {
        return this.resultSets;
    }

    @Deprecated
    public String[] getResulSets() {
        return this.resultSets;
    }

    public BoundSql getBoundSql(Object parameterObject) {
        ResultMap rm;
        BoundSql boundSql = this.sqlSource.getBoundSql(parameterObject);
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (parameterMappings == null || parameterMappings.isEmpty()) {
            boundSql = new BoundSql(this.configuration, boundSql.getSql(), this.parameterMap.getParameterMappings(), parameterObject);
        }
        for (ParameterMapping pm : boundSql.getParameterMappings()) {
            String rmId = pm.getResultMapId();
            if (rmId != null && (rm = this.configuration.getResultMap(rmId)) != null) {
                this.hasNestedResultMaps |= rm.hasNestedResultMaps();
            }
        }
        return boundSql;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String[] delimitedStringToArray(String in) {
        if (in == null || in.trim().length() == 0) {
            return null;
        }
        return in.split(",");
    }
}
