package org.aspectj.weaver.bcel;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import org.aspectj.apache.bcel.classfile.Attribute;
import org.aspectj.apache.bcel.classfile.Constant;
import org.aspectj.apache.bcel.classfile.ConstantUtf8;
import org.aspectj.apache.bcel.classfile.Field;
import org.aspectj.apache.bcel.classfile.JavaClass;
import org.aspectj.apache.bcel.classfile.LocalVariable;
import org.aspectj.apache.bcel.classfile.LocalVariableTable;
import org.aspectj.apache.bcel.classfile.Method;
import org.aspectj.apache.bcel.classfile.Unknown;
import org.aspectj.apache.bcel.classfile.annotation.AnnotationGen;
import org.aspectj.apache.bcel.classfile.annotation.ArrayElementValue;
import org.aspectj.apache.bcel.classfile.annotation.ClassElementValue;
import org.aspectj.apache.bcel.classfile.annotation.ElementValue;
import org.aspectj.apache.bcel.classfile.annotation.NameValuePair;
import org.aspectj.apache.bcel.classfile.annotation.RuntimeAnnos;
import org.aspectj.apache.bcel.classfile.annotation.RuntimeVisAnnos;
import org.aspectj.apache.bcel.generic.Type;
import org.aspectj.asm.AsmManager;
import org.aspectj.asm.IHierarchy;
import org.aspectj.asm.IProgramElement;
import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.IMessageHandler;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.Message;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.weaver.AdviceKind;
import org.aspectj.weaver.AjAttribute;
import org.aspectj.weaver.AjcMemberMaker;
import org.aspectj.weaver.BindingScope;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.MethodDelegateTypeMunger;
import org.aspectj.weaver.NameMangler;
import org.aspectj.weaver.ReferenceType;
import org.aspectj.weaver.ReferenceTypeDelegate;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedPointcutDefinition;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.WeaverMessages;
import org.aspectj.weaver.World;
import org.aspectj.weaver.patterns.DeclareErrorOrWarning;
import org.aspectj.weaver.patterns.DeclareParents;
import org.aspectj.weaver.patterns.DeclareParentsMixin;
import org.aspectj.weaver.patterns.DeclarePrecedence;
import org.aspectj.weaver.patterns.FormalBinding;
import org.aspectj.weaver.patterns.IScope;
import org.aspectj.weaver.patterns.ParserException;
import org.aspectj.weaver.patterns.PatternParser;
import org.aspectj.weaver.patterns.PerCflow;
import org.aspectj.weaver.patterns.PerClause;
import org.aspectj.weaver.patterns.PerFromSuper;
import org.aspectj.weaver.patterns.PerObject;
import org.aspectj.weaver.patterns.PerSingleton;
import org.aspectj.weaver.patterns.PerTypeWithin;
import org.aspectj.weaver.patterns.Pointcut;
import org.aspectj.weaver.patterns.TypePattern;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/AtAjAttributes.class */
public class AtAjAttributes {
    private static final List<AjAttribute> NO_ATTRIBUTES = Collections.emptyList();
    private static final String[] EMPTY_STRINGS = new String[0];
    private static final String VALUE = "value";
    private static final String ARGNAMES = "argNames";
    private static final String POINTCUT = "pointcut";
    private static final String THROWING = "throwing";
    private static final String RETURNING = "returning";
    private static final String STRING_DESC = "Ljava/lang/String;";

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/AtAjAttributes$AjAttributeStruct.class */
    private static class AjAttributeStruct {
        List<AjAttribute> ajAttributes = new ArrayList();
        final ResolvedType enclosingType;
        final ISourceContext context;
        final IMessageHandler handler;

        public AjAttributeStruct(ResolvedType type, ISourceContext sourceContext, IMessageHandler messageHandler) {
            this.enclosingType = type;
            this.context = sourceContext;
            this.handler = messageHandler;
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/AtAjAttributes$AjAttributeMethodStruct.class */
    private static class AjAttributeMethodStruct extends AjAttributeStruct {
        private String[] m_argumentNamesLazy;
        public String unparsedArgumentNames;
        final Method method;
        final BcelMethod bMethod;

        public AjAttributeMethodStruct(Method method, BcelMethod bMethod, ResolvedType type, ISourceContext sourceContext, IMessageHandler messageHandler) {
            super(type, sourceContext, messageHandler);
            this.m_argumentNamesLazy = null;
            this.unparsedArgumentNames = null;
            this.method = method;
            this.bMethod = bMethod;
        }

        public String[] getArgumentNames() {
            if (this.m_argumentNamesLazy == null) {
                this.m_argumentNamesLazy = AtAjAttributes.getMethodArgumentNames(this.method, this.unparsedArgumentNames, this);
            }
            return this.m_argumentNamesLazy;
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/AtAjAttributes$AjAttributeFieldStruct.class */
    private static class AjAttributeFieldStruct extends AjAttributeStruct {
        final Field field;

        public AjAttributeFieldStruct(Field field, BcelField bField, ResolvedType type, ISourceContext sourceContext, IMessageHandler messageHandler) {
            super(type, sourceContext, messageHandler);
            this.field = field;
        }
    }

    public static boolean acceptAttribute(Attribute attribute) {
        return attribute instanceof RuntimeVisAnnos;
    }

    public static List<AjAttribute> readAj5ClassAttributes(AsmManager model, JavaClass javaClass, ReferenceType type, ISourceContext context, IMessageHandler msgHandler, boolean isCodeStyleAspect) {
        boolean ignoreThisClass = javaClass.getClassName().charAt(0) == 'o' && javaClass.getClassName().startsWith("org.aspectj.lang.annotation");
        if (ignoreThisClass) {
            return NO_ATTRIBUTES;
        }
        boolean containsPointcut = false;
        boolean containsAnnotationClassReference = false;
        Constant[] cpool = javaClass.getConstantPool().getConstantPool();
        for (Constant constant : cpool) {
            if (constant != null && constant.getTag() == 1) {
                String constantValue = ((ConstantUtf8) constant).getValue();
                if (constantValue.length() > 28 && constantValue.charAt(1) == 'o' && constantValue.startsWith("Lorg/aspectj/lang/annotation")) {
                    containsAnnotationClassReference = true;
                    if ("Lorg/aspectj/lang/annotation/DeclareAnnotation;".equals(constantValue)) {
                        msgHandler.handleMessage(new Message("Found @DeclareAnnotation while current release does not support it (see '" + type.getName() + "')", IMessage.WARNING, (Throwable) null, type.getSourceLocation()));
                    }
                    if ("Lorg/aspectj/lang/annotation/Pointcut;".equals(constantValue)) {
                        containsPointcut = true;
                    }
                }
            }
        }
        if (!containsAnnotationClassReference) {
            return NO_ATTRIBUTES;
        }
        AjAttributeStruct struct = new AjAttributeStruct(type, context, msgHandler);
        Attribute[] attributes = javaClass.getAttributes();
        boolean hasAtAspectAnnotation = false;
        boolean hasAtPrecedenceAnnotation = false;
        AjAttribute.WeaverVersionInfo wvinfo = null;
        int i = 0;
        while (true) {
            if (i >= attributes.length) {
                break;
            }
            Attribute attribute = attributes[i];
            if (!acceptAttribute(attribute)) {
                i++;
            } else {
                RuntimeAnnos rvs = (RuntimeAnnos) attribute;
                if (!isCodeStyleAspect && !javaClass.isInterface()) {
                    hasAtAspectAnnotation = handleAspectAnnotation(rvs, struct);
                    hasAtPrecedenceAnnotation = handlePrecedenceAnnotation(rvs, struct);
                }
            }
        }
        for (int i2 = attributes.length - 1; i2 >= 0; i2--) {
            Attribute attribute2 = attributes[i2];
            if (attribute2.getName().equals(AjAttribute.WeaverVersionInfo.AttributeName)) {
                try {
                    VersionedDataInputStream s = new VersionedDataInputStream(new ByteArrayInputStream(((Unknown) attribute2).getBytes()), null);
                    wvinfo = AjAttribute.WeaverVersionInfo.read(s);
                    struct.ajAttributes.add(0, wvinfo);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        if (wvinfo == null) {
            ReferenceTypeDelegate delegate = type.getDelegate();
            if (delegate instanceof BcelObjectType) {
                wvinfo = ((BcelObjectType) delegate).getWeaverVersionAttribute();
                if (wvinfo != null) {
                    if (wvinfo.getMajorVersion() != 0) {
                        struct.ajAttributes.add(0, wvinfo);
                    } else {
                        wvinfo = null;
                    }
                }
            }
            if (wvinfo == null) {
                struct.ajAttributes.add(0, new AjAttribute.WeaverVersionInfo());
            }
        }
        if (hasAtPrecedenceAnnotation && !hasAtAspectAnnotation) {
            msgHandler.handleMessage(new Message("Found @DeclarePrecedence on a non @Aspect type '" + type.getName() + "'", IMessage.WARNING, (Throwable) null, type.getSourceLocation()));
            return NO_ATTRIBUTES;
        }
        if (!hasAtAspectAnnotation && !isCodeStyleAspect && !containsPointcut) {
            return NO_ATTRIBUTES;
        }
        for (int i3 = 0; i3 < javaClass.getMethods().length; i3++) {
            Method method = javaClass.getMethods()[i3];
            if (!method.getName().startsWith(NameMangler.PREFIX)) {
                AjAttributeMethodStruct mstruct = null;
                boolean processedPointcut = false;
                Attribute[] mattributes = method.getAttributes();
                int j = 0;
                while (true) {
                    if (j >= mattributes.length) {
                        break;
                    }
                    Attribute mattribute = mattributes[j];
                    if (!acceptAttribute(mattribute)) {
                        j++;
                    } else {
                        mstruct = new AjAttributeMethodStruct(method, null, type, context, msgHandler);
                        processedPointcut = handlePointcutAnnotation((RuntimeAnnos) mattribute, mstruct);
                        if (!processedPointcut) {
                            processedPointcut = handleDeclareMixinAnnotation((RuntimeAnnos) mattribute, mstruct);
                        }
                    }
                }
                if (processedPointcut) {
                    struct.ajAttributes.addAll(mstruct.ajAttributes);
                }
            }
        }
        Field[] fs = javaClass.getFields();
        for (Field field : fs) {
            if (!field.getName().startsWith(NameMangler.PREFIX)) {
                AjAttributeFieldStruct fstruct = new AjAttributeFieldStruct(field, null, type, context, msgHandler);
                Attribute[] fattributes = field.getAttributes();
                int j2 = 0;
                while (true) {
                    if (j2 >= fattributes.length) {
                        break;
                    }
                    Attribute fattribute = fattributes[j2];
                    if (!acceptAttribute(fattribute)) {
                        j2++;
                    } else {
                        RuntimeAnnos frvs = (RuntimeAnnos) fattribute;
                        if ((handleDeclareErrorOrWarningAnnotation(model, frvs, fstruct) || handleDeclareParentsAnnotation(frvs, fstruct)) && !type.isAnnotationStyleAspect() && !isCodeStyleAspect) {
                            msgHandler.handleMessage(new Message("Found @AspectJ annotations in a non @Aspect type '" + type.getName() + "'", IMessage.WARNING, (Throwable) null, type.getSourceLocation()));
                        }
                    }
                }
                struct.ajAttributes.addAll(fstruct.ajAttributes);
            }
        }
        return struct.ajAttributes;
    }

    public static List<AjAttribute> readAj5MethodAttributes(Method method, BcelMethod bMethod, ResolvedType type, ResolvedPointcutDefinition preResolvedPointcut, ISourceContext context, IMessageHandler msgHandler) {
        boolean z;
        if (method.getName().startsWith(NameMangler.PREFIX)) {
            return Collections.emptyList();
        }
        AjAttributeMethodStruct struct = new AjAttributeMethodStruct(method, bMethod, type, context, msgHandler);
        Attribute[] attributes = method.getAttributes();
        boolean hasAtAspectJAnnotation = false;
        boolean hasAtAspectJAnnotationMustReturnVoid = false;
        for (Attribute attribute : attributes) {
            try {
            } catch (ReturningFormalNotDeclaredInAdviceSignatureException e) {
                msgHandler.handleMessage(new Message(WeaverMessages.format(WeaverMessages.RETURNING_FORMAL_NOT_DECLARED_IN_ADVICE, e.getFormalName()), IMessage.ERROR, (Throwable) null, bMethod.getSourceLocation()));
            } catch (ThrownFormalNotDeclaredInAdviceSignatureException e2) {
                msgHandler.handleMessage(new Message(WeaverMessages.format(WeaverMessages.THROWN_FORMAL_NOT_DECLARED_IN_ADVICE, e2.getFormalName()), IMessage.ERROR, (Throwable) null, bMethod.getSourceLocation()));
            }
            if (!acceptAttribute(attribute)) {
                continue;
            } else {
                RuntimeAnnos rvs = (RuntimeAnnos) attribute;
                boolean hasAtAspectJAnnotationMustReturnVoid2 = hasAtAspectJAnnotationMustReturnVoid || handleBeforeAnnotation(rvs, struct, preResolvedPointcut);
                boolean hasAtAspectJAnnotationMustReturnVoid3 = hasAtAspectJAnnotationMustReturnVoid2 || handleAfterAnnotation(rvs, struct, preResolvedPointcut);
                boolean hasAtAspectJAnnotationMustReturnVoid4 = hasAtAspectJAnnotationMustReturnVoid3 || handleAfterReturningAnnotation(rvs, struct, preResolvedPointcut, bMethod);
                hasAtAspectJAnnotationMustReturnVoid = hasAtAspectJAnnotationMustReturnVoid4 || handleAfterThrowingAnnotation(rvs, struct, preResolvedPointcut, bMethod);
                if (0 == 0) {
                    if (!handleAroundAnnotation(rvs, struct, preResolvedPointcut)) {
                        z = false;
                    }
                    hasAtAspectJAnnotation = z;
                    break;
                }
                z = true;
                hasAtAspectJAnnotation = z;
                break;
            }
        }
        boolean hasAtAspectJAnnotation2 = hasAtAspectJAnnotation || hasAtAspectJAnnotationMustReturnVoid;
        if (hasAtAspectJAnnotation2 && !type.isAspect()) {
            msgHandler.handleMessage(new Message("Found @AspectJ annotations in a non @Aspect type '" + type.getName() + "'", IMessage.WARNING, (Throwable) null, type.getSourceLocation()));
        }
        if (hasAtAspectJAnnotation2 && !struct.method.isPublic()) {
            msgHandler.handleMessage(new Message("Found @AspectJ annotation on a non public advice '" + methodToString(struct.method) + "'", IMessage.ERROR, (Throwable) null, type.getSourceLocation()));
        }
        if (hasAtAspectJAnnotation2 && struct.method.isStatic()) {
            msgHandler.handleMessage(MessageUtil.error("Advice cannot be declared static '" + methodToString(struct.method) + "'", type.getSourceLocation()));
        }
        if (hasAtAspectJAnnotationMustReturnVoid && !Type.VOID.equals(struct.method.getReturnType())) {
            msgHandler.handleMessage(new Message("Found @AspectJ annotation on a non around advice not returning void '" + methodToString(struct.method) + "'", IMessage.ERROR, (Throwable) null, type.getSourceLocation()));
        }
        return struct.ajAttributes;
    }

    public static List<AjAttribute> readAj5FieldAttributes(Field field, BcelField bField, ResolvedType type, ISourceContext context, IMessageHandler msgHandler) {
        return Collections.emptyList();
    }

    private static boolean handleAspectAnnotation(RuntimeAnnos runtimeAnnotations, AjAttributeStruct struct) throws AbortException {
        PerClause perClause;
        AnnotationGen aspect = getAnnotation(runtimeAnnotations, AjcMemberMaker.ASPECT_ANNOTATION);
        if (aspect != null) {
            boolean extendsAspect = false;
            if (!"java.lang.Object".equals(struct.enclosingType.getSuperclass().getName())) {
                if (!struct.enclosingType.getSuperclass().isAbstract() && struct.enclosingType.getSuperclass().isAspect()) {
                    reportError("cannot extend a concrete aspect", struct);
                    return false;
                }
                extendsAspect = struct.enclosingType.getSuperclass().isAspect();
            }
            NameValuePair aspectPerClause = getAnnotationElement(aspect, "value");
            if (aspectPerClause == null) {
                if (!extendsAspect) {
                    perClause = new PerSingleton();
                } else {
                    perClause = new PerFromSuper(struct.enclosingType.getSuperclass().getPerClause().getKind());
                }
            } else {
                String perX = aspectPerClause.getValue().stringifyValue();
                if (perX == null || perX.length() <= 0) {
                    perClause = new PerSingleton();
                } else {
                    perClause = parsePerClausePointcut(perX, struct);
                }
            }
            if (perClause == null) {
                return false;
            }
            perClause.setLocation(struct.context, -1, -1);
            AjAttribute.Aspect aspectAttribute = new AjAttribute.Aspect(perClause);
            struct.ajAttributes.add(aspectAttribute);
            FormalBinding[] bindings = new FormalBinding[0];
            IScope binding = new BindingScope(struct.enclosingType, struct.context, bindings);
            aspectAttribute.setResolutionScope(binding);
            return true;
        }
        return false;
    }

    private static PerClause parsePerClausePointcut(String perClauseString, AjAttributeStruct struct) throws AbortException {
        PerClause perClause;
        Pointcut pointcut = null;
        TypePattern typePattern = null;
        if (perClauseString.startsWith(PerClause.KindAnnotationPrefix.PERCFLOW.getName())) {
            String pointcutString = PerClause.KindAnnotationPrefix.PERCFLOW.extractPointcut(perClauseString);
            pointcut = parsePointcut(pointcutString, struct, false);
            perClause = new PerCflow(pointcut, false);
        } else if (perClauseString.startsWith(PerClause.KindAnnotationPrefix.PERCFLOWBELOW.getName())) {
            String pointcutString2 = PerClause.KindAnnotationPrefix.PERCFLOWBELOW.extractPointcut(perClauseString);
            pointcut = parsePointcut(pointcutString2, struct, false);
            perClause = new PerCflow(pointcut, true);
        } else if (perClauseString.startsWith(PerClause.KindAnnotationPrefix.PERTARGET.getName())) {
            String pointcutString3 = PerClause.KindAnnotationPrefix.PERTARGET.extractPointcut(perClauseString);
            pointcut = parsePointcut(pointcutString3, struct, false);
            perClause = new PerObject(pointcut, false);
        } else if (perClauseString.startsWith(PerClause.KindAnnotationPrefix.PERTHIS.getName())) {
            String pointcutString4 = PerClause.KindAnnotationPrefix.PERTHIS.extractPointcut(perClauseString);
            pointcut = parsePointcut(pointcutString4, struct, false);
            perClause = new PerObject(pointcut, true);
        } else if (perClauseString.startsWith(PerClause.KindAnnotationPrefix.PERTYPEWITHIN.getName())) {
            String pointcutString5 = PerClause.KindAnnotationPrefix.PERTYPEWITHIN.extractPointcut(perClauseString);
            typePattern = parseTypePattern(pointcutString5, struct);
            perClause = new PerTypeWithin(typePattern);
        } else if (perClauseString.equalsIgnoreCase(PerClause.SINGLETON.getName() + "()")) {
            perClause = new PerSingleton();
        } else {
            reportError("@Aspect per clause cannot be read '" + perClauseString + "'", struct);
            return null;
        }
        if (!PerClause.SINGLETON.equals(perClause.getKind()) && !PerClause.PERTYPEWITHIN.equals(perClause.getKind()) && pointcut == null) {
            return null;
        }
        if (PerClause.PERTYPEWITHIN.equals(perClause.getKind()) && typePattern == null) {
            return null;
        }
        return perClause;
    }

    private static boolean handlePrecedenceAnnotation(RuntimeAnnos runtimeAnnotations, AjAttributeStruct struct) {
        NameValuePair precedence;
        AnnotationGen aspect = getAnnotation(runtimeAnnotations, AjcMemberMaker.DECLAREPRECEDENCE_ANNOTATION);
        if (aspect != null && (precedence = getAnnotationElement(aspect, "value")) != null) {
            String precedencePattern = precedence.getValue().stringifyValue();
            PatternParser parser = new PatternParser(precedencePattern);
            DeclarePrecedence ajPrecedence = parser.parseDominates();
            struct.ajAttributes.add(new AjAttribute.DeclareAttribute(ajPrecedence));
            return true;
        }
        return false;
    }

    private static boolean handleDeclareParentsAnnotation(RuntimeAnnos runtimeAnnotations, AjAttributeFieldStruct struct) throws AbortException {
        AnnotationGen decp = getAnnotation(runtimeAnnotations, AjcMemberMaker.DECLAREPARENTS_ANNOTATION);
        if (decp != null) {
            NameValuePair decpPatternNVP = getAnnotationElement(decp, "value");
            String decpPattern = decpPatternNVP.getValue().stringifyValue();
            if (decpPattern != null) {
                TypePattern typePattern = parseTypePattern(decpPattern, struct);
                ResolvedType fieldType = UnresolvedType.forSignature(struct.field.getSignature()).resolve(struct.enclosingType.getWorld());
                if (fieldType.isParameterizedOrRawType()) {
                    fieldType = fieldType.getGenericType();
                }
                if (fieldType.isInterface()) {
                    TypePattern parent = parseTypePattern(fieldType.getName(), struct);
                    FormalBinding[] bindings = new FormalBinding[0];
                    IScope binding = new BindingScope(struct.enclosingType, struct.context, bindings);
                    List<TypePattern> parents = new ArrayList<>(1);
                    parents.add(parent);
                    DeclareParents dp = new DeclareParents(typePattern, (List) parents, false);
                    dp.resolve(binding);
                    TypePattern typePattern2 = dp.getChild();
                    dp.setLocation(struct.context, -1, -1);
                    struct.ajAttributes.add(new AjAttribute.DeclareAttribute(dp));
                    String defaultImplClassName = null;
                    NameValuePair defaultImplNVP = getAnnotationElement(decp, "defaultImpl");
                    if (defaultImplNVP != null) {
                        ClassElementValue defaultImpl = (ClassElementValue) defaultImplNVP.getValue();
                        defaultImplClassName = UnresolvedType.forSignature(defaultImpl.getClassString()).getName();
                        if (defaultImplClassName.equals("org.aspectj.lang.annotation.DeclareParents")) {
                            defaultImplClassName = null;
                        } else {
                            ResolvedType impl = struct.enclosingType.getWorld().resolve(defaultImplClassName, false);
                            ResolvedMember[] mm = impl.getDeclaredMethods();
                            int implModifiers = impl.getModifiers();
                            boolean defaultVisibilityImpl = (Modifier.isPrivate(implModifiers) || Modifier.isProtected(implModifiers) || Modifier.isPublic(implModifiers)) ? false : true;
                            boolean hasNoCtorOrANoArgOne = true;
                            ResolvedMember foundOneOfIncorrectVisibility = null;
                            for (ResolvedMember resolvedMember : mm) {
                                if (resolvedMember.getName().equals("<init>")) {
                                    hasNoCtorOrANoArgOne = false;
                                    if (resolvedMember.getParameterTypes().length == 0) {
                                        if (defaultVisibilityImpl) {
                                            if (resolvedMember.isPublic() || resolvedMember.isDefault()) {
                                                hasNoCtorOrANoArgOne = true;
                                            } else {
                                                foundOneOfIncorrectVisibility = resolvedMember;
                                            }
                                        } else if (Modifier.isPublic(implModifiers)) {
                                            if (resolvedMember.isPublic()) {
                                                hasNoCtorOrANoArgOne = true;
                                            } else {
                                                foundOneOfIncorrectVisibility = resolvedMember;
                                            }
                                        }
                                    }
                                }
                                if (hasNoCtorOrANoArgOne) {
                                    break;
                                }
                            }
                            if (!hasNoCtorOrANoArgOne) {
                                if (foundOneOfIncorrectVisibility != null) {
                                    reportError("@DeclareParents: defaultImpl=\"" + defaultImplClassName + "\" has a no argument constructor, but it is of incorrect visibility.  It must be at least as visible as the type.", struct);
                                } else {
                                    reportError("@DeclareParents: defaultImpl=\"" + defaultImplClassName + "\" has no public no-arg constructor", struct);
                                }
                            }
                            if (!fieldType.isAssignableFrom(impl)) {
                                reportError("@DeclareParents: defaultImpl=\"" + defaultImplClassName + "\" does not implement the interface '" + fieldType.toString() + "'", struct);
                            }
                        }
                    }
                    boolean hasAtLeastOneMethod = false;
                    Iterator<ResolvedMember> methodIterator = fieldType.getMethodsIncludingIntertypeDeclarations(false, true);
                    while (methodIterator.hasNext()) {
                        ResolvedMember method = methodIterator.next();
                        if (method.isAbstract()) {
                            hasAtLeastOneMethod = true;
                            MethodDelegateTypeMunger mdtm = new MethodDelegateTypeMunger(method, struct.enclosingType, defaultImplClassName, typePattern2);
                            mdtm.setFieldType(fieldType);
                            mdtm.setSourceLocation(struct.enclosingType.getSourceLocation());
                            struct.ajAttributes.add(new AjAttribute.TypeMunger(mdtm));
                        }
                    }
                    if (hasAtLeastOneMethod && defaultImplClassName != null) {
                        ResolvedMember fieldHost = AjcMemberMaker.itdAtDeclareParentsField(null, fieldType, struct.enclosingType);
                        struct.ajAttributes.add(new AjAttribute.TypeMunger(new MethodDelegateTypeMunger.FieldHostTypeMunger(fieldHost, struct.enclosingType, typePattern2)));
                        return true;
                    }
                    return true;
                }
                reportError("@DeclareParents: can only be used on a field whose type is an interface", struct);
                return false;
            }
            return false;
        }
        return false;
    }

    public static String getMethodForMessage(AjAttributeMethodStruct methodstructure) {
        StringBuffer sb = new StringBuffer();
        sb.append("Method '");
        sb.append(methodstructure.method.getReturnType().toString());
        sb.append(SymbolConstants.SPACE_SYMBOL).append(methodstructure.enclosingType).append(".").append(methodstructure.method.getName());
        sb.append("(");
        Type[] args = methodstructure.method.getArgumentTypes();
        if (args != null) {
            for (int t = 0; t < args.length; t++) {
                if (t > 0) {
                    sb.append(",");
                }
                sb.append(args[t].toString());
            }
        }
        sb.append(")'");
        return sb.toString();
    }

    private static boolean handleDeclareMixinAnnotation(RuntimeAnnos runtimeAnnotations, AjAttributeMethodStruct struct) throws AbortException {
        AnnotationGen declareMixinAnnotation = getAnnotation(runtimeAnnotations, AjcMemberMaker.DECLAREMIXIN_ANNOTATION);
        if (declareMixinAnnotation == null) {
            return false;
        }
        Method annotatedMethod = struct.method;
        World world = struct.enclosingType.getWorld();
        NameValuePair declareMixinPatternNameValuePair = getAnnotationElement(declareMixinAnnotation, "value");
        String declareMixinPattern = declareMixinPatternNameValuePair.getValue().stringifyValue();
        TypePattern targetTypePattern = parseTypePattern(declareMixinPattern, struct);
        ResolvedType methodReturnType = UnresolvedType.forSignature(annotatedMethod.getReturnType().getSignature()).resolve(world);
        if (methodReturnType.isParameterizedOrRawType()) {
            methodReturnType = methodReturnType.getGenericType();
        }
        if (methodReturnType.isPrimitiveType()) {
            reportError(getMethodForMessage(struct) + ":  factory methods for a mixin cannot return void or a primitive type", struct);
            return false;
        }
        if (annotatedMethod.getArgumentTypes().length > 1) {
            reportError(getMethodForMessage(struct) + ": factory methods for a mixin can take a maximum of one parameter", struct);
            return false;
        }
        NameValuePair interfaceListSpecified = getAnnotationElement(declareMixinAnnotation, "interfaces");
        List<TypePattern> newParents = new ArrayList<>(1);
        List<ResolvedType> newInterfaceTypes = new ArrayList<>(1);
        if (interfaceListSpecified != null) {
            ArrayElementValue arrayOfInterfaceTypes = (ArrayElementValue) interfaceListSpecified.getValue();
            int numberOfTypes = arrayOfInterfaceTypes.getElementValuesArraySize();
            ElementValue[] theTypes = arrayOfInterfaceTypes.getElementValuesArray();
            for (int i = 0; i < numberOfTypes; i++) {
                ClassElementValue interfaceType = (ClassElementValue) theTypes[i];
                ResolvedType ajInterfaceType = UnresolvedType.forSignature(interfaceType.getClassString().replace("/", ".")).resolve(world);
                if (ajInterfaceType.isMissing() || !ajInterfaceType.isInterface()) {
                    reportError("Types listed in the 'interfaces' DeclareMixin annotation value must be valid interfaces. This is invalid: " + ajInterfaceType.getName(), struct);
                    return false;
                }
                if (!ajInterfaceType.isAssignableFrom(methodReturnType)) {
                    reportError(getMethodForMessage(struct) + ": factory method does not return something that implements '" + ajInterfaceType.getName() + "'", struct);
                    return false;
                }
                newInterfaceTypes.add(ajInterfaceType);
                TypePattern newParent = parseTypePattern(ajInterfaceType.getName(), struct);
                newParents.add(newParent);
            }
        } else {
            if (methodReturnType.isClass()) {
                reportError(getMethodForMessage(struct) + ": factory methods for a mixin must either return an interface type or specify interfaces in the annotation and return a class", struct);
                return false;
            }
            TypePattern newParent2 = parseTypePattern(methodReturnType.getName(), struct);
            newInterfaceTypes.add(methodReturnType);
            newParents.add(newParent2);
        }
        if (newParents.size() == 0) {
            return false;
        }
        FormalBinding[] bindings = new FormalBinding[0];
        IScope binding = new BindingScope(struct.enclosingType, struct.context, bindings);
        DeclareParents dp = new DeclareParentsMixin(targetTypePattern, newParents);
        dp.resolve(binding);
        TypePattern targetTypePattern2 = dp.getChild();
        dp.setLocation(struct.context, -1, -1);
        struct.ajAttributes.add(new AjAttribute.DeclareAttribute(dp));
        boolean hasAtLeastOneMethod = false;
        for (ResolvedType typeForDelegation : newInterfaceTypes) {
            ResolvedMember[] methods = (ResolvedMember[]) typeForDelegation.getMethodsWithoutIterator(true, false, false).toArray(new ResolvedMember[0]);
            for (ResolvedMember method : methods) {
                if (method.isAbstract()) {
                    hasAtLeastOneMethod = true;
                    if (method.hasBackingGenericMember()) {
                        method = method.getBackingGenericMember();
                    }
                    MethodDelegateTypeMunger mdtm = new MethodDelegateTypeMunger(method, struct.enclosingType, "", targetTypePattern2, struct.method.getName(), struct.method.getSignature());
                    mdtm.setFieldType(methodReturnType);
                    mdtm.setSourceLocation(struct.enclosingType.getSourceLocation());
                    struct.ajAttributes.add(new AjAttribute.TypeMunger(mdtm));
                }
            }
        }
        if (hasAtLeastOneMethod) {
            ResolvedMember fieldHost = AjcMemberMaker.itdAtDeclareParentsField(null, methodReturnType, struct.enclosingType);
            struct.ajAttributes.add(new AjAttribute.TypeMunger(new MethodDelegateTypeMunger.FieldHostTypeMunger(fieldHost, struct.enclosingType, targetTypePattern2)));
            return true;
        }
        return true;
    }

    private static boolean handleBeforeAnnotation(RuntimeAnnos runtimeAnnotations, AjAttributeMethodStruct struct, ResolvedPointcutDefinition preResolvedPointcut) throws AbortException {
        NameValuePair beforeAdvice;
        Pointcut pc;
        AnnotationGen before = getAnnotation(runtimeAnnotations, AjcMemberMaker.BEFORE_ANNOTATION);
        if (before != null && (beforeAdvice = getAnnotationElement(before, "value")) != null) {
            String argumentNames = getArgNamesValue(before);
            if (argumentNames != null) {
                struct.unparsedArgumentNames = argumentNames;
            }
            FormalBinding[] formalBindingArr = new FormalBinding[0];
            try {
                FormalBinding[] bindings = extractBindings(struct);
                IScope binding = new BindingScope(struct.enclosingType, struct.context, bindings);
                int extraArgument = extractExtraArgument(struct.method);
                if (preResolvedPointcut != null) {
                    pc = preResolvedPointcut.getPointcut();
                } else {
                    Pointcut pc2 = parsePointcut(beforeAdvice.getValue().stringifyValue(), struct, false);
                    if (pc2 == null) {
                        return false;
                    }
                    pc = pc2.resolve(binding);
                }
                setIgnoreUnboundBindingNames(pc, bindings);
                ISourceLocation sl = struct.context.makeSourceLocation(struct.bMethod.getDeclarationLineNumber(), struct.bMethod.getDeclarationOffset());
                struct.ajAttributes.add(new AjAttribute.AdviceAttribute(AdviceKind.Before, pc, extraArgument, sl.getOffset(), sl.getOffset() + 1, struct.context));
                return true;
            } catch (UnreadableDebugInfoException e) {
                return false;
            }
        }
        return false;
    }

    private static boolean handleAfterAnnotation(RuntimeAnnos runtimeAnnotations, AjAttributeMethodStruct struct, ResolvedPointcutDefinition preResolvedPointcut) throws AbortException {
        NameValuePair afterAdvice;
        Pointcut pc;
        AnnotationGen after = getAnnotation(runtimeAnnotations, AjcMemberMaker.AFTER_ANNOTATION);
        if (after != null && (afterAdvice = getAnnotationElement(after, "value")) != null) {
            FormalBinding[] formalBindingArr = new FormalBinding[0];
            String argumentNames = getArgNamesValue(after);
            if (argumentNames != null) {
                struct.unparsedArgumentNames = argumentNames;
            }
            try {
                FormalBinding[] bindings = extractBindings(struct);
                IScope binding = new BindingScope(struct.enclosingType, struct.context, bindings);
                int extraArgument = extractExtraArgument(struct.method);
                if (preResolvedPointcut != null) {
                    pc = preResolvedPointcut.getPointcut();
                } else {
                    pc = parsePointcut(afterAdvice.getValue().stringifyValue(), struct, false);
                    if (pc == null) {
                        return false;
                    }
                    pc.resolve(binding);
                }
                setIgnoreUnboundBindingNames(pc, bindings);
                ISourceLocation sl = struct.context.makeSourceLocation(struct.bMethod.getDeclarationLineNumber(), struct.bMethod.getDeclarationOffset());
                struct.ajAttributes.add(new AjAttribute.AdviceAttribute(AdviceKind.After, pc, extraArgument, sl.getOffset(), sl.getOffset() + 1, struct.context));
                return true;
            } catch (UnreadableDebugInfoException e) {
                return false;
            }
        }
        return false;
    }

    private static boolean handleAfterReturningAnnotation(RuntimeAnnos runtimeAnnotations, AjAttributeMethodStruct struct, ResolvedPointcutDefinition preResolvedPointcut, BcelMethod owningMethod) throws AbortException, ReturningFormalNotDeclaredInAdviceSignatureException {
        String pointcut;
        Pointcut pc;
        AnnotationGen after = getAnnotation(runtimeAnnotations, AjcMemberMaker.AFTERRETURNING_ANNOTATION);
        if (after != null) {
            NameValuePair annValue = getAnnotationElement(after, "value");
            NameValuePair annPointcut = getAnnotationElement(after, POINTCUT);
            NameValuePair annReturned = getAnnotationElement(after, RETURNING);
            String returned = null;
            if ((annValue != null && annPointcut != null) || (annValue == null && annPointcut == null)) {
                reportError("@AfterReturning: either 'value' or 'poincut' must be provided, not both", struct);
                return false;
            }
            if (annValue != null) {
                pointcut = annValue.getValue().stringifyValue();
            } else {
                pointcut = annPointcut.getValue().stringifyValue();
            }
            if (isNullOrEmpty(pointcut)) {
                reportError("@AfterReturning: either 'value' or 'poincut' must be provided, not both", struct);
                return false;
            }
            if (annReturned != null) {
                returned = annReturned.getValue().stringifyValue();
                if (isNullOrEmpty(returned)) {
                    returned = null;
                } else {
                    String[] pNames = owningMethod.getParameterNames();
                    if (pNames == null || pNames.length == 0 || !Arrays.asList(pNames).contains(returned)) {
                        throw new ReturningFormalNotDeclaredInAdviceSignatureException(returned);
                    }
                }
            }
            String argumentNames = getArgNamesValue(after);
            if (argumentNames != null) {
                struct.unparsedArgumentNames = argumentNames;
            }
            FormalBinding[] formalBindingArr = new FormalBinding[0];
            try {
                FormalBinding[] bindings = returned == null ? extractBindings(struct) : extractBindings(struct, returned);
                IScope binding = new BindingScope(struct.enclosingType, struct.context, bindings);
                int extraArgument = extractExtraArgument(struct.method);
                if (returned != null) {
                    extraArgument |= 1;
                }
                if (preResolvedPointcut != null) {
                    pc = preResolvedPointcut.getPointcut();
                } else {
                    pc = parsePointcut(pointcut, struct, false);
                    if (pc == null) {
                        return false;
                    }
                    pc.resolve(binding);
                }
                setIgnoreUnboundBindingNames(pc, bindings);
                ISourceLocation sl = struct.context.makeSourceLocation(struct.bMethod.getDeclarationLineNumber(), struct.bMethod.getDeclarationOffset());
                struct.ajAttributes.add(new AjAttribute.AdviceAttribute(AdviceKind.AfterReturning, pc, extraArgument, sl.getOffset(), sl.getOffset() + 1, struct.context));
                return true;
            } catch (UnreadableDebugInfoException e) {
                return false;
            }
        }
        return false;
    }

    private static boolean handleAfterThrowingAnnotation(RuntimeAnnos runtimeAnnotations, AjAttributeMethodStruct struct, ResolvedPointcutDefinition preResolvedPointcut, BcelMethod owningMethod) throws AbortException, ThrownFormalNotDeclaredInAdviceSignatureException {
        String pointcut;
        Pointcut pc;
        AnnotationGen after = getAnnotation(runtimeAnnotations, AjcMemberMaker.AFTERTHROWING_ANNOTATION);
        if (after != null) {
            NameValuePair annValue = getAnnotationElement(after, "value");
            NameValuePair annPointcut = getAnnotationElement(after, POINTCUT);
            NameValuePair annThrown = getAnnotationElement(after, THROWING);
            String thrownFormal = null;
            if ((annValue != null && annPointcut != null) || (annValue == null && annPointcut == null)) {
                reportError("@AfterThrowing: either 'value' or 'poincut' must be provided, not both", struct);
                return false;
            }
            if (annValue != null) {
                pointcut = annValue.getValue().stringifyValue();
            } else {
                pointcut = annPointcut.getValue().stringifyValue();
            }
            if (isNullOrEmpty(pointcut)) {
                reportError("@AfterThrowing: either 'value' or 'poincut' must be provided, not both", struct);
                return false;
            }
            if (annThrown != null) {
                thrownFormal = annThrown.getValue().stringifyValue();
                if (isNullOrEmpty(thrownFormal)) {
                    thrownFormal = null;
                } else {
                    String[] pNames = owningMethod.getParameterNames();
                    if (pNames == null || pNames.length == 0 || !Arrays.asList(pNames).contains(thrownFormal)) {
                        throw new ThrownFormalNotDeclaredInAdviceSignatureException(thrownFormal);
                    }
                }
            }
            String argumentNames = getArgNamesValue(after);
            if (argumentNames != null) {
                struct.unparsedArgumentNames = argumentNames;
            }
            FormalBinding[] formalBindingArr = new FormalBinding[0];
            try {
                FormalBinding[] bindings = thrownFormal == null ? extractBindings(struct) : extractBindings(struct, thrownFormal);
                IScope binding = new BindingScope(struct.enclosingType, struct.context, bindings);
                int extraArgument = extractExtraArgument(struct.method);
                if (thrownFormal != null) {
                    extraArgument |= 1;
                }
                if (preResolvedPointcut != null) {
                    pc = preResolvedPointcut.getPointcut();
                } else {
                    pc = parsePointcut(pointcut, struct, false);
                    if (pc == null) {
                        return false;
                    }
                    pc.resolve(binding);
                }
                setIgnoreUnboundBindingNames(pc, bindings);
                ISourceLocation sl = struct.context.makeSourceLocation(struct.bMethod.getDeclarationLineNumber(), struct.bMethod.getDeclarationOffset());
                struct.ajAttributes.add(new AjAttribute.AdviceAttribute(AdviceKind.AfterThrowing, pc, extraArgument, sl.getOffset(), sl.getOffset() + 1, struct.context));
                return true;
            } catch (UnreadableDebugInfoException e) {
                return false;
            }
        }
        return false;
    }

    private static boolean handleAroundAnnotation(RuntimeAnnos runtimeAnnotations, AjAttributeMethodStruct struct, ResolvedPointcutDefinition preResolvedPointcut) throws AbortException {
        NameValuePair aroundAdvice;
        Pointcut pc;
        AnnotationGen around = getAnnotation(runtimeAnnotations, AjcMemberMaker.AROUND_ANNOTATION);
        if (around != null && (aroundAdvice = getAnnotationElement(around, "value")) != null) {
            String argumentNames = getArgNamesValue(around);
            if (argumentNames != null) {
                struct.unparsedArgumentNames = argumentNames;
            }
            FormalBinding[] formalBindingArr = new FormalBinding[0];
            try {
                FormalBinding[] bindings = extractBindings(struct);
                IScope binding = new BindingScope(struct.enclosingType, struct.context, bindings);
                int extraArgument = extractExtraArgument(struct.method);
                if (preResolvedPointcut != null) {
                    pc = preResolvedPointcut.getPointcut();
                } else {
                    pc = parsePointcut(aroundAdvice.getValue().stringifyValue(), struct, false);
                    if (pc == null) {
                        return false;
                    }
                    pc.resolve(binding);
                }
                setIgnoreUnboundBindingNames(pc, bindings);
                ISourceLocation sl = struct.context.makeSourceLocation(struct.bMethod.getDeclarationLineNumber(), struct.bMethod.getDeclarationOffset());
                struct.ajAttributes.add(new AjAttribute.AdviceAttribute(AdviceKind.Around, pc, extraArgument, sl.getOffset(), sl.getOffset() + 1, struct.context));
                return true;
            } catch (UnreadableDebugInfoException e) {
                return false;
            }
        }
        return false;
    }

    private static boolean handlePointcutAnnotation(RuntimeAnnos runtimeAnnotations, AjAttributeMethodStruct struct) throws AbortException {
        IScope binding;
        AnnotationGen pointcut = getAnnotation(runtimeAnnotations, AjcMemberMaker.POINTCUT_ANNOTATION);
        if (pointcut == null) {
            return false;
        }
        NameValuePair pointcutExpr = getAnnotationElement(pointcut, "value");
        if (!Type.VOID.equals(struct.method.getReturnType()) && (!Type.BOOLEAN.equals(struct.method.getReturnType()) || !struct.method.isStatic() || !struct.method.isPublic())) {
            reportWarning("Found @Pointcut on a method not returning 'void' or not 'public static boolean'", struct);
        }
        if (struct.method.getExceptionTable() != null) {
            reportWarning("Found @Pointcut on a method throwing exception", struct);
        }
        String argumentNames = getArgNamesValue(pointcut);
        if (argumentNames != null) {
            struct.unparsedArgumentNames = argumentNames;
        }
        try {
            if (struct.method.isAbstract()) {
                binding = null;
            } else {
                binding = new BindingScope(struct.enclosingType, struct.context, extractBindings(struct));
            }
            UnresolvedType[] argumentTypes = new UnresolvedType[struct.method.getArgumentTypes().length];
            for (int i = 0; i < argumentTypes.length; i++) {
                argumentTypes[i] = UnresolvedType.forSignature(struct.method.getArgumentTypes()[i].getSignature());
            }
            Pointcut pc = null;
            if (struct.method.isAbstract()) {
                if ((pointcutExpr == null || !isNullOrEmpty(pointcutExpr.getValue().stringifyValue())) && pointcutExpr != null) {
                    reportError("Found defined @Pointcut on an abstract method", struct);
                    return false;
                }
            } else if (pointcutExpr != null && !isNullOrEmpty(pointcutExpr.getValue().stringifyValue())) {
                pc = parsePointcut(pointcutExpr.getValue().stringifyValue(), struct, true);
                if (pc == null) {
                    return false;
                }
                pc.setLocation(struct.context, -1, -1);
            }
            struct.ajAttributes.add(new AjAttribute.PointcutDeclarationAttribute(new LazyResolvedPointcutDefinition(struct.enclosingType, struct.method.getModifiers(), struct.method.getName(), argumentTypes, UnresolvedType.forSignature(struct.method.getReturnType().getSignature()), pc, binding)));
            return true;
        } catch (UnreadableDebugInfoException e) {
            return false;
        }
    }

    private static boolean handleDeclareErrorOrWarningAnnotation(AsmManager model, RuntimeAnnos runtimeAnnotations, AjAttributeFieldStruct struct) throws AbortException {
        NameValuePair declareWarning;
        NameValuePair declareError;
        AnnotationGen error = getAnnotation(runtimeAnnotations, AjcMemberMaker.DECLAREERROR_ANNOTATION);
        boolean hasError = false;
        if (error != null && (declareError = getAnnotationElement(error, "value")) != null) {
            if (!STRING_DESC.equals(struct.field.getSignature()) || struct.field.getConstantValue() == null) {
                reportError("@DeclareError used on a non String constant field", struct);
                return false;
            }
            Pointcut pc = parsePointcut(declareError.getValue().stringifyValue(), struct, false);
            if (pc == null) {
                hasError = false;
            } else {
                DeclareErrorOrWarning deow = new DeclareErrorOrWarning(true, pc, struct.field.getConstantValue().toString());
                setDeclareErrorOrWarningLocation(model, deow, struct);
                struct.ajAttributes.add(new AjAttribute.DeclareAttribute(deow));
                hasError = true;
            }
        }
        AnnotationGen warning = getAnnotation(runtimeAnnotations, AjcMemberMaker.DECLAREWARNING_ANNOTATION);
        boolean hasWarning = false;
        if (warning != null && (declareWarning = getAnnotationElement(warning, "value")) != null) {
            if (!STRING_DESC.equals(struct.field.getSignature()) || struct.field.getConstantValue() == null) {
                reportError("@DeclareWarning used on a non String constant field", struct);
                return false;
            }
            Pointcut pc2 = parsePointcut(declareWarning.getValue().stringifyValue(), struct, false);
            if (pc2 == null) {
                hasWarning = false;
            } else {
                DeclareErrorOrWarning deow2 = new DeclareErrorOrWarning(false, pc2, struct.field.getConstantValue().toString());
                setDeclareErrorOrWarningLocation(model, deow2, struct);
                struct.ajAttributes.add(new AjAttribute.DeclareAttribute(deow2));
                return true;
            }
        }
        return hasError || hasWarning;
    }

    private static void setDeclareErrorOrWarningLocation(AsmManager model, DeclareErrorOrWarning deow, AjAttributeFieldStruct struct) {
        IProgramElement ipe;
        IHierarchy top = model == null ? null : model.getHierarchy();
        if (top != null && top.getRoot() != null && (ipe = top.findElementForLabel(top.getRoot(), IProgramElement.Kind.FIELD, struct.field.getName())) != null && ipe.getSourceLocation() != null) {
            ISourceLocation sourceLocation = ipe.getSourceLocation();
            int start = sourceLocation.getOffset();
            int end = start + struct.field.getName().length();
            deow.setLocation(struct.context, start, end);
            return;
        }
        deow.setLocation(struct.context, -1, -1);
    }

    private static String methodToString(Method method) {
        StringBuffer sb = new StringBuffer();
        sb.append(method.getName());
        sb.append(method.getSignature());
        return sb.toString();
    }

    private static FormalBinding[] extractBindings(AjAttributeMethodStruct struct) throws AbortException, UnreadableDebugInfoException {
        Method method = struct.method;
        String[] argumentNames = struct.getArgumentNames();
        if (argumentNames.length != method.getArgumentTypes().length) {
            reportError("Cannot read debug info for @Aspect to handle formal binding in pointcuts (please compile with 'javac -g' or '<javac debug='true'.../>' in Ant)", struct);
            throw new UnreadableDebugInfoException();
        }
        List<FormalBinding> bindings = new ArrayList<>();
        for (int i = 0; i < argumentNames.length; i++) {
            String argumentName = argumentNames[i];
            UnresolvedType argumentType = UnresolvedType.forSignature(method.getArgumentTypes()[i].getSignature());
            if (AjcMemberMaker.TYPEX_JOINPOINT.equals(argumentType) || AjcMemberMaker.TYPEX_PROCEEDINGJOINPOINT.equals(argumentType) || AjcMemberMaker.TYPEX_STATICJOINPOINT.equals(argumentType) || AjcMemberMaker.TYPEX_ENCLOSINGSTATICJOINPOINT.equals(argumentType) || AjcMemberMaker.AROUND_CLOSURE_TYPE.equals(argumentType)) {
                bindings.add(new FormalBinding.ImplicitFormalBinding(argumentType, argumentName, i));
            } else {
                bindings.add(new FormalBinding(argumentType, argumentName, i));
            }
        }
        return (FormalBinding[]) bindings.toArray(new FormalBinding[0]);
    }

    private static FormalBinding[] extractBindings(AjAttributeMethodStruct struct, String excludeFormal) throws AbortException, UnreadableDebugInfoException {
        FormalBinding[] bindings = extractBindings(struct);
        int i = 0;
        while (true) {
            if (i >= bindings.length) {
                break;
            }
            FormalBinding binding = bindings[i];
            if (!binding.getName().equals(excludeFormal)) {
                i++;
            } else {
                bindings[i] = new FormalBinding.ImplicitFormalBinding(binding.getType(), binding.getName(), binding.getIndex());
                break;
            }
        }
        return bindings;
    }

    private static int extractExtraArgument(Method method) {
        Type[] methodArgs = method.getArgumentTypes();
        String[] sigs = new String[methodArgs.length];
        for (int i = 0; i < methodArgs.length; i++) {
            sigs[i] = methodArgs[i].getSignature();
        }
        return extractExtraArgument(sigs);
    }

    public static int extractExtraArgument(String[] argumentSignatures) {
        int extraArgument = 0;
        for (int i = 0; i < argumentSignatures.length; i++) {
            if (AjcMemberMaker.TYPEX_JOINPOINT.getSignature().equals(argumentSignatures[i])) {
                extraArgument |= 2;
            } else if (AjcMemberMaker.TYPEX_PROCEEDINGJOINPOINT.getSignature().equals(argumentSignatures[i])) {
                extraArgument |= 2;
            } else if (AjcMemberMaker.TYPEX_STATICJOINPOINT.getSignature().equals(argumentSignatures[i])) {
                extraArgument |= 4;
            } else if (AjcMemberMaker.TYPEX_ENCLOSINGSTATICJOINPOINT.getSignature().equals(argumentSignatures[i])) {
                extraArgument |= 8;
            }
        }
        return extraArgument;
    }

    private static AnnotationGen getAnnotation(RuntimeAnnos rvs, UnresolvedType annotationType) {
        String annotationTypeName = annotationType.getName();
        for (AnnotationGen rv : rvs.getAnnotations()) {
            if (annotationTypeName.equals(rv.getTypeName())) {
                return rv;
            }
        }
        return null;
    }

    private static NameValuePair getAnnotationElement(AnnotationGen annotation, String elementName) {
        for (NameValuePair element : annotation.getValues()) {
            if (elementName.equals(element.getNameString())) {
                return element;
            }
        }
        return null;
    }

    private static String getArgNamesValue(AnnotationGen anno) {
        List<NameValuePair> elements = anno.getValues();
        for (NameValuePair element : elements) {
            if (ARGNAMES.equals(element.getNameString())) {
                return element.getValue().stringifyValue();
            }
        }
        return null;
    }

    private static String lastbit(String fqname) {
        int i = fqname.lastIndexOf(".");
        if (i == -1) {
            return fqname;
        }
        return fqname.substring(i + 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String[] getMethodArgumentNames(Method method, String argNamesFromAnnotation, AjAttributeMethodStruct methodStruct) throws AbortException {
        LocalVariable localVariable;
        if (method.getArgumentTypes().length == 0) {
            return EMPTY_STRINGS;
        }
        int startAtStackIndex = method.isStatic() ? 0 : 1;
        List<MethodArgument> arguments = new ArrayList<>();
        LocalVariableTable lt = method.getLocalVariableTable();
        if (lt != null) {
            LocalVariable[] lvt = lt.getLocalVariableTable();
            for (LocalVariable localVariable2 : lvt) {
                if (localVariable2 != null) {
                    if (localVariable2.getStartPC() == 0 && localVariable2.getIndex() >= startAtStackIndex) {
                        arguments.add(new MethodArgument(localVariable2.getName(), localVariable2.getIndex()));
                    }
                } else {
                    String typename = methodStruct.enclosingType != null ? methodStruct.enclosingType.getName() : "";
                    System.err.println("AspectJ: 348488 debug: unusual local variable table for method " + typename + "." + method.getName());
                }
            }
            if (arguments.size() == 0 && (localVariable = lvt[0]) != null && localVariable.getStartPC() != 0) {
                for (int j = 0; j < lvt.length && arguments.size() < method.getArgumentTypes().length; j++) {
                    LocalVariable localVariable3 = lvt[j];
                    if (localVariable3.getIndex() >= startAtStackIndex) {
                        arguments.add(new MethodArgument(localVariable3.getName(), localVariable3.getIndex()));
                    }
                }
            }
        } else if (argNamesFromAnnotation != null) {
            StringTokenizer st = new StringTokenizer(argNamesFromAnnotation, " ,");
            List<String> args = new ArrayList<>();
            while (st.hasMoreTokens()) {
                args.add(st.nextToken());
            }
            if (args.size() != method.getArgumentTypes().length) {
                StringBuffer shortString = new StringBuffer().append(lastbit(method.getReturnType().toString())).append(SymbolConstants.SPACE_SYMBOL).append(method.getName());
                if (method.getArgumentTypes().length > 0) {
                    shortString.append("(");
                    for (int i = 0; i < method.getArgumentTypes().length; i++) {
                        shortString.append(lastbit(method.getArgumentTypes()[i].toString()));
                        if (i + 1 < method.getArgumentTypes().length) {
                            shortString.append(",");
                        }
                    }
                    shortString.append(")");
                }
                reportError("argNames annotation value does not specify the right number of argument names for the method '" + shortString.toString() + "'", methodStruct);
                return EMPTY_STRINGS;
            }
            return (String[]) args.toArray(new String[0]);
        }
        if (arguments.size() != method.getArgumentTypes().length) {
            return EMPTY_STRINGS;
        }
        Collections.sort(arguments, new Comparator<MethodArgument>() { // from class: org.aspectj.weaver.bcel.AtAjAttributes.1
            @Override // java.util.Comparator
            public int compare(MethodArgument mo, MethodArgument mo1) {
                if (mo.indexOnStack == mo1.indexOnStack) {
                    return 0;
                }
                if (mo.indexOnStack > mo1.indexOnStack) {
                    return 1;
                }
                return -1;
            }
        });
        String[] argumentNames = new String[arguments.size()];
        int i2 = 0;
        for (MethodArgument methodArgument : arguments) {
            int i3 = i2;
            i2++;
            argumentNames[i3] = methodArgument.name;
        }
        return argumentNames;
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/AtAjAttributes$MethodArgument.class */
    private static class MethodArgument {
        String name;
        int indexOnStack;

        public MethodArgument(String name, int indexOnStack) {
            this.name = name;
            this.indexOnStack = indexOnStack;
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/AtAjAttributes$LazyResolvedPointcutDefinition.class */
    public static class LazyResolvedPointcutDefinition extends ResolvedPointcutDefinition {
        private final Pointcut m_pointcutUnresolved;
        private final IScope m_binding;
        private Pointcut m_lazyPointcut;

        public LazyResolvedPointcutDefinition(UnresolvedType declaringType, int modifiers, String name, UnresolvedType[] parameterTypes, UnresolvedType returnType, Pointcut pointcut, IScope binding) {
            super(declaringType, modifiers, name, parameterTypes, returnType, Pointcut.makeMatchesNothing(Pointcut.RESOLVED));
            this.m_lazyPointcut = null;
            this.m_pointcutUnresolved = pointcut;
            this.m_binding = binding;
        }

        @Override // org.aspectj.weaver.ResolvedPointcutDefinition
        public Pointcut getPointcut() {
            if (this.m_lazyPointcut == null && this.m_pointcutUnresolved == null) {
                this.m_lazyPointcut = Pointcut.makeMatchesNothing(Pointcut.CONCRETE);
            }
            if (this.m_lazyPointcut == null && this.m_pointcutUnresolved != null) {
                this.m_lazyPointcut = this.m_pointcutUnresolved.resolve(this.m_binding);
                this.m_lazyPointcut.copyLocationFrom(this.m_pointcutUnresolved);
            }
            return this.m_lazyPointcut;
        }
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.length() <= 0;
    }

    private static void setIgnoreUnboundBindingNames(Pointcut pointcut, FormalBinding[] bindings) {
        List<String> ignores = new ArrayList<>();
        for (FormalBinding formalBinding : bindings) {
            if (formalBinding instanceof FormalBinding.ImplicitFormalBinding) {
                ignores.add(formalBinding.getName());
            }
        }
        pointcut.m_ignoreUnboundBindingForNames = (String[]) ignores.toArray(new String[ignores.size()]);
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/AtAjAttributes$UnreadableDebugInfoException.class */
    private static class UnreadableDebugInfoException extends Exception {
        private UnreadableDebugInfoException() {
        }
    }

    private static void reportError(String message, AjAttributeStruct location) throws AbortException {
        if (!location.handler.isIgnoring(IMessage.ERROR)) {
            location.handler.handleMessage(new Message(message, location.enclosingType.getSourceLocation(), true));
        }
    }

    private static void reportWarning(String message, AjAttributeStruct location) throws AbortException {
        if (!location.handler.isIgnoring(IMessage.WARNING)) {
            location.handler.handleMessage(new Message(message, location.enclosingType.getSourceLocation(), false));
        }
    }

    private static Pointcut parsePointcut(String pointcutString, AjAttributeStruct struct, boolean allowIf) throws AbortException {
        try {
            PatternParser parser = new PatternParser(pointcutString, struct.context);
            Pointcut pointcut = parser.parsePointcut();
            parser.checkEof();
            pointcut.check(null, struct.enclosingType.getWorld());
            if (!allowIf && pointcutString.indexOf("if()") >= 0 && hasIf(pointcut)) {
                reportError("if() pointcut is not allowed at this pointcut location '" + pointcutString + "'", struct);
                return null;
            }
            pointcut.setLocation(struct.context, -1, -1);
            return pointcut;
        } catch (ParserException e) {
            reportError("Invalid pointcut '" + pointcutString + "': " + e.toString() + (e.getLocation() == null ? "" : " at position " + e.getLocation().getStart()), struct);
            return null;
        }
    }

    private static boolean hasIf(Pointcut pointcut) {
        IfFinder visitor = new IfFinder();
        pointcut.accept(visitor, null);
        return visitor.hasIf;
    }

    private static TypePattern parseTypePattern(String patternString, AjAttributeStruct location) throws AbortException {
        try {
            TypePattern typePattern = new PatternParser(patternString).parseTypePattern();
            typePattern.setLocation(location.context, -1, -1);
            return typePattern;
        } catch (ParserException e) {
            reportError("Invalid type pattern'" + patternString + "' : " + e.getLocation(), location);
            return null;
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/AtAjAttributes$ThrownFormalNotDeclaredInAdviceSignatureException.class */
    static class ThrownFormalNotDeclaredInAdviceSignatureException extends Exception {
        private final String formalName;

        public ThrownFormalNotDeclaredInAdviceSignatureException(String formalName) {
            this.formalName = formalName;
        }

        public String getFormalName() {
            return this.formalName;
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/AtAjAttributes$ReturningFormalNotDeclaredInAdviceSignatureException.class */
    static class ReturningFormalNotDeclaredInAdviceSignatureException extends Exception {
        private final String formalName;

        public ReturningFormalNotDeclaredInAdviceSignatureException(String formalName) {
            this.formalName = formalName;
        }

        public String getFormalName() {
            return this.formalName;
        }
    }
}
