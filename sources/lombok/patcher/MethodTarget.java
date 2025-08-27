package lombok.patcher;

import com.mysql.jdbc.MysqlErrorNumbers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.xmlbeans.XmlErrorCodes;
import org.springframework.beans.PropertyAccessor;

/* loaded from: lombok-1.16.22.jar:lombok/patcher/MethodTarget.SCL.lombok */
public final class MethodTarget implements TargetMatcher {
    private final String classSpec;
    private final String methodName;
    private final String returnSpec;
    private final List<String> parameterSpec;
    private boolean hasDescription;
    private static final String JVM_TYPE_SPEC = "\\[*(?:[BCDFIJSZ]|L[^;]+;)";
    private static final Pattern PARAM_SPEC = Pattern.compile(JVM_TYPE_SPEC);
    private static final Pattern COMPLETE_SPEC = Pattern.compile("^\\(((?:\\[*(?:[BCDFIJSZ]|L[^;]+;))*)\\)(V|\\[*(?:[BCDFIJSZ]|L[^;]+;))$");
    private static final Pattern BRACE_PAIRS = Pattern.compile("^(?:\\[\\])*$");

    public String getClassSpec() {
        return this.classSpec;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public String getReturnSpec() {
        return this.returnSpec;
    }

    public List<String> getParameterSpec() {
        return this.parameterSpec;
    }

    public boolean isHasDescription() {
        return this.hasDescription;
    }

    public MethodTarget(String classSpec, String methodName) {
        this(classSpec, methodName, false, null, null);
    }

    public MethodTarget(String classSpec, String methodName, String returnSpec, String... parameterSpecs) {
        this(classSpec, methodName, true, returnSpec, parameterSpecs);
    }

    public Boolean returnTypeIsVoid() {
        if (this.hasDescription) {
            return Boolean.valueOf(this.returnSpec.equals("void"));
        }
        return null;
    }

    private MethodTarget(String classSpec, String methodName, boolean hasDescription, String returnSpec, String[] parameterSpecs) {
        if (classSpec == null) {
            throw new NullPointerException("classSpec");
        }
        if (methodName == null) {
            throw new NullPointerException("methodName");
        }
        if (hasDescription && returnSpec == null) {
            throw new NullPointerException("returnSpec");
        }
        if (hasDescription && parameterSpecs == null) {
            throw new NullPointerException("parameterSpecs");
        }
        if (methodName.contains(PropertyAccessor.PROPERTY_KEY_PREFIX) || methodName.contains(".")) {
            throw new IllegalArgumentException("Your method name contained dots or braces. Perhaps you switched return type and method name around?");
        }
        this.hasDescription = hasDescription;
        this.classSpec = classSpec;
        this.methodName = methodName;
        this.returnSpec = returnSpec;
        this.parameterSpec = parameterSpecs == null ? null : Collections.unmodifiableList(Arrays.asList(parameterSpecs));
    }

    public static List<String> decomposeFullDesc(String desc) {
        Matcher descMatcher = COMPLETE_SPEC.matcher(desc);
        if (!descMatcher.matches()) {
            throw new IllegalArgumentException("This isn't a valid spec: " + desc);
        }
        List<String> out = new ArrayList<>();
        out.add(descMatcher.group(2));
        Matcher paramMatcher = PARAM_SPEC.matcher(descMatcher.group(1));
        while (paramMatcher.find()) {
            out.add(paramMatcher.group(0));
        }
        return out;
    }

    public boolean classMatches(String classSpec) {
        return typeMatches(classSpec, this.classSpec);
    }

    @Override // lombok.patcher.TargetMatcher
    public Collection<String> getAffectedClasses() {
        return Collections.singleton(this.classSpec);
    }

    @Override // lombok.patcher.TargetMatcher
    public boolean matches(String classSpec, String methodName, String descriptor) {
        if (methodName.equals(this.methodName) && classMatches(classSpec)) {
            return descriptorMatch(descriptor);
        }
        return false;
    }

    private boolean descriptorMatch(String descriptor) {
        if (this.returnSpec == null) {
            return true;
        }
        Iterator<String> targetSpecs = decomposeFullDesc(descriptor).iterator();
        if (!typeSpecMatch(targetSpecs.next(), this.returnSpec)) {
            return false;
        }
        Iterator<String> patternSpecs = this.parameterSpec.iterator();
        while (targetSpecs.hasNext() && patternSpecs.hasNext()) {
            if (!typeSpecMatch(targetSpecs.next(), patternSpecs.next())) {
                return false;
            }
        }
        return (targetSpecs.hasNext() || patternSpecs.hasNext()) ? false : true;
    }

    public static boolean typeSpecMatch(String type, String pattern) {
        if (type.equals("V")) {
            return pattern.equals("void");
        }
        int dimsInType = 0;
        while (dimsInType < type.length() && type.charAt(dimsInType) == '[') {
            dimsInType++;
        }
        String type2 = type.substring(dimsInType);
        int start = pattern.length() - (dimsInType * 2);
        if (start < 0) {
            return false;
        }
        String braces = pattern.substring(start);
        if (!BRACE_PAIRS.matcher(braces).matches()) {
            return false;
        }
        String pattern2 = pattern.substring(0, start);
        switch (type2.charAt(0)) {
            case 'B':
                return pattern2.equals("byte");
            case 'C':
                return pattern2.equals("char");
            case 'D':
                return pattern2.equals(XmlErrorCodes.DOUBLE);
            case 'F':
                return pattern2.equals(XmlErrorCodes.FLOAT);
            case 'I':
                return pattern2.equals(XmlErrorCodes.INT);
            case 'J':
                return pattern2.equals(XmlErrorCodes.LONG);
            case 'L':
                return typeMatches(type2.substring(1, type2.length() - 1), pattern2);
            case 'S':
                return pattern2.equals("short");
            case 'Z':
                return pattern2.equals("boolean");
            default:
                return false;
        }
    }

    public static boolean typeMatches(String type, String pattern) {
        return type.replace("/", ".").equals(pattern);
    }

    public int hashCode() {
        int result = (31 * 1) + (this.classSpec == null ? 0 : this.classSpec.hashCode());
        return (31 * ((31 * ((31 * ((31 * result) + (this.hasDescription ? MysqlErrorNumbers.ER_WRONG_VALUE_FOR_VAR : MysqlErrorNumbers.ER_SLAVE_IGNORED_TABLE))) + (this.methodName == null ? 0 : this.methodName.hashCode()))) + (this.parameterSpec == null ? 0 : this.parameterSpec.hashCode()))) + (this.returnSpec == null ? 0 : this.returnSpec.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MethodTarget other = (MethodTarget) obj;
        if (this.classSpec == null) {
            if (other.classSpec != null) {
                return false;
            }
        } else if (!this.classSpec.equals(other.classSpec)) {
            return false;
        }
        if (this.hasDescription != other.hasDescription) {
            return false;
        }
        if (this.methodName == null) {
            if (other.methodName != null) {
                return false;
            }
        } else if (!this.methodName.equals(other.methodName)) {
            return false;
        }
        if (this.parameterSpec == null) {
            if (other.parameterSpec != null) {
                return false;
            }
        } else if (!this.parameterSpec.equals(other.parameterSpec)) {
            return false;
        }
        return this.returnSpec == null ? other.returnSpec == null : this.returnSpec.equals(other.returnSpec);
    }

    public String toString() {
        return "MethodTarget[classSpec=" + this.classSpec + ", methodName=" + this.methodName + ", returnSpec=" + this.returnSpec + ", parameterSpec=" + this.parameterSpec + ", hasDescription=" + this.hasDescription + "]";
    }
}
