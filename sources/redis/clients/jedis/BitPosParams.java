package redis.clients.jedis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/BitPosParams.class */
public class BitPosParams {
    private List<byte[]> params;

    protected BitPosParams() {
        this.params = new ArrayList();
    }

    public BitPosParams(long start) {
        this.params = new ArrayList();
        this.params.add(Protocol.toByteArray(start));
    }

    public BitPosParams(long start, long end) {
        this(start);
        this.params.add(Protocol.toByteArray(end));
    }

    public Collection<byte[]> getParams() {
        return Collections.unmodifiableCollection(this.params);
    }
}
