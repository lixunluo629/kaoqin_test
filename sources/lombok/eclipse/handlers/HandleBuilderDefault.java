package lombok.eclipse.handlers;

import lombok.Builder;
import lombok.core.AST;
import lombok.core.AnnotationValues;
import lombok.core.HandlerPriority;
import lombok.eclipse.EclipseAnnotationHandler;
import lombok.eclipse.EclipseNode;
import org.eclipse.jdt.internal.compiler.ast.Annotation;

@HandlerPriority(-1025)
/* loaded from: lombok-1.16.22.jar:lombok/eclipse/handlers/HandleBuilderDefault.SCL.lombok */
public class HandleBuilderDefault extends EclipseAnnotationHandler<Builder.Default> {
    @Override // lombok.eclipse.EclipseAnnotationHandler
    public void handle(AnnotationValues<Builder.Default> annotation, Annotation ast, EclipseNode annotationNode) {
        EclipseNode annotatedField = annotationNode.up();
        if (annotatedField.getKind() != AST.Kind.FIELD) {
            return;
        }
        EclipseNode classWithAnnotatedField = annotatedField.up();
        if (!EclipseHandlerUtil.hasAnnotation((Class<? extends java.lang.annotation.Annotation>) Builder.class, classWithAnnotatedField) && !EclipseHandlerUtil.hasAnnotation("lombok.experimental.Builder", classWithAnnotatedField)) {
            annotationNode.addWarning("@Builder.Default requires @Builder on the class for it to mean anything.");
        }
    }
}
