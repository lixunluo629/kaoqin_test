package lombok.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: lombok-1.16.22.jar:lombok/core/AnnotationValues.SCL.lombok */
public class AnnotationValues<A extends Annotation> {
    private final Class<A> type;
    private final Map<String, AnnotationValue> values;
    private final LombokNode<?, ?, ?> ast;
    private A cachedInstance = null;

    /* loaded from: lombok-1.16.22.jar:lombok/core/AnnotationValues$AnnotationValue.SCL.lombok */
    public static class AnnotationValue {
        public final List<String> raws;
        public final List<Object> valueGuesses;
        public final List<Object> expressions;
        private final LombokNode<?, ?, ?> node;
        private final boolean isExplicit;

        public AnnotationValue(LombokNode<?, ?, ?> node, List<String> raws, List<Object> expressions, List<Object> valueGuesses, boolean isExplicit) {
            this.node = node;
            this.raws = raws;
            this.expressions = expressions;
            this.valueGuesses = valueGuesses;
            this.isExplicit = isExplicit;
        }

        public void setError(String message, int valueIdx) {
            this.node.addError(message);
        }

        public void setWarning(String message, int valueIdx) {
            this.node.addError(message);
        }

        public String toString() {
            return "raws: " + this.raws + " valueGuesses: " + this.valueGuesses;
        }

        public boolean isExplicit() {
            return this.isExplicit;
        }
    }

    public AnnotationValues(Class<A> type, Map<String, AnnotationValue> values, LombokNode<?, ?, ?> ast) {
        this.type = type;
        this.values = values;
        this.ast = ast;
    }

    public static <A extends Annotation> AnnotationValues<A> of(Class<A> type) {
        return new AnnotationValues<>(type, Collections.emptyMap(), null);
    }

    public static <A extends Annotation> AnnotationValues<A> of(Class<A> type, LombokNode<?, ?, ?> ast) {
        return new AnnotationValues<>(type, Collections.emptyMap(), ast);
    }

    /* loaded from: lombok-1.16.22.jar:lombok/core/AnnotationValues$AnnotationValueDecodeFail.SCL.lombok */
    public static class AnnotationValueDecodeFail extends RuntimeException {
        private static final long serialVersionUID = 1;
        public final int idx;
        public final AnnotationValue owner;

        public AnnotationValueDecodeFail(AnnotationValue owner, String msg, int idx) {
            super(msg);
            this.idx = idx;
            this.owner = owner;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static AnnotationValueDecodeFail makeNoDefaultFail(AnnotationValue owner, Method method) {
        return new AnnotationValueDecodeFail(owner, "No value supplied but " + method.getName() + " has no default either.", -1);
    }

    public List<String> getAsStringList(String methodName) {
        AnnotationValue v = this.values.get(methodName);
        if (v == null) {
            String[] s = (String[]) getDefaultIf(methodName, String[].class, new String[0]);
            return Collections.unmodifiableList(Arrays.asList(s));
        }
        List<String> out = new ArrayList<>(v.valueGuesses.size());
        Iterator<Object> it = v.valueGuesses.iterator();
        while (it.hasNext()) {
            Object guess = it.next();
            Object result = guess == null ? null : guessToType(guess, String.class, v, 0);
            if (result == null) {
                if (v.valueGuesses.size() == 1) {
                    String[] s2 = (String[]) getDefaultIf(methodName, String[].class, new String[0]);
                    return Collections.unmodifiableList(Arrays.asList(s2));
                }
                throw new AnnotationValueDecodeFail(v, "I can't make sense of this annotation value. Try using a fully qualified literal.", 0);
            }
            out.add((String) result);
        }
        return Collections.unmodifiableList(out);
    }

    public String getAsString(String methodName) {
        AnnotationValue v = this.values.get(methodName);
        if (v == null || v.valueGuesses.size() != 1) {
            return (String) getDefaultIf(methodName, String.class, "");
        }
        Object guess = guessToType(v.valueGuesses.get(0), String.class, v, 0);
        return guess instanceof String ? (String) guess : (String) getDefaultIf(methodName, String.class, "");
    }

    public boolean getAsBoolean(String methodName) {
        AnnotationValue v = this.values.get(methodName);
        if (v == null || v.valueGuesses.size() != 1) {
            return ((Boolean) getDefaultIf(methodName, Boolean.class, false)).booleanValue();
        }
        Object guess = guessToType(v.valueGuesses.get(0), Boolean.class, v, 0);
        return guess instanceof Boolean ? ((Boolean) guess).booleanValue() : ((Boolean) getDefaultIf(methodName, Boolean.class, false)).booleanValue();
    }

    public <T> T getDefaultIf(String methodName, Class<T> type, T defaultValue) {
        try {
            return type.cast(type.getMethod(methodName, new Class[0]).getDefaultValue());
        } catch (Exception unused) {
            return defaultValue;
        }
    }

    public A getInstance() {
        if (this.cachedInstance != null) {
            return this.cachedInstance;
        }
        InvocationHandler invocations = new InvocationHandler() { // from class: lombok.core.AnnotationValues.1
            @Override // java.lang.reflect.InvocationHandler
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                AnnotationValue v = (AnnotationValue) AnnotationValues.this.values.get(method.getName());
                if (v == null) {
                    Object defaultValue = method.getDefaultValue();
                    if (defaultValue != null) {
                        return defaultValue;
                    }
                    throw AnnotationValues.makeNoDefaultFail(v, method);
                }
                boolean isArray = false;
                Class<?> expected = method.getReturnType();
                Object array = null;
                if (expected.isArray()) {
                    isArray = true;
                    expected = expected.getComponentType();
                    array = Array.newInstance(expected, v.valueGuesses.size());
                }
                if (!isArray && v.valueGuesses.size() > 1) {
                    throw new AnnotationValueDecodeFail(v, "Expected a single value, but " + method.getName() + " has an array of values", -1);
                }
                if (v.valueGuesses.size() == 0 && !isArray) {
                    Object defaultValue2 = method.getDefaultValue();
                    if (defaultValue2 == null) {
                        throw AnnotationValues.makeNoDefaultFail(v, method);
                    }
                    return defaultValue2;
                }
                int idx = 0;
                Iterator<Object> it = v.valueGuesses.iterator();
                while (it.hasNext()) {
                    Object guess = it.next();
                    Object result = guess == null ? null : AnnotationValues.this.guessToType(guess, expected, v, idx);
                    if (!isArray) {
                        if (result == null) {
                            Object defaultValue3 = method.getDefaultValue();
                            if (defaultValue3 == null) {
                                throw AnnotationValues.makeNoDefaultFail(v, method);
                            }
                            return defaultValue3;
                        }
                        return result;
                    }
                    if (result == null) {
                        if (v.valueGuesses.size() == 1) {
                            Object defaultValue4 = method.getDefaultValue();
                            if (defaultValue4 == null) {
                                throw AnnotationValues.makeNoDefaultFail(v, method);
                            }
                            return defaultValue4;
                        }
                        throw new AnnotationValueDecodeFail(v, "I can't make sense of this annotation value. Try using a fully qualified literal.", idx);
                    }
                    int i = idx;
                    idx++;
                    Array.set(array, i, result);
                }
                return array;
            }
        };
        A a = (A) Proxy.newProxyInstance(this.type.getClassLoader(), new Class[]{this.type}, invocations);
        this.cachedInstance = a;
        return a;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Object guessToType(Object guess, Class<?> expected, AnnotationValue v, int pos) {
        if (expected == Integer.TYPE && ((guess instanceof Integer) || (guess instanceof Short) || (guess instanceof Byte))) {
            return Integer.valueOf(((Number) guess).intValue());
        }
        if (expected == Long.TYPE && ((guess instanceof Long) || (guess instanceof Integer) || (guess instanceof Short) || (guess instanceof Byte))) {
            return Long.valueOf(((Number) guess).longValue());
        }
        if (expected == Short.TYPE && ((guess instanceof Integer) || (guess instanceof Short) || (guess instanceof Byte))) {
            int intVal = ((Number) guess).intValue();
            int shortVal = ((Number) guess).shortValue();
            if (shortVal == intVal) {
                return Integer.valueOf(shortVal);
            }
        }
        if (expected == Byte.TYPE && ((guess instanceof Integer) || (guess instanceof Short) || (guess instanceof Byte))) {
            int intVal2 = ((Number) guess).intValue();
            int byteVal = ((Number) guess).byteValue();
            if (byteVal == intVal2) {
                return Integer.valueOf(byteVal);
            }
        }
        if (expected == Double.TYPE && (guess instanceof Number)) {
            return Double.valueOf(((Number) guess).doubleValue());
        }
        if (expected == Float.TYPE && (guess instanceof Number)) {
            return Float.valueOf(((Number) guess).floatValue());
        }
        if (expected == Boolean.TYPE && (guess instanceof Boolean)) {
            return Boolean.valueOf(((Boolean) guess).booleanValue());
        }
        if (expected == Character.TYPE && (guess instanceof Character)) {
            return Character.valueOf(((Character) guess).charValue());
        }
        if (expected == String.class && (guess instanceof String)) {
            return guess;
        }
        if (Enum.class.isAssignableFrom(expected) && (guess instanceof FieldSelect)) {
            String fieldSel = ((FieldSelect) guess).getFinalPart();
            for (Object enumConstant : expected.getEnumConstants()) {
                String target = ((Enum) enumConstant).name();
                if (target.equals(fieldSel)) {
                    return enumConstant;
                }
            }
            throw new AnnotationValueDecodeFail(v, "Can't translate " + fieldSel + " to an enum of type " + expected, pos);
        }
        if (Class.class == expected && (guess instanceof ClassLiteral)) {
            try {
                String classLit = ((ClassLiteral) guess).getClassName();
                return Class.forName(toFQ(classLit));
            } catch (ClassNotFoundException unused) {
                throw new AnnotationValueDecodeFail(v, "Can't translate " + guess + " to a class object.", pos);
            }
        }
        if (guess instanceof AnnotationValues) {
            return ((AnnotationValues) guess).getInstance();
        }
        if (guess instanceof FieldSelect) {
            throw new AnnotationValueDecodeFail(v, "You must use constant literals in lombok annotations; they cannot be references to (static) fields.", pos);
        }
        throw new AnnotationValueDecodeFail(v, "Can't translate a " + guess.getClass() + " to the expected " + expected, pos);
    }

    public List<String> getRawExpressions(String annotationMethodName) {
        AnnotationValue v = this.values.get(annotationMethodName);
        return v == null ? Collections.emptyList() : v.raws;
    }

    public List<Object> getActualExpressions(String annotationMethodName) {
        AnnotationValue v = this.values.get(annotationMethodName);
        return v == null ? Collections.emptyList() : v.expressions;
    }

    public boolean isExplicit(String annotationMethodName) {
        AnnotationValue annotationValue = this.values.get(annotationMethodName);
        return annotationValue != null && annotationValue.isExplicit();
    }

    public String getRawExpression(String annotationMethodName) {
        List<String> l = getRawExpressions(annotationMethodName);
        if (l.isEmpty()) {
            return null;
        }
        return l.get(0);
    }

    public Object getActualExpression(String annotationMethodName) {
        List<Object> l = getActualExpressions(annotationMethodName);
        if (l.isEmpty()) {
            return null;
        }
        return l.get(0);
    }

    public void setError(String annotationMethodName, String message) {
        setError(annotationMethodName, message, -1);
    }

    public void setWarning(String annotationMethodName, String message) {
        setWarning(annotationMethodName, message, -1);
    }

    public void setError(String annotationMethodName, String message, int index) {
        AnnotationValue v = this.values.get(annotationMethodName);
        if (v == null) {
            return;
        }
        v.setError(message, index);
    }

    public void setWarning(String annotationMethodName, String message, int index) {
        AnnotationValue v = this.values.get(annotationMethodName);
        if (v == null) {
            return;
        }
        v.setWarning(message, index);
    }

    public List<String> getProbableFQTypes(String annotationMethodName) {
        List<String> result = new ArrayList<>();
        AnnotationValue v = this.values.get(annotationMethodName);
        if (v == null) {
            return Collections.emptyList();
        }
        Iterator<Object> it = v.valueGuesses.iterator();
        while (it.hasNext()) {
            Object o = it.next();
            result.add(o == null ? null : toFQ(o.toString()));
        }
        return result;
    }

    public String getProbableFQType(String annotationMethodName) {
        List<String> l = getProbableFQTypes(annotationMethodName);
        if (l.isEmpty()) {
            return null;
        }
        return l.get(0);
    }

    /* JADX WARN: Code restructure failed: missing block: B:35:0x00f2, code lost:
    
        if (r7.equals(r6) == false) goto L40;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00f5, code lost:
    
        r0 = r5.ast.getImportList().getFullyQualifiedNameForSimpleName(r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0104, code lost:
    
        if (r0 == null) goto L40;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0108, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x0109, code lost:
    
        r0 = r5.ast.getImportList().applyNameToStarImports("java", r6).iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0123, code lost:
    
        r0 = r0.next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x012e, code lost:
    
        r0 = java.lang.Class.forName(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0136, code lost:
    
        if (r0 == null) goto L71;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x013e, code lost:
    
        return r0.getName();
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x0147, code lost:
    
        if (r0.hasNext() != false) goto L41;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x0151, code lost:
    
        if (r6.indexOf(46) != (-1)) goto L53;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x015c, code lost:
    
        return inLocalPackage(r5.ast, r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x015d, code lost:
    
        r0 = r6.charAt(0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x0167, code lost:
    
        if (java.lang.Character.isTitleCase(r0) != false) goto L57;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x016e, code lost:
    
        if (java.lang.Character.isUpperCase(r0) == false) goto L59;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x0179, code lost:
    
        return inLocalPackage(r5.ast, r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x017b, code lost:
    
        return r6;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.String toFQ(java.lang.String r6) {
        /*
            Method dump skipped, instructions count: 380
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: lombok.core.AnnotationValues.toFQ(java.lang.String):java.lang.String");
    }

    private static String inLocalPackage(LombokNode<?, ?, ?> node, String typeName) {
        StringBuilder result = new StringBuilder();
        if (node != null && node.getPackageDeclaration() != null) {
            result.append(node.getPackageDeclaration());
        }
        if (result.length() > 0) {
            result.append('.');
        }
        result.append(typeName);
        return result.toString();
    }
}
