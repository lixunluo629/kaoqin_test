package lombok.javac;

import com.sun.source.util.Trees;
import com.sun.tools.javac.tree.JCTree;
import java.io.PrintStream;

/* loaded from: lombok-1.16.22.jar:lombok/javac/JavacASTVisitor.SCL.lombok */
public interface JavacASTVisitor {
    void setTrees(Trees trees);

    void visitCompilationUnit(JavacNode javacNode, JCTree.JCCompilationUnit jCCompilationUnit);

    void endVisitCompilationUnit(JavacNode javacNode, JCTree.JCCompilationUnit jCCompilationUnit);

    void visitType(JavacNode javacNode, JCTree.JCClassDecl jCClassDecl);

    void visitAnnotationOnType(JCTree.JCClassDecl jCClassDecl, JavacNode javacNode, JCTree.JCAnnotation jCAnnotation);

    void endVisitType(JavacNode javacNode, JCTree.JCClassDecl jCClassDecl);

    void visitField(JavacNode javacNode, JCTree.JCVariableDecl jCVariableDecl);

    void visitAnnotationOnField(JCTree.JCVariableDecl jCVariableDecl, JavacNode javacNode, JCTree.JCAnnotation jCAnnotation);

    void endVisitField(JavacNode javacNode, JCTree.JCVariableDecl jCVariableDecl);

    void visitInitializer(JavacNode javacNode, JCTree.JCBlock jCBlock);

    void endVisitInitializer(JavacNode javacNode, JCTree.JCBlock jCBlock);

    void visitMethod(JavacNode javacNode, JCTree.JCMethodDecl jCMethodDecl);

    void visitAnnotationOnMethod(JCTree.JCMethodDecl jCMethodDecl, JavacNode javacNode, JCTree.JCAnnotation jCAnnotation);

    void endVisitMethod(JavacNode javacNode, JCTree.JCMethodDecl jCMethodDecl);

    void visitMethodArgument(JavacNode javacNode, JCTree.JCVariableDecl jCVariableDecl, JCTree.JCMethodDecl jCMethodDecl);

    void visitAnnotationOnMethodArgument(JCTree.JCVariableDecl jCVariableDecl, JCTree.JCMethodDecl jCMethodDecl, JavacNode javacNode, JCTree.JCAnnotation jCAnnotation);

    void endVisitMethodArgument(JavacNode javacNode, JCTree.JCVariableDecl jCVariableDecl, JCTree.JCMethodDecl jCMethodDecl);

    void visitLocal(JavacNode javacNode, JCTree.JCVariableDecl jCVariableDecl);

    void visitAnnotationOnLocal(JCTree.JCVariableDecl jCVariableDecl, JavacNode javacNode, JCTree.JCAnnotation jCAnnotation);

    void endVisitLocal(JavacNode javacNode, JCTree.JCVariableDecl jCVariableDecl);

    void visitStatement(JavacNode javacNode, JCTree jCTree);

    void endVisitStatement(JavacNode javacNode, JCTree jCTree);

    /* loaded from: lombok-1.16.22.jar:lombok/javac/JavacASTVisitor$Printer.SCL.lombok */
    public static class Printer implements JavacASTVisitor {
        private final PrintStream out;
        private final boolean printContent;
        private int disablePrinting;
        private int indent;

        public Printer(boolean printContent) {
            this(printContent, System.out);
        }

        public Printer(boolean printContent, PrintStream out) {
            this.disablePrinting = 0;
            this.indent = 0;
            this.printContent = printContent;
            this.out = out;
        }

        @Override // lombok.javac.JavacASTVisitor
        public void setTrees(Trees trees) {
        }

        private void forcePrint(String text, Object... params) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < this.indent; i++) {
                sb.append("  ");
            }
            this.out.printf(sb.append(text).append('\n').toString(), params);
            this.out.flush();
        }

        private void print(String text, Object... params) {
            if (this.disablePrinting == 0) {
                forcePrint(text, params);
            }
        }

        @Override // lombok.javac.JavacASTVisitor
        public void visitCompilationUnit(JavacNode LombokNode, JCTree.JCCompilationUnit unit) {
            this.out.println("---------------------------------------------------------");
            print("<CU %s>", LombokNode.getFileName());
            this.indent++;
        }

        @Override // lombok.javac.JavacASTVisitor
        public void endVisitCompilationUnit(JavacNode node, JCTree.JCCompilationUnit unit) {
            this.indent--;
            print("</CUD>", new Object[0]);
        }

        @Override // lombok.javac.JavacASTVisitor
        public void visitType(JavacNode node, JCTree.JCClassDecl type) {
            print("<TYPE %s>", type.name);
            this.indent++;
            if (this.printContent) {
                print("%s", type);
                this.disablePrinting++;
            }
        }

        @Override // lombok.javac.JavacASTVisitor
        public void visitAnnotationOnType(JCTree.JCClassDecl type, JavacNode node, JCTree.JCAnnotation annotation) {
            forcePrint("<ANNOTATION: %s />", annotation);
        }

        @Override // lombok.javac.JavacASTVisitor
        public void endVisitType(JavacNode node, JCTree.JCClassDecl type) {
            if (this.printContent) {
                this.disablePrinting--;
            }
            this.indent--;
            print("</TYPE %s>", type.name);
        }

        @Override // lombok.javac.JavacASTVisitor
        public void visitInitializer(JavacNode node, JCTree.JCBlock initializer) {
            Object[] objArr = new Object[1];
            objArr[0] = initializer.isStatic() ? "static" : "instance";
            print("<%s INITIALIZER>", objArr);
            this.indent++;
            if (this.printContent) {
                print("%s", initializer);
                this.disablePrinting++;
            }
        }

        @Override // lombok.javac.JavacASTVisitor
        public void endVisitInitializer(JavacNode node, JCTree.JCBlock initializer) {
            if (this.printContent) {
                this.disablePrinting--;
            }
            this.indent--;
            Object[] objArr = new Object[1];
            objArr[0] = initializer.isStatic() ? "static" : "instance";
            print("</%s INITIALIZER>", objArr);
        }

        @Override // lombok.javac.JavacASTVisitor
        public void visitField(JavacNode node, JCTree.JCVariableDecl field) {
            print("<FIELD %s %s>", field.vartype, field.name);
            this.indent++;
            if (this.printContent) {
                if (field.init != null) {
                    print("%s", field.init);
                }
                this.disablePrinting++;
            }
        }

        @Override // lombok.javac.JavacASTVisitor
        public void visitAnnotationOnField(JCTree.JCVariableDecl field, JavacNode node, JCTree.JCAnnotation annotation) {
            forcePrint("<ANNOTATION: %s />", annotation);
        }

        @Override // lombok.javac.JavacASTVisitor
        public void endVisitField(JavacNode node, JCTree.JCVariableDecl field) {
            if (this.printContent) {
                this.disablePrinting--;
            }
            this.indent--;
            print("</FIELD %s %s>", field.vartype, field.name);
        }

        @Override // lombok.javac.JavacASTVisitor
        public void visitMethod(JavacNode node, JCTree.JCMethodDecl method) {
            String type;
            if (!method.name.contentEquals("<init>")) {
                type = "METHOD";
            } else if ((method.mods.flags & 68719476736L) != 0) {
                type = "DEFAULTCONSTRUCTOR";
            } else {
                type = "CONSTRUCTOR";
            }
            print("<%s %s> returns: %s", type, method.name, method.restype);
            this.indent++;
            if (this.printContent) {
                if (method.body == null) {
                    print("(ABSTRACT)", new Object[0]);
                } else {
                    print("%s", method.body);
                }
                this.disablePrinting++;
            }
        }

        @Override // lombok.javac.JavacASTVisitor
        public void visitAnnotationOnMethod(JCTree.JCMethodDecl method, JavacNode node, JCTree.JCAnnotation annotation) {
            forcePrint("<ANNOTATION: %s />", annotation);
        }

        @Override // lombok.javac.JavacASTVisitor
        public void endVisitMethod(JavacNode node, JCTree.JCMethodDecl method) {
            if (this.printContent) {
                this.disablePrinting--;
            }
            this.indent--;
            print("</%s %s>", "XMETHOD", method.name);
        }

        @Override // lombok.javac.JavacASTVisitor
        public void visitMethodArgument(JavacNode node, JCTree.JCVariableDecl arg, JCTree.JCMethodDecl method) {
            print("<METHODARG %s %s>", arg.vartype, arg.name);
            this.indent++;
        }

        @Override // lombok.javac.JavacASTVisitor
        public void visitAnnotationOnMethodArgument(JCTree.JCVariableDecl arg, JCTree.JCMethodDecl method, JavacNode nodeAnnotation, JCTree.JCAnnotation annotation) {
            forcePrint("<ANNOTATION: %s />", annotation);
        }

        @Override // lombok.javac.JavacASTVisitor
        public void endVisitMethodArgument(JavacNode node, JCTree.JCVariableDecl arg, JCTree.JCMethodDecl method) {
            this.indent--;
            print("</METHODARG %s %s>", arg.vartype, arg.name);
        }

        @Override // lombok.javac.JavacASTVisitor
        public void visitLocal(JavacNode node, JCTree.JCVariableDecl local) {
            print("<LOCAL %s %s>", local.vartype, local.name);
            this.indent++;
        }

        @Override // lombok.javac.JavacASTVisitor
        public void visitAnnotationOnLocal(JCTree.JCVariableDecl local, JavacNode node, JCTree.JCAnnotation annotation) {
            print("<ANNOTATION: %s />", annotation);
        }

        @Override // lombok.javac.JavacASTVisitor
        public void endVisitLocal(JavacNode node, JCTree.JCVariableDecl local) {
            this.indent--;
            print("</LOCAL %s %s>", local.vartype, local.name);
        }

        @Override // lombok.javac.JavacASTVisitor
        public void visitStatement(JavacNode node, JCTree statement) {
            print("<%s>", statement.getClass());
            this.indent++;
            print("%s", statement);
        }

        @Override // lombok.javac.JavacASTVisitor
        public void endVisitStatement(JavacNode node, JCTree statement) {
            this.indent--;
            print("</%s>", statement.getClass());
        }
    }
}
