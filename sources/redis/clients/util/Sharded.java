package redis.clients.util;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import redis.clients.util.ShardInfo;

/* loaded from: jedis-2.9.3.jar:redis/clients/util/Sharded.class */
public class Sharded<R, S extends ShardInfo<R>> {
    public static final int DEFAULT_WEIGHT = 1;
    private TreeMap<Long, S> nodes;
    private final Hashing algo;
    private final Map<ShardInfo<R>, R> resources;
    private Pattern tagPattern;
    public static final Pattern DEFAULT_KEY_TAG_PATTERN = Pattern.compile("\\{(.+?)\\}");

    public Sharded(List<S> shards) {
        this(shards, Hashing.MURMUR_HASH);
    }

    public Sharded(List<S> shards, Hashing algo) {
        this.resources = new LinkedHashMap();
        this.tagPattern = null;
        this.algo = algo;
        initialize(shards);
    }

    public Sharded(List<S> shards, Pattern tagPattern) {
        this(shards, Hashing.MURMUR_HASH, tagPattern);
    }

    public Sharded(List<S> shards, Hashing algo, Pattern tagPattern) {
        this.resources = new LinkedHashMap();
        this.tagPattern = null;
        this.algo = algo;
        this.tagPattern = tagPattern;
        initialize(shards);
    }

    private void initialize(List<S> list) {
        this.nodes = new TreeMap<>();
        for (int i = 0; i != list.size(); i++) {
            S s = list.get(i);
            if (s.getName() == null) {
                for (int i2 = 0; i2 < 160 * s.getWeight(); i2++) {
                    this.nodes.put(Long.valueOf(this.algo.hash("SHARD-" + i + "-NODE-" + i2)), s);
                }
            } else {
                for (int i3 = 0; i3 < 160 * s.getWeight(); i3++) {
                    this.nodes.put(Long.valueOf(this.algo.hash(s.getName() + "*" + s.getWeight() + i3)), s);
                }
            }
            this.resources.put(s, s.createResource());
        }
    }

    public R getShard(byte[] key) {
        return this.resources.get(getShardInfo(key));
    }

    public R getShard(String key) {
        return this.resources.get(getShardInfo(key));
    }

    public S getShardInfo(byte[] key) {
        SortedMap<Long, S> tail = this.nodes.tailMap(Long.valueOf(this.algo.hash(key)));
        if (tail.isEmpty()) {
            return this.nodes.get(this.nodes.firstKey());
        }
        return tail.get(tail.firstKey());
    }

    public S getShardInfo(String str) {
        return (S) getShardInfo(SafeEncoder.encode(getKeyTag(str)));
    }

    public String getKeyTag(String key) {
        if (this.tagPattern != null) {
            Matcher m = this.tagPattern.matcher(key);
            if (m.find()) {
                return m.group(1);
            }
        }
        return key;
    }

    public Collection<S> getAllShardInfo() {
        return Collections.unmodifiableCollection(this.nodes.values());
    }

    public Collection<R> getAllShards() {
        return Collections.unmodifiableCollection(this.resources.values());
    }
}
