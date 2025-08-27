package org.apache.commons.httpclient;

import java.io.Serializable;
import org.apache.commons.httpclient.util.LangUtils;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/NameValuePair.class */
public class NameValuePair implements Serializable {
    private String name;
    private String value;

    public NameValuePair() {
        this(null, null);
    }

    public NameValuePair(String name, String value) {
        this.name = null;
        this.value = null;
        this.name = name;
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public String toString() {
        return new StringBuffer().append("name=").append(this.name).append(", ").append("value=").append(this.value).toString();
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (object instanceof NameValuePair) {
            NameValuePair that = (NameValuePair) object;
            return LangUtils.equals(this.name, that.name) && LangUtils.equals(this.value, that.value);
        }
        return false;
    }

    public int hashCode() {
        int hash = LangUtils.hashCode(17, this.name);
        return LangUtils.hashCode(hash, this.value);
    }
}
