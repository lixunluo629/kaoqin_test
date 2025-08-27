package org.springframework.data.redis.listener;

import org.springframework.util.Assert;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/listener/PatternTopic.class */
public class PatternTopic implements Topic {
    private final String channelPattern;

    public PatternTopic(String pattern) {
        Assert.notNull(pattern, "a valid topic is required");
        this.channelPattern = pattern;
    }

    @Override // org.springframework.data.redis.listener.Topic
    public String getTopic() {
        return this.channelPattern;
    }

    public int hashCode() {
        return this.channelPattern.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof PatternTopic)) {
            return false;
        }
        PatternTopic other = (PatternTopic) obj;
        if (this.channelPattern == null) {
            if (other.channelPattern != null) {
                return false;
            }
            return true;
        }
        if (!this.channelPattern.equals(other.channelPattern)) {
            return false;
        }
        return true;
    }

    public String toString() {
        return this.channelPattern;
    }
}
