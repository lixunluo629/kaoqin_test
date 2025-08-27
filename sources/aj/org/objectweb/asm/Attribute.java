package aj.org.objectweb.asm;

/* loaded from: aspectjweaver-1.8.14.jar:aj/org/objectweb/asm/Attribute.class */
public class Attribute {
    public final String type;
    byte[] b;
    Attribute a;

    protected Attribute(String str) {
        this.type = str;
    }

    public boolean isUnknown() {
        return true;
    }

    public boolean isCodeAttribute() {
        return false;
    }

    protected Label[] getLabels() {
        return null;
    }

    protected Attribute read(ClassReader classReader, int i, int i2, char[] cArr, int i3, Label[] labelArr) {
        Attribute attribute = new Attribute(this.type);
        attribute.b = new byte[i2];
        System.arraycopy(classReader.b, i, attribute.b, 0, i2);
        return attribute;
    }

    protected ByteVector write(ClassWriter classWriter, byte[] bArr, int i, int i2, int i3) {
        ByteVector byteVector = new ByteVector();
        byteVector.a = this.b;
        byteVector.b = this.b.length;
        return byteVector;
    }

    final int a() {
        int i = 0;
        Attribute attribute = this;
        while (true) {
            Attribute attribute2 = attribute;
            if (attribute2 == null) {
                return i;
            }
            i++;
            attribute = attribute2.a;
        }
    }

    final int a(ClassWriter classWriter, byte[] bArr, int i, int i2, int i3) {
        int i4 = 0;
        for (Attribute attribute = this; attribute != null; attribute = attribute.a) {
            classWriter.newUTF8(attribute.type);
            i4 += attribute.write(classWriter, bArr, i, i2, i3).b + 6;
        }
        return i4;
    }

    final void a(ClassWriter classWriter, byte[] bArr, int i, int i2, int i3, ByteVector byteVector) {
        Attribute attribute = this;
        while (true) {
            Attribute attribute2 = attribute;
            if (attribute2 == null) {
                return;
            }
            ByteVector byteVectorWrite = attribute2.write(classWriter, bArr, i, i2, i3);
            byteVector.putShort(classWriter.newUTF8(attribute2.type)).putInt(byteVectorWrite.b);
            byteVector.putByteArray(byteVectorWrite.a, 0, byteVectorWrite.b);
            attribute = attribute2.a;
        }
    }
}
