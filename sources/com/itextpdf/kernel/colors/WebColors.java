package com.itextpdf.kernel.colors;

import com.itextpdf.io.LogMessageConstant;
import java.util.HashMap;
import java.util.StringTokenizer;
import org.apache.poi.ddf.EscherProperties;
import org.apache.poi.hssf.record.DrawingGroupRecord;
import org.apache.poi.hssf.record.DrawingSelectionRecord;
import org.apache.poi.hssf.record.UnknownRecord;
import org.aspectj.apache.bcel.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.target.QuickTargetSourceCreator;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/colors/WebColors.class */
public class WebColors extends HashMap<String, int[]> {
    private static final long serialVersionUID = 3542523100813372896L;
    public static final WebColors NAMES = new WebColors();
    private static final double RGB_MAX_VAL = 255.0d;

    static {
        NAMES.put("aliceblue", new int[]{240, EscherProperties.GEOTEXT__STRETCHCHARHEIGHT, 255, 255});
        NAMES.put("antiquewhite", new int[]{EscherProperties.GEOTEXT__BOLDFONT, DrawingGroupRecord.sid, 215, 255});
        NAMES.put("aqua", new int[]{0, 255, 255, 255});
        NAMES.put("aquamarine", new int[]{127, 255, Constants.GETSTATIC2_QUICK, 255});
        NAMES.put("azure", new int[]{240, 255, 255, 255});
        NAMES.put("beige", new int[]{EscherProperties.GEOTEXT__STRETCHTOFITSHAPE, EscherProperties.GEOTEXT__STRETCHTOFITSHAPE, 220, 255});
        NAMES.put("bisque", new int[]{255, Constants.PUTFIELD_QUICK_W, 196, 255});
        NAMES.put("black", new int[]{0, 0, 0, 255});
        NAMES.put("blanchedalmond", new int[]{255, DrawingGroupRecord.sid, 205, 255});
        NAMES.put("blue", new int[]{0, 0, 255, 255});
        NAMES.put("blueviolet", new int[]{138, 43, 226, 255});
        NAMES.put("brown", new int[]{165, 42, 42, 255});
        NAMES.put("burlywood", new int[]{Constants.ANEWARRAY_QUICK, 184, 135, 255});
        NAMES.put("cadetblue", new int[]{95, 158, 160, 255});
        NAMES.put("chartreuse", new int[]{127, 255, 0, 255});
        NAMES.put("chocolate", new int[]{Constants.GETSTATIC_QUICK, 105, 30, 255});
        NAMES.put("coral", new int[]{255, 127, 80, 255});
        NAMES.put("cornflowerblue", new int[]{100, 149, DrawingSelectionRecord.sid, 255});
        NAMES.put("cornsilk", new int[]{255, EscherProperties.GEOTEXT__STRETCHCHARHEIGHT, 220, 255});
        NAMES.put("crimson", new int[]{220, 20, 60, 255});
        NAMES.put("cyan", new int[]{0, 255, 255, 255});
        NAMES.put("darkblue", new int[]{0, 0, 139, 255});
        NAMES.put("darkcyan", new int[]{0, 139, 139, 255});
        NAMES.put("darkgoldenrod", new int[]{184, 134, 11, 255});
        NAMES.put("darkgray", new int[]{169, 169, 169, 255});
        NAMES.put("darkgrey", new int[]{169, 169, 169, 255});
        NAMES.put("darkgreen", new int[]{0, 100, 0, 255});
        NAMES.put("darkkhaki", new int[]{189, 183, 107, 255});
        NAMES.put("darkmagenta", new int[]{139, 0, 139, 255});
        NAMES.put("darkolivegreen", new int[]{85, 107, 47, 255});
        NAMES.put("darkorange", new int[]{255, 140, 0, 255});
        NAMES.put("darkorchid", new int[]{153, 50, 204, 255});
        NAMES.put("darkred", new int[]{139, 0, 0, 255});
        NAMES.put("darksalmon", new int[]{UnknownRecord.BITMAP_00E9, 150, 122, 255});
        NAMES.put("darkseagreen", new int[]{143, 188, 143, 255});
        NAMES.put("darkslateblue", new int[]{72, 61, 139, 255});
        NAMES.put("darkslategray", new int[]{47, 79, 79, 255});
        NAMES.put("darkslategrey", new int[]{47, 79, 79, 255});
        NAMES.put("darkturquoise", new int[]{0, 206, Constants.PUTFIELD2_QUICK, 255});
        NAMES.put("darkviolet", new int[]{148, 0, 211, 255});
        NAMES.put("deeppink", new int[]{255, 20, 147, 255});
        NAMES.put("deepskyblue", new int[]{0, 191, 255, 255});
        NAMES.put("dimgray", new int[]{105, 105, 105, 255});
        NAMES.put("dimgrey", new int[]{105, 105, 105, 255});
        NAMES.put("dodgerblue", new int[]{30, 144, 255, 255});
        NAMES.put("firebrick", new int[]{178, 34, 34, 255});
        NAMES.put("floralwhite", new int[]{255, EscherProperties.GEOTEXT__BOLDFONT, 240, 255});
        NAMES.put("forestgreen", new int[]{34, 139, 34, 255});
        NAMES.put("fuchsia", new int[]{255, 0, 255, 255});
        NAMES.put("gainsboro", new int[]{220, 220, 220, 255});
        NAMES.put("ghostwhite", new int[]{EscherProperties.GEOTEXT__STRETCHCHARHEIGHT, EscherProperties.GEOTEXT__STRETCHCHARHEIGHT, 255, 255});
        NAMES.put("gold", new int[]{255, 215, 0, 255});
        NAMES.put("goldenrod", new int[]{218, 165, 32, 255});
        NAMES.put("gray", new int[]{128, 128, 128, 255});
        NAMES.put("grey", new int[]{128, 128, 128, 255});
        NAMES.put("green", new int[]{0, 128, 0, 255});
        NAMES.put("greenyellow", new int[]{173, 255, 47, 255});
        NAMES.put("honeydew", new int[]{240, 255, 240, 255});
        NAMES.put("hotpink", new int[]{255, 105, 180, 255});
        NAMES.put("indianred", new int[]{205, 92, 92, 255});
        NAMES.put("indigo", new int[]{75, 0, 130, 255});
        NAMES.put("ivory", new int[]{255, 255, 240, 255});
        NAMES.put("khaki", new int[]{240, 230, 140, 255});
        NAMES.put("lavender", new int[]{230, 230, EscherProperties.GEOTEXT__BOLDFONT, 255});
        NAMES.put("lavenderblush", new int[]{255, 240, EscherProperties.GEOTEXT__STRETCHTOFITSHAPE, 255});
        NAMES.put("lawngreen", new int[]{124, 252, 0, 255});
        NAMES.put("lemonchiffon", new int[]{255, EscherProperties.GEOTEXT__BOLDFONT, 205, 255});
        NAMES.put("lightblue", new int[]{173, Constants.INVOKESUPER_QUICK, 230, 255});
        NAMES.put("lightcoral", new int[]{240, 128, 128, 255});
        NAMES.put("lightcyan", new int[]{224, 255, 255, 255});
        NAMES.put("lightgoldenrodyellow", new int[]{EscherProperties.GEOTEXT__BOLDFONT, EscherProperties.GEOTEXT__BOLDFONT, Constants.GETSTATIC_QUICK, 255});
        NAMES.put("lightgreen", new int[]{144, 238, 144, 255});
        NAMES.put("lightgray", new int[]{211, 211, 211, 255});
        NAMES.put("lightgrey", new int[]{211, 211, 211, 255});
        NAMES.put("lightpink", new int[]{255, 182, 193, 255});
        NAMES.put("lightsalmon", new int[]{255, 160, 122, 255});
        NAMES.put("lightseagreen", new int[]{32, 178, 170, 255});
        NAMES.put("lightskyblue", new int[]{135, 206, EscherProperties.GEOTEXT__BOLDFONT, 255});
        NAMES.put("lightslategray", new int[]{119, 136, 153, 255});
        NAMES.put("lightslategrey", new int[]{119, 136, 153, 255});
        NAMES.put("lightsteelblue", new int[]{176, 196, Constants.ANEWARRAY_QUICK, 255});
        NAMES.put("lightyellow", new int[]{255, 255, 224, 255});
        NAMES.put("lime", new int[]{0, 255, 0, 255});
        NAMES.put("limegreen", new int[]{50, 205, 50, 255});
        NAMES.put("linen", new int[]{EscherProperties.GEOTEXT__BOLDFONT, 240, 230, 255});
        NAMES.put("magenta", new int[]{255, 0, 255, 255});
        NAMES.put("maroon", new int[]{128, 0, 0, 255});
        NAMES.put("mediumaquamarine", new int[]{102, 205, 170, 255});
        NAMES.put("mediumblue", new int[]{0, 0, 205, 255});
        NAMES.put("mediumorchid", new int[]{186, 85, 211, 255});
        NAMES.put("mediumpurple", new int[]{147, 112, Constants.INVOKEVIRTUALOBJECT_QUICK, 255});
        NAMES.put("mediumseagreen", new int[]{60, 179, 113, 255});
        NAMES.put("mediumslateblue", new int[]{123, 104, 238, 255});
        NAMES.put("mediumspringgreen", new int[]{0, EscherProperties.GEOTEXT__BOLDFONT, 154, 255});
        NAMES.put("mediumturquoise", new int[]{72, Constants.PUTFIELD2_QUICK, 204, 255});
        NAMES.put("mediumvioletred", new int[]{199, 21, 133, 255});
        NAMES.put("midnightblue", new int[]{25, 25, 112, 255});
        NAMES.put("mintcream", new int[]{EscherProperties.GEOTEXT__STRETCHTOFITSHAPE, 255, EscherProperties.GEOTEXT__BOLDFONT, 255});
        NAMES.put("mistyrose", new int[]{255, Constants.PUTFIELD_QUICK_W, 225, 255});
        NAMES.put("moccasin", new int[]{255, Constants.PUTFIELD_QUICK_W, 181, 255});
        NAMES.put("navajowhite", new int[]{255, Constants.ANEWARRAY_QUICK, 173, 255});
        NAMES.put("navy", new int[]{0, 0, 128, 255});
        NAMES.put("oldlace", new int[]{253, EscherProperties.GEOTEXT__STRETCHTOFITSHAPE, 230, 255});
        NAMES.put("olive", new int[]{128, 128, 0, 255});
        NAMES.put("olivedrab", new int[]{107, 142, 35, 255});
        NAMES.put("orange", new int[]{255, 165, 0, 255});
        NAMES.put("orangered", new int[]{255, 69, 0, 255});
        NAMES.put("orchid", new int[]{218, 112, Constants.INVOKEVIRTUAL_QUICK, 255});
        NAMES.put("palegoldenrod", new int[]{238, 232, 170, 255});
        NAMES.put("palegreen", new int[]{152, 251, 152, 255});
        NAMES.put("paleturquoise", new int[]{175, 238, 238, 255});
        NAMES.put("palevioletred", new int[]{Constants.INVOKEVIRTUALOBJECT_QUICK, 112, 147, 255});
        NAMES.put("papayawhip", new int[]{255, UnknownRecord.PHONETICPR_00EF, 213, 255});
        NAMES.put("peachpuff", new int[]{255, 218, 185, 255});
        NAMES.put("peru", new int[]{205, 133, 63, 255});
        NAMES.put("pink", new int[]{255, 192, 203, 255});
        NAMES.put("plum", new int[]{221, 160, 221, 255});
        NAMES.put("powderblue", new int[]{176, 224, 230, 255});
        NAMES.put("purple", new int[]{128, 0, 128, 255});
        NAMES.put("red", new int[]{255, 0, 0, 255});
        NAMES.put("rosybrown", new int[]{188, 143, 143, 255});
        NAMES.put("royalblue", new int[]{65, 105, 225, 255});
        NAMES.put("saddlebrown", new int[]{139, 69, 19, 255});
        NAMES.put("salmon", new int[]{EscherProperties.GEOTEXT__BOLDFONT, 128, 114, 255});
        NAMES.put("sandybrown", new int[]{EscherProperties.GEOTEXT__TIGHTORTRACK, 164, 96, 255});
        NAMES.put("seagreen", new int[]{46, 139, 87, 255});
        NAMES.put("seashell", new int[]{255, EscherProperties.GEOTEXT__STRETCHTOFITSHAPE, 238, 255});
        NAMES.put("sienna", new int[]{160, 82, 45, 255});
        NAMES.put("silver", new int[]{192, 192, 192, 255});
        NAMES.put("skyblue", new int[]{135, 206, DrawingGroupRecord.sid, 255});
        NAMES.put("slateblue", new int[]{106, 90, 205, 255});
        NAMES.put("slategray", new int[]{112, 128, 144, 255});
        NAMES.put("slategrey", new int[]{112, 128, 144, 255});
        NAMES.put("snow", new int[]{255, EscherProperties.GEOTEXT__BOLDFONT, EscherProperties.GEOTEXT__BOLDFONT, 255});
        NAMES.put("springgreen", new int[]{0, 255, 127, 255});
        NAMES.put("steelblue", new int[]{70, 130, 180, 255});
        NAMES.put("tan", new int[]{Constants.GETSTATIC_QUICK, 180, 140, 255});
        NAMES.put("teal", new int[]{0, 128, 128, 255});
        NAMES.put("thistle", new int[]{Constants.INVOKESUPER_QUICK, 191, Constants.INVOKESUPER_QUICK, 255});
        NAMES.put("tomato", new int[]{255, 99, 71, 255});
        NAMES.put("transparent", new int[]{255, 255, 255, 0});
        NAMES.put("turquoise", new int[]{64, 224, 208, 255});
        NAMES.put("violet", new int[]{238, 130, 238, 255});
        NAMES.put("wheat", new int[]{EscherProperties.GEOTEXT__STRETCHTOFITSHAPE, Constants.ANEWARRAY_QUICK, 179, 255});
        NAMES.put("white", new int[]{255, 255, 255, 255});
        NAMES.put("whitesmoke", new int[]{EscherProperties.GEOTEXT__STRETCHTOFITSHAPE, EscherProperties.GEOTEXT__STRETCHTOFITSHAPE, EscherProperties.GEOTEXT__STRETCHTOFITSHAPE, 255});
        NAMES.put("yellow", new int[]{255, 255, 0, 255});
        NAMES.put("yellowgreen", new int[]{154, 205, 50, 255});
    }

    public static DeviceRgb getRGBColor(String name) {
        float[] rgbaColor = getRGBAColor(name);
        if (rgbaColor == null) {
            return new DeviceRgb(0, 0, 0);
        }
        return new DeviceRgb(rgbaColor[0], rgbaColor[1], rgbaColor[2]);
    }

    public static float[] getRGBAColor(String name) {
        float[] color = null;
        try {
            String colorName = name.toLowerCase();
            boolean colorStrWithoutHash = missingHashColorFormat(colorName);
            if (colorName.startsWith("#") || colorStrWithoutHash) {
                if (!colorStrWithoutHash) {
                    colorName = colorName.substring(1);
                }
                if (colorName.length() == 3) {
                    String red = colorName.substring(0, 1);
                    color = new float[]{0.0f, 0.0f, 0.0f, 1.0f};
                    color[0] = (float) (Integer.parseInt(red + red, 16) / RGB_MAX_VAL);
                    String green = colorName.substring(1, 2);
                    color[1] = (float) (Integer.parseInt(green + green, 16) / RGB_MAX_VAL);
                    String blue = colorName.substring(2);
                    color[2] = (float) (Integer.parseInt(blue + blue, 16) / RGB_MAX_VAL);
                } else if (colorName.length() == 6) {
                    color = new float[]{0.0f, 0.0f, 0.0f, 1.0f};
                    color[0] = (float) (Integer.parseInt(colorName.substring(0, 2), 16) / RGB_MAX_VAL);
                    color[1] = (float) (Integer.parseInt(colorName.substring(2, 4), 16) / RGB_MAX_VAL);
                    color[2] = (float) (Integer.parseInt(colorName.substring(4), 16) / RGB_MAX_VAL);
                } else {
                    Logger logger = LoggerFactory.getLogger((Class<?>) WebColors.class);
                    logger.error(LogMessageConstant.UNKNOWN_COLOR_FORMAT_MUST_BE_RGB_OR_RRGGBB);
                }
            } else if (colorName.startsWith("rgb(")) {
                color = new float[]{0.0f, 0.0f, 0.0f, 1.0f};
                parseRGBColors(color, new StringTokenizer(colorName, "rgb(), \t\r\n\f"));
            } else if (colorName.startsWith("rgba(")) {
                StringTokenizer tok = new StringTokenizer(colorName, "rgba(), \t\r\n\f");
                color = new float[]{0.0f, 0.0f, 0.0f, 1.0f};
                parseRGBColors(color, tok);
                if (tok.hasMoreTokens()) {
                    color[3] = getAlphaChannelValue(tok.nextToken());
                }
            } else if (NAMES.containsKey(colorName)) {
                int[] intColor = NAMES.get(colorName);
                color = new float[]{0.0f, 0.0f, 0.0f, 1.0f};
                color[0] = (float) (intColor[0] / RGB_MAX_VAL);
                color[1] = (float) (intColor[1] / RGB_MAX_VAL);
                color[2] = (float) (intColor[2] / RGB_MAX_VAL);
            }
        } catch (Exception e) {
            color = null;
        }
        return color;
    }

    private static void parseRGBColors(float[] color, StringTokenizer tok) {
        for (int k = 0; k < 3; k++) {
            if (tok.hasMoreTokens()) {
                color[k] = getRGBChannelValue(tok.nextToken());
                color[k] = Math.max(0.0f, color[k]);
                color[k] = Math.min(1.0f, color[k]);
            }
        }
    }

    private static boolean missingHashColorFormat(String colStr) {
        int len = colStr.length();
        if (len == 3 || len == 6) {
            String match = "[0-9a-f]{" + len + "}";
            return colStr.matches(match);
        }
        return false;
    }

    private static float getRGBChannelValue(String rgbChannel) {
        if (rgbChannel.endsWith(QuickTargetSourceCreator.PREFIX_THREAD_LOCAL)) {
            return parsePercentValue(rgbChannel);
        }
        return (float) (Integer.parseInt(rgbChannel) / RGB_MAX_VAL);
    }

    private static float getAlphaChannelValue(String rgbChannel) throws NumberFormatException {
        float alpha;
        if (rgbChannel.endsWith(QuickTargetSourceCreator.PREFIX_THREAD_LOCAL)) {
            alpha = parsePercentValue(rgbChannel);
        } else {
            alpha = Float.parseFloat(rgbChannel);
        }
        return Math.min(1.0f, Math.max(0.0f, alpha));
    }

    private static float parsePercentValue(String rgbChannel) {
        return (float) (Float.parseFloat(rgbChannel.substring(0, rgbChannel.length() - 1)) / 100.0d);
    }
}
