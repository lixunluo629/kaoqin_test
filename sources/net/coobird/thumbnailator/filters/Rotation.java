package net.coobird.thumbnailator.filters;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import net.coobird.thumbnailator.builders.BufferedImageBuilder;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/filters/Rotation.class */
public class Rotation {
    public static final Rotator LEFT_90_DEGREES = newRotator(-90.0d);
    public static final Rotator RIGHT_90_DEGREES = newRotator(90.0d);
    public static final Rotator ROTATE_180_DEGREES = newRotator(180.0d);

    private Rotation() {
    }

    /* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/filters/Rotation$Rotator.class */
    public static abstract class Rotator implements ImageFilter {
        private Rotator() {
        }
    }

    public static Rotator newRotator(final double d) {
        return new Rotator() { // from class: net.coobird.thumbnailator.filters.Rotation.1
            private double[] calculatePosition(double d2, double d3, double d4) {
                double radians = Math.toRadians(d4);
                return new double[]{(Math.cos(radians) * d2) - (Math.sin(radians) * d3), (Math.sin(radians) * d2) + (Math.cos(radians) * d3)};
            }

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // net.coobird.thumbnailator.filters.ImageFilter
            public BufferedImage apply(BufferedImage bufferedImage) {
                int width = bufferedImage.getWidth();
                int height = bufferedImage.getHeight();
                double[] dArr = {calculatePosition(0.0d, 0.0d, d), calculatePosition(width, 0.0d, d), calculatePosition(0.0d, height, d), calculatePosition(width, height, d)};
                double dMin = Math.min(Math.min((double) dArr[0][0], (double) dArr[1][0]), Math.min((double) dArr[2][0], (double) dArr[3][0]));
                double dMax = Math.max(Math.max((double) dArr[0][0], (double) dArr[1][0]), Math.max((double) dArr[2][0], (double) dArr[3][0]));
                double dMin2 = Math.min(Math.min((double) dArr[0][1], (double) dArr[1][1]), Math.min((double) dArr[2][1], (double) dArr[3][1]));
                double dMax2 = Math.max(Math.max((double) dArr[0][1], (double) dArr[1][1]), Math.max((double) dArr[2][1], (double) dArr[3][1]));
                int iRound = (int) Math.round(dMax - dMin);
                int iRound2 = (int) Math.round(dMax2 - dMin2);
                BufferedImage bufferedImageBuild = new BufferedImageBuilder(iRound, iRound2).build();
                Graphics2D graphics2DCreateGraphics = bufferedImageBuild.createGraphics();
                graphics2DCreateGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                graphics2DCreateGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2DCreateGraphics.rotate(Math.toRadians(d), iRound / 2.0d, iRound2 / 2.0d);
                graphics2DCreateGraphics.drawImage(bufferedImage, (int) Math.round((iRound - width) / 2.0d), (int) Math.round((iRound2 - height) / 2.0d), (ImageObserver) null);
                graphics2DCreateGraphics.dispose();
                return bufferedImageBuild;
            }
        };
    }
}
