package redis.clients.jedis.exceptions;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/exceptions/JedisClusterException.class */
public class JedisClusterException extends JedisDataException {
    private static final long serialVersionUID = 3878126572474819403L;

    public JedisClusterException(Throwable cause) {
        super(cause);
    }

    public JedisClusterException(String message, Throwable cause) {
        super(message, cause);
    }

    public JedisClusterException(String message) {
        super(message);
    }
}
