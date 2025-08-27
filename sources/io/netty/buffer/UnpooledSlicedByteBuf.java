package io.netty.buffer;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/buffer/UnpooledSlicedByteBuf.class */
class UnpooledSlicedByteBuf extends AbstractUnpooledSlicedByteBuf {
    UnpooledSlicedByteBuf(AbstractByteBuf buffer, int index, int length) {
        super(buffer, index, length);
    }

    @Override // io.netty.buffer.ByteBuf
    public int capacity() {
        return maxCapacity();
    }

    @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.ByteBuf
    public AbstractByteBuf unwrap() {
        return (AbstractByteBuf) super.unwrap();
    }

    @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.AbstractByteBuf
    protected byte _getByte(int index) {
        return unwrap()._getByte(idx(index));
    }

    @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.AbstractByteBuf
    protected short _getShort(int index) {
        return unwrap()._getShort(idx(index));
    }

    @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.AbstractByteBuf
    protected short _getShortLE(int index) {
        return unwrap()._getShortLE(idx(index));
    }

    @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.AbstractByteBuf
    protected int _getUnsignedMedium(int index) {
        return unwrap()._getUnsignedMedium(idx(index));
    }

    @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.AbstractByteBuf
    protected int _getUnsignedMediumLE(int index) {
        return unwrap()._getUnsignedMediumLE(idx(index));
    }

    @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.AbstractByteBuf
    protected int _getInt(int index) {
        return unwrap()._getInt(idx(index));
    }

    @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.AbstractByteBuf
    protected int _getIntLE(int index) {
        return unwrap()._getIntLE(idx(index));
    }

    @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.AbstractByteBuf
    protected long _getLong(int index) {
        return unwrap()._getLong(idx(index));
    }

    @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.AbstractByteBuf
    protected long _getLongLE(int index) {
        return unwrap()._getLongLE(idx(index));
    }

    @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.AbstractByteBuf
    protected void _setByte(int index, int value) {
        unwrap()._setByte(idx(index), value);
    }

    @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.AbstractByteBuf
    protected void _setShort(int index, int value) {
        unwrap()._setShort(idx(index), value);
    }

    @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.AbstractByteBuf
    protected void _setShortLE(int index, int value) {
        unwrap()._setShortLE(idx(index), value);
    }

    @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.AbstractByteBuf
    protected void _setMedium(int index, int value) {
        unwrap()._setMedium(idx(index), value);
    }

    @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.AbstractByteBuf
    protected void _setMediumLE(int index, int value) {
        unwrap()._setMediumLE(idx(index), value);
    }

    @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.AbstractByteBuf
    protected void _setInt(int index, int value) {
        unwrap()._setInt(idx(index), value);
    }

    @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.AbstractByteBuf
    protected void _setIntLE(int index, int value) {
        unwrap()._setIntLE(idx(index), value);
    }

    @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.AbstractByteBuf
    protected void _setLong(int index, long value) {
        unwrap()._setLong(idx(index), value);
    }

    @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.AbstractByteBuf
    protected void _setLongLE(int index, long value) {
        unwrap()._setLongLE(idx(index), value);
    }
}
