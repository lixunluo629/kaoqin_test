package org.springframework.data.redis;

import org.springframework.dao.DataAccessResourceFailureException;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/ClusterStateFailureException.class */
public class ClusterStateFailureException extends DataAccessResourceFailureException {
    private static final long serialVersionUID = 333399051713240852L;

    public ClusterStateFailureException(String msg) {
        super(msg);
    }

    public ClusterStateFailureException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
