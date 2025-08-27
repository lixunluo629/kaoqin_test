package org.springframework.jdbc.object;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/object/BatchSqlUpdate.class */
public class BatchSqlUpdate extends SqlUpdate {
    public static final int DEFAULT_BATCH_SIZE = 5000;
    private int batchSize;
    private boolean trackRowsAffected;
    private final LinkedList<Object[]> parameterQueue;
    private final List<Integer> rowsAffected;

    public BatchSqlUpdate() {
        this.batchSize = 5000;
        this.trackRowsAffected = true;
        this.parameterQueue = new LinkedList<>();
        this.rowsAffected = new ArrayList();
    }

    public BatchSqlUpdate(DataSource ds, String sql) {
        super(ds, sql);
        this.batchSize = 5000;
        this.trackRowsAffected = true;
        this.parameterQueue = new LinkedList<>();
        this.rowsAffected = new ArrayList();
    }

    public BatchSqlUpdate(DataSource ds, String sql, int[] types) {
        super(ds, sql, types);
        this.batchSize = 5000;
        this.trackRowsAffected = true;
        this.parameterQueue = new LinkedList<>();
        this.rowsAffected = new ArrayList();
    }

    public BatchSqlUpdate(DataSource ds, String sql, int[] types, int batchSize) {
        super(ds, sql, types);
        this.batchSize = 5000;
        this.trackRowsAffected = true;
        this.parameterQueue = new LinkedList<>();
        this.rowsAffected = new ArrayList();
        setBatchSize(batchSize);
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public void setTrackRowsAffected(boolean trackRowsAffected) {
        this.trackRowsAffected = trackRowsAffected;
    }

    @Override // org.springframework.jdbc.object.RdbmsOperation
    protected boolean supportsLobParameters() {
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.springframework.jdbc.object.SqlUpdate
    public int update(Object... params) throws DataAccessException {
        validateParameters(params);
        this.parameterQueue.add(params.clone());
        if (this.parameterQueue.size() == this.batchSize) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Triggering auto-flush because queue reached batch size of " + this.batchSize);
            }
            flush();
            return -1;
        }
        return -1;
    }

    public int[] flush() throws DataAccessException {
        if (this.parameterQueue.isEmpty()) {
            return new int[0];
        }
        int[] rowsAffected = getJdbcTemplate().batchUpdate(getSql(), new BatchPreparedStatementSetter() { // from class: org.springframework.jdbc.object.BatchSqlUpdate.1
            @Override // org.springframework.jdbc.core.BatchPreparedStatementSetter
            public int getBatchSize() {
                return BatchSqlUpdate.this.parameterQueue.size();
            }

            @Override // org.springframework.jdbc.core.BatchPreparedStatementSetter
            public void setValues(PreparedStatement ps, int index) throws SQLException {
                Object[] params = (Object[]) BatchSqlUpdate.this.parameterQueue.removeFirst();
                BatchSqlUpdate.this.newPreparedStatementSetter(params).setValues(ps);
            }
        });
        for (int rowCount : rowsAffected) {
            checkRowsAffected(rowCount);
            if (this.trackRowsAffected) {
                this.rowsAffected.add(Integer.valueOf(rowCount));
            }
        }
        return rowsAffected;
    }

    public int getQueueCount() {
        return this.parameterQueue.size();
    }

    public int getExecutionCount() {
        return this.rowsAffected.size();
    }

    public int[] getRowsAffected() {
        int[] result = new int[this.rowsAffected.size()];
        int i = 0;
        Iterator<Integer> it = this.rowsAffected.iterator();
        while (it.hasNext()) {
            result[i] = it.next().intValue();
            i++;
        }
        return result;
    }

    public void reset() {
        this.parameterQueue.clear();
        this.rowsAffected.clear();
    }
}
