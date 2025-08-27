package redis.clients.jedis.exceptions;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/exceptions/JedisClusterCrossSlotException.class */
public class JedisClusterCrossSlotException extends JedisClusterException {
    private static final long serialVersionUID = -6355518994901704067L;

    public JedisClusterCrossSlotException(Throwable cause) {
        super(cause);
    }

    public JedisClusterCrossSlotException(String message, Throwable cause) {
        super(message, cause);
    }

    public JedisClusterCrossSlotException(String message) {
        super(message);
    }
}
