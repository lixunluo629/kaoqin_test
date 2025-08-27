package lombok.eclipse.handlers;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import lombok.AccessLevel;
import lombok.ConfigurationKeys;
import lombok.core.AST;
import lombok.core.AnnotationValues;
import lombok.core.handlers.HandlerUtil;
import lombok.eclipse.Eclipse;
import lombok.eclipse.EclipseAnnotationHandler;
import lombok.eclipse.EclipseNode;
import lombok.eclipse.handlers.EclipseHandlerUtil;
import lombok.experimental.Wither;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.AllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.Argument;
import org.eclipse.jdt.internal.compiler.ast.ConditionalExpression;
import org.eclipse.jdt.internal.compiler.ast.EqualExpression;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;

/* loaded from: lombok-1.16.22.jar:lombok/eclipse/handlers/HandleWither.SCL.lombok */
public class HandleWither extends EclipseAnnotationHandler<Wither> {
    private static /* synthetic */ int[] $SWITCH_TABLE$lombok$core$AST$Kind;
    private static /* synthetic */ int[] $SWITCH_TABLE$lombok$eclipse$handlers$EclipseHandlerUtil$MemberExistsResult;

    static /* synthetic */ int[] $SWITCH_TABLE$lombok$core$AST$Kind() {
        int[] iArr = $SWITCH_TABLE$lombok$core$AST$Kind;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[AST.Kind.valuesCustom().length];
        try {
            iArr2[AST.Kind.ANNOTATION.ordinal()] = 6;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[AST.Kind.ARGUMENT.ordinal()] = 7;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[AST.Kind.COMPILATION_UNIT.ordinal()] = 1;
        } catch (NoSuchFieldError unused3) {
        }
        try {
            iArr2[AST.Kind.FIELD.ordinal()] = 3;
        } catch (NoSuchFieldError unused4) {
        }
        try {
            iArr2[AST.Kind.INITIALIZER.ordinal()] = 4;
        } catch (NoSuchFieldError unused5) {
        }
        try {
            iArr2[AST.Kind.LOCAL.ordinal()] = 8;
        } catch (NoSuchFieldError unused6) {
        }
        try {
            iArr2[AST.Kind.METHOD.ordinal()] = 5;
        } catch (NoSuchFieldError unused7) {
        }
        try {
            iArr2[AST.Kind.STATEMENT.ordinal()] = 9;
        } catch (NoSuchFieldError unused8) {
        }
        try {
            iArr2[AST.Kind.TYPE.ordinal()] = 2;
        } catch (NoSuchFieldError unused9) {
        }
        $SWITCH_TABLE$lombok$core$AST$Kind = iArr2;
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

    public boolean generateWitherForType(EclipseNode typeNode, EclipseNode pos, AccessLevel level, boolean checkForTypeLevelWither) {
        if (checkForTypeLevelWither && EclipseHandlerUtil.hasAnnotation((Class<? extends Annotation>) Wither.class, typeNode)) {
            return true;
        }
        TypeDeclaration typeDecl = null;
        if (typeNode.get() instanceof TypeDeclaration) {
            typeDecl = (TypeDeclaration) typeNode.get();
        }
        int modifiers = typeDecl == null ? 0 : typeDecl.modifiers;
        boolean notAClass = (modifiers & 25088) != 0;
        if (typeDecl == null || notAClass) {
            pos.addError("@Wither is only supported on a class or a field.");
            return false;
        }
        Iterator<EclipseNode> it = typeNode.down().iterator();
        while (it.hasNext()) {
            EclipseNode field = it.next();
            if (field.getKind() == AST.Kind.FIELD) {
                FieldDeclaration fieldDecl = field.get();
                if (EclipseHandlerUtil.filterField(fieldDecl) && ((fieldDecl.modifiers & 16) == 0 || fieldDecl.initialization == null)) {
                    generateWitherForField(field, pos, level);
                }
            }
        }
        return true;
    }

    public void generateWitherForField(EclipseNode fieldNode, EclipseNode sourceNode, AccessLevel level) {
        Iterator<EclipseNode> it = fieldNode.down().iterator();
        while (it.hasNext()) {
            EclipseNode child = it.next();
            if (child.getKind() == AST.Kind.ANNOTATION && EclipseHandlerUtil.annotationTypeMatches((Class<? extends Annotation>) Wither.class, child)) {
                return;
            }
        }
        List<org.eclipse.jdt.internal.compiler.ast.Annotation> empty = Collections.emptyList();
        createWitherForField(level, fieldNode, sourceNode, false, empty, empty);
    }

    @Override // lombok.eclipse.EclipseAnnotationHandler
    public void handle(AnnotationValues<Wither> annotation, org.eclipse.jdt.internal.compiler.ast.Annotation ast, EclipseNode annotationNode) {
        HandlerUtil.handleExperimentalFlagUsage(annotationNode, ConfigurationKeys.WITHER_FLAG_USAGE, "@Wither");
        EclipseNode node = annotationNode.up();
        AccessLevel level = ((Wither) annotation.getInstance()).value();
        if (level == AccessLevel.NONE || node == null) {
            return;
        }
        List<org.eclipse.jdt.internal.compiler.ast.Annotation> onMethod = EclipseHandlerUtil.unboxAndRemoveAnnotationParameter(ast, "onMethod", "@Wither(onMethod", annotationNode);
        List<org.eclipse.jdt.internal.compiler.ast.Annotation> onParam = EclipseHandlerUtil.unboxAndRemoveAnnotationParameter(ast, "onParam", "@Wither(onParam", annotationNode);
        switch ($SWITCH_TABLE$lombok$core$AST$Kind()[node.getKind().ordinal()]) {
            case 2:
                if (!onMethod.isEmpty()) {
                    annotationNode.addError("'onMethod' is not supported for @Wither on a type.");
                }
                if (!onParam.isEmpty()) {
                    annotationNode.addError("'onParam' is not supported for @Wither on a type.");
                }
                generateWitherForType(node, annotationNode, level, false);
                break;
            case 3:
                createWitherForFields(level, annotationNode.upFromAnnotationToFields(), annotationNode, true, onMethod, onParam);
                break;
        }
    }

    public void createWitherForFields(AccessLevel level, Collection<EclipseNode> fieldNodes, EclipseNode sourceNode, boolean whineIfExists, List<org.eclipse.jdt.internal.compiler.ast.Annotation> onMethod, List<org.eclipse.jdt.internal.compiler.ast.Annotation> onParam) {
        for (EclipseNode fieldNode : fieldNodes) {
            createWitherForField(level, fieldNode, sourceNode, whineIfExists, onMethod, onParam);
        }
    }

    public void createWitherForField(AccessLevel level, EclipseNode fieldNode, EclipseNode sourceNode, boolean whineIfExists, List<org.eclipse.jdt.internal.compiler.ast.Annotation> onMethod, List<org.eclipse.jdt.internal.compiler.ast.Annotation> onParam) {
        ASTNode source = sourceNode.get();
        if (fieldNode.getKind() != AST.Kind.FIELD) {
            sourceNode.addError("@Wither is only supported on a class or a field.");
            return;
        }
        EclipseNode typeNode = fieldNode.up();
        boolean makeAbstract = (typeNode == null || typeNode.getKind() != AST.Kind.TYPE || (typeNode.get().modifiers & 1024) == 0) ? false : true;
        FieldDeclaration field = fieldNode.get();
        TypeReference fieldType = EclipseHandlerUtil.copyType(field.type, source);
        boolean isBoolean = EclipseHandlerUtil.isBoolean(fieldType);
        String witherName = EclipseHandlerUtil.toWitherName(fieldNode, isBoolean);
        if (witherName == null) {
            fieldNode.addWarning("Not generating wither for this field: It does not fit your @Accessors prefix list.");
            return;
        }
        if ((field.modifiers & 8) != 0) {
            fieldNode.addWarning("Not generating wither for this field: Withers cannot be generated for static fields.");
            return;
        }
        if ((field.modifiers & 16) != 0 && field.initialization != null) {
            fieldNode.addWarning("Not generating wither for this field: Withers cannot be generated for final, initialized fields.");
            return;
        }
        if (field.name != null && field.name.length > 0 && field.name[0] == '$') {
            fieldNode.addWarning("Not generating wither for this field: Withers cannot be generated for fields starting with $.");
            return;
        }
        for (String altName : EclipseHandlerUtil.toAllWitherNames(fieldNode, isBoolean)) {
            switch ($SWITCH_TABLE$lombok$eclipse$handlers$EclipseHandlerUtil$MemberExistsResult()[EclipseHandlerUtil.methodExists(altName, fieldNode, false, 1).ordinal()]) {
                case 2:
                    return;
                case 3:
                    if (whineIfExists) {
                        String altNameExpl = altName.equals(witherName) ? "" : String.format(" (%s)", altName);
                        fieldNode.addWarning(String.format("Not generating %s(): A method with that name already exists%s", witherName, altNameExpl));
                        return;
                    }
                    return;
            }
        }
        int modifier = EclipseHandlerUtil.toEclipseModifier(level);
        MethodDeclaration method = createWither((TypeDeclaration) fieldNode.up().get(), fieldNode, witherName, modifier, sourceNode, onMethod, onParam, makeAbstract);
        EclipseHandlerUtil.injectMethod(fieldNode.up(), method);
    }

    /* JADX WARN: Type inference failed for: r2v10, types: [org.eclipse.jdt.internal.compiler.ast.Annotation[], org.eclipse.jdt.internal.compiler.ast.Annotation[][]] */
    /* JADX WARN: Type inference failed for: r2v4, types: [org.eclipse.jdt.internal.compiler.ast.Annotation[], org.eclipse.jdt.internal.compiler.ast.Annotation[][]] */
    public MethodDeclaration createWither(TypeDeclaration parent, EclipseNode fieldNode, String name, int modifier, EclipseNode sourceNode, List<org.eclipse.jdt.internal.compiler.ast.Annotation> onMethod, List<org.eclipse.jdt.internal.compiler.ast.Annotation> onParam, boolean makeAbstract) {
        Statement nullCheck;
        ASTNode source = sourceNode.get();
        if (name == null) {
            return null;
        }
        FieldDeclaration field = fieldNode.get();
        int pS = source.sourceStart;
        int pE = source.sourceEnd;
        long p = (pS << 32) | pE;
        MethodDeclaration method = new MethodDeclaration(parent.compilationResult);
        if (makeAbstract) {
            modifier = modifier | 1024 | 16777216;
        }
        method.modifiers = modifier;
        method.returnType = EclipseHandlerUtil.cloneSelfType(fieldNode, source);
        if (method.returnType == null) {
            return null;
        }
        org.eclipse.jdt.internal.compiler.ast.Annotation[] deprecated = null;
        if (EclipseHandlerUtil.isFieldDeprecated(fieldNode)) {
            deprecated = new org.eclipse.jdt.internal.compiler.ast.Annotation[]{EclipseHandlerUtil.generateDeprecatedAnnotation(source)};
        }
        method.annotations = EclipseHandlerUtil.copyAnnotations(source, new org.eclipse.jdt.internal.compiler.ast.Annotation[]{(org.eclipse.jdt.internal.compiler.ast.Annotation[]) onMethod.toArray(new org.eclipse.jdt.internal.compiler.ast.Annotation[0]), deprecated});
        Argument param = new Argument(field.name, p, EclipseHandlerUtil.copyType(field.type, source), 16);
        param.sourceStart = pS;
        param.sourceEnd = pE;
        method.arguments = new Argument[]{param};
        method.selector = name.toCharArray();
        method.binding = null;
        method.thrownExceptions = null;
        method.typeParameters = null;
        method.bits |= 8388608;
        org.eclipse.jdt.internal.compiler.ast.Annotation[] nonNulls = Eclipse.findAnnotations(field, HandlerUtil.NON_NULL_PATTERN);
        org.eclipse.jdt.internal.compiler.ast.Annotation[] nullables = Eclipse.findAnnotations(field, HandlerUtil.NULLABLE_PATTERN);
        if (!makeAbstract) {
            List<Expression> args = new ArrayList<>();
            Iterator<EclipseNode> it = fieldNode.up().down().iterator();
            while (it.hasNext()) {
                EclipseNode child = it.next();
                if (child.getKind() == AST.Kind.FIELD) {
                    FieldDeclaration childDecl = child.get();
                    if (childDecl.name == null || childDecl.name.length <= 0 || childDecl.name[0] != '$') {
                        long fieldFlags = childDecl.modifiers;
                        if ((fieldFlags & 8) == 0 && ((fieldFlags & 16) == 0 || childDecl.initialization == null)) {
                            if (child.get() == fieldNode.get()) {
                                args.add(new SingleNameReference(field.name, p));
                            } else {
                                args.add(EclipseHandlerUtil.createFieldAccessor(child, HandlerUtil.FieldAccess.ALWAYS_FIELD, source));
                            }
                        }
                    }
                }
            }
            AllocationExpression constructorCall = new AllocationExpression();
            constructorCall.arguments = (Expression[]) args.toArray(new Expression[0]);
            constructorCall.type = EclipseHandlerUtil.cloneSelfType(fieldNode, source);
            EqualExpression equalExpression = new EqualExpression(EclipseHandlerUtil.createFieldAccessor(fieldNode, HandlerUtil.FieldAccess.ALWAYS_FIELD, source), new SingleNameReference(field.name, p), 18);
            ThisReference thisRef = new ThisReference(pS, pE);
            ReturnStatement returnStatement = new ReturnStatement(new ConditionalExpression(equalExpression, thisRef, constructorCall), pS, pE);
            int i = source.sourceStart;
            method.sourceStart = i;
            method.declarationSourceStart = i;
            method.bodyStart = i;
            int i2 = source.sourceEnd;
            method.sourceEnd = i2;
            method.declarationSourceEnd = i2;
            method.bodyEnd = i2;
            List<Statement> statements = new ArrayList<>(5);
            if (nonNulls.length > 0 && (nullCheck = EclipseHandlerUtil.generateNullCheck(field, sourceNode)) != null) {
                statements.add(nullCheck);
            }
            statements.add(returnStatement);
            method.statements = (Statement[]) statements.toArray(new Statement[0]);
        }
        param.annotations = EclipseHandlerUtil.copyAnnotations(source, new org.eclipse.jdt.internal.compiler.ast.Annotation[]{nonNulls, nullables, (org.eclipse.jdt.internal.compiler.ast.Annotation[]) onParam.toArray(new org.eclipse.jdt.internal.compiler.ast.Annotation[0])});
        method.traverse(new SetGeneratedByVisitor(source), parent.scope);
        return method;
    }
}
