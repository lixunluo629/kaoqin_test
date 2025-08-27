package org.apache.ibatis.javassist;

/* compiled from: ClassPoolTail.java */
/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/ClassPathList.class */
final class ClassPathList {
    ClassPathList next;
    ClassPath path;

    ClassPathList(ClassPath p, ClassPathList n) {
        this.next = n;
        this.path = p;
    }
}
