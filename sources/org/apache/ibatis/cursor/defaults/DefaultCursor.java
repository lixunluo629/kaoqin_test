package org.apache.ibatis.cursor.defaults;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetWrapper;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/cursor/defaults/DefaultCursor.class */
public class DefaultCursor<T> implements Cursor<T> {
    private final DefaultResultSetHandler resultSetHandler;
    private final ResultMap resultMap;
    private final ResultSetWrapper rsw;
    private final RowBounds rowBounds;
    private boolean iteratorRetrieved;
    private final ObjectWrapperResultHandler<T> objectWrapperResultHandler = new ObjectWrapperResultHandler<>();
    private final DefaultCursor<T>.CursorIterator cursorIterator = new CursorIterator();
    private CursorStatus status = CursorStatus.CREATED;
    private int indexWithRowBound = -1;

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/cursor/defaults/DefaultCursor$CursorStatus.class */
    private enum CursorStatus {
        CREATED,
        OPEN,
        CLOSED,
        CONSUMED
    }

    public DefaultCursor(DefaultResultSetHandler resultSetHandler, ResultMap resultMap, ResultSetWrapper rsw, RowBounds rowBounds) {
        this.resultSetHandler = resultSetHandler;
        this.resultMap = resultMap;
        this.rsw = rsw;
        this.rowBounds = rowBounds;
    }

    @Override // org.apache.ibatis.cursor.Cursor
    public boolean isOpen() {
        return this.status == CursorStatus.OPEN;
    }

    @Override // org.apache.ibatis.cursor.Cursor
    public boolean isConsumed() {
        return this.status == CursorStatus.CONSUMED;
    }

    @Override // org.apache.ibatis.cursor.Cursor
    public int getCurrentIndex() {
        return this.rowBounds.getOffset() + this.cursorIterator.iteratorIndex;
    }

    @Override // java.lang.Iterable
    public Iterator<T> iterator() {
        if (this.iteratorRetrieved) {
            throw new IllegalStateException("Cannot open more than one iterator on a Cursor");
        }
        this.iteratorRetrieved = true;
        return this.cursorIterator;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws SQLException {
        if (isClosed()) {
            return;
        }
        ResultSet rs = this.rsw.getResultSet();
        if (rs != null) {
            try {
                Statement statement = rs.getStatement();
                rs.close();
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                return;
            }
        }
        this.status = CursorStatus.CLOSED;
    }

    protected T fetchNextUsingRowBound() throws SQLException {
        T result;
        T tFetchNextObjectFromDatabase = fetchNextObjectFromDatabase();
        while (true) {
            result = tFetchNextObjectFromDatabase;
            if (result == null || this.indexWithRowBound >= this.rowBounds.getOffset()) {
                break;
            }
            tFetchNextObjectFromDatabase = fetchNextObjectFromDatabase();
        }
        return result;
    }

    protected T fetchNextObjectFromDatabase() throws SQLException {
        if (isClosed()) {
            return null;
        }
        try {
            this.status = CursorStatus.OPEN;
            this.resultSetHandler.handleRowValues(this.rsw, this.resultMap, this.objectWrapperResultHandler, RowBounds.DEFAULT, null);
            T t = (T) ((ObjectWrapperResultHandler) this.objectWrapperResultHandler).result;
            if (t != null) {
                this.indexWithRowBound++;
            }
            if (t == null || getReadItemsCount() == this.rowBounds.getOffset() + this.rowBounds.getLimit()) {
                close();
                this.status = CursorStatus.CONSUMED;
            }
            ((ObjectWrapperResultHandler) this.objectWrapperResultHandler).result = null;
            return t;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isClosed() {
        return this.status == CursorStatus.CLOSED || this.status == CursorStatus.CONSUMED;
    }

    private int getReadItemsCount() {
        return this.indexWithRowBound + 1;
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/cursor/defaults/DefaultCursor$ObjectWrapperResultHandler.class */
    private static class ObjectWrapperResultHandler<T> implements ResultHandler<T> {
        private T result;

        private ObjectWrapperResultHandler() {
        }

        @Override // org.apache.ibatis.session.ResultHandler
        public void handleResult(ResultContext<? extends T> context) {
            this.result = context.getResultObject();
            context.stop();
        }
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/cursor/defaults/DefaultCursor$CursorIterator.class */
    private class CursorIterator implements Iterator<T> {
        T object;
        int iteratorIndex;

        private CursorIterator() {
            this.iteratorIndex = -1;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.object == null) {
                this.object = (T) DefaultCursor.this.fetchNextUsingRowBound();
            }
            return this.object != null;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Iterator
        public T next() throws SQLException {
            T next = this.object;
            if (next == null) {
                next = DefaultCursor.this.fetchNextUsingRowBound();
            }
            if (next != null) {
                this.object = null;
                this.iteratorIndex++;
                return next;
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Cannot remove element from Cursor");
        }
    }
}
