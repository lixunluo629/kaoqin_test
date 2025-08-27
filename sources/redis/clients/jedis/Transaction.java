package redis.clients.jedis;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import redis.clients.jedis.exceptions.JedisDataException;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/Transaction.class */
public class Transaction extends MultiKeyPipelineBase implements Closeable {
    protected boolean inTransaction = true;

    protected Transaction() {
    }

    public Transaction(Client client) {
        this.client = client;
    }

    @Override // redis.clients.jedis.PipelineBase
    protected Client getClient(String key) {
        return this.client;
    }

    @Override // redis.clients.jedis.PipelineBase
    protected Client getClient(byte[] key) {
        return this.client;
    }

    public void clear() {
        if (this.inTransaction) {
            discard();
        }
    }

    public List<Object> exec() {
        this.client.exec();
        this.client.getAll(1);
        this.inTransaction = false;
        List<Object> unformatted = this.client.getObjectMultiBulkReply();
        if (unformatted == null) {
            return null;
        }
        List<Object> formatted = new ArrayList<>();
        for (Object o : unformatted) {
            try {
                formatted.add(generateResponse(o).get());
            } catch (JedisDataException e) {
                formatted.add(e);
            }
        }
        return formatted;
    }

    public List<Response<?>> execGetResponse() {
        this.client.exec();
        this.client.getAll(1);
        this.inTransaction = false;
        List<Object> unformatted = this.client.getObjectMultiBulkReply();
        if (unformatted == null) {
            return null;
        }
        List<Response<?>> response = new ArrayList<>();
        for (Object o : unformatted) {
            response.add(generateResponse(o));
        }
        return response;
    }

    public String discard() {
        this.client.discard();
        this.client.getAll(1);
        this.inTransaction = false;
        clean();
        return this.client.getStatusCodeReply();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        clear();
    }
}
