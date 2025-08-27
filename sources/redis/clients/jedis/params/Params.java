package redis.clients.jedis.params;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import redis.clients.util.SafeEncoder;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/params/Params.class */
public abstract class Params {
    private Map<String, Object> params;

    public <T> T getParam(String str) {
        if (this.params == null) {
            return null;
        }
        return (T) this.params.get(str);
    }

    public byte[][] getByteParams() {
        ArrayList<byte[]> byteParams = new ArrayList<>();
        for (Map.Entry<String, Object> param : this.params.entrySet()) {
            byteParams.add(SafeEncoder.encode(param.getKey()));
            if (param.getValue() != null) {
                byteParams.add(SafeEncoder.encode(String.valueOf(param.getValue())));
            }
        }
        return (byte[][]) byteParams.toArray((Object[]) new byte[byteParams.size()]);
    }

    public boolean contains(String name) {
        if (this.params == null) {
            return false;
        }
        return this.params.containsKey(name);
    }

    protected void addParam(String name, Object value) {
        if (this.params == null) {
            this.params = new HashMap();
        }
        this.params.put(name, value);
    }

    protected void addParam(String name) {
        if (this.params == null) {
            this.params = new HashMap();
        }
        this.params.put(name, null);
    }
}
