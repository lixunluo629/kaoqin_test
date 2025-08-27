package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/RandomBalanceStrategy.class */
public class RandomBalanceStrategy implements BalanceStrategy {
    @Override // com.mysql.jdbc.Extension
    public void destroy() {
    }

    @Override // com.mysql.jdbc.Extension
    public void init(Connection conn, Properties props) throws SQLException {
    }

    @Override // com.mysql.jdbc.BalanceStrategy
    public ConnectionImpl pickConnection(LoadBalancedConnectionProxy proxy, List<String> configuredHosts, Map<String, ConnectionImpl> liveConnections, long[] responseTimes, int numRetries) throws SQLException, InterruptedException {
        int numHosts = configuredHosts.size();
        SQLException ex = null;
        List<String> whiteList = new ArrayList<>(numHosts);
        whiteList.addAll(configuredHosts);
        Map<String, Long> blackList = proxy.getGlobalBlacklist();
        whiteList.removeAll(blackList.keySet());
        Map<String, Integer> whiteListMap = getArrayIndexMap(whiteList);
        int attempts = 0;
        while (attempts < numRetries) {
            int random = (int) Math.floor(Math.random() * whiteList.size());
            if (whiteList.size() == 0) {
                throw SQLError.createSQLException("No hosts configured", null);
            }
            String hostPortSpec = whiteList.get(random);
            ConnectionImpl conn = liveConnections.get(hostPortSpec);
            if (conn == null) {
                try {
                    conn = proxy.createConnectionForHost(hostPortSpec);
                } catch (SQLException sqlEx) {
                    ex = sqlEx;
                    if (proxy.shouldExceptionTriggerConnectionSwitch(sqlEx)) {
                        Integer whiteListIndex = whiteListMap.get(hostPortSpec);
                        if (whiteListIndex != null) {
                            whiteList.remove(whiteListIndex.intValue());
                            whiteListMap = getArrayIndexMap(whiteList);
                        }
                        proxy.addToGlobalBlacklist(hostPortSpec);
                        if (whiteList.size() == 0) {
                            attempts++;
                            try {
                                Thread.sleep(250L);
                            } catch (InterruptedException e) {
                            }
                            new HashMap(numHosts);
                            whiteList.addAll(configuredHosts);
                            Map<String, Long> blackList2 = proxy.getGlobalBlacklist();
                            whiteList.removeAll(blackList2.keySet());
                            whiteListMap = getArrayIndexMap(whiteList);
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

    private Map<String, Integer> getArrayIndexMap(List<String> l) {
        Map<String, Integer> m = new HashMap<>(l.size());
        for (int i = 0; i < l.size(); i++) {
            m.put(l.get(i), Integer.valueOf(i));
        }
        return m;
    }
}
