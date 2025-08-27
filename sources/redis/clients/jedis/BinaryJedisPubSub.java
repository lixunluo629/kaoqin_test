package redis.clients.jedis;

import java.util.Arrays;
import java.util.List;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.exceptions.JedisException;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/BinaryJedisPubSub.class */
public abstract class BinaryJedisPubSub {
    private int subscribedChannels = 0;
    private Client client;

    public void onMessage(byte[] channel, byte[] message) {
    }

    public void onPMessage(byte[] pattern, byte[] channel, byte[] message) {
    }

    public void onSubscribe(byte[] channel, int subscribedChannels) {
    }

    public void onUnsubscribe(byte[] channel, int subscribedChannels) {
    }

    public void onPUnsubscribe(byte[] pattern, int subscribedChannels) {
    }

    public void onPSubscribe(byte[] pattern, int subscribedChannels) {
    }

    public void unsubscribe() {
        this.client.unsubscribe();
        this.client.flush();
    }

    public void unsubscribe(byte[]... channels) {
        this.client.unsubscribe(channels);
        this.client.flush();
    }

    public void subscribe(byte[]... channels) {
        this.client.subscribe(channels);
        this.client.flush();
    }

    public void psubscribe(byte[]... patterns) {
        this.client.psubscribe(patterns);
        this.client.flush();
    }

    public void punsubscribe() {
        this.client.punsubscribe();
        this.client.flush();
    }

    public void punsubscribe(byte[]... patterns) {
        this.client.punsubscribe(patterns);
        this.client.flush();
    }

    public boolean isSubscribed() {
        return this.subscribedChannels > 0;
    }

    public void proceedWithPatterns(Client client, byte[]... patterns) {
        this.client = client;
        client.psubscribe(patterns);
        client.flush();
        process(client);
    }

    public void proceed(Client client, byte[]... channels) {
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
                onSubscribe(bchannel, this.subscribedChannels);
            } else if (Arrays.equals(Protocol.Keyword.UNSUBSCRIBE.raw, resp)) {
                this.subscribedChannels = ((Long) reply.get(2)).intValue();
                byte[] bchannel2 = (byte[]) reply.get(1);
                onUnsubscribe(bchannel2, this.subscribedChannels);
            } else if (Arrays.equals(Protocol.Keyword.MESSAGE.raw, resp)) {
                byte[] bchannel3 = (byte[]) reply.get(1);
                byte[] bmesg = (byte[]) reply.get(2);
                onMessage(bchannel3, bmesg);
            } else if (Arrays.equals(Protocol.Keyword.PMESSAGE.raw, resp)) {
                byte[] bpattern = (byte[]) reply.get(1);
                byte[] bchannel4 = (byte[]) reply.get(2);
                byte[] bmesg2 = (byte[]) reply.get(3);
                onPMessage(bpattern, bchannel4, bmesg2);
            } else if (Arrays.equals(Protocol.Keyword.PSUBSCRIBE.raw, resp)) {
                this.subscribedChannels = ((Long) reply.get(2)).intValue();
                byte[] bpattern2 = (byte[]) reply.get(1);
                onPSubscribe(bpattern2, this.subscribedChannels);
            } else if (Arrays.equals(Protocol.Keyword.PUNSUBSCRIBE.raw, resp)) {
                this.subscribedChannels = ((Long) reply.get(2)).intValue();
                byte[] bpattern3 = (byte[]) reply.get(1);
                onPUnsubscribe(bpattern3, this.subscribedChannels);
            } else {
                throw new JedisException("Unknown message type: " + firstObj);
            }
        } while (isSubscribed());
    }

    public int getSubscribedChannels() {
        return this.subscribedChannels;
    }
}
