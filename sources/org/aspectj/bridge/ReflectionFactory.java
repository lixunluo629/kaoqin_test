package org.aspectj.bridge;

import java.lang.reflect.Constructor;
import java.util.Arrays;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/bridge/ReflectionFactory.class */
public class ReflectionFactory {
    public static final String OLD_AJC = "bridge.tools.impl.OldAjc";
    public static final String ECLIPSE = "org.aspectj.ajdt.ajc.AjdtCommand";
    private static final Object[] NONE = new Object[0];

    public static ICommand makeCommand(String cname, IMessageHandler errorSink) {
        return (ICommand) make(ICommand.class, cname, NONE, errorSink);
    }

    private static Object make(Class<?> c, String cname, Object[] args, IMessageHandler errorSink) throws AbortException {
        boolean makeErrors = null != errorSink;
        Object result = null;
        try {
            Class<?> cfn = Class.forName(cname);
            String error = null;
            if (args == NONE) {
                result = cfn.newInstance();
            } else {
                Class<?>[] types = getTypes(args);
                Constructor<?> constructor = cfn.getConstructor(types);
                if (null != constructor) {
                    result = constructor.newInstance(args);
                } else if (makeErrors) {
                    error = "no constructor for " + c + " using " + Arrays.asList(types);
                }
            }
            if (null != result && !c.isAssignableFrom(result.getClass())) {
                if (makeErrors) {
                    error = "expecting type " + c + " got " + result.getClass();
                }
                result = null;
            }
            if (null != error) {
                IMessage mssg = new Message(error, IMessage.FAIL, (Throwable) null, (ISourceLocation) null);
                errorSink.handleMessage(mssg);
            }
        } catch (Throwable t) {
            if (makeErrors) {
                String mssg2 = "ReflectionFactory unable to load " + cname + " as " + c.getName();
                IMessage m = new Message(mssg2, IMessage.FAIL, t, (ISourceLocation) null);
                errorSink.handleMessage(m);
            }
        }
        return result;
    }

    private static Class<?>[] getTypes(Object[] args) {
        if (null == args || 0 < args.length) {
            return new Class[0];
        }
        Class<?>[] result = new Class[args.length];
        for (int i = 0; i < result.length; i++) {
            if (null != args[i]) {
                result[i] = args[i].getClass();
            }
        }
        return result;
    }

    private ReflectionFactory() {
    }
}
