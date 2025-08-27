package org.springframework.data.mapping.model;

import ch.qos.logback.core.CoreConstants;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Generated;
import lombok.NonNull;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;
import org.springframework.asm.ClassWriter;
import org.springframework.asm.Label;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Type;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.data.mapping.Association;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.PersistentPropertyAccessor;
import org.springframework.data.mapping.SimpleAssociationHandler;
import org.springframework.data.mapping.SimplePropertyHandler;
import org.springframework.data.util.TypeInformation;
import org.springframework.jmx.export.naming.IdentityNamingStrategy;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/model/ClassGeneratingPropertyAccessorFactory.class */
public class ClassGeneratingPropertyAccessorFactory implements PersistentPropertyAccessorFactory {
    private static final boolean IS_JAVA_7_OR_BETTER = ClassUtils.isPresent("java.lang.invoke.MethodHandle", ClassGeneratingPropertyAccessorFactory.class.getClassLoader());
    private volatile Map<TypeInformation<?>, Class<PersistentPropertyAccessor>> propertyAccessorClasses = new HashMap(32);

    @Override // org.springframework.data.mapping.model.PersistentPropertyAccessorFactory
    public PersistentPropertyAccessor getPropertyAccessor(PersistentEntity<?, ?> entity, Object bean) {
        Class<PersistentPropertyAccessor> propertyAccessorClass = this.propertyAccessorClasses.get(entity.getTypeInformation());
        if (propertyAccessorClass == null) {
            propertyAccessorClass = potentiallyCreateAndRegisterPersistentPropertyAccessorClass(entity);
        }
        try {
            return (PersistentPropertyAccessor) propertyAccessorClass.getConstructors()[0].newInstance(bean);
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("Cannot create persistent property accessor for %s", entity), e);
        }
    }

    @Override // org.springframework.data.mapping.model.PersistentPropertyAccessorFactory
    public boolean isSupported(PersistentEntity<?, ?> entity) {
        Assert.notNull(entity, "PersistentEntity must not be null!");
        if (!IS_JAVA_7_OR_BETTER) {
            return false;
        }
        try {
            Evil.getClassLoaderMethod(entity);
            if (!isTypeInjectable(entity)) {
                return false;
            }
            final Set<Integer> hashCodes = new HashSet<>();
            final AtomicInteger propertyCount = new AtomicInteger();
            entity.doWithProperties(new SimplePropertyHandler() { // from class: org.springframework.data.mapping.model.ClassGeneratingPropertyAccessorFactory.1
                @Override // org.springframework.data.mapping.SimplePropertyHandler
                public void doWithPersistentProperty(PersistentProperty<?> property) {
                    hashCodes.add(Integer.valueOf(property.getName().hashCode()));
                    propertyCount.incrementAndGet();
                }
            });
            entity.doWithAssociations(new SimpleAssociationHandler() { // from class: org.springframework.data.mapping.model.ClassGeneratingPropertyAccessorFactory.2
                @Override // org.springframework.data.mapping.SimpleAssociationHandler
                public void doWithAssociation(Association<? extends PersistentProperty<?>> association) {
                    if (association.getInverse() != null) {
                        hashCodes.add(Integer.valueOf(association.getInverse().getName().hashCode()));
                        propertyCount.incrementAndGet();
                    }
                }
            });
            return hashCodes.size() == propertyCount.get();
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isTypeInjectable(PersistentEntity<?, ?> entity) {
        Class<?> type = entity.getType();
        return type.getClassLoader() != null && (type.getPackage() == null || !type.getPackage().getName().startsWith("java"));
    }

    private synchronized Class<PersistentPropertyAccessor> potentiallyCreateAndRegisterPersistentPropertyAccessorClass(PersistentEntity<?, ?> entity) {
        Map<TypeInformation<?>, Class<PersistentPropertyAccessor>> map = this.propertyAccessorClasses;
        Class<PersistentPropertyAccessor> propertyAccessorClass = map.get(entity.getTypeInformation());
        if (propertyAccessorClass != null) {
            return propertyAccessorClass;
        }
        Class<PersistentPropertyAccessor> propertyAccessorClass2 = createAccessorClass(entity);
        Map<TypeInformation<?>, Class<PersistentPropertyAccessor>> map2 = new HashMap<>(map);
        map2.put(entity.getTypeInformation(), propertyAccessorClass2);
        this.propertyAccessorClasses = map2;
        return propertyAccessorClass2;
    }

    private Class<PersistentPropertyAccessor> createAccessorClass(PersistentEntity<?, ?> entity) {
        try {
            return PropertyAccessorClassGenerator.generateCustomAccessorClass(entity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/model/ClassGeneratingPropertyAccessorFactory$PropertyAccessorClassGenerator.class */
    static class PropertyAccessorClassGenerator {
        private static final String INIT = "<init>";
        private static final String CLINIT = "<clinit>";
        private static final String TAG = "_Accessor_";
        private static final String JAVA_LANG_OBJECT = "java/lang/Object";
        private static final String JAVA_LANG_STRING = "java/lang/String";
        private static final String JAVA_LANG_REFLECT_METHOD = "java/lang/reflect/Method";
        private static final String JAVA_LANG_INVOKE_METHOD_HANDLE = "java/lang/invoke/MethodHandle";
        private static final String JAVA_LANG_CLASS = "java/lang/Class";
        private static final String BEAN_FIELD = "bean";
        private static final String THIS_REF = "this";
        private static final String PERSISTENT_PROPERTY = "org/springframework/data/mapping/PersistentProperty";
        private static final String SET_ACCESSIBLE = "setAccessible";
        private static final String JAVA_LANG_REFLECT_FIELD = "java/lang/reflect/Field";
        private static final String JAVA_LANG_INVOKE_METHOD_HANDLES = "java/lang/invoke/MethodHandles";
        private static final String JAVA_LANG_INVOKE_METHOD_HANDLES_LOOKUP = "java/lang/invoke/MethodHandles$Lookup";
        private static final String JAVA_LANG_UNSUPPORTED_OPERATION_EXCEPTION = "java/lang/UnsupportedOperationException";
        private static final String[] IMPLEMENTED_INTERFACES = {Type.getInternalName(PersistentPropertyAccessor.class)};

        PropertyAccessorClassGenerator() {
        }

        public static Class<?> generateCustomAccessorClass(PersistentEntity<?, ?> entity) {
            String className = generateClassName(entity);
            byte[] bytecode = generateBytecode(className.replace('.', '/'), entity);
            Class<?> accessorClass = Evil.defineClass(className, bytecode, 0, bytecode.length, entity);
            return accessorClass;
        }

        public static byte[] generateBytecode(String internalClassName, PersistentEntity<?, ?> entity) {
            ClassWriter cw = new ClassWriter(1);
            cw.visit(50, 33, internalClassName, null, JAVA_LANG_OBJECT, IMPLEMENTED_INTERFACES);
            List<PersistentProperty<?>> persistentProperties = getPersistentProperties(entity);
            visitFields(entity, persistentProperties, cw);
            visitDefaultConstructor(entity, internalClassName, cw);
            visitStaticInitializer(entity, persistentProperties, internalClassName, cw);
            visitBeanGetter(entity, internalClassName, cw);
            visitSetProperty(entity, persistentProperties, internalClassName, cw);
            visitGetProperty(entity, persistentProperties, internalClassName, cw);
            cw.visitEnd();
            return cw.toByteArray();
        }

        private static List<PersistentProperty<?>> getPersistentProperties(PersistentEntity<?, ?> entity) {
            final List<PersistentProperty<?>> persistentProperties = new ArrayList<>();
            entity.doWithAssociations(new SimpleAssociationHandler() { // from class: org.springframework.data.mapping.model.ClassGeneratingPropertyAccessorFactory.PropertyAccessorClassGenerator.1
                @Override // org.springframework.data.mapping.SimpleAssociationHandler
                public void doWithAssociation(Association<? extends PersistentProperty<?>> association) {
                    if (association.getInverse() != null) {
                        persistentProperties.add(association.getInverse());
                    }
                }
            });
            entity.doWithProperties(new SimplePropertyHandler() { // from class: org.springframework.data.mapping.model.ClassGeneratingPropertyAccessorFactory.PropertyAccessorClassGenerator.2
                @Override // org.springframework.data.mapping.SimplePropertyHandler
                public void doWithPersistentProperty(PersistentProperty<?> property) {
                    persistentProperties.add(property);
                }
            });
            return persistentProperties;
        }

        private static void visitFields(PersistentEntity<?, ?> entity, List<PersistentProperty<?>> persistentProperties, ClassWriter cw) {
            cw.visitInnerClass(JAVA_LANG_INVOKE_METHOD_HANDLES_LOOKUP, JAVA_LANG_INVOKE_METHOD_HANDLES, "Lookup", 26);
            boolean accessibleType = isAccessible(entity);
            if (accessibleType) {
                cw.visitField(18, "bean", ClassGeneratingPropertyAccessorFactory.referenceName(Type.getInternalName(entity.getType())), null, null).visitEnd();
            } else {
                cw.visitField(18, "bean", ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_OBJECT), null, null).visitEnd();
            }
            for (PersistentProperty<?> property : persistentProperties) {
                Method setter = property.getSetter();
                if (setter != null && generateMethodHandle(entity, setter)) {
                    cw.visitField(26, setterName(property), ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_INVOKE_METHOD_HANDLE), null, null).visitEnd();
                }
                Method getter = property.getGetter();
                if (getter != null && generateMethodHandle(entity, getter)) {
                    cw.visitField(26, getterName(property), ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_INVOKE_METHOD_HANDLE), null, null).visitEnd();
                }
                Field field = property.getField();
                if (field != null && generateSetterMethodHandle(entity, field)) {
                    cw.visitField(26, fieldSetterName(property), ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_INVOKE_METHOD_HANDLE), null, null).visitEnd();
                    cw.visitField(26, fieldGetterName(property), ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_INVOKE_METHOD_HANDLE), null, null).visitEnd();
                }
            }
        }

        private static void visitDefaultConstructor(PersistentEntity<?, ?> entity, String internalClassName, ClassWriter cw) {
            MethodVisitor mv;
            boolean accessibleType = isAccessible(entity);
            if (accessibleType) {
                mv = cw.visitMethod(1, "<init>", String.format("(%s)V", ClassGeneratingPropertyAccessorFactory.referenceName(entity.getType())), null, null);
            } else {
                mv = cw.visitMethod(1, "<init>", String.format("(%s)V", ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_OBJECT)), null, null);
            }
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitVarInsn(25, 0);
            mv.visitMethodInsn(183, JAVA_LANG_OBJECT, "<init>", "()V", false);
            mv.visitVarInsn(25, 1);
            mv.visitLdcInsn("Bean must not be null!");
            mv.visitMethodInsn(184, "org/springframework/util/Assert", "notNull", String.format("(%s%s)V", ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_OBJECT), ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_STRING)), false);
            mv.visitVarInsn(25, 0);
            mv.visitVarInsn(25, 1);
            if (accessibleType) {
                mv.visitFieldInsn(181, internalClassName, "bean", ClassGeneratingPropertyAccessorFactory.referenceName(entity.getType()));
            } else {
                mv.visitFieldInsn(181, internalClassName, "bean", ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_OBJECT));
            }
            mv.visitInsn(177);
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitLocalVariable("this", ClassGeneratingPropertyAccessorFactory.referenceName(internalClassName), null, l0, l3, 0);
            if (accessibleType) {
                mv.visitLocalVariable("bean", ClassGeneratingPropertyAccessorFactory.referenceName(Type.getInternalName(entity.getType())), null, l0, l3, 1);
            } else {
                mv.visitLocalVariable("bean", ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_OBJECT), null, l0, l3, 1);
            }
            mv.visitMaxs(2, 2);
        }

        private static void visitStaticInitializer(PersistentEntity<?, ?> entity, List<PersistentProperty<?>> persistentProperties, String internalClassName, ClassWriter cw) {
            MethodVisitor mv = cw.visitMethod(8, "<clinit>", "()V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            Label l1 = new Label();
            mv.visitLabel(l0);
            mv.visitMethodInsn(184, JAVA_LANG_INVOKE_METHOD_HANDLES, "lookup", String.format("()%s", ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_INVOKE_METHOD_HANDLES_LOOKUP)), false);
            mv.visitVarInsn(58, 0);
            List<Class<?>> entityClasses = getPropertyDeclaratingClasses(persistentProperties);
            for (Class<?> entityClass : entityClasses) {
                mv.visitLdcInsn(entityClass.getName());
                mv.visitMethodInsn(184, JAVA_LANG_CLASS, "forName", String.format("(%s)%s", ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_STRING), ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_CLASS)), false);
                mv.visitVarInsn(58, classVariableIndex4(entityClasses, entityClass));
            }
            for (PersistentProperty<?> property : persistentProperties) {
                if (property.usePropertyAccess()) {
                    if (property.getGetter() != null && generateMethodHandle(entity, property.getGetter())) {
                        visitPropertyGetterInitializer(property, mv, entityClasses, internalClassName);
                    }
                    if (property.getSetter() != null && generateMethodHandle(entity, property.getSetter())) {
                        visitPropertySetterInitializer(property, mv, entityClasses, internalClassName);
                    }
                }
                if (property.getField() != null && generateSetterMethodHandle(entity, property.getField())) {
                    visitFieldGetterSetterInitializer(property, mv, entityClasses, internalClassName);
                }
            }
            mv.visitLabel(l1);
            mv.visitInsn(177);
            mv.visitLocalVariable("lookup", ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_INVOKE_METHOD_HANDLES_LOOKUP), null, l0, l1, 0);
            mv.visitLocalVariable(JamXmlElements.FIELD, ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_REFLECT_FIELD), null, l0, l1, 1);
            mv.visitLocalVariable("setter", ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_REFLECT_METHOD), null, l0, l1, 2);
            mv.visitLocalVariable("getter", ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_REFLECT_METHOD), null, l0, l1, 3);
            Iterator<Class<?>> it = entityClasses.iterator();
            while (it.hasNext()) {
                int index = classVariableIndex4(entityClasses, it.next());
                mv.visitLocalVariable(String.format("class_%d", Integer.valueOf(index)), ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_CLASS), null, l0, l1, index);
            }
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }

        private static List<Class<?>> getPropertyDeclaratingClasses(List<PersistentProperty<?>> persistentProperties) {
            Set<Class<?>> entityClassesSet = new HashSet<>();
            for (PersistentProperty<?> property : persistentProperties) {
                if (property.getField() != null) {
                    entityClassesSet.add(property.getField().getDeclaringClass());
                }
                if (property.getGetter() != null) {
                    entityClassesSet.add(property.getGetter().getDeclaringClass());
                }
                if (property.getSetter() != null) {
                    entityClassesSet.add(property.getSetter().getDeclaringClass());
                }
            }
            return new ArrayList(entityClassesSet);
        }

        private static void visitPropertyGetterInitializer(PersistentProperty<?> property, MethodVisitor mv, List<Class<?>> entityClasses, String internalClassName) {
            Method getter = property.getGetter();
            if (getter != null) {
                mv.visitVarInsn(25, classVariableIndex4(entityClasses, getter.getDeclaringClass()));
                mv.visitLdcInsn(getter.getName());
                mv.visitInsn(3);
                mv.visitTypeInsn(189, JAVA_LANG_CLASS);
                mv.visitMethodInsn(182, JAVA_LANG_CLASS, "getDeclaredMethod", String.format("(%s[%s)%s", ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_STRING), ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_CLASS), ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_REFLECT_METHOD)), false);
                mv.visitVarInsn(58, 3);
                mv.visitVarInsn(25, 3);
                mv.visitInsn(4);
                mv.visitMethodInsn(182, JAVA_LANG_REFLECT_METHOD, SET_ACCESSIBLE, "(Z)V", false);
                mv.visitVarInsn(25, 0);
                mv.visitVarInsn(25, 3);
                mv.visitMethodInsn(182, JAVA_LANG_INVOKE_METHOD_HANDLES_LOOKUP, "unreflect", String.format("(%s)%s", ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_REFLECT_METHOD), ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_INVOKE_METHOD_HANDLE)), false);
            } else {
                mv.visitInsn(1);
            }
            mv.visitFieldInsn(179, internalClassName, getterName(property), ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_INVOKE_METHOD_HANDLE));
        }

        private static void visitPropertySetterInitializer(PersistentProperty<?> property, MethodVisitor mv, List<Class<?>> entityClasses, String internalClassName) {
            Method setter = property.getSetter();
            if (setter != null) {
                mv.visitVarInsn(25, classVariableIndex4(entityClasses, setter.getDeclaringClass()));
                mv.visitLdcInsn(setter.getName());
                mv.visitInsn(4);
                mv.visitTypeInsn(189, JAVA_LANG_CLASS);
                mv.visitInsn(89);
                mv.visitInsn(3);
                Class<?> parameterType = setter.getParameterTypes()[0];
                if (parameterType.isPrimitive()) {
                    mv.visitFieldInsn(178, Type.getInternalName(ClassGeneratingPropertyAccessorFactory.autoboxType(setter.getParameterTypes()[0])), "TYPE", ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_CLASS));
                } else {
                    mv.visitLdcInsn(Type.getType(ClassGeneratingPropertyAccessorFactory.referenceName(parameterType)));
                }
                mv.visitInsn(83);
                mv.visitMethodInsn(182, JAVA_LANG_CLASS, "getDeclaredMethod", String.format("(%s[%s)%s", ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_STRING), ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_CLASS), ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_REFLECT_METHOD)), false);
                mv.visitVarInsn(58, 2);
                mv.visitVarInsn(25, 2);
                mv.visitInsn(4);
                mv.visitMethodInsn(182, JAVA_LANG_REFLECT_METHOD, SET_ACCESSIBLE, "(Z)V", false);
                mv.visitVarInsn(25, 0);
                mv.visitVarInsn(25, 2);
                mv.visitMethodInsn(182, JAVA_LANG_INVOKE_METHOD_HANDLES_LOOKUP, "unreflect", String.format("(%s)%s", ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_REFLECT_METHOD), ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_INVOKE_METHOD_HANDLE)), false);
            } else {
                mv.visitInsn(1);
            }
            mv.visitFieldInsn(179, internalClassName, setterName(property), ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_INVOKE_METHOD_HANDLE));
        }

        private static void visitFieldGetterSetterInitializer(PersistentProperty<?> property, MethodVisitor mv, List<Class<?>> entityClasses, String internalClassName) {
            Field field = property.getField();
            mv.visitVarInsn(25, classVariableIndex4(entityClasses, field.getDeclaringClass()));
            mv.visitLdcInsn(field.getName());
            mv.visitMethodInsn(182, JAVA_LANG_CLASS, "getDeclaredField", String.format("(%s)%s", ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_STRING), ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_REFLECT_FIELD)), false);
            mv.visitVarInsn(58, 1);
            mv.visitVarInsn(25, 1);
            mv.visitInsn(4);
            mv.visitMethodInsn(182, JAVA_LANG_REFLECT_FIELD, SET_ACCESSIBLE, "(Z)V", false);
            mv.visitVarInsn(25, 0);
            mv.visitVarInsn(25, 1);
            mv.visitMethodInsn(182, JAVA_LANG_INVOKE_METHOD_HANDLES_LOOKUP, "unreflectGetter", String.format("(%s)%s", ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_REFLECT_FIELD), ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_INVOKE_METHOD_HANDLE)), false);
            mv.visitFieldInsn(179, internalClassName, fieldGetterName(property), ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_INVOKE_METHOD_HANDLE));
            mv.visitVarInsn(25, 0);
            mv.visitVarInsn(25, 1);
            mv.visitMethodInsn(182, JAVA_LANG_INVOKE_METHOD_HANDLES_LOOKUP, "unreflectSetter", String.format("(%s)%s", ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_REFLECT_FIELD), ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_INVOKE_METHOD_HANDLE)), false);
            mv.visitFieldInsn(179, internalClassName, fieldSetterName(property), ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_INVOKE_METHOD_HANDLE));
        }

        private static void visitBeanGetter(PersistentEntity<?, ?> entity, String internalClassName, ClassWriter cw) {
            MethodVisitor mv = cw.visitMethod(1, "getBean", String.format("()%s", ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_OBJECT)), null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitVarInsn(25, 0);
            if (isAccessible(entity)) {
                mv.visitFieldInsn(180, internalClassName, "bean", ClassGeneratingPropertyAccessorFactory.referenceName(entity.getType()));
            } else {
                mv.visitFieldInsn(180, internalClassName, "bean", ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_OBJECT));
            }
            mv.visitInsn(176);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", ClassGeneratingPropertyAccessorFactory.referenceName(internalClassName), null, l0, l1, 0);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }

        private static void visitGetProperty(PersistentEntity<?, ?> entity, List<PersistentProperty<?>> persistentProperties, String internalClassName, ClassWriter cw) {
            MethodVisitor mv = cw.visitMethod(1, "getProperty", "(Lorg/springframework/data/mapping/PersistentProperty;)Ljava/lang/Object;", "(Lorg/springframework/data/mapping/PersistentProperty<*>;)Ljava/lang/Object;", null);
            mv.visitCode();
            Label l0 = new Label();
            Label l1 = new Label();
            mv.visitLabel(l0);
            visitAssertNotNull(mv);
            mv.visitVarInsn(25, 0);
            if (isAccessible(entity)) {
                mv.visitFieldInsn(180, internalClassName, "bean", ClassGeneratingPropertyAccessorFactory.referenceName(entity.getType()));
            } else {
                mv.visitFieldInsn(180, internalClassName, "bean", ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_OBJECT));
            }
            mv.visitVarInsn(58, 2);
            visitGetPropertySwitch(entity, persistentProperties, internalClassName, mv);
            mv.visitLabel(l1);
            visitThrowUnsupportedOperationException(mv, "No accessor to get property %s");
            mv.visitLocalVariable("this", ClassGeneratingPropertyAccessorFactory.referenceName(internalClassName), null, l0, l1, 0);
            mv.visitLocalVariable(BeanDefinitionParserDelegate.PROPERTY_ELEMENT, ClassGeneratingPropertyAccessorFactory.referenceName(PERSISTENT_PROPERTY), "Lorg/springframework/data/mapping/PersistentProperty<*>;", l0, l1, 1);
            if (isAccessible(entity)) {
                mv.visitLocalVariable("bean", ClassGeneratingPropertyAccessorFactory.referenceName(entity.getType()), null, l0, l1, 2);
            } else {
                mv.visitLocalVariable("bean", ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_OBJECT), null, l0, l1, 2);
            }
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }

        private static void visitGetPropertySwitch(PersistentEntity<?, ?> entity, List<PersistentProperty<?>> persistentProperties, String internalClassName, MethodVisitor mv) {
            Map<String, PropertyStackAddress> propertyStackMap = ClassGeneratingPropertyAccessorFactory.createPropertyStackMap(persistentProperties);
            int[] hashes = new int[propertyStackMap.size()];
            Label[] switchJumpLabels = new Label[propertyStackMap.size()];
            List<PropertyStackAddress> stackmap = new ArrayList<>(propertyStackMap.values());
            Collections.sort(stackmap);
            for (int i = 0; i < stackmap.size(); i++) {
                PropertyStackAddress propertyStackAddress = stackmap.get(i);
                hashes[i] = propertyStackAddress.hash;
                switchJumpLabels[i] = propertyStackAddress.label;
            }
            Label dfltLabel = new Label();
            mv.visitVarInsn(25, 1);
            mv.visitMethodInsn(185, PERSISTENT_PROPERTY, "getName", String.format("()%s", ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_STRING)), true);
            mv.visitMethodInsn(182, JAVA_LANG_STRING, IdentityNamingStrategy.HASH_CODE_KEY, "()I", false);
            mv.visitLookupSwitchInsn(dfltLabel, hashes, switchJumpLabels);
            for (PersistentProperty<?> property : persistentProperties) {
                mv.visitLabel(propertyStackMap.get(property.getName()).label);
                mv.visitFrame(3, 0, null, 0, null);
                if (property.getGetter() != null || property.getField() != null) {
                    visitGetProperty0(entity, property, mv, internalClassName);
                } else {
                    mv.visitJumpInsn(167, dfltLabel);
                }
            }
            mv.visitLabel(dfltLabel);
            mv.visitFrame(3, 0, null, 0, null);
        }

        private static void visitGetProperty0(PersistentEntity<?, ?> entity, PersistentProperty<?> property, MethodVisitor mv, String internalClassName) {
            Method getter = property.getGetter();
            if (property.usePropertyAccess() && getter != null) {
                if (generateMethodHandle(entity, getter)) {
                    mv.visitFieldInsn(178, internalClassName, getterName(property), ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_INVOKE_METHOD_HANDLE));
                    mv.visitVarInsn(25, 2);
                    mv.visitMethodInsn(182, JAVA_LANG_INVOKE_METHOD_HANDLE, "invoke", String.format("(%s)%s", ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_OBJECT), ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_OBJECT)), false);
                } else {
                    mv.visitVarInsn(25, 2);
                    int invokeOpCode = 182;
                    Class<?> declaringClass = getter.getDeclaringClass();
                    boolean interfaceDefinition = declaringClass.isInterface();
                    if (interfaceDefinition) {
                        invokeOpCode = 185;
                    }
                    mv.visitMethodInsn(invokeOpCode, Type.getInternalName(declaringClass), getter.getName(), String.format("()%s", ClassGeneratingPropertyAccessorFactory.signatureTypeName(getter.getReturnType())), interfaceDefinition);
                    ClassGeneratingPropertyAccessorFactory.autoboxIfNeeded(getter.getReturnType(), ClassGeneratingPropertyAccessorFactory.autoboxType(getter.getReturnType()), mv);
                }
            } else {
                Field field = property.getField();
                if (generateMethodHandle(entity, field)) {
                    mv.visitFieldInsn(178, internalClassName, fieldGetterName(property), ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_INVOKE_METHOD_HANDLE));
                    mv.visitVarInsn(25, 2);
                    mv.visitMethodInsn(182, JAVA_LANG_INVOKE_METHOD_HANDLE, "invoke", String.format("(%s)%s", ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_OBJECT), ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_OBJECT)), false);
                } else {
                    mv.visitVarInsn(25, 2);
                    mv.visitFieldInsn(180, Type.getInternalName(field.getDeclaringClass()), field.getName(), ClassGeneratingPropertyAccessorFactory.signatureTypeName(field.getType()));
                    ClassGeneratingPropertyAccessorFactory.autoboxIfNeeded(field.getType(), ClassGeneratingPropertyAccessorFactory.autoboxType(field.getType()), mv);
                }
            }
            mv.visitInsn(176);
        }

        private static void visitSetProperty(PersistentEntity<?, ?> entity, List<PersistentProperty<?>> persistentProperties, String internalClassName, ClassWriter cw) {
            MethodVisitor mv = cw.visitMethod(1, "setProperty", "(Lorg/springframework/data/mapping/PersistentProperty;Ljava/lang/Object;)V", "(Lorg/springframework/data/mapping/PersistentProperty<*>;Ljava/lang/Object;)V", null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            visitAssertNotNull(mv);
            mv.visitVarInsn(25, 0);
            if (isAccessible(entity)) {
                mv.visitFieldInsn(180, internalClassName, "bean", ClassGeneratingPropertyAccessorFactory.referenceName(entity.getType()));
            } else {
                mv.visitFieldInsn(180, internalClassName, "bean", ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_OBJECT));
            }
            mv.visitVarInsn(58, 3);
            visitSetPropertySwitch(entity, persistentProperties, internalClassName, mv);
            Label l1 = new Label();
            mv.visitLabel(l1);
            visitThrowUnsupportedOperationException(mv, "No accessor to set property %s");
            mv.visitLocalVariable("this", ClassGeneratingPropertyAccessorFactory.referenceName(internalClassName), null, l0, l1, 0);
            mv.visitLocalVariable(BeanDefinitionParserDelegate.PROPERTY_ELEMENT, "Lorg/springframework/data/mapping/PersistentProperty;", "Lorg/springframework/data/mapping/PersistentProperty<*>;", l0, l1, 1);
            mv.visitLocalVariable("value", ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_OBJECT), null, l0, l1, 2);
            if (isAccessible(entity)) {
                mv.visitLocalVariable("bean", ClassGeneratingPropertyAccessorFactory.referenceName(entity.getType()), null, l0, l1, 3);
            } else {
                mv.visitLocalVariable("bean", ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_OBJECT), null, l0, l1, 3);
            }
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }

        private static void visitSetPropertySwitch(PersistentEntity<?, ?> entity, List<PersistentProperty<?>> persistentProperties, String internalClassName, MethodVisitor mv) {
            Map<String, PropertyStackAddress> propertyStackMap = ClassGeneratingPropertyAccessorFactory.createPropertyStackMap(persistentProperties);
            int[] hashes = new int[propertyStackMap.size()];
            Label[] switchJumpLabels = new Label[propertyStackMap.size()];
            List<PropertyStackAddress> stackmap = new ArrayList<>(propertyStackMap.values());
            Collections.sort(stackmap);
            for (int i = 0; i < stackmap.size(); i++) {
                PropertyStackAddress propertyStackAddress = stackmap.get(i);
                hashes[i] = propertyStackAddress.hash;
                switchJumpLabels[i] = propertyStackAddress.label;
            }
            Label dfltLabel = new Label();
            mv.visitVarInsn(25, 1);
            mv.visitMethodInsn(185, PERSISTENT_PROPERTY, "getName", String.format("()%s", ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_STRING)), true);
            mv.visitMethodInsn(182, JAVA_LANG_STRING, IdentityNamingStrategy.HASH_CODE_KEY, "()I", false);
            mv.visitLookupSwitchInsn(dfltLabel, hashes, switchJumpLabels);
            for (PersistentProperty<?> property : persistentProperties) {
                mv.visitLabel(propertyStackMap.get(property.getName()).label);
                mv.visitFrame(3, 0, null, 0, null);
                if (property.getSetter() != null || property.getField() != null) {
                    visitSetProperty0(entity, property, mv, internalClassName);
                } else {
                    mv.visitJumpInsn(167, dfltLabel);
                }
            }
            mv.visitLabel(dfltLabel);
            mv.visitFrame(3, 0, null, 0, null);
        }

        private static void visitSetProperty0(PersistentEntity<?, ?> entity, PersistentProperty<?> property, MethodVisitor mv, String internalClassName) {
            Method setter = property.getSetter();
            if (property.usePropertyAccess() && setter != null) {
                if (generateMethodHandle(entity, setter)) {
                    mv.visitFieldInsn(178, internalClassName, setterName(property), ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_INVOKE_METHOD_HANDLE));
                    mv.visitVarInsn(25, 3);
                    mv.visitVarInsn(25, 2);
                    mv.visitMethodInsn(182, JAVA_LANG_INVOKE_METHOD_HANDLE, "invoke", String.format("(%s%s)V", ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_OBJECT), ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_OBJECT)), false);
                } else {
                    mv.visitVarInsn(25, 3);
                    mv.visitVarInsn(25, 2);
                    Class<?> parameterType = setter.getParameterTypes()[0];
                    mv.visitTypeInsn(192, Type.getInternalName(ClassGeneratingPropertyAccessorFactory.autoboxType(parameterType)));
                    ClassGeneratingPropertyAccessorFactory.autoboxIfNeeded(ClassGeneratingPropertyAccessorFactory.autoboxType(parameterType), parameterType, mv);
                    int invokeOpCode = 182;
                    Class<?> declaringClass = setter.getDeclaringClass();
                    boolean interfaceDefinition = declaringClass.isInterface();
                    if (interfaceDefinition) {
                        invokeOpCode = 185;
                    }
                    mv.visitMethodInsn(invokeOpCode, Type.getInternalName(setter.getDeclaringClass()), setter.getName(), String.format("(%s)V", ClassGeneratingPropertyAccessorFactory.signatureTypeName(parameterType)), interfaceDefinition);
                }
            } else {
                Field field = property.getField();
                if (field != null) {
                    if (generateSetterMethodHandle(entity, field)) {
                        mv.visitFieldInsn(178, internalClassName, fieldSetterName(property), ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_INVOKE_METHOD_HANDLE));
                        mv.visitVarInsn(25, 3);
                        mv.visitVarInsn(25, 2);
                        mv.visitMethodInsn(182, JAVA_LANG_INVOKE_METHOD_HANDLE, "invoke", String.format("(%s%s)V", ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_OBJECT), ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_OBJECT)), false);
                    } else {
                        mv.visitVarInsn(25, 3);
                        mv.visitVarInsn(25, 2);
                        Class<?> fieldType = field.getType();
                        mv.visitTypeInsn(192, Type.getInternalName(ClassGeneratingPropertyAccessorFactory.autoboxType(fieldType)));
                        ClassGeneratingPropertyAccessorFactory.autoboxIfNeeded(ClassGeneratingPropertyAccessorFactory.autoboxType(fieldType), fieldType, mv);
                        mv.visitFieldInsn(181, Type.getInternalName(field.getDeclaringClass()), field.getName(), ClassGeneratingPropertyAccessorFactory.signatureTypeName(fieldType));
                    }
                }
            }
            mv.visitInsn(177);
        }

        private static void visitAssertNotNull(MethodVisitor mv) {
            mv.visitVarInsn(25, 1);
            mv.visitLdcInsn("Property must not be null!");
            mv.visitMethodInsn(184, "org/springframework/util/Assert", "notNull", String.format("(%s%s)V", ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_OBJECT), ClassGeneratingPropertyAccessorFactory.referenceName(JAVA_LANG_STRING)), false);
        }

        private static void visitThrowUnsupportedOperationException(MethodVisitor mv, String message) {
            mv.visitTypeInsn(187, JAVA_LANG_UNSUPPORTED_OPERATION_EXCEPTION);
            mv.visitInsn(89);
            mv.visitLdcInsn(message);
            mv.visitInsn(4);
            mv.visitTypeInsn(189, JAVA_LANG_OBJECT);
            mv.visitInsn(89);
            mv.visitInsn(3);
            mv.visitVarInsn(25, 1);
            mv.visitInsn(83);
            mv.visitMethodInsn(184, JAVA_LANG_STRING, "format", "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", false);
            mv.visitMethodInsn(183, JAVA_LANG_UNSUPPORTED_OPERATION_EXCEPTION, "<init>", "(Ljava/lang/String;)V", false);
            mv.visitInsn(191);
        }

        private static String fieldSetterName(PersistentProperty<?> property) {
            return String.format("$%s_fieldSetter", property.getName());
        }

        private static String fieldGetterName(PersistentProperty<?> property) {
            return String.format("$%s_fieldGetter", property.getName());
        }

        private static String setterName(PersistentProperty<?> property) {
            return String.format("$%s_setter", property.getName());
        }

        private static String getterName(PersistentProperty<?> property) {
            return String.format("$%s_getter", property.getName());
        }

        private static boolean isAccessible(PersistentEntity<?, ?> entity) {
            return isAccessible(entity.getType());
        }

        private static boolean isAccessible(Class<?> theClass) {
            return isAccessible(theClass.getModifiers());
        }

        private static boolean isAccessible(int modifiers) {
            return !Modifier.isPrivate(modifiers);
        }

        private static boolean isDefault(int modifiers) {
            if (Modifier.isPrivate(modifiers) || Modifier.isProtected(modifiers) || Modifier.isPublic(modifiers)) {
                return false;
            }
            return true;
        }

        private static boolean generateSetterMethodHandle(PersistentEntity<?, ?> entity, Field field) {
            return generateMethodHandle(entity, field) || Modifier.isFinal(field.getModifiers());
        }

        private static boolean generateMethodHandle(PersistentEntity<?, ?> entity, Member member) {
            if (isAccessible(entity)) {
                if (((!Modifier.isProtected(member.getModifiers()) && !isDefault(member.getModifiers())) || member.getDeclaringClass().getPackage().equals(entity.getClass().getPackage())) && isAccessible(member.getDeclaringClass()) && isAccessible(member.getModifiers())) {
                    return false;
                }
                return true;
            }
            return true;
        }

        private static int classVariableIndex4(List<Class<?>> list, Class<?> item) {
            return 4 + list.indexOf(item);
        }

        private static String generateClassName(PersistentEntity<?, ?> entity) {
            return entity.getType().getName() + TAG + Integer.toString(entity.hashCode(), 36);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String referenceName(Class<?> type) {
        if (type.isArray()) {
            return Type.getInternalName(type);
        }
        return referenceName(Type.getInternalName(type));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String referenceName(String internalTypeName) {
        return String.format("L%s;", internalTypeName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Map<String, PropertyStackAddress> createPropertyStackMap(List<PersistentProperty<?>> persistentProperties) {
        Map<String, PropertyStackAddress> stackmap = new HashMap<>();
        for (PersistentProperty<?> property : persistentProperties) {
            stackmap.put(property.getName(), new PropertyStackAddress(new Label(), property.getName().hashCode()));
        }
        return stackmap;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Class<?> autoboxType(Class<?> unboxed) {
        if (unboxed.equals(Boolean.TYPE)) {
            return Boolean.class;
        }
        if (unboxed.equals(Byte.TYPE)) {
            return Byte.class;
        }
        if (unboxed.equals(Character.TYPE)) {
            return Character.class;
        }
        if (unboxed.equals(Double.TYPE)) {
            return Double.class;
        }
        if (unboxed.equals(Float.TYPE)) {
            return Float.class;
        }
        if (unboxed.equals(Integer.TYPE)) {
            return Integer.class;
        }
        if (unboxed.equals(Long.TYPE)) {
            return Long.class;
        }
        if (unboxed.equals(Short.TYPE)) {
            return Short.class;
        }
        if (unboxed.equals(Void.TYPE)) {
            return Void.class;
        }
        return unboxed;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void autoboxIfNeeded(Class<?> in, Class<?> out, MethodVisitor visitor) {
        if (in.equals(Boolean.class) && out.equals(Boolean.TYPE)) {
            visitor.visitMethodInsn(182, "java/lang/Boolean", "booleanValue", "()Z", false);
        }
        if (in.equals(Boolean.TYPE) && out.equals(Boolean.class)) {
            visitor.visitMethodInsn(184, "java/lang/Boolean", CoreConstants.VALUE_OF, "(Z)Ljava/lang/Boolean;", false);
        }
        if (in.equals(Byte.class) && out.equals(Byte.TYPE)) {
            visitor.visitMethodInsn(182, "java/lang/Byte", "byteValue", "()B", false);
        }
        if (in.equals(Byte.TYPE) && out.equals(Byte.class)) {
            visitor.visitMethodInsn(184, "java/lang/Byte", CoreConstants.VALUE_OF, "(B)Ljava/lang/Byte;", false);
        }
        if (in.equals(Character.class) && out.equals(Character.TYPE)) {
            visitor.visitMethodInsn(182, "java/lang/Character", "charValue", "()C", false);
        }
        if (in.equals(Character.TYPE) && out.equals(Character.class)) {
            visitor.visitMethodInsn(184, "java/lang/Character", CoreConstants.VALUE_OF, "(C)Ljava/lang/Character;", false);
        }
        if (in.equals(Double.class) && out.equals(Double.TYPE)) {
            visitor.visitMethodInsn(182, "java/lang/Double", "doubleValue", "()D", false);
        }
        if (in.equals(Double.TYPE) && out.equals(Double.class)) {
            visitor.visitMethodInsn(184, "java/lang/Double", CoreConstants.VALUE_OF, "(D)Ljava/lang/Double;", false);
        }
        if (in.equals(Float.class) && out.equals(Float.TYPE)) {
            visitor.visitMethodInsn(182, "java/lang/Float", "floatValue", "()F", false);
        }
        if (in.equals(Float.TYPE) && out.equals(Float.class)) {
            visitor.visitMethodInsn(184, "java/lang/Float", CoreConstants.VALUE_OF, "(F)Ljava/lang/Float;", false);
        }
        if (in.equals(Integer.class) && out.equals(Integer.TYPE)) {
            visitor.visitMethodInsn(182, "java/lang/Integer", "intValue", "()I", false);
        }
        if (in.equals(Integer.TYPE) && out.equals(Integer.class)) {
            visitor.visitMethodInsn(184, "java/lang/Integer", CoreConstants.VALUE_OF, "(I)Ljava/lang/Integer;", false);
        }
        if (in.equals(Long.class) && out.equals(Long.TYPE)) {
            visitor.visitMethodInsn(182, "java/lang/Long", "longValue", "()J", false);
        }
        if (in.equals(Long.TYPE) && out.equals(Long.class)) {
            visitor.visitMethodInsn(184, "java/lang/Long", CoreConstants.VALUE_OF, "(J)Ljava/lang/Long;", false);
        }
        if (in.equals(Short.class) && out.equals(Short.TYPE)) {
            visitor.visitMethodInsn(182, "java/lang/Short", "shortValue", "()S", false);
        }
        if (in.equals(Short.TYPE) && out.equals(Short.class)) {
            visitor.visitMethodInsn(184, "java/lang/Short", CoreConstants.VALUE_OF, "(S)Ljava/lang/Short;", false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String signatureTypeName(Class<?> type) {
        if (type.equals(Boolean.TYPE)) {
            return "Z";
        }
        if (type.equals(Byte.TYPE)) {
            return "B";
        }
        if (type.equals(Character.TYPE)) {
            return "C";
        }
        if (type.equals(Double.TYPE)) {
            return "D";
        }
        if (type.equals(Float.TYPE)) {
            return "F";
        }
        if (type.equals(Integer.TYPE)) {
            return "I";
        }
        if (type.equals(Long.TYPE)) {
            return "J";
        }
        if (type.equals(Short.TYPE)) {
            return "S";
        }
        if (type.equals(Void.TYPE)) {
            return "V";
        }
        return referenceName(type);
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/model/ClassGeneratingPropertyAccessorFactory$PropertyStackAddress.class */
    static class PropertyStackAddress implements Comparable<PropertyStackAddress> {

        @NonNull
        private final Label label;
        private final int hash;

        @Generated
        public PropertyStackAddress(@NonNull Label label, int hash) {
            if (label == null) {
                throw new IllegalArgumentException("label is marked @NonNull but is null");
            }
            this.label = label;
            this.hash = hash;
        }

        @Override // java.lang.Comparable
        public int compareTo(PropertyStackAddress o) {
            if (this.hash < o.hash) {
                return -1;
            }
            return this.hash == o.hash ? 0 : 1;
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/model/ClassGeneratingPropertyAccessorFactory$Evil.class */
    private static final class Evil {
        @Generated
        private Evil() {
            throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
        }

        static Class<?> defineClass(String name, byte[] bytes, int offset, int len, PersistentEntity<?, ?> persistentEntity) {
            Class<?> entityType = persistentEntity.getType();
            ClassLoader classLoader = entityType.getClassLoader();
            try {
                Method defineClass = getClassLoaderMethod(persistentEntity);
                defineClass.setAccessible(true);
                return (Class) defineClass.invoke(classLoader, name, bytes, Integer.valueOf(offset), Integer.valueOf(len), entityType.getProtectionDomain());
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException(e);
            }
        }

        static Method getClassLoaderMethod(PersistentEntity<?, ?> entity) {
            ClassLoader classLoader = entity.getType().getClassLoader();
            Class<?> classLoaderClass = classLoader.getClass();
            return ReflectionUtils.findMethod(classLoaderClass, "defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class);
        }
    }
}
