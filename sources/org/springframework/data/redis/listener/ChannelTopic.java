package org.springframework.data.redis.listener;

import org.springframework.util.Assert;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/listener/ChannelTopic.class */
public class ChannelTopic implements Topic {
    private final String channelName;

    public ChannelTopic(String name) {
        Assert.notNull(name, "a valid topic is required");
        this.channelName = name;
    }

    @Override // org.springframework.data.redis.listener.Topic
    public String getTopic() {
        return this.channelName;
    }

    public int hashCode() {
        return this.channelName.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof ChannelTopic)) {
            return false;
        }
        ChannelTopic other = (ChannelTopic) obj;
        if (this.channelName == null) {
            if (other.channelName != null) {
                return false;
            }
            return true;
        }
        if (!this.channelName.equals(other.channelName)) {
            return false;
        }
        return true;
    }

    public String toString() {
        return this.channelName;
    }
}
