package org.springframework.data.redis.connection;

import com.moredian.onpremise.core.common.constants.Constants;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisClusterNode.class */
public class RedisClusterNode extends RedisNode {
    private SlotRange slotRange;
    private LinkState linkState;
    private Set<Flag> flags;

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisClusterNode$LinkState.class */
    public enum LinkState {
        CONNECTED,
        DISCONNECTED
    }

    protected RedisClusterNode() {
    }

    public RedisClusterNode(String host, int port) {
        this(host, port, new SlotRange(Collections.emptySet()));
    }

    public RedisClusterNode(String id) {
        this(new SlotRange(Collections.emptySet()));
        Assert.notNull(id, "Id must not be null");
        this.id = id;
    }

    public RedisClusterNode(String host, int port, SlotRange slotRange) {
        super(host, port);
        this.slotRange = slotRange != null ? slotRange : new SlotRange(Collections.emptySet());
    }

    public RedisClusterNode(SlotRange slotRange) {
        this.slotRange = slotRange != null ? slotRange : new SlotRange(Collections.emptySet());
    }

    public SlotRange getSlotRange() {
        return this.slotRange;
    }

    public boolean servesSlot(int slot) {
        return this.slotRange.contains(slot);
    }

    public LinkState getLinkState() {
        return this.linkState;
    }

    public boolean isConnected() {
        return LinkState.CONNECTED.equals(this.linkState);
    }

    public Set<Flag> getFlags() {
        return this.flags == null ? Collections.emptySet() : this.flags;
    }

    public boolean isMarkedAsFail() {
        if (CollectionUtils.isEmpty(this.flags)) {
            return false;
        }
        return this.flags.contains(Flag.FAIL) || this.flags.contains(Flag.PFAIL);
    }

    @Override // org.springframework.data.redis.connection.RedisNode
    public String toString() {
        return super.toString();
    }

    public static RedisClusterNodeBuilder newRedisClusterNode() {
        return new RedisClusterNodeBuilder();
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisClusterNode$SlotRange.class */
    public static class SlotRange {
        private final Set<Integer> range;

        public SlotRange(Integer lowerBound, Integer upperBound) {
            Assert.notNull(lowerBound, "LowerBound must not be null!");
            Assert.notNull(upperBound, "UpperBound must not be null!");
            this.range = new LinkedHashSet();
            for (int i = lowerBound.intValue(); i <= upperBound.intValue(); i++) {
                this.range.add(Integer.valueOf(i));
            }
        }

        public SlotRange(Collection<Integer> range) {
            this.range = CollectionUtils.isEmpty(range) ? Collections.emptySet() : new LinkedHashSet<>(range);
        }

        public String toString() {
            return this.range.toString();
        }

        public boolean contains(int slot) {
            return this.range.contains(Integer.valueOf(slot));
        }

        public Set<Integer> getSlots() {
            return Collections.unmodifiableSet(this.range);
        }

        public int[] getSlotsArray() {
            int[] slots = new int[this.range.size()];
            int pos = 0;
            for (Integer value : this.range) {
                int i = pos;
                pos++;
                slots[i] = value.intValue();
            }
            return slots;
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisClusterNode$Flag.class */
    public enum Flag {
        MYSELF("myself"),
        MASTER("master"),
        SLAVE("slave"),
        FAIL(Constants.FAIL),
        PFAIL("fail?"),
        HANDSHAKE("handshake"),
        NOADDR("noaddr"),
        NOFLAGS("noflags");

        private String raw;

        Flag(String raw) {
            this.raw = raw;
        }

        public String getRaw() {
            return this.raw;
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisClusterNode$RedisClusterNodeBuilder.class */
    public static class RedisClusterNodeBuilder extends RedisNode.RedisNodeBuilder {
        Set<Flag> flags;
        LinkState linkState;
        SlotRange slotRange;

        @Override // org.springframework.data.redis.connection.RedisNode.RedisNodeBuilder
        public RedisClusterNodeBuilder listeningAt(String host, int port) {
            super.listeningAt(host, port);
            return this;
        }

        @Override // org.springframework.data.redis.connection.RedisNode.RedisNodeBuilder
        public RedisClusterNodeBuilder withName(String name) {
            super.withName(name);
            return this;
        }

        @Override // org.springframework.data.redis.connection.RedisNode.RedisNodeBuilder
        public RedisClusterNodeBuilder withId(String id) {
            super.withId(id);
            return this;
        }

        @Override // org.springframework.data.redis.connection.RedisNode.RedisNodeBuilder
        public RedisClusterNodeBuilder promotedAs(RedisNode.NodeType nodeType) {
            super.promotedAs(nodeType);
            return this;
        }

        @Override // org.springframework.data.redis.connection.RedisNode.RedisNodeBuilder
        public RedisClusterNodeBuilder slaveOf(String masterId) {
            super.slaveOf(masterId);
            return this;
        }

        public RedisClusterNodeBuilder withFlags(Set<Flag> flags) {
            this.flags = flags;
            return this;
        }

        public RedisClusterNodeBuilder serving(SlotRange range) {
            this.slotRange = range;
            return this;
        }

        public RedisClusterNodeBuilder linkState(LinkState linkState) {
            this.linkState = linkState;
            return this;
        }

        @Override // org.springframework.data.redis.connection.RedisNode.RedisNodeBuilder
        public RedisClusterNode build() {
            RedisClusterNode node;
            RedisNode base = super.build();
            if (base.host != null) {
                node = new RedisClusterNode(base.getHost(), base.getPort().intValue(), this.slotRange);
            } else {
                node = new RedisClusterNode(this.slotRange);
            }
            node.id = base.id;
            node.type = base.type;
            node.masterId = base.masterId;
            node.name = base.name;
            node.flags = this.flags;
            node.linkState = this.linkState;
            return node;
        }
    }
}
