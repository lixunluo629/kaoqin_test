package org.apache.ibatis.datasource.pooled;

import ch.qos.logback.core.joran.action.ActionConst;
import java.util.ArrayList;
import java.util.List;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/datasource/pooled/PoolState.class */
public class PoolState {
    protected PooledDataSource dataSource;
    protected final List<PooledConnection> idleConnections = new ArrayList();
    protected final List<PooledConnection> activeConnections = new ArrayList();
    protected long requestCount = 0;
    protected long accumulatedRequestTime = 0;
    protected long accumulatedCheckoutTime = 0;
    protected long claimedOverdueConnectionCount = 0;
    protected long accumulatedCheckoutTimeOfOverdueConnections = 0;
    protected long accumulatedWaitTime = 0;
    protected long hadToWaitCount = 0;
    protected long badConnectionCount = 0;

    public PoolState(PooledDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public synchronized long getRequestCount() {
        return this.requestCount;
    }

    public synchronized long getAverageRequestTime() {
        if (this.requestCount == 0) {
            return 0L;
        }
        return this.accumulatedRequestTime / this.requestCount;
    }

    public synchronized long getAverageWaitTime() {
        if (this.hadToWaitCount == 0) {
            return 0L;
        }
        return this.accumulatedWaitTime / this.hadToWaitCount;
    }

    public synchronized long getHadToWaitCount() {
        return this.hadToWaitCount;
    }

    public synchronized long getBadConnectionCount() {
        return this.badConnectionCount;
    }

    public synchronized long getClaimedOverdueConnectionCount() {
        return this.claimedOverdueConnectionCount;
    }

    public synchronized long getAverageOverdueCheckoutTime() {
        if (this.claimedOverdueConnectionCount == 0) {
            return 0L;
        }
        return this.accumulatedCheckoutTimeOfOverdueConnections / this.claimedOverdueConnectionCount;
    }

    public synchronized long getAverageCheckoutTime() {
        if (this.requestCount == 0) {
            return 0L;
        }
        return this.accumulatedCheckoutTime / this.requestCount;
    }

    public synchronized int getIdleConnectionCount() {
        return this.idleConnections.size();
    }

    public synchronized int getActiveConnectionCount() {
        return this.activeConnections.size();
    }

    public synchronized String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n===CONFINGURATION==============================================");
        builder.append("\n jdbcDriver                     ").append(this.dataSource.getDriver());
        builder.append("\n jdbcUrl                        ").append(this.dataSource.getUrl());
        builder.append("\n jdbcUsername                   ").append(this.dataSource.getUsername());
        builder.append("\n jdbcPassword                   ").append(this.dataSource.getPassword() == null ? ActionConst.NULL : "************");
        builder.append("\n poolMaxActiveConnections       ").append(this.dataSource.poolMaximumActiveConnections);
        builder.append("\n poolMaxIdleConnections         ").append(this.dataSource.poolMaximumIdleConnections);
        builder.append("\n poolMaxCheckoutTime            ").append(this.dataSource.poolMaximumCheckoutTime);
        builder.append("\n poolTimeToWait                 ").append(this.dataSource.poolTimeToWait);
        builder.append("\n poolPingEnabled                ").append(this.dataSource.poolPingEnabled);
        builder.append("\n poolPingQuery                  ").append(this.dataSource.poolPingQuery);
        builder.append("\n poolPingConnectionsNotUsedFor  ").append(this.dataSource.poolPingConnectionsNotUsedFor);
        builder.append("\n ---STATUS-----------------------------------------------------");
        builder.append("\n activeConnections              ").append(getActiveConnectionCount());
        builder.append("\n idleConnections                ").append(getIdleConnectionCount());
        builder.append("\n requestCount                   ").append(getRequestCount());
        builder.append("\n averageRequestTime             ").append(getAverageRequestTime());
        builder.append("\n averageCheckoutTime            ").append(getAverageCheckoutTime());
        builder.append("\n claimedOverdue                 ").append(getClaimedOverdueConnectionCount());
        builder.append("\n averageOverdueCheckoutTime     ").append(getAverageOverdueCheckoutTime());
        builder.append("\n hadToWait                      ").append(getHadToWaitCount());
        builder.append("\n averageWaitTime                ").append(getAverageWaitTime());
        builder.append("\n badConnectionCount             ").append(getBadConnectionCount());
        builder.append("\n===============================================================");
        return builder.toString();
    }
}
