package org.apache.ibatis.executor.statement;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/executor/statement/RoutingStatementHandler.class */
public class RoutingStatementHandler implements StatementHandler {
    private final StatementHandler delegate;

    public RoutingStatementHandler(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        switch (ms.getStatementType()) {
            case STATEMENT:
                this.delegate = new SimpleStatementHandler(executor, ms, parameter, rowBounds, resultHandler, boundSql);
                return;
            case PREPARED:
                this.delegate = new PreparedStatementHandler(executor, ms, parameter, rowBounds, resultHandler, boundSql);
                return;
            case CALLABLE:
                this.delegate = new CallableStatementHandler(executor, ms, parameter, rowBounds, resultHandler, boundSql);
                return;
            default:
                throw new ExecutorException("Unknown statement type: " + ms.getStatementType());
        }
    }

    @Override // org.apache.ibatis.executor.statement.StatementHandler
    public Statement prepare(Connection connection, Integer transactionTimeout) throws SQLException {
        return this.delegate.prepare(connection, transactionTimeout);
    }

    @Override // org.apache.ibatis.executor.statement.StatementHandler
    public void parameterize(Statement statement) throws SQLException {
        this.delegate.parameterize(statement);
    }

    @Override // org.apache.ibatis.executor.statement.StatementHandler
    public void batch(Statement statement) throws SQLException {
        this.delegate.batch(statement);
    }

    @Override // org.apache.ibatis.executor.statement.StatementHandler
    public int update(Statement statement) throws SQLException {
        return this.delegate.update(statement);
    }

    @Override // org.apache.ibatis.executor.statement.StatementHandler
    public <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException {
        return this.delegate.query(statement, resultHandler);
    }

    @Override // org.apache.ibatis.executor.statement.StatementHandler
    public <E> Cursor<E> queryCursor(Statement statement) throws SQLException {
        return this.delegate.queryCursor(statement);
    }

    @Override // org.apache.ibatis.executor.statement.StatementHandler
    public BoundSql getBoundSql() {
        return this.delegate.getBoundSql();
    }

    @Override // org.apache.ibatis.executor.statement.StatementHandler
    public ParameterHandler getParameterHandler() {
        return this.delegate.getParameterHandler();
    }
}
