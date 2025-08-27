package org.springframework.data.redis.listener;

import java.util.List;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/listener/KeyspaceEventMessageListener.class */
public abstract class KeyspaceEventMessageListener implements MessageListener, InitializingBean, DisposableBean {
    private final RedisMessageListenerContainer listenerContainer;
    private static final Topic TOPIC_ALL_KEYEVENTS = new PatternTopic("__keyevent@*");
    private String keyspaceNotificationsConfigParameter = "EA";

    protected abstract void doHandleMessage(Message message);

    public KeyspaceEventMessageListener(RedisMessageListenerContainer listenerContainer) {
        Assert.notNull(listenerContainer, "RedisMessageListenerContainer to run in must not be null!");
        this.listenerContainer = listenerContainer;
    }

    @Override // org.springframework.data.redis.connection.MessageListener
    public void onMessage(Message message, byte[] pattern) {
        if (message == null || message.getChannel() == null || message.getBody() == null) {
            return;
        }
        doHandleMessage(message);
    }

    public void init() throws DataAccessException {
        if (StringUtils.hasText(this.keyspaceNotificationsConfigParameter)) {
            RedisConnection connection = this.listenerContainer.getConnectionFactory().getConnection();
            try {
                List<String> config = connection.getConfig("notify-keyspace-events");
                if (config.size() == 2 && !StringUtils.hasText(config.get(1))) {
                    connection.setConfig("notify-keyspace-events", this.keyspaceNotificationsConfigParameter);
                }
            } finally {
                connection.close();
            }
        }
        doRegister(this.listenerContainer);
    }

    protected void doRegister(RedisMessageListenerContainer container) {
        this.listenerContainer.addMessageListener(this, TOPIC_ALL_KEYEVENTS);
    }

    @Override // org.springframework.beans.factory.DisposableBean
    public void destroy() throws Exception {
        this.listenerContainer.removeMessageListener(this);
    }

    public void setKeyspaceNotificationsConfigParameter(String keyspaceNotificationsConfigParameter) {
        this.keyspaceNotificationsConfigParameter = keyspaceNotificationsConfigParameter;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws Exception {
        init();
    }
}
