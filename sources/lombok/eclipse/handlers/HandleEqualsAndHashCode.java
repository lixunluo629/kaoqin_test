package lombok.eclipse.handlers;

import ch.qos.logback.core.pattern.color.ANSIConstants;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.ConfigurationKeys;
import lombok.EqualsAndHashCode;
import lombok.core.AST;
import lombok.core.AnnotationValues;
import lombok.core.configuration.CallSuperType;
import lombok.core.handlers.HandlerUtil;
import lombok.core.handlers.InclusionExclusionUtils;
import lombok.eclipse.Eclipse;
import lombok.eclipse.EclipseAnnotationHandler;
import lombok.eclipse.EclipseNode;
import lombok.eclipse.handlers.EclipseHandlerUtil;
import org.apache.ibatis.ognl.OgnlContext;
import org.apache.xmlbeans.XmlErrorCodes;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.Annotation;
import org.eclipse.jdt.internal.compiler.ast.Argument;
import org.eclipse.jdt.internal.compiler.ast.Assignment;
import org.eclipse.jdt.internal.compiler.ast.BinaryExpression;
import org.eclipse.jdt.internal.compiler.ast.CastExpression;
import org.eclipse.jdt.internal.compiler.ast.ConditionalExpression;
import org.eclipse.jdt.internal.compiler.ast.EqualExpression;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.FalseLiteral;
import org.eclipse.jdt.internal.compiler.ast.IfStatement;
import org.eclipse.jdt.internal.compiler.ast.InstanceOfExpression;
import org.eclipse.jdt.internal.compiler.ast.IntLiteral;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.NameReference;
import org.eclipse.jdt.internal.compiler.ast.NullLiteral;
import org.eclipse.jdt.internal.compiler.ast.ParameterizedQualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ParameterizedSingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedNameReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.SingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.SuperReference;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.eclipse.jdt.internal.compiler.ast.TrueLiteral;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.jdt.internal.compiler.ast.UnaryExpression;
import org.eclipse.jdt.internal.compiler.ast.Wildcard;
import org.eclipse.jdt.internal.compiler.lookup.TypeConstants;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.cache.interceptor.CacheOperationExpressionEvaluator;
import org.springframework.jmx.export.naming.IdentityNamingStrategy;
import org.springframework.util.ClassUtils;

/* loaded from: lombok-1.16.22.jar:lombok/eclipse/handlers/HandleEqualsAndHashCode.SCL.lombok */
public class HandleEqualsAndHashCode extends EclipseAnnotationHandler<EqualsAndHashCode> {
    private final char[] PRIME = "PRIME".toCharArray();
    private final char[] RESULT = CacheOperationExpressionEvaluator.RESULT_VARIABLE.toCharArray();
    public static final Set<String> BUILT_IN_TYPES = Collections.unmodifiableSet(new HashSet(Arrays.asList("byte", "short", XmlErrorCodes.INT, XmlErrorCodes.LONG, "char", "boolean", XmlErrorCodes.DOUBLE, XmlErrorCodes.FLOAT)));
    private static /* synthetic */ int[] $SWITCH_TABLE$lombok$core$configuration$CallSuperType;
    private static /* synthetic */ int[] $SWITCH_TABLE$lombok$eclipse$handlers$EclipseHandlerUtil$MemberExistsResult;

    static /* synthetic */ int[] $SWITCH_TABLE$lombok$core$configuration$CallSuperType() {
        int[] iArr = $SWITCH_TABLE$lombok$core$configuration$CallSuperType;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[CallSuperType.valuesCustom().length];
        try {
            iArr2[CallSuperType.CALL.ordinal()] = 1;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[CallSuperType.SKIP.ordinal()] = 2;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[CallSuperType.WARN.ordinal()] = 3;
        } catch (NoSuchFieldError unused3) {
        }
        $SWITCH_TABLE$lombok$core$configuration$CallSuperType = iArr2;
        return iArr2;
    }

    static /* synthetic */ int[] $SWITCH_TABLE$lombok$eclipse$handlers$EclipseHandlerUtil$MemberExistsResult() {
        int[] iArr = $SWITCH_TABLE$lombok$eclipse$handlers$EclipseHandlerUtil$MemberExistsResult;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[EclipseHandlerUtil.MemberExistsResult.valuesCustom().length];
        try {
            iArr2[EclipseHandlerUtil.MemberExistsResult.EXISTS_BY_LOMBOK.ordinal()] = 2;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[EclipseHandlerUtil.MemberExistsResult.EXISTS_BY_USER.ordinal()] = 3;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[EclipseHandlerUtil.MemberExistsResult.NOT_EXISTS.ordinal()] = 1;
        } catch (NoSuchFieldError unused3) {
        }
        $SWITCH_TABLE$lombok$eclipse$handlers$EclipseHandlerUtil$MemberExistsResult = iArr2;
        return iArr2;
    }

    @Override // lombok.eclipse.EclipseAnnotationHandler
    public void handle(AnnotationValues<EqualsAndHashCode> annotation, Annotation ast, EclipseNode annotationNode) throws IllegalAccessException, InstantiationException, SecurityException, IllegalArgumentException, InvocationTargetException {
        HandlerUtil.handleFlagUsage(annotationNode, ConfigurationKeys.EQUALS_AND_HASH_CODE_FLAG_USAGE, "@EqualsAndHashCode");
        EqualsAndHashCode ann = (EqualsAndHashCode) annotation.getInstance();
        List<InclusionExclusionUtils.Included<EclipseNode, EqualsAndHashCode.Include>> members = InclusionExclusionUtils.handleEqualsAndHashCodeMarking(annotationNode.up(), annotation, annotationNode);
        if (members == null) {
            return;
        }
        List<Annotation> onParam = EclipseHandlerUtil.unboxAndRemoveAnnotationParameter(ast, "onParam", "@EqualsAndHashCode(onParam", annotationNode);
        Boolean callSuper = Boolean.valueOf(ann.callSuper());
        if (!annotation.isExplicit("callSuper")) {
            callSuper = null;
        }
        Boolean doNotUseGettersConfiguration = (Boolean) annotationNode.getAst().readConfiguration(ConfigurationKeys.EQUALS_AND_HASH_CODE_DO_NOT_USE_GETTERS);
        boolean doNotUseGetters = (annotation.isExplicit("doNotUseGetters") || doNotUseGettersConfiguration == null) ? ann.doNotUseGetters() : doNotUseGettersConfiguration.booleanValue();
        HandlerUtil.FieldAccess fieldAccess = doNotUseGetters ? HandlerUtil.FieldAccess.PREFER_FIELD : HandlerUtil.FieldAccess.GETTER;
        generateMethods(annotationNode.up(), annotationNode, members, callSuper, true, fieldAccess, onParam);
    }

    public void generateEqualsAndHashCodeForType(EclipseNode typeNode, EclipseNode errorNode) throws IllegalAccessException, InstantiationException, SecurityException, IllegalArgumentException, InvocationTargetException {
        if (EclipseHandlerUtil.hasAnnotation((Class<? extends java.lang.annotation.Annotation>) EqualsAndHashCode.class, typeNode)) {
            return;
        }
        List<InclusionExclusionUtils.Included<EclipseNode, EqualsAndHashCode.Include>> members = InclusionExclusionUtils.handleEqualsAndHashCodeMarking(typeNode, null, null);
        Boolean doNotUseGettersConfiguration = (Boolean) typeNode.getAst().readConfiguration(ConfigurationKeys.EQUALS_AND_HASH_CODE_DO_NOT_USE_GETTERS);
        HandlerUtil.FieldAccess access = (doNotUseGettersConfiguration == null || !doNotUseGettersConfiguration.booleanValue()) ? HandlerUtil.FieldAccess.GETTER : HandlerUtil.FieldAccess.PREFER_FIELD;
        generateMethods(typeNode, errorNode, members, null, false, access, new ArrayList());
    }

    public void generateMethods(EclipseNode typeNode, EclipseNode errorNode, List<InclusionExclusionUtils.Included<EclipseNode, EqualsAndHashCode.Include>> members, Boolean callSuper, boolean whineIfExists, HandlerUtil.FieldAccess fieldAccess, List<Annotation> onParam) throws IllegalAccessException, InstantiationException, SecurityException, IllegalArgumentException, InvocationTargetException {
        TypeDeclaration typeDecl = null;
        if (typeNode.get() instanceof TypeDeclaration) {
            typeDecl = (TypeDeclaration) typeNode.get();
        }
        int modifiers = typeDecl == null ? 0 : typeDecl.modifiers;
        boolean notAClass = (modifiers & 25088) != 0;
        if (typeDecl == null || notAClass) {
            errorNode.addError("@EqualsAndHashCode is only supported on a class.");
            return;
        }
        boolean implicitCallSuper = callSuper == null;
        if (callSuper == null) {
            try {
                callSuper = Boolean.valueOf(((Boolean) EqualsAndHashCode.class.getMethod("callSuper", new Class[0]).getDefaultValue()).booleanValue());
            } catch (Exception unused) {
                throw new InternalError("Lombok bug - this cannot happen - can't find callSuper field in EqualsAndHashCode annotation.");
            }
        }
        boolean isDirectDescendantOfObject = true;
        if (typeDecl.superclass != null) {
            String p = typeDecl.superclass.toString();
            isDirectDescendantOfObject = p.equals("Object") || p.equals("java.lang.Object");
        }
        if (isDirectDescendantOfObject && callSuper.booleanValue()) {
            errorNode.addError("Generating equals/hashCode with a supercall to java.lang.Object is pointless.");
            return;
        }
        if (implicitCallSuper && !isDirectDescendantOfObject) {
            CallSuperType cst = (CallSuperType) typeNode.getAst().readConfiguration(ConfigurationKeys.EQUALS_AND_HASH_CODE_CALL_SUPER);
            if (cst == null) {
                cst = CallSuperType.WARN;
            }
            switch ($SWITCH_TABLE$lombok$core$configuration$CallSuperType()[cst.ordinal()]) {
                case 1:
                    callSuper = true;
                    break;
                case 2:
                    callSuper = false;
                    break;
                case 3:
                default:
                    errorNode.addWarning("Generating equals/hashCode implementation but without a call to superclass, even though this class does not extend java.lang.Object. If this is intentional, add '@EqualsAndHashCode(callSuper=false)' to your type.");
                    callSuper = false;
                    break;
            }
        }
        boolean isFinal = (typeDecl.modifiers & 16) != 0;
        boolean needsCanEqual = (isFinal && isDirectDescendantOfObject) ? false : true;
        EclipseHandlerUtil.MemberExistsResult equalsExists = EclipseHandlerUtil.methodExists("equals", typeNode, 1);
        EclipseHandlerUtil.MemberExistsResult hashCodeExists = EclipseHandlerUtil.methodExists(IdentityNamingStrategy.HASH_CODE_KEY, typeNode, 0);
        EclipseHandlerUtil.MemberExistsResult canEqualExists = EclipseHandlerUtil.methodExists("canEqual", typeNode, 1);
        switch ($SWITCH_TABLE$lombok$eclipse$handlers$EclipseHandlerUtil$MemberExistsResult()[((EclipseHandlerUtil.MemberExistsResult) Collections.max(Arrays.asList(equalsExists, hashCodeExists))).ordinal()]) {
            case 1:
            default:
                MethodDeclaration equalsMethod = createEquals(typeNode, members, callSuper.booleanValue(), errorNode.get(), fieldAccess, needsCanEqual, onParam);
                equalsMethod.traverse(new SetGeneratedByVisitor(errorNode.get()), typeNode.get().scope);
                EclipseHandlerUtil.injectMethod(typeNode, equalsMethod);
                if (needsCanEqual && canEqualExists == EclipseHandlerUtil.MemberExistsResult.NOT_EXISTS) {
                    MethodDeclaration canEqualMethod = createCanEqual(typeNode, errorNode.get(), onParam);
                    canEqualMethod.traverse(new SetGeneratedByVisitor(errorNode.get()), typeNode.get().scope);
                    EclipseHandlerUtil.injectMethod(typeNode, canEqualMethod);
                }
                MethodDeclaration hashCodeMethod = createHashCode(typeNode, members, callSuper.booleanValue(), errorNode.get(), fieldAccess);
                hashCodeMethod.traverse(new SetGeneratedByVisitor(errorNode.get()), typeNode.get().scope);
                EclipseHandlerUtil.injectMethod(typeNode, hashCodeMethod);
                return;
            case 2:
                return;
            case 3:
                if (whineIfExists) {
                    errorNode.addWarning("Not generating equals and hashCode: A method with one of those names already exists. (Either both or none of these methods will be generated).");
                    return;
                }
                if (equalsExists == EclipseHandlerUtil.MemberExistsResult.NOT_EXISTS || hashCodeExists == EclipseHandlerUtil.MemberExistsResult.NOT_EXISTS) {
                    Object[] objArr = new Object[1];
                    objArr[0] = equalsExists == EclipseHandlerUtil.MemberExistsResult.NOT_EXISTS ? "equals" : IdentityNamingStrategy.HASH_CODE_KEY;
                    String msg = String.format("Not generating %s: One of equals or hashCode exists. You should either write both of these or none of these (in the latter case, lombok generates them).", objArr);
                    errorNode.addWarning(msg);
                    return;
                }
                return;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v13, types: [char[], char[][]] */
    public MethodDeclaration createHashCode(EclipseNode type, Collection<InclusionExclusionUtils.Included<EclipseNode, EqualsAndHashCode.Include>> members, boolean callSuper, ASTNode source, HandlerUtil.FieldAccess fieldAccess) throws SecurityException {
        MessageSend messageSendMakeIntLiteral;
        int pS = source.sourceStart;
        int pE = source.sourceEnd;
        long p = (pS << 32) | pE;
        MethodDeclaration method = new MethodDeclaration(type.top().get().compilationResult);
        EclipseHandlerUtil.setGeneratedBy(method, source);
        method.modifiers = EclipseHandlerUtil.toEclipseModifier(AccessLevel.PUBLIC);
        method.returnType = TypeReference.baseTypeReference(10, 0);
        EclipseHandlerUtil.setGeneratedBy(method.returnType, source);
        method.annotations = new Annotation[]{EclipseHandlerUtil.makeMarkerAnnotation(TypeConstants.JAVA_LANG_OVERRIDE, source)};
        method.selector = IdentityNamingStrategy.HASH_CODE_KEY.toCharArray();
        method.thrownExceptions = null;
        method.typeParameters = null;
        method.bits |= 8388608;
        int i = source.sourceStart;
        method.sourceStart = i;
        method.declarationSourceStart = i;
        method.bodyStart = i;
        int i2 = source.sourceEnd;
        method.sourceEnd = i2;
        method.declarationSourceEnd = i2;
        method.bodyEnd = i2;
        method.arguments = null;
        List<Statement> statements = new ArrayList<>();
        boolean isEmpty = members.isEmpty();
        if (!isEmpty) {
            LocalDeclaration primeDecl = new LocalDeclaration(this.PRIME, pS, pE);
            EclipseHandlerUtil.setGeneratedBy(primeDecl, source);
            primeDecl.modifiers |= 16;
            primeDecl.type = TypeReference.baseTypeReference(10, 0);
            primeDecl.type.sourceStart = pS;
            primeDecl.type.sourceEnd = pE;
            EclipseHandlerUtil.setGeneratedBy(primeDecl.type, source);
            primeDecl.initialization = EclipseHandlerUtil.makeIntLiteral(String.valueOf(HandlerUtil.primeForHashcode()).toCharArray(), source);
            statements.add(primeDecl);
        }
        LocalDeclaration resultDecl = new LocalDeclaration(this.RESULT, pS, pE);
        EclipseHandlerUtil.setGeneratedBy(resultDecl, source);
        if (callSuper) {
            MessageSend callToSuper = new MessageSend();
            EclipseHandlerUtil.setGeneratedBy(callToSuper, source);
            callToSuper.sourceStart = pS;
            callToSuper.sourceEnd = pE;
            callToSuper.receiver = new SuperReference(pS, pE);
            EclipseHandlerUtil.setGeneratedBy(callToSuper.receiver, source);
            callToSuper.selector = IdentityNamingStrategy.HASH_CODE_KEY.toCharArray();
            messageSendMakeIntLiteral = callToSuper;
        } else {
            messageSendMakeIntLiteral = EclipseHandlerUtil.makeIntLiteral("1".toCharArray(), source);
        }
        resultDecl.initialization = messageSendMakeIntLiteral;
        resultDecl.type = TypeReference.baseTypeReference(10, 0);
        resultDecl.type.sourceStart = pS;
        resultDecl.type.sourceEnd = pE;
        EclipseHandlerUtil.setGeneratedBy(resultDecl.type, source);
        statements.add(resultDecl);
        for (InclusionExclusionUtils.Included<EclipseNode, EqualsAndHashCode.Include> member : members) {
            EclipseNode memberNode = member.getNode();
            boolean isMethod = memberNode.getKind() == AST.Kind.METHOD;
            TypeReference fType = EclipseHandlerUtil.getFieldType(memberNode, fieldAccess);
            char[] dollarFieldName = (String.valueOf(isMethod ? ClassUtils.CGLIB_CLASS_SEPARATOR : PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX) + memberNode.getName()).toCharArray();
            char[] token = fType.getLastToken();
            Expression fieldAccessor = isMethod ? EclipseHandlerUtil.createMethodAccessor(memberNode, source) : EclipseHandlerUtil.createFieldAccessor(memberNode, fieldAccess, source);
            if (fType.dimensions() != 0 || token == null) {
                if (fType.dimensions() > 0 && token != null) {
                    MessageSend arraysHashCodeCall = new MessageSend();
                    arraysHashCodeCall.sourceStart = pS;
                    arraysHashCodeCall.sourceEnd = pE;
                    EclipseHandlerUtil.setGeneratedBy(arraysHashCodeCall, source);
                    arraysHashCodeCall.receiver = generateQualifiedNameRef(source, new char[]{TypeConstants.JAVA, TypeConstants.UTIL, "Arrays".toCharArray()});
                    if (fType.dimensions() > 1 || !BUILT_IN_TYPES.contains(new String(token))) {
                        arraysHashCodeCall.selector = "deepHashCode".toCharArray();
                    } else {
                        arraysHashCodeCall.selector = IdentityNamingStrategy.HASH_CODE_KEY.toCharArray();
                    }
                    arraysHashCodeCall.arguments = new Expression[]{fieldAccessor};
                    statements.add(createResultCalculation(source, arraysHashCodeCall));
                }
            } else if (Arrays.equals(TypeConstants.BOOLEAN, token)) {
                IntLiteral intTrue = EclipseHandlerUtil.makeIntLiteral(String.valueOf(HandlerUtil.primeForTrue()).toCharArray(), source);
                IntLiteral intFalse = EclipseHandlerUtil.makeIntLiteral(String.valueOf(HandlerUtil.primeForFalse()).toCharArray(), source);
                ConditionalExpression intForBool = new ConditionalExpression(fieldAccessor, intTrue, intFalse);
                EclipseHandlerUtil.setGeneratedBy(intForBool, source);
                statements.add(createResultCalculation(source, intForBool));
            } else if (Arrays.equals(TypeConstants.LONG, token)) {
                statements.add(createLocalDeclaration(source, dollarFieldName, TypeReference.baseTypeReference(7, 0), fieldAccessor));
                SingleNameReference copy1 = new SingleNameReference(dollarFieldName, p);
                EclipseHandlerUtil.setGeneratedBy(copy1, source);
                SingleNameReference copy2 = new SingleNameReference(dollarFieldName, p);
                EclipseHandlerUtil.setGeneratedBy(copy2, source);
                statements.add(createResultCalculation(source, longToIntForHashCode(copy1, copy2, source)));
            } else if (Arrays.equals(TypeConstants.FLOAT, token)) {
                MessageSend floatToIntBits = new MessageSend();
                floatToIntBits.sourceStart = pS;
                floatToIntBits.sourceEnd = pE;
                EclipseHandlerUtil.setGeneratedBy(floatToIntBits, source);
                floatToIntBits.receiver = generateQualifiedNameRef(source, TypeConstants.JAVA_LANG_FLOAT);
                floatToIntBits.selector = "floatToIntBits".toCharArray();
                floatToIntBits.arguments = new Expression[]{fieldAccessor};
                statements.add(createResultCalculation(source, floatToIntBits));
            } else if (Arrays.equals(TypeConstants.DOUBLE, token)) {
                MessageSend doubleToLongBits = new MessageSend();
                doubleToLongBits.sourceStart = pS;
                doubleToLongBits.sourceEnd = pE;
                EclipseHandlerUtil.setGeneratedBy(doubleToLongBits, source);
                doubleToLongBits.receiver = generateQualifiedNameRef(source, TypeConstants.JAVA_LANG_DOUBLE);
                doubleToLongBits.selector = "doubleToLongBits".toCharArray();
                doubleToLongBits.arguments = new Expression[]{fieldAccessor};
                statements.add(createLocalDeclaration(source, dollarFieldName, TypeReference.baseTypeReference(7, 0), doubleToLongBits));
                SingleNameReference copy12 = new SingleNameReference(dollarFieldName, p);
                EclipseHandlerUtil.setGeneratedBy(copy12, source);
                SingleNameReference copy22 = new SingleNameReference(dollarFieldName, p);
                EclipseHandlerUtil.setGeneratedBy(copy22, source);
                statements.add(createResultCalculation(source, longToIntForHashCode(copy12, copy22, source)));
            } else if (BUILT_IN_TYPES.contains(new String(token))) {
                statements.add(createResultCalculation(source, fieldAccessor));
            } else {
                statements.add(createLocalDeclaration(source, dollarFieldName, generateQualifiedTypeRef(source, TypeConstants.JAVA_LANG_OBJECT), fieldAccessor));
                SingleNameReference copy13 = new SingleNameReference(dollarFieldName, p);
                EclipseHandlerUtil.setGeneratedBy(copy13, source);
                SingleNameReference copy23 = new SingleNameReference(dollarFieldName, p);
                EclipseHandlerUtil.setGeneratedBy(copy23, source);
                MessageSend hashCodeCall = new MessageSend();
                hashCodeCall.sourceStart = pS;
                hashCodeCall.sourceEnd = pE;
                EclipseHandlerUtil.setGeneratedBy(hashCodeCall, source);
                hashCodeCall.receiver = copy13;
                hashCodeCall.selector = IdentityNamingStrategy.HASH_CODE_KEY.toCharArray();
                NullLiteral nullLiteral = new NullLiteral(pS, pE);
                EclipseHandlerUtil.setGeneratedBy(nullLiteral, source);
                EqualExpression objIsNull = new EqualExpression(copy23, nullLiteral, 18);
                EclipseHandlerUtil.setGeneratedBy(objIsNull, source);
                IntLiteral intMagic = EclipseHandlerUtil.makeIntLiteral(String.valueOf(HandlerUtil.primeForNull()).toCharArray(), source);
                ConditionalExpression nullOrHashCode = new ConditionalExpression(objIsNull, intMagic, hashCodeCall);
                nullOrHashCode.sourceStart = pS;
                nullOrHashCode.sourceEnd = pE;
                EclipseHandlerUtil.setGeneratedBy(nullOrHashCode, source);
                statements.add(createResultCalculation(source, nullOrHashCode));
            }
        }
        SingleNameReference resultRef = new SingleNameReference(this.RESULT, p);
        EclipseHandlerUtil.setGeneratedBy(resultRef, source);
        ReturnStatement returnStatement = new ReturnStatement(resultRef, pS, pE);
        EclipseHandlerUtil.setGeneratedBy(returnStatement, source);
        statements.add(returnStatement);
        method.statements = (Statement[]) statements.toArray(new Statement[statements.size()]);
        return method;
    }

    public LocalDeclaration createLocalDeclaration(ASTNode source, char[] dollarFieldName, TypeReference type, Expression initializer) {
        int pS = source.sourceStart;
        int pE = source.sourceEnd;
        LocalDeclaration tempVar = new LocalDeclaration(dollarFieldName, pS, pE);
        EclipseHandlerUtil.setGeneratedBy(tempVar, source);
        tempVar.initialization = initializer;
        tempVar.type = type;
        tempVar.type.sourceStart = pS;
        tempVar.type.sourceEnd = pE;
        EclipseHandlerUtil.setGeneratedBy(tempVar.type, source);
        tempVar.modifiers = 16;
        return tempVar;
    }

    public Expression createResultCalculation(ASTNode source, Expression ex) {
        int pS = source.sourceStart;
        int pE = source.sourceEnd;
        long p = (pS << 32) | pE;
        SingleNameReference resultRef = new SingleNameReference(this.RESULT, p);
        EclipseHandlerUtil.setGeneratedBy(resultRef, source);
        SingleNameReference primeRef = new SingleNameReference(this.PRIME, p);
        EclipseHandlerUtil.setGeneratedBy(primeRef, source);
        BinaryExpression multiplyByPrime = new BinaryExpression(resultRef, primeRef, 15);
        multiplyByPrime.sourceStart = pS;
        multiplyByPrime.sourceEnd = pE;
        EclipseHandlerUtil.setGeneratedBy(multiplyByPrime, source);
        BinaryExpression addItem = new BinaryExpression(multiplyByPrime, ex, 14);
        addItem.sourceStart = pS;
        addItem.sourceEnd = pE;
        EclipseHandlerUtil.setGeneratedBy(addItem, source);
        SingleNameReference resultRef2 = new SingleNameReference(this.RESULT, p);
        EclipseHandlerUtil.setGeneratedBy(resultRef2, source);
        Assignment assignment = new Assignment(resultRef2, addItem, pE);
        assignment.sourceStart = pS;
        assignment.statementEnd = pE;
        assignment.sourceEnd = pE;
        EclipseHandlerUtil.setGeneratedBy(assignment, source);
        return assignment;
    }

    /* JADX WARN: Type inference failed for: r0v33, types: [char[], char[][]] */
    /* JADX WARN: Type inference failed for: r0v39, types: [org.eclipse.jdt.internal.compiler.ast.TypeReference[], org.eclipse.jdt.internal.compiler.ast.TypeReference[][]] */
    public TypeReference createTypeReference(EclipseNode type, long p, ASTNode source, boolean addWildcards) {
        int pS = source.sourceStart;
        int pE = source.sourceEnd;
        List<String> list = new ArrayList<>();
        List<Integer> genericsCount = addWildcards ? new ArrayList<>() : null;
        list.add(type.getName());
        if (addWildcards) {
            genericsCount.add(Integer.valueOf(arraySizeOf(type.get().typeParameters)));
        }
        boolean staticContext = (type.get().modifiers & 8) != 0;
        EclipseNode tNode = type.up();
        if (!staticContext && tNode.getKind() == AST.Kind.TYPE && (tNode.get().modifiers & 512) != 0) {
            staticContext = true;
        }
        while (tNode != null && tNode.getKind() == AST.Kind.TYPE) {
            list.add(tNode.getName());
            if (addWildcards) {
                genericsCount.add(Integer.valueOf(staticContext ? 0 : arraySizeOf(tNode.get().typeParameters)));
            }
            if (!staticContext) {
                staticContext = (tNode.get().modifiers & 8) != 0;
            }
            tNode = tNode.up();
            if (!staticContext && tNode.getKind() == AST.Kind.TYPE && (tNode.get().modifiers & 512) != 0) {
                staticContext = true;
            }
        }
        Collections.reverse(list);
        if (addWildcards) {
            Collections.reverse(genericsCount);
        }
        if (list.size() == 1) {
            if (!addWildcards || genericsCount.get(0).intValue() == 0) {
                return new SingleTypeReference(list.get(0).toCharArray(), p);
            }
            return new ParameterizedSingleTypeReference(list.get(0).toCharArray(), wildcardify(pS, pE, source, genericsCount.get(0).intValue()), 0, p);
        }
        if (addWildcards) {
            addWildcards = false;
            Iterator<Integer> it = genericsCount.iterator();
            while (it.hasNext()) {
                int i = it.next().intValue();
                if (i > 0) {
                    addWildcards = true;
                }
            }
        }
        long[] ps = new long[list.size()];
        ?? r0 = new char[list.size()];
        for (int i2 = 0; i2 < list.size(); i2++) {
            ps[i2] = p;
            r0[i2] = list.get(i2).toCharArray();
        }
        if (!addWildcards) {
            return new QualifiedTypeReference((char[][]) r0, ps);
        }
        ?? r02 = new TypeReference[r0.length];
        for (int i3 = 0; i3 < r0.length; i3++) {
            r02[i3] = wildcardify(pS, pE, source, genericsCount.get(i3).intValue());
        }
        return new ParameterizedQualifiedTypeReference((char[][]) r0, (TypeReference[][]) r02, 0, ps);
    }

    private TypeReference[] wildcardify(int pS, int pE, ASTNode source, int count) {
        if (count == 0) {
            return null;
        }
        ASTNode[] aSTNodeArr = new TypeReference[count];
        for (int i = 0; i < count; i++) {
            aSTNodeArr[i] = new Wildcard(0);
            ((TypeReference) aSTNodeArr[i]).sourceStart = pS;
            ((TypeReference) aSTNodeArr[i]).sourceEnd = pE;
            EclipseHandlerUtil.setGeneratedBy(aSTNodeArr[i], source);
        }
        return aSTNodeArr;
    }

    private int arraySizeOf(Object[] arr) {
        if (arr == null) {
            return 0;
        }
        return arr.length;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v37, types: [char[], char[][]] */
    public MethodDeclaration createEquals(EclipseNode type, Collection<InclusionExclusionUtils.Included<EclipseNode, EqualsAndHashCode.Include>> members, boolean callSuper, ASTNode source, HandlerUtil.FieldAccess fieldAccess, boolean needsCanEqual, List<Annotation> onParam) throws IllegalAccessException, InstantiationException, SecurityException, IllegalArgumentException, InvocationTargetException {
        int pS = source.sourceStart;
        int pE = source.sourceEnd;
        long p = (pS << 32) | pE;
        MethodDeclaration method = new MethodDeclaration(type.top().get().compilationResult);
        EclipseHandlerUtil.setGeneratedBy(method, source);
        method.modifiers = EclipseHandlerUtil.toEclipseModifier(AccessLevel.PUBLIC);
        method.returnType = TypeReference.baseTypeReference(5, 0);
        method.returnType.sourceStart = pS;
        method.returnType.sourceEnd = pE;
        EclipseHandlerUtil.setGeneratedBy(method.returnType, source);
        method.annotations = new Annotation[]{EclipseHandlerUtil.makeMarkerAnnotation(TypeConstants.JAVA_LANG_OVERRIDE, source)};
        method.selector = "equals".toCharArray();
        method.thrownExceptions = null;
        method.typeParameters = null;
        method.bits |= 8388608;
        int i = source.sourceStart;
        method.sourceStart = i;
        method.declarationSourceStart = i;
        method.bodyStart = i;
        int i2 = source.sourceEnd;
        method.sourceEnd = i2;
        method.declarationSourceEnd = i2;
        method.bodyEnd = i2;
        QualifiedTypeReference qualifiedTypeReference = new QualifiedTypeReference(TypeConstants.JAVA_LANG_OBJECT, new long[]{p, p, p});
        EclipseHandlerUtil.setGeneratedBy(qualifiedTypeReference, source);
        method.arguments = new Argument[]{new Argument(new char[]{'o'}, 0L, qualifiedTypeReference, 16)};
        method.arguments[0].sourceStart = pS;
        method.arguments[0].sourceEnd = pE;
        if (!onParam.isEmpty()) {
            method.arguments[0].annotations = (Annotation[]) onParam.toArray(new Annotation[0]);
        }
        EclipseHandlerUtil.setGeneratedBy(method.arguments[0], source);
        List<Statement> statements = new ArrayList<>();
        SingleNameReference oRef = new SingleNameReference(new char[]{'o'}, p);
        EclipseHandlerUtil.setGeneratedBy(oRef, source);
        ThisReference thisRef = new ThisReference(pS, pE);
        EclipseHandlerUtil.setGeneratedBy(thisRef, source);
        EqualExpression otherEqualsThis = new EqualExpression(oRef, thisRef, 18);
        EclipseHandlerUtil.setGeneratedBy(otherEqualsThis, source);
        TrueLiteral trueLiteral = new TrueLiteral(pS, pE);
        EclipseHandlerUtil.setGeneratedBy(trueLiteral, source);
        ReturnStatement returnTrue = new ReturnStatement(trueLiteral, pS, pE);
        EclipseHandlerUtil.setGeneratedBy(returnTrue, source);
        IfStatement ifOtherEqualsThis = new IfStatement(otherEqualsThis, returnTrue, pS, pE);
        EclipseHandlerUtil.setGeneratedBy(ifOtherEqualsThis, source);
        statements.add(ifOtherEqualsThis);
        SingleNameReference oRef2 = new SingleNameReference(new char[]{'o'}, p);
        EclipseHandlerUtil.setGeneratedBy(oRef2, source);
        TypeReference typeReference = createTypeReference(type, p, source, false);
        EclipseHandlerUtil.setGeneratedBy(typeReference, source);
        InstanceOfExpression instanceOf = new InstanceOfExpression(oRef2, typeReference);
        instanceOf.sourceStart = pS;
        instanceOf.sourceEnd = pE;
        EclipseHandlerUtil.setGeneratedBy(instanceOf, source);
        UnaryExpression unaryExpression = new UnaryExpression(instanceOf, 11);
        EclipseHandlerUtil.setGeneratedBy(unaryExpression, source);
        FalseLiteral falseLiteral = new FalseLiteral(pS, pE);
        EclipseHandlerUtil.setGeneratedBy(falseLiteral, source);
        ReturnStatement returnFalse = new ReturnStatement(falseLiteral, pS, pE);
        EclipseHandlerUtil.setGeneratedBy(returnFalse, source);
        IfStatement ifNotInstanceOf = new IfStatement(unaryExpression, returnFalse, pS, pE);
        EclipseHandlerUtil.setGeneratedBy(ifNotInstanceOf, source);
        statements.add(ifNotInstanceOf);
        char[] otherName = "other".toCharArray();
        if (!members.isEmpty() || needsCanEqual) {
            LocalDeclaration other = new LocalDeclaration(otherName, pS, pE);
            other.modifiers |= 16;
            EclipseHandlerUtil.setGeneratedBy(other, source);
            TypeReference targetType = createTypeReference(type, p, source, true);
            EclipseHandlerUtil.setGeneratedBy(targetType, source);
            other.type = createTypeReference(type, p, source, true);
            EclipseHandlerUtil.setGeneratedBy(other.type, source);
            SingleNameReference singleNameReference = new SingleNameReference(new char[]{'o'}, p);
            EclipseHandlerUtil.setGeneratedBy(singleNameReference, source);
            other.initialization = EclipseHandlerUtil.makeCastExpression(singleNameReference, targetType, source);
            statements.add(other);
        }
        if (needsCanEqual) {
            MessageSend otherCanEqual = new MessageSend();
            otherCanEqual.sourceStart = pS;
            otherCanEqual.sourceEnd = pE;
            EclipseHandlerUtil.setGeneratedBy(otherCanEqual, source);
            otherCanEqual.receiver = new SingleNameReference(otherName, p);
            EclipseHandlerUtil.setGeneratedBy(otherCanEqual.receiver, source);
            otherCanEqual.selector = "canEqual".toCharArray();
            ThisReference thisReference = new ThisReference(pS, pE);
            EclipseHandlerUtil.setGeneratedBy(thisReference, source);
            Expression expressionMakeCastExpression = EclipseHandlerUtil.makeCastExpression(thisReference, generateQualifiedTypeRef(source, TypeConstants.JAVA_LANG_OBJECT), source);
            ((CastExpression) expressionMakeCastExpression).sourceStart = pS;
            ((CastExpression) expressionMakeCastExpression).sourceEnd = pE;
            otherCanEqual.arguments = new Expression[]{expressionMakeCastExpression};
            UnaryExpression unaryExpression2 = new UnaryExpression(otherCanEqual, 11);
            EclipseHandlerUtil.setGeneratedBy(unaryExpression2, source);
            FalseLiteral falseLiteral2 = new FalseLiteral(pS, pE);
            EclipseHandlerUtil.setGeneratedBy(falseLiteral2, source);
            ReturnStatement returnFalse2 = new ReturnStatement(falseLiteral2, pS, pE);
            EclipseHandlerUtil.setGeneratedBy(returnFalse2, source);
            IfStatement ifNotCanEqual = new IfStatement(unaryExpression2, returnFalse2, pS, pE);
            EclipseHandlerUtil.setGeneratedBy(ifNotCanEqual, source);
            statements.add(ifNotCanEqual);
        }
        if (callSuper) {
            MessageSend callToSuper = new MessageSend();
            callToSuper.sourceStart = pS;
            callToSuper.sourceEnd = pE;
            EclipseHandlerUtil.setGeneratedBy(callToSuper, source);
            callToSuper.receiver = new SuperReference(pS, pE);
            EclipseHandlerUtil.setGeneratedBy(callToSuper.receiver, source);
            callToSuper.selector = "equals".toCharArray();
            Expression singleNameReference2 = new SingleNameReference(new char[]{'o'}, p);
            EclipseHandlerUtil.setGeneratedBy(singleNameReference2, source);
            callToSuper.arguments = new Expression[]{singleNameReference2};
            UnaryExpression unaryExpression3 = new UnaryExpression(callToSuper, 11);
            EclipseHandlerUtil.setGeneratedBy(unaryExpression3, source);
            FalseLiteral falseLiteral3 = new FalseLiteral(pS, pE);
            EclipseHandlerUtil.setGeneratedBy(falseLiteral3, source);
            ReturnStatement returnFalse3 = new ReturnStatement(falseLiteral3, pS, pE);
            EclipseHandlerUtil.setGeneratedBy(returnFalse3, source);
            IfStatement ifSuperEquals = new IfStatement(unaryExpression3, returnFalse3, pS, pE);
            EclipseHandlerUtil.setGeneratedBy(ifSuperEquals, source);
            statements.add(ifSuperEquals);
        }
        for (InclusionExclusionUtils.Included<EclipseNode, EqualsAndHashCode.Include> member : members) {
            EclipseNode memberNode = member.getNode();
            boolean isMethod = memberNode.getKind() == AST.Kind.METHOD;
            TypeReference fType = EclipseHandlerUtil.getFieldType(memberNode, fieldAccess);
            char[] token = fType.getLastToken();
            Expression thisFieldAccessor = isMethod ? EclipseHandlerUtil.createMethodAccessor(memberNode, source) : EclipseHandlerUtil.createFieldAccessor(memberNode, fieldAccess, source);
            Expression otherFieldAccessor = isMethod ? EclipseHandlerUtil.createMethodAccessor(memberNode, source, otherName) : EclipseHandlerUtil.createFieldAccessor(memberNode, fieldAccess, source, otherName);
            if (fType.dimensions() != 0 || token == null) {
                if (fType.dimensions() > 0 && token != null) {
                    MessageSend arraysEqualCall = new MessageSend();
                    arraysEqualCall.sourceStart = pS;
                    arraysEqualCall.sourceEnd = pE;
                    EclipseHandlerUtil.setGeneratedBy(arraysEqualCall, source);
                    arraysEqualCall.receiver = generateQualifiedNameRef(source, new char[]{TypeConstants.JAVA, TypeConstants.UTIL, "Arrays".toCharArray()});
                    if (fType.dimensions() > 1 || !BUILT_IN_TYPES.contains(new String(token))) {
                        arraysEqualCall.selector = "deepEquals".toCharArray();
                    } else {
                        arraysEqualCall.selector = "equals".toCharArray();
                    }
                    arraysEqualCall.arguments = new Expression[]{thisFieldAccessor, otherFieldAccessor};
                    UnaryExpression arraysNotEqual = new UnaryExpression(arraysEqualCall, 11);
                    arraysNotEqual.sourceStart = pS;
                    arraysNotEqual.sourceEnd = pE;
                    EclipseHandlerUtil.setGeneratedBy(arraysNotEqual, source);
                    FalseLiteral falseLiteral4 = new FalseLiteral(pS, pE);
                    EclipseHandlerUtil.setGeneratedBy(falseLiteral4, source);
                    ReturnStatement returnStatement = new ReturnStatement(falseLiteral4, pS, pE);
                    EclipseHandlerUtil.setGeneratedBy(returnStatement, source);
                    IfStatement ifStatement = new IfStatement(arraysNotEqual, returnStatement, pS, pE);
                    EclipseHandlerUtil.setGeneratedBy(ifStatement, source);
                    statements.add(ifStatement);
                }
            } else if (Arrays.equals(TypeConstants.FLOAT, token)) {
                statements.add(generateCompareFloatOrDouble(thisFieldAccessor, otherFieldAccessor, "Float".toCharArray(), source));
            } else if (Arrays.equals(TypeConstants.DOUBLE, token)) {
                statements.add(generateCompareFloatOrDouble(thisFieldAccessor, otherFieldAccessor, "Double".toCharArray(), source));
            } else if (BUILT_IN_TYPES.contains(new String(token))) {
                EqualExpression fieldsNotEqual = new EqualExpression(thisFieldAccessor, otherFieldAccessor, 29);
                EclipseHandlerUtil.setGeneratedBy(fieldsNotEqual, source);
                FalseLiteral falseLiteral5 = new FalseLiteral(pS, pE);
                EclipseHandlerUtil.setGeneratedBy(falseLiteral5, source);
                ReturnStatement returnStatement2 = new ReturnStatement(falseLiteral5, pS, pE);
                EclipseHandlerUtil.setGeneratedBy(returnStatement2, source);
                IfStatement ifStatement2 = new IfStatement(fieldsNotEqual, returnStatement2, pS, pE);
                EclipseHandlerUtil.setGeneratedBy(ifStatement2, source);
                statements.add(ifStatement2);
            } else {
                char[] thisDollarFieldName = (OgnlContext.THIS_CONTEXT_KEY + (isMethod ? ClassUtils.CGLIB_CLASS_SEPARATOR : PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX) + memberNode.getName()).toCharArray();
                char[] otherDollarFieldName = ("other" + (isMethod ? ClassUtils.CGLIB_CLASS_SEPARATOR : PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX) + memberNode.getName()).toCharArray();
                statements.add(createLocalDeclaration(source, thisDollarFieldName, generateQualifiedTypeRef(source, TypeConstants.JAVA_LANG_OBJECT), thisFieldAccessor));
                statements.add(createLocalDeclaration(source, otherDollarFieldName, generateQualifiedTypeRef(source, TypeConstants.JAVA_LANG_OBJECT), otherFieldAccessor));
                SingleNameReference this1 = new SingleNameReference(thisDollarFieldName, p);
                EclipseHandlerUtil.setGeneratedBy(this1, source);
                SingleNameReference this2 = new SingleNameReference(thisDollarFieldName, p);
                EclipseHandlerUtil.setGeneratedBy(this2, source);
                SingleNameReference other1 = new SingleNameReference(otherDollarFieldName, p);
                EclipseHandlerUtil.setGeneratedBy(other1, source);
                Expression singleNameReference3 = new SingleNameReference(otherDollarFieldName, p);
                EclipseHandlerUtil.setGeneratedBy(singleNameReference3, source);
                NullLiteral nullLiteral = new NullLiteral(pS, pE);
                EclipseHandlerUtil.setGeneratedBy(nullLiteral, source);
                EqualExpression fieldIsNull = new EqualExpression(this1, nullLiteral, 18);
                NullLiteral nullLiteral2 = new NullLiteral(pS, pE);
                EclipseHandlerUtil.setGeneratedBy(nullLiteral2, source);
                EqualExpression otherFieldIsntNull = new EqualExpression(other1, nullLiteral2, 29);
                MessageSend equalsCall = new MessageSend();
                equalsCall.sourceStart = pS;
                equalsCall.sourceEnd = pE;
                EclipseHandlerUtil.setGeneratedBy(equalsCall, source);
                equalsCall.receiver = this2;
                equalsCall.selector = "equals".toCharArray();
                equalsCall.arguments = new Expression[]{singleNameReference3};
                UnaryExpression fieldsNotEqual2 = new UnaryExpression(equalsCall, 11);
                fieldsNotEqual2.sourceStart = pS;
                fieldsNotEqual2.sourceEnd = pE;
                EclipseHandlerUtil.setGeneratedBy(fieldsNotEqual2, source);
                ConditionalExpression fullEquals = new ConditionalExpression(fieldIsNull, otherFieldIsntNull, fieldsNotEqual2);
                fullEquals.sourceStart = pS;
                fullEquals.sourceEnd = pE;
                EclipseHandlerUtil.setGeneratedBy(fullEquals, source);
                FalseLiteral falseLiteral6 = new FalseLiteral(pS, pE);
                EclipseHandlerUtil.setGeneratedBy(falseLiteral6, source);
                ReturnStatement returnStatement3 = new ReturnStatement(falseLiteral6, pS, pE);
                EclipseHandlerUtil.setGeneratedBy(returnStatement3, source);
                IfStatement ifStatement3 = new IfStatement(fullEquals, returnStatement3, pS, pE);
                EclipseHandlerUtil.setGeneratedBy(ifStatement3, source);
                statements.add(ifStatement3);
            }
        }
        TrueLiteral trueLiteral2 = new TrueLiteral(pS, pE);
        EclipseHandlerUtil.setGeneratedBy(trueLiteral2, source);
        ReturnStatement returnStatement4 = new ReturnStatement(trueLiteral2, pS, pE);
        EclipseHandlerUtil.setGeneratedBy(returnStatement4, source);
        statements.add(returnStatement4);
        method.statements = (Statement[]) statements.toArray(new Statement[statements.size()]);
        return method;
    }

    public MethodDeclaration createCanEqual(EclipseNode type, ASTNode source, List<Annotation> onParam) {
        int pS = source.sourceStart;
        int pE = source.sourceEnd;
        long p = (pS << 32) | pE;
        char[] otherName = "other".toCharArray();
        MethodDeclaration method = new MethodDeclaration(type.top().get().compilationResult);
        EclipseHandlerUtil.setGeneratedBy(method, source);
        method.modifiers = EclipseHandlerUtil.toEclipseModifier(AccessLevel.PROTECTED);
        method.returnType = TypeReference.baseTypeReference(5, 0);
        method.returnType.sourceStart = pS;
        method.returnType.sourceEnd = pE;
        EclipseHandlerUtil.setGeneratedBy(method.returnType, source);
        method.selector = "canEqual".toCharArray();
        method.thrownExceptions = null;
        method.typeParameters = null;
        method.bits |= 8388608;
        int i = source.sourceStart;
        method.sourceStart = i;
        method.declarationSourceStart = i;
        method.bodyStart = i;
        int i2 = source.sourceEnd;
        method.sourceEnd = i2;
        method.declarationSourceEnd = i2;
        method.bodyEnd = i2;
        QualifiedTypeReference qualifiedTypeReference = new QualifiedTypeReference(TypeConstants.JAVA_LANG_OBJECT, new long[]{p, p, p});
        EclipseHandlerUtil.setGeneratedBy(qualifiedTypeReference, source);
        method.arguments = new Argument[]{new Argument(otherName, 0L, qualifiedTypeReference, 16)};
        method.arguments[0].sourceStart = pS;
        method.arguments[0].sourceEnd = pE;
        if (!onParam.isEmpty()) {
            method.arguments[0].annotations = (Annotation[]) onParam.toArray(new Annotation[0]);
        }
        EclipseHandlerUtil.setGeneratedBy(method.arguments[0], source);
        SingleNameReference otherRef = new SingleNameReference(otherName, p);
        EclipseHandlerUtil.setGeneratedBy(otherRef, source);
        TypeReference typeReference = createTypeReference(type, p, source, false);
        EclipseHandlerUtil.setGeneratedBy(typeReference, source);
        InstanceOfExpression instanceOf = new InstanceOfExpression(otherRef, typeReference);
        instanceOf.sourceStart = pS;
        instanceOf.sourceEnd = pE;
        EclipseHandlerUtil.setGeneratedBy(instanceOf, source);
        Statement returnStatement = new ReturnStatement(instanceOf, pS, pE);
        EclipseHandlerUtil.setGeneratedBy(returnStatement, source);
        method.statements = new Statement[]{returnStatement};
        return method;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v1, types: [char[], char[][]] */
    public IfStatement generateCompareFloatOrDouble(Expression thisRef, Expression otherRef, char[] floatOrDouble, ASTNode source) {
        int pS = source.sourceStart;
        int pE = source.sourceEnd;
        MessageSend floatCompare = new MessageSend();
        floatCompare.sourceStart = pS;
        floatCompare.sourceEnd = pE;
        EclipseHandlerUtil.setGeneratedBy(floatCompare, source);
        floatCompare.receiver = generateQualifiedNameRef(source, new char[]{TypeConstants.JAVA, TypeConstants.LANG, floatOrDouble});
        floatCompare.selector = "compare".toCharArray();
        floatCompare.arguments = new Expression[]{thisRef, otherRef};
        IntLiteral int0 = EclipseHandlerUtil.makeIntLiteral("0".toCharArray(), source);
        EqualExpression ifFloatCompareIsNot0 = new EqualExpression(floatCompare, int0, 29);
        ifFloatCompareIsNot0.sourceStart = pS;
        ifFloatCompareIsNot0.sourceEnd = pE;
        EclipseHandlerUtil.setGeneratedBy(ifFloatCompareIsNot0, source);
        FalseLiteral falseLiteral = new FalseLiteral(pS, pE);
        EclipseHandlerUtil.setGeneratedBy(falseLiteral, source);
        ReturnStatement returnFalse = new ReturnStatement(falseLiteral, pS, pE);
        EclipseHandlerUtil.setGeneratedBy(returnFalse, source);
        IfStatement ifStatement = new IfStatement(ifFloatCompareIsNot0, returnFalse, pS, pE);
        EclipseHandlerUtil.setGeneratedBy(ifStatement, source);
        return ifStatement;
    }

    public Expression longToIntForHashCode(Expression ref1, Expression ref2, ASTNode source) throws IllegalAccessException, InstantiationException, IllegalArgumentException, InvocationTargetException {
        int pS = source.sourceStart;
        int pE = source.sourceEnd;
        IntLiteral int32 = EclipseHandlerUtil.makeIntLiteral(ANSIConstants.GREEN_FG.toCharArray(), source);
        BinaryExpression higherBits = new BinaryExpression(ref1, int32, 19);
        EclipseHandlerUtil.setGeneratedBy(higherBits, source);
        BinaryExpression xorParts = new BinaryExpression(ref2, higherBits, 8);
        EclipseHandlerUtil.setGeneratedBy(xorParts, source);
        TypeReference intRef = TypeReference.baseTypeReference(10, 0);
        intRef.sourceStart = pS;
        intRef.sourceEnd = pE;
        EclipseHandlerUtil.setGeneratedBy(intRef, source);
        CastExpression expr = EclipseHandlerUtil.makeCastExpression(xorParts, intRef, source);
        expr.sourceStart = pS;
        expr.sourceEnd = pE;
        return expr;
    }

    public NameReference generateQualifiedNameRef(ASTNode source, char[]... varNames) {
        int pS = source.sourceStart;
        int pE = source.sourceEnd;
        long p = (pS << 32) | pE;
        QualifiedNameReference qualifiedNameReference = varNames.length > 1 ? new QualifiedNameReference(varNames, new long[varNames.length], pS, pE) : new SingleNameReference(varNames[0], p);
        EclipseHandlerUtil.setGeneratedBy(qualifiedNameReference, source);
        return qualifiedNameReference;
    }

    public TypeReference generateQualifiedTypeRef(ASTNode source, char[]... varNames) {
        int pS = source.sourceStart;
        int pE = source.sourceEnd;
        long p = (pS << 32) | pE;
        long[] poss = Eclipse.poss(source, varNames.length);
        QualifiedTypeReference qualifiedTypeReference = varNames.length > 1 ? new QualifiedTypeReference(varNames, poss) : new SingleTypeReference(varNames[0], p);
        EclipseHandlerUtil.setGeneratedBy(qualifiedTypeReference, source);
        return qualifiedTypeReference;
    }
}
