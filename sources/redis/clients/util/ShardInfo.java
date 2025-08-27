package redis.clients.util;

/* loaded from: jedis-2.9.3.jar:redis/clients/util/ShardInfo.class */
public abstract class ShardInfo<T> {
    private int weight;

    protected abstract T createResource();

    public abstract String getName();

    public ShardInfo() {
    }

    public ShardInfo(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return this.weight;
    }
}
