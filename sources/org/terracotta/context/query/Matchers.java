package org.terracotta.context.query;

import java.util.Map;
import org.terracotta.context.ContextElement;
import org.terracotta.context.TreeNode;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/query/Matchers.class */
public final class Matchers {
    private Matchers() {
    }

    public static Matcher<TreeNode> context(final Matcher<ContextElement> matcher) {
        return new Matcher<TreeNode>() { // from class: org.terracotta.context.query.Matchers.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.terracotta.context.query.Matcher
            public boolean matchesSafely(TreeNode t) {
                return matcher.matches(t.getContext());
            }

            public String toString() {
                return "a context that has " + matcher;
            }
        };
    }

    public static Matcher<ContextElement> attributes(final Matcher<Map<String, Object>> matcher) {
        return new Matcher<ContextElement>() { // from class: org.terracotta.context.query.Matchers.2
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.terracotta.context.query.Matcher
            public boolean matchesSafely(ContextElement t) {
                return matcher.matches(t.attributes());
            }

            public String toString() {
                return "an attributes " + matcher;
            }
        };
    }

    public static Matcher<ContextElement> identifier(final Matcher<Class<?>> matcher) {
        return new Matcher<ContextElement>() { // from class: org.terracotta.context.query.Matchers.3
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.terracotta.context.query.Matcher
            public boolean matchesSafely(ContextElement t) {
                return matcher.matches(t.identifier());
            }

            public String toString() {
                return "an identifier that is " + matcher;
            }
        };
    }

    public static Matcher<Class<?>> subclassOf(final Class<?> klazz) {
        return new Matcher<Class<?>>() { // from class: org.terracotta.context.query.Matchers.4
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.terracotta.context.query.Matcher
            public boolean matchesSafely(Class<?> t) {
                return klazz.isAssignableFrom(t);
            }

            public String toString() {
                return "a subtype of " + klazz;
            }
        };
    }

    public static Matcher<Map<String, Object>> hasAttribute(final String key, final Object value) {
        return new Matcher<Map<String, Object>>() { // from class: org.terracotta.context.query.Matchers.5
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.terracotta.context.query.Matcher
            public boolean matchesSafely(Map<String, Object> object) {
                return object.containsKey(key) && value.equals(object.get(key));
            }
        };
    }

    public static Matcher<Map<String, Object>> hasAttribute(final String key, final Matcher<? extends Object> value) {
        return new Matcher<Map<String, Object>>() { // from class: org.terracotta.context.query.Matchers.6
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.terracotta.context.query.Matcher
            public boolean matchesSafely(Map<String, Object> object) {
                return object.containsKey(key) && value.matches(object.get(key));
            }
        };
    }

    public static <T> Matcher<T> anyOf(final Matcher<? super T>... matchers) {
        return new Matcher<T>() { // from class: org.terracotta.context.query.Matchers.7
            @Override // org.terracotta.context.query.Matcher
            protected boolean matchesSafely(T object) {
                Matcher<? super T>[] arr$ = matchers;
                for (Matcher<? super T> matcher : arr$) {
                    if (matcher.matches(object)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public static <T> Matcher<T> allOf(final Matcher<? super T>... matchers) {
        return new Matcher<T>() { // from class: org.terracotta.context.query.Matchers.8
            @Override // org.terracotta.context.query.Matcher
            protected boolean matchesSafely(T object) {
                Matcher<? super T>[] arr$ = matchers;
                for (Matcher<? super T> matcher : arr$) {
                    if (!matcher.matches(object)) {
                        return false;
                    }
                }
                return true;
            }
        };
    }

    public static <T> Matcher<T> not(final Matcher<T> matcher) {
        return new Matcher<T>() { // from class: org.terracotta.context.query.Matchers.9
            @Override // org.terracotta.context.query.Matcher
            protected boolean matchesSafely(T object) {
                return !matcher.matches(object);
            }
        };
    }
}
