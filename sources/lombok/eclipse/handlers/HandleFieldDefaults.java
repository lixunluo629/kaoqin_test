package lombok.eclipse.handlers;

import java.lang.annotation.Annotation;
import java.util.Iterator;
import lombok.AccessLevel;
import lombok.core.AST;
import lombok.core.HandlerPriority;
import lombok.eclipse.EclipseASTAdapter;
import lombok.eclipse.EclipseNode;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.experimental.PackagePrivate;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;

@HandlerPriority(-2048)
/* loaded from: lombok-1.16.22.jar:lombok/eclipse/handlers/HandleFieldDefaults.SCL.lombok */
public class HandleFieldDefaults extends EclipseASTAdapter {
    private static final char[] FIELD_DEFAULTS = "FieldDefaults".toCharArray();

    public boolean generateFieldDefaultsForType(EclipseNode typeNode, EclipseNode pos, AccessLevel level, boolean makeFinal, boolean checkForTypeLevelFieldDefaults) {
        if (checkForTypeLevelFieldDefaults && EclipseHandlerUtil.hasAnnotation((Class<? extends Annotation>) FieldDefaults.class, typeNode)) {
            return true;
        }
        TypeDeclaration typeDecl = null;
        if (typeNode.get() instanceof TypeDeclaration) {
            typeDecl = (TypeDeclaration) typeNode.get();
        }
        int modifiers = typeDecl == null ? 0 : typeDecl.modifiers;
        boolean notAClass = (modifiers & 8704) != 0;
        if (typeDecl == null || notAClass) {
            pos.addError("@FieldDefaults is only supported on a class or an enum.");
            return false;
        }
        Iterator<EclipseNode> it = typeNode.down().iterator();
        while (it.hasNext()) {
            EclipseNode field = it.next();
            if (field.getKind() == AST.Kind.FIELD) {
                FieldDeclaration fieldDecl = field.get();
                if (EclipseHandlerUtil.filterField(fieldDecl, false)) {
                    Class<?> t = field.get().getClass();
                    if (t == FieldDeclaration.class) {
                        setFieldDefaultsForField(field, pos.get(), level, makeFinal);
                    }
                }
            }
        }
        return true;
    }

    public void setFieldDefaultsForField(EclipseNode fieldNode, ASTNode pos, AccessLevel level, boolean makeFinal) {
        FieldDeclaration field = fieldNode.get();
        if (level != null && level != AccessLevel.NONE && (field.modifiers & 7) == 0 && !EclipseHandlerUtil.hasAnnotation((Class<? extends Annotation>) PackagePrivate.class, fieldNode) && (field.modifiers & 8) == 0) {
            field.modifiers |= EclipseHandlerUtil.toEclipseModifier(level);
        }
        if (makeFinal && (field.modifiers & 16) == 0 && !EclipseHandlerUtil.hasAnnotation((Class<? extends Annotation>) NonFinal.class, fieldNode) && (field.modifiers & 8) == 0) {
            field.modifiers |= 16;
        }
        fieldNode.rebuild();
    }

    /* JADX WARN: Removed duplicated region for block: B:76:0x009b A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:85:0x00fa A[SYNTHETIC] */
    @Override // lombok.eclipse.EclipseASTAdapter, lombok.eclipse.EclipseASTVisitor
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void visitType(lombok.eclipse.EclipseNode r8, org.eclipse.jdt.internal.compiler.ast.TypeDeclaration r9) throws java.lang.SecurityException {
        /*
            Method dump skipped, instructions count: 422
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: lombok.eclipse.handlers.HandleFieldDefaults.visitType(lombok.eclipse.EclipseNode, org.eclipse.jdt.internal.compiler.ast.TypeDeclaration):void");
    }
}
