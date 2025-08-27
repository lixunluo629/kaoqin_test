package com.itextpdf.io.font.woff2;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/woff2/Woff2MemoryOut.class */
class Woff2MemoryOut implements Woff2Out {
    private byte[] buf_;
    private int buf_size_;
    private int offset_ = 0;

    public Woff2MemoryOut(byte[] buf_, int buf_size_) {
        this.buf_ = buf_;
        this.buf_size_ = buf_size_;
    }

    @Override // com.itextpdf.io.font.woff2.Woff2Out
    public void write(byte[] buf, int buff_offset, int n) {
        write(buf, buff_offset, this.offset_, n);
    }

    @Override // com.itextpdf.io.font.woff2.Woff2Out
    public void write(byte[] buf, int buff_offset, int offset, int n) {
        if (offset > this.buf_size_ || n > this.buf_size_ - offset) {
            throw new FontCompressionException(FontCompressionException.WRITE_FAILED);
        }
        System.arraycopy(buf, buff_offset, this.buf_, offset, n);
        this.offset_ = Math.max(this.offset_, offset + n);
    }

    @Override // com.itextpdf.io.font.woff2.Woff2Out
    public int size() {
        return this.offset_;
    }
}
