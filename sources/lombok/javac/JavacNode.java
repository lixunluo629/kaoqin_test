package lombok.javac;

import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.model.JavacTypes;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.JCDiagnostic;
import com.sun.tools.javac.util.Name;
import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.List;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
import lombok.core.AST;
import lombok.core.AnnotationValues;
import lombok.core.LombokNode;
import lombok.javac.handlers.JavacHandlerUtil;

/* loaded from: lombok-1.16.22.jar:lombok/javac/JavacNode.SCL.lombok */
public class JavacNode extends LombokNode<JavacAST, JavacNode, JCTree> {
    public JavacNode(JavacAST ast, JCTree node, List<JavacNode> children, AST.Kind kind) {
        super(ast, node, children, kind);
    }

    public Element getElement() {
        if (this.node instanceof JCTree.JCClassDecl) {
            return ((JCTree.JCClassDecl) this.node).sym;
        }
        if (this.node instanceof JCTree.JCMethodDecl) {
            return ((JCTree.JCMethodDecl) this.node).sym;
        }
        if (this.node instanceof JCTree.JCVariableDecl) {
            return ((JCTree.JCVariableDecl) this.node).sym;
        }
        return null;
    }

    public int getEndPosition(JCDiagnostic.DiagnosticPosition pos) {
        JCTree.JCCompilationUnit cu = top().get();
        return Javac.getEndPosition(pos, cu);
    }

    public int getEndPosition() {
        return getEndPosition((JCDiagnostic.DiagnosticPosition) this.node);
    }

    public void traverse(JavacASTVisitor visitor) {
        switch (getKind()) {
            case TYPE:
                visitor.visitType(this, (JCTree.JCClassDecl) get());
                ((JavacAST) this.ast).traverseChildren(visitor, this);
                visitor.endVisitType(this, (JCTree.JCClassDecl) get());
                return;
            case FIELD:
                visitor.visitField(this, (JCTree.JCVariableDecl) get());
                ((JavacAST) this.ast).traverseChildren(visitor, this);
                visitor.endVisitField(this, (JCTree.JCVariableDecl) get());
                return;
            case METHOD:
                visitor.visitMethod(this, (JCTree.JCMethodDecl) get());
                ((JavacAST) this.ast).traverseChildren(visitor, this);
                visitor.endVisitMethod(this, (JCTree.JCMethodDecl) get());
                return;
            case ARGUMENT:
                JCTree.JCMethodDecl parentMethod = (JCTree.JCMethodDecl) up().get();
                visitor.visitMethodArgument(this, (JCTree.JCVariableDecl) get(), parentMethod);
                ((JavacAST) this.ast).traverseChildren(visitor, this);
                visitor.endVisitMethodArgument(this, (JCTree.JCVariableDecl) get(), parentMethod);
                return;
            case LOCAL:
                visitor.visitLocal(this, (JCTree.JCVariableDecl) get());
                ((JavacAST) this.ast).traverseChildren(visitor, this);
                visitor.endVisitLocal(this, (JCTree.JCVariableDecl) get());
                return;
            case COMPILATION_UNIT:
                visitor.visitCompilationUnit(this, (JCTree.JCCompilationUnit) get());
                ((JavacAST) this.ast).traverseChildren(visitor, this);
                visitor.endVisitCompilationUnit(this, (JCTree.JCCompilationUnit) get());
                return;
            case INITIALIZER:
                visitor.visitInitializer(this, (JCTree.JCBlock) get());
                ((JavacAST) this.ast).traverseChildren(visitor, this);
                visitor.endVisitInitializer(this, (JCTree.JCBlock) get());
                return;
            case STATEMENT:
                visitor.visitStatement(this, get());
                ((JavacAST) this.ast).traverseChildren(visitor, this);
                visitor.endVisitStatement(this, get());
                return;
            case ANNOTATION:
                switch (up().getKind()) {
                    case TYPE:
                        visitor.visitAnnotationOnType((JCTree.JCClassDecl) up().get(), this, (JCTree.JCAnnotation) get());
                        return;
                    case FIELD:
                        visitor.visitAnnotationOnField((JCTree.JCVariableDecl) up().get(), this, (JCTree.JCAnnotation) get());
                        return;
                    case METHOD:
                        visitor.visitAnnotationOnMethod((JCTree.JCMethodDecl) up().get(), this, (JCTree.JCAnnotation) get());
                        return;
                    case ARGUMENT:
                        JCTree.JCVariableDecl argument = (JCTree.JCVariableDecl) up().get();
                        JCTree.JCMethodDecl method = (JCTree.JCMethodDecl) up().up().get();
                        visitor.visitAnnotationOnMethodArgument(argument, method, this, (JCTree.JCAnnotation) get());
                        return;
                    case LOCAL:
                        visitor.visitAnnotationOnLocal((JCTree.JCVariableDecl) up().get(), this, (JCTree.JCAnnotation) get());
                        return;
                    default:
                        throw new AssertionError("Annotion not expected as child of a " + up().getKind());
                }
            default:
                throw new AssertionError("Unexpected kind during node traversal: " + getKind());
        }
    }

    @Override // lombok.core.LombokNode
    public String getName() {
        Name n;
        if (this.node instanceof JCTree.JCClassDecl) {
            n = ((JCTree.JCClassDecl) this.node).name;
        } else if (this.node instanceof JCTree.JCMethodDecl) {
            n = ((JCTree.JCMethodDecl) this.node).name;
        } else {
            n = this.node instanceof JCTree.JCVariableDecl ? ((JCTree.JCVariableDecl) this.node).name : null;
        }
        if (n == null) {
            return null;
        }
        return n.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // lombok.core.LombokNode
    public boolean calculateIsStructurallySignificant(JCTree parent) {
        if ((this.node instanceof JCTree.JCClassDecl) || (this.node instanceof JCTree.JCMethodDecl) || (this.node instanceof JCTree.JCVariableDecl) || (this.node instanceof JCTree.JCCompilationUnit)) {
            return true;
        }
        if (this.node instanceof JCTree.JCBlock) {
            return parent instanceof JCTree.JCClassDecl;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // lombok.core.LombokNode
    public boolean fieldContainsAnnotation(JCTree field, JCTree annotation) {
        if (!(field instanceof JCTree.JCVariableDecl)) {
            return false;
        }
        JCTree.JCVariableDecl f = (JCTree.JCVariableDecl) field;
        if (f.mods.annotations == null) {
            return false;
        }
        Iterator it = f.mods.annotations.iterator();
        while (it.hasNext()) {
            JCTree.JCAnnotation childAnnotation = (JCTree.JCAnnotation) it.next();
            if (childAnnotation == annotation) {
                return true;
            }
        }
        return false;
    }

    public JavacTreeMaker getTreeMaker() {
        return ((JavacAST) this.ast).getTreeMaker();
    }

    public Symtab getSymbolTable() {
        return ((JavacAST) this.ast).getSymbolTable();
    }

    public JavacTypes getTypesUtil() {
        return ((JavacAST) this.ast).getTypesUtil();
    }

    public Context getContext() {
        return ((JavacAST) this.ast).getContext();
    }

    public boolean shouldDeleteLombokAnnotations() {
        return LombokOptions.shouldDeleteLombokAnnotations(((JavacAST) this.ast).getContext());
    }

    public Name toName(String name) {
        return ((JavacAST) this.ast).toName(name);
    }

    public void removeDeferredErrors() {
        ((JavacAST) this.ast).removeDeferredErrors(this);
    }

    @Override // lombok.core.DiagnosticsReceiver
    public void addError(String message) {
        ((JavacAST) this.ast).printMessage(Diagnostic.Kind.ERROR, message, this, null, true);
    }

    public void addError(String message, JCDiagnostic.DiagnosticPosition pos) {
        ((JavacAST) this.ast).printMessage(Diagnostic.Kind.ERROR, message, null, pos, true);
    }

    @Override // lombok.core.DiagnosticsReceiver
    public void addWarning(String message) {
        ((JavacAST) this.ast).printMessage(Diagnostic.Kind.WARNING, message, this, null, false);
    }

    public void addWarning(String message, JCDiagnostic.DiagnosticPosition pos) {
        ((JavacAST) this.ast).printMessage(Diagnostic.Kind.WARNING, message, null, pos, false);
    }

    @Override // lombok.core.LombokNode
    public boolean hasAnnotation(Class<? extends Annotation> type) {
        return JavacHandlerUtil.hasAnnotationAndDeleteIfNeccessary(type, this);
    }

    @Override // lombok.core.LombokNode
    public <Z extends Annotation> AnnotationValues<Z> findAnnotation(Class<Z> type) {
        JavacNode annotation = JavacHandlerUtil.findAnnotation(type, this, true);
        if (annotation == null) {
            return null;
        }
        return JavacHandlerUtil.createAnnotation(type, annotation);
    }

    private JCTree.JCModifiers getModifiers() {
        if (this.node instanceof JCTree.JCClassDecl) {
            return ((JCTree.JCClassDecl) this.node).getModifiers();
        }
        if (this.node instanceof JCTree.JCMethodDecl) {
            return ((JCTree.JCMethodDecl) this.node).getModifiers();
        }
        if (this.node instanceof JCTree.JCVariableDecl) {
            return ((JCTree.JCVariableDecl) this.node).getModifiers();
        }
        return null;
    }

    @Override // lombok.core.LombokNode
    public boolean isStatic() {
        JavacNode directUp;
        if (this.node instanceof JCTree.JCClassDecl) {
            JavacNode directUp2 = directUp();
            if (directUp2 == null || directUp2.getKind() == AST.Kind.COMPILATION_UNIT) {
                return true;
            }
            if (!(directUp2.get() instanceof JCTree.JCClassDecl)) {
                return false;
            }
            JCTree.JCClassDecl p = directUp2.get();
            long f = p.mods.flags;
            if ((512 & f) != 0 || (16384 & f) != 0) {
                return true;
            }
        }
        if ((this.node instanceof JCTree.JCVariableDecl) && (directUp = directUp()) != null && (directUp.get() instanceof JCTree.JCClassDecl)) {
            JCTree.JCClassDecl p2 = directUp.get();
            if ((512 & p2.mods.flags) != 0) {
                return true;
            }
        }
        JCTree.JCModifiers mods = getModifiers();
        return (mods == null || (mods.flags & 8) == 0) ? false : true;
    }

    @Override // lombok.core.LombokNode
    public boolean isEnumMember() {
        JCTree.JCModifiers mods;
        return (getKind() != AST.Kind.FIELD || (mods = getModifiers()) == null || (16384 & mods.flags) == 0) ? false : true;
    }

    @Override // lombok.core.LombokNode
    public boolean isTransient() {
        JCTree.JCModifiers mods;
        return (getKind() != AST.Kind.FIELD || (mods = getModifiers()) == null || (128 & mods.flags) == 0) ? false : true;
    }

    @Override // lombok.core.LombokNode
    public int countMethodParameters() {
        com.sun.tools.javac.util.List<JCTree.JCVariableDecl> params;
        if (getKind() == AST.Kind.METHOD && (params = ((JCTree.JCMethodDecl) this.node).params) != null) {
            return params.size();
        }
        return 0;
    }

    @Override // lombok.core.LombokNode
    public int getStartPos() {
        return ((JCTree) this.node).getPreferredPosition();
    }
}
