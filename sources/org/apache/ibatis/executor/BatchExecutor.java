package org.apache.ibatis.executor;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.transaction.Transaction;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/executor/BatchExecutor.class */
public class BatchExecutor extends BaseExecutor {
    public static final int BATCH_UPDATE_RETURN_VALUE = -2147482646;
    private final List<Statement> statementList;
    private final List<BatchResult> batchResultList;
    private String currentSql;
    private MappedStatement currentStatement;

    public BatchExecutor(Configuration configuration, Transaction transaction) {
        super(configuration, transaction);
        this.statementList = new ArrayList();
        this.batchResultList = new ArrayList();
    }

    @Override // org.apache.ibatis.executor.BaseExecutor
    public int doUpdate(MappedStatement ms, Object parameterObject) throws SQLException {
        Statement stmt;
        Configuration configuration = ms.getConfiguration();
        StatementHandler handler = configuration.newStatementHandler(this, ms, parameterObject, RowBounds.DEFAULT, null, null);
        BoundSql boundSql = handler.getBoundSql();
        String sql = boundSql.getSql();
        if (sql.equals(this.currentSql) && ms.equals(this.currentStatement)) {
            int last = this.statementList.size() - 1;
            stmt = this.statementList.get(last);
            applyTransactionTimeout(stmt);
            handler.parameterize(stmt);
            BatchResult batchResult = this.batchResultList.get(last);
            batchResult.addParameterObject(parameterObject);
        } else {
            Connection connection = getConnection(ms.getStatementLog());
            stmt = handler.prepare(connection, this.transaction.getTimeout());
            handler.parameterize(stmt);
            this.currentSql = sql;
            this.currentStatement = ms;
            this.statementList.add(stmt);
            this.batchResultList.add(new BatchResult(ms, sql, parameterObject));
        }
        handler.batch(stmt);
        return BATCH_UPDATE_RETURN_VALUE;
    }

    @Override // org.apache.ibatis.executor.BaseExecutor
    public <E> List<E> doQuery(MappedStatement ms, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        Statement stmt = null;
        try {
            flushStatements();
            Configuration configuration = ms.getConfiguration();
            StatementHandler handler = configuration.newStatementHandler(this.wrapper, ms, parameterObject, rowBounds, resultHandler, boundSql);
            Connection connection = getConnection(ms.getStatementLog());
            stmt = handler.prepare(connection, this.transaction.getTimeout());
            handler.parameterize(stmt);
            List<E> listQuery = handler.query(stmt, resultHandler);
            closeStatement(stmt);
            return listQuery;
        } catch (Throwable th) {
            closeStatement(stmt);
            throw th;
        }
    }

    @Override // org.apache.ibatis.executor.BaseExecutor
    protected <E> Cursor<E> doQueryCursor(MappedStatement ms, Object parameter, RowBounds rowBounds, BoundSql boundSql) throws SQLException {
        flushStatements();
        Configuration configuration = ms.getConfiguration();
        StatementHandler handler = configuration.newStatementHandler(this.wrapper, ms, parameter, rowBounds, null, boundSql);
        Connection connection = getConnection(ms.getStatementLog());
        Statement stmt = handler.prepare(connection, this.transaction.getTimeout());
        handler.parameterize(stmt);
        return handler.queryCursor(stmt);
    }

    @Override // org.apache.ibatis.executor.BaseExecutor
    public List<BatchResult> doFlushStatements(boolean isRollback) throws SQLException {
        try {
            List<BatchResult> results = new ArrayList<>();
            if (isRollback) {
                List<BatchResult> listEmptyList = Collections.emptyList();
                for (Statement stmt : this.statementList) {
                    closeStatement(stmt);
                }
                this.currentSql = null;
                this.statementList.clear();
                this.batchResultList.clear();
                return listEmptyList;
            }
            int n = this.statementList.size();
            for (int i = 0; i < n; i++) {
                Statement stmt2 = this.statementList.get(i);
                applyTransactionTimeout(stmt2);
                BatchResult batchResult = this.batchResultList.get(i);
                try {
                    batchResult.setUpdateCounts(stmt2.executeBatch());
                    MappedStatement ms = batchResult.getMappedStatement();
                    List<Object> parameterObjects = batchResult.getParameterObjects();
                    KeyGenerator keyGenerator = ms.getKeyGenerator();
                    if (Jdbc3KeyGenerator.class.equals(keyGenerator.getClass())) {
                        Jdbc3KeyGenerator jdbc3KeyGenerator = (Jdbc3KeyGenerator) keyGenerator;
                        jdbc3KeyGenerator.processBatch(ms, stmt2, parameterObjects);
                    } else if (!NoKeyGenerator.class.equals(keyGenerator.getClass())) {
                        for (Object parameter : parameterObjects) {
                            keyGenerator.processAfter(this, ms, stmt2, parameter);
                        }
                    }
                    closeStatement(stmt2);
                    results.add(batchResult);
                } catch (BatchUpdateException e) {
                    StringBuilder message = new StringBuilder();
                    message.append(batchResult.getMappedStatement().getId()).append(" (batch index #").append(i + 1).append(")").append(" failed.");
                    if (i > 0) {
                        message.append(SymbolConstants.SPACE_SYMBOL).append(i).append(" prior sub executor(s) completed successfully, but will be rolled back.");
                    }
                    throw new BatchExecutorException(message.toString(), e, results, batchResult);
                }
            }
            return results;
        } finally {
            Iterator<Statement> it = this.statementList.iterator();
            while (it.hasNext()) {
                closeStatement(it.next());
            }
            this.currentSql = null;
            this.statementList.clear();
            this.batchResultList.clear();
        }
    }
}
