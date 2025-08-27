package org.apache.commons.lang.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.springframework.validation.DataBinder;

/* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/exception/ExceptionUtils.class */
public class ExceptionUtils {
    static final String WRAPPED_MARKER = " [wrapped] ";
    private static final Object CAUSE_METHOD_NAMES_LOCK = new Object();
    private static String[] CAUSE_METHOD_NAMES = {"getCause", "getNextException", "getTargetException", "getException", "getSourceException", "getRootCause", "getCausedByException", "getNested", "getLinkedException", "getNestedException", "getLinkedCause", "getThrowable"};
    private static final Method THROWABLE_CAUSE_METHOD;
    private static final Method THROWABLE_INITCAUSE_METHOD;
    static Class class$java$lang$Throwable;

    static {
        Method causeMethod;
        Method causeMethod2;
        Class clsClass$;
        Class<?> clsClass$2;
        Class clsClass$3;
        try {
            if (class$java$lang$Throwable == null) {
                clsClass$3 = class$("java.lang.Throwable");
                class$java$lang$Throwable = clsClass$3;
            } else {
                clsClass$3 = class$java$lang$Throwable;
            }
            causeMethod = clsClass$3.getMethod("getCause", null);
        } catch (Exception e) {
            causeMethod = null;
        }
        THROWABLE_CAUSE_METHOD = causeMethod;
        try {
            if (class$java$lang$Throwable == null) {
                clsClass$ = class$("java.lang.Throwable");
                class$java$lang$Throwable = clsClass$;
            } else {
                clsClass$ = class$java$lang$Throwable;
            }
            Class<?>[] clsArr = new Class[1];
            if (class$java$lang$Throwable == null) {
                clsClass$2 = class$("java.lang.Throwable");
                class$java$lang$Throwable = clsClass$2;
            } else {
                clsClass$2 = class$java$lang$Throwable;
            }
            clsArr[0] = clsClass$2;
            causeMethod2 = clsClass$.getMethod("initCause", clsArr);
        } catch (Exception e2) {
            causeMethod2 = null;
        }
        THROWABLE_INITCAUSE_METHOD = causeMethod2;
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    public static void addCauseMethodName(String methodName) {
        if (StringUtils.isNotEmpty(methodName) && !isCauseMethodName(methodName)) {
            List list = getCauseMethodNameList();
            if (list.add(methodName)) {
                synchronized (CAUSE_METHOD_NAMES_LOCK) {
                    CAUSE_METHOD_NAMES = toArray(list);
                }
            }
        }
    }

    public static void removeCauseMethodName(String methodName) {
        if (StringUtils.isNotEmpty(methodName)) {
            List list = getCauseMethodNameList();
            if (list.remove(methodName)) {
                synchronized (CAUSE_METHOD_NAMES_LOCK) {
                    CAUSE_METHOD_NAMES = toArray(list);
                }
            }
        }
    }

    public static boolean setCause(Throwable target, Throwable cause) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        Class<?> clsClass$;
        if (target == null) {
            throw new NullArgumentException(DataBinder.DEFAULT_OBJECT_NAME);
        }
        Object[] causeArgs = {cause};
        boolean modifiedTarget = false;
        if (THROWABLE_INITCAUSE_METHOD != null) {
            try {
                THROWABLE_INITCAUSE_METHOD.invoke(target, causeArgs);
                modifiedTarget = true;
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e2) {
            }
        }
        try {
            Class<?> cls = target.getClass();
            Class<?>[] clsArr = new Class[1];
            if (class$java$lang$Throwable == null) {
                clsClass$ = class$("java.lang.Throwable");
                class$java$lang$Throwable = clsClass$;
            } else {
                clsClass$ = class$java$lang$Throwable;
            }
            clsArr[0] = clsClass$;
            Method setCauseMethod = cls.getMethod("setCause", clsArr);
            setCauseMethod.invoke(target, causeArgs);
            modifiedTarget = true;
        } catch (IllegalAccessException e3) {
        } catch (NoSuchMethodException e4) {
        } catch (InvocationTargetException e5) {
        }
        return modifiedTarget;
    }

    private static String[] toArray(List list) {
        return (String[]) list.toArray(new String[list.size()]);
    }

    private static ArrayList getCauseMethodNameList() {
        ArrayList arrayList;
        synchronized (CAUSE_METHOD_NAMES_LOCK) {
            arrayList = new ArrayList(Arrays.asList(CAUSE_METHOD_NAMES));
        }
        return arrayList;
    }

    public static boolean isCauseMethodName(String methodName) {
        boolean z;
        synchronized (CAUSE_METHOD_NAMES_LOCK) {
            z = ArrayUtils.indexOf(CAUSE_METHOD_NAMES, methodName) >= 0;
        }
        return z;
    }

    public static Throwable getCause(Throwable throwable) {
        Throwable cause;
        synchronized (CAUSE_METHOD_NAMES_LOCK) {
            cause = getCause(throwable, CAUSE_METHOD_NAMES);
        }
        return cause;
    }

    public static Throwable getCause(Throwable throwable, String[] methodNames) throws NoSuchFieldException, NoSuchMethodException, SecurityException {
        if (throwable == null) {
            return null;
        }
        Throwable cause = getCauseUsingWellKnownTypes(throwable);
        if (cause == null) {
            if (methodNames == null) {
                synchronized (CAUSE_METHOD_NAMES_LOCK) {
                    methodNames = CAUSE_METHOD_NAMES;
                }
            }
            for (String methodName : methodNames) {
                if (methodName != null) {
                    cause = getCauseUsingMethodName(throwable, methodName);
                    if (cause != null) {
                        break;
                    }
                }
            }
            if (cause == null) {
                cause = getCauseUsingFieldName(throwable, "detail");
            }
        }
        return cause;
    }

    public static Throwable getRootCause(Throwable throwable) {
        List list = getThrowableList(throwable);
        if (list.size() < 2) {
            return null;
        }
        return (Throwable) list.get(list.size() - 1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static Throwable getCauseUsingWellKnownTypes(Throwable th) {
        if (th instanceof Nestable) {
            return ((Nestable) th).getCause();
        }
        if (th instanceof SQLException) {
            return ((SQLException) th).getNextException();
        }
        if (th instanceof InvocationTargetException) {
            return ((InvocationTargetException) th).getTargetException();
        }
        return null;
    }

    private static Throwable getCauseUsingMethodName(Throwable throwable, String methodName) throws NoSuchMethodException, SecurityException {
        Class clsClass$;
        Method method = null;
        try {
            method = throwable.getClass().getMethod(methodName, null);
        } catch (NoSuchMethodException e) {
        } catch (SecurityException e2) {
        }
        if (method == null) {
            return null;
        }
        if (class$java$lang$Throwable == null) {
            clsClass$ = class$("java.lang.Throwable");
            class$java$lang$Throwable = clsClass$;
        } else {
            clsClass$ = class$java$lang$Throwable;
        }
        if (clsClass$.isAssignableFrom(method.getReturnType())) {
            try {
                return (Throwable) method.invoke(throwable, ArrayUtils.EMPTY_OBJECT_ARRAY);
            } catch (IllegalAccessException e3) {
                return null;
            } catch (IllegalArgumentException e4) {
                return null;
            } catch (InvocationTargetException e5) {
                return null;
            }
        }
        return null;
    }

    private static Throwable getCauseUsingFieldName(Throwable throwable, String fieldName) throws NoSuchFieldException {
        Class clsClass$;
        Field field = null;
        try {
            field = throwable.getClass().getField(fieldName);
        } catch (NoSuchFieldException e) {
        } catch (SecurityException e2) {
        }
        if (field == null) {
            return null;
        }
        if (class$java$lang$Throwable == null) {
            clsClass$ = class$("java.lang.Throwable");
            class$java$lang$Throwable = clsClass$;
        } else {
            clsClass$ = class$java$lang$Throwable;
        }
        if (clsClass$.isAssignableFrom(field.getType())) {
            try {
                return (Throwable) field.get(throwable);
            } catch (IllegalAccessException e3) {
                return null;
            } catch (IllegalArgumentException e4) {
                return null;
            }
        }
        return null;
    }

    public static boolean isThrowableNested() {
        return THROWABLE_CAUSE_METHOD != null;
    }

    public static boolean isNestedThrowable(Throwable throwable) throws NoSuchFieldException {
        Class clsClass$;
        if (throwable == null) {
            return false;
        }
        if ((throwable instanceof Nestable) || (throwable instanceof SQLException) || (throwable instanceof InvocationTargetException) || isThrowableNested()) {
            return true;
        }
        Class cls = throwable.getClass();
        synchronized (CAUSE_METHOD_NAMES_LOCK) {
            int isize = CAUSE_METHOD_NAMES.length;
            for (int i = 0; i < isize; i++) {
                try {
                    Method method = cls.getMethod(CAUSE_METHOD_NAMES[i], null);
                    if (method != null) {
                        if (class$java$lang$Throwable == null) {
                            clsClass$ = class$("java.lang.Throwable");
                            class$java$lang$Throwable = clsClass$;
                        } else {
                            clsClass$ = class$java$lang$Throwable;
                        }
                        if (clsClass$.isAssignableFrom(method.getReturnType())) {
                            return true;
                        }
                    }
                } catch (NoSuchMethodException e) {
                } catch (SecurityException e2) {
                }
            }
            try {
                Field field = cls.getField("detail");
                if (field != null) {
                    return true;
                }
                return false;
            } catch (NoSuchFieldException e3) {
                return false;
            } catch (SecurityException e4) {
                return false;
            }
        }
    }

    public static int getThrowableCount(Throwable throwable) {
        return getThrowableList(throwable).size();
    }

    public static Throwable[] getThrowables(Throwable throwable) {
        List list = getThrowableList(throwable);
        return (Throwable[]) list.toArray(new Throwable[list.size()]);
    }

    public static List getThrowableList(Throwable throwable) {
        List list = new ArrayList();
        while (throwable != null && !list.contains(throwable)) {
            list.add(throwable);
            throwable = getCause(throwable);
        }
        return list;
    }

    public static int indexOfThrowable(Throwable throwable, Class clazz) {
        return indexOf(throwable, clazz, 0, false);
    }

    public static int indexOfThrowable(Throwable throwable, Class clazz, int fromIndex) {
        return indexOf(throwable, clazz, fromIndex, false);
    }

    public static int indexOfType(Throwable throwable, Class type) {
        return indexOf(throwable, type, 0, true);
    }

    public static int indexOfType(Throwable throwable, Class type, int fromIndex) {
        return indexOf(throwable, type, fromIndex, true);
    }

    private static int indexOf(Throwable throwable, Class type, int fromIndex, boolean subclass) {
        if (throwable == null || type == null) {
            return -1;
        }
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        Throwable[] throwables = getThrowables(throwable);
        if (fromIndex >= throwables.length) {
            return -1;
        }
        if (subclass) {
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

    public static void printRootCauseStackTrace(Throwable throwable) {
        printRootCauseStackTrace(throwable, System.err);
    }

    public static void printRootCauseStackTrace(Throwable throwable, PrintStream stream) {
        if (throwable == null) {
            return;
        }
        if (stream == null) {
            throw new IllegalArgumentException("The PrintStream must not be null");
        }
        String[] trace = getRootCauseStackTrace(throwable);
        for (String str : trace) {
            stream.println(str);
        }
        stream.flush();
    }

    public static void printRootCauseStackTrace(Throwable throwable, PrintWriter writer) {
        if (throwable == null) {
            return;
        }
        if (writer == null) {
            throw new IllegalArgumentException("The PrintWriter must not be null");
        }
        String[] trace = getRootCauseStackTrace(throwable);
        for (String str : trace) {
            writer.println(str);
        }
        writer.flush();
    }

    public static String[] getRootCauseStackTrace(Throwable throwable) {
        if (throwable == null) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        Throwable[] throwables = getThrowables(throwable);
        int count = throwables.length;
        ArrayList frames = new ArrayList();
        List nextTrace = getStackFrameList(throwables[count - 1]);
        int i = count;
        while (true) {
            i--;
            if (i >= 0) {
                List trace = nextTrace;
                if (i != 0) {
                    nextTrace = getStackFrameList(throwables[i - 1]);
                    removeCommonFrames(trace, nextTrace);
                }
                if (i == count - 1) {
                    frames.add(throwables[i].toString());
                } else {
                    frames.add(new StringBuffer().append(WRAPPED_MARKER).append(throwables[i].toString()).toString());
                }
                for (int j = 0; j < trace.size(); j++) {
                    frames.add(trace.get(j));
                }
            } else {
                return (String[]) frames.toArray(new String[0]);
            }
        }
    }

    public static void removeCommonFrames(List causeFrames, List wrapperFrames) {
        if (causeFrames == null || wrapperFrames == null) {
            throw new IllegalArgumentException("The List must not be null");
        }
        int causeFrameIndex = causeFrames.size() - 1;
        for (int wrapperFrameIndex = wrapperFrames.size() - 1; causeFrameIndex >= 0 && wrapperFrameIndex >= 0; wrapperFrameIndex--) {
            String causeFrame = (String) causeFrames.get(causeFrameIndex);
            String wrapperFrame = (String) wrapperFrames.get(wrapperFrameIndex);
            if (causeFrame.equals(wrapperFrame)) {
                causeFrames.remove(causeFrameIndex);
            }
            causeFrameIndex--;
        }
    }

    public static String getFullStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter((Writer) sw, true);
        Throwable[] ts = getThrowables(throwable);
        for (int i = 0; i < ts.length; i++) {
            ts[i].printStackTrace(pw);
            if (isNestedThrowable(ts[i])) {
                break;
            }
        }
        return sw.getBuffer().toString();
    }

    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter((Writer) sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

    public static String[] getStackFrames(Throwable throwable) {
        if (throwable == null) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        return getStackFrames(getStackTrace(throwable));
    }

    static String[] getStackFrames(String stackTrace) {
        String linebreak = SystemUtils.LINE_SEPARATOR;
        StringTokenizer frames = new StringTokenizer(stackTrace, linebreak);
        List list = new ArrayList();
        while (frames.hasMoreTokens()) {
            list.add(frames.nextToken());
        }
        return toArray(list);
    }

    static List getStackFrameList(Throwable t) {
        String stackTrace = getStackTrace(t);
        String linebreak = SystemUtils.LINE_SEPARATOR;
        StringTokenizer frames = new StringTokenizer(stackTrace, linebreak);
        List list = new ArrayList();
        boolean traceStarted = false;
        while (frames.hasMoreTokens()) {
            String token = frames.nextToken();
            int at = token.indexOf("at");
            if (at != -1 && token.substring(0, at).trim().length() == 0) {
                traceStarted = true;
                list.add(token);
            } else if (traceStarted) {
                break;
            }
        }
        return list;
    }

    public static String getMessage(Throwable th) {
        if (th == null) {
            return "";
        }
        String clsName = ClassUtils.getShortClassName(th, null);
        String msg = th.getMessage();
        return new StringBuffer().append(clsName).append(": ").append(StringUtils.defaultString(msg)).toString();
    }

    public static String getRootCauseMessage(Throwable th) {
        Throwable root = getRootCause(th);
        return getMessage(root == null ? th : root);
    }
}
