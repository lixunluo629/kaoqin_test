package org.apache.tomcat.util.bcel.classfile;

import java.io.DataInput;
import java.io.IOException;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/bcel/classfile/ConstantClass.class */
public final class ConstantClass extends Constant {
    private final int name_index;

    ConstantClass(DataInput dataInput) throws IOException {
        super((byte) 7);
        this.name_index = dataInput.readUnsignedShort();
    }

    public final int getNameIndex() {
        return this.name_index;
    }
}
