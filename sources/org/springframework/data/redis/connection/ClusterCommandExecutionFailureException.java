package org.springframework.data.redis.connection;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.dao.UncategorizedDataAccessException;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/ClusterCommandExecutionFailureException.class */
public class ClusterCommandExecutionFailureException extends UncategorizedDataAccessException {
    private static final long serialVersionUID = 5727044227040368955L;
    private final Collection<? extends Throwable> causes;

    public ClusterCommandExecutionFailureException(Throwable cause) {
        this((List<? extends Throwable>) Collections.singletonList(cause));
    }

    public ClusterCommandExecutionFailureException(List<? extends Throwable> causes) {
        super(causes.get(0).getMessage(), causes.get(0));
        this.causes = causes;
    }

    public Collection<? extends Throwable> getCauses() {
        return this.causes;
    }
}
