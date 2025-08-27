package redis.clients.jedis;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/ShardedJedisPipeline.class */
public class ShardedJedisPipeline extends PipelineBase {
    private BinaryShardedJedis jedis;
    private List<FutureResult> results = new ArrayList();
    private Queue<Client> clients = new LinkedList();

    /* loaded from: jedis-2.9.3.jar:redis/clients/jedis/ShardedJedisPipeline$FutureResult.class */
    private static class FutureResult {
        private Client client;

        public FutureResult(Client client) {
            this.client = client;
        }

        public Object get() {
            return this.client.getOne();
        }
    }

    public void setShardedJedis(BinaryShardedJedis jedis) {
        this.jedis = jedis;
    }

    public List<Object> getResults() {
        List<Object> r = new ArrayList<>();
        for (FutureResult fr : this.results) {
            r.add(fr.get());
        }
        return r;
    }

    public void sync() {
        for (Client client : this.clients) {
            generateResponse(client.getOne());
        }
    }

    public List<Object> syncAndReturnAll() {
        List<Object> formatted = new ArrayList<>();
        for (Client client : this.clients) {
            formatted.add(generateResponse(client.getOne()).get());
        }
        return formatted;
    }

    @Deprecated
    public void execute() {
    }

    @Override // redis.clients.jedis.PipelineBase
    protected Client getClient(String key) {
        Client client = this.jedis.getShard(key).getClient();
        this.clients.add(client);
        this.results.add(new FutureResult(client));
        return client;
    }

    @Override // redis.clients.jedis.PipelineBase
    protected Client getClient(byte[] key) {
        Client client = this.jedis.getShard(key).getClient();
        this.clients.add(client);
        this.results.add(new FutureResult(client));
        return client;
    }
}
