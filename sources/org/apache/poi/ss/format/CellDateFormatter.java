package org.apache.poi.ss.format;

import com.alibaba.excel.constant.ExcelXmlConstants;
import com.mysql.jdbc.MysqlErrorNumbers;
import java.text.AttributedCharacterIterator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.regex.Matcher;
import org.apache.poi.ss.format.CellFormatPart;
import org.apache.poi.util.LocaleUtil;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/format/CellDateFormatter.class */
public class CellDateFormatter extends CellFormatter {
    private boolean amPmUpper;
    private boolean showM;
    private boolean showAmPm;
    private final DateFormat dateFmt;
    private String sFmt;
    private final Calendar EXCEL_EPOCH_CAL;
    private static CellDateFormatter SIMPLE_DATE = null;

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/format/CellDateFormatter$DatePartHandler.class */
    private class DatePartHandler implements CellFormatPart.PartHandler {
        private int mStart;
        private int mLen;
        private int hStart;
        private int hLen;

        private DatePartHandler() {
            this.mStart = -1;
            this.hStart = -1;
        }

        @Override // org.apache.poi.ss.format.CellFormatPart.PartHandler
        public String handlePart(Matcher m, String part, CellFormatType type, StringBuffer desc) {
            int pos = desc.length();
            char firstCh = part.charAt(0);
            switch (firstCh) {
                case '0':
                    this.mStart = -1;
                    int sLen = part.length();
                    CellDateFormatter.this.sFmt = "%0" + (sLen + 2) + "." + sLen + ExcelXmlConstants.CELL_FORMULA_TAG;
                    break;
                case 'A':
                case 'P':
                case 'a':
                case 'p':
                    if (part.length() > 1) {
                        this.mStart = -1;
                        CellDateFormatter.this.showAmPm = true;
                        CellDateFormatter.this.showM = Character.toLowerCase(part.charAt(1)) == 'm';
                        CellDateFormatter.this.amPmUpper = CellDateFormatter.this.showM || Character.isUpperCase(part.charAt(0));
                        break;
                    }
                    break;
                case 'D':
                case 'd':
                    this.mStart = -1;
                    if (part.length() <= 2) {
                        break;
                    } else {
                        break;
                    }
                case 'H':
                case 'h':
                    this.mStart = -1;
                    this.hStart = pos;
                    this.hLen = part.length();
                    break;
                case 'M':
                case 'm':
                    this.mStart = pos;
                    this.mLen = part.length();
                    if (this.hStart >= 0) {
                        break;
                    } else {
                        break;
                    }
                case 'S':
                case 's':
                    if (this.mStart >= 0) {
                        for (int i = 0; i < this.mLen; i++) {
                            desc.setCharAt(this.mStart + i, 'm');
                        }
                        this.mStart = -1;
                    }
                    break;
                case 'Y':
                case 'y':
                    this.mStart = -1;
                    if (part.length() == 3) {
                        part = "yyyy";
                    }
                    break;
            }
            return part.toLowerCase(Locale.ROOT);
        }

        public void finish(StringBuffer toAppendTo) {
            if (this.hStart >= 0 && !CellDateFormatter.this.showAmPm) {
                for (int i = 0; i < this.hLen; i++) {
                    toAppendTo.setCharAt(this.hStart + i, 'H');
                }
            }
        }
    }

    public CellDateFormatter(String format) {
        this(LocaleUtil.getUserLocale(), format);
    }

    public CellDateFormatter(Locale locale, String format) {
        super(format);
        this.EXCEL_EPOCH_CAL = LocaleUtil.getLocaleCalendar(MysqlErrorNumbers.ER_SLAVE_IO_THREAD_MUST_STOP, 0, 1);
        DatePartHandler partHandler = new DatePartHandler();
        StringBuffer descBuf = CellFormatPart.parseFormat(format, CellFormatType.DATE, partHandler);
        partHandler.finish(descBuf);
        String ptrn = descBuf.toString().replaceAll("((y)(?!y))(?<!yy)", "yy");
        this.dateFmt = new SimpleDateFormat(ptrn, locale);
        this.dateFmt.setTimeZone(LocaleUtil.getUserTimeZone());
    }

    @Override // org.apache.poi.ss.format.CellFormatter
    public void formatValue(StringBuffer toAppendTo, Object value) {
        if (value == null) {
            value = Double.valueOf(0.0d);
        }
        if (value instanceof Number) {
            Number num = (Number) value;
            long v = num.longValue();
            if (v == 0) {
                value = this.EXCEL_EPOCH_CAL.getTime();
            } else {
                Calendar c = (Calendar) this.EXCEL_EPOCH_CAL.clone();
                c.add(13, (int) (v / 1000));
                c.add(14, (int) (v % 1000));
                value = c.getTime();
            }
        }
        AttributedCharacterIterator it = this.dateFmt.formatToCharacterIterator(value);
        boolean doneAm = false;
        boolean doneMillis = false;
        it.first();
        char cFirst = it.first();
        while (true) {
            char ch2 = cFirst;
            if (ch2 != 65535) {
                if (it.getAttribute(DateFormat.Field.MILLISECOND) != null) {
                    if (doneMillis) {
                        continue;
                    } else {
                        Date dateObj = (Date) value;
                        int pos = toAppendTo.length();
                        Formatter formatter = new Formatter(toAppendTo, Locale.ROOT);
                        try {
                            long msecs = dateObj.getTime() % 1000;
                            formatter.format(this.locale, this.sFmt, Double.valueOf(msecs / 1000.0d));
                            formatter.close();
                            toAppendTo.delete(pos, pos + 2);
                            doneMillis = true;
                        } catch (Throwable th) {
                            formatter.close();
                            throw th;
                        }
                    }
                } else if (it.getAttribute(DateFormat.Field.AM_PM) != null) {
                    if (!doneAm) {
                        if (this.showAmPm) {
                            if (this.amPmUpper) {
                                toAppendTo.append(Character.toUpperCase(ch2));
                                if (this.showM) {
                                    toAppendTo.append('M');
                                }
                            } else {
                                toAppendTo.append(Character.toLowerCase(ch2));
                                if (this.showM) {
                                    toAppendTo.append('m');
                                }
                            }
                        }
                        doneAm = true;
                    }
                } else {
                    toAppendTo.append(ch2);
                }
                cFirst = it.next();
            } else {
                return;
            }
        }
    }

    @Override // org.apache.poi.ss.format.CellFormatter
    public void simpleValue(StringBuffer toAppendTo, Object value) {
        synchronized (CellDateFormatter.class) {
            if (SIMPLE_DATE == null || !SIMPLE_DATE.EXCEL_EPOCH_CAL.equals(this.EXCEL_EPOCH_CAL)) {
                SIMPLE_DATE = new CellDateFormatter("mm/d/y");
            }
        }
        SIMPLE_DATE.formatValue(toAppendTo, value);
    }
}
