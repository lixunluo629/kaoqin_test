package net.dongliu.apk.parser.cert.asn1;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.dongliu.apk.parser.cert.asn1.ber.BerDataValue;
import net.dongliu.apk.parser.cert.asn1.ber.BerDataValueFormatException;
import net.dongliu.apk.parser.cert.asn1.ber.BerDataValueReader;
import net.dongliu.apk.parser.cert.asn1.ber.BerEncoding;
import net.dongliu.apk.parser.cert.asn1.ber.ByteBufferBerDataValueReader;
import net.dongliu.apk.parser.utils.Buffers;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/cert/asn1/Asn1BerParser.class */
public final class Asn1BerParser {
    private Asn1BerParser() {
    }

    public static <T> T parse(ByteBuffer byteBuffer, Class<T> cls) throws Asn1DecodingException {
        try {
            BerDataValue dataValue = new ByteBufferBerDataValueReader(byteBuffer).readDataValue();
            if (dataValue == null) {
                throw new Asn1DecodingException("Empty input");
            }
            return (T) parse(dataValue, cls);
        } catch (BerDataValueFormatException e) {
            throw new Asn1DecodingException("Failed to decode top-level data value", e);
        }
    }

    public static <T> List<T> parseImplicitSetOf(ByteBuffer encoded, Class<T> elementClass) throws Asn1DecodingException {
        try {
            BerDataValue containerDataValue = new ByteBufferBerDataValueReader(encoded).readDataValue();
            if (containerDataValue == null) {
                throw new Asn1DecodingException("Empty input");
            }
            return parseSetOf(containerDataValue, elementClass);
        } catch (BerDataValueFormatException e) {
            throw new Asn1DecodingException("Failed to decode top-level data value", e);
        }
    }

    private static <T> T parse(BerDataValue berDataValue, Class<T> cls) throws Asn1DecodingException {
        if (berDataValue == null) {
            throw new NullPointerException("container == null");
        }
        if (cls == null) {
            throw new NullPointerException("containerClass == null");
        }
        Asn1Type containerAsn1Type = getContainerAsn1Type(cls);
        switch (containerAsn1Type) {
            case CHOICE:
                return (T) parseChoice(berDataValue, cls);
            case SEQUENCE:
                int tagNumber = BerEncoding.getTagNumber(containerAsn1Type);
                if (berDataValue.getTagClass() != 0 || berDataValue.getTagNumber() != tagNumber) {
                    throw new Asn1UnexpectedTagException("Unexpected data value read as " + cls.getName() + ". Expected " + BerEncoding.tagClassAndNumberToString(0, tagNumber) + ", but read: " + BerEncoding.tagClassAndNumberToString(berDataValue.getTagClass(), berDataValue.getTagNumber()));
                }
                return (T) parseSequence(berDataValue, cls);
            default:
                throw new Asn1DecodingException("Parsing container " + containerAsn1Type + " not supported");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T> T parseChoice(BerDataValue dataValue, Class<T> containerClass) throws IllegalAccessException, Asn1DecodingException, InstantiationException, IllegalArgumentException, InvocationTargetException {
        List<AnnotatedField> fields = getAnnotatedFields(containerClass);
        if (fields.isEmpty()) {
            throw new Asn1DecodingException("No fields annotated with " + Asn1Field.class.getName() + " in CHOICE class " + containerClass.getName());
        }
        for (int i = 0; i < fields.size() - 1; i++) {
            AnnotatedField f1 = fields.get(i);
            int tagNumber1 = f1.getBerTagNumber();
            int tagClass1 = f1.getBerTagClass();
            for (int j = i + 1; j < fields.size(); j++) {
                AnnotatedField f2 = fields.get(j);
                int tagNumber2 = f2.getBerTagNumber();
                int tagClass2 = f2.getBerTagClass();
                if (tagNumber1 == tagNumber2 && tagClass1 == tagClass2) {
                    throw new Asn1DecodingException("CHOICE fields are indistinguishable because they have the same tag class and number: " + containerClass.getName() + "." + f1.getField().getName() + " and ." + f2.getField().getName());
                }
            }
        }
        try {
            T obj = containerClass.getConstructor(new Class[0]).newInstance(new Object[0]);
            for (AnnotatedField field : fields) {
                try {
                    field.setValueFrom(dataValue, obj);
                    return obj;
                } catch (Asn1UnexpectedTagException e) {
                }
            }
            throw new Asn1DecodingException("No options of CHOICE " + containerClass.getName() + " matched");
        } catch (IllegalArgumentException | ReflectiveOperationException e2) {
            throw new Asn1DecodingException("Failed to instantiate " + containerClass.getName(), e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T> T parseSequence(BerDataValue container, Class<T> containerClass) throws IllegalAccessException, Asn1DecodingException, InstantiationException, IllegalArgumentException, InvocationTargetException {
        List<AnnotatedField> fields = getAnnotatedFields(containerClass);
        Collections.sort(fields, new Comparator<AnnotatedField>() { // from class: net.dongliu.apk.parser.cert.asn1.Asn1BerParser.1
            @Override // java.util.Comparator
            public int compare(AnnotatedField f1, AnnotatedField f2) {
                return f1.getAnnotation().index() - f2.getAnnotation().index();
            }
        });
        if (fields.size() > 1) {
            AnnotatedField lastField = null;
            for (AnnotatedField field : fields) {
                if (lastField != null && lastField.getAnnotation().index() == field.getAnnotation().index()) {
                    throw new Asn1DecodingException("Fields have the same index: " + containerClass.getName() + "." + lastField.getField().getName() + " and ." + field.getField().getName());
                }
                lastField = field;
            }
        }
        try {
            T t = containerClass.getConstructor(new Class[0]).newInstance(new Object[0]);
            int nextUnreadFieldIndex = 0;
            BerDataValueReader elementsReader = container.contentsReader();
            while (nextUnreadFieldIndex < fields.size()) {
                try {
                    BerDataValue dataValue = elementsReader.readDataValue();
                    if (dataValue == null) {
                        break;
                    }
                    int i = nextUnreadFieldIndex;
                    while (true) {
                        if (i < fields.size()) {
                            AnnotatedField field2 = fields.get(i);
                            try {
                                if (field2.isOptional()) {
                                    try {
                                        field2.setValueFrom(dataValue, t);
                                        nextUnreadFieldIndex = i + 1;
                                        break;
                                    } catch (Asn1UnexpectedTagException e) {
                                        i++;
                                    }
                                } else {
                                    field2.setValueFrom(dataValue, t);
                                    nextUnreadFieldIndex = i + 1;
                                    break;
                                }
                            } catch (Asn1DecodingException e2) {
                                throw new Asn1DecodingException("Failed to parse " + containerClass.getName() + "." + field2.getField().getName(), e2);
                            }
                        }
                    }
                } catch (BerDataValueFormatException e3) {
                    throw new Asn1DecodingException("Malformed data value", e3);
                }
            }
            return t;
        } catch (IllegalArgumentException | ReflectiveOperationException e4) {
            throw new Asn1DecodingException("Failed to instantiate " + containerClass.getName(), e4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v13, types: [java.lang.Object] */
    /* JADX WARN: Type inference failed for: r0v14, types: [net.dongliu.apk.parser.cert.asn1.Asn1OpaqueObject] */
    /* JADX WARN: Type inference failed for: r0v15 */
    /* JADX WARN: Type inference failed for: r0v19, types: [java.nio.ByteBuffer] */
    public static <T> List<T> parseSetOf(BerDataValue container, Class<T> elementClass) throws Asn1DecodingException {
        ?? asn1OpaqueObject;
        List<T> result = new ArrayList<>();
        BerDataValueReader elementsReader = container.contentsReader();
        while (true) {
            try {
                BerDataValue dataValue = elementsReader.readDataValue();
                if (dataValue != null) {
                    if (ByteBuffer.class.equals(elementClass)) {
                        asn1OpaqueObject = dataValue.getEncodedContents();
                    } else if (Asn1OpaqueObject.class.equals(elementClass)) {
                        asn1OpaqueObject = new Asn1OpaqueObject(dataValue.getEncoded());
                    } else {
                        asn1OpaqueObject = parse(dataValue, elementClass);
                    }
                    T element = asn1OpaqueObject;
                    result.add(element);
                } else {
                    return result;
                }
            } catch (BerDataValueFormatException e) {
                throw new Asn1DecodingException("Malformed data value", e);
            }
        }
    }

    private static Asn1Type getContainerAsn1Type(Class<?> containerClass) throws Asn1DecodingException {
        Asn1Class containerAnnotation = (Asn1Class) containerClass.getAnnotation(Asn1Class.class);
        if (containerAnnotation == null) {
            throw new Asn1DecodingException(containerClass.getName() + " is not annotated with " + Asn1Class.class.getName());
        }
        switch (containerAnnotation.type()) {
            case CHOICE:
            case SEQUENCE:
                return containerAnnotation.type();
            default:
                throw new Asn1DecodingException("Unsupported ASN.1 container annotation type: " + containerAnnotation.type());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Class<?> getElementType(Field field) throws Asn1DecodingException, ClassNotFoundException {
        String type = field.getGenericType().toString();
        int delimiterIndex = type.indexOf(60);
        if (delimiterIndex == -1) {
            throw new Asn1DecodingException("Not a container type: " + field.getGenericType());
        }
        int startIndex = delimiterIndex + 1;
        int endIndex = type.indexOf(62, startIndex);
        if (endIndex == -1) {
            throw new Asn1DecodingException("Not a container type: " + field.getGenericType());
        }
        String elementClassName = type.substring(startIndex, endIndex);
        return Class.forName(elementClassName);
    }

    /* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/cert/asn1/Asn1BerParser$AnnotatedField.class */
    private static final class AnnotatedField {
        private final Field mField;
        private final Asn1Field mAnnotation;
        private final Asn1Type mDataType;
        private final Asn1TagClass mTagClass;
        private final int mBerTagClass;
        private final int mBerTagNumber;
        private final Asn1Tagging mTagging;
        private final boolean mOptional;

        public AnnotatedField(Field field, Asn1Field annotation) throws Asn1DecodingException {
            int tagNumber;
            this.mField = field;
            this.mAnnotation = annotation;
            this.mDataType = annotation.type();
            Asn1TagClass tagClass = annotation.cls();
            if (tagClass == Asn1TagClass.AUTOMATIC) {
                if (annotation.tagNumber() != -1) {
                    tagClass = Asn1TagClass.CONTEXT_SPECIFIC;
                } else {
                    tagClass = Asn1TagClass.UNIVERSAL;
                }
            }
            this.mTagClass = tagClass;
            this.mBerTagClass = BerEncoding.getTagClass(this.mTagClass);
            if (annotation.tagNumber() != -1) {
                tagNumber = annotation.tagNumber();
            } else if (this.mDataType == Asn1Type.CHOICE || this.mDataType == Asn1Type.ANY) {
                tagNumber = -1;
            } else {
                tagNumber = BerEncoding.getTagNumber(this.mDataType);
            }
            this.mBerTagNumber = tagNumber;
            this.mTagging = annotation.tagging();
            if ((this.mTagging == Asn1Tagging.EXPLICIT || this.mTagging == Asn1Tagging.IMPLICIT) && annotation.tagNumber() == -1) {
                throw new Asn1DecodingException("Tag number must be specified when tagging mode is " + this.mTagging);
            }
            this.mOptional = annotation.optional();
        }

        public Field getField() {
            return this.mField;
        }

        public Asn1Field getAnnotation() {
            return this.mAnnotation;
        }

        public boolean isOptional() {
            return this.mOptional;
        }

        public int getBerTagClass() {
            return this.mBerTagClass;
        }

        public int getBerTagNumber() {
            return this.mBerTagNumber;
        }

        public void setValueFrom(BerDataValue dataValue, Object obj) throws IllegalAccessException, Asn1DecodingException, IllegalArgumentException {
            int readTagClass = dataValue.getTagClass();
            if (this.mBerTagNumber != -1) {
                int readTagNumber = dataValue.getTagNumber();
                if (readTagClass != this.mBerTagClass || readTagNumber != this.mBerTagNumber) {
                    throw new Asn1UnexpectedTagException("Tag mismatch. Expected: " + BerEncoding.tagClassAndNumberToString(this.mBerTagClass, this.mBerTagNumber) + ", but found " + BerEncoding.tagClassAndNumberToString(readTagClass, readTagNumber));
                }
            } else if (readTagClass != this.mBerTagClass) {
                throw new Asn1UnexpectedTagException("Tag mismatch. Expected class: " + BerEncoding.tagClassToString(this.mBerTagClass) + ", but found " + BerEncoding.tagClassToString(readTagClass));
            }
            if (this.mTagging == Asn1Tagging.EXPLICIT) {
                try {
                    dataValue = dataValue.contentsReader().readDataValue();
                } catch (BerDataValueFormatException e) {
                    throw new Asn1DecodingException("Failed to read contents of EXPLICIT data value", e);
                }
            }
            BerToJavaConverter.setFieldValue(obj, this.mField, this.mDataType, dataValue);
        }
    }

    /* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/cert/asn1/Asn1BerParser$Asn1UnexpectedTagException.class */
    private static class Asn1UnexpectedTagException extends Asn1DecodingException {
        private static final long serialVersionUID = 1;

        public Asn1UnexpectedTagException(String message) {
            super(message);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String oidToString(ByteBuffer encodedOid) throws Asn1DecodingException {
        if (!encodedOid.hasRemaining()) {
            throw new Asn1DecodingException("Empty OBJECT IDENTIFIER");
        }
        long firstComponent = decodeBase128UnsignedLong(encodedOid);
        int firstNode = (int) Math.min(firstComponent / 40, 2L);
        long secondNode = firstComponent - (firstNode * 40);
        StringBuilder result = new StringBuilder();
        result.append(Long.toString(firstNode)).append('.').append(Long.toString(secondNode));
        while (encodedOid.hasRemaining()) {
            long node = decodeBase128UnsignedLong(encodedOid);
            result.append('.').append(Long.toString(node));
        }
        return result.toString();
    }

    private static long decodeBase128UnsignedLong(ByteBuffer encoded) throws Asn1DecodingException {
        if (!encoded.hasRemaining()) {
            return 0L;
        }
        long result = 0;
        while (encoded.hasRemaining()) {
            if (result > 72057594037927935L) {
                throw new Asn1DecodingException("Base-128 number too large");
            }
            int b = encoded.get() & 255;
            result = (result << 7) | (b & 127);
            if ((b & 128) == 0) {
                return result;
            }
        }
        throw new Asn1DecodingException("Truncated base-128 encoded input: missing terminating byte, with highest bit not set");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static BigInteger integerToBigInteger(ByteBuffer encoded) {
        if (!encoded.hasRemaining()) {
            return BigInteger.ZERO;
        }
        return new BigInteger(Buffers.readBytes(encoded));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int integerToInt(ByteBuffer encoded) throws Asn1DecodingException {
        BigInteger value = integerToBigInteger(encoded);
        try {
            return value.intValue();
        } catch (ArithmeticException e) {
            throw new Asn1DecodingException(String.format("INTEGER cannot be represented as int: %1$d (0x%1$x)", value), e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long integerToLong(ByteBuffer encoded) throws Asn1DecodingException {
        BigInteger value = integerToBigInteger(encoded);
        try {
            return value.intValue();
        } catch (ArithmeticException e) {
            throw new Asn1DecodingException(String.format("INTEGER cannot be represented as long: %1$d (0x%1$x)", value), e);
        }
    }

    private static List<AnnotatedField> getAnnotatedFields(Class<?> containerClass) throws Asn1DecodingException {
        Field[] declaredFields = containerClass.getDeclaredFields();
        List<AnnotatedField> result = new ArrayList<>(declaredFields.length);
        for (Field field : declaredFields) {
            Asn1Field annotation = (Asn1Field) field.getAnnotation(Asn1Field.class);
            if (annotation != null) {
                if (Modifier.isStatic(field.getModifiers())) {
                    throw new Asn1DecodingException(Asn1Field.class.getName() + " used on a static field: " + containerClass.getName() + "." + field.getName());
                }
                try {
                    AnnotatedField annotatedField = new AnnotatedField(field, annotation);
                    result.add(annotatedField);
                } catch (Asn1DecodingException e) {
                    throw new Asn1DecodingException("Invalid ASN.1 annotation on " + containerClass.getName() + "." + field.getName(), e);
                }
            }
        }
        return result;
    }

    /* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/cert/asn1/Asn1BerParser$BerToJavaConverter.class */
    private static final class BerToJavaConverter {
        private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

        private BerToJavaConverter() {
        }

        public static void setFieldValue(Object obj, Field field, Asn1Type type, BerDataValue dataValue) throws IllegalAccessException, Asn1DecodingException, IllegalArgumentException {
            try {
                switch (type) {
                    case SET_OF:
                    case SEQUENCE_OF:
                        if (Asn1OpaqueObject.class.equals(field.getType())) {
                            field.set(obj, convert(type, dataValue, field.getType()));
                            return;
                        } else {
                            field.set(obj, Asn1BerParser.parseSetOf(dataValue, Asn1BerParser.getElementType(field)));
                            return;
                        }
                    default:
                        field.set(obj, convert(type, dataValue, field.getType()));
                        return;
                }
            } catch (ReflectiveOperationException e) {
                throw new Asn1DecodingException("Failed to set value of " + obj.getClass().getName() + "." + field.getName(), e);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v56, types: [T, byte[]] */
        public static <T> T convert(Asn1Type asn1Type, BerDataValue berDataValue, Class<T> cls) throws Asn1DecodingException {
            if (ByteBuffer.class.equals(cls)) {
                return (T) berDataValue.getEncodedContents();
            }
            if (byte[].class.equals(cls)) {
                ByteBuffer encodedContents = berDataValue.getEncodedContents();
                if (!encodedContents.hasRemaining()) {
                    return (T) EMPTY_BYTE_ARRAY;
                }
                ?? r0 = (T) new byte[encodedContents.remaining()];
                encodedContents.get((byte[]) r0);
                return r0;
            }
            if (Asn1OpaqueObject.class.equals(cls)) {
                return (T) new Asn1OpaqueObject(berDataValue.getEncoded());
            }
            ByteBuffer encodedContents2 = berDataValue.getEncodedContents();
            switch (asn1Type) {
                case CHOICE:
                    Asn1Class asn1Class = (Asn1Class) cls.getAnnotation(Asn1Class.class);
                    if (asn1Class != null && asn1Class.type() == Asn1Type.CHOICE) {
                        return (T) Asn1BerParser.parseChoice(berDataValue, cls);
                    }
                    break;
                case SEQUENCE:
                    Asn1Class asn1Class2 = (Asn1Class) cls.getAnnotation(Asn1Class.class);
                    if (asn1Class2 != null && asn1Class2.type() == Asn1Type.SEQUENCE) {
                        return (T) Asn1BerParser.parseSequence(berDataValue, cls);
                    }
                    break;
                case INTEGER:
                    if (Integer.TYPE.equals(cls) || Integer.class.equals(cls)) {
                        return (T) Integer.valueOf(Asn1BerParser.integerToInt(encodedContents2));
                    }
                    if (Long.TYPE.equals(cls) || Long.class.equals(cls)) {
                        return (T) Long.valueOf(Asn1BerParser.integerToLong(encodedContents2));
                    }
                    if (BigInteger.class.equals(cls)) {
                        return (T) Asn1BerParser.integerToBigInteger(encodedContents2);
                    }
                    break;
                case OBJECT_IDENTIFIER:
                    if (String.class.equals(cls)) {
                        return (T) Asn1BerParser.oidToString(encodedContents2);
                    }
                    break;
            }
            throw new Asn1DecodingException("Unsupported conversion: ASN.1 " + asn1Type + " to " + cls.getName());
        }
    }
}
