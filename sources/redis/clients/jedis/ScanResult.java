package redis.clients.jedis;

import java.util.List;
import redis.clients.util.SafeEncoder;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/ScanResult.class */
public class ScanResult<T> {
    private byte[] cursor;
    private List<T> results;

    @Deprecated
    public ScanResult(int cursor, List<T> results) {
        this(Protocol.toByteArray(cursor), results);
    }

    public ScanResult(String cursor, List<T> results) {
        this(SafeEncoder.encode(cursor), results);
    }

    public ScanResult(byte[] cursor, List<T> results) {
        this.cursor = cursor;
        this.results = results;
    }

    @Deprecated
    public int getCursor() {
        return Integer.parseInt(getStringCursor());
    }

    public String getStringCursor() {
        return SafeEncoder.encode(this.cursor);
    }

    public boolean isCompleteIteration() {
        return ScanParams.SCAN_POINTER_START.equals(getStringCursor());
    }

    public byte[] getCursorAsBytes() {
        return this.cursor;
    }

    public List<T> getResult() {
        return this.results;
    }
}
