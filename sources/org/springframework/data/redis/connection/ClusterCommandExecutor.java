package org.springframework.data.redis.connection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.ClusterRedirectException;
import org.springframework.data.redis.ClusterStateFailureException;
import org.springframework.data.redis.ExceptionTranslationStrategy;
import org.springframework.data.redis.TooManyClusterRedirectionsException;
import org.springframework.data.redis.connection.util.ByteArraySet;
import org.springframework.data.redis.connection.util.ByteArrayWrapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/ClusterCommandExecutor.class */
public class ClusterCommandExecutor implements DisposableBean {
    private AsyncTaskExecutor executor;
    private final ClusterTopologyProvider topologyProvider;
    private final ClusterNodeResourceProvider resourceProvider;
    private final ExceptionTranslationStrategy exceptionTranslationStrategy;
    private int maxRedirects;

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/ClusterCommandExecutor$ClusterCommandCallback.class */
    public interface ClusterCommandCallback<T, S> {
        S doInCluster(T t);
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/ClusterCommandExecutor$MultiKeyClusterCommandCallback.class */
    public interface MultiKeyClusterCommandCallback<T, S> {
        S doInCluster(T t, byte[] bArr);
    }

    public ClusterCommandExecutor(ClusterTopologyProvider topologyProvider, ClusterNodeResourceProvider resourceProvider, ExceptionTranslationStrategy exceptionTranslation) {
        this.maxRedirects = 5;
        if (this.executor == null) {
            ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
            threadPoolTaskExecutor.initialize();
            this.executor = threadPoolTaskExecutor;
        }
        Assert.notNull(topologyProvider, "ClusterTopologyProvider must not be null!");
        Assert.notNull(resourceProvider, "ClusterNodeResourceProvider must not be null!");
        Assert.notNull(exceptionTranslation, "ExceptionTranslationStrategy must not be null!");
        this.topologyProvider = topologyProvider;
        this.resourceProvider = resourceProvider;
        this.exceptionTranslationStrategy = exceptionTranslation;
    }

    public ClusterCommandExecutor(ClusterTopologyProvider topologyProvider, ClusterNodeResourceProvider resourceProvider, ExceptionTranslationStrategy exceptionTranslation, AsyncTaskExecutor executor) {
        this(topologyProvider, resourceProvider, exceptionTranslation);
        this.executor = executor;
    }

    public <T> NodeResult<T> executeCommandOnArbitraryNode(ClusterCommandCallback<?, T> cmd) {
        Assert.notNull(cmd, "ClusterCommandCallback must not be null!");
        List<RedisClusterNode> nodes = new ArrayList<>(getClusterTopology().getActiveNodes());
        return executeCommandOnSingleNode(cmd, nodes.get(new Random().nextInt(nodes.size())));
    }

    public <S, T> NodeResult<T> executeCommandOnSingleNode(ClusterCommandCallback<S, T> cmd, RedisClusterNode node) {
        return executeCommandOnSingleNode(cmd, node, 0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <S, T> NodeResult<T> executeCommandOnSingleNode(ClusterCommandCallback<S, T> clusterCommandCallback, RedisClusterNode node, int redirectCount) {
        Assert.notNull(clusterCommandCallback, "ClusterCommandCallback must not be null!");
        Assert.notNull(node, "RedisClusterNode must not be null!");
        if (redirectCount > this.maxRedirects) {
            throw new TooManyClusterRedirectionsException(String.format("Cannot follow Cluster Redirects over more than %s legs. Please consider increasing the number of redirects to follow. Current value is: %s.", Integer.valueOf(redirectCount), Integer.valueOf(this.maxRedirects)));
        }
        RedisClusterNode nodeToUse = lookupNode(node);
        Object resourceForSpecificNode = this.resourceProvider.getResourceForSpecificNode(nodeToUse);
        Assert.notNull(resourceForSpecificNode, "Could not acquire resource for node. Is your cluster info up to date?");
        try {
            try {
                NodeResult<T> nodeResult = new NodeResult<>(node, clusterCommandCallback.doInCluster(resourceForSpecificNode));
                this.resourceProvider.returnResourceForSpecificNode(nodeToUse, resourceForSpecificNode);
                return nodeResult;
            } catch (RuntimeException ex) {
                RuntimeException translatedException = convertToDataAccessException(ex);
                if (translatedException instanceof ClusterRedirectException) {
                    ClusterRedirectException cre = (ClusterRedirectException) translatedException;
                    NodeResult<T> nodeResultExecuteCommandOnSingleNode = executeCommandOnSingleNode(clusterCommandCallback, this.topologyProvider.getTopology().lookup(cre.getTargetHost(), cre.getTargetPort()), redirectCount + 1);
                    this.resourceProvider.returnResourceForSpecificNode(nodeToUse, resourceForSpecificNode);
                    return nodeResultExecuteCommandOnSingleNode;
                }
                if (translatedException != null) {
                    throw translatedException;
                }
                throw ex;
            }
        } catch (Throwable th) {
            this.resourceProvider.returnResourceForSpecificNode(nodeToUse, resourceForSpecificNode);
            throw th;
        }
    }

    private RedisClusterNode lookupNode(RedisClusterNode node) {
        try {
            return this.topologyProvider.getTopology().lookup(node);
        } catch (ClusterStateFailureException e) {
            throw new IllegalArgumentException(String.format("Node %s is unknown to cluster", node), e);
        }
    }

    public <S, T> MulitNodeResult<T> executeCommandOnAllNodes(ClusterCommandCallback<S, T> cmd) {
        return executeCommandAsyncOnNodes(cmd, getClusterTopology().getActiveMasterNodes());
    }

    public <S, T> MulitNodeResult<T> executeCommandAsyncOnNodes(final ClusterCommandCallback<S, T> callback, Iterable<RedisClusterNode> nodes) {
        Assert.notNull(callback, "Callback must not be null!");
        Assert.notNull(nodes, "Nodes must not be null!");
        List<RedisClusterNode> resolvedRedisClusterNodes = new ArrayList<>();
        ClusterTopology topology = this.topologyProvider.getTopology();
        for (RedisClusterNode node : nodes) {
            try {
                resolvedRedisClusterNodes.add(topology.lookup(node));
            } catch (ClusterStateFailureException e) {
                throw new IllegalArgumentException(String.format("Node %s is unknown to cluster", node), e);
            }
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (final RedisClusterNode node2 : resolvedRedisClusterNodes) {
            linkedHashMap.put(new NodeExecution(node2), this.executor.submit(new Callable<NodeResult<T>>() { // from class: org.springframework.data.redis.connection.ClusterCommandExecutor.1
                @Override // java.util.concurrent.Callable
                public NodeResult<T> call() throws Exception {
                    return ClusterCommandExecutor.this.executeCommandOnSingleNode(callback, node2);
                }
            }));
        }
        return collectResults(linkedHashMap);
    }

    private <T> MulitNodeResult<T> collectResults(Map<NodeExecution, Future<NodeResult<T>>> futures) throws InterruptedException {
        boolean done = false;
        MulitNodeResult<T> result = new MulitNodeResult<>();
        Map<RedisClusterNode, Throwable> exceptions = new HashMap<>();
        Set<String> saveGuard = new HashSet<>();
        while (!done) {
            done = true;
            for (Map.Entry<NodeExecution, Future<NodeResult<T>>> entry : futures.entrySet()) {
                if (!entry.getValue().isDone() && !entry.getValue().isCancelled()) {
                    done = false;
                } else {
                    NodeExecution execution = entry.getKey();
                    try {
                        String futureId = ObjectUtils.getIdentityHexString(entry.getValue());
                        if (!saveGuard.contains(futureId)) {
                            if (execution.isPositional()) {
                                result.add(execution.getPositionalKey(), entry.getValue().get());
                            } else {
                                result.add(entry.getValue().get());
                            }
                            saveGuard.add(futureId);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        RuntimeException ex = convertToDataAccessException((Exception) e.getCause());
                        exceptions.put(execution.getNode(), ex != null ? ex : e.getCause());
                    } catch (ExecutionException e2) {
                        RuntimeException ex2 = convertToDataAccessException((Exception) e2.getCause());
                        exceptions.put(execution.getNode(), ex2 != null ? ex2 : e2.getCause());
                    }
                }
            }
            try {
                Thread.sleep(10L);
            } catch (InterruptedException e3) {
                done = true;
                Thread.currentThread().interrupt();
            }
        }
        if (!exceptions.isEmpty()) {
            throw new ClusterCommandExecutionFailureException(new ArrayList(exceptions.values()));
        }
        return result;
    }

    public <S, T> MulitNodeResult<T> executeMuliKeyCommand(final MultiKeyClusterCommandCallback<S, T> cmd, Iterable<byte[]> keys) {
        Map<RedisClusterNode, PositionalKeys> nodeKeyMap = new HashMap<>();
        int index = 0;
        for (byte[] key : keys) {
            for (RedisClusterNode node : getClusterTopology().getKeyServingNodes(key)) {
                if (nodeKeyMap.containsKey(node)) {
                    int i = index;
                    index++;
                    nodeKeyMap.get(node).append(PositionalKey.of(key, i));
                } else {
                    int i2 = index;
                    index++;
                    nodeKeyMap.put(node, PositionalKeys.of(PositionalKey.of(key, i2)));
                }
            }
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (final Map.Entry<RedisClusterNode, PositionalKeys> entry : nodeKeyMap.entrySet()) {
            if (entry.getKey().isMaster()) {
                Iterator<PositionalKey> it = entry.getValue().iterator();
                while (it.hasNext()) {
                    final PositionalKey key2 = it.next();
                    linkedHashMap.put(new NodeExecution(entry.getKey(), key2), this.executor.submit(new Callable<NodeResult<T>>() { // from class: org.springframework.data.redis.connection.ClusterCommandExecutor.2
                        @Override // java.util.concurrent.Callable
                        public NodeResult<T> call() throws Exception {
                            return ClusterCommandExecutor.this.executeMultiKeyCommandOnSingleNode(cmd, (RedisClusterNode) entry.getKey(), key2.getBytes());
                        }
                    }));
                }
            }
        }
        return collectResults(linkedHashMap);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public <S, T> NodeResult<T> executeMultiKeyCommandOnSingleNode(MultiKeyClusterCommandCallback<S, T> multiKeyClusterCommandCallback, RedisClusterNode node, byte[] key) {
        Assert.notNull(multiKeyClusterCommandCallback, "MultiKeyCommandCallback must not be null!");
        Assert.notNull(node, "RedisClusterNode must not be null!");
        Assert.notNull(key, "Keys for execution must not be null!");
        Object resourceForSpecificNode = this.resourceProvider.getResourceForSpecificNode(node);
        Assert.notNull(resourceForSpecificNode, "Could not acquire resource for node. Is your cluster info up to date?");
        try {
            try {
                NodeResult<T> nodeResult = new NodeResult<>(node, multiKeyClusterCommandCallback.doInCluster(resourceForSpecificNode, key), key);
                this.resourceProvider.returnResourceForSpecificNode(node, resourceForSpecificNode);
                return nodeResult;
            } catch (RuntimeException ex) {
                RuntimeException translatedException = convertToDataAccessException(ex);
                if (translatedException != null) {
                    throw translatedException;
                }
                throw ex;
            }
        } catch (Throwable th) {
            this.resourceProvider.returnResourceForSpecificNode(node, resourceForSpecificNode);
            throw th;
        }
    }

    private ClusterTopology getClusterTopology() {
        return this.topologyProvider.getTopology();
    }

    private DataAccessException convertToDataAccessException(Exception e) {
        return this.exceptionTranslationStrategy.translate(e);
    }

    public void setMaxRedirects(int maxRedirects) {
        this.maxRedirects = maxRedirects;
    }

    @Override // org.springframework.beans.factory.DisposableBean
    public void destroy() throws Exception {
        if (this.executor instanceof DisposableBean) {
            ((DisposableBean) this.executor).destroy();
        }
        if (this.resourceProvider instanceof DisposableBean) {
            ((DisposableBean) this.resourceProvider).destroy();
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/ClusterCommandExecutor$NodeExecution.class */
    private static class NodeExecution {
        private final RedisClusterNode node;
        private final PositionalKey positionalKey;

        NodeExecution(RedisClusterNode node) {
            this(node, null);
        }

        NodeExecution(RedisClusterNode node, PositionalKey positionalKey) {
            this.node = node;
            this.positionalKey = positionalKey;
        }

        RedisClusterNode getNode() {
            return this.node;
        }

        PositionalKey getPositionalKey() {
            return this.positionalKey;
        }

        boolean isPositional() {
            return this.positionalKey != null;
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/ClusterCommandExecutor$NodeResult.class */
    public static class NodeResult<T> {
        private RedisClusterNode node;
        private T value;
        private ByteArrayWrapper key;

        public NodeResult(RedisClusterNode node, T value) {
            this(node, value, new byte[0]);
        }

        public NodeResult(RedisClusterNode node, T value, byte[] key) {
            this.node = node;
            this.value = value;
            this.key = new ByteArrayWrapper(key);
        }

        public T getValue() {
            return this.value;
        }

        public RedisClusterNode getNode() {
            return this.node;
        }

        public byte[] getKey() {
            return this.key.getArray();
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/ClusterCommandExecutor$MulitNodeResult.class */
    public static class MulitNodeResult<T> {
        List<NodeResult<T>> nodeResults = new ArrayList();
        Map<PositionalKey, NodeResult<T>> positionalResults = new LinkedHashMap();

        /* JADX INFO: Access modifiers changed from: private */
        public void add(NodeResult<T> result) {
            this.nodeResults.add(result);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void add(PositionalKey key, NodeResult<T> result) {
            this.positionalResults.put(key, result);
            add(result);
        }

        public List<NodeResult<T>> getResults() {
            return Collections.unmodifiableList(this.nodeResults);
        }

        public List<T> resultsAsList() {
            return toList(this.nodeResults);
        }

        public List<T> resultsAsListSortBy(byte[]... keys) {
            if (this.positionalResults.isEmpty()) {
                List<NodeResult<T>> clone = new ArrayList<>(this.nodeResults);
                clone.sort(new ResultByReferenceKeyPositionComparator(keys));
                return toList(clone);
            }
            Map<PositionalKey, NodeResult<T>> result = new TreeMap<>(new ResultByKeyPositionComparator(keys));
            result.putAll(this.positionalResults);
            ArrayList arrayList = new ArrayList(result.size());
            for (NodeResult<T> value : result.values()) {
                arrayList.add(((NodeResult) value).value);
            }
            return arrayList;
        }

        public T getFirstNonNullNotEmptyOrDefault(T returnValue) {
            for (NodeResult<T> nodeResult : this.nodeResults) {
                if (nodeResult.getValue() != null) {
                    if (nodeResult.getValue() instanceof Map) {
                        if (CollectionUtils.isEmpty((Map<?, ?>) nodeResult.getValue())) {
                            return nodeResult.getValue();
                        }
                    } else {
                        if (CollectionUtils.isEmpty((Collection<?>) nodeResult.getValue())) {
                            return nodeResult.getValue();
                        }
                        return nodeResult.getValue();
                    }
                }
            }
            return returnValue;
        }

        private List<T> toList(Collection<NodeResult<T>> source) {
            ArrayList<T> result = new ArrayList<>();
            for (NodeResult<T> nodeResult : source) {
                result.add(nodeResult.getValue());
            }
            return result;
        }

        /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/ClusterCommandExecutor$MulitNodeResult$ResultByReferenceKeyPositionComparator.class */
        private static class ResultByReferenceKeyPositionComparator implements Comparator<NodeResult<?>> {
            private final List<ByteArrayWrapper> reference;

            ResultByReferenceKeyPositionComparator(byte[]... keys) {
                this.reference = new ArrayList(new ByteArraySet(Arrays.asList(keys)));
            }

            @Override // java.util.Comparator
            public int compare(NodeResult<?> o1, NodeResult<?> o2) {
                return Integer.compare(this.reference.indexOf(((NodeResult) o1).key), this.reference.indexOf(((NodeResult) o2).key));
            }
        }

        /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/ClusterCommandExecutor$MulitNodeResult$ResultByKeyPositionComparator.class */
        private static class ResultByKeyPositionComparator implements Comparator<PositionalKey> {
            private final PositionalKeys reference;

            ResultByKeyPositionComparator(byte[]... keys) {
                this.reference = PositionalKeys.of(keys);
            }

            @Override // java.util.Comparator
            public int compare(PositionalKey o1, PositionalKey o2) {
                return Integer.compare(this.reference.indexOf(o1), this.reference.indexOf(o2));
            }
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/ClusterCommandExecutor$PositionalKey.class */
    static class PositionalKey {
        private final ByteArrayWrapper key;
        private final int position;

        PositionalKey(ByteArrayWrapper key, int position) {
            this.key = key;
            this.position = position;
        }

        static PositionalKey of(byte[] key, int index) {
            return new PositionalKey(new ByteArrayWrapper(key), index);
        }

        byte[] getBytes() {
            return this.key.getArray();
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            PositionalKey that = (PositionalKey) o;
            if (!ObjectUtils.nullSafeEquals(Integer.valueOf(this.position), Integer.valueOf(that.position))) {
                return false;
            }
            return ObjectUtils.nullSafeEquals(this.key, that.key);
        }

        public int hashCode() {
            int result = ObjectUtils.nullSafeHashCode(this.key);
            return (31 * result) + ObjectUtils.nullSafeHashCode(Integer.valueOf(this.position));
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/ClusterCommandExecutor$PositionalKeys.class */
    static class PositionalKeys implements Iterable<PositionalKey> {
        private final List<PositionalKey> keys;

        PositionalKeys(List<PositionalKey> keys) {
            this.keys = keys;
        }

        static PositionalKeys empty() {
            return new PositionalKeys(new ArrayList());
        }

        static PositionalKeys of(byte[]... keys) {
            List<PositionalKey> result = new ArrayList<>(keys.length);
            for (int i = 0; i < keys.length; i++) {
                result.add(PositionalKey.of(keys[i], i));
            }
            return new PositionalKeys(result);
        }

        static PositionalKeys of(PositionalKey... keys) {
            PositionalKeys result = empty();
            result.append(keys);
            return result;
        }

        void append(PositionalKey... keys) {
            this.keys.addAll(Arrays.asList(keys));
        }

        int indexOf(PositionalKey key) {
            return this.keys.indexOf(key);
        }

        @Override // java.lang.Iterable
        public Iterator<PositionalKey> iterator() {
            return this.keys.iterator();
        }
    }
}
