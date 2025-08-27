package redis.clients.jedis.exceptions;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/exceptions/JedisNoScriptException.class */
public class JedisNoScriptException extends JedisDataException {
    private static final long serialVersionUID = 4674378093072060731L;

    public JedisNoScriptException(String message) {
        super(message);
    }

    public JedisNoScriptException(Throwable cause) {
        super(cause);
    }

    public JedisNoScriptException(String message, Throwable cause) {
        super(message, cause);
    }
}
