package org.apache.xmlbeans.xml.stream.utils;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import org.springframework.beans.PropertyAccessor;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/xml/stream/utils/NestedThrowable.class */
public interface NestedThrowable {
    Throwable getNested();

    String superToString();

    void superPrintStackTrace(PrintStream printStream);

    void superPrintStackTrace(PrintWriter printWriter);

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/xml/stream/utils/NestedThrowable$Util.class */
    public static class Util {
        private static String EOL = System.getProperty("line.separator");

        public static String toString(NestedThrowable nt) {
            Throwable nested = nt.getNested();
            if (nested == null) {
                return nt.superToString();
            }
            return nt.superToString() + " - with nested exception:" + EOL + PropertyAccessor.PROPERTY_KEY_PREFIX + nestedToString(nested) + "]";
        }

        private static String nestedToString(Throwable nested) {
            if (nested instanceof InvocationTargetException) {
                InvocationTargetException ite = (InvocationTargetException) nested;
                return nested.toString() + " - with target exception:" + EOL + PropertyAccessor.PROPERTY_KEY_PREFIX + ite.getTargetException().toString() + "]";
            }
            return nested.toString();
        }

        public static void printStackTrace(NestedThrowable nt, PrintStream s) {
            Throwable nested = nt.getNested();
            if (nested != null) {
                nested.printStackTrace(s);
                s.println("--------------- nested within: ------------------");
            }
            nt.superPrintStackTrace(s);
        }

        public static void printStackTrace(NestedThrowable nt, PrintWriter w) {
            Throwable nested = nt.getNested();
            if (nested != null) {
                nested.printStackTrace(w);
                w.println("--------------- nested within: ------------------");
            }
            nt.superPrintStackTrace(w);
        }
    }
}
