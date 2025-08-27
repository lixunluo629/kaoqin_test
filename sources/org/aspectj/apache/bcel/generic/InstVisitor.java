package org.aspectj.apache.bcel.generic;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/InstVisitor.class */
public interface InstVisitor {
    void visitStackInstruction(Instruction instruction);

    void visitLocalVariableInstruction(InstructionLV instructionLV);

    void visitBranchInstruction(InstructionBranch instructionBranch);

    void visitLoadClass(Instruction instruction);

    void visitFieldInstruction(Instruction instruction);

    void visitIfInstruction(Instruction instruction);

    void visitConversionInstruction(Instruction instruction);

    void visitPopInstruction(Instruction instruction);

    void visitStoreInstruction(Instruction instruction);

    void visitTypedInstruction(Instruction instruction);

    void visitSelect(InstructionSelect instructionSelect);

    void visitJsrInstruction(InstructionBranch instructionBranch);

    void visitGotoInstruction(Instruction instruction);

    void visitUnconditionalBranch(Instruction instruction);

    void visitPushInstruction(Instruction instruction);

    void visitArithmeticInstruction(Instruction instruction);

    void visitCPInstruction(Instruction instruction);

    void visitInvokeInstruction(InvokeInstruction invokeInstruction);

    void visitArrayInstruction(Instruction instruction);

    void visitAllocationInstruction(Instruction instruction);

    void visitReturnInstruction(Instruction instruction);

    void visitFieldOrMethod(Instruction instruction);

    void visitConstantPushInstruction(Instruction instruction);

    void visitExceptionThrower(Instruction instruction);

    void visitLoadInstruction(Instruction instruction);

    void visitVariableLengthInstruction(Instruction instruction);

    void visitStackProducer(Instruction instruction);

    void visitStackConsumer(Instruction instruction);

    void visitACONST_NULL(Instruction instruction);

    void visitGETSTATIC(FieldInstruction fieldInstruction);

    void visitIF_ICMPLT(Instruction instruction);

    void visitMONITOREXIT(Instruction instruction);

    void visitIFLT(Instruction instruction);

    void visitLSTORE(Instruction instruction);

    void visitPOP2(Instruction instruction);

    void visitBASTORE(Instruction instruction);

    void visitISTORE(Instruction instruction);

    void visitCHECKCAST(Instruction instruction);

    void visitFCMPG(Instruction instruction);

    void visitI2F(Instruction instruction);

    void visitATHROW(Instruction instruction);

    void visitDCMPL(Instruction instruction);

    void visitARRAYLENGTH(Instruction instruction);

    void visitDUP(Instruction instruction);

    void visitINVOKESTATIC(InvokeInstruction invokeInstruction);

    void visitLCONST(Instruction instruction);

    void visitDREM(Instruction instruction);

    void visitIFGE(Instruction instruction);

    void visitCALOAD(Instruction instruction);

    void visitLASTORE(Instruction instruction);

    void visitI2D(Instruction instruction);

    void visitDADD(Instruction instruction);

    void visitINVOKESPECIAL(InvokeInstruction invokeInstruction);

    void visitIAND(Instruction instruction);

    void visitPUTFIELD(FieldInstruction fieldInstruction);

    void visitILOAD(Instruction instruction);

    void visitDLOAD(Instruction instruction);

    void visitDCONST(Instruction instruction);

    void visitNEW(Instruction instruction);

    void visitIFNULL(Instruction instruction);

    void visitLSUB(Instruction instruction);

    void visitL2I(Instruction instruction);

    void visitISHR(Instruction instruction);

    void visitTABLESWITCH(TABLESWITCH tableswitch);

    void visitIINC(IINC iinc);

    void visitDRETURN(Instruction instruction);

    void visitFSTORE(Instruction instruction);

    void visitDASTORE(Instruction instruction);

    void visitIALOAD(Instruction instruction);

    void visitDDIV(Instruction instruction);

    void visitIF_ICMPGE(Instruction instruction);

    void visitLAND(Instruction instruction);

    void visitIDIV(Instruction instruction);

    void visitLOR(Instruction instruction);

    void visitCASTORE(Instruction instruction);

    void visitFREM(Instruction instruction);

    void visitLDC(Instruction instruction);

    void visitBIPUSH(Instruction instruction);

    void visitDSTORE(Instruction instruction);

    void visitF2L(Instruction instruction);

    void visitFMUL(Instruction instruction);

    void visitLLOAD(Instruction instruction);

    void visitJSR(InstructionBranch instructionBranch);

    void visitFSUB(Instruction instruction);

    void visitSASTORE(Instruction instruction);

    void visitALOAD(Instruction instruction);

    void visitDUP2_X2(Instruction instruction);

    void visitRETURN(Instruction instruction);

    void visitDALOAD(Instruction instruction);

    void visitSIPUSH(Instruction instruction);

    void visitDSUB(Instruction instruction);

    void visitL2F(Instruction instruction);

    void visitIF_ICMPGT(Instruction instruction);

    void visitF2D(Instruction instruction);

    void visitI2L(Instruction instruction);

    void visitIF_ACMPNE(Instruction instruction);

    void visitPOP(Instruction instruction);

    void visitI2S(Instruction instruction);

    void visitIFEQ(Instruction instruction);

    void visitSWAP(Instruction instruction);

    void visitIOR(Instruction instruction);

    void visitIREM(Instruction instruction);

    void visitIASTORE(Instruction instruction);

    void visitNEWARRAY(Instruction instruction);

    void visitINVOKEINTERFACE(INVOKEINTERFACE invokeinterface);

    void visitINEG(Instruction instruction);

    void visitLCMP(Instruction instruction);

    void visitJSR_W(InstructionBranch instructionBranch);

    void visitMULTIANEWARRAY(MULTIANEWARRAY multianewarray);

    void visitDUP_X2(Instruction instruction);

    void visitSALOAD(Instruction instruction);

    void visitIFNONNULL(Instruction instruction);

    void visitDMUL(Instruction instruction);

    void visitIFNE(Instruction instruction);

    void visitIF_ICMPLE(Instruction instruction);

    void visitLDC2_W(Instruction instruction);

    void visitGETFIELD(FieldInstruction fieldInstruction);

    void visitLADD(Instruction instruction);

    void visitNOP(Instruction instruction);

    void visitFALOAD(Instruction instruction);

    void visitINSTANCEOF(Instruction instruction);

    void visitIFLE(Instruction instruction);

    void visitLXOR(Instruction instruction);

    void visitLRETURN(Instruction instruction);

    void visitFCONST(Instruction instruction);

    void visitIUSHR(Instruction instruction);

    void visitBALOAD(Instruction instruction);

    void visitDUP2(Instruction instruction);

    void visitIF_ACMPEQ(Instruction instruction);

    void visitIMPDEP1(Instruction instruction);

    void visitMONITORENTER(Instruction instruction);

    void visitLSHL(Instruction instruction);

    void visitDCMPG(Instruction instruction);

    void visitD2L(Instruction instruction);

    void visitIMPDEP2(Instruction instruction);

    void visitL2D(Instruction instruction);

    void visitRET(RET ret);

    void visitIFGT(Instruction instruction);

    void visitIXOR(Instruction instruction);

    void visitINVOKEVIRTUAL(InvokeInstruction invokeInstruction);

    void visitFASTORE(Instruction instruction);

    void visitIRETURN(Instruction instruction);

    void visitIF_ICMPNE(Instruction instruction);

    void visitFLOAD(Instruction instruction);

    void visitLDIV(Instruction instruction);

    void visitPUTSTATIC(FieldInstruction fieldInstruction);

    void visitAALOAD(Instruction instruction);

    void visitD2I(Instruction instruction);

    void visitIF_ICMPEQ(Instruction instruction);

    void visitAASTORE(Instruction instruction);

    void visitARETURN(Instruction instruction);

    void visitDUP2_X1(Instruction instruction);

    void visitFNEG(Instruction instruction);

    void visitGOTO_W(Instruction instruction);

    void visitD2F(Instruction instruction);

    void visitGOTO(Instruction instruction);

    void visitISUB(Instruction instruction);

    void visitF2I(Instruction instruction);

    void visitDNEG(Instruction instruction);

    void visitICONST(Instruction instruction);

    void visitFDIV(Instruction instruction);

    void visitI2B(Instruction instruction);

    void visitLNEG(Instruction instruction);

    void visitLREM(Instruction instruction);

    void visitIMUL(Instruction instruction);

    void visitIADD(Instruction instruction);

    void visitLSHR(Instruction instruction);

    void visitLOOKUPSWITCH(LOOKUPSWITCH lookupswitch);

    void visitDUP_X1(Instruction instruction);

    void visitFCMPL(Instruction instruction);

    void visitI2C(Instruction instruction);

    void visitLMUL(Instruction instruction);

    void visitLUSHR(Instruction instruction);

    void visitISHL(Instruction instruction);

    void visitLALOAD(Instruction instruction);

    void visitASTORE(Instruction instruction);

    void visitANEWARRAY(Instruction instruction);

    void visitFRETURN(Instruction instruction);

    void visitFADD(Instruction instruction);

    void visitBREAKPOINT(Instruction instruction);
}
