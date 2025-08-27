package redis.clients.jedis;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/JedisMonitor.class */
public abstract class JedisMonitor {
    protected Client client;

    public abstract void onCommand(String str);

    public void proceed(Client client) {
        this.client = client;
        this.client.setTimeoutInfinite();
        do {
            String command = client.getBulkReply();
            onCommand(command);
        } while (client.isConnected());
    }
}
