package org.apache.tomcat.util.bcel.classfile;

import java.io.DataInput;
import java.io.IOException;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/bcel/classfile/ConstantLong.class */
public final class ConstantLong extends Constant {
    private final long bytes;

    ConstantLong(DataInput input) throws IOException {
        super((byte) 5);
        this.bytes = input.readLong();
    }

    public final long getBytes() {
        return this.bytes;
    }
}
