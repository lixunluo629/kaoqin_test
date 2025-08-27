package org.apache.xmlbeans.impl.jam.internal.elements;

import java.util.HashMap;
import java.util.Map;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.impl.jam.JClass;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/elements/PrimitiveClassImpl.class */
public final class PrimitiveClassImpl extends BuiltinClassImpl {
    private static final Object[][] PRIMITIVES = {new Object[]{XmlErrorCodes.INT, "I", Integer.TYPE}, new Object[]{XmlErrorCodes.LONG, "J", Long.TYPE}, new Object[]{"boolean", "Z", Boolean.TYPE}, new Object[]{"short", "S", Short.TYPE}, new Object[]{"byte", "B", Byte.TYPE}, new Object[]{"char", "C", Character.TYPE}, new Object[]{XmlErrorCodes.FLOAT, "F", Float.TYPE}, new Object[]{XmlErrorCodes.DOUBLE, "D", Double.TYPE}};
    private static final Map NAME_TO_FD = new HashMap();
    private static final Map NAME_TO_CLASS = new HashMap();

    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    static {
        for (int i = 0; i < PRIMITIVES.length; i++) {
            NAME_TO_FD.put(PRIMITIVES[i][0], PRIMITIVES[i][1]);
            NAME_TO_CLASS.put(PRIMITIVES[i][0], PRIMITIVES[i][2]);
        }
    }

    public static void mapNameToPrimitive(ElementContext ctx, Map out) {
        for (int i = 0; i < PRIMITIVES.length; i++) {
            PrimitiveClassImpl primitiveClassImpl = new PrimitiveClassImpl(ctx, (String) PRIMITIVES[i][0]);
            out.put(PRIMITIVES[i][0], primitiveClassImpl);
            out.put(PRIMITIVES[i][1], primitiveClassImpl);
        }
    }

    public static String getPrimitiveClassForName(String named) {
        return (String) NAME_TO_FD.get(named);
    }

    public static boolean isPrimitive(String name) {
        return NAME_TO_FD.get(name) != null;
    }

    public static final String getFieldDescriptor(String classname) {
        return (String) NAME_TO_FD.get(classname);
    }

    public static final Class getPrimitiveClass(String classname) {
        return (Class) NAME_TO_CLASS.get(classname);
    }

    private PrimitiveClassImpl(ElementContext ctx, String name) {
        super(ctx);
        if (name == null) {
            throw new IllegalArgumentException("null name");
        }
        if (!NAME_TO_FD.containsKey(name)) {
            throw new IllegalArgumentException("Unknown primitive class '" + name + "'");
        }
        reallySetSimpleName(name);
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.BuiltinClassImpl, org.apache.xmlbeans.impl.jam.JElement
    public String getQualifiedName() {
        return getSimpleName();
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.BuiltinClassImpl, org.apache.xmlbeans.impl.jam.JClass
    public String getFieldDescriptor() {
        return (String) NAME_TO_FD.get(getSimpleName());
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public boolean isAssignableFrom(JClass c) {
        return c.isPrimitiveType() && c.getSimpleName().equals(getSimpleName());
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.BuiltinClassImpl, org.apache.xmlbeans.impl.jam.JClass
    public boolean isPrimitiveType() {
        return true;
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.BuiltinClassImpl, org.apache.xmlbeans.impl.jam.JClass
    public Class getPrimitiveClass() {
        return (Class) NAME_TO_CLASS.get(getSimpleName());
    }
}
