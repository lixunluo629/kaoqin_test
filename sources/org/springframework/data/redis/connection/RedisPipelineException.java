package org.springframework.data.redis.connection;

import java.util.Collections;
import java.util.List;
import org.springframework.dao.InvalidDataAccessResourceUsageException;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisPipelineException.class */
public class RedisPipelineException extends InvalidDataAccessResourceUsageException {
    private final List<Object> results;

    public RedisPipelineException(String msg, Throwable cause, List<Object> pipelineResult) {
        super(msg, cause);
        this.results = Collections.unmodifiableList(pipelineResult);
    }

    public RedisPipelineException(Exception cause, List<Object> pipelineResult) {
        this("Pipeline contained one or more invalid commands", cause, pipelineResult);
    }

    public RedisPipelineException(Exception cause) {
        this("Pipeline contained one or more invalid commands", cause, Collections.emptyList());
    }

    public RedisPipelineException(String msg, List<Object> pipelineResult) {
        super(msg);
        this.results = Collections.unmodifiableList(pipelineResult);
    }

    public List<Object> getPipelineResult() {
        return this.results;
    }
}
