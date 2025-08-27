package org.apache.poi.ss.format;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JLabel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.util.LocaleUtil;
import org.apache.poi.util.Removal;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/format/CellFormat.class */
public class CellFormat {
    private final Locale locale;
    private final String format;
    private final CellFormatPart posNumFmt;
    private final CellFormatPart zeroNumFmt;
    private final CellFormatPart negNumFmt;
    private final CellFormatPart textFmt;
    private final int formatPartCount;
    private static final String INVALID_VALUE_FOR_FORMAT = "###############################################################################################################################################################################################################################################################";
    private static final Pattern ONE_PART = Pattern.compile(CellFormatPart.FORMAT_PAT.pattern() + "(;|$)", 6);
    private static String QUOTE = SymbolConstants.QUOTES_SYMBOL;

    @Removal(version = "3.18")
    @Deprecated
    public static final CellFormat GENERAL_FORMAT = createGeneralFormat(LocaleUtil.getUserLocale());
    private static final Map<Locale, Map<String, CellFormat>> formatCache = new WeakHashMap();

    private static CellFormat createGeneralFormat(final Locale locale) {
        return new CellFormat(locale, "General") { // from class: org.apache.poi.ss.format.CellFormat.1
            @Override // org.apache.poi.ss.format.CellFormat
            public CellFormatResult apply(Object value) {
                String text = new CellGeneralFormatter(locale).format(value);
                return new CellFormatResult(true, text, null);
            }
        };
    }

    public static CellFormat getInstance(String format) {
        return getInstance(LocaleUtil.getUserLocale(), format);
    }

    public static synchronized CellFormat getInstance(Locale locale, String format) {
        Map<String, CellFormat> formatMap = formatCache.get(locale);
        if (formatMap == null) {
            formatMap = new WeakHashMap();
            formatCache.put(locale, formatMap);
        }
        CellFormat fmt = formatMap.get(format);
        if (fmt == null) {
            if (format.equals("General") || format.equals("@")) {
                fmt = createGeneralFormat(locale);
            } else {
                fmt = new CellFormat(locale, format);
            }
            formatMap.put(format, fmt);
        }
        return fmt;
    }

    private CellFormat(Locale locale, String format) {
        this.locale = locale;
        this.format = format;
        CellFormatPart defaultTextFormat = new CellFormatPart(locale, "@");
        Matcher m = ONE_PART.matcher(format);
        List<CellFormatPart> parts = new ArrayList<>();
        while (m.find()) {
            try {
                String valueDesc = m.group();
                parts.add(new CellFormatPart(locale, valueDesc.endsWith(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR) ? valueDesc.substring(0, valueDesc.length() - 1) : valueDesc));
            } catch (RuntimeException e) {
                CellFormatter.logger.log(Level.WARNING, "Invalid format: " + CellFormatter.quote(m.group()), (Throwable) e);
                parts.add(null);
            }
        }
        this.formatPartCount = parts.size();
        switch (this.formatPartCount) {
            case 1:
                this.posNumFmt = parts.get(0);
                this.negNumFmt = null;
                this.zeroNumFmt = null;
                this.textFmt = defaultTextFormat;
                break;
            case 2:
                this.posNumFmt = parts.get(0);
                this.negNumFmt = parts.get(1);
                this.zeroNumFmt = null;
                this.textFmt = defaultTextFormat;
                break;
            case 3:
                this.posNumFmt = parts.get(0);
                this.negNumFmt = parts.get(1);
                this.zeroNumFmt = parts.get(2);
                this.textFmt = defaultTextFormat;
                break;
            case 4:
            default:
                this.posNumFmt = parts.get(0);
                this.negNumFmt = parts.get(1);
                this.zeroNumFmt = parts.get(2);
                this.textFmt = parts.get(3);
                break;
        }
    }

    public CellFormatResult apply(Object value) {
        if (value instanceof Number) {
            Number num = (Number) value;
            double val = num.doubleValue();
            if (val < 0.0d && ((this.formatPartCount == 2 && !this.posNumFmt.hasCondition() && !this.negNumFmt.hasCondition()) || ((this.formatPartCount == 3 && !this.negNumFmt.hasCondition()) || (this.formatPartCount == 4 && !this.negNumFmt.hasCondition())))) {
                return this.negNumFmt.apply(Double.valueOf(-val));
            }
            return getApplicableFormatPart(Double.valueOf(val)).apply(Double.valueOf(val));
        }
        if (value instanceof Date) {
            Double numericValue = Double.valueOf(DateUtil.getExcelDate((Date) value));
            if (DateUtil.isValidExcelDate(numericValue.doubleValue())) {
                return getApplicableFormatPart(numericValue).apply(value);
            }
            throw new IllegalArgumentException("value " + numericValue + " of date " + value + " is not a valid Excel date");
        }
        return this.textFmt.apply(value);
    }

    private CellFormatResult apply(Date date, double numericValue) {
        return getApplicableFormatPart(Double.valueOf(numericValue)).apply(date);
    }

    public CellFormatResult apply(Cell c) {
        switch (ultimateTypeEnum(c)) {
            case BLANK:
                return apply("");
            case BOOLEAN:
                return apply(Boolean.valueOf(c.getBooleanCellValue()));
            case NUMERIC:
                Double value = Double.valueOf(c.getNumericCellValue());
                if (getApplicableFormatPart(value).getCellFormatType() == CellFormatType.DATE) {
                    if (DateUtil.isValidExcelDate(value.doubleValue())) {
                        return apply(c.getDateCellValue(), value.doubleValue());
                    }
                    return apply(INVALID_VALUE_FOR_FORMAT);
                }
                return apply(value);
            case STRING:
                return apply(c.getStringCellValue());
            default:
                return apply("?");
        }
    }

    public CellFormatResult apply(JLabel label, Object value) {
        CellFormatResult result = apply(value);
        label.setText(result.text);
        if (result.textColor != null) {
            label.setForeground(result.textColor);
        }
        return result;
    }

    private CellFormatResult apply(JLabel label, Date date, double numericValue) {
        CellFormatResult result = apply(date, numericValue);
        label.setText(result.text);
        if (result.textColor != null) {
            label.setForeground(result.textColor);
        }
        return result;
    }

    public CellFormatResult apply(JLabel label, Cell c) {
        switch (ultimateTypeEnum(c)) {
            case BLANK:
                return apply(label, "");
            case BOOLEAN:
                return apply(label, Boolean.valueOf(c.getBooleanCellValue()));
            case NUMERIC:
                Double value = Double.valueOf(c.getNumericCellValue());
                if (getApplicableFormatPart(value).getCellFormatType() == CellFormatType.DATE) {
                    if (DateUtil.isValidExcelDate(value.doubleValue())) {
                        return apply(label, c.getDateCellValue(), value.doubleValue());
                    }
                    return apply(label, INVALID_VALUE_FOR_FORMAT);
                }
                return apply(label, value);
            case STRING:
                return apply(label, c.getStringCellValue());
            default:
                return apply(label, "?");
        }
    }

    private CellFormatPart getApplicableFormatPart(Object value) {
        if (value instanceof Number) {
            double val = ((Number) value).doubleValue();
            if (this.formatPartCount == 1) {
                if (!this.posNumFmt.hasCondition() || (this.posNumFmt.hasCondition() && this.posNumFmt.applies(Double.valueOf(val)))) {
                    return this.posNumFmt;
                }
                return new CellFormatPart(this.locale, "General");
            }
            if (this.formatPartCount == 2) {
                if ((!this.posNumFmt.hasCondition() && val >= 0.0d) || (this.posNumFmt.hasCondition() && this.posNumFmt.applies(Double.valueOf(val)))) {
                    return this.posNumFmt;
                }
                if (!this.negNumFmt.hasCondition() || (this.negNumFmt.hasCondition() && this.negNumFmt.applies(Double.valueOf(val)))) {
                    return this.negNumFmt;
                }
                return new CellFormatPart(QUOTE + INVALID_VALUE_FOR_FORMAT + QUOTE);
            }
            if ((!this.posNumFmt.hasCondition() && val > 0.0d) || (this.posNumFmt.hasCondition() && this.posNumFmt.applies(Double.valueOf(val)))) {
                return this.posNumFmt;
            }
            if ((!this.negNumFmt.hasCondition() && val < 0.0d) || (this.negNumFmt.hasCondition() && this.negNumFmt.applies(Double.valueOf(val)))) {
                return this.negNumFmt;
            }
            return this.zeroNumFmt;
        }
        throw new IllegalArgumentException("value must be a Number");
    }

    public static int ultimateType(Cell cell) {
        return ultimateTypeEnum(cell).getCode();
    }

    public static CellType ultimateTypeEnum(Cell cell) {
        CellType type = cell.getCellTypeEnum();
        if (type == CellType.FORMULA) {
            return cell.getCachedFormulaResultTypeEnum();
        }
        return type;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof CellFormat) {
            CellFormat that = (CellFormat) obj;
            return this.format.equals(that.format);
        }
        return false;
    }

    public int hashCode() {
        return this.format.hashCode();
    }
}
