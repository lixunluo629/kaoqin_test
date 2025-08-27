package com.mysql.fabric;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/fabric/ShardIndex.class */
public class ShardIndex {
    private String bound;
    private Integer shardId;
    private String groupName;

    public ShardIndex(String bound, Integer shardId, String groupName) {
        this.bound = bound;
        this.shardId = shardId;
        this.groupName = groupName;
    }

    public String getBound() {
        return this.bound;
    }

    public Integer getShardId() {
        return this.shardId;
    }

    public String getGroupName() {
        return this.groupName;
    }
}
