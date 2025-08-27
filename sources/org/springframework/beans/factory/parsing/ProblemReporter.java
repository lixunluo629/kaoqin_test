package org.springframework.beans.factory.parsing;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/parsing/ProblemReporter.class */
public interface ProblemReporter {
    void fatal(Problem problem);

    void error(Problem problem);

    void warning(Problem problem);
}
