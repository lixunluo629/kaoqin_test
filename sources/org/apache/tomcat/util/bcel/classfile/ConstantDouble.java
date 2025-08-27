package org.apache.tomcat.util.bcel.classfile;

import java.io.DataInput;
import java.io.IOException;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/bcel/classfile/ConstantDouble.class */
public final class ConstantDouble extends Constant {
    private final double bytes;

    ConstantDouble(DataInput file) throws IOException {
        super((byte) 6);
        this.bytes = file.readDouble();
    }

    public final double getBytes() {
        return this.bytes;
    }
}
