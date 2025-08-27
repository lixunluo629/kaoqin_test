package org.aspectj.apache.bcel.generic;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.aspectj.apache.bcel.ConstantsInitializer;
import org.aspectj.apache.bcel.classfile.ClassFormatException;
import org.aspectj.apache.bcel.classfile.Utility;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/Type.class */
public abstract class Type {
    protected byte type;
    protected String signature;
    public static final BasicType VOID = new BasicType((byte) 12);
    public static final BasicType BOOLEAN = new BasicType((byte) 4);
    public static final BasicType INT = new BasicType((byte) 10);
    public static final BasicType SHORT = new BasicType((byte) 9);
    public static final BasicType BYTE = new BasicType((byte) 8);
    public static final BasicType LONG = new BasicType((byte) 11);
    public static final BasicType DOUBLE = new BasicType((byte) 7);
    public static final BasicType FLOAT = new BasicType((byte) 6);
    public static final BasicType CHAR = new BasicType((byte) 5);
    public static final ObjectType OBJECT = new ObjectType("java.lang.Object");
    public static final ObjectType STRING = new ObjectType("java.lang.String");
    public static final ObjectType OBJECT_ARRAY = new ObjectType("java.lang.Object[]");
    public static final ObjectType STRING_ARRAY = new ObjectType("java.lang.String[]");
    public static final ObjectType STRINGBUFFER = new ObjectType("java.lang.StringBuffer");
    public static final ObjectType STRINGBUILDER = new ObjectType("java.lang.StringBuilder");
    public static final ObjectType THROWABLE = new ObjectType("java.lang.Throwable");
    public static final ObjectType CLASS = new ObjectType("java.lang.Class");
    public static final ObjectType INTEGER = new ObjectType("java.lang.Integer");
    public static final ObjectType EXCEPTION = new ObjectType("java.lang.Exception");
    public static final ObjectType LIST = new ObjectType("java.util.List");
    public static final ObjectType ITERATOR = new ObjectType("java.util.Iterator");
    public static final Type[] NO_ARGS = new Type[0];
    public static final ReferenceType NULL = new ReferenceType() { // from class: org.aspectj.apache.bcel.generic.Type.1
    };
    public static final Type UNKNOWN = new Type(15, "<unknown object>") { // from class: org.aspectj.apache.bcel.generic.Type.2
    };
    public static final Type[] STRINGARRAY1 = {STRING};
    public static final Type[] STRINGARRAY2 = {STRING, STRING};
    public static final Type[] STRINGARRAY3 = {STRING, STRING, STRING};
    public static final Type[] STRINGARRAY4 = {STRING, STRING, STRING, STRING};
    public static final Type[] STRINGARRAY5 = {STRING, STRING, STRING, STRING, STRING};
    public static final Type[] STRINGARRAY6 = {STRING, STRING, STRING, STRING, STRING, STRING};
    public static final Type[] STRINGARRAY7 = {STRING, STRING, STRING, STRING, STRING, STRING, STRING};
    private static Map<String, Type> commonTypes = new HashMap();

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/Type$TypeHolder.class */
    public static class TypeHolder {
        private Type t;
        private int consumed;

        public Type getType() {
            return this.t;
        }

        public int getConsumed() {
            return this.consumed;
        }

        public TypeHolder(Type type, int i) {
            this.t = type;
            this.consumed = i;
        }
    }

    protected Type(byte b, String str) {
        this.type = b;
        this.signature = str;
    }

    public String getSignature() {
        return this.signature;
    }

    public byte getType() {
        return this.type;
    }

    public int getSize() {
        switch (this.type) {
            case 7:
            case 11:
                return 2;
            case 12:
                return 0;
            default:
                return 1;
        }
    }

    public String toString() {
        return (equals(NULL) || this.type >= 15) ? this.signature : Utility.signatureToString(this.signature, false);
    }

    public static final Type getType(String str) throws ClassFormatException {
        String strReplace;
        Type type = commonTypes.get(str);
        if (type != null) {
            return type;
        }
        byte bTypeOfSignature = Utility.typeOfSignature(str);
        if (bTypeOfSignature <= 12) {
            return BasicType.getType(bTypeOfSignature);
        }
        if (bTypeOfSignature == 13) {
            int i = 0;
            do {
                i++;
            } while (str.charAt(i) == '[');
            return new ArrayType(getType(str.substring(i)), i);
        }
        int iIndexOf = str.indexOf(60);
        int iIndexOf2 = str.indexOf(59);
        if (iIndexOf == -1 || iIndexOf > iIndexOf2) {
            strReplace = str.substring(1, iIndexOf2).replace('/', '.');
        } else {
            boolean z = false;
            int i2 = iIndexOf;
            int i3 = 0;
            while (!z) {
                int i4 = i2;
                i2++;
                switch (str.charAt(i4)) {
                    case ';':
                        if (i3 != 0) {
                            break;
                        } else {
                            z = true;
                            break;
                        }
                    case '<':
                        i3++;
                        break;
                    case '>':
                        i3--;
                        break;
                }
            }
            int i5 = i2 - 1;
            strReplace = str.substring(1, iIndexOf).replace('/', '.');
        }
        return new ObjectType(strReplace);
    }

    public static final TypeHolder getTypeInternal(String str) throws StringIndexOutOfBoundsException, ClassFormatException {
        String strReplace;
        byte bTypeOfSignature = Utility.typeOfSignature(str);
        if (bTypeOfSignature <= 12) {
            return new TypeHolder(BasicType.getType(bTypeOfSignature), 1);
        }
        if (bTypeOfSignature == 13) {
            int i = 0;
            do {
                i++;
            } while (str.charAt(i) == '[');
            TypeHolder typeInternal = getTypeInternal(str.substring(i));
            return new TypeHolder(new ArrayType(typeInternal.getType(), i), i + typeInternal.getConsumed());
        }
        int iIndexOf = str.indexOf(59);
        if (iIndexOf < 0) {
            throw new ClassFormatException("Invalid signature: " + str);
        }
        int iIndexOf2 = str.indexOf(60);
        if (iIndexOf2 == -1 || iIndexOf2 > iIndexOf) {
            strReplace = str.substring(1, iIndexOf).replace('/', '.');
        } else {
            boolean z = false;
            int i2 = iIndexOf2;
            int i3 = 0;
            while (!z) {
                int i4 = i2;
                i2++;
                switch (str.charAt(i4)) {
                    case ';':
                        if (i3 != 0) {
                            break;
                        } else {
                            z = true;
                            break;
                        }
                    case '<':
                        i3++;
                        break;
                    case '>':
                        i3--;
                        break;
                }
            }
            iIndexOf = i2 - 1;
            strReplace = str.substring(1, iIndexOf2).replace('/', '.');
        }
        return new TypeHolder(new ObjectType(strReplace), iIndexOf + 1);
    }

    public static Type getReturnType(String str) {
        try {
            return getType(str.substring(str.lastIndexOf(41) + 1));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ClassFormatException("Invalid method signature: " + str);
        }
    }

    public static Type[] getArgumentTypes(String str) throws ClassFormatException {
        ArrayList arrayList = new ArrayList();
        try {
            if (str.charAt(0) != '(') {
                throw new ClassFormatException("Invalid method signature: " + str);
            }
            int consumed = 1;
            while (str.charAt(consumed) != ')') {
                TypeHolder typeInternal = getTypeInternal(str.substring(consumed));
                arrayList.add(typeInternal.getType());
                consumed += typeInternal.getConsumed();
            }
            Type[] typeArr = new Type[arrayList.size()];
            arrayList.toArray(typeArr);
            return typeArr;
        } catch (StringIndexOutOfBoundsException e) {
            throw new ClassFormatException("Invalid method signature: " + str);
        }
    }

    public static int getArgumentSizes(String str) throws ClassFormatException {
        int size = 0;
        if (str.charAt(0) != '(') {
            throw new ClassFormatException("Invalid method signature: " + str);
        }
        int consumed = 1;
        while (str.charAt(consumed) != ')') {
            try {
                byte bTypeOfSignature = Utility.typeOfSignature(str.charAt(consumed));
                if (bTypeOfSignature <= 12) {
                    size += BasicType.getType(bTypeOfSignature).getSize();
                    consumed++;
                } else if (bTypeOfSignature == 13) {
                    int i = 0;
                    do {
                        i++;
                    } while (str.charAt(i + consumed) == '[');
                    size++;
                    consumed += i + getTypeInternal(str.substring(i + consumed)).getConsumed();
                } else {
                    int iIndexOf = str.indexOf(59, consumed);
                    int iIndexOf2 = str.indexOf(60, consumed);
                    if (iIndexOf2 != -1 && iIndexOf2 <= iIndexOf) {
                        boolean z = false;
                        int i2 = iIndexOf2;
                        int i3 = 0;
                        while (!z) {
                            int i4 = i2;
                            i2++;
                            switch (str.charAt(i4)) {
                                case ';':
                                    if (i3 != 0) {
                                        break;
                                    } else {
                                        z = true;
                                        break;
                                    }
                                case '<':
                                    i3++;
                                    break;
                                case '>':
                                    i3--;
                                    break;
                            }
                        }
                        iIndexOf = i2 - 1;
                    }
                    size++;
                    consumed = iIndexOf + 1;
                }
            } catch (StringIndexOutOfBoundsException e) {
                throw new ClassFormatException("Invalid method signature: " + str);
            }
        }
        return size;
    }

    public static int getTypeSize(String str) throws ClassFormatException {
        byte bTypeOfSignature = Utility.typeOfSignature(str.charAt(0));
        return bTypeOfSignature <= 12 ? BasicType.getType(bTypeOfSignature).getSize() : bTypeOfSignature == 13 ? 1 : 1;
    }

    public static Type getType(Class cls) {
        if (cls == null) {
            throw new IllegalArgumentException("Class must not be null");
        }
        if (cls.isArray()) {
            return getType(cls.getName());
        }
        if (!cls.isPrimitive()) {
            return new ObjectType(cls.getName());
        }
        if (cls == Integer.TYPE) {
            return INT;
        }
        if (cls == Void.TYPE) {
            return VOID;
        }
        if (cls == Double.TYPE) {
            return DOUBLE;
        }
        if (cls == Float.TYPE) {
            return FLOAT;
        }
        if (cls == Boolean.TYPE) {
            return BOOLEAN;
        }
        if (cls == Byte.TYPE) {
            return BYTE;
        }
        if (cls == Short.TYPE) {
            return SHORT;
        }
        if (cls == Byte.TYPE) {
            return BYTE;
        }
        if (cls == Long.TYPE) {
            return LONG;
        }
        if (cls == Character.TYPE) {
            return CHAR;
        }
        throw new IllegalStateException("Ooops, what primitive type is " + cls);
    }

    public static String getSignature(Method method) {
        StringBuffer stringBuffer = new StringBuffer("(");
        for (Class<?> cls : method.getParameterTypes()) {
            stringBuffer.append(getType(cls).getSignature());
        }
        stringBuffer.append(")");
        stringBuffer.append(getType(method.getReturnType()).getSignature());
        return stringBuffer.toString();
    }

    public static String getSignature(Constructor<?> constructor) {
        StringBuffer stringBuffer = new StringBuffer("(");
        for (Class<?> cls : constructor.getParameterTypes()) {
            stringBuffer.append(getType(cls).getSignature());
        }
        stringBuffer.append(")V");
        return stringBuffer.toString();
    }

    static {
        commonTypes.put(STRING.getSignature(), STRING);
        commonTypes.put(THROWABLE.getSignature(), THROWABLE);
        commonTypes.put(VOID.getSignature(), VOID);
        commonTypes.put(BOOLEAN.getSignature(), BOOLEAN);
        commonTypes.put(BYTE.getSignature(), BYTE);
        commonTypes.put(SHORT.getSignature(), SHORT);
        commonTypes.put(CHAR.getSignature(), CHAR);
        commonTypes.put(INT.getSignature(), INT);
        commonTypes.put(LONG.getSignature(), LONG);
        commonTypes.put(DOUBLE.getSignature(), DOUBLE);
        commonTypes.put(FLOAT.getSignature(), FLOAT);
        commonTypes.put(CLASS.getSignature(), CLASS);
        commonTypes.put(OBJECT.getSignature(), OBJECT);
        commonTypes.put(STRING_ARRAY.getSignature(), STRING_ARRAY);
        commonTypes.put(OBJECT_ARRAY.getSignature(), OBJECT_ARRAY);
        commonTypes.put(INTEGER.getSignature(), INTEGER);
        commonTypes.put(EXCEPTION.getSignature(), EXCEPTION);
        commonTypes.put(STRINGBUFFER.getSignature(), STRINGBUFFER);
        commonTypes.put(STRINGBUILDER.getSignature(), STRINGBUILDER);
        commonTypes.put(LIST.getSignature(), LIST);
        commonTypes.put(ITERATOR.getSignature(), ITERATOR);
        ConstantsInitializer.initialize();
    }
}
