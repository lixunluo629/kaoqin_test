package redis.clients.jedis;

import redis.clients.jedis.exceptions.JedisDataException;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/Response.class */
public class Response<T> {
    private Builder<T> builder;
    private Object data;
    protected T response = null;
    protected JedisDataException exception = null;
    private boolean building = false;
    private boolean built = false;
    private boolean set = false;
    private Response<?> dependency = null;

    public Response(Builder<T> b) {
        this.builder = b;
    }

    public void set(Object data) {
        this.data = data;
        this.set = true;
    }

    public T get() {
        if (this.dependency != null && this.dependency.set && !this.dependency.built) {
            this.dependency.build();
        }
        if (!this.set) {
            throw new JedisDataException("Please close pipeline or multi block before calling this method.");
        }
        if (!this.built) {
            build();
        }
        if (this.exception != null) {
            throw this.exception;
        }
        return this.response;
    }

    public void setDependency(Response<?> dependency) {
        this.dependency = dependency;
    }

    private void build() {
        if (this.building) {
            return;
        }
        this.building = true;
        try {
            if (this.data != null) {
                if (this.data instanceof JedisDataException) {
                    this.exception = (JedisDataException) this.data;
                } else {
                    this.response = this.builder.build(this.data);
                }
            }
            this.data = null;
        } finally {
            this.building = false;
            this.built = true;
        }
    }

    public String toString() {
        return "Response " + this.builder.toString();
    }
}
