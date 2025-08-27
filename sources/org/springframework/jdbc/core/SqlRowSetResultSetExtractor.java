package org.springframework.jdbc.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.lang.UsesJava7;
import org.springframework.util.ClassUtils;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/SqlRowSetResultSetExtractor.class */
public class SqlRowSetResultSetExtractor implements ResultSetExtractor<SqlRowSet> {
    private static final CachedRowSetFactory cachedRowSetFactory;

    /* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/SqlRowSetResultSetExtractor$CachedRowSetFactory.class */
    private interface CachedRowSetFactory {
        CachedRowSet createCachedRowSet() throws SQLException;
    }

    static {
        if (ClassUtils.isPresent("javax.sql.rowset.RowSetProvider", SqlRowSetResultSetExtractor.class.getClassLoader())) {
            cachedRowSetFactory = new StandardCachedRowSetFactory();
        } else {
            cachedRowSetFactory = new SunCachedRowSetFactory();
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.jdbc.core.ResultSetExtractor
    public SqlRowSet extractData(ResultSet rs) throws SQLException {
        return createSqlRowSet(rs);
    }

    protected SqlRowSet createSqlRowSet(ResultSet rs) throws SQLException {
        CachedRowSet rowSet = newCachedRowSet();
        rowSet.populate(rs);
        return new ResultSetWrappingSqlRowSet(rowSet);
    }

    protected CachedRowSet newCachedRowSet() throws SQLException {
        return cachedRowSetFactory.createCachedRowSet();
    }

    @UsesJava7
    /* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/SqlRowSetResultSetExtractor$StandardCachedRowSetFactory.class */
    private static class StandardCachedRowSetFactory implements CachedRowSetFactory {
        private final RowSetFactory rowSetFactory;

        public StandardCachedRowSetFactory() {
            try {
                this.rowSetFactory = RowSetProvider.newFactory();
            } catch (SQLException ex) {
                throw new IllegalStateException("Cannot create RowSetFactory through RowSetProvider", ex);
            }
        }

        @Override // org.springframework.jdbc.core.SqlRowSetResultSetExtractor.CachedRowSetFactory
        public CachedRowSet createCachedRowSet() throws SQLException {
            return this.rowSetFactory.createCachedRowSet();
        }
    }

    /* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/SqlRowSetResultSetExtractor$SunCachedRowSetFactory.class */
    private static class SunCachedRowSetFactory implements CachedRowSetFactory {
        private static final Class<?> implementationClass;

        private SunCachedRowSetFactory() {
        }

        static {
            try {
                implementationClass = ClassUtils.forName("com.sun.rowset.CachedRowSetImpl", SqlRowSetResultSetExtractor.class.getClassLoader());
            } catch (Throwable ex) {
                throw new IllegalStateException(ex);
            }
        }

        @Override // org.springframework.jdbc.core.SqlRowSetResultSetExtractor.CachedRowSetFactory
        public CachedRowSet createCachedRowSet() throws SQLException {
            try {
                return (CachedRowSet) implementationClass.newInstance();
            } catch (Throwable ex) {
                throw new IllegalStateException(ex);
            }
        }
    }
}
