package org.springframework.jmx.export.assembler;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/jmx/export/assembler/AutodetectCapableMBeanInfoAssembler.class */
public interface AutodetectCapableMBeanInfoAssembler extends MBeanInfoAssembler {
    boolean includeBean(Class<?> cls, String str);
}
