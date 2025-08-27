package io.netty.buffer.search;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/buffer/search/AbstractSearchProcessorFactory.class */
public abstract class AbstractSearchProcessorFactory implements SearchProcessorFactory {
    public static KmpSearchProcessorFactory newKmpSearchProcessorFactory(byte[] needle) {
        return new KmpSearchProcessorFactory(needle);
    }

    public static BitapSearchProcessorFactory newBitapSearchProcessorFactory(byte[] needle) {
        return new BitapSearchProcessorFactory(needle);
    }
}
