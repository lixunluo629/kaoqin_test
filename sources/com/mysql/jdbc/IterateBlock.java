package com.mysql.jdbc;

import com.mysql.jdbc.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Iterator;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/IterateBlock.class */
public abstract class IterateBlock<T> {
    DatabaseMetaData.IteratorWithCleanup<T> iteratorWithCleanup;
    Iterator<T> javaIterator;
    boolean stopIterating;

    abstract void forEach(T t) throws SQLException;

    IterateBlock(DatabaseMetaData.IteratorWithCleanup<T> i) {
        this.stopIterating = false;
        this.iteratorWithCleanup = i;
        this.javaIterator = null;
    }

    IterateBlock(Iterator<T> i) {
        this.stopIterating = false;
        this.javaIterator = i;
        this.iteratorWithCleanup = null;
    }

    public void doForAll() throws SQLException {
        if (this.iteratorWithCleanup != null) {
            while (this.iteratorWithCleanup.hasNext()) {
                try {
                    forEach(this.iteratorWithCleanup.next());
                    if (this.stopIterating) {
                        break;
                    }
                } finally {
                    this.iteratorWithCleanup.close();
                }
            }
            return;
        }
        while (this.javaIterator.hasNext()) {
            forEach(this.javaIterator.next());
            if (this.stopIterating) {
                return;
            }
        }
    }

    public final boolean fullIteration() {
        return !this.stopIterating;
    }
}
