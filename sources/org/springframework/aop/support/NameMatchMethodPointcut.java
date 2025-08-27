package org.springframework.aop.support;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.springframework.util.ObjectUtils;
import org.springframework.util.PatternMatchUtils;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/support/NameMatchMethodPointcut.class */
public class NameMatchMethodPointcut extends StaticMethodMatcherPointcut implements Serializable {
    private List<String> mappedNames = new LinkedList();

    public void setMappedName(String mappedName) {
        setMappedNames(mappedName);
    }

    public void setMappedNames(String... mappedNames) {
        this.mappedNames = new LinkedList();
        if (mappedNames != null) {
            this.mappedNames.addAll(Arrays.asList(mappedNames));
        }
    }

    public NameMatchMethodPointcut addMethodName(String name) {
        this.mappedNames.add(name);
        return this;
    }

    @Override // org.springframework.aop.MethodMatcher
    public boolean matches(Method method, Class<?> targetClass) {
        for (String mappedName : this.mappedNames) {
            if (mappedName.equals(method.getName()) || isMatch(method.getName(), mappedName)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isMatch(String methodName, String mappedName) {
        return PatternMatchUtils.simpleMatch(mappedName, methodName);
    }

    public boolean equals(Object other) {
        return this == other || ((other instanceof NameMatchMethodPointcut) && ObjectUtils.nullSafeEquals(this.mappedNames, ((NameMatchMethodPointcut) other).mappedNames));
    }

    public int hashCode() {
        if (this.mappedNames != null) {
            return this.mappedNames.hashCode();
        }
        return 0;
    }
}
