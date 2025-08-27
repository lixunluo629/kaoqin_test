package lombok.eclipse.handlers;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.ConfigurationKeys;
import lombok.ToString;
import lombok.core.AST;
import lombok.core.AnnotationValues;
import lombok.core.handlers.HandlerUtil;
import lombok.core.handlers.InclusionExclusionUtils;
import lombok.eclipse.EclipseAnnotationHandler;
import lombok.eclipse.EclipseNode;
import lombok.eclipse.handlers.EclipseHandlerUtil;
import org.apache.xmlbeans.XmlErrorCodes;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.Annotation;
import org.eclipse.jdt.internal.compiler.ast.BinaryExpression;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.NameReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedNameReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.StringLiteral;
import org.eclipse.jdt.internal.compiler.ast.SuperReference;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.jdt.internal.compiler.lookup.TypeConstants;

/* loaded from: lombok-1.16.22.jar:lombok/eclipse/handlers/HandleToString.SCL.lombok */
public class HandleToString extends EclipseAnnotationHandler<ToString> {
    private static final Set<String> BUILT_IN_TYPES = Collections.unmodifiableSet(new HashSet(Arrays.asList("byte", "short", XmlErrorCodes.INT, XmlErrorCodes.LONG, "char", "boolean", XmlErrorCodes.DOUBLE, XmlErrorCodes.FLOAT)));
    private static /* synthetic */ int[] $SWITCH_TABLE$lombok$eclipse$handlers$EclipseHandlerUtil$MemberExistsResult;

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
    public void handle(AnnotationValues<ToString> annotation, Annotation ast, EclipseNode annotationNode) throws SecurityException {
        HandlerUtil.handleFlagUsage(annotationNode, ConfigurationKeys.TO_STRING_FLAG_USAGE, "@ToString");
        ToString ann = (ToString) annotation.getInstance();
        List<InclusionExclusionUtils.Included<EclipseNode, ToString.Include>> members = InclusionExclusionUtils.handleToStringMarking(annotationNode.up(), annotation, annotationNode);
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
        boolean includeFieldNames = (annotation.isExplicit("includeFieldNames") || fieldNamesConfiguration == null) ? ann.includeFieldNames() : fieldNamesConfiguration.booleanValue();
        generateToString(annotationNode.up(), annotationNode, members, includeFieldNames, callSuper, true, fieldAccess);
    }

    public void generateToStringForType(EclipseNode typeNode, EclipseNode errorNode) throws SecurityException {
        if (EclipseHandlerUtil.hasAnnotation((Class<? extends java.lang.annotation.Annotation>) ToString.class, typeNode)) {
            return;
        }
        boolean includeFieldNames = true;
        try {
            Boolean configuration = (Boolean) typeNode.getAst().readConfiguration(ConfigurationKeys.TO_STRING_INCLUDE_FIELD_NAMES);
            includeFieldNames = configuration != null ? configuration.booleanValue() : ((Boolean) ToString.class.getMethod("includeFieldNames", new Class[0]).getDefaultValue()).booleanValue();
        } catch (Exception unused) {
        }
        Boolean doNotUseGettersConfiguration = (Boolean) typeNode.getAst().readConfiguration(ConfigurationKeys.TO_STRING_DO_NOT_USE_GETTERS);
        HandlerUtil.FieldAccess access = (doNotUseGettersConfiguration == null || !doNotUseGettersConfiguration.booleanValue()) ? HandlerUtil.FieldAccess.GETTER : HandlerUtil.FieldAccess.PREFER_FIELD;
        List<InclusionExclusionUtils.Included<EclipseNode, ToString.Include>> members = InclusionExclusionUtils.handleToStringMarking(typeNode, null, null);
        generateToString(typeNode, errorNode, members, includeFieldNames, null, false, access);
    }

    public void generateToString(EclipseNode typeNode, EclipseNode errorNode, List<InclusionExclusionUtils.Included<EclipseNode, ToString.Include>> members, boolean includeFieldNames, Boolean callSuper, boolean whineIfExists, HandlerUtil.FieldAccess fieldAccess) throws SecurityException {
        TypeDeclaration typeDecl = null;
        if (typeNode.get() instanceof TypeDeclaration) {
            typeDecl = (TypeDeclaration) typeNode.get();
        }
        int modifiers = typeDecl == null ? 0 : typeDecl.modifiers;
        boolean notAClass = (modifiers & 8704) != 0;
        if (callSuper == null) {
            try {
                callSuper = Boolean.valueOf(((Boolean) ToString.class.getMethod("callSuper", new Class[0]).getDefaultValue()).booleanValue());
            } catch (Exception unused) {
            }
        }
        if (typeDecl == null || notAClass) {
            errorNode.addError("@ToString is only supported on a class or enum.");
            return;
        }
        switch ($SWITCH_TABLE$lombok$eclipse$handlers$EclipseHandlerUtil$MemberExistsResult()[EclipseHandlerUtil.methodExists("toString", typeNode, 0).ordinal()]) {
            case 1:
                MethodDeclaration toString = createToString(typeNode, members, includeFieldNames, callSuper.booleanValue(), errorNode.get(), fieldAccess);
                EclipseHandlerUtil.injectMethod(typeNode, toString);
                break;
            case 2:
                break;
            case 3:
            default:
                if (whineIfExists) {
                    errorNode.addWarning("Not generating toString(): A method with that name already exists");
                    break;
                }
                break;
        }
    }

    /* JADX WARN: Type inference failed for: r2v29, types: [char[], char[][]] */
    public static MethodDeclaration createToString(EclipseNode type, Collection<InclusionExclusionUtils.Included<EclipseNode, ToString.Include>> members, boolean includeNames, boolean callSuper, ASTNode source, HandlerUtil.FieldAccess fieldAccess) throws SecurityException {
        char[] prefix;
        Expression memberAccessor;
        Expression ex;
        StringLiteral stringLiteral;
        String typeName = getTypeName(type);
        char[] suffix = ")".toCharArray();
        char[] infix = ", ".toCharArray();
        int pS = source.sourceStart;
        int pE = source.sourceEnd;
        long p = (pS << 32) | pE;
        if (callSuper) {
            prefix = (String.valueOf(typeName) + "(super=").toCharArray();
        } else if (members.isEmpty()) {
            prefix = (String.valueOf(typeName) + "()").toCharArray();
        } else if (includeNames) {
            InclusionExclusionUtils.Included<EclipseNode, ToString.Include> firstMember = members.iterator().next();
            String name = firstMember.getInc() == null ? "" : ((ToString.Include) firstMember.getInc()).name();
            if (name.isEmpty()) {
                name = firstMember.getNode().getName();
            }
            prefix = (String.valueOf(typeName) + "(" + name + SymbolConstants.EQUAL_SYMBOL).toCharArray();
        } else {
            prefix = (String.valueOf(typeName) + "(").toCharArray();
        }
        boolean first = true;
        ASTNode stringLiteral2 = new StringLiteral(prefix, pS, pE, 0);
        EclipseHandlerUtil.setGeneratedBy(stringLiteral2, source);
        if (callSuper) {
            MessageSend callToSuper = new MessageSend();
            callToSuper.sourceStart = pS;
            callToSuper.sourceEnd = pE;
            EclipseHandlerUtil.setGeneratedBy(callToSuper, source);
            callToSuper.receiver = new SuperReference(pS, pE);
            EclipseHandlerUtil.setGeneratedBy(callToSuper, source);
            callToSuper.selector = "toString".toCharArray();
            stringLiteral2 = new BinaryExpression(stringLiteral2, callToSuper, 14);
            EclipseHandlerUtil.setGeneratedBy(stringLiteral2, source);
            first = false;
        }
        for (InclusionExclusionUtils.Included<EclipseNode, ToString.Include> member : members) {
            EclipseNode memberNode = member.getNode();
            TypeReference fieldType = EclipseHandlerUtil.getFieldType(memberNode, fieldAccess);
            if (memberNode.getKind() == AST.Kind.METHOD) {
                memberAccessor = EclipseHandlerUtil.createMethodAccessor(memberNode, source);
            } else {
                memberAccessor = EclipseHandlerUtil.createFieldAccessor(memberNode, fieldAccess, source);
            }
            boolean fieldBaseTypeIsPrimitive = BUILT_IN_TYPES.contains(new String(fieldType.getLastToken()));
            if (fieldType.dimensions() == 0) {
            }
            boolean fieldIsPrimitiveArray = fieldType.dimensions() == 1 && fieldBaseTypeIsPrimitive;
            boolean fieldIsObjectArray = fieldType.dimensions() > 0 && !fieldIsPrimitiveArray;
            if (fieldIsPrimitiveArray || fieldIsObjectArray) {
                Expression messageSend = new MessageSend();
                ((MessageSend) messageSend).sourceStart = pS;
                ((MessageSend) messageSend).sourceEnd = pE;
                ((MessageSend) messageSend).receiver = generateQualifiedNameRef(source, new char[]{TypeConstants.JAVA, TypeConstants.UTIL, "Arrays".toCharArray()});
                ((MessageSend) messageSend).arguments = new Expression[]{memberAccessor};
                EclipseHandlerUtil.setGeneratedBy(((MessageSend) messageSend).arguments[0], source);
                ((MessageSend) messageSend).selector = (fieldIsObjectArray ? "deepToString" : "toString").toCharArray();
                ex = messageSend;
            } else {
                ex = memberAccessor;
            }
            EclipseHandlerUtil.setGeneratedBy(ex, source);
            if (first) {
                stringLiteral2 = new BinaryExpression(stringLiteral2, ex, 14);
                ((Expression) stringLiteral2).sourceStart = pS;
                ((Expression) stringLiteral2).sourceEnd = pE;
                EclipseHandlerUtil.setGeneratedBy(stringLiteral2, source);
                first = false;
            } else {
                if (includeNames) {
                    String n = member.getInc() == null ? "" : ((ToString.Include) member.getInc()).name();
                    if (n.isEmpty()) {
                        n = memberNode.getName();
                    }
                    char[] namePlusEqualsSign = (String.valueOf(", ") + n + SymbolConstants.EQUAL_SYMBOL).toCharArray();
                    stringLiteral = new StringLiteral(namePlusEqualsSign, pS, pE, 0);
                } else {
                    stringLiteral = new StringLiteral(infix, pS, pE, 0);
                }
                StringLiteral fieldNameLiteral = stringLiteral;
                EclipseHandlerUtil.setGeneratedBy(fieldNameLiteral, source);
                BinaryExpression binaryExpression = new BinaryExpression(stringLiteral2, fieldNameLiteral, 14);
                EclipseHandlerUtil.setGeneratedBy(binaryExpression, source);
                stringLiteral2 = new BinaryExpression(binaryExpression, ex, 14);
                EclipseHandlerUtil.setGeneratedBy(stringLiteral2, source);
            }
        }
        if (!first) {
            StringLiteral suffixLiteral = new StringLiteral(suffix, pS, pE, 0);
            EclipseHandlerUtil.setGeneratedBy(suffixLiteral, source);
            stringLiteral2 = new BinaryExpression(stringLiteral2, suffixLiteral, 14);
            EclipseHandlerUtil.setGeneratedBy(stringLiteral2, source);
        }
        Statement returnStatement = new ReturnStatement(stringLiteral2, pS, pE);
        EclipseHandlerUtil.setGeneratedBy(returnStatement, source);
        MethodDeclaration method = new MethodDeclaration(type.top().get().compilationResult);
        EclipseHandlerUtil.setGeneratedBy(method, source);
        method.modifiers = EclipseHandlerUtil.toEclipseModifier(AccessLevel.PUBLIC);
        method.returnType = new QualifiedTypeReference(TypeConstants.JAVA_LANG_STRING, new long[]{p, p, p});
        EclipseHandlerUtil.setGeneratedBy(method.returnType, source);
        method.annotations = new Annotation[]{EclipseHandlerUtil.makeMarkerAnnotation(TypeConstants.JAVA_LANG_OVERRIDE, source)};
        method.arguments = null;
        method.selector = "toString".toCharArray();
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
        method.statements = new Statement[]{returnStatement};
        return method;
    }

    public static String getTypeName(EclipseNode type) {
        String typeName = getSingleTypeName(type);
        EclipseNode eclipseNodeUp = type.up();
        while (true) {
            EclipseNode upType = eclipseNodeUp;
            if (upType.getKind() == AST.Kind.TYPE) {
                typeName = String.valueOf(getSingleTypeName(upType)) + "." + typeName;
                eclipseNodeUp = upType.up();
            } else {
                return typeName;
            }
        }
    }

    public static String getSingleTypeName(EclipseNode type) {
        TypeDeclaration typeDeclaration = type.get();
        char[] rawTypeName = typeDeclaration.name;
        return rawTypeName == null ? "" : new String(rawTypeName);
    }

    public static NameReference generateQualifiedNameRef(ASTNode source, char[]... varNames) {
        int pS = source.sourceStart;
        int pE = source.sourceEnd;
        long p = (pS << 32) | pE;
        QualifiedNameReference qualifiedNameReference = varNames.length > 1 ? new QualifiedNameReference(varNames, new long[varNames.length], pS, pE) : new SingleNameReference(varNames[0], p);
        EclipseHandlerUtil.setGeneratedBy(qualifiedNameReference, source);
        return qualifiedNameReference;
    }
}
