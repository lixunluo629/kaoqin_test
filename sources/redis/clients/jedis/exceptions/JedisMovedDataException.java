package redis.clients.jedis.exceptions;

import redis.clients.jedis.HostAndPort;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/exceptions/JedisMovedDataException.class */
public class JedisMovedDataException extends JedisRedirectionException {
    private static final long serialVersionUID = 3878126572474819403L;

    public JedisMovedDataException(String message, HostAndPort targetNode, int slot) {
        super(message, targetNode, slot);
    }

    public JedisMovedDataException(Throwable cause, HostAndPort targetNode, int slot) {
        super(cause, targetNode, slot);
    }

    public JedisMovedDataException(String message, Throwable cause, HostAndPort targetNode, int slot) {
        super(message, cause, targetNode, slot);
    }
}
