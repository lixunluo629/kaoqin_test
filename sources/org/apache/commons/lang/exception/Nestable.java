package org.apache.commons.lang.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/exception/Nestable.class */
public interface Nestable {
    Throwable getCause();

    String getMessage();

    String getMessage(int i);

    String[] getMessages();

    Throwable getThrowable(int i);

    int getThrowableCount();

    Throwable[] getThrowables();

    int indexOfThrowable(Class cls);

    int indexOfThrowable(Class cls, int i);

    void printStackTrace(PrintWriter printWriter);

    void printStackTrace(PrintStream printStream);

    void printPartialStackTrace(PrintWriter printWriter);
}
