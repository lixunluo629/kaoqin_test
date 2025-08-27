package org.springframework.core.type.classreading;

import java.lang.reflect.Field;
import java.security.AccessControlException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.asm.AnnotationVisitor;
import org.springframework.asm.Type;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/type/classreading/AbstractRecursiveAnnotationVisitor.class */
abstract class AbstractRecursiveAnnotationVisitor extends AnnotationVisitor {
    protected final Log logger;
    protected final AnnotationAttributes attributes;
    protected final ClassLoader classLoader;

    public AbstractRecursiveAnnotationVisitor(ClassLoader classLoader, AnnotationAttributes attributes) {
        super(393216);
        this.logger = LogFactory.getLog(getClass());
        this.classLoader = classLoader;
        this.attributes = attributes;
    }

    @Override // org.springframework.asm.AnnotationVisitor
    public void visit(String attributeName, Object attributeValue) {
        this.attributes.put(attributeName, attributeValue);
    }

    @Override // org.springframework.asm.AnnotationVisitor
    public AnnotationVisitor visitAnnotation(String attributeName, String asmTypeDescriptor) {
        String annotationType = Type.getType(asmTypeDescriptor).getClassName();
        AnnotationAttributes nestedAttributes = new AnnotationAttributes(annotationType, this.classLoader);
        this.attributes.put(attributeName, nestedAttributes);
        return new RecursiveAnnotationAttributesVisitor(annotationType, nestedAttributes, this.classLoader);
    }

    @Override // org.springframework.asm.AnnotationVisitor
    public AnnotationVisitor visitArray(String attributeName) {
        return new RecursiveAnnotationArrayVisitor(attributeName, this.attributes, this.classLoader);
    }

    @Override // org.springframework.asm.AnnotationVisitor
    public void visitEnum(String attributeName, String asmTypeDescriptor, String attributeValue) throws IllegalAccessException, ClassNotFoundException, IllegalArgumentException {
        Object newValue = getEnumValue(asmTypeDescriptor, attributeValue);
        visit(attributeName, newValue);
    }

    protected Object getEnumValue(String asmTypeDescriptor, String attributeValue) throws IllegalAccessException, ClassNotFoundException, IllegalArgumentException {
        Object valueToUse = attributeValue;
        try {
            Class<?> enumType = this.classLoader.loadClass(Type.getType(asmTypeDescriptor).getClassName());
            Field enumConstant = ReflectionUtils.findField(enumType, attributeValue);
            if (enumConstant != null) {
                ReflectionUtils.makeAccessible(enumConstant);
                valueToUse = enumConstant.get(null);
            }
        } catch (ClassNotFoundException ex) {
            this.logger.debug("Failed to classload enum type while reading annotation metadata", ex);
        } catch (IllegalAccessException ex2) {
            this.logger.debug("Could not access enum value while reading annotation metadata", ex2);
        } catch (NoClassDefFoundError ex3) {
            this.logger.debug("Failed to classload enum type while reading annotation metadata", ex3);
        } catch (AccessControlException ex4) {
            this.logger.debug("Could not access enum value while reading annotation metadata", ex4);
        }
        return valueToUse;
    }
}
