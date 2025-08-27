package org.springframework.context.expression;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import java.util.Map;
import org.springframework.asm.MethodVisitor;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.CodeFlow;
import org.springframework.expression.spel.CompilablePropertyAccessor;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/expression/MapAccessor.class */
public class MapAccessor implements CompilablePropertyAccessor {
    @Override // org.springframework.expression.PropertyAccessor
    public Class<?>[] getSpecificTargetClasses() {
        return new Class[]{Map.class};
    }

    @Override // org.springframework.expression.PropertyAccessor
    public boolean canRead(EvaluationContext context, Object target, String name) throws AccessException {
        Map<?, ?> map = (Map) target;
        return map.containsKey(name);
    }

    @Override // org.springframework.expression.PropertyAccessor
    public TypedValue read(EvaluationContext context, Object target, String name) throws AccessException {
        Map<?, ?> map = (Map) target;
        Object value = map.get(name);
        if (value == null && !map.containsKey(name)) {
            throw new MapAccessException(name);
        }
        return new TypedValue(value);
    }

    @Override // org.springframework.expression.PropertyAccessor
    public boolean canWrite(EvaluationContext context, Object target, String name) throws AccessException {
        return true;
    }

    @Override // org.springframework.expression.PropertyAccessor
    public void write(EvaluationContext context, Object target, String name, Object newValue) throws AccessException {
        Map<Object, Object> map = (Map) target;
        map.put(name, newValue);
    }

    @Override // org.springframework.expression.spel.CompilablePropertyAccessor
    public boolean isCompilable() {
        return true;
    }

    @Override // org.springframework.expression.spel.CompilablePropertyAccessor
    public Class<?> getPropertyType() {
        return Object.class;
    }

    @Override // org.springframework.expression.spel.CompilablePropertyAccessor
    public void generateCode(String propertyName, MethodVisitor mv, CodeFlow cf) {
        String descriptor = cf.lastDescriptor();
        if (descriptor == null || !descriptor.equals("Ljava/util/Map")) {
            if (descriptor == null) {
                cf.loadTarget(mv);
            }
            CodeFlow.insertCheckCast(mv, "Ljava/util/Map");
        }
        mv.visitLdcInsn(propertyName);
        mv.visitMethodInsn(185, "java/util/Map", BeanUtil.PREFIX_GETTER_GET, "(Ljava/lang/Object;)Ljava/lang/Object;", true);
    }

    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/expression/MapAccessor$MapAccessException.class */
    private static class MapAccessException extends AccessException {
        private final String key;

        public MapAccessException(String key) {
            super(null);
            this.key = key;
        }

        @Override // java.lang.Throwable
        public String getMessage() {
            return "Map does not contain a value for key '" + this.key + "'";
        }
    }
}
