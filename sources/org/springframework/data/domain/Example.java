package org.springframework.data.domain;

import lombok.Generated;
import lombok.NonNull;
import org.springframework.data.util.ProxyUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/Example.class */
public class Example<T> {

    @NonNull
    private final T probe;

    @NonNull
    private final ExampleMatcher matcher;

    @Generated
    public String toString() {
        return "Example(probe=" + getProbe() + ", matcher=" + getMatcher() + ")";
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Example)) {
            return false;
        }
        Example<?> other = (Example) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$probe = getProbe();
        Object other$probe = other.getProbe();
        if (this$probe == null) {
            if (other$probe != null) {
                return false;
            }
        } else if (!this$probe.equals(other$probe)) {
            return false;
        }
        Object this$matcher = getMatcher();
        Object other$matcher = other.getMatcher();
        return this$matcher == null ? other$matcher == null : this$matcher.equals(other$matcher);
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof Example;
    }

    @Generated
    public int hashCode() {
        Object $probe = getProbe();
        int result = (1 * 59) + ($probe == null ? 43 : $probe.hashCode());
        Object $matcher = getMatcher();
        return (result * 59) + ($matcher == null ? 43 : $matcher.hashCode());
    }

    @Generated
    private Example(@NonNull T probe, @NonNull ExampleMatcher matcher) {
        if (probe == null) {
            throw new IllegalArgumentException("probe is marked @NonNull but is null");
        }
        if (matcher == null) {
            throw new IllegalArgumentException("matcher is marked @NonNull but is null");
        }
        this.probe = probe;
        this.matcher = matcher;
    }

    public static <T> Example<T> of(T probe) {
        return new Example<>(probe, ExampleMatcher.matching());
    }

    public static <T> Example<T> of(T probe, ExampleMatcher matcher) {
        return new Example<>(probe, matcher);
    }

    public T getProbe() {
        return this.probe;
    }

    public ExampleMatcher getMatcher() {
        return this.matcher;
    }

    public Class<T> getProbeType() {
        return (Class<T>) ProxyUtils.getUserClass(this.probe.getClass());
    }
}
