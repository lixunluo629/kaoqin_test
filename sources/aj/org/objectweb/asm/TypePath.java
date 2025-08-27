package aj.org.objectweb.asm;

/* loaded from: aspectjweaver-1.8.14.jar:aj/org/objectweb/asm/TypePath.class */
public class TypePath {
    public static final int ARRAY_ELEMENT = 0;
    public static final int INNER_TYPE = 1;
    public static final int WILDCARD_BOUND = 2;
    public static final int TYPE_ARGUMENT = 3;
    byte[] a;
    int b;

    TypePath(byte[] bArr, int i) {
        this.a = bArr;
        this.b = i;
    }

    public int getLength() {
        return this.a[this.b];
    }

    public int getStep(int i) {
        return this.a[this.b + (2 * i) + 1];
    }

    public int getStepArgument(int i) {
        return this.a[this.b + (2 * i) + 2];
    }

    public static TypePath fromString(String str) {
        char cCharAt;
        if (str == null || str.length() == 0) {
            return null;
        }
        int length = str.length();
        ByteVector byteVector = new ByteVector(length);
        byteVector.putByte(0);
        int i = 0;
        while (i < length) {
            int i2 = i;
            i++;
            char cCharAt2 = str.charAt(i2);
            if (cCharAt2 == '[') {
                byteVector.a(0, 0);
            } else if (cCharAt2 == '.') {
                byteVector.a(1, 0);
            } else if (cCharAt2 == '*') {
                byteVector.a(2, 0);
            } else if (cCharAt2 >= '0' && cCharAt2 <= '9') {
                int i3 = cCharAt2 - '0';
                while (i < length && (cCharAt = str.charAt(i)) >= '0' && cCharAt <= '9') {
                    i3 = ((i3 * 10) + cCharAt) - 48;
                    i++;
                }
                if (i < length && str.charAt(i) == ';') {
                    i++;
                }
                byteVector.a(3, i3);
            }
        }
        byteVector.a[0] = (byte) (byteVector.b / 2);
        return new TypePath(byteVector.a, 0);
    }

    public String toString() {
        int length = getLength();
        StringBuffer stringBuffer = new StringBuffer(length * 2);
        for (int i = 0; i < length; i++) {
            switch (getStep(i)) {
                case 0:
                    stringBuffer.append('[');
                    break;
                case 1:
                    stringBuffer.append('.');
                    break;
                case 2:
                    stringBuffer.append('*');
                    break;
                case 3:
                    stringBuffer.append(getStepArgument(i)).append(';');
                    break;
                default:
                    stringBuffer.append('_');
                    break;
            }
        }
        return stringBuffer.toString();
    }
}
