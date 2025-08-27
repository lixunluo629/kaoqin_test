package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/SequentialBalanceStrategy.class */
public class SequentialBalanceStrategy implements BalanceStrategy {
    private int currentHostIndex = -1;

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
        Map<String, Long> blackList = proxy.getGlobalBlacklist();
        for (int attempts = 0; attempts < numRetries; attempts++) {
            if (numHosts == 1) {
                this.currentHostIndex = 0;
            } else {
                if (this.currentHostIndex == -1) {
                    int random = (int) Math.floor(Math.random() * numHosts);
                    int i = random;
                    while (true) {
                        if (i >= numHosts) {
                            break;
                        }
                        if (blackList.containsKey(configuredHosts.get(i))) {
                            i++;
                        } else {
                            this.currentHostIndex = i;
                            break;
                        }
                    }
                    if (this.currentHostIndex == -1) {
                        int i2 = 0;
                        while (true) {
                            if (i2 >= random) {
                                break;
                            }
                            if (blackList.containsKey(configuredHosts.get(i2))) {
                                i2++;
                            } else {
                                this.currentHostIndex = i2;
                                break;
                            }
                        }
                    }
                    if (this.currentHostIndex == -1) {
                        blackList = proxy.getGlobalBlacklist();
                        try {
                            Thread.sleep(250L);
                        } catch (InterruptedException e) {
                        }
                    }
                } else {
                    int i3 = this.currentHostIndex + 1;
                    boolean foundGoodHost = false;
                    while (true) {
                        if (i3 >= numHosts) {
                            break;
                        }
                        if (blackList.containsKey(configuredHosts.get(i3))) {
                            i3++;
                        } else {
                            this.currentHostIndex = i3;
                            foundGoodHost = true;
                            break;
                        }
                    }
                    if (!foundGoodHost) {
                        int i4 = 0;
                        while (true) {
                            if (i4 >= this.currentHostIndex) {
                                break;
                            }
                            if (blackList.containsKey(configuredHosts.get(i4))) {
                                i4++;
                            } else {
                                this.currentHostIndex = i4;
                                foundGoodHost = true;
                                break;
                            }
                        }
                    }
                    if (!foundGoodHost) {
                        blackList = proxy.getGlobalBlacklist();
                        try {
                            Thread.sleep(250L);
                        } catch (InterruptedException e2) {
                        }
                    }
                }
            }
            String hostPortSpec = configuredHosts.get(this.currentHostIndex);
            ConnectionImpl conn = liveConnections.get(hostPortSpec);
            if (conn == null) {
                try {
                    conn = proxy.createConnectionForHost(hostPortSpec);
                } catch (SQLException sqlEx) {
                    ex = sqlEx;
                    if (proxy.shouldExceptionTriggerConnectionSwitch(sqlEx)) {
                        proxy.addToGlobalBlacklist(hostPortSpec);
                        try {
                            Thread.sleep(250L);
                        } catch (InterruptedException e3) {
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
