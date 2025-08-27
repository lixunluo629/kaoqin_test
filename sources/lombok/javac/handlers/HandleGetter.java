package lombok.javac.handlers;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.JCDiagnostic;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import lombok.AccessLevel;
import lombok.ConfigurationKeys;
import lombok.Getter;
import lombok.core.AST;
import lombok.core.AnnotationValues;
import lombok.core.handlers.HandlerUtil;
import lombok.experimental.Delegate;
import lombok.javac.Javac;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;
import lombok.javac.handlers.JavacHandlerUtil;
import org.apache.ibatis.javassist.bytecode.DeprecatedAttribute;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;

/* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/HandleGetter.SCL.lombok */
public class HandleGetter extends JavacAnnotationHandler<Getter> {
    private static final String AR = "java.util.concurrent.atomic.AtomicReference";
    private static final List<JCTree.JCExpression> NIL_EXPRESSION = List.nil();
    public static final Map<JavacTreeMaker.TypeTag, String> TYPE_MAP;

    public void generateGetterForType(JavacNode typeNode, JavacNode errorNode, AccessLevel level, boolean checkForTypeLevelGetter, List<JCTree.JCAnnotation> onMethod) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (checkForTypeLevelGetter && JavacHandlerUtil.hasAnnotation((Class<? extends Annotation>) Getter.class, typeNode)) {
            return;
        }
        JCTree.JCClassDecl typeDecl = null;
        if (typeNode.get() instanceof JCTree.JCClassDecl) {
            typeDecl = (JCTree.JCClassDecl) typeNode.get();
        }
        long modifiers = typeDecl == null ? 0L : typeDecl.mods.flags;
        boolean notAClass = (modifiers & 8704) != 0;
        if (typeDecl == null || notAClass) {
            errorNode.addError("@Getter is only supported on a class, an enum, or a field.");
            return;
        }
        Iterator<JavacNode> it = typeNode.down().iterator();
        while (it.hasNext()) {
            JavacNode field = it.next();
            if (fieldQualifiesForGetterGeneration(field)) {
                generateGetterForField(field, (JCDiagnostic.DiagnosticPosition) errorNode.get(), level, false, onMethod);
            }
        }
    }

    public static boolean fieldQualifiesForGetterGeneration(JavacNode field) {
        if (field.getKind() != AST.Kind.FIELD) {
            return false;
        }
        JCTree.JCVariableDecl fieldDecl = field.get();
        return !fieldDecl.name.toString().startsWith(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX) && (fieldDecl.mods.flags & 8) == 0;
    }

    public void generateGetterForField(JavacNode fieldNode, JCDiagnostic.DiagnosticPosition pos, AccessLevel level, boolean lazy, List<JCTree.JCAnnotation> onMethod) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (JavacHandlerUtil.hasAnnotation((Class<? extends Annotation>) Getter.class, fieldNode)) {
            return;
        }
        createGetterForField(level, fieldNode, fieldNode, false, lazy, onMethod);
    }

    @Override // lombok.javac.JavacAnnotationHandler
    public void handle(AnnotationValues<Getter> annotation, JCTree.JCAnnotation ast, JavacNode annotationNode) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        HandlerUtil.handleFlagUsage(annotationNode, ConfigurationKeys.GETTER_FLAG_USAGE, "@Getter");
        Collection<JavacNode> fields = annotationNode.upFromAnnotationToFields();
        JavacHandlerUtil.deleteAnnotationIfNeccessary(annotationNode, (Class<? extends Annotation>) Getter.class);
        JavacHandlerUtil.deleteImportFromCompilationUnit(annotationNode, "lombok.AccessLevel");
        JavacNode node = annotationNode.up();
        Getter annotationInstance = (Getter) annotation.getInstance();
        AccessLevel level = annotationInstance.value();
        boolean lazy = annotationInstance.lazy();
        if (lazy) {
            HandlerUtil.handleFlagUsage(annotationNode, ConfigurationKeys.GETTER_LAZY_FLAG_USAGE, "@Getter(lazy=true)");
        }
        if (level == AccessLevel.NONE) {
            if (lazy) {
                annotationNode.addWarning("'lazy' does not work with AccessLevel.NONE.");
            }
        } else {
            if (node == null) {
                return;
            }
            List<JCTree.JCAnnotation> onMethod = JavacHandlerUtil.unboxAndRemoveAnnotationParameter(ast, "onMethod", "@Getter(onMethod", annotationNode);
            switch (node.getKind()) {
                case FIELD:
                    createGetterForFields(level, fields, annotationNode, true, lazy, onMethod);
                    break;
                case TYPE:
                    if (lazy) {
                        annotationNode.addError("'lazy' is not supported for @Getter on a type.");
                    }
                    generateGetterForType(node, annotationNode, level, false, onMethod);
                    break;
            }
        }
    }

    public void createGetterForFields(AccessLevel level, Collection<JavacNode> fieldNodes, JavacNode errorNode, boolean whineIfExists, boolean lazy, List<JCTree.JCAnnotation> onMethod) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        for (JavacNode fieldNode : fieldNodes) {
            createGetterForField(level, fieldNode, errorNode, whineIfExists, lazy, onMethod);
        }
    }

    public void createGetterForField(AccessLevel level, JavacNode fieldNode, JavacNode source, boolean whineIfExists, boolean lazy, List<JCTree.JCAnnotation> onMethod) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (fieldNode.getKind() != AST.Kind.FIELD) {
            source.addError("@Getter is only supported on a class or a field.");
            return;
        }
        JCTree.JCVariableDecl fieldDecl = fieldNode.get();
        if (lazy) {
            if ((fieldDecl.mods.flags & 2) == 0 || (fieldDecl.mods.flags & 16) == 0) {
                source.addError("'lazy' requires the field to be private and final.");
                return;
            } else if ((fieldDecl.mods.flags & 128) != 0) {
                source.addError("'lazy' is not supported on transient fields.");
                return;
            } else if (fieldDecl.init == null) {
                source.addError("'lazy' requires field initialization.");
                return;
            }
        }
        String methodName = JavacHandlerUtil.toGetterName(fieldNode);
        if (methodName == null) {
            source.addWarning("Not generating getter for this field: It does not fit your @Accessors prefix list.");
            return;
        }
        for (String altName : JavacHandlerUtil.toAllGetterNames(fieldNode)) {
            switch (JavacHandlerUtil.methodExists(altName, fieldNode, false, 0)) {
                case EXISTS_BY_LOMBOK:
                    return;
                case EXISTS_BY_USER:
                    if (whineIfExists) {
                        String altNameExpl = altName.equals(methodName) ? "" : String.format(" (%s)", altName);
                        source.addWarning(String.format("Not generating %s(): A method with that name already exists%s", methodName, altNameExpl));
                        return;
                    }
                    return;
            }
        }
        long access = JavacHandlerUtil.toJavacModifier(level) | (fieldDecl.mods.flags & 8);
        JavacHandlerUtil.injectMethod(fieldNode.up(), createGetter(access, fieldNode, fieldNode.getTreeMaker(), source.get(), lazy, onMethod), List.nil(), JavacHandlerUtil.getMirrorForFieldType(fieldNode));
    }

    public JCTree.JCMethodDecl createGetter(long access, JavacNode field, JavacTreeMaker treeMaker, JCTree source, boolean lazy, List<JCTree.JCAnnotation> onMethod) {
        List<JCTree.JCStatement> statements;
        JCTree.JCVariableDecl fieldNode = (JCTree.JCVariableDecl) field.get();
        JCTree.JCExpression methodType = copyType(treeMaker, fieldNode);
        Name methodName = field.toName(JavacHandlerUtil.toGetterName(field));
        JCTree.JCExpression jCExpression = null;
        if (lazy && !JavacHandlerUtil.inNetbeansEditor(field)) {
            jCExpression = fieldNode.init;
            statements = createLazyGetterBody(treeMaker, field, source);
        } else {
            statements = createSimpleGetterBody(treeMaker, field);
        }
        JCTree.JCBlock methodBody = treeMaker.Block(0L, statements);
        List<JCTree.JCTypeParameter> methodGenericParams = List.nil();
        List<JCTree.JCVariableDecl> parameters = List.nil();
        List<JCTree.JCExpression> throwsClauses = List.nil();
        List<JCTree.JCAnnotation> nonNulls = JavacHandlerUtil.findAnnotations(field, HandlerUtil.NON_NULL_PATTERN);
        List<JCTree.JCAnnotation> nullables = JavacHandlerUtil.findAnnotations(field, HandlerUtil.NULLABLE_PATTERN);
        List<JCTree.JCAnnotation> delegates = findDelegatesAndRemoveFromField(field);
        List<JCTree.JCAnnotation> annsOnMethod = JavacHandlerUtil.copyAnnotations(onMethod).appendList(nonNulls).appendList(nullables);
        if (JavacHandlerUtil.isFieldDeprecated(field)) {
            annsOnMethod = annsOnMethod.prepend(treeMaker.Annotation(JavacHandlerUtil.genJavaLangTypeRef(field, DeprecatedAttribute.tag), List.nil()));
        }
        JCTree.JCMethodDecl decl = JavacHandlerUtil.recursiveSetGeneratedBy(treeMaker.MethodDef(treeMaker.Modifiers(access, annsOnMethod), methodName, methodType, methodGenericParams, parameters, throwsClauses, methodBody, null), source, field.getContext());
        if (jCExpression != null) {
            JavacHandlerUtil.recursiveSetGeneratedBy(jCExpression, null, null);
        }
        decl.mods.annotations = decl.mods.annotations.appendList(delegates);
        JavacHandlerUtil.copyJavadoc(field, decl, JavacHandlerUtil.CopyJavadoc.GETTER);
        return decl;
    }

    public static List<JCTree.JCAnnotation> findDelegatesAndRemoveFromField(JavacNode field) {
        JCTree.JCVariableDecl fieldNode = field.get();
        List<JCTree.JCAnnotation> delegates = List.nil();
        Iterator it = fieldNode.mods.annotations.iterator();
        while (it.hasNext()) {
            JCTree.JCAnnotation annotation = (JCTree.JCAnnotation) it.next();
            if (JavacHandlerUtil.typeMatches((Class<?>) Delegate.class, field, annotation.annotationType)) {
                delegates = delegates.append(annotation);
            }
        }
        if (!delegates.isEmpty()) {
            ListBuffer<JCTree.JCAnnotation> withoutDelegates = new ListBuffer<>();
            Iterator it2 = fieldNode.mods.annotations.iterator();
            while (it2.hasNext()) {
                JCTree.JCAnnotation annotation2 = (JCTree.JCAnnotation) it2.next();
                if (!delegates.contains(annotation2)) {
                    withoutDelegates.append(annotation2);
                }
            }
            fieldNode.mods.annotations = withoutDelegates.toList();
            field.rebuild();
        }
        return delegates;
    }

    public List<JCTree.JCStatement> createSimpleGetterBody(JavacTreeMaker treeMaker, JavacNode field) {
        return List.of(treeMaker.Return(JavacHandlerUtil.createFieldAccessor(treeMaker, field, HandlerUtil.FieldAccess.ALWAYS_FIELD)));
    }

    static {
        Map<JavacTreeMaker.TypeTag, String> m = new HashMap<>();
        m.put(Javac.CTC_INT, "Integer");
        m.put(Javac.CTC_DOUBLE, "Double");
        m.put(Javac.CTC_FLOAT, "Float");
        m.put(Javac.CTC_SHORT, "Short");
        m.put(Javac.CTC_BYTE, "Byte");
        m.put(Javac.CTC_LONG, "Long");
        m.put(Javac.CTC_BOOLEAN, "Boolean");
        m.put(Javac.CTC_CHAR, "Character");
        TYPE_MAP = Collections.unmodifiableMap(m);
    }

    public List<JCTree.JCStatement> createLazyGetterBody(JavacTreeMaker maker, JavacNode fieldNode, JCTree source) {
        String boxed;
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
        JCTree.JCVariableDecl field = (JCTree.JCVariableDecl) fieldNode.get();
        JCTree.JCExpression copyOfRawFieldType = copyType(maker, field);
        JCTree.JCExpression copyOfBoxedFieldType = null;
        field.type = null;
        boolean isPrimitive = false;
        if ((field.vartype instanceof JCTree.JCPrimitiveTypeTree) && (boxed = TYPE_MAP.get(JavacTreeMaker.TypeTag.typeTag((JCTree) field.vartype))) != null) {
            isPrimitive = true;
            field.vartype = JavacHandlerUtil.genJavaLangTypeRef(fieldNode, boxed);
            copyOfBoxedFieldType = JavacHandlerUtil.genJavaLangTypeRef(fieldNode, boxed);
        }
        if (copyOfBoxedFieldType == null) {
            copyOfBoxedFieldType = copyType(maker, field);
        }
        Name valueName = fieldNode.toName("value");
        Name actualValueName = fieldNode.toName("actualValue");
        JCTree.JCExpression valueVarType = JavacHandlerUtil.genJavaLangTypeRef(fieldNode, "Object");
        statements.append(maker.VarDef(maker.Modifiers(0L), valueName, valueVarType, callGet(fieldNode, JavacHandlerUtil.createFieldAccessor(maker, fieldNode, HandlerUtil.FieldAccess.ALWAYS_FIELD))));
        ListBuffer<JCTree.JCStatement> synchronizedStatements = new ListBuffer<>();
        JCTree.JCExpressionStatement newAssign = maker.Exec(maker.Assign(maker.Ident(valueName), callGet(fieldNode, JavacHandlerUtil.createFieldAccessor(maker, fieldNode, HandlerUtil.FieldAccess.ALWAYS_FIELD))));
        synchronizedStatements.append(newAssign);
        ListBuffer<JCTree.JCStatement> innerIfStatements = new ListBuffer<>();
        innerIfStatements.append(maker.VarDef(maker.Modifiers(16L), actualValueName, copyOfRawFieldType, field.init));
        if (isPrimitive) {
            innerIfStatements.append(maker.Exec(maker.Assign(maker.Ident(valueName), maker.Ident(actualValueName))));
        }
        if (!isPrimitive) {
            JCTree.JCBinary jCBinaryBinary = maker.Binary(Javac.CTC_EQUAL, maker.Ident(actualValueName), maker.Literal(Javac.CTC_BOT, null));
            JCTree.JCExpression thisDotFieldName = JavacHandlerUtil.createFieldAccessor(maker, fieldNode, HandlerUtil.FieldAccess.ALWAYS_FIELD);
            innerIfStatements.append(maker.Exec(maker.Assign(maker.Ident(valueName), maker.Conditional(jCBinaryBinary, thisDotFieldName, maker.Ident(actualValueName)))));
        }
        JCTree.JCStatement statement = callSet(fieldNode, JavacHandlerUtil.createFieldAccessor(maker, fieldNode, HandlerUtil.FieldAccess.ALWAYS_FIELD), maker.Ident(valueName));
        innerIfStatements.append(statement);
        JCTree.JCBinary isNull = maker.Binary(Javac.CTC_EQUAL, maker.Ident(valueName), maker.Literal(Javac.CTC_BOT, null));
        JCTree.JCIf ifStatement = maker.If(isNull, maker.Block(0L, innerIfStatements.toList()), null);
        synchronizedStatements.append(ifStatement);
        JCTree.JCSynchronized synchronizedStatement = maker.Synchronized(JavacHandlerUtil.createFieldAccessor(maker, fieldNode, HandlerUtil.FieldAccess.ALWAYS_FIELD), maker.Block(0L, synchronizedStatements.toList()));
        JCTree.JCBinary isNull2 = maker.Binary(Javac.CTC_EQUAL, maker.Ident(valueName), maker.Literal(Javac.CTC_BOT, null));
        JCTree.JCIf ifStatement2 = maker.If(isNull2, maker.Block(0L, List.of(synchronizedStatement)), null);
        statements.append(ifStatement2);
        if (isPrimitive) {
            statements.append(maker.Return(maker.TypeCast(copyOfBoxedFieldType, maker.Ident(valueName))));
        }
        if (!isPrimitive) {
            statements.append(maker.Return(maker.TypeCast(copyOfBoxedFieldType, maker.Parens(maker.Conditional(maker.Binary(Javac.CTC_EQUAL, maker.Ident(valueName), JavacHandlerUtil.createFieldAccessor(maker, fieldNode, HandlerUtil.FieldAccess.ALWAYS_FIELD)), maker.Literal(Javac.CTC_BOT, null), maker.Ident(valueName))))));
        }
        field.vartype = JavacHandlerUtil.recursiveSetGeneratedBy(maker.TypeApply(JavacHandlerUtil.chainDotsString(fieldNode, AR), List.of(JavacHandlerUtil.genJavaLangTypeRef(fieldNode, "Object"))), source, fieldNode.getContext());
        field.init = JavacHandlerUtil.recursiveSetGeneratedBy(maker.NewClass(null, NIL_EXPRESSION, copyType(maker, field), NIL_EXPRESSION, null), source, fieldNode.getContext());
        return statements.toList();
    }

    public JCTree.JCMethodInvocation callGet(JavacNode source, JCTree.JCExpression receiver) {
        JavacTreeMaker maker = source.getTreeMaker();
        return maker.Apply(NIL_EXPRESSION, maker.Select(receiver, source.toName(BeanUtil.PREFIX_GETTER_GET)), NIL_EXPRESSION);
    }

    public JCTree.JCStatement callSet(JavacNode source, JCTree.JCExpression receiver, JCTree.JCExpression value) {
        JavacTreeMaker maker = source.getTreeMaker();
        return maker.Exec(maker.Apply(NIL_EXPRESSION, maker.Select(receiver, source.toName("set")), List.of(value)));
    }

    public JCTree.JCExpression copyType(JavacTreeMaker treeMaker, JCTree.JCVariableDecl fieldNode) {
        return fieldNode.type != null ? treeMaker.Type(fieldNode.type) : fieldNode.vartype;
    }
}
