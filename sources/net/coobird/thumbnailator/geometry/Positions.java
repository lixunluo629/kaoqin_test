package net.coobird.thumbnailator.geometry;

import java.awt.Point;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/geometry/Positions.class */
public enum Positions implements Position {
    TOP_LEFT { // from class: net.coobird.thumbnailator.geometry.Positions.1
        @Override // net.coobird.thumbnailator.geometry.Position
        public Point calculate(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            return new Point(i5, i7);
        }
    },
    TOP_CENTER { // from class: net.coobird.thumbnailator.geometry.Positions.2
        @Override // net.coobird.thumbnailator.geometry.Position
        public Point calculate(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            return new Point((i / 2) - (i3 / 2), i7);
        }
    },
    TOP_RIGHT { // from class: net.coobird.thumbnailator.geometry.Positions.3
        @Override // net.coobird.thumbnailator.geometry.Position
        public Point calculate(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            return new Point((i - i3) - i6, i7);
        }
    },
    CENTER_LEFT { // from class: net.coobird.thumbnailator.geometry.Positions.4
        @Override // net.coobird.thumbnailator.geometry.Position
        public Point calculate(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            return new Point(i5, (i2 / 2) - (i4 / 2));
        }
    },
    CENTER { // from class: net.coobird.thumbnailator.geometry.Positions.5
        @Override // net.coobird.thumbnailator.geometry.Position
        public Point calculate(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            return new Point((i / 2) - (i3 / 2), (i2 / 2) - (i4 / 2));
        }
    },
    CENTER_RIGHT { // from class: net.coobird.thumbnailator.geometry.Positions.6
        @Override // net.coobird.thumbnailator.geometry.Position
        public Point calculate(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            return new Point((i - i3) - i6, (i2 / 2) - (i4 / 2));
        }
    },
    BOTTOM_LEFT { // from class: net.coobird.thumbnailator.geometry.Positions.7
        @Override // net.coobird.thumbnailator.geometry.Position
        public Point calculate(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            return new Point(i5, (i2 - i4) - i8);
        }
    },
    BOTTOM_CENTER { // from class: net.coobird.thumbnailator.geometry.Positions.8
        @Override // net.coobird.thumbnailator.geometry.Position
        public Point calculate(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            return new Point((i / 2) - (i3 / 2), (i2 - i4) - i8);
        }
    },
    BOTTOM_RIGHT { // from class: net.coobird.thumbnailator.geometry.Positions.9
        @Override // net.coobird.thumbnailator.geometry.Position
        public Point calculate(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            return new Point((i - i3) - i6, (i2 - i4) - i8);
        }
    }
}
