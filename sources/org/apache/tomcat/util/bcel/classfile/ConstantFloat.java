package org.apache.tomcat.util.bcel.classfile;

import java.io.DataInput;
import java.io.IOException;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/bcel/classfile/ConstantFloat.class */
public final class ConstantFloat extends Constant {
    private final float bytes;

    ConstantFloat(DataInput file) throws IOException {
        super((byte) 4);
        this.bytes = file.readFloat();
    }

    public final float getBytes() {
        return this.bytes;
    }
}
