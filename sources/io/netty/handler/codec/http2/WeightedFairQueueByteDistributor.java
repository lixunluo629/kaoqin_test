package io.netty.handler.codec.http2;

import io.netty.handler.codec.http2.Http2Connection;
import io.netty.handler.codec.http2.Http2Stream;
import io.netty.handler.codec.http2.StreamByteDistributor;
import io.netty.util.collection.IntCollections;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import io.netty.util.internal.DefaultPriorityQueue;
import io.netty.util.internal.EmptyPriorityQueue;
import io.netty.util.internal.MathUtil;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PriorityQueue;
import io.netty.util.internal.PriorityQueueNode;
import io.netty.util.internal.SystemPropertyUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/WeightedFairQueueByteDistributor.class */
public final class WeightedFairQueueByteDistributor implements StreamByteDistributor {
    static final int INITIAL_CHILDREN_MAP_SIZE;
    private static final int DEFAULT_MAX_STATE_ONLY_SIZE = 5;
    private final Http2Connection.PropertyKey stateKey;
    private final IntObjectMap<State> stateOnlyMap;
    private final PriorityQueue<State> stateOnlyRemovalQueue;
    private final Http2Connection connection;
    private final State connectionState;
    private int allocationQuantum;
    private final int maxStateOnlySize;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !WeightedFairQueueByteDistributor.class.desiredAssertionStatus();
        INITIAL_CHILDREN_MAP_SIZE = Math.max(1, SystemPropertyUtil.getInt("io.netty.http2.childrenMapSize", 2));
    }

    public WeightedFairQueueByteDistributor(Http2Connection connection) {
        this(connection, 5);
    }

    public WeightedFairQueueByteDistributor(Http2Connection connection, int maxStateOnlySize) {
        this.allocationQuantum = 1024;
        ObjectUtil.checkPositiveOrZero(maxStateOnlySize, "maxStateOnlySize");
        if (maxStateOnlySize == 0) {
            this.stateOnlyMap = IntCollections.emptyMap();
            this.stateOnlyRemovalQueue = EmptyPriorityQueue.instance();
        } else {
            this.stateOnlyMap = new IntObjectHashMap(maxStateOnlySize);
            this.stateOnlyRemovalQueue = new DefaultPriorityQueue(StateOnlyComparator.INSTANCE, maxStateOnlySize + 2);
        }
        this.maxStateOnlySize = maxStateOnlySize;
        this.connection = connection;
        this.stateKey = connection.newKey();
        Http2Stream connectionStream = connection.connectionStream();
        Http2Connection.PropertyKey propertyKey = this.stateKey;
        State state = new State(this, connectionStream, 16);
        this.connectionState = state;
        connectionStream.setProperty(propertyKey, state);
        connection.addListener(new Http2ConnectionAdapter() { // from class: io.netty.handler.codec.http2.WeightedFairQueueByteDistributor.1
            @Override // io.netty.handler.codec.http2.Http2ConnectionAdapter, io.netty.handler.codec.http2.Http2Connection.Listener
            public void onStreamAdded(Http2Stream stream) {
                State state2 = (State) WeightedFairQueueByteDistributor.this.stateOnlyMap.remove(stream.id());
                if (state2 != null) {
                    WeightedFairQueueByteDistributor.this.stateOnlyRemovalQueue.removeTyped(state2);
                    state2.stream = stream;
                } else {
                    state2 = new State(WeightedFairQueueByteDistributor.this, stream);
                    List<ParentChangedEvent> events = new ArrayList<>(1);
                    WeightedFairQueueByteDistributor.this.connectionState.takeChild(state2, false, events);
                    WeightedFairQueueByteDistributor.this.notifyParentChanged(events);
                }
                switch (AnonymousClass2.$SwitchMap$io$netty$handler$codec$http2$Http2Stream$State[stream.state().ordinal()]) {
                    case 1:
                    case 2:
                        state2.setStreamReservedOrActivated();
                        break;
                }
                stream.setProperty(WeightedFairQueueByteDistributor.this.stateKey, state2);
            }

            @Override // io.netty.handler.codec.http2.Http2ConnectionAdapter, io.netty.handler.codec.http2.Http2Connection.Listener
            public void onStreamActive(Http2Stream stream) {
                WeightedFairQueueByteDistributor.this.state(stream).setStreamReservedOrActivated();
            }

            @Override // io.netty.handler.codec.http2.Http2ConnectionAdapter, io.netty.handler.codec.http2.Http2Connection.Listener
            public void onStreamClosed(Http2Stream stream) {
                WeightedFairQueueByteDistributor.this.state(stream).close();
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // io.netty.handler.codec.http2.Http2ConnectionAdapter, io.netty.handler.codec.http2.Http2Connection.Listener
            public void onStreamRemoved(Http2Stream stream) {
                State state2 = WeightedFairQueueByteDistributor.this.state(stream);
                state2.stream = null;
                if (WeightedFairQueueByteDistributor.this.maxStateOnlySize != 0) {
                    if (WeightedFairQueueByteDistributor.this.stateOnlyRemovalQueue.size() == WeightedFairQueueByteDistributor.this.maxStateOnlySize) {
                        State stateToRemove = (State) WeightedFairQueueByteDistributor.this.stateOnlyRemovalQueue.peek();
                        if (StateOnlyComparator.INSTANCE.compare(stateToRemove, state2) < 0) {
                            WeightedFairQueueByteDistributor.this.stateOnlyRemovalQueue.poll();
                            stateToRemove.parent.removeChild(stateToRemove);
                            WeightedFairQueueByteDistributor.this.stateOnlyMap.remove(stateToRemove.streamId);
                        } else {
                            state2.parent.removeChild(state2);
                            return;
                        }
                    }
                    WeightedFairQueueByteDistributor.this.stateOnlyRemovalQueue.add(state2);
                    WeightedFairQueueByteDistributor.this.stateOnlyMap.put(state2.streamId, (int) state2);
                    return;
                }
                state2.parent.removeChild(state2);
            }
        });
    }

    /* renamed from: io.netty.handler.codec.http2.WeightedFairQueueByteDistributor$2, reason: invalid class name */
    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/WeightedFairQueueByteDistributor$2.class */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$io$netty$handler$codec$http2$Http2Stream$State = new int[Http2Stream.State.values().length];

        static {
            try {
                $SwitchMap$io$netty$handler$codec$http2$Http2Stream$State[Http2Stream.State.RESERVED_REMOTE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$io$netty$handler$codec$http2$Http2Stream$State[Http2Stream.State.RESERVED_LOCAL.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    @Override // io.netty.handler.codec.http2.StreamByteDistributor
    public void updateStreamableBytes(StreamByteDistributor.StreamState state) {
        state(state.stream()).updateStreamableBytes(Http2CodecUtil.streamableBytes(state), state.hasFrame() && state.windowSize() >= 0);
    }

    @Override // io.netty.handler.codec.http2.StreamByteDistributor
    public void updateDependencyTree(int childStreamId, int parentStreamId, short weight, boolean exclusive) {
        List<ParentChangedEvent> events;
        State state = state(childStreamId);
        if (state == null) {
            if (this.maxStateOnlySize == 0) {
                return;
            }
            state = new State(this, childStreamId);
            this.stateOnlyRemovalQueue.add(state);
            this.stateOnlyMap.put(childStreamId, (int) state);
        }
        State newParent = state(parentStreamId);
        if (newParent == null) {
            if (this.maxStateOnlySize == 0) {
                return;
            }
            newParent = new State(this, parentStreamId);
            this.stateOnlyRemovalQueue.add(newParent);
            this.stateOnlyMap.put(parentStreamId, (int) newParent);
            List<ParentChangedEvent> events2 = new ArrayList<>(1);
            this.connectionState.takeChild(newParent, false, events2);
            notifyParentChanged(events2);
        }
        if (state.activeCountForTree != 0 && state.parent != null) {
            state.parent.totalQueuedWeights += weight - state.weight;
        }
        state.weight = weight;
        if (newParent != state.parent || (exclusive && newParent.children.size() != 1)) {
            if (newParent.isDescendantOf(state)) {
                events = new ArrayList<>(2 + (exclusive ? newParent.children.size() : 0));
                state.parent.takeChild(newParent, false, events);
            } else {
                events = new ArrayList<>(1 + (exclusive ? newParent.children.size() : 0));
            }
            newParent.takeChild(state, exclusive, events);
            notifyParentChanged(events);
        }
        while (this.stateOnlyRemovalQueue.size() > this.maxStateOnlySize) {
            State stateToRemove = this.stateOnlyRemovalQueue.poll();
            stateToRemove.parent.removeChild(stateToRemove);
            this.stateOnlyMap.remove(stateToRemove.streamId);
        }
    }

    @Override // io.netty.handler.codec.http2.StreamByteDistributor
    public boolean distribute(int maxBytes, StreamByteDistributor.Writer writer) throws Http2Exception {
        if (this.connectionState.activeCountForTree == 0) {
            return false;
        }
        while (true) {
            int oldIsActiveCountForTree = this.connectionState.activeCountForTree;
            maxBytes -= distributeToChildren(maxBytes, writer, this.connectionState);
            if (this.connectionState.activeCountForTree == 0 || (maxBytes <= 0 && oldIsActiveCountForTree == this.connectionState.activeCountForTree)) {
                break;
            }
        }
        return this.connectionState.activeCountForTree != 0;
    }

    public void allocationQuantum(int allocationQuantum) {
        ObjectUtil.checkPositive(allocationQuantum, "allocationQuantum");
        this.allocationQuantum = allocationQuantum;
    }

    private int distribute(int maxBytes, StreamByteDistributor.Writer writer, State state) throws Http2Exception {
        if (state.isActive()) {
            int nsent = Math.min(maxBytes, state.streamableBytes);
            state.write(nsent, writer);
            if (nsent == 0 && maxBytes != 0) {
                state.updateStreamableBytes(state.streamableBytes, false);
            }
            return nsent;
        }
        return distributeToChildren(maxBytes, writer, state);
    }

    private int distributeToChildren(int maxBytes, StreamByteDistributor.Writer writer, State state) throws Http2Exception {
        long oldTotalQueuedWeights = state.totalQueuedWeights;
        State childState = state.pollPseudoTimeQueue();
        State nextChildState = state.peekPseudoTimeQueue();
        childState.setDistributing();
        try {
            if (!$assertionsDisabled && nextChildState != null && nextChildState.pseudoTimeToWrite < childState.pseudoTimeToWrite) {
                throw new AssertionError("nextChildState[" + nextChildState.streamId + "].pseudoTime(" + nextChildState.pseudoTimeToWrite + ") <  childState[" + childState.streamId + "].pseudoTime(" + childState.pseudoTimeToWrite + ")");
            }
            int nsent = distribute(nextChildState == null ? maxBytes : Math.min(maxBytes, (int) Math.min((((nextChildState.pseudoTimeToWrite - childState.pseudoTimeToWrite) * childState.weight) / oldTotalQueuedWeights) + this.allocationQuantum, 2147483647L)), writer, childState);
            state.pseudoTime += nsent;
            childState.updatePseudoTime(state, nsent, oldTotalQueuedWeights);
            childState.unsetDistributing();
            if (childState.activeCountForTree != 0) {
                state.offerPseudoTimeQueue(childState);
            }
            return nsent;
        } catch (Throwable th) {
            childState.unsetDistributing();
            if (childState.activeCountForTree != 0) {
                state.offerPseudoTimeQueue(childState);
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public State state(Http2Stream stream) {
        return (State) stream.getProperty(this.stateKey);
    }

    private State state(int streamId) {
        Http2Stream stream = this.connection.stream(streamId);
        return stream != null ? state(stream) : this.stateOnlyMap.get(streamId);
    }

    boolean isChild(int childId, int parentId, short weight) {
        State parent = state(parentId);
        if (parent.children.containsKey(childId)) {
            State child = state(childId);
            if (child.parent == parent && child.weight == weight) {
                return true;
            }
        }
        return false;
    }

    int numChildren(int streamId) {
        State state = state(streamId);
        if (state == null) {
            return 0;
        }
        return state.children.size();
    }

    void notifyParentChanged(List<ParentChangedEvent> events) {
        for (int i = 0; i < events.size(); i++) {
            ParentChangedEvent event = events.get(i);
            this.stateOnlyRemovalQueue.priorityChanged(event.state);
            if (event.state.parent != null && event.state.activeCountForTree != 0) {
                event.state.parent.offerAndInitializePseudoTime(event.state);
                event.state.parent.activeCountChangeForTree(event.state.activeCountForTree);
            }
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/WeightedFairQueueByteDistributor$StateOnlyComparator.class */
    private static final class StateOnlyComparator implements Comparator<State>, Serializable {
        private static final long serialVersionUID = -4806936913002105966L;
        static final StateOnlyComparator INSTANCE = new StateOnlyComparator();

        private StateOnlyComparator() {
        }

        @Override // java.util.Comparator
        public int compare(State o1, State o2) {
            boolean o1Actived = o1.wasStreamReservedOrActivated();
            if (o1Actived != o2.wasStreamReservedOrActivated()) {
                return o1Actived ? -1 : 1;
            }
            int x = o2.dependencyTreeDepth - o1.dependencyTreeDepth;
            return x != 0 ? x : o1.streamId - o2.streamId;
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/WeightedFairQueueByteDistributor$StatePseudoTimeComparator.class */
    private static final class StatePseudoTimeComparator implements Comparator<State>, Serializable {
        private static final long serialVersionUID = -1437548640227161828L;
        static final StatePseudoTimeComparator INSTANCE = new StatePseudoTimeComparator();

        private StatePseudoTimeComparator() {
        }

        @Override // java.util.Comparator
        public int compare(State o1, State o2) {
            return MathUtil.compare(o1.pseudoTimeToWrite, o2.pseudoTimeToWrite);
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/WeightedFairQueueByteDistributor$State.class */
    private final class State implements PriorityQueueNode {
        private static final byte STATE_IS_ACTIVE = 1;
        private static final byte STATE_IS_DISTRIBUTING = 2;
        private static final byte STATE_STREAM_ACTIVATED = 4;
        Http2Stream stream;
        State parent;
        IntObjectMap<State> children;
        private final PriorityQueue<State> pseudoTimeQueue;
        final int streamId;
        int streamableBytes;
        int dependencyTreeDepth;
        int activeCountForTree;
        private int pseudoTimeQueueIndex;
        private int stateOnlyQueueIndex;
        long pseudoTimeToWrite;
        long pseudoTime;
        long totalQueuedWeights;
        private byte flags;
        short weight;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !WeightedFairQueueByteDistributor.class.desiredAssertionStatus();
        }

        State(WeightedFairQueueByteDistributor weightedFairQueueByteDistributor, int streamId) {
            this(streamId, null, 0);
        }

        State(WeightedFairQueueByteDistributor weightedFairQueueByteDistributor, Http2Stream stream) {
            this(weightedFairQueueByteDistributor, stream, 0);
        }

        State(WeightedFairQueueByteDistributor weightedFairQueueByteDistributor, Http2Stream stream, int initialSize) {
            this(stream.id(), stream, initialSize);
        }

        State(int streamId, Http2Stream stream, int initialSize) {
            this.children = IntCollections.emptyMap();
            this.pseudoTimeQueueIndex = -1;
            this.stateOnlyQueueIndex = -1;
            this.weight = (short) 16;
            this.stream = stream;
            this.streamId = streamId;
            this.pseudoTimeQueue = new DefaultPriorityQueue(StatePseudoTimeComparator.INSTANCE, initialSize);
        }

        boolean isDescendantOf(State state) {
            State state2 = this.parent;
            while (true) {
                State next = state2;
                if (next != null) {
                    if (next == state) {
                        return true;
                    }
                    state2 = next.parent;
                } else {
                    return false;
                }
            }
        }

        void takeChild(State child, boolean exclusive, List<ParentChangedEvent> events) {
            takeChild(null, child, exclusive, events);
        }

        void takeChild(Iterator<IntObjectMap.PrimitiveEntry<State>> childItr, State child, boolean exclusive, List<ParentChangedEvent> events) {
            State oldParent = child.parent;
            if (oldParent != this) {
                events.add(new ParentChangedEvent(child, oldParent));
                child.setParent(this);
                if (childItr != null) {
                    childItr.remove();
                } else if (oldParent != null) {
                    oldParent.children.remove(child.streamId);
                }
                initChildrenIfEmpty();
                State oldChild = this.children.put(child.streamId, (int) child);
                if (!$assertionsDisabled && oldChild != null) {
                    throw new AssertionError("A stream with the same stream ID was already in the child map.");
                }
            }
            if (exclusive && !this.children.isEmpty()) {
                Iterator<IntObjectMap.PrimitiveEntry<State>> itr = removeAllChildrenExcept(child).entries().iterator();
                while (itr.hasNext()) {
                    child.takeChild(itr, itr.next().value(), false, events);
                }
            }
        }

        void removeChild(State child) {
            if (this.children.remove(child.streamId) != null) {
                List<ParentChangedEvent> events = new ArrayList<>(1 + child.children.size());
                events.add(new ParentChangedEvent(child, child.parent));
                child.setParent(null);
                Iterator<IntObjectMap.PrimitiveEntry<State>> itr = child.children.entries().iterator();
                while (itr.hasNext()) {
                    takeChild(itr, itr.next().value(), false, events);
                }
                WeightedFairQueueByteDistributor.this.notifyParentChanged(events);
            }
        }

        private IntObjectMap<State> removeAllChildrenExcept(State stateToRetain) {
            State stateToRetain2 = this.children.remove(stateToRetain.streamId);
            IntObjectMap<State> prevChildren = this.children;
            initChildren();
            if (stateToRetain2 != null) {
                this.children.put(stateToRetain2.streamId, (int) stateToRetain2);
            }
            return prevChildren;
        }

        private void setParent(State newParent) {
            if (this.activeCountForTree != 0 && this.parent != null) {
                this.parent.removePseudoTimeQueue(this);
                this.parent.activeCountChangeForTree(-this.activeCountForTree);
            }
            this.parent = newParent;
            this.dependencyTreeDepth = newParent == null ? Integer.MAX_VALUE : newParent.dependencyTreeDepth + 1;
        }

        private void initChildrenIfEmpty() {
            if (this.children == IntCollections.emptyMap()) {
                initChildren();
            }
        }

        private void initChildren() {
            this.children = new IntObjectHashMap(WeightedFairQueueByteDistributor.INITIAL_CHILDREN_MAP_SIZE);
        }

        void write(int numBytes, StreamByteDistributor.Writer writer) throws Http2Exception {
            if (!$assertionsDisabled && this.stream == null) {
                throw new AssertionError();
            }
            try {
                writer.write(this.stream, numBytes);
            } catch (Throwable t) {
                throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, t, "byte distribution write error", new Object[0]);
            }
        }

        void activeCountChangeForTree(int increment) {
            if (!$assertionsDisabled && this.activeCountForTree + increment < 0) {
                throw new AssertionError();
            }
            this.activeCountForTree += increment;
            if (this.parent != null) {
                if (!$assertionsDisabled && this.activeCountForTree == increment && this.pseudoTimeQueueIndex != -1 && !this.parent.pseudoTimeQueue.containsTyped(this)) {
                    throw new AssertionError("State[" + this.streamId + "].activeCountForTree changed from 0 to " + increment + " is in a pseudoTimeQueue, but not in parent[ " + this.parent.streamId + "]'s pseudoTimeQueue");
                }
                if (this.activeCountForTree == 0) {
                    this.parent.removePseudoTimeQueue(this);
                } else if (this.activeCountForTree == increment && !isDistributing()) {
                    this.parent.offerAndInitializePseudoTime(this);
                }
                this.parent.activeCountChangeForTree(increment);
            }
        }

        void updateStreamableBytes(int newStreamableBytes, boolean isActive) {
            if (isActive() != isActive) {
                if (isActive) {
                    activeCountChangeForTree(1);
                    setActive();
                } else {
                    activeCountChangeForTree(-1);
                    unsetActive();
                }
            }
            this.streamableBytes = newStreamableBytes;
        }

        void updatePseudoTime(State parentState, int nsent, long totalQueuedWeights) {
            if (!$assertionsDisabled && (this.streamId == 0 || nsent < 0)) {
                throw new AssertionError();
            }
            this.pseudoTimeToWrite = Math.min(this.pseudoTimeToWrite, parentState.pseudoTime) + ((nsent * totalQueuedWeights) / this.weight);
        }

        void offerAndInitializePseudoTime(State state) {
            state.pseudoTimeToWrite = this.pseudoTime;
            offerPseudoTimeQueue(state);
        }

        void offerPseudoTimeQueue(State state) {
            this.pseudoTimeQueue.offer(state);
            this.totalQueuedWeights += state.weight;
        }

        State pollPseudoTimeQueue() {
            State state = this.pseudoTimeQueue.poll();
            this.totalQueuedWeights -= state.weight;
            return state;
        }

        void removePseudoTimeQueue(State state) {
            if (this.pseudoTimeQueue.removeTyped(state)) {
                this.totalQueuedWeights -= state.weight;
            }
        }

        State peekPseudoTimeQueue() {
            return this.pseudoTimeQueue.peek();
        }

        void close() {
            updateStreamableBytes(0, false);
            this.stream = null;
        }

        boolean wasStreamReservedOrActivated() {
            return (this.flags & 4) != 0;
        }

        void setStreamReservedOrActivated() {
            this.flags = (byte) (this.flags | 4);
        }

        boolean isActive() {
            return (this.flags & 1) != 0;
        }

        private void setActive() {
            this.flags = (byte) (this.flags | 1);
        }

        private void unsetActive() {
            this.flags = (byte) (this.flags & (-2));
        }

        boolean isDistributing() {
            return (this.flags & 2) != 0;
        }

        void setDistributing() {
            this.flags = (byte) (this.flags | 2);
        }

        void unsetDistributing() {
            this.flags = (byte) (this.flags & (-3));
        }

        @Override // io.netty.util.internal.PriorityQueueNode
        public int priorityQueueIndex(DefaultPriorityQueue<?> queue) {
            return queue == WeightedFairQueueByteDistributor.this.stateOnlyRemovalQueue ? this.stateOnlyQueueIndex : this.pseudoTimeQueueIndex;
        }

        @Override // io.netty.util.internal.PriorityQueueNode
        public void priorityQueueIndex(DefaultPriorityQueue<?> queue, int i) {
            if (queue == WeightedFairQueueByteDistributor.this.stateOnlyRemovalQueue) {
                this.stateOnlyQueueIndex = i;
            } else {
                this.pseudoTimeQueueIndex = i;
            }
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(256 * (this.activeCountForTree > 0 ? this.activeCountForTree : 1));
            toString(sb);
            return sb.toString();
        }

        private void toString(StringBuilder sb) {
            sb.append("{streamId ").append(this.streamId).append(" streamableBytes ").append(this.streamableBytes).append(" activeCountForTree ").append(this.activeCountForTree).append(" pseudoTimeQueueIndex ").append(this.pseudoTimeQueueIndex).append(" pseudoTimeToWrite ").append(this.pseudoTimeToWrite).append(" pseudoTime ").append(this.pseudoTime).append(" flags ").append((int) this.flags).append(" pseudoTimeQueue.size() ").append(this.pseudoTimeQueue.size()).append(" stateOnlyQueueIndex ").append(this.stateOnlyQueueIndex).append(" parent.streamId ").append(this.parent == null ? -1 : this.parent.streamId).append("} [");
            if (!this.pseudoTimeQueue.isEmpty()) {
                for (State s : this.pseudoTimeQueue) {
                    s.toString(sb);
                    sb.append(", ");
                }
                sb.setLength(sb.length() - 2);
            }
            sb.append(']');
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/WeightedFairQueueByteDistributor$ParentChangedEvent.class */
    private static final class ParentChangedEvent {
        final State state;
        final State oldParent;

        ParentChangedEvent(State state, State oldParent) {
            this.state = state;
            this.oldParent = oldParent;
        }
    }
}
