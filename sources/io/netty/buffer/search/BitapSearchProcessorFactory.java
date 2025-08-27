package io.netty.buffer.search;

import io.netty.util.internal.PlatformDependent;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/buffer/search/BitapSearchProcessorFactory.class */
public class BitapSearchProcessorFactory extends AbstractSearchProcessorFactory {
    private final long[] bitMasks = new long[256];
    private final long successBit;

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/buffer/search/BitapSearchProcessorFactory$Processor.class */
    public static class Processor implements SearchProcessor {
        private final long[] bitMasks;
        private final long successBit;
        private long currentMask;

        Processor(long[] bitMasks, long successBit) {
            this.bitMasks = bitMasks;
            this.successBit = successBit;
        }

        @Override // io.netty.util.ByteProcessor
        public boolean process(byte value) {
            this.currentMask = ((this.currentMask << 1) | 1) & PlatformDependent.getLong(this.bitMasks, value & 255);
            return (this.currentMask & this.successBit) == 0;
        }

        @Override // io.netty.buffer.search.SearchProcessor
        public void reset() {
            this.currentMask = 0L;
        }
    }

    BitapSearchProcessorFactory(byte[] needle) {
        if (needle.length > 64) {
            throw new IllegalArgumentException("Maximum supported search pattern length is 64, got " + needle.length);
        }
        long bit = 1;
        for (byte c : needle) {
            long[] jArr = this.bitMasks;
            int i = c & 255;
            jArr[i] = jArr[i] | bit;
            bit <<= 1;
        }
        this.successBit = 1 << (needle.length - 1);
    }

    @Override // io.netty.buffer.search.SearchProcessorFactory
    public Processor newSearchProcessor() {
        return new Processor(this.bitMasks, this.successBit);
    }
}
