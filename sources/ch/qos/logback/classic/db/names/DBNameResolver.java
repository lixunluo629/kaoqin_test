package ch.qos.logback.classic.db.names;

/* JADX WARN: Classes with same name are omitted:
  logback-classic-1.2.3.jar:ch/qos/logback/classic/db/names/DBNameResolver.class
 */
/* loaded from: logback-classic-1.2.3.jar.bak:ch/qos/logback/classic/db/names/DBNameResolver.class */
public interface DBNameResolver {
    <N extends Enum<?>> String getTableName(N n);

    <N extends Enum<?>> String getColumnName(N n);
}
