package aj.org.objectweb.asm;

/* loaded from: aspectjweaver-1.8.14.jar:aj/org/objectweb/asm/TypeReference.class */
public class TypeReference {
    public static final int CLASS_TYPE_PARAMETER = 0;
    public static final int METHOD_TYPE_PARAMETER = 1;
    public static final int CLASS_EXTENDS = 16;
    public static final int CLASS_TYPE_PARAMETER_BOUND = 17;
    public static final int METHOD_TYPE_PARAMETER_BOUND = 18;
    public static final int FIELD = 19;
    public static final int METHOD_RETURN = 20;
    public static final int METHOD_RECEIVER = 21;
    public static final int METHOD_FORMAL_PARAMETER = 22;
    public static final int THROWS = 23;
    public static final int LOCAL_VARIABLE = 64;
    public static final int RESOURCE_VARIABLE = 65;
    public static final int EXCEPTION_PARAMETER = 66;
    public static final int INSTANCEOF = 67;
    public static final int NEW = 68;
    public static final int CONSTRUCTOR_REFERENCE = 69;
    public static final int METHOD_REFERENCE = 70;
    public static final int CAST = 71;
    public static final int CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT = 72;
    public static final int METHOD_INVOCATION_TYPE_ARGUMENT = 73;
    public static final int CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT = 74;
    public static final int METHOD_REFERENCE_TYPE_ARGUMENT = 75;
    private int a;

    public TypeReference(int i) {
        this.a = i;
    }

    public static TypeReference newTypeReference(int i) {
        return new TypeReference(i << 24);
    }

    public static TypeReference newTypeParameterReference(int i, int i2) {
        return new TypeReference((i << 24) | (i2 << 16));
    }

    public static TypeReference newTypeParameterBoundReference(int i, int i2, int i3) {
        return new TypeReference((i << 24) | (i2 << 16) | (i3 << 8));
    }

    public static TypeReference newSuperTypeReference(int i) {
        return new TypeReference(268435456 | ((i & 65535) << 8));
    }

    public static TypeReference newFormalParameterReference(int i) {
        return new TypeReference(369098752 | (i << 16));
    }

    public static TypeReference newExceptionReference(int i) {
        return new TypeReference(385875968 | (i << 8));
    }

    public static TypeReference newTryCatchReference(int i) {
        return new TypeReference(1107296256 | (i << 8));
    }

    public static TypeReference newTypeArgumentReference(int i, int i2) {
        return new TypeReference((i << 24) | i2);
    }

    public int getSort() {
        return this.a >>> 24;
    }

    public int getTypeParameterIndex() {
        return (this.a & 16711680) >> 16;
    }

    public int getTypeParameterBoundIndex() {
        return (this.a & 65280) >> 8;
    }

    public int getSuperTypeIndex() {
        return (short) ((this.a & 16776960) >> 8);
    }

    public int getFormalParameterIndex() {
        return (this.a & 16711680) >> 16;
    }

    public int getExceptionIndex() {
        return (this.a & 16776960) >> 8;
    }

    public int getTryCatchBlockIndex() {
        return (this.a & 16776960) >> 8;
    }

    public int getTypeArgumentIndex() {
        return this.a & 255;
    }

    public int getValue() {
        return this.a;
    }
}
