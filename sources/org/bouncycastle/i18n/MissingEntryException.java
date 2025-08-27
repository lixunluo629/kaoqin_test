package org.bouncycastle.i18n;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/i18n/MissingEntryException.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/i18n/MissingEntryException.class */
public class MissingEntryException extends RuntimeException {
    protected final String resource;
    protected final String key;
    protected final ClassLoader loader;
    protected final Locale locale;
    private String debugMsg;

    public MissingEntryException(String str, String str2, String str3, Locale locale, ClassLoader classLoader) {
        super(str);
        this.resource = str2;
        this.key = str3;
        this.locale = locale;
        this.loader = classLoader;
    }

    public MissingEntryException(String str, Throwable th, String str2, String str3, Locale locale, ClassLoader classLoader) {
        super(str, th);
        this.resource = str2;
        this.key = str3;
        this.locale = locale;
        this.loader = classLoader;
    }

    public String getKey() {
        return this.key;
    }

    public String getResource() {
        return this.resource;
    }

    public ClassLoader getClassLoader() {
        return this.loader;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public String getDebugMsg() {
        if (this.debugMsg == null) {
            this.debugMsg = "Can not find entry " + this.key + " in resource file " + this.resource + " for the locale " + this.locale + ".";
            if (this.loader instanceof URLClassLoader) {
                URL[] uRLs = ((URLClassLoader) this.loader).getURLs();
                this.debugMsg += " The following entries in the classpath were searched: ";
                for (int i = 0; i != uRLs.length; i++) {
                    this.debugMsg += uRLs[i] + SymbolConstants.SPACE_SYMBOL;
                }
            }
        }
        return this.debugMsg;
    }
}
