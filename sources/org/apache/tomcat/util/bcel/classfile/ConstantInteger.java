package org.apache.tomcat.util.bcel.classfile;

import java.io.DataInput;
import java.io.IOException;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/bcel/classfile/ConstantInteger.class */
public final class ConstantInteger extends Constant {
    private final int bytes;

    ConstantInteger(DataInput file) throws IOException {
        super((byte) 3);
        this.bytes = file.readInt();
    }

    public final int getBytes() {
        return this.bytes;
    }
}
