package org.springframework.data.convert;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.asm.ClassWriter;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Type;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.PreferredConstructor;
import org.springframework.data.mapping.model.MappingInstantiationException;
import org.springframework.data.mapping.model.ParameterValueProvider;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/ClassGeneratingEntityInstantiator.class */
public class ClassGeneratingEntityInstantiator implements EntityInstantiator {
    private volatile Map<TypeInformation<?>, EntityInstantiator> entityInstantiators = new HashMap(32);
    private final ObjectInstantiatorClassGenerator generator = new ObjectInstantiatorClassGenerator();

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/ClassGeneratingEntityInstantiator$ObjectInstantiator.class */
    public interface ObjectInstantiator {
        Object newInstance(Object... objArr);
    }

    @Override // org.springframework.data.convert.EntityInstantiator
    public <T, E extends PersistentEntity<? extends T, P>, P extends PersistentProperty<P>> T createInstance(E e, ParameterValueProvider<P> parameterValueProvider) {
        EntityInstantiator entityInstantiatorPotentiallyCreateAndRegisterEntityInstantiator = this.entityInstantiators.get(e.getTypeInformation());
        if (entityInstantiatorPotentiallyCreateAndRegisterEntityInstantiator == null) {
            entityInstantiatorPotentiallyCreateAndRegisterEntityInstantiator = potentiallyCreateAndRegisterEntityInstantiator(e);
        }
        return (T) entityInstantiatorPotentiallyCreateAndRegisterEntityInstantiator.createInstance(e, parameterValueProvider);
    }

    private synchronized EntityInstantiator potentiallyCreateAndRegisterEntityInstantiator(PersistentEntity<?, ?> entity) {
        Map<TypeInformation<?>, EntityInstantiator> map = this.entityInstantiators;
        EntityInstantiator instantiator = map.get(entity.getTypeInformation());
        if (instantiator != null) {
            return instantiator;
        }
        EntityInstantiator instantiator2 = createEntityInstantiator(entity);
        Map<TypeInformation<?>, EntityInstantiator> map2 = new HashMap<>(map);
        map2.put(entity.getTypeInformation(), instantiator2);
        this.entityInstantiators = map2;
        return instantiator2;
    }

    private EntityInstantiator createEntityInstantiator(PersistentEntity<?, ?> entity) {
        if (shouldUseReflectionEntityInstantiator(entity)) {
            return ReflectionEntityInstantiator.INSTANCE;
        }
        try {
            return new EntityInstantiatorAdapter(createObjectInstantiator(entity));
        } catch (Throwable th) {
            return ReflectionEntityInstantiator.INSTANCE;
        }
    }

    private boolean shouldUseReflectionEntityInstantiator(PersistentEntity<?, ?> entity) {
        PreferredConstructor<?, P> persistenceConstructor;
        Class<?> type = entity.getType();
        if (!type.isInterface() && !type.isArray() && Modifier.isPublic(type.getModifiers())) {
            if ((type.isMemberClass() && !Modifier.isStatic(type.getModifiers())) || ClassUtils.isCglibProxyClass(type) || (persistenceConstructor = entity.getPersistenceConstructor()) == 0 || !Modifier.isPublic(persistenceConstructor.getConstructor().getModifiers())) {
                return true;
            }
            return false;
        }
        return true;
    }

    private ObjectInstantiator createObjectInstantiator(PersistentEntity<?, ?> entity) {
        try {
            return (ObjectInstantiator) this.generator.generateCustomInstantiatorClass(entity).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/ClassGeneratingEntityInstantiator$EntityInstantiatorAdapter.class */
    private static class EntityInstantiatorAdapter implements EntityInstantiator {
        private static final Object[] EMPTY_ARRAY = new Object[0];
        private final ObjectInstantiator instantiator;

        public EntityInstantiatorAdapter(ObjectInstantiator instantiator) {
            this.instantiator = instantiator;
        }

        @Override // org.springframework.data.convert.EntityInstantiator
        public <T, E extends PersistentEntity<? extends T, P>, P extends PersistentProperty<P>> T createInstance(E e, ParameterValueProvider<P> parameterValueProvider) {
            Object[] objArrExtractInvocationArguments = extractInvocationArguments(e.getPersistenceConstructor(), parameterValueProvider);
            try {
                return (T) this.instantiator.newInstance(objArrExtractInvocationArguments);
            } catch (Exception e2) {
                throw new MappingInstantiationException(e, Arrays.asList(objArrExtractInvocationArguments), e2);
            }
        }

        private <P extends PersistentProperty<P>, T> Object[] extractInvocationArguments(PreferredConstructor<? extends T, P> constructor, ParameterValueProvider<P> parameterValueProvider) {
            if (parameterValueProvider == null || constructor == null || !constructor.hasParameters()) {
                return EMPTY_ARRAY;
            }
            List<Object> params = new ArrayList<>();
            for (PreferredConstructor.Parameter<?, P> parameter : constructor.getParameters()) {
                params.add(parameterValueProvider.getParameterValue(parameter));
            }
            return params.toArray();
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/ClassGeneratingEntityInstantiator$ObjectInstantiatorClassGenerator.class */
    static class ObjectInstantiatorClassGenerator {
        private static final String INIT = "<init>";
        private static final String TAG = "_Instantiator_";
        private static final String JAVA_LANG_OBJECT = "java/lang/Object";
        private static final String CREATE_METHOD_NAME = "newInstance";
        private static final String[] IMPLEMENTED_INTERFACES = {Type.getInternalName(ObjectInstantiator.class)};
        private final ByteArrayClassLoader classLoader;

        private ObjectInstantiatorClassGenerator() {
            this.classLoader = (ByteArrayClassLoader) AccessController.doPrivileged(new PrivilegedAction<ByteArrayClassLoader>() { // from class: org.springframework.data.convert.ClassGeneratingEntityInstantiator.ObjectInstantiatorClassGenerator.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public ByteArrayClassLoader run() {
                    return ObjectInstantiatorClassGenerator.this.new ByteArrayClassLoader(ClassUtils.getDefaultClassLoader());
                }
            });
        }

        public Class<?> generateCustomInstantiatorClass(PersistentEntity<?, ?> entity) {
            String className = generateClassName(entity);
            byte[] bytecode = generateBytecode(className, entity);
            return this.classLoader.loadClass(className, bytecode);
        }

        private String generateClassName(PersistentEntity<?, ?> entity) {
            return entity.getType().getName() + TAG + Integer.toString(entity.hashCode(), 36);
        }

        public byte[] generateBytecode(String internalClassName, PersistentEntity<?, ?> entity) {
            ClassWriter cw = new ClassWriter(1);
            cw.visit(50, 33, internalClassName.replace('.', '/'), null, JAVA_LANG_OBJECT, IMPLEMENTED_INTERFACES);
            visitDefaultConstructor(cw);
            visitCreateMethod(cw, entity);
            cw.visitEnd();
            return cw.toByteArray();
        }

        private void visitDefaultConstructor(ClassWriter cw) {
            MethodVisitor mv = cw.visitMethod(1, "<init>", "()V", null, null);
            mv.visitCode();
            mv.visitVarInsn(25, 0);
            mv.visitMethodInsn(183, JAVA_LANG_OBJECT, "<init>", "()V", false);
            mv.visitInsn(177);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }

        private void visitCreateMethod(ClassWriter cw, PersistentEntity<?, ?> entity) {
            String entityTypeResourcePath = Type.getInternalName(entity.getType());
            MethodVisitor mv = cw.visitMethod(129, CREATE_METHOD_NAME, "([Ljava/lang/Object;)Ljava/lang/Object;", null, null);
            mv.visitCode();
            mv.visitTypeInsn(187, entityTypeResourcePath);
            mv.visitInsn(89);
            Constructor<?> ctor = entity.getPersistenceConstructor().getConstructor();
            Class<?>[] parameterTypes = ctor.getParameterTypes();
            for (int i = 0; i < parameterTypes.length; i++) {
                mv.visitVarInsn(25, 1);
                visitArrayIndex(mv, i);
                mv.visitInsn(50);
                if (parameterTypes[i].isPrimitive()) {
                    insertUnboxInsns(mv, Type.getType(parameterTypes[i]).toString().charAt(0), "");
                } else {
                    mv.visitTypeInsn(192, Type.getInternalName(parameterTypes[i]));
                }
            }
            mv.visitMethodInsn(183, entityTypeResourcePath, "<init>", Type.getConstructorDescriptor(ctor), false);
            mv.visitInsn(176);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }

        private static void visitArrayIndex(MethodVisitor mv, int idx) {
            if (idx >= 0 && idx < 6) {
                mv.visitInsn(3 + idx);
            } else {
                mv.visitLdcInsn(Integer.valueOf(idx));
            }
        }

        private static void insertUnboxInsns(MethodVisitor mv, char ch2, String stackDescriptor) {
            switch (ch2) {
                case 'B':
                    if (!stackDescriptor.equals("Ljava/lang/Byte")) {
                        mv.visitTypeInsn(192, "java/lang/Byte");
                    }
                    mv.visitMethodInsn(182, "java/lang/Byte", "byteValue", "()B", false);
                    return;
                case 'C':
                    if (!stackDescriptor.equals("Ljava/lang/Character")) {
                        mv.visitTypeInsn(192, "java/lang/Character");
                    }
                    mv.visitMethodInsn(182, "java/lang/Character", "charValue", "()C", false);
                    return;
                case 'D':
                    if (!stackDescriptor.equals("Ljava/lang/Double")) {
                        mv.visitTypeInsn(192, "java/lang/Double");
                    }
                    mv.visitMethodInsn(182, "java/lang/Double", "doubleValue", "()D", false);
                    return;
                case 'E':
                case 'G':
                case 'H':
                case 'K':
                case 'L':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R':
                case 'T':
                case 'U':
                case 'V':
                case 'W':
                case 'X':
                case 'Y':
                default:
                    throw new IllegalArgumentException("Unboxing should not be attempted for descriptor '" + ch2 + "'");
                case 'F':
                    if (!stackDescriptor.equals("Ljava/lang/Float")) {
                        mv.visitTypeInsn(192, "java/lang/Float");
                    }
                    mv.visitMethodInsn(182, "java/lang/Float", "floatValue", "()F", false);
                    return;
                case 'I':
                    if (!stackDescriptor.equals("Ljava/lang/Integer")) {
                        mv.visitTypeInsn(192, "java/lang/Integer");
                    }
                    mv.visitMethodInsn(182, "java/lang/Integer", "intValue", "()I", false);
                    return;
                case 'J':
                    if (!stackDescriptor.equals("Ljava/lang/Long")) {
                        mv.visitTypeInsn(192, "java/lang/Long");
                    }
                    mv.visitMethodInsn(182, "java/lang/Long", "longValue", "()J", false);
                    return;
                case 'S':
                    if (!stackDescriptor.equals("Ljava/lang/Short")) {
                        mv.visitTypeInsn(192, "java/lang/Short");
                    }
                    mv.visitMethodInsn(182, "java/lang/Short", "shortValue", "()S", false);
                    return;
                case 'Z':
                    if (!stackDescriptor.equals("Ljava/lang/Boolean")) {
                        mv.visitTypeInsn(192, "java/lang/Boolean");
                    }
                    mv.visitMethodInsn(182, "java/lang/Boolean", "booleanValue", "()Z", false);
                    return;
            }
        }

        /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/ClassGeneratingEntityInstantiator$ObjectInstantiatorClassGenerator$ByteArrayClassLoader.class */
        private class ByteArrayClassLoader extends ClassLoader {
            public ByteArrayClassLoader(ClassLoader parent) {
                super(parent);
            }

            public Class<?> loadClass(String name, byte[] bytes) {
                Assert.notNull(name, "name must not be null");
                Assert.notNull(bytes, "bytes must not be null");
                try {
                    Class<?> clazz = findClass(name);
                    if (clazz != null) {
                        return clazz;
                    }
                } catch (ClassNotFoundException e) {
                }
                return defineClass(name, bytes, 0, bytes.length);
            }
        }
    }
}
