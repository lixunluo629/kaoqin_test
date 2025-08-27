package org.springframework.boot.autoconfigure.condition;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Conditional;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional({OnJavaCondition.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/condition/ConditionalOnJava.class */
public @interface ConditionalOnJava {

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/condition/ConditionalOnJava$Range.class */
    public enum Range {
        EQUAL_OR_NEWER,
        OLDER_THAN
    }

    Range range() default Range.EQUAL_OR_NEWER;

    JavaVersion value();

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/condition/ConditionalOnJava$JavaVersion.class */
    public enum JavaVersion {
        NINE(9, "1.9", "java.security.cert.URICertStoreParameters"),
        EIGHT(8, "1.8", "java.util.function.Function"),
        SEVEN(7, "1.7", "java.nio.file.Files"),
        SIX(6, "1.6", "java.util.ServiceLoader");

        private final int value;
        private final String name;
        private final boolean available;

        JavaVersion(int value, String name, String className) {
            this.value = value;
            this.name = name;
            this.available = ClassUtils.isPresent(className, getClass().getClassLoader());
        }

        public boolean isWithin(Range range, JavaVersion version) {
            Assert.notNull(range, "Range must not be null");
            Assert.notNull(version, "Version must not be null");
            switch (range) {
                case EQUAL_OR_NEWER:
                    return this.value >= version.value;
                case OLDER_THAN:
                    return this.value < version.value;
                default:
                    throw new IllegalStateException("Unknown range " + range);
            }
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.name;
        }

        public static JavaVersion getJavaVersion() {
            for (JavaVersion candidate : values()) {
                if (candidate.available) {
                    return candidate;
                }
            }
            return SIX;
        }
    }
}
