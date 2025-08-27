package com.mysql.fabric;

import java.util.Collections;
import java.util.Set;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/fabric/ShardMapping.class */
public abstract class ShardMapping {
    private int mappingId;
    private ShardingType shardingType;
    private String globalGroupName;
    protected Set<ShardTable> shardTables;
    protected Set<ShardIndex> shardIndices;

    protected abstract ShardIndex getShardIndexForKey(String str);

    public ShardMapping(int mappingId, ShardingType shardingType, String globalGroupName, Set<ShardTable> shardTables, Set<ShardIndex> shardIndices) {
        this.mappingId = mappingId;
        this.shardingType = shardingType;
        this.globalGroupName = globalGroupName;
        this.shardTables = shardTables;
        this.shardIndices = shardIndices;
    }

    public String getGroupNameForKey(String key) {
        return getShardIndexForKey(key).getGroupName();
    }

    public int getMappingId() {
        return this.mappingId;
    }

    public ShardingType getShardingType() {
        return this.shardingType;
    }

    public String getGlobalGroupName() {
        return this.globalGroupName;
    }

    public Set<ShardTable> getShardTables() {
        return Collections.unmodifiableSet(this.shardTables);
    }

    public Set<ShardIndex> getShardIndices() {
        return Collections.unmodifiableSet(this.shardIndices);
    }
}
