package lombok.patcher.scripts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.patcher.Hook;
import lombok.patcher.MethodLogistics;
import lombok.patcher.PatchScript;
import lombok.patcher.TargetMatcher;
import lombok.patcher.TransplantMapper;
import org.hyperic.sigar.NetFlags;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: lombok-1.16.22.jar:lombok/patcher/scripts/SetSymbolDuringMethodCallScript.SCL.lombok */
public class SetSymbolDuringMethodCallScript extends MethodLevelPatchScript {
    private final Hook callToWrap;
    private final String symbol;
    private final boolean report;

    SetSymbolDuringMethodCallScript(List<TargetMatcher> matchers, Hook callToWrap, String symbol, boolean report) {
        super(matchers);
        if (callToWrap == null) {
            throw new NullPointerException("callToWrap");
        }
        if (symbol == null) {
            throw new NullPointerException("symbol");
        }
        this.callToWrap = callToWrap;
        this.symbol = symbol;
        this.report = report;
    }

    @Override // lombok.patcher.scripts.MethodLevelPatchScript
    protected PatchScript.MethodPatcher createPatcher(ClassWriter writer, final String classSpec, TransplantMapper transplantMapper) {
        final List<WrapperMethodDescriptor> descriptors = new ArrayList<>();
        PatchScript.MethodPatcher patcher = new PatchScript.MethodPatcher(writer, transplantMapper, new PatchScript.MethodPatcherFactory() { // from class: lombok.patcher.scripts.SetSymbolDuringMethodCallScript.1
            @Override // lombok.patcher.PatchScript.MethodPatcherFactory
            public MethodVisitor createMethodVisitor(String name, String desc, MethodVisitor parent, MethodLogistics logistics) {
                return SetSymbolDuringMethodCallScript.this.new WrapWithSymbol(name, parent, classSpec, descriptors);
            }
        }) { // from class: lombok.patcher.scripts.SetSymbolDuringMethodCallScript.2
            @Override // lombok.patcher.PatchScript.MethodPatcher, org.objectweb.asm.ClassVisitor
            public void visitEnd() throws IOException {
                for (WrapperMethodDescriptor wmd : descriptors) {
                    SetSymbolDuringMethodCallScript.this.makeWrapperMethod(this, wmd);
                }
                super.visitEnd();
            }
        };
        return patcher;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void makeWrapperMethod(ClassVisitor cv, WrapperMethodDescriptor wmd) {
        MethodVisitor mv = cv.visitMethod(4106, wmd.getWrapperName(), wmd.getWrapperDescriptor(), null, null);
        MethodLogistics logistics = new MethodLogistics(8, wmd.getWrapperDescriptor());
        mv.visitCode();
        Label start = new Label();
        Label end = new Label();
        Label handler = new Label();
        mv.visitTryCatchBlock(start, end, handler, null);
        mv.visitLabel(start);
        mv.visitLdcInsn(this.symbol);
        mv.visitMethodInsn(184, "lombok/patcher/Symbols", "push", "(Ljava/lang/String;)V", false);
        for (int i = 0; i < logistics.getParamCount(); i++) {
            logistics.generateLoadOpcodeForParam(i, mv);
        }
        mv.visitMethodInsn(wmd.getOpcode(), wmd.getOwner(), wmd.getName(), wmd.getTargetDescriptor(), wmd.isItf());
        mv.visitLabel(end);
        mv.visitMethodInsn(184, "lombok/patcher/Symbols", "pop", "()V", false);
        logistics.generateReturnOpcode(mv);
        mv.visitLabel(handler);
        mv.visitFrame(0, 0, null, 1, new Object[]{"java/lang/Throwable"});
        mv.visitMethodInsn(184, "lombok/patcher/Symbols", "pop", "()V", false);
        mv.visitInsn(191);
        mv.visitMaxs(Math.max(1, logistics.getParamCount()), logistics.getParamCount());
        mv.visitEnd();
    }

    /* loaded from: lombok-1.16.22.jar:lombok/patcher/scripts/SetSymbolDuringMethodCallScript$WrapWithSymbol.SCL.lombok */
    private class WrapWithSymbol extends MethodVisitor {
        private final String selfMethodName;
        private final String selfTypeName;
        private final List<WrapperMethodDescriptor> descriptors;

        public WrapWithSymbol(String selfMethodName, MethodVisitor mv, String selfTypeName, List<WrapperMethodDescriptor> descriptors) {
            super(262144, mv);
            this.selfMethodName = selfMethodName;
            this.selfTypeName = selfTypeName;
            this.descriptors = descriptors;
        }

        @Override // org.objectweb.asm.MethodVisitor
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
            boolean addOwner;
            String fixedDesc;
            if (opcode == 185 || opcode == 182) {
                addOwner = true;
            } else {
                if (opcode != 184) {
                    super.visitMethodInsn(opcode, owner, name, desc, itf);
                    return;
                }
                addOwner = false;
            }
            if (!SetSymbolDuringMethodCallScript.this.callToWrap.getClassSpec().equals(owner) || !SetSymbolDuringMethodCallScript.this.callToWrap.getMethodName().equals(name) || !SetSymbolDuringMethodCallScript.this.callToWrap.getMethodDescriptor().equals(desc)) {
                super.visitMethodInsn(opcode, owner, name, desc, itf);
                return;
            }
            if (addOwner) {
                fixedDesc = "(L" + SetSymbolDuringMethodCallScript.this.callToWrap.getClassSpec() + ScriptUtils.DEFAULT_STATEMENT_SEPARATOR + desc.substring(1);
            } else {
                fixedDesc = desc;
            }
            WrapperMethodDescriptor wmd = new WrapperMethodDescriptor(this.descriptors.size(), opcode, owner, name, fixedDesc, desc, itf);
            if (SetSymbolDuringMethodCallScript.this.report) {
                System.out.println("Changing method " + this.selfTypeName + NetFlags.ANY_ADDR_V6 + this.selfMethodName + " wrapping call to " + owner + NetFlags.ANY_ADDR_V6 + name + " to set symbol " + SetSymbolDuringMethodCallScript.this.symbol);
            }
            super.visitMethodInsn(184, this.selfTypeName, wmd.getWrapperName(), fixedDesc, false);
            this.descriptors.add(wmd);
        }
    }
}
