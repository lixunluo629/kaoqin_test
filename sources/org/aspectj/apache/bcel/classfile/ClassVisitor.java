package org.aspectj.apache.bcel.classfile;

import org.aspectj.apache.bcel.classfile.annotation.RuntimeInvisAnnos;
import org.aspectj.apache.bcel.classfile.annotation.RuntimeInvisParamAnnos;
import org.aspectj.apache.bcel.classfile.annotation.RuntimeInvisTypeAnnos;
import org.aspectj.apache.bcel.classfile.annotation.RuntimeVisAnnos;
import org.aspectj.apache.bcel.classfile.annotation.RuntimeVisParamAnnos;
import org.aspectj.apache.bcel.classfile.annotation.RuntimeVisTypeAnnos;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/ClassVisitor.class */
public interface ClassVisitor {
    void visitCode(Code code);

    void visitCodeException(CodeException codeException);

    void visitConstantClass(ConstantClass constantClass);

    void visitConstantDouble(ConstantDouble constantDouble);

    void visitConstantFieldref(ConstantFieldref constantFieldref);

    void visitConstantFloat(ConstantFloat constantFloat);

    void visitConstantInteger(ConstantInteger constantInteger);

    void visitConstantInterfaceMethodref(ConstantInterfaceMethodref constantInterfaceMethodref);

    void visitConstantLong(ConstantLong constantLong);

    void visitConstantMethodref(ConstantMethodref constantMethodref);

    void visitConstantMethodHandle(ConstantMethodHandle constantMethodHandle);

    void visitConstantNameAndType(ConstantNameAndType constantNameAndType);

    void visitConstantMethodType(ConstantMethodType constantMethodType);

    void visitConstantInvokeDynamic(ConstantInvokeDynamic constantInvokeDynamic);

    void visitConstantPool(ConstantPool constantPool);

    void visitConstantString(ConstantString constantString);

    void visitConstantUtf8(ConstantUtf8 constantUtf8);

    void visitConstantValue(ConstantValue constantValue);

    void visitDeprecated(Deprecated deprecated);

    void visitExceptionTable(ExceptionTable exceptionTable);

    void visitField(Field field);

    void visitInnerClass(InnerClass innerClass);

    void visitInnerClasses(InnerClasses innerClasses);

    void visitJavaClass(JavaClass javaClass);

    void visitLineNumber(LineNumber lineNumber);

    void visitLineNumberTable(LineNumberTable lineNumberTable);

    void visitLocalVariable(LocalVariable localVariable);

    void visitLocalVariableTable(LocalVariableTable localVariableTable);

    void visitMethod(Method method);

    void visitSignature(Signature signature);

    void visitSourceFile(SourceFile sourceFile);

    void visitSynthetic(Synthetic synthetic);

    void visitBootstrapMethods(BootstrapMethods bootstrapMethods);

    void visitUnknown(Unknown unknown);

    void visitStackMap(StackMap stackMap);

    void visitStackMapEntry(StackMapEntry stackMapEntry);

    void visitEnclosingMethod(EnclosingMethod enclosingMethod);

    void visitRuntimeVisibleAnnotations(RuntimeVisAnnos runtimeVisAnnos);

    void visitRuntimeInvisibleAnnotations(RuntimeInvisAnnos runtimeInvisAnnos);

    void visitRuntimeVisibleParameterAnnotations(RuntimeVisParamAnnos runtimeVisParamAnnos);

    void visitRuntimeInvisibleParameterAnnotations(RuntimeInvisParamAnnos runtimeInvisParamAnnos);

    void visitRuntimeVisibleTypeAnnotations(RuntimeVisTypeAnnos runtimeVisTypeAnnos);

    void visitRuntimeInvisibleTypeAnnotations(RuntimeInvisTypeAnnos runtimeInvisTypeAnnos);

    void visitAnnotationDefault(AnnotationDefault annotationDefault);

    void visitLocalVariableTypeTable(LocalVariableTypeTable localVariableTypeTable);

    void visitMethodParameters(MethodParameters methodParameters);
}
