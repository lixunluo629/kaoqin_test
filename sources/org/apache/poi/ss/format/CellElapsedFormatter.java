package org.apache.poi.ss.format;

import ch.qos.logback.core.rolling.helper.DateTokenConverter;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.ss.format.CellFormatPart;
import org.springframework.aop.framework.autoproxy.target.QuickTargetSourceCreator;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/format/CellElapsedFormatter.class */
public class CellElapsedFormatter extends CellFormatter {
    private final List<TimeSpec> specs;
    private TimeSpec topmost;
    private final String printfFmt;
    private static final Pattern PERCENTS = Pattern.compile(QuickTargetSourceCreator.PREFIX_THREAD_LOCAL);
    private static final double HOUR__FACTOR = 0.041666666666666664d;
    private static final double MIN__FACTOR = 6.944444444444444E-4d;
    private static final double SEC__FACTOR = 1.1574074074074073E-5d;

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/format/CellElapsedFormatter$TimeSpec.class */
    private static class TimeSpec {
        final char type;
        final int pos;
        final int len;
        final double factor;
        double modBy = 0.0d;

        public TimeSpec(char type, int pos, int len, double factor) {
            this.type = type;
            this.pos = pos;
            this.len = len;
            this.factor = factor;
        }

        public long valueFor(double elapsed) {
            double val;
            if (this.modBy == 0.0d) {
                val = elapsed / this.factor;
            } else {
                val = (elapsed / this.factor) % this.modBy;
            }
            if (this.type == '0') {
                return Math.round(val);
            }
            return (long) val;
        }
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/format/CellElapsedFormatter$ElapsedPartHandler.class */
    private class ElapsedPartHandler implements CellFormatPart.PartHandler {
        private ElapsedPartHandler() {
        }

        @Override // org.apache.poi.ss.format.CellFormatPart.PartHandler
        public String handlePart(Matcher m, String part, CellFormatType type, StringBuffer desc) {
            int pos = desc.length();
            char firstCh = part.charAt(0);
            switch (firstCh) {
                case '\n':
                    return "%n";
                case '\"':
                    part = part.substring(1, part.length() - 1);
                    break;
                case '*':
                    if (part.length() > 1) {
                        part = CellFormatPart.expandChar(part);
                        break;
                    }
                    break;
                case '0':
                case 'h':
                case 'm':
                case 's':
                    String part2 = part.toLowerCase(Locale.ROOT);
                    CellElapsedFormatter.this.assignSpec(part2.charAt(0), pos, part2.length());
                    return part2;
                case '[':
                    if (part.length() >= 3) {
                        if (CellElapsedFormatter.this.topmost != null) {
                            throw new IllegalArgumentException("Duplicate '[' times in format");
                        }
                        String part3 = part.toLowerCase(Locale.ROOT);
                        int specLen = part3.length() - 2;
                        CellElapsedFormatter.this.topmost = CellElapsedFormatter.this.assignSpec(part3.charAt(1), pos, specLen);
                        return part3.substring(1, 1 + specLen);
                    }
                    break;
                case '\\':
                    part = part.substring(1);
                    break;
                case '_':
                    return null;
            }
            return CellElapsedFormatter.PERCENTS.matcher(part).replaceAll("%%");
        }
    }

    public CellElapsedFormatter(String pattern) {
        super(pattern);
        this.specs = new ArrayList();
        StringBuffer desc = CellFormatPart.parseFormat(pattern, CellFormatType.ELAPSED, new ElapsedPartHandler());
        ListIterator<TimeSpec> it = this.specs.listIterator(this.specs.size());
        while (it.hasPrevious()) {
            TimeSpec spec = it.previous();
            desc.replace(spec.pos, spec.pos + spec.len, "%0" + spec.len + DateTokenConverter.CONVERTER_KEY);
            if (spec.type != this.topmost.type) {
                spec.modBy = modFor(spec.type, spec.len);
            }
        }
        this.printfFmt = desc.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public TimeSpec assignSpec(char type, int pos, int len) {
        TimeSpec spec = new TimeSpec(type, pos, len, factorFor(type, len));
        this.specs.add(spec);
        return spec;
    }

    private static double factorFor(char type, int len) {
        switch (type) {
            case '0':
                return SEC__FACTOR / Math.pow(10.0d, len);
            case 'h':
                return HOUR__FACTOR;
            case 'm':
                return MIN__FACTOR;
            case 's':
                return SEC__FACTOR;
            default:
                throw new IllegalArgumentException("Uknown elapsed time spec: " + type);
        }
    }

    private static double modFor(char type, int len) {
        switch (type) {
            case '0':
                return Math.pow(10.0d, len);
            case 'h':
                return 24.0d;
            case 'm':
                return 60.0d;
            case 's':
                return 60.0d;
            default:
                throw new IllegalArgumentException("Uknown elapsed time spec: " + type);
        }
    }

    @Override // org.apache.poi.ss.format.CellFormatter
    public void formatValue(StringBuffer toAppendTo, Object value) {
        double elapsed = ((Number) value).doubleValue();
        if (elapsed < 0.0d) {
            toAppendTo.append('-');
            elapsed = -elapsed;
        }
        Object[] parts = new Long[this.specs.size()];
        for (int i = 0; i < this.specs.size(); i++) {
            parts[i] = Long.valueOf(this.specs.get(i).valueFor(elapsed));
        }
        Formatter formatter = new Formatter(toAppendTo, Locale.ROOT);
        try {
            formatter.format(this.printfFmt, parts);
            formatter.close();
        } catch (Throwable th) {
            formatter.close();
            throw th;
        }
    }

    @Override // org.apache.poi.ss.format.CellFormatter
    public void simpleValue(StringBuffer toAppendTo, Object value) {
        formatValue(toAppendTo, value);
    }
}
