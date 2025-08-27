package redis.clients.jedis.exceptions;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/exceptions/InvalidURIException.class */
public class InvalidURIException extends JedisException {
    private static final long serialVersionUID = -781691993326357802L;

    public InvalidURIException(String message) {
        super(message);
    }

    public InvalidURIException(Throwable cause) {
        super(cause);
    }

    public InvalidURIException(String message, Throwable cause) {
        super(message, cause);
    }
}
