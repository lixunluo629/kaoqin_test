package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/ServerAffinityStrategy.class */
public class ServerAffinityStrategy extends RandomBalanceStrategy {
    public static final String AFFINITY_ORDER = "serverAffinityOrder";
    public String[] affinityOrderedServers = null;

    @Override // com.mysql.jdbc.RandomBalanceStrategy, com.mysql.jdbc.Extension
    public void init(Connection conn, Properties props) throws SQLException {
        super.init(conn, props);
        String hosts = props.getProperty(AFFINITY_ORDER);
        if (!StringUtils.isNullOrEmpty(hosts)) {
            this.affinityOrderedServers = hosts.split(",");
        }
    }

    @Override // com.mysql.jdbc.RandomBalanceStrategy, com.mysql.jdbc.BalanceStrategy
    public ConnectionImpl pickConnection(LoadBalancedConnectionProxy proxy, List<String> configuredHosts, Map<String, ConnectionImpl> liveConnections, long[] responseTimes, int numRetries) throws SQLException {
        if (this.affinityOrderedServers == null) {
            return super.pickConnection(proxy, configuredHosts, liveConnections, responseTimes, numRetries);
        }
        Map<String, Long> blackList = proxy.getGlobalBlacklist();
        String[] arr$ = this.affinityOrderedServers;
        for (String host : arr$) {
            if (configuredHosts.contains(host) && !blackList.containsKey(host)) {
                ConnectionImpl conn = liveConnections.get(host);
                if (conn != null) {
                    return conn;
                }
                try {
                    return proxy.createConnectionForHost(host);
                } catch (SQLException sqlEx) {
                    if (proxy.shouldExceptionTriggerConnectionSwitch(sqlEx)) {
                        proxy.addToGlobalBlacklist(host);
                    }
                }
            }
        }
        return super.pickConnection(proxy, configuredHosts, liveConnections, responseTimes, numRetries);
    }
}
