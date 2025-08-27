package org.apache.commons.compress.parallel;

import java.io.IOException;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/parallel/ScatterGatherBackingStoreSupplier.class */
public interface ScatterGatherBackingStoreSupplier {
    ScatterGatherBackingStore get() throws IOException;
}
