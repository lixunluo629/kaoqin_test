package net.sf.cglib.core;

/* loaded from: cglib-3.1.jar:net/sf/cglib/core/NamingPolicy.class */
public interface NamingPolicy {
    String getClassName(String str, String str2, Object obj, Predicate predicate);

    boolean equals(Object obj);
}
