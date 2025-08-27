package org.apache.commons.io.serialization;

import java.io.ObjectStreamClass;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/serialization/ObjectStreamClassPredicate.class */
public class ObjectStreamClassPredicate implements Predicate<ObjectStreamClass> {
    private final List<ClassNameMatcher> acceptMatchers = new ArrayList();
    private final List<ClassNameMatcher> rejectMatchers = new ArrayList();

    public ObjectStreamClassPredicate accept(Class<?>... classes) {
        Stream map = Stream.of((Object[]) classes).map(c -> {
            return new FullClassNameMatcher(c.getName());
        });
        List<ClassNameMatcher> list = this.acceptMatchers;
        Objects.requireNonNull(list);
        map.forEach((v1) -> {
            r1.add(v1);
        });
        return this;
    }

    public ObjectStreamClassPredicate accept(ClassNameMatcher matcher) {
        this.acceptMatchers.add(matcher);
        return this;
    }

    public ObjectStreamClassPredicate accept(Pattern pattern) {
        this.acceptMatchers.add(new RegexpClassNameMatcher(pattern));
        return this;
    }

    public ObjectStreamClassPredicate accept(String... patterns) {
        Stream map = Stream.of((Object[]) patterns).map(WildcardClassNameMatcher::new);
        List<ClassNameMatcher> list = this.acceptMatchers;
        Objects.requireNonNull(list);
        map.forEach((v1) -> {
            r1.add(v1);
        });
        return this;
    }

    public ObjectStreamClassPredicate reject(Class<?>... classes) {
        Stream map = Stream.of((Object[]) classes).map(c -> {
            return new FullClassNameMatcher(c.getName());
        });
        List<ClassNameMatcher> list = this.rejectMatchers;
        Objects.requireNonNull(list);
        map.forEach((v1) -> {
            r1.add(v1);
        });
        return this;
    }

    public ObjectStreamClassPredicate reject(ClassNameMatcher m) {
        this.rejectMatchers.add(m);
        return this;
    }

    public ObjectStreamClassPredicate reject(Pattern pattern) {
        this.rejectMatchers.add(new RegexpClassNameMatcher(pattern));
        return this;
    }

    public ObjectStreamClassPredicate reject(String... patterns) {
        Stream map = Stream.of((Object[]) patterns).map(WildcardClassNameMatcher::new);
        List<ClassNameMatcher> list = this.rejectMatchers;
        Objects.requireNonNull(list);
        map.forEach((v1) -> {
            r1.add(v1);
        });
        return this;
    }

    @Override // java.util.function.Predicate
    public boolean test(ObjectStreamClass objectStreamClass) {
        return test(objectStreamClass.getName());
    }

    public boolean test(String name) {
        for (ClassNameMatcher m : this.rejectMatchers) {
            if (m.matches(name)) {
                return false;
            }
        }
        for (ClassNameMatcher m2 : this.acceptMatchers) {
            if (m2.matches(name)) {
                return true;
            }
        }
        return false;
    }
}
