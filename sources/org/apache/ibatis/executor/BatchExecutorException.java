package org.apache.ibatis.executor;

import java.sql.BatchUpdateException;
import java.util.List;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/executor/BatchExecutorException.class */
public class BatchExecutorException extends ExecutorException {
    private static final long serialVersionUID = 154049229650533990L;
    private final List<BatchResult> successfulBatchResults;
    private final BatchUpdateException batchUpdateException;
    private final BatchResult batchResult;

    public BatchExecutorException(String message, BatchUpdateException cause, List<BatchResult> successfulBatchResults, BatchResult batchResult) {
        super(message + " Cause: " + cause, cause);
        this.batchUpdateException = cause;
        this.successfulBatchResults = successfulBatchResults;
        this.batchResult = batchResult;
    }

    public BatchUpdateException getBatchUpdateException() {
        return this.batchUpdateException;
    }

    public List<BatchResult> getSuccessfulBatchResults() {
        return this.successfulBatchResults;
    }

    public String getFailingSqlStatement() {
        return this.batchResult.getSql();
    }

    public String getFailingStatementId() {
        return this.batchResult.getMappedStatement().getId();
    }
}
