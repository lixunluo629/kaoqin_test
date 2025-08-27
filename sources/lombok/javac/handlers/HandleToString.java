package lombok.javac.handlers;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import com.sun.tools.javac.tree.JCTree;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import lombok.ConfigurationKeys;
import lombok.ToString;
import lombok.core.AST;
import lombok.core.AnnotationValues;
import lombok.core.handlers.HandlerUtil;
import lombok.core.handlers.InclusionExclusionUtils;
import lombok.javac.Javac;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;

/* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/HandleToString.SCL.lombok */
public class HandleToString extends JavacAnnotationHandler<ToString> {
    @Override // lombok.javac.JavacAnnotationHandler
    public void handle(AnnotationValues<ToString> annotation, JCTree.JCAnnotation ast, JavacNode annotationNode) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        HandlerUtil.handleFlagUsage(annotationNode, ConfigurationKeys.TO_STRING_FLAG_USAGE, "@ToString");
        JavacHandlerUtil.deleteAnnotationIfNeccessary(annotationNode, (Class<? extends Annotation>) ToString.class);
        ToString ann = (ToString) annotation.getInstance();
        List<InclusionExclusionUtils.Included<JavacNode, ToString.Include>> members = InclusionExclusionUtils.handleToStringMarking(annotationNode.up(), annotation, annotationNode);
        if (members == null) {
            return;
        }
        Boolean callSuper = Boolean.valueOf(ann.callSuper());
        if (!annotation.isExplicit("callSuper")) {
            callSuper = null;
        }
        Boolean doNotUseGettersConfiguration = (Boolean) annotationNode.getAst().readConfiguration(ConfigurationKeys.TO_STRING_DO_NOT_USE_GETTERS);
        boolean doNotUseGetters = (annotation.isExplicit("doNotUseGetters") || doNotUseGettersConfiguration == null) ? ann.doNotUseGetters() : doNotUseGettersConfiguration.booleanValue();
        HandlerUtil.FieldAccess fieldAccess = doNotUseGetters ? HandlerUtil.FieldAccess.PREFER_FIELD : HandlerUtil.FieldAccess.GETTER;
        Boolean fieldNamesConfiguration = (Boolean) annotationNode.getAst().readConfiguration(ConfigurationKeys.TO_STRING_INCLUDE_FIELD_NAMES);
        boolean includeNames = (annotation.isExplicit("includeFieldNames") || fieldNamesConfiguration == null) ? ann.includeFieldNames() : fieldNamesConfiguration.booleanValue();
        generateToString(annotationNode.up(), annotationNode, members, includeNames, callSuper, true, fieldAccess);
    }

    public void generateToStringForType(JavacNode typeNode, JavacNode errorNode) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (JavacHandlerUtil.hasAnnotation((Class<? extends Annotation>) ToString.class, typeNode)) {
            return;
        }
        boolean includeFieldNames = true;
        try {
            Boolean configuration = (Boolean) typeNode.getAst().readConfiguration(ConfigurationKeys.TO_STRING_INCLUDE_FIELD_NAMES);
            includeFieldNames = configuration != null ? configuration.booleanValue() : ((Boolean) ToString.class.getMethod("includeFieldNames", new Class[0]).getDefaultValue()).booleanValue();
        } catch (Exception e) {
        }
        Boolean doNotUseGettersConfiguration = (Boolean) typeNode.getAst().readConfiguration(ConfigurationKeys.TO_STRING_DO_NOT_USE_GETTERS);
        HandlerUtil.FieldAccess access = (doNotUseGettersConfiguration == null || !doNotUseGettersConfiguration.booleanValue()) ? HandlerUtil.FieldAccess.GETTER : HandlerUtil.FieldAccess.PREFER_FIELD;
        List<InclusionExclusionUtils.Included<JavacNode, ToString.Include>> members = InclusionExclusionUtils.handleToStringMarking(typeNode, null, null);
        generateToString(typeNode, errorNode, members, includeFieldNames, null, false, access);
    }

    public void generateToString(JavacNode typeNode, JavacNode source, List<InclusionExclusionUtils.Included<JavacNode, ToString.Include>> members, boolean includeFieldNames, Boolean callSuper, boolean whineIfExists, HandlerUtil.FieldAccess fieldAccess) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        boolean notAClass = true;
        if (typeNode.get() instanceof JCTree.JCClassDecl) {
            long flags = typeNode.get().mods.flags;
            notAClass = (flags & 8704) != 0;
        }
        if (callSuper == null) {
            try {
                callSuper = Boolean.valueOf(((Boolean) ToString.class.getMethod("callSuper", new Class[0]).getDefaultValue()).booleanValue());
            } catch (Exception e) {
            }
        }
        if (notAClass) {
            source.addError("@ToString is only supported on a class or enum.");
        }
        switch (JavacHandlerUtil.methodExists("toString", typeNode, 0)) {
            case NOT_EXISTS:
                JCTree.JCMethodDecl method = createToString(typeNode, members, includeFieldNames, callSuper.booleanValue(), fieldAccess, source.get());
                JavacHandlerUtil.injectMethod(typeNode, method);
                break;
            case EXISTS_BY_LOMBOK:
                break;
            case EXISTS_BY_USER:
            default:
                if (whineIfExists) {
                    source.addWarning("Not generating toString(): A method with that name already exists");
                    break;
                }
                break;
        }
    }

    static JCTree.JCMethodDecl createToString(JavacNode typeNode, Collection<InclusionExclusionUtils.Included<JavacNode, ToString.Include>> members, boolean includeNames, boolean callSuper, HandlerUtil.FieldAccess fieldAccess, JCTree source) {
        String prefix;
        JCTree.JCExpression memberAccessor;
        JCTree.JCExpression expr;
        JCTree.JCBinary jCBinaryBinary;
        JavacTreeMaker maker = typeNode.getTreeMaker();
        JCTree.JCAnnotation overrideAnnotation = maker.Annotation(JavacHandlerUtil.genJavaLangTypeRef(typeNode, "Override"), com.sun.tools.javac.util.List.nil());
        JCTree.JCModifiers mods = maker.Modifiers(1L, com.sun.tools.javac.util.List.of(overrideAnnotation));
        JCTree.JCExpression returnType = JavacHandlerUtil.genJavaLangTypeRef(typeNode, "String");
        boolean first = true;
        String typeName = getTypeName(typeNode);
        if (callSuper) {
            prefix = typeName + "(super=";
        } else if (members.isEmpty()) {
            prefix = typeName + "()";
        } else if (includeNames) {
            InclusionExclusionUtils.Included<JavacNode, ToString.Include> firstMember = members.iterator().next();
            String name = firstMember.getInc() == null ? "" : ((ToString.Include) firstMember.getInc()).name();
            if (name.isEmpty()) {
                name = firstMember.getNode().getName();
            }
            prefix = typeName + "(" + name + SymbolConstants.EQUAL_SYMBOL;
        } else {
            prefix = typeName + "(";
        }
        JCTree.JCExpression current = maker.Literal(prefix);
        if (callSuper) {
            JCTree.JCMethodInvocation callToSuper = maker.Apply(com.sun.tools.javac.util.List.nil(), maker.Select(maker.Ident(typeNode.toName("super")), typeNode.toName("toString")), com.sun.tools.javac.util.List.nil());
            current = maker.Binary(Javac.CTC_PLUS, current, callToSuper);
            first = false;
        }
        for (InclusionExclusionUtils.Included<JavacNode, ToString.Include> member : members) {
            JavacNode memberNode = member.getNode();
            if (memberNode.getKind() == AST.Kind.METHOD) {
                memberAccessor = JavacHandlerUtil.createMethodAccessor(maker, memberNode);
            } else {
                memberAccessor = JavacHandlerUtil.createFieldAccessor(maker, memberNode, fieldAccess);
            }
            JCTree.JCArrayTypeTree fieldType = JavacHandlerUtil.getFieldType(memberNode, fieldAccess);
            boolean z = fieldType instanceof JCTree.JCPrimitiveTypeTree;
            boolean fieldIsPrimitiveArray = (fieldType instanceof JCTree.JCArrayTypeTree) && (fieldType.elemtype instanceof JCTree.JCPrimitiveTypeTree);
            boolean fieldIsObjectArray = !fieldIsPrimitiveArray && (fieldType instanceof JCTree.JCArrayTypeTree);
            if (fieldIsPrimitiveArray || fieldIsObjectArray) {
                String[] strArr = new String[2];
                strArr[0] = "Arrays";
                strArr[1] = fieldIsObjectArray ? "deepToString" : "toString";
                JCTree.JCExpression tsMethod = JavacHandlerUtil.chainDots(typeNode, "java", "util", strArr);
                expr = maker.Apply(com.sun.tools.javac.util.List.nil(), tsMethod, com.sun.tools.javac.util.List.of(memberAccessor));
            } else {
                expr = memberAccessor;
            }
            if (first) {
                current = maker.Binary(Javac.CTC_PLUS, current, expr);
                first = false;
            } else {
                if (includeNames) {
                    String n = member.getInc() == null ? "" : ((ToString.Include) member.getInc()).name();
                    if (n.isEmpty()) {
                        n = memberNode.getName();
                    }
                    jCBinaryBinary = maker.Binary(Javac.CTC_PLUS, current, maker.Literal(", " + n + SymbolConstants.EQUAL_SYMBOL));
                } else {
                    jCBinaryBinary = maker.Binary(Javac.CTC_PLUS, current, maker.Literal(", "));
                }
                current = maker.Binary(Javac.CTC_PLUS, jCBinaryBinary, expr);
            }
        }
        if (!first) {
            current = maker.Binary(Javac.CTC_PLUS, current, maker.Literal(")"));
        }
        JCTree.JCBlock body = maker.Block(0L, com.sun.tools.javac.util.List.of(maker.Return(current)));
        return JavacHandlerUtil.recursiveSetGeneratedBy(maker.MethodDef(mods, typeNode.toName("toString"), returnType, com.sun.tools.javac.util.List.nil(), com.sun.tools.javac.util.List.nil(), com.sun.tools.javac.util.List.nil(), body, null), source, typeNode.getContext());
    }

    public static String getTypeName(JavacNode typeNode) {
        String typeName = typeNode.get().name.toString();
        JavacNode javacNodeUp = typeNode.up();
        while (true) {
            JavacNode upType = javacNodeUp;
            if (upType.getKind() == AST.Kind.TYPE) {
                typeName = upType.get().name.toString() + "." + typeName;
                javacNodeUp = upType.up();
            } else {
                return typeName;
            }
        }
    }
}
