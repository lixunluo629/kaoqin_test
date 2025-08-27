package lombok.javac.handlers;

import com.sun.tools.javac.tree.JCTree;
import java.lang.annotation.Annotation;
import lombok.Builder;
import lombok.core.AST;
import lombok.core.AnnotationValues;
import lombok.core.HandlerPriority;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;

@HandlerPriority(-1025)
/* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/HandleBuilderDefault.SCL.lombok */
public class HandleBuilderDefault extends JavacAnnotationHandler<Builder.Default> {
    @Override // lombok.javac.JavacAnnotationHandler
    public void handle(AnnotationValues<Builder.Default> annotation, JCTree.JCAnnotation ast, JavacNode annotationNode) {
        JavacNode annotatedField = annotationNode.up();
        if (annotatedField.getKind() != AST.Kind.FIELD) {
            return;
        }
        JavacNode classWithAnnotatedField = annotatedField.up();
        if (!JavacHandlerUtil.hasAnnotation((Class<? extends Annotation>) Builder.class, classWithAnnotatedField) && !JavacHandlerUtil.hasAnnotation("lombok.experimental.Builder", classWithAnnotatedField)) {
            annotationNode.addWarning("@Builder.Default requires @Builder on the class for it to mean anything.");
            JavacHandlerUtil.deleteAnnotationIfNeccessary(annotationNode, (Class<? extends Annotation>) Builder.Default.class);
        }
    }
}
