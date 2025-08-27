package ch.qos.logback.core.db.dialect;

/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/db/dialect/MsSQLDialect.class */
public class MsSQLDialect implements SQLDialect {
    public static final String SELECT_CURRVAL = "SELECT @@identity id";

    @Override // ch.qos.logback.core.db.dialect.SQLDialect
    public String getSelectInsertId() {
        return "SELECT @@identity id";
    }
}
