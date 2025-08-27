package org.springframework.data.redis.connection;

import java.io.Serializable;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/Message.class */
public interface Message extends Serializable {
    byte[] getBody();

    byte[] getChannel();
}
