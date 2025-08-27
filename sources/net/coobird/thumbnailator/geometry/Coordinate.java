package net.coobird.thumbnailator.geometry;

import java.awt.Point;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/geometry/Coordinate.class */
public final class Coordinate implements Position {
    private final int x;
    private final int y;

    public Coordinate(int i, int i2) {
        this.x = i;
        this.y = i2;
    }

    @Override // net.coobird.thumbnailator.geometry.Position
    public Point calculate(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        return new Point(this.x + i5, this.y + i7);
    }
}
