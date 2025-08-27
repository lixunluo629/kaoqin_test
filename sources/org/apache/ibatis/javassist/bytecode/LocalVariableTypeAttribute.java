package org.apache.ibatis.javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/bytecode/LocalVariableTypeAttribute.class */
public class LocalVariableTypeAttribute extends LocalVariableAttribute {
    public static final String tag = "LocalVariableTypeTable";

    public LocalVariableTypeAttribute(ConstPool cp) {
        super(cp, "LocalVariableTypeTable", new byte[2]);
        ByteArray.write16bit(0, this.info, 0);
    }

    LocalVariableTypeAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
        super(cp, n, in);
    }

    private LocalVariableTypeAttribute(ConstPool cp, byte[] dest) {
        super(cp, "LocalVariableTypeTable", dest);
    }

    @Override // org.apache.ibatis.javassist.bytecode.LocalVariableAttribute
    String renameEntry(String desc, String oldname, String newname) {
        return SignatureAttribute.renameClass(desc, oldname, newname);
    }

    @Override // org.apache.ibatis.javassist.bytecode.LocalVariableAttribute
    String renameEntry(String desc, Map classnames) {
        return SignatureAttribute.renameClass(desc, classnames);
    }

    @Override // org.apache.ibatis.javassist.bytecode.LocalVariableAttribute
    LocalVariableAttribute makeThisAttr(ConstPool cp, byte[] dest) {
        return new LocalVariableTypeAttribute(cp, dest);
    }
}
