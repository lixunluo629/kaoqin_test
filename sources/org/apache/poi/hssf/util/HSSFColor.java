package org.apache.poi.hssf.util;

import com.itextpdf.io.codec.TIFFConstants;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.util.Removal;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor.class */
public class HSSFColor implements Color {
    private static Map<Integer, HSSFColor> indexHash;
    private static Map<HSSFColorPredefined, HSSFColor> enumList;
    private java.awt.Color color;
    private int index;
    private int index2;

    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$HSSFColorPredefined.class */
    public enum HSSFColorPredefined {
        BLACK(8, -1, 0),
        BROWN(60, -1, 10040064),
        OLIVE_GREEN(59, -1, 3355392),
        DARK_GREEN(58, -1, 13056),
        DARK_TEAL(56, -1, 13158),
        DARK_BLUE(18, 32, 128),
        INDIGO(62, -1, 3355545),
        GREY_80_PERCENT(63, -1, 3355443),
        ORANGE(53, -1, 16737792),
        DARK_YELLOW(19, -1, 8421376),
        GREEN(17, -1, 32768),
        TEAL(21, 38, TIFFConstants.COMPRESSION_IT8LW),
        BLUE(12, 39, 255),
        BLUE_GREY(54, -1, 6710937),
        GREY_50_PERCENT(23, -1, 8421504),
        RED(10, -1, 16711680),
        LIGHT_ORANGE(52, -1, 16750848),
        LIME(50, -1, 10079232),
        SEA_GREEN(57, -1, 3381606),
        AQUA(49, -1, 3394764),
        LIGHT_BLUE(48, -1, 3368703),
        VIOLET(20, 36, 8388736),
        GREY_40_PERCENT(55, -1, 9868950),
        PINK(14, 33, 16711935),
        GOLD(51, -1, 16763904),
        YELLOW(13, 34, 16776960),
        BRIGHT_GREEN(11, -1, 65280),
        TURQUOISE(15, 35, 65535),
        DARK_RED(16, 37, 8388608),
        SKY_BLUE(40, -1, 52479),
        PLUM(61, 25, 10040166),
        GREY_25_PERCENT(22, -1, 12632256),
        ROSE(45, -1, 16751052),
        LIGHT_YELLOW(43, -1, 16777113),
        LIGHT_GREEN(42, -1, 13434828),
        LIGHT_TURQUOISE(41, 27, 13434879),
        PALE_BLUE(44, -1, 10079487),
        LAVENDER(46, -1, 13408767),
        WHITE(9, -1, 16777215),
        CORNFLOWER_BLUE(24, -1, 10066431),
        LEMON_CHIFFON(26, -1, 16777164),
        MAROON(25, -1, 8323072),
        ORCHID(28, -1, 6684774),
        CORAL(29, -1, 16744576),
        ROYAL_BLUE(30, -1, 26316),
        LIGHT_CORNFLOWER_BLUE(31, -1, 13421823),
        TAN(47, -1, 16764057),
        AUTOMATIC(64, -1, 0);

        private HSSFColor color;

        HSSFColorPredefined(int index, int index2, int rgb) {
            this.color = new HSSFColor(index, index2, new java.awt.Color(rgb));
        }

        public short getIndex() {
            return this.color.getIndex();
        }

        public short getIndex2() {
            return this.color.getIndex2();
        }

        public short[] getTriplet() {
            return this.color.getTriplet();
        }

        public String getHexString() {
            return this.color.getHexString();
        }

        public HSSFColor getColor() {
            return new HSSFColor(getIndex(), getIndex2(), this.color.color);
        }
    }

    public HSSFColor() {
        this(64, -1, java.awt.Color.BLACK);
    }

    public HSSFColor(int index, int index2, java.awt.Color color) {
        this.index = index;
        this.index2 = index2;
        this.color = color;
    }

    public static final synchronized Map<Integer, HSSFColor> getIndexHash() {
        if (indexHash == null) {
            indexHash = Collections.unmodifiableMap(createColorsByIndexMap());
        }
        return indexHash;
    }

    public static final Map<Integer, HSSFColor> getMutableIndexHash() {
        return createColorsByIndexMap();
    }

    private static Map<Integer, HSSFColor> createColorsByIndexMap() {
        Map<HSSFColorPredefined, HSSFColor> eList = mapEnumToColorClass();
        Map<Integer, HSSFColor> result = new HashMap<>((eList.size() * 3) / 2);
        for (Map.Entry<HSSFColorPredefined, HSSFColor> colorRef : eList.entrySet()) {
            Integer index1 = Integer.valueOf(colorRef.getKey().getIndex());
            if (!result.containsKey(index1)) {
                result.put(index1, colorRef.getValue());
            }
            Integer index2 = Integer.valueOf(colorRef.getKey().getIndex2());
            if (index2.intValue() != -1 && !result.containsKey(index2)) {
                result.put(index2, colorRef.getValue());
            }
        }
        return result;
    }

    public static Map<String, HSSFColor> getTripletHash() {
        return createColorsByHexStringMap();
    }

    private static Map<String, HSSFColor> createColorsByHexStringMap() {
        Map<HSSFColorPredefined, HSSFColor> eList = mapEnumToColorClass();
        Map<String, HSSFColor> result = new HashMap<>(eList.size());
        for (Map.Entry<HSSFColorPredefined, HSSFColor> colorRef : eList.entrySet()) {
            String hexString = colorRef.getKey().getHexString();
            if (!result.containsKey(hexString)) {
                result.put(hexString, colorRef.getValue());
            }
        }
        return result;
    }

    @Removal(version = "3.18")
    @Deprecated
    private static synchronized Map<HSSFColorPredefined, HSSFColor> mapEnumToColorClass() {
        if (enumList == null) {
            enumList = new EnumMap(HSSFColorPredefined.class);
            enumList.put(HSSFColorPredefined.BLACK, new BLACK());
            enumList.put(HSSFColorPredefined.BROWN, new BROWN());
            enumList.put(HSSFColorPredefined.OLIVE_GREEN, new OLIVE_GREEN());
            enumList.put(HSSFColorPredefined.DARK_GREEN, new DARK_GREEN());
            enumList.put(HSSFColorPredefined.DARK_TEAL, new DARK_TEAL());
            enumList.put(HSSFColorPredefined.DARK_BLUE, new DARK_BLUE());
            enumList.put(HSSFColorPredefined.INDIGO, new INDIGO());
            enumList.put(HSSFColorPredefined.GREY_80_PERCENT, new GREY_80_PERCENT());
            enumList.put(HSSFColorPredefined.ORANGE, new ORANGE());
            enumList.put(HSSFColorPredefined.DARK_YELLOW, new DARK_YELLOW());
            enumList.put(HSSFColorPredefined.GREEN, new GREEN());
            enumList.put(HSSFColorPredefined.TEAL, new TEAL());
            enumList.put(HSSFColorPredefined.BLUE, new BLUE());
            enumList.put(HSSFColorPredefined.BLUE_GREY, new BLUE_GREY());
            enumList.put(HSSFColorPredefined.GREY_50_PERCENT, new GREY_50_PERCENT());
            enumList.put(HSSFColorPredefined.RED, new RED());
            enumList.put(HSSFColorPredefined.LIGHT_ORANGE, new LIGHT_ORANGE());
            enumList.put(HSSFColorPredefined.LIME, new LIME());
            enumList.put(HSSFColorPredefined.SEA_GREEN, new SEA_GREEN());
            enumList.put(HSSFColorPredefined.AQUA, new AQUA());
            enumList.put(HSSFColorPredefined.LIGHT_BLUE, new LIGHT_BLUE());
            enumList.put(HSSFColorPredefined.VIOLET, new VIOLET());
            enumList.put(HSSFColorPredefined.GREY_40_PERCENT, new GREY_40_PERCENT());
            enumList.put(HSSFColorPredefined.PINK, new PINK());
            enumList.put(HSSFColorPredefined.GOLD, new GOLD());
            enumList.put(HSSFColorPredefined.YELLOW, new YELLOW());
            enumList.put(HSSFColorPredefined.BRIGHT_GREEN, new BRIGHT_GREEN());
            enumList.put(HSSFColorPredefined.TURQUOISE, new TURQUOISE());
            enumList.put(HSSFColorPredefined.DARK_RED, new DARK_RED());
            enumList.put(HSSFColorPredefined.SKY_BLUE, new SKY_BLUE());
            enumList.put(HSSFColorPredefined.PLUM, new PLUM());
            enumList.put(HSSFColorPredefined.GREY_25_PERCENT, new GREY_25_PERCENT());
            enumList.put(HSSFColorPredefined.ROSE, new ROSE());
            enumList.put(HSSFColorPredefined.LIGHT_YELLOW, new LIGHT_YELLOW());
            enumList.put(HSSFColorPredefined.LIGHT_GREEN, new LIGHT_GREEN());
            enumList.put(HSSFColorPredefined.LIGHT_TURQUOISE, new LIGHT_TURQUOISE());
            enumList.put(HSSFColorPredefined.PALE_BLUE, new PALE_BLUE());
            enumList.put(HSSFColorPredefined.LAVENDER, new LAVENDER());
            enumList.put(HSSFColorPredefined.WHITE, new WHITE());
            enumList.put(HSSFColorPredefined.CORNFLOWER_BLUE, new CORNFLOWER_BLUE());
            enumList.put(HSSFColorPredefined.LEMON_CHIFFON, new LEMON_CHIFFON());
            enumList.put(HSSFColorPredefined.MAROON, new MAROON());
            enumList.put(HSSFColorPredefined.ORCHID, new ORCHID());
            enumList.put(HSSFColorPredefined.CORAL, new CORAL());
            enumList.put(HSSFColorPredefined.ROYAL_BLUE, new ROYAL_BLUE());
            enumList.put(HSSFColorPredefined.LIGHT_CORNFLOWER_BLUE, new LIGHT_CORNFLOWER_BLUE());
            enumList.put(HSSFColorPredefined.TAN, new TAN());
        }
        return enumList;
    }

    public short getIndex() {
        return (short) this.index;
    }

    public short getIndex2() {
        return (short) this.index2;
    }

    public short[] getTriplet() {
        return new short[]{(short) this.color.getRed(), (short) this.color.getGreen(), (short) this.color.getBlue()};
    }

    public String getHexString() {
        return (Integer.toHexString(this.color.getRed() * 257) + ":" + Integer.toHexString(this.color.getGreen() * 257) + ":" + Integer.toHexString(this.color.getBlue() * 257)).toUpperCase(Locale.ROOT);
    }

    public static HSSFColor toHSSFColor(Color color) {
        if (color != null && !(color instanceof HSSFColor)) {
            throw new IllegalArgumentException("Only HSSFColor objects are supported");
        }
        return (HSSFColor) color;
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$HSSFColorRef.class */
    private static class HSSFColorRef extends HSSFColor {
        HSSFColorRef(HSSFColorPredefined colorEnum) {
            super(colorEnum.getIndex(), colorEnum.getIndex2(), colorEnum.color.color);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$BLACK.class */
    public static class BLACK extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.BLACK;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public BLACK() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$BROWN.class */
    public static class BROWN extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.BROWN;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public BROWN() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$OLIVE_GREEN.class */
    public static class OLIVE_GREEN extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.OLIVE_GREEN;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public OLIVE_GREEN() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$DARK_GREEN.class */
    public static class DARK_GREEN extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.DARK_GREEN;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public DARK_GREEN() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$DARK_TEAL.class */
    public static class DARK_TEAL extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.DARK_TEAL;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public DARK_TEAL() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$DARK_BLUE.class */
    public static class DARK_BLUE extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.DARK_BLUE;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public DARK_BLUE() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$INDIGO.class */
    public static class INDIGO extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.INDIGO;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public INDIGO() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$GREY_80_PERCENT.class */
    public static class GREY_80_PERCENT extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.GREY_80_PERCENT;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public GREY_80_PERCENT() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$DARK_RED.class */
    public static class DARK_RED extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.DARK_RED;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public DARK_RED() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$ORANGE.class */
    public static class ORANGE extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.ORANGE;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public ORANGE() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$DARK_YELLOW.class */
    public static class DARK_YELLOW extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.DARK_YELLOW;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public DARK_YELLOW() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$GREEN.class */
    public static class GREEN extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.GREEN;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public GREEN() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$TEAL.class */
    public static class TEAL extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.TEAL;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public TEAL() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$BLUE.class */
    public static class BLUE extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.BLUE;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public BLUE() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$BLUE_GREY.class */
    public static class BLUE_GREY extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.BLUE_GREY;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public BLUE_GREY() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$GREY_50_PERCENT.class */
    public static class GREY_50_PERCENT extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.GREY_50_PERCENT;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public GREY_50_PERCENT() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$RED.class */
    public static class RED extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.RED;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public RED() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$LIGHT_ORANGE.class */
    public static class LIGHT_ORANGE extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.LIGHT_ORANGE;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public LIGHT_ORANGE() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$LIME.class */
    public static class LIME extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.LIME;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public LIME() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$SEA_GREEN.class */
    public static class SEA_GREEN extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.SEA_GREEN;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public SEA_GREEN() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$AQUA.class */
    public static class AQUA extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.AQUA;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public AQUA() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$LIGHT_BLUE.class */
    public static class LIGHT_BLUE extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.LIGHT_BLUE;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public LIGHT_BLUE() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$VIOLET.class */
    public static class VIOLET extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.VIOLET;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public VIOLET() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$GREY_40_PERCENT.class */
    public static class GREY_40_PERCENT extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.GREY_40_PERCENT;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public GREY_40_PERCENT() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$PINK.class */
    public static class PINK extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.PINK;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public PINK() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$GOLD.class */
    public static class GOLD extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.GOLD;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public GOLD() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$YELLOW.class */
    public static class YELLOW extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.YELLOW;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public YELLOW() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$BRIGHT_GREEN.class */
    public static class BRIGHT_GREEN extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.BRIGHT_GREEN;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public BRIGHT_GREEN() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$TURQUOISE.class */
    public static class TURQUOISE extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.TURQUOISE;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public TURQUOISE() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$SKY_BLUE.class */
    public static class SKY_BLUE extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.SKY_BLUE;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public SKY_BLUE() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$PLUM.class */
    public static class PLUM extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.PLUM;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public PLUM() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$GREY_25_PERCENT.class */
    public static class GREY_25_PERCENT extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.GREY_25_PERCENT;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public GREY_25_PERCENT() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$ROSE.class */
    public static class ROSE extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.ROSE;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public ROSE() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$TAN.class */
    public static class TAN extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.TAN;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public TAN() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$LIGHT_YELLOW.class */
    public static class LIGHT_YELLOW extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.LIGHT_YELLOW;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public LIGHT_YELLOW() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$LIGHT_GREEN.class */
    public static class LIGHT_GREEN extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.LIGHT_GREEN;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public LIGHT_GREEN() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$LIGHT_TURQUOISE.class */
    public static class LIGHT_TURQUOISE extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.LIGHT_TURQUOISE;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public LIGHT_TURQUOISE() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$PALE_BLUE.class */
    public static class PALE_BLUE extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.PALE_BLUE;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public PALE_BLUE() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$LAVENDER.class */
    public static class LAVENDER extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.LAVENDER;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public LAVENDER() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$WHITE.class */
    public static class WHITE extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.WHITE;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public WHITE() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$CORNFLOWER_BLUE.class */
    public static class CORNFLOWER_BLUE extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.CORNFLOWER_BLUE;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public CORNFLOWER_BLUE() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$LEMON_CHIFFON.class */
    public static class LEMON_CHIFFON extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.LEMON_CHIFFON;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public LEMON_CHIFFON() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$MAROON.class */
    public static class MAROON extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.MAROON;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public MAROON() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$ORCHID.class */
    public static class ORCHID extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.ORCHID;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public ORCHID() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$CORAL.class */
    public static class CORAL extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.CORAL;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public CORAL() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$ROYAL_BLUE.class */
    public static class ROYAL_BLUE extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.ROYAL_BLUE;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public ROYAL_BLUE() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$LIGHT_CORNFLOWER_BLUE.class */
    public static class LIGHT_CORNFLOWER_BLUE extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.LIGHT_CORNFLOWER_BLUE;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public LIGHT_CORNFLOWER_BLUE() {
            super(ref);
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/util/HSSFColor$AUTOMATIC.class */
    public static class AUTOMATIC extends HSSFColorRef {
        private static final HSSFColorPredefined ref = HSSFColorPredefined.AUTOMATIC;
        public static final short index = ref.getIndex();
        public static final int index2 = ref.getIndex2();
        public static final short[] triplet = ref.getTriplet();
        public static final String hexString = ref.getHexString();

        public AUTOMATIC() {
            super(ref);
        }

        public static HSSFColor getInstance() {
            return ref.color;
        }
    }
}
