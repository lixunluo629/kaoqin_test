package org.aspectj.weaver.bcel;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.aspectj.apache.bcel.classfile.ConstantPool;
import org.aspectj.apache.bcel.generic.FieldInstruction;
import org.aspectj.apache.bcel.generic.Instruction;
import org.aspectj.apache.bcel.generic.InstructionConstants;
import org.aspectj.apache.bcel.generic.InstructionFactory;
import org.aspectj.apache.bcel.generic.InstructionHandle;
import org.aspectj.apache.bcel.generic.InstructionList;
import org.aspectj.apache.bcel.generic.InvokeDynamic;
import org.aspectj.apache.bcel.generic.InvokeInstruction;
import org.aspectj.apache.bcel.generic.Type;
import org.aspectj.weaver.AjAttribute;
import org.aspectj.weaver.AjcMemberMaker;
import org.aspectj.weaver.Member;
import org.aspectj.weaver.NameMangler;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.UnresolvedType;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/BcelAccessForInlineMunger.class */
public class BcelAccessForInlineMunger extends BcelTypeMunger {
    private Map<String, ResolvedMember> inlineAccessors;
    private LazyClassGen aspectGen;
    private Set<LazyMethodGen> inlineAccessorMethodGens;

    public BcelAccessForInlineMunger(ResolvedType aspectType) {
        super(null, aspectType);
        if (aspectType.getWorld().isXnoInline()) {
            throw new Error("This should not happen");
        }
    }

    @Override // org.aspectj.weaver.bcel.BcelTypeMunger
    public boolean munge(BcelClassWeaver weaver) {
        this.aspectGen = weaver.getLazyClassGen();
        this.inlineAccessors = new HashMap(0);
        this.inlineAccessorMethodGens = new HashSet();
        for (LazyMethodGen methodGen : this.aspectGen.getMethodGens()) {
            if (methodGen.hasAnnotation(UnresolvedType.forName("org/aspectj/lang/annotation/Around"))) {
                openAroundAdvice(methodGen);
            }
        }
        for (LazyMethodGen lazyMethodGen : this.inlineAccessorMethodGens) {
            this.aspectGen.addMethodGen(lazyMethodGen);
        }
        this.inlineAccessorMethodGens = null;
        return true;
    }

    @Override // org.aspectj.weaver.ConcreteTypeMunger
    public ResolvedMember getMatchingSyntheticMember(Member member) {
        ResolvedMember rm = this.inlineAccessors.get(member.getName());
        return rm;
    }

    @Override // org.aspectj.weaver.ConcreteTypeMunger
    public ResolvedMember getSignature() {
        return null;
    }

    @Override // org.aspectj.weaver.ConcreteTypeMunger
    public boolean matches(ResolvedType onType) {
        return this.aspectType.equals(onType);
    }

    private void openAroundAdvice(LazyMethodGen aroundAdvice) {
        ResolvedMember accessor;
        InstructionHandle curr = aroundAdvice.getBody().getStart();
        InstructionHandle end = aroundAdvice.getBody().getEnd();
        ConstantPool cpg = aroundAdvice.getEnclosingClass().getConstantPool();
        InstructionFactory factory = aroundAdvice.getEnclosingClass().getFactory();
        boolean realizedCannotInline = false;
        while (true) {
            if (curr == end || realizedCannotInline) {
                break;
            }
            InstructionHandle next = curr.getNext();
            Instruction inst = curr.getInstruction();
            if (inst instanceof InvokeInstruction) {
                InvokeInstruction invoke = (InvokeInstruction) inst;
                if (invoke instanceof InvokeDynamic) {
                    realizedCannotInline = true;
                    break;
                }
                List<ResolvedMember> methods = this.aspectGen.getWorld().resolve(UnresolvedType.forName(invoke.getClassName(cpg))).getMethodsWithoutIterator(false, true, false);
                Iterator<ResolvedMember> it = methods.iterator();
                while (true) {
                    if (it.hasNext()) {
                        ResolvedMember resolvedMember = it.next();
                        if (invoke.getName(cpg).equals(resolvedMember.getName()) && invoke.getSignature(cpg).equals(resolvedMember.getSignature()) && !resolvedMember.isPublic()) {
                            if ("<init>".equals(invoke.getName(cpg))) {
                                aroundAdvice.setCanInline(false);
                                realizedCannotInline = true;
                            } else {
                                ResolvedType memberType = this.aspectGen.getWorld().resolve(resolvedMember.getDeclaringType());
                                if (!this.aspectType.equals(memberType) && memberType.isAssignableFrom(this.aspectType)) {
                                    ResolvedMember accessor2 = createOrGetInlineAccessorForSuperDispatch(resolvedMember);
                                    InvokeInstruction newInst = factory.createInvoke(this.aspectType.getName(), accessor2.getName(), BcelWorld.makeBcelType(accessor2.getReturnType()), BcelWorld.makeBcelTypes(accessor2.getParameterTypes()), (short) 182);
                                    curr.setInstruction(newInst);
                                } else {
                                    ResolvedMember accessor3 = createOrGetInlineAccessorForMethod(resolvedMember);
                                    InvokeInstruction newInst2 = factory.createInvoke(this.aspectType.getName(), accessor3.getName(), BcelWorld.makeBcelType(accessor3.getReturnType()), BcelWorld.makeBcelTypes(accessor3.getParameterTypes()), (short) 184);
                                    curr.setInstruction(newInst2);
                                }
                            }
                        }
                    }
                }
            } else if (inst instanceof FieldInstruction) {
                FieldInstruction invoke2 = (FieldInstruction) inst;
                ResolvedType callee = this.aspectGen.getWorld().resolve(UnresolvedType.forName(invoke2.getClassName(cpg)));
                int i = 0;
                while (true) {
                    if (i < callee.getDeclaredJavaFields().length) {
                        ResolvedMember resolvedMember2 = callee.getDeclaredJavaFields()[i];
                        if (!invoke2.getName(cpg).equals(resolvedMember2.getName()) || !invoke2.getSignature(cpg).equals(resolvedMember2.getSignature()) || resolvedMember2.isPublic()) {
                            i++;
                        } else {
                            if (inst.opcode == 180 || inst.opcode == 178) {
                                accessor = createOrGetInlineAccessorForFieldGet(resolvedMember2);
                            } else {
                                accessor = createOrGetInlineAccessorForFieldSet(resolvedMember2);
                            }
                            InvokeInstruction newInst3 = factory.createInvoke(this.aspectType.getName(), accessor.getName(), BcelWorld.makeBcelType(accessor.getReturnType()), BcelWorld.makeBcelTypes(accessor.getParameterTypes()), (short) 184);
                            curr.setInstruction(newInst3);
                        }
                    }
                }
            }
            curr = next;
        }
        if (!realizedCannotInline) {
            aroundAdvice.setCanInline(true);
        }
    }

    private ResolvedMember createOrGetInlineAccessorForMethod(ResolvedMember resolvedMember) {
        String accessorName = NameMangler.inlineAccessMethodForMethod(resolvedMember.getName(), resolvedMember.getDeclaringType(), this.aspectType);
        ResolvedMember inlineAccessor = this.inlineAccessors.get(accessorName);
        if (inlineAccessor == null) {
            inlineAccessor = AjcMemberMaker.inlineAccessMethodForMethod(this.aspectType, resolvedMember);
            InstructionFactory factory = this.aspectGen.getFactory();
            LazyMethodGen method = makeMethodGen(this.aspectGen, inlineAccessor);
            method.makeSynthetic();
            List<AjAttribute> methodAttributes = new ArrayList<>();
            methodAttributes.add(new AjAttribute.AjSynthetic());
            methodAttributes.add(new AjAttribute.EffectiveSignatureAttribute(resolvedMember, Shadow.MethodCall, false));
            method.addAttribute(Utility.bcelAttribute(methodAttributes.get(0), this.aspectGen.getConstantPool()));
            method.addAttribute(Utility.bcelAttribute(methodAttributes.get(1), this.aspectGen.getConstantPool()));
            this.inlineAccessorMethodGens.add(method);
            InstructionList il = method.getBody();
            int register = 0;
            int max = inlineAccessor.getParameterTypes().length;
            for (int i = 0; i < max; i++) {
                UnresolvedType ptype = inlineAccessor.getParameterTypes()[i];
                Type type = BcelWorld.makeBcelType(ptype);
                il.append(InstructionFactory.createLoad(type, register));
                register += type.getSize();
            }
            il.append(Utility.createInvoke(factory, Modifier.isStatic(resolvedMember.getModifiers()) ? (short) 184 : (short) 182, resolvedMember));
            il.append(InstructionFactory.createReturn(BcelWorld.makeBcelType(inlineAccessor.getReturnType())));
            this.inlineAccessors.put(accessorName, new BcelMethod(this.aspectGen.getBcelObjectType(), method.getMethod(), methodAttributes));
        }
        return inlineAccessor;
    }

    private ResolvedMember createOrGetInlineAccessorForSuperDispatch(ResolvedMember resolvedMember) {
        String accessor = NameMangler.superDispatchMethod(this.aspectType, resolvedMember.getName());
        ResolvedMember inlineAccessor = this.inlineAccessors.get(accessor);
        if (inlineAccessor == null) {
            inlineAccessor = AjcMemberMaker.superAccessMethod(this.aspectType, resolvedMember);
            InstructionFactory factory = this.aspectGen.getFactory();
            LazyMethodGen method = makeMethodGen(this.aspectGen, inlineAccessor);
            method.makeSynthetic();
            List<AjAttribute> methodAttributes = new ArrayList<>();
            methodAttributes.add(new AjAttribute.AjSynthetic());
            methodAttributes.add(new AjAttribute.EffectiveSignatureAttribute(resolvedMember, Shadow.MethodCall, false));
            method.addAttribute(Utility.bcelAttribute(methodAttributes.get(0), this.aspectGen.getConstantPool()));
            method.addAttribute(Utility.bcelAttribute(methodAttributes.get(1), this.aspectGen.getConstantPool()));
            this.inlineAccessorMethodGens.add(method);
            InstructionList il = method.getBody();
            il.append(InstructionConstants.ALOAD_0);
            int register = 1;
            for (int i = 0; i < inlineAccessor.getParameterTypes().length; i++) {
                UnresolvedType typeX = inlineAccessor.getParameterTypes()[i];
                Type type = BcelWorld.makeBcelType(typeX);
                il.append(InstructionFactory.createLoad(type, register));
                register += type.getSize();
            }
            il.append(Utility.createInvoke(factory, (short) 183, (Member) resolvedMember));
            il.append(InstructionFactory.createReturn(BcelWorld.makeBcelType(inlineAccessor.getReturnType())));
            this.inlineAccessors.put(accessor, new BcelMethod(this.aspectGen.getBcelObjectType(), method.getMethod(), methodAttributes));
        }
        return inlineAccessor;
    }

    private ResolvedMember createOrGetInlineAccessorForFieldGet(ResolvedMember resolvedMember) {
        String accessor = NameMangler.inlineAccessMethodForFieldGet(resolvedMember.getName(), resolvedMember.getDeclaringType(), this.aspectType);
        ResolvedMember inlineAccessor = this.inlineAccessors.get(accessor);
        if (inlineAccessor == null) {
            inlineAccessor = AjcMemberMaker.inlineAccessMethodForFieldGet(this.aspectType, resolvedMember);
            InstructionFactory factory = this.aspectGen.getFactory();
            LazyMethodGen method = makeMethodGen(this.aspectGen, inlineAccessor);
            method.makeSynthetic();
            List<AjAttribute> methodAttributes = new ArrayList<>();
            methodAttributes.add(new AjAttribute.AjSynthetic());
            methodAttributes.add(new AjAttribute.EffectiveSignatureAttribute(resolvedMember, Shadow.FieldGet, false));
            method.addAttribute(Utility.bcelAttribute(methodAttributes.get(0), this.aspectGen.getConstantPool()));
            method.addAttribute(Utility.bcelAttribute(methodAttributes.get(1), this.aspectGen.getConstantPool()));
            this.inlineAccessorMethodGens.add(method);
            InstructionList il = method.getBody();
            if (!Modifier.isStatic(resolvedMember.getModifiers())) {
                il.append(InstructionConstants.ALOAD_0);
            }
            il.append(Utility.createGet(factory, resolvedMember));
            il.append(InstructionFactory.createReturn(BcelWorld.makeBcelType(inlineAccessor.getReturnType())));
            this.inlineAccessors.put(accessor, new BcelMethod(this.aspectGen.getBcelObjectType(), method.getMethod(), methodAttributes));
        }
        return inlineAccessor;
    }

    private ResolvedMember createOrGetInlineAccessorForFieldSet(ResolvedMember resolvedMember) {
        String accessor = NameMangler.inlineAccessMethodForFieldSet(resolvedMember.getName(), resolvedMember.getDeclaringType(), this.aspectType);
        ResolvedMember inlineAccessor = this.inlineAccessors.get(accessor);
        if (inlineAccessor == null) {
            inlineAccessor = AjcMemberMaker.inlineAccessMethodForFieldSet(this.aspectType, resolvedMember);
            InstructionFactory factory = this.aspectGen.getFactory();
            LazyMethodGen method = makeMethodGen(this.aspectGen, inlineAccessor);
            method.makeSynthetic();
            List<AjAttribute> methodAttributes = new ArrayList<>();
            methodAttributes.add(new AjAttribute.AjSynthetic());
            methodAttributes.add(new AjAttribute.EffectiveSignatureAttribute(resolvedMember, Shadow.FieldSet, false));
            method.addAttribute(Utility.bcelAttribute(methodAttributes.get(0), this.aspectGen.getConstantPool()));
            method.addAttribute(Utility.bcelAttribute(methodAttributes.get(1), this.aspectGen.getConstantPool()));
            this.inlineAccessorMethodGens.add(method);
            InstructionList il = method.getBody();
            if (Modifier.isStatic(resolvedMember.getModifiers())) {
                il.append(InstructionFactory.createLoad(BcelWorld.makeBcelType(resolvedMember.getReturnType()), 0));
            } else {
                il.append(InstructionConstants.ALOAD_0);
                il.append(InstructionFactory.createLoad(BcelWorld.makeBcelType(resolvedMember.getReturnType()), 1));
            }
            il.append(Utility.createSet(factory, resolvedMember));
            il.append(InstructionConstants.RETURN);
            this.inlineAccessors.put(accessor, new BcelMethod(this.aspectGen.getBcelObjectType(), method.getMethod(), methodAttributes));
        }
        return inlineAccessor;
    }
}
