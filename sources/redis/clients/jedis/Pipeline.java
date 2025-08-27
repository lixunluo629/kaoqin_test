package redis.clients.jedis;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import redis.clients.jedis.exceptions.JedisDataException;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/Pipeline.class */
public class Pipeline extends MultiKeyPipelineBase implements Closeable {
    private MultiResponseBuilder currentMulti;

    /* loaded from: jedis-2.9.3.jar:redis/clients/jedis/Pipeline$MultiResponseBuilder.class */
    private class MultiResponseBuilder extends Builder<List<Object>> {
        private List<Response<?>> responses;

        private MultiResponseBuilder() {
            this.responses = new ArrayList();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // redis.clients.jedis.Builder
        public List<Object> build(Object data) {
            Object builtResponse;
            List<Object> list = (List) data;
            List<Object> values = new ArrayList<>();
            if (list.size() != this.responses.size()) {
                throw new JedisDataException("Expected data size " + this.responses.size() + " but was " + list.size());
            }
            for (int i = 0; i < list.size(); i++) {
                Response<?> response = this.responses.get(i);
                response.set(list.get(i));
                try {
                    builtResponse = response.get();
                } catch (JedisDataException e) {
                    builtResponse = e;
                }
                values.add(builtResponse);
            }
            return values;
        }

        public void setResponseDependency(Response<?> dependency) {
            for (Response<?> response : this.responses) {
                response.setDependency(dependency);
            }
        }

        public void addResponse(Response<?> response) {
            this.responses.add(response);
        }
    }

    @Override // redis.clients.jedis.Queable
    protected <T> Response<T> getResponse(Builder<T> builder) {
        if (this.currentMulti != null) {
            super.getResponse(BuilderFactory.STRING);
            Response<T> lr = new Response<>(builder);
            this.currentMulti.addResponse(lr);
            return lr;
        }
        return super.getResponse(builder);
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override // redis.clients.jedis.PipelineBase
    protected Client getClient(byte[] key) {
        return this.client;
    }

    @Override // redis.clients.jedis.PipelineBase
    protected Client getClient(String key) {
        return this.client;
    }

    public void clear() {
        if (isInMulti()) {
            discard();
        }
        sync();
    }

    public boolean isInMulti() {
        return this.currentMulti != null;
    }

    public void sync() {
        if (getPipelinedResponseLength() > 0) {
            List<Object> unformatted = this.client.getAll();
            for (Object o : unformatted) {
                generateResponse(o);
            }
        }
    }

    public List<Object> syncAndReturnAll() {
        if (getPipelinedResponseLength() > 0) {
            List<Object> unformatted = this.client.getAll();
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
        return Collections.emptyList();
    }

    public Response<String> discard() {
        if (this.currentMulti == null) {
            throw new JedisDataException("DISCARD without MULTI");
        }
        this.client.discard();
        this.currentMulti = null;
        return getResponse(BuilderFactory.STRING);
    }

    public Response<List<Object>> exec() {
        if (this.currentMulti == null) {
            throw new JedisDataException("EXEC without MULTI");
        }
        this.client.exec();
        Response<List<Object>> response = super.getResponse(this.currentMulti);
        this.currentMulti.setResponseDependency(response);
        this.currentMulti = null;
        return response;
    }

    public Response<String> multi() {
        if (this.currentMulti != null) {
            throw new JedisDataException("MULTI calls can not be nested");
        }
        this.client.multi();
        Response<String> response = getResponse(BuilderFactory.STRING);
        this.currentMulti = new MultiResponseBuilder();
        return response;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        clear();
    }
}
