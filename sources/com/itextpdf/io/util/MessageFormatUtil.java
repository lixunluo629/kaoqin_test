package com.itextpdf.io.util;

import java.text.MessageFormat;
import java.util.Locale;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/util/MessageFormatUtil.class */
public class MessageFormatUtil {
    public static String format(String pattern, Object... arguments) {
        return new MessageFormat(pattern, Locale.ROOT).format(arguments);
    }
}
