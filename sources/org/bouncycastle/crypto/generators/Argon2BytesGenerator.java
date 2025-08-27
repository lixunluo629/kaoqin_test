package org.bouncycastle.crypto.generators;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.Blake2bDigest;
import org.bouncycastle.crypto.params.Argon2Parameters;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Pack;
import org.bouncycastle.util.encoders.Hex;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/generators/Argon2BytesGenerator.class */
public class Argon2BytesGenerator {
    private static final int ARGON2_BLOCK_SIZE = 1024;
    private static final int ARGON2_QWORDS_IN_BLOCK = 128;
    private static final int ARGON2_ADDRESSES_IN_BLOCK = 128;
    private static final int ARGON2_PREHASH_DIGEST_LENGTH = 64;
    private static final int ARGON2_PREHASH_SEED_LENGTH = 72;
    private static final int ARGON2_SYNC_POINTS = 4;
    private static final int MIN_PARALLELISM = 1;
    private static final int MAX_PARALLELISM = 16777216;
    private static final int MIN_OUTLEN = 4;
    private static final int MIN_ITERATIONS = 1;
    private Block[] memory;
    private int segmentLength;
    private int laneLength;
    private Argon2Parameters parameters;
    private byte[] result;

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/generators/Argon2BytesGenerator$Block.class */
    private static class Block {
        private static final int SIZE = 128;
        private final long[] v;

        private Block() {
            this.v = new long[128];
        }

        void fromBytes(byte[] bArr) {
            if (bArr.length != 1024) {
                throw new IllegalArgumentException("input shorter than blocksize");
            }
            for (int i = 0; i < 128; i++) {
                this.v[i] = Pack.littleEndianToLong(bArr, i * 8);
            }
        }

        byte[] toBytes() {
            byte[] bArr = new byte[1024];
            for (int i = 0; i < 128; i++) {
                Pack.longToLittleEndian(this.v[i], bArr, i * 8);
            }
            return bArr;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void copyBlock(Block block) {
            System.arraycopy(block.v, 0, this.v, 0, 128);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void xor(Block block, Block block2) {
            for (int i = 0; i < 128; i++) {
                this.v[i] = block.v[i] ^ block2.v[i];
            }
        }

        public void xor(Block block, Block block2, Block block3) {
            for (int i = 0; i < 128; i++) {
                this.v[i] = (block.v[i] ^ block2.v[i]) ^ block3.v[i];
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void xorWith(Block block) {
            for (int i = 0; i < this.v.length; i++) {
                this.v[i] = this.v[i] ^ block.v[i];
            }
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < 128; i++) {
                stringBuffer.append(Hex.toHexString(Pack.longToLittleEndian(this.v[i])));
            }
            return stringBuffer.toString();
        }

        public Block clear() {
            Arrays.fill(this.v, 0L);
            return this;
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/generators/Argon2BytesGenerator$FillBlock.class */
    private static class FillBlock {
        Block R;
        Block Z;
        Block addressBlock;
        Block zeroBlock;
        Block inputBlock;

        private FillBlock() {
            this.R = new Block();
            this.Z = new Block();
            this.addressBlock = new Block();
            this.zeroBlock = new Block();
            this.inputBlock = new Block();
        }

        private void applyBlake() {
            for (int i = 0; i < 8; i++) {
                int i2 = 16 * i;
                Argon2BytesGenerator.roundFunction(this.Z, i2, i2 + 1, i2 + 2, i2 + 3, i2 + 4, i2 + 5, i2 + 6, i2 + 7, i2 + 8, i2 + 9, i2 + 10, i2 + 11, i2 + 12, i2 + 13, i2 + 14, i2 + 15);
            }
            for (int i3 = 0; i3 < 8; i3++) {
                int i4 = 2 * i3;
                Argon2BytesGenerator.roundFunction(this.Z, i4, i4 + 1, i4 + 16, i4 + 17, i4 + 32, i4 + 33, i4 + 48, i4 + 49, i4 + 64, i4 + 65, i4 + 80, i4 + 81, i4 + 96, i4 + 97, i4 + 112, i4 + 113);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void fillBlock(Block block, Block block2, Block block3) {
            if (block == this.zeroBlock) {
                this.R.copyBlock(block2);
            } else {
                this.R.xor(block, block2);
            }
            this.Z.copyBlock(this.R);
            applyBlake();
            block3.xor(this.R, this.Z);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void fillBlockWithXor(Block block, Block block2, Block block3) {
            this.R.xor(block, block2);
            this.Z.copyBlock(this.R);
            applyBlake();
            block3.xor(this.R, this.Z, block3);
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/generators/Argon2BytesGenerator$Position.class */
    private static class Position {
        int pass;
        int lane;
        int slice;
        int index;

        Position() {
        }

        void update(int i, int i2, int i3, int i4) {
            this.pass = i;
            this.lane = i2;
            this.slice = i3;
            this.index = i4;
        }
    }

    public void init(Argon2Parameters argon2Parameters) {
        this.parameters = argon2Parameters;
        if (argon2Parameters.getLanes() < 1) {
            throw new IllegalStateException("lanes must be greater than 1");
        }
        if (argon2Parameters.getLanes() > 16777216) {
            throw new IllegalStateException("lanes must be less than 16777216");
        }
        if (argon2Parameters.getMemory() < 2 * argon2Parameters.getLanes()) {
            throw new IllegalStateException("memory is less than: " + (2 * argon2Parameters.getLanes()) + " expected " + (2 * argon2Parameters.getLanes()));
        }
        if (argon2Parameters.getIterations() < 1) {
            throw new IllegalStateException("iterations is less than: 1");
        }
        doInit(argon2Parameters);
    }

    public int generateBytes(char[] cArr, byte[] bArr) {
        return generateBytes(this.parameters.getCharToByteConverter().convert(cArr), bArr);
    }

    public int generateBytes(char[] cArr, byte[] bArr, int i, int i2) {
        return generateBytes(this.parameters.getCharToByteConverter().convert(cArr), bArr, i, i2);
    }

    public int generateBytes(byte[] bArr, byte[] bArr2) {
        return generateBytes(bArr, bArr2, 0, bArr2.length);
    }

    public int generateBytes(byte[] bArr, byte[] bArr2, int i, int i2) {
        if (i2 < 4) {
            throw new IllegalStateException("output length less than 4");
        }
        initialize(bArr, i2);
        fillMemoryBlocks();
        digest(i2);
        System.arraycopy(this.result, 0, bArr2, i, i2);
        reset();
        return i2;
    }

    private void reset() {
        for (int i = 0; i < this.memory.length; i++) {
            this.memory[i].clear();
        }
        this.memory = null;
        Arrays.fill(this.result, (byte) 0);
    }

    private void doInit(Argon2Parameters argon2Parameters) {
        int memory = argon2Parameters.getMemory();
        if (memory < 8 * argon2Parameters.getLanes()) {
            memory = 8 * argon2Parameters.getLanes();
        }
        this.segmentLength = memory / (argon2Parameters.getLanes() * 4);
        this.laneLength = this.segmentLength * 4;
        initMemory(this.segmentLength * argon2Parameters.getLanes() * 4);
    }

    private void initMemory(int i) {
        this.memory = new Block[i];
        for (int i2 = 0; i2 < this.memory.length; i2++) {
            this.memory[i2] = new Block();
        }
    }

    private void fillMemoryBlocks() {
        FillBlock fillBlock = new FillBlock();
        Position position = new Position();
        for (int i = 0; i < this.parameters.getIterations(); i++) {
            for (int i2 = 0; i2 < 4; i2++) {
                for (int i3 = 0; i3 < this.parameters.getLanes(); i3++) {
                    position.update(i, i3, i2, 0);
                    fillSegment(fillBlock, position);
                }
            }
        }
    }

    private void fillSegment(FillBlock fillBlock, Position position) {
        Block blockClear = null;
        Block blockClear2 = null;
        Block blockClear3 = null;
        boolean zIsDataIndependentAddressing = isDataIndependentAddressing(position);
        int startingIndex = getStartingIndex(position);
        int i = (position.lane * this.laneLength) + (position.slice * this.segmentLength) + startingIndex;
        int prevOffset = getPrevOffset(i);
        if (zIsDataIndependentAddressing) {
            blockClear = fillBlock.addressBlock.clear();
            blockClear3 = fillBlock.zeroBlock.clear();
            blockClear2 = fillBlock.inputBlock.clear();
            initAddressBlocks(fillBlock, position, blockClear3, blockClear2, blockClear);
        }
        position.index = startingIndex;
        while (position.index < this.segmentLength) {
            int iRotatePrevOffset = rotatePrevOffset(i, prevOffset);
            long pseudoRandom = getPseudoRandom(fillBlock, position, blockClear, blockClear2, blockClear3, iRotatePrevOffset, zIsDataIndependentAddressing);
            int refLane = getRefLane(position, pseudoRandom);
            int refColumn = getRefColumn(position, pseudoRandom, refLane == position.lane);
            Block block = this.memory[iRotatePrevOffset];
            Block block2 = this.memory[(this.laneLength * refLane) + refColumn];
            Block block3 = this.memory[i];
            if (isWithXor(position)) {
                fillBlock.fillBlockWithXor(block, block2, block3);
            } else {
                fillBlock.fillBlock(block, block2, block3);
            }
            position.index++;
            i++;
            prevOffset = iRotatePrevOffset + 1;
        }
    }

    private boolean isDataIndependentAddressing(Position position) {
        return this.parameters.getType() == 1 || (this.parameters.getType() == 2 && position.pass == 0 && position.slice < 2);
    }

    private void initAddressBlocks(FillBlock fillBlock, Position position, Block block, Block block2, Block block3) {
        block2.v[0] = intToLong(position.pass);
        block2.v[1] = intToLong(position.lane);
        block2.v[2] = intToLong(position.slice);
        block2.v[3] = intToLong(this.memory.length);
        block2.v[4] = intToLong(this.parameters.getIterations());
        block2.v[5] = intToLong(this.parameters.getType());
        if (position.pass == 0 && position.slice == 0) {
            nextAddresses(fillBlock, block, block2, block3);
        }
    }

    private boolean isWithXor(Position position) {
        return (position.pass == 0 || this.parameters.getVersion() == 16) ? false : true;
    }

    private int getPrevOffset(int i) {
        return i % this.laneLength == 0 ? (i + this.laneLength) - 1 : i - 1;
    }

    private int rotatePrevOffset(int i, int i2) {
        if (i % this.laneLength == 1) {
            i2 = i - 1;
        }
        return i2;
    }

    private static int getStartingIndex(Position position) {
        return (position.pass == 0 && position.slice == 0) ? 2 : 0;
    }

    private void nextAddresses(FillBlock fillBlock, Block block, Block block2, Block block3) {
        long[] jArr = block2.v;
        jArr[6] = jArr[6] + 1;
        fillBlock.fillBlock(block, block2, block3);
        fillBlock.fillBlock(block, block3, block3);
    }

    private long getPseudoRandom(FillBlock fillBlock, Position position, Block block, Block block2, Block block3, int i, boolean z) {
        if (!z) {
            return this.memory[i].v[0];
        }
        if (position.index % 128 == 0) {
            nextAddresses(fillBlock, block3, block2, block);
        }
        return block.v[position.index % 128];
    }

    private int getRefLane(Position position, long j) {
        int lanes = (int) ((j >>> 32) % this.parameters.getLanes());
        if (position.pass == 0 && position.slice == 0) {
            lanes = position.lane;
        }
        return lanes;
    }

    private int getRefColumn(Position position, long j, boolean z) {
        int i;
        int i2;
        if (position.pass == 0) {
            i = 0;
            if (z) {
                i2 = ((position.slice * this.segmentLength) + position.index) - 1;
            } else {
                i2 = (position.slice * this.segmentLength) + (position.index == 0 ? -1 : 0);
            }
        } else {
            i = ((position.slice + 1) * this.segmentLength) % this.laneLength;
            if (z) {
                i2 = ((this.laneLength - this.segmentLength) + position.index) - 1;
            } else {
                i2 = (this.laneLength - this.segmentLength) + (position.index == 0 ? -1 : 0);
            }
        }
        long j2 = j & 4294967295L;
        return ((int) (i + ((i2 - 1) - ((i2 * ((j2 * j2) >>> 32)) >>> 32)))) % this.laneLength;
    }

    private void digest(int i) {
        Block block = this.memory[this.laneLength - 1];
        for (int i2 = 1; i2 < this.parameters.getLanes(); i2++) {
            block.xorWith(this.memory[(i2 * this.laneLength) + (this.laneLength - 1)]);
        }
        this.result = hash(block.toBytes(), i);
    }

    private byte[] initialHash(Argon2Parameters argon2Parameters, int i, byte[] bArr) {
        Blake2bDigest blake2bDigest = new Blake2bDigest(512);
        addIntToLittleEndian(blake2bDigest, argon2Parameters.getLanes());
        addIntToLittleEndian(blake2bDigest, i);
        addIntToLittleEndian(blake2bDigest, argon2Parameters.getMemory());
        addIntToLittleEndian(blake2bDigest, argon2Parameters.getIterations());
        addIntToLittleEndian(blake2bDigest, argon2Parameters.getVersion());
        addIntToLittleEndian(blake2bDigest, argon2Parameters.getType());
        addByteString(blake2bDigest, bArr);
        addByteString(blake2bDigest, argon2Parameters.getSalt());
        addByteString(blake2bDigest, argon2Parameters.getSecret());
        addByteString(blake2bDigest, argon2Parameters.getAdditional());
        byte[] bArr2 = new byte[blake2bDigest.getDigestSize()];
        blake2bDigest.doFinal(bArr2, 0);
        return bArr2;
    }

    private byte[] hash(byte[] bArr, int i) {
        byte[] bArr2 = new byte[i];
        byte[] bArrIntToLittleEndian = Pack.intToLittleEndian(i);
        if (i <= 64) {
            Blake2bDigest blake2bDigest = new Blake2bDigest(i * 8);
            blake2bDigest.update(bArrIntToLittleEndian, 0, bArrIntToLittleEndian.length);
            blake2bDigest.update(bArr, 0, bArr.length);
            blake2bDigest.doFinal(bArr2, 0);
        } else {
            Blake2bDigest blake2bDigest2 = new Blake2bDigest(64 * 8);
            byte[] bArr3 = new byte[64];
            blake2bDigest2.update(bArrIntToLittleEndian, 0, bArrIntToLittleEndian.length);
            blake2bDigest2.update(bArr, 0, bArr.length);
            blake2bDigest2.doFinal(bArr3, 0);
            System.arraycopy(bArr3, 0, bArr2, 0, 64 / 2);
            int i2 = ((i + 31) / 32) - 2;
            int i3 = 64 / 2;
            int i4 = 2;
            while (i4 <= i2) {
                blake2bDigest2.update(bArr3, 0, bArr3.length);
                blake2bDigest2.doFinal(bArr3, 0);
                System.arraycopy(bArr3, 0, bArr2, i3, 64 / 2);
                i4++;
                i3 += 64 / 2;
            }
            Blake2bDigest blake2bDigest3 = new Blake2bDigest((i - (32 * i2)) * 8);
            blake2bDigest3.update(bArr3, 0, bArr3.length);
            blake2bDigest3.doFinal(bArr2, i3);
        }
        return bArr2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void roundFunction(Block block, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, int i16) {
        F(block, i, i5, i9, i13);
        F(block, i2, i6, i10, i14);
        F(block, i3, i7, i11, i15);
        F(block, i4, i8, i12, i16);
        F(block, i, i6, i11, i16);
        F(block, i2, i7, i12, i13);
        F(block, i3, i8, i9, i14);
        F(block, i4, i5, i10, i15);
    }

    private static void F(Block block, int i, int i2, int i3, int i4) {
        fBlaMka(block, i, i2);
        rotr64(block, i4, i, 32L);
        fBlaMka(block, i3, i4);
        rotr64(block, i2, i3, 24L);
        fBlaMka(block, i, i2);
        rotr64(block, i4, i, 16L);
        fBlaMka(block, i3, i4);
        rotr64(block, i2, i3, 63L);
    }

    private static void fBlaMka(Block block, int i, int i2) {
        block.v[i] = block.v[i] + block.v[i2] + (2 * (block.v[i] & 4294967295L) * (block.v[i2] & 4294967295L));
    }

    private static void rotr64(Block block, int i, int i2, long j) {
        long j2 = block.v[i] ^ block.v[i2];
        block.v[i] = (j2 >>> ((int) j)) | (j2 << ((int) (64 - j)));
    }

    private void initialize(byte[] bArr, int i) {
        fillFirstBlocks(initialHash(this.parameters, i, bArr));
    }

    private static void addIntToLittleEndian(Digest digest, int i) {
        digest.update((byte) i);
        digest.update((byte) (i >>> 8));
        digest.update((byte) (i >>> 16));
        digest.update((byte) (i >>> 24));
    }

    private static void addByteString(Digest digest, byte[] bArr) {
        if (bArr == null) {
            addIntToLittleEndian(digest, 0);
        } else {
            addIntToLittleEndian(digest, bArr.length);
            digest.update(bArr, 0, bArr.length);
        }
    }

    private void fillFirstBlocks(byte[] bArr) {
        byte[] initialHashLong = getInitialHashLong(bArr, new byte[]{0, 0, 0, 0});
        byte[] initialHashLong2 = getInitialHashLong(bArr, new byte[]{1, 0, 0, 0});
        for (int i = 0; i < this.parameters.getLanes(); i++) {
            Pack.intToLittleEndian(i, initialHashLong, 68);
            Pack.intToLittleEndian(i, initialHashLong2, 68);
            this.memory[(i * this.laneLength) + 0].fromBytes(hash(initialHashLong, 1024));
            this.memory[(i * this.laneLength) + 1].fromBytes(hash(initialHashLong2, 1024));
        }
    }

    private byte[] getInitialHashLong(byte[] bArr, byte[] bArr2) {
        byte[] bArr3 = new byte[72];
        System.arraycopy(bArr, 0, bArr3, 0, 64);
        System.arraycopy(bArr2, 0, bArr3, 64, 4);
        return bArr3;
    }

    private long intToLong(int i) {
        return i & 4294967295L;
    }
}
