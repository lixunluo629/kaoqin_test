package net.sf.cglib.core;

/* loaded from: cglib-3.1.jar:net/sf/cglib/core/GeneratorStrategy.class */
public interface GeneratorStrategy {
    byte[] generate(ClassGenerator classGenerator) throws Exception;

    boolean equals(Object obj);
}
