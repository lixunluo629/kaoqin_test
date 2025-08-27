package redis.clients.jedis.exceptions;

import redis.clients.jedis.HostAndPort;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/exceptions/JedisRedirectionException.class */
public class JedisRedirectionException extends JedisDataException {
    private static final long serialVersionUID = 3878126572474819403L;
    private HostAndPort targetNode;
    private int slot;

    public JedisRedirectionException(String message, HostAndPort targetNode, int slot) {
        super(message);
        this.targetNode = targetNode;
        this.slot = slot;
    }

    public JedisRedirectionException(Throwable cause, HostAndPort targetNode, int slot) {
        super(cause);
        this.targetNode = targetNode;
        this.slot = slot;
    }

    public JedisRedirectionException(String message, Throwable cause, HostAndPort targetNode, int slot) {
        super(message, cause);
        this.targetNode = targetNode;
        this.slot = slot;
    }

    public HostAndPort getTargetNode() {
        return this.targetNode;
    }

    public int getSlot() {
        return this.slot;
    }
}
