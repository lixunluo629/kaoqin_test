package org.springframework.aop;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/IntroductionAdvisor.class */
public interface IntroductionAdvisor extends Advisor, IntroductionInfo {
    ClassFilter getClassFilter();

    void validateInterfaces() throws IllegalArgumentException;
}
