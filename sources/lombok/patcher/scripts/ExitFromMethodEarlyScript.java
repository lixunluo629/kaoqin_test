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
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

/* loaded from: lombok-1.16.22.jar:lombok/patcher/scripts/ExitFromMethodEarlyScript.SCL.lombok */
public class ExitFromMethodEarlyScript extends MethodLevelPatchScript {
    private final Hook decisionWrapper;
    private final Hook valueWrapper;
    private final Set<StackRequest> requests;
    private final boolean transplant;
    private final boolean insert;
    private final boolean insertCallOnly;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ExitFromMethodEarlyScript.class.desiredAssertionStatus();
    }

    ExitFromMethodEarlyScript(List<TargetMatcher> matchers, Hook decisionWrapper, Hook valueWrapper, boolean transplant, boolean insert, Set<StackRequest> requests) {
        super(matchers);
        this.decisionWrapper = decisionWrapper;
        this.valueWrapper = valueWrapper;
        this.requests = requests;
        this.transplant = transplant;
        this.insert = insert;
        this.insertCallOnly = decisionWrapper != null && decisionWrapper.getMethodDescriptor().endsWith(")V");
        if (!this.insertCallOnly && decisionWrapper != null && !decisionWrapper.getMethodDescriptor().endsWith(")Z")) {
            throw new IllegalArgumentException("The decisionWrapper method must either return 'boolean' or return 'void'.");
        }
        if (!$assertionsDisabled && insert && transplant) {
            throw new AssertionError();
        }
    }

    @Override // lombok.patcher.scripts.MethodLevelPatchScript
    protected PatchScript.MethodPatcher createPatcher(ClassWriter writer, final String classSpec, TransplantMapper transplantMapper) {
        PatchScript.MethodPatcher patcher = new PatchScript.MethodPatcher(writer, transplantMapper, new PatchScript.MethodPatcherFactory() { // from class: lombok.patcher.scripts.ExitFromMethodEarlyScript.1
            @Override // lombok.patcher.PatchScript.MethodPatcherFactory
            public MethodVisitor createMethodVisitor(String name, String desc, MethodVisitor parent, MethodLogistics logistics) {
                if (ExitFromMethodEarlyScript.this.valueWrapper == null && !ExitFromMethodEarlyScript.this.insertCallOnly && logistics.getReturnOpcode() != 177) {
                    throw new IllegalStateException("method " + name + desc + " must return something, but you did not provide a value hook method.");
                }
                return ExitFromMethodEarlyScript.this.new ExitEarly(parent, logistics, classSpec);
            }
        });
        if (this.transplant) {
            patcher.addTransplant(this.decisionWrapper);
            if (this.valueWrapper != null) {
                patcher.addTransplant(this.valueWrapper);
            }
        }
        return patcher;
    }

    /* loaded from: lombok-1.16.22.jar:lombok/patcher/scripts/ExitFromMethodEarlyScript$ExitEarly.SCL.lombok */
    private class ExitEarly extends MethodVisitor {
        private final MethodLogistics logistics;
        private final String ownClassSpec;

        public ExitEarly(MethodVisitor mv, MethodLogistics logistics, String ownClassSpec) {
            super(262144, mv);
            this.logistics = logistics;
            this.ownClassSpec = ownClassSpec;
        }

        @Override // org.objectweb.asm.MethodVisitor
        public void visitCode() {
            if (ExitFromMethodEarlyScript.this.decisionWrapper != null) {
                if (ExitFromMethodEarlyScript.this.requests.contains(StackRequest.THIS)) {
                    this.logistics.generateLoadOpcodeForThis(this.mv);
                }
                for (StackRequest param : StackRequest.PARAMS_IN_ORDER) {
                    if (ExitFromMethodEarlyScript.this.requests.contains(param)) {
                        this.logistics.generateLoadOpcodeForParam(param.getParamPos(), this.mv);
                    }
                }
                if (ExitFromMethodEarlyScript.this.insert) {
                    ExitFromMethodEarlyScript.insertMethod(ExitFromMethodEarlyScript.this.decisionWrapper, this.mv);
                } else {
                    super.visitMethodInsn(184, ExitFromMethodEarlyScript.this.transplant ? this.ownClassSpec : ExitFromMethodEarlyScript.this.decisionWrapper.getClassSpec(), ExitFromMethodEarlyScript.this.decisionWrapper.getMethodName(), ExitFromMethodEarlyScript.this.decisionWrapper.getMethodDescriptor(), false);
                }
                if (ExitFromMethodEarlyScript.this.insertCallOnly) {
                    super.visitCode();
                    return;
                }
                Label label0 = new Label();
                this.mv.visitJumpInsn(153, label0);
                if (this.logistics.getReturnOpcode() != 177) {
                    if (ExitFromMethodEarlyScript.this.requests.contains(StackRequest.THIS)) {
                        this.logistics.generateLoadOpcodeForThis(this.mv);
                    }
                    for (StackRequest param2 : StackRequest.PARAMS_IN_ORDER) {
                        if (ExitFromMethodEarlyScript.this.requests.contains(param2)) {
                            this.logistics.generateLoadOpcodeForParam(param2.getParamPos(), this.mv);
                        }
                    }
                    if (ExitFromMethodEarlyScript.this.insert) {
                        ExitFromMethodEarlyScript.insertMethod(ExitFromMethodEarlyScript.this.valueWrapper, this.mv);
                    } else {
                        super.visitMethodInsn(184, ExitFromMethodEarlyScript.this.transplant ? this.ownClassSpec : ExitFromMethodEarlyScript.this.valueWrapper.getClassSpec(), ExitFromMethodEarlyScript.this.valueWrapper.getMethodName(), ExitFromMethodEarlyScript.this.valueWrapper.getMethodDescriptor(), false);
                    }
                    this.logistics.generateReturnOpcode(this.mv);
                } else {
                    this.mv.visitInsn(177);
                }
                this.mv.visitLabel(label0);
                this.mv.visitFrame(3, 0, null, 0, null);
                super.visitCode();
                return;
            }
            if (this.logistics.getReturnOpcode() == 177) {
                this.mv.visitInsn(177);
            } else {
                insertValueWrapperCall();
            }
        }

        private void insertValueWrapperCall() {
            if (ExitFromMethodEarlyScript.this.requests.contains(StackRequest.THIS)) {
                this.logistics.generateLoadOpcodeForThis(this.mv);
            }
            for (StackRequest param : StackRequest.PARAMS_IN_ORDER) {
                if (ExitFromMethodEarlyScript.this.requests.contains(param)) {
                    this.logistics.generateLoadOpcodeForParam(param.getParamPos(), this.mv);
                }
            }
            if (ExitFromMethodEarlyScript.this.insert) {
                ExitFromMethodEarlyScript.insertMethod(ExitFromMethodEarlyScript.this.valueWrapper, this.mv);
            } else {
                super.visitMethodInsn(184, ExitFromMethodEarlyScript.this.transplant ? this.ownClassSpec : ExitFromMethodEarlyScript.this.valueWrapper.getClassSpec(), ExitFromMethodEarlyScript.this.valueWrapper.getMethodName(), ExitFromMethodEarlyScript.this.valueWrapper.getMethodDescriptor(), false);
            }
            this.logistics.generateReturnOpcode(this.mv);
        }
    }
}
