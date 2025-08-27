package redis.clients.jedis;

import java.util.List;
import java.util.Map;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/SentinelCommands.class */
public interface SentinelCommands {
    List<Map<String, String>> sentinelMasters();

    List<String> sentinelGetMasterAddrByName(String str);

    Long sentinelReset(String str);

    List<Map<String, String>> sentinelSlaves(String str);

    String sentinelFailover(String str);

    String sentinelMonitor(String str, String str2, int i, int i2);

    String sentinelRemove(String str);

    String sentinelSet(String str, Map<String, String> map);
}
