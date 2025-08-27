package org.apache.poi.ss.format;

import ch.qos.logback.core.rolling.helper.DateTokenConverter;
import com.alibaba.excel.constant.ExcelXmlConstants;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.FieldPosition;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import org.apache.poi.util.LocaleUtil;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.springframework.aop.framework.autoproxy.target.QuickTargetSourceCreator;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/format/CellNumberFormatter.class */
public class CellNumberFormatter extends CellFormatter {
    private static final POILogger LOG = POILogFactory.getLogger((Class<?>) CellNumberFormatter.class);
    private final String desc;
    private final String printfFmt;
    private final double scale;
    private final Special decimalPoint;
    private final Special slash;
    private final Special exponent;
    private final Special numerator;
    private final Special afterInteger;
    private final Special afterFractional;
    private final boolean showGroupingSeparator;
    private final List<Special> specials;
    private final List<Special> integerSpecials;
    private final List<Special> fractionalSpecials;
    private final List<Special> numeratorSpecials;
    private final List<Special> denominatorSpecials;
    private final List<Special> exponentSpecials;
    private final List<Special> exponentDigitSpecials;
    private final int maxDenominator;
    private final String numeratorFmt;
    private final String denominatorFmt;
    private final boolean improperFraction;
    private final DecimalFormat decimalFmt;
    private final CellFormatter SIMPLE_NUMBER;

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/format/CellNumberFormatter$GeneralNumberFormatter.class */
    private static class GeneralNumberFormatter extends CellFormatter {
        private GeneralNumberFormatter(Locale locale) {
            super(locale, "General");
        }

        @Override // org.apache.poi.ss.format.CellFormatter
        public void formatValue(StringBuffer toAppendTo, Object value) {
            CellFormatter cf;
            if (value == null) {
                return;
            }
            if (value instanceof Number) {
                Number num = (Number) value;
                cf = num.doubleValue() % 1.0d == 0.0d ? new CellNumberFormatter(this.locale, "#") : new CellNumberFormatter(this.locale, "#.#");
            } else {
                cf = CellTextFormatter.SIMPLE_TEXT;
            }
            cf.formatValue(toAppendTo, value);
        }

        @Override // org.apache.poi.ss.format.CellFormatter
        public void simpleValue(StringBuffer toAppendTo, Object value) {
            formatValue(toAppendTo, value);
        }
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/format/CellNumberFormatter$Special.class */
    static class Special {

        /* renamed from: ch, reason: collision with root package name */
        final char f9ch;
        int pos;

        Special(char ch2, int pos) {
            this.f9ch = ch2;
            this.pos = pos;
        }

        public String toString() {
            return "'" + this.f9ch + "' @ " + this.pos;
        }
    }

    public CellNumberFormatter(String format) {
        this(LocaleUtil.getUserLocale(), format);
    }

    public CellNumberFormatter(Locale locale, String format) {
        super(locale, format);
        this.specials = new ArrayList();
        this.integerSpecials = new ArrayList();
        this.fractionalSpecials = new ArrayList();
        this.numeratorSpecials = new ArrayList();
        this.denominatorSpecials = new ArrayList();
        this.exponentSpecials = new ArrayList();
        this.exponentDigitSpecials = new ArrayList();
        this.SIMPLE_NUMBER = new GeneralNumberFormatter(this.locale);
        CellNumberPartHandler ph = new CellNumberPartHandler();
        StringBuffer descBuf = CellFormatPart.parseFormat(format, CellFormatType.NUMBER, ph);
        this.exponent = ph.getExponent();
        this.specials.addAll(ph.getSpecials());
        this.improperFraction = ph.isImproperFraction();
        if ((ph.getDecimalPoint() != null || ph.getExponent() != null) && ph.getSlash() != null) {
            this.slash = null;
            this.numerator = null;
        } else {
            this.slash = ph.getSlash();
            this.numerator = ph.getNumerator();
        }
        int precision = interpretPrecision(ph.getDecimalPoint(), this.specials);
        int fractionPartWidth = 0;
        if (ph.getDecimalPoint() != null) {
            fractionPartWidth = 1 + precision;
            if (precision == 0) {
                this.specials.remove(ph.getDecimalPoint());
                this.decimalPoint = null;
            } else {
                this.decimalPoint = ph.getDecimalPoint();
            }
        } else {
            this.decimalPoint = null;
        }
        if (this.decimalPoint != null) {
            this.afterInteger = this.decimalPoint;
        } else if (this.exponent != null) {
            this.afterInteger = this.exponent;
        } else if (this.numerator != null) {
            this.afterInteger = this.numerator;
        } else {
            this.afterInteger = null;
        }
        if (this.exponent != null) {
            this.afterFractional = this.exponent;
        } else if (this.numerator != null) {
            this.afterFractional = this.numerator;
        } else {
            this.afterFractional = null;
        }
        double[] scaleByRef = {ph.getScale()};
        this.showGroupingSeparator = interpretIntegerCommas(descBuf, this.specials, this.decimalPoint, integerEnd(), fractionalEnd(), scaleByRef);
        if (this.exponent == null) {
            this.scale = scaleByRef[0];
        } else {
            this.scale = 1.0d;
        }
        if (precision != 0) {
            this.fractionalSpecials.addAll(this.specials.subList(this.specials.indexOf(this.decimalPoint) + 1, fractionalEnd()));
        }
        if (this.exponent != null) {
            int exponentPos = this.specials.indexOf(this.exponent);
            this.exponentSpecials.addAll(specialsFor(exponentPos, 2));
            this.exponentDigitSpecials.addAll(specialsFor(exponentPos + 2));
        }
        if (this.slash != null) {
            if (this.numerator != null) {
                this.numeratorSpecials.addAll(specialsFor(this.specials.indexOf(this.numerator)));
            }
            this.denominatorSpecials.addAll(specialsFor(this.specials.indexOf(this.slash) + 1));
            if (this.denominatorSpecials.isEmpty()) {
                this.numeratorSpecials.clear();
                this.maxDenominator = 1;
                this.numeratorFmt = null;
                this.denominatorFmt = null;
            } else {
                this.maxDenominator = maxValue(this.denominatorSpecials);
                this.numeratorFmt = singleNumberFormat(this.numeratorSpecials);
                this.denominatorFmt = singleNumberFormat(this.denominatorSpecials);
            }
        } else {
            this.maxDenominator = 1;
            this.numeratorFmt = null;
            this.denominatorFmt = null;
        }
        this.integerSpecials.addAll(this.specials.subList(0, integerEnd()));
        if (this.exponent == null) {
            StringBuffer fmtBuf = new StringBuffer(QuickTargetSourceCreator.PREFIX_THREAD_LOCAL);
            int integerPartWidth = calculateIntegerPartWidth();
            int totalWidth = integerPartWidth + fractionPartWidth;
            fmtBuf.append('0').append(totalWidth).append('.').append(precision);
            fmtBuf.append(ExcelXmlConstants.CELL_FORMULA_TAG);
            this.printfFmt = fmtBuf.toString();
            this.decimalFmt = null;
        } else {
            StringBuffer fmtBuf2 = new StringBuffer();
            boolean first = true;
            List<Special> specialList = this.integerSpecials;
            if (this.integerSpecials.size() == 1) {
                fmtBuf2.append("0");
                first = false;
            } else {
                for (Special s : specialList) {
                    if (isDigitFmt(s)) {
                        fmtBuf2.append(first ? '#' : '0');
                        first = false;
                    }
                }
            }
            if (this.fractionalSpecials.size() > 0) {
                fmtBuf2.append('.');
                for (Special s2 : this.fractionalSpecials) {
                    if (isDigitFmt(s2)) {
                        if (!first) {
                            fmtBuf2.append('0');
                        }
                        first = false;
                    }
                }
            }
            fmtBuf2.append('E');
            placeZeros(fmtBuf2, this.exponentSpecials.subList(2, this.exponentSpecials.size()));
            this.decimalFmt = new DecimalFormat(fmtBuf2.toString(), getDecimalFormatSymbols());
            this.printfFmt = null;
        }
        this.desc = descBuf.toString();
    }

    private DecimalFormatSymbols getDecimalFormatSymbols() {
        return DecimalFormatSymbols.getInstance(this.locale);
    }

    private static void placeZeros(StringBuffer sb, List<Special> specials) {
        for (Special s : specials) {
            if (isDigitFmt(s)) {
                sb.append('0');
            }
        }
    }

    private static CellNumberStringMod insertMod(Special special, CharSequence toAdd, int where) {
        return new CellNumberStringMod(special, toAdd, where);
    }

    private static CellNumberStringMod deleteMod(Special start, boolean startInclusive, Special end, boolean endInclusive) {
        return new CellNumberStringMod(start, startInclusive, end, endInclusive);
    }

    private static CellNumberStringMod replaceMod(Special start, boolean startInclusive, Special end, boolean endInclusive, char withChar) {
        return new CellNumberStringMod(start, startInclusive, end, endInclusive, withChar);
    }

    private static String singleNumberFormat(List<Special> numSpecials) {
        return "%0" + numSpecials.size() + DateTokenConverter.CONVERTER_KEY;
    }

    private static int maxValue(List<Special> s) {
        return (int) Math.round(Math.pow(10.0d, s.size()) - 1.0d);
    }

    private List<Special> specialsFor(int pos, int takeFirst) {
        if (pos >= this.specials.size()) {
            return Collections.emptyList();
        }
        ListIterator<Special> it = this.specials.listIterator(pos + takeFirst);
        Special last = it.next();
        int end = pos + takeFirst;
        while (it.hasNext()) {
            Special s = it.next();
            if (!isDigitFmt(s) || s.pos - last.pos > 1) {
                break;
            }
            end++;
            last = s;
        }
        return this.specials.subList(pos, end + 1);
    }

    private List<Special> specialsFor(int pos) {
        return specialsFor(pos, 0);
    }

    private static boolean isDigitFmt(Special s) {
        return s.f9ch == '0' || s.f9ch == '?' || s.f9ch == '#';
    }

    private int calculateIntegerPartWidth() {
        Special s;
        int digitCount = 0;
        Iterator i$ = this.specials.iterator();
        while (i$.hasNext() && (s = i$.next()) != this.afterInteger) {
            if (isDigitFmt(s)) {
                digitCount++;
            }
        }
        return digitCount;
    }

    private static int interpretPrecision(Special decimalPoint, List<Special> specials) {
        int idx = specials.indexOf(decimalPoint);
        int precision = 0;
        if (idx != -1) {
            ListIterator<Special> it = specials.listIterator(idx + 1);
            while (it.hasNext()) {
                Special s = it.next();
                if (!isDigitFmt(s)) {
                    break;
                }
                precision++;
            }
        }
        return precision;
    }

    private static boolean interpretIntegerCommas(StringBuffer sb, List<Special> specials, Special decimalPoint, int integerEnd, int fractionalEnd, double[] scale) {
        ListIterator<Special> it = specials.listIterator(integerEnd);
        boolean stillScaling = true;
        boolean integerCommas = false;
        while (it.hasPrevious()) {
            if (it.previous().f9ch != ',') {
                stillScaling = false;
            } else if (stillScaling) {
                scale[0] = scale[0] / 1000.0d;
            } else {
                integerCommas = true;
            }
        }
        if (decimalPoint != null) {
            ListIterator<Special> it2 = specials.listIterator(fractionalEnd);
            while (it2.hasPrevious() && it2.previous().f9ch == ',') {
                scale[0] = scale[0] / 1000.0d;
            }
        }
        ListIterator<Special> it3 = specials.listIterator();
        int removed = 0;
        while (it3.hasNext()) {
            Special s = it3.next();
            s.pos -= removed;
            if (s.f9ch == ',') {
                removed++;
                it3.remove();
                sb.deleteCharAt(s.pos);
            }
        }
        return integerCommas;
    }

    private int integerEnd() {
        return this.afterInteger == null ? this.specials.size() : this.specials.indexOf(this.afterInteger);
    }

    private int fractionalEnd() {
        return this.afterFractional == null ? this.specials.size() : this.specials.indexOf(this.afterFractional);
    }

    @Override // org.apache.poi.ss.format.CellFormatter
    public void formatValue(StringBuffer toAppendTo, Object valueObject) {
        double value = ((Number) valueObject).doubleValue() * this.scale;
        boolean negative = value < 0.0d;
        if (negative) {
            value = -value;
        }
        double fractional = 0.0d;
        if (this.slash != null) {
            if (this.improperFraction) {
                fractional = value;
                value = 0.0d;
            } else {
                fractional = value % 1.0d;
                value = (long) value;
            }
        }
        Set<CellNumberStringMod> mods = new TreeSet<>();
        StringBuffer output = new StringBuffer(localiseFormat(this.desc));
        if (this.exponent != null) {
            writeScientific(value, output, mods);
        } else if (this.improperFraction) {
            writeFraction(value, null, fractional, output, mods);
        } else {
            StringBuffer result = new StringBuffer();
            Formatter f = new Formatter(result, this.locale);
            try {
                f.format(this.locale, this.printfFmt, Double.valueOf(value));
                f.close();
                if (this.numerator == null) {
                    writeFractional(result, output);
                    writeInteger(result, output, this.integerSpecials, mods, this.showGroupingSeparator);
                } else {
                    writeFraction(value, result, fractional, output, mods);
                }
            } catch (Throwable th) {
                f.close();
                throw th;
            }
        }
        DecimalFormatSymbols dfs = getDecimalFormatSymbols();
        String groupingSeparator = Character.toString(dfs.getGroupingSeparator());
        Iterator<CellNumberStringMod> changes = mods.iterator();
        CellNumberStringMod nextChange = changes.hasNext() ? changes.next() : null;
        BitSet deletedChars = new BitSet();
        int adjust = 0;
        for (Special s : this.specials) {
            int adjustedPos = s.pos + adjust;
            if (!deletedChars.get(s.pos) && output.charAt(adjustedPos) == '#') {
                output.deleteCharAt(adjustedPos);
                adjust--;
                deletedChars.set(s.pos);
            }
            while (nextChange != null && s == nextChange.getSpecial()) {
                int lenBefore = output.length();
                int modPos = s.pos + adjust;
                switch (nextChange.getOp()) {
                    case 1:
                        output.insert(modPos, nextChange.getToAdd());
                        break;
                    case 2:
                        if (!nextChange.getToAdd().equals(groupingSeparator) || !deletedChars.get(s.pos)) {
                            output.insert(modPos + 1, nextChange.getToAdd());
                            break;
                        }
                        break;
                    case 3:
                        int delPos = s.pos;
                        if (!nextChange.isStartInclusive()) {
                            delPos++;
                            modPos++;
                        }
                        while (deletedChars.get(delPos)) {
                            delPos++;
                            modPos++;
                        }
                        int delEndPos = nextChange.getEnd().pos;
                        if (nextChange.isEndInclusive()) {
                            delEndPos++;
                        }
                        int modEndPos = delEndPos + adjust;
                        if (modPos < modEndPos) {
                            if ("".equals(nextChange.getToAdd())) {
                                output.delete(modPos, modEndPos);
                            } else {
                                char fillCh = nextChange.getToAdd().charAt(0);
                                for (int i = modPos; i < modEndPos; i++) {
                                    output.setCharAt(i, fillCh);
                                }
                            }
                            deletedChars.set(delPos, delEndPos);
                            break;
                        }
                        break;
                    default:
                        throw new IllegalStateException("Unknown op: " + nextChange.getOp());
                }
                adjust += output.length() - lenBefore;
                nextChange = changes.hasNext() ? changes.next() : null;
            }
        }
        if (negative) {
            toAppendTo.append('-');
        }
        toAppendTo.append(output);
    }

    private void writeScientific(double value, StringBuffer output, Set<CellNumberStringMod> mods) {
        StringBuffer result = new StringBuffer();
        FieldPosition fractionPos = new FieldPosition(1);
        this.decimalFmt.format(value, result, fractionPos);
        writeInteger(result, output, this.integerSpecials, mods, this.showGroupingSeparator);
        writeFractional(result, output);
        int ePos = fractionPos.getEndIndex();
        int signPos = ePos + 1;
        char expSignRes = result.charAt(signPos);
        if (expSignRes != '-') {
            expSignRes = '+';
            result.insert(signPos, '+');
        }
        ListIterator<Special> it = this.exponentSpecials.listIterator(1);
        Special expSign = it.next();
        char expSignFmt = expSign.f9ch;
        if (expSignRes == '-' || expSignFmt == '+') {
            mods.add(replaceMod(expSign, true, expSign, true, expSignRes));
        } else {
            mods.add(deleteMod(expSign, true, expSign, true));
        }
        StringBuffer exponentNum = new StringBuffer(result.substring(signPos + 1));
        writeInteger(exponentNum, output, this.exponentDigitSpecials, mods, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:61:0x01a2 A[Catch: RuntimeException -> 0x0206, TryCatch #0 {RuntimeException -> 0x0206, blocks: (B:57:0x0192, B:59:0x0199, B:62:0x01b0, B:63:0x01c9, B:65:0x01d0, B:66:0x01df, B:61:0x01a2), top: B:70:0x0192 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void writeFraction(double r8, java.lang.StringBuffer r10, double r11, java.lang.StringBuffer r13, java.util.Set<org.apache.poi.ss.format.CellNumberStringMod> r14) {
        /*
            Method dump skipped, instructions count: 543
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.poi.ss.format.CellNumberFormatter.writeFraction(double, java.lang.StringBuffer, double, java.lang.StringBuffer, java.util.Set):void");
    }

    private String localiseFormat(String format) {
        DecimalFormatSymbols dfs = getDecimalFormatSymbols();
        if (format.contains(",") && dfs.getGroupingSeparator() != ',') {
            if (format.contains(".") && dfs.getDecimalSeparator() != '.') {
                format = replaceLast(format, "\\.", "[DECIMAL_SEPARATOR]").replace(',', dfs.getGroupingSeparator()).replace("[DECIMAL_SEPARATOR]", Character.toString(dfs.getDecimalSeparator()));
            } else {
                format = format.replace(',', dfs.getGroupingSeparator());
            }
        } else if (format.contains(".") && dfs.getDecimalSeparator() != '.') {
            format = format.replace('.', dfs.getDecimalSeparator());
        }
        return format;
    }

    private static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
    }

    private static boolean hasChar(char ch2, List<Special>... numSpecials) {
        for (List<Special> specials : numSpecials) {
            for (Special s : specials) {
                if (s.f9ch == ch2) {
                    return true;
                }
            }
        }
        return false;
    }

    private void writeSingleInteger(String fmt, int num, StringBuffer output, List<Special> numSpecials, Set<CellNumberStringMod> mods) {
        StringBuffer sb = new StringBuffer();
        Formatter formatter = new Formatter(sb, this.locale);
        try {
            formatter.format(this.locale, fmt, Integer.valueOf(num));
            formatter.close();
            writeInteger(sb, output, numSpecials, mods, false);
        } catch (Throwable th) {
            formatter.close();
            throw th;
        }
    }

    private void writeInteger(StringBuffer result, StringBuffer output, List<Special> numSpecials, Set<CellNumberStringMod> mods, boolean showGroupingSeparator) {
        char resultCh;
        char resultCh2;
        DecimalFormatSymbols dfs = getDecimalFormatSymbols();
        String decimalSeparator = Character.toString(dfs.getDecimalSeparator());
        String groupingSeparator = Character.toString(dfs.getGroupingSeparator());
        int pos = result.indexOf(decimalSeparator) - 1;
        if (pos < 0) {
            if (this.exponent != null && numSpecials == this.integerSpecials) {
                pos = result.indexOf("E") - 1;
            } else {
                pos = result.length() - 1;
            }
        }
        int strip = 0;
        while (strip < pos && ((resultCh2 = result.charAt(strip)) == '0' || resultCh2 == dfs.getGroupingSeparator())) {
            strip++;
        }
        ListIterator<Special> it = numSpecials.listIterator(numSpecials.size());
        Special lastOutputIntegerDigit = null;
        int digit = 0;
        while (it.hasPrevious()) {
            if (pos >= 0) {
                resultCh = result.charAt(pos);
            } else {
                resultCh = '0';
            }
            Special s = it.previous();
            boolean followWithGroupingSeparator = showGroupingSeparator && digit > 0 && digit % 3 == 0;
            boolean zeroStrip = false;
            if (resultCh != '0' || s.f9ch == '0' || s.f9ch == '?' || pos >= strip) {
                zeroStrip = s.f9ch == '?' && pos < strip;
                output.setCharAt(s.pos, zeroStrip ? ' ' : resultCh);
                lastOutputIntegerDigit = s;
            }
            if (followWithGroupingSeparator) {
                mods.add(insertMod(s, zeroStrip ? SymbolConstants.SPACE_SYMBOL : groupingSeparator, 2));
            }
            digit++;
            pos--;
        }
        new StringBuffer();
        if (pos >= 0) {
            int pos2 = pos + 1;
            StringBuffer extraLeadingDigits = new StringBuffer(result.substring(0, pos2));
            if (showGroupingSeparator) {
                while (pos2 > 0) {
                    if (digit > 0 && digit % 3 == 0) {
                        extraLeadingDigits.insert(pos2, groupingSeparator);
                    }
                    digit++;
                    pos2--;
                }
            }
            mods.add(insertMod(lastOutputIntegerDigit, extraLeadingDigits, 1));
        }
    }

    private void writeFractional(StringBuffer result, StringBuffer output) {
        int strip;
        if (this.fractionalSpecials.size() > 0) {
            String decimalSeparator = Character.toString(getDecimalFormatSymbols().getDecimalSeparator());
            int digit = result.indexOf(decimalSeparator) + 1;
            if (this.exponent != null) {
                strip = result.indexOf("e") - 1;
            } else {
                strip = result.length() - 1;
            }
            while (strip > digit && result.charAt(strip) == '0') {
                strip--;
            }
            for (Special s : this.fractionalSpecials) {
                char resultCh = result.charAt(digit);
                if (resultCh != '0' || s.f9ch == '0' || digit < strip) {
                    output.setCharAt(s.pos, resultCh);
                } else if (s.f9ch == '?') {
                    output.setCharAt(s.pos, ' ');
                }
                digit++;
            }
        }
    }

    @Override // org.apache.poi.ss.format.CellFormatter
    public void simpleValue(StringBuffer toAppendTo, Object value) {
        this.SIMPLE_NUMBER.formatValue(toAppendTo, value);
    }

    private static Special lastSpecial(List<Special> s) {
        return s.get(s.size() - 1);
    }
}
