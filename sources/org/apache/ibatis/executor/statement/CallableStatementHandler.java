package org.apache.ibatis.executor.statement;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.JdbcType;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/executor/statement/CallableStatementHandler.class */
public class CallableStatementHandler extends BaseStatementHandler {
    public CallableStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        super(executor, mappedStatement, parameter, rowBounds, resultHandler, boundSql);
    }

    @Override // org.apache.ibatis.executor.statement.StatementHandler
    public int update(Statement statement) throws SQLException {
        CallableStatement cs = (CallableStatement) statement;
        cs.execute();
        int rows = cs.getUpdateCount();
        Object parameterObject = this.boundSql.getParameterObject();
        KeyGenerator keyGenerator = this.mappedStatement.getKeyGenerator();
        keyGenerator.processAfter(this.executor, this.mappedStatement, cs, parameterObject);
        this.resultSetHandler.handleOutputParameters(cs);
        return rows;
    }

    @Override // org.apache.ibatis.executor.statement.StatementHandler
    public void batch(Statement statement) throws SQLException {
        CallableStatement cs = (CallableStatement) statement;
        cs.addBatch();
    }

    @Override // org.apache.ibatis.executor.statement.StatementHandler
    public <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException {
        CallableStatement cs = (CallableStatement) statement;
        cs.execute();
        List<E> resultList = this.resultSetHandler.handleResultSets(cs);
        this.resultSetHandler.handleOutputParameters(cs);
        return resultList;
    }

    @Override // org.apache.ibatis.executor.statement.StatementHandler
    public <E> Cursor<E> queryCursor(Statement statement) throws SQLException {
        CallableStatement cs = (CallableStatement) statement;
        cs.execute();
        Cursor<E> resultList = this.resultSetHandler.handleCursorResultSets(cs);
        this.resultSetHandler.handleOutputParameters(cs);
        return resultList;
    }

    @Override // org.apache.ibatis.executor.statement.BaseStatementHandler
    protected Statement instantiateStatement(Connection connection) throws SQLException {
        String sql = this.boundSql.getSql();
        if (this.mappedStatement.getResultSetType() != null) {
            return connection.prepareCall(sql, this.mappedStatement.getResultSetType().getValue(), 1007);
        }
        return connection.prepareCall(sql);
    }

    @Override // org.apache.ibatis.executor.statement.StatementHandler
    public void parameterize(Statement statement) throws SQLException {
        registerOutputParameters((CallableStatement) statement);
        this.parameterHandler.setParameters((CallableStatement) statement);
    }

    private void registerOutputParameters(CallableStatement cs) throws SQLException {
        List<ParameterMapping> parameterMappings = this.boundSql.getParameterMappings();
        int n = parameterMappings.size();
        for (int i = 0; i < n; i++) {
            ParameterMapping parameterMapping = parameterMappings.get(i);
            if (parameterMapping.getMode() == ParameterMode.OUT || parameterMapping.getMode() == ParameterMode.INOUT) {
                if (null == parameterMapping.getJdbcType()) {
                    throw new ExecutorException("The JDBC Type must be specified for output parameter.  Parameter: " + parameterMapping.getProperty());
                }
                if (parameterMapping.getNumericScale() != null && (parameterMapping.getJdbcType() == JdbcType.NUMERIC || parameterMapping.getJdbcType() == JdbcType.DECIMAL)) {
                    cs.registerOutParameter(i + 1, parameterMapping.getJdbcType().TYPE_CODE, parameterMapping.getNumericScale().intValue());
                } else if (parameterMapping.getJdbcTypeName() == null) {
                    cs.registerOutParameter(i + 1, parameterMapping.getJdbcType().TYPE_CODE);
                } else {
                    cs.registerOutParameter(i + 1, parameterMapping.getJdbcType().TYPE_CODE, parameterMapping.getJdbcTypeName());
                }
            }
        }
    }
}
