package org.objectweb.asm;

import org.apache.ibatis.javassist.bytecode.AnnotationsAttribute;
import org.apache.ibatis.javassist.bytecode.ConstantAttribute;
import org.apache.ibatis.javassist.bytecode.DeprecatedAttribute;
import org.apache.ibatis.javassist.bytecode.SignatureAttribute;
import org.apache.ibatis.javassist.bytecode.SyntheticAttribute;
import org.apache.ibatis.javassist.bytecode.TypeAnnotationsAttribute;
import org.objectweb.asm.Attribute;

/* JADX WARN: Classes with same name are omitted:
  asm-4.2.jar:org/objectweb/asm/FieldWriter.class
 */
/* loaded from: lombok-1.16.22.jar:org/objectweb/asm/FieldWriter.SCL.lombok */
final class FieldWriter extends FieldVisitor {
    private final SymbolTable symbolTable;
    private final int accessFlags;
    private final int nameIndex;
    private final int descriptorIndex;
    private int signatureIndex;
    private int constantValueIndex;
    private AnnotationWriter lastRuntimeVisibleAnnotation;
    private AnnotationWriter lastRuntimeInvisibleAnnotation;
    private AnnotationWriter lastRuntimeVisibleTypeAnnotation;
    private AnnotationWriter lastRuntimeInvisibleTypeAnnotation;
    private Attribute firstAttribute;

    FieldWriter(SymbolTable symbolTable, int access, String name, String descriptor, String signature, Object constantValue) {
        super(393216);
        this.symbolTable = symbolTable;
        this.accessFlags = access;
        this.nameIndex = symbolTable.addConstantUtf8(name);
        this.descriptorIndex = symbolTable.addConstantUtf8(descriptor);
        if (signature != null) {
            this.signatureIndex = symbolTable.addConstantUtf8(signature);
        }
        if (constantValue != null) {
            this.constantValueIndex = symbolTable.addConstant(constantValue).index;
        }
    }

    @Override // org.objectweb.asm.FieldVisitor
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        ByteVector annotation = new ByteVector();
        annotation.putShort(this.symbolTable.addConstantUtf8(descriptor)).putShort(0);
        if (visible) {
            AnnotationWriter annotationWriter = new AnnotationWriter(this.symbolTable, annotation, this.lastRuntimeVisibleAnnotation);
            this.lastRuntimeVisibleAnnotation = annotationWriter;
            return annotationWriter;
        }
        AnnotationWriter annotationWriter2 = new AnnotationWriter(this.symbolTable, annotation, this.lastRuntimeInvisibleAnnotation);
        this.lastRuntimeInvisibleAnnotation = annotationWriter2;
        return annotationWriter2;
    }

    @Override // org.objectweb.asm.FieldVisitor
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        ByteVector typeAnnotation = new ByteVector();
        TypeReference.putTarget(typeRef, typeAnnotation);
        TypePath.put(typePath, typeAnnotation);
        typeAnnotation.putShort(this.symbolTable.addConstantUtf8(descriptor)).putShort(0);
        if (visible) {
            AnnotationWriter annotationWriter = new AnnotationWriter(this.symbolTable, typeAnnotation, this.lastRuntimeVisibleTypeAnnotation);
            this.lastRuntimeVisibleTypeAnnotation = annotationWriter;
            return annotationWriter;
        }
        AnnotationWriter annotationWriter2 = new AnnotationWriter(this.symbolTable, typeAnnotation, this.lastRuntimeInvisibleTypeAnnotation);
        this.lastRuntimeInvisibleTypeAnnotation = annotationWriter2;
        return annotationWriter2;
    }

    @Override // org.objectweb.asm.FieldVisitor
    public void visitAttribute(Attribute attribute) {
        attribute.nextAttribute = this.firstAttribute;
        this.firstAttribute = attribute;
    }

    @Override // org.objectweb.asm.FieldVisitor
    public void visitEnd() {
    }

    int computeFieldInfoSize() {
        int size = 8;
        if (this.constantValueIndex != 0) {
            this.symbolTable.addConstantUtf8(ConstantAttribute.tag);
            size = 8 + 8;
        }
        if ((this.accessFlags & 4096) != 0 && this.symbolTable.getMajorVersion() < 49) {
            this.symbolTable.addConstantUtf8(SyntheticAttribute.tag);
            size += 6;
        }
        if (this.signatureIndex != 0) {
            this.symbolTable.addConstantUtf8(SignatureAttribute.tag);
            size += 8;
        }
        if ((this.accessFlags & 131072) != 0) {
            this.symbolTable.addConstantUtf8(DeprecatedAttribute.tag);
            size += 6;
        }
        if (this.lastRuntimeVisibleAnnotation != null) {
            size += this.lastRuntimeVisibleAnnotation.computeAnnotationsSize(AnnotationsAttribute.visibleTag);
        }
        if (this.lastRuntimeInvisibleAnnotation != null) {
            size += this.lastRuntimeInvisibleAnnotation.computeAnnotationsSize(AnnotationsAttribute.invisibleTag);
        }
        if (this.lastRuntimeVisibleTypeAnnotation != null) {
            size += this.lastRuntimeVisibleTypeAnnotation.computeAnnotationsSize(TypeAnnotationsAttribute.visibleTag);
        }
        if (this.lastRuntimeInvisibleTypeAnnotation != null) {
            size += this.lastRuntimeInvisibleTypeAnnotation.computeAnnotationsSize(TypeAnnotationsAttribute.invisibleTag);
        }
        if (this.firstAttribute != null) {
            size += this.firstAttribute.computeAttributesSize(this.symbolTable);
        }
        return size;
    }

    void putFieldInfo(ByteVector output) {
        boolean useSyntheticAttribute = this.symbolTable.getMajorVersion() < 49;
        int mask = useSyntheticAttribute ? 4096 : 0;
        output.putShort(this.accessFlags & (mask ^ (-1))).putShort(this.nameIndex).putShort(this.descriptorIndex);
        int attributesCount = 0;
        if (this.constantValueIndex != 0) {
            attributesCount = 0 + 1;
        }
        if ((this.accessFlags & 4096) != 0 && useSyntheticAttribute) {
            attributesCount++;
        }
        if (this.signatureIndex != 0) {
            attributesCount++;
        }
        if ((this.accessFlags & 131072) != 0) {
            attributesCount++;
        }
        if (this.lastRuntimeVisibleAnnotation != null) {
            attributesCount++;
        }
        if (this.lastRuntimeInvisibleAnnotation != null) {
            attributesCount++;
        }
        if (this.lastRuntimeVisibleTypeAnnotation != null) {
            attributesCount++;
        }
        if (this.lastRuntimeInvisibleTypeAnnotation != null) {
            attributesCount++;
        }
        if (this.firstAttribute != null) {
            attributesCount += this.firstAttribute.getAttributeCount();
        }
        output.putShort(attributesCount);
        if (this.constantValueIndex != 0) {
            output.putShort(this.symbolTable.addConstantUtf8(ConstantAttribute.tag)).putInt(2).putShort(this.constantValueIndex);
        }
        if ((this.accessFlags & 4096) != 0 && useSyntheticAttribute) {
            output.putShort(this.symbolTable.addConstantUtf8(SyntheticAttribute.tag)).putInt(0);
        }
        if (this.signatureIndex != 0) {
            output.putShort(this.symbolTable.addConstantUtf8(SignatureAttribute.tag)).putInt(2).putShort(this.signatureIndex);
        }
        if ((this.accessFlags & 131072) != 0) {
            output.putShort(this.symbolTable.addConstantUtf8(DeprecatedAttribute.tag)).putInt(0);
        }
        if (this.lastRuntimeVisibleAnnotation != null) {
            this.lastRuntimeVisibleAnnotation.putAnnotations(this.symbolTable.addConstantUtf8(AnnotationsAttribute.visibleTag), output);
        }
        if (this.lastRuntimeInvisibleAnnotation != null) {
            this.lastRuntimeInvisibleAnnotation.putAnnotations(this.symbolTable.addConstantUtf8(AnnotationsAttribute.invisibleTag), output);
        }
        if (this.lastRuntimeVisibleTypeAnnotation != null) {
            this.lastRuntimeVisibleTypeAnnotation.putAnnotations(this.symbolTable.addConstantUtf8(TypeAnnotationsAttribute.visibleTag), output);
        }
        if (this.lastRuntimeInvisibleTypeAnnotation != null) {
            this.lastRuntimeInvisibleTypeAnnotation.putAnnotations(this.symbolTable.addConstantUtf8(TypeAnnotationsAttribute.invisibleTag), output);
        }
        if (this.firstAttribute != null) {
            this.firstAttribute.putAttributes(this.symbolTable, output);
        }
    }

    final void collectAttributePrototypes(Attribute.Set attributePrototypes) {
        attributePrototypes.addAttributes(this.firstAttribute);
    }
}
