package com.mysql.fabric;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/fabric/RangeShardMapping.class */
public class RangeShardMapping extends ShardMapping {

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/fabric/RangeShardMapping$RangeShardIndexSorter.class */
    private static class RangeShardIndexSorter implements Comparator<ShardIndex> {
        public static final RangeShardIndexSorter instance = new RangeShardIndexSorter();

        private RangeShardIndexSorter() {
        }

        @Override // java.util.Comparator
        public int compare(ShardIndex i1, ShardIndex i2) {
            Integer bound1 = Integer.valueOf(Integer.parseInt(i1.getBound()));
            Integer bound2 = Integer.valueOf(Integer.parseInt(i2.getBound()));
            return bound2.compareTo(bound1);
        }
    }

    public RangeShardMapping(int mappingId, ShardingType shardingType, String globalGroupName, Set<ShardTable> shardTables, Set<ShardIndex> shardIndices) {
        super(mappingId, shardingType, globalGroupName, shardTables, new TreeSet(RangeShardIndexSorter.instance));
        this.shardIndices.addAll(shardIndices);
    }

    @Override // com.mysql.fabric.ShardMapping
    protected ShardIndex getShardIndexForKey(String stringKey) throws NumberFormatException {
        Integer key = Integer.valueOf(Integer.parseInt(stringKey));
        for (ShardIndex i : this.shardIndices) {
            Integer lowerBound = Integer.valueOf(i.getBound());
            if (key.intValue() >= lowerBound.intValue()) {
                return i;
            }
        }
        return null;
    }
}
