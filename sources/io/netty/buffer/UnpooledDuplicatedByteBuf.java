package io.netty.buffer;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/buffer/UnpooledDuplicatedByteBuf.class */
class UnpooledDuplicatedByteBuf extends DuplicatedByteBuf {
    UnpooledDuplicatedByteBuf(AbstractByteBuf buffer) {
        super(buffer);
    }

    @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.ByteBuf
    public AbstractByteBuf unwrap() {
        return (AbstractByteBuf) super.unwrap();
    }

    @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.AbstractByteBuf
    protected byte _getByte(int index) {
        return unwrap()._getByte(index);
    }

    @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.AbstractByteBuf
    protected short _getShort(int index) {
        return unwrap()._getShort(index);
    }

    @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.AbstractByteBuf
    protected short _getShortLE(int index) {
        return unwrap()._getShortLE(index);
    }

    @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.AbstractByteBuf
    protected int _getUnsignedMedium(int index) {
        return unwrap()._getUnsignedMedium(index);
    }

    @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.AbstractByteBuf
    protected int _getUnsignedMediumLE(int index) {
        return unwrap()._getUnsignedMediumLE(index);
    }

    @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.AbstractByteBuf
    protected int _getInt(int index) {
        return unwrap()._getInt(index);
    }

    @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.AbstractByteBuf
    protected int _getIntLE(int index) {
        return unwrap()._getIntLE(index);
    }

    @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.AbstractByteBuf
    protected long _getLong(int index) {
        return unwrap()._getLong(index);
    }

    @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.AbstractByteBuf
    protected long _getLongLE(int index) {
        return unwrap()._getLongLE(index);
    }

    @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.AbstractByteBuf
    protected void _setByte(int index, int value) {
        unwrap()._setByte(index, value);
    }

    @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.AbstractByteBuf
    protected void _setShort(int index, int value) {
        unwrap()._setShort(index, value);
    }

    @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.AbstractByteBuf
    protected void _setShortLE(int index, int value) {
        unwrap()._setShortLE(index, value);
    }

    @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.AbstractByteBuf
    protected void _setMedium(int index, int value) {
        unwrap()._setMedium(index, value);
    }

    @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.AbstractByteBuf
    protected void _setMediumLE(int index, int value) {
        unwrap()._setMediumLE(index, value);
    }

    @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.AbstractByteBuf
    protected void _setInt(int index, int value) {
        unwrap()._setInt(index, value);
    }

    @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.AbstractByteBuf
    protected void _setIntLE(int index, int value) {
        unwrap()._setIntLE(index, value);
    }

    @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.AbstractByteBuf
    protected void _setLong(int index, long value) {
        unwrap()._setLong(index, value);
    }

    @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.AbstractByteBuf
    protected void _setLongLE(int index, long value) {
        unwrap()._setLongLE(index, value);
    }
}
