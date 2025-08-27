package redis.clients.jedis;

import java.util.Arrays;
import java.util.List;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.SafeEncoder;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/JedisPubSub.class */
public abstract class JedisPubSub {
    private int subscribedChannels = 0;
    private volatile Client client;

    public void onMessage(String channel, String message) {
    }

    public void onPMessage(String pattern, String channel, String message) {
    }

    public void onSubscribe(String channel, int subscribedChannels) {
    }

    public void onUnsubscribe(String channel, int subscribedChannels) {
    }

    public void onPUnsubscribe(String pattern, int subscribedChannels) {
    }

    public void onPSubscribe(String pattern, int subscribedChannels) {
    }

    public void onPong(String pattern) {
    }

    public void unsubscribe() {
        if (this.client == null) {
            throw new JedisConnectionException("JedisPubSub was not subscribed to a Jedis instance.");
        }
        this.client.unsubscribe();
        this.client.flush();
    }

    public void unsubscribe(String... channels) {
        if (this.client == null) {
            throw new JedisConnectionException("JedisPubSub is not subscribed to a Jedis instance.");
        }
        this.client.unsubscribe(channels);
        this.client.flush();
    }

    public void subscribe(String... channels) {
        if (this.client == null) {
            throw new JedisConnectionException("JedisPubSub is not subscribed to a Jedis instance.");
        }
        this.client.subscribe(channels);
        this.client.flush();
    }

    public void psubscribe(String... patterns) {
        if (this.client == null) {
            throw new JedisConnectionException("JedisPubSub is not subscribed to a Jedis instance.");
        }
        this.client.psubscribe(patterns);
        this.client.flush();
    }

    public void punsubscribe() {
        if (this.client == null) {
            throw new JedisConnectionException("JedisPubSub is not subscribed to a Jedis instance.");
        }
        this.client.punsubscribe();
        this.client.flush();
    }

    public void punsubscribe(String... patterns) {
        if (this.client == null) {
            throw new JedisConnectionException("JedisPubSub is not subscribed to a Jedis instance.");
        }
        this.client.punsubscribe(patterns);
        this.client.flush();
    }

    public void ping() {
        if (this.client == null) {
            throw new JedisConnectionException("JedisPubSub is not subscribed to a Jedis instance.");
        }
        this.client.ping();
        this.client.flush();
    }

    public boolean isSubscribed() {
        return this.subscribedChannels > 0;
    }

    public void proceedWithPatterns(Client client, String... patterns) {
        this.client = client;
        client.psubscribe(patterns);
        client.flush();
        process(client);
    }

    public void proceed(Client client, String... channels) {
        this.client = client;
        client.subscribe(channels);
        client.flush();
        process(client);
    }

    private void process(Client client) {
        do {
            List<Object> reply = client.getRawObjectMultiBulkReply();
            Object firstObj = reply.get(0);
            if (!(firstObj instanceof byte[])) {
                throw new JedisException("Unknown message type: " + firstObj);
            }
            byte[] resp = (byte[]) firstObj;
            if (Arrays.equals(Protocol.Keyword.SUBSCRIBE.raw, resp)) {
                this.subscribedChannels = ((Long) reply.get(2)).intValue();
                byte[] bchannel = (byte[]) reply.get(1);
                String strchannel = bchannel == null ? null : SafeEncoder.encode(bchannel);
                onSubscribe(strchannel, this.subscribedChannels);
            } else if (Arrays.equals(Protocol.Keyword.UNSUBSCRIBE.raw, resp)) {
                this.subscribedChannels = ((Long) reply.get(2)).intValue();
                byte[] bchannel2 = (byte[]) reply.get(1);
                String strchannel2 = bchannel2 == null ? null : SafeEncoder.encode(bchannel2);
                onUnsubscribe(strchannel2, this.subscribedChannels);
            } else if (Arrays.equals(Protocol.Keyword.MESSAGE.raw, resp)) {
                byte[] bchannel3 = (byte[]) reply.get(1);
                byte[] bmesg = (byte[]) reply.get(2);
                String strchannel3 = bchannel3 == null ? null : SafeEncoder.encode(bchannel3);
                String strmesg = bmesg == null ? null : SafeEncoder.encode(bmesg);
                onMessage(strchannel3, strmesg);
            } else if (Arrays.equals(Protocol.Keyword.PMESSAGE.raw, resp)) {
                byte[] bpattern = (byte[]) reply.get(1);
                byte[] bchannel4 = (byte[]) reply.get(2);
                byte[] bmesg2 = (byte[]) reply.get(3);
                String strpattern = bpattern == null ? null : SafeEncoder.encode(bpattern);
                String strchannel4 = bchannel4 == null ? null : SafeEncoder.encode(bchannel4);
                String strmesg2 = bmesg2 == null ? null : SafeEncoder.encode(bmesg2);
                onPMessage(strpattern, strchannel4, strmesg2);
            } else if (Arrays.equals(Protocol.Keyword.PSUBSCRIBE.raw, resp)) {
                this.subscribedChannels = ((Long) reply.get(2)).intValue();
                byte[] bpattern2 = (byte[]) reply.get(1);
                String strpattern2 = bpattern2 == null ? null : SafeEncoder.encode(bpattern2);
                onPSubscribe(strpattern2, this.subscribedChannels);
            } else if (Arrays.equals(Protocol.Keyword.PUNSUBSCRIBE.raw, resp)) {
                this.subscribedChannels = ((Long) reply.get(2)).intValue();
                byte[] bpattern3 = (byte[]) reply.get(1);
                String strpattern3 = bpattern3 == null ? null : SafeEncoder.encode(bpattern3);
                onPUnsubscribe(strpattern3, this.subscribedChannels);
            } else if (Arrays.equals(Protocol.Keyword.PONG.raw, resp)) {
                byte[] bpattern4 = (byte[]) reply.get(1);
                String strpattern4 = bpattern4 == null ? null : SafeEncoder.encode(bpattern4);
                onPong(strpattern4);
            } else {
                throw new JedisException("Unknown message type: " + firstObj);
            }
        } while (isSubscribed());
        this.client = null;
        client.resetPipelinedCount();
    }

    public int getSubscribedChannels() {
        return this.subscribedChannels;
    }
}
