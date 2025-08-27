package ch.qos.logback.core.db.dialect;

/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/db/dialect/H2Dialect.class */
public class H2Dialect implements SQLDialect {
    public static final String SELECT_CURRVAL = "CALL IDENTITY()";

    @Override // ch.qos.logback.core.db.dialect.SQLDialect
    public String getSelectInsertId() {
        return "CALL IDENTITY()";
    }
}
