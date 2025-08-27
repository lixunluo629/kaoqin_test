package net.dongliu.apk.parser.cert.asn1;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.dongliu.apk.parser.cert.asn1.ber.BerEncoding;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/cert/asn1/Asn1DerEncoder.class */
public final class Asn1DerEncoder {
    private Asn1DerEncoder() {
    }

    public static byte[] encode(Object container) throws Asn1EncodingException {
        Class<?> containerClass = container.getClass();
        Asn1Class containerAnnotation = (Asn1Class) containerClass.getAnnotation(Asn1Class.class);
        if (containerAnnotation == null) {
            throw new Asn1EncodingException(containerClass.getName() + " not annotated with " + Asn1Class.class.getName());
        }
        Asn1Type containerType = containerAnnotation.type();
        switch (containerType) {
            case CHOICE:
                return toChoice(container);
            case SEQUENCE:
                return toSequence(container);
            default:
                throw new Asn1EncodingException("Unsupported container type: " + containerType);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static byte[] toChoice(Object container) throws Asn1EncodingException {
        Class<?> containerClass = container.getClass();
        List<AnnotatedField> fields = getAnnotatedFields(container);
        if (fields.isEmpty()) {
            throw new Asn1EncodingException("No fields annotated with " + Asn1Field.class.getName() + " in CHOICE class " + containerClass.getName());
        }
        AnnotatedField resultField = null;
        for (AnnotatedField field : fields) {
            Object fieldValue = getMemberFieldValue(container, field.getField());
            if (fieldValue != null) {
                if (resultField != null) {
                    throw new Asn1EncodingException("Multiple non-null fields in CHOICE class " + containerClass.getName() + ": " + resultField.getField().getName() + ", " + field.getField().getName());
                }
                resultField = field;
            }
        }
        if (resultField == null) {
            throw new Asn1EncodingException("No non-null fields in CHOICE class " + containerClass.getName());
        }
        return resultField.toDer();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static byte[] toSequence(Object container) throws Asn1EncodingException {
        Class<?> containerClass = container.getClass();
        List<AnnotatedField> fields = getAnnotatedFields(container);
        Collections.sort(fields, new Comparator<AnnotatedField>() { // from class: net.dongliu.apk.parser.cert.asn1.Asn1DerEncoder.1
            @Override // java.util.Comparator
            public int compare(AnnotatedField f1, AnnotatedField f2) {
                return f1.getAnnotation().index() - f2.getAnnotation().index();
            }
        });
        if (fields.size() > 1) {
            AnnotatedField lastField = null;
            for (AnnotatedField field : fields) {
                if (lastField != null && lastField.getAnnotation().index() == field.getAnnotation().index()) {
                    throw new Asn1EncodingException("Fields have the same index: " + containerClass.getName() + "." + lastField.getField().getName() + " and ." + field.getField().getName());
                }
                lastField = field;
            }
        }
        List<byte[]> serializedFields = new ArrayList<>(fields.size());
        for (AnnotatedField field2 : fields) {
            try {
                byte[] serializedField = field2.toDer();
                if (serializedField != null) {
                    serializedFields.add(serializedField);
                }
            } catch (Asn1EncodingException e) {
                throw new Asn1EncodingException("Failed to encode " + containerClass.getName() + "." + field2.getField().getName(), e);
            }
        }
        return createTag(0, true, 16, (byte[][]) serializedFields.toArray((Object[]) new byte[0]));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static byte[] toSetOf(Collection<?> values, Asn1Type elementType) throws Asn1EncodingException {
        List<byte[]> serializedValues = new ArrayList<>(values.size());
        for (Object value : values) {
            serializedValues.add(JavaToDerConverter.toDer(value, elementType, null));
        }
        if (serializedValues.size() > 1) {
            Collections.sort(serializedValues, ByteArrayLexicographicComparator.INSTANCE);
        }
        return createTag(0, true, 17, (byte[][]) serializedValues.toArray((Object[]) new byte[0]));
    }

    /* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/cert/asn1/Asn1DerEncoder$ByteArrayLexicographicComparator.class */
    private static class ByteArrayLexicographicComparator implements Comparator<byte[]> {
        private static final ByteArrayLexicographicComparator INSTANCE = new ByteArrayLexicographicComparator();

        private ByteArrayLexicographicComparator() {
        }

        @Override // java.util.Comparator
        public int compare(byte[] arr1, byte[] arr2) {
            int commonLength = Math.min(arr1.length, arr2.length);
            for (int i = 0; i < commonLength; i++) {
                int diff = (arr1[i] & 255) - (arr2[i] & 255);
                if (diff != 0) {
                    return diff;
                }
            }
            return arr1.length - arr2.length;
        }
    }

    private static List<AnnotatedField> getAnnotatedFields(Object container) throws Asn1EncodingException {
        Class<?> containerClass = container.getClass();
        Field[] declaredFields = containerClass.getDeclaredFields();
        List<AnnotatedField> result = new ArrayList<>(declaredFields.length);
        for (Field field : declaredFields) {
            Asn1Field annotation = (Asn1Field) field.getAnnotation(Asn1Field.class);
            if (annotation != null) {
                if (Modifier.isStatic(field.getModifiers())) {
                    throw new Asn1EncodingException(Asn1Field.class.getName() + " used on a static field: " + containerClass.getName() + "." + field.getName());
                }
                try {
                    AnnotatedField annotatedField = new AnnotatedField(container, field, annotation);
                    result.add(annotatedField);
                } catch (Asn1EncodingException e) {
                    throw new Asn1EncodingException("Invalid ASN.1 annotation on " + containerClass.getName() + "." + field.getName(), e);
                }
            }
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static byte[] toInteger(int value) {
        return toInteger(value);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static byte[] toInteger(long value) {
        return toInteger(BigInteger.valueOf(value));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Type inference failed for: r3v1, types: [byte[], byte[][]] */
    public static byte[] toInteger(BigInteger value) {
        return createTag(0, false, 2, new byte[]{value.toByteArray()});
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Type inference failed for: r3v15, types: [byte[], byte[][]] */
    public static byte[] toOid(String oid) throws Asn1EncodingException, NumberFormatException {
        ByteArrayOutputStream encodedValue = new ByteArrayOutputStream();
        String[] nodes = oid.split("\\.");
        if (nodes.length < 2) {
            throw new Asn1EncodingException("OBJECT IDENTIFIER must contain at least two nodes: " + oid);
        }
        try {
            int firstNode = Integer.parseInt(nodes[0]);
            if (firstNode > 6 || firstNode < 0) {
                throw new Asn1EncodingException("Invalid value for node #1: " + firstNode);
            }
            try {
                int secondNode = Integer.parseInt(nodes[1]);
                if (secondNode >= 40 || secondNode < 0) {
                    throw new Asn1EncodingException("Invalid value for node #2: " + secondNode);
                }
                int firstByte = (firstNode * 40) + secondNode;
                if (firstByte > 255) {
                    throw new Asn1EncodingException("First two nodes out of range: " + firstNode + "." + secondNode);
                }
                encodedValue.write(firstByte);
                for (int i = 2; i < nodes.length; i++) {
                    String nodeString = nodes[i];
                    try {
                        int node = Integer.parseInt(nodeString);
                        if (node < 0) {
                            throw new Asn1EncodingException("Invalid value for node #" + (i + 1) + ": " + node);
                        }
                        if (node <= 127) {
                            encodedValue.write(node);
                        } else if (node < 16384) {
                            encodedValue.write(128 | (node >> 7));
                            encodedValue.write(node & 127);
                        } else if (node < 2097152) {
                            encodedValue.write(128 | (node >> 14));
                            encodedValue.write(128 | ((node >> 7) & 127));
                            encodedValue.write(node & 127);
                        } else {
                            throw new Asn1EncodingException("Node #" + (i + 1) + " too large: " + node);
                        }
                    } catch (NumberFormatException e) {
                        throw new Asn1EncodingException("Node #" + (i + 1) + " not numeric: " + nodeString);
                    }
                }
                return createTag(0, false, 6, new byte[]{encodedValue.toByteArray()});
            } catch (NumberFormatException e2) {
                throw new Asn1EncodingException("Node #2 not numeric: " + nodes[1]);
            }
        } catch (NumberFormatException e3) {
            throw new Asn1EncodingException("Node #1 not numeric: " + nodes[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Object getMemberFieldValue(Object obj, Field field) throws Asn1EncodingException {
        try {
            return field.get(obj);
        } catch (ReflectiveOperationException e) {
            throw new Asn1EncodingException("Failed to read " + obj.getClass().getName() + "." + field.getName(), e);
        }
    }

    /* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/cert/asn1/Asn1DerEncoder$AnnotatedField.class */
    private static final class AnnotatedField {
        private final Field mField;
        private final Object mObject;
        private final Asn1Field mAnnotation;
        private final Asn1Type mDataType;
        private final Asn1Type mElementDataType;
        private final Asn1TagClass mTagClass;
        private final int mDerTagClass;
        private final int mDerTagNumber;
        private final Asn1Tagging mTagging;
        private final boolean mOptional;

        public AnnotatedField(Object obj, Field field, Asn1Field annotation) throws Asn1EncodingException {
            int tagNumber;
            this.mObject = obj;
            this.mField = field;
            this.mAnnotation = annotation;
            this.mDataType = annotation.type();
            this.mElementDataType = annotation.elementType();
            Asn1TagClass tagClass = annotation.cls();
            if (tagClass == Asn1TagClass.AUTOMATIC) {
                if (annotation.tagNumber() != -1) {
                    tagClass = Asn1TagClass.CONTEXT_SPECIFIC;
                } else {
                    tagClass = Asn1TagClass.UNIVERSAL;
                }
            }
            this.mTagClass = tagClass;
            this.mDerTagClass = BerEncoding.getTagClass(this.mTagClass);
            if (annotation.tagNumber() != -1) {
                tagNumber = annotation.tagNumber();
            } else if (this.mDataType == Asn1Type.CHOICE || this.mDataType == Asn1Type.ANY) {
                tagNumber = -1;
            } else {
                tagNumber = BerEncoding.getTagNumber(this.mDataType);
            }
            this.mDerTagNumber = tagNumber;
            this.mTagging = annotation.tagging();
            if ((this.mTagging == Asn1Tagging.EXPLICIT || this.mTagging == Asn1Tagging.IMPLICIT) && annotation.tagNumber() == -1) {
                throw new Asn1EncodingException("Tag number must be specified when tagging mode is " + this.mTagging);
            }
            this.mOptional = annotation.optional();
        }

        public Field getField() {
            return this.mField;
        }

        public Asn1Field getAnnotation() {
            return this.mAnnotation;
        }

        /* JADX WARN: Type inference failed for: r3v15, types: [byte[], byte[][]] */
        public byte[] toDer() throws Asn1EncodingException {
            Object fieldValue = Asn1DerEncoder.getMemberFieldValue(this.mObject, this.mField);
            if (fieldValue == null) {
                if (this.mOptional) {
                    return null;
                }
                throw new Asn1EncodingException("Required field not set");
            }
            byte[] encoded = JavaToDerConverter.toDer(fieldValue, this.mDataType, this.mElementDataType);
            switch (this.mTagging) {
                case NORMAL:
                    return encoded;
                case EXPLICIT:
                    return Asn1DerEncoder.createTag(this.mDerTagClass, true, this.mDerTagNumber, new byte[]{encoded});
                case IMPLICIT:
                    int originalTagNumber = BerEncoding.getTagNumber(encoded[0]);
                    if (originalTagNumber == 31) {
                        throw new Asn1EncodingException("High-tag-number form not supported");
                    }
                    if (this.mDerTagNumber >= 31) {
                        throw new Asn1EncodingException("Unsupported high tag number: " + this.mDerTagNumber);
                    }
                    encoded[0] = BerEncoding.setTagNumber(encoded[0], this.mDerTagNumber);
                    encoded[0] = BerEncoding.setTagClass(encoded[0], this.mDerTagClass);
                    return encoded;
                default:
                    throw new RuntimeException("Unknown tagging mode: " + this.mTagging);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static byte[] createTag(int tagClass, boolean constructed, int tagNumber, byte[]... contents) {
        int contentsPosInResult;
        byte[] result;
        if (tagNumber >= 31) {
            throw new IllegalArgumentException("High tag numbers not supported: " + tagNumber);
        }
        byte firstIdentifierByte = (byte) ((tagClass << 6) | (constructed ? 32 : 0) | tagNumber);
        int contentsLength = 0;
        for (byte[] bArr : contents) {
            contentsLength += bArr.length;
        }
        if (contentsLength < 128) {
            contentsPosInResult = 2;
            result = new byte[2 + contentsLength];
            result[0] = firstIdentifierByte;
            result[1] = (byte) contentsLength;
        } else {
            if (contentsLength <= 255) {
                contentsPosInResult = 3;
                result = new byte[3 + contentsLength];
                result[1] = -127;
                result[2] = (byte) contentsLength;
            } else if (contentsLength <= 65535) {
                contentsPosInResult = 4;
                result = new byte[4 + contentsLength];
                result[1] = -126;
                result[2] = (byte) (contentsLength >> 8);
                result[3] = (byte) (contentsLength & 255);
            } else if (contentsLength <= 16777215) {
                contentsPosInResult = 5;
                result = new byte[5 + contentsLength];
                result[1] = -125;
                result[2] = (byte) (contentsLength >> 16);
                result[3] = (byte) ((contentsLength >> 8) & 255);
                result[4] = (byte) (contentsLength & 255);
            } else {
                contentsPosInResult = 6;
                result = new byte[6 + contentsLength];
                result[1] = -124;
                result[2] = (byte) (contentsLength >> 24);
                result[3] = (byte) ((contentsLength >> 16) & 255);
                result[4] = (byte) ((contentsLength >> 8) & 255);
                result[5] = (byte) (contentsLength & 255);
            }
            result[0] = firstIdentifierByte;
        }
        for (byte[] c : contents) {
            System.arraycopy(c, 0, result, contentsPosInResult, c.length);
            contentsPosInResult += c.length;
        }
        return result;
    }

    /* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/cert/asn1/Asn1DerEncoder$JavaToDerConverter.class */
    private static final class JavaToDerConverter {
        private JavaToDerConverter() {
        }

        /* JADX WARN: Type inference failed for: r3v7, types: [byte[], byte[][]] */
        public static byte[] toDer(Object source, Asn1Type targetType, Asn1Type targetElementType) throws Asn1EncodingException {
            Class<?> sourceType = source.getClass();
            if (Asn1OpaqueObject.class.equals(sourceType)) {
                ByteBuffer buf = ((Asn1OpaqueObject) source).getEncoded();
                byte[] result = new byte[buf.remaining()];
                buf.get(result);
                return result;
            }
            if (targetType == null || targetType == Asn1Type.ANY) {
                return Asn1DerEncoder.encode(source);
            }
            switch (targetType) {
                case CHOICE:
                    Asn1Class containerAnnotation = (Asn1Class) sourceType.getAnnotation(Asn1Class.class);
                    if (containerAnnotation != null && containerAnnotation.type() == Asn1Type.CHOICE) {
                        return Asn1DerEncoder.toChoice(source);
                    }
                    break;
                case SEQUENCE:
                    Asn1Class containerAnnotation2 = (Asn1Class) sourceType.getAnnotation(Asn1Class.class);
                    if (containerAnnotation2 != null && containerAnnotation2.type() == Asn1Type.SEQUENCE) {
                        return Asn1DerEncoder.toSequence(source);
                    }
                    break;
                case OCTET_STRING:
                    byte[] value = null;
                    if (source instanceof ByteBuffer) {
                        ByteBuffer buf2 = (ByteBuffer) source;
                        value = new byte[buf2.remaining()];
                        buf2.slice().get(value);
                    } else if (source instanceof byte[]) {
                        value = (byte[]) source;
                    }
                    if (value != null) {
                        return Asn1DerEncoder.createTag(0, false, 4, new byte[]{value});
                    }
                    break;
                case INTEGER:
                    if (source instanceof Integer) {
                        return Asn1DerEncoder.toInteger(((Integer) source).intValue());
                    }
                    if (source instanceof Long) {
                        return Asn1DerEncoder.toInteger(((Long) source).longValue());
                    }
                    if (source instanceof BigInteger) {
                        return Asn1DerEncoder.toInteger((BigInteger) source);
                    }
                    break;
                case OBJECT_IDENTIFIER:
                    if (source instanceof String) {
                        return Asn1DerEncoder.toOid((String) source);
                    }
                    break;
                case SET_OF:
                    return Asn1DerEncoder.toSetOf((Collection) source, targetElementType);
            }
            throw new Asn1EncodingException("Unsupported conversion: " + sourceType.getName() + " to ASN.1 " + targetType);
        }
    }
}
