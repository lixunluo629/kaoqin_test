package redis.clients.util;

import java.io.IOException;
import java.net.Socket;

/* loaded from: jedis-2.9.3.jar:redis/clients/util/IOUtils.class */
public class IOUtils {
    private IOUtils() {
    }

    public static void closeQuietly(Socket sock) throws IOException {
        if (sock != null) {
            try {
                sock.close();
            } catch (IOException e) {
            }
        }
    }
}
