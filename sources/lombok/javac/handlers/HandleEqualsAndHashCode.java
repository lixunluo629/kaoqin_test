package lombok.javac.handlers;

import com.sun.tools.javac.code.BoundKind;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.lang.model.type.TypeKind;
import lombok.ConfigurationKeys;
import lombok.EqualsAndHashCode;
import lombok.core.AST;
import lombok.core.AnnotationValues;
import lombok.core.configuration.CallSuperType;
import lombok.core.handlers.HandlerUtil;
import lombok.core.handlers.InclusionExclusionUtils;
import lombok.javac.Javac;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;
import lombok.javac.handlers.JavacHandlerUtil;
import org.apache.ibatis.ognl.OgnlContext;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.jmx.export.naming.IdentityNamingStrategy;
import org.springframework.util.ClassUtils;

/* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/HandleEqualsAndHashCode.SCL.lombok */
public class HandleEqualsAndHashCode extends JavacAnnotationHandler<EqualsAndHashCode> {
    private static final String RESULT_NAME = "result";
    private static final String PRIME_NAME = "PRIME";

    @Override // lombok.javac.JavacAnnotationHandler
    public void handle(AnnotationValues<EqualsAndHashCode> annotation, JCTree.JCAnnotation ast, JavacNode annotationNode) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        HandlerUtil.handleFlagUsage(annotationNode, ConfigurationKeys.EQUALS_AND_HASH_CODE_FLAG_USAGE, "@EqualsAndHashCode");
        JavacHandlerUtil.deleteAnnotationIfNeccessary(annotationNode, (Class<? extends Annotation>) EqualsAndHashCode.class);
        EqualsAndHashCode ann = (EqualsAndHashCode) annotation.getInstance();
        List<InclusionExclusionUtils.Included<JavacNode, EqualsAndHashCode.Include>> members = InclusionExclusionUtils.handleEqualsAndHashCodeMarking(annotationNode.up(), annotation, annotationNode);
        JavacNode typeNode = annotationNode.up();
        com.sun.tools.javac.util.List<JCTree.JCAnnotation> onParam = JavacHandlerUtil.unboxAndRemoveAnnotationParameter(ast, "onParam", "@EqualsAndHashCode(onParam", annotationNode);
        Boolean callSuper = Boolean.valueOf(ann.callSuper());
        if (!annotation.isExplicit("callSuper")) {
            callSuper = null;
        }
        Boolean doNotUseGettersConfiguration = (Boolean) annotationNode.getAst().readConfiguration(ConfigurationKeys.EQUALS_AND_HASH_CODE_DO_NOT_USE_GETTERS);
        boolean doNotUseGetters = (annotation.isExplicit("doNotUseGetters") || doNotUseGettersConfiguration == null) ? ann.doNotUseGetters() : doNotUseGettersConfiguration.booleanValue();
        HandlerUtil.FieldAccess fieldAccess = doNotUseGetters ? HandlerUtil.FieldAccess.PREFER_FIELD : HandlerUtil.FieldAccess.GETTER;
        generateMethods(typeNode, annotationNode, members, callSuper, true, fieldAccess, onParam);
    }

    public void generateEqualsAndHashCodeForType(JavacNode typeNode, JavacNode source) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (JavacHandlerUtil.hasAnnotation((Class<? extends Annotation>) EqualsAndHashCode.class, typeNode)) {
            return;
        }
        Boolean doNotUseGettersConfiguration = (Boolean) typeNode.getAst().readConfiguration(ConfigurationKeys.EQUALS_AND_HASH_CODE_DO_NOT_USE_GETTERS);
        HandlerUtil.FieldAccess access = (doNotUseGettersConfiguration == null || !doNotUseGettersConfiguration.booleanValue()) ? HandlerUtil.FieldAccess.GETTER : HandlerUtil.FieldAccess.PREFER_FIELD;
        List<InclusionExclusionUtils.Included<JavacNode, EqualsAndHashCode.Include>> members = InclusionExclusionUtils.handleEqualsAndHashCodeMarking(typeNode, null, null);
        generateMethods(typeNode, source, members, null, false, access, com.sun.tools.javac.util.List.nil());
    }

    public void generateMethods(JavacNode typeNode, JavacNode source, List<InclusionExclusionUtils.Included<JavacNode, EqualsAndHashCode.Include>> members, Boolean callSuper, boolean whineIfExists, HandlerUtil.FieldAccess fieldAccess, com.sun.tools.javac.util.List<JCTree.JCAnnotation> onParam) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        boolean notAClass = true;
        if (typeNode.get() instanceof JCTree.JCClassDecl) {
            long flags = typeNode.get().mods.flags;
            notAClass = (flags & 25088) != 0;
        }
        if (notAClass) {
            source.addError("@EqualsAndHashCode is only supported on a class.");
            return;
        }
        boolean isDirectDescendantOfObject = true;
        boolean implicitCallSuper = callSuper == null;
        if (callSuper == null) {
            try {
                callSuper = Boolean.valueOf(((Boolean) EqualsAndHashCode.class.getMethod("callSuper", new Class[0]).getDefaultValue()).booleanValue());
            } catch (Exception e) {
                throw new InternalError("Lombok bug - this cannot happen - can't find callSuper field in EqualsAndHashCode annotation.");
            }
        }
        JCTree extending = Javac.getExtendsClause(typeNode.get());
        if (extending != null) {
            String p = extending.toString();
            isDirectDescendantOfObject = p.equals("Object") || p.equals("java.lang.Object");
        }
        if (isDirectDescendantOfObject && callSuper.booleanValue()) {
            source.addError("Generating equals/hashCode with a supercall to java.lang.Object is pointless.");
            return;
        }
        if (implicitCallSuper && !isDirectDescendantOfObject) {
            CallSuperType cst = (CallSuperType) typeNode.getAst().readConfiguration(ConfigurationKeys.EQUALS_AND_HASH_CODE_CALL_SUPER);
            if (cst == null) {
                cst = CallSuperType.WARN;
            }
            switch (cst) {
                case WARN:
                default:
                    source.addWarning("Generating equals/hashCode implementation but without a call to superclass, even though this class does not extend java.lang.Object. If this is intentional, add '@EqualsAndHashCode(callSuper=false)' to your type.");
                    callSuper = false;
                    break;
                case SKIP:
                    callSuper = false;
                    break;
                case CALL:
                    callSuper = true;
                    break;
            }
        }
        boolean isFinal = (typeNode.get().mods.flags & 16) != 0;
        boolean needsCanEqual = (isFinal && isDirectDescendantOfObject) ? false : true;
        JavacHandlerUtil.MemberExistsResult equalsExists = JavacHandlerUtil.methodExists("equals", typeNode, 1);
        JavacHandlerUtil.MemberExistsResult hashCodeExists = JavacHandlerUtil.methodExists(IdentityNamingStrategy.HASH_CODE_KEY, typeNode, 0);
        JavacHandlerUtil.MemberExistsResult canEqualExists = JavacHandlerUtil.methodExists("canEqual", typeNode, 1);
        switch ((JavacHandlerUtil.MemberExistsResult) Collections.max(Arrays.asList(equalsExists, hashCodeExists))) {
            case EXISTS_BY_LOMBOK:
                return;
            case EXISTS_BY_USER:
                if (whineIfExists) {
                    source.addWarning("Not generating equals and hashCode: A method with one of those names already exists. (Either both or none of these methods will be generated).");
                    return;
                }
                if (equalsExists == JavacHandlerUtil.MemberExistsResult.NOT_EXISTS || hashCodeExists == JavacHandlerUtil.MemberExistsResult.NOT_EXISTS) {
                    Object[] objArr = new Object[1];
                    objArr[0] = equalsExists == JavacHandlerUtil.MemberExistsResult.NOT_EXISTS ? "equals" : IdentityNamingStrategy.HASH_CODE_KEY;
                    String msg = String.format("Not generating %s: One of equals or hashCode exists. You should either write both of these or none of these (in the latter case, lombok generates them).", objArr);
                    source.addWarning(msg);
                    return;
                }
                return;
            case NOT_EXISTS:
            default:
                JCTree.JCMethodDecl equalsMethod = createEquals(typeNode, members, callSuper.booleanValue(), fieldAccess, needsCanEqual, source.get(), onParam);
                JavacHandlerUtil.injectMethod(typeNode, equalsMethod);
                if (needsCanEqual && canEqualExists == JavacHandlerUtil.MemberExistsResult.NOT_EXISTS) {
                    JCTree.JCMethodDecl canEqualMethod = createCanEqual(typeNode, source.get(), onParam);
                    JavacHandlerUtil.injectMethod(typeNode, canEqualMethod);
                }
                JCTree.JCMethodDecl hashCodeMethod = createHashCode(typeNode, members, callSuper.booleanValue(), fieldAccess, source.get());
                JavacHandlerUtil.injectMethod(typeNode, hashCodeMethod);
                return;
        }
    }

    public JCTree.JCMethodDecl createHashCode(JavacNode typeNode, List<InclusionExclusionUtils.Included<JavacNode, EqualsAndHashCode.Include>> members, boolean callSuper, HandlerUtil.FieldAccess fieldAccess, JCTree source) {
        JCTree.JCMethodInvocation jCMethodInvocationLiteral;
        JavacTreeMaker maker = typeNode.getTreeMaker();
        JCTree.JCAnnotation overrideAnnotation = maker.Annotation(JavacHandlerUtil.genJavaLangTypeRef(typeNode, "Override"), com.sun.tools.javac.util.List.nil());
        JCTree.JCModifiers mods = maker.Modifiers(1L, com.sun.tools.javac.util.List.of(overrideAnnotation));
        JCTree.JCPrimitiveTypeTree jCPrimitiveTypeTreeTypeIdent = maker.TypeIdent(Javac.CTC_INT);
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
        Name primeName = typeNode.toName(PRIME_NAME);
        Name resultName = typeNode.toName("result");
        long finalFlag = JavacHandlerUtil.addFinalIfNeeded(0L, typeNode.getContext());
        if (!members.isEmpty()) {
            statements.append(maker.VarDef(maker.Modifiers(finalFlag), primeName, maker.TypeIdent(Javac.CTC_INT), maker.Literal(Integer.valueOf(HandlerUtil.primeForHashcode()))));
        }
        if (callSuper) {
            jCMethodInvocationLiteral = maker.Apply(com.sun.tools.javac.util.List.nil(), maker.Select(maker.Ident(typeNode.toName("super")), typeNode.toName(IdentityNamingStrategy.HASH_CODE_KEY)), com.sun.tools.javac.util.List.nil());
        } else {
            jCMethodInvocationLiteral = maker.Literal(1);
        }
        statements.append(maker.VarDef(maker.Modifiers(0L), resultName, maker.TypeIdent(Javac.CTC_INT), jCMethodInvocationLiteral));
        for (InclusionExclusionUtils.Included<JavacNode, EqualsAndHashCode.Include> member : members) {
            JavacNode memberNode = member.getNode();
            JCTree.JCArrayTypeTree fieldType = JavacHandlerUtil.getFieldType(memberNode, fieldAccess);
            boolean isMethod = memberNode.getKind() == AST.Kind.METHOD;
            JCTree.JCExpression fieldAccessor = isMethod ? JavacHandlerUtil.createMethodAccessor(maker, memberNode) : JavacHandlerUtil.createFieldAccessor(maker, memberNode, fieldAccess);
            if (fieldType instanceof JCTree.JCPrimitiveTypeTree) {
                switch (AnonymousClass1.$SwitchMap$javax$lang$model$type$TypeKind[((JCTree.JCPrimitiveTypeTree) fieldType).getPrimitiveTypeKind().ordinal()]) {
                    case 1:
                        statements.append(createResultCalculation(typeNode, maker.Parens(maker.Conditional(fieldAccessor, maker.Literal(Integer.valueOf(HandlerUtil.primeForTrue())), maker.Literal(Integer.valueOf(HandlerUtil.primeForFalse()))))));
                        break;
                    case 2:
                        Name dollarFieldName = memberNode.toName((isMethod ? ClassUtils.CGLIB_CLASS_SEPARATOR : PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX) + memberNode.getName());
                        statements.append(maker.VarDef(maker.Modifiers(finalFlag), dollarFieldName, maker.TypeIdent(Javac.CTC_LONG), fieldAccessor));
                        statements.append(createResultCalculation(typeNode, longToIntForHashCode(maker, maker.Ident(dollarFieldName), maker.Ident(dollarFieldName))));
                        break;
                    case 3:
                        statements.append(createResultCalculation(typeNode, maker.Apply(com.sun.tools.javac.util.List.nil(), JavacHandlerUtil.genJavaLangTypeRef(typeNode, "Float", "floatToIntBits"), com.sun.tools.javac.util.List.of(fieldAccessor))));
                        break;
                    case 4:
                        Name dollarFieldName2 = memberNode.toName((isMethod ? ClassUtils.CGLIB_CLASS_SEPARATOR : PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX) + memberNode.getName());
                        statements.append(maker.VarDef(maker.Modifiers(finalFlag), dollarFieldName2, maker.TypeIdent(Javac.CTC_LONG), maker.Apply(com.sun.tools.javac.util.List.nil(), JavacHandlerUtil.genJavaLangTypeRef(typeNode, "Double", "doubleToLongBits"), com.sun.tools.javac.util.List.of(fieldAccessor))));
                        statements.append(createResultCalculation(typeNode, longToIntForHashCode(maker, maker.Ident(dollarFieldName2), maker.Ident(dollarFieldName2))));
                        break;
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    default:
                        statements.append(createResultCalculation(typeNode, fieldAccessor));
                        break;
                }
            } else if (fieldType instanceof JCTree.JCArrayTypeTree) {
                boolean multiDim = fieldType.elemtype instanceof JCTree.JCArrayTypeTree;
                boolean primitiveArray = fieldType.elemtype instanceof JCTree.JCPrimitiveTypeTree;
                boolean useDeepHC = multiDim || !primitiveArray;
                String[] strArr = new String[2];
                strArr[0] = "Arrays";
                strArr[1] = useDeepHC ? "deepHashCode" : IdentityNamingStrategy.HASH_CODE_KEY;
                JCTree.JCExpression hcMethod = JavacHandlerUtil.chainDots(typeNode, "java", "util", strArr);
                statements.append(createResultCalculation(typeNode, maker.Apply(com.sun.tools.javac.util.List.nil(), hcMethod, com.sun.tools.javac.util.List.of(fieldAccessor))));
            } else {
                Name dollarFieldName3 = memberNode.toName((isMethod ? ClassUtils.CGLIB_CLASS_SEPARATOR : PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX) + memberNode.getName());
                statements.append(maker.VarDef(maker.Modifiers(finalFlag), dollarFieldName3, JavacHandlerUtil.genJavaLangTypeRef(typeNode, "Object"), fieldAccessor));
                statements.append(createResultCalculation(typeNode, maker.Parens(maker.Conditional(maker.Binary(Javac.CTC_EQUAL, maker.Ident(dollarFieldName3), maker.Literal(Javac.CTC_BOT, null)), maker.Literal(Integer.valueOf(HandlerUtil.primeForNull())), maker.Apply(com.sun.tools.javac.util.List.nil(), maker.Select(maker.Ident(dollarFieldName3), typeNode.toName(IdentityNamingStrategy.HASH_CODE_KEY)), com.sun.tools.javac.util.List.nil())))));
            }
        }
        statements.append(maker.Return(maker.Ident(resultName)));
        JCTree.JCBlock body = maker.Block(0L, statements.toList());
        return JavacHandlerUtil.recursiveSetGeneratedBy(maker.MethodDef(mods, typeNode.toName(IdentityNamingStrategy.HASH_CODE_KEY), jCPrimitiveTypeTreeTypeIdent, com.sun.tools.javac.util.List.nil(), com.sun.tools.javac.util.List.nil(), com.sun.tools.javac.util.List.nil(), body, null), source, typeNode.getContext());
    }

    /* renamed from: lombok.javac.handlers.HandleEqualsAndHashCode$1, reason: invalid class name */
    /* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/HandleEqualsAndHashCode$1.SCL.lombok */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$javax$lang$model$type$TypeKind = new int[TypeKind.values().length];

        static {
            try {
                $SwitchMap$javax$lang$model$type$TypeKind[TypeKind.BOOLEAN.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$javax$lang$model$type$TypeKind[TypeKind.LONG.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$javax$lang$model$type$TypeKind[TypeKind.FLOAT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$javax$lang$model$type$TypeKind[TypeKind.DOUBLE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$javax$lang$model$type$TypeKind[TypeKind.BYTE.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$javax$lang$model$type$TypeKind[TypeKind.SHORT.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$javax$lang$model$type$TypeKind[TypeKind.INT.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$javax$lang$model$type$TypeKind[TypeKind.CHAR.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            $SwitchMap$lombok$javac$handlers$JavacHandlerUtil$MemberExistsResult = new int[JavacHandlerUtil.MemberExistsResult.values().length];
            try {
                $SwitchMap$lombok$javac$handlers$JavacHandlerUtil$MemberExistsResult[JavacHandlerUtil.MemberExistsResult.EXISTS_BY_LOMBOK.ordinal()] = 1;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$lombok$javac$handlers$JavacHandlerUtil$MemberExistsResult[JavacHandlerUtil.MemberExistsResult.EXISTS_BY_USER.ordinal()] = 2;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$lombok$javac$handlers$JavacHandlerUtil$MemberExistsResult[JavacHandlerUtil.MemberExistsResult.NOT_EXISTS.ordinal()] = 3;
            } catch (NoSuchFieldError e11) {
            }
            $SwitchMap$lombok$core$configuration$CallSuperType = new int[CallSuperType.valuesCustom().length];
            try {
                $SwitchMap$lombok$core$configuration$CallSuperType[CallSuperType.WARN.ordinal()] = 1;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$lombok$core$configuration$CallSuperType[CallSuperType.SKIP.ordinal()] = 2;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$lombok$core$configuration$CallSuperType[CallSuperType.CALL.ordinal()] = 3;
            } catch (NoSuchFieldError e14) {
            }
        }
    }

    public JCTree.JCExpressionStatement createResultCalculation(JavacNode typeNode, JCTree.JCExpression expr) {
        JavacTreeMaker maker = typeNode.getTreeMaker();
        Name resultName = typeNode.toName("result");
        return maker.Exec(maker.Assign(maker.Ident(resultName), maker.Binary(Javac.CTC_PLUS, maker.Binary(Javac.CTC_MUL, maker.Ident(resultName), maker.Ident(typeNode.toName(PRIME_NAME))), expr)));
    }

    public JCTree.JCExpression longToIntForHashCode(JavacTreeMaker maker, JCTree.JCExpression ref1, JCTree.JCExpression ref2) {
        return maker.TypeCast(maker.TypeIdent(Javac.CTC_INT), maker.Parens(maker.Binary(Javac.CTC_BITXOR, maker.Binary(Javac.CTC_UNSIGNED_SHIFT_RIGHT, ref1, maker.Literal(32)), ref2)));
    }

    public JCTree.JCExpression createTypeReference(JavacNode type, boolean addWildcards) {
        List<String> list = new ArrayList<>();
        List<Integer> genericsCount = addWildcards ? new ArrayList<>() : null;
        list.add(type.getName());
        if (addWildcards) {
            genericsCount.add(Integer.valueOf(type.get().typarams.size()));
        }
        boolean staticContext = (type.get().getModifiers().flags & 8) != 0;
        JavacNode javacNodeUp = type.up();
        while (true) {
            JavacNode tNode = javacNodeUp;
            if (tNode == null || tNode.getKind() != AST.Kind.TYPE) {
                break;
            }
            list.add(tNode.getName());
            if (addWildcards) {
                genericsCount.add(Integer.valueOf(staticContext ? 0 : tNode.get().typarams.size()));
            }
            if (!staticContext) {
                staticContext = (tNode.get().getModifiers().flags & 8) != 0;
            }
            javacNodeUp = tNode.up();
        }
        Collections.reverse(list);
        if (addWildcards) {
            Collections.reverse(genericsCount);
        }
        JavacTreeMaker maker = type.getTreeMaker();
        JCTree.JCExpression chain = maker.Ident(type.toName(list.get(0)));
        if (addWildcards) {
            chain = wildcardify(maker, chain, genericsCount.get(0).intValue());
        }
        for (int i = 1; i < list.size(); i++) {
            chain = maker.Select(chain, type.toName(list.get(i)));
            if (addWildcards) {
                chain = wildcardify(maker, chain, genericsCount.get(i).intValue());
            }
        }
        return chain;
    }

    private JCTree.JCExpression wildcardify(JavacTreeMaker maker, JCTree.JCExpression expr, int count) {
        if (count == 0) {
            return expr;
        }
        ListBuffer<JCTree.JCExpression> wildcards = new ListBuffer<>();
        for (int i = 0; i < count; i++) {
            wildcards.append(maker.Wildcard(maker.TypeBoundKind(BoundKind.UNBOUND), null));
        }
        return maker.TypeApply(expr, wildcards.toList());
    }

    public JCTree.JCMethodDecl createEquals(JavacNode typeNode, List<InclusionExclusionUtils.Included<JavacNode, EqualsAndHashCode.Include>> members, boolean callSuper, HandlerUtil.FieldAccess fieldAccess, boolean needsCanEqual, JCTree source, com.sun.tools.javac.util.List<JCTree.JCAnnotation> onParam) {
        JavacTreeMaker maker = typeNode.getTreeMaker();
        Name oName = typeNode.toName("o");
        Name otherName = typeNode.toName("other");
        Name thisName = typeNode.toName(OgnlContext.THIS_CONTEXT_KEY);
        JCTree.JCAnnotation overrideAnnotation = maker.Annotation(JavacHandlerUtil.genJavaLangTypeRef(typeNode, "Override"), com.sun.tools.javac.util.List.nil());
        JCTree.JCModifiers mods = maker.Modifiers(1L, com.sun.tools.javac.util.List.of(overrideAnnotation));
        JCTree.JCExpression objectType = JavacHandlerUtil.genJavaLangTypeRef(typeNode, "Object");
        JCTree.JCPrimitiveTypeTree jCPrimitiveTypeTreeTypeIdent = maker.TypeIdent(Javac.CTC_BOOLEAN);
        long finalFlag = JavacHandlerUtil.addFinalIfNeeded(0L, typeNode.getContext());
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
        com.sun.tools.javac.util.List<JCTree.JCVariableDecl> params = com.sun.tools.javac.util.List.of(maker.VarDef(maker.Modifiers(finalFlag | 8589934592L, onParam), oName, objectType, null));
        statements.append(maker.If(maker.Binary(Javac.CTC_EQUAL, maker.Ident(oName), maker.Ident(thisName)), returnBool(maker, true), null));
        JCTree.JCUnary notInstanceOf = maker.Unary(Javac.CTC_NOT, maker.Parens(maker.TypeTest(maker.Ident(oName), createTypeReference(typeNode, false))));
        statements.append(maker.If(notInstanceOf, returnBool(maker, false), null));
        if (!members.isEmpty() || needsCanEqual) {
            JCTree.JCExpression selfType1 = createTypeReference(typeNode, true);
            JCTree.JCExpression selfType2 = createTypeReference(typeNode, true);
            statements.append(maker.VarDef(maker.Modifiers(finalFlag), otherName, selfType1, maker.TypeCast(selfType2, maker.Ident(oName))));
        }
        if (needsCanEqual) {
            com.sun.tools.javac.util.List<JCTree.JCExpression> exprNil = com.sun.tools.javac.util.List.nil();
            statements.append(maker.If(maker.Unary(Javac.CTC_NOT, maker.Apply(exprNil, maker.Select(maker.Ident(otherName), typeNode.toName("canEqual")), com.sun.tools.javac.util.List.of(maker.TypeCast(JavacHandlerUtil.genJavaLangTypeRef(typeNode, "Object"), maker.Ident(thisName))))), returnBool(maker, false), null));
        }
        if (callSuper) {
            JCTree.JCMethodInvocation callToSuper = maker.Apply(com.sun.tools.javac.util.List.nil(), maker.Select(maker.Ident(typeNode.toName("super")), typeNode.toName("equals")), com.sun.tools.javac.util.List.of(maker.Ident(oName)));
            JCTree.JCUnary superNotEqual = maker.Unary(Javac.CTC_NOT, callToSuper);
            statements.append(maker.If(superNotEqual, returnBool(maker, false), null));
        }
        for (InclusionExclusionUtils.Included<JavacNode, EqualsAndHashCode.Include> member : members) {
            JavacNode memberNode = member.getNode();
            boolean isMethod = memberNode.getKind() == AST.Kind.METHOD;
            JCTree.JCArrayTypeTree fieldType = JavacHandlerUtil.getFieldType(memberNode, fieldAccess);
            JCTree.JCExpression thisFieldAccessor = isMethod ? JavacHandlerUtil.createMethodAccessor(maker, memberNode) : JavacHandlerUtil.createFieldAccessor(maker, memberNode, fieldAccess);
            JCTree.JCExpression otherFieldAccessor = isMethod ? JavacHandlerUtil.createMethodAccessor(maker, memberNode, maker.Ident(otherName)) : JavacHandlerUtil.createFieldAccessor(maker, memberNode, fieldAccess, maker.Ident(otherName));
            if (fieldType instanceof JCTree.JCPrimitiveTypeTree) {
                switch (AnonymousClass1.$SwitchMap$javax$lang$model$type$TypeKind[((JCTree.JCPrimitiveTypeTree) fieldType).getPrimitiveTypeKind().ordinal()]) {
                    case 3:
                        statements.append(generateCompareFloatOrDouble(thisFieldAccessor, otherFieldAccessor, maker, typeNode, false));
                        break;
                    case 4:
                        statements.append(generateCompareFloatOrDouble(thisFieldAccessor, otherFieldAccessor, maker, typeNode, true));
                        break;
                    default:
                        statements.append(maker.If(maker.Binary(Javac.CTC_NOT_EQUAL, thisFieldAccessor, otherFieldAccessor), returnBool(maker, false), null));
                        break;
                }
            } else if (fieldType instanceof JCTree.JCArrayTypeTree) {
                boolean multiDim = fieldType.elemtype instanceof JCTree.JCArrayTypeTree;
                boolean primitiveArray = fieldType.elemtype instanceof JCTree.JCPrimitiveTypeTree;
                boolean useDeepEquals = multiDim || !primitiveArray;
                String[] strArr = new String[2];
                strArr[0] = "Arrays";
                strArr[1] = useDeepEquals ? "deepEquals" : "equals";
                JCTree.JCExpression eqMethod = JavacHandlerUtil.chainDots(typeNode, "java", "util", strArr);
                com.sun.tools.javac.util.List<JCTree.JCExpression> args = com.sun.tools.javac.util.List.of(thisFieldAccessor, otherFieldAccessor);
                statements.append(maker.If(maker.Unary(Javac.CTC_NOT, maker.Apply(com.sun.tools.javac.util.List.nil(), eqMethod, args)), returnBool(maker, false), null));
            } else {
                Name thisDollarFieldName = memberNode.toName(OgnlContext.THIS_CONTEXT_KEY + (isMethod ? ClassUtils.CGLIB_CLASS_SEPARATOR : PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX) + memberNode.getName());
                Name otherDollarFieldName = memberNode.toName("other" + (isMethod ? ClassUtils.CGLIB_CLASS_SEPARATOR : PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX) + memberNode.getName());
                statements.append(maker.VarDef(maker.Modifiers(finalFlag), thisDollarFieldName, JavacHandlerUtil.genJavaLangTypeRef(typeNode, "Object"), thisFieldAccessor));
                statements.append(maker.VarDef(maker.Modifiers(finalFlag), otherDollarFieldName, JavacHandlerUtil.genJavaLangTypeRef(typeNode, "Object"), otherFieldAccessor));
                statements.append(maker.If(maker.Conditional(maker.Binary(Javac.CTC_EQUAL, maker.Ident(thisDollarFieldName), maker.Literal(Javac.CTC_BOT, null)), maker.Binary(Javac.CTC_NOT_EQUAL, maker.Ident(otherDollarFieldName), maker.Literal(Javac.CTC_BOT, null)), maker.Unary(Javac.CTC_NOT, maker.Apply(com.sun.tools.javac.util.List.nil(), maker.Select(maker.Ident(thisDollarFieldName), typeNode.toName("equals")), com.sun.tools.javac.util.List.of(maker.Ident(otherDollarFieldName))))), returnBool(maker, false), null));
            }
        }
        statements.append(returnBool(maker, true));
        JCTree.JCBlock body = maker.Block(0L, statements.toList());
        return JavacHandlerUtil.recursiveSetGeneratedBy(maker.MethodDef(mods, typeNode.toName("equals"), jCPrimitiveTypeTreeTypeIdent, com.sun.tools.javac.util.List.nil(), params, com.sun.tools.javac.util.List.nil(), body, null), source, typeNode.getContext());
    }

    public JCTree.JCMethodDecl createCanEqual(JavacNode typeNode, JCTree source, com.sun.tools.javac.util.List<JCTree.JCAnnotation> onParam) {
        JavacTreeMaker maker = typeNode.getTreeMaker();
        JCTree.JCModifiers mods = maker.Modifiers(4L, com.sun.tools.javac.util.List.nil());
        JCTree.JCPrimitiveTypeTree jCPrimitiveTypeTreeTypeIdent = maker.TypeIdent(Javac.CTC_BOOLEAN);
        Name canEqualName = typeNode.toName("canEqual");
        JCTree.JCExpression objectType = JavacHandlerUtil.genJavaLangTypeRef(typeNode, "Object");
        Name otherName = typeNode.toName("other");
        long flags = JavacHandlerUtil.addFinalIfNeeded(8589934592L, typeNode.getContext());
        com.sun.tools.javac.util.List<JCTree.JCVariableDecl> params = com.sun.tools.javac.util.List.of(maker.VarDef(maker.Modifiers(flags, onParam), otherName, objectType, null));
        JCTree.JCBlock body = maker.Block(0L, com.sun.tools.javac.util.List.of(maker.Return(maker.TypeTest(maker.Ident(otherName), createTypeReference(typeNode, false)))));
        return JavacHandlerUtil.recursiveSetGeneratedBy(maker.MethodDef(mods, canEqualName, jCPrimitiveTypeTreeTypeIdent, com.sun.tools.javac.util.List.nil(), params, com.sun.tools.javac.util.List.nil(), body, null), source, typeNode.getContext());
    }

    public JCTree.JCStatement generateCompareFloatOrDouble(JCTree.JCExpression thisDotField, JCTree.JCExpression otherDotField, JavacTreeMaker maker, JavacNode node, boolean isDouble) {
        String[] strArr = new String[1];
        strArr[0] = isDouble ? "Double" : "Float";
        JCTree.JCExpression clazz = JavacHandlerUtil.genJavaLangTypeRef(node, strArr);
        com.sun.tools.javac.util.List<JCTree.JCExpression> args = com.sun.tools.javac.util.List.of(thisDotField, otherDotField);
        JCTree.JCBinary compareCallEquals0 = maker.Binary(Javac.CTC_NOT_EQUAL, maker.Apply(com.sun.tools.javac.util.List.nil(), maker.Select(clazz, node.toName("compare")), args), maker.Literal(0));
        return maker.If(compareCallEquals0, returnBool(maker, false), null);
    }

    public JCTree.JCStatement returnBool(JavacTreeMaker maker, boolean bool) {
        return maker.Return(maker.Literal(Javac.CTC_BOOLEAN, Integer.valueOf(bool ? 1 : 0)));
    }
}
