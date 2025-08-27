package org.apache.ibatis.javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/bytecode/DeprecatedAttribute.class */
public class DeprecatedAttribute extends AttributeInfo {
    public static final String tag = "Deprecated";

    DeprecatedAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
        super(cp, n, in);
    }

    public DeprecatedAttribute(ConstPool cp) {
        super(cp, tag, new byte[0]);
    }

    @Override // org.apache.ibatis.javassist.bytecode.AttributeInfo
    public AttributeInfo copy(ConstPool newCp, Map classnames) {
        return new DeprecatedAttribute(newCp);
    }
}
