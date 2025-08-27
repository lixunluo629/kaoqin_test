package org.springframework.jmx.export.assembler;

import javax.management.JMException;
import javax.management.modelmbean.ModelMBeanInfo;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/jmx/export/assembler/MBeanInfoAssembler.class */
public interface MBeanInfoAssembler {
    ModelMBeanInfo getMBeanInfo(Object obj, String str) throws JMException;
}
