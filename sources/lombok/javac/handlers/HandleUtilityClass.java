package lombok.javac.handlers;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import lombok.ConfigurationKeys;
import lombok.core.AST;
import lombok.core.AnnotationValues;
import lombok.core.HandlerPriority;
import lombok.core.handlers.HandlerUtil;
import lombok.experimental.UtilityClass;
import lombok.javac.Javac;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;
import org.apache.poi.ddf.EscherContainerRecord;

@HandlerPriority(EscherContainerRecord.DGG_CONTAINER)
/* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/HandleUtilityClass.SCL.lombok */
public class HandleUtilityClass extends JavacAnnotationHandler<UtilityClass> {
    @Override // lombok.javac.JavacAnnotationHandler
    public void handle(AnnotationValues<UtilityClass> annotation, JCTree.JCAnnotation ast, JavacNode annotationNode) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        HandlerUtil.handleExperimentalFlagUsage(annotationNode, ConfigurationKeys.UTILITY_CLASS_FLAG_USAGE, "@UtilityClass");
        JavacHandlerUtil.deleteAnnotationIfNeccessary(annotationNode, (Class<? extends Annotation>) UtilityClass.class);
        JavacNode typeNode = annotationNode.up();
        if (checkLegality(typeNode, annotationNode)) {
            changeModifiersAndGenerateConstructor(annotationNode.up(), annotationNode);
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:200)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:61)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:103)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:101)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    private static boolean checkLegality(lombok.javac.JavacNode r5, lombok.javac.JavacNode r6) {
        /*
            r0 = 0
            r7 = r0
            r0 = r5
            java.lang.Object r0 = r0.get()
            boolean r0 = r0 instanceof com.sun.tools.javac.tree.JCTree.JCClassDecl
            if (r0 == 0) goto L14
            r0 = r5
            java.lang.Object r0 = r0.get()
            com.sun.tools.javac.tree.JCTree$JCClassDecl r0 = (com.sun.tools.javac.tree.JCTree.JCClassDecl) r0
            r7 = r0
        L14:
            r0 = r7
            if (r0 != 0) goto L1c
            r0 = 0
            goto L23
        L1c:
            r0 = r7
            com.sun.tools.javac.tree.JCTree$JCModifiers r0 = r0.mods
            long r0 = r0.flags
        L23:
            r8 = r0
            r0 = r8
            r1 = 25088(0x6200, double:1.2395E-319)
            long r0 = r0 & r1
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 == 0) goto L32
            r0 = 1
            goto L33
        L32:
            r0 = 0
        L33:
            r10 = r0
            r0 = r7
            if (r0 == 0) goto L3e
            r0 = r10
            if (r0 == 0) goto L46
        L3e:
            r0 = r6
            java.lang.String r1 = "@UtilityClass is only supported on a class (can't be an interface, enum, or annotation)."
            r0.addError(r1)
            r0 = 0
            return r0
        L46:
            r0 = r5
            r11 = r0
        L49:
            r0 = r11
            lombok.core.LombokNode r0 = r0.up()
            lombok.javac.JavacNode r0 = (lombok.javac.JavacNode) r0
            r11 = r0
            int[] r0 = lombok.javac.handlers.HandleUtilityClass.AnonymousClass1.$SwitchMap$lombok$core$AST$Kind
            r1 = r11
            lombok.core.AST$Kind r1 = r1.getKind()
            int r1 = r1.ordinal()
            r0 = r0[r1]
            switch(r0) {
                case 1: goto L78;
                case 2: goto Lb1;
                default: goto Lb3;
            }
        L78:
            r0 = r11
            java.lang.Object r0 = r0.get()
            com.sun.tools.javac.tree.JCTree$JCClassDecl r0 = (com.sun.tools.javac.tree.JCTree.JCClassDecl) r0
            r12 = r0
            r0 = r12
            com.sun.tools.javac.tree.JCTree$JCModifiers r0 = r0.mods
            long r0 = r0.flags
            r1 = 25096(0x6208, double:1.2399E-319)
            long r0 = r0 & r1
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 == 0) goto L96
            goto L49
        L96:
            r0 = r11
            lombok.core.LombokNode r0 = r0.up()
            lombok.javac.JavacNode r0 = (lombok.javac.JavacNode) r0
            lombok.core.AST$Kind r0 = r0.getKind()
            lombok.core.AST$Kind r1 = lombok.core.AST.Kind.COMPILATION_UNIT
            if (r0 != r1) goto La9
            r0 = 1
            return r0
        La9:
            r0 = r6
            java.lang.String r1 = "@UtilityClass automatically makes the class static, however, this class cannot be made static."
            r0.addError(r1)
            r0 = 0
            return r0
        Lb1:
            r0 = 1
            return r0
        Lb3:
            r0 = r6
            java.lang.String r1 = "@UtilityClass cannot be placed on a method local or anonymous inner class, or any class nested in such a class."
            r0.addError(r1)
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: lombok.javac.handlers.HandleUtilityClass.checkLegality(lombok.javac.JavacNode, lombok.javac.JavacNode):boolean");
    }

    /* renamed from: lombok.javac.handlers.HandleUtilityClass$1, reason: invalid class name */
    /* loaded from: lombok-1.16.22.jar:lombok/javac/handlers/HandleUtilityClass$1.SCL.lombok */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$lombok$core$AST$Kind = new int[AST.Kind.valuesCustom().length];

        static {
            try {
                $SwitchMap$lombok$core$AST$Kind[AST.Kind.TYPE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$lombok$core$AST$Kind[AST.Kind.COMPILATION_UNIT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    private void changeModifiersAndGenerateConstructor(JavacNode typeNode, JavacNode errorNode) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        JCTree.JCClassDecl classDecl = typeNode.get();
        boolean makeConstructor = true;
        classDecl.mods.flags |= 16;
        boolean markStatic = true;
        if (typeNode.up().getKind() == AST.Kind.COMPILATION_UNIT) {
            markStatic = false;
        }
        if (markStatic && typeNode.up().getKind() == AST.Kind.TYPE) {
            JCTree.JCClassDecl typeDecl = typeNode.up().get();
            if ((typeDecl.mods.flags & 512) != 0) {
                markStatic = false;
            }
        }
        if (markStatic) {
            classDecl.mods.flags |= 8;
        }
        Iterator<JavacNode> it = typeNode.down().iterator();
        while (it.hasNext()) {
            JavacNode element = it.next();
            if (element.getKind() == AST.Kind.FIELD) {
                JCTree.JCVariableDecl fieldDecl = element.get();
                fieldDecl.mods.flags |= 8;
            } else if (element.getKind() == AST.Kind.METHOD) {
                JCTree.JCMethodDecl methodDecl = element.get();
                if (methodDecl.name.contentEquals("<init>") && JavacHandlerUtil.getGeneratedBy(methodDecl) == null && (methodDecl.mods.flags & 68719476736L) == 0) {
                    element.addError("@UtilityClasses cannot have declared constructors.");
                    makeConstructor = false;
                } else {
                    methodDecl.mods.flags |= 8;
                }
            } else if (element.getKind() == AST.Kind.TYPE) {
                JCTree.JCClassDecl innerClassDecl = element.get();
                innerClassDecl.mods.flags |= 8;
            }
        }
        if (makeConstructor) {
            createPrivateDefaultConstructor(typeNode);
        }
    }

    private void createPrivateDefaultConstructor(JavacNode typeNode) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        JavacTreeMaker maker = typeNode.getTreeMaker();
        JCTree.JCModifiers mods = maker.Modifiers(2L, List.nil());
        Name name = typeNode.toName("<init>");
        JCTree.JCBlock block = maker.Block(0L, createThrowStatement(typeNode, maker));
        JCTree.JCMethodDecl methodDef = maker.MethodDef(mods, name, null, List.nil(), List.nil(), List.nil(), block, null);
        JCTree.JCMethodDecl constructor = JavacHandlerUtil.recursiveSetGeneratedBy(methodDef, typeNode.get(), typeNode.getContext());
        JavacHandlerUtil.injectMethod(typeNode, constructor, List.nil(), Javac.createVoidType(typeNode.getSymbolTable(), Javac.CTC_VOID));
    }

    private List<JCTree.JCStatement> createThrowStatement(JavacNode typeNode, JavacTreeMaker maker) {
        JCTree.JCExpression exceptionType = JavacHandlerUtil.genJavaLangTypeRef(typeNode, "UnsupportedOperationException");
        List<JCTree.JCExpression> jceBlank = List.nil();
        return List.of(maker.Throw(maker.NewClass(null, jceBlank, exceptionType, List.of(maker.Literal("This is a utility class and cannot be instantiated")), null)));
    }
}
