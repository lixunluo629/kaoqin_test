package org.apache.ibatis.executor.statement;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/executor/statement/BaseStatementHandler.class */
public abstract class BaseStatementHandler implements StatementHandler {
    protected final Configuration configuration;
    protected final ObjectFactory objectFactory;
    protected final TypeHandlerRegistry typeHandlerRegistry;
    protected final ResultSetHandler resultSetHandler;
    protected final ParameterHandler parameterHandler;
    protected final Executor executor;
    protected final MappedStatement mappedStatement;
    protected final RowBounds rowBounds;
    protected BoundSql boundSql;

    protected abstract Statement instantiateStatement(Connection connection) throws SQLException;

    protected BaseStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        this.configuration = mappedStatement.getConfiguration();
        this.executor = executor;
        this.mappedStatement = mappedStatement;
        this.rowBounds = rowBounds;
        this.typeHandlerRegistry = this.configuration.getTypeHandlerRegistry();
        this.objectFactory = this.configuration.getObjectFactory();
        if (boundSql == null) {
            generateKeys(parameterObject);
            boundSql = mappedStatement.getBoundSql(parameterObject);
        }
        this.boundSql = boundSql;
        this.parameterHandler = this.configuration.newParameterHandler(mappedStatement, parameterObject, boundSql);
        this.resultSetHandler = this.configuration.newResultSetHandler(executor, mappedStatement, rowBounds, this.parameterHandler, resultHandler, boundSql);
    }

    @Override // org.apache.ibatis.executor.statement.StatementHandler
    public BoundSql getBoundSql() {
        return this.boundSql;
    }

    @Override // org.apache.ibatis.executor.statement.StatementHandler
    public ParameterHandler getParameterHandler() {
        return this.parameterHandler;
    }

    @Override // org.apache.ibatis.executor.statement.StatementHandler
    public Statement prepare(Connection connection, Integer transactionTimeout) throws SQLException {
        ErrorContext.instance().sql(this.boundSql.getSql());
        Statement statement = null;
        try {
            statement = instantiateStatement(connection);
            setStatementTimeout(statement, transactionTimeout);
            setFetchSize(statement);
            return statement;
        } catch (SQLException e) {
            closeStatement(statement);
            throw e;
        } catch (Exception e2) {
            closeStatement(statement);
            throw new ExecutorException("Error preparing statement.  Cause: " + e2, e2);
        }
    }

    protected void setStatementTimeout(Statement stmt, Integer transactionTimeout) throws SQLException {
        Integer queryTimeout = null;
        if (this.mappedStatement.getTimeout() != null) {
            queryTimeout = this.mappedStatement.getTimeout();
        } else if (this.configuration.getDefaultStatementTimeout() != null) {
            queryTimeout = this.configuration.getDefaultStatementTimeout();
        }
        if (queryTimeout != null) {
            stmt.setQueryTimeout(queryTimeout.intValue());
        }
        StatementUtil.applyTransactionTimeout(stmt, queryTimeout, transactionTimeout);
    }

    protected void setFetchSize(Statement stmt) throws SQLException {
        Integer fetchSize = this.mappedStatement.getFetchSize();
        if (fetchSize != null) {
            stmt.setFetchSize(fetchSize.intValue());
            return;
        }
        Integer defaultFetchSize = this.configuration.getDefaultFetchSize();
        if (defaultFetchSize != null) {
            stmt.setFetchSize(defaultFetchSize.intValue());
        }
    }

    protected void closeStatement(Statement statement) throws SQLException {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
            }
        }
    }

    protected void generateKeys(Object parameter) {
        KeyGenerator keyGenerator = this.mappedStatement.getKeyGenerator();
        ErrorContext.instance().store();
        keyGenerator.processBefore(this.executor, this.mappedStatement, null, parameter);
        ErrorContext.instance().recall();
    }
}
