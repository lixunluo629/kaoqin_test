package org.apache.xmlbeans.impl.jam.internal.elements;

import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import java.io.StringWriter;
import org.apache.xmlbeans.impl.jam.JClass;
import org.apache.xmlbeans.impl.jam.JamClassLoader;
import org.springframework.beans.PropertyAccessor;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/elements/ArrayClassImpl.class */
public final class ArrayClassImpl extends BuiltinClassImpl {
    private int mDimensions;
    private JClass mComponentType;

    public static JClass createClassForFD(String arrayFD, JamClassLoader loader) {
        if (!arrayFD.startsWith(PropertyAccessor.PROPERTY_KEY_PREFIX)) {
            throw new IllegalArgumentException("must be an array type fd: " + arrayFD);
        }
        if (arrayFD.endsWith(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR)) {
            int dims = arrayFD.indexOf(StandardRoles.L);
            if (dims != -1 && dims < arrayFD.length() - 2) {
                String componentType = arrayFD.substring(dims + 1, arrayFD.length() - 1);
                return new ArrayClassImpl(loader.loadClass(componentType), dims);
            }
            throw new IllegalArgumentException("array type field descriptor '" + arrayFD + "' is malformed");
        }
        int dims2 = arrayFD.lastIndexOf(PropertyAccessor.PROPERTY_KEY_PREFIX) + 1;
        String compFd = arrayFD.substring(dims2, dims2 + 1);
        JClass primType = loader.loadClass(compFd);
        if (primType == null) {
            throw new IllegalArgumentException("array type field descriptor '" + arrayFD + "' is malformed");
        }
        return new ArrayClassImpl(primType, dims2);
    }

    public static String normalizeArrayName(String declaration) {
        if (declaration.startsWith(PropertyAccessor.PROPERTY_KEY_PREFIX)) {
            return declaration;
        }
        if (declaration.endsWith("]")) {
            int bracket = declaration.indexOf(91);
            if (bracket != -1) {
                String typeName = declaration.substring(0, bracket);
                String fd = PrimitiveClassImpl.getPrimitiveClassForName(typeName);
                if (fd == null) {
                    fd = 'L' + typeName + ';';
                }
                StringWriter out = new StringWriter();
                do {
                    out.write(91);
                    bracket = declaration.indexOf(91, bracket + 1);
                } while (bracket != -1);
                out.write(fd);
                return out.toString();
            }
        }
        throw new IllegalArgumentException("'" + declaration + "' does not name an array");
    }

    /* JADX WARN: Multi-variable type inference failed */
    private ArrayClassImpl(JClass jClass, int dimensions) {
        super(((ElementImpl) jClass).getContext());
        if (dimensions < 1) {
            throw new IllegalArgumentException("dimensions=" + dimensions);
        }
        if (jClass == 0) {
            throw new IllegalArgumentException("null componentType");
        }
        this.mComponentType = jClass;
        this.mDimensions = dimensions;
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.ElementImpl, org.apache.xmlbeans.impl.jam.JElement
    public String getSimpleName() {
        String out = getQualifiedName();
        int lastDot = out.lastIndexOf(46);
        return lastDot == -1 ? out : out.substring(lastDot + 1);
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.BuiltinClassImpl, org.apache.xmlbeans.impl.jam.JElement
    public String getQualifiedName() {
        StringWriter out = new StringWriter();
        out.write(this.mComponentType.getQualifiedName());
        for (int i = 0; i < this.mDimensions; i++) {
            out.write("[]");
        }
        return out.toString();
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.BuiltinClassImpl, org.apache.xmlbeans.impl.jam.JClass
    public boolean isArrayType() {
        return true;
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.BuiltinClassImpl, org.apache.xmlbeans.impl.jam.JClass
    public JClass getArrayComponentType() {
        return this.mComponentType;
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.BuiltinClassImpl, org.apache.xmlbeans.impl.jam.JClass
    public int getArrayDimensions() {
        return this.mDimensions;
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.BuiltinClassImpl, org.apache.xmlbeans.impl.jam.JClass
    public JClass getSuperclass() {
        return getClassLoader().loadClass("java.lang.Object");
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public boolean isAssignableFrom(JClass c) {
        return c.isArrayType() && c.getArrayDimensions() == this.mDimensions && this.mComponentType.isAssignableFrom(c.getArrayComponentType());
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.BuiltinClassImpl, org.apache.xmlbeans.impl.jam.JClass
    public String getFieldDescriptor() {
        StringWriter out = new StringWriter();
        for (int i = 0; i < this.mDimensions; i++) {
            out.write(PropertyAccessor.PROPERTY_KEY_PREFIX);
        }
        if (this.mComponentType.isPrimitiveType()) {
            out.write(this.mComponentType.getFieldDescriptor());
        } else {
            out.write(StandardRoles.L);
            out.write(this.mComponentType.getQualifiedName());
            out.write(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
        }
        return out.toString();
    }
}
