package org.apache.ibatis.javassist.tools.web;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/tools/web/BadHttpRequest.class */
public class BadHttpRequest extends Exception {
    private Exception e;

    public BadHttpRequest() {
        this.e = null;
    }

    public BadHttpRequest(Exception _e) {
        this.e = _e;
    }

    @Override // java.lang.Throwable
    public String toString() {
        if (this.e == null) {
            return super.toString();
        }
        return this.e.toString();
    }
}
