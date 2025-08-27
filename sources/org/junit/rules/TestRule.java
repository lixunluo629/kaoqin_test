package org.junit.rules;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/* loaded from: junit-4.12.jar:org/junit/rules/TestRule.class */
public interface TestRule {
    Statement apply(Statement statement, Description description);
}
