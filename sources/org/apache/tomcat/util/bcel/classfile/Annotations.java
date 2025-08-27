package org.apache.tomcat.util.bcel.classfile;

import java.io.DataInput;
import java.io.IOException;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/bcel/classfile/Annotations.class */
public class Annotations {
    private final AnnotationEntry[] annotation_table;

    Annotations(DataInput input, ConstantPool constant_pool) throws IOException {
        int annotation_table_length = input.readUnsignedShort();
        this.annotation_table = new AnnotationEntry[annotation_table_length];
        for (int i = 0; i < annotation_table_length; i++) {
            this.annotation_table[i] = new AnnotationEntry(input, constant_pool);
        }
    }

    public AnnotationEntry[] getAnnotationEntries() {
        return this.annotation_table;
    }
}
