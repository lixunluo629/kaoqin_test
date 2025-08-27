package org.apache.xmlbeans.impl.jam.internal.elements;

import org.apache.xmlbeans.impl.jam.JAnnotation;
import org.apache.xmlbeans.impl.jam.JAnnotationValue;
import org.apache.xmlbeans.impl.jam.JClass;
import org.apache.xmlbeans.impl.jam.internal.classrefs.JClassRef;
import org.apache.xmlbeans.impl.jam.internal.classrefs.QualifiedJClassRef;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/elements/AnnotationValueImpl.class */
public class AnnotationValueImpl implements JAnnotationValue {
    private Object mValue;
    private JClassRef mType;
    private String mName;
    private ElementContext mContext;

    public AnnotationValueImpl(ElementContext ctx, String name, Object value, JClass type) {
        this.mValue = null;
        this.mType = null;
        if (ctx == null) {
            throw new IllegalArgumentException("null ctx");
        }
        if (name == null) {
            throw new IllegalArgumentException("null name");
        }
        if (value == null) {
            throw new IllegalArgumentException("null value");
        }
        if (type == null) {
            throw new IllegalArgumentException("null type");
        }
        if (value.getClass().isArray()) {
            this.mValue = ensureArrayWrapped(value);
        } else {
            this.mValue = value;
        }
        this.mContext = ctx;
        this.mName = name;
        this.mType = QualifiedJClassRef.create(type);
    }

    @Override // org.apache.xmlbeans.impl.jam.JAnnotationValue
    public boolean isDefaultValueUsed() {
        throw new IllegalStateException("NYI");
    }

    @Override // org.apache.xmlbeans.impl.jam.JAnnotationValue
    public String getName() {
        return this.mName;
    }

    @Override // org.apache.xmlbeans.impl.jam.JAnnotationValue
    public JClass getType() {
        return this.mType.getRefClass();
    }

    @Override // org.apache.xmlbeans.impl.jam.JAnnotationValue
    public JAnnotation asAnnotation() {
        if (this.mValue instanceof JAnnotation) {
            return (JAnnotation) this.mValue;
        }
        return null;
    }

    @Override // org.apache.xmlbeans.impl.jam.JAnnotationValue
    public JClass asClass() {
        if (this.mValue instanceof JClass) {
            return (JClass) this.mValue;
        }
        return null;
    }

    @Override // org.apache.xmlbeans.impl.jam.JAnnotationValue
    public String asString() {
        if (this.mValue == null) {
            return null;
        }
        return this.mValue.toString();
    }

    @Override // org.apache.xmlbeans.impl.jam.JAnnotationValue
    public int asInt() throws NumberFormatException {
        if (this.mValue == null) {
            return 0;
        }
        if (this.mValue instanceof Number) {
            return ((Number) this.mValue).intValue();
        }
        try {
            return Integer.parseInt(this.mValue.toString().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override // org.apache.xmlbeans.impl.jam.JAnnotationValue
    public boolean asBoolean() throws IllegalArgumentException {
        if (this.mValue == null) {
            return false;
        }
        return Boolean.valueOf(this.mValue.toString().trim()).booleanValue();
    }

    @Override // org.apache.xmlbeans.impl.jam.JAnnotationValue
    public long asLong() throws NumberFormatException {
        if (this.mValue == null) {
            return 0L;
        }
        if (this.mValue instanceof Number) {
            return ((Number) this.mValue).longValue();
        }
        try {
            return Long.parseLong(this.mValue.toString().trim());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    @Override // org.apache.xmlbeans.impl.jam.JAnnotationValue
    public short asShort() throws NumberFormatException {
        if (this.mValue == null) {
            return (short) 0;
        }
        if (this.mValue instanceof Number) {
            return ((Number) this.mValue).shortValue();
        }
        try {
            return Short.parseShort(this.mValue.toString().trim());
        } catch (NumberFormatException e) {
            return (short) 0;
        }
    }

    @Override // org.apache.xmlbeans.impl.jam.JAnnotationValue
    public double asDouble() throws NumberFormatException {
        if (this.mValue == null) {
            return 0.0d;
        }
        if (this.mValue instanceof Number) {
            return ((Number) this.mValue).doubleValue();
        }
        try {
            return Double.parseDouble(this.mValue.toString().trim());
        } catch (NumberFormatException e) {
            return 0.0d;
        }
    }

    @Override // org.apache.xmlbeans.impl.jam.JAnnotationValue
    public float asFloat() throws NumberFormatException {
        if (this.mValue == null) {
            return 0.0f;
        }
        if (this.mValue instanceof Number) {
            return ((Number) this.mValue).floatValue();
        }
        try {
            return Float.parseFloat(this.mValue.toString().trim());
        } catch (NumberFormatException e) {
            return 0.0f;
        }
    }

    @Override // org.apache.xmlbeans.impl.jam.JAnnotationValue
    public byte asByte() throws NumberFormatException {
        if (this.mValue == null) {
            return (byte) 0;
        }
        if (this.mValue instanceof Number) {
            return ((Number) this.mValue).byteValue();
        }
        try {
            return Byte.parseByte(this.mValue.toString().trim());
        } catch (NumberFormatException e) {
            return (byte) 0;
        }
    }

    @Override // org.apache.xmlbeans.impl.jam.JAnnotationValue
    public char asChar() throws IllegalArgumentException {
        if (this.mValue == null) {
            return (char) 0;
        }
        if (this.mValue instanceof Character) {
            return ((Character) this.mValue).charValue();
        }
        this.mValue = this.mValue.toString();
        if (((String) this.mValue).length() == 0) {
            return (char) 0;
        }
        return ((String) this.mValue).charAt(0);
    }

    @Override // org.apache.xmlbeans.impl.jam.JAnnotationValue
    public JClass[] asClassArray() {
        if (this.mValue instanceof JClass[]) {
            return (JClass[]) this.mValue;
        }
        return null;
    }

    @Override // org.apache.xmlbeans.impl.jam.JAnnotationValue
    public JAnnotation[] asAnnotationArray() {
        if (this.mValue instanceof JAnnotation[]) {
            return (JAnnotation[]) this.mValue;
        }
        return null;
    }

    @Override // org.apache.xmlbeans.impl.jam.JAnnotationValue
    public String[] asStringArray() {
        if (!this.mValue.getClass().isArray()) {
            return null;
        }
        String[] out = new String[((Object[]) this.mValue).length];
        for (int i = 0; i < out.length; i++) {
            if (((Object[]) this.mValue)[i] == null) {
                this.mContext.getLogger().error("Null annotation value array element on " + getName());
                out[i] = "";
            } else {
                out[i] = ((Object[]) this.mValue)[i].toString();
            }
        }
        return out;
    }

    @Override // org.apache.xmlbeans.impl.jam.JAnnotationValue
    public int[] asIntArray() throws NumberFormatException {
        if (!this.mValue.getClass().isArray()) {
            return null;
        }
        int[] out = new int[((Object[]) this.mValue).length];
        for (int i = 0; i < out.length; i++) {
            if (((Object[]) this.mValue)[i] == null) {
                this.mContext.getLogger().error("Null annotation value array element " + i + " on " + getName());
                out[i] = 0;
            } else {
                out[i] = Integer.parseInt(((Object[]) this.mValue)[i].toString());
            }
        }
        return out;
    }

    @Override // org.apache.xmlbeans.impl.jam.JAnnotationValue
    public boolean[] asBooleanArray() throws IllegalArgumentException {
        if (!this.mValue.getClass().isArray()) {
            return null;
        }
        boolean[] out = new boolean[((Object[]) this.mValue).length];
        for (int i = 0; i < out.length; i++) {
            if (((Object[]) this.mValue)[i] == null) {
                this.mContext.getLogger().error("Null annotation value array element " + i + " on " + getName());
                out[i] = false;
            } else {
                out[i] = Boolean.valueOf(((Object[]) this.mValue)[i].toString()).booleanValue();
            }
        }
        return out;
    }

    @Override // org.apache.xmlbeans.impl.jam.JAnnotationValue
    public short[] asShortArray() throws NumberFormatException {
        if (!this.mValue.getClass().isArray()) {
            return null;
        }
        short[] out = new short[((Object[]) this.mValue).length];
        for (int i = 0; i < out.length; i++) {
            if (((Object[]) this.mValue)[i] == null) {
                this.mContext.getLogger().error("Null annotation value array element " + i + " on " + getName());
                out[i] = 0;
            } else {
                out[i] = Short.parseShort(((Object[]) this.mValue)[i].toString());
            }
        }
        return out;
    }

    @Override // org.apache.xmlbeans.impl.jam.JAnnotationValue
    public long[] asLongArray() throws NumberFormatException {
        if (!this.mValue.getClass().isArray()) {
            return null;
        }
        long[] out = new long[((Object[]) this.mValue).length];
        for (int i = 0; i < out.length; i++) {
            if (((Object[]) this.mValue)[i] == null) {
                this.mContext.getLogger().error("Null annotation value array element " + i + " on " + getName());
                out[i] = 0;
            } else {
                out[i] = Long.parseLong(((Object[]) this.mValue)[i].toString());
            }
        }
        return out;
    }

    @Override // org.apache.xmlbeans.impl.jam.JAnnotationValue
    public double[] asDoubleArray() throws NumberFormatException {
        if (!this.mValue.getClass().isArray()) {
            return null;
        }
        double[] out = new double[((Object[]) this.mValue).length];
        for (int i = 0; i < out.length; i++) {
            if (((Object[]) this.mValue)[i] == null) {
                this.mContext.getLogger().error("Null annotation value array element " + i + " on " + getName());
                out[i] = 0.0d;
            } else {
                out[i] = Double.parseDouble(((Object[]) this.mValue)[i].toString());
            }
        }
        return out;
    }

    @Override // org.apache.xmlbeans.impl.jam.JAnnotationValue
    public float[] asFloatArray() throws NumberFormatException {
        if (!this.mValue.getClass().isArray()) {
            return null;
        }
        float[] out = new float[((Object[]) this.mValue).length];
        for (int i = 0; i < out.length; i++) {
            if (((Object[]) this.mValue)[i] == null) {
                this.mContext.getLogger().error("Null annotation value array element " + i + " on " + getName());
                out[i] = 0.0f;
            } else {
                out[i] = Float.parseFloat(((Object[]) this.mValue)[i].toString());
            }
        }
        return out;
    }

    @Override // org.apache.xmlbeans.impl.jam.JAnnotationValue
    public byte[] asByteArray() throws NumberFormatException {
        if (!this.mValue.getClass().isArray()) {
            return null;
        }
        byte[] out = new byte[((Object[]) this.mValue).length];
        for (int i = 0; i < out.length; i++) {
            if (((Object[]) this.mValue)[i] == null) {
                this.mContext.getLogger().error("Null annotation value array element " + i + " on " + getName());
                out[i] = 0;
            } else {
                out[i] = Byte.parseByte(((Object[]) this.mValue)[i].toString());
            }
        }
        return out;
    }

    @Override // org.apache.xmlbeans.impl.jam.JAnnotationValue
    public char[] asCharArray() throws IllegalArgumentException {
        if (!this.mValue.getClass().isArray()) {
            return null;
        }
        char[] out = new char[((Object[]) this.mValue).length];
        for (int i = 0; i < out.length; i++) {
            if (((Object[]) this.mValue)[i] == null) {
                this.mContext.getLogger().error("Null annotation value array element " + i + " on " + getName());
                out[i] = 0;
            } else {
                out[i] = ((Object[]) this.mValue)[i].toString().charAt(0);
            }
        }
        return out;
    }

    private static final Object[] ensureArrayWrapped(Object o) {
        if (o instanceof Object[]) {
            return (Object[]) o;
        }
        if (o instanceof int[]) {
            int dims = ((int[]) o).length;
            Integer[] out = new Integer[dims];
            for (int i = 0; i < dims; i++) {
                out[i] = new Integer(((int[]) o)[i]);
            }
            return out;
        }
        if (o instanceof boolean[]) {
            int dims2 = ((boolean[]) o).length;
            Boolean[] out2 = new Boolean[dims2];
            for (int i2 = 0; i2 < dims2; i2++) {
                out2[i2] = Boolean.valueOf(((boolean[]) o)[i2]);
            }
            return out2;
        }
        if (o instanceof byte[]) {
            int dims3 = ((byte[]) o).length;
            Byte[] out3 = new Byte[dims3];
            for (int i3 = 0; i3 < dims3; i3++) {
                out3[i3] = new Byte(((byte[]) o)[i3]);
            }
            return out3;
        }
        if (o instanceof char[]) {
            int dims4 = ((char[]) o).length;
            Character[] out4 = new Character[dims4];
            for (int i4 = 0; i4 < dims4; i4++) {
                out4[i4] = new Character(((char[]) o)[i4]);
            }
            return out4;
        }
        if (o instanceof float[]) {
            int dims5 = ((float[]) o).length;
            Float[] out5 = new Float[dims5];
            for (int i5 = 0; i5 < dims5; i5++) {
                out5[i5] = new Float(((float[]) o)[i5]);
            }
            return out5;
        }
        if (o instanceof double[]) {
            int dims6 = ((double[]) o).length;
            Double[] out6 = new Double[dims6];
            for (int i6 = 0; i6 < dims6; i6++) {
                out6[i6] = new Double(((double[]) o)[i6]);
            }
            return out6;
        }
        if (o instanceof long[]) {
            int dims7 = ((long[]) o).length;
            Long[] out7 = new Long[dims7];
            for (int i7 = 0; i7 < dims7; i7++) {
                out7[i7] = new Long(((long[]) o)[i7]);
            }
            return out7;
        }
        if (o instanceof short[]) {
            int dims8 = ((short[]) o).length;
            Short[] out8 = new Short[dims8];
            for (int i8 = 0; i8 < dims8; i8++) {
                out8[i8] = new Short(((short[]) o)[i8]);
            }
            return out8;
        }
        throw new IllegalStateException("Unknown array type " + o.getClass());
    }

    @Override // org.apache.xmlbeans.impl.jam.JAnnotationValue
    public Object getValue() {
        return this.mValue;
    }
}
