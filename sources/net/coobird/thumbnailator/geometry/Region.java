package net.coobird.thumbnailator.geometry;

import java.awt.Dimension;
import java.awt.Rectangle;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/geometry/Region.class */
public final class Region {
    private final Position position;
    private final Size size;

    public Region(Position position, Size size) {
        if (position == null) {
            throw new NullPointerException("Position cannot be null.");
        }
        if (size == null) {
            throw new NullPointerException("Size cannot be null.");
        }
        this.position = position;
        this.size = size;
    }

    public Position getPosition() {
        return this.position;
    }

    public Size getSize() {
        return this.size;
    }

    public Rectangle calculate(int i, int i2) {
        Dimension dimensionCalculate = this.size.calculate(i, i2);
        return new Rectangle(0, 0, i, i2).intersection(new Rectangle(this.position.calculate(i, i2, dimensionCalculate.width, dimensionCalculate.height, 0, 0, 0, 0), dimensionCalculate));
    }

    public String toString() {
        return "Region [position=" + this.position + ", size=" + this.size + "]";
    }
}
