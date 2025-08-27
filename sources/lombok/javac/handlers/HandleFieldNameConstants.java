package lombok.javac.handlers;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.JCDiagnostic;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Iterator;
import lombok.AccessLevel;
import lombok.ConfigurationKeys;
import lombok.core.AST;
import lombok.core.AnnotationValues;
import lombok.core.handlers.HandlerUtil;
import lombok.experimental.FieldNameConstants;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.web.servlet.tags.form.AbstractHtmlElementTag;

/* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/HandleFieldNameConstants.SCL.lombok */
public class HandleFieldNameConstants extends JavacAnnotationHandler<FieldNameConstants> {
    public void generateFieldNameConstantsForType(JavacNode typeNode, JavacNode errorNode, AccessLevel level) {
        JCTree.JCClassDecl typeDecl = null;
        if (typeNode.get() instanceof JCTree.JCClassDecl) {
            typeDecl = (JCTree.JCClassDecl) typeNode.get();
        }
        long modifiers = typeDecl == null ? 0L : typeDecl.mods.flags;
        boolean notAClass = (modifiers & 8704) != 0;
        if (typeDecl == null || notAClass) {
            errorNode.addError("@FieldNameConstants is only supported on a class, an enum, or a field.");
            return;
        }
        Iterator<JavacNode> it = typeNode.down().iterator();
        while (it.hasNext()) {
            JavacNode field = it.next();
            if (fieldQualifiesForFieldNameConstantsGeneration(field)) {
                generateFieldNameConstantsForField(field, (JCDiagnostic.DiagnosticPosition) errorNode.get(), level);
            }
        }
    }

    private void generateFieldNameConstantsForField(JavacNode fieldNode, JCDiagnostic.DiagnosticPosition pos, AccessLevel level) {
        if (JavacHandlerUtil.hasAnnotation((Class<? extends Annotation>) FieldNameConstants.class, fieldNode)) {
            return;
        }
        createFieldNameConstantsForField(level, fieldNode, fieldNode, false);
    }

    private boolean fieldQualifiesForFieldNameConstantsGeneration(JavacNode field) {
        if (field.getKind() != AST.Kind.FIELD) {
            return false;
        }
        JCTree.JCVariableDecl fieldDecl = field.get();
        return !fieldDecl.name.toString().startsWith(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX) && (fieldDecl.mods.flags & 8) == 0;
    }

    @Override // lombok.javac.JavacAnnotationHandler
    public void handle(AnnotationValues<FieldNameConstants> annotation, JCTree.JCAnnotation ast, JavacNode annotationNode) {
        HandlerUtil.handleExperimentalFlagUsage(annotationNode, ConfigurationKeys.FIELD_NAME_CONSTANTS_FLAG_USAGE, "@FieldNameConstants");
        Collection<JavacNode> fields = annotationNode.upFromAnnotationToFields();
        JavacHandlerUtil.deleteAnnotationIfNeccessary(annotationNode, (Class<? extends Annotation>) FieldNameConstants.class);
        JavacHandlerUtil.deleteImportFromCompilationUnit(annotationNode, "lombok.AccessLevel");
        JavacNode node = annotationNode.up();
        FieldNameConstants annotatationInstance = (FieldNameConstants) annotation.getInstance();
        AccessLevel level = annotatationInstance.level();
        if (node == null) {
        }
        switch (node.getKind()) {
            case FIELD:
                if (level != AccessLevel.NONE) {
                    createFieldNameConstantsForFields(level, fields, annotationNode, annotationNode, true);
                    break;
                }
                break;
            case TYPE:
                if (level == AccessLevel.NONE) {
                    annotationNode.addWarning("type-level '@FieldNameConstants' does not work with AccessLevel.NONE.");
                    break;
                } else {
                    generateFieldNameConstantsForType(node, annotationNode, level);
                    break;
                }
        }
    }

    private void createFieldNameConstantsForFields(AccessLevel level, Collection<JavacNode> fieldNodes, JavacNode annotationNode, JavacNode errorNode, boolean whineIfExists) {
        for (JavacNode fieldNode : fieldNodes) {
            createFieldNameConstantsForField(level, fieldNode, errorNode, whineIfExists);
        }
    }

    private void createFieldNameConstantsForField(AccessLevel level, JavacNode fieldNode, JavacNode source, boolean whineIfExists) {
        if (fieldNode.getKind() != AST.Kind.FIELD) {
            source.addError("@FieldNameConstants is only supported on a class, an enum, or a field");
            return;
        }
        JCTree.JCVariableDecl field = (JCTree.JCVariableDecl) fieldNode.get();
        String fieldName = field.name.toString();
        String constantName = HandlerUtil.camelCaseToConstant(fieldName);
        if (constantName.equals(fieldName)) {
            fieldNode.addWarning("Not generating constant for this field: The name of the constant would be equal to the name of this field.");
            return;
        }
        JavacTreeMaker treeMaker = fieldNode.getTreeMaker();
        JCTree.JCModifiers modifiers = treeMaker.Modifiers(JavacHandlerUtil.toJavacModifier(level) | 8 | 16);
        JCTree.JCExpression returnType = JavacHandlerUtil.chainDots(fieldNode, "java", AbstractHtmlElementTag.LANG_ATTRIBUTE, "String");
        JCTree.JCVariableDecl fieldConstant = treeMaker.VarDef(modifiers, fieldNode.toName(constantName), returnType, treeMaker.Literal(fieldNode.getName()));
        JavacHandlerUtil.injectField((JavacNode) fieldNode.up(), fieldConstant);
    }
}
