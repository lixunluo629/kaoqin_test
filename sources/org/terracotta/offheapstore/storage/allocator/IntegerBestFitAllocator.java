package org.terracotta.offheapstore.storage.allocator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.terracotta.offheapstore.paging.OffHeapStorageArea;
import org.terracotta.offheapstore.util.Validation;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/storage/allocator/IntegerBestFitAllocator.class */
public final class IntegerBestFitAllocator implements Allocator {
    private static final int SIZE_T_SIZE = 4;
    private static final int SIZE_T_BITSIZE = 32;
    private static final int MALLOC_ALIGNMENT = 8;
    private static final int CHUNK_ALIGN_MASK = 7;
    private static final int MCHUNK_SIZE = 16;
    private static final int CHUNK_OVERHEAD = 8;
    private static final int MIN_CHUNK_SIZE = 16;
    private static final int MAX_REQUEST = 2147483584;
    private static final int MIN_REQUEST = 7;
    private static final int PINUSE_BIT = 1;
    private static final int CINUSE_BIT = 2;
    private static final int FLAG4_BIT = 4;
    private static final int INUSE_BITS = 3;
    private static final int FLAG_BITS = 7;
    private static final int NSMALLBINS = 32;
    private static final int NTREEBINS = 32;
    private static final int SMALLBIN_SHIFT = 3;
    private static final int TREEBIN_SHIFT = 8;
    private static final int MIN_LARGE_SIZE = 256;
    private static final int MAX_SMALL_SIZE = 255;
    private static final int MAX_SMALL_REQUEST = 240;
    private final OffHeapStorageArea storage;
    private int smallMap;
    private int treeMap;
    private final int[] smallBins = new int[32];
    private final int[] treeBins = new int[32];
    private int designatedVictimSize = 0;
    private int designatedVictim = -1;
    private int topSize = 0;
    private int top = 0;
    private int occupied;
    private static final boolean DEBUG = Boolean.getBoolean(IntegerBestFitAllocator.class.getName() + ".DEBUG");
    private static final boolean VALIDATING = Validation.shouldValidate(IntegerBestFitAllocator.class);
    private static final int TOP_FOOT_SIZE = alignOffset(chunkToMem(0)) + padRequest(16);
    private static final int MINIMAL_SIZE = Integer.highestOneBit(TOP_FOOT_SIZE) << 1;
    private static final int TOP_FOOT_OFFSET = memToChunk(0) + TOP_FOOT_SIZE;

    public IntegerBestFitAllocator(OffHeapStorageArea storage) {
        this.storage = storage;
        clear();
    }

    @Override // org.terracotta.offheapstore.storage.allocator.Allocator
    public void clear() {
        this.top = 0;
        this.topSize = -TOP_FOOT_SIZE;
        this.designatedVictim = -1;
        this.designatedVictimSize = 0;
        for (int i = 0; i < this.treeBins.length; i++) {
            this.treeBins[i] = -1;
            clearTreeMap(i);
        }
        for (int i2 = 0; i2 < this.smallBins.length; i2++) {
            this.smallBins[i2] = -1;
            clearSmallMap(i2);
        }
        this.occupied = 0;
    }

    @Override // org.terracotta.offheapstore.storage.allocator.Allocator
    public void expand(long increase) {
        this.topSize = (int) (this.topSize + increase);
        head(this.top, this.topSize | 1);
        if (this.topSize >= 0) {
            checkTopChunk(this.top);
        }
    }

    @Override // org.terracotta.offheapstore.storage.allocator.Allocator
    public long allocate(long size) {
        return dlmalloc((int) size);
    }

    @Override // org.terracotta.offheapstore.storage.allocator.Allocator
    public void free(long address) {
        dlfree((int) address, true);
    }

    @Override // org.terracotta.offheapstore.storage.allocator.Allocator
    public long occupied() {
        return this.occupied;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        if (DEBUG) {
            sb.append("\nChunks:").append(dump());
        }
        return sb.toString();
    }

    private String dump() {
        StringBuilder sb = new StringBuilder();
        int iNextChunk = 0;
        while (true) {
            int q = iNextChunk;
            if (q != this.top) {
                if (isInUse(q)) {
                    sb.append(" InUseChunk:&").append(q).append(':').append(chunkSize(q)).append('b');
                } else {
                    sb.append(" FreeChunk:&").append(q).append(':').append(chunkSize(q)).append('b');
                }
                iNextChunk = nextChunk(q);
            } else {
                sb.append(" TopChunk:&").append(this.top).append(':').append(this.topSize + TOP_FOOT_SIZE).append('b');
                return sb.toString();
            }
        }
    }

    private int dlmalloc(int bytes) {
        int nb = bytes < 7 ? 16 : padRequest(bytes);
        if (bytes <= 240) {
            int index = smallBinIndex(nb);
            int smallBits = this.smallMap >>> index;
            if ((smallBits & 3) != 0) {
                return allocateFromSmallBin(index + ((smallBits ^ (-1)) & 1), nb);
            }
            if (nb > this.designatedVictimSize) {
                if (smallBits != 0) {
                    return splitFromSmallBin(Integer.numberOfTrailingZeros(smallBits << index), nb);
                }
                if (this.treeMap != 0) {
                    return splitSmallFromTree(nb);
                }
            }
        } else {
            if (bytes > MAX_REQUEST) {
                return -1;
            }
            if (this.treeMap != 0) {
                int mem = splitFromTree(nb);
                if (okAddress(mem)) {
                    return mem;
                }
            }
        }
        if (nb <= this.designatedVictimSize) {
            return splitFromDesignatedVictim(nb);
        }
        if (nb < this.topSize) {
            return splitFromTop(nb);
        }
        return -1;
    }

    private int allocateFromSmallBin(int index, int nb) {
        int h = this.smallBins[index];
        Validation.validate(!VALIDATING || chunkSize(h) == smallBinIndexToSize(index));
        int f = forward(h);
        int b = backward(h);
        if (f == h) {
            Validation.validate(!VALIDATING || b == h);
            clearSmallMap(index);
            this.smallBins[index] = -1;
        } else {
            this.smallBins[index] = f;
            backward(f, b);
            forward(b, f);
        }
        setInUseAndPreviousInUse(h, smallBinIndexToSize(index));
        int mem = chunkToMem(h);
        checkMallocedChunk(mem, nb);
        return mem;
    }

    private int splitFromSmallBin(int index, int nb) {
        int h = this.smallBins[index];
        Validation.validate(!VALIDATING || chunkSize(h) == smallBinIndexToSize(index));
        int f = forward(h);
        int b = backward(h);
        if (f == h) {
            Validation.validate(!VALIDATING || b == h);
            clearSmallMap(index);
            this.smallBins[index] = -1;
        } else {
            this.smallBins[index] = f;
            backward(f, b);
            forward(b, f);
        }
        int rsize = smallBinIndexToSize(index) - nb;
        if (rsize < 16) {
            setInUseAndPreviousInUse(h, smallBinIndexToSize(index));
        } else {
            setSizeAndPreviousInUseOfInUseChunk(h, nb);
            int r = h + nb;
            setSizeAndPreviousInUseOfFreeChunk(r, rsize);
            replaceDesignatedVictim(r, rsize);
        }
        int mem = chunkToMem(h);
        checkMallocedChunk(mem, nb);
        return mem;
    }

    private void replaceDesignatedVictim(int p, int s) {
        int dvs = this.designatedVictimSize;
        if (dvs != 0) {
            int dv = this.designatedVictim;
            Validation.validate(!VALIDATING || isSmall(dvs));
            insertSmallChunk(dv, dvs);
        }
        this.designatedVictimSize = s;
        this.designatedVictim = p;
    }

    private int splitSmallFromTree(int nb) {
        int index = Integer.numberOfTrailingZeros(this.treeMap);
        int i = this.treeBins[index];
        int t = i;
        int v = i;
        int rsize = chunkSize(t) - nb;
        while (true) {
            int iLeftmostChild = leftmostChild(t);
            t = iLeftmostChild;
            if (iLeftmostChild == -1) {
                break;
            }
            int trem = chunkSize(t) - nb;
            if (trem >= 0 && trem < rsize) {
                rsize = trem;
                v = t;
            }
        }
        if (okAddress(v)) {
            int r = v + nb;
            Validation.validate(!VALIDATING || chunkSize(v) == rsize + nb);
            if (okNext(v, r)) {
                unlinkLargeChunk(v);
                if (rsize < 16) {
                    setInUseAndPreviousInUse(v, rsize + nb);
                } else {
                    setSizeAndPreviousInUseOfInUseChunk(v, nb);
                    setSizeAndPreviousInUseOfFreeChunk(r, rsize);
                    replaceDesignatedVictim(r, rsize);
                }
                int mem = chunkToMem(v);
                checkMallocedChunk(mem, nb);
                return mem;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x004a A[PHI: r7 r8
  0x004a: PHI (r7v6 'v' int) = (r7v5 'v' int), (r7v5 'v' int), (r7v7 'v' int) binds: [B:6:0x0034, B:8:0x003a, B:10:0x0044] A[DONT_GENERATE, DONT_INLINE]
  0x004a: PHI (r8v6 'rsize' int) = (r8v5 'rsize' int), (r8v5 'rsize' int), (r8v7 'rsize' int) binds: [B:6:0x0034, B:8:0x003a, B:10:0x0044] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int splitFromTree(int r6) {
        /*
            Method dump skipped, instructions count: 351
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.terracotta.offheapstore.storage.allocator.IntegerBestFitAllocator.splitFromTree(int):int");
    }

    private int splitFromDesignatedVictim(int nb) {
        int rsize = this.designatedVictimSize - nb;
        int p = this.designatedVictim;
        if (rsize >= 16) {
            int r = p + nb;
            this.designatedVictim = r;
            this.designatedVictimSize = rsize;
            setSizeAndPreviousInUseOfFreeChunk(r, rsize);
            setSizeAndPreviousInUseOfInUseChunk(p, nb);
        } else {
            int dvs = this.designatedVictimSize;
            this.designatedVictimSize = 0;
            this.designatedVictim = -1;
            setInUseAndPreviousInUse(p, dvs);
        }
        int mem = chunkToMem(p);
        checkMallocedChunk(mem, nb);
        return mem;
    }

    private int splitFromTop(int nb) {
        int rSize = this.topSize - nb;
        this.topSize = rSize;
        int p = this.top;
        int r = p + nb;
        this.top = r;
        head(r, rSize | 1);
        setSizeAndPreviousInUseOfInUseChunk(p, nb);
        int mem = chunkToMem(p);
        checkTopChunk(this.top);
        checkMallocedChunk(mem, nb);
        return mem;
    }

    private void dlfree(int mem, boolean shrink) {
        int p = memToChunk(mem);
        if (okAddress(p) && isInUse(p)) {
            checkInUseChunk(p);
            int psize = chunkSize(p);
            this.occupied -= psize;
            int next = p + psize;
            if (!previousInUse(p)) {
                int previousSize = prevFoot(p);
                int previous = p - previousSize;
                psize += previousSize;
                p = previous;
                if (okAddress(previous)) {
                    if (p != this.designatedVictim) {
                        unlinkChunk(p, previousSize);
                    } else if ((head(next) & 3) == 3) {
                        this.designatedVictimSize = psize;
                        setFreeWithPreviousInUse(p, psize, next);
                        return;
                    }
                } else {
                    throw new AssertionError();
                }
            }
            if (okNext(p, next) && previousInUse(next)) {
                if (!chunkInUse(next)) {
                    if (next == this.top) {
                        int tsize = this.topSize + psize;
                        this.topSize = tsize;
                        this.top = p;
                        head(p, tsize | 1);
                        if (p == this.designatedVictim) {
                            this.designatedVictim = -1;
                            this.designatedVictimSize = 0;
                        }
                        if (shrink) {
                            this.storage.release(p + TOP_FOOT_SIZE);
                            return;
                        }
                        return;
                    }
                    if (next == this.designatedVictim) {
                        int dsize = this.designatedVictimSize + psize;
                        this.designatedVictimSize = dsize;
                        this.designatedVictim = p;
                        setSizeAndPreviousInUseOfFreeChunk(p, dsize);
                        return;
                    }
                    int nsize = chunkSize(next);
                    psize += nsize;
                    unlinkChunk(next, nsize);
                    setSizeAndPreviousInUseOfFreeChunk(p, psize);
                    if (p == this.designatedVictim) {
                        this.designatedVictimSize = psize;
                        return;
                    }
                } else {
                    setFreeWithPreviousInUse(p, psize, next);
                }
                if (isSmall(psize)) {
                    insertSmallChunk(p, psize);
                    return;
                } else {
                    insertLargeChunk(p, psize);
                    return;
                }
            }
            throw new AssertionError("Problem with next chunk [" + p + "][" + next + ":previous-inuse=" + previousInUse(next) + "]");
        }
        throw new IllegalArgumentException("Address " + mem + " has not been allocated");
    }

    private void insertChunk(int p, int s) {
        if (isSmall(s)) {
            insertSmallChunk(p, s);
        } else {
            insertLargeChunk(p, s);
        }
    }

    private void insertSmallChunk(int p, int s) {
        int index = smallBinIndex(s);
        int h = this.smallBins[index];
        if (!smallMapIsMarked(index)) {
            markSmallMap(index);
            this.smallBins[index] = p;
            forward(p, p);
            backward(p, p);
        } else if (okAddress(h)) {
            int b = backward(h);
            forward(b, p);
            forward(p, h);
            backward(h, p);
            backward(p, b);
        } else {
            throw new AssertionError();
        }
        checkFreeChunk(p);
    }

    private void insertLargeChunk(int x, int s) {
        int index = treeBinIndex(s);
        int h = this.treeBins[index];
        index(x, index);
        child(x, 0, -1);
        child(x, 1, -1);
        if (!treeMapIsMarked(index)) {
            markTreeMap(index);
            this.treeBins[index] = x;
            parent(x, -1);
            forward(x, x);
            backward(x, x);
        } else {
            int t = h;
            int k = s << leftShiftForTreeIndex(index);
            while (true) {
                if (chunkSize(t) != s) {
                    int childIndex = (k >>> 31) & 1;
                    int child = child(t, childIndex);
                    k <<= 1;
                    if (okAddress(child)) {
                        t = child;
                    } else {
                        child(t, childIndex, x);
                        parent(x, t);
                        forward(x, x);
                        backward(x, x);
                        break;
                    }
                } else {
                    int f = forward(t);
                    if (okAddress(t) && okAddress(f)) {
                        backward(f, x);
                        forward(t, x);
                        forward(x, f);
                        backward(x, t);
                        parent(x, -1);
                    } else {
                        throw new AssertionError();
                    }
                }
            }
        }
        checkFreeChunk(x);
    }

    private void unlinkChunk(int p, int s) {
        if (isSmall(s)) {
            unlinkSmallChunk(p, s);
        } else {
            unlinkLargeChunk(p);
        }
    }

    private void unlinkSmallChunk(int p, int s) {
        int f = forward(p);
        int b = backward(p);
        int index = smallBinIndex(s);
        Validation.validate(!VALIDATING || chunkSize(p) == smallBinIndexToSize(index));
        if (f == p) {
            Validation.validate(!VALIDATING || b == p);
            clearSmallMap(index);
            this.smallBins[index] = -1;
        } else {
            if (okAddress(this.smallBins[index])) {
                if (this.smallBins[index] == p) {
                    this.smallBins[index] = f;
                }
                forward(b, f);
                backward(f, b);
                return;
            }
            throw new AssertionError();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x005e A[PHI: r8 r9
  0x005e: PHI (r8v1 'r' int) = (r8v0 'r' int), (r8v6 'r' int) binds: [B:11:0x004c, B:13:0x005b] A[DONT_GENERATE, DONT_INLINE]
  0x005e: PHI (r9v1 'rpIndex' int) = (r9v0 'rpIndex' int), (r9v6 'rpIndex' int) binds: [B:11:0x004c, B:13:0x005b] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void unlinkLargeChunk(int r6) {
        /*
            Method dump skipped, instructions count: 415
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.terracotta.offheapstore.storage.allocator.IntegerBestFitAllocator.unlinkLargeChunk(int):void");
    }

    private boolean chunkInUse(int p) {
        return (head(p) & 2) != 0;
    }

    private boolean previousInUse(int p) {
        return (head(p) & 1) != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isInUse(int p) {
        return (head(p) & 3) != 1;
    }

    private int chunkSize(int p) {
        return head(p) & (-8);
    }

    private void clearPreviousInUse(int p) {
        head(p, head(p) & (-2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int nextChunk(int p) {
        return p + chunkSize(p);
    }

    private int prevChunk(int p) {
        return p - prevFoot(p);
    }

    private boolean nextPreviousInUse(int p) {
        return (head(nextChunk(p)) & 1) != 0;
    }

    private void setFoot(int p, int s) {
        prevFoot(p + s, s);
    }

    private void setSizeAndPreviousInUseOfFreeChunk(int p, int s) {
        head(p, s | 1);
        setFoot(p, s);
    }

    private void setFreeWithPreviousInUse(int p, int s, int n) {
        clearPreviousInUse(n);
        setSizeAndPreviousInUseOfFreeChunk(p, s);
    }

    private void setInUseAndPreviousInUse(int p, int s) {
        setSizeAndPreviousInUseOfInUseChunk(p, s);
        head(p + s, head(p + s) | 1);
    }

    private void setSizeAndPreviousInUseOfInUseChunk(int p, int s) {
        head(p, s | 1 | 2);
        setFoot(p, s);
        this.occupied += s;
    }

    private int prevFoot(int p) {
        return this.storage.readInt(p);
    }

    private void prevFoot(int p, int value) {
        this.storage.writeInt(p, value);
    }

    private int head(int p) {
        return this.storage.readInt(p + 4);
    }

    private void head(int p, int value) {
        this.storage.writeInt(p + 4, value);
    }

    private int forward(int p) {
        return this.storage.readInt(p + 8);
    }

    private void forward(int p, int value) {
        this.storage.writeInt(p + 8, value);
    }

    private int backward(int p) {
        return this.storage.readInt(p + 12);
    }

    private void backward(int p, int value) {
        this.storage.writeInt(p + 12, value);
    }

    private int child(int p, int index) {
        return this.storage.readInt(p + 16 + (4 * index));
    }

    private void child(int p, int index, int value) {
        this.storage.writeInt(p + 16 + (4 * index), value);
    }

    private int parent(int p) {
        return this.storage.readInt(p + 24);
    }

    private void parent(int p, int value) {
        this.storage.writeInt(p + 24, value);
    }

    private int index(int p) {
        return this.storage.readInt(p + 28);
    }

    private void index(int p, int value) {
        this.storage.writeInt(p + 28, value);
    }

    private int leftmostChild(int x) {
        int left = child(x, 0);
        return left != -1 ? left : child(x, 1);
    }

    private static int padRequest(int req) {
        return (req + 8 + 7) & (-8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int chunkToMem(int p) {
        return p + 8;
    }

    private static int memToChunk(int p) {
        return p - 8;
    }

    private static boolean okAddress(int a) {
        return a >= 0;
    }

    private static boolean okNext(int p, int n) {
        return p < n;
    }

    private static boolean isAligned(int a) {
        return (a & 7) == 0;
    }

    private static int alignOffset(int a) {
        if ((a & 7) == 0) {
            return 0;
        }
        return (8 - (a & 7)) & 7;
    }

    private static boolean isSmall(int s) {
        return smallBinIndex(s) < 32;
    }

    private static int smallBinIndex(int s) {
        return s >>> 3;
    }

    private static int smallBinIndexToSize(int i) {
        return i << 3;
    }

    private static int treeBinIndex(int s) {
        int x = s >>> 8;
        if (x == 0) {
            return 0;
        }
        if (x > 65535) {
            return 31;
        }
        int k = 31 - Integer.numberOfLeadingZeros(x);
        return (k << 1) + ((s >>> (k + 7)) & 1);
    }

    private void markSmallMap(int i) {
        this.smallMap |= 1 << i;
    }

    private void clearSmallMap(int i) {
        this.smallMap &= (1 << i) ^ (-1);
    }

    private boolean smallMapIsMarked(int i) {
        return (this.smallMap & (1 << i)) != 0;
    }

    private void markTreeMap(int i) {
        this.treeMap |= 1 << i;
    }

    private void clearTreeMap(int i) {
        this.treeMap &= (1 << i) ^ (-1);
    }

    private boolean treeMapIsMarked(int i) {
        return (this.treeMap & (1 << i)) != 0;
    }

    private static int leftShiftForTreeIndex(int i) {
        if (i == 31) {
            return 0;
        }
        return 31 - (((i >>> 1) + 8) - 2);
    }

    private static int minSizeForTreeIndex(int i) {
        return (1 << ((i >>> 1) + 8)) | ((i & 1) << (((i >>> 1) + 8) - 1));
    }

    private static int leftBits(int i) {
        return (i << 1) | (-(i << 1));
    }

    @Override // org.terracotta.offheapstore.storage.allocator.Allocator
    public void validateAllocator() {
        if (this.topSize < 0) {
            return;
        }
        traverseAndCheck();
        for (int i = 0; i < this.smallBins.length; i++) {
            checkSmallBin(i);
        }
        for (int i2 = 0; i2 < this.treeBins.length; i2++) {
            checkTreeBin(i2);
        }
    }

    public int validateMallocedPointer(int m) {
        int p = memToChunk(m);
        checkMallocedChunk(m, chunkSize(p));
        if (findFreeChunk(p)) {
            throw new AssertionError();
        }
        return chunkSize(p);
    }

    private void checkAnyChunk(int p) {
        if (VALIDATING) {
            if (!isAligned(chunkToMem(p))) {
                throw new AssertionError("Chunk address [mem:" + p + "=>chunk:" + chunkToMem(p) + "] is incorrectly aligned");
            }
            if (!okAddress(p)) {
                throw new AssertionError("Memory address " + p + " is invalid");
            }
        }
    }

    private void checkTopChunk(int p) {
        if (VALIDATING) {
            int sz = head(p) & (-4);
            if (!isAligned(chunkToMem(p))) {
                throw new AssertionError("Chunk address [mem:" + p + "=>chunk:" + chunkToMem(p) + "] of top chunk is incorrectly aligned");
            }
            if (!okAddress(p)) {
                throw new AssertionError("Memory address " + p + " of top chunk is invalid");
            }
            if (sz != this.topSize) {
                throw new AssertionError("Marked size top chunk " + sz + " is not equals to the recorded top size " + this.topSize);
            }
            if (sz <= 0) {
                throw new AssertionError("Top chunk size " + sz + " is not positive");
            }
            if (!previousInUse(p)) {
                throw new AssertionError("Chunk before top chunk is free - why has it not been merged in to the top chunk?");
            }
        }
    }

    private void checkInUseChunk(int p) {
        if (VALIDATING) {
            checkAnyChunk(p);
            if (!isInUse(p)) {
                throw new AssertionError("Chunk at " + p + " is not in use");
            }
            if (!nextPreviousInUse(p)) {
                throw new AssertionError("Chunk after " + p + " does not see this chunk as in use");
            }
            if (!previousInUse(p) && nextChunk(prevChunk(p)) != p) {
                throw new AssertionError("Previous chunk to " + p + " is marked free but has an incorrect next pointer");
            }
        }
    }

    private void checkFreeChunk(int p) {
        if (VALIDATING) {
            int sz = chunkSize(p);
            int next = p + sz;
            checkAnyChunk(p);
            if (isInUse(p)) {
                throw new AssertionError("Free chunk " + p + " is not marked as free");
            }
            if (nextPreviousInUse(p)) {
                throw new AssertionError("Next chunk after " + p + " has it marked as in use");
            }
            if (p != this.designatedVictim && p != this.top) {
                if (sz < 16) {
                    throw new AssertionError("Free chunk " + p + " is too small");
                }
                if ((sz & 7) != 0) {
                    throw new AssertionError("Chunk size " + sz + " of " + p + " is not correctly aligned");
                }
                if (!isAligned(chunkToMem(p))) {
                    throw new AssertionError("User pointer for chunk " + p + " is not correctly aligned");
                }
                if (prevFoot(next) != sz) {
                    throw new AssertionError("Next chunk after " + p + " has an incorrect previous size");
                }
                if (!previousInUse(p)) {
                    throw new AssertionError("Chunk before free chunk " + p + " is free - should have been merged");
                }
                if (next != this.top && !isInUse(next)) {
                    throw new AssertionError("Chunk after free chunk " + p + " is free - should have been merged");
                }
                if (backward(forward(p)) != p) {
                    throw new AssertionError("Free chunk " + p + " has invalid chain links");
                }
                if (forward(backward(p)) != p) {
                    throw new AssertionError("Free chunk " + p + " has invalid chain links");
                }
            }
        }
    }

    private void checkMallocedChunk(int mem, int s) {
        if (VALIDATING) {
            int p = memToChunk(mem);
            int sz = head(p) & (-4);
            checkInUseChunk(p);
            if (sz < 16) {
                throw new AssertionError("Allocated chunk " + p + " is too small");
            }
            if ((sz & 7) != 0) {
                throw new AssertionError("Chunk size " + sz + " of " + p + " is not correctly aligned");
            }
            if (sz < s) {
                throw new AssertionError("Allocated chunk " + p + " is smaller than requested [" + sz + "<" + s + "]");
            }
            if (sz > s + 16) {
                throw new AssertionError("Allocated chunk " + p + " is too large (should have been split off) [" + sz + ">>" + s + "]");
            }
        }
    }

    private void checkTree(int t) {
        int head = -1;
        int u = t;
        int tindex = index(t);
        int tsize = chunkSize(t);
        int index = treeBinIndex(tsize);
        if (tindex != index) {
            throw new AssertionError("Tree node " + u + " has incorrect index [" + index(u) + "!=" + tindex + "]");
        }
        if (tsize < 256) {
            throw new AssertionError("Tree node " + u + " is too small to be in a tree [" + tsize + "<256]");
        }
        if (tsize < minSizeForTreeIndex(index)) {
            throw new AssertionError("Tree node " + u + " is too small to be in this tree [" + tsize + "<" + minSizeForTreeIndex(index) + "]");
        }
        if (index != 31 && tsize >= minSizeForTreeIndex(index + 1)) {
            throw new AssertionError("Tree node " + u + " is too large to be in this tree [" + tsize + ">=" + minSizeForTreeIndex(index + 1) + "]");
        }
        do {
            checkAnyChunk(u);
            if (index(u) != tindex) {
                throw new AssertionError("Tree node " + u + " has incorrect index [" + index(u) + "!=" + tindex + "]");
            }
            if (chunkSize(u) != tsize) {
                throw new AssertionError("Tree node " + u + " has an mismatching size [" + chunkSize(u) + "!=" + tsize + "]");
            }
            if (isInUse(u)) {
                throw new AssertionError("Tree node " + u + " is in use");
            }
            if (nextPreviousInUse(u)) {
                throw new AssertionError("Tree node " + u + " is marked as in use in the next chunk");
            }
            if (backward(forward(u)) != u) {
                throw new AssertionError("Tree node " + u + " has incorrect chain links");
            }
            if (forward(backward(u)) != u) {
                throw new AssertionError("Tree node " + u + " has incorrect chain links");
            }
            if (parent(u) == -1 && u != this.treeBins[index]) {
                if (child(u, 0) != -1) {
                    throw new AssertionError("Tree node " + u + " is chained from the tree but has a child " + child(u, 0));
                }
                if (child(u, 1) != -1) {
                    throw new AssertionError("Tree node " + u + " is chained from the tree but has a child" + child(u, 1));
                }
            } else {
                if (head != -1) {
                    throw new AssertionError("Tree node " + u + " is the second node in this chain with a parent [first was " + head + "]");
                }
                head = u;
                if (this.treeBins[index] == u) {
                    if (parent(u) != -1) {
                        throw new AssertionError("Tree node " + u + " is the head of the tree but has a parent " + parent(u));
                    }
                } else {
                    if (parent(u) == u) {
                        throw new AssertionError("Tree node " + u + " is its own parent");
                    }
                    if (child(parent(u), 0) != u && child(parent(u), 1) != u) {
                        throw new AssertionError("Tree node " + u + " is not a child of its parent");
                    }
                }
                if (child(u, 0) != -1) {
                    if (parent(child(u, 0)) != u) {
                        throw new AssertionError("Tree node " + u + " is not the parent of its left child");
                    }
                    if (child(u, 0) == u) {
                        throw new AssertionError("Tree node " + u + " is its own left child");
                    }
                    checkTree(child(u, 0));
                }
                if (child(u, 1) != -1) {
                    if (parent(child(u, 1)) != u) {
                        throw new AssertionError("Tree node " + u + " is not the parent of its right child");
                    }
                    if (child(u, 1) == u) {
                        throw new AssertionError("Tree node " + u + " is its own right child");
                    }
                    checkTree(child(u, 1));
                }
                if (child(u, 0) != -1 && child(u, 1) != -1 && chunkSize(child(u, 0)) >= chunkSize(child(u, 1))) {
                    throw new AssertionError("Tree node " + u + " has it's left child bigger than it's right child");
                }
            }
            u = forward(u);
        } while (u != t);
        if (head == -1) {
            throw new AssertionError("This tree level has no nodes with a parent");
        }
    }

    private void checkTreeBin(int index) {
        int tb = this.treeBins[index];
        boolean empty = (this.treeMap & (1 << index)) == 0;
        if (tb == -1 && !empty) {
            throw new AssertionError("Tree " + index + " is marked as occupied but has an invalid head pointer");
        }
        if (!empty) {
            checkTree(tb);
        }
    }

    private void checkSmallBin(int index) {
        int h = this.smallBins[index];
        boolean empty = (this.smallMap & (1 << index)) == 0;
        if (h == -1 && !empty) {
            throw new AssertionError("Small bin chain " + index + " is marked as occupied but has an invalid head pointer");
        }
        if (!empty) {
            int p = h;
            do {
                int size = chunkSize(p);
                checkFreeChunk(p);
                if (smallBinIndex(size) != index) {
                    throw new AssertionError("Chunk " + p + " is the wrong size to be in bin " + index);
                }
                if (backward(p) != p && chunkSize(backward(p)) != chunkSize(p)) {
                    throw new AssertionError("Chunk " + p + " is the linked to a chunk of the wrong size");
                }
                checkInUseChunk(nextChunk(p));
                p = backward(p);
            } while (p != h);
        }
    }

    private int traverseAndCheck() {
        int sum = 0 + this.topSize + TOP_FOOT_SIZE;
        int q = 0;
        int lastq = 0;
        if (!previousInUse(0)) {
            throw new AssertionError("Chunk before zeroth chunk is marked as free");
        }
        while (q != this.top) {
            sum += chunkSize(q);
            if (isInUse(q)) {
                if (findFreeChunk(q)) {
                    throw new AssertionError("Chunk marked as in-use appears in a free structure");
                }
                checkInUseChunk(q);
            } else {
                if (q != this.designatedVictim && !findFreeChunk(q)) {
                    throw new AssertionError("Chunk marked as free cannot be found in any free structure");
                }
                if (lastq != 0 && !isInUse(lastq)) {
                    throw new AssertionError("Chunk before free chunk is not in-use");
                }
                checkFreeChunk(q);
            }
            lastq = q;
            q = nextChunk(q);
        }
        return sum;
    }

    private boolean findFreeChunk(int x) {
        int size = chunkSize(x);
        if (isSmall(size)) {
            int sidx = smallBinIndex(size);
            int h = this.smallBins[sidx];
            if (smallMapIsMarked(sidx)) {
                int p = h;
                while (p != x) {
                    int iForward = forward(p);
                    p = iForward;
                    if (iForward == h) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
        int tidx = treeBinIndex(size);
        if (treeMapIsMarked(tidx)) {
            int t = this.treeBins[tidx];
            int i = size;
            int iLeftShiftForTreeIndex = leftShiftForTreeIndex(tidx);
            while (true) {
                int sizebits = i << iLeftShiftForTreeIndex;
                if (t == -1 || chunkSize(t) == size) {
                    break;
                }
                t = child(t, (sizebits >>> 31) & 1);
                i = sizebits;
                iLeftShiftForTreeIndex = 1;
            }
            if (t != -1) {
                int u = t;
                while (u != x) {
                    int iForward2 = forward(u);
                    u = iForward2;
                    if (iForward2 == t) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
        return false;
    }

    @Override // org.terracotta.offheapstore.storage.allocator.Allocator
    public long getLastUsedPointer() {
        if (this.top <= 0) {
            return -1L;
        }
        return chunkToMem(prevChunk(this.top));
    }

    @Override // org.terracotta.offheapstore.storage.allocator.Allocator
    public long getLastUsedAddress() {
        if (this.top <= 0) {
            return 0L;
        }
        return this.top;
    }

    @Override // org.terracotta.offheapstore.storage.allocator.Allocator
    public int getMinimalSize() {
        return MINIMAL_SIZE;
    }

    @Override // org.terracotta.offheapstore.storage.allocator.Allocator
    public long getMaximumAddress() {
        return 2147483647L;
    }

    @Override // java.lang.Iterable
    public Iterator<Long> iterator() {
        return new Iterator<Long>() { // from class: org.terracotta.offheapstore.storage.allocator.IntegerBestFitAllocator.1
            private int current;

            {
                this.current = (IntegerBestFitAllocator.this.isInUse(0) || 0 >= IntegerBestFitAllocator.this.top) ? 0 : IntegerBestFitAllocator.this.nextChunk(0);
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.current < IntegerBestFitAllocator.this.top;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Iterator
            public Long next() {
                if (this.current >= IntegerBestFitAllocator.this.top) {
                    throw new NoSuchElementException();
                }
                int result = this.current;
                int next = IntegerBestFitAllocator.this.nextChunk(this.current);
                if (next >= IntegerBestFitAllocator.this.top) {
                    this.current = next;
                } else {
                    this.current = IntegerBestFitAllocator.this.isInUse(next) ? next : IntegerBestFitAllocator.this.nextChunk(next);
                }
                return Long.valueOf(IntegerBestFitAllocator.chunkToMem(result));
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
