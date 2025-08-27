package redis.clients.jedis;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/DebugParams.class */
public class DebugParams {
    private String[] command;

    private DebugParams() {
    }

    public String[] getCommand() {
        return this.command;
    }

    public static DebugParams SEGFAULT() {
        DebugParams debugParams = new DebugParams();
        debugParams.command = new String[]{"SEGFAULT"};
        return debugParams;
    }

    public static DebugParams OBJECT(String key) {
        DebugParams debugParams = new DebugParams();
        debugParams.command = new String[]{"OBJECT", key};
        return debugParams;
    }

    public static DebugParams RELOAD() {
        DebugParams debugParams = new DebugParams();
        debugParams.command = new String[]{"RELOAD"};
        return debugParams;
    }
}
