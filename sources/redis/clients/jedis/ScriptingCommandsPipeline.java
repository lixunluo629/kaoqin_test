package redis.clients.jedis;

import java.util.List;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/ScriptingCommandsPipeline.class */
public interface ScriptingCommandsPipeline {
    Response<Object> eval(String str, int i, String... strArr);

    Response<Object> eval(String str, List<String> list, List<String> list2);

    Response<Object> eval(String str);

    Response<Object> evalsha(String str);

    Response<Object> evalsha(String str, List<String> list, List<String> list2);

    Response<Object> evalsha(String str, int i, String... strArr);
}
