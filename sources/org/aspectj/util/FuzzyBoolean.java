package org.aspectj.util;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/util/FuzzyBoolean.class */
public abstract class FuzzyBoolean {
    public static final FuzzyBoolean YES = new YesFuzzyBoolean();
    public static final FuzzyBoolean NO = new NoFuzzyBoolean();
    public static final FuzzyBoolean MAYBE = new MaybeFuzzyBoolean();
    public static final FuzzyBoolean NEVER = new NeverFuzzyBoolean();

    public abstract boolean alwaysTrue();

    public abstract boolean alwaysFalse();

    public abstract boolean maybeTrue();

    public abstract boolean maybeFalse();

    public abstract FuzzyBoolean and(FuzzyBoolean fuzzyBoolean);

    public abstract FuzzyBoolean or(FuzzyBoolean fuzzyBoolean);

    public abstract FuzzyBoolean not();

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/util/FuzzyBoolean$YesFuzzyBoolean.class */
    private static class YesFuzzyBoolean extends FuzzyBoolean {
        private YesFuzzyBoolean() {
        }

        @Override // org.aspectj.util.FuzzyBoolean
        public boolean alwaysFalse() {
            return false;
        }

        @Override // org.aspectj.util.FuzzyBoolean
        public boolean alwaysTrue() {
            return true;
        }

        @Override // org.aspectj.util.FuzzyBoolean
        public boolean maybeFalse() {
            return false;
        }

        @Override // org.aspectj.util.FuzzyBoolean
        public boolean maybeTrue() {
            return true;
        }

        @Override // org.aspectj.util.FuzzyBoolean
        public FuzzyBoolean and(FuzzyBoolean other) {
            return other;
        }

        @Override // org.aspectj.util.FuzzyBoolean
        public FuzzyBoolean not() {
            return FuzzyBoolean.NO;
        }

        @Override // org.aspectj.util.FuzzyBoolean
        public FuzzyBoolean or(FuzzyBoolean other) {
            return this;
        }

        public String toString() {
            return "YES";
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/util/FuzzyBoolean$NoFuzzyBoolean.class */
    private static class NoFuzzyBoolean extends FuzzyBoolean {
        private NoFuzzyBoolean() {
        }

        @Override // org.aspectj.util.FuzzyBoolean
        public boolean alwaysFalse() {
            return true;
        }

        @Override // org.aspectj.util.FuzzyBoolean
        public boolean alwaysTrue() {
            return false;
        }

        @Override // org.aspectj.util.FuzzyBoolean
        public boolean maybeFalse() {
            return true;
        }

        @Override // org.aspectj.util.FuzzyBoolean
        public boolean maybeTrue() {
            return false;
        }

        @Override // org.aspectj.util.FuzzyBoolean
        public FuzzyBoolean and(FuzzyBoolean other) {
            return this;
        }

        @Override // org.aspectj.util.FuzzyBoolean
        public FuzzyBoolean not() {
            return FuzzyBoolean.YES;
        }

        @Override // org.aspectj.util.FuzzyBoolean
        public FuzzyBoolean or(FuzzyBoolean other) {
            return other;
        }

        public String toString() {
            return "NO";
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/util/FuzzyBoolean$NeverFuzzyBoolean.class */
    private static class NeverFuzzyBoolean extends FuzzyBoolean {
        private NeverFuzzyBoolean() {
        }

        @Override // org.aspectj.util.FuzzyBoolean
        public boolean alwaysFalse() {
            return true;
        }

        @Override // org.aspectj.util.FuzzyBoolean
        public boolean alwaysTrue() {
            return false;
        }

        @Override // org.aspectj.util.FuzzyBoolean
        public boolean maybeFalse() {
            return true;
        }

        @Override // org.aspectj.util.FuzzyBoolean
        public boolean maybeTrue() {
            return false;
        }

        @Override // org.aspectj.util.FuzzyBoolean
        public FuzzyBoolean and(FuzzyBoolean other) {
            return this;
        }

        @Override // org.aspectj.util.FuzzyBoolean
        public FuzzyBoolean not() {
            return this;
        }

        @Override // org.aspectj.util.FuzzyBoolean
        public FuzzyBoolean or(FuzzyBoolean other) {
            return this;
        }

        public String toString() {
            return "NEVER";
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/util/FuzzyBoolean$MaybeFuzzyBoolean.class */
    private static class MaybeFuzzyBoolean extends FuzzyBoolean {
        private MaybeFuzzyBoolean() {
        }

        @Override // org.aspectj.util.FuzzyBoolean
        public boolean alwaysFalse() {
            return false;
        }

        @Override // org.aspectj.util.FuzzyBoolean
        public boolean alwaysTrue() {
            return false;
        }

        @Override // org.aspectj.util.FuzzyBoolean
        public boolean maybeFalse() {
            return true;
        }

        @Override // org.aspectj.util.FuzzyBoolean
        public boolean maybeTrue() {
            return true;
        }

        @Override // org.aspectj.util.FuzzyBoolean
        public FuzzyBoolean and(FuzzyBoolean other) {
            return other.alwaysFalse() ? other : this;
        }

        @Override // org.aspectj.util.FuzzyBoolean
        public FuzzyBoolean not() {
            return this;
        }

        @Override // org.aspectj.util.FuzzyBoolean
        public FuzzyBoolean or(FuzzyBoolean other) {
            return other.alwaysTrue() ? other : this;
        }

        public String toString() {
            return "MAYBE";
        }
    }

    public static final FuzzyBoolean fromBoolean(boolean b) {
        return b ? YES : NO;
    }
}
