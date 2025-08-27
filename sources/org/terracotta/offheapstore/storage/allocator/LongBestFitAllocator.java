package org.terracotta.offheapstore.storage.allocator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.terracotta.offheapstore.paging.OffHeapStorageArea;
import org.terracotta.offheapstore.util.FindbugsSuppressWarnings;
import org.terracotta.offheapstore.util.Validation;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/storage/allocator/LongBestFitAllocator.class */
public final class LongBestFitAllocator implements Allocator {
    private static final int SIZE_T_BITSIZE = 64;
    private static final int SIZE_T_SIZE = 8;
    private static final int MALLOC_ALIGNMENT = 16;
    private static final int CHUNK_ALIGN_MASK = 15;
    private static final int MCHUNK_SIZE = 32;
    private static final int CHUNK_OVERHEAD = 16;
    private static final long MAX_REQUEST = 2147483520;
    private static final long MIN_REQUEST = 15;
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
    private static final int MAX_SMALL_REQUEST = 224;
    private final OffHeapStorageArea storage;
    private int smallMap;
    private int treeMap;
    private final long[] smallBins = new long[32];
    private final long[] treeBins = new long[32];
    private long designatedVictimSize = 0;
    private long designatedVictim = -1;
    private long topSize = 0;
    private long top = 0;
    private long occupied;
    private static final boolean DEBUG = Boolean.getBoolean(LongBestFitAllocator.class.getName() + ".DEBUG");
    private static final boolean VALIDATING = Validation.shouldValidate(LongBestFitAllocator.class);
    private static final long MIN_CHUNK_SIZE = 32;
    private static final long TOP_FOOT_SIZE = alignOffset(chunkToMem(0)) + padRequest(MIN_CHUNK_SIZE);
    private static final long MINIMAL_SIZE = Long.highestOneBit(TOP_FOOT_SIZE) << 1;
    private static final long TOP_FOOT_OFFSET = memToChunk(0) + TOP_FOOT_SIZE;

    /*  JADX ERROR: Failed to decode insn: 0x001A: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    private long splitFromDesignatedVictim(long r7) {
        /*
            r6 = this;
            r0 = r6
            long r0 = r0.designatedVictimSize
            r1 = r7
            long r0 = r0 - r1
            r9 = r0
            r0 = r6
            long r0 = r0.designatedVictim
            r11 = r0
            r0 = r9
            r1 = 32
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 < 0) goto L36
            r0 = r6
            r1 = r11
            r2 = r7
            long r1 = r1 + r2
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.designatedVictim = r1
            r13 = r-1
            r-1 = r6
            r0 = r9
            r-1.designatedVictimSize = r0
            r-1 = r6
            r0 = r13
            r1 = r9
            r-1.setSizeAndPreviousInUseOfFreeChunk(r0, r1)
            r-1 = r6
            r0 = r11
            r1 = r7
            r-1.setSizeAndPreviousInUseOfInUseChunk(r0, r1)
            goto L50
            r0 = r6
            long r0 = r0.designatedVictimSize
            r13 = r0
            r0 = r6
            r1 = 0
            r0.designatedVictimSize = r1
            r0 = r6
            r1 = -1
            r0.designatedVictim = r1
            r0 = r6
            r1 = r11
            r2 = r13
            r0.setInUseAndPreviousInUse(r1, r2)
            r-1 = r11
            chunkToMem(r-1)
            r13 = r-1
            r-1 = r6
            r0 = r13
            r1 = r7
            r-1.checkMallocedChunk(r0, r1)
            r-1 = r13
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.terracotta.offheapstore.storage.allocator.LongBestFitAllocator.splitFromDesignatedVictim(long):long");
    }

    /*  JADX ERROR: Failed to decode insn: 0x0007: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[8]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    /*  JADX ERROR: Failed to decode insn: 0x0017: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -2 out of bounds for object array[8]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    private long splitFromTop(long r9) {
        /*
            r8 = this;
            r0 = r8
            r1 = r0
            long r1 = r1.topSize
            r2 = r9
            long r1 = r1 - r2
            // decode failed: arraycopy: source index -1 out of bounds for object array[8]
            r0.topSize = r1
            r11 = r-1
            r-1 = r8
            long r-1 = r-1.top
            r13 = r-1
            r-1 = r8
            r0 = r13
            r1 = r9
            long r0 = r0 + r1
            // decode failed: arraycopy: source index -2 out of bounds for object array[8]
            r-1.top = r0
            r15 = r-2
            r-2 = r8
            r-1 = r15
            r0 = r11
            r1 = 1
            long r0 = r0 | r1
            r-2.head(r-1, r0)
            r-2 = r8
            r-1 = r13
            r0 = r9
            r-2.setSizeAndPreviousInUseOfInUseChunk(r-1, r0)
            r-2 = r13
            long r-2 = chunkToMem(r-2)
            r17 = r-2
            r-2 = r8
            r-1 = r8
            long r-1 = r-1.top
            r-2.checkTopChunk(r-1)
            r-2 = r8
            r-1 = r17
            r0 = r9
            r-2.checkMallocedChunk(r-1, r0)
            r-2 = r17
            return r-2
        */
        throw new UnsupportedOperationException("Method not decompiled: org.terracotta.offheapstore.storage.allocator.LongBestFitAllocator.splitFromTop(long):long");
    }

    /*  JADX ERROR: Failed to decode insn: 0x00CF: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[8]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    /*  JADX ERROR: Failed to decode insn: 0x011F: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[8]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    private void dlfree(long r9, boolean r11) {
        /*
            Method dump skipped, instructions count: 492
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.terracotta.offheapstore.storage.allocator.LongBestFitAllocator.dlfree(long, boolean):void");
    }

    public LongBestFitAllocator(OffHeapStorageArea storage) {
        this.storage = storage;
        clear();
    }

    @Override // org.terracotta.offheapstore.storage.allocator.Allocator
    public void clear() {
        this.top = 0L;
        this.topSize = -TOP_FOOT_SIZE;
        this.designatedVictim = -1L;
        this.designatedVictimSize = 0L;
        for (int i = 0; i < this.treeBins.length; i++) {
            this.treeBins[i] = -1;
            clearTreeMap(i);
        }
        for (int i2 = 0; i2 < this.smallBins.length; i2++) {
            this.smallBins[i2] = -1;
            clearSmallMap(i2);
        }
        this.occupied = 0L;
    }

    @Override // org.terracotta.offheapstore.storage.allocator.Allocator
    public void expand(long increase) {
        this.topSize += increase;
        head(this.top, this.topSize | 1);
        if (this.topSize >= 0) {
            checkTopChunk(this.top);
        }
    }

    @Override // org.terracotta.offheapstore.storage.allocator.Allocator
    public long allocate(long size) {
        return dlmalloc(size);
    }

    @Override // org.terracotta.offheapstore.storage.allocator.Allocator
    public void free(long address) {
        dlfree(address, true);
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
        long jNextChunk = 0;
        while (true) {
            long q = jNextChunk;
            if (q != this.top) {
                if (isInUse(q)) {
                    sb.append(" InUseChunk:&").append(q).append(':').append(chunkSize(q)).append('b');
                } else {
                    sb.append(" FreeChunk:&").append(q).append(':').append(chunkSize(q)).append('b');
                }
                jNextChunk = nextChunk(q);
            } else {
                sb.append(" TopChunk:&").append(this.top).append(':').append(this.topSize + TOP_FOOT_SIZE).append('b');
                return sb.toString();
            }
        }
    }

    private long dlmalloc(long bytes) {
        long nb = bytes < MIN_REQUEST ? MIN_CHUNK_SIZE : padRequest(bytes);
        if (bytes <= 224) {
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
                return -1L;
            }
            if (this.treeMap != 0) {
                long mem = splitFromTree(nb);
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
        return -1L;
    }

    private long allocateFromSmallBin(int index, long nb) {
        long h = this.smallBins[index];
        Validation.validate(!VALIDATING || chunkSize(h) == ((long) smallBinIndexToSize(index)));
        long f = forward(h);
        long b = backward(h);
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
        long mem = chunkToMem(h);
        checkMallocedChunk(mem, nb);
        return mem;
    }

    private long splitFromSmallBin(int index, long nb) {
        long h = this.smallBins[index];
        Validation.validate(!VALIDATING || chunkSize(h) == ((long) smallBinIndexToSize(index)));
        long f = forward(h);
        long b = backward(h);
        if (f == h) {
            Validation.validate(!VALIDATING || b == h);
            clearSmallMap(index);
            this.smallBins[index] = -1;
        } else {
            this.smallBins[index] = f;
            backward(f, b);
            forward(b, f);
        }
        long rsize = smallBinIndexToSize(index) - nb;
        if (rsize < MIN_CHUNK_SIZE) {
            setInUseAndPreviousInUse(h, smallBinIndexToSize(index));
        } else {
            setSizeAndPreviousInUseOfInUseChunk(h, nb);
            long r = h + nb;
            setSizeAndPreviousInUseOfFreeChunk(r, rsize);
            replaceDesignatedVictim(r, rsize);
        }
        long mem = chunkToMem(h);
        checkMallocedChunk(mem, nb);
        return mem;
    }

    private void replaceDesignatedVictim(long p, long s) {
        long dvs = this.designatedVictimSize;
        if (dvs != 0) {
            long dv = this.designatedVictim;
            Validation.validate(!VALIDATING || isSmall(dvs));
            insertSmallChunk(dv, dvs);
        }
        this.designatedVictimSize = s;
        this.designatedVictim = p;
    }

    private long splitSmallFromTree(long nb) {
        int index = Integer.numberOfTrailingZeros(this.treeMap);
        long j = this.treeBins[index];
        long t = j;
        long v = j;
        long rsize = chunkSize(t) - nb;
        while (true) {
            long jLeftmostChild = leftmostChild(t);
            t = jLeftmostChild;
            if (jLeftmostChild == -1) {
                break;
            }
            long trem = chunkSize(t) - nb;
            if (trem >= 0 && trem < rsize) {
                rsize = trem;
                v = t;
            }
        }
        if (okAddress(v)) {
            long r = v + nb;
            Validation.validate(!VALIDATING || chunkSize(v) == rsize + nb);
            if (okNext(v, r)) {
                unlinkLargeChunk(v);
                if (rsize < MIN_CHUNK_SIZE) {
                    setInUseAndPreviousInUse(v, rsize + nb);
                } else {
                    setSizeAndPreviousInUseOfInUseChunk(v, nb);
                    setSizeAndPreviousInUseOfFreeChunk(r, rsize);
                    replaceDesignatedVictim(r, rsize);
                }
                long mem = chunkToMem(v);
                checkMallocedChunk(mem, nb);
                return mem;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x005a A[PHI: r11 r13
  0x005a: PHI (r11v6 'v' long) = (r11v5 'v' long), (r11v5 'v' long), (r11v7 'v' long) binds: [B:6:0x003f, B:8:0x0047, B:10:0x0054] A[DONT_GENERATE, DONT_INLINE]
  0x005a: PHI (r13v6 'rsize' long) = (r13v5 'rsize' long), (r13v5 'rsize' long), (r13v7 'rsize' long) binds: [B:6:0x003f, B:8:0x0047, B:10:0x0054] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private long splitFromTree(long r9) {
        /*
            Method dump skipped, instructions count: 406
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.terracotta.offheapstore.storage.allocator.LongBestFitAllocator.splitFromTree(long):long");
    }

    private void insertChunk(long p, long s) {
        if (isSmall(s)) {
            insertSmallChunk(p, s);
        } else {
            insertLargeChunk(p, s);
        }
    }

    private void insertSmallChunk(long p, long s) {
        int index = smallBinIndex(s);
        long h = this.smallBins[index];
        if (!smallMapIsMarked(index)) {
            markSmallMap(index);
            this.smallBins[index] = p;
            forward(p, p);
            backward(p, p);
        } else if (okAddress(h)) {
            long b = backward(h);
            forward(b, p);
            forward(p, h);
            backward(h, p);
            backward(p, b);
        } else {
            throw new AssertionError();
        }
        checkFreeChunk(p);
    }

    private void insertLargeChunk(long x, long s) {
        int index = treeBinIndex(s);
        long h = this.treeBins[index];
        index(x, index);
        child(x, 0, -1L);
        child(x, 1, -1L);
        if (!treeMapIsMarked(index)) {
            markTreeMap(index);
            this.treeBins[index] = x;
            parent(x, -1L);
            forward(x, x);
            backward(x, x);
        } else {
            long t = h;
            long k = s << leftShiftForTreeIndex(index);
            while (true) {
                if (chunkSize(t) != s) {
                    int childIndex = (int) ((k >>> 63) & 1);
                    long child = child(t, childIndex);
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
                    long f = forward(t);
                    if (okAddress(t) && okAddress(f)) {
                        backward(f, x);
                        forward(t, x);
                        forward(x, f);
                        backward(x, t);
                        parent(x, -1L);
                    } else {
                        throw new AssertionError();
                    }
                }
            }
        }
        checkFreeChunk(x);
    }

    private void unlinkChunk(long p, long s) {
        if (isSmall(s)) {
            unlinkSmallChunk(p, s);
        } else {
            unlinkLargeChunk(p);
        }
    }

    private void unlinkSmallChunk(long p, long s) {
        long f = forward(p);
        long b = backward(p);
        int index = smallBinIndex(s);
        Validation.validate(!VALIDATING || chunkSize(p) == ((long) smallBinIndexToSize(index)));
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

    /* JADX WARN: Removed duplicated region for block: B:14:0x006a A[PHI: r12 r14
  0x006a: PHI (r12v1 'r' long) = (r12v0 'r' long), (r12v6 'r' long) binds: [B:11:0x0054, B:13:0x0067] A[DONT_GENERATE, DONT_INLINE]
  0x006a: PHI (r14v1 'rpIndex' int) = (r14v0 'rpIndex' int), (r14v6 'rpIndex' int) binds: [B:11:0x0054, B:13:0x0067] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void unlinkLargeChunk(long r8) {
        /*
            Method dump skipped, instructions count: 474
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.terracotta.offheapstore.storage.allocator.LongBestFitAllocator.unlinkLargeChunk(long):void");
    }

    private boolean chunkInUse(long p) {
        return (head(p) & 2) != 0;
    }

    private boolean previousInUse(long p) {
        return (head(p) & 1) != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isInUse(long p) {
        return (head(p) & 3) != 1;
    }

    private long chunkSize(long p) {
        return head(p) & (-8);
    }

    private void clearPreviousInUse(long p) {
        head(p, head(p) & (-2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long nextChunk(long p) {
        return p + chunkSize(p);
    }

    private long prevChunk(long p) {
        return p - prevFoot(p);
    }

    private boolean nextPreviousInUse(long p) {
        return (head(nextChunk(p)) & 1) != 0;
    }

    private void setFoot(long p, long s) {
        prevFoot(p + s, s);
    }

    private void setSizeAndPreviousInUseOfFreeChunk(long p, long s) {
        head(p, s | 1);
        setFoot(p, s);
    }

    private void setFreeWithPreviousInUse(long p, long s, long n) {
        clearPreviousInUse(n);
        setSizeAndPreviousInUseOfFreeChunk(p, s);
    }

    private void setInUseAndPreviousInUse(long p, long s) {
        setSizeAndPreviousInUseOfInUseChunk(p, s);
        head(p + s, head(p + s) | 1);
    }

    private void setSizeAndPreviousInUseOfInUseChunk(long p, long s) {
        head(p, s | 1 | 2);
        setFoot(p, s);
        this.occupied += s;
    }

    private long prevFoot(long p) {
        return this.storage.readLong(p);
    }

    private void prevFoot(long p, long value) {
        this.storage.writeLong(p, value);
    }

    private long head(long p) {
        return this.storage.readLong(p + 8);
    }

    private void head(long p, long value) {
        this.storage.writeLong(p + 8, value);
    }

    private long forward(long p) {
        return this.storage.readLong(p + 16);
    }

    private void forward(long p, long value) {
        this.storage.writeLong(p + 16, value);
    }

    private long backward(long p) {
        return this.storage.readLong(p + 24);
    }

    private void backward(long p, long value) {
        this.storage.writeLong(p + 24, value);
    }

    @FindbugsSuppressWarnings({"ICAST_INTEGER_MULTIPLY_CAST_TO_LONG"})
    private long child(long p, int index) {
        return this.storage.readLong(p + ((4 + index) * 8));
    }

    @FindbugsSuppressWarnings({"ICAST_INTEGER_MULTIPLY_CAST_TO_LONG"})
    private void child(long p, int index, long value) {
        this.storage.writeLong(p + ((4 + index) * 8), value);
    }

    private long parent(long p) {
        return this.storage.readLong(p + 48);
    }

    private void parent(long p, long value) {
        this.storage.writeLong(p + 48, value);
    }

    private int index(long p) {
        return this.storage.readInt(p + 56);
    }

    private void index(long p, int value) {
        this.storage.writeInt(p + 56, value);
    }

    private long leftmostChild(long x) {
        long left = child(x, 0);
        return left != -1 ? left : child(x, 1);
    }

    private static long padRequest(long req) {
        return (req + 16 + MIN_REQUEST) & (-16);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long chunkToMem(long p) {
        return p + 16;
    }

    private static long memToChunk(long p) {
        return p - 16;
    }

    private static boolean okAddress(long a) {
        return a >= 0;
    }

    private static boolean okNext(long p, long n) {
        return p < n;
    }

    private static boolean isAligned(long a) {
        return (a & MIN_REQUEST) == 0;
    }

    private static long alignOffset(long a) {
        if ((a & MIN_REQUEST) == 0) {
            return 0L;
        }
        return (16 - (a & MIN_REQUEST)) & MIN_REQUEST;
    }

    private static boolean isSmall(long s) {
        return smallBinIndex(s) < 32;
    }

    private static int smallBinIndex(long s) {
        return Integer.MAX_VALUE & ((int) (s >>> 3));
    }

    private static int smallBinIndexToSize(int i) {
        return i << 3;
    }

    private static int treeBinIndex(long s) {
        int x = Integer.MAX_VALUE & ((int) (s >>> 8));
        if (x == 0) {
            return 0;
        }
        if (x > 65535) {
            return 31;
        }
        int k = 31 - Integer.numberOfLeadingZeros(x);
        return (k << 1) + ((int) ((s >>> (k + 7)) & 1));
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
        return 63 - (((i >>> 1) + 8) - 2);
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

    public long validateMallocedPointer(long m) {
        long p = memToChunk(m);
        checkMallocedChunk(m, chunkSize(p));
        if (findFreeChunk(p)) {
            throw new AssertionError();
        }
        return chunkSize(p);
    }

    private void checkAnyChunk(long p) {
        if (VALIDATING) {
            if (!isAligned(chunkToMem(p))) {
                throw new AssertionError("Chunk address [mem:" + p + "=>chunk:" + chunkToMem(p) + "] is incorrectly aligned");
            }
            if (!okAddress(p)) {
                throw new AssertionError("Memory address " + p + " is invalid");
            }
        }
    }

    private void checkTopChunk(long p) {
        if (VALIDATING) {
            long sz = head(p) & (-4);
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

    private void checkInUseChunk(long p) {
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

    private void checkFreeChunk(long p) {
        if (VALIDATING) {
            long sz = chunkSize(p);
            long next = p + sz;
            checkAnyChunk(p);
            if (isInUse(p)) {
                throw new AssertionError("Free chunk " + p + " is not marked as free");
            }
            if (nextPreviousInUse(p)) {
                throw new AssertionError("Next chunk after " + p + " has it marked as in use");
            }
            if (p == this.designatedVictim || p == this.top) {
                return;
            }
            if (sz < MIN_CHUNK_SIZE) {
                throw new AssertionError("Free chunk " + p + " is too small");
            }
            if ((sz & MIN_REQUEST) != 0) {
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

    private void checkMallocedChunk(long mem, long s) {
        if (VALIDATING) {
            long p = memToChunk(mem);
            long sz = head(p) & (-4);
            checkInUseChunk(p);
            if (sz < MIN_CHUNK_SIZE) {
                throw new AssertionError("Allocated chunk " + p + " is too small");
            }
            if ((sz & MIN_REQUEST) != 0) {
                throw new AssertionError("Chunk size " + sz + " of " + p + " is not correctly aligned");
            }
            if (sz < s) {
                throw new AssertionError("Allocated chunk " + p + " is smaller than requested [" + sz + "<" + s + "]");
            }
            if (sz > s + MIN_CHUNK_SIZE) {
                throw new AssertionError("Allocated chunk " + p + " is too large (should have been split off) [" + sz + ">>" + s + "]");
            }
        }
    }

    private void checkTree(long t) {
        long head = -1;
        long u = t;
        int tindex = index(t);
        long tsize = chunkSize(t);
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
            if (parent(u) != -1 || u == this.treeBins[index]) {
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
            } else {
                if (child(u, 0) != -1) {
                    throw new AssertionError("Tree node " + u + " is chained from the tree but has a child " + child(u, 0));
                }
                if (child(u, 1) != -1) {
                    throw new AssertionError("Tree node " + u + " is chained from the tree but has a child" + child(u, 1));
                }
            }
            u = forward(u);
        } while (u != t);
        if (head == -1) {
            throw new AssertionError("This tree level has no nodes with a parent");
        }
    }

    private void checkTreeBin(int index) {
        long tb = this.treeBins[index];
        boolean empty = (this.treeMap & (1 << index)) == 0;
        if (tb == -1 && !empty) {
            throw new AssertionError("Tree " + index + " is marked as occupied but has an invalid head pointer");
        }
        if (!empty) {
            checkTree(tb);
        }
    }

    private void checkSmallBin(int index) {
        long h = this.smallBins[index];
        boolean empty = (this.smallMap & (1 << index)) == 0;
        if (h == -1 && !empty) {
            throw new AssertionError("Small bin chain " + index + " is marked as occupied but has an invalid head pointer");
        }
        if (!empty) {
            long p = h;
            do {
                long size = chunkSize(p);
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

    private long traverseAndCheck() {
        long sum = 0 + this.topSize + TOP_FOOT_SIZE;
        long q = 0;
        long lastq = 0;
        if (!previousInUse(0L)) {
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

    private boolean findFreeChunk(long x) {
        int i;
        long size = chunkSize(x);
        if (isSmall(size)) {
            int sidx = smallBinIndex(size);
            long h = this.smallBins[sidx];
            if (smallMapIsMarked(sidx)) {
                long p = h;
                while (p != x) {
                    long jForward = forward(p);
                    p = jForward;
                    if (jForward == h) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
        int tidx = treeBinIndex(size);
        if (treeMapIsMarked(tidx)) {
            long t = this.treeBins[tidx];
            long j = size;
            int iLeftShiftForTreeIndex = leftShiftForTreeIndex(tidx);
            while (true) {
                long sizebits = j << iLeftShiftForTreeIndex;
                if (t == -1 || chunkSize(t) == size) {
                    break;
                }
                i = (int) ((sizebits >>> 63) & 1);
                t = child(t, i);
                j = sizebits;
                iLeftShiftForTreeIndex = 1;
            }
            if (t != -1) {
                long u = t;
                while (u != x) {
                    u = forward(u);
                    if (i == t) {
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
        return (int) MINIMAL_SIZE;
    }

    @Override // org.terracotta.offheapstore.storage.allocator.Allocator
    public long getMaximumAddress() {
        return Long.MAX_VALUE;
    }

    @Override // java.lang.Iterable
    public Iterator<Long> iterator() {
        return new Iterator<Long>() { // from class: org.terracotta.offheapstore.storage.allocator.LongBestFitAllocator.1
            private long current;

            {
                this.current = (LongBestFitAllocator.this.isInUse(0L) || 0 >= LongBestFitAllocator.this.top) ? 0L : LongBestFitAllocator.this.nextChunk(0L);
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.current < LongBestFitAllocator.this.top;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Iterator
            public Long next() {
                if (this.current >= LongBestFitAllocator.this.top) {
                    throw new NoSuchElementException();
                }
                long result = this.current;
                long next = LongBestFitAllocator.this.nextChunk(this.current);
                if (next >= LongBestFitAllocator.this.top) {
                    this.current = next;
                } else {
                    this.current = LongBestFitAllocator.this.isInUse(next) ? next : LongBestFitAllocator.this.nextChunk(next);
                }
                return Long.valueOf(LongBestFitAllocator.chunkToMem(result));
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }
}
