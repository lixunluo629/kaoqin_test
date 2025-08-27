package org.apache.commons.httpclient;

import org.apache.commons.httpclient.util.LangUtils;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/UsernamePasswordCredentials.class */
public class UsernamePasswordCredentials implements Credentials {
    private String userName;
    private String password;

    public UsernamePasswordCredentials() {
    }

    public UsernamePasswordCredentials(String usernamePassword) {
        if (usernamePassword == null) {
            throw new IllegalArgumentException("Username:password string may not be null");
        }
        int atColon = usernamePassword.indexOf(58);
        if (atColon >= 0) {
            this.userName = usernamePassword.substring(0, atColon);
            this.password = usernamePassword.substring(atColon + 1);
        } else {
            this.userName = usernamePassword;
        }
    }

    public UsernamePasswordCredentials(String userName, String password) {
        if (userName == null) {
            throw new IllegalArgumentException("Username may not be null");
        }
        this.userName = userName;
        this.password = password;
    }

    public void setUserName(String userName) {
        if (userName == null) {
            throw new IllegalArgumentException("Username may not be null");
        }
        this.userName = userName;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append(this.userName);
        result.append(":");
        result.append(this.password == null ? "null" : this.password);
        return result.toString();
    }

    public int hashCode() {
        int hash = LangUtils.hashCode(17, this.userName);
        return LangUtils.hashCode(hash, this.password);
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (getClass().equals(o.getClass())) {
            UsernamePasswordCredentials that = (UsernamePasswordCredentials) o;
            if (LangUtils.equals(this.userName, that.userName) && LangUtils.equals(this.password, that.password)) {
                return true;
            }
            return false;
        }
        return false;
    }
}
