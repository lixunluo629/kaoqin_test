package io.netty.buffer.search;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/buffer/search/AbstractMultiSearchProcessorFactory.class */
public abstract class AbstractMultiSearchProcessorFactory implements MultiSearchProcessorFactory {
    public static AhoCorasicSearchProcessorFactory newAhoCorasicSearchProcessorFactory(byte[]... needles) {
        return new AhoCorasicSearchProcessorFactory(needles);
    }
}
