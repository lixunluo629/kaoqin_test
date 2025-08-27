package redis.clients.jedis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import redis.clients.jedis.Protocol;
import redis.clients.util.SafeEncoder;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/SortingParams.class */
public class SortingParams {
    private List<byte[]> params = new ArrayList();

    public SortingParams by(String pattern) {
        return by(SafeEncoder.encode(pattern));
    }

    public SortingParams by(byte[] pattern) {
        this.params.add(Protocol.Keyword.BY.raw);
        this.params.add(pattern);
        return this;
    }

    public SortingParams nosort() {
        this.params.add(Protocol.Keyword.BY.raw);
        this.params.add(Protocol.Keyword.NOSORT.raw);
        return this;
    }

    public Collection<byte[]> getParams() {
        return Collections.unmodifiableCollection(this.params);
    }

    public SortingParams desc() {
        this.params.add(Protocol.Keyword.DESC.raw);
        return this;
    }

    public SortingParams asc() {
        this.params.add(Protocol.Keyword.ASC.raw);
        return this;
    }

    public SortingParams limit(int start, int count) {
        this.params.add(Protocol.Keyword.LIMIT.raw);
        this.params.add(Protocol.toByteArray(start));
        this.params.add(Protocol.toByteArray(count));
        return this;
    }

    public SortingParams alpha() {
        this.params.add(Protocol.Keyword.ALPHA.raw);
        return this;
    }

    public SortingParams get(String... patterns) {
        for (String pattern : patterns) {
            this.params.add(Protocol.Keyword.GET.raw);
            this.params.add(SafeEncoder.encode(pattern));
        }
        return this;
    }

    public SortingParams get(byte[]... patterns) {
        for (byte[] pattern : patterns) {
            this.params.add(Protocol.Keyword.GET.raw);
            this.params.add(pattern);
        }
        return this;
    }
}
