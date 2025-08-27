package org.apache.commons.lang.exception;

import ch.qos.logback.core.CoreConstants;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/exception/NestableDelegate.class */
public class NestableDelegate implements Serializable {
    private static final long serialVersionUID = 1;
    private static final transient String MUST_BE_THROWABLE = "The Nestable implementation passed to the NestableDelegate(Nestable) constructor must extend java.lang.Throwable";
    private Throwable nestable;
    public static boolean topDown = true;
    public static boolean trimStackFrames = true;
    public static boolean matchSubclasses = true;
    static Class class$org$apache$commons$lang$exception$Nestable;

    /* JADX WARN: Multi-variable type inference failed */
    public NestableDelegate(Nestable nestable) {
        this.nestable = null;
        if (nestable instanceof Throwable) {
            this.nestable = (Throwable) nestable;
            return;
        }
        throw new IllegalArgumentException(MUST_BE_THROWABLE);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public String getMessage(int i) {
        Class cls;
        Throwable throwable = getThrowable(i);
        if (class$org$apache$commons$lang$exception$Nestable == null) {
            Class clsClass$ = class$("org.apache.commons.lang.exception.Nestable");
            class$org$apache$commons$lang$exception$Nestable = clsClass$;
            cls = clsClass$;
        } else {
            cls = class$org$apache$commons$lang$exception$Nestable;
        }
        if (cls.isInstance(throwable)) {
            return ((Nestable) throwable).getMessage(0);
        }
        return throwable.getMessage();
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    public String getMessage(String baseMsg) {
        Throwable nestedCause = ExceptionUtils.getCause(this.nestable);
        String causeMsg = nestedCause == null ? null : nestedCause.getMessage();
        if (nestedCause == null || causeMsg == null) {
            return baseMsg;
        }
        if (baseMsg == null) {
            return causeMsg;
        }
        return new StringBuffer().append(baseMsg).append(": ").append(causeMsg).toString();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public String[] getMessages() {
        Class cls;
        Throwable[] throwables = getThrowables();
        String[] strArr = new String[throwables.length];
        for (int i = 0; i < throwables.length; i++) {
            int i2 = i;
            if (class$org$apache$commons$lang$exception$Nestable == null) {
                Class clsClass$ = class$("org.apache.commons.lang.exception.Nestable");
                class$org$apache$commons$lang$exception$Nestable = clsClass$;
                cls = clsClass$;
            } else {
                cls = class$org$apache$commons$lang$exception$Nestable;
            }
            strArr[i2] = cls.isInstance(throwables[i]) ? ((Nestable) throwables[i]).getMessage(0) : throwables[i].getMessage();
        }
        return strArr;
    }

    public Throwable getThrowable(int index) {
        if (index == 0) {
            return this.nestable;
        }
        Throwable[] throwables = getThrowables();
        return throwables[index];
    }

    public int getThrowableCount() {
        return ExceptionUtils.getThrowableCount(this.nestable);
    }

    public Throwable[] getThrowables() {
        return ExceptionUtils.getThrowables(this.nestable);
    }

    public int indexOfThrowable(Class type, int fromIndex) {
        if (type == null) {
            return -1;
        }
        if (fromIndex < 0) {
            throw new IndexOutOfBoundsException(new StringBuffer().append("The start index was out of bounds: ").append(fromIndex).toString());
        }
        Throwable[] throwables = ExceptionUtils.getThrowables(this.nestable);
        if (fromIndex >= throwables.length) {
            throw new IndexOutOfBoundsException(new StringBuffer().append("The start index was out of bounds: ").append(fromIndex).append(" >= ").append(throwables.length).toString());
        }
        if (matchSubclasses) {
            for (int i = fromIndex; i < throwables.length; i++) {
                if (type.isAssignableFrom(throwables[i].getClass())) {
                    return i;
                }
            }
            return -1;
        }
        for (int i2 = fromIndex; i2 < throwables.length; i2++) {
            if (type.equals(throwables[i2].getClass())) {
                return i2;
            }
        }
        return -1;
    }

    public void printStackTrace() {
        printStackTrace(System.err);
    }

    public void printStackTrace(PrintStream out) {
        synchronized (out) {
            PrintWriter pw = new PrintWriter((OutputStream) out, false);
            printStackTrace(pw);
            pw.flush();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void printStackTrace(PrintWriter printWriter) {
        Throwable th = this.nestable;
        if (ExceptionUtils.isThrowableNested()) {
            if (th instanceof Nestable) {
                ((Nestable) th).printPartialStackTrace(printWriter);
                return;
            } else {
                th.printStackTrace(printWriter);
                return;
            }
        }
        ArrayList arrayList = new ArrayList();
        for (Throwable cause = th; cause != false; cause = ExceptionUtils.getCause(cause)) {
            arrayList.add(getStackFrames(cause));
        }
        String str = CoreConstants.CAUSED_BY;
        if (!topDown) {
            str = "Rethrown as: ";
            Collections.reverse(arrayList);
        }
        if (trimStackFrames) {
            trimStackFrames(arrayList);
        }
        synchronized (printWriter) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                for (String str2 : (String[]) it.next()) {
                    printWriter.println(str2);
                }
                if (it.hasNext()) {
                    printWriter.print(str);
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected String[] getStackFrames(Throwable th) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter((Writer) sw, true);
        if (th instanceof Nestable) {
            ((Nestable) th).printPartialStackTrace(pw);
        } else {
            th.printStackTrace(pw);
        }
        return ExceptionUtils.getStackFrames(sw.getBuffer().toString());
    }

    protected void trimStackFrames(List stacks) {
        int size = stacks.size();
        for (int i = size - 1; i > 0; i--) {
            String[] curr = (String[]) stacks.get(i);
            String[] next = (String[]) stacks.get(i - 1);
            List currList = new ArrayList(Arrays.asList(curr));
            List nextList = new ArrayList(Arrays.asList(next));
            ExceptionUtils.removeCommonFrames(currList, nextList);
            int trimmed = curr.length - currList.size();
            if (trimmed > 0) {
                currList.add(new StringBuffer().append("\t... ").append(trimmed).append(" more").toString());
                stacks.set(i, currList.toArray(new String[currList.size()]));
            }
        }
    }
}
