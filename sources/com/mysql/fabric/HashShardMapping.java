package com.mysql.fabric;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/fabric/HashShardMapping.class */
public class HashShardMapping extends ShardMapping {
    private static final MessageDigest md5Hasher;

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/fabric/HashShardMapping$ReverseShardIndexSorter.class */
    private static class ReverseShardIndexSorter implements Comparator<ShardIndex> {
        public static final ReverseShardIndexSorter instance = new ReverseShardIndexSorter();

        private ReverseShardIndexSorter() {
        }

        @Override // java.util.Comparator
        public int compare(ShardIndex i1, ShardIndex i2) {
            return i2.getBound().compareTo(i1.getBound());
        }
    }

    static {
        try {
            md5Hasher = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
        } catch (NoSuchAlgorithmException ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public HashShardMapping(int mappingId, ShardingType shardingType, String globalGroupName, Set<ShardTable> shardTables, Set<ShardIndex> shardIndices) {
        super(mappingId, shardingType, globalGroupName, shardTables, new TreeSet(ReverseShardIndexSorter.instance));
        this.shardIndices.addAll(shardIndices);
    }

    @Override // com.mysql.fabric.ShardMapping
    protected ShardIndex getShardIndexForKey(String stringKey) {
        String hashedKey;
        synchronized (md5Hasher) {
            hashedKey = new BigInteger(1, md5Hasher.digest(stringKey.getBytes())).toString(16).toUpperCase();
        }
        for (int i = 0; i < 32 - hashedKey.length(); i++) {
            hashedKey = "0" + hashedKey;
        }
        for (ShardIndex i2 : this.shardIndices) {
            if (i2.getBound().compareTo(hashedKey) <= 0) {
                return i2;
            }
        }
        return this.shardIndices.iterator().next();
    }
}
