package redis.clients.jedis;

import java.util.List;
import redis.clients.util.Slowlog;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/AdvancedJedisCommands.class */
public interface AdvancedJedisCommands {
    List<String> configGet(String str);

    String configSet(String str, String str2);

    String slowlogReset();

    Long slowlogLen();

    List<Slowlog> slowlogGet();

    List<Slowlog> slowlogGet(long j);

    Long objectRefcount(String str);

    String objectEncoding(String str);

    Long objectIdletime(String str);
}
