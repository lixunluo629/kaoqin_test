package com.mysql.fabric;

import java.util.Set;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/fabric/ShardMappingFactory.class */
public class ShardMappingFactory {
    public ShardMapping createShardMapping(int mappingId, ShardingType shardingType, String globalGroupName, Set<ShardTable> shardTables, Set<ShardIndex> shardIndices) {
        ShardMapping sm;
        switch (shardingType) {
            case RANGE:
                sm = new RangeShardMapping(mappingId, shardingType, globalGroupName, shardTables, shardIndices);
                break;
            case HASH:
                sm = new HashShardMapping(mappingId, shardingType, globalGroupName, shardTables, shardIndices);
                break;
            default:
                throw new IllegalArgumentException("Invalid ShardingType");
        }
        return sm;
    }
}
