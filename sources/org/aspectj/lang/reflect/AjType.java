package org.aspectj.lang.reflect;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/lang/reflect/AjType.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/lang/reflect/AjType.class */
public interface AjType<T> extends Type, AnnotatedElement {
    String getName();

    Package getPackage();

    AjType<?>[] getInterfaces();

    int getModifiers();

    Class<T> getJavaClass();

    AjType<?> getSupertype();

    Type getGenericSupertype();

    Method getEnclosingMethod();

    Constructor getEnclosingConstructor();

    AjType<?> getEnclosingType();

    AjType<?> getDeclaringType();

    PerClause getPerClause();

    AjType<?>[] getAjTypes();

    AjType<?>[] getDeclaredAjTypes();

    Constructor getConstructor(AjType<?>... ajTypeArr) throws NoSuchMethodException;

    Constructor[] getConstructors();

    Constructor getDeclaredConstructor(AjType<?>... ajTypeArr) throws NoSuchMethodException;

    Constructor[] getDeclaredConstructors();

    Field getDeclaredField(String str) throws NoSuchFieldException;

    Field[] getDeclaredFields();

    Field getField(String str) throws NoSuchFieldException;

    Field[] getFields();

    Method getDeclaredMethod(String str, AjType<?>... ajTypeArr) throws NoSuchMethodException;

    Method getMethod(String str, AjType<?>... ajTypeArr) throws NoSuchMethodException;

    Method[] getDeclaredMethods();

    Method[] getMethods();

    Pointcut getDeclaredPointcut(String str) throws NoSuchPointcutException;

    Pointcut getPointcut(String str) throws NoSuchPointcutException;

    Pointcut[] getDeclaredPointcuts();

    Pointcut[] getPointcuts();

    Advice[] getDeclaredAdvice(AdviceKind... adviceKindArr);

    Advice[] getAdvice(AdviceKind... adviceKindArr);

    Advice getAdvice(String str) throws NoSuchAdviceException;

    Advice getDeclaredAdvice(String str) throws NoSuchAdviceException;

    InterTypeMethodDeclaration getDeclaredITDMethod(String str, AjType<?> ajType, AjType<?>... ajTypeArr) throws NoSuchMethodException;

    InterTypeMethodDeclaration[] getDeclaredITDMethods();

    InterTypeMethodDeclaration getITDMethod(String str, AjType<?> ajType, AjType<?>... ajTypeArr) throws NoSuchMethodException;

    InterTypeMethodDeclaration[] getITDMethods();

    InterTypeConstructorDeclaration getDeclaredITDConstructor(AjType<?> ajType, AjType<?>... ajTypeArr) throws NoSuchMethodException;

    InterTypeConstructorDeclaration[] getDeclaredITDConstructors();

    InterTypeConstructorDeclaration getITDConstructor(AjType<?> ajType, AjType<?>... ajTypeArr) throws NoSuchMethodException;

    InterTypeConstructorDeclaration[] getITDConstructors();

    InterTypeFieldDeclaration getDeclaredITDField(String str, AjType<?> ajType) throws NoSuchFieldException;

    InterTypeFieldDeclaration[] getDeclaredITDFields();

    InterTypeFieldDeclaration getITDField(String str, AjType<?> ajType) throws NoSuchFieldException;

    InterTypeFieldDeclaration[] getITDFields();

    DeclareErrorOrWarning[] getDeclareErrorOrWarnings();

    DeclareParents[] getDeclareParents();

    DeclareSoft[] getDeclareSofts();

    DeclareAnnotation[] getDeclareAnnotations();

    DeclarePrecedence[] getDeclarePrecedence();

    T[] getEnumConstants();

    TypeVariable<Class<T>>[] getTypeParameters();

    boolean isEnum();

    boolean isInstance(Object obj);

    boolean isInterface();

    boolean isLocalClass();

    boolean isMemberClass();

    boolean isArray();

    boolean isPrimitive();

    boolean isAspect();

    boolean isMemberAspect();

    boolean isPrivileged();
}
