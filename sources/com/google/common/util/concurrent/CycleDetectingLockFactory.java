package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

@Beta
@ThreadSafe
/* loaded from: guava-18.0.jar:com/google/common/util/concurrent/CycleDetectingLockFactory.class */
public class CycleDetectingLockFactory {
    final Policy policy;
    private static final ConcurrentMap<Class<? extends Enum>, Map<? extends Enum, LockGraphNode>> lockGraphNodesPerType = new MapMaker().weakKeys2().makeMap();
    private static final Logger logger = Logger.getLogger(CycleDetectingLockFactory.class.getName());
    private static final ThreadLocal<ArrayList<LockGraphNode>> acquiredLocks = new ThreadLocal<ArrayList<LockGraphNode>>() { // from class: com.google.common.util.concurrent.CycleDetectingLockFactory.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ThreadLocal
        public ArrayList<LockGraphNode> initialValue() {
            return Lists.newArrayListWithCapacity(3);
        }
    };

    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock.class */
    private interface CycleDetectingLock {
        LockGraphNode getLockGraphNode();

        boolean isAcquiredByCurrentThread();
    }

    @Beta
    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/CycleDetectingLockFactory$Policies.class */
    public enum Policies implements Policy {
        THROW { // from class: com.google.common.util.concurrent.CycleDetectingLockFactory.Policies.1
            @Override // com.google.common.util.concurrent.CycleDetectingLockFactory.Policy
            public void handlePotentialDeadlock(PotentialDeadlockException e) {
                throw e;
            }
        },
        WARN { // from class: com.google.common.util.concurrent.CycleDetectingLockFactory.Policies.2
            @Override // com.google.common.util.concurrent.CycleDetectingLockFactory.Policy
            public void handlePotentialDeadlock(PotentialDeadlockException e) {
                CycleDetectingLockFactory.logger.log(Level.SEVERE, "Detected potential deadlock", (Throwable) e);
            }
        },
        DISABLED { // from class: com.google.common.util.concurrent.CycleDetectingLockFactory.Policies.3
            @Override // com.google.common.util.concurrent.CycleDetectingLockFactory.Policy
            public void handlePotentialDeadlock(PotentialDeadlockException e) {
            }
        }
    }

    @Beta
    @ThreadSafe
    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/CycleDetectingLockFactory$Policy.class */
    public interface Policy {
        void handlePotentialDeadlock(PotentialDeadlockException potentialDeadlockException);
    }

    public static CycleDetectingLockFactory newInstance(Policy policy) {
        return new CycleDetectingLockFactory(policy);
    }

    public ReentrantLock newReentrantLock(String lockName) {
        return newReentrantLock(lockName, false);
    }

    public ReentrantLock newReentrantLock(String lockName, boolean fair) {
        return this.policy == Policies.DISABLED ? new ReentrantLock(fair) : new CycleDetectingReentrantLock(new LockGraphNode(lockName), fair);
    }

    public ReentrantReadWriteLock newReentrantReadWriteLock(String lockName) {
        return newReentrantReadWriteLock(lockName, false);
    }

    public ReentrantReadWriteLock newReentrantReadWriteLock(String lockName, boolean fair) {
        return this.policy == Policies.DISABLED ? new ReentrantReadWriteLock(fair) : new CycleDetectingReentrantReadWriteLock(new LockGraphNode(lockName), fair);
    }

    public static <E extends Enum<E>> WithExplicitOrdering<E> newInstanceWithExplicitOrdering(Class<E> enumClass, Policy policy) {
        Preconditions.checkNotNull(enumClass);
        Preconditions.checkNotNull(policy);
        return new WithExplicitOrdering<>(policy, getOrCreateNodes(enumClass));
    }

    private static Map<? extends Enum, LockGraphNode> getOrCreateNodes(Class<? extends Enum> clazz) {
        Map<? extends Enum, LockGraphNode> existing = lockGraphNodesPerType.get(clazz);
        if (existing != null) {
            return existing;
        }
        Map<? extends Enum, LockGraphNode> created = createNodes(clazz);
        return (Map) MoreObjects.firstNonNull(lockGraphNodesPerType.putIfAbsent(clazz, created), created);
    }

    @VisibleForTesting
    static <E extends Enum<E>> Map<E, LockGraphNode> createNodes(Class<E> clazz) {
        EnumMap<E, LockGraphNode> map = Maps.newEnumMap(clazz);
        E[] keys = clazz.getEnumConstants();
        int numKeys = keys.length;
        ArrayList<LockGraphNode> nodes = Lists.newArrayListWithCapacity(numKeys);
        for (E key : keys) {
            LockGraphNode node = new LockGraphNode(getLockName(key));
            nodes.add(node);
            map.put((EnumMap<E, LockGraphNode>) key, (E) node);
        }
        for (int i = 1; i < numKeys; i++) {
            nodes.get(i).checkAcquiredLocks(Policies.THROW, nodes.subList(0, i));
        }
        for (int i2 = 0; i2 < numKeys - 1; i2++) {
            nodes.get(i2).checkAcquiredLocks(Policies.DISABLED, nodes.subList(i2 + 1, numKeys));
        }
        return Collections.unmodifiableMap(map);
    }

    private static String getLockName(Enum<?> rank) {
        String strValueOf = String.valueOf(String.valueOf(rank.getDeclaringClass().getSimpleName()));
        String strValueOf2 = String.valueOf(String.valueOf(rank.name()));
        return new StringBuilder(1 + strValueOf.length() + strValueOf2.length()).append(strValueOf).append(".").append(strValueOf2).toString();
    }

    @Beta
    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/CycleDetectingLockFactory$WithExplicitOrdering.class */
    public static final class WithExplicitOrdering<E extends Enum<E>> extends CycleDetectingLockFactory {
        private final Map<E, LockGraphNode> lockGraphNodes;

        @VisibleForTesting
        WithExplicitOrdering(Policy policy, Map<E, LockGraphNode> lockGraphNodes) {
            super(policy);
            this.lockGraphNodes = lockGraphNodes;
        }

        public ReentrantLock newReentrantLock(E rank) {
            return newReentrantLock((WithExplicitOrdering<E>) rank, false);
        }

        public ReentrantLock newReentrantLock(E rank, boolean fair) {
            return this.policy == Policies.DISABLED ? new ReentrantLock(fair) : new CycleDetectingReentrantLock(this.lockGraphNodes.get(rank), fair);
        }

        public ReentrantReadWriteLock newReentrantReadWriteLock(E rank) {
            return newReentrantReadWriteLock((WithExplicitOrdering<E>) rank, false);
        }

        public ReentrantReadWriteLock newReentrantReadWriteLock(E rank, boolean fair) {
            return this.policy == Policies.DISABLED ? new ReentrantReadWriteLock(fair) : new CycleDetectingReentrantReadWriteLock(this.lockGraphNodes.get(rank), fair);
        }
    }

    private CycleDetectingLockFactory(Policy policy) {
        this.policy = (Policy) Preconditions.checkNotNull(policy);
    }

    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/CycleDetectingLockFactory$ExampleStackTrace.class */
    private static class ExampleStackTrace extends IllegalStateException {
        static final StackTraceElement[] EMPTY_STACK_TRACE = new StackTraceElement[0];
        static Set<String> EXCLUDED_CLASS_NAMES = ImmutableSet.of(CycleDetectingLockFactory.class.getName(), ExampleStackTrace.class.getName(), LockGraphNode.class.getName());

        /* JADX WARN: Illegal instructions before constructor call */
        ExampleStackTrace(LockGraphNode node1, LockGraphNode node2) {
            String strValueOf = String.valueOf(String.valueOf(node1.getLockName()));
            String strValueOf2 = String.valueOf(String.valueOf(node2.getLockName()));
            super(new StringBuilder(4 + strValueOf.length() + strValueOf2.length()).append(strValueOf).append(" -> ").append(strValueOf2).toString());
            StackTraceElement[] origStackTrace = getStackTrace();
            int n = origStackTrace.length;
            for (int i = 0; i < n; i++) {
                if (WithExplicitOrdering.class.getName().equals(origStackTrace[i].getClassName())) {
                    setStackTrace(EMPTY_STACK_TRACE);
                    return;
                } else {
                    if (!EXCLUDED_CLASS_NAMES.contains(origStackTrace[i].getClassName())) {
                        setStackTrace((StackTraceElement[]) Arrays.copyOfRange(origStackTrace, i, n));
                        return;
                    }
                }
            }
        }
    }

    @Beta
    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/CycleDetectingLockFactory$PotentialDeadlockException.class */
    public static final class PotentialDeadlockException extends ExampleStackTrace {
        private final ExampleStackTrace conflictingStackTrace;

        private PotentialDeadlockException(LockGraphNode node1, LockGraphNode node2, ExampleStackTrace conflictingStackTrace) {
            super(node1, node2);
            this.conflictingStackTrace = conflictingStackTrace;
            initCause(conflictingStackTrace);
        }

        public ExampleStackTrace getConflictingStackTrace() {
            return this.conflictingStackTrace;
        }

        @Override // java.lang.Throwable
        public String getMessage() {
            StringBuilder message = new StringBuilder(super.getMessage());
            Throwable cause = this.conflictingStackTrace;
            while (true) {
                Throwable t = cause;
                if (t != null) {
                    message.append(", ").append(t.getMessage());
                    cause = t.getCause();
                } else {
                    return message.toString();
                }
            }
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/CycleDetectingLockFactory$LockGraphNode.class */
    private static class LockGraphNode {
        final Map<LockGraphNode, ExampleStackTrace> allowedPriorLocks = new MapMaker().weakKeys2().makeMap();
        final Map<LockGraphNode, PotentialDeadlockException> disallowedPriorLocks = new MapMaker().weakKeys2().makeMap();
        final String lockName;

        LockGraphNode(String lockName) {
            this.lockName = (String) Preconditions.checkNotNull(lockName);
        }

        String getLockName() {
            return this.lockName;
        }

        void checkAcquiredLocks(Policy policy, List<LockGraphNode> acquiredLocks) {
            int size = acquiredLocks.size();
            for (int i = 0; i < size; i++) {
                checkAcquiredLock(policy, acquiredLocks.get(i));
            }
        }

        void checkAcquiredLock(Policy policy, LockGraphNode acquiredLock) {
            String strConcat;
            boolean z = this != acquiredLock;
            String strValueOf = String.valueOf(acquiredLock.getLockName());
            if (strValueOf.length() != 0) {
                strConcat = "Attempted to acquire multiple locks with the same rank ".concat(strValueOf);
            } else {
                strConcat = str;
                String str = new String("Attempted to acquire multiple locks with the same rank ");
            }
            Preconditions.checkState(z, strConcat);
            if (this.allowedPriorLocks.containsKey(acquiredLock)) {
                return;
            }
            PotentialDeadlockException previousDeadlockException = this.disallowedPriorLocks.get(acquiredLock);
            if (previousDeadlockException != null) {
                policy.handlePotentialDeadlock(new PotentialDeadlockException(acquiredLock, this, previousDeadlockException.getConflictingStackTrace()));
                return;
            }
            Set<LockGraphNode> seen = Sets.newIdentityHashSet();
            ExampleStackTrace path = acquiredLock.findPathTo(this, seen);
            if (path == null) {
                this.allowedPriorLocks.put(acquiredLock, new ExampleStackTrace(acquiredLock, this));
                return;
            }
            PotentialDeadlockException exception = new PotentialDeadlockException(acquiredLock, this, path);
            this.disallowedPriorLocks.put(acquiredLock, exception);
            policy.handlePotentialDeadlock(exception);
        }

        @Nullable
        private ExampleStackTrace findPathTo(LockGraphNode node, Set<LockGraphNode> seen) {
            if (!seen.add(this)) {
                return null;
            }
            ExampleStackTrace found = this.allowedPriorLocks.get(node);
            if (found != null) {
                return found;
            }
            for (Map.Entry<LockGraphNode, ExampleStackTrace> entry : this.allowedPriorLocks.entrySet()) {
                LockGraphNode preAcquiredLock = entry.getKey();
                ExampleStackTrace found2 = preAcquiredLock.findPathTo(node, seen);
                if (found2 != null) {
                    ExampleStackTrace path = new ExampleStackTrace(preAcquiredLock, this);
                    path.setStackTrace(entry.getValue().getStackTrace());
                    path.initCause(found2);
                    return path;
                }
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void aboutToAcquire(CycleDetectingLock lock) {
        if (!lock.isAcquiredByCurrentThread()) {
            ArrayList<LockGraphNode> acquiredLockList = acquiredLocks.get();
            LockGraphNode node = lock.getLockGraphNode();
            node.checkAcquiredLocks(this.policy, acquiredLockList);
            acquiredLockList.add(node);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void lockStateChanged(CycleDetectingLock lock) {
        if (!lock.isAcquiredByCurrentThread()) {
            ArrayList<LockGraphNode> acquiredLockList = acquiredLocks.get();
            LockGraphNode node = lock.getLockGraphNode();
            for (int i = acquiredLockList.size() - 1; i >= 0; i--) {
                if (acquiredLockList.get(i) == node) {
                    acquiredLockList.remove(i);
                    return;
                }
            }
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantLock.class */
    final class CycleDetectingReentrantLock extends ReentrantLock implements CycleDetectingLock {
        private final LockGraphNode lockGraphNode;

        private CycleDetectingReentrantLock(LockGraphNode lockGraphNode, boolean fair) {
            super(fair);
            this.lockGraphNode = (LockGraphNode) Preconditions.checkNotNull(lockGraphNode);
        }

        @Override // com.google.common.util.concurrent.CycleDetectingLockFactory.CycleDetectingLock
        public LockGraphNode getLockGraphNode() {
            return this.lockGraphNode;
        }

        @Override // com.google.common.util.concurrent.CycleDetectingLockFactory.CycleDetectingLock
        public boolean isAcquiredByCurrentThread() {
            return isHeldByCurrentThread();
        }

        @Override // java.util.concurrent.locks.ReentrantLock, java.util.concurrent.locks.Lock
        public void lock() {
            CycleDetectingLockFactory.this.aboutToAcquire(this);
            try {
                super.lock();
                CycleDetectingLockFactory.this.lockStateChanged(this);
            } catch (Throwable th) {
                CycleDetectingLockFactory.this.lockStateChanged(this);
                throw th;
            }
        }

        @Override // java.util.concurrent.locks.ReentrantLock, java.util.concurrent.locks.Lock
        public void lockInterruptibly() throws InterruptedException {
            CycleDetectingLockFactory.this.aboutToAcquire(this);
            try {
                super.lockInterruptibly();
                CycleDetectingLockFactory.this.lockStateChanged(this);
            } catch (Throwable th) {
                CycleDetectingLockFactory.this.lockStateChanged(this);
                throw th;
            }
        }

        @Override // java.util.concurrent.locks.ReentrantLock, java.util.concurrent.locks.Lock
        public boolean tryLock() {
            CycleDetectingLockFactory.this.aboutToAcquire(this);
            try {
                boolean zTryLock = super.tryLock();
                CycleDetectingLockFactory.this.lockStateChanged(this);
                return zTryLock;
            } catch (Throwable th) {
                CycleDetectingLockFactory.this.lockStateChanged(this);
                throw th;
            }
        }

        @Override // java.util.concurrent.locks.ReentrantLock, java.util.concurrent.locks.Lock
        public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
            CycleDetectingLockFactory.this.aboutToAcquire(this);
            try {
                boolean zTryLock = super.tryLock(timeout, unit);
                CycleDetectingLockFactory.this.lockStateChanged(this);
                return zTryLock;
            } catch (Throwable th) {
                CycleDetectingLockFactory.this.lockStateChanged(this);
                throw th;
            }
        }

        @Override // java.util.concurrent.locks.ReentrantLock, java.util.concurrent.locks.Lock
        public void unlock() {
            try {
                super.unlock();
                CycleDetectingLockFactory.this.lockStateChanged(this);
            } catch (Throwable th) {
                CycleDetectingLockFactory.this.lockStateChanged(this);
                throw th;
            }
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadWriteLock.class */
    final class CycleDetectingReentrantReadWriteLock extends ReentrantReadWriteLock implements CycleDetectingLock {
        private final CycleDetectingReentrantReadLock readLock;
        private final CycleDetectingReentrantWriteLock writeLock;
        private final LockGraphNode lockGraphNode;

        private CycleDetectingReentrantReadWriteLock(LockGraphNode lockGraphNode, boolean fair) {
            super(fair);
            this.readLock = CycleDetectingLockFactory.this.new CycleDetectingReentrantReadLock(this);
            this.writeLock = CycleDetectingLockFactory.this.new CycleDetectingReentrantWriteLock(this);
            this.lockGraphNode = (LockGraphNode) Preconditions.checkNotNull(lockGraphNode);
        }

        @Override // java.util.concurrent.locks.ReentrantReadWriteLock, java.util.concurrent.locks.ReadWriteLock
        public ReentrantReadWriteLock.ReadLock readLock() {
            return this.readLock;
        }

        @Override // java.util.concurrent.locks.ReentrantReadWriteLock, java.util.concurrent.locks.ReadWriteLock
        public ReentrantReadWriteLock.WriteLock writeLock() {
            return this.writeLock;
        }

        @Override // com.google.common.util.concurrent.CycleDetectingLockFactory.CycleDetectingLock
        public LockGraphNode getLockGraphNode() {
            return this.lockGraphNode;
        }

        @Override // com.google.common.util.concurrent.CycleDetectingLockFactory.CycleDetectingLock
        public boolean isAcquiredByCurrentThread() {
            return isWriteLockedByCurrentThread() || getReadHoldCount() > 0;
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadLock.class */
    private class CycleDetectingReentrantReadLock extends ReentrantReadWriteLock.ReadLock {
        final CycleDetectingReentrantReadWriteLock readWriteLock;

        CycleDetectingReentrantReadLock(CycleDetectingReentrantReadWriteLock readWriteLock) {
            super(readWriteLock);
            this.readWriteLock = readWriteLock;
        }

        @Override // java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock, java.util.concurrent.locks.Lock
        public void lock() {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                super.lock();
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
            } catch (Throwable th) {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
                throw th;
            }
        }

        @Override // java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock, java.util.concurrent.locks.Lock
        public void lockInterruptibly() throws InterruptedException {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                super.lockInterruptibly();
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
            } catch (Throwable th) {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
                throw th;
            }
        }

        @Override // java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock, java.util.concurrent.locks.Lock
        public boolean tryLock() {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                boolean zTryLock = super.tryLock();
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
                return zTryLock;
            } catch (Throwable th) {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
                throw th;
            }
        }

        @Override // java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock, java.util.concurrent.locks.Lock
        public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                boolean zTryLock = super.tryLock(timeout, unit);
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
                return zTryLock;
            } catch (Throwable th) {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
                throw th;
            }
        }

        @Override // java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock, java.util.concurrent.locks.Lock
        public void unlock() {
            try {
                super.unlock();
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
            } catch (Throwable th) {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
                throw th;
            }
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantWriteLock.class */
    private class CycleDetectingReentrantWriteLock extends ReentrantReadWriteLock.WriteLock {
        final CycleDetectingReentrantReadWriteLock readWriteLock;

        CycleDetectingReentrantWriteLock(CycleDetectingReentrantReadWriteLock readWriteLock) {
            super(readWriteLock);
            this.readWriteLock = readWriteLock;
        }

        @Override // java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock, java.util.concurrent.locks.Lock
        public void lock() {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                super.lock();
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
            } catch (Throwable th) {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
                throw th;
            }
        }

        @Override // java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock, java.util.concurrent.locks.Lock
        public void lockInterruptibly() throws InterruptedException {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                super.lockInterruptibly();
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
            } catch (Throwable th) {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
                throw th;
            }
        }

        @Override // java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock, java.util.concurrent.locks.Lock
        public boolean tryLock() {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                boolean zTryLock = super.tryLock();
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
                return zTryLock;
            } catch (Throwable th) {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
                throw th;
            }
        }

        @Override // java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock, java.util.concurrent.locks.Lock
        public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                boolean zTryLock = super.tryLock(timeout, unit);
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
                return zTryLock;
            } catch (Throwable th) {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
                throw th;
            }
        }

        @Override // java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock, java.util.concurrent.locks.Lock
        public void unlock() {
            try {
                super.unlock();
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
            } catch (Throwable th) {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
                throw th;
            }
        }
    }
}
