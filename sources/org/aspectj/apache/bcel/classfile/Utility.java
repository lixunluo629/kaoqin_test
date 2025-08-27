package org.aspectj.apache.bcel.classfile;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.ibatis.javassist.bytecode.AnnotationsAttribute;
import org.apache.ibatis.javassist.bytecode.ParameterAnnotationsAttribute;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;
import org.aspectj.apache.bcel.Constants;
import org.aspectj.apache.bcel.classfile.annotation.AnnotationGen;
import org.aspectj.apache.bcel.classfile.annotation.RuntimeAnnos;
import org.aspectj.apache.bcel.classfile.annotation.RuntimeInvisAnnos;
import org.aspectj.apache.bcel.classfile.annotation.RuntimeInvisParamAnnos;
import org.aspectj.apache.bcel.classfile.annotation.RuntimeVisAnnos;
import org.aspectj.apache.bcel.classfile.annotation.RuntimeVisParamAnnos;
import org.aspectj.apache.bcel.generic.Type;
import org.aspectj.apache.bcel.util.ByteSequence;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/Utility.class */
public abstract class Utility {
    private static boolean wide = false;

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/Utility$ResultHolder.class */
    public static class ResultHolder {
        private String result;
        private int consumed;
        public static final ResultHolder BYTE = new ResultHolder("byte", 1);
        public static final ResultHolder CHAR = new ResultHolder("char", 1);
        public static final ResultHolder DOUBLE = new ResultHolder(XmlErrorCodes.DOUBLE, 1);
        public static final ResultHolder FLOAT = new ResultHolder(XmlErrorCodes.FLOAT, 1);
        public static final ResultHolder INT = new ResultHolder(XmlErrorCodes.INT, 1);
        public static final ResultHolder LONG = new ResultHolder(XmlErrorCodes.LONG, 1);
        public static final ResultHolder SHORT = new ResultHolder("short", 1);
        public static final ResultHolder BOOLEAN = new ResultHolder("boolean", 1);
        public static final ResultHolder VOID = new ResultHolder("void", 1);

        public ResultHolder(String str, int i) {
            this.result = str;
            this.consumed = i;
        }

        public String getResult() {
            return this.result;
        }

        public int getConsumedChars() {
            return this.consumed;
        }
    }

    public static final String accessToString(int i) {
        return accessToString(i, false);
    }

    public static final String accessToString(int i, boolean z) {
        StringBuffer stringBuffer = new StringBuffer();
        int iPow2 = 0;
        int i2 = 0;
        while (iPow2 < 2048) {
            iPow2 = pow2(i2);
            if ((i & iPow2) != 0 && (!z || (iPow2 != 32 && iPow2 != 512))) {
                stringBuffer.append(Constants.ACCESS_NAMES[i2]).append(SymbolConstants.SPACE_SYMBOL);
            }
            i2++;
        }
        return stringBuffer.toString().trim();
    }

    public static final String classOrInterface(int i) {
        return (i & 512) != 0 ? JamXmlElements.INTERFACE : "class";
    }

    public static final String codeToString(byte[] bArr, ConstantPool constantPool, int i, int i2, boolean z) {
        StringBuffer stringBuffer = new StringBuffer(bArr.length * 20);
        ByteSequence byteSequence = new ByteSequence(bArr);
        for (int i3 = 0; i3 < i; i3++) {
            try {
                codeToString(byteSequence, constantPool, z);
            } catch (IOException e) {
                System.out.println(stringBuffer.toString());
                e.printStackTrace();
                throw new ClassFormatException("Byte code error: " + e);
            }
        }
        int i4 = 0;
        while (byteSequence.available() > 0) {
            if (i2 < 0 || i4 < i2) {
                stringBuffer.append(fillup(byteSequence.getIndex() + ":", 6, true, ' ') + codeToString(byteSequence, constantPool, z) + '\n');
            }
            i4++;
        }
        return stringBuffer.toString();
    }

    public static final String codeToString(byte[] bArr, ConstantPool constantPool, int i, int i2) {
        return codeToString(bArr, constantPool, i, i2, true);
    }

    public static final String codeToString(ByteSequence byteSequence, ConstantPool constantPool) throws IOException {
        return codeToString(byteSequence, constantPool, true);
    }

    public static final String compactClassName(String str) {
        return compactClassName(str, true);
    }

    public static final String compactClassName(String str, String str2, boolean z) {
        String strReplace = str.replace('/', '.');
        if (z) {
            int length = str2.length();
            if (strReplace.startsWith(str2)) {
                String strSubstring = strReplace.substring(length);
                if (strSubstring.indexOf(46) == -1) {
                    strReplace = strSubstring;
                }
            }
        }
        return strReplace;
    }

    public static final String compactClassName(String str, boolean z) {
        return compactClassName(str, "java.lang.", z);
    }

    public static final String methodSignatureToString(String str, String str2, String str3) {
        return methodSignatureToString(str, str2, str3, true);
    }

    public static final String methodSignatureToString(String str, String str2, String str3, boolean z) {
        return methodSignatureToString(str, str2, str3, z, null);
    }

    public static final String methodSignatureToString(String str, String str2, String str3, boolean z, LocalVariableTable localVariableTable) throws ClassFormatException {
        StringBuffer stringBuffer = new StringBuffer("(");
        int i = str3.indexOf("static") >= 0 ? 0 : 1;
        try {
            if (str.charAt(0) != '(') {
                throw new ClassFormatException("Invalid method signature: " + str);
            }
            int consumedChars = 1;
            while (str.charAt(consumedChars) != ')') {
                ResultHolder resultHolderSignatureToStringInternal = signatureToStringInternal(str.substring(consumedChars), z);
                String result = resultHolderSignatureToStringInternal.getResult();
                stringBuffer.append(result);
                if (localVariableTable != null) {
                    LocalVariable localVariable = localVariableTable.getLocalVariable(i);
                    if (localVariable != null) {
                        stringBuffer.append(SymbolConstants.SPACE_SYMBOL + localVariable.getName());
                    }
                } else {
                    stringBuffer.append(" arg" + i);
                }
                i = (XmlErrorCodes.DOUBLE.equals(result) || XmlErrorCodes.LONG.equals(result)) ? i + 2 : i + 1;
                stringBuffer.append(", ");
                consumedChars += resultHolderSignatureToStringInternal.getConsumedChars();
            }
            String strSignatureToString = signatureToString(str.substring(consumedChars + 1), z);
            if (stringBuffer.length() > 1) {
                stringBuffer.setLength(stringBuffer.length() - 2);
            }
            stringBuffer.append(")");
            return str3 + (str3.length() > 0 ? SymbolConstants.SPACE_SYMBOL : "") + strSignatureToString + SymbolConstants.SPACE_SYMBOL + str2 + stringBuffer.toString();
        } catch (StringIndexOutOfBoundsException e) {
            throw new ClassFormatException("Invalid method signature: " + str);
        }
    }

    public static final String replace(String str, String str2, String str3) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            if (str.indexOf(str2) != -1) {
                int length = 0;
                while (true) {
                    int iIndexOf = str.indexOf(str2, length);
                    if (iIndexOf == -1) {
                        break;
                    }
                    stringBuffer.append(str.substring(length, iIndexOf));
                    stringBuffer.append(str3);
                    length = iIndexOf + str2.length();
                }
                stringBuffer.append(str.substring(length));
                str = stringBuffer.toString();
            }
        } catch (StringIndexOutOfBoundsException e) {
            System.err.println(e);
        }
        return str;
    }

    public static final String signatureToString(String str) {
        return signatureToString(str, true);
    }

    public static final String signatureToString(String str, boolean z) {
        return signatureToStringInternal(str, z).getResult();
    }

    public static final ResultHolder signatureToStringInternal(String str, boolean z) {
        try {
            switch (str.charAt(0)) {
                case 'B':
                    return ResultHolder.BYTE;
                case 'C':
                    return ResultHolder.CHAR;
                case 'D':
                    return ResultHolder.DOUBLE;
                case 'E':
                case 'G':
                case 'H':
                case 'K':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R':
                case 'T':
                case 'U':
                case 'W':
                case 'X':
                case 'Y':
                default:
                    throw new ClassFormatException("Invalid signature: `" + str + "'");
                case 'F':
                    return ResultHolder.FLOAT;
                case 'I':
                    return ResultHolder.INT;
                case 'J':
                    return ResultHolder.LONG;
                case 'L':
                    int iIndexOf = str.indexOf(59);
                    if (iIndexOf < 0) {
                        throw new ClassFormatException("Invalid signature: " + str);
                    }
                    if (str.length() > iIndexOf + 1 && str.charAt(iIndexOf + 1) == '>') {
                        iIndexOf += 2;
                    }
                    int iIndexOf2 = str.indexOf(60);
                    if (iIndexOf2 == -1) {
                        return new ResultHolder(compactClassName(str.substring(1, iIndexOf), z), iIndexOf + 1);
                    }
                    int iIndexOf3 = str.indexOf(62);
                    ResultHolder resultHolderSignatureToStringInternal = signatureToStringInternal(str.substring(iIndexOf2 + 1, iIndexOf3), z);
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append(str.substring(1, iIndexOf2));
                    stringBuffer.append("<").append(resultHolderSignatureToStringInternal.getResult()).append(">");
                    return new ResultHolder(compactClassName(stringBuffer.toString(), z), iIndexOf3 + 1);
                case 'S':
                    return ResultHolder.SHORT;
                case 'V':
                    return ResultHolder.VOID;
                case 'Z':
                    return ResultHolder.BOOLEAN;
                case '[':
                    StringBuffer stringBuffer2 = new StringBuffer();
                    int i = 0;
                    while (str.charAt(i) == '[') {
                        stringBuffer2.append("[]");
                        i++;
                    }
                    ResultHolder resultHolderSignatureToStringInternal2 = signatureToStringInternal(str.substring(i), z);
                    int consumedChars = i + resultHolderSignatureToStringInternal2.getConsumedChars();
                    stringBuffer2.insert(0, resultHolderSignatureToStringInternal2.getResult());
                    return new ResultHolder(stringBuffer2.toString(), consumedChars);
            }
        } catch (StringIndexOutOfBoundsException e) {
            throw new ClassFormatException("Invalid signature: " + e + ":" + str);
        }
    }

    public static final byte typeOfMethodSignature(String str) throws ClassFormatException {
        try {
            if (str.charAt(0) != '(') {
                throw new ClassFormatException("Invalid method signature: " + str);
            }
            return typeOfSignature(str.substring(str.lastIndexOf(41) + 1));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ClassFormatException("Invalid method signature: " + str);
        }
    }

    private static final short byteToShort(byte b) {
        return b < 0 ? (short) (256 + b) : b;
    }

    public static final String toHexString(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < bArr.length; i++) {
            short sByteToShort = byteToShort(bArr[i]);
            String string = Integer.toString(sByteToShort, 16);
            if (sByteToShort < 16) {
                stringBuffer.append('0');
            }
            stringBuffer.append(string);
            if (i < bArr.length - 1) {
                stringBuffer.append(' ');
            }
        }
        return stringBuffer.toString();
    }

    public static final String format(int i, int i2, boolean z, char c) {
        return fillup(Integer.toString(i), i2, z, c);
    }

    public static final String fillup(String str, int i, boolean z, char c) {
        int length = i - str.length();
        char[] cArr = new char[length < 0 ? 0 : length];
        for (int i2 = 0; i2 < cArr.length; i2++) {
            cArr[i2] = c;
        }
        return z ? str + new String(cArr) : new String(cArr) + str;
    }

    public static final String convertString(String str) {
        char[] charArray = str.toCharArray();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < charArray.length; i++) {
            switch (charArray[i]) {
                case '\n':
                    stringBuffer.append("\\n");
                    break;
                case '\r':
                    stringBuffer.append("\\r");
                    break;
                case '\"':
                    stringBuffer.append("\\\"");
                    break;
                case '\'':
                    stringBuffer.append("\\'");
                    break;
                case '\\':
                    stringBuffer.append("\\\\");
                    break;
                default:
                    stringBuffer.append(charArray[i]);
                    break;
            }
        }
        return stringBuffer.toString();
    }

    public static Collection<RuntimeAnnos> getAnnotationAttributes(ConstantPool constantPool, List<AnnotationGen> list) throws IOException {
        if (list.size() == 0) {
            return null;
        }
        try {
            int i = 0;
            int i2 = 0;
            Iterator<AnnotationGen> it = list.iterator();
            while (it.hasNext()) {
                if (it.next().isRuntimeVisible()) {
                    i++;
                } else {
                    i2++;
                }
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            DataOutputStream dataOutputStream2 = new DataOutputStream(byteArrayOutputStream2);
            dataOutputStream.writeShort(i);
            dataOutputStream2.writeShort(i2);
            for (AnnotationGen annotationGen : list) {
                if (annotationGen.isRuntimeVisible()) {
                    annotationGen.dump(dataOutputStream);
                } else {
                    annotationGen.dump(dataOutputStream2);
                }
            }
            dataOutputStream.close();
            dataOutputStream2.close();
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            byte[] byteArray2 = byteArrayOutputStream2.toByteArray();
            int iAddUtf8 = byteArray.length > 2 ? constantPool.addUtf8(AnnotationsAttribute.visibleTag) : -1;
            int iAddUtf82 = byteArray2.length > 2 ? constantPool.addUtf8(AnnotationsAttribute.invisibleTag) : -1;
            ArrayList arrayList = new ArrayList();
            if (byteArray.length > 2) {
                arrayList.add(new RuntimeVisAnnos(iAddUtf8, byteArray.length, byteArray, constantPool));
            }
            if (byteArray2.length > 2) {
                arrayList.add(new RuntimeInvisAnnos(iAddUtf82, byteArray2.length, byteArray2, constantPool));
            }
            return arrayList;
        } catch (IOException e) {
            System.err.println("IOException whilst processing annotations");
            e.printStackTrace();
            return null;
        }
    }

    public static Attribute[] getParameterAnnotationAttributes(ConstantPool constantPool, List<AnnotationGen>[] listArr) throws IOException {
        int[] iArr = new int[listArr.length];
        int i = 0;
        int[] iArr2 = new int[listArr.length];
        int i2 = 0;
        for (int i3 = 0; i3 < listArr.length; i3++) {
            try {
                List<AnnotationGen> list = listArr[i3];
                if (list != null) {
                    Iterator<AnnotationGen> it = list.iterator();
                    while (it.hasNext()) {
                        if (it.next().isRuntimeVisible()) {
                            int i4 = i3;
                            iArr[i4] = iArr[i4] + 1;
                            i++;
                        } else {
                            int i5 = i3;
                            iArr2[i5] = iArr2[i5] + 1;
                            i2++;
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("IOException whilst processing parameter annotations");
                e.printStackTrace();
                return null;
            }
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        dataOutputStream.writeByte(listArr.length);
        for (int i6 = 0; i6 < listArr.length; i6++) {
            dataOutputStream.writeShort(iArr[i6]);
            if (iArr[i6] > 0) {
                for (AnnotationGen annotationGen : listArr[i6]) {
                    if (annotationGen.isRuntimeVisible()) {
                        annotationGen.dump(dataOutputStream);
                    }
                }
            }
        }
        dataOutputStream.close();
        ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream2 = new DataOutputStream(byteArrayOutputStream2);
        dataOutputStream2.writeByte(listArr.length);
        for (int i7 = 0; i7 < listArr.length; i7++) {
            dataOutputStream2.writeShort(iArr2[i7]);
            if (iArr2[i7] > 0) {
                for (AnnotationGen annotationGen2 : listArr[i7]) {
                    if (!annotationGen2.isRuntimeVisible()) {
                        annotationGen2.dump(dataOutputStream2);
                    }
                }
            }
        }
        dataOutputStream2.close();
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        byte[] byteArray2 = byteArrayOutputStream2.toByteArray();
        int iAddUtf8 = i > 0 ? constantPool.addUtf8(ParameterAnnotationsAttribute.visibleTag) : -1;
        int iAddUtf82 = i2 > 0 ? constantPool.addUtf8(ParameterAnnotationsAttribute.invisibleTag) : -1;
        ArrayList arrayList = new ArrayList();
        if (i > 0) {
            arrayList.add(new RuntimeVisParamAnnos(iAddUtf8, byteArray.length, byteArray, constantPool));
        }
        if (i2 > 0) {
            arrayList.add(new RuntimeInvisParamAnnos(iAddUtf82, byteArray2.length, byteArray2, constantPool));
        }
        return (Attribute[]) arrayList.toArray(new Attribute[0]);
    }

    public static final byte typeOfSignature(String str) throws ClassFormatException {
        try {
            switch (str.charAt(0)) {
                case 'B':
                    return (byte) 8;
                case 'C':
                    return (byte) 5;
                case 'D':
                    return (byte) 7;
                case 'E':
                case 'G':
                case 'H':
                case 'K':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R':
                case 'T':
                case 'U':
                case 'W':
                case 'X':
                case 'Y':
                default:
                    throw new ClassFormatException("Invalid method signature: " + str);
                case 'F':
                    return (byte) 6;
                case 'I':
                    return (byte) 10;
                case 'J':
                    return (byte) 11;
                case 'L':
                    return (byte) 14;
                case 'S':
                    return (byte) 9;
                case 'V':
                    return (byte) 12;
                case 'Z':
                    return (byte) 4;
                case '[':
                    return (byte) 13;
            }
        } catch (StringIndexOutOfBoundsException e) {
            throw new ClassFormatException("Invalid method signature: " + str);
        }
        throw new ClassFormatException("Invalid method signature: " + str);
    }

    public static final byte typeOfSignature(char c) throws ClassFormatException {
        switch (c) {
            case 'B':
                return (byte) 8;
            case 'C':
                return (byte) 5;
            case 'D':
                return (byte) 7;
            case 'E':
            case 'G':
            case 'H':
            case 'K':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'T':
            case 'U':
            case 'W':
            case 'X':
            case 'Y':
            default:
                throw new ClassFormatException("Invalid type of signature: " + c);
            case 'F':
                return (byte) 6;
            case 'I':
                return (byte) 10;
            case 'J':
                return (byte) 11;
            case 'L':
                return (byte) 14;
            case 'S':
                return (byte) 9;
            case 'V':
                return (byte) 12;
            case 'Z':
                return (byte) 4;
            case '[':
                return (byte) 13;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0611  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x062d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static final java.lang.String codeToString(org.aspectj.apache.bcel.util.ByteSequence r6, org.aspectj.apache.bcel.classfile.ConstantPool r7, boolean r8) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 2395
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.aspectj.apache.bcel.classfile.Utility.codeToString(org.aspectj.apache.bcel.util.ByteSequence, org.aspectj.apache.bcel.classfile.ConstantPool, boolean):java.lang.String");
    }

    private static final int pow2(int i) {
        return 1 << i;
    }

    public static String toMethodSignature(Type type, Type[] typeArr) {
        StringBuffer stringBuffer = new StringBuffer("(");
        int length = typeArr == null ? 0 : typeArr.length;
        for (int i = 0; i < length; i++) {
            stringBuffer.append(typeArr[i].getSignature());
        }
        stringBuffer.append(')');
        stringBuffer.append(type.getSignature());
        return stringBuffer.toString();
    }
}
