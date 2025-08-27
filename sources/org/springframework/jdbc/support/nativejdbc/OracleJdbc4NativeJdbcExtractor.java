package org.springframework.jdbc.support.nativejdbc;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/nativejdbc/OracleJdbc4NativeJdbcExtractor.class */
public class OracleJdbc4NativeJdbcExtractor extends Jdbc4NativeJdbcExtractor {
    /* JADX WARN: Multi-variable type inference failed */
    public OracleJdbc4NativeJdbcExtractor() {
        try {
            setConnectionType(getClass().getClassLoader().loadClass("oracle.jdbc.OracleConnection"));
            setStatementType(getClass().getClassLoader().loadClass("oracle.jdbc.OracleStatement"));
            setPreparedStatementType(getClass().getClassLoader().loadClass("oracle.jdbc.OraclePreparedStatement"));
            setCallableStatementType(getClass().getClassLoader().loadClass("oracle.jdbc.OracleCallableStatement"));
            setResultSetType(getClass().getClassLoader().loadClass("oracle.jdbc.OracleResultSet"));
        } catch (Exception ex) {
            throw new IllegalStateException("Could not initialize OracleJdbc4NativeJdbcExtractor because Oracle API classes are not available: " + ex);
        }
    }
}
