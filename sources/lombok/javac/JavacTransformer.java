package lombok.javac;

import com.sun.source.util.Trees;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.processing.Messager;

/* loaded from: lombok-1.16.22.jar:lombok/javac/JavacTransformer.SCL.lombok */
public class JavacTransformer {
    private final HandlerLibrary handlers;
    private final Messager messager;

    public JavacTransformer(Messager messager, Trees trees) {
        this.messager = messager;
        this.handlers = HandlerLibrary.load(messager, trees);
    }

    public SortedSet<Long> getPriorities() {
        return this.handlers.getPriorities();
    }

    public SortedSet<Long> getPrioritiesRequiringResolutionReset() {
        return this.handlers.getPrioritiesRequiringResolutionReset();
    }

    public void transform(long priority, Context context, List<JCTree.JCCompilationUnit> compilationUnitsRaw) {
        com.sun.tools.javac.util.List<JCTree.JCCompilationUnit> compilationUnits;
        if (compilationUnitsRaw instanceof com.sun.tools.javac.util.List) {
            compilationUnits = (com.sun.tools.javac.util.List) compilationUnitsRaw;
        } else {
            compilationUnits = com.sun.tools.javac.util.List.nil();
            for (int i = compilationUnitsRaw.size() - 1; i >= 0; i--) {
                compilationUnits = compilationUnits.prepend(compilationUnitsRaw.get(i));
            }
        }
        List<JavacAST> asts = new ArrayList<>();
        Iterator it = compilationUnits.iterator();
        while (it.hasNext()) {
            JCTree.JCCompilationUnit unit = (JCTree.JCCompilationUnit) it.next();
            asts.add(new JavacAST(this.messager, context, unit));
        }
        for (JavacAST ast : asts) {
            ast.traverse(new AnnotationVisitor(priority));
            this.handlers.callASTVisitors(ast, priority);
        }
        for (JavacAST ast2 : asts) {
            if (ast2.isChanged()) {
                LombokOptions.markChanged(context, ast2.top().get());
            }
        }
    }

    /* loaded from: lombok-1.16.22.jar:lombok/javac/JavacTransformer$AnnotationVisitor.SCL.lombok */
    private class AnnotationVisitor extends JavacASTAdapter {
        private final long priority;

        AnnotationVisitor(long priority) {
            this.priority = priority;
        }

        @Override // lombok.javac.JavacASTAdapter, lombok.javac.JavacASTVisitor
        public void visitAnnotationOnType(JCTree.JCClassDecl type, JavacNode annotationNode, JCTree.JCAnnotation annotation) {
            JCTree.JCCompilationUnit top = annotationNode.top().get();
            JavacTransformer.this.handlers.handleAnnotation(top, annotationNode, annotation, this.priority);
        }

        @Override // lombok.javac.JavacASTAdapter, lombok.javac.JavacASTVisitor
        public void visitAnnotationOnField(JCTree.JCVariableDecl field, JavacNode annotationNode, JCTree.JCAnnotation annotation) {
            JCTree.JCCompilationUnit top = annotationNode.top().get();
            JavacTransformer.this.handlers.handleAnnotation(top, annotationNode, annotation, this.priority);
        }

        @Override // lombok.javac.JavacASTAdapter, lombok.javac.JavacASTVisitor
        public void visitAnnotationOnMethod(JCTree.JCMethodDecl method, JavacNode annotationNode, JCTree.JCAnnotation annotation) {
            JCTree.JCCompilationUnit top = annotationNode.top().get();
            JavacTransformer.this.handlers.handleAnnotation(top, annotationNode, annotation, this.priority);
        }

        @Override // lombok.javac.JavacASTAdapter, lombok.javac.JavacASTVisitor
        public void visitAnnotationOnMethodArgument(JCTree.JCVariableDecl argument, JCTree.JCMethodDecl method, JavacNode annotationNode, JCTree.JCAnnotation annotation) {
            JCTree.JCCompilationUnit top = annotationNode.top().get();
            JavacTransformer.this.handlers.handleAnnotation(top, annotationNode, annotation, this.priority);
        }

        @Override // lombok.javac.JavacASTAdapter, lombok.javac.JavacASTVisitor
        public void visitAnnotationOnLocal(JCTree.JCVariableDecl local, JavacNode annotationNode, JCTree.JCAnnotation annotation) {
            JCTree.JCCompilationUnit top = annotationNode.top().get();
            JavacTransformer.this.handlers.handleAnnotation(top, annotationNode, annotation, this.priority);
        }
    }
}
