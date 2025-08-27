package io.netty.buffer;

import io.netty.util.ByteProcessor;
import io.netty.util.IllegalReferenceCountException;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.RecyclableArrayList;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/buffer/CompositeByteBuf.class */
public class CompositeByteBuf extends AbstractReferenceCountedByteBuf implements Iterable<ByteBuf> {
    private static final ByteBuffer EMPTY_NIO_BUFFER;
    private static final Iterator<ByteBuf> EMPTY_ITERATOR;
    private final ByteBufAllocator alloc;
    private final boolean direct;
    private final int maxNumComponents;
    private int componentCount;
    private Component[] components;
    private boolean freed;
    static final ByteWrapper<byte[]> BYTE_ARRAY_WRAPPER;
    static final ByteWrapper<ByteBuffer> BYTE_BUFFER_WRAPPER;
    private Component lastAccessed;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/buffer/CompositeByteBuf$ByteWrapper.class */
    interface ByteWrapper<T> {
        ByteBuf wrap(T t);

        boolean isEmpty(T t);
    }

    static {
        $assertionsDisabled = !CompositeByteBuf.class.desiredAssertionStatus();
        EMPTY_NIO_BUFFER = Unpooled.EMPTY_BUFFER.nioBuffer();
        EMPTY_ITERATOR = Collections.emptyList().iterator();
        BYTE_ARRAY_WRAPPER = new ByteWrapper<byte[]>() { // from class: io.netty.buffer.CompositeByteBuf.1
            @Override // io.netty.buffer.CompositeByteBuf.ByteWrapper
            public ByteBuf wrap(byte[] bytes) {
                return Unpooled.wrappedBuffer(bytes);
            }

            @Override // io.netty.buffer.CompositeByteBuf.ByteWrapper
            public boolean isEmpty(byte[] bytes) {
                return bytes.length == 0;
            }
        };
        BYTE_BUFFER_WRAPPER = new ByteWrapper<ByteBuffer>() { // from class: io.netty.buffer.CompositeByteBuf.2
            @Override // io.netty.buffer.CompositeByteBuf.ByteWrapper
            public ByteBuf wrap(ByteBuffer bytes) {
                return Unpooled.wrappedBuffer(bytes);
            }

            @Override // io.netty.buffer.CompositeByteBuf.ByteWrapper
            public boolean isEmpty(ByteBuffer bytes) {
                return !bytes.hasRemaining();
            }
        };
    }

    private CompositeByteBuf(ByteBufAllocator alloc, boolean direct, int maxNumComponents, int initSize) {
        super(Integer.MAX_VALUE);
        this.alloc = (ByteBufAllocator) ObjectUtil.checkNotNull(alloc, "alloc");
        if (maxNumComponents < 1) {
            throw new IllegalArgumentException("maxNumComponents: " + maxNumComponents + " (expected: >= 1)");
        }
        this.direct = direct;
        this.maxNumComponents = maxNumComponents;
        this.components = newCompArray(initSize, maxNumComponents);
    }

    public CompositeByteBuf(ByteBufAllocator alloc, boolean direct, int maxNumComponents) {
        this(alloc, direct, maxNumComponents, 0);
    }

    public CompositeByteBuf(ByteBufAllocator alloc, boolean direct, int maxNumComponents, ByteBuf... buffers) {
        this(alloc, direct, maxNumComponents, buffers, 0);
    }

    CompositeByteBuf(ByteBufAllocator alloc, boolean direct, int maxNumComponents, ByteBuf[] buffers, int offset) {
        this(alloc, direct, maxNumComponents, buffers.length - offset);
        addComponents0(false, 0, buffers, offset);
        consolidateIfNeeded();
        setIndex0(0, capacity());
    }

    public CompositeByteBuf(ByteBufAllocator alloc, boolean direct, int maxNumComponents, Iterable<ByteBuf> buffers) {
        this(alloc, direct, maxNumComponents, buffers instanceof Collection ? ((Collection) buffers).size() : 0);
        addComponents(false, 0, buffers);
        setIndex(0, capacity());
    }

    <T> CompositeByteBuf(ByteBufAllocator alloc, boolean direct, int maxNumComponents, ByteWrapper<T> wrapper, T[] buffers, int offset) {
        this(alloc, direct, maxNumComponents, buffers.length - offset);
        addComponents0(false, 0, wrapper, buffers, offset);
        consolidateIfNeeded();
        setIndex(0, capacity());
    }

    private static Component[] newCompArray(int initComponents, int maxNumComponents) {
        int capacityGuess = Math.min(16, maxNumComponents);
        return new Component[Math.max(initComponents, capacityGuess)];
    }

    CompositeByteBuf(ByteBufAllocator alloc) {
        super(Integer.MAX_VALUE);
        this.alloc = alloc;
        this.direct = false;
        this.maxNumComponents = 0;
        this.components = null;
    }

    public CompositeByteBuf addComponent(ByteBuf buffer) {
        return addComponent(false, buffer);
    }

    public CompositeByteBuf addComponents(ByteBuf... buffers) {
        return addComponents(false, buffers);
    }

    public CompositeByteBuf addComponents(Iterable<ByteBuf> buffers) {
        return addComponents(false, buffers);
    }

    public CompositeByteBuf addComponent(int cIndex, ByteBuf buffer) {
        return addComponent(false, cIndex, buffer);
    }

    public CompositeByteBuf addComponent(boolean increaseWriterIndex, ByteBuf buffer) {
        return addComponent(increaseWriterIndex, this.componentCount, buffer);
    }

    public CompositeByteBuf addComponents(boolean increaseWriterIndex, ByteBuf... buffers) {
        ObjectUtil.checkNotNull(buffers, "buffers");
        addComponents0(increaseWriterIndex, this.componentCount, buffers, 0);
        consolidateIfNeeded();
        return this;
    }

    public CompositeByteBuf addComponents(boolean increaseWriterIndex, Iterable<ByteBuf> buffers) {
        return addComponents(increaseWriterIndex, this.componentCount, buffers);
    }

    public CompositeByteBuf addComponent(boolean increaseWriterIndex, int cIndex, ByteBuf buffer) {
        ObjectUtil.checkNotNull(buffer, "buffer");
        addComponent0(increaseWriterIndex, cIndex, buffer);
        consolidateIfNeeded();
        return this;
    }

    private int addComponent0(boolean increaseWriterIndex, int cIndex, ByteBuf buffer) {
        if (!$assertionsDisabled && buffer == null) {
            throw new AssertionError();
        }
        boolean wasAdded = false;
        try {
            checkComponentIndex(cIndex);
            Component c = newComponent(ensureAccessible(buffer), 0);
            int readableBytes = c.length();
            if (capacity() + readableBytes < 0) {
                throw new IllegalArgumentException("Can't increase by " + readableBytes);
            }
            addComp(cIndex, c);
            wasAdded = true;
            if (readableBytes > 0 && cIndex < this.componentCount - 1) {
                updateComponentOffsets(cIndex);
            } else if (cIndex > 0) {
                c.reposition(this.components[cIndex - 1].endOffset);
            }
            if (increaseWriterIndex) {
                this.writerIndex += readableBytes;
            }
            return cIndex;
        } finally {
            if (!wasAdded) {
                buffer.release();
            }
        }
    }

    private static ByteBuf ensureAccessible(ByteBuf buf) {
        if (checkAccessible && !buf.isAccessible()) {
            throw new IllegalReferenceCountException(0);
        }
        return buf;
    }

    private Component newComponent(ByteBuf buf, int offset) {
        int srcIndex = buf.readerIndex();
        int len = buf.readableBytes();
        ByteBuf unwrapped = buf;
        int unwrappedIndex = srcIndex;
        while (true) {
            if (!(unwrapped instanceof WrappedByteBuf) && !(unwrapped instanceof SwappedByteBuf)) {
                break;
            }
            unwrapped = unwrapped.unwrap();
        }
        if (unwrapped instanceof AbstractUnpooledSlicedByteBuf) {
            unwrappedIndex += ((AbstractUnpooledSlicedByteBuf) unwrapped).idx(0);
            unwrapped = unwrapped.unwrap();
        } else if (unwrapped instanceof PooledSlicedByteBuf) {
            unwrappedIndex += ((PooledSlicedByteBuf) unwrapped).adjustment;
            unwrapped = unwrapped.unwrap();
        } else if ((unwrapped instanceof DuplicatedByteBuf) || (unwrapped instanceof PooledDuplicatedByteBuf)) {
            unwrapped = unwrapped.unwrap();
        }
        ByteBuf slice = buf.capacity() == len ? buf : null;
        return new Component(buf.order(ByteOrder.BIG_ENDIAN), srcIndex, unwrapped.order(ByteOrder.BIG_ENDIAN), unwrappedIndex, offset, len, slice);
    }

    public CompositeByteBuf addComponents(int cIndex, ByteBuf... buffers) {
        ObjectUtil.checkNotNull(buffers, "buffers");
        addComponents0(false, cIndex, buffers, 0);
        consolidateIfNeeded();
        return this;
    }

    /* JADX WARN: Finally extract failed */
    private CompositeByteBuf addComponents0(boolean increaseWriterIndex, int cIndex, ByteBuf[] buffers, int arrOffset) {
        int len = buffers.length;
        int count = len - arrOffset;
        int readableBytes = 0;
        int capacity = capacity();
        for (ByteBuf byteBuf : buffers) {
            readableBytes += byteBuf.readableBytes();
            if (capacity + readableBytes < 0) {
                throw new IllegalArgumentException("Can't increase by " + readableBytes);
            }
        }
        int ci = Integer.MAX_VALUE;
        try {
            checkComponentIndex(cIndex);
            shiftComps(cIndex, count);
            int nextOffset = cIndex > 0 ? this.components[cIndex - 1].endOffset : 0;
            ci = cIndex;
            while (arrOffset < len) {
                ByteBuf b = buffers[arrOffset];
                if (b == null) {
                    break;
                }
                Component c = newComponent(ensureAccessible(b), nextOffset);
                this.components[ci] = c;
                nextOffset = c.endOffset;
                arrOffset++;
                ci++;
            }
            if (ci < this.componentCount) {
                if (ci < cIndex + count) {
                    removeCompRange(ci, cIndex + count);
                    while (arrOffset < len) {
                        ReferenceCountUtil.safeRelease(buffers[arrOffset]);
                        arrOffset++;
                    }
                }
                updateComponentOffsets(ci);
            }
            if (increaseWriterIndex && ci > cIndex && ci <= this.componentCount) {
                this.writerIndex += this.components[ci - 1].endOffset - this.components[cIndex].offset;
            }
            return this;
        } catch (Throwable th) {
            if (ci < this.componentCount) {
                if (ci < cIndex + count) {
                    removeCompRange(ci, cIndex + count);
                    while (arrOffset < len) {
                        ReferenceCountUtil.safeRelease(buffers[arrOffset]);
                        arrOffset++;
                    }
                }
                updateComponentOffsets(ci);
            }
            if (increaseWriterIndex && ci > cIndex && ci <= this.componentCount) {
                this.writerIndex += this.components[ci - 1].endOffset - this.components[cIndex].offset;
            }
            throw th;
        }
    }

    private <T> int addComponents0(boolean increaseWriterIndex, int cIndex, ByteWrapper<T> wrapper, T[] buffers, int offset) {
        T b;
        checkComponentIndex(cIndex);
        int len = buffers.length;
        for (int i = offset; i < len && (b = buffers[i]) != null; i++) {
            if (!wrapper.isEmpty(b)) {
                cIndex = addComponent0(increaseWriterIndex, cIndex, wrapper.wrap(b)) + 1;
                int size = this.componentCount;
                if (cIndex > size) {
                    cIndex = size;
                }
            }
        }
        return cIndex;
    }

    public CompositeByteBuf addComponents(int cIndex, Iterable<ByteBuf> buffers) {
        return addComponents(false, cIndex, buffers);
    }

    /* JADX WARN: Finally extract failed */
    public CompositeByteBuf addFlattenedComponents(boolean increaseWriterIndex, ByteBuf buffer) {
        ObjectUtil.checkNotNull(buffer, "buffer");
        int ridx = buffer.readerIndex();
        int widx = buffer.writerIndex();
        if (ridx == widx) {
            buffer.release();
            return this;
        }
        if (!(buffer instanceof CompositeByteBuf)) {
            addComponent0(increaseWriterIndex, this.componentCount, buffer);
            consolidateIfNeeded();
            return this;
        }
        CompositeByteBuf from = buffer instanceof WrappedCompositeByteBuf ? (CompositeByteBuf) buffer.unwrap() : (CompositeByteBuf) buffer;
        from.checkIndex(ridx, widx - ridx);
        Component[] fromComponents = from.components;
        int compCountBefore = this.componentCount;
        int writerIndexBefore = this.writerIndex;
        try {
            int cidx = from.toComponentIndex0(ridx);
            int newOffset = capacity();
            while (true) {
                Component component = fromComponents[cidx];
                int compOffset = component.offset;
                int fromIdx = Math.max(ridx, compOffset);
                int toIdx = Math.min(widx, component.endOffset);
                int len = toIdx - fromIdx;
                if (len > 0) {
                    addComp(this.componentCount, new Component(component.srcBuf.retain(), component.srcIdx(fromIdx), component.buf, component.idx(fromIdx), newOffset, len, null));
                }
                if (widx == toIdx) {
                    break;
                }
                newOffset += len;
                cidx++;
            }
            if (increaseWriterIndex) {
                this.writerIndex = writerIndexBefore + (widx - ridx);
            }
            consolidateIfNeeded();
            buffer.release();
            buffer = null;
            if (0 != 0) {
                if (increaseWriterIndex) {
                    this.writerIndex = writerIndexBefore;
                }
                for (int cidx2 = this.componentCount - 1; cidx2 >= compCountBefore; cidx2--) {
                    this.components[cidx2].free();
                    removeComp(cidx2);
                }
            }
            return this;
        } catch (Throwable th) {
            if (buffer != null) {
                if (increaseWriterIndex) {
                    this.writerIndex = writerIndexBefore;
                }
                for (int cidx3 = this.componentCount - 1; cidx3 >= compCountBefore; cidx3--) {
                    this.components[cidx3].free();
                    removeComp(cidx3);
                }
            }
            throw th;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private CompositeByteBuf addComponents(boolean increaseIndex, int cIndex, Iterable<ByteBuf> iterable) {
        boolean zHasNext;
        ByteBuf b;
        if (iterable instanceof ByteBuf) {
            return addComponent(increaseIndex, cIndex, (ByteBuf) iterable);
        }
        ObjectUtil.checkNotNull(iterable, "buffers");
        Iterator<ByteBuf> it = iterable.iterator();
        try {
            checkComponentIndex(cIndex);
            while (it.hasNext() && (b = it.next()) != null) {
                cIndex = Math.min(addComponent0(increaseIndex, cIndex, b) + 1, this.componentCount);
            }
            while (true) {
                if (!zHasNext) {
                    consolidateIfNeeded();
                    return this;
                }
            }
        } finally {
            while (it.hasNext()) {
                ReferenceCountUtil.safeRelease(it.next());
            }
        }
    }

    private void consolidateIfNeeded() {
        int size = this.componentCount;
        if (size > this.maxNumComponents) {
            consolidate0(0, size);
        }
    }

    private void checkComponentIndex(int cIndex) {
        ensureAccessible();
        if (cIndex < 0 || cIndex > this.componentCount) {
            throw new IndexOutOfBoundsException(String.format("cIndex: %d (expected: >= 0 && <= numComponents(%d))", Integer.valueOf(cIndex), Integer.valueOf(this.componentCount)));
        }
    }

    private void checkComponentIndex(int cIndex, int numComponents) {
        ensureAccessible();
        if (cIndex < 0 || cIndex + numComponents > this.componentCount) {
            throw new IndexOutOfBoundsException(String.format("cIndex: %d, numComponents: %d (expected: cIndex >= 0 && cIndex + numComponents <= totalNumComponents(%d))", Integer.valueOf(cIndex), Integer.valueOf(numComponents), Integer.valueOf(this.componentCount)));
        }
    }

    private void updateComponentOffsets(int cIndex) {
        int size = this.componentCount;
        if (size <= cIndex) {
            return;
        }
        int nextIndex = cIndex > 0 ? this.components[cIndex - 1].endOffset : 0;
        while (cIndex < size) {
            Component c = this.components[cIndex];
            c.reposition(nextIndex);
            nextIndex = c.endOffset;
            cIndex++;
        }
    }

    public CompositeByteBuf removeComponent(int cIndex) {
        checkComponentIndex(cIndex);
        Component comp = this.components[cIndex];
        if (this.lastAccessed == comp) {
            this.lastAccessed = null;
        }
        comp.free();
        removeComp(cIndex);
        if (comp.length() > 0) {
            updateComponentOffsets(cIndex);
        }
        return this;
    }

    public CompositeByteBuf removeComponents(int cIndex, int numComponents) {
        checkComponentIndex(cIndex, numComponents);
        if (numComponents == 0) {
            return this;
        }
        int endIndex = cIndex + numComponents;
        boolean needsUpdate = false;
        for (int i = cIndex; i < endIndex; i++) {
            Component c = this.components[i];
            if (c.length() > 0) {
                needsUpdate = true;
            }
            if (this.lastAccessed == c) {
                this.lastAccessed = null;
            }
            c.free();
        }
        removeCompRange(cIndex, endIndex);
        if (needsUpdate) {
            updateComponentOffsets(cIndex);
        }
        return this;
    }

    public Iterator<ByteBuf> iterator() {
        ensureAccessible();
        return this.componentCount == 0 ? EMPTY_ITERATOR : new CompositeByteBufIterator();
    }

    @Override // io.netty.buffer.AbstractByteBuf
    protected int forEachByteAsc0(int start, int end, ByteProcessor processor) throws Exception {
        int iForEachByte;
        if (end <= start) {
            return -1;
        }
        int i = toComponentIndex0(start);
        int length = end - start;
        while (length > 0) {
            Component c = this.components[i];
            if (c.offset != c.endOffset) {
                ByteBuf s = c.buf;
                int localStart = c.idx(start);
                int localLength = Math.min(length, c.endOffset - start);
                if (s instanceof AbstractByteBuf) {
                    iForEachByte = ((AbstractByteBuf) s).forEachByteAsc0(localStart, localStart + localLength, processor);
                } else {
                    iForEachByte = s.forEachByte(localStart, localLength, processor);
                }
                int result = iForEachByte;
                if (result != -1) {
                    return result - c.adjustment;
                }
                start += localLength;
                length -= localLength;
            }
            i++;
        }
        return -1;
    }

    @Override // io.netty.buffer.AbstractByteBuf
    protected int forEachByteDesc0(int rStart, int rEnd, ByteProcessor processor) throws Exception {
        int iForEachByteDesc;
        if (rEnd > rStart) {
            return -1;
        }
        int i = toComponentIndex0(rStart);
        int length = (1 + rStart) - rEnd;
        while (length > 0) {
            Component c = this.components[i];
            if (c.offset != c.endOffset) {
                ByteBuf s = c.buf;
                int localRStart = c.idx(length + rEnd);
                int localLength = Math.min(length, localRStart);
                int localIndex = localRStart - localLength;
                if (s instanceof AbstractByteBuf) {
                    iForEachByteDesc = ((AbstractByteBuf) s).forEachByteDesc0(localRStart - 1, localIndex, processor);
                } else {
                    iForEachByteDesc = s.forEachByteDesc(localIndex, localLength, processor);
                }
                int result = iForEachByteDesc;
                if (result != -1) {
                    return result - c.adjustment;
                }
                length -= localLength;
            }
            i--;
        }
        return -1;
    }

    public List<ByteBuf> decompose(int offset, int length) {
        checkIndex(offset, length);
        if (length == 0) {
            return Collections.emptyList();
        }
        int componentId = toComponentIndex0(offset);
        Component firstC = this.components[componentId];
        ByteBuf slice = firstC.buf.slice(firstC.idx(offset), Math.min(firstC.endOffset - offset, length));
        int bytesToSlice = length - slice.readableBytes();
        if (bytesToSlice == 0) {
            return Collections.singletonList(slice);
        }
        List<ByteBuf> sliceList = new ArrayList<>(this.componentCount - componentId);
        sliceList.add(slice);
        do {
            componentId++;
            Component component = this.components[componentId];
            ByteBuf slice2 = component.buf.slice(component.idx(component.offset), Math.min(component.length(), bytesToSlice));
            bytesToSlice -= slice2.readableBytes();
            sliceList.add(slice2);
        } while (bytesToSlice > 0);
        return sliceList;
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean isDirect() {
        int size = this.componentCount;
        if (size == 0) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!this.components[i].buf.isDirect()) {
                return false;
            }
        }
        return true;
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean hasArray() {
        switch (this.componentCount) {
            case 0:
                return true;
            case 1:
                return this.components[0].buf.hasArray();
            default:
                return false;
        }
    }

    @Override // io.netty.buffer.ByteBuf
    public byte[] array() {
        switch (this.componentCount) {
            case 0:
                return EmptyArrays.EMPTY_BYTES;
            case 1:
                return this.components[0].buf.array();
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override // io.netty.buffer.ByteBuf
    public int arrayOffset() {
        switch (this.componentCount) {
            case 0:
                return 0;
            case 1:
                Component c = this.components[0];
                return c.idx(c.buf.arrayOffset());
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean hasMemoryAddress() {
        switch (this.componentCount) {
            case 0:
                return Unpooled.EMPTY_BUFFER.hasMemoryAddress();
            case 1:
                return this.components[0].buf.hasMemoryAddress();
            default:
                return false;
        }
    }

    @Override // io.netty.buffer.ByteBuf
    public long memoryAddress() {
        switch (this.componentCount) {
            case 0:
                return Unpooled.EMPTY_BUFFER.memoryAddress();
            case 1:
                Component c = this.components[0];
                return c.buf.memoryAddress() + c.adjustment;
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override // io.netty.buffer.ByteBuf
    public int capacity() {
        int size = this.componentCount;
        if (size > 0) {
            return this.components[size - 1].endOffset;
        }
        return 0;
    }

    @Override // io.netty.buffer.ByteBuf
    public CompositeByteBuf capacity(int newCapacity) {
        checkNewCapacity(newCapacity);
        int size = this.componentCount;
        int oldCapacity = capacity();
        if (newCapacity > oldCapacity) {
            int paddingLength = newCapacity - oldCapacity;
            ByteBuf padding = allocBuffer(paddingLength).setIndex(0, paddingLength);
            addComponent0(false, size, padding);
            if (this.componentCount >= this.maxNumComponents) {
                consolidateIfNeeded();
            }
        } else if (newCapacity < oldCapacity) {
            this.lastAccessed = null;
            int i = size - 1;
            int bytesToTrim = oldCapacity - newCapacity;
            while (true) {
                if (i < 0) {
                    break;
                }
                Component c = this.components[i];
                int cLength = c.length();
                if (bytesToTrim < cLength) {
                    c.endOffset -= bytesToTrim;
                    ByteBuf slice = c.slice;
                    if (slice != null) {
                        c.slice = slice.slice(0, c.length());
                    }
                } else {
                    c.free();
                    bytesToTrim -= cLength;
                    i--;
                }
            }
            removeCompRange(i + 1, size);
            if (readerIndex() > newCapacity) {
                setIndex0(newCapacity, newCapacity);
            } else if (this.writerIndex > newCapacity) {
                this.writerIndex = newCapacity;
            }
        }
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBufAllocator alloc() {
        return this.alloc;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteOrder order() {
        return ByteOrder.BIG_ENDIAN;
    }

    public int numComponents() {
        return this.componentCount;
    }

    public int maxNumComponents() {
        return this.maxNumComponents;
    }

    public int toComponentIndex(int offset) {
        checkIndex(offset);
        return toComponentIndex0(offset);
    }

    private int toComponentIndex0(int offset) {
        int size = this.componentCount;
        if (offset == 0) {
            for (int i = 0; i < size; i++) {
                if (this.components[i].endOffset > 0) {
                    return i;
                }
            }
        }
        if (size <= 2) {
            return (size == 1 || offset < this.components[0].endOffset) ? 0 : 1;
        }
        int low = 0;
        int high = size;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            Component c = this.components[mid];
            if (offset >= c.endOffset) {
                low = mid + 1;
            } else if (offset < c.offset) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        throw new Error("should not reach here");
    }

    public int toByteIndex(int cIndex) {
        checkComponentIndex(cIndex);
        return this.components[cIndex].offset;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public byte getByte(int index) {
        Component c = findComponent(index);
        return c.buf.getByte(c.idx(index));
    }

    @Override // io.netty.buffer.AbstractByteBuf
    protected byte _getByte(int index) {
        Component c = findComponent0(index);
        return c.buf.getByte(c.idx(index));
    }

    @Override // io.netty.buffer.AbstractByteBuf
    protected short _getShort(int index) {
        Component c = findComponent0(index);
        if (index + 2 <= c.endOffset) {
            return c.buf.getShort(c.idx(index));
        }
        if (order() == ByteOrder.BIG_ENDIAN) {
            return (short) (((_getByte(index) & 255) << 8) | (_getByte(index + 1) & 255));
        }
        return (short) ((_getByte(index) & 255) | ((_getByte(index + 1) & 255) << 8));
    }

    @Override // io.netty.buffer.AbstractByteBuf
    protected short _getShortLE(int index) {
        Component c = findComponent0(index);
        if (index + 2 <= c.endOffset) {
            return c.buf.getShortLE(c.idx(index));
        }
        if (order() == ByteOrder.BIG_ENDIAN) {
            return (short) ((_getByte(index) & 255) | ((_getByte(index + 1) & 255) << 8));
        }
        return (short) (((_getByte(index) & 255) << 8) | (_getByte(index + 1) & 255));
    }

    @Override // io.netty.buffer.AbstractByteBuf
    protected int _getUnsignedMedium(int index) {
        Component c = findComponent0(index);
        if (index + 3 <= c.endOffset) {
            return c.buf.getUnsignedMedium(c.idx(index));
        }
        if (order() == ByteOrder.BIG_ENDIAN) {
            return ((_getShort(index) & 65535) << 8) | (_getByte(index + 2) & 255);
        }
        return (_getShort(index) & 65535) | ((_getByte(index + 2) & 255) << 16);
    }

    @Override // io.netty.buffer.AbstractByteBuf
    protected int _getUnsignedMediumLE(int index) {
        Component c = findComponent0(index);
        if (index + 3 <= c.endOffset) {
            return c.buf.getUnsignedMediumLE(c.idx(index));
        }
        if (order() == ByteOrder.BIG_ENDIAN) {
            return (_getShortLE(index) & 65535) | ((_getByte(index + 2) & 255) << 16);
        }
        return ((_getShortLE(index) & 65535) << 8) | (_getByte(index + 2) & 255);
    }

    @Override // io.netty.buffer.AbstractByteBuf
    protected int _getInt(int index) {
        Component c = findComponent0(index);
        if (index + 4 <= c.endOffset) {
            return c.buf.getInt(c.idx(index));
        }
        if (order() == ByteOrder.BIG_ENDIAN) {
            return ((_getShort(index) & 65535) << 16) | (_getShort(index + 2) & 65535);
        }
        return (_getShort(index) & 65535) | ((_getShort(index + 2) & 65535) << 16);
    }

    @Override // io.netty.buffer.AbstractByteBuf
    protected int _getIntLE(int index) {
        Component c = findComponent0(index);
        if (index + 4 <= c.endOffset) {
            return c.buf.getIntLE(c.idx(index));
        }
        if (order() == ByteOrder.BIG_ENDIAN) {
            return (_getShortLE(index) & 65535) | ((_getShortLE(index + 2) & 65535) << 16);
        }
        return ((_getShortLE(index) & 65535) << 16) | (_getShortLE(index + 2) & 65535);
    }

    @Override // io.netty.buffer.AbstractByteBuf
    protected long _getLong(int index) {
        Component c = findComponent0(index);
        if (index + 8 <= c.endOffset) {
            return c.buf.getLong(c.idx(index));
        }
        if (order() == ByteOrder.BIG_ENDIAN) {
            return ((_getInt(index) & 4294967295L) << 32) | (_getInt(index + 4) & 4294967295L);
        }
        return (_getInt(index) & 4294967295L) | ((_getInt(index + 4) & 4294967295L) << 32);
    }

    @Override // io.netty.buffer.AbstractByteBuf
    protected long _getLongLE(int index) {
        Component c = findComponent0(index);
        if (index + 8 <= c.endOffset) {
            return c.buf.getLongLE(c.idx(index));
        }
        if (order() == ByteOrder.BIG_ENDIAN) {
            return (_getIntLE(index) & 4294967295L) | ((_getIntLE(index + 4) & 4294967295L) << 32);
        }
        return ((_getIntLE(index) & 4294967295L) << 32) | (_getIntLE(index + 4) & 4294967295L);
    }

    @Override // io.netty.buffer.ByteBuf
    public CompositeByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
        checkDstIndex(index, length, dstIndex, dst.length);
        if (length == 0) {
            return this;
        }
        int i = toComponentIndex0(index);
        while (length > 0) {
            Component c = this.components[i];
            int localLength = Math.min(length, c.endOffset - index);
            c.buf.getBytes(c.idx(index), dst, dstIndex, localLength);
            index += localLength;
            dstIndex += localLength;
            length -= localLength;
            i++;
        }
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public CompositeByteBuf getBytes(int index, ByteBuffer dst) {
        int limit = dst.limit();
        int length = dst.remaining();
        checkIndex(index, length);
        if (length == 0) {
            return this;
        }
        int i = toComponentIndex0(index);
        while (length > 0) {
            try {
                Component c = this.components[i];
                int localLength = Math.min(length, c.endOffset - index);
                dst.limit(dst.position() + localLength);
                c.buf.getBytes(c.idx(index), dst);
                index += localLength;
                length -= localLength;
                i++;
            } finally {
                dst.limit(limit);
            }
        }
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public CompositeByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
        checkDstIndex(index, length, dstIndex, dst.capacity());
        if (length == 0) {
            return this;
        }
        int i = toComponentIndex0(index);
        while (length > 0) {
            Component c = this.components[i];
            int localLength = Math.min(length, c.endOffset - index);
            c.buf.getBytes(c.idx(index), dst, dstIndex, localLength);
            index += localLength;
            dstIndex += localLength;
            length -= localLength;
            i++;
        }
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public int getBytes(int index, GatheringByteChannel out, int length) throws IOException {
        int count = nioBufferCount();
        if (count == 1) {
            return out.write(internalNioBuffer(index, length));
        }
        long writtenBytes = out.write(nioBuffers(index, length));
        if (writtenBytes > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int) writtenBytes;
    }

    @Override // io.netty.buffer.ByteBuf
    public int getBytes(int index, FileChannel out, long position, int length) throws IOException {
        int count = nioBufferCount();
        if (count == 1) {
            return out.write(internalNioBuffer(index, length), position);
        }
        long writtenBytes = 0;
        for (ByteBuffer buf : nioBuffers(index, length)) {
            writtenBytes += out.write(buf, position + writtenBytes);
        }
        if (writtenBytes > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int) writtenBytes;
    }

    @Override // io.netty.buffer.ByteBuf
    public CompositeByteBuf getBytes(int index, OutputStream out, int length) throws IOException {
        checkIndex(index, length);
        if (length == 0) {
            return this;
        }
        int i = toComponentIndex0(index);
        while (length > 0) {
            Component c = this.components[i];
            int localLength = Math.min(length, c.endOffset - index);
            c.buf.getBytes(c.idx(index), out, localLength);
            index += localLength;
            length -= localLength;
            i++;
        }
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf setByte(int index, int value) {
        Component c = findComponent(index);
        c.buf.setByte(c.idx(index), value);
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf
    protected void _setByte(int index, int value) {
        Component c = findComponent0(index);
        c.buf.setByte(c.idx(index), value);
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf setShort(int index, int value) {
        checkIndex(index, 2);
        _setShort(index, value);
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf
    protected void _setShort(int index, int value) {
        Component c = findComponent0(index);
        if (index + 2 <= c.endOffset) {
            c.buf.setShort(c.idx(index), value);
        } else if (order() == ByteOrder.BIG_ENDIAN) {
            _setByte(index, (byte) (value >>> 8));
            _setByte(index + 1, (byte) value);
        } else {
            _setByte(index, (byte) value);
            _setByte(index + 1, (byte) (value >>> 8));
        }
    }

    @Override // io.netty.buffer.AbstractByteBuf
    protected void _setShortLE(int index, int value) {
        Component c = findComponent0(index);
        if (index + 2 <= c.endOffset) {
            c.buf.setShortLE(c.idx(index), value);
        } else if (order() == ByteOrder.BIG_ENDIAN) {
            _setByte(index, (byte) value);
            _setByte(index + 1, (byte) (value >>> 8));
        } else {
            _setByte(index, (byte) (value >>> 8));
            _setByte(index + 1, (byte) value);
        }
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf setMedium(int index, int value) {
        checkIndex(index, 3);
        _setMedium(index, value);
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf
    protected void _setMedium(int index, int value) {
        Component c = findComponent0(index);
        if (index + 3 <= c.endOffset) {
            c.buf.setMedium(c.idx(index), value);
        } else if (order() == ByteOrder.BIG_ENDIAN) {
            _setShort(index, (short) (value >> 8));
            _setByte(index + 2, (byte) value);
        } else {
            _setShort(index, (short) value);
            _setByte(index + 2, (byte) (value >>> 16));
        }
    }

    @Override // io.netty.buffer.AbstractByteBuf
    protected void _setMediumLE(int index, int value) {
        Component c = findComponent0(index);
        if (index + 3 <= c.endOffset) {
            c.buf.setMediumLE(c.idx(index), value);
        } else if (order() == ByteOrder.BIG_ENDIAN) {
            _setShortLE(index, (short) value);
            _setByte(index + 2, (byte) (value >>> 16));
        } else {
            _setShortLE(index, (short) (value >> 8));
            _setByte(index + 2, (byte) value);
        }
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf setInt(int index, int value) {
        checkIndex(index, 4);
        _setInt(index, value);
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf
    protected void _setInt(int index, int value) {
        Component c = findComponent0(index);
        if (index + 4 <= c.endOffset) {
            c.buf.setInt(c.idx(index), value);
        } else if (order() == ByteOrder.BIG_ENDIAN) {
            _setShort(index, (short) (value >>> 16));
            _setShort(index + 2, (short) value);
        } else {
            _setShort(index, (short) value);
            _setShort(index + 2, (short) (value >>> 16));
        }
    }

    @Override // io.netty.buffer.AbstractByteBuf
    protected void _setIntLE(int index, int value) {
        Component c = findComponent0(index);
        if (index + 4 <= c.endOffset) {
            c.buf.setIntLE(c.idx(index), value);
        } else if (order() == ByteOrder.BIG_ENDIAN) {
            _setShortLE(index, (short) value);
            _setShortLE(index + 2, (short) (value >>> 16));
        } else {
            _setShortLE(index, (short) (value >>> 16));
            _setShortLE(index + 2, (short) value);
        }
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf setLong(int index, long value) {
        checkIndex(index, 8);
        _setLong(index, value);
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf
    protected void _setLong(int index, long value) {
        Component c = findComponent0(index);
        if (index + 8 <= c.endOffset) {
            c.buf.setLong(c.idx(index), value);
        } else if (order() == ByteOrder.BIG_ENDIAN) {
            _setInt(index, (int) (value >>> 32));
            _setInt(index + 4, (int) value);
        } else {
            _setInt(index, (int) value);
            _setInt(index + 4, (int) (value >>> 32));
        }
    }

    @Override // io.netty.buffer.AbstractByteBuf
    protected void _setLongLE(int index, long value) {
        Component c = findComponent0(index);
        if (index + 8 <= c.endOffset) {
            c.buf.setLongLE(c.idx(index), value);
        } else if (order() == ByteOrder.BIG_ENDIAN) {
            _setIntLE(index, (int) value);
            _setIntLE(index + 4, (int) (value >>> 32));
        } else {
            _setIntLE(index, (int) (value >>> 32));
            _setIntLE(index + 4, (int) value);
        }
    }

    @Override // io.netty.buffer.ByteBuf
    public CompositeByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
        checkSrcIndex(index, length, srcIndex, src.length);
        if (length == 0) {
            return this;
        }
        int i = toComponentIndex0(index);
        while (length > 0) {
            Component c = this.components[i];
            int localLength = Math.min(length, c.endOffset - index);
            c.buf.setBytes(c.idx(index), src, srcIndex, localLength);
            index += localLength;
            srcIndex += localLength;
            length -= localLength;
            i++;
        }
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public CompositeByteBuf setBytes(int index, ByteBuffer src) {
        int limit = src.limit();
        int length = src.remaining();
        checkIndex(index, length);
        if (length == 0) {
            return this;
        }
        int i = toComponentIndex0(index);
        while (length > 0) {
            try {
                Component c = this.components[i];
                int localLength = Math.min(length, c.endOffset - index);
                src.limit(src.position() + localLength);
                c.buf.setBytes(c.idx(index), src);
                index += localLength;
                length -= localLength;
                i++;
            } finally {
                src.limit(limit);
            }
        }
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public CompositeByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
        checkSrcIndex(index, length, srcIndex, src.capacity());
        if (length == 0) {
            return this;
        }
        int i = toComponentIndex0(index);
        while (length > 0) {
            Component c = this.components[i];
            int localLength = Math.min(length, c.endOffset - index);
            c.buf.setBytes(c.idx(index), src, srcIndex, localLength);
            index += localLength;
            srcIndex += localLength;
            length -= localLength;
            i++;
        }
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public int setBytes(int index, InputStream in, int length) throws IOException {
        checkIndex(index, length);
        if (length == 0) {
            return in.read(EmptyArrays.EMPTY_BYTES);
        }
        int i = toComponentIndex0(index);
        int readBytes = 0;
        while (true) {
            Component c = this.components[i];
            int localLength = Math.min(length, c.endOffset - index);
            if (localLength == 0) {
                i++;
            } else {
                int localReadBytes = c.buf.setBytes(c.idx(index), in, localLength);
                if (localReadBytes < 0) {
                    if (readBytes == 0) {
                        return -1;
                    }
                } else {
                    index += localReadBytes;
                    length -= localReadBytes;
                    readBytes += localReadBytes;
                    if (localReadBytes == localLength) {
                        i++;
                    }
                }
            }
            if (length <= 0) {
                break;
            }
        }
        return readBytes;
    }

    @Override // io.netty.buffer.ByteBuf
    public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException {
        checkIndex(index, length);
        if (length == 0) {
            return in.read(EMPTY_NIO_BUFFER);
        }
        int i = toComponentIndex0(index);
        int readBytes = 0;
        while (true) {
            Component c = this.components[i];
            int localLength = Math.min(length, c.endOffset - index);
            if (localLength == 0) {
                i++;
            } else {
                int localReadBytes = c.buf.setBytes(c.idx(index), in, localLength);
                if (localReadBytes == 0) {
                    break;
                }
                if (localReadBytes < 0) {
                    if (readBytes == 0) {
                        return -1;
                    }
                } else {
                    index += localReadBytes;
                    length -= localReadBytes;
                    readBytes += localReadBytes;
                    if (localReadBytes == localLength) {
                        i++;
                    }
                }
            }
            if (length <= 0) {
                break;
            }
        }
        return readBytes;
    }

    @Override // io.netty.buffer.ByteBuf
    public int setBytes(int index, FileChannel in, long position, int length) throws IOException {
        checkIndex(index, length);
        if (length == 0) {
            return in.read(EMPTY_NIO_BUFFER, position);
        }
        int i = toComponentIndex0(index);
        int readBytes = 0;
        while (true) {
            Component c = this.components[i];
            int localLength = Math.min(length, c.endOffset - index);
            if (localLength == 0) {
                i++;
            } else {
                int localReadBytes = c.buf.setBytes(c.idx(index), in, position + readBytes, localLength);
                if (localReadBytes == 0) {
                    break;
                }
                if (localReadBytes < 0) {
                    if (readBytes == 0) {
                        return -1;
                    }
                } else {
                    index += localReadBytes;
                    length -= localReadBytes;
                    readBytes += localReadBytes;
                    if (localReadBytes == localLength) {
                        i++;
                    }
                }
            }
            if (length <= 0) {
                break;
            }
        }
        return readBytes;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf copy(int index, int length) {
        checkIndex(index, length);
        ByteBuf dst = allocBuffer(length);
        if (length != 0) {
            copyTo(index, length, toComponentIndex0(index), dst);
        }
        return dst;
    }

    private void copyTo(int index, int length, int componentId, ByteBuf dst) {
        int dstIndex = 0;
        int i = componentId;
        while (length > 0) {
            Component c = this.components[i];
            int localLength = Math.min(length, c.endOffset - index);
            c.buf.getBytes(c.idx(index), dst, dstIndex, localLength);
            index += localLength;
            dstIndex += localLength;
            length -= localLength;
            i++;
        }
        dst.writerIndex(dst.capacity());
    }

    public ByteBuf component(int cIndex) {
        checkComponentIndex(cIndex);
        return this.components[cIndex].duplicate();
    }

    public ByteBuf componentAtOffset(int offset) {
        return findComponent(offset).duplicate();
    }

    public ByteBuf internalComponent(int cIndex) {
        checkComponentIndex(cIndex);
        return this.components[cIndex].slice();
    }

    public ByteBuf internalComponentAtOffset(int offset) {
        return findComponent(offset).slice();
    }

    private Component findComponent(int offset) {
        Component la = this.lastAccessed;
        if (la != null && offset >= la.offset && offset < la.endOffset) {
            ensureAccessible();
            return la;
        }
        checkIndex(offset);
        return findIt(offset);
    }

    private Component findComponent0(int offset) {
        Component la = this.lastAccessed;
        if (la != null && offset >= la.offset && offset < la.endOffset) {
            return la;
        }
        return findIt(offset);
    }

    private Component findIt(int offset) {
        int low = 0;
        int high = this.componentCount;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            Component c = this.components[mid];
            if (offset >= c.endOffset) {
                low = mid + 1;
            } else if (offset < c.offset) {
                high = mid - 1;
            } else {
                this.lastAccessed = c;
                return c;
            }
        }
        throw new Error("should not reach here");
    }

    @Override // io.netty.buffer.ByteBuf
    public int nioBufferCount() {
        int size = this.componentCount;
        switch (size) {
            case 0:
                return 1;
            case 1:
                return this.components[0].buf.nioBufferCount();
            default:
                int count = 0;
                for (int i = 0; i < size; i++) {
                    count += this.components[i].buf.nioBufferCount();
                }
                return count;
        }
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuffer internalNioBuffer(int index, int length) {
        switch (this.componentCount) {
            case 0:
                return EMPTY_NIO_BUFFER;
            case 1:
                return this.components[0].internalNioBuffer(index, length);
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuffer nioBuffer(int index, int length) {
        checkIndex(index, length);
        switch (this.componentCount) {
            case 0:
                return EMPTY_NIO_BUFFER;
            case 1:
                Component c = this.components[0];
                ByteBuf buf = c.buf;
                if (buf.nioBufferCount() == 1) {
                    return buf.nioBuffer(c.idx(index), length);
                }
                break;
        }
        ByteBuffer[] buffers = nioBuffers(index, length);
        if (buffers.length == 1) {
            return buffers[0];
        }
        ByteBuffer merged = ByteBuffer.allocate(length).order(order());
        for (ByteBuffer byteBuffer : buffers) {
            merged.put(byteBuffer);
        }
        merged.flip();
        return merged;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuffer[] nioBuffers(int index, int length) {
        checkIndex(index, length);
        if (length == 0) {
            return new ByteBuffer[]{EMPTY_NIO_BUFFER};
        }
        RecyclableArrayList buffers = RecyclableArrayList.newInstance(this.componentCount);
        try {
            int i = toComponentIndex0(index);
            while (length > 0) {
                Component c = this.components[i];
                ByteBuf s = c.buf;
                int localLength = Math.min(length, c.endOffset - index);
                switch (s.nioBufferCount()) {
                    case 0:
                        throw new UnsupportedOperationException();
                    case 1:
                        buffers.add(s.nioBuffer(c.idx(index), localLength));
                        break;
                    default:
                        Collections.addAll(buffers, s.nioBuffers(c.idx(index), localLength));
                        break;
                }
                index += localLength;
                length -= localLength;
                i++;
            }
            ByteBuffer[] byteBufferArr = (ByteBuffer[]) buffers.toArray(new ByteBuffer[0]);
            buffers.recycle();
            return byteBufferArr;
        } catch (Throwable th) {
            buffers.recycle();
            throw th;
        }
    }

    public CompositeByteBuf consolidate() {
        ensureAccessible();
        consolidate0(0, this.componentCount);
        return this;
    }

    public CompositeByteBuf consolidate(int cIndex, int numComponents) {
        checkComponentIndex(cIndex, numComponents);
        consolidate0(cIndex, numComponents);
        return this;
    }

    private void consolidate0(int cIndex, int numComponents) {
        if (numComponents <= 1) {
            return;
        }
        int endCIndex = cIndex + numComponents;
        int startOffset = cIndex != 0 ? this.components[cIndex].offset : 0;
        int capacity = this.components[endCIndex - 1].endOffset - startOffset;
        ByteBuf consolidated = allocBuffer(capacity);
        for (int i = cIndex; i < endCIndex; i++) {
            this.components[i].transferTo(consolidated);
        }
        this.lastAccessed = null;
        removeCompRange(cIndex + 1, endCIndex);
        this.components[cIndex] = newComponent(consolidated, 0);
        if (cIndex != 0 || numComponents != this.componentCount) {
            updateComponentOffsets(cIndex);
        }
    }

    public CompositeByteBuf discardReadComponents() {
        ensureAccessible();
        int readerIndex = readerIndex();
        if (readerIndex == 0) {
            return this;
        }
        int writerIndex = writerIndex();
        if (readerIndex == writerIndex && writerIndex == capacity()) {
            int size = this.componentCount;
            for (int i = 0; i < size; i++) {
                this.components[i].free();
            }
            this.lastAccessed = null;
            clearComps();
            setIndex(0, 0);
            adjustMarkers(readerIndex);
            return this;
        }
        int firstComponentId = 0;
        Component c = null;
        int size2 = this.componentCount;
        while (firstComponentId < size2) {
            c = this.components[firstComponentId];
            if (c.endOffset > readerIndex) {
                break;
            }
            c.free();
            firstComponentId++;
        }
        if (firstComponentId == 0) {
            return this;
        }
        Component la = this.lastAccessed;
        if (la != null && la.endOffset <= readerIndex) {
            this.lastAccessed = null;
        }
        removeCompRange(0, firstComponentId);
        int offset = c.offset;
        updateComponentOffsets(0);
        setIndex(readerIndex - offset, writerIndex - offset);
        adjustMarkers(offset);
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf discardReadBytes() {
        ensureAccessible();
        int readerIndex = readerIndex();
        if (readerIndex == 0) {
            return this;
        }
        int writerIndex = writerIndex();
        if (readerIndex == writerIndex && writerIndex == capacity()) {
            int size = this.componentCount;
            for (int i = 0; i < size; i++) {
                this.components[i].free();
            }
            this.lastAccessed = null;
            clearComps();
            setIndex(0, 0);
            adjustMarkers(readerIndex);
            return this;
        }
        int firstComponentId = 0;
        Component c = null;
        int size2 = this.componentCount;
        while (firstComponentId < size2) {
            c = this.components[firstComponentId];
            if (c.endOffset > readerIndex) {
                break;
            }
            c.free();
            firstComponentId++;
        }
        int trimmedBytes = readerIndex - c.offset;
        c.offset = 0;
        c.endOffset -= readerIndex;
        c.srcAdjustment += readerIndex;
        c.adjustment += readerIndex;
        ByteBuf slice = c.slice;
        if (slice != null) {
            c.slice = slice.slice(trimmedBytes, c.length());
        }
        Component la = this.lastAccessed;
        if (la != null && la.endOffset <= readerIndex) {
            this.lastAccessed = null;
        }
        removeCompRange(0, firstComponentId);
        updateComponentOffsets(0);
        setIndex(0, writerIndex - readerIndex);
        adjustMarkers(readerIndex);
        return this;
    }

    private ByteBuf allocBuffer(int capacity) {
        return this.direct ? alloc().directBuffer(capacity) : alloc().heapBuffer(capacity);
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public String toString() {
        String result = super.toString();
        return result.substring(0, result.length() - 1) + ", components=" + this.componentCount + ')';
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/buffer/CompositeByteBuf$Component.class */
    private static final class Component {
        final ByteBuf srcBuf;
        final ByteBuf buf;
        int srcAdjustment;
        int adjustment;
        int offset;
        int endOffset;
        private ByteBuf slice;

        Component(ByteBuf srcBuf, int srcOffset, ByteBuf buf, int bufOffset, int offset, int len, ByteBuf slice) {
            this.srcBuf = srcBuf;
            this.srcAdjustment = srcOffset - offset;
            this.buf = buf;
            this.adjustment = bufOffset - offset;
            this.offset = offset;
            this.endOffset = offset + len;
            this.slice = slice;
        }

        int srcIdx(int index) {
            return index + this.srcAdjustment;
        }

        int idx(int index) {
            return index + this.adjustment;
        }

        int length() {
            return this.endOffset - this.offset;
        }

        void reposition(int newOffset) {
            int move = newOffset - this.offset;
            this.endOffset += move;
            this.srcAdjustment -= move;
            this.adjustment -= move;
            this.offset = newOffset;
        }

        void transferTo(ByteBuf dst) {
            dst.writeBytes(this.buf, idx(this.offset), length());
            free();
        }

        ByteBuf slice() {
            ByteBuf s = this.slice;
            if (s == null) {
                ByteBuf byteBufSlice = this.srcBuf.slice(srcIdx(this.offset), length());
                s = byteBufSlice;
                this.slice = byteBufSlice;
            }
            return s;
        }

        ByteBuf duplicate() {
            return this.srcBuf.duplicate();
        }

        ByteBuffer internalNioBuffer(int index, int length) {
            return this.srcBuf.internalNioBuffer(srcIdx(index), length);
        }

        void free() {
            this.slice = null;
            this.srcBuf.release();
        }
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf readerIndex(int readerIndex) {
        super.readerIndex(readerIndex);
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf writerIndex(int writerIndex) {
        super.writerIndex(writerIndex);
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf setIndex(int readerIndex, int writerIndex) {
        super.setIndex(readerIndex, writerIndex);
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf clear() {
        super.clear();
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf markReaderIndex() {
        super.markReaderIndex();
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf resetReaderIndex() {
        super.resetReaderIndex();
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf markWriterIndex() {
        super.markWriterIndex();
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf resetWriterIndex() {
        super.resetWriterIndex();
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf ensureWritable(int minWritableBytes) {
        super.ensureWritable(minWritableBytes);
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf getBytes(int index, ByteBuf dst) {
        return getBytes(index, dst, dst.writableBytes());
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf getBytes(int index, ByteBuf dst, int length) {
        getBytes(index, dst, dst.writerIndex(), length);
        dst.writerIndex(dst.writerIndex() + length);
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf getBytes(int index, byte[] dst) {
        return getBytes(index, dst, 0, dst.length);
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf setBoolean(int index, boolean value) {
        return setByte(index, value ? 1 : 0);
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf setChar(int index, int value) {
        return setShort(index, value);
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf setFloat(int index, float value) {
        return setInt(index, Float.floatToRawIntBits(value));
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf setDouble(int index, double value) {
        return setLong(index, Double.doubleToRawLongBits(value));
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf setBytes(int index, ByteBuf src) {
        super.setBytes(index, src, src.readableBytes());
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf setBytes(int index, ByteBuf src, int length) {
        super.setBytes(index, src, length);
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf setBytes(int index, byte[] src) {
        return setBytes(index, src, 0, src.length);
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf setZero(int index, int length) {
        super.setZero(index, length);
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf readBytes(ByteBuf dst) {
        super.readBytes(dst, dst.writableBytes());
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf readBytes(ByteBuf dst, int length) {
        super.readBytes(dst, length);
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf readBytes(ByteBuf dst, int dstIndex, int length) {
        super.readBytes(dst, dstIndex, length);
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf readBytes(byte[] dst) {
        super.readBytes(dst, 0, dst.length);
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf readBytes(byte[] dst, int dstIndex, int length) {
        super.readBytes(dst, dstIndex, length);
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf readBytes(ByteBuffer dst) {
        super.readBytes(dst);
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf readBytes(OutputStream out, int length) throws IOException {
        super.readBytes(out, length);
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf skipBytes(int length) {
        super.skipBytes(length);
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf writeBoolean(boolean value) {
        writeByte(value ? 1 : 0);
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf writeByte(int value) {
        ensureWritable0(1);
        int i = this.writerIndex;
        this.writerIndex = i + 1;
        _setByte(i, value);
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf writeShort(int value) {
        super.writeShort(value);
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf writeMedium(int value) {
        super.writeMedium(value);
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf writeInt(int value) {
        super.writeInt(value);
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf writeLong(long value) {
        super.writeLong(value);
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf writeChar(int value) {
        super.writeShort(value);
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf writeFloat(float value) {
        super.writeInt(Float.floatToRawIntBits(value));
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf writeDouble(double value) {
        super.writeLong(Double.doubleToRawLongBits(value));
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf writeBytes(ByteBuf src) {
        super.writeBytes(src, src.readableBytes());
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf writeBytes(ByteBuf src, int length) {
        super.writeBytes(src, length);
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf writeBytes(ByteBuf src, int srcIndex, int length) {
        super.writeBytes(src, srcIndex, length);
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf writeBytes(byte[] src) {
        super.writeBytes(src, 0, src.length);
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf writeBytes(byte[] src, int srcIndex, int length) {
        super.writeBytes(src, srcIndex, length);
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf writeBytes(ByteBuffer src) {
        super.writeBytes(src);
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf writeZero(int length) {
        super.writeZero(length);
        return this;
    }

    @Override // io.netty.buffer.AbstractReferenceCountedByteBuf, io.netty.buffer.ByteBuf, io.netty.util.ReferenceCounted
    public CompositeByteBuf retain(int increment) {
        super.retain(increment);
        return this;
    }

    @Override // io.netty.buffer.AbstractReferenceCountedByteBuf, io.netty.buffer.ByteBuf, io.netty.util.ReferenceCounted
    public CompositeByteBuf retain() {
        super.retain();
        return this;
    }

    @Override // io.netty.buffer.AbstractReferenceCountedByteBuf, io.netty.buffer.ByteBuf, io.netty.util.ReferenceCounted
    public CompositeByteBuf touch() {
        return this;
    }

    @Override // io.netty.buffer.AbstractReferenceCountedByteBuf, io.netty.buffer.ByteBuf, io.netty.util.ReferenceCounted
    public CompositeByteBuf touch(Object hint) {
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public ByteBuffer[] nioBuffers() {
        return nioBuffers(readerIndex(), readableBytes());
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public CompositeByteBuf discardSomeReadBytes() {
        return discardReadComponents();
    }

    @Override // io.netty.buffer.AbstractReferenceCountedByteBuf
    protected void deallocate() {
        if (this.freed) {
            return;
        }
        this.freed = true;
        int size = this.componentCount;
        for (int i = 0; i < size; i++) {
            this.components[i].free();
        }
    }

    @Override // io.netty.buffer.AbstractReferenceCountedByteBuf, io.netty.buffer.ByteBuf
    boolean isAccessible() {
        return !this.freed;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf unwrap() {
        return null;
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/buffer/CompositeByteBuf$CompositeByteBufIterator.class */
    private final class CompositeByteBufIterator implements Iterator<ByteBuf> {
        private final int size;
        private int index;

        private CompositeByteBufIterator() {
            this.size = CompositeByteBuf.this.numComponents();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.size > this.index;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public ByteBuf next() {
            if (this.size != CompositeByteBuf.this.numComponents()) {
                throw new ConcurrentModificationException();
            }
            if (hasNext()) {
                try {
                    Component[] componentArr = CompositeByteBuf.this.components;
                    int i = this.index;
                    this.index = i + 1;
                    return componentArr[i].slice();
                } catch (IndexOutOfBoundsException e) {
                    throw new ConcurrentModificationException();
                }
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Read-Only");
        }
    }

    private void clearComps() {
        removeCompRange(0, this.componentCount);
    }

    private void removeComp(int i) {
        removeCompRange(i, i + 1);
    }

    private void removeCompRange(int from, int to) {
        if (from >= to) {
            return;
        }
        int size = this.componentCount;
        if (!$assertionsDisabled && (from < 0 || to > size)) {
            throw new AssertionError();
        }
        if (to < size) {
            System.arraycopy(this.components, to, this.components, from, size - to);
        }
        int newSize = (size - to) + from;
        for (int i = newSize; i < size; i++) {
            this.components[i] = null;
        }
        this.componentCount = newSize;
    }

    private void addComp(int i, Component c) {
        shiftComps(i, 1);
        this.components[i] = c;
    }

    private void shiftComps(int i, int count) {
        Component[] newArr;
        int size = this.componentCount;
        int newSize = size + count;
        if (!$assertionsDisabled && (i < 0 || i > size || count <= 0)) {
            throw new AssertionError();
        }
        if (newSize > this.components.length) {
            int newArrSize = Math.max(size + (size >> 1), newSize);
            if (i == size) {
                newArr = (Component[]) Arrays.copyOf(this.components, newArrSize, Component[].class);
            } else {
                newArr = new Component[newArrSize];
                if (i > 0) {
                    System.arraycopy(this.components, 0, newArr, 0, i);
                }
                if (i < size) {
                    System.arraycopy(this.components, i, newArr, i + count, size - i);
                }
            }
            this.components = newArr;
        } else if (i < size) {
            System.arraycopy(this.components, i, this.components, i + count, size - i);
        }
        this.componentCount = newSize;
    }
}
