package org.springframework.data.redis;

import org.springframework.dao.DataRetrievalFailureException;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/TooManyClusterRedirectionsException.class */
public class TooManyClusterRedirectionsException extends DataRetrievalFailureException {
    private static final long serialVersionUID = -2818933672669154328L;

    public TooManyClusterRedirectionsException(String msg) {
        super(msg);
    }

    public TooManyClusterRedirectionsException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
