package org.apache.ibatis.executor.statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/executor/statement/PreparedStatementHandler.class */
public class PreparedStatementHandler extends BaseStatementHandler {
    public PreparedStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        super(executor, mappedStatement, parameter, rowBounds, resultHandler, boundSql);
    }

    @Override // org.apache.ibatis.executor.statement.StatementHandler
    public int update(Statement statement) throws SQLException {
        PreparedStatement ps = (PreparedStatement) statement;
        ps.execute();
        int rows = ps.getUpdateCount();
        Object parameterObject = this.boundSql.getParameterObject();
        KeyGenerator keyGenerator = this.mappedStatement.getKeyGenerator();
        keyGenerator.processAfter(this.executor, this.mappedStatement, ps, parameterObject);
        return rows;
    }

    @Override // org.apache.ibatis.executor.statement.StatementHandler
    public void batch(Statement statement) throws SQLException {
        PreparedStatement ps = (PreparedStatement) statement;
        ps.addBatch();
    }

    @Override // org.apache.ibatis.executor.statement.StatementHandler
    public <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException {
        PreparedStatement ps = (PreparedStatement) statement;
        ps.execute();
        return this.resultSetHandler.handleResultSets(ps);
    }

    @Override // org.apache.ibatis.executor.statement.StatementHandler
    public <E> Cursor<E> queryCursor(Statement statement) throws SQLException {
        PreparedStatement ps = (PreparedStatement) statement;
        ps.execute();
        return this.resultSetHandler.handleCursorResultSets(ps);
    }

    @Override // org.apache.ibatis.executor.statement.BaseStatementHandler
    protected Statement instantiateStatement(Connection connection) throws SQLException {
        String sql = this.boundSql.getSql();
        if (this.mappedStatement.getKeyGenerator() instanceof Jdbc3KeyGenerator) {
            String[] keyColumnNames = this.mappedStatement.getKeyColumns();
            if (keyColumnNames == null) {
                return connection.prepareStatement(sql, 1);
            }
            return connection.prepareStatement(sql, keyColumnNames);
        }
        if (this.mappedStatement.getResultSetType() != null) {
            return connection.prepareStatement(sql, this.mappedStatement.getResultSetType().getValue(), 1007);
        }
        return connection.prepareStatement(sql);
    }

    @Override // org.apache.ibatis.executor.statement.StatementHandler
    public void parameterize(Statement statement) throws SQLException {
        this.parameterHandler.setParameters((PreparedStatement) statement);
    }
}
