package org.apache.commons.httpclient.params;

import java.io.Serializable;
import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/params/DefaultHttpParams.class */
public class DefaultHttpParams implements HttpParams, Serializable, Cloneable {
    private static final Log LOG;
    private static HttpParamsFactory httpParamsFactory;
    private HttpParams defaults;
    private HashMap parameters;
    static Class class$org$apache$commons$httpclient$params$DefaultHttpParams;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        if (class$org$apache$commons$httpclient$params$DefaultHttpParams == null) {
            clsClass$ = class$("org.apache.commons.httpclient.params.DefaultHttpParams");
            class$org$apache$commons$httpclient$params$DefaultHttpParams = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$httpclient$params$DefaultHttpParams;
        }
        LOG = LogFactory.getLog(clsClass$);
        httpParamsFactory = new DefaultHttpParamsFactory();
    }

    public static HttpParams getDefaultParams() {
        return httpParamsFactory.getDefaultParams();
    }

    public static void setHttpParamsFactory(HttpParamsFactory httpParamsFactory2) {
        if (httpParamsFactory2 == null) {
            throw new IllegalArgumentException("httpParamsFactory may not be null");
        }
        httpParamsFactory = httpParamsFactory2;
    }

    public DefaultHttpParams(HttpParams defaults) {
        this.defaults = null;
        this.parameters = null;
        this.defaults = defaults;
    }

    public DefaultHttpParams() {
        this(getDefaultParams());
    }

    @Override // org.apache.commons.httpclient.params.HttpParams
    public synchronized HttpParams getDefaults() {
        return this.defaults;
    }

    @Override // org.apache.commons.httpclient.params.HttpParams
    public synchronized void setDefaults(HttpParams params) {
        this.defaults = params;
    }

    @Override // org.apache.commons.httpclient.params.HttpParams
    public synchronized Object getParameter(String name) {
        Object param = null;
        if (this.parameters != null) {
            param = this.parameters.get(name);
        }
        if (param != null) {
            return param;
        }
        if (this.defaults != null) {
            return this.defaults.getParameter(name);
        }
        return null;
    }

    @Override // org.apache.commons.httpclient.params.HttpParams
    public synchronized void setParameter(String name, Object value) {
        if (this.parameters == null) {
            this.parameters = new HashMap();
        }
        this.parameters.put(name, value);
        if (LOG.isDebugEnabled()) {
            LOG.debug(new StringBuffer().append("Set parameter ").append(name).append(" = ").append(value).toString());
        }
    }

    public synchronized void setParameters(String[] names, Object value) {
        for (String str : names) {
            setParameter(str, value);
        }
    }

    @Override // org.apache.commons.httpclient.params.HttpParams
    public long getLongParameter(String name, long defaultValue) {
        Object param = getParameter(name);
        if (param == null) {
            return defaultValue;
        }
        return ((Long) param).longValue();
    }

    @Override // org.apache.commons.httpclient.params.HttpParams
    public void setLongParameter(String name, long value) {
        setParameter(name, new Long(value));
    }

    @Override // org.apache.commons.httpclient.params.HttpParams
    public int getIntParameter(String name, int defaultValue) {
        Object param = getParameter(name);
        if (param == null) {
            return defaultValue;
        }
        return ((Integer) param).intValue();
    }

    @Override // org.apache.commons.httpclient.params.HttpParams
    public void setIntParameter(String name, int value) {
        setParameter(name, new Integer(value));
    }

    @Override // org.apache.commons.httpclient.params.HttpParams
    public double getDoubleParameter(String name, double defaultValue) {
        Object param = getParameter(name);
        if (param == null) {
            return defaultValue;
        }
        return ((Double) param).doubleValue();
    }

    @Override // org.apache.commons.httpclient.params.HttpParams
    public void setDoubleParameter(String name, double value) {
        setParameter(name, new Double(value));
    }

    @Override // org.apache.commons.httpclient.params.HttpParams
    public boolean getBooleanParameter(String name, boolean defaultValue) {
        Object param = getParameter(name);
        if (param == null) {
            return defaultValue;
        }
        return ((Boolean) param).booleanValue();
    }

    @Override // org.apache.commons.httpclient.params.HttpParams
    public void setBooleanParameter(String name, boolean value) {
        setParameter(name, value ? Boolean.TRUE : Boolean.FALSE);
    }

    @Override // org.apache.commons.httpclient.params.HttpParams
    public boolean isParameterSet(String name) {
        return getParameter(name) != null;
    }

    @Override // org.apache.commons.httpclient.params.HttpParams
    public boolean isParameterSetLocally(String name) {
        return (this.parameters == null || this.parameters.get(name) == null) ? false : true;
    }

    @Override // org.apache.commons.httpclient.params.HttpParams
    public boolean isParameterTrue(String name) {
        return getBooleanParameter(name, false);
    }

    @Override // org.apache.commons.httpclient.params.HttpParams
    public boolean isParameterFalse(String name) {
        return !getBooleanParameter(name, false);
    }

    public void clear() {
        this.parameters = null;
    }

    public Object clone() throws CloneNotSupportedException {
        DefaultHttpParams clone = (DefaultHttpParams) super.clone();
        if (this.parameters != null) {
            clone.parameters = (HashMap) this.parameters.clone();
        }
        clone.setDefaults(this.defaults);
        return clone;
    }
}
