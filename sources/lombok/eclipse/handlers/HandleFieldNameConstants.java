package lombok.eclipse.handlers;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Iterator;
import lombok.AccessLevel;
import lombok.ConfigurationKeys;
import lombok.core.AST;
import lombok.core.AnnotationValues;
import lombok.core.handlers.HandlerUtil;
import lombok.eclipse.EclipseAnnotationHandler;
import lombok.eclipse.EclipseNode;
import lombok.experimental.FieldNameConstants;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.StringLiteral;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.TypeConstants;

/* loaded from: lombok-1.16.22.jar:lombok/eclipse/handlers/HandleFieldNameConstants.SCL.lombok */
public class HandleFieldNameConstants extends EclipseAnnotationHandler<FieldNameConstants> {
    private static /* synthetic */ int[] $SWITCH_TABLE$lombok$core$AST$Kind;

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

    public void generateFieldNameConstantsForType(EclipseNode typeNode, EclipseNode errorNode, AccessLevel level) {
        TypeDeclaration typeDecl = null;
        if (typeNode.get() instanceof TypeDeclaration) {
            typeDecl = (TypeDeclaration) typeNode.get();
        }
        int modifiers = typeDecl == null ? 0 : typeDecl.modifiers;
        boolean notAClass = (modifiers & 8704) != 0;
        if (typeDecl == null || notAClass) {
            errorNode.addError("@FieldNameConstants is only supported on a class, an enum, or a field.");
            return;
        }
        Iterator<EclipseNode> it = typeNode.down().iterator();
        while (it.hasNext()) {
            EclipseNode field = it.next();
            if (fieldQualifiesForFieldNameConstantsGeneration(field)) {
                generateFieldNameConstantsForField(field, errorNode.get(), level);
            }
        }
    }

    private void generateFieldNameConstantsForField(EclipseNode fieldNode, ASTNode pos, AccessLevel level) {
        if (EclipseHandlerUtil.hasAnnotation((Class<? extends Annotation>) FieldNameConstants.class, fieldNode)) {
            return;
        }
        createFieldNameConstantsForField(level, fieldNode, fieldNode, pos, false);
    }

    private boolean fieldQualifiesForFieldNameConstantsGeneration(EclipseNode field) {
        if (field.getKind() != AST.Kind.FIELD) {
            return false;
        }
        FieldDeclaration fieldDecl = field.get();
        return EclipseHandlerUtil.filterField(fieldDecl);
    }

    @Override // lombok.eclipse.EclipseAnnotationHandler
    public void handle(AnnotationValues<FieldNameConstants> annotation, org.eclipse.jdt.internal.compiler.ast.Annotation ast, EclipseNode annotationNode) {
        HandlerUtil.handleExperimentalFlagUsage(annotationNode, ConfigurationKeys.FIELD_NAME_CONSTANTS_FLAG_USAGE, "@FieldNameConstants");
        EclipseNode node = annotationNode.up();
        FieldNameConstants annotatationInstance = (FieldNameConstants) annotation.getInstance();
        AccessLevel level = annotatationInstance.level();
        if (node == null) {
        }
        switch ($SWITCH_TABLE$lombok$core$AST$Kind()[node.getKind().ordinal()]) {
            case 2:
                if (level == AccessLevel.NONE) {
                    annotationNode.addWarning("type-level '@FieldNameConstants' does not work with AccessLevel.NONE.");
                    break;
                } else {
                    generateFieldNameConstantsForType(node, annotationNode, level);
                    break;
                }
            case 3:
                if (level != AccessLevel.NONE) {
                    createFieldNameConstantsForFields(level, annotationNode.upFromAnnotationToFields(), annotationNode, annotationNode.get(), true);
                    break;
                }
                break;
        }
    }

    private void createFieldNameConstantsForFields(AccessLevel level, Collection<EclipseNode> fieldNodes, EclipseNode errorNode, ASTNode source, boolean whineIfExists) {
        for (EclipseNode fieldNode : fieldNodes) {
            createFieldNameConstantsForField(level, fieldNode, errorNode, source, whineIfExists);
        }
    }

    private void createFieldNameConstantsForField(AccessLevel level, EclipseNode fieldNode, EclipseNode errorNode, ASTNode source, boolean whineIfExists) {
        if (fieldNode.getKind() != AST.Kind.FIELD) {
            errorNode.addError("@FieldNameConstants is only supported on a class, an enum, or a field");
            return;
        }
        FieldDeclaration field = fieldNode.get();
        String fieldName = new String(field.name);
        String constantName = HandlerUtil.camelCaseToConstant(fieldName);
        if (constantName.equals(fieldName)) {
            fieldNode.addWarning("Not generating constant for this field: The name of the constant would be equal to the name of this field.");
            return;
        }
        int pS = source.sourceStart;
        int pE = source.sourceEnd;
        long p = (pS << 32) | pE;
        FieldDeclaration fieldConstant = new FieldDeclaration(constantName.toCharArray(), pS, pE);
        fieldConstant.bits |= 8388608;
        fieldConstant.modifiers = EclipseHandlerUtil.toEclipseModifier(level) | 8 | 16;
        fieldConstant.type = new QualifiedTypeReference(TypeConstants.JAVA_LANG_STRING, new long[]{p, p, p});
        fieldConstant.initialization = new StringLiteral(field.name, pS, pE, 0);
        EclipseHandlerUtil.injectField(fieldNode.up(), fieldConstant);
    }
}
