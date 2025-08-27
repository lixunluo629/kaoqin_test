package lombok.eclipse.handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import lombok.Lombok;
import lombok.core.AnnotationValues;
import lombok.core.HandlerPriority;
import lombok.core.PrintAST;
import lombok.eclipse.DeferUntilPostDiet;
import lombok.eclipse.EclipseASTVisitor;
import lombok.eclipse.EclipseAnnotationHandler;
import lombok.eclipse.EclipseNode;
import org.eclipse.jdt.internal.compiler.ast.Annotation;

@DeferUntilPostDiet
@HandlerPriority(536870912)
/* loaded from: lombok-1.16.22.jar:lombok/eclipse/handlers/HandlePrintAST.SCL.lombok */
public class HandlePrintAST extends EclipseAnnotationHandler<PrintAST> {
    @Override // lombok.eclipse.EclipseAnnotationHandler
    public void handle(AnnotationValues<PrintAST> annotation, Annotation ast, EclipseNode annotationNode) {
        PrintStream stream = System.out;
        String fileName = ((PrintAST) annotation.getInstance()).outfile();
        if (fileName.length() > 0) {
            try {
                stream = new PrintStream(new File(fileName));
            } catch (FileNotFoundException e) {
                throw Lombok.sneakyThrow(e);
            }
        }
        try {
            annotationNode.up().traverse(new EclipseASTVisitor.Printer(((PrintAST) annotation.getInstance()).printContent(), stream, ((PrintAST) annotation.getInstance()).printPositions()));
            if (stream != System.out) {
                try {
                    stream.close();
                } catch (Exception e2) {
                    throw Lombok.sneakyThrow(e2);
                }
            }
        } catch (Throwable th) {
            if (stream != System.out) {
                try {
                    stream.close();
                } catch (Exception e3) {
                    throw Lombok.sneakyThrow(e3);
                }
            }
            throw th;
        }
    }
}
