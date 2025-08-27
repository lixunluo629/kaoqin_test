package net.coobird.thumbnailator.util.exif;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/util/exif/Orientation.class */
public enum Orientation {
    TOP_LEFT(1),
    TOP_RIGHT(2),
    BOTTOM_RIGHT(3),
    BOTTOM_LEFT(4),
    LEFT_TOP(5),
    RIGHT_TOP(6),
    RIGHT_BOTTOM(7),
    LEFT_BOTTOM(8);

    private int value;

    Orientation(int i) {
        this.value = i;
    }

    public static Orientation typeOf(int i) {
        for (Orientation orientation : values()) {
            if (orientation.value == i) {
                return orientation;
            }
        }
        return null;
    }

    @Override // java.lang.Enum
    public String toString() {
        return "Orientation [type=" + this.value + "]";
    }
}
