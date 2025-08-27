package redis.clients.jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/MultiKeyPipelineBase.class */
public abstract class MultiKeyPipelineBase extends PipelineBase implements BasicRedisPipeline, MultiKeyBinaryRedisPipeline, MultiKeyCommandsPipeline, ClusterPipeline, BinaryScriptingCommandsPipeline {
    protected Client client = null;

    @Override // redis.clients.jedis.MultiKeyCommandsPipeline
    public Response<List<String>> brpop(String... args) {
        this.client.brpop(args);
        return getResponse(BuilderFactory.STRING_LIST);
    }

    public Response<List<String>> brpop(int timeout, String... keys) {
        this.client.brpop(timeout, keys);
        return getResponse(BuilderFactory.STRING_LIST);
    }

    @Override // redis.clients.jedis.MultiKeyCommandsPipeline
    public Response<List<String>> blpop(String... args) {
        this.client.blpop(args);
        return getResponse(BuilderFactory.STRING_LIST);
    }

    public Response<List<String>> blpop(int timeout, String... keys) {
        this.client.blpop(timeout, keys);
        return getResponse(BuilderFactory.STRING_LIST);
    }

    public Response<Map<String, String>> blpopMap(int timeout, String... keys) {
        this.client.blpop(timeout, keys);
        return getResponse(BuilderFactory.STRING_MAP);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryRedisPipeline
    public Response<List<byte[]>> brpop(byte[]... args) {
        this.client.brpop(args);
        return getResponse(BuilderFactory.BYTE_ARRAY_LIST);
    }

    public Response<List<String>> brpop(int timeout, byte[]... keys) {
        this.client.brpop(timeout, keys);
        return getResponse(BuilderFactory.STRING_LIST);
    }

    public Response<Map<String, String>> brpopMap(int timeout, String... keys) {
        this.client.blpop(timeout, keys);
        return getResponse(BuilderFactory.STRING_MAP);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryRedisPipeline
    public Response<List<byte[]>> blpop(byte[]... args) {
        this.client.blpop(args);
        return getResponse(BuilderFactory.BYTE_ARRAY_LIST);
    }

    public Response<List<String>> blpop(int timeout, byte[]... keys) {
        this.client.blpop(timeout, keys);
        return getResponse(BuilderFactory.STRING_LIST);
    }

    @Override // redis.clients.jedis.MultiKeyCommandsPipeline
    public Response<Long> del(String... keys) {
        this.client.del(keys);
        return getResponse(BuilderFactory.LONG);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryRedisPipeline
    public Response<Long> del(byte[]... keys) {
        this.client.del(keys);
        return getResponse(BuilderFactory.LONG);
    }

    @Override // redis.clients.jedis.MultiKeyCommandsPipeline
    public Response<Long> exists(String... keys) {
        this.client.exists(keys);
        return getResponse(BuilderFactory.LONG);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryRedisPipeline
    public Response<Long> exists(byte[]... keys) {
        this.client.exists(keys);
        return getResponse(BuilderFactory.LONG);
    }

    @Override // redis.clients.jedis.MultiKeyCommandsPipeline
    public Response<Set<String>> keys(String pattern) {
        getClient(pattern).keys(pattern);
        return getResponse(BuilderFactory.STRING_SET);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryRedisPipeline
    public Response<Set<byte[]>> keys(byte[] pattern) {
        getClient(pattern).keys(pattern);
        return getResponse(BuilderFactory.BYTE_ARRAY_ZSET);
    }

    @Override // redis.clients.jedis.MultiKeyCommandsPipeline
    public Response<List<String>> mget(String... keys) {
        this.client.mget(keys);
        return getResponse(BuilderFactory.STRING_LIST);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryRedisPipeline
    public Response<List<byte[]>> mget(byte[]... keys) {
        this.client.mget(keys);
        return getResponse(BuilderFactory.BYTE_ARRAY_LIST);
    }

    @Override // redis.clients.jedis.MultiKeyCommandsPipeline
    public Response<String> mset(String... keysvalues) {
        this.client.mset(keysvalues);
        return getResponse(BuilderFactory.STRING);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryRedisPipeline
    public Response<String> mset(byte[]... keysvalues) {
        this.client.mset(keysvalues);
        return getResponse(BuilderFactory.STRING);
    }

    @Override // redis.clients.jedis.MultiKeyCommandsPipeline
    public Response<Long> msetnx(String... keysvalues) {
        this.client.msetnx(keysvalues);
        return getResponse(BuilderFactory.LONG);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryRedisPipeline
    public Response<Long> msetnx(byte[]... keysvalues) {
        this.client.msetnx(keysvalues);
        return getResponse(BuilderFactory.LONG);
    }

    @Override // redis.clients.jedis.MultiKeyCommandsPipeline
    public Response<String> rename(String oldkey, String newkey) {
        this.client.rename(oldkey, newkey);
        return getResponse(BuilderFactory.STRING);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryRedisPipeline
    public Response<String> rename(byte[] oldkey, byte[] newkey) {
        this.client.rename(oldkey, newkey);
        return getResponse(BuilderFactory.STRING);
    }

    @Override // redis.clients.jedis.MultiKeyCommandsPipeline
    public Response<Long> renamenx(String oldkey, String newkey) {
        this.client.renamenx(oldkey, newkey);
        return getResponse(BuilderFactory.LONG);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryRedisPipeline
    public Response<Long> renamenx(byte[] oldkey, byte[] newkey) {
        this.client.renamenx(oldkey, newkey);
        return getResponse(BuilderFactory.LONG);
    }

    @Override // redis.clients.jedis.MultiKeyCommandsPipeline
    public Response<String> rpoplpush(String srckey, String dstkey) {
        this.client.rpoplpush(srckey, dstkey);
        return getResponse(BuilderFactory.STRING);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryRedisPipeline
    public Response<byte[]> rpoplpush(byte[] srckey, byte[] dstkey) {
        this.client.rpoplpush(srckey, dstkey);
        return getResponse(BuilderFactory.BYTE_ARRAY);
    }

    @Override // redis.clients.jedis.MultiKeyCommandsPipeline
    public Response<Set<String>> sdiff(String... keys) {
        this.client.sdiff(keys);
        return getResponse(BuilderFactory.STRING_SET);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryRedisPipeline
    public Response<Set<byte[]>> sdiff(byte[]... keys) {
        this.client.sdiff(keys);
        return getResponse(BuilderFactory.BYTE_ARRAY_ZSET);
    }

    @Override // redis.clients.jedis.MultiKeyCommandsPipeline
    public Response<Long> sdiffstore(String dstkey, String... keys) {
        this.client.sdiffstore(dstkey, keys);
        return getResponse(BuilderFactory.LONG);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryRedisPipeline
    public Response<Long> sdiffstore(byte[] dstkey, byte[]... keys) {
        this.client.sdiffstore(dstkey, keys);
        return getResponse(BuilderFactory.LONG);
    }

    @Override // redis.clients.jedis.MultiKeyCommandsPipeline
    public Response<Set<String>> sinter(String... keys) {
        this.client.sinter(keys);
        return getResponse(BuilderFactory.STRING_SET);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryRedisPipeline
    public Response<Set<byte[]>> sinter(byte[]... keys) {
        this.client.sinter(keys);
        return getResponse(BuilderFactory.BYTE_ARRAY_ZSET);
    }

    @Override // redis.clients.jedis.MultiKeyCommandsPipeline
    public Response<Long> sinterstore(String dstkey, String... keys) {
        this.client.sinterstore(dstkey, keys);
        return getResponse(BuilderFactory.LONG);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryRedisPipeline
    public Response<Long> sinterstore(byte[] dstkey, byte[]... keys) {
        this.client.sinterstore(dstkey, keys);
        return getResponse(BuilderFactory.LONG);
    }

    @Override // redis.clients.jedis.MultiKeyCommandsPipeline
    public Response<Long> smove(String srckey, String dstkey, String member) {
        this.client.smove(srckey, dstkey, member);
        return getResponse(BuilderFactory.LONG);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryRedisPipeline
    public Response<Long> smove(byte[] srckey, byte[] dstkey, byte[] member) {
        this.client.smove(srckey, dstkey, member);
        return getResponse(BuilderFactory.LONG);
    }

    @Override // redis.clients.jedis.MultiKeyCommandsPipeline
    public Response<Long> sort(String key, SortingParams sortingParameters, String dstkey) {
        this.client.sort(key, sortingParameters, dstkey);
        return getResponse(BuilderFactory.LONG);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryRedisPipeline
    public Response<Long> sort(byte[] key, SortingParams sortingParameters, byte[] dstkey) {
        this.client.sort(key, sortingParameters, dstkey);
        return getResponse(BuilderFactory.LONG);
    }

    @Override // redis.clients.jedis.MultiKeyCommandsPipeline
    public Response<Long> sort(String key, String dstkey) {
        this.client.sort(key, dstkey);
        return getResponse(BuilderFactory.LONG);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryRedisPipeline
    public Response<Long> sort(byte[] key, byte[] dstkey) {
        this.client.sort(key, dstkey);
        return getResponse(BuilderFactory.LONG);
    }

    @Override // redis.clients.jedis.MultiKeyCommandsPipeline
    public Response<Set<String>> sunion(String... keys) {
        this.client.sunion(keys);
        return getResponse(BuilderFactory.STRING_SET);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryRedisPipeline
    public Response<Set<byte[]>> sunion(byte[]... keys) {
        this.client.sunion(keys);
        return getResponse(BuilderFactory.BYTE_ARRAY_ZSET);
    }

    @Override // redis.clients.jedis.MultiKeyCommandsPipeline
    public Response<Long> sunionstore(String dstkey, String... keys) {
        this.client.sunionstore(dstkey, keys);
        return getResponse(BuilderFactory.LONG);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryRedisPipeline
    public Response<Long> sunionstore(byte[] dstkey, byte[]... keys) {
        this.client.sunionstore(dstkey, keys);
        return getResponse(BuilderFactory.LONG);
    }

    @Override // redis.clients.jedis.MultiKeyCommandsPipeline
    public Response<String> watch(String... keys) {
        this.client.watch(keys);
        return getResponse(BuilderFactory.STRING);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryRedisPipeline
    public Response<String> watch(byte[]... keys) {
        this.client.watch(keys);
        return getResponse(BuilderFactory.STRING);
    }

    @Override // redis.clients.jedis.MultiKeyCommandsPipeline
    public Response<Long> zinterstore(String dstkey, String... sets) {
        this.client.zinterstore(dstkey, sets);
        return getResponse(BuilderFactory.LONG);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryRedisPipeline
    public Response<Long> zinterstore(byte[] dstkey, byte[]... sets) {
        this.client.zinterstore(dstkey, sets);
        return getResponse(BuilderFactory.LONG);
    }

    @Override // redis.clients.jedis.MultiKeyCommandsPipeline
    public Response<Long> zinterstore(String dstkey, ZParams params, String... sets) {
        this.client.zinterstore(dstkey, params, sets);
        return getResponse(BuilderFactory.LONG);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryRedisPipeline
    public Response<Long> zinterstore(byte[] dstkey, ZParams params, byte[]... sets) {
        this.client.zinterstore(dstkey, params, sets);
        return getResponse(BuilderFactory.LONG);
    }

    @Override // redis.clients.jedis.MultiKeyCommandsPipeline
    public Response<Long> zunionstore(String dstkey, String... sets) {
        this.client.zunionstore(dstkey, sets);
        return getResponse(BuilderFactory.LONG);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryRedisPipeline
    public Response<Long> zunionstore(byte[] dstkey, byte[]... sets) {
        this.client.zunionstore(dstkey, sets);
        return getResponse(BuilderFactory.LONG);
    }

    @Override // redis.clients.jedis.MultiKeyCommandsPipeline
    public Response<Long> zunionstore(String dstkey, ZParams params, String... sets) {
        this.client.zunionstore(dstkey, params, sets);
        return getResponse(BuilderFactory.LONG);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryRedisPipeline
    public Response<Long> zunionstore(byte[] dstkey, ZParams params, byte[]... sets) {
        this.client.zunionstore(dstkey, params, sets);
        return getResponse(BuilderFactory.LONG);
    }

    @Override // redis.clients.jedis.BasicRedisPipeline
    public Response<String> bgrewriteaof() {
        this.client.bgrewriteaof();
        return getResponse(BuilderFactory.STRING);
    }

    @Override // redis.clients.jedis.BasicRedisPipeline
    public Response<String> bgsave() {
        this.client.bgsave();
        return getResponse(BuilderFactory.STRING);
    }

    @Override // redis.clients.jedis.BasicRedisPipeline
    public Response<List<String>> configGet(String pattern) {
        this.client.configGet(pattern);
        return getResponse(BuilderFactory.STRING_LIST);
    }

    @Override // redis.clients.jedis.BasicRedisPipeline
    public Response<String> configSet(String parameter, String value) {
        this.client.configSet(parameter, value);
        return getResponse(BuilderFactory.STRING);
    }

    @Override // redis.clients.jedis.MultiKeyCommandsPipeline
    public Response<String> brpoplpush(String source, String destination, int timeout) {
        this.client.brpoplpush(source, destination, timeout);
        return getResponse(BuilderFactory.STRING);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryRedisPipeline
    public Response<byte[]> brpoplpush(byte[] source, byte[] destination, int timeout) {
        this.client.brpoplpush(source, destination, timeout);
        return getResponse(BuilderFactory.BYTE_ARRAY);
    }

    @Override // redis.clients.jedis.BasicRedisPipeline
    public Response<String> configResetStat() {
        this.client.configResetStat();
        return getResponse(BuilderFactory.STRING);
    }

    @Override // redis.clients.jedis.BasicRedisPipeline
    public Response<String> save() {
        this.client.save();
        return getResponse(BuilderFactory.STRING);
    }

    @Override // redis.clients.jedis.BasicRedisPipeline
    public Response<Long> lastsave() {
        this.client.lastsave();
        return getResponse(BuilderFactory.LONG);
    }

    @Override // redis.clients.jedis.MultiKeyCommandsPipeline
    public Response<Long> publish(String channel, String message) {
        this.client.publish(channel, message);
        return getResponse(BuilderFactory.LONG);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryRedisPipeline
    public Response<Long> publish(byte[] channel, byte[] message) {
        this.client.publish(channel, message);
        return getResponse(BuilderFactory.LONG);
    }

    @Override // redis.clients.jedis.MultiKeyCommandsPipeline
    public Response<String> randomKey() {
        this.client.randomKey();
        return getResponse(BuilderFactory.STRING);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryRedisPipeline
    public Response<byte[]> randomKeyBinary() {
        this.client.randomKey();
        return getResponse(BuilderFactory.BYTE_ARRAY);
    }

    @Override // redis.clients.jedis.BasicRedisPipeline
    public Response<String> flushDB() {
        this.client.flushDB();
        return getResponse(BuilderFactory.STRING);
    }

    @Override // redis.clients.jedis.BasicRedisPipeline
    public Response<String> flushAll() {
        this.client.flushAll();
        return getResponse(BuilderFactory.STRING);
    }

    @Override // redis.clients.jedis.BasicRedisPipeline
    public Response<String> info() {
        this.client.info();
        return getResponse(BuilderFactory.STRING);
    }

    public Response<String> info(String section) {
        this.client.info(section);
        return getResponse(BuilderFactory.STRING);
    }

    @Override // redis.clients.jedis.BasicRedisPipeline
    public Response<List<String>> time() {
        this.client.time();
        return getResponse(BuilderFactory.STRING_LIST);
    }

    @Override // redis.clients.jedis.BasicRedisPipeline
    public Response<Long> dbSize() {
        this.client.dbSize();
        return getResponse(BuilderFactory.LONG);
    }

    @Override // redis.clients.jedis.BasicRedisPipeline
    public Response<String> shutdown() {
        this.client.shutdown();
        return getResponse(BuilderFactory.STRING);
    }

    @Override // redis.clients.jedis.BasicRedisPipeline
    public Response<String> ping() {
        this.client.ping();
        return getResponse(BuilderFactory.STRING);
    }

    @Override // redis.clients.jedis.BasicRedisPipeline
    public Response<String> select(int index) {
        this.client.select(index);
        Response<String> response = getResponse(BuilderFactory.STRING);
        this.client.setDb(index);
        return response;
    }

    @Override // redis.clients.jedis.MultiKeyBinaryRedisPipeline
    public Response<Long> bitop(BitOP op, byte[] destKey, byte[]... srcKeys) {
        this.client.bitop(op, destKey, srcKeys);
        return getResponse(BuilderFactory.LONG);
    }

    @Override // redis.clients.jedis.MultiKeyCommandsPipeline
    public Response<Long> bitop(BitOP op, String destKey, String... srcKeys) {
        this.client.bitop(op, destKey, srcKeys);
        return getResponse(BuilderFactory.LONG);
    }

    @Override // redis.clients.jedis.ClusterPipeline
    public Response<String> clusterNodes() {
        this.client.clusterNodes();
        return getResponse(BuilderFactory.STRING);
    }

    @Override // redis.clients.jedis.ClusterPipeline
    public Response<String> clusterMeet(String ip, int port) {
        this.client.clusterMeet(ip, port);
        return getResponse(BuilderFactory.STRING);
    }

    @Override // redis.clients.jedis.ClusterPipeline
    public Response<String> clusterAddSlots(int... slots) {
        this.client.clusterAddSlots(slots);
        return getResponse(BuilderFactory.STRING);
    }

    @Override // redis.clients.jedis.ClusterPipeline
    public Response<String> clusterDelSlots(int... slots) {
        this.client.clusterDelSlots(slots);
        return getResponse(BuilderFactory.STRING);
    }

    @Override // redis.clients.jedis.ClusterPipeline
    public Response<String> clusterInfo() {
        this.client.clusterInfo();
        return getResponse(BuilderFactory.STRING);
    }

    @Override // redis.clients.jedis.ClusterPipeline
    public Response<List<String>> clusterGetKeysInSlot(int slot, int count) {
        this.client.clusterGetKeysInSlot(slot, count);
        return getResponse(BuilderFactory.STRING_LIST);
    }

    @Override // redis.clients.jedis.ClusterPipeline
    public Response<String> clusterSetSlotNode(int slot, String nodeId) {
        this.client.clusterSetSlotNode(slot, nodeId);
        return getResponse(BuilderFactory.STRING);
    }

    @Override // redis.clients.jedis.ClusterPipeline
    public Response<String> clusterSetSlotMigrating(int slot, String nodeId) {
        this.client.clusterSetSlotMigrating(slot, nodeId);
        return getResponse(BuilderFactory.STRING);
    }

    @Override // redis.clients.jedis.ClusterPipeline
    public Response<String> clusterSetSlotImporting(int slot, String nodeId) {
        this.client.clusterSetSlotImporting(slot, nodeId);
        return getResponse(BuilderFactory.STRING);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v1, types: [byte[], byte[][]] */
    @Override // redis.clients.jedis.BinaryScriptingCommandsPipeline
    public Response<Object> eval(byte[] script) {
        return eval(script, 0, (byte[][]) new byte[0]);
    }

    @Override // redis.clients.jedis.BinaryScriptingCommandsPipeline
    public Response<Object> eval(byte[] script, byte[] keyCount, byte[]... params) {
        getClient(script).eval(script, keyCount, params);
        return getResponse(BuilderFactory.EVAL_BINARY_RESULT);
    }

    @Override // redis.clients.jedis.BinaryScriptingCommandsPipeline
    public Response<Object> eval(byte[] script, List<byte[]> keys, List<byte[]> args) {
        byte[][] argv = BinaryJedis.getParamsWithBinary(keys, args);
        return eval(script, keys.size(), argv);
    }

    @Override // redis.clients.jedis.BinaryScriptingCommandsPipeline
    public Response<Object> eval(byte[] script, int keyCount, byte[]... params) {
        getClient(script).eval(script, keyCount, params);
        return getResponse(BuilderFactory.EVAL_BINARY_RESULT);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v1, types: [byte[], byte[][]] */
    @Override // redis.clients.jedis.BinaryScriptingCommandsPipeline
    public Response<Object> evalsha(byte[] sha1) {
        return evalsha(sha1, 0, (byte[][]) new byte[0]);
    }

    @Override // redis.clients.jedis.BinaryScriptingCommandsPipeline
    public Response<Object> evalsha(byte[] sha1, List<byte[]> keys, List<byte[]> args) {
        byte[][] argv = BinaryJedis.getParamsWithBinary(keys, args);
        return evalsha(sha1, keys.size(), argv);
    }

    @Override // redis.clients.jedis.BinaryScriptingCommandsPipeline
    public Response<Object> evalsha(byte[] sha1, int keyCount, byte[]... params) {
        getClient(sha1).evalsha(sha1, keyCount, params);
        return getResponse(BuilderFactory.EVAL_BINARY_RESULT);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryRedisPipeline
    public Response<String> pfmerge(byte[] destkey, byte[]... sourcekeys) {
        this.client.pfmerge(destkey, sourcekeys);
        return getResponse(BuilderFactory.STRING);
    }

    @Override // redis.clients.jedis.MultiKeyCommandsPipeline
    public Response<String> pfmerge(String destkey, String... sourcekeys) {
        this.client.pfmerge(destkey, sourcekeys);
        return getResponse(BuilderFactory.STRING);
    }

    @Override // redis.clients.jedis.MultiKeyCommandsPipeline
    public Response<Long> pfcount(String... keys) {
        this.client.pfcount(keys);
        return getResponse(BuilderFactory.LONG);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryRedisPipeline
    public Response<Long> pfcount(byte[]... keys) {
        this.client.pfcount(keys);
        return getResponse(BuilderFactory.LONG);
    }
}
