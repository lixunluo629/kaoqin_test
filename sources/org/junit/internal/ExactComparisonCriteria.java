package org.junit.internal;

import org.junit.Assert;

/* loaded from: junit-4.12.jar:org/junit/internal/ExactComparisonCriteria.class */
public class ExactComparisonCriteria extends ComparisonCriteria {
    @Override // org.junit.internal.ComparisonCriteria
    protected void assertElementsEqual(Object expected, Object actual) {
        Assert.assertEquals(expected, actual);
    }
}
