package lombok.javac.handlers;

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.JCDiagnostic;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;
import lombok.AccessLevel;
import lombok.ConfigurationKeys;
import lombok.core.AST;
import lombok.core.AnnotationValues;
import lombok.core.handlers.HandlerUtil;
import lombok.experimental.Wither;
import lombok.javac.Javac;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;
import lombok.javac.handlers.JavacHandlerUtil;
import org.apache.ibatis.javassist.bytecode.DeprecatedAttribute;
import org.apache.ibatis.ognl.OgnlContext;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;

/* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/HandleWither.SCL.lombok */
public class HandleWither extends JavacAnnotationHandler<Wither> {
    public void generateWitherForType(JavacNode typeNode, JavacNode errorNode, AccessLevel level, boolean checkForTypeLevelWither) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (checkForTypeLevelWither && JavacHandlerUtil.hasAnnotation((Class<? extends Annotation>) Wither.class, typeNode)) {
            return;
        }
        JCTree.JCClassDecl typeDecl = null;
        if (typeNode.get() instanceof JCTree.JCClassDecl) {
            typeDecl = (JCTree.JCClassDecl) typeNode.get();
        }
        long modifiers = typeDecl == null ? 0L : typeDecl.mods.flags;
        boolean notAClass = (modifiers & 25088) != 0;
        if (typeDecl == null || notAClass) {
            errorNode.addError("@Wither is only supported on a class or a field.");
            return;
        }
        Iterator<JavacNode> it = typeNode.down().iterator();
        while (it.hasNext()) {
            JavacNode field = it.next();
            if (field.getKind() == AST.Kind.FIELD) {
                JCTree.JCVariableDecl fieldDecl = field.get();
                if (!fieldDecl.name.toString().startsWith(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX) && (fieldDecl.mods.flags & 8) == 0 && ((fieldDecl.mods.flags & 16) == 0 || fieldDecl.init == null)) {
                    generateWitherForField(field, (JCDiagnostic.DiagnosticPosition) errorNode.get(), level);
                }
            }
        }
    }

    public void generateWitherForField(JavacNode fieldNode, JCDiagnostic.DiagnosticPosition pos, AccessLevel level) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (JavacHandlerUtil.hasAnnotation((Class<? extends Annotation>) Wither.class, fieldNode)) {
            return;
        }
        createWitherForField(level, fieldNode, fieldNode, false, List.nil(), List.nil());
    }

    @Override // lombok.javac.JavacAnnotationHandler
    public void handle(AnnotationValues<Wither> annotation, JCTree.JCAnnotation ast, JavacNode annotationNode) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        HandlerUtil.handleExperimentalFlagUsage(annotationNode, ConfigurationKeys.WITHER_FLAG_USAGE, "@Wither");
        Collection<JavacNode> fields = annotationNode.upFromAnnotationToFields();
        JavacHandlerUtil.deleteAnnotationIfNeccessary(annotationNode, (Class<? extends Annotation>) Wither.class);
        JavacHandlerUtil.deleteImportFromCompilationUnit(annotationNode, "lombok.AccessLevel");
        JavacNode node = annotationNode.up();
        AccessLevel level = ((Wither) annotation.getInstance()).value();
        if (level == AccessLevel.NONE || node == null) {
            return;
        }
        List<JCTree.JCAnnotation> onMethod = JavacHandlerUtil.unboxAndRemoveAnnotationParameter(ast, "onMethod", "@Wither(onMethod", annotationNode);
        List<JCTree.JCAnnotation> onParam = JavacHandlerUtil.unboxAndRemoveAnnotationParameter(ast, "onParam", "@Wither(onParam", annotationNode);
        switch (node.getKind()) {
            case FIELD:
                createWitherForFields(level, fields, annotationNode, true, onMethod, onParam);
                break;
            case TYPE:
                if (!onMethod.isEmpty()) {
                    annotationNode.addError("'onMethod' is not supported for @Wither on a type.");
                }
                if (!onParam.isEmpty()) {
                    annotationNode.addError("'onParam' is not supported for @Wither on a type.");
                }
                generateWitherForType(node, annotationNode, level, false);
                break;
        }
    }

    public void createWitherForFields(AccessLevel level, Collection<JavacNode> fieldNodes, JavacNode errorNode, boolean whineIfExists, List<JCTree.JCAnnotation> onMethod, List<JCTree.JCAnnotation> onParam) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        for (JavacNode fieldNode : fieldNodes) {
            createWitherForField(level, fieldNode, errorNode, whineIfExists, onMethod, onParam);
        }
    }

    public void createWitherForField(AccessLevel level, JavacNode fieldNode, JavacNode source, boolean strictMode, List<JCTree.JCAnnotation> onMethod, List<JCTree.JCAnnotation> onParam) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        JavacNode typeNode = fieldNode.up();
        boolean makeAbstract = (typeNode == null || typeNode.getKind() != AST.Kind.TYPE || (typeNode.get().mods.flags & 1024) == 0) ? false : true;
        if (fieldNode.getKind() != AST.Kind.FIELD) {
            fieldNode.addError("@Wither is only supported on a class or a field.");
            return;
        }
        JCTree.JCVariableDecl fieldDecl = fieldNode.get();
        String methodName = JavacHandlerUtil.toWitherName(fieldNode);
        if (methodName == null) {
            fieldNode.addWarning("Not generating wither for this field: It does not fit your @Accessors prefix list.");
            return;
        }
        if ((fieldDecl.mods.flags & 8) != 0) {
            if (strictMode) {
                fieldNode.addWarning("Not generating wither for this field: Withers cannot be generated for static fields.");
                return;
            }
            return;
        }
        if ((fieldDecl.mods.flags & 16) != 0 && fieldDecl.init != null) {
            if (strictMode) {
                fieldNode.addWarning("Not generating wither for this field: Withers cannot be generated for final, initialized fields.");
                return;
            }
            return;
        }
        if (fieldDecl.name.toString().startsWith(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX)) {
            if (strictMode) {
                fieldNode.addWarning("Not generating wither for this field: Withers cannot be generated for fields starting with $.");
                return;
            }
            return;
        }
        for (String altName : JavacHandlerUtil.toAllWitherNames(fieldNode)) {
            switch (JavacHandlerUtil.methodExists(altName, fieldNode, false, 1)) {
                case EXISTS_BY_LOMBOK:
                    return;
                case EXISTS_BY_USER:
                    if (strictMode) {
                        String altNameExpl = altName.equals(methodName) ? "" : String.format(" (%s)", altName);
                        fieldNode.addWarning(String.format("Not generating %s(): A method with that name already exists%s", methodName, altNameExpl));
                        return;
                    }
                    return;
            }
        }
        long access = JavacHandlerUtil.toJavacModifier(level);
        JCTree.JCMethodDecl createdWither = createWither(access, fieldNode, fieldNode.getTreeMaker(), source, onMethod, onParam, makeAbstract);
        Symbol.ClassSymbol sym = fieldNode.up().get().sym;
        Type returnType = sym == null ? null : sym.type;
        JavacHandlerUtil.injectMethod(typeNode, createdWither, List.of(JavacHandlerUtil.getMirrorForFieldType(fieldNode)), returnType);
    }

    public JCTree.JCMethodDecl createWither(long access, JavacNode field, JavacTreeMaker maker, JavacNode source, List<JCTree.JCAnnotation> onMethod, List<JCTree.JCAnnotation> onParam, boolean makeAbstract) {
        JCTree.JCStatement nullCheck;
        String witherName = JavacHandlerUtil.toWitherName(field);
        if (witherName == null) {
            return null;
        }
        JCTree.JCVariableDecl fieldDecl = field.get();
        List<JCTree.JCAnnotation> nonNulls = JavacHandlerUtil.findAnnotations(field, HandlerUtil.NON_NULL_PATTERN);
        List<JCTree.JCAnnotation> nullables = JavacHandlerUtil.findAnnotations(field, HandlerUtil.NULLABLE_PATTERN);
        Name methodName = field.toName(witherName);
        JCTree.JCExpression returnType = JavacHandlerUtil.cloneSelfType(field);
        JCTree.JCBlock methodBody = null;
        long flags = JavacHandlerUtil.addFinalIfNeeded(8589934592L, field.getContext());
        List<JCTree.JCAnnotation> annsOnParam = JavacHandlerUtil.copyAnnotations(onParam).appendList(nonNulls).appendList(nullables);
        JCTree.JCVariableDecl param = maker.VarDef(maker.Modifiers(flags, annsOnParam), fieldDecl.name, fieldDecl.vartype, null);
        if (!makeAbstract) {
            ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
            JCTree.JCExpression selfType = JavacHandlerUtil.cloneSelfType(field);
            if (selfType == null) {
                return null;
            }
            ListBuffer<JCTree.JCExpression> args = new ListBuffer<>();
            Iterator<JavacNode> it = field.up().down().iterator();
            while (it.hasNext()) {
                JavacNode child = it.next();
                if (child.getKind() == AST.Kind.FIELD) {
                    JCTree.JCVariableDecl childDecl = child.get();
                    if (!childDecl.name.toString().startsWith(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX)) {
                        long fieldFlags = childDecl.mods.flags;
                        if ((fieldFlags & 8) == 0 && ((fieldFlags & 16) == 0 || childDecl.init == null)) {
                            if (child.get() == field.get()) {
                                args.append(maker.Ident(fieldDecl.name));
                            } else {
                                args.append(JavacHandlerUtil.createFieldAccessor(maker, child, HandlerUtil.FieldAccess.ALWAYS_FIELD));
                            }
                        }
                    }
                }
            }
            JCTree.JCNewClass newClass = maker.NewClass(null, List.nil(), selfType, args.toList(), null);
            JCTree.JCConditional conditional = maker.Conditional(maker.Binary(Javac.CTC_EQUAL, JavacHandlerUtil.createFieldAccessor(maker, field, HandlerUtil.FieldAccess.ALWAYS_FIELD), maker.Ident(fieldDecl.name)), maker.Ident(field.toName(OgnlContext.THIS_CONTEXT_KEY)), newClass);
            JCTree.JCReturn returnStatement = maker.Return(conditional);
            if (!nonNulls.isEmpty() && (nullCheck = JavacHandlerUtil.generateNullCheck(maker, field, source)) != null) {
                statements.append(nullCheck);
            }
            statements.append(returnStatement);
            methodBody = maker.Block(0L, statements.toList());
        }
        List<JCTree.JCTypeParameter> methodGenericParams = List.nil();
        List<JCTree.JCVariableDecl> parameters = List.of(param);
        List<JCTree.JCExpression> throwsClauses = List.nil();
        List<JCTree.JCAnnotation> annsOnMethod = JavacHandlerUtil.copyAnnotations(onMethod);
        if (JavacHandlerUtil.isFieldDeprecated(field)) {
            annsOnMethod = annsOnMethod.prepend(maker.Annotation(JavacHandlerUtil.genJavaLangTypeRef(field, DeprecatedAttribute.tag), List.nil()));
        }
        if (makeAbstract) {
            access |= 1024;
        }
        JCTree.JCMethodDecl decl = JavacHandlerUtil.recursiveSetGeneratedBy(maker.MethodDef(maker.Modifiers(access, annsOnMethod), methodName, returnType, methodGenericParams, parameters, throwsClauses, methodBody, null), source.get(), field.getContext());
        JavacHandlerUtil.copyJavadoc(field, decl, JavacHandlerUtil.CopyJavadoc.WITHER);
        return decl;
    }
}
