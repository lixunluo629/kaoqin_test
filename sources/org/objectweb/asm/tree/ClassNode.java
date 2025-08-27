package org.objectweb.asm.tree;

import java.util.ArrayList;
import java.util.List;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.ModuleVisitor;
import org.objectweb.asm.TypePath;

/* loaded from: lombok-1.16.22.jar:org/objectweb/asm/tree/ClassNode.SCL.lombok */
public class ClassNode extends ClassVisitor {
    public int version;
    public int access;
    public String name;
    public String signature;
    public String superName;
    public List<String> interfaces;
    public String sourceFile;
    public String sourceDebug;
    public ModuleNode module;
    public String outerClass;
    public String outerMethod;
    public String outerMethodDesc;
    public List<AnnotationNode> visibleAnnotations;
    public List<AnnotationNode> invisibleAnnotations;
    public List<TypeAnnotationNode> visibleTypeAnnotations;
    public List<TypeAnnotationNode> invisibleTypeAnnotations;
    public List<Attribute> attrs;
    public List<InnerClassNode> innerClasses;
    public String nestHostClassExperimental;
    public List<String> nestMembersExperimental;
    public List<FieldNode> fields;
    public List<MethodNode> methods;

    public ClassNode() {
        this(393216);
        if (getClass() != ClassNode.class) {
            throw new IllegalStateException();
        }
    }

    public ClassNode(int api) {
        super(api);
        this.interfaces = new ArrayList();
        this.innerClasses = new ArrayList();
        this.fields = new ArrayList();
        this.methods = new ArrayList();
    }

    @Override // org.objectweb.asm.ClassVisitor
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.version = version;
        this.access = access;
        this.name = name;
        this.signature = signature;
        this.superName = superName;
        this.interfaces = Util.asArrayList(interfaces);
    }

    @Override // org.objectweb.asm.ClassVisitor
    public void visitSource(String file, String debug) {
        this.sourceFile = file;
        this.sourceDebug = debug;
    }

    @Override // org.objectweb.asm.ClassVisitor
    public ModuleVisitor visitModule(String name, int access, String version) {
        this.module = new ModuleNode(name, access, version);
        return this.module;
    }

    @Override // org.objectweb.asm.ClassVisitor
    public void visitNestHostExperimental(String nestHost) {
        this.nestHostClassExperimental = nestHost;
    }

    @Override // org.objectweb.asm.ClassVisitor
    public void visitOuterClass(String owner, String name, String descriptor) {
        this.outerClass = owner;
        this.outerMethod = name;
        this.outerMethodDesc = descriptor;
    }

    @Override // org.objectweb.asm.ClassVisitor
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        AnnotationNode annotation = new AnnotationNode(descriptor);
        if (visible) {
            if (this.visibleAnnotations == null) {
                this.visibleAnnotations = new ArrayList(1);
            }
            this.visibleAnnotations.add(annotation);
        } else {
            if (this.invisibleAnnotations == null) {
                this.invisibleAnnotations = new ArrayList(1);
            }
            this.invisibleAnnotations.add(annotation);
        }
        return annotation;
    }

    @Override // org.objectweb.asm.ClassVisitor
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        TypeAnnotationNode typeAnnotation = new TypeAnnotationNode(typeRef, typePath, descriptor);
        if (visible) {
            if (this.visibleTypeAnnotations == null) {
                this.visibleTypeAnnotations = new ArrayList(1);
            }
            this.visibleTypeAnnotations.add(typeAnnotation);
        } else {
            if (this.invisibleTypeAnnotations == null) {
                this.invisibleTypeAnnotations = new ArrayList(1);
            }
            this.invisibleTypeAnnotations.add(typeAnnotation);
        }
        return typeAnnotation;
    }

    @Override // org.objectweb.asm.ClassVisitor
    public void visitAttribute(Attribute attribute) {
        if (this.attrs == null) {
            this.attrs = new ArrayList(1);
        }
        this.attrs.add(attribute);
    }

    @Override // org.objectweb.asm.ClassVisitor
    public void visitNestMemberExperimental(String nestMember) {
        if (this.nestMembersExperimental == null) {
            this.nestMembersExperimental = new ArrayList();
        }
        this.nestMembersExperimental.add(nestMember);
    }

    @Override // org.objectweb.asm.ClassVisitor
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        InnerClassNode innerClass = new InnerClassNode(name, outerName, innerName, access);
        this.innerClasses.add(innerClass);
    }

    @Override // org.objectweb.asm.ClassVisitor
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        FieldNode field = new FieldNode(access, name, descriptor, signature, value);
        this.fields.add(field);
        return field;
    }

    @Override // org.objectweb.asm.ClassVisitor
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodNode method = new MethodNode(access, name, descriptor, signature, exceptions);
        this.methods.add(method);
        return method;
    }

    @Override // org.objectweb.asm.ClassVisitor
    public void visitEnd() {
    }

    public void check(int api) {
        if (api < 17235968 && (this.nestHostClassExperimental != null || this.nestMembersExperimental != null)) {
            throw new UnsupportedClassVersionException();
        }
        if (api < 393216 && this.module != null) {
            throw new UnsupportedClassVersionException();
        }
        if (api < 327680) {
            if (this.visibleTypeAnnotations != null && !this.visibleTypeAnnotations.isEmpty()) {
                throw new UnsupportedClassVersionException();
            }
            if (this.invisibleTypeAnnotations != null && !this.invisibleTypeAnnotations.isEmpty()) {
                throw new UnsupportedClassVersionException();
            }
        }
        if (this.visibleAnnotations != null) {
            for (int i = this.visibleAnnotations.size() - 1; i >= 0; i--) {
                this.visibleAnnotations.get(i).check(api);
            }
        }
        if (this.invisibleAnnotations != null) {
            for (int i2 = this.invisibleAnnotations.size() - 1; i2 >= 0; i2--) {
                this.invisibleAnnotations.get(i2).check(api);
            }
        }
        if (this.visibleTypeAnnotations != null) {
            for (int i3 = this.visibleTypeAnnotations.size() - 1; i3 >= 0; i3--) {
                this.visibleTypeAnnotations.get(i3).check(api);
            }
        }
        if (this.invisibleTypeAnnotations != null) {
            for (int i4 = this.invisibleTypeAnnotations.size() - 1; i4 >= 0; i4--) {
                this.invisibleTypeAnnotations.get(i4).check(api);
            }
        }
        for (int i5 = this.fields.size() - 1; i5 >= 0; i5--) {
            this.fields.get(i5).check(api);
        }
        for (int i6 = this.methods.size() - 1; i6 >= 0; i6--) {
            this.methods.get(i6).check(api);
        }
    }

    public void accept(ClassVisitor classVisitor) {
        String[] interfacesArray = new String[this.interfaces.size()];
        this.interfaces.toArray(interfacesArray);
        classVisitor.visit(this.version, this.access, this.name, this.signature, this.superName, interfacesArray);
        if (this.sourceFile != null || this.sourceDebug != null) {
            classVisitor.visitSource(this.sourceFile, this.sourceDebug);
        }
        if (this.module != null) {
            this.module.accept(classVisitor);
        }
        if (this.nestHostClassExperimental != null) {
            classVisitor.visitNestHostExperimental(this.nestHostClassExperimental);
        }
        if (this.outerClass != null) {
            classVisitor.visitOuterClass(this.outerClass, this.outerMethod, this.outerMethodDesc);
        }
        if (this.visibleAnnotations != null) {
            int n = this.visibleAnnotations.size();
            for (int i = 0; i < n; i++) {
                AnnotationNode annotation = this.visibleAnnotations.get(i);
                annotation.accept(classVisitor.visitAnnotation(annotation.desc, true));
            }
        }
        if (this.invisibleAnnotations != null) {
            int n2 = this.invisibleAnnotations.size();
            for (int i2 = 0; i2 < n2; i2++) {
                AnnotationNode annotation2 = this.invisibleAnnotations.get(i2);
                annotation2.accept(classVisitor.visitAnnotation(annotation2.desc, false));
            }
        }
        if (this.visibleTypeAnnotations != null) {
            int n3 = this.visibleTypeAnnotations.size();
            for (int i3 = 0; i3 < n3; i3++) {
                TypeAnnotationNode typeAnnotation = this.visibleTypeAnnotations.get(i3);
                typeAnnotation.accept(classVisitor.visitTypeAnnotation(typeAnnotation.typeRef, typeAnnotation.typePath, typeAnnotation.desc, true));
            }
        }
        if (this.invisibleTypeAnnotations != null) {
            int n4 = this.invisibleTypeAnnotations.size();
            for (int i4 = 0; i4 < n4; i4++) {
                TypeAnnotationNode typeAnnotation2 = this.invisibleTypeAnnotations.get(i4);
                typeAnnotation2.accept(classVisitor.visitTypeAnnotation(typeAnnotation2.typeRef, typeAnnotation2.typePath, typeAnnotation2.desc, false));
            }
        }
        if (this.attrs != null) {
            int n5 = this.attrs.size();
            for (int i5 = 0; i5 < n5; i5++) {
                classVisitor.visitAttribute(this.attrs.get(i5));
            }
        }
        if (this.nestMembersExperimental != null) {
            int n6 = this.nestMembersExperimental.size();
            for (int i6 = 0; i6 < n6; i6++) {
                classVisitor.visitNestMemberExperimental(this.nestMembersExperimental.get(i6));
            }
        }
        int n7 = this.innerClasses.size();
        for (int i7 = 0; i7 < n7; i7++) {
            this.innerClasses.get(i7).accept(classVisitor);
        }
        int n8 = this.fields.size();
        for (int i8 = 0; i8 < n8; i8++) {
            this.fields.get(i8).accept(classVisitor);
        }
        int n9 = this.methods.size();
        for (int i9 = 0; i9 < n9; i9++) {
            this.methods.get(i9).accept(classVisitor);
        }
        classVisitor.visitEnd();
    }
}
