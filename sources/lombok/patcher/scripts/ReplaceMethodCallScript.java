package lombok.patcher.scripts;

import java.util.List;
import java.util.Set;
import lombok.patcher.Hook;
import lombok.patcher.MethodLogistics;
import lombok.patcher.PatchScript;
import lombok.patcher.StackRequest;
import lombok.patcher.TargetMatcher;
import lombok.patcher.TransplantMapper;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

/* loaded from: lombok-1.16.22.jar:lombok/patcher/scripts/ReplaceMethodCallScript.SCL.lombok */
public class ReplaceMethodCallScript extends MethodLevelPatchScript {
    private final Hook wrapper;
    private final Hook methodToReplace;
    private final boolean transplant;
    private final boolean insert;
    private final Set<StackRequest> extraRequests;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ReplaceMethodCallScript.class.desiredAssertionStatus();
    }

    ReplaceMethodCallScript(List<TargetMatcher> matchers, Hook callToReplace, Hook wrapper, boolean transplant, boolean insert, Set<StackRequest> extraRequests) {
        super(matchers);
        if (callToReplace == null) {
            throw new NullPointerException("callToReplace");
        }
        if (wrapper == null) {
            throw new NullPointerException("wrapper");
        }
        this.methodToReplace = callToReplace;
        this.wrapper = wrapper;
        this.transplant = transplant;
        this.insert = insert;
        if (!$assertionsDisabled && insert && transplant) {
            throw new AssertionError();
        }
        this.extraRequests = extraRequests;
    }

    @Override // lombok.patcher.scripts.MethodLevelPatchScript
    protected PatchScript.MethodPatcher createPatcher(ClassWriter writer, final String classSpec, TransplantMapper transplantMapper) {
        PatchScript.MethodPatcher patcher = new PatchScript.MethodPatcher(writer, transplantMapper, new PatchScript.MethodPatcherFactory() { // from class: lombok.patcher.scripts.ReplaceMethodCallScript.1
            @Override // lombok.patcher.PatchScript.MethodPatcherFactory
            public MethodVisitor createMethodVisitor(String name, String desc, MethodVisitor parent, MethodLogistics logistics) {
                return ReplaceMethodCallScript.this.new ReplaceMethodCall(parent, classSpec, logistics);
            }
        });
        if (this.transplant) {
            patcher.addTransplant(this.wrapper);
        }
        return patcher;
    }

    /* loaded from: lombok-1.16.22.jar:lombok/patcher/scripts/ReplaceMethodCallScript$ReplaceMethodCall.SCL.lombok */
    private class ReplaceMethodCall extends MethodVisitor {
        private final String ownClassSpec;
        private final MethodLogistics logistics;

        public ReplaceMethodCall(MethodVisitor mv, String ownClassSpec, MethodLogistics logistics) {
            super(262144, mv);
            this.ownClassSpec = ownClassSpec;
            this.logistics = logistics;
        }

        @Override // org.objectweb.asm.MethodVisitor
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
            if (ReplaceMethodCallScript.this.methodToReplace.getClassSpec().equals(owner) && ReplaceMethodCallScript.this.methodToReplace.getMethodName().equals(name) && ReplaceMethodCallScript.this.methodToReplace.getMethodDescriptor().equals(desc)) {
                if (ReplaceMethodCallScript.this.extraRequests.contains(StackRequest.THIS)) {
                    this.logistics.generateLoadOpcodeForThis(this.mv);
                }
                for (StackRequest param : StackRequest.PARAMS_IN_ORDER) {
                    if (ReplaceMethodCallScript.this.extraRequests.contains(param)) {
                        this.logistics.generateLoadOpcodeForParam(param.getParamPos(), this.mv);
                    }
                }
                if (ReplaceMethodCallScript.this.insert) {
                    ReplaceMethodCallScript.insertMethod(ReplaceMethodCallScript.this.wrapper, this.mv);
                    return;
                } else {
                    super.visitMethodInsn(184, ReplaceMethodCallScript.this.transplant ? this.ownClassSpec : ReplaceMethodCallScript.this.wrapper.getClassSpec(), ReplaceMethodCallScript.this.wrapper.getMethodName(), ReplaceMethodCallScript.this.wrapper.getMethodDescriptor(), itf);
                    return;
                }
            }
            super.visitMethodInsn(opcode, owner, name, desc, itf);
        }
    }
}
