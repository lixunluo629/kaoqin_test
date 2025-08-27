package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/BestResponseTimeBalanceStrategy.class */
public class BestResponseTimeBalanceStrategy implements BalanceStrategy {
    @Override // com.mysql.jdbc.Extension
    public void destroy() {
    }

    @Override // com.mysql.jdbc.Extension
    public void init(Connection conn, Properties props) throws SQLException {
    }

    @Override // com.mysql.jdbc.BalanceStrategy
    public ConnectionImpl pickConnection(LoadBalancedConnectionProxy proxy, List<String> configuredHosts, Map<String, ConnectionImpl> liveConnections, long[] responseTimes, int numRetries) throws SQLException, InterruptedException {
        Map<String, Long> blackList = proxy.getGlobalBlacklist();
        SQLException ex = null;
        int attempts = 0;
        while (attempts < numRetries) {
            long minResponseTime = Long.MAX_VALUE;
            int bestHostIndex = 0;
            if (blackList.size() == configuredHosts.size()) {
                blackList = proxy.getGlobalBlacklist();
            }
            int i = 0;
            while (true) {
                if (i >= responseTimes.length) {
                    break;
                }
                long candidateResponseTime = responseTimes[i];
                if (candidateResponseTime < minResponseTime && !blackList.containsKey(configuredHosts.get(i))) {
                    if (candidateResponseTime == 0) {
                        bestHostIndex = i;
                        break;
                    }
                    bestHostIndex = i;
                    minResponseTime = candidateResponseTime;
                }
                i++;
            }
            String bestHost = configuredHosts.get(bestHostIndex);
            ConnectionImpl conn = liveConnections.get(bestHost);
            if (conn == null) {
                try {
                    conn = proxy.createConnectionForHost(bestHost);
                } catch (SQLException sqlEx) {
                    ex = sqlEx;
                    if (proxy.shouldExceptionTriggerConnectionSwitch(sqlEx)) {
                        proxy.addToGlobalBlacklist(bestHost);
                        blackList.put(bestHost, null);
                        if (blackList.size() == configuredHosts.size()) {
                            attempts++;
                            try {
                                Thread.sleep(250L);
                            } catch (InterruptedException e) {
                            }
                            blackList = proxy.getGlobalBlacklist();
                        }
                    } else {
                        throw sqlEx;
                    }
                }
            }
            return conn;
        }
        if (ex != null) {
            throw ex;
        }
        return null;
    }
}
