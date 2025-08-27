package org.springframework.aop.config;

import org.springframework.beans.factory.parsing.ParseState;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/config/AdvisorEntry.class */
public class AdvisorEntry implements ParseState.Entry {
    private final String name;

    public AdvisorEntry(String name) {
        this.name = name;
    }

    public String toString() {
        return "Advisor '" + this.name + "'";
    }
}
