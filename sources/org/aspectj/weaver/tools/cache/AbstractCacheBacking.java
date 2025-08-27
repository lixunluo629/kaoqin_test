package org.aspectj.weaver.tools.cache;

import java.util.zip.CRC32;
import org.aspectj.weaver.tools.Trace;
import org.aspectj.weaver.tools.TraceFactory;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/cache/AbstractCacheBacking.class */
public abstract class AbstractCacheBacking implements CacheBacking {
    protected final Trace logger = TraceFactory.getTraceFactory().getTrace(getClass());

    protected AbstractCacheBacking() {
    }

    public static final long crc(byte[] bytes) {
        if (bytes == null || bytes.length <= 0) {
            return 0L;
        }
        CRC32 crc32 = new CRC32();
        crc32.update(bytes);
        return crc32.getValue();
    }
}
