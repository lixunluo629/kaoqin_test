package org.springframework.format.number;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/format/number/PercentStyleFormatter.class */
public class PercentStyleFormatter extends AbstractNumberFormatter {
    @Override // org.springframework.format.number.AbstractNumberFormatter
    protected NumberFormat getNumberFormat(Locale locale) {
        NumberFormat format = NumberFormat.getPercentInstance(locale);
        if (format instanceof DecimalFormat) {
            ((DecimalFormat) format).setParseBigDecimal(true);
        }
        return format;
    }
}
