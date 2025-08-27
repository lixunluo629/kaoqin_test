package org.aspectj.weaver.tools;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.File;
import java.lang.reflect.Array;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Pattern;
import org.aspectj.bridge.IMessage;
import org.springframework.beans.PropertyAccessor;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/AbstractTrace.class */
public abstract class AbstractTrace implements Trace {
    private static final Pattern packagePrefixPattern = Pattern.compile("([^.])[^.]*(\\.)");
    protected Class<?> tracedClass;
    private static SimpleDateFormat timeFormat;

    @Override // org.aspectj.weaver.tools.Trace
    public abstract void enter(String str, Object obj, Object[] objArr);

    @Override // org.aspectj.weaver.tools.Trace
    public abstract void enter(String str, Object obj);

    @Override // org.aspectj.weaver.tools.Trace
    public abstract void exit(String str, Object obj);

    @Override // org.aspectj.weaver.tools.Trace
    public abstract void exit(String str, Throwable th);

    protected AbstractTrace(Class clazz) {
        this.tracedClass = clazz;
    }

    public void enter(String methodName) {
        enter(methodName, (Object) null, (Object[]) null);
    }

    @Override // org.aspectj.weaver.tools.Trace
    public void enter(String methodName, Object thiz, Object arg) {
        enter(methodName, thiz, new Object[]{arg});
    }

    @Override // org.aspectj.weaver.tools.Trace
    public void enter(String methodName, Object thiz, boolean z) {
        enter(methodName, thiz, new Boolean(z));
    }

    @Override // org.aspectj.weaver.tools.Trace
    public void exit(String methodName, boolean b) {
        exit(methodName, new Boolean(b));
    }

    @Override // org.aspectj.weaver.tools.Trace
    public void exit(String methodName, int i) {
        exit(methodName, new Integer(i));
    }

    @Override // org.aspectj.weaver.tools.Trace
    public void event(String methodName, Object thiz, Object arg) {
        event(methodName, thiz, new Object[]{arg});
    }

    @Override // org.aspectj.weaver.tools.Trace
    public void warn(String message) {
        warn(message, null);
    }

    @Override // org.aspectj.weaver.tools.Trace
    public void error(String message) {
        error(message, null);
    }

    @Override // org.aspectj.weaver.tools.Trace
    public void fatal(String message) {
        fatal(message, null);
    }

    protected String formatMessage(String kind, String className, String methodName, Object thiz, Object[] args) {
        StringBuffer message = new StringBuffer();
        Date now = new Date();
        message.append(formatDate(now)).append(SymbolConstants.SPACE_SYMBOL);
        message.append(Thread.currentThread().getName()).append(SymbolConstants.SPACE_SYMBOL);
        message.append(kind).append(SymbolConstants.SPACE_SYMBOL);
        message.append(formatClassName(className));
        message.append(".").append(methodName);
        if (thiz != null) {
            message.append(SymbolConstants.SPACE_SYMBOL).append(formatObj(thiz));
        }
        if (args != null) {
            message.append(SymbolConstants.SPACE_SYMBOL).append(formatArgs(args));
        }
        return message.toString();
    }

    private String formatClassName(String className) {
        return packagePrefixPattern.matcher(className).replaceAll("$1.");
    }

    protected String formatMessage(String kind, String text, Throwable th) {
        StringBuffer message = new StringBuffer();
        Date now = new Date();
        message.append(formatDate(now)).append(SymbolConstants.SPACE_SYMBOL);
        message.append(Thread.currentThread().getName()).append(SymbolConstants.SPACE_SYMBOL);
        message.append(kind).append(SymbolConstants.SPACE_SYMBOL);
        message.append(text);
        if (th != null) {
            message.append(SymbolConstants.SPACE_SYMBOL).append(formatObj(th));
        }
        return message.toString();
    }

    private static String formatDate(Date date) {
        if (timeFormat == null) {
            timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");
        }
        return timeFormat.format(date);
    }

    protected Object formatObj(Object obj) {
        if (obj == null || (obj instanceof String) || (obj instanceof Number) || (obj instanceof Boolean) || (obj instanceof Exception) || (obj instanceof Character) || (obj instanceof Class) || (obj instanceof File) || (obj instanceof StringBuffer) || (obj instanceof URL) || (obj instanceof IMessage.Kind)) {
            return obj;
        }
        if (obj.getClass().isArray()) {
            return formatArray(obj);
        }
        if (obj instanceof Collection) {
            return formatCollection((Collection) obj);
        }
        try {
            if (obj instanceof Traceable) {
                return ((Traceable) obj).toTraceString();
            }
            return formatClassName(obj.getClass().getName()) + "@" + Integer.toHexString(System.identityHashCode(obj));
        } catch (Exception e) {
            return obj.getClass().getName() + "@FFFFFFFF";
        }
    }

    protected String formatArray(Object obj) {
        return obj.getClass().getComponentType().getName() + PropertyAccessor.PROPERTY_KEY_PREFIX + Array.getLength(obj) + "]";
    }

    protected String formatCollection(Collection<?> c) {
        return c.getClass().getName() + "(" + c.size() + ")";
    }

    protected String formatArgs(Object[] args) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < args.length; i++) {
            sb.append(formatObj(args[i]));
            if (i < args.length - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    protected Object[] formatObjects(Object[] args) {
        for (int i = 0; i < args.length; i++) {
            args[i] = formatObj(args[i]);
        }
        return args;
    }
}
