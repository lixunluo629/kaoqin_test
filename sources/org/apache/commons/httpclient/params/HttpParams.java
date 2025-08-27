package org.apache.commons.httpclient.params;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/params/HttpParams.class */
public interface HttpParams {
    HttpParams getDefaults();

    void setDefaults(HttpParams httpParams);

    Object getParameter(String str);

    void setParameter(String str, Object obj);

    long getLongParameter(String str, long j);

    void setLongParameter(String str, long j);

    int getIntParameter(String str, int i);

    void setIntParameter(String str, int i);

    double getDoubleParameter(String str, double d);

    void setDoubleParameter(String str, double d);

    boolean getBooleanParameter(String str, boolean z);

    void setBooleanParameter(String str, boolean z);

    boolean isParameterSet(String str);

    boolean isParameterSetLocally(String str);

    boolean isParameterTrue(String str);

    boolean isParameterFalse(String str);
}
