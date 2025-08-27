package redis.clients.jedis;

import redis.clients.jedis.exceptions.JedisException;

@Deprecated
/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/TransactionBlock.class */
public abstract class TransactionBlock extends Transaction {
    public abstract void execute() throws JedisException;

    public TransactionBlock(Client client) {
        super(client);
    }

    public TransactionBlock() {
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
