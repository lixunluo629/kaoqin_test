package ch.qos.logback.classic.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/* loaded from: logback-classic-1.2.3.jar.bak:ch/qos/logback/classic/util/JNDIUtil.class */
public class JNDIUtil {
    public static Context getInitialContext() throws NamingException {
        return new InitialContext();
    }

    public static String lookup(Context ctx, String name) {
        if (ctx == null) {
            return null;
        }
        try {
            Object lookup = ctx.lookup(name);
            if (lookup == null) {
                return null;
            }
            return lookup.toString();
        } catch (NamingException e) {
            return null;
        }
    }
}
