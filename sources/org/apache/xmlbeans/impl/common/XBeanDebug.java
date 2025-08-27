package org.apache.xmlbeans.impl.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.apache.xmlbeans.SystemProperties;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/XBeanDebug.class */
public class XBeanDebug {
    public static final int TRACE_SCHEMA_LOADING = 1;
    public static final String traceProp = "org.apache.xmlbeans.impl.debug";
    public static final String defaultProp = "";
    private static int _enabled = initializeBitsFromProperty();
    private static int _indent = 0;
    private static String _indentspace = "                                                                                ";
    static PrintStream _err;

    private static int initializeBitsFromProperty() {
        int bits = 0;
        String prop = SystemProperties.getProperty(traceProp, "");
        if (prop.indexOf("TRACE_SCHEMA_LOADING") >= 0) {
            bits = 0 | 1;
        }
        return bits;
    }

    public static void enable(int bits) {
        _enabled |= bits;
    }

    public static void disable(int bits) {
        _enabled &= bits ^ (-1);
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x001f  */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0024 A[Catch: all -> 0x0083, TryCatch #0 {, blocks: (B:8:0x0011, B:9:0x0019, B:16:0x0040, B:18:0x0076, B:20:0x007f, B:12:0x0024, B:14:0x0030, B:15:0x0036), top: B:28:0x0011 }] */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0076 A[Catch: all -> 0x0083, TryCatch #0 {, blocks: (B:8:0x0011, B:9:0x0019, B:16:0x0040, B:18:0x0076, B:20:0x007f, B:12:0x0024, B:14:0x0030, B:15:0x0036), top: B:28:0x0011 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void trace(int r4, java.lang.String r5, int r6) {
        /*
            r0 = r4
            boolean r0 = test(r0)
            if (r0 == 0) goto L8a
            java.lang.Class<org.apache.xmlbeans.impl.common.XBeanDebug> r0 = org.apache.xmlbeans.impl.common.XBeanDebug.class
            r1 = r0
            r7 = r1
            monitor-enter(r0)
            r0 = r6
            if (r0 >= 0) goto L19
            int r0 = org.apache.xmlbeans.impl.common.XBeanDebug._indent     // Catch: java.lang.Throwable -> L83
            r1 = r6
            int r0 = r0 + r1
            org.apache.xmlbeans.impl.common.XBeanDebug._indent = r0     // Catch: java.lang.Throwable -> L83
        L19:
            int r0 = org.apache.xmlbeans.impl.common.XBeanDebug._indent     // Catch: java.lang.Throwable -> L83
            if (r0 >= 0) goto L24
            java.lang.String r0 = ""
            goto L40
        L24:
            int r0 = org.apache.xmlbeans.impl.common.XBeanDebug._indent     // Catch: java.lang.Throwable -> L83
            java.lang.String r1 = org.apache.xmlbeans.impl.common.XBeanDebug._indentspace     // Catch: java.lang.Throwable -> L83
            int r1 = r1.length()     // Catch: java.lang.Throwable -> L83
            if (r0 <= r1) goto L36
            java.lang.String r0 = org.apache.xmlbeans.impl.common.XBeanDebug._indentspace     // Catch: java.lang.Throwable -> L83
            goto L40
        L36:
            java.lang.String r0 = org.apache.xmlbeans.impl.common.XBeanDebug._indentspace     // Catch: java.lang.Throwable -> L83
            r1 = 0
            int r2 = org.apache.xmlbeans.impl.common.XBeanDebug._indent     // Catch: java.lang.Throwable -> L83
            java.lang.String r0 = r0.substring(r1, r2)     // Catch: java.lang.Throwable -> L83
        L40:
            r8 = r0
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L83
            r1 = r0
            r1.<init>()     // Catch: java.lang.Throwable -> L83
            java.lang.Thread r1 = java.lang.Thread.currentThread()     // Catch: java.lang.Throwable -> L83
            java.lang.String r1 = r1.getName()     // Catch: java.lang.Throwable -> L83
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch: java.lang.Throwable -> L83
            java.lang.String r1 = ": "
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch: java.lang.Throwable -> L83
            r1 = r8
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch: java.lang.Throwable -> L83
            r1 = r5
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch: java.lang.Throwable -> L83
            java.lang.String r1 = "\n"
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch: java.lang.Throwable -> L83
            java.lang.String r0 = r0.toString()     // Catch: java.lang.Throwable -> L83
            r9 = r0
            java.io.PrintStream r0 = java.lang.System.err     // Catch: java.lang.Throwable -> L83
            r1 = r9
            r0.print(r1)     // Catch: java.lang.Throwable -> L83
            r0 = r6
            if (r0 <= 0) goto L7e
            int r0 = org.apache.xmlbeans.impl.common.XBeanDebug._indent     // Catch: java.lang.Throwable -> L83
            r1 = r6
            int r0 = r0 + r1
            org.apache.xmlbeans.impl.common.XBeanDebug._indent = r0     // Catch: java.lang.Throwable -> L83
        L7e:
            r0 = r7
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L83
            goto L8a
        L83:
            r10 = move-exception
            r0 = r7
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L83
            r0 = r10
            throw r0
        L8a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.impl.common.XBeanDebug.trace(int, java.lang.String, int):void");
    }

    public static boolean test(int bits) {
        return (_enabled & bits) != 0;
    }

    public static String log(String message) throws IOException {
        log(message, null);
        return message;
    }

    public static String logStackTrace(String message) throws IOException {
        log(message, new Throwable());
        return message;
    }

    private static synchronized String log(String message, Throwable stackTrace) throws IOException {
        if (_err == null) {
            try {
                File diagnosticFile = File.createTempFile("xmlbeandebug", ".log");
                _err = new PrintStream(new FileOutputStream(diagnosticFile));
                System.err.println("Diagnostic XML Bean debug log file created: " + diagnosticFile);
            } catch (IOException e) {
                _err = System.err;
            }
        }
        _err.println(message);
        if (stackTrace != null) {
            stackTrace.printStackTrace(_err);
        }
        return message;
    }

    public static Throwable logException(Throwable t) throws IOException {
        log(t.getMessage(), t);
        return t;
    }
}
