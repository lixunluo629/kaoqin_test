package lombok.eclipse;

import java.io.PrintStream;
import lombok.eclipse.handlers.EclipseHandlerUtil;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Annotation;
import org.eclipse.jdt.internal.compiler.ast.Argument;
import org.eclipse.jdt.internal.compiler.ast.Block;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ConstructorDeclaration;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Initializer;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: lombok-1.16.22.jar:lombok/eclipse/EclipseASTVisitor.SCL.lombok */
public interface EclipseASTVisitor {
    void visitCompilationUnit(EclipseNode eclipseNode, CompilationUnitDeclaration compilationUnitDeclaration);

    void endVisitCompilationUnit(EclipseNode eclipseNode, CompilationUnitDeclaration compilationUnitDeclaration);

    void visitType(EclipseNode eclipseNode, TypeDeclaration typeDeclaration);

    void visitAnnotationOnType(TypeDeclaration typeDeclaration, EclipseNode eclipseNode, Annotation annotation);

    void endVisitType(EclipseNode eclipseNode, TypeDeclaration typeDeclaration);

    void visitField(EclipseNode eclipseNode, FieldDeclaration fieldDeclaration);

    void visitAnnotationOnField(FieldDeclaration fieldDeclaration, EclipseNode eclipseNode, Annotation annotation);

    void endVisitField(EclipseNode eclipseNode, FieldDeclaration fieldDeclaration);

    void visitInitializer(EclipseNode eclipseNode, Initializer initializer);

    void endVisitInitializer(EclipseNode eclipseNode, Initializer initializer);

    void visitMethod(EclipseNode eclipseNode, AbstractMethodDeclaration abstractMethodDeclaration);

    void visitAnnotationOnMethod(AbstractMethodDeclaration abstractMethodDeclaration, EclipseNode eclipseNode, Annotation annotation);

    void endVisitMethod(EclipseNode eclipseNode, AbstractMethodDeclaration abstractMethodDeclaration);

    void visitMethodArgument(EclipseNode eclipseNode, Argument argument, AbstractMethodDeclaration abstractMethodDeclaration);

    void visitAnnotationOnMethodArgument(Argument argument, AbstractMethodDeclaration abstractMethodDeclaration, EclipseNode eclipseNode, Annotation annotation);

    void endVisitMethodArgument(EclipseNode eclipseNode, Argument argument, AbstractMethodDeclaration abstractMethodDeclaration);

    void visitLocal(EclipseNode eclipseNode, LocalDeclaration localDeclaration);

    void visitAnnotationOnLocal(LocalDeclaration localDeclaration, EclipseNode eclipseNode, Annotation annotation);

    void endVisitLocal(EclipseNode eclipseNode, LocalDeclaration localDeclaration);

    void visitStatement(EclipseNode eclipseNode, Statement statement);

    void endVisitStatement(EclipseNode eclipseNode, Statement statement);

    /* loaded from: lombok-1.16.22.jar:lombok/eclipse/EclipseASTVisitor$Printer.SCL.lombok */
    public static class Printer implements EclipseASTVisitor {
        private final PrintStream out;
        private final boolean printContent;
        private int disablePrinting;
        private int indent;
        private boolean printClassNames;
        private final boolean printPositions;

        public boolean deferUntilPostDiet() {
            return false;
        }

        public Printer(boolean printContent) {
            this(printContent, System.out, false);
        }

        public Printer(boolean printContent, PrintStream out, boolean printPositions) {
            this.disablePrinting = 0;
            this.indent = 0;
            this.printClassNames = false;
            this.printContent = printContent;
            this.out = out;
            this.printPositions = printPositions;
        }

        private void forcePrint(String text, Object... params) {
            Object[] t;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < this.indent; i++) {
                sb.append("  ");
            }
            sb.append(text);
            if (this.printClassNames && params.length > 0) {
                sb.append(" [");
                for (int i2 = 0; i2 < params.length; i2++) {
                    if (i2 > 0) {
                        sb.append(", ");
                    }
                    sb.append("%s");
                }
                sb.append("]");
                t = new Object[params.length + params.length];
                for (int i3 = 0; i3 < params.length; i3++) {
                    t[i3] = params[i3];
                    t[i3 + params.length] = params[i3] == null ? "NULL " : params[i3].getClass();
                }
            } else {
                t = params;
            }
            sb.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
            this.out.printf(sb.toString(), t);
            this.out.flush();
        }

        private void print(String text, Object... params) {
            if (this.disablePrinting == 0) {
                forcePrint(text, params);
            }
        }

        private String str(char[] c) {
            return c == null ? "(NULL)" : new String(c);
        }

        private String str(TypeReference type) {
            if (type == null) {
                return "(NULL)";
            }
            char[][] c = type.getTypeName();
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (char[] d : c) {
                sb.append(first ? "" : ".").append(new String(d));
                first = false;
            }
            return sb.toString();
        }

        @Override // lombok.eclipse.EclipseASTVisitor
        public void visitCompilationUnit(EclipseNode node, CompilationUnitDeclaration unit) {
            this.out.println("---------------------------------------------------------");
            this.out.println(node.isCompleteParse() ? "COMPLETE" : "incomplete");
            Object[] objArr = new Object[3];
            objArr[0] = node.getFileName();
            objArr[1] = EclipseHandlerUtil.isGenerated(unit) ? " (GENERATED)" : "";
            objArr[2] = position(node);
            print("<CUD %s%s%s>", objArr);
            this.indent++;
        }

        @Override // lombok.eclipse.EclipseASTVisitor
        public void endVisitCompilationUnit(EclipseNode node, CompilationUnitDeclaration unit) {
            this.indent--;
            print("</CUD>", new Object[0]);
        }

        @Override // lombok.eclipse.EclipseASTVisitor
        public void visitType(EclipseNode node, TypeDeclaration type) {
            Object[] objArr = new Object[3];
            objArr[0] = str(type.name);
            objArr[1] = EclipseHandlerUtil.isGenerated(type) ? " (GENERATED)" : "";
            objArr[2] = position(node);
            print("<TYPE %s%s%s>", objArr);
            this.indent++;
            if (this.printContent) {
                print("%s", type);
                this.disablePrinting++;
            }
        }

        @Override // lombok.eclipse.EclipseASTVisitor
        public void visitAnnotationOnType(TypeDeclaration type, EclipseNode node, Annotation annotation) {
            Object[] objArr = new Object[3];
            objArr[0] = EclipseHandlerUtil.isGenerated(annotation) ? " (GENERATED)" : "";
            objArr[1] = annotation;
            objArr[2] = position(node);
            forcePrint("<ANNOTATION%s: %s%s />", objArr);
        }

        @Override // lombok.eclipse.EclipseASTVisitor
        public void endVisitType(EclipseNode node, TypeDeclaration type) {
            if (this.printContent) {
                this.disablePrinting--;
            }
            this.indent--;
            print("</TYPE %s>", str(type.name));
        }

        @Override // lombok.eclipse.EclipseASTVisitor
        public void visitInitializer(EclipseNode node, Initializer initializer) {
            Block block = initializer.block;
            boolean s = (block == null || block.statements == null) ? false : true;
            Object[] objArr = new Object[4];
            objArr[0] = (initializer.modifiers & 8) != 0 ? "static" : "instance";
            objArr[1] = s ? "filled" : "blank";
            objArr[2] = EclipseHandlerUtil.isGenerated(initializer) ? " (GENERATED)" : "";
            objArr[3] = position(node);
            print("<%s INITIALIZER: %s%s%s>", objArr);
            this.indent++;
            if (this.printContent) {
                if (initializer.block != null) {
                    print("%s", initializer.block);
                }
                this.disablePrinting++;
            }
        }

        @Override // lombok.eclipse.EclipseASTVisitor
        public void endVisitInitializer(EclipseNode node, Initializer initializer) {
            if (this.printContent) {
                this.disablePrinting--;
            }
            this.indent--;
            Object[] objArr = new Object[1];
            objArr[0] = (initializer.modifiers & 8) != 0 ? "static" : "instance";
            print("</%s INITIALIZER>", objArr);
        }

        @Override // lombok.eclipse.EclipseASTVisitor
        public void visitField(EclipseNode node, FieldDeclaration field) {
            Object[] objArr = new Object[5];
            objArr[0] = EclipseHandlerUtil.isGenerated(field) ? " (GENERATED)" : "";
            objArr[1] = str(field.type);
            objArr[2] = str(field.name);
            objArr[3] = field.initialization;
            objArr[4] = position(node);
            print("<FIELD%s %s %s = %s%s>", objArr);
            this.indent++;
            if (this.printContent) {
                if (field.initialization != null) {
                    print("%s", field.initialization);
                }
                this.disablePrinting++;
            }
        }

        @Override // lombok.eclipse.EclipseASTVisitor
        public void visitAnnotationOnField(FieldDeclaration field, EclipseNode node, Annotation annotation) {
            Object[] objArr = new Object[3];
            objArr[0] = EclipseHandlerUtil.isGenerated(annotation) ? " (GENERATED)" : "";
            objArr[1] = annotation;
            objArr[2] = position(node);
            forcePrint("<ANNOTATION%s: %s%s />", objArr);
        }

        @Override // lombok.eclipse.EclipseASTVisitor
        public void endVisitField(EclipseNode node, FieldDeclaration field) {
            if (this.printContent) {
                this.disablePrinting--;
            }
            this.indent--;
            print("</FIELD %s %s>", str(field.type), str(field.name));
        }

        @Override // lombok.eclipse.EclipseASTVisitor
        public void visitMethod(EclipseNode node, AbstractMethodDeclaration method) {
            String type = method instanceof ConstructorDeclaration ? "CONSTRUCTOR" : "METHOD";
            Object[] objArr = new Object[5];
            objArr[0] = type;
            objArr[1] = str(method.selector);
            objArr[2] = method.statements != null ? "filled" : "blank";
            objArr[3] = EclipseHandlerUtil.isGenerated(method) ? " (GENERATED)" : "";
            objArr[4] = position(node);
            print("<%s %s: %s%s%s>", objArr);
            this.indent++;
            if (this.printContent) {
                if (method.statements != null) {
                    print("%s", method);
                }
                this.disablePrinting++;
            }
        }

        @Override // lombok.eclipse.EclipseASTVisitor
        public void visitAnnotationOnMethod(AbstractMethodDeclaration method, EclipseNode node, Annotation annotation) {
            Object[] objArr = new Object[3];
            objArr[0] = EclipseHandlerUtil.isGenerated(method) ? " (GENERATED)" : "";
            objArr[1] = annotation;
            objArr[2] = position(node);
            forcePrint("<ANNOTATION%s: %s%s />", objArr);
        }

        @Override // lombok.eclipse.EclipseASTVisitor
        public void endVisitMethod(EclipseNode node, AbstractMethodDeclaration method) {
            if (this.printContent) {
                this.disablePrinting--;
            }
            String type = method instanceof ConstructorDeclaration ? "CONSTRUCTOR" : "METHOD";
            this.indent--;
            print("</%s %s>", type, str(method.selector));
        }

        @Override // lombok.eclipse.EclipseASTVisitor
        public void visitMethodArgument(EclipseNode node, Argument arg, AbstractMethodDeclaration method) {
            Object[] objArr = new Object[5];
            objArr[0] = EclipseHandlerUtil.isGenerated(arg) ? " (GENERATED)" : "";
            objArr[1] = str(arg.type);
            objArr[2] = str(arg.name);
            objArr[3] = arg.initialization;
            objArr[4] = position(node);
            print("<METHODARG%s %s %s = %s%s>", objArr);
            this.indent++;
        }

        @Override // lombok.eclipse.EclipseASTVisitor
        public void visitAnnotationOnMethodArgument(Argument arg, AbstractMethodDeclaration method, EclipseNode node, Annotation annotation) {
            Object[] objArr = new Object[3];
            objArr[0] = EclipseHandlerUtil.isGenerated(annotation) ? " (GENERATED)" : "";
            objArr[1] = annotation;
            objArr[2] = position(node);
            print("<ANNOTATION%s: %s%s />", objArr);
        }

        @Override // lombok.eclipse.EclipseASTVisitor
        public void endVisitMethodArgument(EclipseNode node, Argument arg, AbstractMethodDeclaration method) {
            this.indent--;
            print("</METHODARG %s %s>", str(arg.type), str(arg.name));
        }

        @Override // lombok.eclipse.EclipseASTVisitor
        public void visitLocal(EclipseNode node, LocalDeclaration local) {
            Object[] objArr = new Object[5];
            objArr[0] = EclipseHandlerUtil.isGenerated(local) ? " (GENERATED)" : "";
            objArr[1] = str(local.type);
            objArr[2] = str(local.name);
            objArr[3] = local.initialization;
            objArr[4] = position(node);
            print("<LOCAL%s %s %s = %s%s>", objArr);
            this.indent++;
        }

        @Override // lombok.eclipse.EclipseASTVisitor
        public void visitAnnotationOnLocal(LocalDeclaration local, EclipseNode node, Annotation annotation) {
            Object[] objArr = new Object[2];
            objArr[0] = EclipseHandlerUtil.isGenerated(annotation) ? " (GENERATED)" : "";
            objArr[1] = annotation;
            print("<ANNOTATION%s: %s />", objArr);
        }

        @Override // lombok.eclipse.EclipseASTVisitor
        public void endVisitLocal(EclipseNode node, LocalDeclaration local) {
            this.indent--;
            print("</LOCAL %s %s>", str(local.type), str(local.name));
        }

        @Override // lombok.eclipse.EclipseASTVisitor
        public void visitStatement(EclipseNode node, Statement statement) {
            Object[] objArr = new Object[3];
            objArr[0] = statement.getClass();
            objArr[1] = EclipseHandlerUtil.isGenerated(statement) ? " (GENERATED)" : "";
            objArr[2] = position(node);
            print("<%s%s%s>", objArr);
            this.indent++;
            print("%s", statement);
        }

        @Override // lombok.eclipse.EclipseASTVisitor
        public void endVisitStatement(EclipseNode node, Statement statement) {
            this.indent--;
            print("</%s>", statement.getClass());
        }

        String position(EclipseNode node) {
            if (!this.printPositions) {
                return "";
            }
            int start = node.get().sourceStart();
            int end = node.get().sourceEnd();
            return String.format(" [%d, %d]", Integer.valueOf(start), Integer.valueOf(end));
        }
    }
}
