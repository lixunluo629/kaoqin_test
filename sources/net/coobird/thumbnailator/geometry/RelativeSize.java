package net.coobird.thumbnailator.geometry;

import java.awt.Dimension;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/geometry/RelativeSize.class */
public class RelativeSize implements Size {
    private final double scalingFactor;

    public RelativeSize(double d) {
        if (d < 0.0d || d > 1.0d) {
            throw new IllegalArgumentException("The scaling factor must be between 0.0d and 1.0d, inclusive.");
        }
        this.scalingFactor = d;
    }

    @Override // net.coobird.thumbnailator.geometry.Size
    public Dimension calculate(int i, int i2) {
        if (i <= 0 || i2 <= 0) {
            throw new IllegalArgumentException("Width and height must be greater than 0.");
        }
        return new Dimension((int) Math.round(i * this.scalingFactor), (int) Math.round(i2 * this.scalingFactor));
    }

    public String toString() {
        return "RelativeSize [scalingFactor=" + this.scalingFactor + "]";
    }
}
