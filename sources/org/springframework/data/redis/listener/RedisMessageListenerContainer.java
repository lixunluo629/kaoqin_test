package org.springframework.data.redis.listener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.SmartLifecycle;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.ConnectionUtils;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.Subscription;
import org.springframework.data.redis.connection.util.ByteArrayWrapper;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.SchedulingAwareRunnable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ErrorHandler;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/listener/RedisMessageListenerContainer.class */
public class RedisMessageListenerContainer implements InitializingBean, DisposableBean, BeanNameAware, SmartLifecycle {
    public static final String DEFAULT_THREAD_NAME_PREFIX = ClassUtils.getShortName((Class<?>) RedisMessageListenerContainer.class) + "-";
    public static final long DEFAULT_RECOVERY_INTERVAL = 5000;
    public static final long DEFAULT_SUBSCRIPTION_REGISTRATION_WAIT_TIME = 2000;
    private Executor subscriptionExecutor;
    private Executor taskExecutor;
    private RedisConnectionFactory connectionFactory;
    private String beanName;
    private ErrorHandler errorHandler;
    protected final Log logger = LogFactory.getLog(getClass());
    private long initWait = TimeUnit.SECONDS.toMillis(5);
    private final Object monitor = new Object();
    private volatile boolean running = false;
    private volatile boolean initialized = false;
    private volatile boolean listening = false;
    private volatile boolean manageExecutor = false;
    private final Map<ByteArrayWrapper, Collection<MessageListener>> patternMapping = new ConcurrentHashMap();
    private final Map<ByteArrayWrapper, Collection<MessageListener>> channelMapping = new ConcurrentHashMap();
    private final Map<MessageListener, Set<Topic>> listenerTopics = new ConcurrentHashMap();
    private final SubscriptionTask subscriptionTask = new SubscriptionTask();
    private volatile RedisSerializer<String> serializer = new StringRedisSerializer();
    private long recoveryInterval = 5000;
    private long maxSubscriptionRegistrationWaitingTime = 2000;

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/listener/RedisMessageListenerContainer$Condition.class */
    private interface Condition {
        boolean passes();
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        if (this.taskExecutor == null) {
            this.manageExecutor = true;
            this.taskExecutor = createDefaultTaskExecutor();
        }
        if (this.subscriptionExecutor == null) {
            this.subscriptionExecutor = this.taskExecutor;
        }
        this.initialized = true;
    }

    protected TaskExecutor createDefaultTaskExecutor() {
        String threadNamePrefix = this.beanName != null ? this.beanName + "-" : DEFAULT_THREAD_NAME_PREFIX;
        return new SimpleAsyncTaskExecutor(threadNamePrefix);
    }

    @Override // org.springframework.beans.factory.DisposableBean
    public void destroy() throws Exception {
        this.initialized = false;
        stop();
        if (this.manageExecutor && (this.taskExecutor instanceof DisposableBean)) {
            ((DisposableBean) this.taskExecutor).destroy();
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Stopped internally-managed task executor");
            }
        }
    }

    @Override // org.springframework.context.SmartLifecycle
    public boolean isAutoStartup() {
        return true;
    }

    @Override // org.springframework.context.SmartLifecycle
    public void stop(Runnable callback) {
        stop();
        callback.run();
    }

    @Override // org.springframework.context.Phased
    public int getPhase() {
        return Integer.MAX_VALUE;
    }

    @Override // org.springframework.context.Lifecycle
    public boolean isRunning() {
        return this.running;
    }

    @Override // org.springframework.context.Lifecycle
    public void start() {
        if (!this.running) {
            this.running = true;
            synchronized (this.monitor) {
                lazyListen();
                if (this.listening) {
                    try {
                        this.monitor.wait(this.initWait);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        this.running = false;
                        return;
                    }
                }
            }
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Started RedisMessageListenerContainer");
            }
        }
    }

    @Override // org.springframework.context.Lifecycle
    public void stop() {
        if (isRunning()) {
            this.running = false;
            this.subscriptionTask.cancel();
        }
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Stopped RedisMessageListenerContainer");
        }
    }

    protected void processMessage(MessageListener listener, Message message, byte[] pattern) {
        executeListener(listener, message, pattern);
    }

    protected void executeListener(MessageListener listener, Message message, byte[] pattern) {
        try {
            listener.onMessage(message, pattern);
        } catch (Throwable ex) {
            handleListenerException(ex);
        }
    }

    public final boolean isActive() {
        return this.initialized;
    }

    protected void handleListenerException(Throwable ex) {
        if (isActive()) {
            invokeErrorHandler(ex);
        } else {
            this.logger.debug("Listener exception after container shutdown", ex);
        }
    }

    protected void invokeErrorHandler(Throwable ex) {
        if (this.errorHandler != null) {
            this.errorHandler.handleError(ex);
        } else if (this.logger.isWarnEnabled()) {
            this.logger.warn("Execution of message listener failed, and no ErrorHandler has been set.", ex);
        }
    }

    public RedisConnectionFactory getConnectionFactory() {
        return this.connectionFactory;
    }

    public void setConnectionFactory(RedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override // org.springframework.beans.factory.BeanNameAware
    public void setBeanName(String name) {
        this.beanName = name;
    }

    public void setTaskExecutor(Executor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public void setSubscriptionExecutor(Executor subscriptionExecutor) {
        this.subscriptionExecutor = subscriptionExecutor;
    }

    public void setTopicSerializer(RedisSerializer<String> serializer) {
        this.serializer = serializer;
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public void setMessageListeners(Map<? extends MessageListener, Collection<? extends Topic>> listeners) {
        initMapping(listeners);
    }

    public void addMessageListener(MessageListener listener, Collection<? extends Topic> topics) {
        addListener(listener, topics);
        lazyListen();
    }

    public void addMessageListener(MessageListener listener, Topic topic) {
        addMessageListener(listener, Collections.singleton(topic));
    }

    public void removeMessageListener(MessageListener listener, Collection<? extends Topic> topics) {
        removeListener(listener, topics);
    }

    public void removeMessageListener(MessageListener listener, Topic topic) {
        removeMessageListener(listener, Collections.singleton(topic));
    }

    public void removeMessageListener(MessageListener listener) {
        removeMessageListener(listener, Collections.emptySet());
    }

    private void initMapping(Map<? extends MessageListener, Collection<? extends Topic>> listeners) {
        if (isRunning()) {
            this.subscriptionTask.cancel();
        }
        this.patternMapping.clear();
        this.channelMapping.clear();
        this.listenerTopics.clear();
        if (!CollectionUtils.isEmpty(listeners)) {
            for (Map.Entry<? extends MessageListener, Collection<? extends Topic>> entry : listeners.entrySet()) {
                addListener(entry.getKey(), entry.getValue());
            }
        }
        if (this.initialized) {
            start();
        }
    }

    private void lazyListen() {
        boolean debug = this.logger.isDebugEnabled();
        boolean started = false;
        if (isRunning() && !this.listening) {
            synchronized (this.monitor) {
                if (!this.listening && (this.channelMapping.size() > 0 || this.patternMapping.size() > 0)) {
                    this.subscriptionExecutor.execute(this.subscriptionTask);
                    this.listening = true;
                    started = true;
                }
            }
            if (debug) {
                if (started) {
                    this.logger.debug("Started listening for Redis messages");
                } else {
                    this.logger.debug("Postpone listening for Redis messages until actual listeners are added");
                }
            }
        }
    }

    private void addListener(MessageListener listener, Collection<? extends Topic> topics) {
        Assert.notNull(listener, "a valid listener is required");
        Assert.notEmpty(topics, "at least one topic is required");
        List<byte[]> channels = new ArrayList<>(topics.size());
        List<byte[]> patterns = new ArrayList<>(topics.size());
        boolean trace = this.logger.isTraceEnabled();
        Set<Topic> set = this.listenerTopics.get(listener);
        if (set == null) {
            set = new CopyOnWriteArraySet();
            this.listenerTopics.put(listener, set);
        }
        set.addAll(topics);
        for (Topic topic : topics) {
            ByteArrayWrapper holder = new ByteArrayWrapper(this.serializer.serialize(topic.getTopic()));
            if (topic instanceof ChannelTopic) {
                Collection<MessageListener> collection = this.channelMapping.get(holder);
                if (collection == null) {
                    collection = new CopyOnWriteArraySet();
                    this.channelMapping.put(holder, collection);
                }
                collection.add(listener);
                channels.add(holder.getArray());
                if (trace) {
                    this.logger.trace("Adding listener '" + listener + "' on channel '" + topic.getTopic() + "'");
                }
            } else if (topic instanceof PatternTopic) {
                Collection<MessageListener> collection2 = this.patternMapping.get(holder);
                if (collection2 == null) {
                    collection2 = new CopyOnWriteArraySet();
                    this.patternMapping.put(holder, collection2);
                }
                collection2.add(listener);
                patterns.add(holder.getArray());
                if (trace) {
                    this.logger.trace("Adding listener '" + listener + "' for pattern '" + topic.getTopic() + "'");
                }
            } else {
                throw new IllegalArgumentException("Unknown topic type '" + topic.getClass() + "'");
            }
        }
        if (this.listening) {
            this.subscriptionTask.subscribeChannel((byte[][]) channels.toArray((Object[]) new byte[channels.size()]));
            this.subscriptionTask.subscribePattern((byte[][]) patterns.toArray((Object[]) new byte[patterns.size()]));
        }
    }

    private void removeListener(MessageListener listener, Collection<? extends Topic> topics) {
        boolean trace = this.logger.isTraceEnabled();
        if (listener == null && CollectionUtils.isEmpty(topics)) {
            this.subscriptionTask.cancel();
            return;
        }
        List<byte[]> channelsToRemove = new ArrayList<>();
        List<byte[]> patternsToRemove = new ArrayList<>();
        if (CollectionUtils.isEmpty(topics)) {
            Set<Topic> set = this.listenerTopics.remove(listener);
            if (set == null) {
                return;
            } else {
                topics = set;
            }
        }
        for (Topic topic : topics) {
            ByteArrayWrapper holder = new ByteArrayWrapper(this.serializer.serialize(topic.getTopic()));
            if (topic instanceof ChannelTopic) {
                remove(listener, topic, holder, this.channelMapping, channelsToRemove);
                if (trace) {
                    String msg = listener != null ? "listener '" + listener + "'" : "all listeners";
                    this.logger.trace("Removing " + msg + " from channel '" + topic.getTopic() + "'");
                }
            } else if (topic instanceof PatternTopic) {
                remove(listener, topic, holder, this.patternMapping, patternsToRemove);
                if (trace) {
                    String msg2 = listener != null ? "listener '" + listener + "'" : "all listeners";
                    this.logger.trace("Removing " + msg2 + " from pattern '" + topic.getTopic() + "'");
                }
            }
        }
        if (this.listenerTopics.isEmpty()) {
            this.subscriptionTask.cancel();
        } else if (this.listening) {
            this.subscriptionTask.unsubscribeChannel((byte[][]) channelsToRemove.toArray((Object[]) new byte[channelsToRemove.size()]));
            this.subscriptionTask.unsubscribePattern((byte[][]) patternsToRemove.toArray((Object[]) new byte[patternsToRemove.size()]));
        }
    }

    private void remove(MessageListener listener, Topic topic, ByteArrayWrapper holder, Map<ByteArrayWrapper, Collection<MessageListener>> mapping, List<byte[]> topicToRemove) {
        Collection<MessageListener> listenersToRemove;
        Collection<MessageListener> listeners = mapping.get(holder);
        if (listeners != null) {
            if (listener != null) {
                listeners.remove(listener);
                listenersToRemove = Collections.singletonList(listener);
            } else {
                listenersToRemove = listeners;
            }
            for (MessageListener messageListener : listenersToRemove) {
                Set<Topic> topics = this.listenerTopics.get(messageListener);
                if (topics != null) {
                    topics.remove(topic);
                }
                if (CollectionUtils.isEmpty(topics)) {
                    this.listenerTopics.remove(messageListener);
                }
            }
            if (listener == null || listeners.isEmpty()) {
                mapping.remove(holder);
                topicToRemove.add(holder.getArray());
            }
        }
    }

    protected void handleSubscriptionException(Throwable ex) throws InterruptedException {
        this.listening = false;
        this.subscriptionTask.closeConnection();
        if (ex instanceof RedisConnectionFailureException) {
            if (isRunning()) {
                this.logger.error("Connection failure occurred. Restarting subscription task after " + this.recoveryInterval + " ms");
                sleepBeforeRecoveryAttempt();
                lazyListen();
                return;
            }
            return;
        }
        this.logger.error("SubscriptionTask aborted with exception:", ex);
    }

    protected void sleepBeforeRecoveryAttempt() throws InterruptedException {
        if (this.recoveryInterval > 0) {
            try {
                Thread.sleep(this.recoveryInterval);
            } catch (InterruptedException e) {
                this.logger.debug("Thread interrupted while sleeping the recovery interval");
                Thread.currentThread().interrupt();
            }
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/listener/RedisMessageListenerContainer$SubscriptionTask.class */
    private class SubscriptionTask implements SchedulingAwareRunnable {
        private volatile RedisConnection connection;
        private boolean subscriptionTaskRunning;
        private final Object localMonitor;
        private long subscriptionWait;

        private SubscriptionTask() {
            this.subscriptionTaskRunning = false;
            this.localMonitor = new Object();
            this.subscriptionWait = TimeUnit.SECONDS.toMillis(5L);
        }

        /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/listener/RedisMessageListenerContainer$SubscriptionTask$PatternSubscriptionTask.class */
        private class PatternSubscriptionTask implements SchedulingAwareRunnable {
            private long WAIT;
            private long ROUNDS;

            private PatternSubscriptionTask() {
                this.WAIT = 500L;
                this.ROUNDS = 3L;
            }

            @Override // org.springframework.scheduling.SchedulingAwareRunnable
            public boolean isLongLived() {
                return false;
            }

            @Override // java.lang.Runnable
            public void run() {
                boolean done = false;
                for (int i = 0; i < this.ROUNDS && !done; i++) {
                    if (SubscriptionTask.this.connection != null) {
                        synchronized (SubscriptionTask.this.localMonitor) {
                            if (SubscriptionTask.this.connection.isSubscribed()) {
                                done = true;
                                SubscriptionTask.this.connection.getSubscription().pSubscribe(SubscriptionTask.this.unwrap(RedisMessageListenerContainer.this.patternMapping.keySet()));
                            } else {
                                try {
                                    Thread.sleep(this.WAIT);
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }

        @Override // org.springframework.scheduling.SchedulingAwareRunnable
        public boolean isLongLived() {
            return true;
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (this.localMonitor) {
                this.subscriptionTaskRunning = true;
            }
            try {
                try {
                    this.connection = RedisMessageListenerContainer.this.connectionFactory.getConnection();
                    if (!this.connection.isSubscribed()) {
                        boolean asyncConnection = ConnectionUtils.isAsync(RedisMessageListenerContainer.this.connectionFactory);
                        if (!asyncConnection) {
                            synchronized (RedisMessageListenerContainer.this.monitor) {
                                RedisMessageListenerContainer.this.monitor.notify();
                            }
                        }
                        SubscriptionPresentCondition subscriptionPresent = eventuallyPerformSubscription();
                        if (asyncConnection) {
                            SpinBarrier.waitFor(subscriptionPresent, RedisMessageListenerContainer.this.getMaxSubscriptionRegistrationWaitingTime());
                            synchronized (RedisMessageListenerContainer.this.monitor) {
                                RedisMessageListenerContainer.this.monitor.notify();
                            }
                        }
                        synchronized (this.localMonitor) {
                            this.subscriptionTaskRunning = false;
                            this.localMonitor.notify();
                        }
                        return;
                    }
                    throw new IllegalStateException("Retrieved connection is already subscribed; aborting listening");
                } catch (Throwable t) {
                    RedisMessageListenerContainer.this.handleSubscriptionException(t);
                    synchronized (this.localMonitor) {
                        this.subscriptionTaskRunning = false;
                        this.localMonitor.notify();
                    }
                }
            } catch (Throwable th) {
                synchronized (this.localMonitor) {
                    this.subscriptionTaskRunning = false;
                    this.localMonitor.notify();
                    throw th;
                }
            }
        }

        private SubscriptionPresentCondition eventuallyPerformSubscription() {
            SubscriptionPresentCondition condition;
            if (!RedisMessageListenerContainer.this.channelMapping.isEmpty()) {
                if (!RedisMessageListenerContainer.this.patternMapping.isEmpty()) {
                    RedisMessageListenerContainer.this.subscriptionExecutor.execute(new PatternSubscriptionTask());
                    condition = new PatternSubscriptionPresentCondition();
                } else {
                    condition = new SubscriptionPresentCondition();
                }
                this.connection.subscribe(new DispatchMessageListener(), unwrap(RedisMessageListenerContainer.this.channelMapping.keySet()));
            } else {
                condition = new PatternSubscriptionPresentCondition();
                this.connection.pSubscribe(new DispatchMessageListener(), unwrap(RedisMessageListenerContainer.this.patternMapping.keySet()));
            }
            return condition;
        }

        /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/listener/RedisMessageListenerContainer$SubscriptionTask$SubscriptionPresentCondition.class */
        private class SubscriptionPresentCondition implements Condition {
            private SubscriptionPresentCondition() {
            }

            @Override // org.springframework.data.redis.listener.RedisMessageListenerContainer.Condition
            public boolean passes() {
                return SubscriptionTask.this.connection.isSubscribed();
            }
        }

        /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/listener/RedisMessageListenerContainer$SubscriptionTask$PatternSubscriptionPresentCondition.class */
        private class PatternSubscriptionPresentCondition extends SubscriptionPresentCondition {
            private PatternSubscriptionPresentCondition() {
                super();
            }

            @Override // org.springframework.data.redis.listener.RedisMessageListenerContainer.SubscriptionTask.SubscriptionPresentCondition, org.springframework.data.redis.listener.RedisMessageListenerContainer.Condition
            public boolean passes() {
                return super.passes() && !CollectionUtils.isEmpty((Collection<?>) SubscriptionTask.this.connection.getSubscription().getPatterns());
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Type inference failed for: r0v16, types: [byte[], byte[][]] */
        /* JADX WARN: Type inference failed for: r0v4, types: [byte[], byte[][]] */
        public byte[][] unwrap(Collection<ByteArrayWrapper> holders) {
            if (CollectionUtils.isEmpty(holders)) {
                return new byte[0];
            }
            ?? r0 = new byte[holders.size()];
            int index = 0;
            for (ByteArrayWrapper arrayHolder : holders) {
                int i = index;
                index++;
                r0[i] = arrayHolder.getArray();
            }
            return r0;
        }

        /* JADX WARN: Removed duplicated region for block: B:27:0x00a6 A[Catch: all -> 0x00c0, TryCatch #0 {, blocks: (B:14:0x004d, B:16:0x005c, B:17:0x006a, B:20:0x0083, B:22:0x008a, B:25:0x009f, B:27:0x00a6, B:30:0x00bc, B:28:0x00ad, B:24:0x0099, B:19:0x0074), top: B:38:0x004d, inners: #1, #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:28:0x00ad A[Catch: all -> 0x00c0, TryCatch #0 {, blocks: (B:14:0x004d, B:16:0x005c, B:17:0x006a, B:20:0x0083, B:22:0x008a, B:25:0x009f, B:27:0x00a6, B:30:0x00bc, B:28:0x00ad, B:24:0x0099, B:19:0x0074), top: B:38:0x004d, inners: #1, #2 }] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        void cancel() {
            /*
                r4 = this;
                r0 = r4
                org.springframework.data.redis.listener.RedisMessageListenerContainer r0 = org.springframework.data.redis.listener.RedisMessageListenerContainer.this
                boolean r0 = org.springframework.data.redis.listener.RedisMessageListenerContainer.access$1300(r0)
                if (r0 == 0) goto L11
                r0 = r4
                org.springframework.data.redis.connection.RedisConnection r0 = r0.connection
                if (r0 != 0) goto L12
            L11:
                return
            L12:
                r0 = r4
                org.springframework.data.redis.listener.RedisMessageListenerContainer r0 = org.springframework.data.redis.listener.RedisMessageListenerContainer.this
                r1 = 0
                boolean r0 = org.springframework.data.redis.listener.RedisMessageListenerContainer.access$1302(r0, r1)
                r0 = r4
                org.springframework.data.redis.listener.RedisMessageListenerContainer r0 = org.springframework.data.redis.listener.RedisMessageListenerContainer.this
                org.apache.commons.logging.Log r0 = r0.logger
                boolean r0 = r0.isTraceEnabled()
                if (r0 == 0) goto L38
                r0 = r4
                org.springframework.data.redis.listener.RedisMessageListenerContainer r0 = org.springframework.data.redis.listener.RedisMessageListenerContainer.this
                org.apache.commons.logging.Log r0 = r0.logger
                java.lang.String r1 = "Cancelling Redis subscription..."
                r0.trace(r1)
            L38:
                r0 = r4
                org.springframework.data.redis.connection.RedisConnection r0 = r0.connection
                org.springframework.data.redis.connection.Subscription r0 = r0.getSubscription()
                r5 = r0
                r0 = r5
                if (r0 == 0) goto Lc7
                r0 = r4
                java.lang.Object r0 = r0.localMonitor
                r1 = r0
                r6 = r1
                monitor-enter(r0)
                r0 = r4
                org.springframework.data.redis.listener.RedisMessageListenerContainer r0 = org.springframework.data.redis.listener.RedisMessageListenerContainer.this     // Catch: java.lang.Throwable -> Lc0
                org.apache.commons.logging.Log r0 = r0.logger     // Catch: java.lang.Throwable -> Lc0
                boolean r0 = r0.isTraceEnabled()     // Catch: java.lang.Throwable -> Lc0
                if (r0 == 0) goto L6a
                r0 = r4
                org.springframework.data.redis.listener.RedisMessageListenerContainer r0 = org.springframework.data.redis.listener.RedisMessageListenerContainer.this     // Catch: java.lang.Throwable -> Lc0
                org.apache.commons.logging.Log r0 = r0.logger     // Catch: java.lang.Throwable -> Lc0
                java.lang.String r1 = "Unsubscribing from all channels"
                r0.trace(r1)     // Catch: java.lang.Throwable -> Lc0
            L6a:
                r0 = r5
                r0.close()     // Catch: java.lang.Exception -> L73 java.lang.Throwable -> Lc0
                goto L83
            L73:
                r7 = move-exception
                r0 = r4
                org.springframework.data.redis.listener.RedisMessageListenerContainer r0 = org.springframework.data.redis.listener.RedisMessageListenerContainer.this     // Catch: java.lang.Throwable -> Lc0
                org.apache.commons.logging.Log r0 = r0.logger     // Catch: java.lang.Throwable -> Lc0
                java.lang.String r1 = "Unable to unsubscribe from subscriptions"
                r2 = r7
                r0.warn(r1, r2)     // Catch: java.lang.Throwable -> Lc0
            L83:
                r0 = r4
                boolean r0 = r0.subscriptionTaskRunning     // Catch: java.lang.Throwable -> Lc0
                if (r0 == 0) goto L9f
                r0 = r4
                java.lang.Object r0 = r0.localMonitor     // Catch: java.lang.InterruptedException -> L98 java.lang.Throwable -> Lc0
                r1 = r4
                long r1 = r1.subscriptionWait     // Catch: java.lang.InterruptedException -> L98 java.lang.Throwable -> Lc0
                r0.wait(r1)     // Catch: java.lang.InterruptedException -> L98 java.lang.Throwable -> Lc0
                goto L9f
            L98:
                r7 = move-exception
                java.lang.Thread r0 = java.lang.Thread.currentThread()     // Catch: java.lang.Throwable -> Lc0
                r0.interrupt()     // Catch: java.lang.Throwable -> Lc0
            L9f:
                r0 = r4
                boolean r0 = r0.subscriptionTaskRunning     // Catch: java.lang.Throwable -> Lc0
                if (r0 != 0) goto Lad
                r0 = r4
                r0.closeConnection()     // Catch: java.lang.Throwable -> Lc0
                goto Lbb
            Lad:
                r0 = r4
                org.springframework.data.redis.listener.RedisMessageListenerContainer r0 = org.springframework.data.redis.listener.RedisMessageListenerContainer.this     // Catch: java.lang.Throwable -> Lc0
                org.apache.commons.logging.Log r0 = r0.logger     // Catch: java.lang.Throwable -> Lc0
                java.lang.String r1 = "Unable to close connection. Subscription task still running"
                r0.warn(r1)     // Catch: java.lang.Throwable -> Lc0
            Lbb:
                r0 = r6
                monitor-exit(r0)     // Catch: java.lang.Throwable -> Lc0
                goto Lc7
            Lc0:
                r8 = move-exception
                r0 = r6
                monitor-exit(r0)     // Catch: java.lang.Throwable -> Lc0
                r0 = r8
                throw r0
            Lc7:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: org.springframework.data.redis.listener.RedisMessageListenerContainer.SubscriptionTask.cancel():void");
        }

        void closeConnection() {
            if (this.connection != null) {
                RedisMessageListenerContainer.this.logger.trace("Closing connection");
                try {
                    this.connection.close();
                } catch (Exception e) {
                    RedisMessageListenerContainer.this.logger.warn("Error closing subscription connection", e);
                }
                this.connection = null;
            }
        }

        void subscribeChannel(byte[]... channels) {
            if (channels != null && channels.length > 0 && this.connection != null) {
                synchronized (this.localMonitor) {
                    Subscription sub = this.connection.getSubscription();
                    if (sub != null) {
                        sub.subscribe(channels);
                    }
                }
            }
        }

        void subscribePattern(byte[]... patterns) {
            if (patterns != null && patterns.length > 0 && this.connection != null) {
                synchronized (this.localMonitor) {
                    Subscription sub = this.connection.getSubscription();
                    if (sub != null) {
                        sub.pSubscribe(patterns);
                    }
                }
            }
        }

        void unsubscribeChannel(byte[]... channels) {
            if (channels != null && channels.length > 0 && this.connection != null) {
                synchronized (this.localMonitor) {
                    Subscription sub = this.connection.getSubscription();
                    if (sub != null) {
                        sub.unsubscribe(channels);
                    }
                }
            }
        }

        void unsubscribePattern(byte[]... patterns) {
            if (patterns != null && patterns.length > 0 && this.connection != null) {
                synchronized (this.localMonitor) {
                    Subscription sub = this.connection.getSubscription();
                    if (sub != null) {
                        sub.pUnsubscribe(patterns);
                    }
                }
            }
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/listener/RedisMessageListenerContainer$DispatchMessageListener.class */
    private class DispatchMessageListener implements MessageListener {
        private DispatchMessageListener() {
        }

        @Override // org.springframework.data.redis.connection.MessageListener
        public void onMessage(Message message, byte[] pattern) {
            Collection<MessageListener> listeners;
            if (pattern != null && pattern.length > 0) {
                listeners = (Collection) RedisMessageListenerContainer.this.patternMapping.get(new ByteArrayWrapper(pattern));
            } else {
                pattern = null;
                listeners = (Collection) RedisMessageListenerContainer.this.channelMapping.get(new ByteArrayWrapper(message.getChannel()));
            }
            if (!CollectionUtils.isEmpty(listeners)) {
                RedisMessageListenerContainer.this.dispatchMessage(listeners, message, pattern);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchMessage(Collection<MessageListener> listeners, final Message message, byte[] pattern) {
        final byte[] source = pattern != null ? (byte[]) pattern.clone() : message.getChannel();
        for (final MessageListener messageListener : listeners) {
            this.taskExecutor.execute(new Runnable() { // from class: org.springframework.data.redis.listener.RedisMessageListenerContainer.1
                @Override // java.lang.Runnable
                public void run() {
                    RedisMessageListenerContainer.this.processMessage(messageListener, message, source);
                }
            });
        }
    }

    public void setRecoveryInterval(long recoveryInterval) {
        this.recoveryInterval = recoveryInterval;
    }

    public long getMaxSubscriptionRegistrationWaitingTime() {
        return this.maxSubscriptionRegistrationWaitingTime;
    }

    public void setMaxSubscriptionRegistrationWaitingTime(long maxSubscriptionRegistrationWaitingTime) {
        this.maxSubscriptionRegistrationWaitingTime = maxSubscriptionRegistrationWaitingTime;
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/listener/RedisMessageListenerContainer$SpinBarrier.class */
    private static abstract class SpinBarrier {
        private SpinBarrier() {
        }

        static boolean waitFor(Condition condition, long timeout) throws InterruptedException {
            long startTime = System.currentTimeMillis();
            while (!timedOut(startTime, timeout)) {
                try {
                    if (condition.passes()) {
                        return true;
                    }
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
            return false;
        }

        private static boolean timedOut(long startTime, long timeout) {
            return startTime + timeout < System.currentTimeMillis();
        }
    }
}
