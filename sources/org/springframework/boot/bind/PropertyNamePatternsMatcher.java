package org.springframework.boot.bind;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/bind/PropertyNamePatternsMatcher.class */
interface PropertyNamePatternsMatcher {
    public static final PropertyNamePatternsMatcher ALL = new PropertyNamePatternsMatcher() { // from class: org.springframework.boot.bind.PropertyNamePatternsMatcher.1
        @Override // org.springframework.boot.bind.PropertyNamePatternsMatcher
        public boolean matches(String propertyName) {
            return true;
        }
    };
    public static final PropertyNamePatternsMatcher NONE = new PropertyNamePatternsMatcher() { // from class: org.springframework.boot.bind.PropertyNamePatternsMatcher.2
        @Override // org.springframework.boot.bind.PropertyNamePatternsMatcher
        public boolean matches(String propertyName) {
            return false;
        }
    };

    boolean matches(String str);
}
