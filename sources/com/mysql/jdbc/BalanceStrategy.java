package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/BalanceStrategy.class */
public interface BalanceStrategy extends Extension {
    ConnectionImpl pickConnection(LoadBalancedConnectionProxy loadBalancedConnectionProxy, List<String> list, Map<String, ConnectionImpl> map, long[] jArr, int i) throws SQLException;
}
