package com.itextpdf.layout.renderer;

import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.TrueTypeFont;
import com.itextpdf.io.font.otf.Glyph;
import com.itextpdf.io.font.otf.GlyphLine;
import com.itextpdf.io.util.MessageFormatUtil;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.layout.property.BaseDirection;
import com.itextpdf.layout.renderer.LineRenderer;
import java.lang.Character;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/renderer/TypographyUtils.class */
public final class TypographyUtils {
    private static final String TYPOGRAPHY_PACKAGE = "com.itextpdf.typography.";
    private static final String SHAPER = "shaping.Shaper";
    private static final String BIDI_CHARACTER_MAP = "bidi.BidiCharacterMap";
    private static final String BIDI_BRACKET_MAP = "bidi.BidiBracketMap";
    private static final String BIDI_ALGORITHM = "bidi.BidiAlgorithm";
    private static final String APPLY_OTF_SCRIPT = "applyOtfScript";
    private static final String APPLY_KERNING = "applyKerning";
    private static final String GET_SUPPORTED_SCRIPTS = "getSupportedScripts";
    private static final String GET_CHARACTER_TYPES = "getCharacterTypes";
    private static final String GET_BRACKET_TYPES = "getBracketTypes";
    private static final String GET_BRACKET_VALUES = "getBracketValues";
    private static final String GET_PAIRED_BRACKET = "getPairedBracket";
    private static final String GET_LEVELS = "getLevels";
    private static final String COMPUTE_REORDERING = "computeReordering";
    private static final String INVERSE_REORDERING = "inverseReordering";
    private static final Collection<Character.UnicodeScript> SUPPORTED_SCRIPTS;
    private static final boolean TYPOGRAPHY_MODULE_INITIALIZED;
    private static final String typographyNotFoundException = "Cannot find pdfCalligraph module, which was implicitly required by one of the layout properties";
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) TypographyUtils.class);
    private static Map<String, Class<?>> cachedClasses = new HashMap();
    private static Map<TypographyMethodSignature, AccessibleObject> cachedMethods = new HashMap();

    static {
        boolean moduleFound = false;
        try {
            Class<?> type = getTypographyClass("com.itextpdf.typography.shaping.Shaper");
            if (type != null) {
                moduleFound = true;
            }
        } catch (ClassNotFoundException e) {
        }
        Collection<Character.UnicodeScript> supportedScripts = null;
        if (moduleFound) {
            try {
                supportedScripts = (Collection) callMethod("com.itextpdf.typography.shaping.Shaper", GET_SUPPORTED_SCRIPTS, new Class[0], new Object[0]);
            } catch (Exception e2) {
                supportedScripts = null;
                logger.error(e2.getMessage());
            }
        }
        boolean moduleFound2 = supportedScripts != null;
        if (!moduleFound2) {
            cachedClasses.clear();
            cachedMethods.clear();
        }
        TYPOGRAPHY_MODULE_INITIALIZED = moduleFound2;
        SUPPORTED_SCRIPTS = supportedScripts;
    }

    private TypographyUtils() {
    }

    public static boolean isPdfCalligraphAvailable() {
        return TYPOGRAPHY_MODULE_INITIALIZED;
    }

    static void applyOtfScript(FontProgram fontProgram, GlyphLine text, Character.UnicodeScript script, Object typographyConfig) {
        if (!TYPOGRAPHY_MODULE_INITIALIZED) {
            logger.warn(typographyNotFoundException);
        } else {
            callMethod("com.itextpdf.typography.shaping.Shaper", APPLY_OTF_SCRIPT, new Class[]{TrueTypeFont.class, GlyphLine.class, Character.UnicodeScript.class, Object.class}, fontProgram, text, script, typographyConfig);
        }
    }

    static void applyKerning(FontProgram fontProgram, GlyphLine text) {
        if (!TYPOGRAPHY_MODULE_INITIALIZED) {
            logger.warn(typographyNotFoundException);
        } else {
            callMethod("com.itextpdf.typography.shaping.Shaper", APPLY_KERNING, new Class[]{FontProgram.class, GlyphLine.class}, fontProgram, text);
        }
    }

    static byte[] getBidiLevels(BaseDirection baseDirection, int[] unicodeIds) {
        byte direction;
        if (!TYPOGRAPHY_MODULE_INITIALIZED) {
            logger.warn(typographyNotFoundException);
            return null;
        }
        switch (baseDirection) {
            case LEFT_TO_RIGHT:
                direction = 0;
                break;
            case RIGHT_TO_LEFT:
                direction = 1;
                break;
            case DEFAULT_BIDI:
            default:
                direction = 2;
                break;
        }
        int len = unicodeIds.length;
        byte[] types = (byte[]) callMethod("com.itextpdf.typography.bidi.BidiCharacterMap", GET_CHARACTER_TYPES, new Class[]{int[].class, Integer.TYPE, Integer.TYPE}, unicodeIds, 0, Integer.valueOf(len));
        byte[] pairTypes = (byte[]) callMethod("com.itextpdf.typography.bidi.BidiBracketMap", GET_BRACKET_TYPES, new Class[]{int[].class, Integer.TYPE, Integer.TYPE}, unicodeIds, 0, Integer.valueOf(len));
        int[] pairValues = (int[]) callMethod("com.itextpdf.typography.bidi.BidiBracketMap", GET_BRACKET_VALUES, new Class[]{int[].class, Integer.TYPE, Integer.TYPE}, unicodeIds, 0, Integer.valueOf(len));
        Object bidiReorder = callConstructor("com.itextpdf.typography.bidi.BidiAlgorithm", new Class[]{byte[].class, byte[].class, int[].class, Byte.TYPE}, types, pairTypes, pairValues, Byte.valueOf(direction));
        return (byte[]) callMethod("com.itextpdf.typography.bidi.BidiAlgorithm", GET_LEVELS, bidiReorder, new Class[]{int[].class}, new int[]{len});
    }

    static int[] reorderLine(List<LineRenderer.RendererGlyph> line, byte[] lineLevels, byte[] levels) {
        int unicode;
        int pairedBracket;
        if (!TYPOGRAPHY_MODULE_INITIALIZED) {
            logger.warn(typographyNotFoundException);
            return null;
        }
        if (levels == null) {
            return null;
        }
        int[] reorder = (int[]) callMethod("com.itextpdf.typography.bidi.BidiAlgorithm", COMPUTE_REORDERING, new Class[]{byte[].class}, lineLevels);
        int[] inverseReorder = (int[]) callMethod("com.itextpdf.typography.bidi.BidiAlgorithm", INVERSE_REORDERING, new Class[]{int[].class}, reorder);
        List<LineRenderer.RendererGlyph> reorderedLine = new ArrayList<>(lineLevels.length);
        for (int i = 0; i < line.size(); i++) {
            reorderedLine.add(line.get(reorder[i]));
            if (levels[reorder[i]] % 2 == 1 && reorderedLine.get(i).glyph.hasValidUnicode() && (pairedBracket = ((Integer) callMethod("com.itextpdf.typography.bidi.BidiBracketMap", GET_PAIRED_BRACKET, new Class[]{Integer.TYPE}, Integer.valueOf(unicode))).intValue()) != (unicode = reorderedLine.get(i).glyph.getUnicode())) {
                PdfFont font = reorderedLine.get(i).renderer.getPropertyAsFont(20);
                reorderedLine.set(i, new LineRenderer.RendererGlyph(font.getGlyph(pairedBracket), reorderedLine.get(i).renderer));
            }
        }
        for (int i2 = 0; i2 < reorderedLine.size(); i2++) {
            Glyph glyph = reorderedLine.get(i2).glyph;
            if (glyph.hasPlacement()) {
                int oldAnchor = reorder[i2] + glyph.getAnchorDelta();
                int newPos = inverseReorder[oldAnchor];
                int newAnchorDelta = newPos - i2;
                glyph.setAnchorDelta((short) newAnchorDelta);
            }
        }
        line.clear();
        line.addAll(reorderedLine);
        return reorder;
    }

    static Collection<Character.UnicodeScript> getSupportedScripts() {
        if (!TYPOGRAPHY_MODULE_INITIALIZED) {
            logger.warn(typographyNotFoundException);
            return null;
        }
        return SUPPORTED_SCRIPTS;
    }

    static Collection<Character.UnicodeScript> getSupportedScripts(Object typographyConfig) {
        if (!TYPOGRAPHY_MODULE_INITIALIZED) {
            logger.warn(typographyNotFoundException);
            return null;
        }
        return (Collection) callMethod("com.itextpdf.typography.shaping.Shaper", GET_SUPPORTED_SCRIPTS, null, new Class[]{Object.class}, typographyConfig);
    }

    private static Object callMethod(String className, String methodName, Class[] parameterTypes, Object... args) {
        return callMethod(className, methodName, null, parameterTypes, args);
    }

    private static Object callMethod(String className, String methodName, Object target, Class[] parameterTypes, Object... args) {
        try {
            Method method = findMethod(className, methodName, parameterTypes);
            return method.invoke(target, args);
        } catch (ClassNotFoundException e) {
            logger.warn(MessageFormatUtil.format("Cannot find class {0}", className));
            return null;
        } catch (IllegalArgumentException e2) {
            logger.warn(MessageFormatUtil.format("Illegal arguments passed to {0}#{1} method call: {2}", className, methodName, e2.getMessage()));
            return null;
        } catch (NoSuchMethodException e3) {
            logger.warn(MessageFormatUtil.format("Cannot find method {0} for class {1}", methodName, className));
            return null;
        } catch (Exception e4) {
            throw new RuntimeException(e4.toString(), e4);
        }
    }

    private static Object callConstructor(String className, Class[] parameterTypes, Object... args) {
        try {
            Constructor<?> constructor = findConstructor(className, parameterTypes);
            return constructor.newInstance(args);
        } catch (ClassNotFoundException e) {
            logger.warn(MessageFormatUtil.format("Cannot find class {0}", className));
            return null;
        } catch (NoSuchMethodException e2) {
            logger.warn(MessageFormatUtil.format("Cannot find constructor for class {0}", className));
            return null;
        } catch (Exception exc) {
            throw new RuntimeException(exc.toString(), exc);
        }
    }

    private static Method findMethod(String className, String methodName, Class[] parameterTypes) throws NoSuchMethodException, SecurityException, ClassNotFoundException {
        TypographyMethodSignature tm = new TypographyMethodSignature(className, parameterTypes, methodName);
        Method m = (Method) cachedMethods.get(tm);
        if (m == null) {
            m = findClass(className).getMethod(methodName, parameterTypes);
            cachedMethods.put(tm, m);
        }
        return m;
    }

    private static Constructor<?> findConstructor(String className, Class[] parameterTypes) throws NoSuchMethodException, SecurityException, ClassNotFoundException {
        TypographyMethodSignature tc = new TypographyMethodSignature(className, parameterTypes);
        Constructor<?> c = (Constructor) cachedMethods.get(tc);
        if (c == null) {
            c = findClass(className).getConstructor(parameterTypes);
            cachedMethods.put(tc, c);
        }
        return c;
    }

    private static Class<?> findClass(String className) throws ClassNotFoundException {
        Class<?> c = cachedClasses.get(className);
        if (c == null) {
            c = getTypographyClass(className);
            cachedClasses.put(className, c);
        }
        return c;
    }

    private static Class<?> getTypographyClass(String typographyClassName) throws ClassNotFoundException {
        return Class.forName(typographyClassName);
    }

    /* loaded from: layout-7.1.10.jar:com/itextpdf/layout/renderer/TypographyUtils$TypographyMethodSignature.class */
    private static class TypographyMethodSignature {
        protected final String className;
        protected Class[] parameterTypes;
        private final String methodName;

        TypographyMethodSignature(String className, Class[] parameterTypes) {
            this(className, parameterTypes, null);
        }

        TypographyMethodSignature(String className, Class[] parameterTypes, String methodName) {
            this.methodName = methodName;
            this.className = className;
            this.parameterTypes = parameterTypes;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            TypographyMethodSignature that = (TypographyMethodSignature) o;
            if (this.className.equals(that.className) && Arrays.equals(this.parameterTypes, that.parameterTypes)) {
                return this.methodName != null ? this.methodName.equals(that.methodName) : that.methodName == null;
            }
            return false;
        }

        public int hashCode() {
            int result = this.className.hashCode();
            return (31 * ((31 * result) + Arrays.hashCode(this.parameterTypes))) + (this.methodName != null ? this.methodName.hashCode() : 0);
        }
    }
}
