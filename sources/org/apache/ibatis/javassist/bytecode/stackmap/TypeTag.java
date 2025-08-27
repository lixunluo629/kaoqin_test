package org.apache.ibatis.javassist.bytecode.stackmap;

import org.apache.ibatis.javassist.bytecode.stackmap.TypeData;
import org.apache.xmlbeans.XmlErrorCodes;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/bytecode/stackmap/TypeTag.class */
public interface TypeTag {
    public static final String TOP_TYPE = "*top*";
    public static final TypeData.BasicType TOP = new TypeData.BasicType(TOP_TYPE, 0, ' ');
    public static final TypeData.BasicType INTEGER = new TypeData.BasicType(XmlErrorCodes.INT, 1, 'I');
    public static final TypeData.BasicType FLOAT = new TypeData.BasicType(XmlErrorCodes.FLOAT, 2, 'F');
    public static final TypeData.BasicType DOUBLE = new TypeData.BasicType(XmlErrorCodes.DOUBLE, 3, 'D');
    public static final TypeData.BasicType LONG = new TypeData.BasicType(XmlErrorCodes.LONG, 4, 'J');
}
