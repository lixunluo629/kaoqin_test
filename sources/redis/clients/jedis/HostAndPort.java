package redis.clients.jedis;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hyperic.sigar.NetFlags;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/HostAndPort.class */
public class HostAndPort implements Serializable {
    private static final long serialVersionUID = -519876229978427751L;
    protected static Logger log = Logger.getLogger(HostAndPort.class.getName());
    public static final String LOCALHOST_STR = getLocalHostQuietly();
    private String host;
    private int port;

    public HostAndPort(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public boolean equals(Object obj) {
        if (obj instanceof HostAndPort) {
            HostAndPort hp = (HostAndPort) obj;
            String thisHost = convertHost(this.host);
            String hpHost = convertHost(hp.host);
            return this.port == hp.port && thisHost.equals(hpHost);
        }
        return false;
    }

    public int hashCode() {
        return (31 * convertHost(this.host).hashCode()) + this.port;
    }

    public String toString() {
        return this.host + ":" + this.port;
    }

    public static String[] extractParts(String from) {
        int idx = from.lastIndexOf(":");
        String host = idx != -1 ? from.substring(0, idx) : from;
        String port = idx != -1 ? from.substring(idx + 1) : "";
        return new String[]{host, port};
    }

    public static HostAndPort parseString(String from) throws NumberFormatException {
        try {
            String[] parts = extractParts(from);
            String host = parts[0];
            int port = Integer.parseInt(parts[1]);
            return new HostAndPort(convertHost(host), port);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public static String convertHost(String host) {
        if (host.equals(NetFlags.LOOPBACK_ADDRESS) || host.startsWith("localhost") || host.equals("0.0.0.0") || host.startsWith("169.254") || host.startsWith(NetFlags.LOOPBACK_ADDRESS_V6) || host.startsWith("0:0:0:0:0:0:0:1")) {
            return LOCALHOST_STR;
        }
        return host;
    }

    public static String getLocalHostQuietly() {
        String localAddress;
        try {
            localAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception ex) {
            log.logp(Level.SEVERE, HostAndPort.class.getName(), "getLocalHostQuietly", "cant resolve localhost address", (Throwable) ex);
            localAddress = "localhost";
        }
        return localAddress;
    }
}
