package io.netty.util.internal;

import java.util.concurrent.atomic.LongAdder;

@SuppressJava6Requirement(reason = "Usage guarded by java version check")
/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/internal/LongAdderCounter.class */
final class LongAdderCounter extends LongAdder implements LongCounter {
    LongAdderCounter() {
    }

    @Override // io.netty.util.internal.LongCounter
    public long value() {
        return longValue();
    }
}
