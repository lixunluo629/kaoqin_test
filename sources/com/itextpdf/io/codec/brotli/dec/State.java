package com.itextpdf.io.codec.brotli.dec;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/codec/brotli/dec/State.class */
final class State {
    int nextRunningState;
    byte[] ringBuffer;
    int metaBlockLength;
    boolean inputEnd;
    boolean isUncompressed;
    boolean isMetadata;
    int literalTree;
    int j;
    int insertLength;
    byte[] contextModes;
    byte[] contextMap;
    int contextMapSlice;
    int distContextMapSlice;
    int contextLookupOffset1;
    int contextLookupOffset2;
    int treeCommandOffset;
    int distanceCode;
    byte[] distContextMap;
    int numDirectDistanceCodes;
    int distancePostfixMask;
    int distancePostfixBits;
    int distance;
    int copyLength;
    int copyDst;
    int maxBackwardDistance;
    int maxRingBufferSize;
    int outputOffset;
    int outputLength;
    int outputUsed;
    int bytesWritten;
    int bytesToWrite;
    byte[] output;
    int runningState = 0;
    final BitReader br = new BitReader();
    final int[] blockTypeTrees = new int[3240];
    final int[] blockLenTrees = new int[3240];
    final HuffmanTreeGroup hGroup0 = new HuffmanTreeGroup();
    final HuffmanTreeGroup hGroup1 = new HuffmanTreeGroup();
    final HuffmanTreeGroup hGroup2 = new HuffmanTreeGroup();
    final int[] blockLength = new int[3];
    final int[] numBlockTypes = new int[3];
    final int[] blockTypeRb = new int[6];
    final int[] distRb = {16, 15, 11, 4};
    int pos = 0;
    int maxDistance = 0;
    int distRbIdx = 0;
    boolean trivialLiteralContext = false;
    int literalTreeIndex = 0;
    int ringBufferSize = 0;
    long expectedTotalSize = 0;
    byte[] customDictionary = new byte[0];
    int bytesToIgnore = 0;

    State() {
    }

    private static int decodeWindowBits(BitReader br) {
        if (BitReader.readBits(br, 1) == 0) {
            return 16;
        }
        int n = BitReader.readBits(br, 3);
        if (n != 0) {
            return 17 + n;
        }
        int n2 = BitReader.readBits(br, 3);
        if (n2 != 0) {
            return 8 + n2;
        }
        return 17;
    }

    static void setInput(State state, InputStream input) throws IOException {
        if (state.runningState != 0) {
            throw new IllegalStateException("State MUST be uninitialized");
        }
        BitReader.init(state.br, input);
        int windowBits = decodeWindowBits(state.br);
        if (windowBits == 9) {
            throw new BrotliRuntimeException("Invalid 'windowBits' code");
        }
        state.maxRingBufferSize = 1 << windowBits;
        state.maxBackwardDistance = state.maxRingBufferSize - 16;
        state.runningState = 1;
    }

    static void close(State state) throws IOException {
        if (state.runningState == 0) {
            throw new IllegalStateException("State MUST be initialized");
        }
        if (state.runningState == 11) {
            return;
        }
        state.runningState = 11;
        BitReader.close(state.br);
    }
}
