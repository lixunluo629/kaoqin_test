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

/* loaded from: lombok-1.16.22.jar:lombok/patcher/scripts/WrapReturnValuesScript.SCL.lombok */
public final class WrapReturnValuesScript extends MethodLevelPatchScript {
    private final Hook wrapper;
    private final Set<StackRequest> requests;
    private final boolean hijackReturnValue;
    private final boolean transplant;
    private final boolean insert;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !WrapReturnValuesScript.class.desiredAssertionStatus();
    }

    WrapReturnValuesScript(List<TargetMatcher> matchers, Hook wrapper, boolean transplant, boolean insert, Set<StackRequest> requests) {
        super(matchers);
        if (wrapper == null) {
            throw new NullPointerException("wrapper");
        }
        this.wrapper = wrapper;
        this.hijackReturnValue = !wrapper.getMethodDescriptor().endsWith(")V");
        this.requests = requests;
        this.transplant = transplant;
        this.insert = insert;
        if (!$assertionsDisabled && insert && transplant) {
            throw new AssertionError();
        }
    }

    @Override // lombok.patcher.scripts.MethodLevelPatchScript
    protected PatchScript.MethodPatcher createPatcher(ClassWriter writer, final String classSpec, TransplantMapper transplantMapper) {
        PatchScript.MethodPatcher patcher = new PatchScript.MethodPatcher(writer, transplantMapper, new PatchScript.MethodPatcherFactory() { // from class: lombok.patcher.scripts.WrapReturnValuesScript.1
            @Override // lombok.patcher.PatchScript.MethodPatcherFactory
            public MethodVisitor createMethodVisitor(String name, String desc, MethodVisitor parent, MethodLogistics logistics) {
                return WrapReturnValuesScript.this.new WrapReturnValues(parent, logistics, classSpec);
            }
        });
        if (this.transplant) {
            patcher.addTransplant(this.wrapper);
        }
        return patcher;
    }

    /* loaded from: lombok-1.16.22.jar:lombok/patcher/scripts/WrapReturnValuesScript$WrapReturnValues.SCL.lombok */
    private class WrapReturnValues extends MethodVisitor {
        private final MethodLogistics logistics;
        private final String ownClassSpec;

        public WrapReturnValues(MethodVisitor mv, MethodLogistics logistics, String ownClassSpec) {
            super(262144, mv);
            this.logistics = logistics;
            this.ownClassSpec = ownClassSpec;
        }

        @Override // org.objectweb.asm.MethodVisitor
        public void visitInsn(int opcode) {
            if (opcode == this.logistics.getReturnOpcode()) {
                if (WrapReturnValuesScript.this.requests.contains(StackRequest.RETURN_VALUE)) {
                    if (!WrapReturnValuesScript.this.hijackReturnValue) {
                        this.logistics.generateDupForReturn(this.mv);
                    }
                } else if (WrapReturnValuesScript.this.hijackReturnValue) {
                    this.logistics.generatePopForReturn(this.mv);
                }
                if (WrapReturnValuesScript.this.requests.contains(StackRequest.THIS)) {
                    this.logistics.generateLoadOpcodeForThis(this.mv);
                }
                for (StackRequest param : StackRequest.PARAMS_IN_ORDER) {
                    if (WrapReturnValuesScript.this.requests.contains(param)) {
                        this.logistics.generateLoadOpcodeForParam(param.getParamPos(), this.mv);
                    }
                }
                if (WrapReturnValuesScript.this.insert) {
                    WrapReturnValuesScript.insertMethod(WrapReturnValuesScript.this.wrapper, this.mv);
                } else {
                    super.visitMethodInsn(184, WrapReturnValuesScript.this.transplant ? this.ownClassSpec : WrapReturnValuesScript.this.wrapper.getClassSpec(), WrapReturnValuesScript.this.wrapper.getMethodName(), WrapReturnValuesScript.this.wrapper.getMethodDescriptor(), false);
                }
                super.visitInsn(opcode);
                return;
            }
            super.visitInsn(opcode);
        }
    }

    public String toString() {
        return "WrapReturnValues(wrapper: " + this.wrapper + ", hijackReturn: " + this.hijackReturnValue + ", transplant: " + this.transplant + ", insert: " + this.insert + ", requests: " + this.requests + ")";
    }
}
