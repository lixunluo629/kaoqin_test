package org.springframework.beans.factory.parsing;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/parsing/FailFastProblemReporter.class */
public class FailFastProblemReporter implements ProblemReporter {
    private Log logger = LogFactory.getLog(getClass());

    public void setLogger(Log logger) {
        this.logger = logger != null ? logger : LogFactory.getLog(getClass());
    }

    @Override // org.springframework.beans.factory.parsing.ProblemReporter
    public void fatal(Problem problem) {
        throw new BeanDefinitionParsingException(problem);
    }

    @Override // org.springframework.beans.factory.parsing.ProblemReporter
    public void error(Problem problem) {
        throw new BeanDefinitionParsingException(problem);
    }

    @Override // org.springframework.beans.factory.parsing.ProblemReporter
    public void warning(Problem problem) {
        this.logger.warn(problem, problem.getRootCause());
    }
}
