package org.springframework.data.type.classreading;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Generated;
import org.springframework.asm.ClassReader;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Type;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.AnnotationMetadataReadingVisitor;
import org.springframework.core.type.classreading.MethodMetadataReadingVisitor;
import org.springframework.data.type.MethodsMetadata;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/type/classreading/DefaultMethodsMetadataReader.class */
class DefaultMethodsMetadataReader implements MethodsMetadataReader {
    private final Resource resource;
    private final ClassMetadata classMetadata;
    private final AnnotationMetadata annotationMetadata;
    private final MethodsMetadata methodsMetadata;

    @Override // org.springframework.core.type.classreading.MetadataReader
    @Generated
    public Resource getResource() {
        return this.resource;
    }

    @Override // org.springframework.core.type.classreading.MetadataReader
    @Generated
    public ClassMetadata getClassMetadata() {
        return this.classMetadata;
    }

    @Override // org.springframework.core.type.classreading.MetadataReader
    @Generated
    public AnnotationMetadata getAnnotationMetadata() {
        return this.annotationMetadata;
    }

    @Override // org.springframework.data.type.classreading.MethodsMetadataReader
    @Generated
    public MethodsMetadata getMethodsMetadata() {
        return this.methodsMetadata;
    }

    DefaultMethodsMetadataReader(Resource resource, ClassLoader classLoader) throws IOException {
        this.resource = resource;
        InputStream is = null;
        try {
            try {
                is = new BufferedInputStream(getResource().getInputStream());
                ClassReader classReader = new ClassReader(is);
                if (is != null) {
                    is.close();
                }
                MethodsMetadataReadingVisitor visitor = new MethodsMetadataReadingVisitor(classLoader);
                classReader.accept(visitor, 2);
                this.classMetadata = visitor;
                this.annotationMetadata = visitor;
                this.methodsMetadata = visitor;
            } catch (IllegalArgumentException ex) {
                throw new NestedIOException("ASM ClassReader failed to parse class file - probably due to a new Java class file version that isn't supported yet: " + getResource(), ex);
            }
        } catch (Throwable th) {
            if (is != null) {
                is.close();
            }
            throw th;
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/type/classreading/DefaultMethodsMetadataReader$MethodsMetadataReadingVisitor.class */
    static class MethodsMetadataReadingVisitor extends AnnotationMetadataReadingVisitor implements MethodsMetadata {
        MethodsMetadataReadingVisitor(ClassLoader classLoader) {
            super(classLoader);
        }

        @Override // org.springframework.core.type.classreading.AnnotationMetadataReadingVisitor, org.springframework.core.type.classreading.ClassMetadataReadingVisitor, org.springframework.asm.ClassVisitor
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            if ((access & 64) != 0) {
                return super.visitMethod(access, name, desc, signature, exceptions);
            }
            if (name.equals("<init>")) {
                return super.visitMethod(access, name, desc, signature, exceptions);
            }
            MethodMetadataReadingVisitor visitor = new MethodMetadataReadingVisitor(name, access, getClassName(), Type.getReturnType(desc).getClassName(), this.classLoader, this.methodMetadataSet);
            this.methodMetadataSet.add(visitor);
            return visitor;
        }

        @Override // org.springframework.data.type.MethodsMetadata
        public Set<MethodMetadata> getMethods() {
            return Collections.unmodifiableSet(this.methodMetadataSet);
        }

        @Override // org.springframework.data.type.MethodsMetadata
        public Set<MethodMetadata> getMethods(String name) {
            Assert.hasText(name, "Method name must not be null or empty");
            Set<MethodMetadata> result = new LinkedHashSet<>(4);
            for (MethodMetadata metadata : this.methodMetadataSet) {
                if (metadata.getMethodName().equals(name)) {
                    result.add(metadata);
                }
            }
            return Collections.unmodifiableSet(result);
        }
    }
}
