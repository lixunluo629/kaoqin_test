package org.hamcrest;

/* loaded from: hamcrest-core-1.3.jar:org/hamcrest/Condition.class */
public abstract class Condition<T> {
    public static final NotMatched<Object> NOT_MATCHED = new NotMatched<>();

    /* loaded from: hamcrest-core-1.3.jar:org/hamcrest/Condition$Step.class */
    public interface Step<I, O> {
        Condition<O> apply(I i, Description description);
    }

    public abstract boolean matching(Matcher<T> matcher, String str);

    public abstract <U> Condition<U> and(Step<? super T, U> step);

    private Condition() {
    }

    public final boolean matching(Matcher<T> match) {
        return matching(match, "");
    }

    public final <U> Condition<U> then(Step<? super T, U> mapping) {
        return and(mapping);
    }

    public static <T> Condition<T> notMatched() {
        return NOT_MATCHED;
    }

    public static <T> Condition<T> matched(T theValue, Description mismatch) {
        return new Matched(theValue, mismatch);
    }

    /* loaded from: hamcrest-core-1.3.jar:org/hamcrest/Condition$Matched.class */
    private static final class Matched<T> extends Condition<T> {
        private final T theValue;
        private final Description mismatch;

        private Matched(T theValue, Description mismatch) {
            super();
            this.theValue = theValue;
            this.mismatch = mismatch;
        }

        @Override // org.hamcrest.Condition
        public boolean matching(Matcher<T> matcher, String message) {
            if (matcher.matches(this.theValue)) {
                return true;
            }
            this.mismatch.appendText(message);
            matcher.describeMismatch(this.theValue, this.mismatch);
            return false;
        }

        @Override // org.hamcrest.Condition
        public <U> Condition<U> and(Step<? super T, U> step) {
            return step.apply(this.theValue, this.mismatch);
        }
    }

    /* loaded from: hamcrest-core-1.3.jar:org/hamcrest/Condition$NotMatched.class */
    private static final class NotMatched<T> extends Condition<T> {
        private NotMatched() {
            super();
        }

        @Override // org.hamcrest.Condition
        public boolean matching(Matcher<T> match, String message) {
            return false;
        }

        @Override // org.hamcrest.Condition
        public <U> Condition<U> and(Step<? super T, U> mapping) {
            return notMatched();
        }
    }
}
