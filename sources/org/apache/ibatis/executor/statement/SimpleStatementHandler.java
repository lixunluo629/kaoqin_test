package org.apache.ibatis.executor.statement;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/executor/statement/SimpleStatementHandler.class */
public class SimpleStatementHandler extends BaseStatementHandler {
    public SimpleStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        super(executor, mappedStatement, parameter, rowBounds, resultHandler, boundSql);
    }

    @Override // org.apache.ibatis.executor.statement.StatementHandler
    public int update(Statement statement) throws SQLException {
        int rows;
        String sql = this.boundSql.getSql();
        Object parameterObject = this.boundSql.getParameterObject();
        KeyGenerator keyGenerator = this.mappedStatement.getKeyGenerator();
        if (keyGenerator instanceof Jdbc3KeyGenerator) {
            statement.execute(sql, 1);
            rows = statement.getUpdateCount();
            keyGenerator.processAfter(this.executor, this.mappedStatement, statement, parameterObject);
        } else if (keyGenerator instanceof SelectKeyGenerator) {
            statement.execute(sql);
            rows = statement.getUpdateCount();
            keyGenerator.processAfter(this.executor, this.mappedStatement, statement, parameterObject);
        } else {
            statement.execute(sql);
            rows = statement.getUpdateCount();
        }
        return rows;
    }

    @Override // org.apache.ibatis.executor.statement.StatementHandler
    public void batch(Statement statement) throws SQLException {
        String sql = this.boundSql.getSql();
        statement.addBatch(sql);
    }

    @Override // org.apache.ibatis.executor.statement.StatementHandler
    public <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException {
        String sql = this.boundSql.getSql();
        statement.execute(sql);
        return this.resultSetHandler.handleResultSets(statement);
    }

    @Override // org.apache.ibatis.executor.statement.StatementHandler
    public <E> Cursor<E> queryCursor(Statement statement) throws SQLException {
        String sql = this.boundSql.getSql();
        statement.execute(sql);
        return this.resultSetHandler.handleCursorResultSets(statement);
    }

    @Override // org.apache.ibatis.executor.statement.BaseStatementHandler
    protected Statement instantiateStatement(Connection connection) throws SQLException {
        if (this.mappedStatement.getResultSetType() != null) {
            return connection.createStatement(this.mappedStatement.getResultSetType().getValue(), 1007);
        }
        return connection.createStatement();
    }

    @Override // org.apache.ibatis.executor.statement.StatementHandler
    public void parameterize(Statement statement) throws SQLException {
    }
}
