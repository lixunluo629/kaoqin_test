package org.apache.poi.ss.usermodel;

import java.util.Locale;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/ExtendedColor.class */
public abstract class ExtendedColor implements Color {
    public abstract boolean isAuto();

    public abstract boolean isIndexed();

    public abstract boolean isRGB();

    public abstract boolean isThemed();

    public abstract short getIndex();

    public abstract int getTheme();

    public abstract byte[] getRGB();

    public abstract byte[] getARGB();

    protected abstract byte[] getStoredRBG();

    public abstract void setRGB(byte[] bArr);

    protected abstract byte[] getIndexedRGB();

    public abstract double getTint();

    public abstract void setTint(double d);

    protected void setColor(java.awt.Color clr) {
        setRGB(new byte[]{(byte) clr.getRed(), (byte) clr.getGreen(), (byte) clr.getBlue()});
    }

    protected byte[] getRGBOrARGB() {
        byte[] rgb;
        if (isIndexed() && getIndex() > 0 && (rgb = getIndexedRGB()) != null) {
            return rgb;
        }
        return getStoredRBG();
    }

    public byte[] getRGBWithTint() {
        byte[] rgb = getStoredRBG();
        if (rgb != null) {
            if (rgb.length == 4) {
                byte[] tmp = new byte[3];
                System.arraycopy(rgb, 1, tmp, 0, 3);
                rgb = tmp;
            }
            double tint = getTint();
            for (int i = 0; i < rgb.length; i++) {
                rgb[i] = applyTint(rgb[i] & 255, tint);
            }
        }
        return rgb;
    }

    public String getARGBHex() {
        byte[] rgb = getARGB();
        if (rgb == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (byte c : rgb) {
            int i = c & 255;
            String cs = Integer.toHexString(i);
            if (cs.length() == 1) {
                sb.append('0');
            }
            sb.append(cs);
        }
        return sb.toString().toUpperCase(Locale.ROOT);
    }

    public void setARGBHex(String argb) {
        if (argb.length() == 6 || argb.length() == 8) {
            byte[] rgb = new byte[argb.length() / 2];
            for (int i = 0; i < rgb.length; i++) {
                String part = argb.substring(i * 2, (i + 1) * 2);
                rgb[i] = (byte) Integer.parseInt(part, 16);
            }
            setRGB(rgb);
            return;
        }
        throw new IllegalArgumentException("Must be of the form 112233 or FFEEDDCC");
    }

    private static byte applyTint(int lum, double tint) {
        if (tint > 0.0d) {
            return (byte) ((lum * (1.0d - tint)) + (255.0d - (255.0d * (1.0d - tint))));
        }
        if (tint < 0.0d) {
            return (byte) (lum * (1.0d + tint));
        }
        return (byte) lum;
    }
}
