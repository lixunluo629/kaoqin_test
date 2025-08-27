package net.coobird.thumbnailator.util.exif;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/util/exif/IfdType.class */
public enum IfdType {
    BYTE(1, 1),
    ASCII(2, 1),
    SHORT(3, 2),
    LONG(4, 4),
    RATIONAL(5, LONG.size() * 2),
    UNDEFINED(7, 1),
    SLONG(9, 4),
    SRATIONAL(5, SLONG.size() * 2);

    private int value;
    private int size;

    IfdType(int i, int i2) {
        this.value = i;
        this.size = i2;
    }

    public int size() {
        return this.size;
    }

    public int value() {
        return this.value;
    }

    public static IfdType typeOf(int i) {
        for (IfdType ifdType : values()) {
            if (ifdType.value == i) {
                return ifdType;
            }
        }
        return null;
    }

    @Override // java.lang.Enum
    public String toString() {
        return "IfdType [type=" + name() + ", value=" + this.value + ", size=" + this.size + "]";
    }
}
