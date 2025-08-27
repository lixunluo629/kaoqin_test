package org.aspectj.apache.bcel.util;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/util/ByteSequence.class */
public final class ByteSequence extends DataInputStream {
    private ByteArrayStream byte_stream;

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/util/ByteSequence$ByteArrayStream.class */
    private static final class ByteArrayStream extends ByteArrayInputStream {
        ByteArrayStream(byte[] bArr) {
            super(bArr);
        }

        final int getPosition() {
            return this.pos;
        }

        final void unreadByte() {
            if (this.pos > 0) {
                this.pos--;
            }
        }
    }

    public ByteSequence(byte[] bArr) {
        super(new ByteArrayStream(bArr));
        this.byte_stream = (ByteArrayStream) this.in;
    }

    public final int getIndex() {
        return this.byte_stream.getPosition();
    }

    final void unreadByte() {
        this.byte_stream.unreadByte();
    }
}
