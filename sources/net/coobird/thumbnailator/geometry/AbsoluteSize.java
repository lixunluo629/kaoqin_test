package net.coobird.thumbnailator.geometry;

import java.awt.Dimension;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/geometry/AbsoluteSize.class */
public class AbsoluteSize implements Size {
    private final Dimension size;

    public AbsoluteSize(Dimension dimension) {
        if (dimension == null) {
            throw new NullPointerException("Size cannot be null.");
        }
        this.size = new Dimension(dimension);
    }

    public AbsoluteSize(int i, int i2) {
        if (i <= 0 || i2 <= 0) {
            throw new IllegalArgumentException("Width and height must be greater than 0.");
        }
        this.size = new Dimension(i, i2);
    }

    @Override // net.coobird.thumbnailator.geometry.Size
    public Dimension calculate(int i, int i2) {
        if (i <= 0 || i2 <= 0) {
            throw new IllegalArgumentException("Width and height must be greater than 0.");
        }
        return new Dimension(this.size);
    }

    public String toString() {
        return "AbsoluteSize [width=" + this.size.width + ", height=" + this.size.height + "]";
    }
}
