package org.springframework.data.redis;

import org.springframework.dao.DataRetrievalFailureException;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/ClusterRedirectException.class */
public class ClusterRedirectException extends DataRetrievalFailureException {
    private static final long serialVersionUID = -857075813794333965L;
    private final int slot;
    private final String host;
    private final int port;

    public ClusterRedirectException(int slot, String targetHost, int targetPort, Throwable e) {
        super(String.format("Redirect: slot %s to %s:%s.", Integer.valueOf(slot), targetHost, Integer.valueOf(targetPort)), e);
        this.slot = slot;
        this.host = targetHost;
        this.port = targetPort;
    }

    public int getSlot() {
        return this.slot;
    }

    public String getTargetHost() {
        return this.host;
    }

    public int getTargetPort() {
        return this.port;
    }
}
