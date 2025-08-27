package org.aspectj.bridge;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/bridge/Version.class */
public class Version {
    public static final String DEVELOPMENT = "DEVELOPMENT";
    public static final long NOTIME = 0;
    public static final String text = "1.8.14";
    public static final String time_text = "Wednesday Mar 6, 2019 at 20:45:28 GMT";
    private static long time = -1;
    public static final String SIMPLE_DATE_FORMAT = "EEEE MMM d, yyyy 'at' HH:mm:ss z";

    public static long getTime() {
        if (time == -1) {
            long foundTime = 0;
            try {
                SimpleDateFormat format = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
                ParsePosition pos = new ParsePosition(0);
                Date date = format.parse(time_text, pos);
                if (date != null) {
                    foundTime = date.getTime();
                }
            } catch (Throwable th) {
            }
            time = foundTime;
        }
        return time;
    }

    public static void main(String[] args) {
        if (null != args && 0 < args.length && !text.equals(args[0])) {
            System.err.println("version expected: \"" + args[0] + "\" actual=\"" + text + SymbolConstants.QUOTES_SYMBOL);
        }
    }
}
