package redis.clients.jedis.exceptions;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/exceptions/JedisBusyException.class */
public class JedisBusyException extends JedisDataException {
    private static final long serialVersionUID = 3992655220229243478L;

    public JedisBusyException(String message) {
        super(message);
    }

    public JedisBusyException(Throwable cause) {
        super(cause);
    }

    public JedisBusyException(String message, Throwable cause) {
        super(message, cause);
    }
}
