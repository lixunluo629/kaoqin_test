package com.zaxxer.hikari.pool;

import java.sql.CallableStatement;

/* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/pool/ProxyCallableStatement.class */
public abstract class ProxyCallableStatement extends ProxyPreparedStatement implements CallableStatement {
    protected ProxyCallableStatement(ProxyConnection connection, CallableStatement statement) {
        super(connection, statement);
    }
}
