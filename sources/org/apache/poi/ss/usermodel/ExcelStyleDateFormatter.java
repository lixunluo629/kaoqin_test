package org.apache.poi.ss.usermodel;

import java.math.RoundingMode;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.poi.util.LocaleUtil;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/ExcelStyleDateFormatter.class */
public class ExcelStyleDateFormatter extends SimpleDateFormat {
    public static final char MMMMM_START_SYMBOL = 57345;
    public static final char MMMMM_TRUNCATE_SYMBOL = 57346;
    public static final char H_BRACKET_SYMBOL = 57360;
    public static final char HH_BRACKET_SYMBOL = 57361;
    public static final char M_BRACKET_SYMBOL = 57362;
    public static final char MM_BRACKET_SYMBOL = 57363;
    public static final char S_BRACKET_SYMBOL = 57364;
    public static final char SS_BRACKET_SYMBOL = 57365;
    public static final char L_BRACKET_SYMBOL = 57366;
    public static final char LL_BRACKET_SYMBOL = 57367;
    private static final DecimalFormat format1digit;
    private static final DecimalFormat format2digits;
    private static final DecimalFormat format3digit;
    private static final DecimalFormat format4digits;
    private double dateToBeFormatted;

    static {
        DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance(Locale.ROOT);
        format1digit = new DecimalFormat("0", dfs);
        format2digits = new DecimalFormat(TarConstants.VERSION_POSIX, dfs);
        format3digit = new DecimalFormat("0", dfs);
        format4digits = new DecimalFormat(TarConstants.VERSION_POSIX, dfs);
        DataFormatter.setExcelStyleRoundingMode(format1digit, RoundingMode.DOWN);
        DataFormatter.setExcelStyleRoundingMode(format2digits, RoundingMode.DOWN);
        DataFormatter.setExcelStyleRoundingMode(format3digit);
        DataFormatter.setExcelStyleRoundingMode(format4digits);
    }

    public ExcelStyleDateFormatter(String pattern) {
        super(processFormatPattern(pattern), LocaleUtil.getUserLocale());
        setTimeZone(LocaleUtil.getUserTimeZone());
    }

    public ExcelStyleDateFormatter(String pattern, DateFormatSymbols formatSymbols) {
        super(processFormatPattern(pattern), formatSymbols);
        setTimeZone(LocaleUtil.getUserTimeZone());
    }

    public ExcelStyleDateFormatter(String pattern, Locale locale) {
        super(processFormatPattern(pattern), locale);
        setTimeZone(LocaleUtil.getUserTimeZone());
    }

    private static String processFormatPattern(String f) {
        String t = f.replaceAll("MMMMM", "\ue001MMM\ue002");
        return t.replaceAll("\\[H]", String.valueOf((char) 57360)).replaceAll("\\[HH]", String.valueOf((char) 57361)).replaceAll("\\[m]", String.valueOf((char) 57362)).replaceAll("\\[mm]", String.valueOf((char) 57363)).replaceAll("\\[s]", String.valueOf((char) 57364)).replaceAll("\\[ss]", String.valueOf((char) 57365)).replaceAll("s.000", "s.SSS").replaceAll("s.00", "s.\ue017").replaceAll("s.0", "s.\ue016");
    }

    public void setDateToBeFormatted(double date) {
        this.dateToBeFormatted = date;
    }

    @Override // java.text.SimpleDateFormat, java.text.DateFormat
    public StringBuffer format(Date date, StringBuffer paramStringBuffer, FieldPosition paramFieldPosition) {
        String s = super.format(date, paramStringBuffer, paramFieldPosition).toString();
        if (s.indexOf(MMMMM_START_SYMBOL) != -1) {
            s = s.replaceAll("\ue001(\\p{L}|\\p{P})[\\p{L}\\p{P}]+\ue002", "$1");
        }
        if (s.indexOf(H_BRACKET_SYMBOL) != -1 || s.indexOf(HH_BRACKET_SYMBOL) != -1) {
            float hours = ((float) this.dateToBeFormatted) * 24.0f;
            s = s.replaceAll(String.valueOf((char) 57360), format1digit.format(hours)).replaceAll(String.valueOf((char) 57361), format2digits.format(hours));
        }
        if (s.indexOf(M_BRACKET_SYMBOL) != -1 || s.indexOf(MM_BRACKET_SYMBOL) != -1) {
            float minutes = ((float) this.dateToBeFormatted) * 24.0f * 60.0f;
            s = s.replaceAll(String.valueOf((char) 57362), format1digit.format(minutes)).replaceAll(String.valueOf((char) 57363), format2digits.format(minutes));
        }
        if (s.indexOf(S_BRACKET_SYMBOL) != -1 || s.indexOf(SS_BRACKET_SYMBOL) != -1) {
            float seconds = (float) (this.dateToBeFormatted * 24.0d * 60.0d * 60.0d);
            s = s.replaceAll(String.valueOf((char) 57364), format1digit.format(seconds)).replaceAll(String.valueOf((char) 57365), format2digits.format(seconds));
        }
        if (s.indexOf(L_BRACKET_SYMBOL) != -1 || s.indexOf(LL_BRACKET_SYMBOL) != -1) {
            float millisTemp = (float) ((this.dateToBeFormatted - Math.floor(this.dateToBeFormatted)) * 24.0d * 60.0d * 60.0d);
            float millis = millisTemp - ((int) millisTemp);
            s = s.replaceAll(String.valueOf((char) 57366), format3digit.format(millis * 10.0f)).replaceAll(String.valueOf((char) 57367), format4digits.format(millis * 100.0f));
        }
        return new StringBuffer(s);
    }

    @Override // java.text.SimpleDateFormat, java.text.DateFormat
    public boolean equals(Object o) {
        if (!(o instanceof ExcelStyleDateFormatter)) {
            return false;
        }
        ExcelStyleDateFormatter other = (ExcelStyleDateFormatter) o;
        return this.dateToBeFormatted == other.dateToBeFormatted;
    }

    @Override // java.text.SimpleDateFormat, java.text.DateFormat
    public int hashCode() {
        return new Double(this.dateToBeFormatted).hashCode();
    }
}
