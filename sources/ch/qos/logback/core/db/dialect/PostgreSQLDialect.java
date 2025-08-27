package ch.qos.logback.core.db.dialect;

/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/db/dialect/PostgreSQLDialect.class */
public class PostgreSQLDialect implements SQLDialect {
    public static final String SELECT_CURRVAL = "SELECT currval('logging_event_id_seq')";

    @Override // ch.qos.logback.core.db.dialect.SQLDialect
    public String getSelectInsertId() {
        return SELECT_CURRVAL;
    }
}
