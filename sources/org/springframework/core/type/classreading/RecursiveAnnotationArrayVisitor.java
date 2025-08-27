package org.springframework.core.type.classreading;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import org.springframework.asm.AnnotationVisitor;
import org.springframework.asm.Type;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/type/classreading/RecursiveAnnotationArrayVisitor.class */
class RecursiveAnnotationArrayVisitor extends AbstractRecursiveAnnotationVisitor {
    private final String attributeName;
    private final List<AnnotationAttributes> allNestedAttributes;

    public RecursiveAnnotationArrayVisitor(String attributeName, AnnotationAttributes attributes, ClassLoader classLoader) {
        super(classLoader, attributes);
        this.allNestedAttributes = new ArrayList();
        this.attributeName = attributeName;
    }

    @Override // org.springframework.core.type.classreading.AbstractRecursiveAnnotationVisitor, org.springframework.asm.AnnotationVisitor
    public void visit(String attributeName, Object attributeValue) {
        Object newValue;
        Object existingValue = this.attributes.get(this.attributeName);
        if (existingValue != null) {
            newValue = ObjectUtils.addObjectToArray((Object[]) existingValue, attributeValue);
        } else {
            Class<?> arrayClass = attributeValue.getClass();
            if (Enum.class.isAssignableFrom(arrayClass)) {
                while (arrayClass.getSuperclass() != null && !arrayClass.isEnum()) {
                    arrayClass = arrayClass.getSuperclass();
                }
            }
            Object[] newArray = (Object[]) Array.newInstance(arrayClass, 1);
            newArray[0] = attributeValue;
            newValue = newArray;
        }
        this.attributes.put(this.attributeName, newValue);
    }

    @Override // org.springframework.core.type.classreading.AbstractRecursiveAnnotationVisitor, org.springframework.asm.AnnotationVisitor
    public AnnotationVisitor visitAnnotation(String attributeName, String asmTypeDescriptor) {
        String annotationType = Type.getType(asmTypeDescriptor).getClassName();
        AnnotationAttributes nestedAttributes = new AnnotationAttributes(annotationType, this.classLoader);
        this.allNestedAttributes.add(nestedAttributes);
        return new RecursiveAnnotationAttributesVisitor(annotationType, nestedAttributes, this.classLoader);
    }

    @Override // org.springframework.asm.AnnotationVisitor
    public void visitEnd() {
        if (!this.allNestedAttributes.isEmpty()) {
            this.attributes.put(this.attributeName, this.allNestedAttributes.toArray(new AnnotationAttributes[this.allNestedAttributes.size()]));
        }
    }
}
