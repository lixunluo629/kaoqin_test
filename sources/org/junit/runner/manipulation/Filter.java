package org.junit.runner.manipulation;

import java.util.Iterator;
import org.junit.runner.Description;

/* loaded from: junit-4.12.jar:org/junit/runner/manipulation/Filter.class */
public abstract class Filter {
    public static final Filter ALL = new Filter() { // from class: org.junit.runner.manipulation.Filter.1
        @Override // org.junit.runner.manipulation.Filter
        public boolean shouldRun(Description description) {
            return true;
        }

        @Override // org.junit.runner.manipulation.Filter
        public String describe() {
            return "all tests";
        }

        @Override // org.junit.runner.manipulation.Filter
        public void apply(Object child) throws NoTestsRemainException {
        }

        @Override // org.junit.runner.manipulation.Filter
        public Filter intersect(Filter second) {
            return second;
        }
    };

    public abstract boolean shouldRun(Description description);

    public abstract String describe();

    public static Filter matchMethodDescription(final Description desiredDescription) {
        return new Filter() { // from class: org.junit.runner.manipulation.Filter.2
            @Override // org.junit.runner.manipulation.Filter
            public boolean shouldRun(Description description) {
                if (description.isTest()) {
                    return desiredDescription.equals(description);
                }
                Iterator i$ = description.getChildren().iterator();
                while (i$.hasNext()) {
                    Description each = i$.next();
                    if (shouldRun(each)) {
                        return true;
                    }
                }
                return false;
            }

            @Override // org.junit.runner.manipulation.Filter
            public String describe() {
                return String.format("Method %s", desiredDescription.getDisplayName());
            }
        };
    }

    public void apply(Object child) throws NoTestsRemainException {
        if (!(child instanceof Filterable)) {
            return;
        }
        Filterable filterable = (Filterable) child;
        filterable.filter(this);
    }

    public Filter intersect(final Filter second) {
        if (second == this || second == ALL) {
            return this;
        }
        return new Filter() { // from class: org.junit.runner.manipulation.Filter.3
            @Override // org.junit.runner.manipulation.Filter
            public boolean shouldRun(Description description) {
                return this.shouldRun(description) && second.shouldRun(description);
            }

            @Override // org.junit.runner.manipulation.Filter
            public String describe() {
                return this.describe() + " and " + second.describe();
            }
        };
    }
}
