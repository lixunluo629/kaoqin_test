package org.apache.catalina.core;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestWrapper;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/core/ApplicationRequest.class */
class ApplicationRequest extends ServletRequestWrapper {
    protected static final String[] specials = {"javax.servlet.include.request_uri", "javax.servlet.include.context_path", "javax.servlet.include.servlet_path", "javax.servlet.include.path_info", "javax.servlet.include.query_string", "javax.servlet.forward.request_uri", "javax.servlet.forward.context_path", "javax.servlet.forward.servlet_path", "javax.servlet.forward.path_info", "javax.servlet.forward.query_string"};
    protected final HashMap<String, Object> attributes;

    public ApplicationRequest(ServletRequest request) {
        super(request);
        this.attributes = new HashMap<>();
        setRequest(request);
    }

    @Override // javax.servlet.ServletRequestWrapper, javax.servlet.ServletRequest
    public Object getAttribute(String name) {
        Object obj;
        synchronized (this.attributes) {
            obj = this.attributes.get(name);
        }
        return obj;
    }

    @Override // javax.servlet.ServletRequestWrapper, javax.servlet.ServletRequest
    public Enumeration<String> getAttributeNames() {
        Enumeration<String> enumeration;
        synchronized (this.attributes) {
            enumeration = Collections.enumeration(this.attributes.keySet());
        }
        return enumeration;
    }

    @Override // javax.servlet.ServletRequestWrapper, javax.servlet.ServletRequest
    public void removeAttribute(String name) {
        synchronized (this.attributes) {
            this.attributes.remove(name);
            if (!isSpecial(name)) {
                getRequest().removeAttribute(name);
            }
        }
    }

    @Override // javax.servlet.ServletRequestWrapper, javax.servlet.ServletRequest
    public void setAttribute(String name, Object value) {
        synchronized (this.attributes) {
            this.attributes.put(name, value);
            if (!isSpecial(name)) {
                getRequest().setAttribute(name, value);
            }
        }
    }

    @Override // javax.servlet.ServletRequestWrapper
    public void setRequest(ServletRequest request) {
        super.setRequest(request);
        synchronized (this.attributes) {
            this.attributes.clear();
            Enumeration<String> names = request.getAttributeNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                Object value = request.getAttribute(name);
                this.attributes.put(name, value);
            }
        }
    }

    protected boolean isSpecial(String name) {
        for (int i = 0; i < specials.length; i++) {
            if (specials[i].equals(name)) {
                return true;
            }
        }
        return false;
    }
}
