package org.aspectj.lang.reflect;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/lang/reflect/NoSuchPointcutException.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/lang/reflect/NoSuchPointcutException.class */
public class NoSuchPointcutException extends Exception {
    private static final long serialVersionUID = 3256444698657634352L;
    private String name;

    public NoSuchPointcutException(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
