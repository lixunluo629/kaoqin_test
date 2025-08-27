package redis.clients.jedis;

import java.util.List;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/ScriptingCommands.class */
public interface ScriptingCommands {
    Object eval(String str, int i, String... strArr);

    Object eval(String str, List<String> list, List<String> list2);

    Object eval(String str);

    Object evalsha(String str);

    Object evalsha(String str, List<String> list, List<String> list2);

    Object evalsha(String str, int i, String... strArr);

    Boolean scriptExists(String str);

    List<Boolean> scriptExists(String... strArr);

    String scriptLoad(String str);
}
