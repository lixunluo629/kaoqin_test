package lombok.javac;

import com.sun.source.util.Trees;
import com.sun.tools.javac.tree.JCTree;
import java.lang.annotation.Annotation;
import lombok.core.AnnotationValues;
import lombok.core.SpiLoadUtil;

/* loaded from: lombok-1.16.22.jar:lombok/javac/JavacAnnotationHandler.SCL.lombok */
public abstract class JavacAnnotationHandler<T extends Annotation> {
    protected Trees trees;

    public abstract void handle(AnnotationValues<T> annotationValues, JCTree.JCAnnotation jCAnnotation, JavacNode javacNode);

    public Class<T> getAnnotationHandledByThisHandler() {
        return (Class<T>) SpiLoadUtil.findAnnotationClass(getClass(), JavacAnnotationHandler.class);
    }

    public void setTrees(Trees trees) {
        this.trees = trees;
    }
}
