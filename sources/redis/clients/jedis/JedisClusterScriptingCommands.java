package redis.clients.jedis;

import java.util.List;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/JedisClusterScriptingCommands.class */
public interface JedisClusterScriptingCommands {
    Object eval(String str, int i, String... strArr);

    Object eval(String str, List<String> list, List<String> list2);

    Object eval(String str, String str2);

    Object evalsha(String str, String str2);

    Object evalsha(String str, List<String> list, List<String> list2);

    Object evalsha(String str, int i, String... strArr);

    Boolean scriptExists(String str, String str2);

    List<Boolean> scriptExists(String str, String... strArr);

    String scriptLoad(String str, String str2);
}
