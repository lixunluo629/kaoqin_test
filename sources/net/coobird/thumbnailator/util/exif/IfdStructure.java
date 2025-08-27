package net.coobird.thumbnailator.util.exif;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/util/exif/IfdStructure.class */
public class IfdStructure {
    private final int tag;
    private final IfdType type;
    private final int count;
    private final int offsetValue;

    public IfdStructure(int i, int i2, int i3, int i4) {
        this.tag = i;
        this.type = IfdType.typeOf(i2);
        this.count = i3;
        this.offsetValue = i4;
    }

    public int getTag() {
        return this.tag;
    }

    public IfdType getType() {
        return this.type;
    }

    public int getCount() {
        return this.count;
    }

    public int getOffsetValue() {
        return this.offsetValue;
    }

    public boolean isValue() {
        return this.type.size() * this.count <= 4;
    }

    public boolean isOffset() {
        return !isValue();
    }

    public int hashCode() {
        return (31 * ((31 * ((31 * ((31 * 1) + this.count)) + this.offsetValue)) + this.tag)) + (this.type == null ? 0 : this.type.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        IfdStructure ifdStructure = (IfdStructure) obj;
        if (this.count != ifdStructure.count || this.offsetValue != ifdStructure.offsetValue || this.tag != ifdStructure.tag || this.type != ifdStructure.type) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "IfdStructure [tag=" + Integer.toHexString(this.tag) + ", type=" + this.type + ", count=" + this.count + ", offsetValue=" + this.offsetValue + "]";
    }
}
