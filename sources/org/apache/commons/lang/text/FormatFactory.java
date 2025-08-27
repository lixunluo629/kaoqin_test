package org.apache.commons.lang.text;

import java.text.Format;
import java.util.Locale;

/* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/text/FormatFactory.class */
public interface FormatFactory {
    Format getFormat(String str, String str2, Locale locale);
}
